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

public class SchoolYearPlacesBMPBean extends GenericEntity implements SchoolYearPlaces{

  public final static String SCHOOLYEARPLACES = "sch_year_places";
  public final static String SCHOOL = "school_id";
  public final static String SCHOOLYEAR = "school_year";
  public final static String PLACES = "places";

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(SCHOOL,"School",true,true,Integer.class,MANY_TO_ONE,School.class);
    addAttribute(SCHOOLYEAR,"Schoolyear",true,true,Integer.class,MANY_TO_ONE,SchoolYear.class);
    addAttribute(PLACES,"Places",true,true,Integer.class);
  }
  public String getEntityName() {
    return SCHOOLYEARPLACES;
  }
  public int getSchoolId(){
    return getIntColumnValue(SCHOOL);
  }
  public void setSchoolId(int id){
    this.setColumn(SCHOOL,id);
  }
  public int getSchoolYearId(){
    return this.getIntColumnValue(SCHOOLYEAR);
  }
  public void setSchoolYearId(int id){
    this.setColumn(SCHOOLYEAR,id);
  }
  public int getPlaces(){
    return this.getIntColumnValue(PLACES);
  }
  public void setPlaces(int places){
    this.setColumn(PLACES,places);
  }

  public java.util.Collection ejbFindAllBySchool(int schoolId) throws javax.ejb.FinderException{
    return super.idoFindPKsBySQL("select * from "+getEntityName()+" where "+SCHOOL+" = "+schoolId);
  }
}