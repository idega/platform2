package com.idega.block.finance.business;

import com.idega.block.finance.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.data.genericentity.Member;
import java.sql.SQLException;
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

  public static Account[] findAccounts(int iMemberId){
    Account[] A;
    try{
       A = (Account[])new Account().findAllByColumn("member_id",iMemberId);
    }
    catch(Exception e){A=null;}
    return A;
  }

  public  static Account makeNewAccount(int iMemberId, String sName,String sExtra, int iCashierId){
    Account A = new Account();
    A.setBalance(0);
    A.setCreationDate(idegaTimestamp.getTimestampRightNow() );
    A.setLastUpdated(idegaTimestamp.getTimestampRightNow()) ;
    A.setMemberId(iMemberId);
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