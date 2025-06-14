package shooter.game.Entities.Pickups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import shooter.game.Entities.Player;

/**
 * Standard ammo pickup extends Pickup
 * @see Pickup
 */
public class StandardAmmo extends Pickup {

    /**
     * Standard ammo pickup constructor
     * @param world (World) the world to create the pickup in
     * @param position (Vector2) the position to create the pickup at
     */
    public StandardAmmo(World world, Vector2 position) {
        super(world, position, "assets/SpriteTextures/pickups/ammo-pickup.png");
    }

    /**
     * Applies the pickup to the player
     * @param player (Player) the player to apply the pickup to
     */
    @Override
    public void applyToPlayer(Player player) {
        player.reloadAmmo();
    }
}
