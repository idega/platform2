package com.idega.block.school.data;

import com.idega.data.*;


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
  public final static String SCHOOLYEAR = "sch_school_year";
  public final static String TEACHER = "ic_user_id";
  public final static String SEASON = "sch_school_season_id";
  public final static String NAME = "class_name";

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(SCHOOLYEAR,"Schoolyear",true,true,String.class,MANY_TO_ONE,SchoolYear.class);
    addAttribute(TEACHER,"Teacher",true,true,Integer.class);
    addAttribute(SEASON,"Season",true,true,Integer.class,MANY_TO_ONE,SchoolSeason.class);
    addAttribute(NAME,"Name",true,true,String.class);
  }
  public String getEntityName() {
    return SCHOOLCLASS;
  }
  public void setSchoolYear(String year){
    this.setColumn(SCHOOLYEAR,year);
  }
  public String getSchoolYear(){
    return this.getStringColumnValue(SCHOOLYEAR);
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
}