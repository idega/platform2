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

public class StaffInfoBMPBean extends com.idega.data.GenericEntity implements com.idega.block.staff.data.StaffInfo {

    private static String sClassName = StaffInfo.class.getName();

    public StaffInfoBMPBean(){
      super();
    }

    public StaffInfoBMPBean(int id)throws SQLException{
      super(id);
    }


    public String getEntityName(){
            return "st_staff_info";
    }

    public void initializeAttributes(){
      addOneToOneRelationship(getColumnNameUserID(),"Employee",com.idega.core.user.data.User.class);
      addAttribute(getColumnNameTitle(),"Titill",true,true,"java.lang.String");
      addAttribute(getColumnNameEducation(),"Menntun",true,true,"java.lang.String");
      addAttribute(getColumnNameSchool(),"Sk�laganga",true,true,"java.lang.String");
      addAttribute(getColumnNameArea(),"Starfssvi�",true,true,"java.lang.String");
      addAttribute(getColumnNameBeganWork(),"H�f st�rf",true,true,"java.sql.Date");
      addManyToOneRelationship(getColumnNameImageID(),"Image",ICFile.class);
      setNullable(getColumnNameUserID(),false);
      setNullable(getColumnNameImageID(),true);
      setAsPrimaryKey(getColumnNameUserID(),true);
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
    public static String getColumnNameTitle(){return "title";}
    public static String getColumnNameEducation(){return "education";}
    public static String getColumnNameSchool(){return "school";}
    public static String getColumnNameArea(){return "area";}
    public static String getColumnNameBeganWork(){return "began_work";}
    public static String getColumnNameImageID(){return "ic_file_id";}

    /*  ColumNames end   */


    /*  Getters begin   */

    public String getTitle() {
      return (String) getColumnValue(getColumnNameTitle());
    }

    public String getEducation() {
      return (String) getColumnValue(getColumnNameEducation());
    }

    public String getSchool() {
      return (String) getColumnValue(getColumnNameSchool());
    }

    public String getArea() {
      return (String) getColumnValue(getColumnNameArea());
    }

    public Date getBeganWork(){
      return (Date) getColumnValue(getColumnNameBeganWork());
    }

    public int getImageID() {
      return getIntColumnValue(getColumnNameImageID());
    }

    /*  Getters end   */


    /*  Setters begin   */

    public void setTitle(String title) {
      setColumn(getColumnNameTitle(),title);
    }

    public void setEducation(String education) {
      setColumn(getColumnNameEducation(),education);
    }

    public void setSchool(String school) {
      setColumn(getColumnNameSchool(),school);
    }

    public void setArea(String area) {
      setColumn(getColumnNameArea(),area);
    }

    public void setBeganWork(Date beganWork){
      setColumn(getColumnNameBeganWork(),beganWork);
    }

    public void setImageID(int imageID){
      setColumn(getColumnNameImageID(),imageID);
    }
}
