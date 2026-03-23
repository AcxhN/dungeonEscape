package ca.sfu.cmpt276.team7.PlayerTesting;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.reward.PunishmentCell;
import ca.sfu.cmpt276.team7.reward.TrapPunishment;
import ca.sfu.cmpt276.team7.Game;
import ca.sfu.cmpt276.team7.EndReason;
import ca.sfu.cmpt276.team7.ScreenState;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests that stepping on a punishment that reduces score below zero
 * ends the game with LOSE_BY_TRAP.
 */
public class PunishmentResultTest {

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
    void testPunishmentCausesGameOverWhenScoreBelowZero() {
        Board board = makeEmptyBoard(5, 5);

        Position start = new Position(2, 2);
        board.setStartPosition(start);

        // Place a punishment north of the player
        Position punishPos = new Position(2, 1);
        TrapPunishment punishment = new TrapPunishment(999); // guaranteed to drop score below 0
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

        // Move player onto punishment
        game.handleInput(87); // W
        game.updateTick();

        // Game must end
        assertEquals(ScreenState.END, game.getScreenState(),
                "Game should enter END state when punishment reduces score below zero");

        // End reason must be LOSE_BY_TRAP
        assertEquals(EndReason.LOSE_BY_TRAP, game.getEndReason(),
                "End reason should be LOSE_BY_TRAP when punishment kills the player");
    }
}
