
package csc212;

public interface IRideSharingSystem {

    boolean loadRidersFromCSV(String ridersFilePath);

    boolean loadDriversFromCSV(String driversFilePath);

    boolean loadRidesFromCSV(String ridesFilePath);

    boolean addRider(IRider rider);

    boolean addDriver(IDriver driver);

    IRider searchRiderById(int riderId);

    IRider searchRiderByEmail(String email);

    LinkedList<IRider> searchRidersByName(String fullName);

    LinkedList<IRider> searchRidersByHomeCity(String homeCity);

    LinkedList<IRider> getAllRiders();

    IDriver searchDriverById(int driverId);

    IDriver searchDriverByVehiclePlate(String vehiclePlate);

    LinkedList<IDriver> searchDriversByVehicleType(VehicleType vehicleType);

    LinkedList<IDriver> getAllDrivers();

    boolean removeRider(int riderId);

    boolean removeDriver(int driverId);

    boolean schedulePrivateRide(String pickupLocation, IDateTime pickupTime,
                                IDateTime dropoffTime, String dropoffLocation,
                                int riderId, int driverId);

    boolean scheduleSharedRide(String pickupLocation, IDateTime pickupTime,
                               IDateTime dropoffTime, String dropoffLocation,
                               int[] riderIds, int driverId);

    LinkedList<IRide> searchRidesByPickupLocation(String pickupLocation);

    LinkedList<IRide> searchRidesByRiderName(String riderName);

    LinkedList<IRider> getSharedRideParticipants(String pickupLocation);

    LinkedList<IRide> getAllRidesAlphabetically();
}