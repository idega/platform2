package is.idega.idegaweb.project.data;

import java.sql.SQLException;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class IPCategoryBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.project.data.IPCategory {

  public final static String _COLUMN_NAME = "name";
  public final static String _COLUMN_DESCRIPTION = "description";
  public final static String _COLUMN_TYPE_ID = "ip_category_type_id";

  public IPCategoryBMPBean(){
    super();
  }

  public IPCategoryBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(this.getIDColumnName());
    this.addAttribute(_COLUMN_NAME,"Name",true,true,String.class,255);
    this.addAttribute(_COLUMN_DESCRIPTION,"Description",true,true,String.class,1000);
    this.addAttribute(_COLUMN_TYPE_ID,"type_id",true,true, Integer.class, this.ONE_TO_MANY,IPCategoryType.class);
    this.addManyToManyRelationShip(IPProject.class,"ip_project_category");
  }

  public String getEntityName() {
    return "ip_category";
  }


  public String getName(){
    return this.getStringColumnValue(_COLUMN_NAME);
  }

  public void setName(String name){
    this.setColumn(_COLUMN_NAME,name);
  }

  public String getDescription(){
    return this.getStringColumnValue(_COLUMN_DESCRIPTION);
  }

  public void setDescription(String description){
    this.setColumn(_COLUMN_DESCRIPTION,description);
  }

  public int getCategoryTypeId(){
    return this.getIntColumnValue(this._COLUMN_TYPE_ID);
  }

  public void setCategoryTypeId(int id){
    this.setColumn(_COLUMN_TYPE_ID,id);
  }

}
