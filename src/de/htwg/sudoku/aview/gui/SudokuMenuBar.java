package de.htwg.sudoku.aview.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.htwg.sudoku.controller.ISudokuController;

public class SudokuMenuBar extends JMenuBar{

	private static final long serialVersionUID = -4482501416575327082L;

	private static final Logger LOGGER = LogManager.getLogger(SudokuMenuBar.class.getName());

	private static final int SIZE_1BY1 = 1;
	private static final int SIZE_4BY4 = 4;
	private static final int SIZE_9BY9 = 9;


	
	ISudokuController controller;

	JMenu fileMenu;
	JMenuItem newMenuItem, loadMenuItem, saveMenuItem, quitMenuItem;

	JMenu editMenu;
	JMenuItem undoMenuItem, redoMenuItem, copyMenuItem, pasteMenuItem;

	JMenu solveMenu;
	JMenuItem solveMenuItem;

	JMenu digitMenu;
	JMenuItem noneMenuItem, digitMenuItem;

	JMenu optionsMenu;
	JMenuItem showMenuItem, resize9MenuItem, resize4MenuItem, resize1MenuItem;

	public SudokuMenuBar(ISudokuController controller, JFrame frame) {
		this.controller = controller;
		createFileMenu(controller, frame);
		createEditMenu(controller);
		createSolveMenu(controller);
		createHighlightMenu(controller);
		createOptionsMenu(controller);		
	}

	private void createOptionsMenu(ISudokuController controller) {
		optionsMenu = new JMenu("Options");
		optionsMenu.setMnemonic(KeyEvent.VK_O);

		showMenuItem = new JMenuItem("toggle show Candidates");
		showMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showAllCandidates();
			}
		});
		showMenuItem.setMnemonic(KeyEvent.VK_S);
		showMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));

		optionsMenu.add(showMenuItem);

		resize9MenuItem = new JMenuItem("Resize to 9*9");
		resize9MenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.resetSize(SIZE_9BY9);
			}
		});
		resize9MenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_NUMBER_SIGN, 0));
		optionsMenu.add(resize9MenuItem);

		resize4MenuItem = new JMenuItem("Resize to 4*4");
		resize4MenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.resetSize(SIZE_4BY4);
			}
		});
		resize4MenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, 0));
		optionsMenu.add(resize4MenuItem);

		resize1MenuItem = new JMenuItem("Resize to 1*1");
		resize1MenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.resetSize(SIZE_1BY1);
			}
		});
		resize1MenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0));
		optionsMenu.add(resize1MenuItem);
		this.add(optionsMenu);
	}

	private void createHighlightMenu(ISudokuController controller) {
		digitMenu = new JMenu("Highlight");
		digitMenu.setMnemonic(KeyEvent.VK_H);

		noneMenuItem = new JMenuItem("none");
		noneMenuItem.setMnemonic(KeyEvent.VK_0);
		noneMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		noneMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				controller.highlight(0);
			}

		});
		digitMenu.add(noneMenuItem);

		int[] fkey = { KeyEvent.VK_F10, KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F4, KeyEvent.VK_F5,
				KeyEvent.VK_F6, KeyEvent.VK_F7, KeyEvent.VK_F8, KeyEvent.VK_F9 };
		int[] dkey = { KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5,
				KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9 };

		for (int digit = 1; digit <= controller.getSize(); digit++) {
			final int fixdigit = digit;
			digitMenuItem = new JMenuItem(Integer.toString(digit));
			digitMenuItem.setMnemonic(dkey[digit]);
			digitMenuItem.setAccelerator(KeyStroke.getKeyStroke(fkey[digit], 0));
			digitMenu.add(digitMenuItem);
			digitMenuItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent arg0) {
					controller.highlight(fixdigit);
				}
			});
		}

		this.add(digitMenu);
	}

	private void createSolveMenu(ISudokuController controller) {
		solveMenu = new JMenu("Solve");
		solveMenu.setMnemonic(KeyEvent.VK_S);

		solveMenuItem = new JMenuItem("Solve");
		solveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.solve();
			}
		});
		solveMenuItem.setMnemonic(KeyEvent.VK_S);
		solveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));

		solveMenu.add(solveMenuItem);
		this.add(solveMenu);
	}

	private void createEditMenu(ISudokuController controller) {
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		undoMenuItem = new JMenuItem("Undo");
		undoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.undo();
			}
		});
		undoMenuItem.setMnemonic(KeyEvent.VK_U);
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(undoMenuItem);

		redoMenuItem = new JMenuItem("Redo");
		redoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.redo();
			}
		});
		redoMenuItem.setMnemonic(KeyEvent.VK_R);
		redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(redoMenuItem);

		editMenu.addSeparator();

		copyMenuItem = new JMenuItem("Copy");
		copyMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.copy();
			}
		});
		copyMenuItem.setMnemonic(KeyEvent.VK_C);
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(copyMenuItem);

		pasteMenuItem = new JMenuItem("Paste");
		pasteMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.paste();
			}
		});
		pasteMenuItem.setMnemonic(KeyEvent.VK_P);
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		editMenu.add(pasteMenuItem);

		this.add(editMenu);
	}

	private void createFileMenu(ISudokuController controller, JFrame frame) {
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.create();
			}
		});
		newMenuItem.setMnemonic(KeyEvent.VK_N);
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));

		fileMenu.add(newMenuItem);

		fileMenu.addSeparator();

		saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				save(frame);
			}
		});
		saveMenuItem.setMnemonic(KeyEvent.VK_S);
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

		fileMenu.add(saveMenuItem);
		loadMenuItem = new JMenuItem("Load");
		loadMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				load(frame);
			}
		});
		loadMenuItem.setMnemonic(KeyEvent.VK_L);
		loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));

		fileMenu.add(loadMenuItem);

		fileMenu.addSeparator();

		quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.setMnemonic(KeyEvent.VK_Q);
		quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
		quitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				frame.setVisible(false); 
            	frame.dispose();
			}
		});
		fileMenu.add(quitMenuItem);

		this.add(fileMenu);
	}
	
	public void load(JFrame frame) {
		JFileChooser fileChooser = new JFileChooser(".");
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				FileInputStream fis = new FileInputStream(fileChooser.getSelectedFile());
				ObjectInputStream inStream = new ObjectInputStream(fis);
				controller.parseStringToGrid((String) inStream.readObject());
				inStream.close();
			} catch (IOException | ClassNotFoundException ioe) {
			    LOGGER.info(ioe);
				JOptionPane.showMessageDialog(frame, "IOException reading sudoku:\n" + ioe.getLocalizedMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	


	public void save(JFrame frame) {
		JFileChooser fileChooser = new JFileChooser(".");
		int result = fileChooser.showSaveDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file.exists() && JOptionPane.showConfirmDialog(frame,
					"File \"" + file.getName() + "\" already exists.\n" + "Would you like to replace it?", "Save",
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
				return;
			}
			try {
				FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream outStream = new ObjectOutputStream(fos);
				outStream.writeObject(controller.getGridString());
				outStream.flush();
				outStream.close();

			} catch (IOException ioe) {
			    LOGGER.info(ioe);
				JOptionPane.showMessageDialog(frame, "IOException saving sudoku:\n" + ioe.getLocalizedMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
