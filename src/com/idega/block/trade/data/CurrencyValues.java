package com.idega.block.trade.data;

import com.idega.data.*;
import java.sql.*;


/**
 * Title:        User
 * Copyright:    Copyright (c) 2002
 * Company:      idega.is
 * @author 2002 - idega team - <a href="mailto:laddi@idega.is">Þórhallur Helgason</a>
 * @version 1.0
 */

public class CurrencyValues extends GenericEntity {

    private static String sClassName = CurrencyValues.class.getName();

    public CurrencyValues(){
      super();
    }

    public CurrencyValues(int id)throws SQLException{
      super(id);
    }

    public String getEntityName(){
      return "TR_CURRENCY_VALUES";
    }

    public void initializeAttributes(){
      addAttribute(getColumnNameCurrencyID(),"Currency",true,true,"java.lang.Integer","one-to-one","com.idega.block.trade.data.Currency");
      addAttribute(getColumnNameBuyValue(),"Buy value",true,true,Float.class);
      addAttribute(getColumnNameSellValue(),"Sell value",true,true,Float.class);
      addAttribute(getColumnNameMiddleValue(),"Middle value",true,true,Float.class);
      addAttribute(getColumnNameTimestamp(),"Timestamp",true,true,Timestamp.class);
    }

    public void setDefaultValues(){
    }

    public String getIDColumnName(){
      return getColumnNameCurrencyID();
    }

    public static CurrencyValues getStaticInstance(){
      return (CurrencyValues)CurrencyValues.getStaticInstance(sClassName);
    }

    /*  ColumNames begin   */
    public static String getColumnNameCurrencyID(){return Currency.getColumnNameCurrencyID();}
    public static String getColumnNameBuyValue(){return "buy_value";}
    public static String getColumnNameSellValue(){return "sell_value";}
    public static String getColumnNameMiddleValue(){return "middle_value";}
    public static String getColumnNameTimestamp(){return "currency_date";}

    /*  Getters begin   */
    public float getBuyValue(){
      return getFloatColumnValue(getColumnNameBuyValue());
    }

    public float getSellValue() {
      return getFloatColumnValue(getColumnNameSellValue());
    }

    public float getMiddleValue() {
      return getFloatColumnValue(getColumnNameMiddleValue());
    }

    public Timestamp getCurrencyDate() {
      return (Timestamp) getColumnValue(getColumnNameTimestamp());
    }

    /*  Setters begin   */
    public void setBuyValue(float buyValue){
      setColumn(getColumnNameBuyValue(),buyValue);
    }

    public void setSellValue(float sellValue){
      setColumn(getColumnNameSellValue(),sellValue);
    }

    public void setMiddleValue(float middleValue){
      setColumn(getColumnNameMiddleValue(),middleValue);
    }

    public void setCurrencyDate(Timestamp currencyDate){
      setColumn(getColumnNameTimestamp(),currencyDate);
    }

}