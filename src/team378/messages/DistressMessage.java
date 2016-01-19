package team378.messages;


import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.Signal;

public class DistressMessage extends Message {

    private MapLocation location;
    private Direction escapeRoute;

    public DistressMessage(MapLocation location, Direction escapeRoute) {
        this.location = location;
        this.escapeRoute = escapeRoute;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.DISTRESS_SIGNAL;
    }

    @Override
    public int getMessageValue() {
        return (location.x) + (location.y << 10) + (escapeRoute.ordinal() << 20);
    }

    public MapLocation getLocation() {
        return location;
    }

    public void setLocation(MapLocation location) {
        this.location = location;
    }

    public Direction getEscapeRoute() {
        return escapeRoute;
    }

    public void setEscapeRoute(Direction escapeRoute) {
        this.escapeRoute = escapeRoute;
    }

    public static DistressMessage fromSignal(Signal signal) {
        int value = signal.getMessage()[1];
        int valueA = value & 0x3FF;
        int valueB = (value >> 10) & 0x3FF;
        int valueC = (value >> 20) & 0x3FF;

        return new DistressMessage(
                new MapLocation(valueA, valueB),
                Direction.values()[valueC]);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistressMessage that = (DistressMessage) o;

        if (!location.equals(that.location)) return false;
        return escapeRoute == that.escapeRoute;

    }

    @Override
    public int hashCode() {
        int result = location.hashCode();
        result = 31 * result + escapeRoute.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DistressMessage{" +
                "location=" + location +
                ", escapeRoute=" + escapeRoute +
                '}';
    }
}