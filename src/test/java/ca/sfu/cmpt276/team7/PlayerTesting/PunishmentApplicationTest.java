package ca.sfu.cmpt276.team7.PlayerTesting;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.reward.PunishmentCell;
import ca.sfu.cmpt276.team7.reward.TrapPunishment;
import ca.sfu.cmpt276.team7.Game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests that stepping on a punishment reduces score
 * and removes the punishment from the board.
 */
public class PunishmentApplicationTest {

    private Board makeEmptyBoard(int w, int h) {
        Cell[][] grid = new Cell[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                grid[y][x] = new FloorCell(new Position(x, y));
            }
        }
        return new Board(grid);
    }

    @Test
    void testPunishmentReducesScoreAndDisappears() {
        Board board = makeEmptyBoard(5, 5);

        Position start = new Position(2, 2);
        board.setStartPosition(start);

        // Place a punishment north of the player
        Position punishPos = new Position(2, 1);
        TrapPunishment punishment = new TrapPunishment(10);
        board.setCell(2, 1, new PunishmentCell(punishPos, punishment));

        Player player = new Player(board, start);

        Game game = new Game(
                board,
                player,
                List.of(),      // no enemies
                0,              // no regular rewards
                0,              // no bonus rewards
                List.of(),      // no keys
                List.of()       // no traps list needed
        );

        game.startGame();

        int initialScore = player.getTotalScore();

        // Move player onto punishment
        game.handleInput(87); // W
        game.updateTick();

        // Score should decrease
        assertEquals(initialScore - 10, player.getTotalScore(),
                "Stepping on a punishment should reduce the player's score");

        // Punishment should disappear
        Cell cell = board.getCell(punishPos.getX(), punishPos.getY());
        assertTrue(cell instanceof FloorCell,
                "Punishment tile should become FloorCell after activation");

        // No popup should appear
        assertNull(game.getPopupReason(),
                "Punishment activation should not trigger a popup");
    }
}
