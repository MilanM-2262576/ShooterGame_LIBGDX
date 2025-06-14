package shooter.game.Screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import shooter.game.Entities.Player;

/**
 * PlayerHUD represents the Heads-Up Display (HUD) for the player.
 * It displays the player's profile picture, health bar and score.
 */
public class PlayerHUD implements Disposable {
    private BitmapFont $font;
    private Texture $profilePicture;
    private Texture $healthBarBackground;
    private Texture $healthBarForeground;
    private Texture $ammoBarBackground;
    private Texture $ammoBarForeground;

    private Player $player;

    // HUD positie constanten
    private static final float $PROFILE_SIZE = 100f;
    private static final float $BAR_WIDTH = 300f;
    private static final float $BAR_HEIGHT = 40f;
    private static final float $PADDING = 10f;

    public PlayerHUD(Player player) {
        $player = player;

        $font = new BitmapFont();
        $font.setColor(Color.WHITE);
        $font.getData().setScale(1.5f);

        // Laad textures
        $profilePicture = new Texture("assets/BlackSprite/Gunner_Black.png");
        $healthBarBackground = new Texture("assets/HUD/healthbar-background.png");
        $healthBarForeground = new Texture("assets/HUD/healthbar-foreground.png");
        $ammoBarBackground = new Texture("assets/HUD/ammo-bar-background.png");
        $ammoBarForeground = new Texture("assets/HUD/ammo-bar-foreground.png");

    }

    /**
     * Render the HUD
     * @param batch (SpriteBatch)
     */
    public void render(SpriteBatch batch) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Y-positie voor bars
        float barX = $PADDING + $PROFILE_SIZE + $PADDING;
        float healthBarY = screenHeight - $PADDING - $BAR_HEIGHT;
        float ammoBarY = healthBarY - $BAR_HEIGHT - $PADDING;

        drawProfilePicture(batch, screenHeight);
        drawHealthbar(batch, barX, healthBarY);
        drawAmmoBar(batch, barX, ammoBarY);
        drawScore(batch, screenWidth, screenHeight);
    }

    /**
     * Draw the score
     * @param batch (SpriteBatch)
     * @param screenWidth (float)
     * @param screenHeight (float)
     */
    private void drawScore(SpriteBatch batch, float screenWidth, float screenHeight) {
        String scoreText = "Score: " + $player.getScore();
        $font.getData().setScale(3.0f);
        $font.draw(batch, scoreText,
            screenWidth - $PADDING-200,
            screenHeight - $PADDING);

        $font.getData().setScale(1.5f);
    }

    /**
     * Draw the health bar
     * @param batch (SpriteBatch)
     * @param healthBarX (float)
     * @param healthBarY (float)
     */
    private void drawHealthbar(SpriteBatch batch, float healthBarX ,float healthBarY) {
        batch.draw($healthBarBackground, healthBarX, healthBarY, $BAR_WIDTH, $BAR_HEIGHT);
        float healthPercentage = $player.getHealth() / 100f;
        batch.draw($healthBarForeground, healthBarX, healthBarY,
            $BAR_WIDTH * healthPercentage, $BAR_HEIGHT);
    }

    /**
     * Draw the ammo bar and ammo text
     * @param batch (SpriteBatch)
     * @param ammoBarX (float)
     * @param ammoBarY (float)
     */
    private void drawAmmoBar(SpriteBatch batch, float ammoBarX, float ammoBarY) {
        batch.draw($ammoBarBackground, ammoBarX, ammoBarY, $BAR_WIDTH, $BAR_HEIGHT);
        float ammoPercentage = $player.getCurrentAmmo() / (float)$player.getMaxAmmo();
        batch.draw($ammoBarForeground, ammoBarX, ammoBarY,
            $BAR_WIDTH * ammoPercentage, $BAR_HEIGHT);

        // Ammo tekst
        String ammoText = "Ammo: " + $player.getCurrentAmmo() + "/" + $player.getMaxAmmo();
        $font.draw(batch, ammoText, ammoBarX, ammoBarY - $PADDING);
    }

    /**
     * Draw the profile picture
     * @param batch (SpriteBatch)
     * @param screenHeight (float)
     */
    private void drawProfilePicture(SpriteBatch batch, float screenHeight) {
        batch.draw($profilePicture, $PADDING, screenHeight - $PROFILE_SIZE - $PADDING,
            $PROFILE_SIZE, $PROFILE_SIZE);
    }

    /**
     * Dispose of resources
     */
    @Override
    public void dispose() {
        $font.dispose();
        $profilePicture.dispose();
        $healthBarBackground.dispose();
        $healthBarForeground.dispose();
        $ammoBarBackground.dispose();
        $ammoBarForeground.dispose();

    }
}
