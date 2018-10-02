package lib.di;

import lib.logger.ILogger;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IContainer {
	
	<I, T extends I> void registerType(Class<I> interfaceType, Class<T> implementationType);
	
	<I, T extends I> void registerSingleton(Class<I> interfaceType, Class<T> implementationType);
	
	<I, T extends I> T registerSingleton(Class<I> interfaceType, T instance);
	
	<T> T registerInstance(T instance);
	
	<T> T resolve(Class<T> type);
	
	<T> T resolveAndRegisterInstance(Class<T> type);
	
}
