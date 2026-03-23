package ca.sfu.cmpt276.team7.unit.ui;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ca.sfu.cmpt276.team7.Game;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.cells.WallCell;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.core.Direction;
import ca.sfu.cmpt276.team7.enemies.Enemy;
import ca.sfu.cmpt276.team7.enemies.Goblin;
import ca.sfu.cmpt276.team7.enemies.Ogre;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.ui.GamePanel;
import ca.sfu.cmpt276.team7.ui.RenderItem;
import ca.sfu.cmpt276.team7.ui.RenderKind;
import ca.sfu.cmpt276.team7.ui.SheetId;

/**
 * Screen State Visibility
 * Pause Overlay Visibility
 * Replay Prompt Display
 * Defeat Message Selection
 */
public class ScreenRenderTest {

    record SpriteSpec(SheetId sheetId, int srcX, int srcY) {}

    private final int gameSrcSize = 64;
    private final int screenSrcSize = 200;
    private final int srcPadding = 5;

    private int srcSize(int order, int srcSize) {
        return ((srcSize + (srcPadding * 2)) * order) + srcPadding;
    }

    private final SpriteSpec playerSprite = new SpriteSpec(SheetId.GAME_ATLAS, srcSize(7, gameSrcSize), srcPadding);
    private final SpriteSpec goblinSprite = new SpriteSpec(SheetId.GAME_ATLAS, srcSize(6, gameSrcSize), srcPadding);
    private final SpriteSpec ogerSprite = new SpriteSpec(SheetId.GAME_ATLAS, srcSize(5, gameSrcSize), srcPadding);

    private boolean containsSprite(List<RenderItem> items, SpriteSpec sprite) {
        for (RenderItem item : items) {
            if (item.getKind() == RenderKind.SPRITE && item.getSheetId() == sprite.sheetId()
                && item.getSrcX() == sprite.srcX() && item.getSrcY() == sprite.srcY()) {
                return true;
            }
        }
        return false;
    }


    private Board makeSimpleBoard(int width, int height) {
        Cell[][] grid = new Cell[height][width];

        Position start = new Position(0, 1);
        Position end = new Position(width - 1, height - 1);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Position pos = new Position(x, y);

                if ((x == 0 || x == width - 1 || y == 0 || y == height - 1)
                    && !pos.equals(start) && !pos.equals(end)) {
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
    void playingScreen_rendersBoardCharactersAndHud() {
        Board board = makeSimpleBoard(10, 10);
        Player player = new Player(board, board.getStartPosition());

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Goblin(board, new Position(3, 4)));
        enemies.add(new Ogre(board, new Position(5, 2), Direction.WEST));

        Game game = new Game(board, player, enemies, 0, 0, List.of(), List.of());
        game.startGame();

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.bulidRenderItemsForTest();
        List<String> texts = getOnlyTexts(items);

        assertTrue(containsSprite(items, playerSprite));
        assertTrue(containsSprite(items, goblinSprite));
        assertTrue(containsSprite(items, ogerSprite));
        assertTrue(texts.contains("0:00"));
        assertTrue(texts.contains("0 / 0"));
        assertTrue(texts.contains("0"));
    }

    @Test
    void pauseScreen_rendersPauseOverlayWhenNoPopup() {
        Board board = makeSimpleBoard(10, 10);
        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of());

        game.startGame();
        game.togglePause();

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.bulidRenderItemsForTest();
        List<String> texts = getOnlyTexts(items);

        assertTrue(texts.contains("Game Paused"));
        assertTrue(texts.contains("Press space to continue"));

        assertFalse(texts.contains("Press space to continue..."));
        assertFalse(texts.contains("* You found a treasure chest!"));
        assertFalse(texts.contains("* An ogre emerges from the darkness."));
        assertFalse(texts.contains("* After some 'convincing', the gnome gives up his key."));
    }

    @Test
    void endScreen_rendersReplayPrompt() {}

    @Test
    void endScreen_showsCorrectMessageForEachEndReason() {}
}