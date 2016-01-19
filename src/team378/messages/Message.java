package elskippy.messages;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public abstract class Message {
    public abstract MessageType getMessageType();
    public abstract int getMessageValue();

    public void sendMessage(RobotController rc, int radius) throws GameActionException {
        rc.broadcastMessageSignal(getMessageType().ordinal(), getMessageValue(), radius);
    }
}
