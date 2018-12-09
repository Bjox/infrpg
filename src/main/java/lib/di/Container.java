package lib.di;

import lib.di.exceptions.DIException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	//public static final ContainerLifetime DEFAULT_CONTAINER_LIFETIME = ContainerLifetime.PER_RESOLVE;
	private final Map<Class<?>, Object> instances;
	private final Map<Class<?>, Class<?>> typeRegistrations;

	public Container() {
		instances = new HashMap<>();
		typeRegistrations = new HashMap<>();
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
	}

	@Override
	public <I, T extends I> void registerSingleton(Class<I> interfaceType, Class<T> implementationType) {
		if (instances.containsKey(implementationType)) {
			if (!implementationType.isInstance(instances.get(implementationType))) {
				// This should in principle never happen, because objects in instances should always have their respective type as map key.
				throw new DIRegistrationException(interfaceType, implementationType,
					"implementationType is already mapped to instance of type " + instances.get(implementationType).getClass().getName());
			}
		}
		else {
			instances.put(implementationType, new Singleton());
		}

		registerType(interfaceType, implementationType);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <I, T extends I> T registerSingleton(Class<I> interfaceType, T instance) {
		Class<T> implementationType;

		try {
			implementationType = (Class<T>) instance.getClass();
		}
		catch (Exception e) {
			throw new DIRegistrationException(interfaceType, instance.getClass(), "cannot determine implementationType from instance.", e);
		}

		registerSingleton(interfaceType, implementationType);
		instances.put(implementationType, instance);

		return instance;
	}

	@Override
	public <T> void registerSingleton(Class<T> type) {
		throwIfInstancesContainsKey(type);

		instances.put(type, new Singleton());
	}

	@Override
	public <T> T registerInstance(T instance) {
		Class<?> type = instance.getClass();

		throwIfInstancesContainsKey(type);

		instances.put(type, instance);

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
		if (instances.containsKey(type)) {
			if (instances.get(type) instanceof Singleton) {
				constructSingleton(type, typesTriedConstructing);
			}
			return type.cast(instances.get(type));
		}

		if (type.isInterface()) {
			Class<?> mappedType = typeRegistrations.get(type);
			if (mappedType == null) {
				throw new DIResolutionException(type, "type is not registered.");
			}

			if (instances.containsKey(mappedType)) {
				if (instances.get(mappedType) instanceof Singleton) {
					constructSingleton(mappedType, typesTriedConstructing);
				}
				return type.cast(instances.get(mappedType));
			}

			Object instance = constructInstanceOfType(mappedType, typesTriedConstructing);
			return type.cast(instance);
		}

		return constructInstanceOfType(type, typesTriedConstructing);
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
		List<Constructor<?>> triedConstructors = new ArrayList<>();
		List<Throwable> exStack = new ArrayList<>();
		
		for (Constructor<?> constructor : type.getConstructors()) {
			if (!constructor.isAnnotationPresent(Inject.class)) {
				continue;
			}
			injectConstructorFound = true;
			triedConstructors.add(constructor);

			try {
				Class<?>[] paramTypes = constructor.getParameterTypes();
				Object[] paramArgs = Stream.of(paramTypes).map(
					t -> resolveRecursive(t, typesTriedConstructing)).toArray();

				try {
					Object instance = constructor.newInstance(paramArgs);
					return type.cast(instance);
				}
				finally {
					typesTriedConstructing.remove(type);
				}
			}
			catch (DIException e) {
				exStack.add(e);
			}
			catch (InvocationTargetException e) {
				throw new RuntimeException("An exception occurred while constructing instance of type " + type.getName(), e.getCause());
			}
			catch (IllegalAccessException | IllegalArgumentException | InstantiationException e) {
				throw new RuntimeException("An exception occurred while constructing instance of type " + type.getName(), e);
			}
		}

		if (!injectConstructorFound) {
			throw new DIResolutionException(type, "no @Inject annotated constructor was found on the type.");
		}

		//String str = triedConstructors.stream().map(c -> c.toString()).reduce("Constructors tried:", (a, b) -> a + "\n" + b);
		String str2 = exStack.stream().map(e -> e.getMessage()).reduce("", (a, b) -> a + b);
		throw new DIResolutionException(type, "resolution of type dependencies failed. " + str2);
	}

	private void constructSingleton(Class<?> type, Set<Class<?>> typesTriedConstructing) {
		instances.put(type, constructInstanceOfType(type, typesTriedConstructing));
	}

	private void throwIfInstancesContainsKey(Class<?> implementationType) {
		if (instances.containsKey(implementationType)) {
			if (!(instances.get(implementationType) instanceof Singleton)) {
				throw new DIRegistrationException(implementationType, "instance of type already registered.");
			}
		}
	}

	private static class Singleton {
	}

}
