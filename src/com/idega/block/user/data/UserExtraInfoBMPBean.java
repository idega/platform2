package com.idega.block.user.data;

import java.sql.Date;
import java.sql.SQLException;

import com.idega.data.GenericEntity;


/**
 * This bean is almost identical to com.idega.block.staff.data.StaffInfoBMPBean, the main difference being that this one is not a legacy bean and has image
 * relationship removed. The reason to have it here is to have backwards compatibility with the staff module, this info is available to the user block
 * without dependency on the staff block. When a system is upgraded to use the user block and not the staff block, the data available through this bean 
 * does not have to be reentered.
 */
public class UserExtraInfoBMPBean extends GenericEntity implements UserExtraInfo {

    private static String sClassName = UserExtraInfo.class.getName();

    public UserExtraInfoBMPBean(){
      super();
    }

    public UserExtraInfoBMPBean(int id)throws SQLException{
      super(id);
    }


    public String getEntityName(){
            return "st_staff_info";
    }

    public void initializeAttributes(){
      addOneToOneRelationship(getColumnNameUserID(),"Employee",com.idega.core.user.data.User.class);
      addAttribute(getColumnNameTitle(),"Title",true,true,"java.lang.String");
      addAttribute(getColumnNameEducation(),"Education",true,true,"java.lang.String");
      addAttribute(getColumnNameSchool(),"School",true,true,"java.lang.String");
      addAttribute(getColumnNameArea(),"Field�",true,true,"java.lang.String");
      addAttribute(getColumnNameBeganWork(),"Began Work",true,true,"java.sql.Date");
      setNullable(getColumnNameUserID(),false);
      setAsPrimaryKey(getColumnNameUserID(),true);
    }

    public void setDefaultValues(){
    }

    public String getIDColumnName(){
      return getColumnNameUserID();
    }

    public static UserExtraInfo getStaticInstance(){
      return (UserExtraInfo)com.idega.block.user.data.UserExtraInfoBMPBean.getStaticInstance(sClassName);
    }


    /*  ColumNames begin   */

    public static String getColumnNameUserID(){return com.idega.core.user.data.UserBMPBean.getColumnNameUserID();}
    public static String getColumnNameTitle(){return "title";}
    public static String getColumnNameEducation(){return "education";}
    public static String getColumnNameSchool(){return "school";}
    public static String getColumnNameArea(){return "area";}
    public static String getColumnNameBeganWork(){return "began_work";}

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
}
