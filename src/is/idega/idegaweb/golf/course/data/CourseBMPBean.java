/*
 * Created on 9.7.2003
 */
package is.idega.idegaweb.golf.course.data;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.venue.data.AbstractVenueBMPBean;
import com.idega.block.venue.data.Venue;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.Group;

/**
 * @author laddi
 */
public class CourseBMPBean extends AbstractVenueBMPBean implements Course {

	protected String getTypeName() {
		return "GOLF_COURSE";
	}

	protected String getTypeDescription() {
		return "The default type for all golf courses in the idegaWeb Golf System";
	}

	public String getIDColumnName() {
		return COLUMN_COURSE_ID;
	}

	public String getEntityName() {
		return Course.ENTITY_NAME;
	}

	public void initializeAttributes() {
		addGeneralVenueRelation();
		
		addAttribute(COLUMN_NUMBER_OF_HOLES, "Number of holes", true, true, Integer.class);
	}
	
	public void insertStartData() {
		super.insertStartData();
		try {
			Course course = ((CourseHome) com.idega.data.IDOLookup.getHome(Course.class)).create();
			course.setName("Test course");
			course.setDescription("The default test course in the system");
			course.setNumberOfHoles(18);
			course.store();
		}
		catch (IDOLookupException ile) {
			log(ile);
		}
		catch (CreateException ce) {
			log(ce);
		}
	}
	
	//Getters
	public int getNumberOfHoles() {
		return getIntColumnValue(COLUMN_NUMBER_OF_HOLES);
	}	

	//Setters
	public void setNumberOfHoles(int numberOfHoles) {
		setColumn(COLUMN_NUMBER_OF_HOLES, numberOfHoles);
	}	

	//Find methods
	public Collection ejbFindAllCourses() throws FinderException {
		Table table = new Table(this);
		Table venue = new Table(Venue.class);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		try {
			query.addJoin(table, venue);
		}
		catch (IDORelationshipException ire) {
			throw new FinderException(ire.getMessage());
		}
		query.addOrder(venue, Venue.COLUMN_NAME, true);
		
		return this.idoFindPKsBySQL(query.toString());
	}

	public Collection ejbFindCoursesByClub(Group club) throws FinderException {
		Table table = new Table(this);
		Table venue = new Table(Venue.class);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		try {
			query.addJoin(table, venue);
		}
		catch (IDORelationshipException ire) {
			throw new FinderException(ire.getMessage());
		}
		query.addCriteria(new MatchCriteria(venue, Venue.COLUMN_OWNER, MatchCriteria.EQUALS, club));
		query.addOrder(venue, Venue.COLUMN_NAME, true);
		
		return this.idoFindPKsBySQL(query.toString());
	}

	public Integer ejbFindCourseByClubAndName(Group club, String name) throws FinderException {
		Table table = new Table(this);
		Table venue = new Table(Venue.class);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		try {
			query.addJoin(table, venue);
		}
		catch (IDORelationshipException ire) {
			throw new FinderException(ire.getMessage());
		}
		query.addCriteria(new MatchCriteria(venue, Venue.COLUMN_OWNER, MatchCriteria.EQUALS, club));
		query.addCriteria(new MatchCriteria(venue, Venue.COLUMN_NAME, MatchCriteria.EQUALS, name));

		return (Integer) idoFindOnePKBySQL(query.toString());
	}

	public Integer ejbFindCourseByName(String name) throws FinderException {
		Table table = new Table(this);
		Table venue = new Table(Venue.class);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		try {
			query.addJoin(table, venue);
		}
		catch (IDORelationshipException ire) {
			throw new FinderException(ire.getMessage());
		}
		query.addCriteria(new MatchCriteria(venue, Venue.COLUMN_NAME, MatchCriteria.EQUALS, name));

		return (Integer) idoFindOnePKBySQL(query.toString());
	}
}