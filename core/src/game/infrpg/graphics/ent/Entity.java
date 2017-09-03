package game.infrpg.graphics.ent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import static game.infrpg.MyGdxGame.*;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public abstract class Entity {
	
	public static final int ORIGIN_LEFT      = 0;
	public static final int ORIGIN_UPLEFT    = 1;
	public static final int ORIGIN_UP        = 2;
	public static final int ORIGIN_UPRIGHT   = 3;
	public static final int ORIGIN_RIGHT     = 4;
	public static final int ORIGIN_DOWNRIGHT = 5;
	public static final int ORIGIN_DOWN      = 6;
	public static final int ORIGIN_DOWNLEFT  = 7;
	public static final int ORIGIN_CENTER    = 8;
	
	/** Position on the isometric map. */
	public float x, y;
	/** Render scale. */
	public float scale_x, scale_y;
	/** Origin relative to the bottom left corner. */
	public float origin_x, origin_y;

	/**
	 * Constructor.
	 */
	public Entity() {
		this.scale_x = 1;
		this.scale_y = 1;
	}
	
	public float getScreenX() {
		return x - y;
	}
	
	public float getScreenY() {
		return (x + y) * 0.5f;
	}
	
	/**
	 * Set the scale value.
	 * @param scale 
	 */
	public final void setScale(float scale) {
		this.scale_x = scale;
		this.scale_y = scale;
	}
	
	/**
	 * Scale this sprite.
	 * @param scale 
	 */
	public final void scale(float scale) {
		this.scale_x *= scale;
		this.scale_y *= scale;
	}
	
	/**
	 * Sets the origin of this entity. See the entity class
	 * for possible values.
	 * @param origin 
	 */
	public final void setOrigin(int origin) {
		switch (origin) {
			case ORIGIN_CENTER:    origin_x = -0.5f; origin_y = -0.5f; break;
			case ORIGIN_DOWN:      origin_x = -0.5f; origin_y =    0f; break;
			case ORIGIN_DOWNLEFT:  origin_x =    0f; origin_y =    0f; break;
			case ORIGIN_DOWNRIGHT: origin_x =   -1f; origin_y =    0f; break;
			case ORIGIN_LEFT:      origin_x =    0f; origin_y = -0.5f; break;
			case ORIGIN_RIGHT:     origin_x =   -1f; origin_y = -0.5f; break;
			case ORIGIN_UP:        origin_x = -0.5f; origin_y =   -1f; break;
			case ORIGIN_UPLEFT:    origin_x =    0f; origin_y =   -1f; break;
			case ORIGIN_UPRIGHT:   origin_x =   -1f; origin_y =   -1f; break;
			default: logger.warning("Unknown origin passed to setOrigin.");
		}
	}
	
	/**
	 * Render this entity.
	 * @param batch The batch in which to queue this render.
	 */
	public void render(Batch batch) {
		TextureRegion tr = getTextureRegion(elapsedTime());
		float width = tr.getRegionWidth();
		float height = tr.getRegionHeight();
		
		Affine2 aff = new Affine2();
		aff.translate(
				getScreenX() + origin_x * width * scale_x,
				getScreenY() + origin_y * height * scale_y);
		aff.scale(scale_x, scale_y);
		
		batch.draw(tr, width, height, aff);
	}
	
//	public float getWidth() {
//		return getTextureRegion(elapsedTime()).getRegionWidth();
//	}
//	
//	public float getHeight() {
//		return getTextureRegion(elapsedTime()).getRegionHeight();
//	}
//	
//	public float getOriginX() {
//		return origin_x;
//	}
//	
//	public float getOriginY() {
//		return origin_y;
//	}
	
	/**
	 * Get the texture region to use when rendered.
	 * @param t Elapsed time.
	 * @return 
	 */
	protected abstract TextureRegion getTextureRegion(float t);
	
}
