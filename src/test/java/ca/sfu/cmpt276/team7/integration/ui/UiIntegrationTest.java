package ca.sfu.cmpt276.team7.integration.ui; 

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import ca.sfu.cmpt276.team7.Game;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.reward.Player;
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
    void collectingChest_showsPopup_thenResumeContinuesPlay() {}

    @Test
    void endScreen_replayFlow_returnsToPlayingUi() {}
}
