package com.idega.block.finance.business;

import com.idega.block.finance.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.data.genericentity.Member;
import com.idega.core.user.data.User;
import java.sql.SQLException;
import com.idega.data.EntityFinder;
import java.util.List;
/**
 * Title:        AccountManager
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class AccountManager {

  private Account eAccount;
  private Member eMember;

  public AccountManager() {

  }

  public static Account[] findAccounts(int iUserId){
    Account[] A;
    try{
       A = (Account[])new Account().findAllByColumn("ic_user_id",iUserId);
    }
    catch(Exception e){A=null;}
    return A;
  }

  public static List listOfAccounts(int iUserId){
    List A = null;
    try{
       A = EntityFinder.findAllByColumn(new Account(),"ic_user_id",iUserId);
    }
    catch(Exception e){A=null;}
    return A;
  }

  public  static Account makeNewAccount(int iUserId, String sName,String sExtra, int iCashierId){
    Account A = new Account();
    A.setBalance(0);
    A.setCreationDate(idegaTimestamp.getTimestampRightNow() );
    A.setLastUpdated(idegaTimestamp.getTimestampRightNow()) ;
    A.setUserId(iUserId);
    A.setName(sName) ;
    A.setExtraInfo(sExtra);
    A.setCashierId(iCashierId);
    A.setValid(true);
    try{
      A.insert();
    }
    catch(SQLException sql){A = null;}
    return A;
  }
}