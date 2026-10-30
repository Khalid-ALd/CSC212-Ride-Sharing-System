package csc212;

public class RiderList implements IRiderList {

    private LinkedList<IRider> riders = new LinkedList<>();
    private int count = 0;

    @Override
    public boolean add(IRider rider) {

        if (rider == null || findById(rider.getId()) != null) {
            return false;
        }

        if (riders.empty()) {
            riders.insert(rider);
            count++;
            return true;
        }

        riders.findFirst();

        while (true) {

            IRider current = riders.retrieve();

            if (current.getId() > rider.getId()) {
                riders.update(rider);
                riders.insert(current);
                count++;
                return true;
            }

            if (riders.last()) {
                break;
            }

            riders.findNext();
        }

        riders.insert(rider);
        count++;
        return true;
    }

    @Override
    public IRider findById(int riderId) {

        if (riders.empty()) {
            return null;
        }

        riders.findFirst();

        while (true) {

            IRider rider = riders.retrieve();

            if (rider.getId() == riderId) {
                return rider;
            }

            if (riders.last()) {
                break;
            }

            riders.findNext();
        }

        return null;
    }

    @Override
    public LinkedList<IRider> findByName(String fullName) {

        LinkedList<IRider> result = new LinkedList<>();

        if (fullName == null || riders.empty()) {
            return result;
        }

        riders.findFirst();

        while (true) {

            IRider rider = riders.retrieve();

            if (rider.getName().equals(fullName)) {
                result.insert(rider);
            }

            if (riders.last()) {
                break;
            }

            riders.findNext();
        }

        return result;
    }

    @Override
    public IRider findByEmail(String email) {

        if (email == null || riders.empty()) {
            return null;
        }

        riders.findFirst();

        while (true) {

            IRider rider = riders.retrieve();

            if (rider.getEmail().equalsIgnoreCase(email)) {
                return rider;
            }

            if (riders.last()) {
                break;
            }

            riders.findNext();
        }

        return null;
    }

    @Override
    public LinkedList<IRider> findByHomeCity(String homeCity) {

        LinkedList<IRider> result = new LinkedList<>();

        if (homeCity == null || riders.empty()) {
            return result;
        }

        riders.findFirst();

        while (true) {

            IRider rider = riders.retrieve();

            if (rider.getHomeCity().equalsIgnoreCase(homeCity)) {
                result.insert(rider);
            }

            if (riders.last()) {
                break;
            }

            riders.findNext();
        }

        return result;
    }

    @Override
    public LinkedList<IRider> getAll() {

        LinkedList<IRider> result = new LinkedList<>();

        if (riders.empty()) {
            return result;
        }

        riders.findFirst();

        while (true) {

            result.insert(riders.retrieve());

            if (riders.last()) {
                break;
            }

            riders.findNext();
        }

        return result;
    }

    @Override
    public boolean removeById(int riderId) {

        if (riders.empty()) {
            return false;
        }

        riders.findFirst();

        while (true) {

            if (riders.retrieve().getId() == riderId) {
                riders.remove();
                count--;
                return true;
            }

            if (riders.last()) {
                break;
            }

            riders.findNext();
        }

        return false;
    }

    @Override
    public boolean removeByEmail(String email) {

        if (email == null || riders.empty()) {
            return false;
        }

        riders.findFirst();

        while (true) {

            if (riders.retrieve().getEmail().equalsIgnoreCase(email)) {
                riders.remove();
                count--;
                return true;
            }

            if (riders.last()) {
                break;
            }

            riders.findNext();
        }

        return false;
    }

    @Override
    public int removeByName(String fullName) {

        if (fullName == null || riders.empty()) {
            return 0;
        }

        LinkedList<IRider> remaining = new LinkedList<>();
        int removed = 0;

        riders.findFirst();

        while (true) {

            IRider rider = riders.retrieve();

            if (rider.getName().equals(fullName)) {
                removed++;
            } else {
                remaining.insert(rider);
            }

            if (riders.last()) {
                break;
            }

            riders.findNext();
        }

        riders = remaining;
        count -= removed;

        return removed;
    }

    @Override
    public int removeByHomeCity(String homeCity) {

        if (homeCity == null || riders.empty()) {
            return 0;
        }

        LinkedList<IRider> remaining = new LinkedList<>();
        int removed = 0;

        riders.findFirst();

        while (true) {

            IRider rider = riders.retrieve();

            if (rider.getHomeCity().equalsIgnoreCase(homeCity)) {
                removed++;
            } else {
                remaining.insert(rider);
            }

            if (riders.last()) {
                break;
            }

            riders.findNext();
        }

        riders = remaining;
        count -= removed;

        return removed;
    }

    @Override
    public int size() {
        return count;
    }
}