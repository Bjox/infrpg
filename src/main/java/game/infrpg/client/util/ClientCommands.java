package game.infrpg.client.util;

import com.mojang.brigadier.CommandDispatcher;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ClientCommands {

	public static void main(String[] args) throws Exception {
		CommandDispatcher<Object> cmdDispatcher = new CommandDispatcher<>();

		cmdDispatcher.register(
			literal("connect")
				.then(
					argument("ip", string())
						.executes(c -> {
							System.out.println("ip is " + getString(c, "ip"));
							return 1;
						})
						.then(
							argument("port", integer())
								.executes(c -> {
									String ip = getString(c, "ip");
									int port = getInteger(c, "port");
									System.out.println("connecting to " + ip + ":" + port);
									return 1;
								}))
				)
				.executes(c -> {
					System.out.println("Not enough arguments");
					return 1;
				})
		);

		cmdDispatcher.execute("connect localhost 1234", new Object());
	}

}
