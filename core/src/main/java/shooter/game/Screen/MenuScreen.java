package shooter.game.Screen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen implements Screen {
    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private final TextureAtlas playerAtlas;
    private final Animation<TextureRegion> idleAnimation;
    private float stateTime;
    private final Image playerImage;

    public MenuScreen(Game game) {
        this.game = game;
        this.stateTime = 0f;

        // Setup camera and viewport
        OrthographicCamera camera = new OrthographicCamera();
        Viewport viewport = new FitViewport(800, 480, camera);
        stage = new Stage(viewport);

        // Load the skin
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Load player atlas
        playerAtlas = new TextureAtlas("assets/SpriteTextures/player.atlas");

        // Create idle animation
        // Gunner_Black_Idle heeft 5 frames (240px / 48px = 5 frames)
        TextureRegion idleRegion = playerAtlas.findRegion("Gunner_Black_Idle");
        TextureRegion[] idleFrames = new TextureRegion[5];
        int frameWidth = 48; // Breedte van één frame

        // Split de idle sprite in frames
        for(int i = 0; i < 5; i++) {
            idleFrames[i] = new TextureRegion(idleRegion, i * frameWidth, 0, frameWidth, 48);
        }

        // Maak de animatie (0.1f is de tijd per frame)
        idleAnimation = new Animation<>(0.1f, idleFrames);

        // Maak een Image actor voor de animatie
        playerImage = new Image(idleFrames[0]);

        // Create a table for layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Add player image
        float imageSize = 150f;
        table.add(playerImage).size(imageSize).pad(20);
        table.row();

        // Create play button
        TextButton playButton = new TextButton("Play", skin);
        table.add(playButton).width(200).height(60).pad(10);

        // Add button listener
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen());
                dispose();
            }
        });

        // Set stage as input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Update animation state time
        stateTime += delta;

        // Update player image with current animation frame
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime, true);
        playerImage.setDrawable(new TextureRegionDrawable(currentFrame));

        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        playerAtlas.dispose();
    }
}
