package game.infrpg.graphics.assets;

import game.infrpg.entities.rendering.SpriteRenderable;

/**
 * A regular, single framed sprite.
 * 
 * @author Bjørnar W. Alvestad
 */
public class SpriteAsset extends GraphicsAsset {

	public SpriteAsset(String path) {
		super(path);
	}
	
	@Override
	public SpriteRenderable loadRenderable() {
		return new SpriteRenderable(GraphicsAssetLoader.loadSprite(getAtlas(), path));
	}
	
}
