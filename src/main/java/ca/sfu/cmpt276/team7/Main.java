package ca.sfu.cmpt276.team7;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List; 

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.board.BoardLoader;
import ca.sfu.cmpt276.team7.core.Direction;
import ca.sfu.cmpt276.team7.core.Position;
import ca.sfu.cmpt276.team7.enemies.Enemy;
import ca.sfu.cmpt276.team7.enemies.Goblin;
import ca.sfu.cmpt276.team7.enemies.Ogre;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.reward.Punishment;
import ca.sfu.cmpt276.team7.reward.PunishmentCell;
import ca.sfu.cmpt276.team7.reward.RegularReward;
import ca.sfu.cmpt276.team7.reward.Reward;
import ca.sfu.cmpt276.team7.reward.RewardCell;
import ca.sfu.cmpt276.team7.reward.TrapPunishment;
import ca.sfu.cmpt276.team7.ui.GameWindow;

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

            // Bonus rewards are runtime-spawned, so initial total is 0.
            Game game = new Game(board, player, enemies, key_spawns.size(), 0, key_spawns, trap_spawns);

            GameWindow.start(game, board);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}