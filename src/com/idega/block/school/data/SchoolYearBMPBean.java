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
    addAttribute(getIDColumnName());
    addAttribute(NAME,"Name",true,true,String.class,3);
    addAttribute(INFO,"Info",true,true,String.class);
    addAttribute(AGE,"Age",true,true,Integer.class);
    setNullable(NAME,false);
    setUnique(NAME,true);
  }

  public String getEntityName() {
    return SCHOOLYEAR;
  }
  public String getName(){
    return getSchoolYearName();
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

  public Collection ejbFindAllSchoolYears()throws javax.ejb.FinderException{
    return super.idoFindAllIDsBySQL();
  }

  public Collection ejbFindAllByAge(int age) throws javax.ejb.FinderException{
    return super.idoFindPKsBySQL("select * from "+getEntityName()+" where "+AGE+" = "+age);
  }

}