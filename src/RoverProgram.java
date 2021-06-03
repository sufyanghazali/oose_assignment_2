import rover.EarthComm;
import rover.EngineSystem;
import rover.Sensors;
import rover.SoilAnalyser;
import rover.Rover;
import states.Idle;
import states.RoverState;

public class RoverProgram
{
    public static void main(String[] args)
    {
        EarthComm comm = new EarthComm();
        EngineSystem engine = new EngineSystem();
        Sensors sensors = new Sensors();
        SoilAnalyser analyser = new SoilAnalyser();
        RoverState state = new Idle();

        Rover rover = new Rover(comm, engine, sensors, analyser, state);

        rover.rove();
    }
}