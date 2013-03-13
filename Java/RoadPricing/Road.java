
/**
 * Road
 * 
 * @author Samuel Cauvin
 * @version 1.0
 */
public class Road {
    private String roadName;
    private int miles;
    private String roadType;
    private int speedLimitPrivate;
    private int speedLimitCommercial;

    /**
     * Constructor for Road
     * @param rRoadName The name of the road
     * @param rMiles The length of the road in miles
     * @param rSpeedLimitPrivate The speed limit of this road for private cars
     * @param rSpeedLimitCommercial The speed limit of this road for commercial vehicles
     */
    public Road(String rRoadName, int rMiles, String rRoadType, int rSpeedLimitPrivate, int rSpeedLimitCommercial) {
        roadName = rRoadName;
        miles = rMiles;
        roadType = rRoadType;
        speedLimitPrivate = rSpeedLimitPrivate;
        speedLimitCommercial = rSpeedLimitCommercial;
    }
    
    /**
     * Returns the name of the road
     * @return Name of road
     */
    public String getName() {
        return roadName;
    }
    
    /**
     * Returns the length of the road
     * @return Road length in miles
     */
    public int getLength() {
        return miles;
    }
    
    /**
     * Returns the type of the road
     * @return Type of road
     */
    public String getType() {
        return roadType;
    }
    
    /**
     * Returns the speed limit relative to the car type
     * @param carType The type of car requesting the speed limit
     * @return Speed Limit of this road (mph)
     */
    public int getSpeedLimit(String carType) {
        if (carType.equals("private car")) {
            return speedLimitPrivate;
        } else if (carType.equals("commercial")) {
            return speedLimitCommercial;
        }
        return -1;
    }
    
    /**
     * Charges a vehicle based on it's details
     * @param currentVehicle The vehicle object that is being charged
     * @param The value of the charge incurred
     */
    public float setCharge(Vehicle currentVehicle) {
        String vehicleType = currentVehicle.getType();
        
        float charge = 0;
        if (vehicleType.equals("private car")) {
            if (roadType.equals("Motorway")) {
                double chargeRate = 0;
                for (int i = 0; i < miles; i++) {
                    if (i <= 5) {
                        chargeRate = 0;
                    } else if (i <= 15) {
                        chargeRate = 0.02;
                    } else {
                        chargeRate = 0.01;
                    }
                    charge += chargeRate;
                }
            } else if (roadType.equals("Dual Carriageway")) {
                charge = (float)(miles * 0.01);
            } else if (roadType.equals("Other Road")) {
                charge = 0;
            }
        } else if (vehicleType.equals("commercial")) {
            if (roadType.equals("Motorway")) {
                charge = (float)(miles * 0.02);
            } else if (roadType.equals("Dual Carriageway")) {
                double chargeRate = 0;
                for (int i = 0; i < miles; i++) {
                    if (i <= 10) {
                        chargeRate = 0.03;
                    } else {
                        chargeRate = 0.02;
                    }
                    charge += chargeRate;
                }
            } else if (roadType.equals("Other Road")) {
                charge = (float)(miles * 0.05);
            }
        }
        
        currentVehicle.setCharge(charge);
        return charge;
    }
}