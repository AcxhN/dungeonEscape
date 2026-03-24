package ca.sfu.cmpt276.team7.unit.ui;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ca.sfu.cmpt276.team7.Game;
import ca.sfu.cmpt276.team7.EndReason;
import ca.sfu.cmpt276.team7.ScreenState;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.cells.FloorCell;
import ca.sfu.cmpt276.team7.cells.WallCell;
import ca.sfu.cmpt276.team7.cells.BarrierCell;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.core.Direction;
import ca.sfu.cmpt276.team7.enemies.Enemy;
import ca.sfu.cmpt276.team7.enemies.Goblin;
import ca.sfu.cmpt276.team7.enemies.Ogre;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.reward.PunishmentCell;
import ca.sfu.cmpt276.team7.reward.RewardCell;
import ca.sfu.cmpt276.team7.reward.RegularReward;
import ca.sfu.cmpt276.team7.reward.BonusReward;
import ca.sfu.cmpt276.team7.reward.TrapPunishment;
import ca.sfu.cmpt276.team7.ui.GamePanel;
import ca.sfu.cmpt276.team7.ui.RenderItem;
import ca.sfu.cmpt276.team7.ui.RenderKind;
import ca.sfu.cmpt276.team7.ui.SheetId;

import ca.sfu.cmpt276.team7.unit.ui.UiTestSupport;

/**
 * Screen State Visibility
 * Pause Overlay Visibility
 * Replay Prompt Display
 * Defeat Message Selection
 */
public class ScreenRenderTest {
    @Test
    void startScreen_rendersTitleAndStartPrompt() {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);
        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of());

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();
        List<String> texts = UiTestSupport.getOnlyTexts(items);

        assertTrue(texts.contains("Dungeon Crawl"));
        assertTrue(texts.contains("Press Space to Start"));
    }

    @Test
    void playingScreen_rendersBoardCharactersAndHud() {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);
        Player player = new Player(board, board.getStartPosition());

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Goblin(board, new Position(3, 4)));
        enemies.add(new Ogre(board, new Position(5, 2), Direction.WEST));

        Game game = new Game(board, player, enemies, 0, 0, List.of(), List.of());
        game.startGame();

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();
        List<String> texts = UiTestSupport.getOnlyTexts(items);

        assertTrue(UiTestSupport.containsSprite(items, UiTestSupport.playerSprite));
        assertTrue(UiTestSupport.containsSprite(items, UiTestSupport.goblinSprite));
        assertTrue(UiTestSupport.containsSprite(items, UiTestSupport.ogerSprite));
        assertTrue(texts.contains("0:00"));
        assertTrue(texts.contains("0 / 0"));
        assertTrue(texts.contains("0"));
    }

    @Test
    void playingScreen_rendersBarrierCell() {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);

        Position barrierPos = new Position(2, 2);
        board.setCell(barrierPos.getX(), barrierPos.getY(), new BarrierCell(barrierPos));

        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of());
        game.startGame();

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();

        assertTrue(UiTestSupport.containsSpriteAt(items, UiTestSupport.barrierSprite, 2, 2));
    }

    @Test
    void playingScreen_rendersRegularReward() {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);

        Position keyPos = new Position(1, 1);
        board.setCell(keyPos.getX(), keyPos.getY(), new RewardCell(keyPos, new RegularReward(10)));

        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 1, 0, List.of(keyPos), List.of());
        game.startGame();

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();

        assertTrue(UiTestSupport.containsSpriteAt(items, UiTestSupport.keySprite, 1, 1));
    }

    @Test
    void playingScreen_rendersBonusReward() {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);

        Position chestPos = new Position(1, 1);
        board.setCell(chestPos.getX(), chestPos.getY(), new RewardCell(chestPos, new BonusReward(25, 20)));

        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 0, 1, List.of(), List.of());
        game.startGame();

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();

        assertTrue(UiTestSupport.containsSpriteAt(items, UiTestSupport.chestSprite, 1, 1));
    }

    @Test
    void playingScreen_rendersPunishment() {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);

        Position trapPos = new Position(1, 1);
        board.setCell(trapPos.getX(), trapPos.getY(), new PunishmentCell(trapPos, new TrapPunishment(5)));

        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of(trapPos));
        game.startGame();

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();

        assertTrue(UiTestSupport.containsSpriteAt(items, UiTestSupport.trapSprite, 1, 1));
    }

    @Test
    void pauseScreen_rendersPauseOverlayWhenNoPopup() {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);
        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of());

        game.startGame();
        game.togglePause();

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();
        List<String> texts = UiTestSupport.getOnlyTexts(items);

        assertTrue(texts.contains("Game Paused"));
        assertTrue(texts.contains("Press space to continue"));

        assertFalse(texts.contains("Press space to continue..."));
        assertFalse(texts.contains("* You found a treasure chest!"));
        assertFalse(texts.contains("* An ogre emerges from the darkness."));
        assertFalse(texts.contains("* After some 'convincing', the gnome gives up his key."));
    }

    @Test
    void endScreen_rendersReplayPrompt() {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);
        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of());

        game.startGame();
        player.setScore(-1);
        game.checkLoss();

        assertEquals(ScreenState.END, game.getScreenState());

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();
        List<String> texts = UiTestSupport.getOnlyTexts(items);

        assertTrue(texts.contains("Press Space to Play Again"));
    }


    private void forceEndReason(Game game, Board board, Player player, List<Enemy> enemies, EndReason reason) {
        game.startGame();

        switch (reason) {
            case WIN:
                player.setPosition(board.getEndPosition());
                game.updateTick();
                break;
            case LOSE_BY_TRAP:
                player.setScore(-1);
                game.checkLoss();
                break;
            case LOSE_BY_GOBLIN:
                enemies.add(new Goblin(board, player.getPosition()));
                game.checkLoss();
                break;
            case LOSE_BY_OGRE:
                player.setScore(20);
                enemies.add(new Ogre(board, player.getPosition()));
                game.updateTick();
                break;
        }
    }

    private List<String> renderEndScreenTexts(EndReason reason) {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);
        Player player = new Player(board, board.getStartPosition());

        List<Enemy> enemies = new ArrayList<>();
        Game game = new Game(board, player, enemies, 0, 0, List.of(), List.of());

        forceEndReason(game, board, player, enemies, reason);

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();
        return UiTestSupport.getOnlyTexts(items);
    }

    @Test
    void endScreen_showsWinMessage() {
        List<String> texts = renderEndScreenTexts(EndReason.WIN);

        assertTrue(texts.contains("YOU WIN"));
        assertTrue(texts.contains("You braved the terrors of the dungeon and emerged a rich man"));
    }

    @Test
    void endScreen_showsTrapLossMessage() {
        List<String> texts = renderEndScreenTexts(EndReason.LOSE_BY_TRAP);

        assertTrue(texts.contains("GAME OVER"));
        assertTrue(texts.contains("Lost your footing near a pit of spikes!"));
    }

    @Test
    void endScreen_showsOgreLossMessage() {
        List<String> texts = renderEndScreenTexts(EndReason.LOSE_BY_OGRE);

        assertTrue(texts.contains("GAME OVER"));
        assertTrue(texts.contains("Stuck, mashed, and boiled into a stew!"));
    }

    @Test
    void endScreen_showsGoblinLossMessage() {
        List<String> texts = renderEndScreenTexts(EndReason.LOSE_BY_GOBLIN);

        assertTrue(texts.contains("GAME OVER"));
        assertTrue(texts.contains("Caught by a goblin!"));
    }
}