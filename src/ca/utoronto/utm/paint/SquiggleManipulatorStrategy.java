package ca.utoronto.utm.paint;
import javafx.scene.input.MouseEvent;

class SquiggleManipulatorStrategy extends ShapeManipulatorStrategy {
	SquiggleManipulatorStrategy(PaintModel paintModel) {
		super(paintModel);
	}

	private SquiggleCommand squiggleCommand;
	@Override
	//之后都是mouse drap
	public void mouseDragged(MouseEvent e) {
		this.squiggleCommand.add(new Point((int)e.getX(), (int)e.getY()));
	}

	@Override
	//我点的第一下是mouse press
	public void mousePressed(MouseEvent e) {
			this.squiggleCommand = new SquiggleCommand();
			this.addCommand(squiggleCommand);
	}
}
