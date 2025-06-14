package shooter.game.GameEssentials;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class SoundManager implements Disposable {
    private Sound $shootSound;
    private Sound $emptyAmmoSound;
    private Sound $reloadSound;
    private Sound $healSound;
    private Sound $deathEnemySound;
    private Sound $deathPlayerSound;
    private Sound $hurtPlayerSound;

    public SoundManager() {
        $shootSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/shoot.wav"));
        $emptyAmmoSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/emptyammo.wav"));
        $reloadSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/reload.wav"));
        $healSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/heal.wav"));
        $deathPlayerSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/deathPlayer.wav"));
        $hurtPlayerSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/hurtPlayer.wav"));
    }

    public void playShootSound() {
        $shootSound.play();
    }

    public void playEmptyAmmoSound() {
        $emptyAmmoSound.play();
    }

    public void playReloadSound() {
        $reloadSound.play();
    }

    public void playPlayerHurtSound() {
        $hurtPlayerSound.play();
    }

    public void playPlayerDeathSound() {
        $deathPlayerSound.play();
    }

    public void playDeathEnemySound() {
        $deathEnemySound.play();
    }

    public void playHealSound() {
        $healSound.play();
    }
    @Override
    public void dispose() {
        $shootSound.dispose();
    }
}
