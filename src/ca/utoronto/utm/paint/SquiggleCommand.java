package ca.utoronto.utm.paint;

import javafx.scene.canvas.GraphicsContext;

import java.io.PrintWriter;
import java.util.ArrayList;

public class SquiggleCommand extends PaintCommand {
	private ArrayList<Point> points = new ArrayList<Point>();

	public void add(Point p) {
		this.points.add(p);
		this.setChanged();
		this.notifyObservers();
	}

	public ArrayList<Point> getPoints() {
		return this.points;
	}

	@Override
	public void execute(GraphicsContext g) {
		g.setStroke(this.getColor());
		for (int i = 0; i < points.size() - 1; i++) {
			Point p1 = points.get(i);
			Point p2 = points.get(i + 1);
			g.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}
	}

	private void savePoints(PrintWriter writer) {
		writer.println("	points");
		for (Point point : points) {
			writer.println(String.format("		point:(%d,%d)", point.x, point.y));
		}
		writer.println("	end points");
	}

	@Override
	public void save(PrintWriter writer) {
		writer.println("Squiggle");
		writer.println(this.saveColor());
		writer.println(this.saveFill());
		this.savePoints(writer);
		writer.println("End Squiggle");

	}

}
