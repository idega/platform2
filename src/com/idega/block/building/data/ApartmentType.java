package com.idega.block.building.data;

import com.idega.data.GenericEntity;
import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class ApartmentType extends GenericEntity {

  public ApartmentType() {
    super();
  }
  public ApartmentType(int id) throws SQLException{
    super(id);
  }
  public String getEntityName() {
    return "bu_aprt_type";
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getApartmentCategoryIdColumnName(),"Category",true,true,"java.lang.Integer","many-to-one","com.idega.block.building.data.ApartmentCategory");
    addAttribute(getNameColumnName(),"Name",true,true,"java.lang.String");
    addAttribute(getInfoColumnName(),"Info",true,true,"java.lang.String");
    addAttribute(getImageIdColumnName(),"Photo",true,true,"java.lang.Integer");
    addAttribute(getFloorPlanIdColumnName(),"Plan",true,true,"java.lang.Integer");
    addAttribute(getRoomCountColumnName(),"Room Count",true,true,"java.lang.Integer");
    addAttribute(getAreaColumnName(),"Area",true,true,"java.lang.Float");
    addAttribute(getKitchenColumnName(),"Kitchen",true,true,"java.lang.Boolean");
    addAttribute(getBathroomColumnName(),"Bath",true,true,"java.lang.Boolean");
    addAttribute(getStorageColumnName(),"Storage",true,true,"java.lang.Boolean");
    addAttribute(getBalconyColumnName(),"Balcony",true,true,"java.lang.Boolean");
    addAttribute(getStudyColumnName(),"Study",true,true,"java.lang.Boolean");
    addAttribute(getLoftColumnName(),"Loft",true,true,"java.lang.Boolean");
    addAttribute(getRentColumnName(),"Rent",true,true,"java.lang.Integer");
    addAttribute(getFurnitureColumnName(),"Furniture",true,true,"java.lang.Boolean");
    super.setMaxLength(getInfoColumnName(),4000);
  }

  public String getApartmentCategoryIdColumnName(){return "apartment_category_id";}
  public String getNameColumnName(){return "name";}
  public String getInfoColumnName(){return "info";}
  public String getImageIdColumnName(){return "ic_image_id"; }
  public String getFloorPlanIdColumnName(){return "plan_id"; }
  public String getRoomCountColumnName(){return "room_count";}
  public String getAreaColumnName(){return "area";}
  public String getKitchenColumnName(){return "area";}
  public String getBathroomColumnName(){return "name";}
  public String getStorageColumnName(){return "info";}
  public String getBalconyColumnName(){return "name";}
  public String getStudyColumnName(){return "info";}
  public String getLoftColumnName(){return "loft";}
  public String getRentColumnName(){return "rent";}
  public String getFurnitureColumnName(){return "furniture";}


  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }
  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }
  public int getApartmentCategoryId(){
    return getIntColumnValue(getApartmentCategoryIdColumnName());
  }
  public void setApartmentCategoryId(int apartment_category_id){
    setColumn(getApartmentCategoryIdColumnName(),apartment_category_id);
  }
  public void setApartmentCategoryId(Integer apartment_category_id){
    setColumn(getApartmentCategoryIdColumnName(),apartment_category_id);
  }
  public String getInfo(){
    return getStringColumnValue(getInfoColumnName());
  }
  public void setInfo(String info){
    setColumn(getInfoColumnName(),info);
  }
  public int getImageId(){
    return getIntColumnValue(getImageIdColumnName());
  }
  public void setImageId(int image_id){
    setColumn(getImageIdColumnName(),image_id);
  }
  public void setImageId(Integer image_id){
    setColumn(getImageIdColumnName(),image_id);
  }
  public int getFloorPlanId(){
    return getIntColumnValue(getFloorPlanIdColumnName());
  }
  public void setFloorPlanId(int floorplan_id){
    setColumn(getFloorPlanIdColumnName(),floorplan_id);
  }
  public void setFloorPlanId(Integer floorplan_id){
    setColumn(getFloorPlanIdColumnName(),floorplan_id);
  }
  public int getRoomCount(){
    return getIntColumnValue(getRoomCountColumnName());
  }
  public void setRoomCount(int room_count){
    setColumn(getRoomCountColumnName(),room_count);
  }
  public void setRoomCount(Integer room_count){
    setColumn(getRoomCountColumnName(),room_count);
  }
  public float getArea(){
    return getFloatColumnValue(getAreaColumnName());
  }
  public void setArea(float area){
    setColumn(getAreaColumnName(),area);
  }
  public void setArea(Float area){
    setColumn(getAreaColumnName(),area);
  }
  public int getRent(){
    return getIntColumnValue(getRentColumnName());
  }
  public void setRent(int rent){
    setColumn(getRentColumnName(),rent);
  }
  public void setRent(Integer rent){
    setColumn(getRentColumnName(),rent);
  }
  public void setKitchen(boolean kitchen) {
    setColumn(getKitchenColumnName(), kitchen);
  }
  public boolean getKitchen() {
    return getBooleanColumnValue(getKitchenColumnName());
  }
  public void setBathRoom(boolean bathroom) {
    setColumn(getBathroomColumnName(), bathroom);
  }
  public boolean getBathRoom() {
    return getBooleanColumnValue(getBathroomColumnName());
  }
  public void setStorage(boolean storage) {
    setColumn(getStorageColumnName(), storage);
  }
  public boolean getStorage() {
    return getBooleanColumnValue(getStorageColumnName());
  }
  public void setBalcony(boolean balcony) {
    setColumn(getBalconyColumnName(), balcony);
  }
  public boolean getBalcony() {
    return getBooleanColumnValue(getBalconyColumnName());
  }
  public void setStudy(boolean study) {
    setColumn(getStudyColumnName(), study);
  }
  public boolean getStudy() {
    return getBooleanColumnValue(getStudyColumnName());
  }
  public void setLoft(boolean loft) {
    setColumn(getLoftColumnName(), loft);
  }
  public boolean getLoft() {
    return getBooleanColumnValue(getLoftColumnName());
  }
   public boolean getFurniture() {
    return getBooleanColumnValue(getFurnitureColumnName());
  }
  public void setFurniture(boolean furniture) {
    setColumn(getFurnitureColumnName(), furniture);
  }
}