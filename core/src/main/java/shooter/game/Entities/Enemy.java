package shooter.game.Entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import shooter.game.GameEssentials.SoundManager;

/**
 * Enemy class Extends Entity
 * Represents the enemy character
 *
 * @see Entity
 */
public class Enemy extends Entity {
    private static final String $IDLE_REGION = "Gunner_Black_Idle";
    private static final String $RUN_REGION = "Gunner_Black_Run";

    private static final float $ENEMY_SPEED = 0.4f;
    private static final float $ATTACK_RANGE = 0.2f;
    private static final int $KILLSCORE = 10;

    private static final float $DETECTION_RANGE = 5f;
    private static final int $DAMAGE = 10;
    private static final float $ATTACK_COOLDOWN = 1.0f;

    private int $health;
    private Player $target;
    private float $attackCooldown;
    private boolean $isDead;
    private SoundManager $soundManager;

    /**
     * Enemy constructor
     * Initializes the enemy object with the given parameters :
     * @param world (World)
     * @param atlas (TextureAtlas)
     * @param startpos (Vector2)
     * @param soundManager (SoundManager)
     */
    public Enemy(World world, TextureAtlas atlas, Vector2 startpos, Player target, SoundManager soundManager) {
        $soundManager = soundManager;
        $atlas = atlas;
        $body = createBody(world, startpos);
        $target = target;

        $stateTime = 0f;
        $attackCooldown = 0f;
        $isDead = false;

        $idleAnimation = createAnimation($IDLE_REGION, 48, 48);
        $runAnimation = createAnimation($RUN_REGION, 48, 48);

        $health = 20;
    }

    /**
     * Update all time-based variables
     * @param deltaTime (float)
     */
    private void updateTimers(float deltaTime) {
        $stateTime += deltaTime;
        $attackCooldown -= deltaTime;
    }

    /**
     * Calculate direction vector from enemy to player
     */
    private Vector2 calculateDirectionToPlayer() {
        Vector2 myPosition = $body.getPosition();
        Vector2 playerPosition = $target.getBody().getPosition();
        return new Vector2(
            playerPosition.x - myPosition.x,
            playerPosition.y - myPosition.y
        );
    }

    /**
     * Check if player is within detection range
     * @param distance (float)
     */
    private boolean isPlayerInRange(float distance) {
        return distance <= $DETECTION_RANGE;
    }

    /**
     * Handle enemy movement towards player
     * @param direction (Vector2)
     */
    private void handleMovement(Vector2 direction) {
        // Normalize direction and apply speed
        Vector2 velocity = direction.nor().scl($ENEMY_SPEED);
        $body.setLinearVelocity(velocity);
        $isRunning = true;

        updateSpriteDirection(velocity.x);
    }

    /**
     * Update sprite direction based on movement
     * @param velocityX (float)
     */
    private void updateSpriteDirection(float velocityX) {
        if (velocityX < 0 && !$isFlipped) {
            flipAnimationFrames(true);
        } else if (velocityX > 0 && $isFlipped) {
            flipAnimationFrames(false);
        }
    }

    /**
     * Handle enemy idle state
     */
    private void handleIdle() {
        $body.setLinearVelocity(0, 0);
        $isRunning = false;
    }

    /**
     * Handle attack logic
     * @param distanceToPlayer (float)
     */
    private void handleAttack(float distanceToPlayer) {
        if (canAttack(distanceToPlayer)) {
            performAttack();
        }
    }

    /**
     * Check if enemy can attack
     * @param distanceToPlayer (float)
     */
    private boolean canAttack(float distanceToPlayer) {
        return distanceToPlayer <= $ATTACK_RANGE && $attackCooldown <= 0;
    }

    /**
     * Perform the attack action
     * Deal damage to the player
     */
    private void performAttack() {
        //$soundManager.playZombieAttackSound();
        $target.takeDamage($DAMAGE);
        $attackCooldown = $ATTACK_COOLDOWN;
    }


    /**
     * Takes damage
     * @param damage (int)
     */
    public void takeDamage(int damage) {
        $health -= damage;
        if ($health <= 0 && !$isDead) {
            die();
        }
    }

    /**
     * Kills the enemy
     */
    private void die() {
        $isDead = true;
        $target.addScore($KILLSCORE);
        dispose();
        //$soundManager.playDeathEnemySound();
    }


    @Override
    public void update(float deltaTime) {
        if ($isDead) return;

        updateTimers(deltaTime);
        Vector2 directionToPlayer = calculateDirectionToPlayer();
        float distanceToPlayer = directionToPlayer.len();

        if (isPlayerInRange(distanceToPlayer)) {
            handleMovement(directionToPlayer);
            handleAttack(distanceToPlayer);
        } else {
            handleIdle();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if ($isDead) return;

        TextureRegion currentFrame = selectCurrentFrame();
        renderSprite(batch, currentFrame);
    }

    /**
     * Select the current animation frame based on state
     */
    private TextureRegion selectCurrentFrame() {
        return $isRunning ?
            $runAnimation.getKeyFrame($stateTime) :
            $idleAnimation.getKeyFrame($stateTime);
    }

    /**
     * Render the sprite with the current frame
     * @param batch (SpriteBatch)
     * @param currentFrame (TextureRegion)
     */
    private void renderSprite(SpriteBatch batch, TextureRegion currentFrame) {
        Vector2 position = $body.getPosition();
        batch.draw(currentFrame,
            position.x - $PLAYER_WIDTH / 2,
            position.y - $PLAYER_HEIGHT / 2,
            $PLAYER_WIDTH,
            $PLAYER_HEIGHT);
    }

    @Override
    public void dispose() {
        if ($body != null && $body.getWorld() != null) {
            $body.getWorld().destroyBody($body);
        }
    }

    public boolean isDead() {
        return $isDead;
    }

    public Body getBody() {
        return $body;
    }

}
