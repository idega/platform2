package com.idega.block.finance.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.category.data.CategoryEntity;
import com.idega.block.category.data.CategoryEntityBMPBean;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class AccountKeyBMPBean extends CategoryEntityBMPBean implements AccountKey,CategoryEntity {

  public AccountKeyBMPBean() {
    super();
  }
  public AccountKeyBMPBean(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(),"Name",true,true,"java.lang.String");
    addAttribute(getInfoColumnName(),"Info",true,true,"java.lang.String",4000);
    addAttribute(getTariffKeyIdColumnName(),"Tariff key",true,true,"java.lang.Integer");
    addAttribute(getOrdinalColumnName(),"Ordinal",true,true,"java.lang.Integer");
  }

  public static String getAccountKeyEntityName(){return "FIN_ACC_KEY"; }
  public static String getTariffKeyIdColumnName(){return "FIN_TARIFF_KEY_ID";}
  public static String getNameColumnName(){ return "NAME"; }
  public static String getInfoColumnName(){return "INFO";}
  public static String getOrdinalColumnName(){return "ORDINAL";}


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
  
  public Integer getOrdinal(){
      return getIntegerColumnValue(getOrdinalColumnName());
  }
  
  public void setOrdinal(Integer ordinal){
      setColumn(getOrdinalColumnName(),ordinal);
  }
  
  public void setOrdinal(int ordinal){
      setColumn(getOrdinalColumnName(),ordinal);
  }
  
  public Collection ejbFindAll()throws FinderException{
      SelectQuery query = idoSelectQuery();
      query.addOrder(idoQueryTable(),getOrdinalColumnName(),true);
    return super.idoFindPKsByQuery(idoQueryGetSelect());
  }
  
  public Collection ejbFindBySQL(String sql)throws FinderException{
  	return super.idoFindPKsBySQL(sql);
  }
  
  public Collection ejbFindByCategory(Integer categoryID) throws FinderException{
      SelectQuery query = idoSelectQuery();
      query.addCriteria(new MatchCriteria(idoQueryTable(),getColumnCategoryId(),MatchCriteria.EQUALS,categoryID));
      query.addOrder(idoQueryTable(),getOrdinalColumnName(),true);
      return super.idoFindPKsByQuery(query);
  }
  
  public Collection ejbFindByPrimaryKeys(String[] keys) throws FinderException{
      SelectQuery query = idoSelectQuery();
      query.addCriteria(new InCriteria(idoQueryTable(),getIDColumnName(),keys));
      query.addOrder(idoQueryTable(),getOrdinalColumnName(),true);
      return super.idoFindPKsByQuery(query);
  }

}
