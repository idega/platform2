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

public class CashierBMPBean extends com.idega.data.GenericEntity implements com.idega.block.finance.data.Cashier {

  public CashierBMPBean() {
    super();
  }
  public CashierBMPBean(int id) throws SQLException {
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getUserIdColumnName(),"User Id",true,true,"java.lang.Integer","one-to-one","com.idega.core.user.data.User");
  }

  public static String getEntityTableName(){return "FIN_CASHIER"; }
  public static String getUserIdColumnName(){ return "IC_USER_ID"; }

  public String getEntityName() {
    return getEntityTableName();
  }
  public int getUserId(){
    return getIntColumnValue(getUserIdColumnName());
  }
  public void setUserId(int id){
    setColumn(getUserIdColumnName(),id);
  }
}
