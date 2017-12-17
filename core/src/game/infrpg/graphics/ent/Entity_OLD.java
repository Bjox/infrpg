package game.infrpg.graphics.ent;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import static game.infrpg.Infrpg.*;

/**
 *
 * @author Bjørnar W. Alvestad
 */
@Deprecated
public abstract class Entity_OLD {
	
	public enum Origin {
		LEFT,
		UPLEFT,
		UP,
		UPRIGHT,
		RIGHT,
		DOWNRIGHT,
		DOWN,
		DOWNLEFT,
		CENTER
	}
	
	/** Position on the isometric map. */
	public float x, y;
	/** Render scale. */
	public float scale_x, scale_y;
	/** Origin relative to the bottom left corner. */
	public float origin_x, origin_y;
	/** Shear to apply when rendering. */
	public float shear_x, shear_y;
	private final Affine2 transformMatrix = new Affine2();

	/**
	 * Constructor.
	 */
	public Entity_OLD() {
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
	public final void setOrigin(Origin origin) {
		switch (origin) {
			case CENTER:    origin_x = -0.5f; origin_y = -0.5f; break;
			case DOWN:      origin_x = -0.5f; origin_y =    0f; break;
			case DOWNLEFT:  origin_x =    0f; origin_y =    0f; break;
			case DOWNRIGHT: origin_x =   -1f; origin_y =    0f; break;
			case LEFT:      origin_x =    0f; origin_y = -0.5f; break;
			case RIGHT:     origin_x =   -1f; origin_y = -0.5f; break;
			case UP:        origin_x = -0.5f; origin_y =   -1f; break;
			case UPLEFT:    origin_x =    0f; origin_y =   -1f; break;
			case UPRIGHT:   origin_x =   -1f; origin_y =   -1f; break;
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
		
		transformMatrix.idt()
			.translate(
					getScreenX() + origin_x * width * scale_x,
					getScreenY() + origin_y * height * scale_y)
			.shear(
					shear_x,
					shear_y)
			.scale(
					scale_x,
					scale_y);
		
		batch.draw(tr, width, height, transformMatrix);
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
