package com.idega.block.trade.stockroom.data;

import com.idega.data.*;
import com.idega.core.data.*;
import java.sql.SQLException;


/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class Product extends GenericEntity {

  public Product() {
    super();
  }

  public Product(int id) throws SQLException {
    super(id);
  }


  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(getSupplierIdColumnName(),"Birgi",true,true,Integer.class,"many_to_one",Supplier.class);
    this.addAttribute(getFileIdColumnName(),"Fylgiskjal(mynd)",true,true,Integer.class,"many_to_one",ICFile.class);
    this.addAttribute(getProductNameColumnName(),"Nafn vöru",true,true,String.class,255);
    this.addAttribute(getIsValidColumnName(),"í notkun",true,true,Boolean.class);
    this.addAttribute(getPriceIdColumnName(),"Verð",true,true,Integer.class,"many_to_one",ProductPrice.class);
    this.addManyToManyRelationShip(PriceCategory.class,"SR_PRODUCT_PRICE_CATEGORY");
    this.addManyToManyRelationShip(ProductCategory.class,"SR_PRODUCT_PRODUCT_CATEGORY");
  }


  public String getEntityName() {
    return getProductEntityName();
  }


  public static String getProductEntityName(){return "SR_PRODUCT";}
  public static String getSupplierIdColumnName(){return "SR_SUPPLIER_ID";}
  public static String getFileIdColumnName(){return "IC_FILE_ID";}
  public static String getProductNameColumnName(){return "PRODUCT_NAME";}
  public static String getIsValidColumnName(){return "IS_VALID";}
  public static String getPriceIdColumnName(){return "SR_PRODUCT_PRICE";}



}


