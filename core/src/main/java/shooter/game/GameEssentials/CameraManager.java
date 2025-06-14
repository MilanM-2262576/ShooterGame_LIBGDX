package shooter.game.GameEssentials;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Manages the camera and viewport for the game.
 * The camera is used to follow the player around the game world.
 * The CameraManager class is responsible for managing the camera and viewport for the game.
 *  The CameraManager class is used by the GameScreen class to manage the camera and viewport.
 */
public class CameraManager {
    private OrthographicCamera $camera;
    private Viewport $viewport;

    /**
     * CameraManager constructor
     * Initializes the CameraManager object with the given parameters :
     * @param worldWidth (float) The width of the game world
     * @param worldHeight (float) The height of the game world
     */
    public CameraManager(float worldWidth, float worldHeight) {
        $camera = new OrthographicCamera();
        $viewport = new ExtendViewport(worldWidth, worldHeight, $camera);
        $camera.position.set($viewport.getWorldWidth() / 2, $viewport.getWorldHeight() / 2, 0);
        $camera.update();
    }

    /**
     * Follows the player around the game world.
     * This method sets the camera's position to follow the player around the game world.
     * @param playerPosition (Vector2) The player's position in the game world
     */
    public void followPlayer(Vector2 playerPosition) {
        $camera.position.set(playerPosition.x, playerPosition.y, 0);
        $camera.update();
    }

    /**
     * Resizes the camera and viewport.
     * This method resizes the camera and viewport to fit the screen size.
     * @param width (int) The width of the screen
     * @param height (int) The height of the screen
     */
    public void resize(int width, int height) {
        $viewport.update(width, height, true);
        $camera.update();
    }


    public OrthographicCamera getCamera() {
        return $camera;
    }

    public Viewport getViewport() {
        return $viewport;
    }
}
