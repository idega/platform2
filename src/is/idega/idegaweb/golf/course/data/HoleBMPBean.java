/*
 * Created on 14.7.2003
 */
package is.idega.idegaweb.golf.course.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author laddi
 */
public class HoleBMPBean extends GenericEntity implements Hole {

	private static final String ENTITY_NAME = "golf_hole";

	private static final String COLUMN_NAME = "hole_name";
	private static final String COLUMN_NUMBER = "hole_number";
	private static final String COLUMN_PAR = "par";
	private static final String COLUMN_HANDICAP = "handicap";
	private static final String COLUMN_VALID_FROM = "valid_from";
	private static final String COLUMN_VALID_TO = "valid_to";
	private static final String COLUMN_COURSE_ID = "course_id";
	private static final String COLUMN_TEE_COLOR_ID = "tee_color_id";

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
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Hole name", true, true, String.class, 255);
		addAttribute(COLUMN_NUMBER, "Hole number", true, true, Integer.class);
		addAttribute(COLUMN_PAR, "Hole par", true, true, Integer.class);
		addAttribute(COLUMN_HANDICAP, "Hole handicap", true, true, Integer.class);
		addAttribute(COLUMN_VALID_FROM, "Valid from date", true, true, Date.class);
		addAttribute(COLUMN_VALID_TO, "Valid to date", true, true, Date.class);
		addManyToOneRelationship(COLUMN_COURSE_ID, Course.class);
		addManyToOneRelationship(COLUMN_TEE_COLOR_ID, TeeColor.class);
	}

	//Getters
	public String getHoleName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public int getHoleNumber() {
		return getIntColumnValue(COLUMN_NUMBER);
	}

	public int getPar() {
		return getIntColumnValue(COLUMN_PAR);
	}

	public int getHandicap() {
		return getIntColumnValue(COLUMN_HANDICAP);
	}

	public Date getValidFrom() {
		return (Date) getColumnValue(COLUMN_VALID_FROM);
	}

	public Date getValidTo() {
		return (Date) getColumnValue(COLUMN_VALID_TO);
	}

	public int getCourseID() {
		return getIntColumnValue(COLUMN_COURSE_ID);
	}

	public Course getCourse() {
		return (Course) getColumnValue(COLUMN_COURSE_ID);
	}
	
	public int getTeeColorID() {
		return getIntColumnValue(COLUMN_TEE_COLOR_ID);
	}

	public TeeColor getTeeColor() {
		return (TeeColor) getColumnValue(COLUMN_TEE_COLOR_ID);
	}
	
	//Setters
	public void setHoleName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setHoleNumber(int holeNumber) {
		setColumn(COLUMN_NUMBER, holeNumber);
	}

	public void setPar(int par) {
		setColumn(COLUMN_PAR, par);
	}

	public void setHandicap(int handicap) {
		setColumn(COLUMN_HANDICAP, handicap);
	}

	public void setValidFrom(Date validFrom) {
		setColumn(COLUMN_VALID_FROM, validFrom);
	}

	public void setValidTo(Date validTo) {
		setColumn(COLUMN_VALID_TO, validTo);
	}

	public void setCourseID(int courseID) {
		setColumn(COLUMN_COURSE_ID, courseID);
	}
	
	public void setTeeColorID(int teeColorID) {
		setColumn(COLUMN_TEE_COLOR_ID, teeColorID);
	}
	
	//Find methods
	public Collection ejbFindAllByCourse(int courseID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_COURSE_ID, courseID).appendAndIsNull(COLUMN_VALID_TO);
		query.appendOrderBy(COLUMN_NUMBER);
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllByCourse(int courseID, int teeColorID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_COURSE_ID, courseID).appendAndEquals(COLUMN_TEE_COLOR_ID, teeColorID);
		query.appendAndIsNull(COLUMN_VALID_TO).appendOrderBy(COLUMN_NUMBER);
		return idoFindPKsByQuery(query);
	}
}