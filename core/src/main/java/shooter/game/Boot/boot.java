package shooter.game.Boot;

import com.badlogic.gdx.Game;
import shooter.game.Screen.MenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class boot extends Game {
    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    } //new gamescreen
}
