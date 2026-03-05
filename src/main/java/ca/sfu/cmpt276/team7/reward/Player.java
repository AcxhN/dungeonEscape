package ca.sfu.cmpt276.team7.reward;

import ca.sfu.cmpt276.team7.board.Board; 
import ca.sfu.cmpt276.team7.core.GameCharacter; 

import ca.sfu.cmpt276.team7.Position;

/**
 * Represents the player character in the game.
 * Handles movement, score updates, and interactions with rewards and punishments.
 */

public class Player extends GameCharacter {

    private int totalScore;
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

    public int getTotalScore() {
        return totalScore;
    }

    @Override
    public void move(Direction direction) {
        Position newPos = null;

        switch (direction) {
            case UP:
                newPos = new Position(position.getX(), position.getY() - 1);
                break;
            case DOWN:
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

        if (target instanceof RewardCell rewardCell) {
            collectReward(rewardCell.getReward());
            rewardCell.clearReward();
        }

        if (target instanceof PunishmentCell punishmentCell) {
            applyPunishment(punishmentCell.getPunishment());
        }
    }

    @Override
    public boolean canMoveTo(Cell cell) {
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
        totalScore -= punishment.getValue();
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
            if (activeBonus.getRemainingDuration() <= 0) {
                activeBonus = null;
            }
        }
    }
}
