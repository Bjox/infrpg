package game.infrpg.client.logic;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import game.infrpg.client.InfrpgGame;
import java.io.Closeable;
import java.io.IOException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class AbstractScreen implements Screen, RenderCallCounter, Closeable, Disposable {
	
	protected final InfrpgGame game;

	
	public AbstractScreen(InfrpgGame game) {
		this.game = game;
	}
	
	
	@Override
	public void show() {
	}
	

	@Override
	public void render(float delta) {
	}
	

	@Override
	public void resize(int width, int height) {
	}
	

	@Override
	public void pause() {
	}
	

	@Override
	public void resume() {
	}
	

	@Override
	public void hide() {
	}
	
	
	public String debugRenderText() {
		return "";
	}

	@Override
	public void close() throws IOException {
		dispose();
	}
}
