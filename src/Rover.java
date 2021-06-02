import java.util.Base64;

public class Rover
{
    // Rover is the observer... i think

    private EarthComm comm;
    private EngineSystem engine;
    private Sensors sensors;
    private SoilAnalyser analyser;
    private RoverState state;

    /* TODO: What if...
    private String command; // have subject update the rover
     */

    public Rover()
    {
        comm = new EarthComm();
        engine = new EngineSystem();
        sensors = new Sensors();
        analyser = new SoilAnalyser();
        state = new Idle();
    }

    public void setState(RoverState state)
    {
        this.state = state;
    }

    public void sendMessage(String message)
    {
        comm.sendMessage(message);
    }

    public void stop()
    {
        engine.stopDriving();
        comm.sendMessage("D");
    }

    public void update(String command)
    {
        String[] parts = command.split(" ");
        char option = parts[0].charAt(0);

        if (parts.length == 2)
        {
            double value = Double.parseDouble(parts[1]);
            switch (option)
            {
                case 'D':
                    drive(value);
                    break;
                case 'T':
                    turn(value);
                    break;
                default:
                    break;
            }

        } else if (parts.length == 1)
        {
            switch (option)
            {
                case 'P':
                    takePhoto();
                    break;
                case 'E':
                    reportStatus();
                    break;
                case 'S':
                    analyseSoil();
                    break;
                default:
                    break;
            }
        }
    }

    // ============ COMMANDS ==================== //

    public void drive(double distance)
    {
        state.drive(this, distance);
    }

    public void turn(double degree)
    {
        if (degree >= -180.0 && degree <= 180.0)
        {
            engine.turn(degree);
        } else
        {
            throw new IllegalArgumentException("Invalid degree value.");
        }
    }

    public void takePhoto()
    {
        byte[] bytes = sensors.takePhoto();
        String str = Base64.getEncoder().encodeToString(bytes);

        comm.sendMessage("P " + str);
    }

    public void reportStatus()
    {
        double temp, vis, light;

        temp = sensors.readTemperature();
        vis = sensors.readVisibility();
        light = sensors.readLightLevel();

        String report = "E " + temp + " " + vis + " " + light;

        comm.sendMessage(report);
    }

    public void analyseSoil()
    {
        state.analyseSoil(this);
    }

    // ============ GETTERS ====================//

    public EarthComm getComm()
    {
        return comm;
    }

    public EngineSystem getEngine()
    {
        return engine;
    }

    public Sensors getSensors()
    {
        return sensors;
    }

    public SoilAnalyser getAnalyser()
    {
        return analyser;
    }

    public double getDistanceDriven()
    {
        return engine.getDistanceDriven();
    }

    // ============ IN PROGRESS ====================//

    public String pollCommand()
    {
        String command = comm.pollCommand();
        validateCommand(command);
        return command;
    }

    public String pollAnalysis()
    {
        byte[] analysis =  analyser.pollAnalysis();
        String converted = Base64.getEncoder().encodeToString(analysis);
        return converted;
    }

    private void validateCommand(String command)
    {
        String[] options = {"D", "T", "P", "E", "S"};

        String[] parts = command.split(" ");
        if (parts.length > 2)
            throw new IllegalArgumentException("Invalid command format: Too many arguments");
        if (!contains(parts[0], options))
            throw new IllegalArgumentException("Invalid command format: Invalid command");
        if (parts.length == 2)
        {
            double value = Double.parseDouble(parts[1]); // this might throw NumberFormatException
            if (value < 0.0)
                throw new IllegalArgumentException("Negative value given");
        }
    }

    private boolean contains(String str, String[] arr)
    {
        int i = 0;
        boolean found = false;
        while (i < arr.length && !found)
        {
            if (arr[i].equals(str))
                found = true;

            i++;
        }

        return found;
    }
}
