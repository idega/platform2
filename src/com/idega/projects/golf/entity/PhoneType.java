package com.idega.projects.golf.entity;
import java.sql.*;
import com.idega.data.*;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Idega
 * @author       Ægir
 * @version 1.0
 */

public class PhoneType extends GenericEntity {

  public PhoneType() {
      super();
  }

  public PhoneType(int id)throws SQLException{
      super(id);
  }

  public void initializeAttributes(){
      addAttribute(getIDColumnName());
      addAttribute("name", "Tegund", true, true, "java.lang.String");
  }

  public String getEntityName(){
      return "phone_type";
  }

  public String getIDColumnName() {
      return "phone_type_id";
  }

  public String getType(){
      return getStringColumnValue("name");
  }

  public void setType(String name){
      setColumn("name", name);
  }

  public String getName(){
      return getType();
  }

  public void setName(String name){
      setType(name);
  }

}