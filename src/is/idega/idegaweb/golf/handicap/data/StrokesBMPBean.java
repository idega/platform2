/*
 * Created on 14.7.2003
 */
package is.idega.idegaweb.golf.handicap.data;

import is.idega.idegaweb.golf.course.data.Hole;
import is.idega.idegaweb.golf.course.data.HoleBMPBean;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author laddi
 */
public class StrokesBMPBean extends GenericEntity implements Strokes {

	public static final String ENTITY_NAME = "golf_strokes";

	public static final String COLUMN_STROKES_ID = "strokes_id";
	public static final String COLUMN_SCORECARD_ID = ScorecardBMPBean.COLUMN_SCORECARD_ID;
	public static final String COLUMN_HOLE_ID = HoleBMPBean.COLUMN_HOLE_ID;
	public static final String COLUMN_STROKES = "strokes";
	public static final String COLUMN_POINTS = "points";
	public static final String COLUMN_PUTTS = "putts";
	public static final String COLUMN_GREEN_IN_REGULATION = "green_in_regulation";
	public static final String COLUMN_HIT_FAIRWAY = "hit_fairway";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMN_STROKES, "Strokes", true, true, Integer.class);
		addAttribute(COLUMN_POINTS, "Points", true, true, Integer.class);
		addAttribute(COLUMN_PUTTS, "Putts", true, true, Integer.class);
		addAttribute(COLUMN_GREEN_IN_REGULATION, "Green in regulation", true, true, Boolean.class);
		addAttribute(COLUMN_HIT_FAIRWAY, "Fairway hit", true, true, Boolean.class);
		
		addManyToOneRelationship(COLUMN_SCORECARD_ID, Scorecard.class);
		setAsPrimaryKey(COLUMN_SCORECARD_ID, true);
		addManyToOneRelationship(COLUMN_HOLE_ID, Hole.class);
		setAsPrimaryKey(COLUMN_HOLE_ID, true);
		
		setNullable(COLUMN_SCORECARD_ID, false);
		setNullable(COLUMN_HOLE_ID, false);
	}
	
	//Getters
	public int getStrokes() {
		return getIntColumnValue(COLUMN_STROKES);
	}

	public int getPoints() {
		return getIntColumnValue(COLUMN_POINTS);
	}

	public int getPutts() {
		return getIntColumnValue(COLUMN_PUTTS);
	}

	public boolean getGreenInRegulation() {
		return getBooleanColumnValue(COLUMN_GREEN_IN_REGULATION, false);
	}

	public boolean getHitFairway() {
		return getBooleanColumnValue(COLUMN_HIT_FAIRWAY, false);
	}

	public Object getScorecardID() {
		return getColumnValue(COLUMN_SCORECARD_ID);
	}

	public Scorecard getScorecard() {
		return (Scorecard) getColumnValue(COLUMN_SCORECARD_ID);
	}

	public Object getHoleID() {
		return getColumnValue(COLUMN_HOLE_ID);
	}

	public Hole getHole() {
		return (Hole) getColumnValue(COLUMN_HOLE_ID);
	}
	
	//Setters
	public void setStrokes(int strokes) {
		setColumn(COLUMN_STROKES, strokes);
	}
	
	public void setPoints(int points) {
		setColumn(COLUMN_POINTS, points);
	}
	
	public void setPutts(int putts) {
		setColumn(COLUMN_PUTTS, putts);
	}
	
	public void setGreenInRegulation(boolean greenInRegulation) {
		setColumn(COLUMN_GREEN_IN_REGULATION, greenInRegulation);
	}
	
	public void setHitFairway(boolean hitFairway) {
		setColumn(COLUMN_HIT_FAIRWAY, hitFairway);
	}
	
	public void setScorecardID(Object scorecardID) {
		setColumn(COLUMN_SCORECARD_ID, scorecardID);
	}
	
	public void setScorecard(Scorecard scorecard) {
		setColumn(COLUMN_SCORECARD_ID, scorecard);
	}
	
	public void setHoleID(Object holeID) {
		setColumn(COLUMN_HOLE_ID, holeID);
	}
	
	public void setHole(Hole hole) {
		setColumn(COLUMN_HOLE_ID, hole);
	}
	
	public Object ejbFindByPrimaryKey(Object scorecardID, Object holeID) throws FinderException {
		return ejbFindStrokesByScorecardAndHole(scorecardID, holeID);
	}
	
	public Object ejbCreate(Object scorecardID, Object holeID) throws CreateException {
		setScorecardID(scorecardID);
		setHoleID(holeID);
		return super.ejbCreate();
	}

	protected void ejbPostCreate(Object scorecardID, Object holeID) {
		//does nothing
	}

	//Find methods
	public Collection ejbFindAllByScorecard(Object scorecardID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(getEntityName()).append(" s,").append(HoleBMPBean.ENTITY_NAME).append(" h");
		query.appendWhereEquals("s." + COLUMN_HOLE_ID, "h." + COLUMN_HOLE_ID).appendAndEquals(COLUMN_SCORECARD_ID, scorecardID).appendOrderBy("h." + HoleBMPBean.COLUMN_NUMBER);
		
		return idoFindPKsByQuery(query);
	}
	
	public Object ejbFindStrokesByScorecardAndHole(Object scorecardID, Object holeID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_SCORECARD_ID, scorecardID).appendAndEquals(COLUMN_HOLE_ID, holeID);
		
		return idoFindOnePKByQuery(query);
	}

	public Object ejbFindStrokesByScorecardAndHoleNumber(Object scorecardID, int holeNumber) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(getEntityName()).append(" s,").append(HoleBMPBean.ENTITY_NAME).append(" h");
		query.appendWhereEquals("s." + COLUMN_HOLE_ID, "h." + COLUMN_HOLE_ID).appendAndEquals(COLUMN_SCORECARD_ID, scorecardID).appendAndEquals("h."+HoleBMPBean.COLUMN_NUMBER, holeNumber);
		
		return idoFindOnePKByQuery(query);
	}
}