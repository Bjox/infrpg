package game.infrpg.screens;

import com.badlogic.gdx.Screen;
import game.infrpg.MyGdxGame;
import game.infrpg.logic.RenderCallCounter;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class AbstractScreen implements Screen, RenderCallCounter {
	
	protected final MyGdxGame game;

	
	public AbstractScreen(MyGdxGame game) {
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

}
