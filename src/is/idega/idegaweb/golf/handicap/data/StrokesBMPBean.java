/*
 * Created on 14.7.2003
 */
package is.idega.idegaweb.golf.handicap.data;

import is.idega.idegaweb.golf.course.data.Hole;
import is.idega.idegaweb.golf.course.data.HoleBMPBean;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author laddi
 */
public class StrokesBMPBean extends GenericEntity implements Strokes {

	public static final String ENTITY_NAME = "golf_scorecard";

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
		addAttribute(COLUMN_STROKES_ID);
		setAsPrimaryKey(COLUMN_STROKES_ID, true);
		
		addAttribute(COLUMN_STROKES, "Strokes", true, true, Integer.class);
		addAttribute(COLUMN_POINTS, "Points", true, true, Integer.class);
		addAttribute(COLUMN_PUTTS, "Putts", true, true, Integer.class);
		addAttribute(COLUMN_GREEN_IN_REGULATION, "Green in regulation", true, true, Boolean.class);
		addAttribute(COLUMN_HIT_FAIRWAY, "Fairway hit", true, true, Boolean.class);
		addManyToOneRelationship(COLUMN_SCORECARD_ID, Scorecard.class);
		addManyToOneRelationship(COLUMN_HOLE_ID, Hole.class);
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

	public int getScorecardID() {
		return getIntColumnValue(COLUMN_SCORECARD_ID);
	}

	public Scorecard getScorecard() {
		return (Scorecard) getColumnValue(COLUMN_SCORECARD_ID);
	}

	public int getHoleID() {
		return getIntColumnValue(COLUMN_HOLE_ID);
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
	
	public void setScorecardID(int scorecardID) {
		setColumn(COLUMN_SCORECARD_ID, scorecardID);
	}
	
	public void setScorecard(Scorecard scorecard) {
		setScorecardID(((Integer) scorecard.getPrimaryKey()).intValue());
	}
	
	public void setHoleID(int holeID) {
		setColumn(COLUMN_HOLE_ID, holeID);
	}
	
	public void setHole(Hole hole) {
		setHoleID(((Integer) hole.getPrimaryKey()).intValue());
	}
	
	//Find methods
	public Collection ejbFindAllByScorecardID(int scorecardID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(getEntityName()).append(" s,").append(HoleBMPBean.ENTITY_NAME).append(" h");
		query.appendWhereEquals("s." + COLUMN_HOLE_ID, "h." + COLUMN_HOLE_ID).appendOrderBy("h." + HoleBMPBean.COLUMN_NUMBER);
		
		return idoFindPKsByQuery(query);
	}
}