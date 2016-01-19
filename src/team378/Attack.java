package team378;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;

import java.util.Arrays;
import java.util.List;

public class Attack {

	private final RobotController rc;

	public Attack(RobotController rc) {
		this.rc = rc;
	}

	public void aMofo() throws GameActionException {
		if (rc.isWeaponReady()) {
			List<RobotInfo> hostiles = Arrays.asList(rc.senseHostileRobots(rc.getLocation(),
					rc.getType().attackRadiusSquared));
			// attack weakest mofo first.
			// TODO: implement better micro: maybe I can kill multiple robots with 1 shot, so I
			// should choose the one with highest health. Or maybe I can see if the robots can move
			// or not; if so, I should target the ones that can't move (safer bet). Or, I can check
			// if the robots can attack or not; then target the ones that can (minimize attacks).
			// Or, I can focus on the biggest enemies. Or, I can ignore infected robots that are
			// about to die. etc.
			hostiles.sort((a, b) -> (int) Math.round(a.health - b.health));
			if (hostiles.size() > 0) {
				rc.attackLocation(hostiles.get(0).location);
			}
		}
	}
}
