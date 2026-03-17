package ca.sfu.cmpt276.team7;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

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
import ca.sfu.cmpt276.team7.ScreenState;
import ca.sfu.cmpt276.team7.EndReason;
import ca.sfu.cmpt276.team7.PopupReason;



/**
 * Test suite for core game logic
 */
public class coreGameTests 
{
    /**
     * Sets up game application for testing
     * 
     * <p>This method loads the map data from the resource file, initializes all
     * core game objects. Marker positions produced by {@link BoardLoader} 
     * are used to spawn enemies and place special cells such as rewards and punishments
     * 
     * <p>If the map file cannot be loaded, the resulting {@link IOException}
     * is caught and printed to the console
     * 
     * <p>Method is called before every test using {@BeforeEach}
     * 
     */

    private Game game;
    private Player player;
    private Board board;
    private List<Enemy> enemies;

    @BeforeEach
    public void setUp() throws IOException 
    {
        BoardLoader.Result result = BoardLoader.load(Path.of("src/test/resources/maps/valid/map1.txt"));

        board = result.getBoard();
        List<Position> goblin_spawns = result.getGoblinSpawns();
        List<Position> ogre_spawns = result.getOgreSpawns();
        List<Position> key_spawns = result.getKeyPositions();
        List<Position> trap_spawns = result.getTrapPositions();

        player = new Player(board, board.getStartPosition());

        // === Spawn enemies ===
        enemies = new ArrayList<>();

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
        game = new Game(board, player, enemies, key_spawns.size(), 0, key_spawns, trap_spawns);

    }

    //
    @Test
    public void initialScreenStateTest()
    {
        assertEquals(ScreenState.START, game.getScreenState());
    }

    @Test
    public void tickAfterInputTest()
    {
        game.startGame();
        game.handleInput(87);
        game.updateTick();
        assertEquals(1, game.getTimeElapsed());
    }

    @Test
    public void tickAfterInvalidInputTest()
    {
        game.startGame();
        game.handleInput(999);
        assertEquals(0, game.getTimeElapsed());
    }

    @Test
    public void winConditionTest()
    {
        for (Enemy e : enemies) 
        {
            e.setPosition(new Position(1, 1));
        }

        game.startGame();
        player.setPosition(new Position(3, 1)); //left of key 1
        game.handleInput(68);
        game.updateTick();
        assertEquals(1, game.getCollectedRegularRewards(), "K1 not collected");
        assertEquals(PopupReason.KEY_COLLECTED, game.getPopupReason());

        player.setPosition(new Position(4, 3)); //left of key 2
        game.handleInput(68);
        game.updateTick();
        assertEquals(2, game.getCollectedRegularRewards(), "K2 not collected");
        assertEquals(PopupReason.KEY_COLLECTED, game.getPopupReason());

        player.setPosition(board.getEndPosition());
        game.updateTick();

        assertEquals(ScreenState.END, game.getScreenState());
        assertEquals(EndReason.WIN, game.getEndReason());
    }

    @Test
    public void exitWithoutKeysTest()
    {
        game.startGame();
        player.setPosition(new Position(9, 4)); //exit
        game.updateTick();

        assertEquals(ScreenState.PLAYING, game.getScreenState());
    }

    @Test
    public void loseByGoblinTest()
    {
        game.startGame();
        player.setPosition(new Position(6, 2)); //above goblin
        game.updateTick();
        assertEquals(ScreenState.END, game.getScreenState());
        assertEquals(EndReason.LOSE_BY_GOBLIN, game.getEndReason());
    }

    @Test
    public void loseByTrapsTest()
    {
        game.startGame();
        player.setPosition(new Position(6, 2)); //left of trap
        game.handleInput(68);
        game.updateTick();
        assertEquals(ScreenState.END, game.getScreenState());
        assertEquals(EndReason.LOSE_BY_TRAP, game.getEndReason());
    }

    //
    @Test
    public void loseByOgreTest()
    {
        game.startGame();
        player.setPosition(new Position(6, 4));
        game.updateTick();
        assertEquals(ScreenState.END, game.getScreenState());
        assertEquals(EndReason.LOSE_BY_OGRE, game.getEndReason());
    }

    //
    @Test
    public void pauseFreezeTickTest()
    {
        game.startGame();
        game.handleInput(80); //P
        assertEquals(ScreenState.PAUSE, game.getScreenState());
        assertEquals(0, game.getTimeElapsed());
    }

    //
    @Test
    public void pauseFreezeAccumulatedTimeTest() throws InterruptedException
    {
        game.startGame();
        game.handleInput(80);
        Thread.sleep(500);
        game.handleInput(80);
        assertTrue(game.getSeconds() < 1);
    }

    //
    @Test
    public void resumeFromPauseTest()
    {
        game.startGame();
        game.handleInput(80); //P
        game.handleInput(32); //Space
        assertEquals(ScreenState.PLAYING, game.getScreenState());
    }

    //
    @Test
    public void noPopupOnStartTest()
    {
        game.startGame();
        assertNull(game.getPopupReason());
    }

    //
    @Test
    public void restartGameTest()
    {
        game.startGame();
        player.setPosition(new Position(6, 2)); //above goblin
        game.updateTick();
        game.handleInput(32); //Space
        assertEquals(ScreenState.PLAYING, game.getScreenState());
        assertEquals(0, game.getTimeElapsed());
        assertEquals(0, game.getCollectedRegularRewards());
        assertEquals(0, game.getCollectedBonusRewards());
        assertEquals(board.getStartPosition(), player.getPosition());
    }

    //
    @Test
    public void bonusSpawnPositionsNotEmptyTest()
    {
        assertFalse(game.getBonusSpawnPositions().isEmpty());
    }

    //
    @Test
    public void bonusSpawnPositionsNotStartOrEndTest()
    {
        List<Position> spawns = game.getBonusSpawnPositions();
        assertFalse(spawns.contains(board.getStartPosition()));
        assertFalse(spawns.contains(board.getEndPosition()));
    }

    //
    @Test
    public void noTickAfterGameEndTest()
    {
        game.startGame();
        player.setPosition(new Position(6, 2)); //above goblin
        game.updateTick();
        int ticksAtEnd = game.getTimeElapsed();
        game.updateTick();
        assertEquals(ticksAtEnd, game.getTimeElapsed());
    }

}
