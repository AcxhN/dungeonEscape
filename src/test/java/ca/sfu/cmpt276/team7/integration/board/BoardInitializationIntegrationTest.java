package ca.sfu.cmpt276.team7.integration.board;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.board.BoardLoader;
import ca.sfu.cmpt276.team7.core.Position;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for board/map initialization flow
 *
 * Planned coverage:
 * - Valid map -> initialization succeeds
 * - Invalid map -> initialization fails
 * - BoardLoader interacting with filesystem test resources
 */
public class BoardInitializationIntegrationTest {
    /*
     * Planned tests:
     *
     * validMap_initializationFlow_buildsBoardAndMetadata
     * invalidMap_initializationFlow_throwsAndStopsInitialization
     * 
     * BoardTest was isolated in memory
     * BoardLoaderTest focused on one class
     * These two planned tests are broader and test the interaction between:
        * filesystem test resource 
        * Path 
        * BoardLoader
        * Board 
     * So these are intgreation tests because they test the whole flow working together 
     */
    @Test
    void validMap_initializationFlow_buildsBoardAndMetadata() throws IOException {
        // Arrange
        Path mapPath = Path.of("src/test/resources/maps/validWithEntitiesMap.txt");

        // Act
        BoardLoader.Result result = BoardLoader.load(mapPath);
        Board board = result.getBoard();

        // Assert
        assertNotNull(result);
        assertNotNull(board);

        assertEquals(new Position(1, 1), board.getStartPosition());
        assertEquals(new Position(5, 2), board.getEndPosition());

        assertEquals(1, result.getKeyPositions().size());
        assertEquals(1, result.getTrapPositions().size());
        assertEquals(1, result.getGoblinSpawns().size());
        assertEquals(1, result.getOgreSpawns().size());

        assertEquals(new Position(3, 1), result.getKeyPositions().get(0));
        assertEquals(new Position(3, 2), result.getTrapPositions().get(0));
        assertEquals(new Position(4, 1), result.getGoblinSpawns().get(0));
        assertEquals(new Position(4, 2), result.getOgreSpawns().get(0));
    }
}