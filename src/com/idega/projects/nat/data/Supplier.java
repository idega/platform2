//idega 2001 - Gimmi

package com.idega.projects.nat.data;

import java.sql.*;
import com.idega.data.GenericEntity;
import com.idega.core.data.*;

public class Supplier extends GenericEntity{

  public Supplier(){
          super();
  }
  public Supplier(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(), "Name", true, true, String.class);
    addAttribute(getAddressIDColumnName(), "Heimilisfang", true, true, Integer.class ,"many_to_one",Address.class);
    addAttribute(getGroupIDColumnName(),"Hópur", true, true, Integer.class, "many-to-one", GenericGroup.class);
  }

  public void insertStartData()throws Exception{
  }

  public static String getServiceTableName(){return "TB_SUPPLIER";}
  public static String getNameColumnName() {return "NAME";}
  public static String getAddressIDColumnName() {return "IC_ADDRESS_ID";}
  public static String getGroupIDColumnName() {return "IC_GROUP_ID";}



  public String getEntityName(){
    return getServiceTableName();
  }
  public String getName(){
    return getNameColumnName();
  }

  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }

  public Address getAddress() {
      return (Address) getColumnValue("IC_ADDRESS_ID");
  }

}
