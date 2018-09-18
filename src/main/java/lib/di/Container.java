package lib.di;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Container implements IContainer {

	// TODO: when constructing, check accessability (public etc.) cannot construct unaccessable types.
	
	private final Map<Class<?>, Object> instances;
	private final Map<Class<?>, Class<?>> typeRegistrations;
	private final Set<Class<?>> failedTypes;

	public Container() {
		instances = new HashMap<>();
		typeRegistrations = new HashMap<>();
		failedTypes = new HashSet<>();
	}

	@Override
	public <I, T extends I> void registerType(Class<I> interfaceType, Class<T> implementationType) {
		if (interfaceType.equals(implementationType)) {
			throw new DependencyContainerException("Cannot register type mapping " + interfaceType.getName() + " with itself.");
		}
		
		if (!interfaceType.isInterface()) {
			throw new DependencyContainerException(
					"Cannot register type mapping " + interfaceType.getName() + " -> " + implementationType.getName() + ". Type is not an interface.");
		}

		if (typeRegistrations.containsKey(interfaceType)) {
			throw new DependencyContainerException(
					"Cannot register type mapping " + interfaceType.getName() + " -> " + implementationType.getName() + ". Duplicate registration.");
		}

		typeRegistrations.put(interfaceType, implementationType);
		failedTypes.remove(interfaceType);
	}

	@Override
	public <T> T registerInstance(T instance) {
		Class<?> type = instance.getClass();

		if (instances.containsKey(type)) {
			throw new DependencyContainerException("Cannot register instance of type " + type.getName() + ". Duplicate registration.");
		}

		instances.put(type, instance);
		failedTypes.remove(type);
		
		return instance;
	}

	@Override
	public <T> T resolve(Class<T> type) {
		return getOrCreateInstanceOfType(type, new HashSet<>());
	}
	
	@Override
	public <T> T resolveAndRegisterInstance(Class<T> type) {
		T instance = resolve(type);
		registerInstance(instance);
		return instance;
	}

	private <T> T getOrCreateInstanceOfType(Class<T> type, Set<Class<?>> typesTriedConstructing) {
		throwIfTypeIsFailedType(type);

		if (instances.containsKey(type)) {
			return (T) instances.get(type);
		}

		if (type.isPrimitive()) {
			throw new DependencyContainerException("Cannot construct primitive type " + type.getName() + ".");
		}

		try {
			if (type.isInterface()) {
				if (typeRegistrations.containsKey(type)) {
					return (T) constructInstanceOfType(typeRegistrations.get(type), typesTriedConstructing);
				}
				else {
					throw new DependencyContainerException("Cannot resolve type " + type.getName() + ". Type is not registered.");
				}
			}

			return constructInstanceOfType(type, typesTriedConstructing);
		}
		catch (DependencyContainerException e) {
			failedTypes.add(type);
			throw e;
		}
	}

	private <T> T constructInstanceOfType(Class<T> type, Set<Class<?>> typesTriedConstructing) {
		if (typesTriedConstructing.contains(type)) {
			throw new DependencyContainerException("Cannot construct type " + type.getName() + ". Circular dependency resolution.");
		}
		typesTriedConstructing.add(type);
		
		Constructor<?>[] constructors = type.getConstructors();
		for (Constructor<?> constructor : constructors) {
			try {
				Class<?>[] paramTypes = constructor.getParameterTypes();
				Object[] paramArgs = Stream.of(paramTypes).map(t -> getOrCreateInstanceOfType(t, typesTriedConstructing)).toArray();
				
				try {
					return (T) constructor.newInstance(paramArgs);
				}
				finally {
					typesTriedConstructing.remove(type);
				}
			}
			catch (DependencyContainerException e) {
			}
			catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
				throw new DependencyContainerException("An exception occurred while constructing instance of type " + type.getName(), e);
			}
		}

		throw new DependencyContainerException("Cannot construct type " + type.getName() + ". Registration was not found.");
	}

	private void throwIfTypeIsFailedType(Class<?> type) {
		if (failedTypes.contains(type)) {
			throw new DependencyContainerException("Cannot construct type " + type.getName() + ". Registration was not found.");
		}
	}

}
