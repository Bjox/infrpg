package game.infrpg.client.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import game.infrpg.common.util.Util;
import static game.infrpg.common.util.Constants.CHUNK_SIZE;
import static game.infrpg.common.util.Constants.TILE_SIZE;
import game.infrpg.server.map.Chunk;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MapChunk extends Chunk
{
	/** The screen/cartesian position. */
	private final Vector2 screenPos;
	
	private final Vector2 coordBuffer;

	/**
	 *
	 * @param chunk
	 */
	public MapChunk(Chunk chunk)
	{
		super(chunk);
		this.screenPos = new Vector2(position.getX() * CHUNK_SIZE * TILE_SIZE, position.getY() * CHUNK_SIZE * TILE_SIZE);
		Util.iso2cart(screenPos);
		this.coordBuffer = new Vector2();
	}

	public void setTile(int x, int y, Tiles tile)
	{
		super.setTile(x, y, tile.dataValue);
	}

	public void render(Tileset tileset, SpriteBatch batch)
	{
		for (byte x = CHUNK_SIZE - 1; x >= 0; x--)
		{
			for (byte y = CHUNK_SIZE - 1; y >= 0; y--)
			{
				coordBuffer.x = x * TILE_SIZE;
				coordBuffer.y = y * TILE_SIZE;

				Util.iso2cart(coordBuffer);

				batch.draw(tileset.getTexture(getTile(x, y)), coordBuffer.x + screenPos.x, coordBuffer.y + screenPos.y);
			}
		}
	}

	@Override
	public String toString()
	{
		return String.format("Map chunk [%d, %d]", position.getX(), position.getY());
	}

}
