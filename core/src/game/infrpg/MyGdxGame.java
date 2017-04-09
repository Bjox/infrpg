package game.infrpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.infrpg.screens.InGameScreen;
import game.infrpg.util.FPSCounter;
import console.Console;
import java.util.Locale;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.infrpg.screens.AbstractScreen;
import game.infrpg.util.ArgumentParser;
import console.util.logging.ConsoleHandler;
import console.util.logging.FileHandler;
import console.util.logging.Level;
import console.util.logging.Logger;
import java.io.IOException;

public class MyGdxGame extends Game {
	
	public int SCREEN_WIDTH;
	public int SCREEN_HEIGHT;
	
	public final boolean DEBUG;
	
	public static final Logger logger = new Logger();
	public static MyGdxGame instance;
	public final LwjglApplicationConfiguration config;
	public final ArgumentParser args;
	
	private FPSCounter fpsCounter;
	public BitmapFont bitmapFont;
	private SpriteBatch batch;

	
	/**
	 * 
	 * @param config LwjglApplication configuration.
	 * @param args Arguments passed when launcing the application.
	 */
	public MyGdxGame(LwjglApplicationConfiguration config, String[] args) {
		this.config = config;
		this.args = new ArgumentParser(args);
		this.args.printAllOptions();
		
		DEBUG = this.args.isPresent("-debug");
		Locale.setDefault(Locale.ENGLISH);
		
		Console.createConsole("Console");
		Console.attachToErr();
		Console.attachToOut();
		new ConsoleCmds().registerCommands();
		Console.showConsole();
		
		logger.setCurrentLevel(DEBUG ? Level.ALL : Level.INFO);
		logger.addHandler(new ConsoleHandler());
		try {
			logger.addHandler(new FileHandler("../last_run.log"));
		} catch (IOException e) {
			logger.error("Unable to set up file logger: " + e.getMessage());
		}
		
		logger.debug("Game instance created");
	}
	
	
	@Override
	public void create () {
		logger.info("Game init...");
		
		instance = this;
		
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		SCREEN_HEIGHT = Gdx.graphics.getHeight();
		
		fpsCounter = new FPSCounter(1000);
		bitmapFont = new BitmapFont();
		batch = new SpriteBatch();
		
		bitmapFont.setColor(Color.WHITE);
		
		// Enable alpha transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		setScreen(new InGameScreen(this));
	}
	
	
	@Override
	public void render() {
		super.render();
		
		if (DEBUG) {
			double fps = fpsCounter.getFps();
			int renderCalls = getScreen().getRenderCalls();
			
			batch.begin();
			bitmapFont.draw(batch, String.format("FPS: %.1f\nRender calls: %d\n%s", fps, renderCalls, getScreen().debugRenderText()), 5, SCREEN_HEIGHT-5);
			batch.end();
			Gdx.gl.glEnable(GL20.GL_BLEND);
		}
		
		
	}
	
	
	@Override
	public void dispose () {
		logger.debug("Disposing Game...");
		
		getScreen().dispose();
		bitmapFont.dispose();
		batch.dispose();
		Console.destroyConsole();
		
		logger.debug("Cleanup complete");
		logger.info("Application will now exit");
	}

	
	@Override
	public AbstractScreen getScreen() {
		return (AbstractScreen) super.getScreen();
	}
	
}
