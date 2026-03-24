package ca.sfu.cmpt276.team7.unit.board;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.board.BoardLoader;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.cells.WallCell;
import ca.sfu.cmpt276.team7.core.Position;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BoardLoader
 *
 * Planned coverage:
 * - Valid map parsing
 * - Rectangular map validation
 * - Invalid symbol validation
 * - Required start/exit validation
 * - Entity placement from map file
 * - Structural cell type and walkability rules
 */
public class BoardLoaderTest {
    /*
     * Planned tests:
     *
     * load_validMap_returnsBoardWithExpectedDimensions DONE 
     * load_validMap_setsStartAndExitPositions DONE 
     * load_validMap_recordsGoblinSpawnPositions
     * load_validMap_recordsOgreSpawnPositions
     * load_validMap_recordsKeyPositions
     * load_validMap_recordsTrapPositions
     *
     * load_validMap_parsesWallBarrierAndFloorCells DONE 
     * load_validMap_parsesWallAndFloorCells() DONE 
     * load_validMap_appliesWalkabilityRules DONE 
     * load_markerTilesBecomeFloorCells
     * load_walkabilityRulesMatchCellTypes
     *
     * load_rejectsNullPath
     * load_rejectsEmptyMap
     * load_rejectsNonRectangularMap DONE 
     * load_rejectsUnknownSymbol DONE 
     * load_rejectsMissingStart
     * load_rejectsMissingExit
     *
     * Possible rule to confirm with team:
     * - outer wall rule
     * - start/exit boundary position rule
     *
     * These two are not currently enforced by BoardLoader code,
     * so we need to decide whether they are:
     * 1) actual loader validation rules, or
     * 2) just properties of chosen valid maps
     */

    @Test
    void load_validMap_returnsBoardWithExpectedDimensions() throws IOException {
        // Arrange
        Path mapPath = Path.of("src/test/resources/maps/simpleValidMap.txt"); // map 1 is a simple valid map 

        // Act
        BoardLoader.Result result = BoardLoader.load(mapPath);
        Board board = result.getBoard();

        // Assert
        assertEquals(5, board.getWidth());
        assertEquals(4, board.getHeight());
    }

    @Test
    void load_validMap_setsStartAndExitPositions() throws IOException {
        // Arrange
        Path mapPath = Path.of("src/test/resources/maps/simpleValidMap.txt"); // map 1 is a simple valid map 

        // Act
        BoardLoader.Result result = BoardLoader.load(mapPath);
        Board board = result.getBoard();

        // Assert
        assertEquals(new Position(1, 1), board.getStartPosition());
        assertEquals(new Position(3, 2), board.getEndPosition());
    }

    @Test
    void load_validMap_parsesWallAndFloorCells() throws IOException {
        // Arrange
        Path mapPath = Path.of("src/test/resources/maps/simpleValidMap.txt"); // map 1 is a simple valid map 

        // Act
        BoardLoader.Result result = BoardLoader.load(mapPath);
        Board board = result.getBoard();

        // Assert
        assertTrue(board.getCell(0, 0) instanceof WallCell);
        assertTrue(board.getCell(1, 1) instanceof FloorCell);
        assertTrue(board.getCell(2, 1) instanceof FloorCell);
        assertTrue(board.getCell(3, 2) instanceof FloorCell);
    }

    @Test
    void load_validMap_appliesWalkabilityRules() throws IOException {
        // Arrange
        Path mapPath = Path.of("src/test/resources/maps/simpleValidMap.txt"); 

        // Act
        BoardLoader.Result result = BoardLoader.load(mapPath);
        Board board = result.getBoard();

        // Assert
        assertFalse(board.getCell(0, 0).isWalkable());
        assertTrue(board.getCell(1, 1).isWalkable());
        assertTrue(board.getCell(2, 1).isWalkable());
        assertTrue(board.getCell(3, 2).isWalkable());
    }

    @Test
    void load_rejectsNonRectangularMap() {
        // Arrange
        Path mapPath = Path.of("src/test/resources/maps/nonRectangleMap.txt");

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            BoardLoader.load(mapPath);
        });
    }

    @Test
    void load_rejectsUnknownSymbol() {
        // Arrange
        Path mapPath = Path.of("src/test/resources/maps/invalidSymbolMap.txt");

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            BoardLoader.load(mapPath);
        });
    }
}
