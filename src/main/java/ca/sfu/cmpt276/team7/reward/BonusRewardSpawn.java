package ca.sfu.cmpt276.team7.reward;
import ca.sfu.cmpt276.team7.core.Position;

public class BonusRewardSpawn {
    private final Position position;
    private int lifetime;   // how many ticks it stays on the board

    public BonusRewardSpawn(Position position, int lifetime) {
        this.position = position;
        this.lifetime = lifetime;
    }

    public Position getPosition() {
        return position;
    }

    public int getLifetime() {
        return lifetime;
    }

    /** Decrease lifetime each tick. */
    public void tick() {
        if (lifetime > 0) {
            lifetime--;
        }
    }

    /** Whether the bonus reward should disappear. */
    public boolean isExpired() {
        return lifetime <= 0;
    }
   }





