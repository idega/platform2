package com.idega.block.trade.data;

import com.idega.data.*;


/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class Currency extends GenericEntity {

  public Currency() {
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(getColumnNameCurrencyName(),"Nafn",true,true,String.class,255);
    this.addAttribute(getColumnNameCurrencyAbbreviation(),"Skammstöfun",true,true,String.class,20);

  }

  public String getEntityName() {
    return "TR_CURRENCY";
  }





  public static String getColumnNameCurrencyName(){return"CURRENCY_NAME";}
  public static String getColumnNameCurrencyAbbreviation(){return"CURRENCY_ABBREVIATION";}

}