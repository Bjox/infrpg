package lib.di;

import java.util.function.Consumer;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IContainer {
	
	<I, T extends I> void registerType(Class<I> interfaceType, Class<T> implementationType);
	
	<T> T registerInstance(T instance);
	
	<T> T resolve(Class<T> type);
	
	<T> T resolveAndRegisterInstance(Class<T> type);
	
}
