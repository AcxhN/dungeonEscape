package ca.sfu.cmpt276.team7.core;

import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.core.Position;

/**
 * Base GameCharacter class
 */

abstract public class GameCharacter {
    private Position position;
    private Board board;

    public GameCharacter(Board board){
	this.board = board;
    }
    abstract public boolean canMoveto(Cell cell);
    public Position getPosition() {
	return position;
    }
}
