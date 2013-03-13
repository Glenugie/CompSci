import java.util.ArrayList;
import simpleIO.*;
import java.util.Random;
import java.text.DecimalFormat;

/**
 * Simulation
 * 
 * @author Samuel Cauvin
 * @version 1.0
 */
public class Simulation {
    private FileHandler fileHandler;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Road> roads;
    private ArrayList<Sensor> sensors;
    private ArrayList<String> sensorStamps = new ArrayList<String>();
    private ArrayList<String> speedingTickets = new ArrayList<String>();
    private int step; //Step (in seconds) throught the simulation
    private Random rand = new Random();
    private UserDialog ud = new UserDialog();

    //Statistics
    private int totalVehicles = 0;
    private float totalCharge = 0;

    //Decimal Formats
    private DecimalFormat statDisplay = new DecimalFormat("#,##0");
    private DecimalFormat currencyDisplay = new DecimalFormat("#,##0.00");

    /**
     * Main method for the simulation
     */
    public static void main(String [ ] args) {
        new Simulation();
    }

    /**
     * Constructor for simulation, also handles main operations
     */
    public Simulation() {
        fileHandler = new FileHandler();
        vehicles = fileHandler.readAllVehicles();
        roads = fileHandler.readAllRoads();
        sensors = fileHandler.readAllSensors();

        userInterface();

        runSimulation();        
        outputStats();

        //Update text files
        fileHandler.writeAllVehicles(vehicles);
        fileHandler.writeAllSensorStamps(sensorStamps);
        fileHandler.writeAllSpeedingTickets(speedingTickets);
    }

    /**
     * Controls the User Interface
     */
    public void userInterface() {
        if (ud.getBoolean("Generate new vehicle list? (Also clears segment data and speeding tickets)") == true) {
            vehicles = fileHandler.writeNewVehicles();
        }
    }

    /**
     * Responsible for the main simulation loop
     */
    public void runSimulation() {
        int totalSteps = 86400; //One Day
        for (step = 0; step < totalSteps; step += 1) { //Main simulation loop, runs for 24 hours
            for (Vehicle currentVehicle : vehicles) {
                if (currentVehicle.getActive() == false) { //Only inactive vehicles may enter the network
                    if (rand.nextInt(10) == 0) {      
                        Sensor entryPoint = sensors.get(rand.nextInt(sensors.size()));
                        currentVehicle.setEntryPoint(entryPoint, generateTimestamp());

                        Sensor destination = generateDestination(currentVehicle.getLastSensor());
                        totalVehicles += 1;
                        currentVehicle.enterNetwork(destination);
                    }
                } else {
                    Sensor destination = generateDestination(currentVehicle.getNextSensor());                    
                    if (currentVehicle.requireUpdate() == true) { //Requires the segment data to be updated
                        String currentSegment = currentVehicle.getCurrentSegmentName();
                        for (Road currentRoad : roads) {
                            if (currentRoad.getName().equals(currentSegment)) {
                                totalCharge += currentRoad.setCharge(currentVehicle);
                                double journeyTime = currentVehicle.getTimePerSegment()/3600.0;
                                int roadLength = currentRoad.getLength();
                                int averageSpeed = (int)Math.ceil(roadLength / journeyTime);
                                int speedLimit = currentRoad.getSpeedLimit(currentVehicle.getType());
                                if (averageSpeed > speedLimit) {
                                    String speedingTicket = currentVehicle.getReg()+";";
                                    speedingTicket += currentVehicle.getLastSensorTime()+";"+generateTimestamp()+";";
                                    speedingTicket += currentVehicle.getCurrentSegmentName()+";";
                                    speedingTicket += averageSpeed+";"+currentRoad.getType()+";"+speedLimit;
                                    speedingTickets.add(speedingTicket);
                                }
                            }
                        }
                    }
                    String sensorStamp = currentVehicle.drive(generateTimestamp(),destination);
                    if (sensorStamp != null) {
                        sensorStamps.add(sensorStamp);
                    }
                }
            }
        }
    }

    /**
     * Outputs the statistics at the end of execution
     */
    public void outputStats() {
        String stats = "";

        stats += "Total speeding tickets issued: "+statDisplay.format(speedingTickets.size())+"\n";
        stats += "Total vehicles crossing sensors: "+statDisplay.format(sensorStamps.size())+"\n";
        stats += "Total vehicles passing through the network: "+statDisplay.format(totalVehicles)+"\n";
        stats += "Total charges for the day: £"+currencyDisplay.format(totalCharge)+"\n";

        ud.showTextMessage(stats, 10, 40);
    }

    /**
     * Generates a timestamp in the form hh:mm representing current position in the simulation
     * @return String representation of the time (hh:mm)
     */
    public String generateTimestamp() {
        String hourString = "";
        String minuteString = "";
        int hours = step / 3600;
        int minutes = (step - (3600 * hours)) / 60;

        if (hours <= 9) {
            hourString = "0" + Integer.toString(hours);
        } else {
            hourString = Integer.toString(hours);
        }
        if (minutes <= 9) {
            minuteString = "0" + Integer.toString(minutes);
        } else {
            minuteString = Integer.toString(minutes);
        }

        return hourString + ":" + minuteString;
    }

    /**
     * Generates a viable destination based on the last sensor visited
     * @param lastSensor The name of the last sensor passed
     * @return A sensor object for the vehicle to travel to next
     */
    public Sensor generateDestination(String lastSensor) {
        String destinationName;
        
        //Loops through roads working out which lead from the current sensor
        ArrayList<Road> possibleRoads = new ArrayList<Road>();
        for (Road currentRoad : roads) {
            String roadName = currentRoad.getName();
            if (roadName.indexOf(lastSensor) != -1) {
                possibleRoads.add(currentRoad);
            }
        }

        //Chooses a road at random that can be accessed
        Road chosenRoad = possibleRoads.get(rand.nextInt(possibleRoads.size()));
        
        //If the last sensor comes first, take the second letter, else take the first. This gives the destination sensor's name
        int index = chosenRoad.getName().indexOf(lastSensor);
        if (index == 0) {
            destinationName = chosenRoad.getName().substring(1,2);
        } else {
            destinationName = chosenRoad.getName().substring(0,1);
        }

        //Locate sensor by name and return
        for (Sensor currentSensor : sensors) {
            if (currentSensor.getName().equals(destinationName)) {
                return currentSensor;
            }
        }
        return null;
    }
}