package shooter.game.GameEssentials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Handles input from the player and converts it into game actions.
 * <p>
 *     The InputManager class is responsible for handling input from the player and converting it into game actions.
 *     It provides methods for handling movement input, shooting input, and getting the mouse's world position.
 *     The InputManager class is used by the GameScreen class to handle input from the player.
 * </p>
 */
public class InputManager {
    private final CameraManager $cameraManager;

    /**
     * InputManager constructor
     * Initializes the InputManager object with the given parameters :
     * @param cameraManager (CameraManager)
     */
    public InputManager(CameraManager cameraManager) {
        this.$cameraManager = cameraManager;
    }

    /**
     * Handles movement input from the player.
     * This method checks the player's input and returns a Vector2 representing the player's movement velocity.
     * The player can move left, right, up, or down by pressing the A, D, W, or S keys respectively.
     * @return (Vector2) The player's movement velocity
     */
    public Vector2 handleMovementInput() {
        Vector2 velocity = new Vector2(0, 0);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) velocity.x = -1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) velocity.x = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) velocity.y = 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) velocity.y = -1;

        return velocity;
    }

    /**
     * Checks if the player has pressed the shoot button.
     * This method checks if the player has pressed the space key or the left mouse button to shoot.
     * @return (boolean) True if the player has pressed the shoot button, false otherwise
     */
    public boolean isShootPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
            Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
    }

    /**
     * Gets the mouse's world position.
     * This method converts the mouse's screen coordinates to world coordinates and returns them as a Vector2.
     * @return (Vector2) The mouse's world position
     */
    public Vector2 getMouseWorldPosition() {
        Vector3 screenCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 worldCoordinates = $cameraManager.getCamera().unproject(screenCoordinates);
        return new Vector2(worldCoordinates.x, worldCoordinates.y);
    }
}
