/*
 * Created on 14.7.2003
 */
package is.idega.idegaweb.golf.handicap.data;

import is.idega.idegaweb.golf.course.data.Tee;
import is.idega.idegaweb.golf.course.data.TeeBMPBean;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.user.data.User;

/**
 * @author laddi
 */
public class ScorecardBMPBean extends GenericEntity implements Scorecard {

	public static final String ENTITY_NAME = "golf_scorecard";

	public static final String COLUMN_SCORECARD_ID = "scorecard_id";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_DATE_PLAYED = "date_played";
	public static final String COLUMN_TEE_ID = TeeBMPBean.COLUMN_TEE_ID;
	public static final String COLUMN_TOTAL_POINTS = "total_points";
	public static final String COLUMN_TOTAL_STROKES = "total_strokes";
	public static final String COLUMN_HANDICAP_BEFORE = "handicap_before";
	public static final String COLUMN_HANDICAP_AFTER = "handicap_after";
	public static final String COLUMN_CAN_INCREASE_HANDICAP = "can_increase_handicap";
	public static final String COLUMN_CAN_DECREASE_HANDICAP = "can_decrease_handicap";
	public static final String COLUMN_IS_CORRECTION = "is_correction";

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(COLUMN_SCORECARD_ID);
		setAsPrimaryKey(COLUMN_SCORECARD_ID, true);
		
		addAttribute(COLUMN_TOTAL_POINTS, "Total points", true, true, Integer.class);
		addAttribute(COLUMN_TOTAL_STROKES, "Total strokes", true, true, Integer.class);
		addAttribute(COLUMN_HANDICAP_BEFORE, "Handicap before", true, true, Float.class);
		addAttribute(COLUMN_HANDICAP_AFTER, "Handicap after", true, true, Float.class);
		addAttribute(COLUMN_DATE_PLAYED, "Date played", true, true, Timestamp.class);
		addAttribute(COLUMN_CAN_INCREASE_HANDICAP, "Can increase handicap", true, true, Boolean.class);
		addAttribute(COLUMN_CAN_DECREASE_HANDICAP, "Can decrease handicap", true, true, Boolean.class);
		addAttribute(COLUMN_IS_CORRECTION, "Is handicap correction", true, true, Boolean.class);

		addManyToOneRelationship(COLUMN_USER_ID, User.class);
		addManyToOneRelationship(COLUMN_TEE_ID, Tee.class);
		
		setNullable(COLUMN_USER_ID, false);
		setNullable(COLUMN_TEE_ID, false);
	}

///////////////////////////////////////////////////
//      finders      
///////////////////////////////////////////////////

	public Collection ejbFindAll() throws FinderException {
		Table scorecard = new Table(this);
		
		SelectQuery query = new SelectQuery(scorecard);
		query.addColumn(scorecard, COLUMN_SCORECARD_ID);

		return idoFindPKsBySQL(query.toString());
	}

	//Find methods
	public Collection ejbFindAllByUser(Object userID) throws FinderException {
		Table scorecard = new Table(this);
		
		SelectQuery query = new SelectQuery(scorecard);
		query.addColumn(scorecard, COLUMN_SCORECARD_ID);
		
		query.addCriteria(new MatchCriteria(scorecard, COLUMN_USER_ID, MatchCriteria.EQUALS, ((Integer)userID).intValue()));
		query.addOrder(scorecard, COLUMN_DATE_PLAYED, true);

		//IDOQuery query = idoQuery();
		//query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_USER_ID, userID).appendOrderBy(COLUMN_DATE_PLAYED);
		return idoFindPKsBySQL(query.toString());
	}

///////////////////////////////////////////////////
//      getters      
///////////////////////////////////////////////////

	public boolean getCanDecreaseHandicap() {
		return getBooleanColumnValue(COLUMN_CAN_DECREASE_HANDICAP, false);
	}

	public boolean getCanIncreaseHandicap() {
		return getBooleanColumnValue(COLUMN_CAN_INCREASE_HANDICAP, false);
	}

	public Timestamp getDatePlayed() {
		return (Timestamp) getColumnValue(COLUMN_DATE_PLAYED);
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public float getHandicapAfter() {
		return getFloatColumnValue(COLUMN_HANDICAP_AFTER);
	}

	public float getHandicapBefore() {
		return getFloatColumnValue(COLUMN_HANDICAP_BEFORE);
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getIDColumnName()
	 */
	public String getIDColumnName() {
		return COLUMN_SCORECARD_ID;
	}

	public boolean getIsCorrection() {
		return getBooleanColumnValue(COLUMN_IS_CORRECTION, false);
	}

	public Tee getTee() {
		return (Tee) getColumnValue(COLUMN_TEE_ID);
	}

	public Object getTeeID() {
		return getColumnValue(COLUMN_TEE_ID);
	}

	//Getters
	public int getTotalPoints() {
		return getIntColumnValue(COLUMN_TOTAL_POINTS);
	}

	public int getTotalStrokes() {
		return getIntColumnValue(COLUMN_TOTAL_STROKES);
	}

	public User getUser() {
		return (User) getColumnValue(COLUMN_USER_ID);
	}

	public Object getUserID() {
		return getColumnValue(COLUMN_USER_ID);
	}

///////////////////////////////////////////////////
//      setters      
///////////////////////////////////////////////////

	public void setCanDecreaseHandicap(boolean canDecreaseHandicap) {
		setColumn(COLUMN_CAN_DECREASE_HANDICAP, canDecreaseHandicap);
	}

	public void setCanIncreaseHandicap(boolean canIncreaseHandicap) {
		setColumn(COLUMN_CAN_INCREASE_HANDICAP, canIncreaseHandicap);
	}

	public void setDatePlayed(Timestamp datePlayed) {
		setColumn(COLUMN_DATE_PLAYED, datePlayed);
	}

	public void setHandicapAfter(float handicapAfter) {
		setColumn(COLUMN_HANDICAP_AFTER, handicapAfter);
	}

	public void setHandicapBefore(float handicapBefore) {
		setColumn(COLUMN_HANDICAP_BEFORE, handicapBefore);
	}

	public void setIsCorrection(boolean isCorrection) {
		setColumn(COLUMN_IS_CORRECTION, isCorrection);
	}

	public void setTee(Tee tee) {
		setColumn(COLUMN_TEE_ID, tee);
	}

	public void setTeeID(Object teeID) {
		setColumn(COLUMN_TEE_ID, teeID);
	}

	//Setters
	public void setTotalPoints(int totalPoints) {
		setColumn(COLUMN_TOTAL_POINTS, totalPoints);
	}

	public void setTotalStrokes(int totalStrokes) {
		setColumn(COLUMN_TOTAL_STROKES, totalStrokes);
	}

	public void setUser(User user) {
		setColumn(COLUMN_USER_ID, user);
	}

	public void setUserID(Object userID) {
		setColumn(COLUMN_USER_ID, userID);
	}

///////////////////////////////////////////////////
//      -----      
///////////////////////////////////////////////////
}