package ca.sfu.cmpt276.team7.integration.ui; 

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import ca.sfu.cmpt276.team7.Game;
import ca.sfu.cmpt276.team7.PopupReason;
import ca.sfu.cmpt276.team7.ScreenState;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.reward.BonusReward;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.reward.RewardCell;
import ca.sfu.cmpt276.team7.ui.GamePanel;
import ca.sfu.cmpt276.team7.ui.RenderItem;
import ca.sfu.cmpt276.team7.unit.ui.UiTestSupport;

/**
 * Score / Time -> UI Update Flow
 * Chest Popup Gameplay Flow
 * End Screen Message -> Replay UI Flow
*/
public class UiIntegrationTest {
    @Test
    void scoreAndTimeChanges_areReflectedInHud() {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);
        Player player = new Player(board, board.getStartPosition());
        UiTestSupport.FakeClock clock = new UiTestSupport.FakeClock();

        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of(), clock);

        game.startGame();
        player.setScore(55);
        clock.advanceMs(63_000);

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();
        List<String> texts = UiTestSupport.getOnlyTexts(items);

        assertTrue(texts.contains("55"));
        assertTrue(texts.contains("1:03"));
        assertTrue(texts.contains("0 / 0"));
    }

    @Test
    void collectingChest_showsPopup_thenResumeContinuesPlay() {
        Board board = UiTestSupport.makeSimpleBoard(11, 10);

        Position chestPos = new Position(1, 1);
        board.setCell(chestPos.getX(), chestPos.getY(), new RewardCell(chestPos, new BonusReward(25, 20)));

        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 0, 1, List.of(), List.of());

        game.startGame();
        game.handleInput(KeyEvent.VK_RIGHT);
        game.updateTick();

        assertEquals(ScreenState.PAUSE, game.getScreenState());
        assertEquals(PopupReason.BONUS_COLLECTED, game.getPopupReason());
        assertEquals(new Position(1, 1), player.getPosition());
        assertEquals(1, game.getCollectedBonusRewards());

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> popupItems = panel.buildRenderItemsForTest();
        List<String> popupTexts = UiTestSupport.getOnlyTexts(popupItems);

        assertTrue(popupTexts.contains("* You found a treasure chest!"));
        assertTrue(popupTexts.contains("Press space to continue..."));

        game.handleInput(KeyEvent.VK_SPACE);

        assertEquals(ScreenState.PLAYING, game.getScreenState());
        assertNull(game.getPopupReason());

        game.handleInput(KeyEvent.VK_RIGHT);
        game.updateTick();

        assertEquals(new Position(2, 1), player.getPosition());

        List<RenderItem> resumedItems = panel.buildRenderItemsForTest();
        List<String> resumedTexts = UiTestSupport.getOnlyTexts(resumedItems);

        assertFalse(resumedTexts.contains("* You found a treasure chest!"));
        assertFalse(resumedTexts.contains("Press space to continue..."));
    }

    @Test
    void endScreen_replayFlow_returnsToPlayingUi() {
        
    }
}
