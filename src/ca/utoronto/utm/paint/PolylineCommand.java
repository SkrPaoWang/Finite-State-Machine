package ca.utoronto.utm.paint;

import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

public class PolylineCommand extends PaintCommand {

	private ArrayList<Point> points = new ArrayList<Point>();
	private Point p1;
	private Point p2;

	public ArrayList<Point> getPoints() {
		return this.points;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
		this.setChanged();
		this.notifyObservers();
	}

	public void setP2(Point p) {
		this.p2 = p;
		this.points.add(p);
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public void execute(GraphicsContext g) {
		g.setStroke(this.getColor());
		for (int i = 0; i < points.size() - 1; i++) {
			Point p1 = points.get(i);
			Point p2 = points.get(i + 1);
			g.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}
		if (p1 != null && p2 != null) {
			g.strokeLine(p2.x, p2.y, p1.x, p1.y);
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
		writer.println("Polyline");
		writer.println(this.saveColor());
		writer.println(this.saveFill());
		this.savePoints(writer);
		writer.println("End Polyline");
	}

}
