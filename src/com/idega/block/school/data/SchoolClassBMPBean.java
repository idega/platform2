package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
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

  public final static String SCHOOLCLASS = "sch_school_class";
  public final static String SCHOOLYEAR = "sch_school_year_id";
  public final static String SCHOOLTYPE = "sch_school_type_id";
  public final static String TEACHER = "ic_user_id";
  public final static String SEASON = "sch_school_season_id";
  public final static String NAME = "class_name";
  public final static String COLUMN_VALID = "valid";
  public final static String COLUMN_READY = "ready";
  public final static String COLUMN_LOCKED = "locked";
  public final static String SCHOOL = "school_id";

	public final static String SCHOOL_CLASS_YEAR = "sch_school_class_year";
	public final static String SCHOOL_CLASS_TEACHER = "sch_school_class_teacher";

	public final static String VALID = "Y";
	public final static String INVALID = "N";

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addManyToOneRelationship(SCHOOL,"School",School.class);
		addManyToOneRelationship(SCHOOLYEAR,"Schoolyear",SchoolYear.class);
		addManyToOneRelationship(SCHOOLTYPE,"Schoolytype",SchoolType.class);
    addAttribute(TEACHER,"Teacher",true,true,Integer.class);
    addManyToOneRelationship(SEASON,"Season",SchoolSeason.class);
    addAttribute(NAME,"Name",true,true,String.class);
    addAttribute(COLUMN_VALID,"Valid",true,true,String.class,1);
    addAttribute(COLUMN_READY,"Ready",true,true,String.class,1);
    addAttribute(COLUMN_LOCKED,"Ready",true,true,String.class,1);
    
    addManyToManyRelationShip(SchoolYear.class, SCHOOL_CLASS_YEAR);
		addManyToManyRelationShip(User.class, SCHOOL_CLASS_TEACHER);
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
  public SchoolYear getSchoolYear(){
    return (SchoolYear) getColumnValue(SCHOOLYEAR);
  }
  public void setSchoolTypeId(int id){
	this.setColumn(SCHOOLTYPE,id);
  }
  public void setSchoolYearId(int year){
    this.setColumn(SCHOOLYEAR,year);
  }
	public void setSchoolYearId(Integer year){
		this.setColumn(SCHOOLYEAR,year);
	}
  public int getSchoolYearId(){
    return this.getIntColumnValue(SCHOOLYEAR);
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
  public void setTeacherId(int id){
    this.setColumn(TEACHER,id);
  }
	public void setTeacherId(Integer id){
		this.setColumn(TEACHER,id);
	}
  public int getTeacherId(){
    return this.getIntColumnValue(TEACHER);
  }
  public User getTeacher() {
  	return (User) this.getColumnValue(TEACHER);
  }
  public void setSchoolClassName(String name){
    this.setColumn(NAME,name);
  }
  public String getSchoolClassName(){
    return getStringColumnValue(NAME);
  }

	public boolean getValid() {
		String valid = getStringColumnValue(COLUMN_VALID);
		if ((valid == null) || (valid.equals(VALID)))
			return (true);
		else if (valid.equals(INVALID))
			return (false);
		else
			return (true);
	}

	public void setValid(boolean valid) {
		if (valid)
			setColumn(COLUMN_VALID, VALID);
		else
			setColumn(COLUMN_VALID, INVALID);
	}

	public boolean getReady() {
		String valid = getStringColumnValue(COLUMN_READY);
		if ((valid == null) || (valid.equals(INVALID)))
			return (false);
		else if (valid.equals(VALID))
			return (true);
		else
			return (false);
	}

	public void setReady(boolean valid) {
		if (valid)
			setColumn(COLUMN_READY, VALID);
		else
			setColumn(COLUMN_READY, INVALID);
	}

	public boolean getLocked() {
		String valid = getStringColumnValue(COLUMN_LOCKED);
		if ((valid == null) || (valid.equals(INVALID)))
			return (false);
		else if (valid.equals(VALID))
			return (true);
		else
			return (false);
	}

	public void setLocked(boolean valid) {
		if (valid)
			setColumn(COLUMN_LOCKED, VALID);
		else
			setColumn(COLUMN_LOCKED, INVALID);
	}

  public Collection ejbFindBySchool(School school)throws FinderException ,RemoteException{
  	return ejbFindBySchool(((Integer)school.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchool(int schoolID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
  }

  public Collection ejbFindBySchoolAndSeason(School school, SchoolSeason schoolSeason)throws FinderException ,RemoteException{
  	return ejbFindBySchoolAndSeason(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolSeason.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndSeason(int schoolID, int schoolSeasonID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+SEASON+" = "+String.valueOf(schoolSeasonID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
  }

  public Collection ejbFindBySchoolAndYear(School school, SchoolYear schoolYear)throws FinderException ,RemoteException{
  	return ejbFindBySchoolAndYear(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolYear.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndYear(int schoolID, int schoolYearID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+SCHOOLYEAR+" = "+String.valueOf(schoolYearID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
  }

	public Collection ejbFindBySchoolAndInYear(int schoolID, int schoolYearID)throws FinderException ,RemoteException{
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(this.getEntityName() + " s, ").append("sch_school_year y, ").append(SCHOOL_CLASS_YEAR + " sy");
		query.appendWhereEquals(SCHOOL, schoolID).appendAndEquals("s."+getIDColumnName(), "sy."+getIDColumnName()).appendAndEquals("sy.sch_school_year_id", "y.sch_school_year_id");
		query.appendAndEquals("y.sch_school_year_id", schoolYearID);
		query.appendAnd().appendLeftParenthesis().appendEqualsQuoted(COLUMN_VALID, "Y").appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		return idoFindPKsByQuery(query);
	}

  public Collection ejbFindBySchoolAndSeasonAndYear(School school, SchoolSeason schoolSeason,SchoolYear schoolYear)throws FinderException ,RemoteException{
  	return ejbFindBySchoolAndSeasonAndYear(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolSeason.getPrimaryKey()).intValue(),((Integer)schoolYear.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID,int schoolYearID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+SEASON+" = "+String.valueOf(schoolSeasonID)+" and "+SCHOOLYEAR+" = "+schoolYearID+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
  }
  
	public Collection ejbFindBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID)throws FinderException ,RemoteException{
		IDOQuery query = idoQuery();
		query.appendSelect().append("s.*").appendFrom().append(this.getEntityName() + " s, ").append("sch_school_year y, ").append(SCHOOL_CLASS_YEAR + " sy");
		query.appendWhereEquals(SCHOOL, schoolID).appendAndEquals("s."+getIDColumnName(), "sy."+getIDColumnName()).appendAndEquals("sy.sch_school_year_id", "y.sch_school_year_id");
		query.appendAndEquals("y.sch_school_year_id", schoolYearID).appendAndEquals(SEASON, schoolSeasonID);
		query.appendAnd().appendLeftParenthesis().appendEqualsQuoted(COLUMN_VALID, "Y").appendOr().append(COLUMN_VALID).appendIsNull().appendRightParenthesis();
		return idoFindPKsByQuery(query);
	}

  public Collection ejbFindBySeasonAndYear(SchoolSeason schoolSeason,SchoolYear schoolYear)throws FinderException ,RemoteException{
	return ejbFindBySeasonAndYear(((Integer)schoolSeason.getPrimaryKey()).intValue(),((Integer)schoolYear.getPrimaryKey()).intValue());
  }
  
  public Collection ejbFindBySeasonAndYear(int schoolSeasonID,int schoolYearID)throws FinderException ,RemoteException{
	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SEASON+" = "+String.valueOf(schoolSeasonID)+" and "+SCHOOLYEAR+" = "+schoolYearID+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
  }
  
  public Collection ejbFindBySeason(SchoolSeason schoolSeason)throws FinderException ,RemoteException{
	return ejbFindBySeason(((Integer)schoolSeason.getPrimaryKey()).intValue());
  }
  
  public Collection ejbFindBySeason(int schoolSeasonID)throws FinderException ,RemoteException{
	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SEASON+" = "+String.valueOf(schoolSeasonID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
  }
  
  public Collection ejbFindByTeacher(User teacher)throws FinderException ,RemoteException{
  	return ejbFindByTeacher(((Integer)teacher.getPrimaryKey()).intValue());
  }

  public Collection ejbFindByTeacher(int teacherID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+TEACHER+" = "+String.valueOf(teacherID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
  }

  public Collection ejbFindBySchoolAndTeacher(School school, User teacher)throws FinderException ,RemoteException{
  	return ejbFindBySchoolAndTeacher(((Integer)school.getPrimaryKey()).intValue(),((Integer)teacher.getPrimaryKey()).intValue());
  }
  
  public Collection ejbFindBySchoolAndTeacher(int schoolID, int teacherID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+TEACHER+" = "+String.valueOf(teacherID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
  }

  public Collection ejbFindBySchoolAndSeasonAndTeacher(School school, SchoolSeason schoolSeason, User teacher)throws FinderException ,RemoteException{
		return ejbFindBySchoolAndSeasonAndTeacher(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolSeason.getPrimaryKey()).intValue(),((Integer)teacher.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndSeasonAndTeacher(int schoolID, int schoolSeasonID, int teacherID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+SEASON+" = "+String.valueOf(schoolSeasonID)+" and "+TEACHER+" = "+String.valueOf(teacherID)+" and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
  }

  public Integer ejbFindByNameAndSchool(String className,School school) throws FinderException,RemoteException {
  	return ejbFindByNameAndSchool(className,((Integer)school.getPrimaryKey()).intValue());	
  }
  
  public Integer ejbFindByNameAndSchool(String className,int schoolID) throws FinderException,RemoteException {
  	return (Integer) super.idoFindOnePKBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+NAME+ " = '"+className+"' and ("+COLUMN_VALID+" = '"+VALID+"' or "+COLUMN_VALID+" is null)");
  }
  
  public Integer ejbFindBySchoolClassNameSchoolSchoolYearSchoolSeason(String className, School school, SchoolYear schoolYear, SchoolSeason schoolSeason)throws FinderException ,RemoteException{
  	IDOQuery sql = idoQuery();

  	sql.appendSelectAllFrom(this).appendWhere().append(NAME).appendEqualSign().appendWithinSingleQuotes(className)
  	.appendAnd().append(SCHOOL).appendEqualSign().append(((Integer)school.getPrimaryKey()).intValue())
  	.appendAnd().append(SCHOOLYEAR).appendEqualSign().append(((Integer)schoolYear.getPrimaryKey()).intValue())
  	.appendAnd().append(SEASON).appendEqualSign().append(((Integer)schoolSeason.getPrimaryKey()).intValue())
  	.appendAnd().appendLeftParenthesis().append(COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes(VALID).appendOr().append(COLUMN_VALID).append(" is null").appendRightParenthesis();

  	return (Integer)super.idoFindOnePKBySQL(sql.toString());
  }

  public int ejbHomeGetNumberOfStudentsInClass(int schoolClassID) throws FinderException, IDOException, RemoteException {
  	IDOQuery sql = idoQuery();
  	sql.appendSelect().append("count(*)").appendFrom().append(getEntityName()).append(" sc,").append(SchoolClassMemberBMPBean.SCHOOLCLASSMEMBER).append(" scm");
  	sql.appendWhere().append("sc.").append(getIDColumnName()).appendEqualSign().append(schoolClassID).appendAnd().append("sc.").append(getIDColumnName()).appendEqualSign().append("scm.").append(getIDColumnName());

		return super.idoGetNumberOfRecords(sql.toString());
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
	
	/* (non-Javadoc)
	 * @see javax.ejb.EJBLocalObject#remove()
	 */
	public void remove() throws RemoveException {
		setValid(false);
		super.store();
	}
	
	public void removeFromSchoolYear() throws IDORemoveRelationshipException {
		this.idoRemoveFrom(SchoolYear.class);
	}

	public void removeFromUser() throws IDORemoveRelationshipException {
		this.idoRemoveFrom(User.class);
	}
}