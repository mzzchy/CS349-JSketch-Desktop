
// The base for the MVC was taken from the mvc3 example from the lecture example code

import java.awt.*;
import java.awt.geom.*;
import java.io.Serializable;
import java.util.ArrayList;

// View interface
interface IView {
	public void updateView();
}

enum tool {
	SELECT, ERASE, LINE, CIRCLE, RECTANGLE, FILL
}

class MyShape implements Serializable{
	Shape shape;
	Color strokeColor = Color.BLACK;
	Color fillColor = null;
	Integer thickness = 1;

	MyShape(Shape newShape, Color sColor, Integer newThickness) {
		shape = newShape;
		strokeColor = sColor;
		thickness = newThickness;
	};
}

public class Model {	

	private tool currentTool = tool.SELECT;
	private Color currentColor = Color.BLACK;
	private Integer currentThickness = 0;	// Based of index of list so start at 0, canvas' paint component will handle this correctly
	private MyShape drawingShape = null;
	private MyShape selectedShape = null;

	private ArrayList<MyShape> shapeList = new ArrayList<MyShape>();
	// all views of this model
	private ArrayList<IView> views = new ArrayList<IView>();

	// set the view observer
	public void addView(IView view) {
		views.add(view);
		view.updateView();
	}

	public ArrayList<MyShape> getShapeList() {
		return shapeList;
	}
	public tool getCurrentTool() {
		return currentTool;
	}
	public Color getCurrentColor() {
		return currentColor;
	}
	public Integer getCurrentThickness() {
		return currentThickness;
	}
	public MyShape getDrawingShape() {
		return drawingShape;
	}
	public MyShape getSelectedShape() {
		return selectedShape;
	}

	public void setCurrentTool(tool toolName) {
		currentTool = toolName;
		notifyObservers();
	}

	public void setCurrentColor(Color color) {
		currentColor = color;
		notifyObservers();
	}

	public void setCurrentThickness(int thickness) {
		currentThickness = thickness;
		notifyObservers();
	}

	public void setSelectedShapeColor(Color color) {
		if(selectedShape != null) {
			selectedShape.strokeColor = color;
		}
		notifyObservers();
	}

	public void setSelectedShapeThickness(int thickness) {
		if(selectedShape != null) {
			selectedShape.thickness = thickness;
		}
		notifyObservers();
	}

	public void setDrawingShape(MyShape newDrawingShape) {
		drawingShape = newDrawingShape;
		notifyObservers();
	}

	public void newDrawing() {
		shapeList = new ArrayList<MyShape>();
		notifyObservers();
	}

	public void loadShapes(ArrayList<MyShape> loadedShapeList) {
		shapeList = loadedShapeList;
		notifyObservers();
	}

	public void drawingShape(int x1, int y1, int x2, int y2) {
		int x = Math.min(x1, x2);
		int y = Math.min(y1, y2);
		int width = Math.abs(x1 - x2);
		int height = Math.abs(y1 - y2);

		if(currentTool == tool.RECTANGLE) {
			drawingShape = new MyShape(new Rectangle2D.Float(x,y,width,height), currentColor, currentThickness);
		}
		else if(currentTool == tool.CIRCLE) {
			int size = Math.max(width, height);
			drawingShape = new MyShape(new Ellipse2D.Float(x,y,size,size), currentColor, currentThickness);
		}
		else if(currentTool == tool.LINE) {
			drawingShape = new MyShape(new Line2D.Float(x1,y1,x2,y2), currentColor, currentThickness);
		}
		notifyObservers();
	}

	public void addShape() {
		shapeList.add(drawingShape);
		drawingShape = null;
		notifyObservers();
	}

	// These offsets are calculated on a mousePress event and are used to offset a shape when moving relative to where the user clicked
	double offsetX;
	double offsetY;

	public void clearSelectedShape() {
		selectedShape = null;
		notifyObservers();
	}

	public void selectShape(int x, int y) {
		for(int i = shapeList.size()-1; i >= 0; i--) {
			if (shapeList.get(i).shape.contains(x, y) || shapeList.get(i).shape.intersects(x-2,y-2,4,4)) {	// Check intersection with 4 pixel hitbox for lines
				clearSelectedShape();
				selectedShape = shapeList.get(i);
				currentThickness = selectedShape.thickness;
				currentColor = selectedShape.strokeColor;
				break;
			}
		}
		// Used for moving shapes relative to mousePress location
		if(selectedShape != null) {
			offsetX = selectedShape.shape.getBounds().getX() - x;
			offsetY = selectedShape.shape.getBounds().getY() - y;
		}

		notifyObservers();
	}

	public void moveSelectedShape(int x, int y) {
		if(selectedShape != null) {
			double oldShapeX = selectedShape.shape.getBounds().getX();
			double oldShapeY = selectedShape.shape.getBounds().getY();

			AffineTransform tx = new AffineTransform();
			tx.translate(x - oldShapeX + offsetX, y - oldShapeY + offsetY);
			selectedShape.shape = tx.createTransformedShape(selectedShape.shape);
		}
		notifyObservers();
	}

	public void eraseShape(int x, int y) {
		for(int i = shapeList.size()-1; i >= 0; i--) {
			if (shapeList.get(i).shape.contains(x, y) || shapeList.get(i).shape.intersects(x-2,y-2,4,4)) {	// Check intersection with 4 pixel hitbox for lines
				shapeList.remove(i);
				break;
			}
		}
		notifyObservers();
	}

	public void fillShape(int x, int y) {
		for(int i = shapeList.size()-1; i >= 0; i--) {
			if (shapeList.get(i).shape.contains(x, y)) {
				shapeList.get(i).fillColor = currentColor;
				break;
			}
		}
		notifyObservers();
	}

	private void notifyObservers() {
			for (IView view : this.views) {
				view.updateView();
			}
	}
}
