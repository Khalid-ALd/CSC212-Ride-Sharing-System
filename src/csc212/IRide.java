package csc212;

public interface IRide extends Comparable<IRide> {

    int getRideId();

    String getPickupLocation();

    void setPickupLocation(String pickupLocation);

    IDateTime getPickupTime();

    IDateTime getDropoffTime();

    String getDropoffLocation();

    void setDropoffLocation(String dropoffLocation);

    IDriver getDriver();

    void setDriver(IDriver driver);

    boolean hasRider(int riderId);

    @Override
    String toString();

    @Override
    int compareTo(IRide other);
}