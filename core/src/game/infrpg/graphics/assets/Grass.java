package game.infrpg.graphics.assets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import static game.infrpg.MyGdxGame.elapsedTime;
import game.infrpg.graphics.ent.Sprite;
import game.infrpg.util.Util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Grass extends Sprite {
	
	public final float swayOffset;
	public final float amplitude;
	public final float speed;
	
	public Grass() {
		super(GraphicsAssetLoader.loadSprite("grass"));
		setScale(Util.randomFloat(0.2f, 0.5f));
		this.swayOffset = (float)Util.randomGausian(0, 0.3);//Util.randomFloat(0, (float)Math.PI);
		this.amplitude = Util.randomFloat(0.1f, 0.2f);
		this.speed = 2f;//Util.randomFloat(0.8f, 1.5f);
	}

	@Override
	public void render(Batch batch) {
		Affine2 aff = new Affine2();
//		setShear((float)Math.sin(t));
//		batch.setTransformMatrix(shearmat);
		float t = elapsedTime();
		float screenx = x - y;
		float screeny = (x + y) * 0.5f;
		aff.translate(screenx, screeny);
		aff.shear((float)Math.sin(t*speed+swayOffset)*amplitude, 0);
		aff.scale(scale_x, scale_y);
		
		TextureRegion tr = getTextureRegion(t);
		batch.draw(tr, tr.getRegionWidth(), tr.getRegionHeight(), aff);
//		batch.draw(tr, screenx, screeny, 0, 0, tr.getRegionWidth(), tr.getRegionHeight(), scale_x, scale_y, 0);
//		batch.setTransformMatrix(identity);
	}
	
	
}
