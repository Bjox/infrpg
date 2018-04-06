package game.infrpg.client.entities.rendering;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This interface represents types that are suitable for rendering,
 * by exposing the appropriate texture region at the time of rendering.
 * 
 * @author Bjørnar W. Alvestad
 */
public interface Renderable {
	
	/**
	 * A renderable singleton which returns null for the texture region.
	 */
	public static final Renderable NULL_RENDERABLE = new Renderable() {
		@Override
		public TextureRegion getTextureRegion() {
			return null;
		}

		@Override
		public String toString() {
			return "NULL_RENDERABLE@" + Integer.toHexString(hashCode());
		}
	};
	
	public TextureRegion getTextureRegion();
	
}
