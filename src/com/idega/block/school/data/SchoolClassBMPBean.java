package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;


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

  public Integer ejbFindBySchoolClassNameSchoolSchoolYearSchoolSeason(String className, School school, SchoolYear schoolYear, SchoolSeason schoolSeason)throws FinderException ,RemoteException{
  	IDOQuery sql = new IDOQuery();

  	sql.appendSelectAllFrom(this).appendWhere().append(NAME).appendEqualSign().appendWithinSingleQuotes(className)
  	.appendAnd().append(SCHOOL).appendEqualSign().append(((Integer)school.getPrimaryKey()).intValue())
  	.appendAnd().append(SCHOOLYEAR).appendEqualSign().append(((Integer)schoolYear.getPrimaryKey()).intValue())
  	.appendAnd().append(SEASON).appendEqualSign().append(((Integer)schoolSeason.getPrimaryKey()).intValue());

  	return (Integer)super.idoFindOnePKBySQL(sql.toString());
  }
}