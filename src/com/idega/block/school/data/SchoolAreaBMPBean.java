package com.idega.block.school.data;

import com.idega.data.*;
import com.idega.user.data.Group;
import java.util.Collection;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolAreaBMPBean extends GenericEntity implements SchoolArea {

  public final static String SCHOOL = "sch_school_area";
  public final static String NAME = "area_name";
  public final static String INFO = "area_info";
  public final static String CITY = "area_city";


  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(NAME,"Schoolname",true,true,String.class);
    this.addAttribute(INFO,"Info",true,true,String.class);
    this.addAttribute(CITY,"City",true,true,String.class);
  }
  public String getEntityName() {
    return SCHOOL;
  }
  public String getName(){
    return getSchoolAreaName();
  }
  public String getSchoolAreaName(){
    return this.getStringColumnValue(NAME);
  }
  public void setSchoolAreaName(String name){
    this.setColumn(NAME,name);
  }
  public String getSchoolAreaInfo(){
    return this.getStringColumnValue(INFO);
  }
  public void setSchoolAreaInfo(String info){
    this.setColumn(INFO,info);
  }
  public String getSchoolAreaCity(){
    return this.getStringColumnValue(CITY);
  }
  public void setSchoolAreaCity(String city){
    this.setColumn(CITY,city);
  }


  public Collection ejbFindAllSchoolAreas() throws javax.ejb.FinderException{
    return super.idoFindAllIDsBySQL();
  }

  public Collection ejbFindAllBySchoolType(int type_id)throws javax.ejb.FinderException{
    StringBuffer sql = new StringBuffer("select distinct a.* ");
    sql.append(" from sch_school_area a,sch_school s,sch_school_type t,sch_school_sch_school_type m ");
    sql.append(" where a.sch_school_area_id = s.sch_school_area_id ");
    sql.append(" and t.sch_school_type_id = m.sch_school_type_id ");
    sql.append(" and s.sch_school_id = m.sch_school_id ");
    sql.append(" and t.sch_school_type_id = ");
    sql.append(type_id);

    return super.idoFindPKsBySQL(sql.toString());
  }
}