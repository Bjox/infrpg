package game.infrpg.client.logic.mapold;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import game.infrpg.client.entity.Entity;
import game.infrpg.common.util.Util;
import java.io.Serializable;
import org.lwjgl.util.Point;
import static game.infrpg.common.util.Constants.CHUNK_SIZE;
import static game.infrpg.common.util.Constants.TILE_SIZE;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MapChunk implements Serializable {
	
	/** Global chunk position. */
	public final Point position;
	
	/** The screen/cartesian position. */
	private final Vector2 screenPos;
	
	/** Contains a byte that specify what tile to render in a given position. */
	private final byte[][] chunkTileData;
	
	private final Vector2 coordTmp;
	
	private final List<Entity> ents;
	
	
	/**
	 * 
	 * @param x x chunkPos
	 * @param y y chunkPos
	 */
	public MapChunk(int x, int y) {
		this.position = new Point(x, y);
		this.screenPos = new Vector2(x * CHUNK_SIZE * TILE_SIZE, y * CHUNK_SIZE * TILE_SIZE);
		Util.iso2cart(screenPos);
		this.chunkTileData = new byte[CHUNK_SIZE][CHUNK_SIZE];
		this.coordTmp = new Vector2();
		this.ents = new ArrayList<>();
		
		for (int i = 0; i < chunkTileData.length; i++) {
			for (int j = 0; j < chunkTileData[i].length; j++) {
				setTile(i, j, Tiles.GRASS);
			}
		}
		
	}
	
	
	public void addEntity(Entity ent) {
		ents.add(ent);
	}
	
	
	public List<Entity> getEnts() {
		return ents;
	}
	
	
	public void setTile(int x, int y, Tiles tile) {
		chunkTileData[x][y] = tile.dataValue;
	}

	
	public void render(Tileset tileset, SpriteBatch batch) {
		for (byte x = CHUNK_SIZE-1; x >= 0; x--) {
			for (byte y = CHUNK_SIZE-1; y >= 0; y--) {
				coordTmp.x = x * TILE_SIZE;
				coordTmp.y = y * TILE_SIZE;
				
				Util.iso2cart(coordTmp);
				
				batch.draw(tileset.getTexture(chunkTileData[x][y]), coordTmp.x + screenPos.x, coordTmp.y + screenPos.y);
			}
		}
	}

	
	@Override
	public String toString() {
		return String.format("Map chunk [%d, %d]", position.getX(), position.getY());
	}
	
	
}
