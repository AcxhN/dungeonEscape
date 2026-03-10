package ca.sfu.cmpt276.team7.enemies;

import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.board.Board;

import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;

/**
 * The goblin pathfinds towards the player
 */
public class Goblin extends Enemy {
	public Goblin(Board board, Position pos) {
		super(board);
		this.position = pos;
	}

	/**
	 * Moves the goblin 1 tile towards the player
	 */
	public void updateMovement(Position player_position) {
		// A* pathing algorithm
		Map<Position, Integer> costSoFar = new HashMap<Position, Integer>();
		Map<Position, Position> parent = new HashMap<Position, Position>();
		PriorityQueue<Position> open = new PriorityQueue<>((a, b) -> Integer.compare(
				pathingHeuristic(player_position, a) + costSoFar.get(a),
				pathingHeuristic(player_position, b) + costSoFar.get(b)));

		// Add initial node to next_items
		open.add(this.position);
		costSoFar.put(this.position, 0);

		while (!open.isEmpty()) {
			Position next_pos = open.poll();

			if (next_pos.equals(player_position)) {
				// Get the full path
				Position tmp = next_pos;
				Position next_move = next_pos;
				while (!tmp.equals(this.position)) {
					next_move = tmp;
					tmp = parent.get(tmp);
				}
				this.position = next_move;
				return;
			}

			// == If the cell is valid, add it to seen, add neighbours to next queue ==

			// Array for neighbors in cardinal directions
			Position[] neighbours = new Position[4];
			// set positions
			neighbours[0] = new Position(next_pos.getX(), next_pos.getY() + 1);
			neighbours[1] = new Position(next_pos.getX() + 1, next_pos.getY());
			neighbours[2] = new Position(next_pos.getX(), next_pos.getY() - 1);
			neighbours[3] = new Position(next_pos.getX() - 1, next_pos.getY());

			for (Position neighbour : neighbours) {
				if (canMoveto(board.getCell(neighbour.getX(), neighbour.getY()))
						&& board.isInside(neighbour)) { // check if valid
					// If we already have the key, check if the cost is better from this parent
					// before adding the cost
					if (costSoFar.containsKey(neighbour)) {
						if (costSoFar.get(neighbour) > costSoFar.get(next_pos) + 1) {
							costSoFar.put(neighbour, costSoFar.get(next_pos) + 1);
							parent.put(neighbour, next_pos);
							open.add(neighbour);
						}
					} else {
						costSoFar.put(neighbour, costSoFar.get(next_pos) + 1);
						parent.put(neighbour, next_pos);
						open.add(neighbour);
					}
				}
			}
		}
		// If it reaches this point player is unreachable
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

}
