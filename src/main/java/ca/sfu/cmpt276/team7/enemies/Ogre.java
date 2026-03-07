package ca.sfu.cmpt276.team7.enemies;

import java.util.ArrayList;
import java.util.List;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.WallCell;
import ca.sfu.cmpt276.team7.core.Direction;
import ca.sfu.cmpt276.team7.core.Position;

/**
 * The Ogre class
 */
public class Ogre extends Enemy {
    /**
     * Small helper enum for directions in constructor
     */

    private ArrayList<Position> patrolRoute;
    private boolean forward_p;
    private int route_index;
    /**
     * Makes the ogre class, takes a list of positions to follow.
     * sets the ogre to move forward up the list from the first position on the list
     */
    public Ogre(Board board, Position... route) {
	super(board);
	this.patrolRoute = new ArrayList<>(List.of(route));
	forward_p = true;
	position = patrolRoute.get(0);

    }

    /**
     * Helper constructor, that sets the ogre to move back and forth, rather than a patrol route
     */
    public Ogre(Board board, Position first_position, Direction direction) {
	super(board);
	this.position = first_position;
	this.patrolRoute = new ArrayList<Position>();

	Position potential_position = position;
	Cell next_cell = board.getCell(potential_position.getX(), potential_position.getY());

	int add_x = (direction == Direction.EAST) ? 1 : (direction == Direction.WEST ? -1 : 0);
	int add_y = (direction == Direction.SOUTH) ? 1 : (direction == Direction.NORTH ? -1 : 0);

	// Go in one direction until a wall is hit
	while (!(next_cell instanceof WallCell)) {
	    patrolRoute.add(potential_position);
		
		int nextX = potential_position.getX() + add_x;
		int nextY = potential_position.getY() + add_y;

		if (nextX < 0 || nextY < 0 || nextX >= board.getWidth() || nextY >= board.getHeight()) {
			break;
		}

	    potential_position = new Position(nextX, nextY);
	    next_cell = board.getCell(nextX, nextY);
	}

	// Go in the other direction, adding to the other side of the list

	potential_position = this.position;
	next_cell = board.getCell(potential_position.getX(), potential_position.getY());
	int num_added = 0;
	add_x *= -1;
	add_y *= -1;

	while (!(next_cell instanceof WallCell)) {
	    if (num_added != 0) { // Check to make sure the beginning position isn't added twice
		patrolRoute.add(0, potential_position);
	    }
	    num_added++;

		int nextX = potential_position.getX() + add_x;
    	int nextY = potential_position.getY() + add_y;
		
		if (nextX < 0 || nextY < 0 || nextX >= board.getWidth() || nextY >= board.getHeight()) {
			break;
		}

	    potential_position = new Position(nextX, nextY);
	    next_cell = board.getCell(nextX, nextY);
	}

	// set route_index to the middle
	route_index = num_added - 1;

	this.forward_p = (direction == Direction.SOUTH || direction == Direction.EAST) ? true : false;

	
    }

    /**
     * Moves the ogre to the next postion of the patrol route, changing directions if it reaches the end, or hits a wall
     */
    public void updateMovement(Position player_position) {
	if (internalUpdate()) {
	    return;
	} else {
	    // if it fails in both directions, it is stuck, stay still
	    internalUpdate();
	}

    }


    /**
     * Internal function for updating position, attempts to move forward, switching direction and returning false if it fails
     */
    private boolean internalUpdate() {
	// update index

	int next_index = (forward_p) ? route_index + 1: route_index - 1;

	if (next_index < 0 || next_index >= patrolRoute.size()) {
	    forward_p = !forward_p;
	    return false;
	}

	
	Position potential_position = patrolRoute.get(next_index);
	Cell next_cell = board.getCell(potential_position.getX(), potential_position.getY());

	if (this.canMoveto(next_cell)) {
	    position = potential_position;
	    route_index = next_index;
	    return true;
	} else {
	    forward_p = !forward_p;
	    return false;
	}
    }
}
