package lib.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Very simple argument parser. Options must start with dash: "-".
 * @author Bjørnar W. Alvestad
 * @param <T> Argument enum
 */
public class ArgumentParser<T extends Enum<T>> implements IArgumentParser<T> {
	
	private final Map<String, String> map;
	
	/**
	 * 
	 * @param args The String[] arguement passed to the main method.
	 */
	public ArgumentParser(String[] args) {
		this.map = new HashMap<>();
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.startsWith("-")) {
				String value = null;
				if (i+1 < args.length && !args[i+1].startsWith("-")) value = args[i+1];
				map.put(arg, value);
			}
		}
	}
	
	
	@Override
	public boolean isPresent(String option) {
		if (!option.startsWith("-")) option = "-" + option;
		return map.containsKey(option);
	}
	
	
	@Override
	public boolean isPresent(T option) {
		return isPresent(argumentEnumToString(option));
	}
	
	
	@Override
	public String getString(String option) {
		return map.get(option);
	}
	
	
	@Override
	public int getInt(String option) {
		return Integer.parseInt(map.get(option));
	}
	
	
	@Override
	public double getDouble(String option) {
		return Double.parseDouble(map.get(option));
	}
	
	@Override
	public int numArguments() {
		return map.size();
	}
	
	public void printAllOptions() {
		map.forEach((K, V) -> {
			if (V == null) System.out.println(K);
			else System.out.println(K + "=" + V);
		});
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("Arguments: ");
		map.forEach((K, V) -> {
			str.append(K);
			if (V != null) str.append(" ").append(V);
			str.append(" ");
		});
		return str.toString();
	}
	
	private String argumentEnumToString(T argument) {
		return argument.toString().toLowerCase();
	}
	
}
