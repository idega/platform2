/*
 * Created on 15.7.2003
 */
package is.idega.idegaweb.golf.handicap.business;

import is.idega.idegaweb.golf.course.data.Tee;
import is.idega.idegaweb.golf.handicap.data.Scorecard;
import is.idega.idegaweb.golf.handicap.data.ScorecardHome;
import is.idega.idegaweb.golf.handicap.data.Strokes;
import is.idega.idegaweb.golf.handicap.data.StrokesHome;

import java.rmi.RemoteException;
import java.util.Collection;

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
	
	public Scorecard getScorecard(int scorecardID) throws RemoteException, FinderException {
		return getScorecardHome().findByPrimaryKey(new Integer(scorecardID));
	}
	
	public Collection getScorecardsByUser(int userID) throws RemoteException, FinderException {
		return getScorecardHome().findAllByUser(userID);
	}
	
	public Collection getStrokes(int scorecardID) throws RemoteException, FinderException {
		return getStrokesHome().findAllByScorecardID(scorecardID);
	}
	
	public int getCourseHandicap(float handicap, Tee tee) {
		return getCourseHandicap(handicap, tee.getSlope(), tee.getCourseRating(), tee.getPar());
	}
	
	public int getCourseHandicap(float handicap, int slope, float courseRating, int coursePar) {
		float courseHandicap = handicap * (slope / 113) + (courseRating - coursePar);
		return (int) Math.rint(courseHandicap);
	}
}