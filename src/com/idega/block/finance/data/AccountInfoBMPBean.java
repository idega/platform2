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

public class AccountInfoBMPBean extends com.idega.data.GenericEntity implements com.idega.block.finance.data.AccountInfo,com.idega.block.finance.data.FinanceAccount {

  /*
  CREATE VIEW "FIN_ACCOUNT_INFO" (
  "ACCOUNT_ID",
  "CATEGORY_ID",
  "USER_ID",
  "NAME",
  "LAST_UPDATED",
  "BALANCE",
  "ACCOUNT_TYPE",
) AS

select
ac.fin_account_id account_id,
ac.fin_category_id category_id,
ac.ic_user_id user_id,
ac.name,
max(e.last_updated) last_updated,
sum(e.total ) balance,
ac.account_type
from fin_account ass,fin_acc_entry e
where e.fin_account_id = ass.fin_account_id
group by
ac.fin_account_id,
ac.fin_category_id ,
ac.ic_user_id,
ac.name,
as.account_type
*/

  public AccountInfoBMPBean() {

  }

  public AccountInfoBMPBean(int id)throws SQLException{

  }

  public static String getEntityTableName(){ return "FIN_ACCOUNT_INFO";}
  public static String getColumnAccountId(){return  "ACCOUNT_ID";}
  public static String getColumnCategoryId(){return  "CAT_ID";}
  public static String getColumnUserId(){ return "USER_ID";}
  public static String getColumnCashierId(){ return "CASHIER_ID";}
  public static String getColumnName(){ return "NAME";}
  public static String getColumnLastUpdated(){ return "LAST_UPDATED";}
  public static String getColumnBalance(){ return "BALANCE";}
  public static String getColumnType(){return "ACCOUNT_TYPE";}

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnAccountId(),"Accountid",true,true,Integer.class);
    addAttribute(getColumnCategoryId(),"Category",true,true,Integer.class);
    addAttribute(getColumnUserId(),"UserId",true,true,Integer.class);
    addAttribute(getColumnCashierId(),"Cashier",true,true,Integer.class);
    addAttribute(getColumnName(),"Name",true,true,String.class);
    addAttribute(getColumnLastUpdated(),"LastUpdated",true,true,Timestamp.class);
    addAttribute(getColumnBalance(),"Balance",true,true,Float.class);
    addAttribute(getColumnType(),"Type",true,true,String.class);
  }

  public String getEntityName(){
    return getEntityTableName();
  }

  public int getAccountId(){
    return getIntColumnValue( getColumnAccountId() );
  }
  public int getCategoryId(){
    return getIntColumnValue( getColumnCategoryId() );
  }
   public int getUserId(){
    return getIntColumnValue( getColumnUserId() );
  }
   public int getCashierId(){
    return getIntColumnValue( getColumnCashierId() );
  }
  public String getName(){
    return getStringColumnValue(getColumnName());
  }
  public String getAccountType(){
    return getStringColumnValue(getColumnType());
  }
  public Timestamp getLastUpdated(){
    return (Timestamp) getColumnValue(getColumnLastUpdated());
  }
  public float getBalance(){
    return getFloatColumnValue(getColumnBalance());
  }
  public String getAccountName(){
    return getName();
  }

}
