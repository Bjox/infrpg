package lib.di;

import lib.di.exceptions.DIException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import lib.di.exceptions.DIRegistrationException;
import lib.di.exceptions.DIResolutionException;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class Container implements IContainer {

	// TODO: when constructing, check accessability (public etc.) cannot construct unaccessable types.
	
	private final Map<Class<?>, Object> instances;
	private final Map<Class<?>, Class<?>> typeRegistrations;
	@Deprecated
	private final Set<Class<?>> failedTypes;

	public Container() {
		instances = new HashMap<>();
		typeRegistrations = new HashMap<>();
		failedTypes = new HashSet<>();
	}

	@Override
	public <I, T extends I> void registerType(Class<I> interfaceType, Class<T> implementationType) {
		if (interfaceType.equals(implementationType)) {
			throw new DIRegistrationException(interfaceType, implementationType, "registration with itself is not allowed.");
		}
		
		if (!interfaceType.isInterface()) {
			throw new DIRegistrationException(interfaceType, implementationType, "interfaceType is not an interface.");
		}

		if (typeRegistrations.containsKey(interfaceType)) {
			throw new DIRegistrationException(
					interfaceType, implementationType, "interfaceType is already mapped to " + typeRegistrations.get(interfaceType).getName());
		}

		typeRegistrations.put(interfaceType, implementationType);
		failedTypes.remove(interfaceType);
	}

	@Override
	public <T> T registerInstance(T instance) {
		Class<?> type = instance.getClass();

		if (instances.containsKey(type)) {
			throw new DIRegistrationException(type, "instance of type already registered.");
		}

		instances.put(type, instance);
		failedTypes.remove(type);
		
		return instance;
	}

	@Override
	public <T> T resolve(Class<T> type) {
		return resolveRecursive(type, new HashSet<>());
	}
	
	@Override
	public <T> T resolveAndRegisterInstance(Class<T> type) {
		T instance = resolve(type);
		registerInstance(instance);
		return instance;
	}

	private <T> T resolveRecursive(Class<T> type, Set<Class<?>> typesTriedConstructing) {
		if (failedTypes.contains(type)) {
			throw new DIException("Cannot construct type " + type.getName() + ". Registration was not found.");
		}

		if (instances.containsKey(type)) {
			return (T) instances.get(type);
		}

		try {
			if (type.isInterface()) {
				if (typeRegistrations.containsKey(type)) {
					return (T) constructInstanceOfType(typeRegistrations.get(type), typesTriedConstructing);
				}
				else {
					throw new DIResolutionException(type, "type is not registered.");
				}
			}

			return constructInstanceOfType(type, typesTriedConstructing);
		}
		catch (DIException e) {
			failedTypes.add(type);
			throw e;
		}
	}

	private <T> T constructInstanceOfType(Class<T> type, Set<Class<?>> typesTriedConstructing) {
		if (type.isPrimitive()) {
			throw new DIResolutionException(type, "cannot construct primitive types.");
		}
		if (typesTriedConstructing.contains(type)) {
			throw new DIResolutionException(type, "circular dependency resolution.");
		}
		typesTriedConstructing.add(type);
		
		boolean injectConstructorFound = false;
		for (Constructor<?> constructor : type.getConstructors()) {
			if (!constructor.isAnnotationPresent(Inject.class)) {
				continue;
			}
			injectConstructorFound = true;
			
			try {
				Class<?>[] paramTypes = constructor.getParameterTypes();
				Object[] paramArgs = Stream.of(paramTypes).map(t ->
						resolveRecursive(t, typesTriedConstructing)).toArray();
				
				try {
					return (T) constructor.newInstance(paramArgs);
				}
				finally {
					typesTriedConstructing.remove(type);
				}
			}
			catch (DIException e) {
			}
			catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
				throw new DIResolutionException("An exception occurred while constructing instance of type " + type.getName(), e);
			}
		}
		
		if (!injectConstructorFound) {
			throw new DIResolutionException(type, "no @Inject annotated constructor was found on the type.");
		}
		
		throw new DIResolutionException(type, "resolution of type dependencies failed.");
	}

}
