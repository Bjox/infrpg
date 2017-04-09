package game.infrpg.util;

import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Util {
	
	public static void isoToCartesian(Vector2 p) {
		float x = p.x;
		p.x = (2*p.y + p.x) * 0.5f;
		p.y = (2*p.y - x) * 0.5f;
		//return new Vector2((2*p.y + p.x) * 0.5f, (2*p.y - p.x) * 0.5f);
	}
	
	
	public static void cartesianToIso(Vector2 p) {
		float x = p.x;
		p.x = p.x - p.y;
		p.y = (x + p.y) * 0.5f;
		//return new Vector2(p.x - p.y, (p.x + p.y) * 0.5f);
	}
	
}
