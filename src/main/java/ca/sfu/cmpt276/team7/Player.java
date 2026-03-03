package ca.sfu.cmpt276.team7;

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
      
   }
   



}
