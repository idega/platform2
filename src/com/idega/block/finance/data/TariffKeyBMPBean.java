package com.idega.block.finance.data;

import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.category.data.CategoryEntity;
import com.idega.block.category.data.CategoryEntityBMPBean;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class TariffKeyBMPBean extends CategoryEntityBMPBean implements TariffKey,CategoryEntity {

  public TariffKeyBMPBean() {
    super();
  }
  public TariffKeyBMPBean(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnName(),"Name",true,true,"java.lang.String");
    addAttribute(getColumnInfo(),"Info",true,true,"java.lang.String",4000);
  }
  public static String getEntityTableName(){return "FIN_TARIFF_KEY"; }
  public static String getColumnName(){ return "NAME"; }
  public static String getColumnInfo(){return "INFO";}

  public String getEntityName() {
    return getEntityTableName();
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
  public void setInfo(String info){
    setColumn(getColumnInfo(), info);
  }
  
  public Collection ejbFindByCategory(Integer categoryID)throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getColumnCategoryId(),categoryID));
  }
  
  public Collection ejbFindAll()throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect());
  }
}
