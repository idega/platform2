package com.idega.block.staff.data;

import com.idega.data.*;
import com.idega.core.data.*;
import com.idega.core.user.data.User;
import java.sql.*;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class StaffMetaData extends GenericEntity {

    private static String sClassName = StaffMetaData.class.getName();

    public StaffMetaData(){
      super();
    }

    public StaffMetaData(int id)throws SQLException{
      super(id);
    }


    public String getEntityName(){
      return "st_staff_meta_data";
    }

    public void initializeAttributes(){
      addAttribute(getColumnNameUserID(),"User",true,true,"java.lang.Integer","many-to-one","com.idega.core.user.data.User");
      addAttribute(getColumnNameAttribute(),"Attribute",true,true,"java.lang.String");
      addAttribute(getColumnNameValue(),"Value",true,true,"java.lang.String");
    }


    public static StaffMetaData getStaticInstance(){
      return (StaffMetaData)StaffMetaData.getStaticInstance(sClassName);
    }


    /*  ColumnNames begin   */

    public static String getColumnNameUserID(){return User.getColumnNameUserID();}
    public static String getColumnNameAttribute(){return "meta_attribute";}
    public static String getColumnNameValue(){return "meta_value";}

    /*  ColumnNames end   */


    /*  Getters begin   */

    public int getUserID() {
      return getIntColumnValue(getColumnNameUserID());
    }

    public String getAttribute() {
      return (String) getColumnValue(getColumnNameAttribute());
    }

    public String getValue() {
      return (String) getColumnValue(getColumnNameValue());
    }

    /*  Getters end   */


    /*  Setters begin   */
    public void setUserID(int userID) {
      setColumn(getColumnNameUserID(),userID);
    }

    public void setAttribute(String attribute) {
      setColumn(getColumnNameAttribute(),attribute);
    }

    public void setValue(String value) {
      setColumn(getColumnNameValue(),value);
    }

}