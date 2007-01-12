package com.idega.block.staff.data;

import java.sql.Date;
import java.sql.SQLException;

import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.user.data.UserBMPBean;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
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
      //setNullable(getColumnNameImageID(),false);
      setAsPrimaryKey(getColumnNameUserID(),true);
      addManyToManyRelationShip(StaffLocalized.class,"ST_STAFF_LOCALIZED_STAFF");
    }

    public void setDefaultValues(){
    }

    public String getIDColumnName(){
      return getColumnNameUserID();
    }

    public static StaffInfo getStaticInstance(){
      return (StaffInfo)GenericEntity.getStaticInstance(sClassName);
    }

    /*  ColumNames begin   */
    public static String getColumnNameUserID(){return UserBMPBean.getColumnNameUserID();}
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
      removeFrom(GenericEntity.getStaticInstance(StaffLocalized.class));
      GenericEntity.getStaticInstance(StaffMeta.class).deleteMultiple(StaffMetaDataBMPBean.getColumnNameUserID(),Integer.toString(this.getID()));
      super.delete();
    }
}
