package game.infrpg.common.console;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 *
 * @author Bjørnar W. Alvestad
 */
public class PrintStreamInterceptor extends PrintStream {

		private final Color printColor;
		
		public PrintStreamInterceptor(Color printColor) {
			this(new OutputStream() {
				@Override
				public void write(int b) throws IOException {}
			}, printColor);
		}
		
		public PrintStreamInterceptor(OutputStream out, Color printColor) {
			super(out, true);
			this.printColor = printColor;
		}

		@Override
		public void print(String s) {
			super.print(s);
			Console.print(s, printColor);
		}

		@Override
		public void print(Object obj) {
			super.print(obj);
			Console.print(String.valueOf(obj), printColor);
		}

		@Override
		public void print(boolean b) {
			super.print(b);
			Console.print(String.valueOf(b), printColor);
		}

		@Override
		public void print(char c) {
			super.print(c);
			Console.print(String.valueOf(c), printColor);
		}

		@Override
		public void print(double d) {
			super.print(d);
			Console.print(String.valueOf(d), printColor);
		}

		@Override
		public void print(float f) {
			super.print(f);
			Console.print(String.valueOf(f), printColor);
		}

		@Override
		public void print(int i) {
			super.print(i);
			Console.print(String.valueOf(i), printColor);
		}

		@Override
		public void print(long l) {
			super.print(l);
			Console.print(String.valueOf(l), printColor);
		}
		
		@Override
		public void println(String x) {
			super.println(x);
			Console.print(x + "\n");
		}

		@Override
		public void println(Object x) {
			super.println(x);
			Console.print(String.valueOf(x) + "\n");
		}

		@Override
		public void println(boolean x) {
			super.println(x);
			Console.print(String.valueOf(x) + "\n");
		}

		@Override
		public void println(char x) {
			super.println(x);
			Console.print(String.valueOf(x) + "\n");
		}

		@Override
		public void println(double x) {
			super.println(x);
			Console.print(String.valueOf(x) + "\n");
		}

		@Override
		public void println(float x) {
			super.println(x);
			Console.print(String.valueOf(x) + "\n");
		}

		@Override
		public void println(int x) {
			super.println(x);
			Console.print(String.valueOf(x) + "\n");
		}

		@Override
		public void println(long x) {
			super.println(x);
			Console.print(String.valueOf(x) + "\n");
		}

		@Override
		public void println() {
			super.println();
			Console.print("\n");
		}

	}
