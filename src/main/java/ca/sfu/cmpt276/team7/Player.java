package ca.sfu.cmpt276.team7;

public class Player extends Character {

    private int totalScore;
    private BonusReward activeBonus;

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

    public void collectReward(Reward reward) {
        if (reward instanceof RegularReward reg) {
            totalScore += reg.getValue();
        } else if (reward instanceof BonusReward bonus) {
            this.activeBonus = bonus;
        }
    }

    public void applyPunishment(Punishment punishment) {
        totalScore -= punishment.getValue();
        if (totalScore < 0) {
            totalScore = 0;
        }
    }

    public void tickBonus() {
        if (activeBonus != null) {
            activeBonus.tick();
            if (activeBonus.getRemainingDuration() <= 0) {
                activeBonus = null;
            }
        }
    }
}
