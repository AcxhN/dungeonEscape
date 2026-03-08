package ca.sfu.cmpt276.team7;

import java.util.ArrayList;
import java.util.List;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.core.Direction;
import ca.sfu.cmpt276.team7.core.GameCharacter;
import ca.sfu.cmpt276.team7.enemies.Enemy;
import ca.sfu.cmpt276.team7.enemies.Goblin;
import ca.sfu.cmpt276.team7.reward.Player;
import ca.sfu.cmpt276.team7.reward.RegularReward;
import ca.sfu.cmpt276.team7.reward.Reward;
import main.java.ca.sfu.cmpt276.team7.EndReason;
import main.java.ca.sfu.cmpt276.team7.PopupReason;
import main.java.ca.sfu.cmpt276.team7.ScreenState; 

/**
 * Cor controller for the game
 * 
 * <p>Manages the tick game loop, translates player keyboard input
 * into movement commands, check for win and loss conditions every tick,
 * and handles player respawn after death.
 * 
 * <p>Collaborates withgit 
 * <ul>
 *  <li>{@link Board} - supplies start/end positions</li>
 *  <li>{@link Player} - moves and tracks player state</li>
 *  <li>{@link Enemy} - updated each tick for movement</li>
 * </ul>
 */
public class Game 
{
    /**
     * number of ticks since game start
     * incremented once per call to {@link #updateTick()}
     */
    private int timeElapsed;

    /** Total number of regular rewards on the board. */
    private int totalRegularRewards;

    /** Number of regular rewards the player has collected so far. */
    private int collectedRegularRewards;

    /** The current screen state of the game. */
    private ScreenState screenState;

    /** The reason the game ended, set when screenState becomes END. */
    private EndReason endReason;

    /** The reason a popup is currently being shown. */
    private PopupReason popupReason;

    /**Dungeon board, provides layout and positions */
    private Board board;

    /**Player character controlled with keyboard input */
    private Player player;

    /**All enemy instances present on board */
    private List<Enemy> enemies;

    /**Elasped time from start */
    private long startTime;

    /**Total time elsaped */
    private long totalTime;

    /**
     * Contructs new game with given board, player and enemies
     * 
     * @param board
     * @param player
     * @param enemies
     * @param totalRegularRewards
     */
    public Game(Board board, Player player, List<Enemy> enemies, int totalRegularRewards)
    {
        this.board = board;
        this.player = player;
        this.enemies = enemies;
        this.timeElapsed = 0;
        this.totalRegularRewards = totalRegularRewards;
        this.collectedRegularRewards = 0;
        this.screenState = ScreenState.START;
        this.endReason = null;
        this.popupReason = null;
    }

    /**
     * Starts game by reseting tick counter and placing player at
     * designated starting position
     */
    public void startGame()
    {
        startTime = System.currentTimeMillis();
        totalTime = 0;

        timeElapsed = 0;
        screenState = ScreenState.PLAYING;
        endReason = null;
        popupReason = null;
        player.setPosition(board.getStartPosition());
    }

    /**
     * Advances game by one tick
     * 
     * <p>Each tick performs the following:
     * <ol>
     *  <li>Increments {@code timeElapsed}</li>
     *  <li>Calls {@code updateMovement()}</li>
     *  <li>Checks win condition using {@link #checkWin()}</li>
     *  <li>Checks loss condition using {@link #checkLoss()}</li>
     * </ol>
     * 
     * <p>This method is a no-op when {@code screenState} is not {@link ScreenState#PLAYING}
     */
    public void updateTick()
    {
        if(screenState != ScreenState.PLAYING)
        {
            return;
        }
        timeElapsed++;

        for(Enemy enemy : enemies)
        {
            enemy.updateMovement(player.getPosition());
        }

        if(checkWin())
        {
            screenState = ScreenState.END;
            endReason = EndReason.WIN;
            return;
        }

        if(checkLoss())
        {
            screenState = ScreenState.END;
            return;
        }
    }

    /**
     * Getter for the list of characters
     * @return A list of characters
     */
    public List<GameCharacter> getCharacters() {
	List<GameCharacter> char_list = new ArrayList<>(enemies);
	char_list.add(player);
	return char_list;
    }

    /**
     * Translates keyboard keycode into a direction, attempts to move the player,
     * updates reward collection state, and then advances the game by one tick.
     * 
     * <p>Supported key codes:
     * <ul>
     *  <li>87(W) / 38 (↑) - move NORTH</li>
     *  <li>83(S) / 40 (↓) - move SOUTH</li>
     *  <li>65(A) / 37 (←) - move WEST</li>
     *  <li>68(D) / 39 (→) -  move EAST</li>
     * </ul>
     * 
     * <p>This method is a no-op when {@code screenState} is not {@link ScreenState#PLAYING}.
     * 
     * @param keyCode integer key code from a {@code KeyEvent}
     */
    public void handleInput(int keyCode)
    {
        if(screenState != ScreenState.PLAYING)
        {
            return;
        }

        Reward collectedReward = null;

        switch(keyCode)
        {
            case 87: case 38: 
                collectedReward = player.move(Direction.NORTH);
                break;
            case 83: case 40: 
                collectedReward = player.move(Direction.SOUTH);
                break;
            case 65: case 37:
                collectedReward = player.move(Direction.WEST);
                break;
            case 68: case 39:
                collectedReward = player.move(Direction.EAST);
                break;
            default:
                return;
        }

        if (collectedReward instanceof RegularReward) {
            collectedRegularRewards++;
        }
        updateTick();
        
    }

    /**
     * Checks whether player has met win condition
     * 
     * <p>Player wins when all keys have been collected and player
     * is standing on exit cell
     * 
     * @return {@code true} if conditions are met
     */
    public boolean checkWin()
    {
         return collectedRegularRewards >= totalRegularRewards
            && player.getPosition().equals(board.getEndPosition());
    }

    /**
     * Checks whether player has met loss condition
     * <p>Player loses if:
     * <ul>
     *  <li>Total score has dropped to 0 or less, or</li>
     *  <li>Enemy occupies same cell as player</li>
     * </ul>
     * 
     * Sets {@code screenState} to {@link ScreenState#END} and assigns
     * appropriate {@code endReason} if loss
     * 
     * @return {@code true} if conditions met
     */
    public boolean checkLoss()
    {
        if(player.getTotalScore() < 0)
        {
            screenState = ScreenState.END;
            endReason = EndReason.LOSE_BY_TRAP;
            return true;
        }

        for(Enemy enemy : enemies)
        {
            if(enemy.getPosition().equals(player.getPosition()))
            {
                screenState = ScreenState.END;
                endReason = (enemy instanceof Goblin) ? EndReason.LOSE_BY_GOBLIN : EndReason.LOSE_BY_OGRE;
                return true;
            }
        }

        return false;
    }


    /**
     * Manages time depending if game is pouse or currently in playing state
     */
    public void togglePause()
    {
        if(screenState == ScreenState.PLAYING)
        {
            totalTime += System.currentTimeMillis() - startTime;
            screenState = ScreenState.PAUSE;
        }
        else if(screenState == ScreenState.PAUSE)
        {
            startTime = System.currentTimeMillis();
            screenState = ScreenState.PLAYING;
        }
    }

    /**
     * Returns number of seconds game has been running, excludes
     * time when paused
     * @return seconds game has been running
     */
    public int getSeconds()
    {
        if(screenState == ScreenState.PLAYING)
        {
            return (int) ((totalTime + System.currentTimeMillis() - startTime) / 1000);
        }
        return (int) (totalTime / 1000);
    }

    /**
     * Returns the player's current total score.
     *
     * @return the player's total score
     */
    public int getTotalScore()
    {
        return player.getTotalScore();
    }

    /**
    * Returns the total number of regular rewards on the board
    *
    * @return totalRegularRewards
    */
    public int getTotalRegularRewards()
    {
        return totalRegularRewards;
    }

    /**
    * Returns the number of regular rewards the player has collected
    *
    * @return collectedRegularRewards
    */
    public int getCollectedRegularRewards()
    {
        return collectedRegularRewards;
    }

    /**
    * Returns the current screen state of the game
    *
    * @return screenState
    */
    public ScreenState getScreenState()
    {
        return screenState;
    }

    /**
    * Returns the reason the game ended
    *
    * @return endReason, or {@code null} if the game has not ended
    */
    public EndReason getEndReason()
    {
        return endReason;
    }

    /**
    * Returns the reason a popup is currently being shown
    *
    * @return popupReason, or {@code null} if no popup is active
    */
    public PopupReason getPopupReason()
    {
        return popupReason;
    }

    /**
     * Returns the number of ticks elapsed since the game started.
     *
     * @return the current tick count
     */
    public int getTimeElapsed()
    {
        return timeElapsed;
    }

    /**
     * Returns the player instance for this game.
     *
     * @return the current player
     */
    public Player getPlayer() {
        return player;
    }

}
