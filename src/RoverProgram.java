public class RoverProgram
{
    public static void main(String[] args)
    {
        Rover rover = new Rover();
        RoverController rc = new RoverController(rover);

        rc.run();
    }
}