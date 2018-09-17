package lib.di;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public interface IContainer {
	
	<I, T extends I> void registerType(Class<I> interfaceType, Class<T> implementationType);
	<T> void registerInstance(T instance);
	
	<T> T resolve(Class<T> type);
	
}
