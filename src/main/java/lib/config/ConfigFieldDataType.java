package lib.config;

import java.util.function.Function;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public enum ConfigFieldDataType {
	CHAR(s -> s.charAt(0)),
	STRING(s -> s),
	BOOLEAN(Boolean::parseBoolean),
	BYTE(Byte::parseByte),
	SHORT(Short::parseShort),
	INT(Integer::parseInt),
	LONG(Long::parseLong),
	FLOAT(Float::parseFloat),
	DOUBLE(Double::parseDouble);

	private final Function<String, ?> parseFunction;

	private ConfigFieldDataType(Function<String, ?> parseFunction) {
		this.parseFunction = parseFunction;
	}

	public Object parse(String value) throws Exception {
		try {
			return parseFunction.apply(value);
		}
		catch (Exception e) {
			String str = "Error while parsing " + name().toLowerCase() + ". " + e.getMessage();
			throw new Exception(str, e);
		}
	}
}
