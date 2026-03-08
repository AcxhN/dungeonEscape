package ca.sfu.cmpt276.team7;

import ca.sfu.cmpt276.team7.board.*;
import ca.sfu.cmpt276.team7.core.*;
import ca.sfu.cmpt276.team7.enemies.*;
import ca.sfu.cmpt276.team7.reward.*;
import ca.sfu.cmpt276.team7.cells.*;
import ca.sfu.cmpt276.team7.ui.*;


import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Build OK");

	try {
	BoardLoader.Result result = BoardLoader.load(Path.of("./src/main/resources/maps/map1.txt"));

	// Get info from the board
	Board board = result.getBoard();
	List<Position> goblin_spawns = result.getGoblinSpawns();
	List<Position> ogre_spawns = result.getOgreSpawns();
	List<Position> key_spawns = result.getKeyPositions();
	List<Position> trap_spawns = result.getTrapPositions();

	// Make player

	Player player = new Player(board, board.getStartPosition());

	// ==  Make enemies ==
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	// Goblins
	for (Position pos : goblin_spawns) {
	    enemies.add(new Goblin(board, pos));
	}
	// Ogres
	for (Position pos : ogre_spawns) {
	    enemies.add(new Ogre(board, pos));
	}
	// == Set Traps ==
	// == Spawn Keys ==


	
	Game game = new Game(board, player, enemies, 10);
	GamePanel gamepanel = new GamePanel(game, board);
	game.startGame();
	GameWindow.start(game, board);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
