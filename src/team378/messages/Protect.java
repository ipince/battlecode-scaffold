package team378.messages;


import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import team378.Attack;
import team378.Move;

public class Protect {

    private final RobotController rc;
    private final Attack attack;

	public Protect(RobotController rc, Attack attack) {
        this.rc = rc;
        this.attack = attack;
    }

    public void anAlly(MapLocation location, Direction escapePath) throws GameActionException {
        if (rc.canSense(location)) {
			attack.aMofo();
		} else {
            rc.setIndicatorString(0, "Must protect " + location);
			Move.decisively(rc, location);
        }
    }
}
