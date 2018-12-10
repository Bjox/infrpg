package game.infrpg.client.world;

import game.infrpg.client.world.wangtiles.WangTileset;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import game.infrpg.client.rendering.RenderCallCounter;
import game.infrpg.client.rendering.Camera;
import game.infrpg.client.net.ClientNetListener;
import game.infrpg.common.net.packets.ChunkRequest;
import static game.infrpg.common.util.Constants.CHUNK_SIZE;
import static game.infrpg.common.util.Constants.TILE_SIZE;
import static game.infrpg.common.util.Constants.CHUNK_RENDER_DISTANCE;
import game.infrpg.server.map.Chunk;
import java.time.Duration;
import lib.di.Inject;
import lib.logger.ILogger;
import org.lwjgl.util.Point;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ClientMapService implements IClientMapService, Disposable, RenderCallCounter
{
	private static final Duration CHUNK_REQUEST_RETRY_PERIOD = Duration.ofMillis(10000);
	
	private final ILogger logger;
	private final IMapChunkStorage chunkStorage;
	private final Vector2 isoCamPosBuffer;
	private final ClientNetListener netListener;
	private final IChunkProcessor chunkProcessor;
	private WangTileset wangTileset;
	private SpriteBatch batch;
	private Tileset tileset;

	@Inject
	public ClientMapService(
		ILogger logger,
		IMapChunkStorage chunkStorage,
		ClientNetListener netListener,
		IChunkProcessor chunkProcessor)
	{
		this.logger = logger;
		this.chunkStorage = chunkStorage;
		this.isoCamPosBuffer = new Vector2();
		this.netListener = netListener;
		this.chunkProcessor = chunkProcessor;
	}
	
	public void setup()
	{
		this.batch = new SpriteBatch();
		this.wangTileset = WangTileset.getWangTileset("overworld32");
		setTileset(Tileset.Tilesets.NORMAL);
	}

	public void setTileset(Tileset.Tilesets tileset)
	{
		logger.debug("Setting tileset " + tileset.toString());
		this.tileset = tileset.instance();
	}

	public Tileset.Tilesets getTileset()
	{
		return tileset.enumInstance;
	}

	public void render(Camera cam)
	{
		cam.getIsometricPosition(isoCamPosBuffer);

		Point centerChunk = new Point(
			Math.floorDiv((int)isoCamPosBuffer.x, CHUNK_SIZE * TILE_SIZE),
			Math.floorDiv((int)isoCamPosBuffer.y, CHUNK_SIZE * TILE_SIZE));

		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		float zoom = Math.min(cam.zoom, 8f);
		int X_RENDER_DIST = (int)(zoom * 0.4 * CHUNK_RENDER_DISTANCE) + 2;
		int Y_RENDER_DIST = (int)(zoom * 0.3 * CHUNK_RENDER_DISTANCE) + 2;

		for (int i = -X_RENDER_DIST; i <= X_RENDER_DIST; i++)
		{
			for (int j = -Y_RENDER_DIST; j <= Y_RENDER_DIST; j++)
			{
				// Special case of integer iso-transformation
				int x = Math.floorDiv(2 * j + i, 2);
				int y = Math.floorDiv(2 * j - i, 2);
				renderMapChunk(x + centerChunk.getX(), y + centerChunk.getY());
			}
		}

		batch.end();
	}

	private void renderMapChunk(int x, int y)
	{
		IMapChunk chunk = chunkStorage.getIMapChunk(x, y);
		if (chunk == null)
		{
			requestChunk(x, y);
		}
		else if (chunk instanceof RequestedMapChunk)
		{
			Duration requestAge = ((RequestedMapChunk)chunk).getTimeSinceRequested();
			if (requestAge.compareTo(CHUNK_REQUEST_RETRY_PERIOD) > 0)
			{
				requestChunk(x, y);
			}
		}
		else
		{
			chunk.render(wangTileset, batch);
		}
	}
	
	private RequestedMapChunk requestChunk(int x, int y)
	{
		netListener.sendTCP(new ChunkRequest(x, y)); // TODO: make async
		RequestedMapChunk chunk = new RequestedMapChunk(x, y);
		chunkStorage.storeMapChunk(chunk);
		return chunk;
	}

	public int getNumCachedChunks()
	{
		return chunkStorage.getCount();
	}
	
	@Override
	public void processChunk(Chunk chunk)
	{
		if (!validateChunk(chunk))
		{
			return;
		}
		
		IMapChunk processedChunk = chunkProcessor.process(chunk);
		chunkStorage.storeMapChunk(processedChunk);
	}
	
	private boolean validateChunk(Chunk chunk)
	{
		if (chunk == null) return false;
		return true;
	}

	@Override
	public void dispose()
	{
		logger.debug("Disposing map...");
		batch.dispose();
	}

	@Override
	public int getRenderCalls()
	{
		return batch.renderCalls;
	}

}
