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

public class TariffGroupBMPBean extends com.idega.block.category.data.CategoryEntityBMPBean implements com.idega.block.finance.data.TariffGroup {

  public TariffGroupBMPBean() {
    super();
  }
  public TariffGroupBMPBean(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());

    addAttribute(getColumnHandlerId(),"Handler",true,true,Integer.class,"",FinanceHandlerInfo.class);
    addAttribute(getColumnName(),"Name",true,true,String.class);
    addAttribute(getColumnInfo(),"Info",true,true,String.class);
    addAttribute(getColumnGroupDate(),"Group date",true,true,java.sql.Date.class);
    addAttribute(getColumnUseIndex(),"Use Indexes",true,true,Boolean.class);

  }

  public String getEntityName(){
    return getEntityTableName();
  }

  public static String getEntityTableName(){return  "FIN_TARIFF_GROUP";}
  public static String getColumnHandlerId(){return  "FIN_HANDLER_ID";}
  public static String getColumnGroupDate(){return "GROUP_DATE";}
  public static String getColumnInfo(){return "INFO";}
  public static String getColumnName(){return "NAME";}
  public static String getColumnUseIndex(){return "USE_INDEX";}

  public String getName(){
    return getStringColumnValue( getColumnName());
  }
  public void setName(String Name){
    setColumn(getColumnName(),Name);
  }
  public String getInfo(){
    return getStringColumnValue( getColumnInfo() );
  }
  public void setInfo(String info){
    setColumn(getColumnInfo(),info);
  }

  public int getHandlerId(){
    return getIntColumnValue( getColumnHandlerId() );
  }
  public void setHandlerId(int handlerId){
    setColumn(getColumnHandlerId(),handlerId);
  }
  public java.sql.Date getGroupDate(){
    return (java.sql.Date) getColumnValue(getColumnGroupDate());
  }
  public void setGroupDate(java.sql.Date date){
    setColumn(getColumnGroupDate(),date);
  }

  public boolean getUseIndex(){
    return getBooleanColumnValue(getColumnUseIndex());
  }
  public void setUseIndex(boolean use){
    setColumn(getColumnUseIndex(),use);
  }
  
  public Collection ejbFindByCategory(Integer categoryID)throws FinderException{
  		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getColumnCategoryId(),categoryID));
  }
  
  public Collection ejbFindByCategoryWithouthHandlers(Integer categoryID)throws FinderException{
  		return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getColumnCategoryId(),categoryID).appendAndIsNull(getColumnHandlerId()));
  }

}
