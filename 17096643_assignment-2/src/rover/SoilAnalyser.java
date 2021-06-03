package rover;

public class SoilAnalyser
{
    /**
     * Begins a soil analysis. The soil analysis will complete some time later,
     * and its result can be retrieved by calling pollAnalysis().
     * <p>
     * If startAnalysis() is called while analysis is already underway, it will
     * throw an exception.
     */
    public void startAnalysis()
    {
    }

    /**
     * Retrieves the results of a soil analysis, if they're ready yet. If no new
     * results have been produced, this method returns null.
     */
    public byte[] pollAnalysis()
    {
        byte[] arr = new byte[4];
        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = (byte) i;
        }
        return arr;
    }
}