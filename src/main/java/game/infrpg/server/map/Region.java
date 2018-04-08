package game.infrpg.server.map;

import game.infrpg.client.logic.Constants;
import java.io.Serializable;
import org.lwjgl.util.Point;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Region implements Serializable {
	
	private final Chunk[][] chunks;
	public final Point position;

	public Region(int x, int y) {
		this.chunks = new Chunk[Constants.REGION_SIZE][Constants.REGION_SIZE];
		this.position = new Point(x, y);
		
		for (int i = 0; i < Constants.REGION_SIZE; i++) {
			for (int j = 0; j < Constants.REGION_SIZE; j++) {
				chunks[i][j] = new Chunk();
			}
		}
	}
	
	public void setChunk(int x, int y, Chunk chunk) {
		chunks[x][y] = chunk;
	}
	
	public Chunk getChunk(int x, int y) {
		return chunks[x][y];
	}

	@Override
	public String toString() {
		return String.format("Region[%d,%d]", position.getX(), position.getY());
	}
	
	
}
