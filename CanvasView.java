
// The base for the MVC was taken from the mvc3 example from the lecture example code

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class CanvasView extends JPanel implements IView {

	private Model model;

	private int startX;
	private int startY;

	private double canvasWidth = 1200;
	private double canvasHeight = 900;

	private void paintShapes(Graphics2D g2, JPanel canvas) {
		double scaleWidth = canvas.getWidth() / canvasWidth;
		double scaleHeight = canvas.getHeight() / canvasHeight;
		double scale = Math.min(scaleWidth, scaleHeight);
		g2.scale(scale, scale);

		// Draw all shapes in the shape list
		ArrayList<MyShape> shapeList = model.getShapeList();
		for (MyShape shape: shapeList) {

			int shapeThickness = (shape.thickness+1)*2;

			if (shape.fillColor != null) {
				g2.setColor(shape.fillColor);
				g2.fill(shape.shape);
			}

			g2.setStroke(new BasicStroke(shapeThickness));
			if(shape == model.getSelectedShape()) {
				float dash1[] = {10.0f};
				g2.setStroke(new BasicStroke(shapeThickness,
						BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_MITER,
						10, dash1 , 0));
			}

			g2.setColor(shape.strokeColor);
			g2.draw(shape.shape);
			g2.setStroke(new BasicStroke(3));

		}
		// Also draw the shape currently being drawn
		if(model.getDrawingShape() != null) {
			g2.setStroke(new BasicStroke((model.getCurrentThickness()+1)*2));
			g2.setColor(model.getCurrentColor());
			g2.draw(model.getDrawingShape().shape);
		}

	}

	// The main interface of the CanvasView
	private	JPanel canvasFull = new JPanel(){
		@Override
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			paintShapes(g2, this);
		}
	};

	// The main interface of the CanvasView
	private	JPanel canvasFit = new JPanel(){
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			paintShapes(g2, this);
		}
	};

	CanvasView(Model model_) {
		model = model_;

		this.setLayout(new CardLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		canvasFull.setBackground(Color.WHITE);
		canvasFull.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		JPanel canvasContainer = new JPanel(new BorderLayout());
		canvasContainer.setPreferredSize(new Dimension((int)canvasWidth,(int)canvasHeight));
		canvasContainer.setMaximumSize(new Dimension((int)canvasWidth,(int)canvasHeight));
		canvasContainer.setMinimumSize(new Dimension((int)canvasWidth,(int)canvasHeight));
		canvasContainer.add(canvasFull, BorderLayout.CENTER);

		// Used to fix Canvas vertically
		JPanel canvasVContainer = new JPanel();
		canvasVContainer.setLayout(new BoxLayout(canvasVContainer, BoxLayout.Y_AXIS));
        canvasVContainer.setBackground(Color.LIGHT_GRAY);
		canvasVContainer.add(canvasContainer);
		canvasVContainer.add(Box.createVerticalGlue());

        // Used to fix Canvas horizontally
		JPanel canvasHContainer = new JPanel();
		canvasHContainer.setLayout(new BoxLayout(canvasHContainer, BoxLayout.X_AXIS));
        canvasHContainer.setBackground(Color.LIGHT_GRAY);
		canvasHContainer.add(canvasVContainer);
		canvasHContainer.add(Box.createHorizontalGlue());

		JScrollPane scroller = new JScrollPane(canvasHContainer);
		scroller.setSize(new Dimension(640,480));
		this.add(scroller, "Full Size");


		canvasFit.setBackground(Color.WHITE);
		canvasFit.setPreferredSize(new Dimension((int)canvasWidth,(int)canvasHeight));

		JPanel fitWindow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// This listener keeps the canvas' aspect ratio correct when resizing
		fitWindow.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				double w = fitWindow.getWidth() / canvasWidth;
				double h = fitWindow.getHeight() / canvasHeight;
				double scale =  Math.min(w, h);
				canvasFit.setPreferredSize(new Dimension((int)(canvasWidth*scale), (int)(canvasHeight*scale)));
				fitWindow.revalidate();
			}
		});

		fitWindow.setBackground(Color.LIGHT_GRAY);
		fitWindow.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		fitWindow.add(canvasFit);
		this.add(fitWindow, "Fit to Window");

		// Controllers for mouse events for the full size canvas
		canvasFull.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				startX = e.getX();
				startY = e.getY();

				if(model.getCurrentTool() == tool.CIRCLE || model.getCurrentTool() == tool.RECTANGLE || model.getCurrentTool() == tool.LINE) {
					model.drawingShape(0,0,0,0);
				}
				else if (model.getCurrentTool() == tool.SELECT){
					model.selectShape(e.getX(),e.getY());
				}
				else if (model.getCurrentTool() == tool.ERASE){
					model.eraseShape(e.getX(),e.getY());
				}
				else if (model.getCurrentTool() == tool.FILL){
					model.fillShape(e.getX(),e.getY());
				}
			}
			public void mouseReleased(MouseEvent e) {
				if(model.getCurrentTool() == tool.CIRCLE || model.getCurrentTool() == tool.RECTANGLE || model.getCurrentTool() == tool.LINE) {
					model.addShape();
				}
			}
		});
		canvasFull.addMouseMotionListener(new MouseAdapter() {
				public void mouseDragged(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					if(model.getCurrentTool() == tool.CIRCLE || model.getCurrentTool() == tool.RECTANGLE || model.getCurrentTool() == tool.LINE) {
						model.drawingShape(startX, startY, x, y);
					}
					else if (model.getCurrentTool() == tool.SELECT){
						model.moveSelectedShape(x, y);
					}
				}
		});

		// Controllers for mouse events for the full size canvas
		canvasFit.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				double scaleWidth = canvasFit.getWidth() / canvasWidth;
				double scaleHeight = canvasFit.getHeight() / canvasHeight;

				startX = (int) (e.getX()/scaleWidth);
				startY = (int) (e.getY()/scaleHeight);
				int x = (int) (e.getX()/scaleWidth);
				int y =(int) (e.getY()/scaleHeight);

				if(model.getCurrentTool() == tool.CIRCLE || model.getCurrentTool() == tool.RECTANGLE || model.getCurrentTool() == tool.LINE) {
					model.drawingShape(0,0,0,0);
				}
				else if (model.getCurrentTool() == tool.SELECT){
					model.selectShape(x,y);
				}
				else if (model.getCurrentTool() == tool.ERASE){
					model.eraseShape(x,y);
				}
				else if (model.getCurrentTool() == tool.FILL){
					model.fillShape(x,y);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if(model.getCurrentTool() == tool.CIRCLE || model.getCurrentTool() == tool.RECTANGLE || model.getCurrentTool() == tool.LINE) {
					model.addShape();
				}
			}
		});
		canvasFit.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				double scaleWidth = canvasFit.getWidth() / canvasWidth;
				double scaleHeight = canvasFit.getHeight() / canvasHeight;
				int x = (int) (e.getX()/scaleWidth);
				int y =(int) (e.getY()/scaleHeight);
				if(model.getCurrentTool() == tool.CIRCLE || model.getCurrentTool() == tool.RECTANGLE || model.getCurrentTool() == tool.LINE) {
					model.drawingShape(startX, startY, x, y);
				}
				else if (model.getCurrentTool() == tool.SELECT){
					model.moveSelectedShape(x, y);
				}
			}
		});
	}

	// IView interface
	public void updateView() {
		canvasFull.repaint();
		canvasFit.repaint();
	}
}
