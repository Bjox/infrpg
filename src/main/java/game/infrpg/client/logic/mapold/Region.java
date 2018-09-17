package game.infrpg.client.logic.mapold;

import static game.infrpg.client.util.Constants.REGION_SIZE;
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
	
	/** Number of chunks in this region. */
//	private int chunkCounter;
	
	
	public Region(int x, int y) {
		this.x = (short)x;
		this.y = (short)y;
		this.chunks = new MapChunk[REGION_SIZE][REGION_SIZE];
//		this.chunkCounter = 0;
	}
	
	
	/**
	 * Get a chunk. If the chunk does not exist, null is returned.
	 * @param localX
	 * @param localY
	 * @return 
	 */
	public MapChunk getLocalChunk(int localX, int localY) {
		return chunks[localX][localY];
	}
	
	
	/**
	 * Set a chunk. Set chunk to null in order to remove the chunk at the position.
	 * @param chunk
	 * @param localX
	 * @param localY 
	 */
	public void setLocalChunk(MapChunk chunk, int localX, int localY) {
//		if ((chunks[localX][localY] == null) != (chunk == null)) {
//			this.chunkCounter += chunk == null ? -1 : 1;
//		}
		chunks[localX][localY] = chunk;
	}
	

	@Override
	public String toString() {
		return String.format("Map region [%d, %d]", x, y);
	}
	
	
//	public int getChunkCounter() {
//		return chunkCounter;
//	}
	
}
