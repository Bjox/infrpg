package game.infrpg.graphics.ent;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.infrpg.logic.Dir;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class DirSprite extends Entity {
	
	private Dir direction;
	protected final Sprite[] dirSprites; // TODO: Not optimal to store Sprites here. Change to a "Renderable" type superclass.

	public DirSprite(Sprite[] dirSprites) {
		this.dirSprites = dirSprites;
		this.direction = Dir.DOWN;
	}
	
	public void setDirection(Dir dir) {
		this.direction = dir;
	}
	
	public Dir getDirection() {
		return direction;
	}
	
	public Sprite getDirectionalSprite() {
		return dirSprites[direction.index];
	}

	@Override
	protected TextureRegion getTextureRegion(float t) {
		return dirSprites[direction.index].getTextureRegion(t);
	}
	
	
	
	public final void setFps(float fps) {
		for (Sprite s : dirSprites) {
			if (s instanceof AnimatedSprite) {
				((AnimatedSprite)s).setFps(fps);
			}
		}
	}
	
	public final void setPlayMode(Animation.PlayMode playMode) {
		for (Sprite s : dirSprites) {
			if (s instanceof AnimatedSprite) {
				((AnimatedSprite)s).setPlayMode(playMode);
			}
		}
	}
	
}
