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

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;

/**
 * @author laddi
 */
public class HandicapBusinessBean extends IBOServiceBean implements HandicapBusiness {

	protected ScorecardHome getScorecardHome() throws RemoteException {
		return (ScorecardHome) IBOLookup.getServiceInstance(this.getIWApplicationContext(), Scorecard.class);
	}

	protected StrokesHome getStrokesHome() throws RemoteException {
		return (StrokesHome) IBOLookup.getServiceInstance(this.getIWApplicationContext(), Strokes.class);
	}

	public Scorecard getScorecard(Object scorecardPrimaryKey) throws RemoteException, FinderException {
		return getScorecardHome().findByPrimaryKey(scorecardPrimaryKey);
	}

	public Collection getScorecardsByUser(Object userPrimaryKey) throws RemoteException, FinderException {
		return getScorecardHome().findAllByUser(userPrimaryKey);
	}

	public Collection getStrokes(Object scorecardPrimaryKey) throws RemoteException, FinderException {
		return getStrokesHome().findAllByScorecard(scorecardPrimaryKey);
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

	public void updateScorecard(Object scorecardPrimaryKey) throws RemoteException {
		try {
			Scorecard scorecard = getScorecardHome().findByPrimaryKey(scorecardPrimaryKey);
			updateScorecard(scorecard);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void updateScorecard(Scorecard scorecard) throws RemoteException {
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