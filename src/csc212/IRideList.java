
package csc212;

public interface IRideList {

    boolean addRide(IRide ride);

    boolean removeRideById(int rideId);

    LinkedList<IRide> getAllAlphabetically();

    LinkedList<IRide> findByPickupLocation(String pickupLocation);

    LinkedList<IRide> findByRiderName(String riderFullName);

    int size();
}