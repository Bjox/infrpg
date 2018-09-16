package game.engine.client.rendering.renderable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.engine.client.logic.Dir;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class DirRenderable implements Renderable {

	private final Renderable[] directions;
	public Dir direction;

	public DirRenderable() {
		this.directions = new Renderable[Dir.NUM_DIRECTIONS];
		this.direction = Dir.UP;
	}
	
	public void setRenderable(Dir dir, Renderable renderable) {
		directions[dir.index] = renderable;
	}
	
	public void setRenderable(Renderable[] renderables) {
		Dir.forEach((dir) -> {
			int index = dir.index;
			directions[index] = renderables[index];
		});
	}
	
	public void setRenderable(TextureRegion[] trs) {
		Dir.forEach((dir) -> {
			setRenderable(dir, new SpriteRenderable(trs[dir.index]));
		});
	}
	
	public TextureRegion getTextureRegion(Dir dir) {
		return getRenderable(dir).getTextureRegion();
	}
	
	public Renderable getRenderable(Dir dir) {
		return directions[dir.index];
	}

	@Override
	public TextureRegion getTextureRegion() {
		return directions[direction.index].getTextureRegion();
	}
	
}
