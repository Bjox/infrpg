package game.infrpg.graphics.ent;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Sprite extends Entity {
	
	/** Draw scale. */
	public float scale_x, scale_y;
	/** The texture region of this sprite. */
	private TextureRegion tr;

	public Sprite(TextureRegion textureRegion) {
		this.tr = textureRegion;
		this.scale_x = 1;
		this.scale_y = 1;
	}

	@Override
	protected void render(SpriteBatch batch, float t, float screenx, float screeny) {
		// TODO: store width and height for optimization
		batch.draw(tr, screenx, screeny, 0, 0, getWidth(), getHeight(), scale_x, scale_y, 0);
	}
	
	/**
	 * Set the scale value.
	 * @param scale 
	 */
	public void setScale(float scale) {
		this.scale_x = scale;
		this.scale_y = scale;
	}
	
	/**
	 * Scale this sprite.
	 * @param scale 
	 */
	public void scale(float scale) {
		this.scale_x *= scale;
		this.scale_y *= scale;
	}
	
	/**
	 * Get the width of this sprite.
	 * @return 
	 */
	public float getWidth() {
		return this.tr.getRegionWidth();
	}
	
	/**
	 * Get the height of this sprite.
	 * @return 
	 */
	public float getHeight() {
		return this.tr.getRegionHeight();
	}
	
	/**
	 * Set the texture region used during rendering.
	 * @param tr 
	 */
	protected void setTextureRegion(TextureRegion tr) {
		this.tr = tr;
	}
	
	
}
