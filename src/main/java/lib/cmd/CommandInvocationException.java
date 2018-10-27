package lib.cmd;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class CommandInvocationException extends RuntimeException
{
	public CommandInvocationException(Throwable cause)
	{
		super(cause);
	}
	
	public CommandInvocationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
