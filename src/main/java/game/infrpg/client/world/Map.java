package game.infrpg.client.world;

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
import java.time.Duration;
import lib.di.Inject;
import lib.logger.ILogger;
import org.lwjgl.util.Point;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Map implements Disposable, RenderCallCounter
{
	private static final Duration CHUNK_REQUEST_RETRY_PERIOD = Duration.ofMillis(10000);
	
	private final ILogger logger;
	private final ChunkCache chunkCache;
	private final SpriteBatch batch;
	private final Vector2 isoCamPosBuffer;
	private final ClientNetListener netListener;
	private Tileset tileset;

	@Inject
	public Map(
		ILogger logger,
		ChunkCache chunkCache,
		ClientNetListener netListener)
	{
		this.logger = logger;
		this.chunkCache = chunkCache;
		this.batch = new SpriteBatch();
		this.isoCamPosBuffer = new Vector2();
		this.netListener = netListener;
		this.tileset = Tileset.getTileset(Tileset.Tilesets.NORMAL);
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

		float zoom = Math.min(cam.zoom, 4f);
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
		IMapChunk chunk = chunkCache.getChunk(x, y);
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
			chunk.render(tileset, batch);
		}
	}
	
	private RequestedMapChunk requestChunk(int x, int y)
	{
		netListener.sendTCP(new ChunkRequest(x, y)); // TODO: make async
		RequestedMapChunk chunk = new RequestedMapChunk(x, y);
		chunkCache.putChunk(chunk);
		return chunk;
	}

	public int getNumCachedChunks()
	{
		return chunkCache.getElementCount();
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

//	public static void main(String[] args)
//	{
//		final int X_RENDER_DIST = 7; // 7
//		final int Y_RENDER_DIST = 3;  // 3
//		final int RENDER_DIST_AVG = (X_RENDER_DIST + Y_RENDER_DIST) >>> 1; // 5
//		final int RENDER_DIST_OFFSET = (X_RENDER_DIST - Y_RENDER_DIST) >> 1; // 2
//		
//		int xr = RENDER_DIST_AVG;
//		
//		for (int i = -xr; i <= xr; i++)
//		{
//			int yr = xr - Math.abs(i);
//			int offset = -Integer.signum(i) * RENDER_DIST_OFFSET;
//			for (int j = -yr + offset; j <= yr + offset; j++)
//			{
//				System.out.printf("%d,%d\n", i, j);
//				//renderMapChunk(i + centerChunk.getX(), j + centerChunk.getY());
//			}
//		}
//	}
}
