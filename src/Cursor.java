/* Reads a file of coordinates
 */
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cursor {
	
	public static final int NUMBER_OF_DISTANCES = 1000;
	private Robot robot;

	private ArrayList<ArrayList<CursorPath>> cursorPathsByDistance;
	
	public Cursor() throws AWTException {
		ArrayList<CursorPath> cursorPaths = getArrayListOfCursorPathsFromFile("/home/dpapp/GhostMouse/coordinates.txt");// read from file or something;
		initializeCursorPathsByDistance();
		assignCursorPathsByDistance(cursorPaths);
		
		robot = new Robot();
	}
	
	private void initializeCursorPathsByDistance() {
		this.cursorPathsByDistance = new ArrayList<ArrayList<CursorPath>>();
		for (int i = 0; i < NUMBER_OF_DISTANCES; i++) {
			this.cursorPathsByDistance.add(new ArrayList<CursorPath>());
		}
	}
	
	private ArrayList<CursorPath> getArrayListOfCursorPathsFromFile(String path) {
		CursorDataFileParser cursorDataFileParser = new CursorDataFileParser(path);
		return cursorDataFileParser.getArrayListOfCursorPathsFromFile();
	}
	
	private void assignCursorPathsByDistance(ArrayList<CursorPath> cursorPaths) {
		for (CursorPath cursorPath : cursorPaths) {
			if (cursorPath.isCursorPathReasonable()) { 
				addCursorPathToCursorPathsByDistance(cursorPath);
			}
		}
	}
	
	private void addCursorPathToCursorPathsByDistance(CursorPath cursorPath) {
		this.cursorPathsByDistance.get(cursorPath.getCursorPathDistance()).add(cursorPath);
	}
	
	public void moveCursorToCoordinates(Point goalPoint) throws InterruptedException {
		Point startingCursorPoint = getCurrentCursorPoint();
		int distanceToMoveCursor = calculateDistanceBetweenPoints(startingCursorPoint, goalPoint);
		double thetaDirectionToMoveCursor = calculateThetaBetweenPoints(startingCursorPoint, goalPoint);
		
		CursorPath cursorPathToFollow = chooseCursorPathToFollowBasedOnDistance(distanceToMoveCursor);
		System.out.println("Starting from " + startingCursorPoint.x + ", " + startingCursorPoint.y);
		System.out.println("Moving to " + goalPoint.x + ", " + goalPoint.y);
		System.out.println("Moving in " + thetaDirectionToMoveCursor / Math.PI * 180 + " degree direction.");
		followCursorPath(startingCursorPoint, thetaDirectionToMoveCursor, cursorPathToFollow);
	}
	
	private void followCursorPath(Point startingCursorPoint, double thetaDirectionToMoveCursor, CursorPath cursorPathToFollow) throws InterruptedException {
		for (CursorPoint translationPoint : cursorPathToFollow.getCursorPathPoints()) {
			robotMouseMove(calculatePoint(startingCursorPoint, thetaDirectionToMoveCursor, translationPoint));
			Thread.sleep(50);
		}
	}
	
	private Point calculatePoint(Point startingCursorPoint, double thetaDirectionToMoveCursor, CursorPoint translationPoint) {
		int x = (int) (startingCursorPoint.x + Math.cos(thetaDirectionToMoveCursor) * translationPoint.x);
		int y = (int) (startingCursorPoint.y + Math.sin(thetaDirectionToMoveCursor) * translationPoint.y);
		return new Point(x, y);
	}
	
	private void robotMouseMove(Point pointToMoveCursorTo) {
		robot.mouseMove(pointToMoveCursorTo.x, pointToMoveCursorTo.y);
	}

	private CursorPath chooseCursorPathToFollowBasedOnDistance(int distanceToMoveCursor) {
		ArrayList<CursorPath> cursorPathsWithSameDistance = cursorPathsByDistance.get(distanceToMoveCursor);
		return cursorPathsWithSameDistance.get(new Random().nextInt(cursorPathsWithSameDistance.size()));
	}
	
	private int calculateDistanceBetweenPoints(Point a, Point b) {
		return (int) (Math.hypot(a.x - b.x, a.y - b.y));
	}
	
	public double calculateThetaBetweenPoints(Point a, Point b) {
		return Math.atan2((b.y - a.y), (b.x - a.x));
	}
	
	private Point getCurrentCursorPoint() {
		return MouseInfo.getPointerInfo().getLocation();
	}
	
	
	public void displaycursorPathsByDistance() {
		for (int i = 0; i < cursorPathsByDistance.size(); i++) {
			System.out.println("There are " + cursorPathsByDistance.get(i).size() + " CursorPaths of length " + i);
		}
	}
}