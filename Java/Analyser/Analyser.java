import java.util.ArrayList;
import java.util.Random;

/**
 * Write a description of class Analyser here.
 * 
 * @author Sam Cauvin 
 * @version 02-12-10
 */
public class Analyser
{
    private ArrayList<Sample> samples;
    private ArrayList<String> faultySensors;

    /**
     * Constructor for objects of class Analyser
     */
    public Analyser()
    {
        samples = new ArrayList<Sample>();
        faultySensors = new ArrayList<String>();
    }

    /**
     * Adds a sample to the collection, if it is greater than or equal to zero
     * 
     * @param  location The location that the temperature reading was taken from
     * @param value The value of the temperature reading taken
     */
    public void addSample(String location, int value) {
        if (value >= 0) {
            samples.add(new Sample(location, value));
        }
    }
    
    /**
     * Generates numberSamples number of samples and adds them the collection
     * Temperatures are generated in the range of 225 to 325 Kelvin
     * 
     * @param numberSamples The number of samples to be randomly generated, maximum value is 100
     */
    public void generateSamples(int numberSamples) {
        //Correct the number of samples if it's outwith the acceptable range (1-100)
        if (numberSamples <= 0) {
            numberSamples = 1;
        } else if (numberSamples > 100) {
            numberSamples = 100;
        }
        
        int index = 0;
        int lastIndex = samples.size() + 1;
        while (index < numberSamples) {
            Random randomGen = new Random(); //Reference from the Java API. Creates a random number generator
            samples.add(new Sample("Location "+lastIndex, (randomGen.nextInt(100)+225))); //Creates a sample based on last index used and a random number between 225 and 325
            index++;
            lastIndex++;
        }
    }
    
    /*
     * Lists all samples in the terminal window
     */
    public void listSamples() {
        for (int i = 0; i < samples.size(); i++) {
            System.out.println(samples.get(i).toString());
        }
    }
    
    /*
     * Displays the highest temperature recorded
     */
    public void highestTemp() {
        int highestTemp = 0;
        for (int i = 0; i < samples.size(); i++) {
            int sampleTemp = samples.get(i).getTemperature();
            if (highestTemp < sampleTemp) {
                highestTemp = sampleTemp;
            }
        }
        System.out.println("Highest temperature recorded was "+highestTemp+" Kelvin. Location(s) at this temperature:");
        for (int i = 0; i < samples.size(); i++) {
            if (samples.get(i).getTemperature() == highestTemp) {
                System.out.println(samples.get(i).getLocation());
            }
        }
    }
    
    /*
     * Report any sensors that are reading below 250 or above 310 Kelvin as faulty
     */
    public void faultySensors() {
        for (int i = 0; i < samples.size(); i++) {
            int sampleTemp = samples.get(i).getTemperature();
            if (sampleTemp < 250 || sampleTemp > 310) {
                System.out.println("Sensor at "+samples.get(i).getLocation()+" is faulty, reporting "+sampleTemp+" Kelvin");
            }
        }
    }
    
    /*
     * Records all faulty sensors into an array
     */
    public void recordFaultySensors() {
        for (int i = 0; i < samples.size(); i++) {
            int sampleTemp = samples.get(i).getTemperature();
            String sampleLocation = samples.get(i).getLocation();
            if (sampleTemp < 250 || sampleTemp > 310) {                
                if (!faultyCheck(sampleLocation)) {
                    faultySensors.add(sampleLocation);
                }
            }
        }  
    }
    
    /*
     * Reports all buildings that are overheating (over 290 Kelvin)
     */
    public void overheatedBuildings() {
        recordFaultySensors(); //Call to write Faulty Sensors to an array to ensure overheat data is accurate
        for (int i = 0; i < samples.size(); i++) {
            int sampleTemp = samples.get(i).getTemperature();
            if (sampleTemp > 290 && !faultyCheck(samples.get(i).getLocation())) {
                System.out.println("Sensor at "+samples.get(i).getLocation()+" is overheating, reporting "+sampleTemp+" Kelvin");
            }
        }    
    }
    
    /*
     * Checks whether a given sensor is faulty
     * @param location The location of the sensor being checked
     * @return Boolean value for whether the given sensor is recorded as faulty
     */
    private boolean faultyCheck(String location) {
        int i = 0;
        boolean found = false;
        while (i < faultySensors.size() && !found) {
            if (faultySensors.get(i).equals(location)) {
                found = true;
            }
            i++;
        }
        return found;
    }
}
