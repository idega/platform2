/*
 * Created on 9.7.2003
 */
package is.idega.idegaweb.golf.course.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;

/**
 * @author laddi
 */
public class CourseBMPBean extends GenericEntity implements Course {

	private static final String ENTITY_NAME = "golf_course";

	private static final String COLUMN_NAME = "course_name";
	private static final String COLUMN_TYPE = "course_type";
	private static final String COLUMN_NUMBER_OF_HOLES = "number_of_holes";
	private static final String COLUMN_CLUB_ID = "club_id";
	
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
		addAttribute(COLUMN_NAME, "The course name", true, true, String.class, 255);
		addAttribute(COLUMN_TYPE, "The course's type", true, true, String.class, 255);
		addAttribute(COLUMN_NUMBER_OF_HOLES, "Number of holes", true, true, Integer.class);
		addOneToOneRelationship(COLUMN_CLUB_ID, Group.class);
	}
	
	//Getters
	public String getCourseName() {
		return getStringColumnValue(COLUMN_NAME);
	}	

	public String getCourseType() {
		return getStringColumnValue(COLUMN_TYPE);
	}	

	public int getNumberOfHoles() {
		return getIntColumnValue(COLUMN_NUMBER_OF_HOLES);
	}	

	public int getClubID() {
		return getIntColumnValue(COLUMN_CLUB_ID);
	}	

	public Group getClub() {
		return (Group) getColumnValue(COLUMN_CLUB_ID);
	}
	
	//Setters
	public void setCourseName(String name) {
		setColumn(COLUMN_NAME, name);
	}

	public void setCourseType(String type) {
		setColumn(COLUMN_TYPE, type);
	}	

	public void setNumberOfHoles(int numberOfHoles) {
		setColumn(COLUMN_NUMBER_OF_HOLES, numberOfHoles);
	}	

	public void setClubID(int clubID) {
		setColumn(COLUMN_CLUB_ID, clubID);
	}	

	public void setClub(Group club) {
		setColumn(COLUMN_CLUB_ID, club.getPrimaryKey());
	}
	
	//Find methods
	public Collection ejbFindAllCourses() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);

		return this.idoFindPKsByQuery(query);
	}

	public Collection ejbFindCoursesByClub(int clubID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CLUB_ID, clubID);

		return this.idoFindPKsByQuery(query);
	}

	public Collection ejbFindCoursesByType(String type) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_TYPE, type);

		return this.idoFindPKsByQuery(query);
	}

	public Integer ejbFindCourseByClubAndName(int clubID, String name) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CLUB_ID, clubID).appendAndEqualsQuoted(COLUMN_NAME, name);

		return (Integer) idoFindOnePKByQuery(query);
	}

	public Integer ejbFindCourseByName(String name) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(COLUMN_NAME, name);

		return (Integer) idoFindOnePKByQuery(query);
	}

}