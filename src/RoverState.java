public interface RoverState
{
    void drive(Rover context, double distance);

    void analyseSoil(Rover context);
}