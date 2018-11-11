package game.infrpg.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.infrpg.client.util.FPSCounter;
import game.infrpg.common.console.Console;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;
import game.infrpg.client.screens.AbstractScreen;
import game.infrpg.client.world.MapChunk;
import game.infrpg.client.net.ClientNetHandler;
import game.infrpg.client.net.ClientNetListener;
import game.infrpg.client.rendering.DebugTextRenderer;
import game.infrpg.client.screens.ingame.InGameScreen;
import game.infrpg.client.screens.menu.MenuScreen;
import game.infrpg.client.util.ClientConfig;
import game.infrpg.client.world.ChunkCache;
import game.infrpg.client.world.Tileset;
import game.infrpg.common.util.Arguments;
import game.infrpg.common.util.Globals;
import game.infrpg.common.util.Helpers;
import game.infrpg.server.map.Chunk;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import lib.cmd.CommandDispatcher;
import lib.di.IContainer;
import lib.di.Inject;
import lib.logger.ILogger;
import lib.util.IArgumentParser;
import org.lwjgl.util.ReadableDimension;

public class InfrpgGame extends Game
{
	private static InfrpgGame game;

	private final ILogger logger;
	private final ClientConfig config;
	public final ReadableDimension screenSize;
	private float elapsed_t;
	private float delta_t;
	private final ClientNetListener net;
	private final ChunkCache chunkCache;
	private final FPSCounter fpsCounter;

	private BitmapFont consolaFont;
	private SpriteBatch batch;
	private TextureAtlas atlas;
	private DebugTextRenderer debugText;

	/**
	 *
	 * @param logger
	 * @param config
	 * @param net
	 * @param chunkCache
	 */
	@Inject
	public InfrpgGame(
		ILogger logger,
		ClientConfig config,
		ClientNetListener net,
		ChunkCache chunkCache)
	{
		this.logger = logger;
		this.config = config;
		this.net = net;
		this.elapsed_t = 0;
		this.screenSize = config.screenResolution.toDimension();
		this.chunkCache = chunkCache;
		this.fpsCounter = new FPSCounter(1000);

		Console.setCommandHook(InfrpgGame::execCommand);
		Globals.RENDER_DEBUG_TEXT = Globals.DEBUG;

		this.logger.debug("Client config: " + config.getConfigKeyValueMap());
	}

	public static TextureAtlas getAtlas()
	{
		return game.atlas;
	}

	/**
	 * Gets elapsed game-time in seconds.
	 *
	 * @return
	 */
	public static float elapsedTime()
	{
		return game.elapsed_t;
	}

	/**
	 * Gets the delta time is seconds since the last render.
	 *
	 * @return
	 */
	public static float deltaTime()
	{
		return game.delta_t;
	}

	public static CompletableFuture<Void> execCommand(String cmd)
	{
		return Globals.resolve(CommandDispatcher.class).parse(cmd);
	}

	public static void connect(String ip, int port)
	{
		Globals.logger().info("Connecting to " + ip + ":" + port);

		try
		{
			game.net.connect(ip, port);
			game.setScreen(Globals.resolve(InGameScreen.class));
		}
		catch (IOException ex)
		{
			Globals.logger().warning("Could not connect to " + ip + ":" + port + ". " + ex.getMessage());
			throw Helpers.wrapInRuntimeException(ex);
		}
		catch (Exception ex)
		{
			Globals.logger().logException(ex);
			throw Helpers.wrapInRuntimeException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void create()
	{
		logger.info("Game init...");
		
		game = this;
		
		atlas = new TextureAtlas(Gdx.files.internal("packed/pack.atlas"));
		batch = new SpriteBatch();
		
		Tileset.loadTilesets(atlas);

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

		logger.debug("Registering screens");
		registerScreens(Globals.container);

		setScreen(Globals.resolve(MenuScreen.class));

		logger.info("Executing startup commands");
		IArgumentParser<Arguments> argp = Globals.resolve(IArgumentParser.class);
		String execStr = argp.getString(Arguments.EXEC);
		if (execStr != null)
		{
			String[] execParts = execStr.split(";");
			for (String cmd : execParts)
			{
				logger.info("Executing command \"" + cmd + "\"");
				execCommand(cmd);
			}
		}

		logger.info("Game init complete");
	}

	@Override
	public void render()
	{
		tick();

		delta_t = Gdx.graphics.getDeltaTime();
		//elapsed_t = (float) (System.nanoTime() / 1_000_000_000d); // This caused stuttering in animations
		elapsed_t += delta_t;

		super.render();

		double fps = fpsCounter.getFps();
		if (Globals.RENDER_DEBUG_TEXT && getScreen() != null)
		{
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
			debugText.render(batch, 5, screenSize.getHeight() - 5);
			batch.end();

			Gdx.gl.glEnable(GL20.GL_BLEND);
		}
	}

	private void tick()
	{
		ClientNetHandler netHandler = net.getHandler();
		
		Chunk chunk;
		while ((chunk = netHandler.chunksToProcess.poll()) != null)
		{
			chunkCache.putChunk(new MapChunk(chunk));
		}
		
		
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		{
			Gdx.app.exit();
		}
	}

	@Override
	public void dispose()
	{
		logger.debug("Disposing Game...");

		if (getScreen() != null)
		{
			getScreen().dispose();
		}
		tryClose(net);
		tryClose(consolaFont);
		tryClose(batch);
		tryClose(atlas);

		Console.destroyConsole();

		logger.debug("Cleanup complete");
		logger.info("Application will now exit");
	}

	private void tryClose(Closeable closeable)
	{
		String name = closeable.getClass().getSimpleName();
		logger.debug("Closing " + name);
		try
		{
			closeable.close();
		}
		catch (Exception e)
		{
			logger.error("An exception occurred while closing: " + name);
			logger.logException(e);
		}
	}

	private void tryClose(Disposable disposable)
	{
		String name = disposable.getClass().getSimpleName();
		logger.debug("Disposing " + name);
		try
		{
			disposable.dispose();
		}
		catch (Exception e)
		{
			logger.error("An exception occurred while disposing: " + name);
			logger.logException(e);
		}
	}

	@Override
	public AbstractScreen getScreen()
	{
		return (AbstractScreen) super.getScreen();
	}
	
	private static void registerScreens(IContainer container)
	{
		container.resolveAndRegisterInstance(MenuScreen.class);
		container.resolveAndRegisterInstance(InGameScreen.class);
	}

}
