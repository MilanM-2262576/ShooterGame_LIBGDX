package shooter.game.Entities.Pickups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import shooter.game.Entities.Player;

/**
 * Abstract pickup class for all pickups
 */
public abstract class Pickup {
    protected Body $body;
    protected Texture $texture;
    private boolean $isCollected = false;

    protected static final float $PICKUP_WIDTH = 0.2f;
    protected static final float $PICKUP_HEIGHT = 0.2f;

    /**
     * Pickup constructor
     * @param world (World) the world to create the pickup in
     * @param position (Vector2) the position to create the pickup at
     * @param texturepath (String) the path to the texture of the pickup
     */
    public Pickup(World world, Vector2 position, String texturepath) {
        $texture = new Texture(texturepath);
        $body = createBody(world, position);
    }

    /**
     * Abstract blueprint for applying the pickup to the player
     * @param player (Player) the player to apply the pickup to
     * (e.g. increase health, increase ammo, etc.)
     */
    public abstract void applyToPlayer(Player player);


    /**
     * Creates the body of the pickup in the world at the given position
     * @param world (World) the world to create the body in
     * @param position (Vector2) the position to create the body at
     * @return
     */
    protected Body createBody(World world, Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox($PICKUP_WIDTH / 2, $PICKUP_HEIGHT / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }


    /**
     * Renders the pickup
     * @param batch (SpriteBatch) the batch to render the pickup in
     */
    public void render(SpriteBatch batch) {
        if (!$isCollected) {
            Vector2 pos = $body.getPosition();
            batch.draw($texture,
                pos.x - $PICKUP_WIDTH / 2,
                pos.y - $PICKUP_HEIGHT / 2,
                $PICKUP_WIDTH,
                $PICKUP_HEIGHT);
        }
    }

    /**
     * returns a boolean wether or not the pickup can be collected
     * @return boolean
     */
    public boolean canBeCollected() {
        return !$isCollected;
    }

    /**
     * Collects the pickup: sets the pickup to be collected
     */
    public void collect() {
        $isCollected = true;
    }

    /**
     * Disposes of the ammo resources
     */
    public void dispose() {
        $texture.dispose();
        $body.getWorld().destroyBody($body);
    }

    /**
     * Gets the body of the ammo pickup
     * @return the body
     */
    public Body getBody() {
        return $body;
    }
}
