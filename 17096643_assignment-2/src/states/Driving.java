package states;

import rover.EarthComm;
import rover.Rover;

public class Driving implements RoverState
{
    public void drive(Rover context, double distance)
    {
        EarthComm comm = context.getComm();
        comm.sendMessage("! Rover is already driving");
    }

    public void analyseSoil(Rover context)
    {
        EarthComm comm = context.getComm();
        comm.sendMessage("! Cannot perform soil analysis while driving");
    }

}