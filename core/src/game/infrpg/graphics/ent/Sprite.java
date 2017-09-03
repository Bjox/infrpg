package game.infrpg.graphics.ent;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Sprite extends Entity {
	
	/** The texture region of this sprite. */
	private TextureRegion tr;
	
	/** TODO: Endrus was here */
	public boolean shitcod=true;
	
	/**
	 * Constructor.
	 * @param textureRegion 
	 */
	public Sprite(TextureRegion textureRegion) {
		this.tr = textureRegion;
	}
	
	/**
	 * Get the width of this sprite.
	 * @return 
	 */
//	@Override
	public float getWidth() {
		return this.tr.getRegionWidth();
	}
	
	/**
	 * Get the height of this sprite.
	 * @return 
	 */
//	@Override
	public float getHeight() {
		return this.tr.getRegionHeight();
	}
	
	/**
	 * Set the texture region used during rendering.
	 * @param tr 
	 */
	public void setTextureRegion(TextureRegion tr) {
		this.tr = tr;
	}
	
	@Override
	protected TextureRegion getTextureRegion(float t) {
		return tr;
	}
	
}
