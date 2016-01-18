package elskippy;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class Archon {

	private final RobotController rc;

	public Archon(RobotController rc) {
		this.rc = rc;
	}

	public void main() throws GameActionException {
		if (rc.isCoreReady()) {
			Direction[] directions = getDirections(Direction.EAST); // arbitrary
			for (Direction direction : directions) {
				if (rc.canBuild(direction, RobotType.TURRET)) {
					rc.build(direction, RobotType.TURRET);
					break;
				}
			}
		}
	}

	// this is retarded and should probably go away.
	private Direction[] getDirections(Direction desired) {
		int[] deltas = new int[] { 0, 1, -1, -2, 2 };
		Direction[] directions = new Direction[5];
		for (int i = 0; i < directions.length; i++) {
			directions[i] = Direction.values()[(desired.ordinal() + deltas[i]) % Direction.values().length];
		}
		return directions;
	}

}
