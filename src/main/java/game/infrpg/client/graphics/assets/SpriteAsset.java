package game.infrpg.client.graphics.assets;

import game.infrpg.client.rendering.renderable.SpriteRenderable;

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
