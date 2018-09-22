package lib.logger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface ILogger {
	
	LoggerLevel getCurrentLevel();

	void setCurrentLevel(LoggerLevel currentLevel);

	void addHandler(LoggerHandler handler);

	void debug(String message);

	void info(String message);

	void warning(String message);

	void error(String message);

	void logException(Throwable throwable);
	
}
