package shooter.game.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import shooter.game.Entities.Pickups.Pickup;
import shooter.game.Entities.Player;
import shooter.game.GameEssentials.SoundManager;

import java.util.List;

/**
 * WorldManager class
 *
 */
public class WorldManager {
    private TiledMap $tilemap;
    private World $world;
    private float $mapWidth;
    private float $mapHeight;
    private PickupManager $pickupManager;
    private EnemyManager $enemyManager;

    private static final float $TIME_STEP = 1/60f;
    private static final int $VELOCITY_ITERATIONS = 6;
    private static final int $POSITION_ITERATIONS = 2;

    List<Pickup> $pickups;

    public WorldManager(float mapwidth, float mapheight, TiledMap tilemap) {
        $tilemap = tilemap;
        $mapWidth = mapwidth;
        $mapHeight = mapheight;
        $world = new World(new Vector2(0, 0), true);
        $pickupManager = new PickupManager($world, mapwidth, mapheight);

        createMapBoundaries(mapwidth, mapheight);
        loadBuildings();
    }

    public void initializeEnemyManager(Player player, SoundManager soundManager) {
        $enemyManager = new EnemyManager($world, $mapWidth, $mapHeight, player, soundManager);
    }


    /**
     * Creates the boundaries of the map
     * @param $mapWidth the width of the map
     * @param $mapHeight the height of the map
     */
    private void createMapBoundaries(float $mapWidth, float $mapHeight) {
        // Create boundary body
        BodyDef $boundaryDef = new BodyDef();
        $boundaryDef.type = BodyDef.BodyType.StaticBody;
        $boundaryDef.position.set($mapWidth / 2, $mapHeight / 2);

        Body $boundaryBody = $world.createBody($boundaryDef);

        // Create chain shape for the boundaries
        ChainShape $chainShape = new ChainShape();

        // Define the boundary vertices (rectangle)
        Vector2[] $vertices = new Vector2[5];
        $vertices[0] = new Vector2(-$mapWidth/2, -$mapHeight/2); // Bottom-left
        $vertices[1] = new Vector2($mapWidth/2, -$mapHeight/2);  // Bottom-right
        $vertices[2] = new Vector2($mapWidth/2, $mapHeight/2);   // Top-right
        $vertices[3] = new Vector2(-$mapWidth/2, $mapHeight/2);  // Top-left
        $vertices[4] = $vertices[0];  // Close the chain

        $chainShape.createChain($vertices);

        // Create fixture
        FixtureDef $fixtureDef = new FixtureDef();
        $fixtureDef.shape = $chainShape;
        $fixtureDef.friction = 0.0f;        // Geen wrijving met de muren
        $fixtureDef.restitution = 0.2f;     // Lichte bounce bij botsing

        $boundaryBody.createFixture($fixtureDef);

        // Clean up
        $chainShape.dispose();
    }

    /**
     * Loads the buildings from the map
     */
    public void loadBuildings() {
        for (MapObject object : $tilemap.getLayers().get("buildings").getObjects()) {
            if (object instanceof PolygonMapObject) {
                PolygonMapObject polygonObject = (PolygonMapObject) object;
                createStaticBodyFromPolygon(polygonObject);
            }
        }
    }

    /**
     * Creates a static body from a polygon
     * @param polygonObject the polygon object to create the body from
     */
    private void createStaticBodyFromPolygon(PolygonMapObject polygonObject) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = $world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        for (int i = 0; i < vertices.length / 2; i++) {
            worldVertices[i] = new Vector2(vertices[i * 2] / 100f, vertices[i * 2 + 1] / 100f);
        }
        shape.set(worldVertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.2f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    /**
     * Updates the world
     */
    public void update(float deltatime) {
        $world.step($TIME_STEP, $VELOCITY_ITERATIONS, $POSITION_ITERATIONS);
        $pickupManager.update(deltatime);
        if ($enemyManager != null) {
            $enemyManager.update(deltatime);
        }
    }

    /**
     * Renders the pickups
     * @param batch
     */
    public void renderPickups(SpriteBatch batch) {
        $pickupManager.render(batch);
    }

    /**
     * Renders the enemies
     * @param batch
     */
    public void renderEnemies(SpriteBatch batch) {
        if ($enemyManager!= null) {
            $enemyManager.render(batch);
        }
    }

    /**
     * Checks if a player has collected a pickup
     * @param player the player to check for pickup collection
     */
    public void checkPickupCollection(Player player) {
        $pickupManager.checkPickupCollection(player);
    }

    /**
     * Returns the world
     * @return the world (World)
     */
    public World getWorld() {
        return $world;
    }

    /**
     * Disposes the world
     */
    public void dispose() {
        $enemyManager.dispose();
        $pickupManager.dispose();
        $world.dispose();
    }
}
