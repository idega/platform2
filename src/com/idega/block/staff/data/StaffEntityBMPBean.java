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

public class StaffEntityBMPBean extends com.idega.data.GenericEntity implements com.idega.block.staff.data.StaffEntity {

    private static String sClassName = StaffEntity.class.getName();

    public StaffEntityBMPBean(){
      super();
    }

    public StaffEntityBMPBean(int id)throws SQLException{
      super(id);
    }

    public String getEntityName(){
      return "st_staff";
    }

    public void initializeAttributes(){
      addOneToOneRelationship(getColumnNameUserID(),"Employee",com.idega.core.user.data.User.class);
      addAttribute(getColumnNameBeganWork(),"Began work",true,true,"java.sql.Date");
      addManyToOneRelationship(getColumnNameImageID(),"Image",ICFile.class);
      setNullable(getColumnNameUserID(),false);
      setNullable(getColumnNameImageID(),false);
      setAsPrimaryKey(getColumnNameUserID(),true);
      addManyToManyRelationShip(StaffLocalized.class,"ST_STAFF_LOCALIZED_STAFF");
    }

    public void setDefaultValues(){
    }

    public String getIDColumnName(){
      return getColumnNameUserID();
    }

    public static StaffInfo getStaticInstance(){
      return (StaffInfo)com.idega.block.staff.data.StaffInfoBMPBean.getStaticInstance(sClassName);
    }

    /*  ColumNames begin   */
    public static String getColumnNameUserID(){return com.idega.core.user.data.UserBMPBean.getColumnNameUserID();}
    public static String getColumnNameBeganWork(){return "began_work";}
    public static String getColumnNameImageID(){return "ic_file_id";}

    /*  Getters begin   */
    public Date getBeganWork(){
      return (Date) getColumnValue(getColumnNameBeganWork());
    }

    public int getImageID() {
      return getIntColumnValue(getColumnNameImageID());
    }

    /*  Setters begin   */
    public void setBeganWork(Date beganWork){
      setColumn(getColumnNameBeganWork(),beganWork);
    }

    public void setImageID(int imageID){
      setColumn(getColumnNameImageID(),imageID);
    }

    /*  Delete   */
    public void delete() throws SQLException{
      removeFrom(com.idega.block.staff.data.StaffLocalizedBMPBean.getStaticInstance(StaffLocalized.class));
      com.idega.block.staff.data.StaffMetaBMPBean.getStaticInstance(StaffMeta.class).deleteMultiple(com.idega.block.staff.data.StaffMetaBMPBean.getColumnNameUserID(),Integer.toString(this.getID()));
      super.delete();
    }
}
