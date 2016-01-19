package team378;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import battlecode.common.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scout {

	private static final boolean DEBUG = true;

	private static final int MIN_ZOMBIES_IN_VISION = 3;
	private static final int MAX_ZOMBIES_IN_VISION = 5;
	private static final int TARGET_DISTANCE_TO_DANGER = 4;

	// private static final Random random = new
	// Random(System.currentTimeMillis());

	private static List<MapLocation> parts = new ArrayList<>();
	private static List<MapLocation> neutrals = new ArrayList<>();
	private static List<MapLocation> zombieDens = new ArrayList<>();
	private static List<MapLocation> enemyArchons = new ArrayList<>();

	private static Set<MapLocation> seen = new HashSet<>();

	private static enum State {
		EXPLORING,
		WAITING_FOR_ZOMBIES,
		WAITING_TO_EXPLORE,
		LURING,
		FLEEING;
	}

	private State state = State.EXPLORING;
	private RobotController rc;

	public Scout(RobotController rc) {
		this.rc = rc;
	}

	public void main() throws GameActionException {
		if (DEBUG) {
			setDebugInfo();
		}
		switch (state) {
		case EXPLORING:
			explore();
			break;
		case WAITING_FOR_ZOMBIES:
			waitForZombies();
			break;
		default:
			// do nothing
		}

		// 1. move to maximize map exploration and minimize death.
		// keep track of parts, neutrals, enemy, dens, etc.
		// 2. stay near a zombie den if the next wave is close.
		// 3. lure.
	}

	private void explore() throws GameActionException {
		if (!rc.isCoreReady()) {
			return;
		}

		List<MapLocation> seenNow = Arrays.asList(MapLocation.getAllMapLocationsWithinRadiusSq(
				rc.getLocation(), RobotType.SCOUT.sensorRadiusSquared));
		seen.addAll(seenNow);

		Direction dir = Direction.values()[rc.getID() % Direction.values().length];
		if (rc.canMove(dir)) {
			rc.move(dir);
		}

		// TODO(rodrigo): sense useful things and send them out.

		RobotInfo[] infos = rc.senseNearbyRobots();
		for (RobotInfo info : infos) {
			if (info.team == Team.ZOMBIE) {
				state = State.WAITING_FOR_ZOMBIES;
				return;
			}
		}
	}

	private void waitForZombies() throws GameActionException {
		// We are near zombies (probably). Check how many and in how much danger we're in.
		RobotInfo[] zombies = rc.senseNearbyRobots(RobotType.SCOUT.sensorRadiusSquared, Team.ZOMBIE);
		rc.setIndicatorString(1, "Seeing " + zombies.length + " zombies");
		if (zombies.length == 0) {
			// what to do? wait a bit before exploring.
			state = State.WAITING_TO_EXPLORE;
			return;
		} else if (zombies.length > 0 && zombies.length < 4) {
			// sweet spot.
			// let's taunt them.
		} else if (zombies.length >= 4) {
			// getting too hot in here. run away.
		}

		RobotInfo nearestInRange = null;
		int minDistanceToDanger = RobotType.SCOUT.sensorRadiusSquared;
		for (RobotInfo zombie : zombies) {
			int distanceToDanger = rc.getLocation().distanceSquaredTo(zombie.location) - zombie.type.attackRadiusSquared;
			if (distanceToDanger < minDistanceToDanger) {
				nearestInRange = zombie;
				minDistanceToDanger = distanceToDanger;
			}
		}
		assert (nearestInRange != null);
		rc.setIndicatorString(2, "min distance to danger is " + minDistanceToDanger);

		// If we're too close or there are too many zombies, run away.
		if (minDistanceToDanger < TARGET_DISTANCE_TO_DANGER || zombies.length >= MAX_ZOMBIES_IN_VISION) {
			// run away.
			rc.setIndicatorString(3, "running to enemy");
			// Move.towardsTheBadGuys(rc);
			return;
		} else if (minDistanceToDanger > TARGET_DISTANCE_TO_DANGER) {
			// we're too far. get closer. live on the edge.
			rc.setIndicatorString(3, "getting closer to enemy");
			// Move.likeYouJustDontCare(rc, nearestInRange.location);
			return;
		} else {
			rc.setIndicatorString(3, "staying put");
			// we're just right. stay put.
		}
	}

	private void setDebugInfo() {
		rc.setIndicatorString(0, state.name());
//		for (MapLocation location : seen) {
//			rc.setIndicatorDot(location, 0, 1, 0);
//		}
	}

}
