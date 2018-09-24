package game.infrpg.client;

import game.infrpg.client.util.ConsoleCmds;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.infrpg.client.screens.ingame.InGameScreen;
import game.infrpg.client.util.FPSCounter;
import game.infrpg.common.console.Console;
import java.util.Locale;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import game.infrpg.client.logic.AbstractScreen;
import lib.logger.Logger;
import game.infrpg.client.util.Constants;
import game.infrpg.client.rendering.DebugTextRenderer;
import lib.di.Inject;

public class InfrpgGame extends Game {

	private static InfrpgGame instance;
	
	private float elapsed_t;
	private float delta_t;
	
	public final LwjglApplicationConfiguration config;
	private final Logger logger;

	private BitmapFont consolaFont;
	private FPSCounter fpsCounter;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private DebugTextRenderer debugText;

	/**
	 *
	 * @param config LwjglApplication configuration.
	 * @param logger
	 */
	@Inject
	public InfrpgGame(LwjglApplicationConfiguration config, Logger logger) {
		this.config = config;
		this.logger = logger;
		
		this.elapsed_t = 0;
		
		Constants.RENDER_DEBUG_TEXT = Constants.DEBUG;
		Locale.setDefault(Locale.ENGLISH);

		new ConsoleCmds().registerCommands();
	}

	public static InfrpgGame gameInstance() {
		return instance;
	}

	public static TextureAtlas getAtlas() {
		return instance.atlas;
	}

	/**
	 * Gets elapsed game-time in seconds.
	 *
	 * @return
	 */
	public static float elapsedTime() {
		return instance.elapsed_t;
	}

	/**
	 * Gets the delta time is seconds since the last render.
	 *
	 * @return
	 */
	public static float deltaTime() {
		return instance.delta_t;
	}

	@Override
	public void create() {
		logger.info("Game init...");

		instance = this;

		Constants.SCREEN_WIDTH = Gdx.graphics.getWidth();
		Constants.SCREEN_HEIGHT = Gdx.graphics.getHeight();
		
		atlas = new TextureAtlas(Gdx.files.internal("packed/pack.atlas"));

		fpsCounter = new FPSCounter(1000);
		batch = new SpriteBatch();

		FreeTypeFontGenerator fontgenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/consola.ttf"));
		FreeTypeFontParameter fontparameter = new FreeTypeFontParameter();
		fontparameter.size = 20;
		fontparameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		consolaFont = fontgenerator.generateFont(fontparameter);
		consolaFont.setColor(Color.WHITE);
		fontgenerator.dispose();

		debugText = new DebugTextRenderer(consolaFont);

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
		delta_t = Gdx.graphics.getDeltaTime();
		//elapsed_t = (float) (System.nanoTime() / 1_000_000_000d); // This caused stuttering in animations
		elapsed_t += delta_t;

		super.render();

		double fps = fpsCounter.getFps();
		if (Constants.RENDER_DEBUG_TEXT) {
			int renderCalls = getScreen().getRenderCalls();
			long totalMemory = Runtime.getRuntime().totalMemory();
			long maxMemory = Runtime.getRuntime().maxMemory();
			long freeMemory = Runtime.getRuntime().freeMemory();
			long usedMemory = totalMemory - freeMemory;

			debugText.setLine(0, String.format("%-15s %.1f", "FPS:", fps));
			debugText.setLine(1, String.format("%-15s %d/%d MB", "RAM usage:", usedMemory / 1000000, maxMemory / 1000000));
			debugText.setLine(2, String.format("%-15s %d", "Render calls:", renderCalls));
			debugText.setLine(4, getScreen().debugRenderText());

			batch.begin();
			debugText.render(batch, 5, Constants.SCREEN_HEIGHT - 5);
			batch.end();
			
			Gdx.gl.glEnable(GL20.GL_BLEND);
		}
	}

	@Override
	public void dispose() {
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
