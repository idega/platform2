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
    addAttribute(getNameColumnName(), "Name", true, true, String.class);
    //addAttribute(getAddressIDColumnName(), "Heimilisfang", true, true, Integer.class ,"many_to_one",Address.class);
    addAttribute(getGroupIDColumnName(),"Hópur", true, true, Integer.class, "many_to_one", EmployeeGroup.class);
    this.addManyToManyRelationShip(Address.class,"SR_SUPPLIER_IC_ADDRESS");
//    this.addManyToManyRelationShip(Phone.class,"SR_SUPPLIER_IC_PHONE");
    this.addManyToManyRelationShip(PriceCategory.class, "SR_SUPPLIER_PRICE_CATEGORY");
    this.addManyToManyRelationShip(ProductCategory.class, "SR_SUPPLIER_PRODUCT_CATEGORY" );
  }

  public void insertStartData()throws Exception{
  }

  public static String getSupplierTableName(){return "SR_SUPPLIER";}
  public static String getNameColumnName() {return "NAME";}
  public static String getAddressIDColumnName() {return "IC_ADDRESS_ID";}
  public static String getGroupIDColumnName() {return "IC_GROUP_ID";}
 // public static String getSupplierAddressEntityName(){return "sr_supplier_ic_address"}



  public String getEntityName(){
    return getSupplierTableName();
  }
  public String getName(){
    return getNameColumnName();
  }

  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }

/*
  public Address getAddress() {
      return (Address) getColumnValue("IC_ADDRESS_ID"); where address_type is st_supplier_address
  }
*/
}
