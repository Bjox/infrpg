package game.infrpg.client.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import game.infrpg.client.rendering.shapes.RenderUtils;
import static game.infrpg.client.rendering.shapes.RenderUtils.ISOMETRIC_TRANSFORM;
import game.infrpg.client.rendering.shapes.Shape;
import game.infrpg.common.util.Constants;
import game.infrpg.common.util.Util;
import static game.infrpg.common.util.Constants.CHUNK_SIZE;
import static game.infrpg.common.util.Constants.TILE_SIZE;
import game.infrpg.common.util.Globals;
import game.infrpg.server.map.Chunk;
import game.infrpg.server.map.TileDataSupplier;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MapChunk extends Chunk implements IMapChunk
{
	protected final short[][] tileDataExt;
	
	/** The screen/cartesian position. */
	private final Vector2 screenPos;

	private final Vector2 tilePosBuffer;
	
	/**
	 * Creates a new map chunk using data from a regular Chunk object.
	 * @param chunk 
	 */
	public MapChunk(Chunk chunk)
	{
		this(chunk.position.getX(), chunk.position.getY());
		
		for (int i = 0; i < Constants.CHUNK_SIZE; i++)
		{
			for (int j = 0; j < Constants.CHUNK_SIZE; j++)
			{
				byte data = chunk.getTileData(i, j);
				tileData[i][j] = data;
				tileDataExt[i][j] = data;
			}
		}
	}
	
	/**
	 * 
	 * @param x Chunk x position
	 * @param y Chunk y position
	 * @param tileDataSupplier The tile data source
	 */
	public MapChunk(int x, int y, TileDataSupplier tileDataSupplier)
	{
		this(x, y);
		setAllTileData(tileDataSupplier);
	}
	
	private MapChunk(int x, int y)
	{
		super(x, y);
		
		this.tileDataExt = new short[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE];
		this.tilePosBuffer = new Vector2();
		this.screenPos = new Vector2(position.getX() * CHUNK_SIZE * TILE_SIZE, position.getY() * CHUNK_SIZE * TILE_SIZE);
		Util.iso2cart(screenPos);
		screenPos.x -= TILE_SIZE;
	}
	
	/**
	 * Sets both regular and ext tile data using the supplied tile data supplier.
	 * This call yields the same result as calling:
	 * <br><code>this.setTileData(supplier).setTileDataExt(supplier)</code>,
	 * but will set both data values in one iteration and is thus more effective.
	 * @param supplier
	 * @return 
	 */
	public final MapChunk setAllTileData(TileDataSupplier supplier)
	{
		for (int i = 0; i < Constants.CHUNK_SIZE; i++)
		{
			for (int j = 0; j < Constants.CHUNK_SIZE; j++)
			{
				int suppliedData = supplier.supplyTileData(i, j);
				tileData[i][j] = (byte)suppliedData; // tile data in first byte
				tileDataExt[i][j] = (short)(suppliedData >>> 8); // tile data ext in second and third byte
			}
		}
		return this;
	}
	
	/**
	 * Sets all tile data ext using the supplied tile data supplier.
	 * @param supplier
	 * @return A reference to this map chunk.
	 */
	public final MapChunk setTileDataExt(TileDataSupplier supplier)
	{
		for (int i = 0; i < Constants.CHUNK_SIZE; i++)
		{
			for (int j = 0; j < Constants.CHUNK_SIZE; j++)
			{
				tileDataExt[i][j] = (short)(supplier.supplyTileData(i, j) >>> 8);
			}
		}
		return this;
	}
	
	public short getTileDataExt(int x, int y)
	{
		return tileDataExt[x][y];
	}

	@Override
	public void render(ITileProvider tileProvider, SpriteBatch batch)
	{
		for (byte x = CHUNK_SIZE - 1; x >= 0; x--)
		{
			for (byte y = CHUNK_SIZE - 1; y >= 0; y--)
			{
				tilePosBuffer.x = x * TILE_SIZE;
				tilePosBuffer.y = y * TILE_SIZE;

				Util.iso2cart(tilePosBuffer);
				
				int tileRenderValue = tileDataExt[x][y];
				
				TextureRegion tile = tileProvider.getTile(tileRenderValue);
				batch.draw(tile, tilePosBuffer.x + screenPos.x, tilePosBuffer.y + screenPos.y, TILE_SIZE*2, TILE_SIZE);
			}
		}

		if (Globals.RENDER_CHUNK_OUTLINE)
		{
			int size = CHUNK_SIZE * TILE_SIZE;
			RenderUtils.queueOutlinedShape(new Shape.Rect(position.getX() * size, position.getY() * size, size, size, Color.RED, ISOMETRIC_TRANSFORM));
		}
	}

	@Override
	public String toString()
	{
		return String.format("Map chunk [%d, %d]", position.getX(), position.getY());
	}

	@Override
	public long getId()
	{
		return id;
	}

}
