package elskippy.messages;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.Signal;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DistressMessageTest {

    public Signal createSignal(int value) {
        Signal ret = new Signal(null, 0, null, 0, value);
        return ret;
    }

    @Test
    public void testDeserialization() {
        DistressMessage signal = new DistressMessage(new MapLocation(3, 10), Direction.NORTH_EAST);
        DistressMessage reconstructed = DistressMessage.fromSignal(createSignal(signal.getMessageValue()));
        assertEquals(reconstructed, signal);
    }

    @Test
    public void testDeserializationLimits() {
        DistressMessage signal = new DistressMessage(new MapLocation(579, 568), Direction.SOUTH_WEST);
        DistressMessage reconstructed = DistressMessage.fromSignal(createSignal(signal.getMessageValue()));
        assertEquals(reconstructed, signal);

    }

}
