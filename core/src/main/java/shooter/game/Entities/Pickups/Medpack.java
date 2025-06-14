package shooter.game.Entities.Pickups;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import shooter.game.Entities.Player;

/**
 * Medpack pickup extends Pickup
 * @see Pickup
 */
public class Medpack extends Pickup {

    /**
     * Pickup constructor
     * @param world       (World) the world to create the pickup in
     * @param position    (Vector2) the position to create the pickup at
     */
    public Medpack(World world, Vector2 position) {
        super(world, position, "assets/SpriteTextures/pickups/health-pickup.png");
    }

    /**
     * Applies the pickup to the player
     * @param player (Player) the player to apply the pickup to
     */
    @Override
    public void applyToPlayer(Player player) {
        player.Heal();
    }
}
