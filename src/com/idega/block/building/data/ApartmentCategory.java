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

public class ApartmentCategory extends GenericEntity {

  public ApartmentCategory() {
    super();
  }
  public ApartmentCategory(int id) throws SQLException{
    super(id);
  }
  public String getEntityName() {
    return "bu_aprt_cat";
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(),"Name",true,true,"java.lang.String");
    addAttribute(getInfoColumnName(),"Info",true,true,"java.lang.String");
    super.setMaxLength(getInfoColumnName(),4000);
  }

  public String getNameColumnName(){return "name";}
  public String getInfoColumnName(){return "info";}

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
}