import simpleIO.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/**
 * FileHandler
 * 
 * @author Samuel Cauvin
 * @version 1.0
 */
public class FileHandler {
    private final int NUMBER_OF_VEHICLES = 25;

    private boolean fileFound;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<Road> roads;
    private ArrayList<Sensor> sensors;
    private ArrayList<String> oldSensorStamps = new ArrayList<String>();
    private ArrayList<String> oldSpeedingTickets = new ArrayList<String>();
    private String currentLine;
    private Random rand = new Random();

    /**
     * Creates instance of FileHandler
     */
    public FileHandler() {
        vehicles = new ArrayList<Vehicle>();
        roads = new ArrayList<Road>();
        sensors = new ArrayList<Sensor>();
    }

    /**
     * Reads all the vehicles from vehicles.txt and returns them as an array list of vehicle objects
     * @return An array list of vehicle objects
     */
    public ArrayList<Vehicle> readAllVehicles() {
        TextReader fileReader = new TextReader("vehicles.txt");
        currentLine = fileReader.readLine();
        while (currentLine != null) {
            String vehicleData[] = currentLine.split(";");
            
            //Makes sure invalid values for charge cannot break the system, resets charge if invalid input detected
            float currentCharge;
            try {
                currentCharge = new Float(vehicleData[2]);
            } catch(Exception e) {
                currentCharge = new Float(0.00);
            }
            
            //Defaults to private car if invalid value set in text file
            if (!vehicleData[1].equals("private car") && !vehicleData[1].equals("commercial")) {
                vehicleData[1] = "private car";
            }
            Vehicle newVehicle = new Vehicle(vehicleData[0],vehicleData[1],currentCharge);
            vehicles.add(newVehicle);
            currentLine = fileReader.readLine();
        }
        fileReader.close();

        return vehicles;
    }

    /**
     * Reads all the roads from segments.txt and returns them as an array list of road objects
     * @return An array list of road objects
     */
    public ArrayList<Road> readAllRoads() {
        TextReader fileReader = new TextReader("segments.txt");
        currentLine = fileReader.readLine();
        while (currentLine != null) {
            String roadData[] = currentLine.split(";");
            int roadLength;
            try {
                roadLength = new Integer(roadData[1]);
            } catch(Exception e) {
                roadLength = 1;
            }
            String roadType = roadData[2];
            Road newRoad = null;
            if (roadType.equals("M")) {
                newRoad = new Road(roadData[0],roadLength,"Motorway",70,60);
            } else if (roadType.equals("D")) {
                newRoad = new Road(roadData[0],roadLength,"Dual Carriageway",60,50);
            } else if (roadType.equals("O")) {
                newRoad = new Road(roadData[0],roadLength,"Other Road",50,50);
            }
            roads.add(newRoad);
            currentLine = fileReader.readLine();
        }
        fileReader.close();

        return roads;
    }

    /**
     * Reads all the sensors from sensors.txt and returns them as an array list of sensor objects
     * @return An array list of sensor objects
     */
    public ArrayList<Sensor> readAllSensors() {
        TextReader fileReader = new TextReader("sensors.txt");
        currentLine = fileReader.readLine();
        while (currentLine != null) {
            String sensorData[] = currentLine.split(";");
            int sensorX;
            int sensorY;
            
            //Ensures sensorX is an int
            try {
                sensorX = new Integer(sensorData[1]);
            } catch(Exception e) {
                sensorX = 0;
            }
            
            //Ensures sensorY is an int
            try {
                sensorY = new Integer(sensorData[2]);
            } catch(Exception e) {
                sensorY = 0;
            }
            Sensor newSensor = new Sensor(sensorData[0],sensorX,sensorY);
            sensors.add(newSensor);
            currentLine = fileReader.readLine();
        }
        fileReader.close();

        return sensors;
    }

    /**
     * Writes all updated vehicles to vehicles.txt
     * @param updatedVehicles An array list from the simulation containing all vehicles
     */
    public void writeAllVehicles(ArrayList<Vehicle> updatedVehicles) {
        TextWriter fileWriter = new TextWriter("vehicles.txt");
        for (Vehicle currentVehicle : vehicles) {
            String currentReg = currentVehicle.getReg();
            String currentType = currentVehicle.getType();
            float currentCharge = currentVehicle.getCharge();
            fileWriter.writeLine(currentReg+";"+currentType+";"+currentCharge);
        }
        fileWriter.close();
    }
    
    /**
     * Writes all updated sensor data to SegmentData.txt
     * @param sensorStamps An array list from the simulation containing all sensor stamps
     */
    public void writeAllSensorStamps(ArrayList<String> sensorStamps) {
        try {
            TextReader fileReader = new TextReader("SegmentData.txt");
            currentLine = fileReader.readLine();
            while (currentLine != null) {
                oldSensorStamps.add(currentLine);
                currentLine = fileReader.readLine();
            }
            fileReader.close();
        } catch(Exception e) {
            System.out.println("Text file too large to read in");
        }
            
        TextWriter fileWriter = new TextWriter("SegmentData.txt");
        for (String currentStamp : oldSensorStamps) {
            fileWriter.writeLine(currentStamp);
        }
        for (String currentStamp : sensorStamps) {
            fileWriter.writeLine(currentStamp);
        }
        fileWriter.close();
    }
    
    /**
     * Writes all updated speeding tickets to SpeedingTickets.txt
     * @param speedingTickets An array list from the simulation containing all speeding tickets
     */
    public void writeAllSpeedingTickets(ArrayList<String> speedingTickets) {
        try {
            TextReader fileReader = new TextReader("SpeedingTickets.txt");
            currentLine = fileReader.readLine();
            while (currentLine != null) {
                oldSpeedingTickets.add(currentLine);
                currentLine = fileReader.readLine();
            }
            fileReader.close();
        } catch(Exception e) {
            System.out.println("Text file too large to read in");
        }
        
        TextWriter fileWriter = new TextWriter("SpeedingTickets.txt");
        for (String currentTicket : oldSpeedingTickets) {
            fileWriter.writeLine(currentTicket);
        }
        for (String currentTicket : speedingTickets) {
            fileWriter.writeLine(currentTicket);
        }
        fileWriter.close();
    }

    /**
     * Method for generating a random letter
     * @return Random letter (Capital)
     */
    private char genLetter() {
        return (char)(rand.nextInt(26)+65);
    }

    /**
     * Method for generating a random number
     * @return Random number (0-9)
     */
    private char genNumber() {
        return (char)(rand.nextInt(10)+48);
    }
    
    /**
     * Generates a new set of vehicles to work with
     * @return ArrayList of all vehicles
     */
    public ArrayList<Vehicle> writeNewVehicles() {
        TextWriter fileWriter = new TextWriter("vehicles.txt");
        int index = 0;
        vehicles.clear();
        while (index < NUMBER_OF_VEHICLES) {
            String genRegistration = ""+genLetter()+genLetter()+genNumber()+genNumber()+" "+genLetter()+genLetter()+genLetter();
            String genType = "";
            if (rand.nextInt(2) == 0) {
                genType = "private car";
            } else {
                genType = "commercial";
            }
            vehicles.add(new Vehicle(genRegistration,genType,new Float("0.00")));
            fileWriter.writeLine(genRegistration+";"+genType+";0.00");
            index++;
        }
        fileWriter.close();
        
        //Clears Segment Data
        fileWriter = new TextWriter("segmentData.txt");
        fileWriter.close();
        
        //Clears Speeding Tickets
        fileWriter = new TextWriter("speedingTickets.txt");
        fileWriter.close();

        return vehicles;
    }
}