package game.infrpg.common.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import game.infrpg.server.map.IMapStorage;
import game.infrpg.server.map.memory.MemoryMapStorage;

/**
 * Compile-time constants. These values never change.
 * @author Bjørnar W. Alvestad
 */
public final class Constants {
	
	// ONLY 'PUBLIC STATIC FINALS' IN HERE!
	
	/** Region size in chunks. */
	public static final int REGION_SIZE = 32;
	
	/** Chunk size in tiles. */
	public static final int CHUNK_SIZE = 16;
	
	/** Height of an isometric tile in pixels. */
	public static final int TILE_SIZE = 32;
	
	/** Render distance in chunks from camera center. */
	public static final int CHUNK_RENDER_DISTANCE = 2;
	
	/** Compress regions in storage. */
	public static final boolean COMPRESS_REGIONS = true;
	
	/** Default application port. */
	public static final int DEFAULT_PORT = 30555;
	
	/** Default FPS used when loding animations. */
	public static final float DEFAULT_ANIMATION_FRAMERATE = 20;
	
	/** Default playmode used when loading animations. */
	public static final Animation.PlayMode DEFAULT_ANIMATION_PLAYMODE = Animation.PlayMode.LOOP;
	
	/** Path to logile. */
	public static final String LOGFILE = "last_run.log";
	
	/** The default name of the map directory. */
	public static final String DEFAULT_MAP_DIRECTORY = "map";
	
	/** Type of storage used to persist regions long-term. */
	public static final Class<? extends IMapStorage> MAP_STORAGE_TYPE = MemoryMapStorage.class;
	
	
	/**
	 * Private constructor.
	 */
	private Constants() {
	}
	
	
	
	// TOD: To be (re)moved...
	public static final float DEBUG_MOVEMENT_SPEED = 300;//120;
	public static final boolean ENABLE_ZOOM = true;
	public static final boolean VSYNC = false;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	
	
	
	
	
}
