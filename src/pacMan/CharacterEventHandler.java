package pacMan;

public class CharacterEventHandler {

	private int cycle_frame = 0;
	private final int frames_per_cycle;

	private PacMan pacman;
	private Ghost[] ghosts;
	
	private DotState dotstate;
	
	private PacManBoard.Tyle[][] tyle_board;

	public CharacterEventHandler(int frames_per_cycle, PacMan pacman, Ghost[] ghosts, PacManBoard.Tyle[][] tyle_board) {
		this.frames_per_cycle = frames_per_cycle;
		this.pacman = pacman;
		this.ghosts = ghosts;
		this.dotstate = new DotState(pacman, DotState.State.OFF, tyle_board);
		this.tyle_board = tyle_board;
	}
	
	public void setCharacters() {
		for (int i = 0; i < 4; i++) {
			Ghost ghost = ghosts[i];
			ghost.setImage();
			ghost.updateImage();
		}
		pacman.updateImage();
	}
	
	public void postKeyPressEventHandler(int[] delta) {
		pacman.updateImage();		
		if (isNotStalled(pacman.getSpeedPercent())) {
			pacman.update(delta[0], delta[1], tyle_board);
		}
		
		pacman.setSpeed(tyle_board);
		
		if (pacman.getDeltaX() != 0 || pacman.getDeltaY() != 0) {
			for (int i = 0; i < 4; i++) {
				Ghost ghost = ghosts[i];
				ghost.setSpeed();
				ghost.setImage();
				ghost.updateImage();
				
				if (ghost.checkCollision(dotstate, pacman)) {
					pacman.resetPacMan();
					for (int j = 0; j < 4; j++) {
						ghosts[j].resetGhost();
					}
					handleStart();
				}
				
				if (isNotStalled(ghost.getSpeedPercent())) {
					if (ghost.getStartCount() != -1) {
						ghost.ghostStart();
					}
					if (ghost.getStartCount() == -1) {
						ghost.updateMove(pacman.getX(), pacman.getY());
					}
				
					ghost.stateHandler(dotstate);
				
				}
				
				if (ghost.checkCollision(dotstate, pacman)) {
					pacman.resetPacMan();
					for (int j = 0; j < 4; j++) {
						ghosts[j].resetGhost();
						System.out.println("resetting");
					}
					handleStart();
				}
			}
			
			dotstate.blinkPowerUps(cycle_frame, 10);
			dotstate.powerupHandler(pacman);
			
			pacman.updateDots(tyle_board);
			
			cycle_frame++;
			if (cycle_frame == frames_per_cycle)
				cycle_frame = 0;
		}
	}

	public void handleStart() {
		while (pacman.getStartCount() != -1) {
			pacman.pacmanStart();
			PacManBoard.sleep();
		}
	}
	
	private boolean isNotStalled(int speed_percent) {
		double ratio = 1 - speed_percent / 100.0;
		int stall_frame = (int) Math.floor(1.0 / ratio);
		if (cycle_frame % stall_frame == 0)
			return false;
		return true;
	}
	
}