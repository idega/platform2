package com.idega.block.school.data;

import com.idega.data.GenericEntity;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolViewBMPBean extends GenericEntity implements SchoolView{

  /*
    CREATE VIEW V_SCHOOL_LIST(
    SCHOOL_CATEGORY,
    SCH_SCHOOL_TYPE_ID,
    TYPE_NAME,
    SCH_SCHOOL_AREA_ID,
    AREA_NAME,
    SCH_SCHOOL_ID,
    SCHOOL_NAME)
    AS
    select
    T.school_category,
    T.sch_school_type_id,T.type_name,
    A.sch_school_area_id,A.area_name,
    S.sch_school_id,S.school_name
    from
    sch_school_type T,
    sch_school S,
    sch_school_area A,
    sch_school_sch_school_type M
    where M.sch_school_id = S.sch_school_id
    and M.sch_school_type_id = T.sch_school_type_id
    and A.sch_school_area_id = S.sch_school_area_id;
  */

  private final static String SCHOOLLIST = "V_SCHOOL_LIST";
  private final static String SCHOOLID = "sch_school_id";
  private final static String SCHOOLNAME = "school_name";
  private final static String TYPEID = "sch_school_type_id";
  private final static String TYPENAME = "type_name";
  private final static String AREAID = "sch_school_area_id";
  private final static String AREANAME = "area_name";
  private final static String CATEGORY = "school_category";

  public void initializeAttributes() {
    this.addAttribute(SCHOOLID,"Schoolid",true,true,Integer.class);
    this.addAttribute(SCHOOLNAME,"Schoolname",true,true,String.class);
    this.addAttribute(TYPEID,"Schoolid",true,true,Integer.class);
    this.addAttribute(TYPENAME,"Schoolname",true,true,String.class);
    this.addAttribute(AREAID,"Schoolid",true,true,Integer.class);
    this.addAttribute(AREANAME,"Schoolname",true,true,String.class);
    this.addAttribute(CATEGORY,"Schoolname",true,true,String.class);
  }
  public String getEntityName() {
    return SCHOOLLIST;
  }
  public int getSchoolTypeId(){
    return this.getIntColumnValue(TYPEID);
  }
  public int getSchoolAreaId(){
    return this.getIntColumnValue(AREAID);
  }
  public int getSchoolId(){
    return this.getIntColumnValue(SCHOOLID);
  }
  public String getSchoolName(){
    return this.getStringColumnValue(SCHOOLNAME);
  }
  public String getSchoolAreaName(){
    return this.getStringColumnValue(AREANAME);
  }
  public String getSchoolTypeName(){
    return this.getStringColumnValue(TYPENAME);
  }
  public String getSchoolCategory(){
    return this.getStringColumnValue(CATEGORY);
  }

  public java.util.List findAllBySchoolType(int type_id)throws com.idega.data.IDOFinderException{
    return com.idega.data.EntityFinder.getInstance().findAllByColumn(SchoolView.class,TYPEID,type_id);
  }
}