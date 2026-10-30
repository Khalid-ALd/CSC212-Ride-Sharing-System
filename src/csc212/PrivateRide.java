
package csc212;

public class PrivateRide extends Ride implements IPrivateRide {

    private IRider rider;

    public PrivateRide(String pickupLocation, IDateTime pickupTime,
                       IDateTime dropoffTime, String dropoffLocation,
                       IDriver driver, IRider rider) {

        super(pickupLocation, pickupTime, dropoffTime,
              dropoffLocation, driver);

        setRider(rider);
    }

    @Override
    public IRider getRider() {
        return rider;
    }

    @Override
    public void setRider(IRider rider) {
        if (rider == null) {
            throw new IllegalArgumentException(
                "Rider cannot be null"
            );
        }

        this.rider = rider;
    }

    @Override
    public boolean hasRider(int riderId) {
        return rider != null && rider.getId() == riderId;
    }

    @Override
    public String toString() {
        return "Private " + super.toString()
            + " | Rider: "
            + (rider == null ? "None" : rider.getName());
    }
}

