package com.idega.block.finance.data;

import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class AccountTypeBMPBean extends com.idega.block.category.data.CategoryEntityBMPBean implements com.idega.block.finance.data.AccountType {

  public AccountTypeBMPBean() {
    super();
  }

  public AccountTypeBMPBean(int id)throws SQLException{
    super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(),"Name",true,true,java.lang.String.class);
    addAttribute(getInfoColumnName(),"Info",true,true,java.lang.String.class);
  }

  public String getEntityName(){
    return getEntityTableName();
  }

  public static String getEntityTableName(){ return "FIN_ACCT_TYPE";}
  public static String getNameColumnName(){ return "NAME";}
  public static String getInfoColumnName(){ return "INFO";}

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
}
