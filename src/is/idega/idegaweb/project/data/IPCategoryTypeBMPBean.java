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

public class IPCategoryTypeBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.project.data.IPCategoryType {

  public final static String _COLUMN_NAME = "name";
  public final static String _COLUMN_DESCRIPTION = "description";

  public IPCategoryTypeBMPBean(){
    super();
  }

  public IPCategoryTypeBMPBean(int id) throws SQLException {
    super(id);
  }


  public void initializeAttributes() {
    this.addAttribute(this.getIDColumnName());
    this.addAttribute(_COLUMN_NAME,"Name",true,true,String.class,255);
    this.addAttribute(_COLUMN_DESCRIPTION,"Description",true,true,String.class,1000);
  }

  public String getEntityName() {
    return "ip_category_type";
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

}
