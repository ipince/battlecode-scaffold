package team378.messages;


public class ClearDistressMessage extends Message {
    @Override
    public MessageType getMessageType() {
        return MessageType.CLEAR_DISTRESS_SIGNAL;
    }

    @Override
    public int getMessageValue() {
        return 0;
    }

    @Override
    public String toString() {
        return "ClearDistressMessage{}";
    }
}
