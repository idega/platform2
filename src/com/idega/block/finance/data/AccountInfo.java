package com.idega.block.finance.data;

import java.sql.*;
import com.idega.data.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class AccountInfo extends GenericEntity {

  public AccountInfo() {
    super();
  }

  public AccountInfo(int id)throws SQLException{
    super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnAccountId(),"Category",true,true,Integer.class,"",FinanceCategory.class);
  }

  public String getEntityName(){
    return getEntityTableName();
  }

  public static String getEntityTableName(){ return "FIN_ACCT_INFO";}
  public static String getColumnAccountId(){return  "FIN_ACCOUNT_ID";}

  public int getAccountId(){
    return getIntColumnValue( getColumnAccountId() );
  }
  public void setAccountId(int iAccountId){
    setColumn(getColumnAccountId(),iAccountId);
  }
}