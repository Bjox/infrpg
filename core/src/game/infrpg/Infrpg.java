package game.infrpg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.infrpg.screens.InGameScreen;
import game.infrpg.util.FPSCounter;
import console.Console;
import java.util.Locale;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import game.infrpg.screens.AbstractScreen;
import game.infrpg.util.ArgumentParser;
import console.util.logging.ConsoleHandler;
import console.util.logging.FileHandler;
import console.util.logging.Level;
import console.util.logging.Logger;
import game.infrpg.logic.Constants;
import game.infrpg.logic.Dir;
import game.infrpg.util.Util;
import java.io.IOException;
import java.util.Arrays;


public class Infrpg extends Game {
	
	public static final Logger logger = new Logger();
	private static Infrpg instance;
	private double elapsedTime;
	
	public final LwjglApplicationConfiguration config;
	public final ArgumentParser args;
	
	private FPSCounter fpsCounter;
	public BitmapFont consolaFont;
	private SpriteBatch batch;
	private TextureAtlas atlas;

	
	/**
	 * 
	 * @param config LwjglApplication configuration.
	 * @param args Arguments passed when launcing the application.
	 */
	public Infrpg(LwjglApplicationConfiguration config, String[] args) {
		this.elapsedTime = 0;
		this.config = config;
		this.args = new ArgumentParser(args);
		this.args.printAllOptions();
		
		Constants.DEBUG = this.args.isPresent("-debug");
		Locale.setDefault(Locale.ENGLISH);
		
		
		Console.createConsole("Console");
		Console.attachToErr();
		Console.attachToOut();
		new ConsoleCmds().registerCommands();
		Console.showConsole();
		
		logger.setCurrentLevel(Constants.DEBUG ? Level.ALL : Level.INFO);
		logger.addHandler(new ConsoleHandler());
		try {
			logger.addHandler(new FileHandler("../last_run.log"));
		} catch (IOException e) {
			logger.error("Unable to set up file logger: " + e.getMessage());
		}
		
		logger.debug("Game instance created");
	}
	
	
	public static Infrpg gameInstance() {
		return instance;
	}
	
	
	public static TextureAtlas getAtlas() {
		return instance.atlas;
	}
	
	
	/**
	 * Gets elapsed game-time in seconds.
	 * @return 
	 */
	public static float elapsedTime() {
		return (float)instance.elapsedTime;
	}
	
	
	@Override
	public void create () {
		logger.info("Game init...");
		
		instance = this;
		
		Constants.SCREEN_WIDTH = Gdx.graphics.getWidth();
		Constants.SCREEN_HEIGHT = Gdx.graphics.getHeight();
		
		atlas = new TextureAtlas(Gdx.files.internal("packed/pack.atlas"));
		
		fpsCounter = new FPSCounter(1000);
		batch = new SpriteBatch();
		
		FreeTypeFontGenerator fontgenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/consola.ttf"));
		FreeTypeFontParameter fontparameter = new FreeTypeFontParameter();
		fontparameter.size = 16;
		fontparameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		consolaFont = fontgenerator.generateFont(fontparameter);
		consolaFont.setColor(Color.WHITE);
		fontgenerator.dispose();
		
		// Enable alpha transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		Gdx.graphics.setVSync(Constants.VSYNC);
		
//		AssetManager assman = new AssetManager();
//		TextureLoader.TextureParameter texparams = new TextureLoader.TextureParameter();
//		texparams.genMipMaps = true;
		
		setScreen(new InGameScreen(this));
	}
	
	
	@Override
	public void render() {
		elapsedTime = System.nanoTime() / 1_000_000_000.0;
		super.render();
		
		if (Constants.DEBUG) {
			double fps = fpsCounter.getFps();
			int renderCalls = getScreen().getRenderCalls();
			long totalMemory = Runtime.getRuntime().totalMemory();
			long maxMemory = Runtime.getRuntime().maxMemory();
			long freeMemory = Runtime.getRuntime().freeMemory();
			long usedMemory = totalMemory - freeMemory;
			
			StringBuilder debugstr = new StringBuilder();
			debugstr.append(String.format("%-15s %.1f\n", "FPS:", fps));
			debugstr.append(String.format("%-15s %d/%d MB\n", "RAM usage:", usedMemory / 1000000, maxMemory / 1000000));
			debugstr.append(String.format("%-15s %d\n", "Render calls:", renderCalls));
			debugstr.append(getScreen().debugRenderText());
			
			batch.begin();
			consolaFont.draw(batch, debugstr.toString(), 5, Constants.SCREEN_HEIGHT-5);
			batch.end();
			Gdx.gl.glEnable(GL20.GL_BLEND);
		}
		
		
	}
	
	
	@Override
	public void dispose () {
		logger.debug("Disposing Game...");
		
		getScreen().dispose();
		consolaFont.dispose();
		batch.dispose();
		atlas.dispose();
		Console.destroyConsole();
		
		logger.debug("Cleanup complete");
		logger.info("Application will now exit");
	}

	
	@Override
	public AbstractScreen getScreen() {
		return (AbstractScreen) super.getScreen();
	}
	
}
