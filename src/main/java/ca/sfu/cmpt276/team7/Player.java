package ca.sfu.cmpt276.team7;

import javax.swing.text.Position;

public class Player extends Character  {
   private int totalScore;
   private BonusReward activateBonus;

   public Player(Board board, Position start){
      super(board);
      this.position = start;
      this.totalScore = 0;
      this.activateBonus = null;

   }
   public int getTotalScore(){
      return totalScore;
   }
   @override 
   public void move(Direction direction){
      Position newPos = null;
      switch (direction){
         case UP:
            newPos = new Position(position.getX(), position.gettY() - 1);
            break;
         case Down:
            newPos = new Position(position.getX(), position.getY() + 1);
            break;
         case LEFT:
            newPos = new Position(position.getX() - 1, position.getY());
            break;
         case RIGHT:
            newPos = new Position(position.getX() + 1, position.getY());
            break; 

      }
      if (!board.isInside(newPos)) return;

      Cell target = board.getCell(newPos);
      if (!canMoveTo(target)) return;
      this.position = newPos;

      if (target instanceof RewardCell rewardcell){
         collectReward(rewardCell.getReward());
         rewardcell.clearReward();
      }

      if (target instanceof PunishmentCell punishmentCell){
         applyPunishment(punishmentCell.getPunishment());
      }
   }
   



}
