package is.idega.idegaweb.project.data;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.builder.dynamicpagetrigger.data.PageLink;
import com.idega.core.data.GenericGroup;
import com.idega.core.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */


public class IPProjectBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.project.data.IPProject {

    public final static String _COLUMN_NAME = "name";
    public final static String _COLUMN_DESCRIPTION = "description";
    public final static String _COLUMN_PROJECT_NUMBER = "project_number";
    public final static String _COLUMN_PARENT_ID = "parent_id";
    public final static String _COLUMN_CREATION_DATE = "creation_date";
    public final static String _COLUMN_DELETED = "deleted";
    public final static String _COLUMN_DELETED_BY = "deleted_by";
    public final static String _COLUMN_DELETED_WHEN = "deleted_when";

    public IPProjectBMPBean(){
      super();
    }

    public IPProjectBMPBean(int id)throws SQLException{
      super(id);
    }


    public void initializeAttributes(){
      addAttribute(getIDColumnName());
      addAttribute(_COLUMN_NAME,"Nafn",true,true,"java.lang.String");
      addAttribute(_COLUMN_DESCRIPTION,"Description",true,true,String.class,2000);
      addAttribute(_COLUMN_PROJECT_NUMBER,"projectnumber",true,true,"java.lang.String");
      addAttribute(_COLUMN_CREATION_DATE,"creationdate",true,true,"java.sql.Date");
      addAttribute(_COLUMN_PARENT_ID,"yfirverk",true,true,"java.lang.Integer");
      addAttribute(_COLUMN_DELETED,"Deleted",true,true,Boolean.class);
      addAttribute(_COLUMN_DELETED_BY,"Deleted by",true,true,Integer.class,"many-to-one",User.class);
      addAttribute(_COLUMN_DELETED_WHEN,"Deleted when",true,true,Timestamp.class);
      this.addManyToManyRelationShip(IPCategory.class,"ip_project_category");
      this.addManyToManyRelationShip(PageLink.class,"ip_project_dpt_page_link");
      this.addManyToManyRelationShip(GenericGroup.class, "ip_project_ic_group");
    }

    public void setDeleted(boolean value){
      setColumn(_COLUMN_DELETED,value);
    }

    public void setDeletedBy(int userId){
      setColumn(_COLUMN_DELETED_BY,userId);
    }

    public void setDeletedWhen(Timestamp time){
      setColumn(_COLUMN_DELETED_WHEN,time);
    }

    public boolean getDeleted(){
      return getBooleanColumnValue(_COLUMN_DELETED);
    }

    public int getDeletedBy(){
      return getIntColumnValue(_COLUMN_DELETED_BY);
    }

    public Timestamp getDeletedWhen(){
      return (Timestamp)this.getColumnValue(_COLUMN_DELETED_WHEN);
    }

    public String getEntityName(){
      return "ip_project";
    }

    public void setDefaultValues(){
      this.setCreationDate(IWTimestamp.RightNow().getSQLDate());
    }

    public String getName() {
      return (String) getColumnValue(_COLUMN_NAME);
    }

    public void setName(String name) {
      setColumn(_COLUMN_NAME,name);
    }

    public String getDescription(){
      return this.getStringColumnValue(_COLUMN_DESCRIPTION);
    }

    public void setDescription(String description){
      setColumn(_COLUMN_DESCRIPTION,description);
    }

    public String getProjectNumber() {
      return getStringColumnValue(_COLUMN_PROJECT_NUMBER);
    }
    public void setProjectNumber(String project_number) {
      setColumn(_COLUMN_PROJECT_NUMBER,project_number);
    }

    public Date getCreationDate() {
      return (Date)getColumnValue(_COLUMN_CREATION_DATE);
    }
    public void setCreationDate(Date date) {
      setColumn(_COLUMN_CREATION_DATE,date);
    }

    public int getParentId() {
      return getIntColumnValue(_COLUMN_PARENT_ID);
    }
    public void setParentId(int parent_id) {
      setColumn(_COLUMN_PARENT_ID,parent_id);
    }

    public boolean hasParent(){
      return isNull(_COLUMN_PARENT_ID);
    }

    public void setParentIdAsNull() throws SQLException {
      setColumnAsNull(_COLUMN_PARENT_ID);
    }




    /*
    public boolean isValid() {
            return ((Boolean)getColumnValue("valid")).booleanValue();
    }

    public void setValid(boolean valid) {
            setColumn("valid",valid);
    }
    */
}
