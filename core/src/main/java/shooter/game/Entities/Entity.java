package shooter.game.Entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

/**
 * Entity class
 * Abstract class for all game entities
 * Contains common methods and properties like :
 * <p> Creating Body of the Entity
 *     Updating velocity
 *     Creating animations & flipping if needed
 * </p>
 */
public abstract class Entity {
    protected Body $body;
    protected TextureAtlas $atlas;
    protected Animation<TextureRegion> $idleAnimation;
    protected Animation<TextureRegion> $runAnimation;

    protected float $stateTime;
    protected boolean $isRunning;
    protected boolean $isFlipped;

    // Common entity constants (can be overridden by subclasses)
    protected static final float $MOVE_SPEED = 2f;
    protected static final float $PLAYER_WIDTH = 0.4f;
    protected static final float $PLAYER_HEIGHT = 0.4f;
    protected static final float $COLLISION_RADIUS = 0.05f;

    // Abstract methods to be implemented by subclasses
    public abstract void update(float deltaTime);
    public abstract void render(SpriteBatch batch);
    public abstract void dispose();

    // Common methods

    /**
     * Creates an animation from a texture atlas
     * @param regionName (String)
     * @param frameWidth (int)
     * @param frameHeight (int)
     * @return the created animation object
     */
    protected Animation<TextureRegion> createAnimation(String regionName, int frameWidth, int frameHeight) {
        TextureRegion sheet = $atlas.findRegion(regionName);
        TextureRegion[][] frames = sheet.split(frameWidth, frameHeight);
        TextureRegion[] framesArray = new TextureRegion[frames.length * frames[0].length];
        for (int i = 0; i < frames.length; i++) {
            for (int j = 0; j < frames[i].length; j++) {
                framesArray[i * frames[i].length + j] = frames[i][j];
            }
        }
        Animation<TextureRegion> animation = new Animation<TextureRegion>(0.1f, framesArray);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        return animation;
    }

    /**
     * Updates the entity's velocity
     * @param velocity (Vector2)
     */
    public void updateVelocity(Vector2 velocity) {
        if (velocity.len() > 0) {
            velocity.nor().scl($MOVE_SPEED);
            $isRunning = true;
        } else {
            $isRunning = false;
        }

        $body.setLinearDamping(5.0f);
        $body.setLinearVelocity(velocity);

        // Flip logic may need to be customized in subclasses
        if (velocity.x < 0 && !$isFlipped) {
            flipAnimationFrames(true);
        } else if (velocity.x > 0 && $isFlipped) {
            flipAnimationFrames(false);
        }
    }

    /**
     * Flips the animation frames
     * @param flip (boolean)
     */
    protected void flipAnimationFrames(boolean flip) {
        for (TextureRegion region : $idleAnimation.getKeyFrames()) {
            if (region.isFlipX() != flip) {
                region.flip(true, false);
            }
        }
        for (TextureRegion region : $runAnimation.getKeyFrames()) {
            if (region.isFlipX() != flip) {
                region.flip(true, false);
            }
        }
        $isFlipped = flip;
    }

    /**
     * Creates the entity body
     * @param world (World)
     * @param pos (Vector2)
     * @return the created body
     */
    protected Body createBody(World world, Vector2 pos) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos);
        bodyDef.fixedRotation = true;

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius($COLLISION_RADIUS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.3f;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }


    // Getters
    public Body getBody() {
        return $body;
    }

}
