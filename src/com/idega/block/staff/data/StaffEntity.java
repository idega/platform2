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

public class StaffEntity extends GenericEntity {

    private static String sClassName = StaffEntity.class.getName();

    public StaffEntity(){
      super();
    }

    public StaffEntity(int id)throws SQLException{
      super(id);
    }

    public String getEntityName(){
      return "st_staff";
    }

    public void initializeAttributes(){
      addAttribute(getColumnNameUserID(),"Employee",true,true,"java.lang.Integer","one-to-one","com.idega.core.user.data.User");
      addAttribute(getColumnNameBeganWork(),"Began work",true,true,"java.sql.Date");
      addAttribute(getColumnNameImageID(),"Image",true,true,"java.lang.Integer");
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
      return (StaffInfo)StaffInfo.getStaticInstance(sClassName);
    }

    /*  ColumNames begin   */
    public static String getColumnNameUserID(){return User.getColumnNameUserID();}
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
      removeFrom(StaffLocalized.getStaticInstance(StaffLocalized.class));
      StaffMeta.getStaticInstance(StaffMeta.class).deleteMultiple(StaffMeta.getColumnNameUserID(),Integer.toString(this.getID()));
      super.delete();
    }
}