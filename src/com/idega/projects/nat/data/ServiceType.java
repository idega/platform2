//idega 2001 - Gimmi

package com.idega.projects.nat.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;

public class ServiceType extends GenericEntity{

  public ServiceType(){
          super();
  }
  public ServiceType(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(), "Name", true, true, String.class, 255);
    addAttribute(getDescriptionColumnName(), "Lýsing", true, true, java.sql.Date.class);
    addAttribute(getExtraInfoColumnName(), "Aðrar upplysingar", true, true, java.sql.Date.class);

    this.addManyToManyRelationShip(Service.class,"TB_SERVICE_SERVICE_TYPE");
    this.addManyToManyRelationShip(com.idega.core.user.data.User.class,"TB_SERVICE_TYPE_IC_USER");
  }


  public void setDefaultValue() {
    setName("");
  }

  public String getEntityName(){
    return getServiceTypeTableName();
  }
  public String getName(){
    return getNameColumnName();
  }

  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }

  public String getDescription() {
    return getStringColumnValue(getDescriptionColumnName());
  }

  public void setDescription(String description) {
    setColumn(getDescriptionColumnName(),description);
  }

  public String getExtraInfo() {
    return getStringColumnValue(getExtraInfoColumnName());
  }

  public void setExtraInfo(String extraInfo) {
    setColumn(getExtraInfoColumnName(), extraInfo);
  }

  public static String getServiceTypeTableName(){return "TB_SERVICE_TYPE";}
  public static String getNameColumnName() {return "NAME";}
  public static String getDescriptionColumnName() {return "DESCRIPTION";}
  public static String getExtraInfoColumnName() {return "EXTRA_INFO";}






}
