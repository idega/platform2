/*
 * Created on 15.7.2003
 */
package is.idega.idegaweb.golf.handicap.business;

import is.idega.idegaweb.golf.course.data.Hole;
import is.idega.idegaweb.golf.course.data.Tee;
import is.idega.idegaweb.golf.handicap.data.Scorecard;
import is.idega.idegaweb.golf.handicap.data.ScorecardHome;
import is.idega.idegaweb.golf.handicap.data.Strokes;
import is.idega.idegaweb.golf.handicap.data.StrokesHome;
import is.idega.idegaweb.golf.handicap.data.StrokesPK;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;

/**
 * @author laddi
 */
public class HandicapBusinessBean extends IBOServiceBean implements HandicapBusiness {

	protected ScorecardHome getScorecardHome() {
		try {
			return (ScorecardHome) IDOLookup.getHome(Scorecard.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	protected StrokesHome getStrokesHome() {
		try {
			return (StrokesHome) IDOLookup.getHome(Strokes.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public Scorecard getScorecard(Object scorecardID) throws FinderException {
		return getScorecardHome().findByPrimaryKey(scorecardID);
	}

	public Collection getScorecardsByUser(Object userID) throws FinderException {
		return getScorecardHome().findAllByUser(userID);
	}

	public Collection getStrokesByScorecard(Object scorecardID) throws FinderException {
		return getStrokesHome().findAllByScorecard(scorecardID);
	}
	
	public Strokes getStrokes(Object scorecardID, Object holeID) throws FinderException {
		StrokesPK key = new StrokesPK(scorecardID, holeID);
		return getStrokesHome().findByPrimaryKey(key);
	}

	public int getCourseHandicap(float handicap, Tee tee) {
		return getCourseHandicap(handicap, tee, -1);
	}

	public int getCourseHandicap(float handicap, Tee tee, int maxHandicap) {
		return getCourseHandicap(handicap, tee.getSlope(), tee.getCourseRating(), tee.getPar(), maxHandicap);
	}

	public int getCourseHandicap(float handicap, int slope, float courseRating, int coursePar) {
		return getCourseHandicap(handicap, slope, courseRating, coursePar, -1);
	}
	
	public int getCourseHandicap(float handicap, int slope, float courseRating, int coursePar, int maxHandicap) {
		int courseHandicap = (int) Math.rint(handicap * (slope / 113) + (courseRating - coursePar));
		if (maxHandicap != -1 && maxHandicap < courseHandicap)
			return maxHandicap;
		return courseHandicap;
	}
	
	public boolean storeScorecard(Object scorecardID, Object userID, Object teeID, float handicap, int totalPoints, int totalStrokes, Timestamp datePlayed) {
		Scorecard scorecard = null;
		try {
			scorecard = getScorecardHome().findByPrimaryKey(scorecardID);
		}
		catch (FinderException fe) {
			try {
				scorecard = getScorecardHome().create();
			}
			catch (CreateException ce) {
				return false;
			}
		}
		
		scorecard.setHandicapBefore(handicap);
		scorecard.setTeeID(teeID);
		scorecard.setTotalPoints(totalPoints);
		scorecard.setTotalStrokes(totalStrokes);
		scorecard.setUserID(userID);
		scorecard.setDatePlayed(datePlayed);
		scorecard.store();
		
		return true;
	}
	
	private Strokes createStrokes(Object scorecardID, Object holeID) throws CreateException {
		StrokesPK key = new StrokesPK(scorecardID, holeID);
		return getStrokesHome().create(key);
	}
	
	public boolean storeStrokes(Object scorecardID, Object holeID, int strokes, int points, int putts, boolean hitFairway, boolean greenInRegulation) {
		Strokes stroke = null;
		try {
			stroke = getStrokes(scorecardID, holeID);
		}
		catch (FinderException fe) {
			try {
				stroke = createStrokes(scorecardID, holeID);
			}
			catch (CreateException ce) {
				ce.printStackTrace(System.err);
				return false;
			}
		}
		
		stroke.setStrokes(strokes);
		stroke.setPoints(points);
		stroke.setPutts(putts);
		stroke.setHitFairway(hitFairway);
		stroke.setGreenInRegulation(greenInRegulation);
		stroke.store();
		
		return true;
	}

	public void updateScorecard(Object scorecardID) {
		try {
			Scorecard scorecard = getScorecardHome().findByPrimaryKey(scorecardID);
			updateScorecard(scorecard);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void updateScorecard(Scorecard scorecard) {
		try {
			Tee tee = scorecard.getTee();
			int courseHandicap = getCourseHandicap(scorecard.getHandicapBefore(), tee.getSlope(), tee.getCourseRating(), tee.getPar());
			int totalPoints = scorecard.getTotalPoints();

			boolean updateScorecard = false;
			Collection strokes = getStrokesHome().findAllByScorecard(scorecard.getPrimaryKey());
			Iterator iter = strokes.iterator();
			while (iter.hasNext()) {
				Strokes stroke = (Strokes) iter.next();
				Hole hole = stroke.getHole();
				int points = getPointsForHole(hole.getHandicap(), hole.getPar(), courseHandicap, stroke.getStrokes());
				if (points != stroke.getPoints()) {
					updateScorecard = true;
					totalPoints += points - stroke.getPoints();
					stroke.setPoints(points);
					stroke.store();
				}
			}
			
			if (updateScorecard) {
				if (totalPoints != scorecard.getTotalPoints()) {
					scorecard.setTotalPoints(totalPoints);
					scorecard.store();
				}
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	private int getPointsForPar(int holeHandicap, int holePar, int courseHandicap) {
		int coursePoints = courseHandicap + 36;
		int points = 0;

		while (holeHandicap <= coursePoints) {
			points++;
			holeHandicap += 18;
		}

		return points;
	}

	public int getPointsForHole(int holeHandicap, int holePar, int courseHandicap, int strokes) {
		int points = holePar + getPointsForPar(holeHandicap, holePar, courseHandicap) - strokes;
		if (points < 0)
			return 0;
		return points;
	}
}