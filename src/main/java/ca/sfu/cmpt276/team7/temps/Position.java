package main.java.ca.sfu.cmpt276.team7.temps;

/**
 * Temporary for Position.
 * Replace with real implementation when completed
 */
public class Position 
{

    private int x;
    private int y;

    /**
     * Constructs a Position with the given coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Position(int x, int y) 
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate.
     *
     * @return x
     */
    public int getX() 
    {
        return x; 
    }

    /**
     * Returns the y coordinate.
     *
     * @return y
     */
    public int getY() 
    {
        return y; 
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) 
        {
            return true;
        }
        if (!(obj instanceof Position))
        { 
            return false;
        }
        Position other = (Position) obj;
        return this.x == other.x && this.y == other.y;
    }
}