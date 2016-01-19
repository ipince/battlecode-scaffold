package team378;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import team378.messages.ClearDistressMessage;
import team378.messages.DistressMessage;
import team378.messages.Message;
import team378.messages.MessageDispatcher;
import team378.messages.Protect;

import java.util.List;

public class RobotPlayer {

	private static final int RENDEZVOUS_MAX_ROUNDS = 50;
	private static final int SQUARED_DISTANCE_TO_RENDEZVOUS = 4;

	private final RobotController rc;
	private final Attack attack;
	private final Protect protect;

	private final Archon archon;
	private final Scout scout;
	private DistressMessage activeDistressMessage = null;

	private MapLocation rendezvousLocation;

	public static void run(RobotController rc) {
		RobotPlayer player = new RobotPlayer(rc);

		while (true) {
			try {
				player.preLoop();
				player.mainLoop();
				Clock.yield();
			} catch (GameActionException e) {
				// We suck.
			}
		}
	}

	public RobotPlayer(RobotController rc) {
		this.rc = rc;
		attack = new Attack(rc);
		archon = new Archon(rc);
		scout = new Scout(rc);
		protect = new Protect(rc, attack);
	}

	/**
	 * Stuff that only happens once, when the game begins.
	 */
	private void preLoop() throws GameActionException {
		setRendezvousLocation();
		while (rc.getRoundNum() < RENDEZVOUS_MAX_ROUNDS) {
			if (rc.getType() == RobotType.ARCHON) {
				if (rc.isCoreReady()
						&& rc.getLocation().distanceSquaredTo(rendezvousLocation) > SQUARED_DISTANCE_TO_RENDEZVOUS) {
					Move.decisively(rc, rendezvousLocation);
				}
			}
			Clock.yield();
		}
	}

	private void setRendezvousLocation() {
		MapLocation[] locations = rc.getInitialArchonLocations(rc.getTeam());
		int x = 0;
		int y = 0;
		for (MapLocation location : locations) {
			x += location.x;
			y += location.y;
		}
		rendezvousLocation = new MapLocation(x / locations.length, y / locations.length);
	}

	private boolean hostileRobotsNearby() {
		return rc.senseHostileRobots(rc.getLocation(), rc.getType().attackRadiusSquared).length > 0;
	}

	private void checkMessages() throws GameActionException {
		List<Message> messages = MessageDispatcher.getMessages(rc);
		for (Message m: messages) {
			if (m instanceof DistressMessage) {
				activeDistressMessage = (DistressMessage) m;
			} else if (m instanceof ClearDistressMessage) {
				activeDistressMessage = null;
			}
		}
	}

	private void mainLoop() throws GameActionException {
		checkMessages();

		if (rc.getType() == RobotType.ARCHON) {
			archon.main();
		} else if (rc.getType() == RobotType.SCOUT) {
			scout.main();
		} else if (rc.getType().canAttack() && activeDistressMessage != null) {
			protect.anAlly(activeDistressMessage.getLocation(), activeDistressMessage.getEscapeRoute());
		} else if (rc.getType().canAttack() && hostileRobotsNearby()) {
			attack.aMofo();
		} else if (rc.getType().canMove()) {
			Direction randomDir = randomDirection();
			rc.setIndicatorString(0, "I have nothing to do; I'm just wandering around in direction " + randomDir);
			Move.decisively(rc, randomDir);
		}
	}

	private Direction randomDirection() {
		return Direction.values()[(rc.getID() + rc.getRoundNum()) % Direction.values().length];
	}

	// TODO: list of implementable ideas.
	/*
	 * - Lure zombies with scouts towards the enemy.
	 *
	 * - Infected robot logic. (repair vs run away).
	 *
	 * - Collect parts if nearby. (and dig if necessary).
	 *
	 * - Activate if nearby.
	 *
	 * - Make turrets. 4 within 10 radius?
	 */

}
