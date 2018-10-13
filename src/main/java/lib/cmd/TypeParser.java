package lib.cmd;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class TypeParser {
	
	private final Map<Class<?>, Function<String, ?>> parserFunctions;

	public TypeParser() {
		this(true);
	}
	
	public TypeParser(boolean addDefaultParsers) {
		parserFunctions = new HashMap<>();
		
		if (addDefaultParsers) addDefaultParsers();
	}
	
	private void addDefaultParsers() {
		addParser(String.class, s -> s);
		addParser(Character.TYPE, s -> s.charAt(0));
		addParser(Boolean.TYPE, Boolean::parseBoolean);
		addParser(Byte.TYPE, Byte::parseByte);
		addParser(Short.TYPE, Short::parseShort);
		addParser(Integer.TYPE, Integer::parseInt);
		addParser(Long.TYPE, Long::parseLong);
		addParser(Float.TYPE, Float::parseFloat);
		addParser(Double.TYPE, Double::parseDouble);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T parse(Class<T> type, String value) {
		if (!parserFunctions.containsKey(type)) {
			throw new RuntimeException("Cannot parse value \"" + value + "\". No parser for type \"" + type.getSimpleName() + "\" was added.");
		}
		return (T)parserFunctions.get(type).apply(value);
	}
	
	public <T> void addParser(Class<T> type, Function<String, T> parserFunction) {
		if (parserFunctions.containsKey(type)) {
			throw new RuntimeException("Parser for type \"" + type.getSimpleName() + "\" is already added.");
		}
		parserFunctions.put(type, parserFunction);
	}
	
}
