
package csc212;

public abstract class Ride implements IRide, Comparable<IRide> {

    private static int nextId = 1;

    private final int rideId;
    private String pickupLocation;
    private String dropoffLocation;
    private final IDateTime pickupTime;
    private final IDateTime dropoffTime;
    private IDriver driver;

    public Ride(String pickupLocation, IDateTime pickupTime,
                IDateTime dropoffTime, String dropoffLocation,
                IDriver driver) {

        if (pickupLocation == null || pickupLocation.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Pickup location cannot be empty"
            );
        }

        if (dropoffLocation == null || dropoffLocation.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Drop-off location cannot be empty"
            );
        }

        if (pickupTime == null || dropoffTime == null
                || pickupTime.compareTo(dropoffTime) >= 0) {
            throw new IllegalArgumentException(
                "Drop-off must be after pickup"
            );
        }

        this.rideId = nextId++;
        this.pickupLocation = pickupLocation.trim();
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
        this.dropoffLocation = dropoffLocation.trim();
        this.driver = driver;
    }

    @Override
    public int getRideId() {
        return rideId;
    }

    @Override
    public String getPickupLocation() {
        return pickupLocation;
    }

    @Override
    public void setPickupLocation(String pickupLocation) {
        if (pickupLocation == null || pickupLocation.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Pickup location cannot be empty"
            );
        }

        this.pickupLocation = pickupLocation.trim();
    }

    @Override
    public IDateTime getPickupTime() {
        return pickupTime;
    }

    @Override
    public IDateTime getDropoffTime() {
        return dropoffTime;
    }

    @Override
    public String getDropoffLocation() {
        return dropoffLocation;
    }

    @Override
    public void setDropoffLocation(String dropoffLocation) {
        if (dropoffLocation == null || dropoffLocation.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Drop-off location cannot be empty"
            );
        }

        this.dropoffLocation = dropoffLocation.trim();
    }

    @Override
    public IDriver getDriver() {
        return driver;
    }

    @Override
    public void setDriver(IDriver driver) {
        this.driver = driver;
    }

    @Override
    public int compareTo(IRide other) {
        if (other == null) {
            throw new NullPointerException("Ride cannot be null");
        }

        return pickupLocation.compareToIgnoreCase(
            other.getPickupLocation()
        );
    }

    @Override
    public String toString() {
        return "Ride " + rideId + ": " + pickupLocation + " -> "
            + dropoffLocation + " | " + pickupTime.format()
            + " - " + dropoffTime.format() + " | Driver: "
            + (driver == null ? "None" : driver.getName());
    }
}
