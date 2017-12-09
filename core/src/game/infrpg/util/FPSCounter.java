package game.infrpg.util;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class FPSCounter {
	
	private final int refreshTime;
	private long lastFpsPoll;
	private int frames;
	private double fps;
	
	
	/**
	 * 
	 * @param refreshTime in ms
	 */
	public FPSCounter(int refreshTime) {
		this.refreshTime = refreshTime;
		frames = 0;
		lastFpsPoll = System.nanoTime();
	}
	
	
	public double getFps() {
		frames++;
		
		long now = System.nanoTime();
		long delta = now - lastFpsPoll;
		
		if (delta >= refreshTime * 1_000_000) {
			fps = (frames / (double)(now - lastFpsPoll)) * 1_000_000_000;
			lastFpsPoll = now;
			frames = 0;
		}
		
		return fps;
	}
	
}
