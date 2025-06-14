package shooter.game.Screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import shooter.game.GameEssentials.CameraManager;
import shooter.game.GameEssentials.InputManager;
import shooter.game.GameEssentials.MapManager;
import shooter.game.GameEssentials.RenderManager;
import shooter.game.Entities.Player;
import shooter.game.GameEssentials.SoundManager;
import shooter.game.World.WorldManager;

/**
 * GameScreen class implements Screen interface and represents the main game screen
 */
public class GameScreen implements Screen {
    //Constants
    public static final float $PIXELS_TO_METERS = 100f;
    public static final float $CURSOR_SCALE = 0.2f;

    private final WorldManager $worldManager;
    private final Player $player;
    private final PlayerHUD $playerHUD;
    private final CameraManager $cameraManager;
    private final MapManager $mapManager;
    private final InputManager $inputManager;
    private final RenderManager $renderManager;
    private final Box2DDebugRenderer $debugRenderer;
    private boolean $isDebugRenderEnabled = true;

    /**
     * GameScreen constructor
     */
    public GameScreen() {
        SoundManager soundManager = new SoundManager();

        // Initialize managers
        $mapManager = new MapManager($PIXELS_TO_METERS);
        $cameraManager = new CameraManager(
            $mapManager.getMapWidth() / $PIXELS_TO_METERS,
            $mapManager.getMapHeight() / $PIXELS_TO_METERS
        );

        // Initialize world
        $worldManager = new WorldManager(
            $mapManager.getMapWidth() / $PIXELS_TO_METERS,
            $mapManager.getMapHeight() / $PIXELS_TO_METERS,
            $mapManager.getMap()
        );

        // Initialize managers and renderers
        $renderManager = new RenderManager($CURSOR_SCALE);
        $inputManager = new InputManager($cameraManager);
        $debugRenderer = new Box2DDebugRenderer();


        // Initialize game objects
        TextureAtlas playerAtlas = new TextureAtlas("assets/SpriteTextures/player.atlas");

        $player = new Player(
            $worldManager.getWorld(),
            playerAtlas,
            new Vector2($mapManager.getMapWidth() / 2 / $PIXELS_TO_METERS,
                $mapManager.getMapHeight() / 2 / $PIXELS_TO_METERS),
            soundManager
        );

        //Initialize EnemyManager
        $worldManager.initializeEnemyManager($player, soundManager);

        $playerHUD = new PlayerHUD($player);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        //Update
        $worldManager.update(delta);
        Vector2 velocity = $inputManager.handleMovementInput();
        $player.updateVelocity(velocity);
        $player.update(delta);

        if ($inputManager.isShootPressed()) {
            Vector2 mousePosition = $inputManager.getMouseWorldPosition();
            $player.shoot($worldManager.getWorld(), mousePosition);
        }

        // Update camera
        $cameraManager.followPlayer($player.getBody().getPosition());

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render
        $mapManager.render($cameraManager.getCamera());

        $renderManager.beginRender($cameraManager);
        $renderManager.renderGameObjects($worldManager, $player);
        $renderManager.renderCursor($inputManager.getMouseWorldPosition());
        $renderManager.renderHUD($playerHUD);
        $renderManager.endRender();


        // Debug render
        if ($isDebugRenderEnabled) {
            $debugRenderer.render($worldManager.getWorld(), $cameraManager.getCamera().combined);
        }

        $worldManager.checkPickupCollection($player);
    }



    @Override
    public void resize(int width, int height) {
        $cameraManager.resize(width, height);
    }


    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        $player.dispose();
        $renderManager.dispose();
        $mapManager.dispose();
        $worldManager.dispose();
        $debugRenderer.dispose();
    }
}
