package de.htwg.sudoku.aview.tui;

import de.htwg.sudoku.aview.StatusMessage;
import de.htwg.sudoku.controller.ISudokuController;
import de.htwg.util.observer.Event;
import de.htwg.util.observer.IObserver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.inject.Inject;

public class TextUI implements IObserver {

	private static final int SMALL_SIZE = 1;
	private static final int MEDIUM_SIZE = 4;
	private static final int LARGE_SIZE = 9;
	private static final String NEWLINE = System.getProperty("line.separator");

	private static final Logger LOGGER = LogManager.getLogger(TextUI.class.getName());

	protected ISudokuController controller;

	@Inject
	public TextUI(ISudokuController controller) {
		this.controller = controller;
		controller.addObserver(this);
	}

	@Override
	public void update(Event e) {
		LOGGER.entry(toString());
	}

	public boolean processInputLine(String line) {
		boolean continu = true;
		if (line.matches("q")) {
			continu = false;
		} else if (line.matches("\\D")) {
			processSingleCharInput(line);
		} else
		// if the command line has the form 123, set the cell (1,2) to value 3
		if (line.matches("[0-9][0-9][0-9]")) {
			processTrippleDigitInput(line);
		} else
		// if the command line has the form 12, get the candidates of cell (1,2)
		if (line.matches("[0-9][0-9]")) {
			processDoubleDigitInput(line);
		} else 
		// if the command line has the form 5, highlight cells that have 5 as candidate
		if (line.matches("[0-9]")) {
			processSingleDigitInput(line);
		} else
			LOGGER.entry("Illegal command: " + line);
		return continu;
	}

	private void processSingleDigitInput(String line) {
		int[] arg = readToArray(line);
		controller.highlight(arg[0]);
	}

	private void processDoubleDigitInput(String line) {
		int[] arg = readToArray(line);
		controller.showCandidates(arg[0], arg[1]);
	}

	private void processTrippleDigitInput(String line) {
		int[] arg = readToArray(line);
		controller.setValue(arg[0], arg[1], arg[2]);
	}

	protected void processSingleCharInput(String line) {
		switch (line) {
		case "":
		case " ":
		case "f":
			controller.refresh();
			break;
		case "r":
			controller.reset();
			break;
		case "n":
			controller.create();
			break;
		case "s":
			controller.solve();
			break;
		case "u":
		case "z":
			controller.undo();
			break;
		case "y":
			controller.redo();
			break;
		case ".":
		case "-":
			controller.setGrid(SMALL_SIZE);
			break;
		case "+":
			controller.setGrid(MEDIUM_SIZE);
			break;
		case "#":
		case "*":
			controller.setGrid(LARGE_SIZE);
			break;
		default:
			LOGGER.entry("Illegal command: " + line);
		}
	}

	private int[] readToArray(String line) {
		Pattern p = Pattern.compile("[0-9]");
		Matcher m = p.matcher(line);
		int[] result = new int[line.length()];
		for (int i = 0; i < result.length; i++) {
			m.find();
			result[i] = Integer.parseInt(m.group());
		}
		return result;
	}

	@Override
	public String toString() {
		String result = "";
		result += NEWLINE + controller.getGridString();
		result += NEWLINE + StatusMessage.text.get(controller.getStatus()) + controller.getStatusText();
		result += NEWLINE
				+ "Possible commands: q-quit, n-new, r-reset, f-refresh, s-solve, u-undo .,+,#-size, xy-show (x,y), xyz-set (x,y) to z";
		return result;
	}

	public String toHtml() {
		String game = this.toString();
		String result = game.replace(NEWLINE, "<br>");
		result = result.replace("     ", " &nbsp; &nbsp; ");
		result = result.replace("   ", " &nbsp; ");
		return result;
	}
}
