//idega 2000 - ægir og eiki

package com.idega.block.contract.data;

//import java.util.*;
import java.sql.Date;
import java.sql.SQLException;


public class ContractCategoryBMPBean extends com.idega.data.GenericEntity implements com.idega.block.contract.data.ContractCategory {

  public ContractCategoryBMPBean(){
          super();
  }
  public ContractCategoryBMPBean(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(), "Name", true, true, String.class);
    addAttribute(getColumnNameDescription(), "Description", true, true, String.class);
		addAttribute(getColumnNameCreated(),"Created",true,true,java.sql.Date.class);
		addAttribute(getValidColumnName(), "Valid", true, true, Boolean.class);
    addManyToManyRelationShip(com.idega.core.data.ICObjectInstance.class);
  }

  public void insertStartData()throws Exception{
    ContractCategory cat = ((com.idega.block.contract.data.ContractCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ContractCategory.class)).createLegacy();
    cat.setName("Default");
    cat.setDescription("Default Category for idegaWeb");
		cat.setValid(true);
    cat.insert();

  }

  public static String getEntityTableName(){return "CON_CATEGORY";}
  public static String getColumnNameName(){return "NAME";}
  public static String getColumnNameDescription(){return "DESCRIPTION";}
	public static String getColumnNameCreated(){return "CREATED";}
	public static String getValidColumnName(){return "VALID";}

  public String getEntityName(){
    return getEntityTableName();
  }
  public String getName(){
    return getNewsCategoryName();
  }
  public String getNewsCategoryName(){
    return getStringColumnValue(getColumnNameName());
  }

  public void setName(String name){
    setCategoryName(name);
  }

  public void setCategoryName(String category_name){
    setColumn(getColumnNameName(), category_name);
  }
  public String getDescription(){
    return getStringColumnValue(getColumnNameDescription());
  }
  public void setDescription(String description){
    setColumn(getColumnNameDescription(), description);
  }
	public void setCreationDate(Date date) {
    setColumn(getColumnNameCreated(),date);
  }
  public Date getCreationDate(){
    return (Date) getColumnValue(getColumnNameCreated() );
  }

	public boolean getValid(){
    return getBooleanColumnValue(getValidColumnName());
  }
  public void setValid(boolean valid){
    setColumn(getValidColumnName(), valid);
  }

}
