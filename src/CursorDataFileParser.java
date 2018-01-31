import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CursorDataFileParser {
	
	String pathToCursorData;
	Pattern regexPatternToFilterInvalidLines;
	
	public CursorDataFileParser(String pathToCursorData) {
		this.pathToCursorData = pathToCursorData;
		this.regexPatternToFilterInvalidLines = Pattern.compile("[0-9]*,[0-9]*,[0-9]*$");
	}

	public ArrayList<CursorPath> getArrayListOfCursorPathsFromFile() {
		ArrayList<CursorPath> cursorPaths = new ArrayList<CursorPath>();
		try {
			File file = new File(this.pathToCursorData);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			String line;
			CursorPoint lastCursorPoint = new CursorPoint(0, 0, 0);
			int numberOfRepeats = 0;
			ArrayList<CursorPoint> currentCursorPath = new ArrayList<CursorPoint>();
			currentCursorPath.add(lastCursorPoint);

			while ((line = bufferedReader.readLine()) != null) {
				if (lineMatchesPattern(line)) {
					CursorPoint newCursorPoint = getCursorPointFromLine(line);
					if (cursorPointsHaveEqualCoordinates(newCursorPoint, lastCursorPoint)) {
						numberOfRepeats++;
						if (numberOfRepeats == 20) {
							CursorPath newCursorPath = new CursorPath(currentCursorPath);
							cursorPaths.add(newCursorPath);
							currentCursorPath.clear();
						}
					}
					else {
						numberOfRepeats = 0;
						currentCursorPath.add(newCursorPoint);
						lastCursorPoint = newCursorPoint;
					}
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished parsing cursor data...");
		return cursorPaths;
	}
	
	private boolean lineMatchesPattern(String line) {	
		Matcher regexMatcher = this.regexPatternToFilterInvalidLines.matcher(line);
		return regexMatcher.find();
	}
	
	private CursorPoint getCursorPointFromLine(String line) {
		String[] parts = line.split(Pattern.quote(","));
		return new CursorPoint(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
	}
	
	private boolean cursorPointsHaveEqualCoordinates(CursorPoint a, CursorPoint b) {
		return (a.x == b.x && a.y == b.y);
	}
}
