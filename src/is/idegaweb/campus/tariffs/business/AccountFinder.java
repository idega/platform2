package is.idegaweb.campus.tariffs.business;

import java.util.Map;
import java.util.List;
import java.util.Hashtable;
import java.util.Iterator;
import com.idega.data.EntityFinder;
import com.idega.block.finance.data.Account;
import is.idegaweb.campus.allocation.business.ContractFinder;
import is.idegaweb.campus.entity.*;
import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class AccountFinder  {

  public AccountFinder() {
  }

  public static List listOfAccountsInUse(String status){
    StringBuffer sql = new StringBuffer("select acc.* ");
    sql.append(" from fin_account acc,cam_contract con");
    sql.append(" where con.ic_user_id = acc.ic_user_id");
    sql.append(" and con.status = '");
    sql.append(status);
    sql.append("'");
    try{
      return  EntityFinder.findAll(new Account(),sql.toString());
    }
    catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }



  public static Map mapOfAccountsInUseByApartmentId(){
    Hashtable H = null;
    Map M = ContractFinder.mapOfStatusContractUsersByUserId(Contract.statusSigned);
    List L = listOfAccountsInUse(Contract.statusSigned);
    if(L != null && M!= null){
      int len = L.size();
      H = new Hashtable(len);
      Iterator I = L.iterator();
      Account A;
      Contract C;
      Integer ID;
      while(I.hasNext()){
        A = (Account) I.next();
        ID = new Integer(A.getUserId());
        if(M.containsKey(ID)){
          C = (Contract) M.get(ID);
          H.put(C.getApartmentId(),A);
        }
      }
    }
    return H;
  }

}