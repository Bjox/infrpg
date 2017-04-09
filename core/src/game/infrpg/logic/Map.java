package game.infrpg.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import static game.infrpg.MyGdxGame.logger;
import game.infrpg.logic.map.MapChunk;
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
	
	private final HashMap<Point, MapChunk> globalChunks;
	
	
	public Map() {
		this.globalChunks = new HashMap<>(1000);
		setupTransientFields();
		
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				renderChunks.add(new MapChunk(i, j));
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
		
		atlas.getRegions().forEach(region -> {
			if (region.name.startsWith("maptiles")) {
				regionNameIndexMap.put(region.name, mapTextureRegions.size());
				mapTextureRegions.add(region);
			}
		});
	}
	
	
	public void render(OrthographicCamera cam) {
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		int numChunks = renderChunks.size();
		for (int i = 0; i < numChunks; i++) {
			renderChunks.get(i).render(mapTextureRegions, batch);
		}
		batch.end();
	}

	
	@Override
	public void dispose() {
		logger.debug("Disposing map...");
		renderChunks.forEach(c -> c.dispose());
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
