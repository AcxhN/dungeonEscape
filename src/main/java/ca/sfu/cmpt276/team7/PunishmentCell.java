package ca.sfu.cmpt276.team7;

/**
 * A cell that contains a punishment. When the player steps on this cell,
 * the punishment is triggered and applied to the player's score.
 */
public class PunishmentCell extends Cell{
   private Punishment punishment;
   /**
    * create a PunishmentCel at the given position with the specified punishment.
    * @param position the location of the cell on the board 
    * @param punishment the punishment contained in this cell
    */
   public PunishmentCell(Position position, Punishment punishment){
      super(position);
      this.punishment = punishment;
   }
   /**
    * @return the punishment i stored in this cell 
    */
   public Punishment getPunishment(){
      return punishment;
   }

   public boolean isWalkable(){
      return true;
   }
}