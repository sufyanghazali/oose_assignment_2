package states;

import rover.Rover;

public interface RoverState
{
    void drive(Rover context, double distance);

    void analyseSoil(Rover context);
}