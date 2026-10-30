package csc212;
public abstract class Person implements IPerson {
    private final int id;
    private String name;
    private String phoneNumber;
    private LinkedList<IRide> rideHistory = new LinkedList<>();

    public Person(int id, String name, String phoneNumber) {
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }

        this.id = id;
        setName(name);
        setPhoneNumber(phoneNumber);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException(
                "Phone number must contain exactly 10 digits"
            );
        }
        this.phoneNumber = phoneNumber;
    }

    public LinkedList<IRide> getRideHistory() {
        return rideHistory;
    }

    public void addRideToHistory(IRide ride) {
        if (ride == null) {
            return;
        }

        if (!rideHistory.empty()) {
            rideHistory.findFirst();

            while (true) {
                if (rideHistory.retrieve().getRideId() == ride.getRideId()) {
                    return;
                }

                if (rideHistory.last()) {
                    break;
                }

                rideHistory.findNext();
            }
        }

        rideHistory.insert(ride);
    }

    public void removeRideFromHistory(int rideId) {
        if (rideHistory.empty()) {
            return;
        }

        rideHistory.findFirst();

        while (true) {
            if (rideHistory.retrieve().getRideId() == rideId) {
                rideHistory.remove();
                return;
            }

            if (rideHistory.last()) {
                return;
            }

            rideHistory.findNext();
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Phone: " + phoneNumber;
    }
}