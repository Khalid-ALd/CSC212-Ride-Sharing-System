package csc212;

public interface ISharedRide extends IRide {

    LinkedList<IRider> getParticipants();

    boolean addParticipant(IRider rider);

    boolean removeParticipantById(int riderId);

    boolean isEmpty();
}