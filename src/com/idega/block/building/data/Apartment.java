package com.idega.block.building.data;

import com.idega.data.GenericEntity;
import java.sql.SQLException;
import java.sql.Date;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class Apartment extends GenericEntity {

  public Apartment() {
    super();
  }
  public Apartment(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(),"Name",true,true,"java.lang.String");
    addAttribute(getInfoColumnName(),"Info",true,true,"java.lang.String");
    addAttribute(getFloorIdColumnName(),"Floor",true,true,"java.lang.Integer","many-to-one","com.idega.block.building.data.Floor");
    addAttribute(getApartmentTypeColumnName(),"ApartmentType",true,true,"java.lang.Integer","many-to-one","com.idega.block.building.data.ApartmentType");
    addAttribute(getRentableColumnName(),"Leigjanleg",true,true,"java.lang.Boolean");
    addAttribute(getImageIdColumnName(),"Mynd",true,true,"java.lang.Integer");
    addAttribute(getUnavailableUntilColumnName(),"Frosin",true,true,"java.sql.Date");
    addAttribute(getSerieColumnName(),"Serie",true,true,"java.lang.String",2);
    addAttribute(getStatusColumnName(),"Status",true,true,"java.lang.String");
    setMaxLength("info",5000);
    setMaxLength(getStatusColumnName(),1);
  }
  public String getEntityName() {
    return getNameTableName();
  }
  public static String getNameTableName(){return "bu_apartment";}
  public static String getNameColumnName(){return "name"; }
  public static String getImageIdColumnName(){return "ic_image_id"; }
  public static String getInfoColumnName(){return "info"; }
  public static String getFloorIdColumnName(){return "bu_floor_id";}
  public static String getApartmentTypeColumnName(){return "BU_APRT_TYPE_ID"; }
  public static String getRentableColumnName(){return "rentable"; }
  public static String getUnavailableUntilColumnName(){return "unavailable_until"; }
  public static String getSerieColumnName(){return "serie";}
  public static String getStatusColumnName(){return "status";}

  public final static String AVAILABLE = "A";
  public final static String RENTED = "R";
  public final static String FROZEN = "F";

  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }
  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }
  public String getInfo(){
    return getStringColumnValue(getInfoColumnName());
  }
  public void setInfo(String info){
    setColumn(getInfoColumnName(),info);
  }
  public int getFloorId(){
    return getIntColumnValue(getFloorIdColumnName());
  }
  public void setFloorId(int floor_id){
    setColumn(getFloorIdColumnName(),floor_id);
  }
  public void setFloorId(Integer floor_id){
    setColumn(getFloorIdColumnName(),floor_id);
  }
  public int getApartmentTypeId(){
    return getIntColumnValue(getApartmentTypeColumnName());
  }
  public void setApartmentTypeId(int apartment_type_id){
    setColumn(getApartmentTypeColumnName(),apartment_type_id);
  }
  public void setApartmentTypeId(Integer apartment_type_id){
    setColumn(getApartmentTypeColumnName(),apartment_type_id);
  }
  public int getImageId(){
    return getIntColumnValue(getImageIdColumnName());
  }
  public void setImageId(int room_type_id){
    setColumn(getImageIdColumnName(),room_type_id);
  }
  public void setImageId(Integer room_type_id){
    setColumn(getImageIdColumnName(),room_type_id);
  }
  public boolean getRentable(){
    return getBooleanColumnValue(getRentableColumnName());
  }

  public Date getUnavailableUntil() {
    return((Date)getColumnValue(getUnavailableUntilColumnName()));
  }

  public void setRentable(boolean rentable){
    setColumn(getRentableColumnName(),rentable);
  }

  public void setUnavailableUntil(Date date) {
    setColumn(getUnavailableUntilColumnName(),date);
  }
  public String getSerie(){
    return getStringColumnValue(getSerieColumnName());
  }
  public void setSerie(String serie){
    setColumn(getSerieColumnName(),serie);
  }

  public String getStatus() {
    return(getStringColumnValue(getStatusColumnName()));
  }

  public void setStatus(String status) {
    if (status.equalsIgnoreCase(FROZEN) || status.equalsIgnoreCase(RENTED) || status.equalsIgnoreCase(AVAILABLE))
      setColumn(getStatusColumnName(),status);
    else
      System.err.println("Undefined status :" + status);
  }

  public void setStatusFrozen() {
    setStatus(FROZEN);
  }

  public void setStatusAvailable() {
    setStatus(AVAILABLE);
  }

  public void setStatusRented() {
    setStatus(RENTED);
  }
}