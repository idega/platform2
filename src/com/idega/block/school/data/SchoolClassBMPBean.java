package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
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
  public final static String TEACHER = "ic_user_id";
  public final static String SEASON = "sch_school_season_id";
  public final static String NAME = "class_name";
  public final static String SCHOOL = "school_id";

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addManyToOneRelationship(SCHOOL,"School",School.class);
    addManyToOneRelationship(SCHOOLYEAR,"Schoolyear",SchoolYear.class);
    addAttribute(TEACHER,"Teacher",true,true,Integer.class);
    addManyToOneRelationship(SEASON,"Season",SchoolSeason.class);
    addAttribute(NAME,"Name",true,true,String.class);
  }
  public String getEntityName() {
    return SCHOOLCLASS;
  }

  public String getName(){
    return getSchoolClassName(NAME);
  }

  public int getSchoolId(){
    return getIntColumnValue(SCHOOL);
  }
  public void setSchoolId(int id){
    this.setColumn(SCHOOL,id);
  }
  public void setSchoolYearId(int year){
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
  public void setTeacherId(int id){
    this.setColumn(TEACHER,id);
  }
  public int getTeacherId(){
    return this.getIntColumnValue(TEACHER);
  }
  public void setSchoolClassName(String name){
    this.setColumn(NAME,name);
  }
  public String getSchoolClassName(String name){
    return getStringColumnValue(NAME);
  }

  public Collection ejbFindBySchool(School school)throws FinderException ,RemoteException{
  	return ejbFindBySchool(((Integer)school.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchool(int schoolID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID));
  }

  public Collection ejbFindBySchoolAndSeason(School school, SchoolSeason schoolSeason)throws FinderException ,RemoteException{
  	return ejbFindBySchoolAndSeason(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolSeason.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndSeason(int schoolID, int schoolSeasonID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+SEASON+" = "+String.valueOf(schoolSeasonID));
  }

  public Collection ejbFindBySchoolAndYear(School school, SchoolYear schoolYear)throws FinderException ,RemoteException{
  	return ejbFindBySchoolAndYear(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolYear.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndYear(int schoolID, int schoolYearID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+SCHOOLYEAR+" = "+String.valueOf(schoolYearID));
  }

  public Collection ejbFindBySchoolAndSeasonAndYear(School school, SchoolSeason schoolSeason,SchoolYear schoolYear)throws FinderException ,RemoteException{
  	return ejbFindBySchoolAndSeasonAndYear(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolSeason.getPrimaryKey()).intValue(),((Integer)schoolYear.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID,int schoolYearID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+SEASON+" = "+String.valueOf(schoolSeasonID)+" and "+SCHOOLYEAR+" = "+schoolYearID);
  }

  public Collection ejbFindByTeacher(User teacher)throws FinderException ,RemoteException{
  	return ejbFindByTeacher(((Integer)teacher.getPrimaryKey()).intValue());
  }

  public Collection ejbFindByTeacher(int teacherID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+TEACHER+" = "+String.valueOf(teacherID));
  }

  public Collection ejbFindBySchoolAndTeacher(School school, User teacher)throws FinderException ,RemoteException{
  	return ejbFindBySchoolAndTeacher(((Integer)school.getPrimaryKey()).intValue(),((Integer)teacher.getPrimaryKey()).intValue());
  }
  
  public Collection ejbFindBySchoolAndTeacher(int schoolID, int teacherID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+TEACHER+" = "+String.valueOf(teacherID));
  }

  public Collection ejbFindBySchoolAndSeasonAndTeacher(School school, SchoolSeason schoolSeason, User teacher)throws FinderException ,RemoteException{
		return ejbFindBySchoolAndSeasonAndTeacher(((Integer)school.getPrimaryKey()).intValue(),((Integer)schoolSeason.getPrimaryKey()).intValue(),((Integer)teacher.getPrimaryKey()).intValue());
  }

  public Collection ejbFindBySchoolAndSeasonAndTeacher(int schoolID, int schoolSeasonID, int teacherID)throws FinderException ,RemoteException{
  	return super.idoFindPKsBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+SEASON+" = "+String.valueOf(schoolSeasonID)+" and "+TEACHER+" = "+String.valueOf(teacherID));
  }

  public Integer ejbFindByNameAndSchool(String className,School school) throws FinderException,RemoteException {
  	return ejbFindByNameAndSchool(className,((Integer)school.getPrimaryKey()).intValue());	
  }
  
  public Integer ejbFindByNameAndSchool(String className,int schoolID) throws FinderException,RemoteException {
  	return (Integer) super.idoFindOnePKBySQL("select * from "+this.getEntityName()+" where "+SCHOOL+" = "+String.valueOf(schoolID)+" and "+NAME+ " = '"+className+"'");	
  }
  
  public Integer ejbFindBySchoolClassNameSchoolSchoolYearSchoolSeason(String className, School school, SchoolYear schoolYear, SchoolSeason schoolSeason)throws FinderException ,RemoteException{
  	IDOQuery sql = new IDOQuery();

  	sql.appendSelectAllFrom(this).appendWhere().append(NAME).appendEqualSign().appendWithinSingleQuotes(className)
  	.appendAnd().append(SCHOOL).appendEqualSign().append(((Integer)school.getPrimaryKey()).intValue())
  	.appendAnd().append(SCHOOLYEAR).appendEqualSign().append(((Integer)schoolYear.getPrimaryKey()).intValue())
  	.appendAnd().append(SEASON).appendEqualSign().append(((Integer)schoolSeason.getPrimaryKey()).intValue());

  	return (Integer)super.idoFindOnePKBySQL(sql.toString());
  }

  public int ejbHomeGetNumberOfStudentsInClass(int schoolClassID) throws FinderException, IDOException, RemoteException {
  	IDOQuery sql = new IDOQuery();
  	sql.appendSelect().append("count(*)").appendFrom().append(getEntityName()).append(" sc,").append(SchoolClassMemberBMPBean.SCHOOLCLASSMEMBER).append(" scm");
  	sql.appendWhere().append("sc.").append(getIDColumnName()).appendEqualSign().append(schoolClassID).appendAnd().append("sc.").append(getIDColumnName()).appendEqualSign().append("scm.").append(getIDColumnName());

		return super.idoGetNumberOfRecords(sql.toString());
  }
}