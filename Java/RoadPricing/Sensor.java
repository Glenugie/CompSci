
/**
 * Sensor
 * 
 * @author Samuel Cauvin
 * @version 1.0
 */
public class Sensor {
    private String name;
    private int x; //X Co-ord
    private int y; //Y Co-ord

    /**
     * Constructor for Sensor
     * @param sName The name of the sensor
     * @param sX The x co-ordinate of the sensor
     * @param sY The y co-ordinate of the sensor
     */
    public Sensor(String sName, int sX, int sY) {
        name = sName;
        x = sX;
        y = sY;
    }

    /**
     * Returns the name of the sensor
     * @return The name of the sensor
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the x co-ordinate of the sensor
     * @return the x co-ordinate of the sensor
     */
    public int getX() {
        return x;
    }
    
    /**
     * Returns the y co-ordinate of the sensor
     * @return the y co-ordinate of the sensor
     */
    public int getY() {
        return y;
    }
}