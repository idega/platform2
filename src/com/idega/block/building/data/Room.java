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

public class Room extends GenericEntity {

  public Room() {
    super();
  }
  public Room(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("name","Heiti",true,true,"java.lang.String");
    addAttribute("info","Upplýsingar",true,true,"java.lang.String");
    addAttribute("floor_id","Hæð",true,true,"java.lang.Integer","many-to-one","com.idega.block.building.data.Floor");
    addAttribute("room_type_id","Mynd",true,true,"java.lang.Integer","many-to-one","com.idega.block.building.data.RoomType");
    addAttribute("rentable","Leigjanleg",true,true,"java.lang.Boolean");
    addAttribute("image_id","Mynd",true,true,"java.lang.Integer");
  }
  public String getEntityName() {
    return "room";
  }
  public String getName(){
    return getStringColumnValue("name");
  }
  public void setName(String name){
    setColumn("name",name);
  }
  public String getInfo(){
    return getStringColumnValue("info");
  }
  public void setInfo(String info){
    setColumn("info",info);
  }
  public int getBuildingId(){
    return getIntColumnValue("floor_id");
  }
  public void setBuildingId(int floor_id){
    setColumn("floor_id",floor_id);
  }
  public void setBuildingId(Integer floor_id){
    setColumn("floor_id",floor_id);
  }
  public int getImageId(){
    return getIntColumnValue("room_type_id");
  }
  public void setImageId(int room_type_id){
    setColumn("room_type_id",room_type_id);
  }
  public void setImageId(Integer room_type_id){
    setColumn("room_type_id",room_type_id);
  }
  public boolean getRentable(){
    return getBooleanColumnValue("rentable");
  }
  public void setRentable(boolean rentable){
    setColumn("rentable",rentable);
  }



}