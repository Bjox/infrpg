package lib.logger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface ILogger {
	
	LoggerLevel getCurrentLevel();

	void setCurrentLevel(LoggerLevel currentLevel);

	void addHandler(LoggerHandler handler);

	void debug(Object message);

	void info(Object message);

	void warning(Object message);

	void error(Object message);

	void logException(Throwable throwable);
	
}
