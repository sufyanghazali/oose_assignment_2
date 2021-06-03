package states;

import rover.EngineSystem;
import rover.Rover;
import rover.SoilAnalyser;

public class Idle implements RoverState
{
    public void drive(Rover context, double distance)
    {
        if (distance >= 0.0)
        {
            EngineSystem engine = context.getEngine();
            engine.startDriving();
            context.setState(new states.Driving());
        } else
        {
            throw new IllegalArgumentException("Invalid 'distance' value.");
        }
    }

    public void analyseSoil(Rover context)
    {
        SoilAnalyser analyser = context.getAnalyser();
        analyser.startAnalysis();
        context.setState(new states.AnalysingSoil());
    }
}