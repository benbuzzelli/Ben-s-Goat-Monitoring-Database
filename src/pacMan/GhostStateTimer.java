package pacMan;

import java.util.Timer;
import java.util.TimerTask;

import pacMan.DotTimer.FreeGhost;
import pacMan.Ghost.TargetingState;

public class GhostStateTimer {
	private Timer timer = new Timer();
	private TargetingState targetingState = TargetingState.SCATTER;
	
	private int scatter_sec = 7;
	private int attack_sec = 20;
	
	public TargetingState getTargetState() {
		return targetingState;
	}
	
	public void scheduleAttack() {
		timer = new Timer();
		timer.schedule(new SetToAttack(), scatter_sec * 1000);
	}
	
	public void scheduleScatter() {
		timer = new Timer();
		timer.schedule(new SetToScatter(), attack_sec * 1000);
	}
	
	class SetToAttack extends TimerTask {
		
		public void run() {
			targetingState = TargetingState.ATTACK;
			timer.cancel();
			scheduleScatter();
		}
	}
	
	class SetToScatter extends TimerTask {
		
		public void run() {
			targetingState = TargetingState.SCATTER;
			timer.cancel();
			scheduleAttack();
		}
	}
}