package ca.sfu.cmpt276.team7.PlayerTesting;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.reward.RewardCell;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.reward.RegularReward;
import ca.sfu.cmpt276.team7.Game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RemainingKeysStateTest {

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
    void testRemainingKeysUpdateCorrectly() {
        Board board = makeEmptyBoard(5, 5);

        Position key1 = new Position(2, 1);
        Position key2 = new Position(3, 2);

        board.setCell(2, 1, new RewardCell(key1, new RegularReward(10)));
        board.setCell(3, 2, new RewardCell(key2, new RegularReward(10)));

        Player player = new Player(board, new Position(2, 2));

        Game game = new Game(
                board,
                player,
                java.util.List.of(),
                2,
                0,
                java.util.List.of(key1, key2),
                java.util.List.of()
        );

        board.setStartPosition(new Position(2, 2));
        game.startGame();

        assertEquals(0, game.getCollectedRegularRewards());
        assertEquals(2, game.getTotalRegularRewards());

        // Collect first key
        game.handleInput(87); // W = NORTH
        game.updateTick();

        assertEquals(1, game.getCollectedRegularRewards(),
        "Collected keys should increase after picking up a key");
        game.handleInput(32); // SPACE to resume from popup

        // Move EAST then SOUTH to reach second key
        game.handleInput(68); // D = EAST
        game.updateTick();

        game.handleInput(83); // S = SOUTH
        game.updateTick();

        assertEquals(2, game.getCollectedRegularRewards(),
            "Collected keys should reach total after picking up all keys");

    }
}
