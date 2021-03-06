package com.idega.block.school.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
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
	private static String COLUMN_SCHOOL_CATEGORY   = "SCH_SCHOOL_CATEGORY_ID";
	private static String COLUMN_IS_VALID      = "IS_VALID";
	private static String COLUMN_LOCALIZED_KEY = "LOCALIZED_KEY";
	private static String COLUMN_POINTS = "POINTS";
	private static String COLUMN_STUDY_PATH_GROUP_ID = "STUDY_PATH_GROUP_ID";
	
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		setAsPrimaryKey(getIDColumnName(), true);
		addAttribute(COLUMN_CODE, "course name", true, true, String.class);
		addAttribute(COLUMN_DESCRIPTION, "description, ", true, true, String.class);
		addAttribute(COLUMN_IS_VALID, "is valid", true, true, Boolean.class);
		addAttribute(COLUMN_LOCALIZED_KEY, "localized key", String.class);
		addAttribute(COLUMN_POINTS, "points", Integer.class);
		
		this.addManyToOneRelationship(COLUMN_SCHOOL_TYPE, SchoolType.class);
		this.addManyToOneRelationship(COLUMN_SCHOOL_CATEGORY, SchoolCategory.class);
		this.addManyToManyRelationShip(School.class, "sch_school_study_path");
		this.addManyToOneRelationship(COLUMN_STUDY_PATH_GROUP_ID, SchoolStudyPathGroup.class);
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

	public String getLocalizedKey() {
		String key = getStringColumnValue(COLUMN_LOCALIZED_KEY);
		if (key == null) {
			key = "sch_study_path." + getCode();
		}
		return key;
	}
	
	public void setLocalizedKey(String localizedKey) {
		setColumn(COLUMN_LOCALIZED_KEY, localizedKey);
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

	public SchoolType getSchoolCategory() {
		return (SchoolType) getColumnValue(COLUMN_SCHOOL_CATEGORY);
	}
	
	public Object getSchoolCategoryPK() {
		return getColumnValue(COLUMN_SCHOOL_CATEGORY);
	}
	
	public void setSchoolCategory(Object schoolCategory) {
		setColumn(COLUMN_SCHOOL_CATEGORY, schoolCategory);
	}

    public boolean isValid () {
        return getBooleanColumnValue (COLUMN_IS_VALID);
    }
    
    public void setIsValid(boolean isValid) {
		setColumn(COLUMN_IS_VALID, isValid);
	}

	public int getPoints() {
		return getIntColumnValue(COLUMN_POINTS);
	}

	public void setPoints(int points) {
		setColumn(COLUMN_POINTS, points);
	}

	public SchoolStudyPathGroup getStudyPathGroup() {
		return (SchoolStudyPathGroup) getColumnValue(COLUMN_STUDY_PATH_GROUP_ID);
	}

	public int getStudyPathGroupID() {
		return getIntColumnValue(COLUMN_STUDY_PATH_GROUP_ID);
	}

	public void setStudyPathGroupID(int study_path_group) {
		setColumn(COLUMN_STUDY_PATH_GROUP_ID, study_path_group);
	}
  
	public void remove() {
		setIsValid(false);
		this.store();
	}
	
	public void addSchool(School school) throws IDOAddRelationshipException {
		this.idoAddTo(school);
	}
	
	public void addSchoolYear(SchoolYear year) throws IDOAddRelationshipException {
		super.idoAddTo(year);
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
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.append(" WHERE ").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y'");
		query.appendOrderBy(COLUMN_DESCRIPTION);
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindAllStudyPathsByCodeLength(int codeLength) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.append(" WHERE ").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y'");
		query.appendAnd().append("length(").append(COLUMN_CODE).append(") = ").append(codeLength);
		query.appendOrderBy(COLUMN_DESCRIPTION);
		return idoFindPKsByQuery(query);
	}

	public Integer ejbFindByCode(String code) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEqualsQuoted(COLUMN_CODE, code);
		query.append(" AND (").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y')");
		return (Integer) idoFindOnePKByQuery(query);
	}

	public Integer ejbFindByCodeAndSchoolType(String code, int schoolTypeId) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEqualsQuoted(COLUMN_CODE, code);
		query.appendAndEquals(COLUMN_SCHOOL_TYPE, schoolTypeId);
		query.append(" AND (").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y')");
		return (Integer) idoFindOnePKByQuery(query);
	}

	public Collection ejbFindStudyPaths(School school) throws IDORelationshipException, FinderException {
		return ejbFindStudyPaths(school, null, school.getSchoolTypes());
	}

	public Collection ejbFindStudyPaths(School school, SchoolStudyPathGroup group) throws IDORelationshipException, FinderException {
		return ejbFindStudyPaths(school, group, school.getSchoolTypes());
	}

	public Collection ejbFindStudyPaths(School school, Object schoolTypePK) throws FinderException {
		return ejbFindStudyPaths(school, null, schoolTypePK);
	}
	
	public Collection ejbFindStudyPaths(School school, SchoolStudyPathGroup group, Object schoolTypePK) throws FinderException {
		Vector vector = new Vector();
		vector.add(schoolTypePK);
		return ejbFindStudyPaths(school, group, vector);
	}

	public Collection ejbFindStudyPaths(School school, Collection schoolTypePKs) throws FinderException {
		return ejbFindStudyPaths(school, null, schoolTypePKs);
	}
	
	public Collection ejbFindStudyPaths(School school, SchoolStudyPathGroup group, Collection schoolTypePKs) throws FinderException {
		boolean useTypes = schoolTypePKs != null && !schoolTypePKs.isEmpty();
		
		if (useTypes) {
			IDOQuery query = idoQuery();
			query.append("Select s.* from ").append(getEntityName())
			.append(" s,sch_school_sch_study_path r")
			.append(" where s.").append(COLUMN_SCHOOL_TYPE).append(" in ( ");
			Iterator iter = schoolTypePKs.iterator();
			while (iter.hasNext()) {
				Object next = iter.next();
				if (next instanceof IDOEntity) {
					query.append(((IDOEntity) next).getPrimaryKey().toString());
				}
				else {
					query.append(next.toString());
				}
				if (iter.hasNext()) {
					query.append(", ");
				}
			}
			query.append(")")
			.append(" AND r.sch_school_id = ").append(school.getPrimaryKey())
			.append(" AND r." + getIDColumnName() + " = s." + getIDColumnName())
			.append(" AND (s.").append(COLUMN_IS_VALID).append(" is null")
			.append(" OR s.").append(COLUMN_IS_VALID).append(" = 'Y')");
			
			System.out.println(query.toString());
			return this.idoFindPKsByQuery(query);
		} else {
			/** No schoolTypes. Returning empty collection instead of all schoolCourses */
			return new Vector();
		}
	}
    /*

	public Collection ejbFindAllStudyPathsByMemberId(int id) throws FinderException {
		String select = "select s.* from " + TABLE_NAME + 
				" s,sch_study_path_sch_class_membe m" +
                " where m.sch_class_member_id = " + id + 
				" and m." + getIDColumnName() + " = s." + getIDColumnName() +
                " and (s." + COLUMN_IS_VALID + " is null " +
                " or s." + COLUMN_IS_VALID + " = 'Y')" +
                " order by s." + COLUMN_CODE;
		return super.idoFindPKsBySQL(select);
	}
    */
	public Collection ejbFindBySchoolType(int schoolTypeId) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_SCHOOL_TYPE, schoolTypeId);
		query.append(" AND (").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y')");
		query.appendOrderBy(COLUMN_CODE);
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindBySchoolTypes(String[] schoolTypeIDs) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhere().append(COLUMN_SCHOOL_TYPE).appendInArray(schoolTypeIDs);
		query.append(" AND (").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y')");
		query.appendOrderBy(COLUMN_CODE);
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindBySchoolTypes(Collection schoolTypes) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhere().append(COLUMN_SCHOOL_TYPE).appendInCollection(schoolTypes);
		query.append(" AND (").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y')");
		query.appendOrderBy(COLUMN_CODE);
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindBySchoolType(SchoolType schoolType, SchoolStudyPathGroup group) throws FinderException {
		return ejbFindBySchoolType(schoolType, group, COLUMN_CODE);
	}
	
	public Collection ejbFindBySchoolTypeOrderByDescription(SchoolType schoolType, SchoolStudyPathGroup group) throws FinderException {
		return ejbFindBySchoolType(schoolType, group, COLUMN_DESCRIPTION);
	}	
	
	public Collection ejbFindBySchoolType(SchoolType schoolType, SchoolStudyPathGroup group, String orderByColumn) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_SCHOOL_TYPE,schoolType.getPrimaryKey().toString());
		query.append(" AND (").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y')");
		query.appendAndEquals(COLUMN_STUDY_PATH_GROUP_ID, group.getPrimaryKey().toString());
		query.appendOrderBy(orderByColumn);
		return idoFindPKsByQuery(query);
	}	

	public Collection ejbFindBySchoolStudyPathGroup(SchoolStudyPathGroup group) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.append(" where ");
		query.append(" (").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y')");
		query.appendAndEquals(COLUMN_STUDY_PATH_GROUP_ID, group.getPrimaryKey().toString());
		query.appendOrderBy(COLUMN_CODE);
		return idoFindPKsByQuery(query);
	}		

	public Collection ejbFindBySchoolCategory(SchoolCategory schoolCategory) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_SCHOOL_CATEGORY, schoolCategory);
		query.append(" AND (").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR ").append(COLUMN_IS_VALID).append(" = 'Y')");
		query.appendOrderBy(COLUMN_CODE);
		return idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindBySchoolAndSchoolCategory(School school, SchoolCategory schoolCategory) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom().append(getEntityName()).append(" s, sch_school_study_path r");
		query.appendWhereEquals("r.sch_school_id", school.getPrimaryKey());
		if (schoolCategory != null) {
			query.appendAndEquals("s."+COLUMN_SCHOOL_CATEGORY, schoolCategory);
		}
		query.appendAndEquals("r." + getIDColumnName(), "s." + getIDColumnName());
		query.append(" AND (s.").append(COLUMN_IS_VALID).append(" is null");
		query.append(" OR s.").append(COLUMN_IS_VALID).append(" = 'Y')");
		
		return this.idoFindPKsByQuery(query);
	}

	public Collection ejbFindBySchool(School school) throws FinderException {
		return ejbFindBySchoolAndSchoolCategory(school, null);
	}

	public Collection ejbFindAllByIDs(String[] studyPathIDs) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhere().append(getIDColumnName()).appendInArray(studyPathIDs);
		return idoFindPKsByQuery(query);
	}
}