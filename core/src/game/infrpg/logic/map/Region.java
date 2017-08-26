package game.infrpg.logic.map;

import static game.infrpg.logic.Constants.REGION_SIZE;
import org.lwjgl.util.Point;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Region {
	
	/** Region position on the map. */
	public final Point position;
	
	/** MapChunks in this region. */
	private final MapChunk[][] chunks;
	
	
	public Region(int x, int y) {
		this.position = new Point(x, y);
		this.chunks = new MapChunk[REGION_SIZE][REGION_SIZE];
	}
	
	
	public MapChunk getLocalChunk(int x, int y) {
		return chunks[x][y];
	}
	
	
	public void setLocalChunk(MapChunk chunk, int x, int y) {
		chunks[x][y] = chunk;
	}
	
}
