
/**
 * Write a description of class Sample here.
 * 
 * @author Sam Cauvin
 * @version 02-12-10
 */
public class Sample
{
    private String location;
    private int value;

    /**
     * Constructor for objects of class Sample
     */
    public Sample(String locationSet, int valueSet) {
        location = locationSet;
        value = valueSet;
    }
    
    /*
     * Returns a string detailed the location and temperature of the sample
     * @return Location and temperature of the sample
     */
    public String toString() {
        return "["+location+"] "+value+" Kelvin";
    }
    
    /*
     * Returns the temperature of the sample
     * @return Temperature of the sample
     */
    public int getTemperature() {
        return value;
    }
    
    /*
     * Returns the location of the sample
     * @return Location of the sample
     */
    public String getLocation() {
        return location;
    }
}
