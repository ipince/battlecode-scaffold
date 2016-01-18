package elskippy.messages;


import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import elskippy.Attack;
import elskippy.Move;

public class Protect {

    private final RobotController rc;
    private final Attack attack;
    private final Move move;

    public Protect(RobotController rc, Attack attack, Move move) {
        this.rc = rc;
        this.attack = attack;
        this.move = move;
    }

    public void anAlly(MapLocation location, Direction escapePath) throws GameActionException {
        if (rc.canSense(location))
            attack.aMofo();
        else {
            rc.setIndicatorString(0, "Must protect " + location);
            move.decisively(location);
        }
    }
}
