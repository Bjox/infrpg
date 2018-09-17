package game.infrpg.server.map;

import game.infrpg.client.util.Constants;
import java.io.Serializable;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Chunk implements Serializable {
	
	private final byte [][] tileData;

	public Chunk() {
		this.tileData = new byte[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE];
		for (int i = 0; i < Constants.CHUNK_SIZE; i++) {
			for (int j = 0; j < Constants.CHUNK_SIZE; j++) {
				byte v = (byte)(Math.random()*256);
				tileData[i][j] = v;
			}
		}
	}
	
	public void setTile(int x, int y, byte tile) {
		tileData[x][y] = tile;
	}
	
	public byte getTile(int x, int y) {
		return tileData[x][y];
	}
	
}
