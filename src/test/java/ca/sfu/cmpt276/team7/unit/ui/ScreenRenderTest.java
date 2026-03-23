package ca.sfu.cmpt276.team7.unit.ui;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import ca.sfu.cmpt276.team7.Game;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.cells.WallCell;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.ui.GamePanel;
import ca.sfu.cmpt276.team7.ui.RenderItem;
import ca.sfu.cmpt276.team7.ui.RenderKind;

/**
 * Screen State Visibility
 * Pause Overlay Visibility
 * Replay Prompt Display
 * Defeat Message Selection
 */
public class ScreenRenderTest {

    private Board makeSimpleBoard(int width, int height) {
        Cell[][] grid = new Cell[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                    grid[y][x] = new WallCell(new Position(x, y));
                } else {
                    grid[y][x] = new FloorCell(new Position(x, y));
                }
            }
        }

        Board board = new Board(grid);
        board.setStartPosition(new Position(0, 0));
        board.setEndPosition(new Position(width - 1, height - 1));

        return board;
    }

    private List<String> getOnlyTexts(List<RenderItem> items) {
        List<String> texts = new ArrayList<>();

        for (RenderItem item : items) {
            if (item.getKind() == RenderKind.TEXT) {
                texts.add(item.getText());
            }
        }

        return texts;
    }


    @Test
    void startScreen_rendersTitleAndStartPrompt() {
        Board board = makeSimpleBoard(10, 10);
        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of());

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.bulidRenderItemsForTest();
        List<String> texts = getOnlyTexts(items);

        assertTrue(texts.contains("Dungeon Crawl"));
        assertTrue(texts.contains("Press Space to Start"));
    }

    @Test
    void playingScreen_rendersBoardCharactersAndHud() {}

    @Test
    void pauseScreen_rendersPauseOverlayWhenNoPopup() {}

    @Test
    void endScreen_rendersReplayPrompt() {}

    @Test
    void endScreen_showsCorrectMessageForEachEndReason() {}
}