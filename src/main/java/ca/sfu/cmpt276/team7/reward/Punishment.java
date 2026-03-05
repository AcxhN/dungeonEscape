package ca.sfu.cmpt276.team7;
/**
 * Base class for all punishment types in the game.
 * Each punishment has a penalty value and defines behavior when triggered.
 */
public abstract class Punishment{
   protected int penaltyValue;
   /** 
    * Creates a punishment with the given penalty value. 
    * @param penaltyValue the number of points this punishment removes
   */

   public Punishment(int penaltyValue){
      this.penaltyValue = penaltyValue;
   }
   public int getPenaltyValue(){
      return penaltyValue;
   }
   /**
     * Defines what happens when the player triggers this punishment.
     * Player.applyPunishment() handles score reduction; subclasses may add extra effects.
     */
   
   public abstract void onTrigger();

}

