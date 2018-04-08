package game.infrpg.common.console.util.logging;

import game.infrpg.common.console.Console;
import java.awt.Color;
import java.util.EnumMap;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class ConsoleHandler extends Handler {

	private static class Palette {

		public final Color debugColor;
		public final Color infoColor;
		public final Color wanringColor;
		public final Color errorColor;

		public static Color rgb(int r, int g, int b) {
			return new Color(r, g, b);
		}

		public Palette(Color debugColor, Color infoColor, Color wanringColor, Color errorColor) {
			this.debugColor = debugColor;
			this.infoColor = infoColor;
			this.wanringColor = wanringColor;
			this.errorColor = errorColor;
		}
	}

	private static enum Palettes {
		FUN_AND_PROFESSIONAL(new Palette(Palette.rgb(74, 189, 172), Palette.rgb(223, 220, 227), Palette.rgb(247, 183, 51), Palette.rgb(252, 74, 20))),
		RGB(new Palette(Color.CYAN, Color.WHITE, Color.YELLOW, Color.RED)),
		;

		public final Palette palette;

		private Palettes(Palette palette) {
			this.palette = palette;
		}

		public Color getColor(Level level) {
			switch (level) {
				case DEBUG:
					return palette.debugColor;
				case INFO:
					return palette.infoColor;
				case WARNING:
					return palette.wanringColor;
				case ERROR:
					return palette.errorColor;
				default:
					return Color.WHITE;
			}
		}
	}

	private static final Palettes PALETTE = Palettes.FUN_AND_PROFESSIONAL;

	@Override
	public void handle(LogRecord record, Logger logger) {
		Color levelColor = PALETTE.getColor(record.getLevel());

		Console.print("[" + record.getLevel().name() + "] ", levelColor);
		Console.print(record.getPrefix() + " ", Color.GRAY);
		Console.print(record.getMessage() + " ", Color.WHITE);
		Console.print(record.getSuffix() + " ", Color.GRAY);

		if (logger.getCurrentLevel().check(Level.DEBUG)) {
			Console.print("(" + record.getFileName() + ":" + record.getLine() + ")", Color.GRAY);
		}

		Console.println("");
	}

	@Override
	public void close() {
	}

}
