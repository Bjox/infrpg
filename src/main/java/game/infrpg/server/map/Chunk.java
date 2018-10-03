package game.infrpg.server.map;

import game.infrpg.common.util.Constants;
import java.io.Serializable;
import org.lwjgl.util.Point;
import org.lwjgl.util.ReadablePoint;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Chunk implements Serializable {

	private final byte[][] tileData;

	/**
	 * The chunk position.
	 */
	public final ReadablePoint position;

	public Chunk(int x, int y) {
		this.tileData = new byte[Constants.CHUNK_SIZE][Constants.CHUNK_SIZE];
		this.position = new Point(x, y);
	}

	public void setTile(int x, int y, byte tile) {
		tileData[x][y] = tile;
	}

	public byte getTile(int x, int y) {
		return tileData[x][y];
	}

	public void setTiles(TileDataSupplier supplier) {
		for (int i = 0; i < Constants.CHUNK_SIZE; i++) {
			for (int j = 0; j < Constants.CHUNK_SIZE; j++) {
				tileData[i][j] = supplier.supplyTileData(i, j);
			}
		}
	}

	public static interface TileDataSupplier {

		public byte supplyTileData(int x, int y);

	}

}
