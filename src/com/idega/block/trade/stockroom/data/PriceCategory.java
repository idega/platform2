package com.idega.block.trade.stockroom.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class PriceCategory extends GenericEntity{

  public PriceCategory(){
    super();
  }
  public PriceCategory(int id)throws SQLException{
    super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(), "Name", true, true, String.class, 255);
    addAttribute(getColumnNameDescription(), "Lýsing", true, true, String.class, 255);
    addAttribute(getColumnNameType(),"Type",true,true,String.class,255);
    addAttribute(getColumnNameExtraInfo(), "Aðrar upplysingar", true, true, String.class, 255);
    this.addTreeRelationShip();
  }


  public void setDefaultValue() {
    //setName("");
  }

  public String getEntityName(){
    return "SR_PRICE_CATEGORY";
  }
  public String getName(){
    return getStringColumnValue(getColumnNameName());
  }

  public void setName(String name){
    setColumn(getColumnNameName(),name);
  }

  public String getDescription() {
    return getStringColumnValue(getColumnNameDescription());
  }

  public void setDescription(String description) {
    setColumn(getColumnNameDescription(),description);
  }

  public String getExtraInfo(){
    return getStringColumnValue(getColumnNameExtraInfo());
  }

  public void setExtraInfo(String extraInfo){
    setColumn(getColumnNameExtraInfo(),extraInfo);
  }


  public String getType(){
    return getStringColumnValue(getColumnNameType());
  }

  public void setType(String type){
    setColumn(getColumnNameType(),type);
  }

  public static String getColumnNameName() {return "CATEGORY_NAME";}
  public static String getColumnNameDescription() {return "DESCRIPTION";}
  public static String getColumnNameType(){return "CATEGORY_TYPE";}
  public static String getColumnNameExtraInfo() {return "EXTRA_INFO";}






}
