package ca.sfu.cmpt276.team7.unit.ui;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import ca.sfu.cmpt276.team7.Game;
import ca.sfu.cmpt276.team7.PopupReason;
import ca.sfu.cmpt276.team7.ScreenState;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.reward.RegularReward;
import ca.sfu.cmpt276.team7.reward.RewardCell;
import ca.sfu.cmpt276.team7.ui.GamePanel;
import ca.sfu.cmpt276.team7.ui.RenderItem;

/**
 * Key Popup Visibility and Input Blocking
 * Chest Popup Visibility and Input Blocking
 * Ogre Popup Visibility and Input Blocking
 * Popup Resume Control
 */
public class PopupRenderTest {
    @Test
    void keyPopup_isRenderedAndBlocksMovementUntilResume() {
        Board board = RenderTestSupport.makeSimpleBoard(10, 10);

        Position keyPos = new Position(1, 1);
        board.setCell(keyPos.getX(), keyPos.getY(), new RewardCell(keyPos, new RegularReward(10)));

        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 1, 0, List.of(keyPos), List.of());

        game.startGame();
        game.handleInput(KeyEvent.VK_RIGHT);
        game.updateTick();

        assertEquals(ScreenState.PAUSE, game.getScreenState());
        assertEquals(PopupReason.KEY_COLLECTED, game.getPopupReason());
        assertEquals(new Position(1, 1), player.getPosition());

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.bulidRenderItemsForTest();
        List<String> texts = RenderTestSupport.getOnlyTexts(items);

        assertTrue(texts.contains("* After some 'convincing', the gnome gives up his key."));
        assertTrue(texts.contains("Press space to continue..."));

        game.handleInput(KeyEvent.VK_RIGHT);
        game.updateTick();

        assertEquals(new Position(1, 1), player.getPosition());
        assertEquals(ScreenState.PAUSE, game.getScreenState());

        game.handleInput(KeyEvent.VK_SPACE);

        assertEquals(ScreenState.PLAYING, game.getScreenState());
        assertNull(game.getPopupReason());

        game.handleInput(KeyEvent.VK_RIGHT);
        game.updateTick();

        assertEquals(new Position(2, 1), player.getPosition());
    }

    @Test
    void chestPopup_isRenderedAndBlocksMovementUntilResume() {}

    @Test
    void ogrePopup_isRenderedAndBlocksMovementUntilResume() {}

    @Test
    void popup_resumeControl() {}
}
