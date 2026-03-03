package ca.sfu.cmpt276.team7.punishments;

/**
 * a spike trap that deducts score when stepped on 
 */
public final class SpikeTrapPunishment extends Punishment {

    /**
     * @param penaltyValue score deducted when stepped on 
     */
    public SpikeTrapPunishment(int penaltyValue) {
        super(penaltyValue);
    }

}
