package team378;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameConstants;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Move {

	private static MapLocation enemyBaseLocation;

	/**
	 * Moves in the given direction. If there's rubble preventing motion in that
	 * direction, the rubble is cleared.
	 *
	 * Assumes rc.isCoreReady() == true.
	 */
	public static void decisively(RobotController rc, Direction direction) throws GameActionException {
		if (rc.senseRubble(rc.getLocation().add(direction)) >= GameConstants.RUBBLE_OBSTRUCTION_THRESH) {
			rc.clearRubble(direction);
		} else if (rc.canMove(direction)) {
			rc.move(direction);
		}
	}

	public static void decisively(RobotController rc, MapLocation target) throws GameActionException {
		decisively(rc, rc.getLocation().directionTo(target));
	}

	/**
	 * Moves to the target location without checking for rubble. Only used for scouts.
	 */
	public static void likeYouJustDontCare(RobotController rc, MapLocation target) throws GameActionException {
		if (!rc.isCoreReady()) {
			// fail
			return;
		}
		rc.setIndicatorLine(rc.getLocation(), target, 0, 0, 255);
		Direction direction = rc.getLocation().directionTo(target);
		if (rc.canMove(direction)) {
			rc.setIndicatorLine(rc.getLocation(), rc.getLocation().add(direction), 255, 0, 0);
			rc.move(direction);
		} else if (rc.canMove(direction.rotateLeft())) {
			// try 45 degrees each way.
			rc.setIndicatorLine(rc.getLocation(), rc.getLocation().add(direction.rotateLeft()), 255, 0, 0);
			rc.move(direction.rotateLeft());
		} else if (rc.canMove(direction.rotateRight())) {
			rc.setIndicatorLine(rc.getLocation(), rc.getLocation().add(direction.rotateRight()), 255, 0, 0);
			rc.move(direction.rotateRight());
		}
	}

	public static void towardsTheBadGuys(RobotController rc) throws GameActionException {
		if (!rc.isCoreReady()) {
			// fail
			return;
		}
		if (enemyBaseLocation == null) {
			enemyBaseLocation = average(rc.getInitialArchonLocations(rc.getTeam().opponent()));
		}
		likeYouJustDontCare(rc, enemyBaseLocation);
	}

	private static MapLocation average(MapLocation[] locations) {
		int x = 0;
		int y = 0;
		for (MapLocation location : locations) {
			x += location.x;
			y += location.y;
		}
		MapLocation average = new MapLocation(x / locations.length, y / locations.length);
		return average;
	}
}
