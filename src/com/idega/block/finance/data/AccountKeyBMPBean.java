package com.idega.block.finance.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.category.data.CategoryEntityBMPBean;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class AccountKeyBMPBean extends CategoryEntityBMPBean implements AccountKey,com.idega.block.finance.business.Key {

  public AccountKeyBMPBean() {
    super();
  }
  public AccountKeyBMPBean(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(),"Heiti",true,true,"java.lang.String");
    addAttribute(getInfoColumnName(),"Lýsing",true,true,"java.lang.String",4000);
    addAttribute(getTariffKeyIdColumnName(),"Lykill",true,true,"java.lang.Integer");
  }

  public static String getAccountKeyEntityName(){return "FIN_ACC_KEY"; }
  public static String getTariffKeyIdColumnName(){return "FIN_TARIFF_KEY_ID";}
  public static String getNameColumnName(){ return "NAME"; }
  public static String getInfoColumnName(){return "INFO";}


  public String getEntityName() {
    return getAccountKeyEntityName();
  }
  public int getTariffKeyId(){
    return getIntColumnValue(getTariffKeyIdColumnName());
  }
  public void setTariffKeyId(int id){
    setColumn(getTariffKeyIdColumnName(),id);
  }
  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }
  public void setName(String name){
    setColumn(getNameColumnName(), name);
  }
  public String getInfo(){
    return getStringColumnValue(getInfoColumnName());
  }
  public void setInfo(String extra_info){
    setColumn(getInfoColumnName(), extra_info);
  }
  
  public Collection ejbFindAll()throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect());
  }
  
  public Collection ejbFindBySQL(String sql)throws FinderException{
  	return super.idoFindPKsBySQL(sql);
  }
  
  public Collection ejbFindByCategory(Integer categoryID) throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getColumnCategoryId(),categoryID));
  }
}
