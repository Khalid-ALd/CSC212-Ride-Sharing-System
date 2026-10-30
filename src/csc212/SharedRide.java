
package csc212;

public class SharedRide extends Ride implements ISharedRide {

    private LinkedList<IRider> participants =
            new LinkedList<IRider>();

    public SharedRide(String pickupLocation, IDateTime pickupTime,
                      IDateTime dropoffTime, String dropoffLocation,
                      IDriver driver) {

        super(pickupLocation, pickupTime, dropoffTime,
              dropoffLocation, driver);
    }

    @Override
    public LinkedList<IRider> getParticipants() {
        return participants;
    }

    @Override
    public boolean addParticipant(IRider rider) {
        if (rider == null || hasRider(rider.getId())) {
            return false;
        }

        if (!participants.empty()) {
            participants.findFirst();

            while (!participants.last()) {
                participants.findNext();
            }
        }

        participants.insert(rider);
        return true;
    }

    @Override
    public boolean removeParticipantById(int riderId) {
        if (participants.empty()) {
            return false;
        }

        participants.findFirst();

        while (true) {
            if (participants.retrieve().getId() == riderId) {
                participants.remove();
                return true;
            }

            if (participants.last()) {
                break;
            }

            participants.findNext();
        }

        return false;
    }

    @Override
    public boolean hasRider(int riderId) {
        if (participants.empty()) {
            return false;
        }

        participants.findFirst();

        while (true) {
            if (participants.retrieve().getId() == riderId) {
                return true;
            }

            if (participants.last()) {
                break;
            }

            participants.findNext();
        }

        return false;
    }

    @Override
    public boolean isEmpty() {
        return participants.empty();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(
            "Shared " + super.toString() + " | Riders: "
        );

        if (participants.empty()) {
            result.append("None");
            return result.toString();
        }

        participants.findFirst();

        while (true) {
            result.append(participants.retrieve().getName());

            if (participants.last()) {
                break;
            }

            result.append(", ");
            participants.findNext();
        }

        return result.toString();
    }
}
