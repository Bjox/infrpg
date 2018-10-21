package lib.util;

/**
 *
 * @author Bjørnar W. Alvestad
 * @param <T>
 */
public interface IArgumentParser<T extends Enum<T>> {

	boolean isPresent(String option);
	
	boolean isPresent(T option);
	
	String getString(String option);
	
	String getString(T option);
	
	int getInt(String option);
	
	double getDouble(String option);
	
	int numArguments();
	
}
