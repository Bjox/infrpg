package game.infrpg.logic.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import game.infrpg.logic.RenderCallCounter;
import static game.infrpg.MyGdxGame.logger;
import game.infrpg.graphics.Camera;
import game.infrpg.logic.Constants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.lwjgl.util.Point;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Map implements Disposable, RenderCallCounter, Serializable {
	
	private transient TextureAtlas atlas;
	private transient ArrayList<MapChunk> renderChunks;
	private transient ArrayList<TextureRegion> mapTextureRegions;
	private transient HashMap<String, Integer> regionNameIndexMap;
	private transient SpriteBatch batch;
	private transient Vector2 isoCamPosBuffer;
	
	private final HashMap<String, MapChunk> globalChunks;
	public final long seed;
	
	
	public Map(long seed) {
		this.seed = seed;
		this.globalChunks = new HashMap<>(1000);
		
		setupTransientFields();
		
		// create test chunks
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				MapChunk chunk = new MapChunk(i, j);
				setChunk(chunk);
			}
		}
		
		//TextureAtlas.AtlasRegion region = atlas.findRegion("maptiles/grass");
	}
	
	
	private void setupTransientFields() {
		this.renderChunks = new ArrayList<>(6);
		this.mapTextureRegions = new ArrayList<>(16);
		this.regionNameIndexMap = new HashMap<>(16);
		this.atlas = new TextureAtlas(Gdx.files.internal("packed/pack.atlas"));
		this.batch = new SpriteBatch();
		this.isoCamPosBuffer = new Vector2();
		
		atlas.getRegions().forEach(region -> {
			if (region.name.startsWith("maptiles")) {
				regionNameIndexMap.put(region.name, mapTextureRegions.size());
				mapTextureRegions.add(region);
			}
		});
	}
	
	
	public MapChunk getChunk(int x, int y) {
		return globalChunks.get(key(x, y));
	}
	
	
	public void setChunk(MapChunk chunk) {
		globalChunks.put(key(chunk.position.getX(), chunk.position.getY()), chunk);
	}
	
	
	private String key(int x, int y) {
		return String.valueOf(x) + ":" + String.valueOf(y);
	}
	
	
	private TextureRegion getMapTextureRegion(String name) {
		return mapTextureRegions.get(regionNameIndexMap.get(name));
	}
	
	
	public void render(Camera cam) {
		handleChunkGeneration(cam.position.x, cam.position.y);
		
		cam.getIsometricPosition(isoCamPosBuffer);
		Point centerChunk = new Point(
				Math.floorDiv((int)isoCamPosBuffer.x, Constants.CHUNK_SIZE * Constants.TILE_SIZE),
				Math.floorDiv((int)isoCamPosBuffer.y, Constants.CHUNK_SIZE * Constants.TILE_SIZE));
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		int RENDER_DIST = 1;
		for (int i = centerChunk.getX() - RENDER_DIST; i <= centerChunk.getX() + RENDER_DIST; i++) {
			for (int j = centerChunk.getY() - RENDER_DIST; j <= centerChunk.getY() + RENDER_DIST; j++) {
				MapChunk chunk = getChunk(i, j);
				if (chunk != null) chunk.render(mapTextureRegions, batch);
			}
		}
		
		// Render all chunks
//		int numChunks = renderChunks.size();
//		for (int i = 0; i < numChunks; i++) {
//			renderChunks.get(i).render(mapTextureRegions, batch);
//		}
		
		batch.end();
	}
	
	
	private void handleChunkGeneration(float camX, float camY) {
		
	}
	
	
	private void generateChunk(int x, int y) {
		
	}

	
	@Override
	public void dispose() {
		logger.debug("Disposing map...");
		batch.dispose();
		atlas.dispose();
	}
	
	
	@Override
	public int getRenderCalls() {
		return batch.renderCalls;
	}
	
	
	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
		inputStream.defaultReadObject();
		setupTransientFields();
	}
	
	
	public void writeToFile(File file, boolean compress) throws IOException {
		BufferedOutputStream bfos = new BufferedOutputStream(new FileOutputStream(file, false));
		OutputStream os = compress ? new GZIPOutputStream(bfos) : bfos;
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(this);
		oos.flush();
		oos.close();
		os.close();
		bfos.close();
	}
	
	
	public static Map readFromFile(File file, boolean compressed) throws IOException, ClassNotFoundException {
		BufferedInputStream bfis = new BufferedInputStream(new FileInputStream(file));
		InputStream in = compressed ? new GZIPInputStream(bfis) : bfis;
		ObjectInputStream ois = new ObjectInputStream(in);
		Map map = (Map) ois.readObject();
		ois.close();
		in.close();
		bfis.close();
		return map;
	}
}
