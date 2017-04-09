package game.infrpg.util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class FPSCounter {
	
	private final int regreshTime;
	private long lastFpsPoll;
	private int frames;
	private double fps;
	
	
	/**
	 * 
	 * @param refreshTime in ms
	 */
	public FPSCounter(int refreshTime) {
		this.regreshTime = refreshTime;
		frames = 0;
		lastFpsPoll = System.nanoTime();
	}
	
	
	public double getFps() {
		frames++;
		
		long now = System.nanoTime();
		long delta = now - lastFpsPoll;
		
		if (delta >= regreshTime*1_000_000) {
			fps = (frames / (double)(now - lastFpsPoll)) * 1_000_000_000;
			lastFpsPoll = now;
			frames = 0;
		}
		
		return fps;
	}
	
}
