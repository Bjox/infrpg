package game.infrpg.client.util;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class Constants {
	
	public static final float DEBUG_MOVEMENT_SPEED = 120;
	
	public static final boolean ENABLE_ZOOM = true;
	
	/** Region size in chunks. */
	public static final int REGION_SIZE = 32;
	/** Chunk size in tiles. */
	public static final int CHUNK_SIZE = 16;
	/** Height of an isometric tile in pixels. */
	public static final int TILE_SIZE = 32;
	/** Render distance in chunks from camera center. */
	public static final int CHUNK_RENDER_DISTANCE = 2;
	
	public static final boolean COMPRESS_REGIONS = true;
	
	public static final int DEFAULT_PORT = 30555;
	
	public static final boolean VSYNC = false;
	
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static boolean DEBUG;
	public static boolean SERVER;
	public static boolean HEADLESS;
	public static boolean RENDER_DEBUG_TEXT;
	public static boolean RENDER_ENTITY_OUTLINE;
	public static boolean RENDER_ENTITY_ORIGIN;
	
	/** Default FPS used when loding animations. */
	public static final float DEFAULT_ANIMATION_FRAMERATE = 20;
	/** Default playmode used when loading animations. */
	public static Animation.PlayMode DEFAULT_ANIMATION_PLAYMODE = Animation.PlayMode.LOOP;
	
	
	private Constants() {
	}
	
}
