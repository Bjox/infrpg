package game.infrpg.util;

import com.badlogic.gdx.math.Vector2;
import java.util.Random;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Util {
	
	private static Random rand = new Random();
	
	public static void seedRandom(long seed) {
		rand = new Random(seed);
	}
	
	public static void seedRandom(String seed) {
		seedRandom(seed.hashCode());
	}
	
	/**
	 * Returns a pseudorandom double value between 0.0 and 1.0.
	 * @return 
	 */
	public static double randomDouble() {
		return rand.nextDouble();
	}
	
	public static double randomDouble(double min, double max) {
		return rand.nextDouble() * (max - min) + min;
	}
	
	/**
	 * Returns a pseudorandom float value between 0.0 and 1.0.
	 * @return 
	 */
	public static float randomFloat() {
		return rand.nextFloat();
	}
	
	public static float randomFloat(float min, float max) {
		return rand.nextFloat() * (max - min) + min;
	}
	
	/**
	 * Returns a pseudorandom integer between min (inclusive) and max (exclusive).
	 * @param min
	 * @param max
	 * @return 
	 */
	public static int randomInt(int min, int max) {
		return rand.nextInt(max - min) + min;
	}
	
	/**
	 * Returns a pseudorandom integer between 0 (inclusive) and max (exclusive).
	 * @param max
	 * @return 
	 */
	public static int randomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static int randomInt() {
		return rand.nextInt();
	}
	
	public static long randomLong() {
		return rand.nextLong();
	}
	
	public static boolean randomBoolean() {
		return rand.nextBoolean();
	}
	
	public static boolean randomEvent(double prob) {
		return randomDouble() <= prob;
	}
	
	public static double randomGausian(double mean, double std) {
		return rand.nextGaussian() * std + mean;
	}
	
	
	public static void cart2iso(Vector2 p) {
		float x = p.x;
		p.x = (2*p.y + p.x) * 0.5f;
		p.y = (2*p.y - x) * 0.5f;
		//return new Vector2((2*p.y + p.x) * 0.5f, (2*p.y - p.x) * 0.5f);
	}
	
	public static void iso2cart(Vector2 p) {
		float x = p.x;
		p.x = p.x - p.y;
		p.y = (x + p.y) * 0.5f;
		//return new Vector2(p.x - p.y, (p.x + p.y) * 0.5f);
	}
	
}
