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
import com.idega.data.IDOQuery;
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
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

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
	}
	
	//Getters
	public int getTotalPoints() {
		return getIntColumnValue(COLUMN_TOTAL_POINTS);
	}

	public int getTotalStrokes() {
		return getIntColumnValue(COLUMN_TOTAL_STROKES);
	}

	public float getHandicapBefore() {
		return getFloatColumnValue(COLUMN_HANDICAP_BEFORE);
	}

	public float getHandicapAfter() {
		return getFloatColumnValue(COLUMN_HANDICAP_AFTER);
	}

	public Timestamp getDatePlayed() {
		return (Timestamp) getColumnValue(COLUMN_DATE_PLAYED);
	}
	
	public boolean getCanIncreaseHandicap() {
		return getBooleanColumnValue(COLUMN_CAN_INCREASE_HANDICAP, false);
	}

	public boolean getCanDecreaseHandicap() {
		return getBooleanColumnValue(COLUMN_CAN_DECREASE_HANDICAP, false);
	}

	public boolean getIsCorrection() {
		return getBooleanColumnValue(COLUMN_IS_CORRECTION, false);
	}
	
	public int getUserID() {
		return getIntColumnValue(COLUMN_USER_ID);
	}

	public User getUser() {
		return (User) getColumnValue(COLUMN_USER_ID);
	}

	public int getTeeID() {
		return getIntColumnValue(COLUMN_TEE_ID);
	}
	
	public Tee getTee() {
		return (Tee) getColumnValue(COLUMN_TEE_ID);
	}
	
	//Setters
	public void setTotalPoints(int totalPoints) {
		setColumn(COLUMN_TOTAL_POINTS, totalPoints);
	}

	public void setTotalStrokes(int totalStrokes) {
		setColumn(COLUMN_TOTAL_STROKES, totalStrokes);
	}

	public void setHandicapBefore(float handicapBefore) {
		setColumn(COLUMN_HANDICAP_BEFORE, handicapBefore);
	}

	public void setHandicapAfter(float handicapAfter) {
		setColumn(COLUMN_HANDICAP_AFTER, handicapAfter);
	}

	public void setCanIncreaseHandicap(boolean canIncreaseHandicap) {
		setColumn(COLUMN_CAN_INCREASE_HANDICAP, canIncreaseHandicap);
	}

	public void setCanDecreaseHandicap(boolean canDecreaseHandicap) {
		setColumn(COLUMN_CAN_DECREASE_HANDICAP, canDecreaseHandicap);
	}

	public void setIsCorrection(boolean isCorrection) {
		setColumn(COLUMN_IS_CORRECTION, isCorrection);
	}

	public void setUserID(int userID) {
		setColumn(COLUMN_USER_ID, userID);
	}
	
	public void setUser(User user) {
		setUserID(((Integer) user.getPrimaryKey()).intValue());
	}
	
	public void setTeeID(int teeID) {
		setColumn(COLUMN_TEE_ID, teeID);
	}

	public void setTee(Tee tee) {
		setTeeID(((Integer) tee.getPrimaryKey()).intValue());
	}
	
	//Find methods
	public Collection ejbFindAllByUser(int userID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_USER_ID, userID).appendOrderBy(COLUMN_DATE_PLAYED);
		return idoFindPKsByQuery(query);
	}
}