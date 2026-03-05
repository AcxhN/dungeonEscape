package ca.sfu.cmpt276.team7;
/**
 * A cell that contains a reward. When the player steps on this cell,
 * the reward is collected and applied to the player's score.
 */
public class RewardCell extends Cell {
   private Reward reward;

 /**
   * create a RewardCell at the given position with the specified reward.
   * @param position the location of this cell on the board
   * @param reward the reward contained in this cell
   */
   public RewardCell(Position position, Reward reward ){
   super(position);
   this.reward = reward;
   }
   public Reward getReward(){
      return reward;
   }
   public boolean isWalkable(){
      return true;
   }
}
