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

public class Floor extends GenericEntity {

  public Floor() {
    super();
  }
  public Floor(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("name","Heiti",true,true,"java.lang.String");
    addAttribute("info","Upplýsingar",true,true,"java.lang.String");
    addAttribute("building_id","Bygging",true,true,"java.lang.Integer","many-to-one","com.idega.block.building.data.Building");
    addAttribute("image_id","Mynd",true,true,"java.lang.Integer");
  }

  public String getEntityName() {
    return "floor";
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
    return getIntColumnValue("building_id");
  }
  public void setBuildingId(int building_id){
    setColumn("building_id",building_id);
  }
  public void setBuildingId(Integer building_id){
    setColumn("building_id",building_id);
  }
  public int getImageId(){
    return getIntColumnValue("image_id");
  }
  public void setImageId(int image_id){
    setColumn("image_id",image_id);
  }
  public void setImageId(Integer image_id){
    setColumn("image_id",image_id);
  }

}