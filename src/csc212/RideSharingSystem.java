package csc212;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RideSharingSystem implements IRideSharingSystem {

    private final RiderList riders = new RiderList();
    private final DriverList drivers = new DriverList();
    private final RideList rides = new RideList();

    // Adds a rider (false if ID or email already exists)
    @Override
    public boolean addRider(IRider rider) {
        return riders.add(rider);
    }

    // Adds a driver (false if ID or plate already exists)
    @Override
    public boolean addDriver(IDriver driver) {
        if (driver == null || driver.getVehiclePlate() == null
                || drivers.findByVehiclePlate(driver.getVehiclePlate()) != null) {
            return false;
        }

        return drivers.add(driver);
    }

    // Searches for a rider by ID
    @Override
    public IRider searchRiderById(int riderId) {
        return riders.findById(riderId);
    }

    // Searches for a rider by email
    @Override
    public IRider searchRiderByEmail(String email) {
        return riders.findByEmail(email);
    }

    // Searches for riders by exact full name
    @Override
    public LinkedList<IRider> searchRidersByName(String fullName) {
        return riders.findByName(fullName);
    }

    // Searches for riders by home city
    @Override
    public LinkedList<IRider> searchRidersByHomeCity(String homeCity) {
        return riders.findByHomeCity(homeCity);
    }

    // Returns all riders
    @Override
    public LinkedList<IRider> getAllRiders() {
        return riders.getAll();
    }

    // Searches for a driver by ID
    @Override
    public IDriver searchDriverById(int driverId) {
        return drivers.findById(driverId);
    }

    // Searches for a driver by vehicle plate
    @Override
    public IDriver searchDriverByVehiclePlate(String vehiclePlate) {
        return drivers.findByVehiclePlate(vehiclePlate);
    }

    // Searches for drivers by vehicle type
    @Override
    public LinkedList<IDriver> searchDriversByVehicleType(
            VehicleType vehicleType) {
        return drivers.findByVehicleType(vehicleType);
    }

    // Returns all drivers
    @Override
    public LinkedList<IDriver> getAllDrivers() {
        return drivers.getAll();
    }

    // Searches for rides by pickup location
    @Override
    public LinkedList<IRide> searchRidesByPickupLocation(
            String pickupLocation) {
        return rides.findByPickupLocation(pickupLocation);
    }

    // Searches for rides by rider name
    @Override
    public LinkedList<IRide> searchRidesByRiderName(String riderName) {
        return rides.findByRiderName(riderName);
    }

    // Returns all rides sorted by pickup location
    @Override
    public LinkedList<IRide> getAllRidesAlphabetically() {
        return rides.getAllAlphabetically();
    }

    // Returns the riders of all shared rides with this pickup location
    @Override
    public LinkedList<IRider> getSharedRideParticipants(
            String pickupLocation) {
        LinkedList<IRider> result = new LinkedList<>();
        LinkedList<IRide> found = rides.findByPickupLocation(pickupLocation);

        if (found.empty()) {
            return result;
        }

        found.findFirst();

        while (true) {
            IRide ride = found.retrieve();

            if (ride instanceof ISharedRide) {
                LinkedList<IRider> people =
                        ((ISharedRide) ride).getParticipants();

                if (!people.empty()) {
                    people.findFirst();

                    while (true) {
                        result.insert(people.retrieve());

                        if (people.last()) {
                            break;
                        }

                        people.findNext();
                    }
                }
            }

            if (found.last()) {
                break;
            }

            found.findNext();
        }

        return result;
    }

    // Checks if the new time range overlaps a ride of this rider or driver
    private boolean hasConflict(
            IDateTime start, IDateTime end,
            int riderId, int driverId) {

        LinkedList<IRide> existing = rides.getAllAlphabetically();

        if (existing.empty()) {
            return false;
        }

        existing.findFirst();

        while (true) {
            IRide ride = existing.retrieve();

            // Two ranges overlap if each one starts before the other ends
            boolean overlaps =
                    start.compareTo(ride.getDropoffTime()) < 0
                    && ride.getPickupTime().compareTo(end) < 0;

            boolean sameDriver =
                    ride.getDriver() != null
                    && ride.getDriver().getId() == driverId;

            if (overlaps && (sameDriver || ride.hasRider(riderId))) {
                return true;
            }

            if (existing.last()) {
                break;
            }

            existing.findNext();
        }

        return false;
    }

    // Checks that locations are not empty and drop-off is after pickup
    private boolean validSchedule(
            String pickup, IDateTime start,
            IDateTime end, String dropoff) {

        return pickup != null && !pickup.trim().isEmpty()
                && dropoff != null && !dropoff.trim().isEmpty()
                && start != null && end != null
                && start.compareTo(end) < 0;
    }

    // Schedules a private ride if the rider and driver exist and are free
    @Override
    public boolean schedulePrivateRide(
            String pickupLocation, IDateTime pickupTime,
            IDateTime dropoffTime, String dropoffLocation,
            int riderId, int driverId) {

        IRider rider = riders.findById(riderId);
        IDriver driver = drivers.findById(driverId);

        if (rider == null || driver == null
                || !validSchedule(pickupLocation, pickupTime,
                        dropoffTime, dropoffLocation)
                || hasConflict(pickupTime, dropoffTime,
                        riderId, driverId)) {
            return false;
        }

        IRide ride = new PrivateRide(pickupLocation, pickupTime,
                dropoffTime, dropoffLocation, driver, rider);

        if (!rides.addRide(ride)) {
            return false;
        }

        addHistory(rider, ride);
        addHistory(driver, ride);

        return true;
    }

    // Schedules a shared ride if all riders and the driver exist and are free
    @Override
    public boolean scheduleSharedRide(
            String pickupLocation, IDateTime pickupTime,
            IDateTime dropoffTime, String dropoffLocation,
            int[] riderIds, int driverId) {

        IDriver driver = drivers.findById(driverId);

        if (driver == null || riderIds == null
                || riderIds.length < 2
                || !validSchedule(pickupLocation, pickupTime,
                        dropoffTime, dropoffLocation)) {
            return false;
        }

        // Check every rider before creating the ride
        for (int i = 0; i < riderIds.length; i++) {
            if (riders.findById(riderIds[i]) == null
                    || hasConflict(pickupTime, dropoffTime,
                            riderIds[i], driverId)) {
                return false;
            }

            // The same rider cannot be listed twice
            for (int j = 0; j < i; j++) {
                if (riderIds[i] == riderIds[j]) {
                    return false;
                }
            }
        }

        SharedRide ride = new SharedRide(pickupLocation, pickupTime,
                dropoffTime, dropoffLocation, driver);

        for (int id : riderIds) {
            if (!ride.addParticipant(riders.findById(id))) {
                return false;
            }
        }

        if (!rides.addRide(ride)) {
            return false;
        }

        for (int id : riderIds) {
            addHistory(riders.findById(id), ride);
        }

        addHistory(driver, ride);

        return true;
    }

    // Adds the ride to the person's ride history
    private void addHistory(IPerson person, IRide ride) {
        if (person instanceof Person) {
            ((Person) person).addRideToHistory(ride);
        }
    }

    // Removes the ride from the person's ride history
    private void removeHistory(IPerson person, int rideId) {
        if (person instanceof Person) {
            ((Person) person).removeRideFromHistory(rideId);
        }
    }

    // Deletes a ride and removes it from everyone's ride history
    private void deleteRide(IRide ride) {
        if (ride instanceof IPrivateRide) {
            IRider rider = ((IPrivateRide) ride).getRider();

            if (rider != null) {
                removeHistory(rider, ride.getRideId());
            }

        } else if (ride instanceof ISharedRide) {
            LinkedList<IRider> people =
                    ((ISharedRide) ride).getParticipants();

            if (!people.empty()) {
                people.findFirst();

                while (true) {
                    removeHistory(people.retrieve(), ride.getRideId());

                    if (people.last()) {
                        break;
                    }

                    people.findNext();
                }
            }
        }

        if (ride.getDriver() != null) {
            removeHistory(ride.getDriver(), ride.getRideId());
        }

        rides.removeRideById(ride.getRideId());
    }

    // Removes a rider and cleans up the rides that involve this rider
    @Override
    public boolean removeRider(int riderId) {
        IRider rider = riders.findById(riderId);

        if (rider == null) {
            return false;
        }

        LinkedList<IRide> all = rides.getAllAlphabetically();

        if (!all.empty()) {
            all.findFirst();

            while (true) {
                IRide ride = all.retrieve();

                if (ride.hasRider(riderId)) {
                    if (ride instanceof IPrivateRide) {
                        // Private ride is deleted
                        deleteRide(ride);

                    } else if (ride instanceof ISharedRide) {
                        // Shared ride: remove only this rider
                        ISharedRide shared = (ISharedRide) ride;

                        shared.removeParticipantById(riderId);
                        removeHistory(rider, ride.getRideId());

                        // Delete the ride if nobody is left
                        if (shared.isEmpty()) {
                            deleteRide(ride);
                        }
                    }
                }

                if (all.last()) {
                    break;
                }

                all.findNext();
            }
        }

        return riders.removeById(riderId);
    }

    // Removes a driver and deletes all rides assigned to this driver
    @Override
    public boolean removeDriver(int driverId) {
        if (drivers.findById(driverId) == null) {
            return false;
        }

        LinkedList<IRide> all = rides.getAllAlphabetically();

        if (!all.empty()) {
            all.findFirst();

            while (true) {
                IRide ride = all.retrieve();

                if (ride.getDriver() != null
                        && ride.getDriver().getId() == driverId) {
                    deleteRide(ride);
                }

                if (all.last()) {
                    break;
                }

                all.findNext();
            }
        }

        return drivers.removeById(driverId);
    }

    // Converts "MM/DD/YYYY HH:MM" to a DateTime
    private IDateTime parseDateTime(String text) {
        String[] parts = text.trim().split("[ /:]+");

        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid date: " + text);
        }

        return new DateTime(
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4]));
    }

    // Loads riders from a CSV file (false if a line is invalid)
    @Override
    public boolean loadRidersFromCSV(String ridersFilePath) {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(ridersFilePath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] f = line.split(",", -1);

                if (f.length != 5
                        || !addRider(new Rider(
                                Integer.parseInt(f[0].trim()),
                                f[1].trim(),
                                f[2].trim(),
                                f[3].trim(),
                                f[4].trim()))) {
                    return false;
                }
            }

            return true;

        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }

    // Loads drivers from a CSV file (false if a line is invalid)
    @Override
    public boolean loadDriversFromCSV(String driversFilePath) {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(driversFilePath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] f = line.split(",", -1);

                if (f.length != 5
                        || !addDriver(new Driver(
                                Integer.parseInt(f[0].trim()),
                                f[1].trim(),
                                f[2].trim(),
                                f[3].trim(),
                                VehicleType.valueOf(f[4].trim())))) {
                    return false;
                }
            }

            return true;

        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }

    // Loads rides from a CSV file (false if a line is invalid or conflicts)
    @Override
    public boolean loadRidesFromCSV(String ridesFilePath) {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(ridesFilePath))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] f = line.split(",", -1);

                if (f.length != 7) {
                    return false;
                }

                IDateTime start = parseDateTime(f[2]);
                IDateTime end = parseDateTime(f[3]);
                int driverId = Integer.parseInt(f[5].trim());

                String[] ids = f[6].trim().split(";");
                int[] riderIds = new int[ids.length];

                for (int i = 0; i < ids.length; i++) {
                    riderIds[i] = Integer.parseInt(ids[i].trim());
                }

                boolean ok;

                if (f[0].trim().equalsIgnoreCase("PRIVATE")
                        && riderIds.length == 1) {
                    ok = schedulePrivateRide(
                            f[1].trim(), start, end, f[4].trim(),
                            riderIds[0], driverId);

                } else if (f[0].trim().equalsIgnoreCase("SHARED")) {
                    ok = scheduleSharedRide(
                            f[1].trim(), start, end, f[4].trim(),
                            riderIds, driverId);

                } else {
                    return false;
                }

                if (!ok) {
                    return false;
                }
            }

            return true;

        } catch (IOException | IllegalArgumentException e) {
            return false;
        }
    }

    // Removes a ride by its ID (extra method, not in the interface)
    public boolean removeRide(int rideId) {
        LinkedList<IRide> all = rides.getAllAlphabetically();

        if (all.empty()) {
            return false;
        }

        all.findFirst();

        while (true) {
            IRide ride = all.retrieve();

            if (ride.getRideId() == rideId) {
                deleteRide(ride);
                return true;
            }

            if (all.last()) {
                break;
            }

            all.findNext();
        }

        return false;
    }

    // Returns the number of riders (extra method, not in the interface)
    public int getRiderCount() {
        return riders.size();
    }

    // Returns the number of drivers (extra method, not in the interface)
    public int getDriverCount() {
        return drivers.size();
    }

    // Returns the number of rides (extra method, not in the interface)
    public int getRideCount() {
        return rides.size();
    }
}