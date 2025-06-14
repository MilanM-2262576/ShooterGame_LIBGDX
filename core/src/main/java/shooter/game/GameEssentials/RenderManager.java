package shooter.game.GameEssentials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import shooter.game.Entities.Player;
import shooter.game.Screen.PlayerHUD;
import shooter.game.World.WorldManager;

/**
 * Manages the rendering of the game world.
 * The RenderManager class is responsible for rendering the game world.
 * The RenderManager class is used by the GameScreen class to render the game world.
 */
public class RenderManager {
    private final SpriteBatch $batch;
    private final float $cursorScale;
    private final Texture $cursorTexture;

    /**
     * RenderManager constructor
     * Initializes the RenderManager object with the given parameters :
     * @param cursorScale (float) The scale of the cursor
     */
    public RenderManager(float cursorScale) {
        this.$batch = new SpriteBatch();
        this.$cursorScale = cursorScale;
        this.$cursorTexture = new Texture("assets/SpriteTextures/cursor.png");
    }

    /**
     * Begins rendering the game world.
     * This method begins rendering the game world using the given camera.
     * @param cameraManager (CameraManager) The camera manager used to render the game world
     */
    public void beginRender(CameraManager cameraManager) {
        $batch.setProjectionMatrix(cameraManager.getCamera().combined);
        $batch.begin();
    }

    /**
     * Renders the game objects in the world.
     * This method renders the game objects in the world, including the player and pickups.
     * @param worldManager (WorldManager) The world manager containing the game objects
     * @param player (Player) The player object to render
     */
    public void renderGameObjects(WorldManager worldManager, Player player) {
        worldManager.renderPickups($batch);
        worldManager.renderEnemies($batch);
        player.render($batch);
    }

    public SpriteBatch getBatch() {
        return $batch;
    }

    /**
     * Renders the HUD.
     * This method renders the HUD using the given player HUD object.
     * @param playerHUD (PlayerHUD) The player HUD object to render
     */
    public void renderHUD(PlayerHUD playerHUD) {
        $batch.setProjectionMatrix($batch.getProjectionMatrix().setToOrtho2D(
            0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        playerHUD.render($batch);
    }

    /**
     * Renders the cursor.
     * This method renders the cursor at the given position.
     * @param mousePosition (Vector2) The mouse's position in the world
     */
    public void renderCursor(Vector2 mousePosition) {
        $batch.draw(
             $cursorTexture,
            mousePosition.x - $cursorScale / 2,
            mousePosition.y - $cursorScale / 2,
            $cursorScale,
            $cursorScale
        );
    }

    public void endRender() {
        $batch.end();
    }

    public void dispose() {
        $batch.dispose();
    }
}
