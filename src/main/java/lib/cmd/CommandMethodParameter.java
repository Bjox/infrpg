package lib.cmd;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class CommandMethodParameter {
	
	public final String name;
	public final Class<?> type;
	public final Object defaultValue;

	public CommandMethodParameter(String name, Class<?> type, Object defaultValue) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	@Override
	public String toString() {
		if (defaultValue == null) {
			return String.format("<%s>", name);
		}
		return String.format("[%s=%s]", name, defaultValue);
	}
	
}
