package ca.utoronto.utm.paint;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class PolylineManipulatorStrategy extends ShapeManipulatorStrategy {

	private PolylineCommand polylineCommand;

	private boolean init = true;

	PolylineManipulatorStrategy(PaintModel paintModel) {
		super(paintModel);
	}

	public void mouseMoved(MouseEvent e) {
		if (this.polylineCommand != null) {
			this.polylineCommand.setP1(new Point((int) e.getX(), (int) e.getY()));
		}
	}

	public void mousePressed(MouseEvent e) {
		if ((e).getButton().equals(MouseButton.PRIMARY)) {
			if (init) {
				this.polylineCommand = new PolylineCommand();
				this.addCommand(polylineCommand);
				this.init = false;
			}
			this.polylineCommand.setP2(new Point((int) e.getX(), (int) e.getY()));
		}else {
			this.polylineCommand.getPoints().add(new Point((int) e.getX(), (int) e.getY()));
			this.init = true;
			this.polylineCommand = new PolylineCommand();
		}

	}
}
