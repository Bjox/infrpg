package game.infrpg.client.screens.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import game.infrpg.common.util.Globals;
import game.infrpg.client.world.ClientMapService;
import game.infrpg.client.InfrpgGame;
import static game.infrpg.client.InfrpgGame.*;
import game.infrpg.client.rendering.Camera;
import game.infrpg.client.logic.Dir;
import game.infrpg.common.util.Constants;
import game.infrpg.client.rendering.shapes.RenderUtils;
import game.infrpg.client.screens.AbstractScreen;
import game.infrpg.client.util.ClientConfig;
import game.infrpg.common.console.Console;
import lib.di.Inject;
import lib.logger.ILogger;
import org.lwjgl.util.Point;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class InGameScreen extends AbstractScreen
{
	private final ILogger logger;
	private final ClientConfig config;
	private final ClientMapService map;
	private final Camera cam;
	private final TilesetCycler tilesetCycler;
	private final SpriteBatch batch;
	private final Vector2 isoCamPosBuffer;
	private final ShapeRenderer shapeRenderer;

	private float zoomacc = 0.0f;
	private int renderCalls;

	@Inject
	public InGameScreen(
		InfrpgGame game,
		ILogger logger,
		ClientConfig config,
		ClientMapService map)
	{
		super(game);
		this.logger = logger;
		this.config = config;
		this.map = map;

		TextureAtlas atlas = getAtlas();

		this.isoCamPosBuffer = new Vector2();
		batch = new SpriteBatch();

		tilesetCycler = new TilesetCycler(map);
		
		cam = new Camera(game.screenSize.getWidth(), game.screenSize.getHeight());
		cam.zoom = 1.f;

		cam.update();

		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render(float delta)
	{
		tick(delta);

		renderCalls = 0;
		handleInput(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (Math.abs(zoomacc) > 0.01f)
		{
			float zoomspeed = zoomacc * delta * 10.0f;
			zoomacc -= zoomspeed;
			cam.zoom *= 1.0f + zoomspeed;
		}

		cam.update();
		map.render(cam);

		batch.setProjectionMatrix(cam.combined);

		batch.begin();
		batch.end();

		RenderUtils.renderShapes(cam.combined);
		
		if (Globals.RENDER_ENTITY_ORIGIN)
		{
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.setColor(Color.CYAN);
			shapeRenderer.circle(cam.position.x, cam.position.y, 2f);
			shapeRenderer.end();
		}
		
		renderCalls += map.getRenderCalls();
		renderCalls += batch.renderCalls;
	}

	private void tick(float delta)
	{

	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void show()
	{
		Gdx.input.setInputProcessor(new InGameInput());
	}

	@Override
	public void dispose()
	{
		logger.debug("Disposing InGameScreen...");
		batch.dispose();
		map.dispose();
	}

	private void handleInput(float delta)
	{
		int directionMask = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.W))
		{
			directionMask |= Dir.UP.mask;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A))
		{
			directionMask |= Dir.LEFT.mask;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S))
		{
			directionMask &= ~Dir.UP.mask;
			directionMask |= Dir.DOWN.mask;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.D))
		{
			directionMask &= ~Dir.LEFT.mask;
			directionMask |= Dir.RIGHT.mask;
		}

		Dir moveDir = Dir.dirFromMask(directionMask);

		if (moveDir != null)
		{
			Vector2 dirVector = moveDir.getUnitDirVector();
			dirVector.scl(Constants.DEBUG_MOVEMENT_SPEED * cam.zoom * delta);
			cam.position.x += dirVector.x;
			cam.position.y += dirVector.y;
		}
	}

	@Override
	public int getRenderCalls()
	{
		return renderCalls;
	}

	@Override
	public String debugRenderText()
	{
		StringBuilder str = new StringBuilder();

		cam.getIsometricPosition(isoCamPosBuffer);
		str.append(String.format("%-15s (%.1f, %.1f)\n", "Screen pos:", cam.position.x, cam.position.y));
		str.append(String.format("%-15s (%.1f, %.1f)\n", "Isometric pos:", isoCamPosBuffer.x, isoCamPosBuffer.y));

		float chunkX = isoCamPosBuffer.x / (Constants.CHUNK_SIZE * Constants.TILE_SIZE);
		float chunkY = isoCamPosBuffer.y / (Constants.CHUNK_SIZE * Constants.TILE_SIZE);
		Point centerChunk = new Point(
			Math.floorDiv((int) isoCamPosBuffer.x, Constants.CHUNK_SIZE * Constants.TILE_SIZE),
			Math.floorDiv((int) isoCamPosBuffer.y, Constants.CHUNK_SIZE * Constants.TILE_SIZE));

		str.append(String.format("%-15s %.2f, %.2f  [%d, %d]\n", "Chunk pos:",
			chunkX, chunkY, centerChunk.getX(), centerChunk.getY()));

		str.append(String.format("%-15s %d\n", "Cached chunks:", map.getNumCachedChunks()));

		return str.toString();
	}

	public Vector3 getCameraPosition()
	{
		return cam.position;
	}

	private class InGameInput implements InputProcessor
	{

		@Override
		public boolean keyDown(int keycode)
		{
			switch (keycode)
			{
				case Input.Keys.F1:
					Globals.RENDER_DEBUG_TEXT = !Globals.RENDER_DEBUG_TEXT;
					return true;

				case Input.Keys.F2:
					Globals.RENDER_ENTITY_ORIGIN = !Globals.RENDER_ENTITY_ORIGIN;
					return true;

				case Input.Keys.F3:
					Globals.RENDER_ENTITY_OUTLINE = !Globals.RENDER_ENTITY_OUTLINE;
					return true;
					
				case Input.Keys.F4:
					Globals.RENDER_CHUNK_OUTLINE = !Globals.RENDER_CHUNK_OUTLINE;
					return true;

				case Input.Keys.Q:
					map.setTileset(tilesetCycler.getNextTileset());
					return true;

				case Input.Keys.E:
					map.setTileset(tilesetCycler.getPreviousTileset());
					return true;

				case Input.Keys.BACKSLASH:
					Console.showConsole();
					return true;

				default:
					if (Globals.DEBUG && Constants.LOG_INPUT_KEYCODES) logger.debug("Keycode: " + keycode);
			}

			return false;
		}

		@Override
		public boolean keyUp(int keycode)
		{
			return false;
		}

		@Override
		public boolean keyTyped(char character)
		{
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button)
		{
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button)
		{
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer)
		{
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY)
		{
			return false;
		}

		@Override
		public boolean scrolled(int amount)
		{
			if (Constants.ENABLE_ZOOM)
			{
				zoomacc += amount * 0.2;
				return true;
			}

			return false;
		}
	}
}
