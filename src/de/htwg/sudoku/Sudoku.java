package de.htwg.sudoku;

import de.htwg.sudoku.aview.gui.SudokuFrame;
import de.htwg.sudoku.aview.tui.TextUI;
import de.htwg.sudoku.controller.ISudokuController;
import de.htwg.sudoku.controller.impl.SudokuController;

import java.util.Scanner;

public final class Sudoku {
	/* Fields */
	private static Scanner scanner;
	private static TextUI tui;
	@SuppressWarnings("unused")
	private static SudokuFrame gui;
	protected static ISudokuController controller;

	/* Constructor */
	private Sudoku() {

	}

	/* Methods */
	public static void main(String[] args) {

		// Build up the application
		controller = new SudokuController(9);

        gui = new SudokuFrame(controller);
		tui = new TextUI(controller);

		// Create an initial game
		controller.create();

		if (args == null) {
			// continue to read user input on the tui until the user decides to
			// quit
			boolean continu = true;
			scanner = new Scanner(System.in);
			while (continu) {
				continu = tui.processInputLine(scanner.next());
			}
		} else {
			for (String input:args) {
				tui.processInputLine(input);
			}

		}
	}

}
