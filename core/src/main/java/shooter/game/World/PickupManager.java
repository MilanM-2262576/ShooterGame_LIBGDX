package shooter.game.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import shooter.game.Entities.Pickups.Medpack;
import shooter.game.Entities.Pickups.Pickup;
import shooter.game.Entities.Pickups.StandardAmmo;
import shooter.game.Entities.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PickupManager class
 */
public class PickupManager {
    private static final float $MIN_SPAWN_INTERVAL = 5.0f;
    private static final float $MAX_SPAWN_INTERVAL = 10.0f;
    private static final float $SPAWN_MARGIN = 1f;
    private static final float $PICKUP_COLLECTION_DISTANCE = 0.2f;

    private final World $world;
    private final float $mapWidth;
    private final float $mapHeight;
    private final List<Pickup> $pickups;
    private float $timeUntilNextSpawn;

    /**
     * PickupManager constructor
     * @param world
     * @param mapwidth
     * @param mapheigth
     */
    public PickupManager(World world, float mapwidth, float mapheigth) {
        $world = world;
        $mapWidth = mapwidth;
        $mapHeight = mapheigth;
        $pickups = new ArrayList<>();
        resetSpawnTimer();
    }

    /**
     * Resets the spawn timer
     */
    private void resetSpawnTimer() {
        $timeUntilNextSpawn = MathUtils.random($MIN_SPAWN_INTERVAL, $MAX_SPAWN_INTERVAL);
    }

    /**
     * Updates the spawn timer
     * @param deltatime
     */
    private void updateSpawnTimer(float deltatime) {
        $timeUntilNextSpawn -= deltatime;
    }

    /**
     * Updates the pickup manager
     * @param deltatime
     */
    public void update(float deltatime) {
        updateSpawnTimer(deltatime);
        removeCollectedPickups();
        attemptPickupSpawn();
    }

    /**
     * Removes collected pickups
     */
    private void removeCollectedPickups() {
        Iterator<Pickup> iterator = $pickups.iterator();
        while (iterator.hasNext()) {
            Pickup pickup = iterator.next();
            if (!pickup.canBeCollected()) {
                pickup.dispose();
                iterator.remove();
            }
        }
    }

    /**
     * Attempts to spawn a pickup
     */
    private void attemptPickupSpawn() {
        if ($timeUntilNextSpawn <= 0 && $pickups.size() < 2) {
            while ($pickups.size() < 2) {
                spawnRandomPickup();
            }
            resetSpawnTimer();
        }
    }

    /**
     * Spawns a random pickup
     */
    private void spawnRandomPickup() {
        Vector2 position = generateRandomPosition();
        Pickup pickup = createRandomPickup(position);
        $pickups.add(pickup);
    }

    /**
     * Generates a random position
     * @return the generated position
     */
    private Vector2 generateRandomPosition() {
        float x = MathUtils.random($SPAWN_MARGIN, $mapWidth - $SPAWN_MARGIN);
        float y = MathUtils.random($SPAWN_MARGIN, $mapHeight - $SPAWN_MARGIN);
        return new Vector2(x, y);
    }

    /**
     * Creates a random pickup
     * @param position (Vector2)
     * @return the created pickup
     */
    private Pickup createRandomPickup(Vector2 position) {
        return MathUtils.randomBoolean()
            ? new Medpack($world, position)
            : new StandardAmmo($world, position);
    }

    /**
     * Checks if a player is touching a pickup
     * If so, the pickup is collected
     * @param player (Player)
     */
    public void checkPickupCollection(Player player) {
        for (Pickup pickup : $pickups) {
            if (pickup.canBeCollected() && isPlayerTouchingPickup(player, pickup)) {
                pickup.applyToPlayer(player);
                pickup.collect();
            }
        }
    }

    /**
     * Checks if a player is touching a pickup
     * @param player (Player)
     * @param pickup (Pickup)
     * @return boolean
     */
    private boolean isPlayerTouchingPickup(Player player, Pickup pickup) {
        return player.getBody().getPosition()
            .dst(pickup.getBody().getPosition()) < $PICKUP_COLLECTION_DISTANCE;
    }

    /**
     * Renders the pickups
     * @param batch (SpriteBatch)
     */
    public void render(SpriteBatch batch) {
        for (Pickup pickup : $pickups) {
            pickup.render(batch);
        }
    }

    /**
     * Disposes the pickups
     */
    public void dispose() {
        for (Pickup pickup : $pickups) {
            pickup.dispose();
        }
    }

}
