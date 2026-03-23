package ca.sfu.cmpt276.team7.unit.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import ca.sfu.cmpt276.team7.Game;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.ui.GamePanel;
import ca.sfu.cmpt276.team7.ui.RenderItem;

/**
 * Score and Time Display
 */
public class HudRenderTest {

    private static class FakeClock implements LongSupplier {
        private long nowMs = 0L;

        @Override
        public long getAsLong() {
            return nowMs;
        }

        void advanceMs(long ms) {
            nowMs += ms;
        }
    }

    @Test
    void hud_rendersDisplayedScore() {
        Board board = RenderTestSupport.makeSimpleBoard(10, 10);
        Player player = new Player(board, board.getStartPosition());
        FakeClock clock = new FakeClock();

        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of(), clock);

        game.startGame();
        player.setScore(55);

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();
        List<String> texts = RenderTestSupport.getOnlyTexts(items);

        assertTrue(texts.contains("55"));
    }

    @Test
    void hud_rendersFormattedTime() {
        Board board = RenderTestSupport.makeSimpleBoard(10, 10);
        Player player = new Player(board, board.getStartPosition());
        FakeClock clock = new FakeClock();

        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of(), clock);

        game.startGame();
        clock.advanceMs(63_000);

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        List<RenderItem> items = panel.buildRenderItemsForTest();
        List<String> texts = RenderTestSupport.getOnlyTexts(items);

        assertTrue(texts.contains("1:03"));
    }
}
