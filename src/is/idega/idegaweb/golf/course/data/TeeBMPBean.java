/*
 * Created on 9.7.2003
 */
package is.idega.idegaweb.golf.course.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Gender;

/**
 * @author laddi
 */
public class TeeBMPBean extends GenericEntity implements Tee {

	private static final String ENTITY_NAME = "golf_tee";

	private static final String COLUMN_NAME = "tee_name";
	private static final String COLUMN_SLOPE = "slope";
	private static final String COLUMN_COURSE_RATING = "course_rating";
	private static final String COLUMN_PAR = "par";
	private static final String COLUMN_VALID_FROM = "valid_from";
	private static final String COLUMN_VALID_TO = "valid_to";
	private static final String COLUMN_GENDER_ID = "gender_id";
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
		addAttribute(COLUMN_NAME, "The tee name", true, true, String.class, 255);
		addAttribute(COLUMN_SLOPE, "The slope", true, true, Integer.class);
		addAttribute(COLUMN_COURSE_RATING, "The course rating", true, true, Float.class);
		addAttribute(COLUMN_PAR, "The par", true, true, Integer.class);
		addAttribute(COLUMN_VALID_FROM, "Valid from date", true, true, Date.class);
		addAttribute(COLUMN_VALID_TO, "Valid to date", true, true, Date.class);
		addManyToOneRelationship(COLUMN_COURSE_ID, Course.class);
		addManyToOneRelationship(COLUMN_TEE_COLOR_ID, TeeColor.class);
		addManyToOneRelationship(COLUMN_GENDER_ID, Gender.class);
	}
	
	//Getters
	public String getTeeName() {
		return getStringColumnValue(COLUMN_NAME);
	}

	public int getSlope() {
		return getIntColumnValue(COLUMN_SLOPE);
	}

	public float getCourseRating() {
		return getFloatColumnValue(COLUMN_COURSE_RATING);
	}

	public int getPar() {
		return getIntColumnValue(COLUMN_PAR);
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
	
	public int getGenderID() {
		return getIntColumnValue(COLUMN_GENDER_ID);
	}

	public Gender getGender() {
		return (Gender) getColumnValue(COLUMN_GENDER_ID);
	}
	
	//Setters
	public void setTeeName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setSlope(int slope) {
		setColumn(COLUMN_SLOPE, slope);
	}

	public void setCourseRating(float courseRating) {
		setColumn(COLUMN_COURSE_RATING, courseRating);
	}

	public void setPar(int par) {
		setColumn(COLUMN_PAR, par);
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
	
	public void setGenderID(int genderID) {
		setColumn(COLUMN_GENDER_ID, genderID);
	}
	
	//Find methods
	public Collection ejbFindAllByCourse(int courseID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_COURSE_ID, courseID).appendAndIsNull(COLUMN_VALID_TO);
		return idoFindPKsByQuery(query);
	}

	public Integer ejbFindAllByCourse(int courseID, int teeColorID, int genderID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_COURSE_ID, courseID).appendAndIsNull(COLUMN_VALID_TO);
		query.appendAndEquals(COLUMN_TEE_COLOR_ID, teeColorID).appendAndEquals(COLUMN_GENDER_ID, genderID);
		return (Integer) idoFindOnePKByQuery(query);
	}
}