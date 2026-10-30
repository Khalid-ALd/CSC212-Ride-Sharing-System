package csc212;

public class RideList implements IRideList {

    private LinkedList<IRide> rides = new LinkedList<IRide>();
    private int count = 0;

    // Adds a ride in alphabetical order by pickup location
    @Override
    public boolean addRide(IRide ride) {
        if (ride == null || containsRideId(ride.getRideId())) {
            return false;
        }

        if (rides.empty()) {
            rides.insert(ride);
            count++;
            return true;
        }

        rides.findFirst();

        while (true) {
            IRide current = rides.retrieve();

            // Put the new ride before the first later pickup location
            if (ride.compareTo(current) < 0) {
                rides.update(ride);
                rides.insert(current);
                count++;
                return true;
            }

            if (rides.last()) {
                break;
            }

            rides.findNext();
        }

        // Largest pickup location so far, add at the end
        rides.insert(ride);
        count++;
        return true;
    }

    // Checks if a ride with this ID is already in the list
    private boolean containsRideId(int rideId) {
        if (rides.empty()) {
            return false;
        }

        rides.findFirst();

        while (true) {
            if (rides.retrieve().getRideId() == rideId) {
                return true;
            }

            if (rides.last()) {
                break;
            }

            rides.findNext();
        }

        return false;
    }

    // Removes the ride with this ID (true if removed)
    @Override
    public boolean removeRideById(int rideId) {
        if (rides.empty()) {
            return false;
        }

        rides.findFirst();

        while (true) {
            if (rides.retrieve().getRideId() == rideId) {
                rides.remove();
                count--;
                return true;
            }

            if (rides.last()) {
                break;
            }

            rides.findNext();
        }

        return false;
    }

    // Returns all rides sorted by pickup location
    @Override
    public LinkedList<IRide> getAllAlphabetically() {
        LinkedList<IRide> result = new LinkedList<IRide>();

        if (rides.empty()) {
            return result;
        }

        rides.findFirst();

        while (true) {
            result.insert(rides.retrieve());

            if (rides.last()) {
                break;
            }

            rides.findNext();
        }

        return result;
    }

    // Finds all rides with this pickup location
    @Override
    public LinkedList<IRide> findByPickupLocation(String pickupLocation) {
        LinkedList<IRide> result = new LinkedList<IRide>();

        if (pickupLocation == null || rides.empty()) {
            return result;
        }

        rides.findFirst();

        while (true) {
            IRide ride = rides.retrieve();

            if (pickupLocation.equalsIgnoreCase(ride.getPickupLocation())) {
                result.insert(ride);
            }

            if (rides.last()) {
                break;
            }

            rides.findNext();
        }

        return result;
    }

    // Finds all rides that have a rider with this exact full name
    @Override
    public LinkedList<IRide> findByRiderName(String riderFullName) {
        LinkedList<IRide> result = new LinkedList<IRide>();

        if (riderFullName == null || rides.empty()) {
            return result;
        }

        rides.findFirst();

        while (true) {
            IRide ride = rides.retrieve();
            boolean found = false;

            if (ride instanceof IPrivateRide) {
                IRider rider = ((IPrivateRide) ride).getRider();
                found = rider != null
                        && riderFullName.equals(rider.getName());

            } else if (ride instanceof ISharedRide) {
                LinkedList<IRider> people =
                        ((ISharedRide) ride).getParticipants();

                if (!people.empty()) {
                    people.findFirst();

                    while (true) {
                        if (riderFullName.equals(people.retrieve().getName())) {
                            found = true;
                            break;
                        }

                        if (people.last()) {
                            break;
                        }

                        people.findNext();
                    }
                }
            }

            if (found) {
                result.insert(ride);
            }

            if (rides.last()) {
                break;
            }

            rides.findNext();
        }

        return result;
    }

    // Returns the number of rides
    @Override
    public int size() {
        return count;
    }
}