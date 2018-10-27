package game.infrpg.client.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import game.infrpg.client.logic.RenderCallCounter;
import game.infrpg.client.logic.Camera;
import game.infrpg.client.net.ClientNetListener;
import game.infrpg.common.net.packets.ChunkRequest;
import static game.infrpg.common.util.Constants.CHUNK_SIZE;
import static game.infrpg.common.util.Constants.TILE_SIZE;
import static game.infrpg.common.util.Constants.CHUNK_RENDER_DISTANCE;
import java.util.HashSet;
import java.util.Set;
import lib.di.Inject;
import lib.logger.ILogger;
import org.lwjgl.util.Point;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Map implements Disposable, RenderCallCounter
{
	private final ILogger logger;
	private final ChunkCache chunkCache;
	private final SpriteBatch batch;
	private final Vector2 isoCamPosBuffer;
	private final Set<Long> requestedChunks;
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
		this.requestedChunks = new HashSet<>(512);
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
			Math.floorDiv((int) isoCamPosBuffer.x, CHUNK_SIZE * TILE_SIZE),
			Math.floorDiv((int) isoCamPosBuffer.y, CHUNK_SIZE * TILE_SIZE));

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		int X_RENDER_DIST = (int) (cam.zoom * 0.4 * CHUNK_RENDER_DISTANCE) + 2;
		int Y_RENDER_DIST = (int) (cam.zoom * 0.3 * CHUNK_RENDER_DISTANCE) + 2;

		for (int i = -X_RENDER_DIST; i <= X_RENDER_DIST; i++)
		{
			for (int j = -Y_RENDER_DIST; j <= Y_RENDER_DIST; j++)
			{
				// Special case of iso-transformation
				int x = Math.floorDiv(2 * j + i, 2);
				int y = Math.floorDiv(2 * j - i, 2);
				renderMapChunk(x + centerChunk.getX(), y + centerChunk.getY());
			}
		}

		batch.end();
	}

	private void renderMapChunk(int x, int y)
	{
		long chunkKey = getChunkKey(x, y); // TODO: optimize. chunkKey is calculated multiple times
		MapChunk chunk = chunkCache.getChunk(x, y);
		if (chunk != null)
		{
			chunk.render(tileset, batch);
			requestedChunks.remove(chunkKey);
		}
		else if (!requestedChunks.contains(chunkKey))
		{
			requestedChunks.add(chunkKey);
			netListener.sendTCP(new ChunkRequest(x, y)); // TODO: make async
		}
	}

	private long getChunkKey(int x, int y)
	{
		return Integer.toUnsignedLong(x) << 32 | Integer.toUnsignedLong(y);
	}

	public int getNumLoadedChunks()
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
