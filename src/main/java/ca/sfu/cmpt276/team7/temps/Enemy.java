package main.java.ca.sfu.cmpt276.team7.temps;

/**
 * Temporary for Enemy.
 * Replace with real implementation when completed
 */
public abstract class Enemy 
{

    private Position position;

    /**
     * Returns the enemy's current position.
     *
     * @return the current position
     */
    public Position getPosition() 
    {
        return position; 
    }

    /**
     * Sets the enemy's position.
     *
     * @param position the new position
     */
    public void setPosition(Position position) 
    {
        this.position = position; 
    }

    /**
     * Updates the enemy's movement based on the player's current position.
     *
     * @param playerPosition the current position of the player
     */
    public abstract void updateMovement(Position playerPosition);

    /**
     * Resets the enemy to its starting position.
     */
    public void resetToStart() 
    {

    }
}