package shooter.game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import shooter.game.Screen.GameScreen;
import shooter.game.GameEssentials.SoundManager;

/**
 * Player class Extends Entity
 * Represents the player character
 * as well as shooting bullets
 *
 * @see Entity
 */
public class Player extends Entity {
    private static final String $IDLE_REGION = "Gunner_Black_Idle";
    private static final String $RUN_REGION = "Gunner_Black_Run";
    private static final float $BULLET_OFFSET = 0.2f;
    private static final float $SHOOT_COOLDOWN_DURATION = 0.3f;
    private static final int $MAX_AMMO = 20;

    private int $health;
    private int $score;
    private int $currentAmmo = $MAX_AMMO;

    private Array<Bullet> $bullets;
    private float $shootCooldownTime;

    private TextureRegion $muzzleFlashTexture;
    private boolean $showMuzzleFlash;
    private SoundManager $soundManager;

    /**
     * Player constructor
     * Initializes the player object with the given parameters :
     * @param world (World)
     * @param atlas (TextureAtlas)
     * @param startpos (Vector2)
     * @param soundManager (SoundManager)
     */
    public Player(World world, TextureAtlas atlas, Vector2 startpos, SoundManager soundManager) {
        $soundManager = soundManager;

        $atlas = atlas;
        $body = createBody(world, startpos);

        $stateTime = 0f;
        $bullets = new Array<>();
        $muzzleFlashTexture = new TextureRegion(new Texture("assets/BlackSprite/MuzzleFlash.png"));
        $showMuzzleFlash = false;

        $idleAnimation = createAnimation($IDLE_REGION, 48, 48);
        $runAnimation = createAnimation($RUN_REGION, 48, 48);

        $health = 100;
        $score = 0;
    }

    /**
     * Shoots a bullet => creates a bullet object that moves in the direction of the target
     *                    and adds it to the bullets array
     *                    plays the shoot sound
     * @param world (World)
     * @param target (Vector2)
     */
    public void shoot(World world, Vector2 target) {
        if ($shootCooldownTime <= 0 && $currentAmmo > 0) {
            $soundManager.playShootSound();

            Vector2 position = $body.getPosition();
            Vector2 direction = target.sub(position).nor();
            Vector2 bulletPosition = new Vector2(position.x + direction.x * $BULLET_OFFSET, position.y + direction.y * $BULLET_OFFSET);
            Bullet bullet = new Bullet(world, bulletPosition, direction);
            $bullets.add(bullet);

            $showMuzzleFlash = true;
            $shootCooldownTime = $SHOOT_COOLDOWN_DURATION;

            reduceAmmo();
        } else if ($currentAmmo <= 0) {
            $soundManager.playEmptyAmmoSound();
        }
    }

    /**
     * Renders the muzzle flash at the correct position
     * @param batch (SpriteBatch)
     */
    private void renderMuzzleFlash(SpriteBatch batch) {
        if ($showMuzzleFlash) {
            // Get the position of the last bullet (or the player's position if no bullets)
            Vector2 position = $bullets.size > 0 ?
                $bullets.get($bullets.size - 1).getBody().getPosition() :
                $body.getPosition();

            batch.draw($muzzleFlashTexture,
                position.x,
                position.y,
                $muzzleFlashTexture.getRegionWidth() / GameScreen.$PIXELS_TO_METERS,
                $muzzleFlashTexture.getRegionHeight() / GameScreen.$PIXELS_TO_METERS);
            $showMuzzleFlash = false;
        }
    }

    /**
     * Updates the bullets
     * Removes bullets that should be removed
     * Disposes of removed bullets
     */
    protected void updateBullets() {
        for (int i = $bullets.size - 1; i >= 0; i--) {
            Bullet bullet = $bullets.get(i);
            bullet.update();
            if (bullet.shouldRemove()) {
                $bullets.removeIndex(i);
                bullet.dispose();
            }
        }
    }

    private void reduceAmmo() {
        if ($currentAmmo > 0) {
            $currentAmmo--;
        }
    }

    public void reloadAmmo() {
        $currentAmmo = $MAX_AMMO;
        $soundManager.playReloadSound();
    }

    public void Heal() {
        $health = 100;
        $soundManager.playHealSound();
    }

    public void takeDamage(int damage) {
        $health -= damage;
        $soundManager.playPlayerHurtSound();
        if ($health <= 0) {
            // Handle player death
            $soundManager.playPlayerDeathSound();

            Gdx.app.exit();
            $health = 0;
        }

    }

    /**
     * Renders the bullets
     * @param batch (SpriteBatch)
     */
    protected void renderBullets(SpriteBatch batch) {
        for (Bullet bullet : $bullets) {
            bullet.render(batch);
        }
    }

    //getters

    public int getHealth() {
        return $health;
    }

    public int getScore() {
        return $score;
    }

    public int getCurrentAmmo() {
        return $currentAmmo;
    }

    public int getMaxAmmo() {
        return $MAX_AMMO;
    }

    public void addScore(int score) {
        $score += score;
    }

    public Array<Bullet> getBullets() {
        return $bullets;
    }

    // Abstract methods to be implemented from Entity

    /**
     * Updates the player
     * @param deltaTime (float)
     */
    @Override
    public void update(float deltaTime) {
        $stateTime += deltaTime;

        if ($shootCooldownTime > 0) {
            $shootCooldownTime -= deltaTime;
        }

        updateBullets();

    }


    /**
     * Renders the player
     * @param batch (SpriteBatch)
     */
    @Override
    public void render(SpriteBatch batch) {
        Vector2 position = $body.getPosition();
        TextureRegion textureRegion = $isRunning ? $runAnimation.getKeyFrame($stateTime) : $idleAnimation.getKeyFrame($stateTime);

        batch.draw(textureRegion,
            position.x - $PLAYER_WIDTH / 2,
            position.y - $PLAYER_HEIGHT / 2,
            $PLAYER_WIDTH,
            $PLAYER_HEIGHT);

        renderBullets(batch);
        renderMuzzleFlash(batch);
    }


    @Override
    public void dispose() {
        $atlas.dispose();
        for (Bullet bullet : $bullets) {
            bullet.dispose();
        }
    }

}
