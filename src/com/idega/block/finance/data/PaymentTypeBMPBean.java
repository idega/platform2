package com.idega.block.finance.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class PaymentTypeBMPBean extends com.idega.block.category.data.CategoryEntityBMPBean implements com.idega.block.finance.data.PaymentType {

  public PaymentTypeBMPBean() {
    super();
  }
  public PaymentTypeBMPBean(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnName(),"Name",true,true,"java.lang.String");
    addAttribute(getColumnInfo(),"Info",true,true,"java.lang.String",4000);
    addAttribute(getColumnPayments(),"Payments",true,true,"java.lang.Integer");
    addAttribute(getColumnAmountCost(),"Amount Cost",true,true,"java.lang.Float");
    addAttribute(getColumnPercentCost(),"Percent cost",true,true,"java.lang.Float");
  }

  public static String getEntityTableName(){return "FIN_PMT_TYPE"; }
  public static String getColumnTariffKeyId(){return "FIN_TARIFF_KEY_ID";}
  public static String getColumnName(){ return "NAME"; }
  public static String getColumnInfo(){return "INFO";}
  public static String getColumnPayments(){return "PMTS";}
  public static String getColumnAmountCost(){return "AMT_CST";}
  public static String getColumnPercentCost(){return "PCT_CST";}


  public String getEntityName() {
    return getEntityTableName();
  }
  public int getTariffKeyId(){
    return getIntColumnValue(getColumnTariffKeyId());
  }
  public void setTariffKeyId(int id){
    setColumn(getColumnCategoryId(),id);
  }
  public String getName(){
    return getStringColumnValue(getColumnName());
  }
  public void setName(String name){
    setColumn(getColumnName(), name);
  }
  public String getInfo(){
    return getStringColumnValue(getColumnInfo());
  }
  public void setInfo(String extra_info){
    setColumn(getColumnInfo(), extra_info);
  }

  public int getPayments(){
    return getIntColumnValue( getColumnPayments() );
  }
  public void setPayments(int payments){
    setColumn(getColumnPayments(),payments);
  }
  public int getAmountCost(){
    return getIntColumnValue(getColumnAmountCost() );
  }
  public void setAmountCost(float cost){
    setColumn(getColumnAmountCost(),cost);
  }
  public int getPercentCost(){
    return getIntColumnValue( getColumnPercentCost() );
  }
  public void setPercentCost(float cost){
    setColumn(getColumnPercentCost(),cost);
  }
  
  public Collection ejbFindAll()throws FinderException{
  	return super.idoFindAllIDsBySQL();
  }
}
