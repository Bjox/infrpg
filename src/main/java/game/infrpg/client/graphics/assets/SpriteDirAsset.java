package game.infrpg.client.graphics.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.client.rendering.renderable.DirRenderable;
import static game.infrpg.client.graphics.assets.GraphicsAssetLoader.loadSpriteDirectional;

/**
 * A series of sprites, each denoting a direction.
 * 
 * @author Bjørnar W. Alvestad
 */
public class SpriteDirAsset extends GraphicsAsset {

	public SpriteDirAsset(String path) {
		super(path);
	}

	@Override
	public DirRenderable loadRenderable() {
		TextureRegion[] trs = loadSpriteDirectional(getAtlas(), path);
		DirRenderable dr = new DirRenderable();
		dr.setRenderable(trs);
		return dr;
	}
	
}
