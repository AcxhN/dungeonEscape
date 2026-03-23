package ca.sfu.cmpt276.team7.unit.enemies;

import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.cells.WallCell;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.reward.PunishmentCell;
import ca.sfu.cmpt276.team7.reward.TrapPunishment;
import ca.sfu.cmpt276.team7.enemies.Goblin;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class GoblinTest {
	// Goblin Basic Movement
	// Tests that the goblin makes one move towards a single goal position
	@Test
	void TestGoblinBasicMovement() {
		// This is messy, but it reduces the use of other classes in unit tests
		Board board = new Board(new Cell[][] {
				{ new FloorCell(new Position(0, 0)), new FloorCell(new Position(1, 0)) },
				{ new FloorCell(new Position(0, 1)), new FloorCell(new Position(1, 1)) },

		});
		Goblin goblin = new Goblin(board, new Position(1, 1));

		// Goal position to move towards
		Position goalPosition = new Position(1, 0);
		goblin.updateMovement(goalPosition);
		// Goblin should move towards the goal position
		assertTrue(goblin.getPosition().equals(goalPosition));
	}

    //Goblin Chase Rule
	// Tests that the goblin chases a moving goal	
	@Test
	void TestGoblinChase() {
		// This is messy, but it reduces the use of other classes in unit tests
		Board board = new Board(new Cell[][] {
				{ new FloorCell(new Position(0, 0)), new FloorCell(new Position(1, 0)),
				  new FloorCell(new Position(2, 0)), new FloorCell(new Position(3, 0)),
				  new FloorCell(new Position(4, 0)), new FloorCell(new Position(5, 0))},
		});
		Goblin goblin = new Goblin(board, new Position(0, 0));
		for (int i = 1; i < 6; i++) {
			// Goal position to move towards
			Position goalPosition = new Position(i, 0);
			goblin.updateMovement(goalPosition);
			// Goblin should move towards the goal position
			assertTrue(goblin.getPosition().equals(goalPosition));
		}
		for (int i = 4; i >= 0; i--) {
			// Goal position to move towards
			Position goalPosition = new Position(i, 0);
			goblin.updateMovement(goalPosition);
			// Goblin should move towards the goal position
			assertTrue(goblin.getPosition().equals(goalPosition));
		}
	}
    //Goblin Unreachable Player Rule
	//Ensures that the goblin remains still if it cannot path towards the player
	@Test
	void TestGoblinUnreachablePlayer() {
		// This is messy, but it reduces the use of other classes in unit tests
		Board board = new Board(new Cell[][] {
				{ new FloorCell(new Position(0, 0)), new WallCell(new Position(1, 0)), new FloorCell(new Position(2, 0)), new FloorCell(new Position(3, 0)) },
				{ new FloorCell(new Position(0, 1)), new WallCell(new Position(1, 1)), new FloorCell(new Position(2, 1)), new FloorCell(new Position(3, 1)) },

		});
		Goblin goblin = new Goblin(board, new Position(3, 0));

		// Goal position to move towards
		Position goalPosition = new Position(1, 0);
		goblin.updateMovement(goalPosition);
		// Goblin should NOT move towards the goal, as it is unreachable
		assertTrue(goblin.getPosition().equals(new Position(3, 0)));
	}

    //Goblin Blocking Rule
	// 

    //Goblin Non-Destructive Movement
	//Ensures that the goblin doesn't affect the board as it moves through
	@Test
	void TestGoblinNonDestructiveMovement() {
		// This is messy, but it reduces the use of other classes in unit tests
		Cell[][] cellGrid = new Cell[][] {
				{ new FloorCell(new Position(0, 0)), new WallCell(new Position(1, 0)), new WallCell(new Position(2, 0)), new FloorCell(new Position(3, 0)) },
				{ new FloorCell(new Position(0, 1)), new WallCell(new Position(1, 1)), new FloorCell(new Position(2, 1)), new FloorCell(new Position(3, 1)) },
				{ new FloorCell(new Position(0, 2)), new FloorCell(new Position(1, 2)), new FloorCell(new Position(2, 2)), new WallCell(new Position(3, 2)) },
		};

		// Keep a snapshot of the references before the goblin begins pathing
		Cell[][] snapshot = new Cell[3][4];
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 4; x++) {
				snapshot[y][x] = cellGrid[y][x];
			}
		}

		Board board = new Board(cellGrid);

		Goblin goblin = new Goblin(board, new Position(3, 0));

		// Goal position to move towards
		Position goalPosition = new Position(0, 1);

		// Give goblin enough movements to path through
		for (int i = 0; i < 8; i++) {
			goblin.updateMovement(goalPosition);
		}
		// Cells should remain completely untouched
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 4; x++) {
				assertTrue((snapshot[y][x]) == board.getCell(x, y));
			}
		}
	}


    //Goblin Collision Rules
	// Test that the goblin doesn't path into tiles that it shouldn't
	@Test
	void TestGoblinCollisionWall() {
		// Test will wallcell
		Board board = new Board(new Cell[][] {
				{ new FloorCell(new Position(0, 0)), new WallCell(new Position(1, 0)), new FloorCell(new Position(2, 0))}});
		Goblin goblin = new Goblin(board, new Position(2, 0));

		// Goal position to move towards
		Position goalPosition = new Position(0, 0);
		goblin.updateMovement(goalPosition);
		// Goblin should NOT move towards the goal position
		assertTrue(goblin.getPosition().equals(new Position(2, 0)));
	}

	//Test that the goblin doesn't walk over punishments
	@Test
	void TestGoblinCollisionPunishmentCell() {
		// Test will wallcell
		Board board = new Board(new Cell[][] {
				{ new FloorCell(new Position(0, 0)), new PunishmentCell(new Position(1, 0), new TrapPunishment(1)), new FloorCell(new Position(2, 0))}});
		Goblin goblin = new Goblin(board, new Position(2, 0));

		// Goal position to move towards
		Position goalPosition = new Position(0, 0);
		goblin.updateMovement(goalPosition);
		// Goblin should NOT move towards the goal position
		assertTrue(goblin.getPosition().equals(new Position(2, 0)));
	}
	
    
}
