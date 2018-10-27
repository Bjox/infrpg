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
		this.requestedChunks = new HashSet<>(4 * CHUNK_RENDER_DISTANCE * CHUNK_RENDER_DISTANCE * 2);
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

		int fromx = centerChunk.getX() - CHUNK_RENDER_DISTANCE;
		int fromy = centerChunk.getY() - CHUNK_RENDER_DISTANCE;
		int tox = fromx + 2 * CHUNK_RENDER_DISTANCE;
		int toy = fromy + 2 * CHUNK_RENDER_DISTANCE;

		for (int i = tox; i >= fromx; i--)
		{
			for (int j = toy; j >= fromy; j--)
			{
				long chunkKey = getChunkKey(i, j); // TODO: optimize. chunkKey is calculated multiple times
				MapChunk chunk = chunkCache.getChunk(i, j);
				if (chunk != null)
				{
					chunk.render(tileset, batch);
					requestedChunks.remove(chunkKey);
				}
				else if (!requestedChunks.contains(chunkKey))
				{
					requestedChunks.add(chunkKey);
					netListener.sendTCP(new ChunkRequest(i, j)); // TODO: make async
				}
			}
		}

		batch.end();
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

}
