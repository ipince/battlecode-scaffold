package team378;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;
import team378.messages.ClearDistressMessage;
import team378.messages.DistressMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Archon {

	private static final Map<RobotType, Double> robotPcts = new HashMap<>();
	private static final Random random = new Random();

	static {
		robotPcts.put(RobotType.SOLDIER, 0.50);
		robotPcts.put(RobotType.TURRET, 0.20);
		robotPcts.put(RobotType.GUARD, 0.30);
		robotPcts.put(RobotType.SCOUT, 0.05);
	}

	private final RobotController rc;

	public Archon(RobotController rc) {
		this.rc = rc;
		random.setSeed(rc.getID());
	}

	class EnemyInfo {
		private MapLocation closestLocation;
		private int numberOfEnemies = 0;

		public EnemyInfo(MapLocation closestLocation, int numberOfEnemies) {
			this.closestLocation = closestLocation;
			this.numberOfEnemies = numberOfEnemies;
		}
	}

	private EnemyInfo getEnemyInfo() {
		RobotInfo[] enemies = rc.senseHostileRobots(rc.getLocation(), -1);
		MapLocation loc = null;
		if (enemies.length > 0) {
			loc = enemies[0].location;
		}

		return new EnemyInfo(loc, enemies.length);

	}

	boolean activeDistressSignal = false;
	private void sendDistressSignal(MapLocation location) throws GameActionException {
		if (!activeDistressSignal) {
			DistressMessage signal = new DistressMessage(location, Direction.NORTH_EAST);
			signal.sendMessage(rc, 100);
			activeDistressSignal = true;
		}
	}

	private void clearDistressSignal() throws GameActionException {
		if (activeDistressSignal) {
			ClearDistressMessage signal = new ClearDistressMessage();
			activeDistressSignal = false;
			signal.sendMessage(rc, 100);
		}
	}


	public void main() throws GameActionException {
		if (rc.isCoreReady()) {
			EnemyInfo enemies = null;
			if ((enemies = getEnemyInfo()).numberOfEnemies > 0) {
				sendDistressSignal(enemies.closestLocation);
			} else {
				clearDistressSignal();
			}

			MapLocation[] parts = rc.sensePartLocations(RobotType.ARCHON.sensorRadiusSquared);
			if (parts.length > 0) {
				// find min-distance part. if multiple, choose one randomly.
				// maybe consider the rubble before deciding.
			}

			RobotType toBuild = randomType();
			Direction[] directions = getDirections(Direction.EAST); // arbitrary
			for (Direction direction : directions) {
				if (rc.canBuild(direction, toBuild)) {
					rc.build(direction, toBuild);
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

	private RobotType randomType() {
		// No time to implement real random, so making shitty alternative now.
		if (rc.getRoundNum() % 10 < 4) { // 50%
			return RobotType.SOLDIER;
		} else if (rc.getRoundNum() % 10 < 7) { // 30%
			return RobotType.SOLDIER;
		} else {
			return RobotType.TURRET;
		}
	}

}
