package ca.sfu.cmpt276.team7.unit.board;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.board.BoardLoader;

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
     * load_validMap_returnsBoardWithExpectedDimensions
     * load_validMap_setsStartAndExitPositions
     * load_validMap_recordsGoblinSpawnPositions
     * load_validMap_recordsOgreSpawnPositions
     * load_validMap_recordsKeyPositions
     * load_validMap_recordsTrapPositions
     *
     * load_validMap_parsesWallBarrierAndFloorCells
     * load_markerTilesBecomeFloorCells
     * load_walkabilityRulesMatchCellTypes
     *
     * load_rejectsNullPath
     * load_rejectsEmptyMap
     * load_rejectsNonRectangularMap
     * load_rejectsUnknownSymbol
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
        Path mapPath = Path.of("src/test/resources/maps/map1.txt");

        // Act
        BoardLoader.Result result = BoardLoader.load(mapPath);
        Board board = result.getBoard();

        // Assert
        assertEquals(5, board.getWidth());
        assertEquals(4, board.getHeight());
    }
}
