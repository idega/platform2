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

public class SchoolBMPBean extends GenericEntity implements School {

  public final static String SCHOOL = "sch_school";
  public final static String NAME = "school_name";
  public final static String ADDRESS = "school_address";
  public final static String INFO = "school_info";
  public final static String HEADMASTER = "headmaster_group_id";
  public final static String SCHOOLTYPE = "sch_school_type_id";
  public final static String SCHOOLAREA = "sch_school_area_id";
  public final static String ZIPCODE = "zip_code";
  public final static String ZIPAREA = "zip_area";
  public final static String PHONE = "phone";
  public final static String KEYCODE = "key_kode";
  public final static String LONGITUDE = "longitude";
  public final static String LATITUDE = "latitude";


  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    //this.addAttribute(SCHOOLTYPE,"Schooltype",true,true,Integer.class,this.MANY_TO_ONE,SchoolType.class);
    this.addAttribute(SCHOOLAREA,"Schoolarea",true,true,Integer.class,this.MANY_TO_ONE,SchoolArea.class);
    this.addAttribute(NAME,"Schoolname",true,true,String.class);
    this.addAttribute(INFO,"Info",true,true,String.class,4000);
    this.addAttribute(ADDRESS,"Address",true,true,String.class,100);
    this.addAttribute(ZIPAREA,"Ziparea",true,true,String.class,20);
    this.addAttribute(ZIPCODE,"Zipcode",true,true,String.class,20);
    this.addAttribute(PHONE,"phone",true,true,String.class,20);
    this.addAttribute(KEYCODE,"keycode",true,true,String.class,20);
    this.addAttribute(LATITUDE,"latitude",true,true,String.class,20);
    this.addAttribute(LONGITUDE,"longitude",true,true,String.class,20);
    this.addAttribute(HEADMASTER,"Headmaster",true,true,Integer.class,this.MANY_TO_ONE,Group.class);
    this.addManyToManyRelationShip(SchoolType.class);
  }
  public String getEntityName() {
    return SCHOOL;
  }
  public int getSchoolTypeId(){
    return this.getIntColumnValue(SCHOOLTYPE);
  }
  public void setSchoolTypeId(int id){
    this.setColumn(SCHOOLTYPE,id);
  }
   public int getSchoolAreaId(){
    return this.getIntColumnValue(SCHOOLAREA);
  }
  public void setSchoolAreaId(int id){
    this.setColumn(SCHOOLAREA,id);
  }
  public String getSchoolName(){
    return this.getStringColumnValue(NAME);
  }
  public void setSchoolName(String name){
    this.setColumn(NAME,name);
  }
  public String getSchoolInfo(){
    return this.getStringColumnValue(INFO);
  }
  public void setSchoolInfo(String info){
    this.setColumn(INFO,info);
  }
  public String getSchoolAddress(){
    return this.getStringColumnValue(ADDRESS);
  }
  public void setSchoolAddress(String address){
    this.setColumn(ADDRESS,address);
  }

  public String getSchoolZipArea(){
    return this.getStringColumnValue(ZIPAREA);
  }
  public void setSchoolZipArea(String ziparea){
    this.setColumn(ZIPAREA,ziparea);
  }

  public String getSchoolZipCode(){
    return this.getStringColumnValue(ZIPCODE);
  }
  public void setSchoolZipCode(String zipcode){
    this.setColumn(ZIPCODE,zipcode);
  }

  public String getSchoolPhone(){
    return this.getStringColumnValue(ZIPCODE);
  }
  public void setSchoolPhone(String zipcode){
    this.setColumn(ZIPCODE,zipcode);
  }

  public int getHeadmasterGroupId(){
    return this.getIntColumnValue(HEADMASTER);
  }
  public void setHeadmasterGroupId(int masterGroupId ){
    this.setColumn(HEADMASTER,masterGroupId);
  }

  public String getSchoolKeyCode(){
    return this.getStringColumnValue(KEYCODE);
  }
  public void setSchoolKeyCode(String code){
    this.setColumn(KEYCODE,code);
  }
   public String getSchoolLatitude(){
    return this.getStringColumnValue(LATITUDE);
  }
  public void setSchoolLatitude(String lat){
    this.setColumn(LATITUDE,lat);
  }
   public String getSchoolLongitude(){
    return this.getStringColumnValue(LONGITUDE);
  }
  public void setSchoolLongitude(String lon){
    this.setColumn(LONGITUDE,lon);
  }

  public Collection ejbFindAllBySchoolType(int typeId) throws javax.ejb.FinderException{
    String select = "select s.* from "+SCHOOL+" s,sch_school_sch_school_type m where m.sch_school_type_id = "+typeId+" and m.sch_school_id = s.sch_school_id";
    return super.idoFindPKsBySQL(select);
  }

  public Collection ejbFindAllBySchoolArea(int areaId)throws javax.ejb.FinderException{
    return super.idoFindPKsBySQL("select * from "+SCHOOL+" where "+SCHOOLAREA+" = "+areaId);
  }

  public Collection ejbFindAllBySchoolAreaAndType(int areaId,int typeId)throws javax.ejb.FinderException{
    String select = "select s.* from "+SCHOOL+" s,sch_school_sch_school_type m where m.sch_school_type_id = "+typeId+" and m.sch_school_id = s.sch_school_id and "+SCHOOLAREA+" = "+areaId;
    return super.idoFindPKsBySQL(select);
  }

  public Collection ejbFindAllSchools()throws javax.ejb.FinderException{
    return super.idoFindAllIDsBySQL();
  }

  public void addSchoolTypes(int[] ids){
    try{
      super.addTo(SchoolType.class,ids);
    }
    catch(java.sql.SQLException sql){

    }
  }

  public void addSchoolTypesRemoveOther(int[] ids){
    try{
      super.removeFrom(SchoolType.class);
    }
    catch(java.sql.SQLException ex){}
    this.addSchoolTypes(ids);
  }

  public Collection findRelatedSchoolTypes()throws com.idega.data.IDORelationshipException{
    return super.idoGetRelatedEntities(SchoolType.class);
  }
}