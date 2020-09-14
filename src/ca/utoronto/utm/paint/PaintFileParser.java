package ca.utoronto.utm.paint;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.paint.Color;

/**
 * Parse a file in Version 1.0 PaintSaveFile format. An instance of this class
 * understands the paint save file format, storing information about its effort
 * to parse a file. After a successful parse, an instance will have an ArrayList
 * of PaintCommand suitable for rendering. If there is an error in the parse,
 * the instance stores information about the error. For more on the format of
 * Version 1.0 of the paint save file format, see the associated documentation.
 * 
 * @author
 *
 */
public class PaintFileParser {
	private int lineNumber = 0; // the current line being parsed
	private String errorMessage = ""; // error encountered during parse
	private PaintModel paintModel;
	private ArrayList<Pattern> ShapeStart = new ArrayList<Pattern>();
	private PaintCommand currCommand;
	private int transit;
	/**
	 * Below are Patterns used in parsing
	 */
	private Pattern pFileStart = Pattern.compile("^PaintSaveFileVersion1.0$");
	private Pattern pFileEnd = Pattern.compile("^EndPaintSaveFile$");
	private Pattern pColor = Pattern.compile("^color:(\\d+),(\\d+),(\\d+)$");
	private Pattern pFill = Pattern.compile("^filled:(true|false)$");
	private Pattern pCenter = Pattern.compile("^center:\\(([0-9]+,[0-9]+)\\)$");
	private Pattern pRadius = Pattern.compile("^radius:(\\d+)$");
	private Pattern pP1 = Pattern.compile("^p1:\\(([0-9]+,[0-9]+)\\)$");
	private Pattern pP2 = Pattern.compile("^p2:\\(([0-9]+,[0-9]+)\\)$");
	private Pattern pstartPoints = Pattern.compile("^points$");
	private Pattern pPoint = Pattern.compile("^point:\\((-?[0-9]+,-?[0-9]+)\\)$");
	private Pattern pCircleStart = Pattern.compile("^Circle$");
	private Pattern pCircleEnd = Pattern.compile("^EndCircle$");
	private Pattern pRectangleStart = Pattern.compile("^Rectangle$");
	private Pattern pRectangleEnd = Pattern.compile("^EndRectangle$");
	private Pattern pSquiggleStart = Pattern.compile("^Squiggle$");
	private Pattern pSquiggleEnd = Pattern.compile("^EndSquiggle$");
	private Pattern pPolylineStart = Pattern.compile("^Polyline$");
	private Pattern pPolylineEnd = Pattern.compile("^EndPolyline$");

	/**
	 * Store an appropriate error message in this, including lineNumber where the
	 * error occurred.
	 * 
	 * @param mesg
	 */
	private void error(String mesg) {
		this.errorMessage = "Error in line " + lineNumber + " " + mesg;
	}

	private void addPattern() {
		Collections.addAll(ShapeStart, pCircleStart, pRectangleStart, pSquiggleStart, pPolylineStart);
	}

	/**
	 * 
	 * @return the error message resulting from an unsuccessful parse
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * Parse the inputStream as a Paint Save File Format file. The result of the
	 * parse is stored as an ArrayList of Paint command. If the parse was not
	 * successful, this.errorMessage is appropriately set, with a useful error
	 * message.
	 * 
	 * @param inputStream the open file to parse
	 * @param paintModel  the paint model to add the commands to
	 * @return whether the complete file was successfully parsed
	 */
	public boolean parse(BufferedReader inputStream, PaintModel paintModel) {
		this.paintModel = paintModel;
		this.errorMessage = "";
		this.addPattern();
		try {
			int state = 0;
			Matcher m;
			String l;
			this.lineNumber = 0;
			while ((l=inputStream.readLine())!= null) {
				this.lineNumber++;
				l = l.replaceAll("\\s+", "");
				System.out.println(lineNumber + " " + l + " " + state);
				if (!l.isEmpty()) {
					outer: switch (state) {
					case 0:
						m = pFileStart.matcher(l);
						if (m.matches()) {
							state = 1;
							break;
						}
						error("Expected Start of Paint Save File");
						return false;
					case 1:
						m = pFileEnd.matcher(l);
						String nextLine;
						if (m.matches()) {
							while ((nextLine=inputStream.readLine())!= null) {
								if (!nextLine.isEmpty()) {
									this.lineNumber ++;
									error("End should be end");
									return false;	
								}
							}
							return true;
						}
						for (Pattern pattern : this.ShapeStart) {
							m = pattern.matcher(l);
							if (m.matches()) {
								state = 2;
								this.setCurrCommand(l);
								this.transit = this.ShapeStart.indexOf(pattern) + 4;
								break outer;
							}
						}
						error("Expected Start of a Shape line");
						return false;
					case 2:
						m = pColor.matcher(l);
						if (m.matches() && checkColorIntAndSetColor(m.group(1), m.group(2), m.group(3))) {
							state = 3;
							break;
						}
						error("Excepted matching color format");

						return false;
					case 3:
						m = pFill.matcher(l);
						if (m.matches()) {
							this.setFill(m.group(1));
							state = this.transit;
							break;
						}
						error("Excepted matching fill format");
						return false;
					case 4:
						m = pCenter.matcher(l);
						if (m.matches()) {
							this.setCenter(m.group(1));
							state = 8;
							break;
						}
						error("Excepted matching Circle Center format");
						return false;
					case 5:
						m = pP1.matcher(l);
						if (m.matches()) {
							this.setP1(m.group(1));
							state = 10;
							break;
						}
						error("Excepted matching Rectangle point p1 format");
						return false;
					case 6:
					case 7:
						m = pstartPoints.matcher(l);
						if (m.matches()) {
							state = 12;
							break;
						}
						error("Excepted matching start points format");
						return false;
					case 8:
						m = pRadius.matcher(l);
						if (m.matches()) {
							this.setRadius(m.group(1));
							state = 9;
							break;
						}
						error("Excepted matching Circle Radius format");
						return false;
					case 9:
						m = pCircleEnd.matcher(l);
						if (m.matches()) {
							this.paintModel.addCommand(currCommand);
							state = 1;
							break;
						}
						error("Excepted matching end circle line");
						return false;
					case 10:
						m = pP2.matcher(l);
						if (m.matches()) {
							this.setP2(m.group(1));
							state = 11;
							break;
						}
						error("Excepted matching Rectangle point p2 format");
						return false;
					case 11:
						m = pRectangleEnd.matcher(l);
						if (m.matches()) {
							this.paintModel.addCommand(currCommand);
							state = 1;
							break;
						}
					case 12:
						while (l != "endpoints") {
							if (!l.isEmpty()) {
								m = pPoint.matcher(l);
								if (m.matches()) {
									this.setPoint(m.group(1));
								} else {
									error("Excepted matching  point format");
									return false;
								}
							}
							l = inputStream.readLine().replaceAll("\\s+", "");
							this.lineNumber++;
							System.out.println(lineNumber + " " + l + " " + state);
							if (this.currCommand instanceof SquiggleCommand && l.equals("endpoints")) {
								state = 13;
								break;

							} else if (this.currCommand instanceof PolylineCommand && l.equals("endpoints")) {
								state = 14;
								break;
							}
						}
						
					case 13:
						m = pSquiggleEnd.matcher(l);
						if (m.matches()) {
							this.paintModel.addCommand(currCommand);
							state = 1;
							break;
						}
					case 14:
						m = pPolylineEnd.matcher(l);
						if (m.matches()) {
							this.paintModel.addCommand(currCommand);
							state = 1;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	private void setFill(String string) {
		if (string.equals("true")) {
			System.out.println("fill true");
			this.currCommand.setFill(true);
		} else {
			System.out.println("fill false");
			this.currCommand.setFill(false);
		}

	}

	private boolean checkColorIntAndSetColor(String group1, String group2, String group3) {
		int x = Integer.parseInt(group1);
		int y = Integer.parseInt(group2);
		int z = Integer.parseInt(group3);
		if (0 <= x && x <= 255 && 0 <= y && y <= 255 && 0 <= z && z <= 255) {
			this.currCommand.setColor(Color.rgb(x, y, z));
			return true;
		}
		return false;
	}

	private void setCenter(String group) {
		String[] array = group.split(",");
		CircleCommand x = (CircleCommand) this.currCommand;
		x.setCentre(new Point(Integer.parseInt(array[0]), Integer.parseInt(array[1])));
	}

	private void setRadius(String group) {
		CircleCommand x = (CircleCommand) this.currCommand;
		x.setRadius(Integer.parseInt(group));
	}

	private void setP1(String group) {
		String[] array = group.split(",");
		RectangleCommand x = (RectangleCommand) this.currCommand;
		x.setP1(new Point(Integer.parseInt(array[0]), Integer.parseInt(array[1])));

	}

	private void setP2(String group) {
		String[] array = group.split(",");
		RectangleCommand x = (RectangleCommand) this.currCommand;
		x.setP2(new Point(Integer.parseInt(array[0]), Integer.parseInt(array[1])));
	}

	private void setPoint(String group) {
		String[] array = group.split(",");
		if (this.currCommand instanceof SquiggleCommand) {
			SquiggleCommand x = (SquiggleCommand) this.currCommand;
			x.getPoints().add(new Point(Integer.parseInt(array[0]), Integer.parseInt(array[1])));
		} else {
			PolylineCommand x = (PolylineCommand) this.currCommand;
			x.getPoints().add(new Point(Integer.parseInt(array[0]), Integer.parseInt(array[1])));
		}
	}

	private void setCurrCommand(String l) {
		if (l.equals("Circle")) {
			this.currCommand = new CircleCommand();
		} else if (l.equals("Rectangle")) {
			this.currCommand = new RectangleCommand();
		} else if (l.equals("Squiggle")) {
			this.currCommand = new SquiggleCommand();
		} else if (l.equals("Polyline")) {
			this.currCommand = new PolylineCommand();
		}
	}
}
