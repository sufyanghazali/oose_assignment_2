public class AnalysingSoil implements RoverState
{
    public void drive(Rover context, double distance)
    {
        // rover cannot start driving
        context.sendMessage("Cannot start driving while performing soil analysis.");
    }

    public void analyseSoil(Rover context)
    {
        context.sendMessage("Soil analysis is already in progress");
    }
}