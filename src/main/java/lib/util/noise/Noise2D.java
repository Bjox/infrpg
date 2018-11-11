package lib.util.noise;

import java.util.function.DoubleBinaryOperator;

/**
 *
 * @author Bjørnar W. Alvestad
 */
@FunctionalInterface
public interface Noise2D extends DoubleBinaryOperator
{
	@Override
	public default double applyAsDouble(double left, double right)
	{
		return eval(left, right);
	}
	
	/**
	 * Generates noise in the range [-1, 1].
	 * @param x
	 * @param y
	 * @return 
	 */
	double eval(double x, double y);
}
