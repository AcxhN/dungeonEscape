package ca.sfu.cmpt276.team7;

public abstract class Reward{
   protected int value;

   public Reward (int value){
      this.value = value;
   }

   public int getValue(){
      return value;
   }

   public abstract void onCollect();

}