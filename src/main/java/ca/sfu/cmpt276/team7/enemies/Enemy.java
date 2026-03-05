package ca.sfu.cmpt276.team7.enemies;

import ca.sfu.cmpt276.team7.core.Position; 
import ca.sfu.cmpt276.team7.core.GameCharacter;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.*;
import ca.sfu.cmpt276.team7.reward.PunishmentCell;

/**
 * The enemy abstract class is the base of both goblin and ogre.
 */
public abstract class Enemy extends GameCharacter {
    /**
     * updateMovement() updates the position of the enemy by a single tick.
     */
    abstract public void updateMovement(Position player_position);
    /**
     * The Enemy constructor takes a pointer to the board.
     */
    public Enemy(Board board) {
	super(board);
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
