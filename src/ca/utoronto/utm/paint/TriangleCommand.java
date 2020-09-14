package ca.utoronto.utm.paint;

import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

public class TriangleCommand extends PaintCommand{
	public ArrayList<Point> points = new ArrayList<Point>();
	
	public void add(Point p) {
		this.points.add(p);
		this.setChanged();
		this.notifyObservers();
	}


	@Override
	public void execute(GraphicsContext g) {
		if (this.points.size() == 3) {
				Point p1 = points.get(0);
				Point p2 = points.get(1);
				Point p3 = points.get(2);
				g.strokeLine(p1.x, p1.y, p2.x, p2.y);
				g.strokeLine(p2.x, p2.y, p3.x, p3.y);
				g.strokeLine(p1.x, p1.y, p3.x, p3.y);
			
		}
		
	}

	@Override
	public void save(PrintWriter writer) {
		// TODO Auto-generated method stub
		
	}

}
