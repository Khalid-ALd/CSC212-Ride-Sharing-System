package csc212;

public interface IDriverList {

    boolean add(IDriver driver);

    IDriver findById(int driverId);

    LinkedList<IDriver> findByName(String fullName);

    IDriver findByVehiclePlate(String vehiclePlate);

    LinkedList<IDriver> findByVehicleType(VehicleType vehicleType);

    LinkedList<IDriver> getAll();

    boolean removeById(int driverId);

    boolean removeByVehiclePlate(String vehiclePlate);

    int removeByName(String fullName);

    int removeByVehicleType(VehicleType vehicleType);

    int size();
}