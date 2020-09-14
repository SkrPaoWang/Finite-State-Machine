package ca.utoronto.utm.paint;

import javafx.scene.input.MouseEvent;

public class TriangleStrategy extends ShapeManipulatorStrategy{
	private TriangleCommand trianglecommand = new TriangleCommand();
	private int vertex;
	TriangleStrategy(PaintModel paintModel) {
		super(paintModel);
		
	}
	public void mousePressed(MouseEvent e) {
		this.vertex++;
		if (this.vertex == 3) {
			this.addCommand(trianglecommand);
		}
		if (this.vertex == 4) {
			this.trianglecommand = new TriangleCommand();
			this.vertex = 1;
		}
		this.trianglecommand.add(new Point((int)e.getX(), (int)e.getY()));
	
			
	}

}
