
// The base for the MVC was taken from the mvc3 example from the lecture example code

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Main{

	public static void main(String[] args) {
		// create Model and initialize it
		Model model = new Model();

		// Toolbar View
		ToolbarView toolbarView = new ToolbarView(model);
		model.addView(toolbarView);

		// Canvas View
		CanvasView canvasView = new CanvasView(model);
		model.addView(canvasView);

		JFrame frame = new JFrame("JSketch");
		JFileChooser fileChooser = new JFileChooser();

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenu viewMenu = new JMenu("View");
		menuBar.add(fileMenu);
		menuBar.add(viewMenu);

		// "New" menu button
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.newDrawing();
			}
		});
		fileMenu.add(newMenuItem);

		// "Load" menu button
		JMenuItem loadMenuItem = new JMenuItem("Load");
		loadMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();

					// Make sure it's a JSketch drawing file
					String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
					if(!extension.equals("jsd")){
						JOptionPane.showMessageDialog(null, "Not a JSketch Drawing file, please choose a .jsd file");
					}
					else {
						try {
							FileInputStream fileStream = new FileInputStream(file);
							ObjectInputStream objectStream = new ObjectInputStream(fileStream);
							@SuppressWarnings("unchecked")	// Suppresses the unchecked cast in the following line
							ArrayList<MyShape> result = (ArrayList<MyShape>) objectStream.readObject();

							model.loadShapes(result);
							objectStream.close();
						} catch (FileNotFoundException ex) {
							ex.printStackTrace();
						} catch (IOException ex) {
							ex.printStackTrace();
						} catch (ClassNotFoundException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
		fileMenu.add(loadMenuItem);

		// "Save" menu button
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showSaveDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					String filePath = file.getAbsolutePath();
					if(!filePath.endsWith(".jsd")) {
						file = new File(filePath + ".jsd");
					}
					try {
						FileOutputStream fileStream = new FileOutputStream(file);
						ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
						objectStream.writeObject(model.getShapeList());
						objectStream.close();
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		fileMenu.add(saveMenuItem);

		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem fullSizeRB = new JRadioButtonMenuItem("Full Size");
		fullSizeRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)(canvasView.getLayout());
				cl.show(canvasView, "Full Size");
			}
		});
		fullSizeRB.setSelected(true);
		group.add(fullSizeRB);
		viewMenu.add(fullSizeRB);
		JRadioButtonMenuItem fitRB = new JRadioButtonMenuItem("Fit to Window");
		fitRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout)(canvasView.getLayout());
				cl.show(canvasView, "Fit to Window");
			}
		});
		group.add(fitRB);
		viewMenu.add(fitRB);

		frame.setJMenuBar(menuBar);

		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));

		// Tool Palette
		contentPane.add(toolbarView);
		// Canvas View
		contentPane.add(canvasView);

		frame.setMinimumSize(new Dimension(800,700));
		frame.setPreferredSize(new Dimension(900,700));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setFocusable(true);
		frame.setVisible(true);

		// Listens for ESC keep to deselect shape
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(new KeyEventDispatcher() {
					@Override
					public boolean dispatchKeyEvent(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ESCAPE && e.getID() == KeyEvent.KEY_PRESSED) {
							model.clearSelectedShape();
						}
						return false;
					}
				});

	} 
}
