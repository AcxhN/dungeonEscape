package ca.sfu.cmpt276.team7.board;

import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.core.Position;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Board
 *
 * Planned coverage:
 * - Board dimensions
 * - Out-of-bounds access rule
 * - Boundary coordinate checks via isInside
 * - Setting and getting cells
 * - Start/end position validation
 * - Grid constructor validation
 */
public class BoardTest {
    /*
     * Planned tests:
     *
     * constructor_setsWidthAndHeight
     * constructor_rejectsNullGrid
     * constructor_rejectsEmptyGrid
     * constructor_rejectsNonRectangularGrid
     * constructor_rejectsNullCell
     *
     * getCell_returnsCellInBounds
     * getCell_throwsWhenOutOfBounds
     *
     * setCell_replacesCellInBounds
     * setCell_rejectsNullCell
     * setCell_throwsWhenOutOfBounds
     *
     * isInside_returnsTrueForValidPositions
     * isInside_returnsFalseForOutsidePositions
     *
     * setStartPosition_setsValidPosition
     * setStartPosition_rejectsNull
     * setStartPosition_rejectsOutOfBounds
     *
     * setEndPosition_setsValidPosition
     * setEndPosition_rejectsNull
     * setEndPosition_rejectsOutOfBounds
     */

    /**
     * testing Board constructor:
        * this.height = grid.length;
        * this.width = grid[0].length;
     * testing if we pass a 2x3 grid does height = 2 and width = 3?
     */
    @Test // tells JUnit this is a test method 
    void constructor_setsWidthAndHeight() { // trying to follow naming concention: method_beingTested_expectedBehaviour 
        // ========================
        // Arrange
        // ========================
        Cell[][] grid = new Cell[2][3];

        for (int y = 0; y < 2; y++) { // filling b/c constructor requires no null rows and cells 
            for (int x = 0; x < 3; x++) {
                grid[y][x] = new FloorCell(new Position(x, y));
            }
        }

        // ========================
        // Act
        // ========================
        Board board = new Board(grid);

        // ========================
        // Assert (checks expected vs actual)
        // ========================
        assertEquals(3, board.getWidth());
        assertEquals(2, board.getHeight());
    }
}