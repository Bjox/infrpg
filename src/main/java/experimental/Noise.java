package experimental;

import lib.util.noise.FractalBrownianMotion;
import lib.util.noise.NoiseVisualizer;
import lib.util.noise.OpenSimplexNoise;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public final class Noise
{
	
	
	public static void main(String[] args)
	{
		OpenSimplexNoise noise = new OpenSimplexNoise(Double.doubleToLongBits(Math.random()));
		FractalBrownianMotion fbmNoise = new FractalBrownianMotion(8, 0.4, 0.03, noise);
		
		NoiseVisualizer.build(fbmNoise)
			.setWidth(400)
			.setHeight(400)
			.createNoiseVisualizer()
			.setVisible(true);
	}
	
	private Noise()
	{
	}
}
