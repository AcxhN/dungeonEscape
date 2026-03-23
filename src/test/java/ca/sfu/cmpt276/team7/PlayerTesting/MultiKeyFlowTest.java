package ca.sfu.cmpt276.team7.PlayerTesting;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.reward.RewardCell;
import ca.sfu.cmpt276.team7.reward.RegularReward;
import ca.sfu.cmpt276.team7.Game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MultiKeyFlowTest {

    private Board makeBoard() {
        Cell[][] grid = new Cell[5][5];
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                grid[y][x] = new FloorCell(new Position(x, y));
            }
        }
        return new Board(grid);
    }

    @Test
    void testMultiKeyCollectionFlow() {
        Board board = makeBoard();

        Position start = new Position(2, 2);
        board.setStartPosition(start);

        Position key1 = new Position(3, 2);
        Position key2 = new Position(4, 2);

        board.setCell(3, 2, new RewardCell(key1, new RegularReward(0)));
        board.setCell(4, 2, new RewardCell(key2, new RegularReward(0)));

        Player player = new Player(board, start);

        Game game = new Game(
                board,
                player,
                List.of(),
                2,      // totalRegularRewards = 2 keys
                0,
                List.of(key1, key2),
                List.of()
        );

        game.startGame();

        // Step 1: Collect key 1
        game.handleInput(68); // D
        game.updateTick();
        assertEquals(1, game.getCollectedRegularRewards());

        // MUST resume popup here
        game.handleInput(32); // SPACE

        // Step 2: Collect key 2
        game.handleInput(68); // D
        game.updateTick();
        assertEquals(2, game.getCollectedRegularRewards());

        assertTrue(board.getCell(3, 2) instanceof FloorCell);
        assertTrue(board.getCell(4, 2) instanceof FloorCell);
    }
}
