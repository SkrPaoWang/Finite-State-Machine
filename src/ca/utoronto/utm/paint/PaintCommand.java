package ca.utoronto.utm.paint;

import java.io.PrintWriter;
import java.util.Observable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class PaintCommand extends Observable {
	private Color color;
	private boolean fill;
	private int r;
	private int g;
	private int b;

	PaintCommand() {
		// Pick a random color for this
		this.r = (int) (Math.random() * 256);
		this.g = (int) (Math.random() * 256);
		this.b = (int) (Math.random() * 256);
		this.color = Color.rgb(r, g, b);
		this.fill = (1 == (int) (Math.random() * 2));
	}

	public Color getColor() {
		return color;
	}

	public String saveColor() {
		return String.format("	color:%d,%d,%d", r, g, b);
	}

	public String saveFill() {
		return String.format("	filled:%b", this.isFill());
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isFill() {
		return fill;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public String toString() {
		double r = this.color.getRed();
		double g = this.color.getGreen();
		double b = this.color.getBlue();

		String s = "";
		s += "\tcolor:" + r + "," + g + "," + b + "\n";
		s += "\tfilled:" + this.fill + "\n";
		return s;
	}

	public abstract void save(PrintWriter writer);

	public abstract void execute(GraphicsContext g);
}
