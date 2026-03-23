package ca.sfu.cmpt276.team7.unit.board;

import ca.sfu.cmpt276.team7.board.Board;
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
     * constructor validations:
        * constructor_setsWidthAndHeight DONE 
        * constructor_rejectsNullGrid DONE 
        * constructor_rejectsEmptyGrid DONE 
        * constructor_rejectsNonRectangularGrid DONE 
        * constructor_rejectsNullCell DONE 
     *
     * board behaviour: 
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
     * testing if Board constructor properly sets height and width:
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

    /*
    testing if Board constructor throws the right exception
    learning to use assertThrows(), which will also be used to test:
        invalid maps
        bad symbols
        missing start/end
        out of bounds access 

    Board constructor starts with: 
        if (grid == null || grid.length == 0) {
        throw new IllegalArgumentException("grid must be non null and not empty");
        }
    so 
        new Board(null);
    should throw
        IllegalArgumentException 

    lets tests it 
    */
   /*
    the basic form of assert throws is:
        assertThrows(ExceptionType.class, () -> {
        // code that should throw
        });

    () -> { ...}
    is a lambda, a function with not arguments 
    JUnit needs it because it wants to "run this code and watch whether it throws"
    for testing just think that lambas, () -> { ...}, just mean "here is the code I want JUnit to execute"
    */
    /**
    * testing if constructor rejects null grid 
    */
    @Test
    void constructor_rejectsNullGrid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Board(null);
        });
    }

    /**
    * constructor rejects grid.length = 0; 
    */
    @Test
    void constructor_rejectsEmptyGrid() {
        Cell[][] grid = new Cell[0][0];

        assertThrows(IllegalArgumentException.class, () -> {
            new Board(grid);
        });
    }

    /**
     * Board constructor rejects rows with different lengths:
     *  if (grid[y] == null || grid[y].length != width)
     */
    @Test
    void constructor_rejectsNonRectangularGrid() {
        // Arrange
        Cell[][] grid = new Cell[2][];
        grid[0] = new Cell[3];
        grid[1] = new Cell[2];

        for (int x = 0; x < 3; x++) {
            grid[0][x] = new FloorCell(new Position(x, 0));
        }

        for (int x = 0; x < 2; x++) {
            grid[1][x] = new FloorCell(new Position(x, 1));
        }

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Board(grid);
        });
    }

    /*
    Board constructor ensures every coordinate contains a real Cell object:
        if (grid[y][x] == null) {
        throw new IllegalArgumentException(...);
        }
     */
    /**
     * tests if constructors rejects null cells 
     */
    @Test
    void constructor_rejectsNullCell() {
        // Arrange
        Cell[][] grid = new Cell[2][2];

        grid[0][0] = new FloorCell(new Position(0, 0));
        grid[0][1] = new FloorCell(new Position(1, 0));
        grid[1][0] = new FloorCell(new Position(0, 1));
        grid[1][1] = null; // the one invalid cell

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Board(grid);
        });
    }

    /*
    make a valid board
    ask for a cell at a valid coordinate 
    verify the object is correct 

        public Cell getCell(int x, int y) {
        ensureInBounds(x, y);
        return grid[y][x];
        }
    so if (x,y) is valid then it should reutrn the cell stored at that spot 
    this test checks if the board stores cells correctly and if getCell(x,y) uses the coordinates correctly 

    How do we know if we get back the exact same cell object that we put into the grid??
    we use 
        assertSame(expected, actual)
    instead of
        asserEquals
    because Cell does not define a custom equals() method, and in this case we don't want the same value, but the same object reference 
    assertEquals = same value
    assertSame = same object in memory 
     */
    @Test
    void getCell_returnsCellInBounds() {
        // Arrange
        Cell[][] grid = new Cell[2][2];

        FloorCell topLeft = new FloorCell(new Position(0, 0));
        FloorCell topRight = new FloorCell(new Position(1, 0));
        FloorCell bottomLeft = new FloorCell(new Position(0, 1));
        FloorCell bottomRight = new FloorCell(new Position(1, 1));

        grid[0][0] = topLeft;
        grid[0][1] = topRight;
        grid[1][0] = bottomLeft;
        grid[1][1] = bottomRight;

        Board board = new Board(grid);

        // Act
        Cell result = board.getCell(1, 0);

        // Assert
        assertSame(topRight, result);
    }
}
