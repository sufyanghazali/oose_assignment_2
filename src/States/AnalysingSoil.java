package states;

import rover.EarthComm;
import rover.Rover;

public class AnalysingSoil implements RoverState
{
    public void drive(Rover context, double distance)
    {
        EarthComm comm = context.getComm();
        comm.sendMessage("Cannot start driving while performing soil analysis.");
    }

    public void analyseSoil(Rover context)
    {
        EarthComm comm = context.getComm();
        comm.sendMessage("Soil analysis is already in progress");
    }
}