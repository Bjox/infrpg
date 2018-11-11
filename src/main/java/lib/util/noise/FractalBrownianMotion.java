package lib.util.noise;

import java.util.function.DoubleBinaryOperator;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class FractalBrownianMotion implements Noise2D
{
	private final int iterations;
	private final double persistence;
	private final double scale;
	private final Noise2D noiseGenerator;

	public FractalBrownianMotion(int iterations, double persistence, double scale, Noise2D noiseGenerator)
	{
		this.iterations = iterations;
		this.persistence = persistence;
		this.scale = scale;
		this.noiseGenerator = noiseGenerator;
	}

	@Override
	public double eval(double x, double y)
	{
		return sumOctave(iterations, x, y, persistence, scale, noiseGenerator);
	}
	
	/**
	 * Returns noise in the range [-1, 1]
	 * @param iterations
	 * @param x
	 * @param y
	 * @param persistence
	 * @param scale
	 * @param noiseFunction
	 * @return 
	 */
	public static double sumOctave(int iterations, double x, double y, double persistence, double scale, DoubleBinaryOperator noiseFunction)
	{
		double ampSum = 0;
		double amp = 1;
		double freq = scale;
		double noise = 0;

		for (int i = 0; i < iterations; i++)
		{
			noise += noiseFunction.applyAsDouble(x * freq, y * freq) * amp;
			ampSum += amp;
			amp *= persistence;
			freq *= 2;
		}

		return noise / ampSum;
	}
	
}
