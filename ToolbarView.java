
// The base for the MVC was taken from the mvc3 example from the lecture example code

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

class ToolbarView extends JPanel implements IView {

	private JButton button;

	private Model model;

	class ListRenderer extends JLabel implements ListCellRenderer {
		public ListRenderer() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			int selectedIndex = ((Integer)value).intValue();
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(Color.WHITE);
				setForeground(list.getForeground());
			}

			String images[] = {"images/thickness_1.png",
					"images/thickness_2.png",
					"images/thickness_3.png",
					"images/thickness_4.png",
					"images/thickness_5.png"};
			ImageIcon icon = new ImageIcon(images[selectedIndex]);
			setIcon(icon);
			return this;
		}
	}

	private  JToggleButton makeToolButton(Icon icon, tool toolName, boolean selected) {
		JToggleButton button = new JToggleButton(icon, selected);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.clearSelectedShape();
				model.setCurrentTool(toolName);
			}
		});
		return button;
	}

	private  JToggleButton makeColorButton(Color color, boolean selected) {
		JToggleButton button = new JToggleButton("", selected);
		button.setBackground(color);
		String images[] = {"images/black.png",
				"images/black_s.png",
				"images/white.png",
				"images/white_s.png",
				"images/red.png",
				"images/red_s.png",
				"images/yellow.png",
				"images/yellow_s.png",
				"images/green.png",
				"images/green_s.png",
				"images/blue.png",
				"images/blue_s.png"};

		if(color == Color.BLACK) {
			button.setIcon(new ImageIcon(images[0]));
			button.setSelectedIcon(new ImageIcon(images[1]));
		}
		else if(color == Color.WHITE) {
			button.setIcon(new ImageIcon(images[2]));
			button.setSelectedIcon(new ImageIcon(images[3]));
		}
		else if(color == Color.RED) {
			button.setIcon(new ImageIcon(images[4]));
			button.setSelectedIcon(new ImageIcon(images[5]));
		}
		else if(color == Color.YELLOW) {
			button.setIcon(new ImageIcon(images[6]));
			button.setSelectedIcon(new ImageIcon(images[7]));
		}
		else if(color == Color.GREEN) {
			button.setIcon(new ImageIcon(images[8]));
			button.setSelectedIcon(new ImageIcon(images[9]));
		}
		else if(color == Color.BLUE) {
			button.setIcon(new ImageIcon(images[10]));
			button.setSelectedIcon(new ImageIcon(images[11]));
		}

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setCurrentColor(button.getBackground());
				if(model.getCurrentTool() == tool.SELECT) {
					model.setSelectedShapeColor(model.getCurrentColor());
				}
			}
		});
		return button;
	}

	private JList<Integer> thicknessList;
	private JToggleButton color1Button;
	private JToggleButton color2Button;
	private JToggleButton color3Button;
	private JToggleButton color4Button;
	private JToggleButton color5Button;
	private JToggleButton color6Button;

	@SuppressWarnings("unchecked")	// Suppress ListCellRenderer unchecked assignment
	ToolbarView(Model model_) {
		model = model_;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,0,1,Color.BLACK),BorderFactory.createEmptyBorder(10,10,10,10)));
		this.setPreferredSize(new Dimension(160,100));

		JToolBar toolsToolBar = new JToolBar(JToolBar.VERTICAL);
		this.add(toolsToolBar);
		JToolBar colorToolBar = new JToolBar(JToolBar.VERTICAL);
		this.add(colorToolBar);
		JToolBar thicknessToolBar = new JToolBar(JToolBar.VERTICAL);
		thicknessToolBar.setPreferredSize(new Dimension(100, 170));
		thicknessToolBar.setMinimumSize(new Dimension(100, 170));
		this.add(thicknessToolBar);

		JPanel tools = new JPanel(new GridLayout(3,2,5,5));
		tools.setPreferredSize(new Dimension(100, 150));
		ButtonGroup toolButtons = new ButtonGroup();

		JToggleButton selectTool = makeToolButton(new ImageIcon("images/select.png"), tool.SELECT, true);
		JToggleButton eraseTool = makeToolButton(new ImageIcon("images/erase.png"), tool.ERASE, false);
		JToggleButton lineTool = makeToolButton(new ImageIcon("images/line.png"), tool.LINE, false);
		JToggleButton circleTool = makeToolButton(new ImageIcon("images/circle.png"), tool.CIRCLE, false);
		JToggleButton rectangleTool = makeToolButton(new ImageIcon("images/rectangle.png"), tool.RECTANGLE, false);
		JToggleButton fillTool = makeToolButton(new ImageIcon("images/fill.png"), tool.FILL, false);

		tools.add(selectTool);
		tools.add(eraseTool);
		tools.add(lineTool);
		tools.add(circleTool);
		tools.add(rectangleTool);
		tools.add(fillTool);
		toolButtons.add(selectTool);
		toolButtons.add(eraseTool);
		toolButtons.add(lineTool);
		toolButtons.add(circleTool);
		toolButtons.add(rectangleTool);
		toolButtons.add(fillTool);

		toolsToolBar.add(tools);

		JPanel colors = new JPanel(new GridLayout(3,2,5,5));
		colors.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
		colors.setPreferredSize(new Dimension(100, 150));
		ButtonGroup colorButtons = new ButtonGroup();

		color1Button = makeColorButton(Color.BLACK, true);
		color2Button = makeColorButton(Color.WHITE, false);
		color3Button = makeColorButton(Color.RED, false);
		color4Button = makeColorButton(Color.YELLOW, false);
		color5Button = makeColorButton(Color.GREEN, false);
		color6Button = makeColorButton(Color.BLUE, false);

		colors.add(color1Button);
		colors.add(color2Button);
		colors.add(color3Button);
		colors.add(color4Button);
		colors.add(color5Button);
		colors.add(color6Button);
		colorButtons.add(color1Button);
		colorButtons.add(color2Button);
		colorButtons.add(color3Button);
		colorButtons.add(color4Button);
		colorButtons.add(color5Button);
		colorButtons.add(color6Button);

		colorToolBar.add(colors);

		JButton chooserButton = new JButton("Chooser");
		chooserButton.setPreferredSize(new Dimension(100, 50));
		chooserButton.setMaximumSize(new Dimension(200, 50));
		chooserButton.setMinimumSize(new Dimension(100, 50));
		chooserButton.setAlignmentX(0.5F);

		chooserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentColor(JColorChooser.showDialog(null, "Pick a Color", Color.BLACK));
				if(model.getCurrentTool() == tool.SELECT) {
					model.setSelectedShapeColor(model.getCurrentColor());
				}
			}
		});
		colorToolBar.add(chooserButton);

		Integer[] intArray = {0,1,2,3,4};
		thicknessList = new JList<Integer>(intArray);
		thicknessList.setBackground(Color.WHITE);
		ListRenderer renderer = new ListRenderer();
		renderer.setPreferredSize(new Dimension(130, 30));
		thicknessList.setCellRenderer(renderer);
		thicknessList.setSelectedIndex(0);
		thicknessList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				JList list = (JList) e.getSource();
				int selectedIndex = list.getSelectedIndex();
				model.setCurrentThickness(selectedIndex);
				if(model.getCurrentTool() == tool.SELECT) {
					model.setSelectedShapeThickness(selectedIndex);
				}
			}
		});
		thicknessToolBar.add(thicknessList);
	}

	// IView interface
	public void updateView() {
		// Update the buttons here
		if(model.getSelectedShape() != null) {
			thicknessList.setSelectedIndex(model.getCurrentThickness());

			if(model.getCurrentColor().getRGB() == Color.BLACK.getRGB())
				color1Button.setSelected(true);
			else if(model.getCurrentColor().getRGB() == Color.WHITE.getRGB())
				color2Button.setSelected(true);
			else if(model.getCurrentColor().getRGB() == Color.RED.getRGB())
				color3Button.setSelected(true);
			else if(model.getCurrentColor().getRGB() == Color.YELLOW.getRGB())
				color4Button.setSelected(true);
			else if(model.getCurrentColor().getRGB() == Color.GREEN.getRGB())
				color5Button.setSelected(true);
			else if(model.getCurrentColor().getRGB() == Color.BLUE.getRGB())
				color6Button.setSelected(true);

		}
	}
}
