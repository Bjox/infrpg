package lib.config;

import java.io.IOException;
import lib.storage.IStorage;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lib.cmd.TypeParser;

/**
 * Extend this class in order to create a custom configuration class.<br>
 * All configuration fields must have public accessability, be non-static,
 * and be annotated with the <code>@ConfigField</code> annotation. Additionally,
 * the class itself must have the <code>@ConfigClass</code> annotation.<br><br>
 * Remember to call <code>initConfig()</code> before using the configuration object.
 * 
 * @author Bjørnar W. Alvestad
 */
public abstract class AbstractConfig {

	private final IConfigStore configStore;
	private final IStorage storage;
	private final TypeParser typeParser;
	
	/**
	 * Constructor.
	 * @param configStore The key-value buffer backing this configuration.
	 * @param storage The data access layer used to persist the configuration.
	 * @param typeParser
	 * @throws IOException 
	 */
	public AbstractConfig(IConfigStore configStore, IStorage storage, TypeParser typeParser) throws IOException {
		this.configStore = configStore;
		this.storage = storage;
		this.typeParser = typeParser;
		
		throwIfClassLacksConfigClassAnnotation();
	}
	
	public void initConfig() throws IOException {
		try {
			loadConfig();
		}
		catch (IOException e) {
			// If error, store default values
			storeConfig();
		}
	}
	
	public void loadConfig() throws IOException {
		throwIfClassLacksConfigClassAnnotation();
		
		try (InputStream in = storage.getInputStream()) {
			configStore.load(in);
			forEachConfigField(this::setField);
		}
		catch (IOException e) {
			throw e;
		}
		catch (Exception e) {
			String str = "Error while reading configuration for configuration class \"" + getClass().getName()+ "\". " + e.getMessage();
			throw new RuntimeException(str, e);
		}
	}

	public void storeConfig() throws IOException {
		throwIfClassLacksConfigClassAnnotation();
		
		try (OutputStream out = storage.getOutputStream()) {
			forEachConfigField(this::readField);
			configStore.store(out);
		}
		catch (IOException e) {
			throw e;
		}
		catch (Exception e) {
			String str = "Error while storing configuration for configuration class \"" + getClass().getName()+ "\". " + e.getMessage();
			throw new RuntimeException(str, e);
		}
	}
	
	private void throwIfClassLacksConfigClassAnnotation() {
		if (!getClass().isAnnotationPresent(ConfigClass.class)) {
			throw new RuntimeException(
					"Cannot read configuration into " + getClass().getName() + ". The class does not have the @ConfigClass annotation.");
		}
	}
	
	public void forEachConfigField(Consumer<Field> operation) {
		configFieldStream().forEach(operation);
	}
	
	public Stream<Field> configFieldStream() {
		return Stream.of(getClass().getFields())
				.filter(this::hasConfigFieldAnnotation);
	}
	
	public Map<String, Object> getConfigKeyValueMap() {
		HashMap<String, Object> map = new HashMap<>();
		forEachConfigField(f -> map.put(f.getName(), getFieldValue(f)));
		return map;
	}
	
	private boolean hasConfigFieldAnnotation(Field field) {
		return field.isAnnotationPresent(ConfigField.class);
	}
	
	private boolean isStaticField(Field field) {
		return Modifier.isStatic(field.getModifiers());
	}
	
	private void setField(Field field) {
		try {
			Object key = getKey(field);

			if (!configStore.containsKey(key)) {
				return;
			}
			
			String strValue = configStore.getString(key);
			Class<?> type = field.getType();
			
			try {
				field.set(this, typeParser.parse(type, strValue));
			}
			catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		catch (Exception e) {
			String str = "Cannot read configuration field \"" + field.getName() + "\". " + e.getMessage();
			throw new RuntimeException(str, e);
		}

	}
	
	private void readField(Field field) {
		try {
			String value = String.valueOf(field.get(this));
			configStore.put(getKey(field), value);
		}
		catch (IllegalAccessException | IllegalArgumentException e) {
			String str = "Cannot store configuration field \"" + field.getName() + "\". " + e.getMessage();
			throw new RuntimeException(str, e);
		}
	}
	
	private Object getFieldValue(Field field) {
		try {
			return field.get(this);
		}
		catch (IllegalAccessException | IllegalArgumentException e) {
			String str = "Cannot get value of configuration field \"" + field.getName() + "\". " + e.getMessage();
			throw new RuntimeException(str, e);
		}
	}
	
	private Object getKey(Field field) {
		ConfigField fieldAnnotation = field.getAnnotation(ConfigField.class);

		String fieldName = fieldAnnotation.name();
		if ("".equals(fieldName)) {
			fieldName = field.getName();
		}
		
		return fieldName.toLowerCase();
	}
	
}
