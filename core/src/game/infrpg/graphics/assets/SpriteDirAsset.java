package game.infrpg.graphics.assets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.entities.rendering.DirRenderable;
import static game.infrpg.graphics.assets.GraphicsAssetLoader.loadSpriteDirectional;

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
