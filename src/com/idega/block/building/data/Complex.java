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

public class Complex extends GenericEntity {

  public Complex() {
    super();
  }

  public Complex(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(),"Name",true,true,"java.lang.String");
    addAttribute("description","Description",true,true,"java.lang.String");

    super.setMaxLength("info",4000);
  }

  public String getEntityName() {
    return "complex";
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