package rover;

public class Sensors
{
    /**
     * Performs a temperature reading and returns the result in °C.
     */
    public double readTemperature()
    {
        return (Math.random() * (100.0 - (-100.0))) + -100;
    }

    /**
     * Performs a visibility reading and returns the result in km.
     */
    public double readVisibility()
    {
        return (Math.random() * (20.0 - 0.0)) + 0.0;
    }

    /**
     * Performs a light-level reading, and returns the result in lux (units).
     */
    public double readLightLevel()
    {
        return (Math.random() * (200000.0 - 0.0)) + 0.0;
    }

    /**
     * Takes a photo and returns the binary data making up the image.
     */
    public byte[] takePhoto()
    {
        byte[] arr = new byte[4];
        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = (byte) i;
        }
        return arr;
    }
}