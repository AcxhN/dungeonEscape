package ca.sfu.cmpt276.team7.reward;

/**
 * A basic trap punishment that only removes points.
 */
public class TrapPunishment extends Punishment {

    public TrapPunishment(int penaltyValue) {
        super(penaltyValue);
    }

    @Override
    public void onTrigger() {
        // No extra behavior for now
        // Player.applyPunishment() already subtracts the score 
    }
}
