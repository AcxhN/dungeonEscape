package ca.sfu.cmpt276.team7.ui;


import ca.sfu.cmpt276.team7.board.*;
import ca.sfu.cmpt276.team7.Game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * GameWindow opens the window, and starts the panel within it
 */
public class GameWindow {
    public static void start(Game game, Board board) {
	SwingUtilities.invokeLater(() -> {
		JFrame frame = new JFrame("Dungeon Crawl");

		GamePanel panel = new GamePanel(game, board);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	    });
    }
}
