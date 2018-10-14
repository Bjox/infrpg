package game.infrpg.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.infrpg.client.screens.ingame.InGameScreen;
import game.infrpg.client.util.FPSCounter;
import game.infrpg.common.console.Console;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;
import game.infrpg.client.logic.AbstractScreen;
import game.infrpg.client.net.ClientNetListener;
import game.infrpg.client.rendering.DebugTextRenderer;
import game.infrpg.client.util.ClientConfig;
import game.infrpg.common.util.Globals;
import java.awt.Dimension;
import java.io.Closeable;
import java.io.IOException;
import lib.cmd.CommandDispatcher;
import lib.di.Inject;
import lib.logger.ILogger;

public class InfrpgGame extends Game {

	private static InfrpgGame game;
	
	private final ILogger logger;
	private final ClientConfig config;
	private final Dimension screenSize;

	private float elapsed_t;
	private float delta_t;
	private BitmapFont consolaFont;
	private FPSCounter fpsCounter;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private DebugTextRenderer debugText;
	private final ClientNetListener net;

	/**
	 *
	 * @param logger
	 * @param config
	 * @param net
	 */
	@Inject
	public InfrpgGame(ILogger logger, ClientConfig config, ClientNetListener net) {
		this.logger = logger;
		this.config = config;
		this.net = net;
		this.elapsed_t = 0;
		this.screenSize = new Dimension(config.screenWidth, config.screenHeight);
		
		Console.addCommandCallback(cmd -> {
			try {
				Globals.resolve(CommandDispatcher.class).parse(cmd);
			}
			catch (Exception e) {
				Console.println(e.getMessage());
			}
		});

		Globals.RENDER_DEBUG_TEXT = Globals.DEBUG;
		
		this.logger.debug("Client config: " + config.getConfigKeyValueMap());
	}

	public static TextureAtlas getAtlas() {
		return game.atlas;
	}

	/**
	 * Gets elapsed game-time in seconds.
	 *
	 * @return
	 */
	public static float elapsedTime() {
		return game.elapsed_t;
	}

	/**
	 * Gets the delta time is seconds since the last render.
	 *
	 * @return
	 */
	public static float deltaTime() {
		return game.delta_t;
	}
	
	public static void connect(String ip, int port) {
		Globals.logger().info("Connecting to " + ip + ":" + port);
		
		try {
			game.net.connect(ip, port);
		}
		catch (IOException ex) {
			Globals.logger().warning("Could not connect to " + ip + ":" + port + ". " + ex.getMessage());
		}
	}

	@Override
	public void create() {
		logger.info("Game init...");

		game = this;

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

		Gdx.graphics.setVSync(config.verticalSync);

//		AssetManager assman = new AssetManager();
//		TextureLoader.TextureParameter texparams = new TextureLoader.TextureParameter();
//		texparams.genMipMaps = true;

		setScreen(Globals.resolve(InGameScreen.class));
		
		logger.info("Game init complete");
	}

	@Override
	public void render() {
		delta_t = Gdx.graphics.getDeltaTime();
		//elapsed_t = (float) (System.nanoTime() / 1_000_000_000d); // This caused stuttering in animations
		elapsed_t += delta_t;

		super.render();

		double fps = fpsCounter.getFps();
		if (Globals.RENDER_DEBUG_TEXT && getScreen() != null) {
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
			debugText.render(batch, 5, screenSize.height - 5);
			batch.end();

			Gdx.gl.glEnable(GL20.GL_BLEND);
		}
	}

	@Override
	public void dispose() {
		logger.debug("Disposing Game...");

		if (getScreen() != null) getScreen().dispose();
		tryClose(net);
		tryClose(consolaFont);
		tryClose(batch);
		tryClose(atlas);
		
		Console.destroyConsole();

		logger.debug("Cleanup complete");
		logger.info("Application will now exit");
	}
	
	private void tryClose(Closeable closeable) {
		String name = closeable.getClass().getSimpleName();
		logger.debug("Closing " + name);
		try {
			closeable.close();
		}
		catch (Exception e) {
			logger.error("An exception occurred while closing: " + name);
			logger.logException(e);
		}
	}
	
	private void tryClose(Disposable disposable) {
		String name = disposable.getClass().getSimpleName();
		logger.debug("Disposing " + name);
		try {
			disposable.dispose();
		}
		catch (Exception e) {
			logger.error("An exception occurred while disposing: " + name);
			logger.logException(e);
		}
	}

	@Override
	public AbstractScreen getScreen() {
		return (AbstractScreen) super.getScreen();
	}

}
