package ca.utoronto.utm.paint;

import java.io.PrintWriter;

import javafx.scene.canvas.GraphicsContext;

public class CircleCommand extends PaintCommand {
	private Point centre;
	private int radius;

	public CircleCommand(Point centre, int radius) {
		this.centre = centre;
		this.radius = radius;
	}

	public CircleCommand() {
		super();
	}

	public Point getCentre() {
		return centre;
	}

	public void setCentre(Point centre) {
		this.centre = centre;
		this.setChanged();
		this.notifyObservers();
	}

	public String saveCenter() {
		return String.format("	center:(%d,%d)", this.getCentre().x, this.getCentre().y);
	}

	private String saveRadius() {
		return String.format("	radius:%d", this.getRadius());
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
		this.setChanged();
		this.notifyObservers();
	}

	public void execute(GraphicsContext g) {
		int x = this.getCentre().x;
		int y = this.getCentre().y;
		int radius = this.getRadius();
		if (this.isFill()) {
			g.setFill(this.getColor());
			g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
		} else {
			g.setStroke(this.getColor());
			g.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
		}
	}

	@Override
	public void save(PrintWriter writer) {
		writer.println("Circle");
		writer.println(this.saveColor());
		writer.println(this.saveFill());
		writer.println(this.saveCenter());
		writer.println(this.saveRadius());
		writer.println("End Circle");
	}
}
