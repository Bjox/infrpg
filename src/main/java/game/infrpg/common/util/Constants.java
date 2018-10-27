package game.infrpg.common.util;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.esotericsoftware.minlog.Log;
import game.infrpg.server.map.storage.IMapStorage;
import game.infrpg.server.map.storage.sqlite.SQLiteMapStorage;

/**
 * Compile-time constants. These values never change.
 *
 * @author Bjørnar W. Alvestad
 */
public final class Constants
{

	// ONLY 'PUBLIC STATIC FINALS' IN HERE!
	/** Debug movement speed. */
	public static final float DEBUG_MOVEMENT_SPEED = 500;//120;

	/** Debug enable map zoom. */
	public static final boolean ENABLE_ZOOM = true;

	/** Region size in chunks. */
	public static final int REGION_SIZE = 32;

	/** Chunk size in tiles. */
	public static final int CHUNK_SIZE = 16;

	/** Height of an isometric tile in pixels. */
	public static final int TILE_SIZE = 32;

	/** Render distance in chunks from camera center. */
	public static final float CHUNK_RENDER_DISTANCE = 2;

	/** Compress regions in storage. */
	public static final boolean COMPRESS_REGIONS = true;

	/** Default application port. */
	public static final int DEFAULT_PORT = 30555;

	/** Default application port. */
	public static final String DEFAULT_PORT_STR = "30555";

	/** Default FPS used when loding animations. */
	public static final float DEFAULT_ANIMATION_FRAMERATE = 20;

	/** Default playmode used when loading animations. */
	public static final Animation.PlayMode DEFAULT_ANIMATION_PLAYMODE = Animation.PlayMode.LOOP;

	/** Path to logile. */
	public static final String LOGFILE = "last_run.log";

	/** The default name of the map directory. */
	public static final String DEFAULT_MAP_DIRECTORY = "map";

	/** Type of storage used to persist regions long-term. */
	public static final Class<? extends IMapStorage> MAP_STORAGE_TYPE = SQLiteMapStorage.class;// MemoryMapStorage.class;

	/** Client configuration pathname. */
	public static final String CLIENT_CONFIG_PATHNAME = "client.properties";

	/** Server configuration pathname. */
	public static final String SERVER_CONFIG_PATHNAME = "server.properties";

	/** The client window title. */
	public static final String CLIENT_WINDOW_TITLE = "Infrpg";

	/** Client FPS limit when the window is inactive. */
	public static final int CLIENT_BACKGROUND_FPS_LIMIT = 30;

	/** Client FPS limit when the window is active. */
	public static final int CLIENT_FOREGROUND_FPS_LIMIT = 200;

	/** Log keycodes when pressing a button. */
	public static final boolean LOG_INPUT_KEYCODES = false;

	/** Server tickrate in ticks per second. */
	public static final int SERVER_TICKRATE = 30;

	/** The date format pattern used to format Date objects. */
	public static final String DATE_FORMAT_PATTERN = "dd.MM.yyy HH:mm:ss.SSS";

	/** Skip region cache cleanup if debugging. Useful during debugging and development. */
	public static final boolean SKIP_REGION_CACHE_CLEANUP_IF_DEBUG = true;

	/** Connection timeout in ms when connecting to a server. */
	public static final int CLIENT_CONNECTION_TIMEOUT = 5000;

	/** The log level used for Kryonet. */
	public static final int NET_LOG_LEVEL = Log.LEVEL_INFO;
	
	public static final boolean NET_TRACE = NET_LOG_LEVEL == Log.LEVEL_TRACE;

	/**
	 * Private constructor.
	 */
	private Constants()
	{
	}

}
