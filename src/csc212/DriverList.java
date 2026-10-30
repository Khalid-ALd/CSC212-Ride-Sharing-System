package csc212;

public class DriverList implements IDriverList {

    private LinkedList<IDriver> drivers = new LinkedList<>();
    private int count = 0;

    @Override
    public boolean add(IDriver driver) {

        if (driver == null
                || findById(driver.getId()) != null
                || findByVehiclePlate(driver.getVehiclePlate()) != null) {
            return false;
        }

        if (drivers.empty()) {
            drivers.insert(driver);
            count++;
            return true;
        }

        drivers.findFirst();

        while (true) {

            IDriver current = drivers.retrieve();

            if (current.getId() > driver.getId()) {
                drivers.update(driver);
                drivers.insert(current);
                count++;
                return true;
            }

            if (drivers.last()) {
                break;
            }

            drivers.findNext();
        }

        drivers.insert(driver);
        count++;
        return true;
    }

    @Override
    public IDriver findById(int driverId) {

        if (drivers.empty()) {
            return null;
        }

        drivers.findFirst();

        while (true) {

            IDriver driver = drivers.retrieve();

            if (driver.getId() == driverId) {
                return driver;
            }

            if (driver.getId() > driverId || drivers.last()) {
                return null;
            }

            drivers.findNext();
        }
    }

    @Override
    public LinkedList<IDriver> findByName(String fullName) {

        LinkedList<IDriver> result = new LinkedList<>();

        if (fullName == null || drivers.empty()) {
            return result;
        }

        drivers.findFirst();

        while (true) {

            IDriver driver = drivers.retrieve();

            if (driver.getName().equals(fullName)) {
                result.insert(driver);
            }

            if (drivers.last()) {
                break;
            }

            drivers.findNext();
        }

        return result;
    }

    @Override
    public IDriver findByVehiclePlate(String vehiclePlate) {

        if (vehiclePlate == null || drivers.empty()) {
            return null;
        }

        drivers.findFirst();

        while (true) {

            IDriver driver = drivers.retrieve();

            if (driver.getVehiclePlate().equals(vehiclePlate)) {
                return driver;
            }

            if (drivers.last()) {
                return null;
            }

            drivers.findNext();
        }
    }

    @Override
    public LinkedList<IDriver> findByVehicleType(VehicleType vehicleType) {

        LinkedList<IDriver> result = new LinkedList<>();

        if (vehicleType == null || drivers.empty()) {
            return result;
        }

        drivers.findFirst();

        while (true) {

            IDriver driver = drivers.retrieve();

            if (driver.getVehicleType() == vehicleType) {
                result.insert(driver);
            }

            if (drivers.last()) {
                break;
            }

            drivers.findNext();
        }

        return result;
    }

    @Override
    public LinkedList<IDriver> getAll() {

        LinkedList<IDriver> result = new LinkedList<>();

        if (drivers.empty()) {
            return result;
        }

        drivers.findFirst();

        while (true) {

            result.insert(drivers.retrieve());

            if (drivers.last()) {
                break;
            }

            drivers.findNext();
        }

        return result;
    }

    @Override
    public boolean removeById(int driverId) {

        if (drivers.empty()) {
            return false;
        }

        drivers.findFirst();

        while (true) {

            IDriver driver = drivers.retrieve();

            if (driver.getId() == driverId) {
                drivers.remove();
                count--;
                return true;
            }

            if (driver.getId() > driverId || drivers.last()) {
                return false;
            }

            drivers.findNext();
        }
    }

    @Override
    public boolean removeByVehiclePlate(String vehiclePlate) {

        if (vehiclePlate == null || drivers.empty()) {
            return false;
        }

        drivers.findFirst();

        while (true) {

            if (drivers.retrieve().getVehiclePlate().equals(vehiclePlate)) {
                drivers.remove();
                count--;
                return true;
            }

            if (drivers.last()) {
                return false;
            }

            drivers.findNext();
        }
    }

    @Override
    public int removeByName(String fullName) {

        if (fullName == null || drivers.empty()) {
            return 0;
        }

        LinkedList<IDriver> remaining = new LinkedList<>();
        int removed = 0;

        drivers.findFirst();

        while (true) {

            IDriver driver = drivers.retrieve();

            if (driver.getName().equals(fullName)) {
                removed++;
            } else {
                remaining.insert(driver);
            }

            if (drivers.last()) {
                break;
            }

            drivers.findNext();
        }

        drivers = remaining;
        count -= removed;

        return removed;
    }

    @Override
    public int removeByVehicleType(VehicleType vehicleType) {

        if (vehicleType == null || drivers.empty()) {
            return 0;
        }

        LinkedList<IDriver> remaining = new LinkedList<>();
        int removed = 0;

        drivers.findFirst();

        while (true) {

            IDriver driver = drivers.retrieve();

            if (driver.getVehicleType() == vehicleType) {
                removed++;
            } else {
                remaining.insert(driver);
            }

            if (drivers.last()) {
                break;
            }

            drivers.findNext();
        }

        drivers = remaining;
        count -= removed;

        return removed;
    }

    @Override
    public int size() {
        return count;
    }
}