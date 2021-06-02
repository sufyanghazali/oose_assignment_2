

public class Driving implements RoverState
{
    public void drive(Rover context, double distance)
    {
        context.sendMessage("Rover is already driving");
    }

    public void analyseSoil(Rover context)
    {
        context.sendMessage("Cannot perform soil analysis while driving");
    }

}