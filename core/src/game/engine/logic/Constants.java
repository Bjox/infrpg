package game.engine.logic;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Constants {
	
	public static final float DEBUG_MOVEMENT_SPEED = 120;
	public static final boolean ENABLE_ZOOM = true;
	
	/** Region size in chunks. */
	public static final int REGION_SIZE = 16;
	/** Chunk size in tiles. */
	public static final int CHUNK_SIZE = 16;
	/** Tile size in pixels. */
	public static final int TILE_SIZE = 32; // height of an isometric tile
	/** Render distance in chunks from camera center. */
	public static final int CHUNK_RENDER_DISTANCE = 2;
	
	public static final boolean VSYNC = false;
	
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static boolean DEBUG;
	public static boolean RENDER_ENTITY_OUTLINE = true;
	public static boolean RENDER_ENTITY_ORIGIN = true;
	
	/** Default FPS used when loding animations. */
	public static final float DEFAULT_ANIMATION_FRAMERATE = 20;
	/** Default playmode used when loading animations. */
	public static Animation.PlayMode DEFAULT_ANIMATION_PLAYMODE = Animation.PlayMode.LOOP;
}
