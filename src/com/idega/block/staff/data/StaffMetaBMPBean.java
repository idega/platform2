package com.idega.block.staff.data;

import java.sql.SQLException;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class StaffMetaBMPBean extends com.idega.block.staff.data.StaffMetaDataBMPBean implements com.idega.block.staff.data.StaffMeta {

    private static String sClassName = StaffMeta.class.getName();

    public StaffMetaBMPBean(){
      super();
    }

    public StaffMetaBMPBean(int id)throws SQLException{
      super(id);
    }


    public String getEntityName(){
      return "st_staff_meta";
    }

    public void initializeAttributes(){
    	//Adding fake primary key
    	//Disabled for backwards compatability
    	//addAttribute(getIDColumnName());

      addAttribute(getColumnNameUserID(),"User",true,true,Integer.class,"many-to-one",com.idega.core.user.data.User.class);
      addAttribute(getColumnNameLocaleId(), "Locale", true, true, Integer.class,"many_to_one",com.idega.core.localisation.data.ICLocale.class);
      addAttribute(getColumnNameAttribute(),"Attribute",true,true,String.class);
      addAttribute(getColumnNameValue(),"Value",true,true,String.class);
    }


    /*  ColumnNames begin   */
    public static String getColumnNameLocaleId(){ return "IC_LOCALE_ID";}
    /*  ColumnNames end   */


    /*  Getters begin   */
    public int getLocaleId(){
      return getIntColumnValue(getColumnNameLocaleId());
    }
    /*  Getters end   */


    /*  Setters begin   */
    public void setLocaleId(int id){
      setColumn(getColumnNameLocaleId(),id);
    }
    /*  Setters end   */

}
