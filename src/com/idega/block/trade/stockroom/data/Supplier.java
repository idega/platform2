package com.idega.block.trade.stockroom.data;

import java.sql.*;
import com.idega.data.GenericEntity;
import com.idega.core.data.*;
import com.idega.block.employment.data.EmployeeGroup;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */


public class Supplier extends GenericEntity{

  public Supplier(){
          super();
  }
  public Supplier(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(), "Name", true, true, String.class);
    addAttribute(getColumnNameDescription(), "Lýsing", true, true, String.class,500);
    addAttribute(getColumnNameGroupID(),"Hópur", true, true, Integer.class, "many_to_one", EmployeeGroup.class);

    this.addManyToManyRelationShip(Address.class,"SR_SUPPLIER_IC_ADDRESS");
    this.addManyToManyRelationShip(Phone.class,"SR_SUPPLIER_IC_PHONE");
    this.addManyToManyRelationShip(PriceCategory.class, "SR_SUPPLIER_PRICE_CATEGORY");
    this.addManyToManyRelationShip(ProductCategory.class, "SR_SUPPLIER_PRODUCT_CATEGORY" );
  }

  public void insertStartData()throws Exception{
  }

  public static String getSupplierTableName(){return "SR_SUPPLIER";}
  public static String getColumnNameName() {return "NAME";}
  public static String getColumnNameDescription() {return "NAME";}
  public static String getColumnNameGroupID() {return "IC_GROUP_ID";}

  public String getEntityName(){
    return getSupplierTableName();
  }


  public String getName(){
    return getStringColumnValue(getColumnNameName());
  }

  public void setName(String name){
    setColumn(getColumnNameName(),name);
  }

  public String getDescription(){
    return getStringColumnValue(getColumnNameDescription());
  }

  public void setDescription(String description){
    setColumn(getColumnNameDescription(),description);
  }

  public void setGroupId(int id){
    setColumn(getColumnNameGroupID(), id);
  }

  public int getGroupId(){
    return getIntColumnValue(getColumnNameGroupID());
  }

/*
  public Address getAddress() {
      return (Address) getColumnValue("IC_ADDRESS_ID"); where address_type is st_supplier_address
  }
*/
}
