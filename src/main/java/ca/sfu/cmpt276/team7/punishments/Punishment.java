package ca.sfu.cmpt276.team7.punishments;


/**
 * represents a punishment triggered when a player steps on a cell
 * example: spike trap 
 */
public abstract class Punishment {
    private final int penaltyValue;

    /**
     * @param penaltyValue score deducted 
     */
    protected Punishment(int penaltyValue) {
        this.penaltyValue = penaltyValue;
    }

    /**
     * @return penalty value deducted from score 
     */
    public int getPenaltyValue() {
        return penaltyValue;
    }

    /**
     * optional method for extra behaviour when triggered 
     */
    public void onTrigger() {
        // default: no extra behaviour 
    }
}
