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
import static game.infrpg.logic.Constants.CHUNK_SIZE;
import static game.infrpg.logic.Constants.TILE_SIZE;
import static game.infrpg.logic.Constants.REGION_SIZE;
import static game.infrpg.logic.Constants.CHUNK_RENDER_DISTANCE;
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
import java.util.Collection;
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
	
	private final HashMap<Integer, Region> regions;
	public final long seed;
	
	
	public Map(long seed) {
		this.seed = seed;
		this.regions = new HashMap<>(32);
		
		setupTransientFields();
		
		// create test chunks
		for (int i = -200; i <= 200; i++) {
			for (int j = -200; j <= 200; j++) {
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
		short regionX = (short)Math.floorDiv(x, REGION_SIZE);
		short regionY = (short)Math.floorDiv(y, REGION_SIZE);
		
		Region region = regions.get(regionsMapKey(regionX, regionY));
		if (region == null) return null;
		
		int localChunkX = x - regionX * REGION_SIZE;
		int localChunkY = y - regionY * REGION_SIZE;
		return region.getLocalChunk(localChunkX, localChunkY);
	}
	
	
	public void setChunk(MapChunk chunk) {
		// Find the region where this chunk should reside
		short regionX = (short)Math.floorDiv(chunk.position.getX(), REGION_SIZE); // TODO: test with negative
		short regionY = (short)Math.floorDiv(chunk.position.getY(), REGION_SIZE);
		
		// Get/create the region
		Region region = regions.get(regionsMapKey(regionX, regionY));
		if (region == null) region = createRegion(regionX, regionY);
		
		// Calc local position of the chunk within the region
		int localChunkX = chunk.position.getX() - regionX * REGION_SIZE;
		int localChunkY = chunk.position.getY() - regionY * REGION_SIZE;
		region.setLocalChunk(chunk, localChunkX, localChunkY);
	}
	
	
	/**
	 * Calculate the key used for region mapping.
	 * @param x
	 * @param y
	 * @return 
	 */
	private int regionsMapKey(short x, short y) {
		int key = Short.toUnsignedInt(y) << 16;
		key |= Short.toUnsignedInt(x);
		return key;
	}
	
	
	/**
	 * Creates a region at position (x, y), and returns a reference
	 * to the newly created region.
	 * @param x
	 * @param y
	 * @return 
	 */
	private Region createRegion(short x, short y) {
		Region region = new Region(x, y);
		regions.put(regionsMapKey(x, y), region);
		return region;
	}
	
	
	private TextureRegion getMapTextureRegion(String name) {
		return mapTextureRegions.get(regionNameIndexMap.get(name));
	}
	
	
	public void render(Camera cam) {
		handleChunkGeneration(cam.position.x, cam.position.y);
		
		cam.getIsometricPosition(isoCamPosBuffer);
		Point centerChunk = new Point(
				Math.floorDiv((int)isoCamPosBuffer.x, CHUNK_SIZE * TILE_SIZE),
				Math.floorDiv((int)isoCamPosBuffer.y, CHUNK_SIZE * TILE_SIZE));
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		for (int i = centerChunk.getX() - CHUNK_RENDER_DISTANCE; i <= centerChunk.getX() + CHUNK_RENDER_DISTANCE; i++) {
			for (int j = centerChunk.getY() - CHUNK_RENDER_DISTANCE; j <= centerChunk.getY() + CHUNK_RENDER_DISTANCE; j++) {
				MapChunk chunk = getChunk(i, j);
				if (chunk != null) chunk.render(mapTextureRegions, batch);
			}
		}
		
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
