package team378;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Move {

	private final RobotController rc;

	public Move(RobotController rc) {
		this.rc = rc;
	}

	/**
	 * Moves in the direction of the target. If there's rubble preventing motion
	 * in that direction, the rubble is cleared.
	 */
	public void decisively(MapLocation target) throws GameActionException {
		decisively(rc.getLocation().directionTo(target));
	}

	public void decisively(Direction direction) throws GameActionException {
		if (rc.senseRubble(rc.getLocation().add(direction)) >= GameConstants.RUBBLE_OBSTRUCTION_THRESH) {
			rc.clearRubble(direction);
		} else if (rc.canMove(direction)) {
			rc.move(direction);
		}
	}
}
