package com.idega.block.trade.stockroom.data;

import com.idega.data.*;
import java.sql.SQLException;


/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class ProductCategory extends GenericEntity {

  public ProductCategory() {
  }


  public ProductCategory(int id) throws SQLException {
    super(id);
  }


  public void initializeAttributes() {
    this.addAttribute(this.getIDColumnName());
    this.addAttribute(getCategoryNameColumnName(),"Nafn",true,true,String.class,255);
    this.addAttribute(getDescriptionColumnName(),"Lýsing",true,true,String.class,1000);
    this.addAttribute(this.getIsValidColumnName(),"í notkun",true,true,Boolean.class);
    this.addAttribute(getExtraInfoColumnName(),"Aukaupplýsingar",true,true,String.class,1000);
    this.addTreeRelationShip();
  }

  public String getEntityName(){
    return "SR_PRODUCT_CATEGORY";
  }

  public static String getCategoryNameColumnName(){return "category_name";}
  public static String getDescriptionColumnName(){return "description";}
  public static String getExtraInfoColumnName(){return "extra_info";}
  public static String getCategoryTypeColumnName(){return "category_type";}
  public static String getIsValidColumnName(){return "is_valid";}






} // Class ProductCategory