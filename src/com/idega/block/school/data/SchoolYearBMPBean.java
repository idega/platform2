package com.idega.block.school.data;

import com.idega.data.*;
import java.util.Collection;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolYearBMPBean extends GenericEntity implements SchoolYear{

  public final static String SCHOOLYEAR = "sch_school_year";
  public final static String NAME = "year_name";
  public final static String INFO = "year_info";
  public final static String AGE = "year_age";

  public void initializeAttributes() {
    addAttribute(this.getIDColumnName());
    addAttribute(NAME,"Name",true,true,String.class);
    addAttribute(INFO,"Info",true,true,String.class);
    addAttribute(AGE,"Age",true,true,Integer.class);
    addManyToManyRelationShip(School.class);
  }
  public String getEntityName() {
    return SCHOOLYEAR;
  }
  public void setSchoolYearName(String name){
    this.setColumn(NAME,name);
  }
  public String getSchoolYearName(){
    return getStringColumnValue(NAME);
  }
  public void setSchoolYearInfo(String info){
    this.setColumn(INFO,info);
  }
  public String getSchoolYearInfo(){
    return getStringColumnValue(INFO);
  }
  public void setSchoolYearAge(int age){
    this.setColumn(AGE,age);
  }
  public int getSchoolYearAge(){
    return getIntColumnValue(AGE);
  }

}