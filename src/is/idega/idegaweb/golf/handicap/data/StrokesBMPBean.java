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
import com.idega.data.IDOException;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

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

///////////////////////////////////////////////////
//      Counters      
///////////////////////////////////////////////////

	public int ejbHomeGetCount(Object scorecardID) throws IDOException {
		Table hole = new Table(Hole.class);
		Table strokes = new Table(this);
		
		SelectQuery query = new SelectQuery(strokes);
		query.setAsCountQuery(true);
		query.addColumn(new WildCardColumn());
		try {
			query.addJoin(strokes, hole);
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		query.addCriteria(new MatchCriteria(strokes, this.COLUMN_SCORECARD_ID, MatchCriteria.EQUALS, ((Integer)scorecardID).intValue()));
		query.addOrder(hole, HoleBMPBean.COLUMN_NUMBER, true);

		return idoGetNumberOfRecords(query.toString());
	}

///////////////////////////////////////////////////
//      Creation      
///////////////////////////////////////////////////

	public Object ejbCreate(StrokesKey primaryKey) throws CreateException {
		setPrimaryKey(primaryKey);
		return super.ejbCreate();
	}

///////////////////////////////////////////////////
//      Finders      
///////////////////////////////////////////////////

	//Find methods
	public Collection ejbFindAllByScorecard(Object scorecardID) throws FinderException {
		Table strokes = new Table(this);
		Table hole = new Table(Hole.class);
		
		SelectQuery query = new SelectQuery(strokes);
		query.addColumn(strokes, COLUMN_SCORECARD_ID);
		query.addColumn(strokes, COLUMN_HOLE_ID);
		try {
			query.addJoin(strokes, hole);
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		query.addCriteria(new MatchCriteria(strokes, this.COLUMN_SCORECARD_ID, MatchCriteria.EQUALS, ((Integer)scorecardID).intValue()));
		query.addOrder(hole, HoleBMPBean.COLUMN_NUMBER, true);

		return idoFindPKsBySQL(query.toString());
	}

	public Object ejbFindByPrimaryKey(StrokesKey primaryKey) throws FinderException {
		return super.ejbFindByPrimaryKey(primaryKey);
	}

	public Object ejbFindStrokesByScorecardAndHoleNumber(Object scorecardID, int holeNumber) throws FinderException {
		Table strokes = new Table(this);
		Table hole = new Table(Hole.class);
		
		SelectQuery query = new SelectQuery(strokes);
		query.addColumn(strokes, COLUMN_SCORECARD_ID);
		query.addColumn(strokes, COLUMN_HOLE_ID);
		try {
			query.addJoin(strokes, hole);
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		query.addCriteria(new MatchCriteria(strokes, this.COLUMN_SCORECARD_ID, MatchCriteria.EQUALS, ((Integer)scorecardID).intValue()));
		query.addCriteria(new MatchCriteria(strokes, HoleBMPBean.COLUMN_NUMBER, MatchCriteria.EQUALS, holeNumber));

		return idoFindOnePKBySQL(query.toString());
	}

///////////////////////////////////////////////////
//      Getters      
///////////////////////////////////////////////////

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public boolean getGreenInRegulation() {
		return getBooleanColumnValue(COLUMN_GREEN_IN_REGULATION, false);
	}

	public boolean getHitFairway() {
		return getBooleanColumnValue(COLUMN_HIT_FAIRWAY, false);
	}

	public Hole getHole() {
		return (Hole) getColumnValue(COLUMN_HOLE_ID);
	}

	public Object getHoleID() {
		return getColumnValue(COLUMN_HOLE_ID);
	}

	public int getPoints() {
		return getIntColumnValue(COLUMN_POINTS);
	}

	/* (non-Javadoc)
	 * @see com.idega.data.IDOEntityBean#getPrimaryKeyClass()
	 */
	public Class getPrimaryKeyClass() {
		return StrokesKey.class;
	}

	public int getPutts() {
		return getIntColumnValue(COLUMN_PUTTS);
	}

	public Scorecard getScorecard() {
		return (Scorecard) getColumnValue(COLUMN_SCORECARD_ID);
	}

	public Object getScorecardID() {
		return getColumnValue(COLUMN_SCORECARD_ID);
	}

	//Getters
	public int getStrokes() {
		return getIntColumnValue(COLUMN_STROKES);
	}

///////////////////////////////////////////////////
//      Initialize/Finalize      
///////////////////////////////////////////////////

	protected boolean doInsertInCreate() {
		return true;
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

///////////////////////////////////////////////////
//      Setters      
///////////////////////////////////////////////////

	public void setGreenInRegulation(boolean greenInRegulation) {
		setColumn(COLUMN_GREEN_IN_REGULATION, greenInRegulation);
	}

	public void setHitFairway(boolean hitFairway) {
		setColumn(COLUMN_HIT_FAIRWAY, hitFairway);
	}

	public void setHole(Hole hole) {
		setColumn(COLUMN_HOLE_ID, hole);
	}

	public void setHoleID(Object holeID) {
		setColumn(COLUMN_HOLE_ID, holeID);
	}

	public void setPoints(int points) {
		setColumn(COLUMN_POINTS, points);
	}

	public void setPutts(int putts) {
		setColumn(COLUMN_PUTTS, putts);
	}

	public void setScorecard(Scorecard scorecard) {
		setColumn(COLUMN_SCORECARD_ID, scorecard);
	}

	public void setScorecardID(Object scorecardID) {
		setColumn(COLUMN_SCORECARD_ID, scorecardID);
	}

	//Setters
	public void setStrokes(int strokes) {
		setColumn(COLUMN_STROKES, strokes);
	}

///////////////////////////////////////////////////
//      -----      
///////////////////////////////////////////////////
}