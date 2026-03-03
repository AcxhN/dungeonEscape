package ca.sfu.cmpt276.team7.rewards;

/**
 * a simple reward that only grants score (score will likely be represented as coins in the UI)
 * example: a treasure chest 
 */
public final class RegularReward extends Reward {
    
    /**
     * @param value score added to player when this reward is collected 
     */
    public RegularReward(int value) {
        super(value); 
    }
}
