package is.idega.idegaweb.golf.entity;

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



public class PhoneTypeBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.golf.entity.PhoneType {



  public PhoneTypeBMPBean() {

      super();

  }



  public PhoneTypeBMPBean(int id)throws SQLException{

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
