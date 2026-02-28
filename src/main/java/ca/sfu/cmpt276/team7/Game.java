package main.java.ca.sfu.cmpt276.team7;

import java.util.List;

import main.java.ca.sfu.cmpt276.team7.temps.Board;
import main.java.ca.sfu.cmpt276.team7.temps.Enemy;
import main.java.ca.sfu.cmpt276.team7.temps.Player;

public class Game 
{
    private int timeElapsed;
    private boolean isRunning;
    private Board board;
    private Player player;
    private List<Enemy> enemies;

    public Game(Board board, Player player, List<Enemy> enemies)
    {
        this.board = board;
        this.player = player;
        this.enemies = enemies;
        this.timeElapsed = 0;
        this.isRunning = false;
    }

    public void startGame()
    {
        timeElapsed = 0;
        isRunning = true;
        player.setPosition(board.getStartPosition());
    }

    public void updateTick()
    {
        if(!isRunning)
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
            isRunning = false;
            return;
        }

        if(checkLoss())
        {
            handleDeath();
        }
    }

    public void handleInput(int keyCode)
    {
        if(!isRunning)
        {
            return;
        }

        boolean moved = false;
        switch(keyCode)
        {
            case 87: case 38: 
                moved = player.move("UP");
                break;
            case 83: case 40: 
                moved = player.move("DOWN");
                break;
            case 65: case 37:
                moved = player.move("LEFT");
                break;
            case 68: case 39:
                moved = player.move("RIGHT");
                break;
            default:
                return;
        }

        if(moved)
        {
            updateTick();
        }
    }


    public boolean checkWin()
    {
        return player.hasAllKeys() && player.getPosition().equals
        (board.getEndPosition());
    }

    public boolean checkLoss()
    {
        if(player.getTotalScore() <= 0)
        {
            return true;
        }

        for(Enemy enemy : enemies)
        {
            if(enemy.getPosition().equals(player.getPosition()))
            {
                return true;
            }
        }
        return false;
    }

    private void handleDeath()
    {
        player.setPosition(board.getStartPosition());
        for(Enemy enemy : enemies)
        {
            enemy.resetToStart();
        }
    }

    public int getTimeElapsed()
    {
        return timeElapsed;
    }

    public boolean isRunning()
    {
        return isRunning;
    }
}