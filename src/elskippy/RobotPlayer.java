package elskippy;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class RobotPlayer {

	private static RobotController rc;

	public static void run(RobotController rcIn) {
		rc = rcIn;
		while (true) {
			try {
				mainLoop();
				Clock.yield();
			} catch (GameActionException e) {
				// We suck.
			}
		}
	}

	private static void mainLoop() throws GameActionException {

	}

}
