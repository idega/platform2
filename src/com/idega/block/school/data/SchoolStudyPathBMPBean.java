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
	
	private static String TABLE_NAME           = "SCH_STUDY_PATH";
	private static String COLUMN_CODE          = "STUDY_PATH_CODE";
	private static String COLUMN_DESCRIPTION   = "DESCRIPTION";
	private static String COLUMN_SCHOOL_TYPE   = "SCH_SCHOOL_TYPE_ID";
	private static String COLUMN_IS_VALID      = "IS_VALID";
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		setAsPrimaryKey(getIDColumnName(), true);
		addAttribute(COLUMN_CODE, "course name", true, true, String.class);
		addAttribute(COLUMN_DESCRIPTION, "description, ", true, true, String.class);
		addAttribute(COLUMN_IS_VALID, "is valid", true, true, Boolean.class);
		
		this.addManyToOneRelationship(COLUMN_SCHOOL_TYPE, SchoolType.class);

		this.addManyToManyRelationShip(SchoolClassMember.class);
		this.addManyToManyRelationShip(School.class);
	}

	public void setDefaultValues() {
		this.setColumn(COLUMN_IS_VALID, true);	
	}
	
	public String getCode() {
		return getStringColumnValue(COLUMN_CODE);
	}

	public void setCode(String code) {
		setColumn(COLUMN_CODE, code);
	}
	
	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}
	
	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	public SchoolType getSchoolType() {
		return (SchoolType) getColumnValue(COLUMN_SCHOOL_TYPE);
	}
	
	public int getSchoolTypeId() {
		return getIntColumnValue(COLUMN_SCHOOL_TYPE);
	}
	
	public void setSchoolTypeId(Object schoolTypeId) {
		setColumn(COLUMN_SCHOOL_TYPE, schoolTypeId);
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

	public void addSchool(School school) throws IDOAddRelationshipException {
		this.idoAddTo(school);
	}
	
	public void removeSchool(School school) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(school);
	}
	
	public Collection getSchools() throws IDORelationshipException {
		return this.idoGetRelatedEntities(School.class);
	}
	
	public void removeAllSchools() throws IDORemoveRelationshipException {
		this.idoRemoveFrom(School.class);
	}	

	public Collection ejbFindAllStudyPaths() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
//		sql.appendOrderBy(COLUMN_CODE);
		return idoFindPKsByQuery(sql);
	}

	public Collection ejbHomeFindStudyPaths(School school) throws IDOLookupException, IDORelationshipException, FinderException {
		return ejbHomeFindStudyPaths(school, school.getSchoolTypes());
	}

	public Collection ejbHomeFindStudyPaths(School school, Object schoolTypePK) throws IDOLookupException, IDORelationshipException, FinderException {
		Vector vector = new Vector();
		vector.add(schoolTypePK);
		return ejbHomeFindStudyPaths(school, vector);
	}

	public Collection ejbHomeFindStudyPaths(School school, Collection schoolTypePKs) throws FinderException, IDOLookupException {
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
// To do: now many-to-many relationship to School
//			.append(" AND ").append(COLUMN_SCHOOL).append(" = ").append(school.getPrimaryKey())
			.append(" AND (").append(COLUMN_IS_VALID).append(" is null")
			.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y')");
			
			return this.idoFindPKsByQuery(query);
		} else {
			/** No schoolTypes. Returning empty collection instead of all schoolCourses */
			return new Vector();
		}
	}


}
