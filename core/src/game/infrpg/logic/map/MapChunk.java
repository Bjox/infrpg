package game.infrpg.logic.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import game.infrpg.logic.RenderCallCounter;
import game.infrpg.util.Util;
import java.io.Serializable;
import java.util.List;
import org.lwjgl.util.Point;
import static game.infrpg.logic.Constants.CHUNK_SIZE;
import static game.infrpg.logic.Constants.TILE_SIZE;
import java.util.Random;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class MapChunk implements Serializable {
	
	/** Global chunk position. */
	public final Point position;
	
	private final Vector2 screenPos;
	
	/** Contains a byte that specify what tile to render in a given position. */
	private final byte[][] chunkTileData;
	
	//private transient SpriteBatch batch;
	private final Vector2 coordTmp;
	
	
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
		
		Random rnd = new Random();
		for (int i = 0; i < chunkTileData.length; i++) {
			for (int j = 0; j < chunkTileData[i].length; j++) {
				chunkTileData[i][j] = 4;//(byte)rnd.nextInt(6);
			}
		}
	}

	
	public void render(List<TextureRegion> textureRegions, SpriteBatch batch) {
		for (byte x = CHUNK_SIZE-1; x >= 0; x--) {
			for (byte y = CHUNK_SIZE-1; y >= 0; y--) {
				coordTmp.x = x * TILE_SIZE;
				coordTmp.y = y * TILE_SIZE;
				Util.iso2cart(coordTmp);
				batch.draw(textureRegions.get(chunkTileData[x][y]), coordTmp.x + screenPos.x, coordTmp.y + screenPos.y);
			}
		}
	}
	
	
	
//	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
//		inputStream.defaultReadObject();
//	} 

	
	@Override
	public String toString() {
		return String.format("Map chunk [%d, %d]", position.getX(), position.getY());
	}
	
	
}
