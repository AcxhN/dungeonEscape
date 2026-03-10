package ca.sfu.cmpt276.team7.enemies;

import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.*;

import java.util.PriorityQueue;
import java.util.ArrayList;

/**
 * The goblin pathfinds towards the player
 * WIP, currently just a stub
 */
public class Goblin extends Enemy {
	public Goblin(Board board, Position pos) {
		super(board);
		this.position = pos;
	}

	private class pathingEntry {
		public int cost = 1;
		public Position parent;
		public Position pos;

		public pathingEntry(int cost, Position parent, Position pos) {
			this.cost = cost;
			this.parent = parent;
			this.pos = pos;
		}

		/**
		 * Checks if positions are equal and nothing else
		 */
		@Override
		public boolean equals(Object o) {
			if (!(o instanceof pathingEntry))
				return false;
			pathingEntry other = (pathingEntry)o;
			return other.pos.equals(this.pos);
		}
	}

	/**
	 * Moves the goblin 1 tile towards the player
	 */

	public void updateMovement(Position player_position) {
		// A* pathing algorithm
		PriorityQueue<pathingEntry> next_items = new PriorityQueue<>((a, b) -> Integer.compare(
				pathingHeuristic(player_position, a.pos) + a.cost,
				pathingHeuristic(player_position, b.pos) + b.cost));
		ArrayList<pathingEntry> seen_items = new ArrayList<pathingEntry>();

		// Add initial node to next_items
		next_items.add(new pathingEntry(0, null, this.position));

		while (!next_items.isEmpty()) {
			pathingEntry next_entry = next_items.poll();
			Position next_pos = next_entry.pos;

			if (this.position == player_position) {
				// Get the full path
			}

			// If the cell is valid, add it to seen, add neighbours to next queue
			if (canMoveto(board.getCell(next_pos.getX(), next_pos.getY()))) {
				int new_cost = next_entry.cost + 1;
				seen_items.add(new pathingEntry(new_cost, next_pos,));
				seen_items.add(new pathingEntry(new_cost, next_pos,));
				seen_items.add(new pathingEntry(new_cost, next_pos,));
				seen_items.add(new pathingEntry(new_cost, next_pos,));
			}
			//

		}
		// If it reaches this point - player is unreachable
		return;

	}

	/**
	 * Internal function, calculates manhattan distance between the player and a
	 * given cell
	 */
	private int pathingHeuristic(Position player_position, Position cell_position) {
	    return Math.abs(player_position.getX() - cell_position.getX()) +
		Math.abs(player_position.getY() - cell_position.getY());
	}

	private void addNeighbours(pathingEntry parent, ArrayList<pathingEntry> seenlist, ArrayList<pathingEntry> next_items) {
		// Get init cost and parent entries created
		pathingEntry[] neighbours = new pathingEntry[4];
		for (int i = 0; i < 4; i++) {
			neighbours[i] = new pathingEntry(parent.cost + 1, parent.pos, new Position(0, 0));
		}

		// set positions
		neighbours[0].pos = new Position(parent.pos.getX(), parent.pos.getY() + 1);
		neighbours[1].pos = new Position(parent.pos.getX() + 1, parent.pos.getY());
		neighbours[2].pos = new Position(parent.pos.getX(), parent.pos.getY() - 1);
		neighbours[3].pos = new Position(parent.pos.getX() - 1, parent.pos.getY());

		for (pathingEntry neighbour : neighbours) {
			if (board.isInside(neighbour.pos)) { // position must be valid
				int entry_index = seenlist.indexOf(neighbour);
				if (entry_index < 0) {
					// Index is -1, not seen, add to nextitems
					next_items.add(neighbour);
				} else {
					// Already seen, see if the cost is lower then replace it
				}
			}
		}

		
	}
}
