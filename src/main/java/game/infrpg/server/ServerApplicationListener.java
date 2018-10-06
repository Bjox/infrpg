package game.infrpg.server;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class ServerApplicationListener implements ApplicationListener {

	private final ILogger logger;
	
	public ServerApplicationListener(ILogger logger) {
		this.logger = logger;
	}
	
	@Override
	public void resize(int width, int height) {
		logger.debug("Server resize");
	}

	@Override
	public void pause() {
		logger.debug("Server pause");
	}

	@Override
	public void resume() {
		logger.debug("Server resume");
	}

	@Override
	public void render() {
		float delta = Gdx.graphics.getDeltaTime();
		tick(delta);
	}
	
	public abstract void tick(float delta);
	
}
