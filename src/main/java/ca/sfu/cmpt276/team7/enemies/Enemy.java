package main.java.ca.sfu.cmpt276.team7;

/**
 * The enemy abstract class is the base of both goblin and ogre.
 */
public abstract class Enemy extends Character {
    /**
     * updateMovement() updates the position of the enemy by a single tick.
     */
    public void updateMovement(position player_position);
    /**
     * The Enemy constructor takes a pointer to the board.
     */
    public Enemy(Board board) {
	
    };

    /**
     * Enemies cannot move to a cell with a punishment, or a wall
     */
    public boolean canMoveto(Cell cell) {
	if (!(cell instanceof BarrierCell ||
	      cell instanceof WallCell ||
	      cell instanceof PunishmentCell)) {
	    return true;
	}
	return false;
    }
}
