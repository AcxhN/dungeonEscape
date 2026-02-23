package main.java.ca.sfu.cmpt276.team7.temps;

/**
 * Temporary for Player
 * Replace with real implementation when complete
 */
public class Player 
{

    private Position position;
    private int totalScore;

    /**
     * Returns the player's current position.
     *
     * @return the current position
     */
    public Position getPosition() 
    { 
        return position; 
    }

    /**
     * Sets the player's position.
     *
     * @param position the new position
     */
    public void setPosition(Position position) 
    {
        this.position = position; 
    }

    /**
     * Returns the player's total score.
     *
     * @return the total score
     */
    public int getTotalScore() 
    {
        return totalScore; 
    }

    /**
     * Returns whether the player has collected all keys.
     *
     * @return true if all keys are collected
     */
    public boolean hasAllKeys() 
    { 
        return false; 
    }

    /**
     * Attempts to move the player in the given direction.
     *
     * @param direction the direction to move ("UP", "DOWN", "LEFT", "RIGHT")
     * @return true if the move was successful
     */
    public boolean move(String direction) 
    {
        return false; 
    }
}