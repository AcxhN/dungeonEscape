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

            Board board = result.getBoard();
            List<Position> goblin_spawns = result.getGoblinSpawns();
            List<Position> ogre_spawns = result.getOgreSpawns();
            List<Position> key_spawns = result.getKeyPositions();
            List<Position> trap_spawns = result.getTrapPositions();

            Player player = new Player(board, board.getStartPosition());

            // === Spawn enemies ===
            ArrayList<Enemy> enemies = new ArrayList<>();

            for (Position pos : goblin_spawns) {
                enemies.add(new Goblin(board, pos));
            }

            for (Position pos : ogre_spawns) {
                enemies.add(new Ogre(board, pos, Direction.EAST));
            }

            // === Spawn keys ===
            for (Position pos : key_spawns) {
                Reward reward = new RegularReward(10);
                board.setCell(pos.getX(), pos.getY(), new RewardCell(pos, reward));
            }

            // === Spawn traps ===
            for (Position pos : trap_spawns) {
                Punishment trap = new TrapPunishment(5);
                board.setCell(pos.getX(), pos.getY(), new PunishmentCell(pos, trap));
            }

            Game game = new Game(board, player, enemies, key_spawns.size(), trap_spawns.size()); // no need to hardcode 10 

            game.startGame(); 

            GameWindow.start(game, board); // this already creates a panel, so no need for GamePanel gamepanel = new GamePnale(game, board); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}