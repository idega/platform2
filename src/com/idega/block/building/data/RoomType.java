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

public class RoomType extends GenericEntity {

  public RoomType() {
    super();
  }
  public RoomType(int id) throws SQLException{
    super(id);
  }
  public String getEntityName() {
    return "room_type";
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute("name","Heiti",true,true,"java.lang.String");
    addAttribute("info","Upplýsingar",true,true,"java.lang.String");
    addAttribute("image_id","Mynd",true,true,"java.lang.Integer");
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