package elskippy;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class RobotPlayer {

	private static final int RENDEZVOUS_MAX_ROUNDS = 50;
	private static final int SQUARED_DISTANCE_TO_RENDEZVOUS = 4;

	private final RobotController rc;
	private final Move move;
	private final Attack attack;

	private final Archon archon;

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
		move = new Move(rc);
		attack = new Attack(rc);
		archon = new Archon(rc);
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
					move.decisively(rendezvousLocation);
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

	private void mainLoop() throws GameActionException {
		if (rc.getType() == RobotType.ARCHON) {
			archon.main();
		} else if (rc.getType().canAttack()) {
			attack.aMofo();
		}
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
