/*
 * Represents each mouse path as an ArrayList of points.
 */

import java.util.ArrayList;

public class CursorPath {
		
	private ArrayList<CursorPoint> pathCursorPoints;
	private int pathNumPoints;
	private int pathDistance;
	private double pathTheta;
	private int pathTimespanMilliseconds;
		
	public CursorPath(ArrayList<CursorPoint> cursorPoints)
	{
		this.pathCursorPoints = copyCursorPointsWithOffset(cursorPoints);
		this.pathNumPoints = cursorPoints.size();
		this.pathDistance = calculateCursorPathDistance();
		this.pathTheta = calculateCursorPathTheta();
		this.pathTimespanMilliseconds = calculateCursorPathTimespan();
	}
	
	private ArrayList<CursorPoint> copyCursorPointsWithOffset(ArrayList<CursorPoint> cursorPoints) {
		ArrayList<CursorPoint> cursorPointsCopy = new ArrayList<CursorPoint>(cursorPoints.size());
		CursorPoint startingCursorPoint = cursorPoints.get(0);
		for (CursorPoint cursorPoint : cursorPoints) {
			cursorPointsCopy.add(getOffsetCursorPoint(cursorPoint, startingCursorPoint));
		}
		calculatePostMillisecondDelays(cursorPointsCopy);
		return cursorPointsCopy;
	}
	
	private void calculatePostMillisecondDelays(ArrayList<CursorPoint> cursorPoints) {
		for (int i = 1; i < cursorPoints.size(); i++) {
			cursorPoints.get(i - 1).setPostMillisecondDelay(cursorPoints.get(i).postMillisecondDelay - cursorPoints.get(i - 1).postMillisecondDelay);
		}
		cursorPoints.get(cursorPoints.size() - 1).setPostMillisecondDelay(0);
	}
	
	private CursorPoint getOffsetCursorPoint(CursorPoint cursorPoint, CursorPoint offsetPoint) {
		return new CursorPoint(cursorPoint.x - offsetPoint.x, cursorPoint.y - offsetPoint.y, cursorPoint.postMillisecondDelay);
	}
	
	private int calculateCursorPathTimespan() {
		int sumPathTimespanMilliseconds = 0;
		for (CursorPoint cursorPoint : this.pathCursorPoints) {
			sumPathTimespanMilliseconds += cursorPoint.postMillisecondDelay;
		}
		return sumPathTimespanMilliseconds;
	}
	
	private int calculateCursorPathDistance() {
		return (int) calculateDistanceBetweenCursorPoints(getStartingCursorPoint(), getEndingCursorPoint());
	}
	
	private double calculateCursorPathTheta() {
		CursorPoint endingCursorPoint = getEndingCursorPoint();
		return Math.atan2(1.0 * endingCursorPoint.y, 1.0 * endingCursorPoint.x);
	}
	
	private CursorPoint getStartingCursorPoint() {
		return pathCursorPoints.get(0);
	}
	
	private CursorPoint getEndingCursorPoint() {
		return pathCursorPoints.get(pathNumPoints - 1);
	}
	
	private double calculateDistanceBetweenCursorPoints(CursorPoint a, CursorPoint b) {
		return Math.hypot(a.x - b.x, a.y - b.y);
	}
	
	public boolean isCursorPathReasonable() {
		return isCursorPathTimespanReasonable() && isCursorPathDistanceReasonable() &&
				isCursorPathNumPointsReasonable();
	}
	
	private boolean isCursorPathTimespanReasonable() {
		return (this.pathTimespanMilliseconds > 100 && this.pathTimespanMilliseconds < 400);
	}
	
	private boolean isCursorPathDistanceReasonable() {
		return (this.pathDistance > 5 && this.pathDistance < 1000);
	}
	
	private boolean isCursorPathNumPointsReasonable() {
		return (this.pathNumPoints > 5 && this.pathNumPoints < 50);
	}
	
	public ArrayList<CursorPoint> getCursorPathPoints() {
		return pathCursorPoints;
	}
	
	public int getCursorPathDistance() {
		return pathDistance;
	}
	
	public double getCursorPathTheta() {
		return pathTheta;
	}
	
	public void displayCursorPoints() {
		for (CursorPoint p : pathCursorPoints) {
			p.display();
		}
		System.out.println("Length:" + pathNumPoints + ", Timespan:" + pathTimespanMilliseconds);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~ End of Path ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}