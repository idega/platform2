package com.idega.block.school.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolClassBMPBean extends GenericEntity implements SchoolClass{

  public final static String SCHOOLCLASS = "SCH_SCHOOL_CLASS";
  public final static String SCHOOLCLASSID = "SCH_SCHOOL_CLASS_ID";
  public final static String SCHOOLTYPE = "sch_school_type_id";
  public final static String SEASON = "sch_school_season_id";
  public final static String NAME = "class_name";
  public final static String COLUMN_VALID = "valid";
  public final static String COLUMN_READY = "ready";
  public final static String COLUMN_LOCKED = "locked";
  public final static String SCHOOL = "school_id";
	public final static String COLUMN_LOCKED_DATE = "locked_date";
	public final static String COLUMN_READY_DATE = "ready_date";
	public final static String COLUMN_SUB_GROUP = "sub_group";

	public final static String SCHOOL_CLASS_YEAR = "sch_school_class_year";
	public final static String SCHOOL_CLASS_TEACHER = "sch_school_class_teacher";

	public final static String VALID = "Y";
	public final static String INVALID = "N";

	public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addManyToOneRelationship(SCHOOL,"School",School.class);
		addManyToOneRelationship(SCHOOLTYPE,"Schoolytype",SchoolType.class);
    addManyToOneRelationship(SEASON,"Season",SchoolSeason.class);
    addAttribute(NAME,"Name",true,true,String.class);
    addAttribute(COLUMN_VALID,"Valid",true,true,String.class,1);
    addAttribute(COLUMN_READY,"Ready",true,true,String.class,1);
    addAttribute(COLUMN_LOCKED,"Ready",true,true,String.class,1);
		addAttribute(COLUMN_READY_DATE,"Ready date",true,true,Timestamp.class);
		addAttribute(COLUMN_LOCKED_DATE,"Ready date",true,true,Timestamp.class,1);
		addAttribute(COLUMN_SUB_GROUP,"Is a sub group",true,true,Boolean.class);
    
    addManyToManyRelationShip(SchoolYear.class, SCHOOL_CLASS_YEAR);
		addManyToManyRelationShip(User.class, SCHOOL_CLASS_TEACHER);
		addManyToManyRelationShip(SchoolClassMember.class, "sch_sub_group_placements");
		addManyToManyRelationShip(SchoolStudyPath.class, "sch_group_study_path");
 	}
	
  public String getEntityName() {
    return SCHOOLCLASS;
  }

  public String getName(){
    return getSchoolClassName();
  }

  public int getSchoolId(){
    return getIntColumnValue(SCHOOL);
  }
	public School getSchool(){
		return (School) getColumnValue(SCHOOL);
	}
  public void setSchoolId(int id){
    this.setColumn(SCHOOL,id);
  }
  public int getSchoolTypeId(){
	return getIntColumnValue(SCHOOLTYPE);
  }
  public SchoolType getSchoolType(){
    return (SchoolType) getColumnValue(SCHOOLTYPE);
  }
  public void setSchoolTypeId(int id){
	this.setColumn(SCHOOLTYPE,id);
  }
  public void setSchoolSeasonId(int id){
    this.setColumn(SEASON,id);
  }
  public int getSchoolSeasonId(){
    return this.getIntColumnValue(SEASON);
  }
	public SchoolSeason getSchoolSeason(){
		return (SchoolSeason) this.getColumnValue(SEASON);
	}
  public void setSchoolClassName(String name){
    this.setColumn(NAME,name);
  }
  public String getSchoolClassName(){
    return getStringColumnValue(NAME);
  }

	public boolean getValid() {
		String valid = getStringColumnValue(COLUMN_VALID);
		return valid == null || !valid.equals(INVALID);
	}

	public void setValid(boolean valid) {
		if (valid)
			setColumn(COLUMN_VALID, VALID);
		else
			setColumn(COLUMN_VALID, INVALID);
	}

	public boolean getReady() {
		String valid = getStringColumnValue(COLUMN_READY);
		return valid != null && valid.equals(VALID);
	}

	public void setReady(boolean valid) {
		if (valid)
			setColumn(COLUMN_READY, VALID);
		else
			setColumn(COLUMN_READY, INVALID);
	}

	public boolean getLocked() {
		String valid = getStringColumnValue(COLUMN_LOCKED);
		return valid != null && valid.equals (VALID);
	}

	public void setLocked(boolean valid) {
		if (valid)
			setColumn(COLUMN_LOCKED, VALID);
		else
			setColumn(COLUMN_LOCKED, INVALID);
	}
	
	public void setReadyDate(Timestamp timestamp) {
		setColumn(COLUMN_READY_DATE, timestamp);
	}
	
	public Timestamp getReadyDate() {
		return (Timestamp) getColumnValue(COLUMN_READY_DATE);
	}

	public void setLockedDate(Timestamp timestamp) {
		setColumn(COLUMN_LOCKED_DATE, timestamp);
	}
	
	public Timestamp getLockedDate() {
		return (Timestamp) getColumnValue(COLUMN_LOCKED_DATE);
	}

	public void setIsSubGroup(boolean isSubGroup) {
		setColumn(COLUMN_SUB_GROUP, isSubGroup);
	}
	
	public boolean getIsSubGroup() {
		return getBooleanColumnValue(COLUMN_SUB_GROUP, false);
	}
	
	public boolean hasRelationToSchoolYear(SchoolYear schoolYear) {
		try {
			Collection relations = super.idoGetRelatedEntities(schoolYear);
			if (relations != null && !relations.isEmpty())
				return true;
			return false;
		}
		catch (IDORelationshipException e) {
			return false;
		}
	}

	public boolean hasRelationToTeacher(User teacher) {
		try {
			Collection relations = super.idoGetRelatedEntities(teacher);
			if (relations != null && !relations.isEmpty())
				return true;
			return false;
		}
		catch (IDORelationshipException e) {
			return false;
		}
	}

	
	public Collection ejbFindBySchool(School school)throws FinderException {
  	return ejbFindBySchool(((Integer)school.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchool(int schoolID)throws FinderException {
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null) order by "+NAME);
  }

  public Collection ejbFindBySchoolAndSeason(School school, SchoolSeason schoolSeason)throws FinderException {
  	return ejbFindBySchoolAndSeason(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolSeason.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndSeason(int schoolID, int schoolSeasonID)throws FinderException {
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+SEASON+" = "+String.valueOf(schoolSeasonID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null) order by "+NAME);
  }

  public Collection ejbFindBySchoolAndYear(School school, SchoolYear schoolYear)throws FinderException {
  	return ejbFindBySchoolAndYear(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolYear.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndYear(int schoolID, int schoolYearID)throws FinderException {
  	return ejbFindBySchoolAndInYear(schoolID, schoolYearID);
  }

	public Collection ejbFindBySchoolAndInYear(int schoolID, int schoolYearID)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(this.getEntityName() + " s, ").append("sch_school_year y, ").append(SCHOOL_CLASS_YEAR + " sy");
		query.appendWhereEquals(SCHOOL, schoolID).appendAndEquals("s."+getIDColumnName(), "sy."+getIDColumnName()).appendAndEquals("sy.sch_school_year_id", "y.sch_school_year_id");
		query.appendAndEquals("y.sch_school_year_id", schoolYearID);
		query.appendAnd().appendLeftParenthesis().appendEqualsQuoted(COLUMN_VALID, "Y").appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
	}

  public Collection ejbFindBySchoolAndSeasonAndYear(School school, SchoolSeason schoolSeason,SchoolYear schoolYear)throws FinderException {
  	return ejbFindBySchoolAndSeasonAndYear(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolSeason.getPrimaryKey()).intValue(),((Integer)schoolYear.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID,int schoolYearID, boolean showSubGroups)throws FinderException {
  	return ejbFindBySchoolAndSeasonAndInYear(schoolID, schoolSeasonID, schoolYearID, showSubGroups);
  }
  
	public Collection ejbFindBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID, boolean showSubGroups)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("distinct s.*").appendFrom().append(this.getEntityName() + " s, ").append("sch_school_year y, ").append(SCHOOL_CLASS_YEAR + " sy");
		query.appendWhereEquals(SCHOOL, schoolID).appendAndEquals("s."+getIDColumnName(), "sy."+getIDColumnName()).appendAndEquals("sy.sch_school_year_id", "y.sch_school_year_id");
		query.appendAndEquals("y.sch_school_year_id", schoolYearID).appendAndEquals(SEASON, schoolSeasonID);
		query.appendAnd().appendLeftParenthesis().appendEqualsQuoted(COLUMN_VALID, "Y").appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		if (showSubGroups) {
			query.appendAndEquals(this.COLUMN_SUB_GROUP, true);
		}
		else {
			query.appendAnd().appendLeftParenthesis().appendEquals(COLUMN_SUB_GROUP, false).appendOr().append(COLUMN_SUB_GROUP).appendIsNull().appendRightParenthesis();
		}
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindBySchoolAndSeasonAndYearAndStudyPath(School school, SchoolSeason schoolSeason, SchoolYear schoolYear, SchoolStudyPath studyPath, boolean showSubGroups)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("distinct s.*").appendFrom().append(this.getEntityName() + " s, ").append("sch_school_year y, ").append(SCHOOL_CLASS_YEAR + " sy, ").append("sch_group_study_path gsp");
		query.appendWhereEquals(SCHOOL, school);
		query.appendAndEquals("s."+getIDColumnName(), "sy."+getIDColumnName());
		query.appendAndEquals("s."+getIDColumnName(), "gsp."+getIDColumnName());
		query.appendAndEquals("sy.sch_school_year_id", schoolYear);
		query.appendAndEquals("gsp.sch_study_path_id", studyPath);
		query.appendAndEquals(SEASON, schoolSeason);
		query.appendAnd().appendLeftParenthesis().appendEqualsQuoted(COLUMN_VALID, "Y").appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		if (showSubGroups) {
			query.appendAndEquals(this.COLUMN_SUB_GROUP, true);
		}
		else {
			query.appendAnd().appendLeftParenthesis().appendEquals(COLUMN_SUB_GROUP, false).appendOr().append(COLUMN_SUB_GROUP).appendIsNull().appendRightParenthesis();
		}
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
	}

  public Collection ejbFindBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID,int schoolYearID)throws FinderException {
  	return ejbFindBySchoolAndSeasonAndInYear(schoolID, schoolSeasonID, schoolYearID);
  }
  
	public Collection ejbFindBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID)throws FinderException {
		return ejbFindBySchoolAndSeasonAndInYear(schoolID, schoolSeasonID, schoolYearID, -1);
	}
	
	public Collection ejbFindBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID, int studyPathID)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("distinct s.*").appendFrom().append(this.getEntityName() + " s");
		if (schoolYearID != -1) {
			query.append(", " + SCHOOL_CLASS_YEAR + " sy");
		}
		if (studyPathID != -1) {
			query.append(", sch_group_study_path sp");
		}
		query.appendWhereEquals(SCHOOL, schoolID);
		if (schoolYearID != -1) {
			query.appendAndEquals("s."+getIDColumnName(), "sy."+getIDColumnName());
			query.appendAndEquals("sy.sch_school_year_id", schoolYearID);
		}
		if (schoolSeasonID != -1) {
			query.appendAndEquals(SEASON, schoolSeasonID);
		}
		if (studyPathID != -1) {
			query.appendAndEquals("s."+getIDColumnName(), "sp."+getIDColumnName());
			query.appendAndEquals("sp.sch_study_path_id", studyPathID);
		}
		query.appendAnd().appendLeftParenthesis().appendEqualsQuoted(COLUMN_VALID, "Y").appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs)throws FinderException {
		return ejbFindBySchoolAndSeasonAndYears(schoolID, schoolSeasonID, schoolYearIDs, true);
	}
	
	public Collection ejbFindBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs, boolean showSubGroups)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("distinct s.*").appendFrom().append(this.getEntityName() + " s, ").append("sch_school_year y, ").append(SCHOOL_CLASS_YEAR + " sy");
		query.appendWhereEquals(SCHOOL, schoolID).appendAndEquals("s."+getIDColumnName(), "sy."+getIDColumnName()).appendAndEquals("sy.sch_school_year_id", "y.sch_school_year_id");
		query.appendAnd().append("y.sch_school_year_id").appendInArray(schoolYearIDs);
		if (schoolSeasonID != -1) {
			query.appendAndEquals(SEASON, schoolSeasonID);
		}
		query.appendAnd().appendLeftParenthesis().appendEqualsQuoted(COLUMN_VALID, "Y").appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		if (!showSubGroups) {
			query.appendAnd().appendLeftParenthesis().appendEqualsQuoted(COLUMN_SUB_GROUP, "N").appendOr().append(COLUMN_SUB_GROUP).appendIsNull().appendRightParenthesis();
		}
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindBySeasonAndYear(SchoolSeason schoolSeason,SchoolYear schoolYear)throws FinderException {
	return ejbFindBySeasonAndYear(((Integer)schoolSeason.getPrimaryKey()).intValue(),((Integer)schoolYear.getPrimaryKey()).intValue());
  }
	
  public Collection ejbFindBySchoolAndSchoolTypeAndSeason(int schoolID,int schoolTypeID,int seasonID,boolean showSubGroups)throws FinderException{
  	IDOQuery query = idoQueryGetSelect().appendWhereEquals(SCHOOL,schoolID).appendWhereEquals(SCHOOLTYPE,schoolTypeID);
  	if(seasonID>0)
  		query.appendWhereEquals(SEASON,seasonID);
  	if(!showSubGroups)
  		query.appendWhereEquals(COLUMN_SUB_GROUP,false);
  	return super.idoFindPKsByQuery(query);
  }
  	
  
  public Collection ejbFindBySeasonAndYear(int schoolSeasonID,int schoolYearID)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(this.getEntityName() + " s, ").append("sch_school_year y, ").append(SCHOOL_CLASS_YEAR + " sy");
		query.appendWhereEquals(SEASON, schoolSeasonID).appendAndEquals("s."+getIDColumnName(), "sy."+getIDColumnName()).appendAndEquals("sy.sch_school_year_id", "y.sch_school_year_id");
		query.appendAndEquals("y.sch_school_year_id", schoolYearID);
		query.appendAnd().appendLeftParenthesis().appendEquals(COLUMN_VALID, true).appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
  }
  
	public Collection ejbFindBySchoolAndCategory(int schoolID,String category)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(this.getEntityName() + " s, ").append("sch_school_type t");
		query.appendWhereEquals(SCHOOL, schoolID).appendAndEquals("s."+SCHOOLTYPE, "t.sch_school_type_id");
		query.appendAndEqualsQuoted("t.school_category", category);
		query.appendAnd().appendLeftParenthesis().appendEquals(COLUMN_VALID, true).appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
	}
  
  public Collection ejbFindBySeason(SchoolSeason schoolSeason)throws FinderException {
	return ejbFindBySeason(((Integer)schoolSeason.getPrimaryKey()).intValue());
  }
  
  public Collection ejbFindBySeason(int schoolSeasonID)throws FinderException {
	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SEASON+" = "+String.valueOf(schoolSeasonID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null) order by "+NAME);
  }
  
  public Collection ejbFindByTeacher(User teacher)throws FinderException {
  	return ejbFindByTeacher(((Integer)teacher.getPrimaryKey()).intValue());
  }

  public Collection ejbFindByTeacher(int teacherID)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(this.getEntityName() + " s, ").append(SCHOOL_CLASS_TEACHER + " st");
		query.appendWhereEquals("s."+getIDColumnName(), "st."+getIDColumnName());
		query.appendAndEquals("st.ic_user_id", teacherID);
		query.appendAnd().appendLeftParenthesis().appendEquals(COLUMN_VALID, true).appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
  }

  public Collection ejbFindBySchoolAndTeacher(School school, User teacher)throws FinderException {
  	return ejbFindBySchoolAndTeacher(((Integer)school.getPrimaryKey()).intValue(),((Integer)teacher.getPrimaryKey()).intValue());
  }
  
  public Collection ejbFindBySchoolAndTeacher(int schoolID, int teacherID)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(this.getEntityName() + " s, ").append(SCHOOL_CLASS_TEACHER + " st");
		query.appendWhereEquals("s."+getIDColumnName(), "st."+getIDColumnName());
		query.appendAndEquals("st.ic_user_id", teacherID);
		query.appendAndEquals(SCHOOL, schoolID);
		query.appendAnd().appendLeftParenthesis().appendEquals(COLUMN_VALID, true).appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
  }

  public Collection ejbFindBySchoolAndSeasonAndTeacher(School school, SchoolSeason schoolSeason, User teacher)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(this.getEntityName() + " s, ").append(SCHOOL_CLASS_TEACHER + " st");
		query.appendWhereEquals("s."+getIDColumnName(), "st."+getIDColumnName());
		query.appendAndEquals("st.ic_user_id", teacher);
		query.appendAndEquals(SCHOOL, school);
		query.appendAndEquals(SEASON, schoolSeason);
		query.appendAnd().appendLeftParenthesis().appendEquals(COLUMN_VALID, true).appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
  }

  public Collection ejbFindBySchoolAndSeasonAndTeacher(int schoolID, int schoolSeasonID, int teacherID)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(this.getEntityName() + " s, ").append(SCHOOL_CLASS_TEACHER + " st");
		query.appendWhereEquals("s."+getIDColumnName(), "st."+getIDColumnName());
		query.appendAndEquals("st.ic_user_id", teacherID);
		query.appendAndEquals(SCHOOL, schoolID);
		query.appendAndEquals(SEASON, schoolSeasonID);
		query.appendAnd().appendLeftParenthesis().appendEquals(COLUMN_VALID, true).appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		query.appendOrderBy(NAME);
		return idoFindPKsByQuery(query);
  }

  public Integer ejbFindByNameAndSchool(String className,School school) throws FinderException {
  	return ejbFindByNameAndSchool(className,((Integer)school.getPrimaryKey()).intValue());	
  }
  
  public Integer ejbFindByNameAndSchool(String className,int schoolID) throws FinderException {
  	return (Integer) super.idoFindOnePKBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+NAME+ " = '"+className+"' and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null) order by "+NAME);
  }
  
  public Integer ejbFindBySchoolClassNameSchoolSchoolYearSchoolSeason(String className, School school, SchoolYear schoolYear, SchoolSeason schoolSeason)throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(this.getEntityName() + " s, ").append("sch_school_year y, ").append(SCHOOL_CLASS_YEAR + " sy");
		query.appendWhereEquals(SCHOOL, school).appendAndEquals("s."+getIDColumnName(), "sy."+getIDColumnName()).appendAndEquals("sy.sch_school_year_id", "y.sch_school_year_id");
		query.appendAndEquals("y.sch_school_year_id", schoolYear).appendAndEquals(SEASON, schoolSeason);
		query.appendAndEqualsQuoted(NAME, className);
		query.appendAnd().appendLeftParenthesis().appendEquals(COLUMN_VALID, true).appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		return (Integer)super.idoFindOnePKByQuery(query);
  }

  public int ejbHomeGetNumberOfStudentsInClass(int schoolClassID) throws IDOException {
  	IDOQuery sql = idoQuery();
  	sql.appendSelect().append("count(*)").appendFrom().append(getEntityName()).append(" sc,").append(SchoolClassMemberBMPBean.SCHOOLCLASSMEMBER).append(" scm");
  	sql.appendWhere().append("sc.").append(getIDColumnName()).appendEqualSign().append(schoolClassID).appendAnd().append("sc.").append(getIDColumnName()).appendEqualSign().append("scm.").append(getIDColumnName());

		return super.idoGetNumberOfRecords(sql.toString());
  }

	/**
	 * Returns a collection of all school classes. 
	 * Used to create the SchoolClass - SchoolType relation
	 * 
	 * @return Collection of all schoolClass
	 * @throws FinderException
	 */
	public Collection ejbFindAll() throws FinderException{
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendOrderBy(NAME);
		return idoFindPKsByQuery(sql);
	}

	public Collection findRelatedUsers() throws com.idega.data.IDORelationshipException {
		return super.idoGetRelatedEntities(User.class);
	}

	public Collection findRelatedSchoolYears() throws com.idega.data.IDORelationshipException {
		return super.idoGetRelatedEntities(SchoolYear.class);
	}
	
	public void addSchoolYear(SchoolYear year) throws IDOAddRelationshipException {
		super.idoAddTo(year);
	}
	
	public void removeSchoolYear(SchoolYear year) throws IDORemoveRelationshipException {
		super.idoRemoveFrom(year);
	}

	public void addTeacher(User teacher) throws IDOAddRelationshipException {
		super.idoAddTo(teacher);
	}
	
	public void removeTeacher(User teacher) throws IDORemoveRelationshipException {
		super.idoRemoveFrom(teacher);
	}
	
	public Collection findRelatedStudyPaths() throws com.idega.data.IDORelationshipException {
		return super.idoGetRelatedEntities(SchoolStudyPath.class);
	}
	
	public void addStudyPath(SchoolStudyPath studyPath) throws IDOAddRelationshipException {
		super.idoAddTo(studyPath);
	}
	
	public void removeStudyPath(SchoolStudyPath studyPath) throws IDORemoveRelationshipException {
		super.idoRemoveFrom(studyPath);
	}

	public void removeStudyPaths() throws IDORemoveRelationshipException {
		super.idoRemoveFrom(SchoolStudyPath.class);
	}

	/* (non-Javadoc)
	 * @see javax.ejb.EJBLocalObject#remove()
	 */
	public void remove() {
		setValid(false);
		super.store();
	}
	
	public void removeFromSchoolYear() throws IDORemoveRelationshipException {
		this.idoRemoveFrom(SchoolYear.class);
	}

	public void removeFromUser() throws IDORemoveRelationshipException {
		this.idoRemoveFrom(User.class);
	}
	
	public Integer ejbFindOneBySchool(int schoolID) throws FinderException {
		return (Integer) super.idoFindOnePKBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
	}
	
	public Collection getSubGroupPlacements() throws IDORelationshipException {
		if (getIsSubGroup()) {
			return this.idoGetRelatedEntities(SchoolClassMember.class);
		}
		return null;
	}
}