package rover;

import states.Idle;
import states.RoverState;

import java.util.Base64;


public class Rover
{
    private EarthComm comm;
    private EngineSystem engine;
    private Sensors sensors;
    private SoilAnalyser analyser;
    private RoverState state;

    private double targetDistance;
    private double currentDistance;

    public Rover(EarthComm comm, EngineSystem engine, Sensors sensors, SoilAnalyser analyser, RoverState state)
    {
        this.comm = comm;
        this.engine = engine;
        this.sensors = sensors;
        this.analyser = analyser;
        this.state = state;

        targetDistance = 0.0;
        currentDistance = 0.0;
    }

    public void setState(RoverState state)
    {
        this.state = state;
    }

    public void rove()
    {
        while (true)
        {
            try
            {
                // ======= poll everything ======== //
                double totalDistance = engine.getDistanceDriven(); // poll distance

                byte[] soilAnalysis = analyser.pollAnalysis(); // poll analysis

                String command = comm.pollCommand(); // poll command

                double vis = sensors.readVisibility();


                // ======= perform actions if needed ======== //
                if (totalDistance - currentDistance == targetDistance)
                {
                    engine.stopDriving();
                    comm.sendMessage("D");
                    state = new Idle();
                    currentDistance = totalDistance;
                }

                if (soilAnalysis != null)
                    comm.sendMessage("S " + Base64.getEncoder().encodeToString(soilAnalysis));

                if (vis < 4.0)
                    comm.sendMessage(getEnvironmentalStatus());

                if (command != null && validateCommand(command))
                    run(command);

                // ======= sleep for a few milliseconds ======== //
                Thread.sleep(1000);


            } catch (InterruptedException | IllegalArgumentException | IllegalStateException e)
            {
                System.out.println(e.getMessage());
                comm.sendMessage("! " + e.getMessage());
            }
        }
    }

    public void run(String command)
    {
        String[] parts = command.split(" ");
        char option = parts[0].charAt(0);

        if (parts.length == 2)
        {
            double value = Double.parseDouble(parts[1]);
            switch (option)
            {
                case 'D':
                    targetDistance = value;
                    drive(targetDistance);
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
                    getEnvironmentalStatus();
                    break;
                case 'S':
                    analyseSoil();
                    break;
                default:
                    break;
            }
        }
    }

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

    public String takePhoto()
    {
        byte[] bytes = sensors.takePhoto();
        String str = Base64.getEncoder().encodeToString(bytes);

        return "P " + str;
    }

    public String getEnvironmentalStatus()
    {
        double temp, vis, light;

        temp = sensors.readTemperature();
        vis = sensors.readVisibility();
        light = sensors.readLightLevel();

        String report = temp + " " + vis + " " + light;

        return "E " + report;
    }

    public void analyseSoil()
    {
        state.analyseSoil(this);
    }

    public EngineSystem getEngine()
    {
        return engine;
    }

    public SoilAnalyser getAnalyser()
    {
        return analyser;
    }

    public EarthComm getComm()
    {
        return comm;
    }

    private boolean validateCommand(String command)
    {
        boolean valid = false;
        String[] options = {"D", "T", "P", "E", "S"};
        String[] parts = command.split(" ");

        if (parts.length < 3)
        {
            if (contains(parts[0], options))
            {
                if (parts.length == 2)
                {
                    double value = Double.parseDouble(parts[1]); // this might throw NumberFormatException
                    if (value >= 0.0)
                    {
                        valid = true;
                    } else
                    {
                        throw new IllegalArgumentException("Negative value given");
                    }
                }
            } else
            {
                throw new IllegalArgumentException("Invalid command format: Invalid command option");
            }
        } else
        {
            throw new IllegalArgumentException("Invalid command format: Too many arguments");
        }

        return valid;
    }

    private boolean contains(String str, String[] arr)
    {
        int i = 0;
        boolean found = false;
        while (i < arr.length && !found)
        {
            if (arr[i].equals(str))
            {
                found = true;
            }
            i++;
        }

        return found;
    }
}