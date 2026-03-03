package ca.sfu.cmpt276.team7.rewards;

/*
from discord dicussions: reward replaces the cell in the grid. When collected it becomes a floorCell
so RewardCell and PunishmentCell are walkable and after interaction they are replaced with FloorCell
no layering, no overlap, simple!

Cell = location
Reward/Punishment = behvaiour of a Cell 
*/

/**
 * represents a reward that can be collected by the player
 * i.e. key, bonus, chest
 */
public abstract class Reward {
    private final int value; 

    /**
     * @param value score added to player when this reward is collected 
     */
    protected Reward(int value) {
        this.value = value;
    }

    /**
     * @return score value granted when collected 
     */
    public int getValue() {
        return value; 
    }

    /**
     * optional method for additional behvaiour when collected 
     * can be expanded on later 
     */
    public void onCollect() {
        // default: no extra behaviour 
    }
}
