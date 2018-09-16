package game.engine.client.rendering.renderable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A renderable that represents a single, static texture region.
 * 
 * @author Bjørnar W. Alvestad
 */
public class SpriteRenderable implements Renderable {
	
	private final TextureRegion tr;

	public SpriteRenderable(TextureRegion tr) {
		this.tr = tr;
	}
	
	@Override
	public TextureRegion getTextureRegion() {
		return tr;
	}
	
}
