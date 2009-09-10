package com.idega.block.staff.data;

import java.sql.SQLException;
import com.idega.data.GenericEntity;
import com.idega.user.data.UserBMPBean;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class StaffMetaDataBMPBean extends com.idega.data.GenericEntity implements com.idega.block.staff.data.StaffMetaData {

    private static String sClassName = StaffMetaData.class.getName();

    public StaffMetaDataBMPBean(){
      super();
    }

    public StaffMetaDataBMPBean(int id)throws SQLException{
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
      return (StaffMetaData)GenericEntity.getStaticInstance(sClassName);
    }


    /*  ColumnNames begin   */

    public static String getColumnNameUserID(){return UserBMPBean.getColumnNameUserID();}
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
