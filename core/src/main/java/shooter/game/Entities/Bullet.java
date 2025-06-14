package shooter.game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Bullet class
 */
public class Bullet {
    private Body $body;
    private Texture $texture;
    private static final float $BULLET_SPEED = 5f;
    private static final float $BULLET_WIDTH = 0.03f;
    private static final float $BULLET_HEIGHT = 0.03f;
    private static final float $MAX_RANGE = 1f;
    private Vector2 $startPosition;
    private float $distanceTravelled;
    private boolean $isDestroyed;

    /**
     * Bullet constructor
     * @param world (World)
     * @param position (Vector2)
     * @param direction (Vector2)
     */
    public Bullet(World world, Vector2 position, Vector2 direction) {
        $texture = new Texture("assets/BlackSprite/SpongeBullet.png");
        $body = createBody(world, position, direction);
        $startPosition = new Vector2(position);
        $distanceTravelled = 0f;
        $isDestroyed = false;
    }

    /**
     * Updates the bullet
     */
    public void update() {
        Vector2 currentPos = $body.getPosition();
        $distanceTravelled = $startPosition.dst(currentPos);
    }

    /**
     * Checks if the bullet should be removed
     * @return true if the bullet should be removed, false otherwise
     */
    public boolean shouldRemove() {
        return $distanceTravelled >= $MAX_RANGE;
    }

    /**
     * Creates the bullet body
     * @param world (World)
     * @param position (Vector2)
     * @param direction  (Vector2)
     * @return the created body
     */
    private Body createBody(World world, Vector2 position, Vector2 direction) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);
        bodyDef.bullet = true;

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius($BULLET_WIDTH / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef);
        shape.dispose();

        direction.nor().scl($BULLET_SPEED);
        body.setLinearVelocity(direction);

        return body;
    }

    /**
     * Renders the bullet
     * @param batch (SpriteBatch)
     */
    public void render(SpriteBatch batch) {
        Vector2 position = $body.getPosition();
        batch.draw($texture,
            position.x - $BULLET_WIDTH / 2,
            position.y - $BULLET_HEIGHT / 2,
            $BULLET_WIDTH, $BULLET_HEIGHT);
    }

    public boolean isDestroyed() {
        return $isDestroyed;
    }

    public void destroy() {
        $isDestroyed = true;
        //dispose();  ==> zorgt voor crash
    }

    public boolean collidesWith(Enemy enemy) {
        return $body.getPosition().dst(enemy.getBody().getPosition()) < 0.1f;
    }

    /**
     * Disposes the bullet
     */
    public void dispose() {
        $texture.dispose();
        $body.getWorld().destroyBody($body);
    }

    /**
     * Returns the body of the bullet
     * @return the body of the bullet (Body)
     */
    public Body getBody() {
        return $body;
    }
}
