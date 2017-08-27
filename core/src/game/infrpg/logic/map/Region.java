package game.infrpg.logic.map;

import static game.infrpg.logic.Constants.REGION_SIZE;
import java.io.Serializable;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Region implements Serializable {
	
	/** Region x position on the map. */
	public final short x;
	/** Region y position on the map. */
	public final short y;
	
	/** MapChunks in this region. */
	private final MapChunk[][] chunks;
	
	
	public Region(int x, int y) {
		this.x = (short)x;
		this.y = (short)y;
		this.chunks = new MapChunk[REGION_SIZE][REGION_SIZE];
	}
	
	
	public MapChunk getLocalChunk(int localX, int localY) {
		return chunks[localX][localY];
	}
	
	
	public void setLocalChunk(MapChunk chunk, int localX, int localY) {
		chunks[localX][localY] = chunk;
	}
	

	@Override
	public String toString() {
		return String.format("Map region [%d, %d]", x, y);
	}
	
	
	
}
