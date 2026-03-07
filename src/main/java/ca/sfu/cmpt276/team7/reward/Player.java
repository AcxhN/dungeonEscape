package ca.sfu.cmpt276.team7.reward;

import ca.sfu.cmpt276.team7.board.Board;
import ca.sfu.cmpt276.team7.cells.Cell;
import ca.sfu.cmpt276.team7.core.Direction;
import ca.sfu.cmpt276.team7.core.GameCharacter;
import ca.sfu.cmpt276.team7.core.Position; 

/**
 * Represents the player character in the game.
 * Handles movement, score updates, and interactions with rewards and punishments.
 */

public class Player extends GameCharacter {

    /** Total score accumulated by the player across the game. */
    private int totalScore;
    /** Currently active bonus reward (if any). */
    private BonusReward activeBonus;
    /**
     * Creates a player at the given starting position on the board.
     */
    public Player(Board board, Position start) {
        super(board);
        this.position = start;
        this.totalScore = 0;
        this.activeBonus = null;
    }

    /**
     * Returns the player's current total score.
     *
     * @return total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Attempts to move the player one tile in the given direction.
     * <p>
     * If the destination is out of bounds or not walkable, the player does not move and this
     * method returns {@code null}. If the destination contains a reward, the reward is collected
     * and removed from the cell. If the destination contains a punishment, the punishment is applied.
     *
     * @param direction the direction to move
     * @return the {@link Reward} collected on this move, or {@code null} if none was collected
     */
    public Reward move(Direction direction) {
        int newX = position.getX();
        int newY = position.getY();

        // Compute the next tile coordinates based on movement direction.
        switch (direction) {
            case NORTH:
                newY--;
                break;
            case SOUTH:
                newY++;
                break;
            case WEST:
                newX--;
                break;
            case EAST:
                newX++;
                break;
        }

        // Reject moves that would leave the board.
        if (newX < 0 || newY < 0 || newX >= board.getWidth() || newY >= board.getHeight()) {
            return null;
        }

        Position newPos = new Position(newX, newY);
        
        // Check whether the destination cell is walkable for the player.
        Cell target = board.getCell(newPos.getX(),
				    newPos.getY());
        if (!canMoveto(target)) return null;

        // Commit the move.
        this.position = newPos;

        Reward collected = null;

        // Collect reward if present.
        if (target instanceof RewardCell rewardCell) {
            collected = rewardCell.getReward();
            if (collected != null) {
                collectReward(collected);
                rewardCell.clearReward();
            }
        }

        // Apply punishment if present.
        if (target instanceof PunishmentCell punishmentCell) {
            applyPunishment(punishmentCell.getPunishment());
        }

        return collected;
    }

    /**
     * Returns whether the player can move onto the given cell.
     * <p>
     * The player can move onto any cell marked as walkable.
     *
     * @param cell the target cell
     * @return true if the player can move onto the cell, false otherwise
     */
    @Override
    public boolean canMoveto(Cell cell) {
        return cell.isWalkable();
    }
/**
 * Applies the effects of a collected reward.
 * Regular rewards add points; bonus rewards activate a timed effect. 
*/
    public void collectReward(Reward reward) {
        if (reward instanceof RegularReward reg) {
            totalScore += reg.getValue();
        } else if (reward instanceof BonusReward bonus) {
            this.activeBonus = bonus;
        }
    }
/** 
 * Applies the effects of a punishment by reducing the player's score. 
 */
    public void applyPunishment(Punishment punishment) {
        totalScore -= punishment.getPenaltyValue();
        if (totalScore < 0) {
            totalScore = 0;
        }
    }
    /**
     * Updates the active bonus effect each game tick.
     * Removes the bonus when its duration expires.
     */
    public void tickBonus() {
        if (activeBonus != null) {
            activeBonus.tick();
            if (activeBonus.getDuration() <= 0) {
                activeBonus = null;
            }
        }
    }
}
