import java.text.DecimalFormat;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Vehicle
 * 
 * @author Samuel Cauvin
 * @version 1.0
 */
public class Vehicle {
    private String registration;
    private String type;
    private float charge;
    
    private boolean active;
    private boolean updateCharge;
    
    private int remainingSensors;
    private int timePerSegment;
    private int timeThisSegment;

    private Sensor nextSensor;
    private Sensor lastSensor;
    private String lastSensorTime;
    
    private double x;
    private double y;
    
    private int segmentLength;    
    private int journeyTime;

    private Random rand = new Random();
    private DecimalFormat currencyOutput = new DecimalFormat("#,##0.00");

    /**
     * Constructor for Vehicle
     * @param vRegistration The registration of the vehicle
     * @param vType The type of the vehicle (Private or Commercial)
     * @param vCharge The current charge associated with the vehicle
     */
    public Vehicle(String vRegistration, String vType, float vCharge) {
        registration = vRegistration;
        type = vType;
        charge = vCharge;
        active = false;
        x = 0;
        y = 0;
    }
    
    /**
     * Returns the registration of the vehicle
     * @return The registration of the vehicle
     */
    public String getReg() {
        return registration;
    }

    /**
     * Return the type of the vehicle
     * @return The type of the vehicle
     */
    public String getType() {
        return type;
    }

    /**
     * Return the current charge of the vehicle
     * @return The current charge of the vehicle
     */
    public float getCharge() {
        return charge;
    }

    /**
     * Return the x co-ordinate of the vehicle
     * @return The x co-ordinate of the vehicle
     */
    public double getX() {
        return x;
    }

    /**
     * Return the y co-ordinate of the vehicle
     * @return The y co-ordinate of the vehicle
     */
    public double getY() {
        return y;
    }

    /**
     * Return whether the vehicle is active or not
     * @return Boolean for whether the vehicle is currently in the network
     */
    public boolean getActive() {
        return active;
    }
    
    /**
     * Return whether the vehicle requires it's charge to be updated
     * @return Boolean for whether the vehicle has just crossed a sensor
     */
    public boolean requireUpdate() {
        return updateCharge;
    }
    
    /**
     * Return the last sensor crossed by the vehicle
     * @return The name of the last sensor crossed by the vehicle
     */
    public String getLastSensor() {
        return lastSensor.getName();
    }
    
    /**
     * Return the next sensor crossed by the vehicle
     * @return The name of the next sensor crossed by the vehicle
     */
    public String getNextSensor() {
        return nextSensor.getName();
    }
    
    /**
     * Return the time the last sensor was crossed by the vehicle
     * @return The time of the last sensor that was crossed by the vehicle
     */
    public String getLastSensorTime() {
        return lastSensorTime;
    }
    
    /**
     * Return the the time the vehicle should take per segment
     * @return The time the vehicle takes for each segment
     */
    public int getTimePerSegment() {
        return timePerSegment;
    }
    
    /**
     * Switches whether the vehicle is active or not based on past value
     */
    public void setActive() {
        if (active == false) {
            active = true;
        } else {
            active = false;
        }
    }

    /** 
     * Increments the charge by the provided value
     * @increment float The value to increase the charge by
     */
    public void setCharge(float increment) {
        charge += increment;
        charge = new Float(currencyOutput.format(charge)); //Gets the Float to two decimal places
        updateCharge = false;
    }
    
    /**
     * Sets the entry point for the vehicle, changing co-ordinates and the entry name and time
     * @param entryPoint Sensor object for the entry
     * @param entryTime The time at which the vehicle entered the network
     */
    public void setEntryPoint(Sensor entryPoint, String entryTime) {
        x = entryPoint.getX();
        y = entryPoint.getY();
        lastSensor = entryPoint;
        lastSensorTime = entryTime;
    }

    /**
     * Sets the vehicle to enter the network and sets it to travel to the given destination
     * @param destination The sensor object that the vehicle is travelling towards
     */
    public void enterNetwork(Sensor destination) {
        nextSensor = destination;
        
        journeyTime = (rand.nextInt(7200) + 14400);
        remainingSensors = rand.nextInt(10)+1;

        timePerSegment = journeyTime / remainingSensors;
        segmentLength = (timePerSegment + rand.nextInt(3600));//Use this value as the time taken between each sensor, gives varied speeds
        timeThisSegment = segmentLength;
        
        setActive();
    }

    /**
     * Causes the vehicle to increment counters and travel through the network
     * @param time The current time of the simulation (hh:mm)
     * @param destination The sensor which the vehicle is heading for
     * @return The string representation of the sensor stamp (Details for crossing a sensor)
     */
    public String drive(String time, Sensor destination) {
        if (journeyTime == 0) {
            exitNetwork(time);            
            return null;
        } else {
            segmentLength -= 1;
            journeyTime -= 1;
            
            String sensorStamp = null;
            if (segmentLength == 0) {
                sensorStamp = crossSensor(time, destination);
            }   
            
            //Recalculates Co-ordinates
            double totalXDist = (lastSensor.getX() - nextSensor.getX());
            double totalYDist = (lastSensor.getY() - nextSensor.getY());
            double totalDist = java.lang.Math.sqrt(java.lang.Math.pow(totalXDist,2.0) + java.lang.Math.pow(totalYDist,2.0));
            double distPerStep = totalDist / timeThisSegment;
            
            double xDist = java.lang.Math.abs(totalXDist) * (distPerStep / totalDist);
            double yDist = java.lang.Math.abs(totalYDist) * (distPerStep / totalDist);
            
            if (lastSensor.getX() < nextSensor.getX()) { x += xDist;} else { x -= xDist;}
            if (lastSensor.getY() < nextSensor.getY()) { y += yDist;} else { y -= yDist;}
            
            return sensorStamp;
        }
    }

    /**
     * Allows the vehicle to cross a sensor
     * @param crossTime The time at which the vehicle crossed the sensor (hh:mm)
     * @param destination The new destination for the vehicle
     * @return The stamp of details for which the vehicle crossed the sensor
     */
    public String crossSensor(String crossTime, Sensor destination) {
        String sensorStamp = registration+";"+getCurrentSegmentName()+";"+lastSensorTime+";"+crossTime;

        updateCharge = true;
        segmentLength = (timePerSegment + rand.nextInt(3600));
        timeThisSegment = segmentLength;
        remainingSensors -= 1;
        
        //Update sensors
        lastSensor = nextSensor;
        lastSensorTime = crossTime;
        nextSensor = destination;
        
        x = lastSensor.getX();
        y = lastSensor.getY();
        
        return sensorStamp;
    }

    /**
     * Causes the vehicle to exit the network
     * @param The time at which the vehicle exited the network (hh:mm)
     */
    public void exitNetwork(String exitTime) {
        x = 0;
        y = 0;
        setActive();
    }
    
    /**
     * Sorts the sensor names alphabetically and returns them, giving the segment name
     * @return The current segment name
     */
    public String getCurrentSegmentName() {
        String [] sensorNames = {lastSensor.getName(), nextSensor.getName()};
        Arrays.sort(sensorNames);
        return sensorNames[0]+sensorNames[1];
    }
}