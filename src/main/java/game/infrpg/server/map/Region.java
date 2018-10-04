package game.infrpg.server.map;

import game.infrpg.common.util.Constants;
import java.io.Serializable;
import org.lwjgl.util.Point;
import org.lwjgl.util.ReadablePoint;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Region implements Serializable {
	
	// Increment this if non-backwards compatible changes are made on the class.
	private static final long serialVersionUID = 1L;
	
	private final Chunk[][] chunks;
	
	/** The region position. */
	public final ReadablePoint position;
	
	/**
	 * Create a new region at the specified position.
	 * @param x
	 * @param y 
	 */
	public Region(int x, int y) {
		this.chunks = new Chunk[Constants.REGION_SIZE][Constants.REGION_SIZE];
		this.position = new Point(x, y);
	}
	
	public void setChunk(int x, int y, Chunk chunk) {
		chunks[x][y] = chunk;
	}
	
	public Chunk getChunk(int x, int y) {
		return chunks[x][y];
	}

	@Override
	public String toString() {
		return String.format("Region(%d,%d)", position.getX(), position.getY());
	}
	
	
}
