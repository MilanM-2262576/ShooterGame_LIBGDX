package shooter.game.GameEssentials;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Manages the map for the game.
 * The MapManager class is responsible for managing the map for the game.
 * The MapManager class is used by the GameScreen class to manage the map for the game.
 */
public class MapManager {
    private TiledMap $map;
    private OrthogonalTiledMapRenderer $mapRenderer;
    private final int $mapWidth;
    private final int $mapHeight;

    /**
     * MapManager constructor
     * Initializes the MapManager object with the given parameters :
     * @param pixelsToMeters (float) The ratio of pixels to meters
     */
    public MapManager(float pixelsToMeters) {
        $map = new TmxMapLoader().load("assets/Map/Map.tmx");
        $mapWidth = $map.getProperties().get("width", Integer.class) *
            $map.getProperties().get("tilewidth", Integer.class);
        $mapHeight = $map.getProperties().get("height", Integer.class) *
            $map.getProperties().get("tileheight", Integer.class);
        $mapRenderer = new OrthogonalTiledMapRenderer($map, 1 / pixelsToMeters);
    }

    /**
     * Renders the map.
     * This method renders the map using the given camera.
     * @param camera (OrthographicCamera) The camera used to render the map
     */
    public void render(OrthographicCamera camera) {
        $mapRenderer.setView(camera);
        $mapRenderer.render();
    }

    /**
     * Disposes of the map and renderer.
     * This method disposes of the map and renderer to free up resources.
     */
    public void dispose() {
        $map.dispose();
        $mapRenderer.dispose();
    }

    public int getMapWidth() {
        return $mapWidth;
    }

    public int getMapHeight() {
        return $mapHeight;
    }

    public TiledMap getMap() {
        return $map;
    }
}
