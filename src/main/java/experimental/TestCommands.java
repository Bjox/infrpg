package experimental;

import lib.cmd.Command;
import lib.cmd.CommandDispatcher;
import lib.cmd.CommandObject;
import lib.cmd.DefaultValue;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class TestCommands implements CommandObject {

	@Command
	public void foo(
		int a,
		@DefaultValue("lol") String b,
		@DefaultValue("true") boolean c) {
		System.out.printf("foo called with values: %d+%s+%b\n", a, b, c);
	}

	@Command
	public void add(int a, int b) {
		System.out.println(a + b);
	}

	public static void main(String[] args) throws Exception {
		CommandDispatcher dispatcher = new CommandDispatcher(new TestCommands());
		dispatcher.parse("foo 12 \"wat da fak\" false").join();
		dispatcher.parse("add 4 5").join();
		System.out.println(dispatcher);
	}

}
