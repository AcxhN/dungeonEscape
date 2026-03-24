package ca.sfu.cmpt276.team7.unit.ui;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import ca.sfu.cmpt276.team7.Game;
import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.ui.GamePanel;

/**
 * Small Map Padding Display Rule
 * Large Map Viewport Display Rule
 */
public class ViewportRender {
    @Test
    void smallMap_addsPaddingWallsAndUsesMinimumViewport() {
        Board board = UiTestSupport.makeSimpleBoard(5, 5);
        Player player = new Player(board, board.getStartPosition());
        Game game = new Game(board, player, new ArrayList<>(), 0, 0, List.of(), List.of());

        game.startGame();

        GamePanel panel = new GamePanel(game, board);
        panel.setSize(panel.getPreferredSize());

        panel.buildRenderItemsForTest();

        assertEquals(11, panel.getRenderXForTest());
        assertEquals(8, panel.getRenderYForTest());

        assertTrue(panel.getXOffsetForTest() > 0);
        assertTrue(panel.getYOffsetForTest() > 0);

        assertEquals(0, panel.getViewStartXForTest());
        assertEquals(0, panel.getViewStartYForTest());
        assertEquals(board.getWidth(), panel.getViewEndXForTest());
        assertEquals(board.getHeight(), panel.getViewEndYForTest());
    }

    @Test
    void largeMap_usesPlayerCenteredViewport() {}
}
