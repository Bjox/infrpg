package lib.cmd;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class TestCommands implements CommandObject {

	@Command
	public void foo(
		int a,
		@Default("lol") String b,
		@Default("true") boolean c) {

		System.out.printf("foo called with values: %d %s %b\n", a, b, c);
	}

	public static void main(String[] args) throws Exception {
		CommandDispatcher<TestCommands> dispatcher = new CommandDispatcher<>(new TestCommands());
		dispatcher.parse("foo 12 \"wat da fak\" false");
	}

}
