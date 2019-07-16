package game.infrpg.common.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.*;
import lib.cmd.CommandInvocationException;
import lib.cmd.CommandParseException;

/**
 * A swing based console for printing info and retrieve commands.
 *
 * @author Viktor Zoric
 * @author Bjørnar Alvestad
 */
public final class Console extends JFrame {

    private static final int DOC_MAX_LENGTH = 20000;
    private static final String TITLE = "Console";
	private static final int HISTORY_MAX = 100;
	private static final Color COLOR_BACKGROUND = Color.DARK_GRAY.darker().darker();
	private static final Color COLOR_FOREGROUND = Color.LIGHT_GRAY;
	private static final Console console;
	private static final JTextPane textpane;
	private static final Style style;
	private static final JTextField input;
	private static final JPanel inputPanel;
	private static final JLabel label;
	private static final String[] history = new String[HISTORY_MAX];
	private static final StyledDocument doc;
	private static final DefaultStyledDocument defaultDoc;
	private static final List<Thread> shutdownHooks = new ArrayList<>();
	//private static final Map<String, Command> commands = new HashMap<>();
	private static Function<String, CompletableFuture<Void>> commandHook;
	
	private static int historyPointer = 0;
	private static int historyHead = 0;

	public static Color COLOR_ERROR_MSG = new Color(255, 74, 74);
	public static Color COLOR_WARNING_MSG = Color.ORANGE;
	public static Color COLOR_NOTICE_MSG = Color.YELLOW.brighter();
	public static Color COLOR_INFO_MSG = Color.WHITE;
	public static Color COLOR_SUCCESS_MSG = Color.GREEN.darker();
	public static Color COLOR_DEFAULT = Color.LIGHT_GRAY;
	public static Color COLOR_CMD = Color.WHITE;

	/**
	 * Decides whether or not to also print the log to standard output stream.
	 */
	public static final boolean PRINT_TO_STANDARD_OUT = false;

	/**
	 * If true, don't forward data when attached to either standard out or standard err.
	 */
	public static final boolean CONSUME_STANDARD_STREAMS = false;

	/**
	 * Original System.out assignment.
	 */
	private static final PrintStream STANDARD_OUT = System.out;

	/**
	 * Original System.err assignment.
	 */
	private static final PrintStream STANDARD_ERR = System.err;

	/**
	 * Private constructor.
	 */
	private Console() {
	}

	static {
		console = new Console();
		
		console.addWindowListener(new WindowClosedListener() {
			@Override
			public void windowClosed(WindowEvent e) {
				shutdownHooks.stream().forEach((t) -> {
					t.start();
				});
			}
		});

		try {
			Path path = Paths.get("data/icons/terminal-128.png");
			byte[] data = Files.readAllBytes(path);
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image img = kit.createImage(data);
			console.setIconImage(img);
		}
		catch (IOException e) {
		}
		
		console.setTitle(TITLE);
		console.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//setUndecorated(true);
		//setBackground(backgroundcolor);
		console.setResizable(true);
		console.setLayout(new BorderLayout(0, 0));
		defaultDoc = new DefaultStyledDocument();

		textpane = new JTextPane(defaultDoc);
		textpane.setEditable(false);
		textpane.setFocusable(true);
		textpane.setBackground(COLOR_BACKGROUND);
		textpane.setForeground(COLOR_FOREGROUND);
		textpane.setCaretColor(COLOR_FOREGROUND);
		textpane.setFont(new Font("Lucida Console", 0, 12));
		textpane.getCaret().setSelectionVisible(true);
		setConsoleSize(new Dimension(800, 400));
//		textpane.addFocusListener(new FocusListener() {
//			public void focusGained(FocusEvent e) {input.requestFocus();}
//			public void focusLost(FocusEvent e) {}
//		});

		doc = textpane.getStyledDocument();
		style = textpane.addStyle("Console Style", null);
		StyleConstants.setForeground(style, COLOR_FOREGROUND);

		JScrollPane scrollp = new JScrollPane(textpane);
		scrollp.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		scrollp.setBorder(null);
		scrollp.getVerticalScrollBar().setPreferredSize(new Dimension(14, 0));
		scrollp.getHorizontalScrollBar().setPreferredSize(new Dimension(5, 0));
		scrollp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected JButton createDecreaseButton(int orientation) {
				return createZeroButton();
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return createZeroButton();
			}

			@Override
			protected void configureScrollBarColors() {
				super.configureScrollBarColors(); //To change body of generated methods, choose Tools | Templates.
				this.thumbColor = Color.DARK_GRAY;
				this.thumbDarkShadowColor = Color.DARK_GRAY;
				this.thumbHighlightColor = Color.DARK_GRAY;
				this.thumbLightShadowColor = Color.DARK_GRAY;
				this.trackColor = Color.DARK_GRAY.darker();
			}

			private JButton createZeroButton() {
				JButton jbutton = new JButton();
				jbutton.setPreferredSize(new Dimension(0, 0));
				jbutton.setMinimumSize(new Dimension(0, 0));
				jbutton.setMaximumSize(new Dimension(0, 0));
				return jbutton;
			}
		});

		inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout(0, 0));
		inputPanel.setBackground(COLOR_BACKGROUND);
		inputPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, COLOR_BACKGROUND.darker().darker()));
		label = new JLabel(">>", SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(20, 20));
		label.setForeground(COLOR_FOREGROUND);
		label.setBackground(COLOR_BACKGROUND);
		label.setBorder(null);

		input = new JTextField();
		input.setBackground(COLOR_BACKGROUND);
		input.setForeground(COLOR_FOREGROUND);
		input.setFont(new Font("Monospaced", 0, 12));
		input.setBorder(null);
		input.setCaretColor(Color.WHITE);

		inputPanel.add(label, BorderLayout.WEST);
		inputPanel.add(input, BorderLayout.CENTER);
		console.add(inputPanel, BorderLayout.SOUTH);
		console.add(scrollp, BorderLayout.CENTER);

		console.pack();
		addKeyBindings();
		//DefaultCommands.addDefaultCommands(console);
		setConsoleForeground(COLOR_FOREGROUND);
	}

	public static synchronized void setConsoleSize(Dimension size) {
		textpane.setPreferredSize(size);
		if (console != null) {
			console.pack();
		}
	}

//	public static synchronized void addCommand(Command cmd) {
//		commands.put(cmd.getName().toLowerCase(), cmd);
//	}
//
//	public static synchronized String[] getCommandNames() {
//		String[] cmds = new String[commands.size()];
//		return commands.keySet().toArray(cmds);
//	}
//
//	public static synchronized String getCommandHelpString(String cmdName) {
//		if (commands.containsKey(cmdName)) {
//			return commands.get(cmdName).getHelp();
//		}
//		return null;
//	}
//
//	public static synchronized Command getCommand(String name) {
//		return commands.get(name);
//	}

	/**
	 * Adds a shutdown hook; a thread that will be started when the console window is being disposed.<br>
	 * Make sure the thread will terminate.
	 *
	 * @param r
	 */
	public static synchronized void addShutdownHook(Runnable r) {
		shutdownHooks.add(new Thread(r, "Console-Shutdownhook"));
	}

	/**
	 * Prints an Object to the Console. Automatically adds a line feed.
	 *
	 * @param arg0
	 */
	public static synchronized void println(Object arg0) {
		println(arg0, COLOR_DEFAULT);
	}

	/**
	 * Prints an Object to the Console. Automatically adds a line feed.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void println(Object arg0, Color color) {
		println(String.valueOf(arg0), color);
	}

	/**
	 * Prints a String to the Console. Automatically adds a line feed.
	 *
	 * @param arg0
	 */
	public static synchronized void println(String arg0) {
		print(arg0 + "\n");
	}

	/**
	 * Prints a String to the Console. Automatically adds a line feed.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void println(String arg0, Color color) {
		print(arg0 + "\n", color);
	}

	/**
	 * Prints an integer to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 */
	public static synchronized void println(int arg0) {
		println(String.valueOf(arg0));
	}

	/**
	 * Prints an integer to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void println(int arg0, Color color) {
		println(String.valueOf(arg0), color);
	}

	/**
	 * Prints a double to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 */
	public static synchronized void println(double arg0) {
		println(String.valueOf(arg0));
	}

	/**
	 * Prints a double to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void println(double arg0, Color color) {
		println(String.valueOf(arg0), color);
	}

	/**
	 * Prints a float to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 */
	public static synchronized void println(float arg0) {
		println(String.valueOf(arg0));
	}

	/**
	 * Prints a float to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void println(float arg0, Color color) {
		println(String.valueOf(arg0), color);
	}

	/**
	 * Prints a long to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 */
	public static synchronized void println(long arg0) {
		println(String.valueOf(arg0));
	}

	/**
	 * Prints a long to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void println(long arg0, Color color) {
		println(String.valueOf(arg0), color);
	}

	/**
	 * Prints a short to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 */
	public static synchronized void println(short arg0) {
		println(String.valueOf(arg0));
	}

	/**
	 * Prints a short to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void println(short arg0, Color color) {
		println(String.valueOf(arg0), color);
	}

	/**
	 * Prints a byte to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 */
	public static synchronized void println(byte arg0) {
		println(String.valueOf(arg0));
	}

	/**
	 * Prints a byte to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void println(byte arg0, Color color) {
		println(String.valueOf(arg0), color);
	}

	/**
	 * Prints a boolean to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 */
	public static synchronized void println(boolean arg0) {
		println(String.valueOf(arg0));
	}

	/**
	 * Prints a boolean to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void println(boolean arg0, Color color) {
		println(String.valueOf(arg0), color);
	}

	/**
	 * Prints a char to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 */
	public static synchronized void println(char arg0) {
		println(String.valueOf(arg0));
	}

	/**
	 * Prints a char to the console. Automatically adds aline feed.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void println(char arg0, Color color) {
		println(String.valueOf(arg0), color);
	}

	/**
	 * Prints a String to the Console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(String arg0) {
		print(arg0, COLOR_DEFAULT);
	}

	/**
	 * Prints a String to the Console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(Object arg0) {
		print(arg0.toString(), COLOR_DEFAULT);
	}

	/**
	 * Prints a String to the Console.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void print(String arg0, Color color) {
		if (PRINT_TO_STANDARD_OUT) {
			STANDARD_OUT.print(arg0);
		}
		if (console != null) {
			printString(arg0, color);
		}
	}

	/**
	 * Prints an integer to the console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(int arg0) {
		print(String.valueOf(arg0));
	}

	/**
	 * Prints an integer to the console.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void print(int arg0, Color color) {
		print(String.valueOf(arg0), color);
	}

	/**
	 * Prints a double to the console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(double arg0) {
		print(String.valueOf(arg0));
	}

	/**
	 * Prints a double to the console.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void print(double arg0, Color color) {
		print(String.valueOf(arg0), color);
	}

	/**
	 * Prints a float to the console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(float arg0) {
		print(String.valueOf(arg0));
	}

	/**
	 * Prints a float to the console.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void print(float arg0, Color color) {
		print(String.valueOf(arg0), color);
	}

	/**
	 * Prints a long to the console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(long arg0) {
		print(String.valueOf(arg0));
	}

	/**
	 * Prints a long to the console.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void print(long arg0, Color color) {
		print(String.valueOf(arg0), color);
	}

	/**
	 * Prints a short to the console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(short arg0) {
		print(String.valueOf(arg0));
	}

	/**
	 * Prints a short to the console.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void print(short arg0, Color color) {
		print(String.valueOf(arg0), color);
	}

	/**
	 * Prints a byte to the console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(byte arg0) {
		print(String.valueOf(arg0));
	}

	/**
	 * Prints a byte to the console.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void print(byte arg0, Color color) {
		print(String.valueOf(arg0), color);
	}

	/**
	 * Prints a boolean to the console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(boolean arg0) {
		print(String.valueOf(arg0));
	}

	/**
	 * Prints a boolean to the console.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void print(boolean arg0, Color color) {
		print(String.valueOf(arg0), color);
	}

	/**
	 * Prints a char to the console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(char arg0) {
		print(String.valueOf(arg0));
	}

	/**
	 * Prints a char to the console.
	 *
	 * @param arg0
	 * @param color
	 */
	public static synchronized void print(char arg0, Color color) {
		print(String.valueOf(arg0), color);
	}

	/**
	 * Sets the title of this console window.
	 *
	 * @param title the new title
	 */
	public static synchronized void setConsoleTitle(String title) {
		console.setTitle(title);
	}

	public static synchronized void clearScreen() {
		console.clear();
	}

	/**
	 * Opens up the Console-window.
	 */
	public static synchronized void showConsole() {
		console.setVisible(true);
	}

	/**
	 * Closes the Console-window.
	 */
	public static synchronized void hideConsole() {
		console.setVisible(false);
	}

	/**
	 * Disposes the console window. The executing console thread will stop.
	 */
	public static synchronized void destroyConsole() {
		console.dispose();
	}

	public static synchronized boolean exists() {
		return console != null;
	}

	public static synchronized void printException(Throwable ex) {
		StackTraceElement[] stackTr = ex.getStackTrace();
		Console.println("Exception in thread \"" + Thread.currentThread().getName() + "\" " + ex.toString(), Console.COLOR_ERROR_MSG);
		for (StackTraceElement e : stackTr) {
			Console.println("\tat " + e.toString(), Console.COLOR_ERROR_MSG);
		}
	}

	public static void stop() {
		System.out.println("Closing console.");
		console.dispose();
	}

	public static void setConsoleForeground(Color c) {
		if (c == null) {
			return;
		}
		COLOR_DEFAULT = c;
		COLOR_CMD = c.brighter();
		COLOR_INFO_MSG = c.brighter();
		textpane.setForeground(c);
		textpane.setCaretColor(c);
		label.setForeground(c);
		input.setForeground(c);
		StyleConstants.setForeground(style, c);
	}

	public static void setConsoleBackground(Color c) {
		if (c == null) {
			return;
		}
		textpane.setBackground(c);
		textpane.setCaretColor(getConstrastColor(c));
	}

	public static Color getConstrastColor(Color c) {
		double avg = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
		return avg < 128 ? Color.WHITE : Color.BLACK;
	}

	public static PrintStream getPrintStream(Color printColor) {
		return new PrintStreamInterceptor(printColor);
	}

	public static PrintStream getPrintStream() {
		return getPrintStream(COLOR_DEFAULT);
	}

	/**
	 * Intercept the standard error output to also print to the console.
	 */
	public static void attachToErr() {
		if (CONSUME_STANDARD_STREAMS) {
			System.setErr(new PrintStreamInterceptor(COLOR_ERROR_MSG));
		}
		else {
			System.setErr(new PrintStreamInterceptor(STANDARD_ERR, COLOR_ERROR_MSG));
		}
	}

	/**
	 * Intercept the standard output to also print to the console.
	 */
	public static void attachToOut() {
		if (CONSUME_STANDARD_STREAMS) {
			System.setOut(new PrintStreamInterceptor(COLOR_DEFAULT));
		}
		else {
			System.setOut(new PrintStreamInterceptor(STANDARD_OUT, COLOR_DEFAULT));
		}
	}

	public static void setInputEnabled(boolean enable) {
		input.setEnabled(enable);
	}

	private static Color negative(Color c) {
		return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
	}

	private static void clear() {
		textpane.setText("");
	}

	private static void parseCommand(String str) {
		String cmdstr = str.trim().replaceAll("\\s(\\s+)", " "); // ditch them whitespaces
		addToHistory(cmdstr);
		if (commandHook != null) {
			commandHook.apply(cmdstr).exceptionally(e ->
			{
				if (e != null)
				{
					if (e.getCause() != null && (e.getCause() instanceof CommandParseException || e.getCause() instanceof CommandInvocationException))
					{
						println(e.getCause().getMessage(), COLOR_ERROR_MSG);
					}
					else
					{
						printException(e);
					}
				}
				return null;
			});
		}
		
//		if (commands.isEmpty()) {
//			return;
//		}
//		
//		String[] args = cmdstr.split(" ");
//		Command cmd = commands.get(args[0]);
//		if (cmd == null) {
////			parseCommand("call "+s);
//			println("Command not found.", COLOR_ERROR_MSG);
//		}
//		else {
//			cmd.execute(args);
//		}
	}
	
	public static synchronized void setCommandHook(Function<String, CompletableFuture<Void>> callback) {
		commandHook = callback;
	}

	private static synchronized void printString(String arg0) {
		printString(arg0, COLOR_DEFAULT);
	}

	private static synchronized void printString(String arg0, java.awt.Color color) {
		if (arg0 == null) {
			return;
		}
		try {
		    trimDocument();
			int offset = doc.getLength();

			StyleConstants.setForeground(style, color);
			doc.insertString(offset, arg0, style);
			textpane.select(doc.getLength(), doc.getLength());
		}
		catch (BadLocationException e) {
		}
	}

	private static void trimDocument() {
	    int lenToRemove = Math.max(0, doc.getLength() - DOC_MAX_LENGTH);
	    try {
            doc.remove(0, lenToRemove);
        }
	    catch (BadLocationException e) {
        }
    }

	private static String historyBack() {
		int oldHistory = historyPointer;
		historyPointer--;
		if (historyPointer < 0) {
			historyPointer = HISTORY_MAX - 1;
		}
		String value = history[historyPointer];
		if (value == null) {
			historyPointer = oldHistory;
			return history[historyPointer];
		}
		return value;
	}

	private static String historyForwards() {
		int oldHistory = historyPointer;
		historyPointer++;
		if (historyPointer == HISTORY_MAX) {
			historyPointer = 0;
		}
		if (historyPointer == historyHead) {
			return "";
		}
		String value = history[historyPointer];
		if (value == null) {
			historyPointer = oldHistory;
			return history[historyPointer];
		}
		return value;
	}

	private static void addToHistory(String s) {
		history[historyHead++] = s;
		if (historyHead >= HISTORY_MAX) {
			historyHead = 0;
		}
		historyPointer = historyHead;
	}

	private static void addKeyBindings() {
		// --- ENTER ---
		Object defaultEnterKey = textpane.getInputMap().get(KeyStroke.getKeyStroke("ENTER"));
		Action defaultEnterAction = textpane.getActionMap().get(defaultEnterKey);
		Action enterAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String line = input.getText();
				input.setText("");
				print(">> ", COLOR_CMD.darker());
				println(line, COLOR_CMD);
				if (line != null) {
					parseCommand(line);
				}
			}
		};
		input.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
		input.getActionMap().put("enterAction", enterAction);

		// --- UP ---
		Action arrowUpAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				input.setText(historyBack());
			}
		};
		input.getInputMap().put(KeyStroke.getKeyStroke("UP"), "arrowUpAction");
		input.getActionMap().put("arrowUpAction", arrowUpAction);

		// --- DOWN ---
		Action arrowDownAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				input.setText(historyForwards());
			}
		};
		input.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "arrowDownAction");
		input.getActionMap().put("arrowDownAction", arrowDownAction);
	}

}
