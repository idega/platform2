package com.idega.block.trade.data;

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

public class CurrencyBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.data.Currency {

  public CurrencyBMPBean() {
  }

  public CurrencyBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(getColumnNameCurrencyName(),"Nafn",true,true,String.class,255);
    this.addAttribute(getColumnNameCurrencyAbbreviation(),"Skammstöfun",true,true,String.class,20);

  }

  public String getEntityName() {
    return "TR_CURRENCY";
  }

  public String getName() {
    return getCurrencyName();
  }

  public String getCurrencyName() {
    return getStringColumnValue(getColumnNameCurrencyName());
  }

  public String getCurrencyAbbreviation() {
    return getStringColumnValue(getColumnNameCurrencyAbbreviation());
  }

  public void setCurrencyName(String name) {
    setColumn(getColumnNameCurrencyName(), name);
  }

  public void setCurrencyAbbreviation(String abbreviation) {
    setColumn(getColumnNameCurrencyAbbreviation(), abbreviation);
  }



  public static String getColumnNameCurrencyID(){return "TR_CURRENCY_ID";}
  public static String getColumnNameCurrencyName(){return"CURRENCY_NAME";}
  public static String getColumnNameCurrencyAbbreviation(){return"CURRENCY_ABBREVIATION";}

}
