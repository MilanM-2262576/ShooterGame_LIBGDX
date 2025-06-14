package shooter.game.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import shooter.game.Entities.Bullet;
import shooter.game.Entities.Enemy;
import shooter.game.Entities.Player;
import shooter.game.GameEssentials.SoundManager;

/**
 * EnemyManager class
 * Manages the spawning and updating of enemies
 */
public class EnemyManager {
    private World $world;
    private TextureAtlas $enemyAtlas;
    private Array<Enemy> $enemies;
    private float $mapWidth;
    private float $mapHeight;
    private Player $player;
    private SoundManager $soundManager;

    private static float $SPAWN_INTERVAL = 2.0f;
    private static int $MAX_ENEMIES = 20;
    private static final float $MIN_SPAWN_DISTANCE = 5f;
    private static final float $MAX_SPAWN_DISTANCE = 10f;

    private float $spawnTimer;
    private int $currentLevel = 1;

    public EnemyManager(World world, float mapWidth, float mapHeight, Player player, SoundManager soundManager) {
        $world = world;
        $mapWidth = mapWidth;
        $mapHeight = mapHeight;
        $player = player;
        $soundManager = soundManager;

        $enemies = new Array<>();
        $enemyAtlas = new TextureAtlas("assets/SpriteTextures/player.atlas");
        $spawnTimer = 0;
    }

    public void update(float deltaTime) {
        $spawnTimer += deltaTime;

        if ($spawnTimer >= $SPAWN_INTERVAL && $enemies.size < $MAX_ENEMIES) {
            spawnEnemy();
            $spawnTimer = 0;
        }

        for (int i = $enemies.size - 1; i >= 0; i--) {
            Enemy enemy = $enemies.get(i);
            enemy.update(deltaTime);

            if (enemy.isDead()) {
                $enemies.removeIndex(i);
            }
        }

        int newLevel = $player.getScore() / 100 + 1;
        if (newLevel > $currentLevel) {
            $currentLevel = newLevel;
            $SPAWN_INTERVAL = Math.max(0.2f, $SPAWN_INTERVAL - 0.2f);
            $MAX_ENEMIES += 10;
        }

        checkBulletCollisions();
    }

    private void checkBulletCollisions() {
        Array<Bullet> bullets = $player.getBullets();
        for (Bullet bullet : bullets) {
            if (bullet.isDestroyed()) continue;
            for (Enemy enemy : $enemies) {
                if (bullet.collidesWith(enemy)) {
                    enemy.takeDamage(20);
                    bullet.destroy();
                    break;
                }
            }
        }
    }

    private void spawnEnemy() {
        Vector2 playerPos = $player.getBody().getPosition();
        float angle = MathUtils.random(MathUtils.PI2);
        float distance = MathUtils.random($MIN_SPAWN_DISTANCE, $MAX_SPAWN_DISTANCE);
        float spawnX = playerPos.x + MathUtils.cos(angle) * distance;
        float spawnY = playerPos.y + MathUtils.sin(angle) * distance;
        spawnX = MathUtils.clamp(spawnX, 1, $mapWidth - 1);
        spawnY = MathUtils.clamp(spawnY, 1, $mapHeight - 1);
        Enemy enemy = new Enemy($world, $enemyAtlas, new Vector2(spawnX, spawnY), $player, $soundManager);
        $enemies.add(enemy);
    }

    public void render(SpriteBatch batch) {
        for (Enemy enemy : $enemies) {
            enemy.render(batch);
        }
    }

    public void dispose() {
        $enemyAtlas.dispose();
        for (Enemy enemy : $enemies) {
            enemy.dispose();
        }
    }

    public Array<Enemy> getEnemies() {
        return $enemies;
    }
}
