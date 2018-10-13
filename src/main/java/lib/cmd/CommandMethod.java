package lib.cmd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class CommandMethod {
	
	private final Method method;
	private final CommandMethodParameter[] parameters;
	private final int minNumArguments;
	private final int numArguments;

	public CommandMethod(Method method, TypeParser parser) {
		this.method = method;
		this.parameters = new CommandMethodParameter[method.getParameterCount()];
		
		this.numArguments = method.getParameterCount();
		
		boolean shouldHaveDefaultValueAtThisPoint = false;
		
		int numParamsWithDefaultValue = 0;
		int i = 0;
		for (Parameter param : method.getParameters()) {
			Default defAnnotation = param.getAnnotation(Default.class);
			boolean isDefAnnotationPresent = defAnnotation != null;
			
			if (shouldHaveDefaultValueAtThisPoint && !isDefAnnotationPresent) {
				throw new RuntimeException("Error validating command \"" + getName() +
					"\". Parameters with default values must be specified at the end of the parameter list.");
			}
			
			Object defaultValueParsed = null;
			
			if (isDefAnnotationPresent) {
				numParamsWithDefaultValue++;
				shouldHaveDefaultValueAtThisPoint = true;
				String defaultValue = defAnnotation.value();
				defaultValueParsed = parser.parse(param.getType(), defaultValue);
			}
			
			parameters[i] = new CommandMethodParameter(param.getName(), param.getType(), defaultValueParsed);
			
			i++;
		}
		
		this.minNumArguments = numArguments - numParamsWithDefaultValue;
	}
	
	public String getName() {
		return method.getName();
	}
	
	/**
	 * 
	 * @param args Arguments to the command, where index 0 is the command itself.
	 * @param parser
	 * @return 
	 * @throws lib.cmd.CommandParseException 
	 */
	public Object[] parseArguments(String[] args, TypeParser parser) throws CommandParseException {
		int argsSupplied = args.length - 1;
		
		if (argsSupplied < getMinNumArguments()) {
			throw new CommandParseException(
				"Not enough arguments provided to command \"" + getName() + "\". Expected " + getMinNumArguments() + ", got " + argsSupplied);
		}
		if (argsSupplied > getNumArguments()) {
			throw new CommandParseException(
				"Too many arguments provided to command \"" + getName() + "\". Expected " + getNumArguments()+ ", got " + argsSupplied);
		}
		
		Object[] argValues = new Object[getNumArguments()];
		for (int i = 0; i < getNumArguments(); i++) {
			boolean useDefaultValue = (i+1) >= args.length;
			CommandMethodParameter parameter = parameters[i];
			
			Object argValue;
			if (useDefaultValue) {
				argValue = parameter.defaultValue;
			}
			else {
				String argStr = args[i+1];
				argValue = parser.parse(parameter.type, argStr);
			}
			argValues[i] = argValue;
		}
		
		return argValues;
	}
	
	public int getMinNumArguments() {
		return minNumArguments;
	}
	
	public int getNumArguments() {
		return numArguments;
	}
	
	public void invoke(Object[] args, CommandObject cmdObject) throws
		IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		method.invoke(cmdObject, args);
	}
	
}
