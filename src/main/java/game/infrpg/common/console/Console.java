package game.infrpg.common.console;

import game.infrpg.common.console.cmd.Command;
import game.infrpg.common.console.cmd.DefaultCommands;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.*;

/**
 * A swing based console for printing info and retrieve commands.
 * 
 * @author Viktor Zoric
 * @author Bjørnar Alvestad
 */
public final class Console extends JFrame {

	private final static int HISTORY_MAX = 15; // max length history buffer
	
	private static Console               console;
	private static JTextPane             textpane;
	private static Style                 style;
	private static JTextField            input;
	private static JPanel                inputPanel;
	private static JLabel                label;
	private static Color                 backgroundcolor = Color.DARK_GRAY.darker().darker();
	private static Color                 foregroundColor = Color.LIGHT_GRAY;
	private static int                   hIndex = 0;
	private static int                   headIndex = 0;
	private static String[]              history = new String[HISTORY_MAX];
	private static StyledDocument        doc;
	private static DefaultStyledDocument document;
	private static int                   carrigeReturnOffset;

	/** Predefined color for a typical error message. */
	public static Color ERROR_MSG = new Color(255, 74, 74);
	
	/** Predefined color for a typical notice message. */
	public static Color WARNING_MSG = Color.ORANGE;
	
	/** Predefined color for a typical warning message. */
	public static Color NOTICE_MSG = Color.YELLOW.brighter();
	
	/** Predefined color for a typical info message. */
	public static Color INFO_MSG = Color.WHITE;
	
	/** Predefined color for a typical success message. */
	public static Color SUCCESS_MSG = Color.GREEN.darker();
	
	/** The default color for console messages. */
	public static Color DEFAULT_COLOR = Color.LIGHT_GRAY;
	
	/** The default color for console messages. */
	public static Color CMD_COLOR = Color.WHITE;

	/** Decides whether or not to also print the log to standard output stream. */
	public static final boolean PRINT_TO_STANDARD_OUT = false;
	/** Decides whether or not to also print the log to standard error output stream. */
	public static final boolean PRINT_TO_STANDARD_ERR = false;
	
	private static final PrintStream STANDARD_OUT = System.out;
	private static final PrintStream STANDARD_ERR = System.err;
	private static final long serialVersionUID = 1L;

	/** A list of shutdown hook threads that will be started when the console is disposed. */
	private static final ArrayList<Thread>        shutdownHooks = new ArrayList<>();
	
	//private static final ArrayList<PrintStream>   printStreams = new ArrayList<>();
	
	/** HashMap containing all the current console commands. Maps command name to Command object. */
	private static final HashMap<String, Command> commands = new HashMap<>();
	
	//private static final HashMap<String, String>  variables = new HashMap<>();
	
	/**
	 * Creates a new Console window. This should only be done at the start of the application.
	 */
	public Console() {
		this(null);
	}

	
	/**
	 * Creates a new Console window. This should only be done at the start of the application.
	 */
	private Console(String title) {
		addWindowListener(new WindowClosedListener() {
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
			setIconImage(img);
		} catch (IOException e){}
		
		setTitle(title == null ? "Console" : title + " - Console");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//setUndecorated(true);
		//setBackground(backgroundcolor);
		setResizable(true);
		setLayout(new BorderLayout(0, 0));
		document = new DefaultStyledDocument();
		
		textpane = new JTextPane(document);
		textpane.setEditable(false);
		textpane.setFocusable(true);
		textpane.setBackground(backgroundcolor);
		textpane.setForeground(foregroundColor);
		textpane.setCaretColor(foregroundColor);
		textpane.setFont(new Font("Lucida Console", 0, 12));
		textpane.getCaret().setSelectionVisible(true);
		setConsoleSize(new Dimension(800, 400));
//		textpane.addFocusListener(new FocusListener() {
//			public void focusGained(FocusEvent e) {input.requestFocus();}
//			public void focusLost(FocusEvent e) {}
//		});

		doc = textpane.getStyledDocument();
		style = textpane.addStyle("Console Style", null);
		StyleConstants.setForeground(style, foregroundColor);

		JScrollPane scrollp = new JScrollPane(textpane);
		scrollp.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
		scrollp.setBorder(null);
		scrollp.getVerticalScrollBar().setPreferredSize(new Dimension(14, 0));
		scrollp.getHorizontalScrollBar().setPreferredSize(new Dimension(5, 0));
		scrollp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
			@Override
			protected JButton createDecreaseButton(int orientation) {return createZeroButton();}
			@Override
			protected JButton createIncreaseButton(int orientation) {return createZeroButton();}
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
		inputPanel.setLayout(new BorderLayout(0,0));
		inputPanel.setBackground(backgroundcolor);
		inputPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, backgroundcolor.darker().darker()));
		label = new JLabel(">>", SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(20, 20));		
		label.setForeground(foregroundColor);
		label.setBackground(backgroundcolor);
		label.setBorder(null);
		
		input = new JTextField();
		input.setBackground(backgroundcolor);
		input.setForeground(foregroundColor);
		input.setFont(new Font("Monospaced", 0, 12));
		input.setBorder(null);
		input.setCaretColor(Color.WHITE);
		
		inputPanel.add(label, BorderLayout.WEST);
		inputPanel.add(input, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
		add(scrollp, BorderLayout.CENTER);
		
		pack();
		Arrays.fill(history, "");
		console = this;
		addKeyBindings();
		DefaultCommands.addDefaultCommands(this);
		setConsoleForeground(foregroundColor);
		carrigeReturnOffset = 0;
		//		slp(100); // to avoid getting a fucking retarted nullpointer somewhere in awt event queue for no damn reason
	}
	
	
	public static synchronized void setConsoleSize(Dimension size) {
		textpane.setPreferredSize(size);
	}
	
	
	public static synchronized void addCommand(Command cmd) {
		commands.put(cmd.getName().toLowerCase(), cmd);
	}
	
	
	public static synchronized String[] getCommandNames() {
		String[] cmds = new String[commands.size()];
		return commands.keySet().toArray(cmds);
	}
	
	
	public static synchronized String getCommandHelpString(String cmdName) {
		if (commands.containsKey(cmdName))
			return commands.get(cmdName).getHelp();
		return null;
	}
	
	
	public static synchronized Command getCommand(String name) {
		return commands.get(name);
	}

	
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
	 * Creates a new console with title
	 * 
	 * @param title
	 */
	public static synchronized void createConsole(String title) {
		if (console != null) {
			System.out.println("Console has already been created!");
			return;
		}
		console = new Console(title);
	}

	
	/**
	 * Creates a new console
	 */
	public static synchronized void createConsole() {
		createConsole(null);
	}

	
//	public static void setCursorPos(int row, int col) {
//		throw new UnsupportedOperationException("Not supported yet.");
//	}
	
	
	/**
	 * Terminates the JVM by calling the System.exit() method.
	 */
//	public static void shutdown() {
//		System.exit(0);
//	}

	
	/**
	 * Prints an Object to the Console. Automatically adds a line feed.
	 * 
	 * @param arg0 
	 */
	public static synchronized void println(Object arg0) {
		println(arg0, DEFAULT_COLOR);
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
		print(arg0, DEFAULT_COLOR);
	}

	/**
	 * Prints a String to the Console.
	 *
	 * @param arg0
	 */
	public static synchronized void print(Object arg0) {
		print(arg0.toString(), DEFAULT_COLOR);
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
			console.printString(arg0, color);
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
		if (console == null) {
			createConsole();
		}
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
		if (console != null) {
			console.dispose();
			console = null;
		}
	}

	
	public static synchronized boolean exists() {
		return console != null;
	}

	
	public synchronized Console getConsole() {
		return this;
	}

	
	public static synchronized void printException(Exception ex) {
		StackTraceElement[] stackTr = ex.getStackTrace();
		Console.println("Exception in thread \"" + Thread.currentThread().getName() + "\" " + ex.toString(), Console.ERROR_MSG);
		for (StackTraceElement e : stackTr) {
			Console.println("\tat " + e.toString(), Console.ERROR_MSG);
		}
	}

	
	public void stop() {
		System.out.println("Closing console.");
		console.dispose();
	}
	
	
	public static void setConsoleForeground(Color c) {
		if (c == null) {
			return;
		}
		DEFAULT_COLOR = c;
		CMD_COLOR = c.brighter();
		INFO_MSG = c.brighter();
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
		return getPrintStream(DEFAULT_COLOR);
	}
	
	/**
	 * Intercept the standard error output to also print to the console.
	 */
	public static void attachToErr() {
		if (PRINT_TO_STANDARD_ERR)
			System.setErr(new PrintStreamInterceptor(STANDARD_ERR, ERROR_MSG));
		else
			System.setErr(new PrintStreamInterceptor(ERROR_MSG));
	}
	
	/**
	 * Intercept the standard output to also print to the console.
	 */
	public static void attachToOut() {
		System.setOut(new PrintStreamInterceptor(DEFAULT_COLOR));
	}
	
	
	public static void setInputEnabled(boolean enable) {
		input.setEnabled(enable);
	}
	
	
	public static Color negative(Color c) {
		return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
	}
	
	
//=============================================================================
//                           PRIVATE	
//=============================================================================
	
	private static void slp(int ms){
		try {Thread.sleep(ms);} catch (Exception e){}
	}
	
	private void clear() {
		textpane.setText("");
	}
	

	private void parseCommand(String s) {
		s = s.trim().replaceAll("\\s(\\s+)", " "); // ditch them whitespaces
		addToHistory(s);
		String[] args = s.split(" ");
		Command cmd = commands.get(args[0]);
		if (cmd == null) {
//			parseCommand("call "+s);
			println("Command not found.", ERROR_MSG);
		} else {
			cmd.execute(args);
		}
	}
	
	
	private synchronized void printString(String arg0) {
		printString(arg0, DEFAULT_COLOR);
	}

	
	private synchronized void printString(String arg0, java.awt.Color color) {
		if (arg0 == null) {
			return;
		}
		try {
			int offset = doc.getLength();
			int lf = arg0.lastIndexOf("\n");
//			int cr = arg0.indexOf("\r");
//			if (cr != -1) {
//				doc.remove(carrigeReturnOffset, offset-carrigeReturnOffset);
//				offset = carrigeReturnOffset;
//			}
			
			StyleConstants.setForeground(style, color);
			doc.insertString(offset, arg0, style);
			textpane.select(doc.getLength(), doc.getLength());
			
			if (lf == arg0.length()-1) {
				carrigeReturnOffset = doc.getLength();
			}
		} catch (BadLocationException e) {}
	}
	
	
	private String historyBack() {
		int loop = 0;
		do {
			if (hIndex > 0) {
				hIndex--;
			} else {
				hIndex = HISTORY_MAX-1;
			}
			loop++;
		} while (history[hIndex].isEmpty() && loop < HISTORY_MAX);
		return history[hIndex];
	}

	
	private String historyForwards() {
		int loop = 0;
		do {
			if (hIndex < HISTORY_MAX-1) {
				hIndex++;
			} else {
				hIndex = 0;
			}
			loop++;
		} while (history[hIndex].isEmpty() && loop < HISTORY_MAX);
		return history[hIndex];
	}

	
	private void addToHistory(String s) {
		if (headIndex < 9) {
			headIndex++;
		} else {
			headIndex = 0;
		}
		hIndex = headIndex;
		history[hIndex] = s;
	}
	

	private void addKeyBindings() {
		// --- ENTER ---
		Object defaultEnterKey = textpane.getInputMap().get(KeyStroke.getKeyStroke("ENTER"));
		Action defaultEnterAction = textpane.getActionMap().get(defaultEnterKey);
		Action enterAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				String line = input.getText();
				input.setText("");
				print(">> ", CMD_COLOR.darker());
				println(line, CMD_COLOR);
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
	
	
	private static void showMeDerp() {

		Runnable r = new Runnable() {
			JWindow jw;

			class Derp extends Component {

				public BufferedImage img;

				public Derp(BufferedImage img) {
					this.img = img;
				}

				public void paint(Graphics g) {
					g.drawImage(img, 0, 0, null);
				}

				public Dimension getPreferredSize() {
					return new Dimension(img.getWidth(null), img.getHeight(null));
				}
			}

			public void make() {
				BufferedImage img;
				try {
					img = ImageIO.read(new File("res/img/derp.png"));
				} catch (Exception e) {
					return;
				}
				jw = new JWindow();
				jw.add(new Derp(img));
				jw.pack();
				jw.setAlwaysOnTop(true);
				jw.setLocation(0, 0);
				jw.setVisible(true);
			}

			public void run() {
				make();
				java.util.Random rnd = new java.util.Random();
				int max = rnd.nextInt(9000) + 1000;
				Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				Dimension pic = jw.getSize();
				int x = rnd.nextInt(screen.width), y = rnd.nextInt(screen.height), t = 0;
				boolean xup = true, yup = true;
				while (t < max) {
					try {
						Thread.sleep(5); //20
						jw.setLocation(x, y);
						if (x + pic.width > screen.width && xup) {
							xup = false;
						}
						if (x < 0 && !xup) {
							xup = true;
						}
						if (y + pic.height > screen.height && yup) {
							yup = false;
						}
						if (y < 0 && !yup) {
							yup = true;
						}
						if (yup) {
							y++;
						} else {
							y--;
						}
						if (xup) {
							x++;
						} else {
							x--;
						}
						t++;
					} catch (Exception e) {
					}
				}
				jw.dispose();
			}
		};
		new Thread(r, "Derp").start();
	}
	
	
	private static class PrintStreamInterceptor extends PrintStream {

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
			Console.print("\n");
		}

		@Override
		public void println(Object x) {
			super.println(x);
			Console.print("\n");
		}

		@Override
		public void println(boolean x) {
			super.println(x);
			Console.print("\n");
		}

		@Override
		public void println(char x) {
			super.println(x);
			Console.print("\n");
		}

		@Override
		public void println(double x) {
			super.println(x);
			Console.print("\n");
		}

		@Override
		public void println(float x) {
			super.println(x);
			Console.print("\n");
		}

		@Override
		public void println(int x) {
			super.println(x);
			Console.print("\n");
		}

		@Override
		public void println(long x) {
			super.println(x);
			Console.print("\n");
		}

		@Override
		public void println() {
			super.println();
			Console.print("\n");
		}

	}
	
}
