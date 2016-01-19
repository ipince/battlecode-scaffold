package team378.messages;


import battlecode.common.RobotController;
import battlecode.common.Signal;

import java.util.ArrayList;
import java.util.List;

public class MessageDispatcher {

    public static List<Message> getMessages(RobotController rc) {
        Signal[] signals = rc.emptySignalQueue();
        List<Message> messages = new ArrayList<>();
        for (Signal signal: signals) {
            if (signal.getMessage() != null) {
                int[] message = signal.getMessage();
                if (message[0] == MessageType.DISTRESS_SIGNAL.ordinal())
                    messages.add(DistressMessage.fromSignal(signal));
                else if (message[0] == MessageType.CLEAR_DISTRESS_SIGNAL.ordinal())
                    messages.add(new ClearDistressMessage());
            }
        }
        return messages;
    }
}

