public class RoverController
{
    private Rover rover;

    public RoverController(Rover rover)
    {
        this.rover = rover;
    }

    public void run()
    {
        double distanceStartedDriving = rover.getDistanceDriven();
        double targetDistance = 0.0;

        while (true)
        {
            try
            {
                // ======= poll everything ======== //

                // poll distance
                double totalDistance = rover.getDistanceDriven();

                // poll analysis
                String soilAnalysis = rover.pollAnalysis();

                // poll command
                String command = rover.pollCommand();


                // ======= perform actions if needed ======== //

                // TODO: solve targetDistance initialization
                if (totalDistance - distanceStartedDriving == targetDistance)
                {
                    rover.stop();
                    rover.sendMessage("D");
                    distanceStartedDriving = totalDistance;
                }


                if (soilAnalysis != null)
                    rover.sendMessage("S " + soilAnalysis);

                if (command != null)
                    rover.update(command);

                // sleep for a few milliseconds
                Thread.sleep(100);
            } catch (InterruptedException e)
            {
                rover.sendMessage("! " + e.getMessage());

            } catch (NumberFormatException e)
            {
                rover.sendMessage("! Invalid command: Failed to parse given value");
            } catch (IllegalArgumentException e)
            {
                rover.sendMessage("! " + e.getMessage());

            } catch (IllegalStateException e)
            {
                rover.sendMessage("! " + e.getMessage());
            }
        }
    }

}