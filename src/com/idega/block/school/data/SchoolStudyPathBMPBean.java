package com.idega.block.school.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;

/**
 * @author Gimmi
 */
public class SchoolStudyPathBMPBean extends GenericEntity implements SchoolStudyPath{
	
	private static String TABLE_NAME           = "SCH_COURSE";
	private static String COLUMN_NAME          ="COURSE_NAME";
	private static String COLUMN_DESCRIPTION   = "DESCRIPTION";
	private static String COLUMN_SCHOOL        = "SCH_SCHOOL_ID";
	private static String COLUMN_SCHOOL_TYPE   = "SCH_SCHOOL_TYPE_ID";
	private static String COLUMN_IS_VALID      = "IS_VALID";
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "course name", true, true, String.class);
		addAttribute(COLUMN_DESCRIPTION, "description, ", true, true, String.class, 4000);
		addAttribute(COLUMN_IS_VALID, "is valid", true, true, Boolean.class);
		
		this.addManyToOneRelationship(COLUMN_SCHOOL_TYPE, SchoolType.class);
		this.addManyToOneRelationship(COLUMN_SCHOOL, School.class);

		this.addManyToManyRelationShip(SchoolClassMember.class);
	}

	public void setDefaultValues() {
		this.setColumn(COLUMN_IS_VALID, true);	
	}
	
	public String getName() {
		return getCourseName();
	}

	public void setName(String name) {
		setCourseName(name);
	}

	public String getCourseName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public void setCourseName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}
	
	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	public void setSchoolPk(Object schoolPk) {
		setColumn(COLUMN_SCHOOL, schoolPk);
	}

	public void setSchoolTypePk(Object schoolTypePk) {
		setColumn(COLUMN_SCHOOL_TYPE, schoolTypePk);
	}

	public void remove() {
		setColumn(COLUMN_IS_VALID, false);
		this.store();
	}

	public void addSchoolClassMember(SchoolClassMember schoolClassMember) throws IDOAddRelationshipException {
		this.idoAddTo(schoolClassMember);
	}
	
	public void removeSchoolClassMember(SchoolClassMember schoolClassMember) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(schoolClassMember);
	}
	
	public Collection getSchoolClassMembers() throws IDORelationshipException {
		return this.idoGetRelatedEntities(SchoolClassMember.class);
	}
	
	public void removeAllSchoolClassMembers() throws IDORemoveRelationshipException {
		this.idoRemoveFrom(SchoolClassMember.class);
	}	
	
	public Collection ejbHomeFindSchoolCourses(School school) throws IDOLookupException, IDORelationshipException, FinderException {
		return ejbHomeFindSchoolCourses(school, school.getSchoolTypes());
	}

	public Collection ejbHomeFindSchoolCourses(School school, Object schoolTypePK) throws IDOLookupException, IDORelationshipException, FinderException {
		Vector vector = new Vector();
		vector.add(schoolTypePK);
		return ejbHomeFindSchoolCourses(school, vector);
	}

	public Collection ejbHomeFindSchoolCourses(School school, Collection schoolTypePKs) throws FinderException, IDOLookupException {
		boolean useTypes = schoolTypePKs != null && !schoolTypePKs.isEmpty();
		
		if (useTypes) {
			IDOQuery query = idoQuery();
			query.append("Select * from ").append(getEntityName())
			.append(" where ").append(COLUMN_SCHOOL_TYPE).append(" in ( ");
			Iterator iter = schoolTypePKs.iterator();
			while (iter.hasNext()) {
				query.append(iter.next().toString());
				if (iter.hasNext()) {
					query.append(", ");
				}
			}
			query.append(")")
			.append(" AND ").append(COLUMN_SCHOOL).append(" = ").append(school.getPrimaryKey())
			.append(" AND (").append(COLUMN_IS_VALID).append(" is null")
			.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y')");
			
			return this.idoFindPKsByQuery(query);
		} else {
			/** No schoolTypes. Returning empty collection instead of all schoolCourses */
			return new Vector();
		}
	}


}
