package is.idega.idegaweb.campus.block.finance.business;


import com.idega.block.finance.business.*;
import com.idega.block.finance.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.data.SimpleQuerier;
import is.idega.idegaweb.campus.exception.*;
import is.idega.idegaweb.campus.data.ContractAccountApartment;
import is.idega.idegaweb.campus.data.ContractAccounts;
import java.sql.SQLException;
import java.util.List;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class CampusAssessmentBusiness  {

  public static final char cComplex = 'x';
  public static final char cAll = 'a';
  public static final char cBuilding = 'b';
  public static final char cFloor = 'f';
  public static final char cCategory = 'c';
  public static final char cType = 't';
  public static final char cApartment = 'p';

  public CampusAssessmentBusiness() {
  }

  public static void rollBackAssessment(int AssessmentRoundId) throws RollBackException{
    if(AssessmentRoundId > 0){
      javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

      try{
         t.begin();
         AssessmentRound AR = new AssessmentRound(AssessmentRoundId);
         List L = AccountManager.listOfAccountEntries(AR.getID());
         Hashtable H = new Hashtable();
         Vector V = new Vector();
        if(L!=null){
          Iterator I = L.iterator();
          AccountEntry ae;
          Account a;
          Integer Aid;
          float Amount;
          while(I.hasNext()){
            ae = (AccountEntry) I.next();
            Amount = ae.getPrice();
            Aid = new Integer(ae.getAccountId());
            if(!ae.getStatus().equals(ae.statusCreated))
              throw new SQLException("Billed Entries");
            ae.delete();
            if( H.containsKey( Aid ) ){
              a = (Account) H.get(Aid);
            }
            else{
              a = new Account(ae.getAccountId());
            }
            // lowering the account
            a.addKredit( Amount);
            V.add(a);
          }
          Iterator hi = V.iterator();
          while(hi.hasNext()){
            a = (Account) hi.next();
            a.update();
          }
        }
         AR.delete();
       t.commit();
      }
      catch(Exception e) {
        try {
          t.rollback();
        }
        catch(javax.transaction.SystemException ex) {
          ex.printStackTrace();
        }
        //e.printStackTrace();
        throw new RollBackException();
      }
    }
  }

  public static AssessmentRound assessFinance(idegaTimestamp paydate,String roundName,String accountType,int iCashierId)throws CampusFinanceException{

    List listOfTariffs = Finder.listOfTariffs();
    List listOfUsers = CampusAccountFinder.listOfRentingUserAccountsByType(accountType);
    int iAccountCount = 0;
    if(listOfTariffs !=null){
      if(listOfUsers!=null){
        int rlen = listOfUsers.size();
        int tlen = listOfTariffs.size();
        Tariff eTariff;
        char cAttribute;
        ContractAccountApartment user;
        Vector vEntries = new Vector();
        int iAttributeId = -1;
        int iRoundId  = -1;
        AssessmentRound AR = null;
        try {
          AR = new AssessmentRound();
          AR.setAsNew(roundName);
          AR.setType(Account.typeFinancial);
          AR.insert();
          iRoundId = AR.getID();
        }
        catch (SQLException ex) {
          ex.printStackTrace();
          try {
            AR.delete();
          }
          catch (SQLException ex2) {
            ex2.printStackTrace();
            AR = null;
          }
        }

        if(AR != null){
        javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

        try{
          t.begin();
          int totals = 0;
           int totalAmount = 0;
          // All tenants accounts (Outer loop)
          for(int o = 0; o < rlen ; o++){
            user = (ContractAccountApartment)listOfUsers.get(o);
            Account eAccount = new Account(user.getAccountId());
            totalAmount = 0;
            float Amount = 0;
            // For each tariff (Inner loop)
            for (int i=0; i < tlen ;i++ ) {
              Amount = 0;
              eTariff = (Tariff) listOfTariffs.get(i);
              String sAttribute = eTariff.getTariffAttribute();
              // If we have an tariff attribute
              if(sAttribute != null){
                iAttributeId = -1;
                cAttribute = sAttribute.charAt(0);
                // If All
                if(cAttribute == cAll){
                  Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                }
                // other than all
                else{
                  // attribute check
                  if(sAttribute.length() >= 3){
                  iAttributeId = Integer.parseInt(sAttribute.substring(2));
                    switch (cAttribute) {
                      case cType: // Apartment type
                        if(iAttributeId == user.getApartmentTypeId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                      case cCategory  : // Apartment category
                        if(iAttributeId == user.getApartmentCategoryId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                      case cBuilding  : // Building
                        if(iAttributeId == user.getBuildingId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                      case cFloor     : // Floor
                        if(iAttributeId == user.getFloorId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                      case cComplex   : // Complex
                        if(iAttributeId == user.getComplexId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                      case cApartment : // Apartment
                        if(iAttributeId == user.getApartmentId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                    }// switch
                  } // attribute check
                }// other than all
                if(sAttribute.length() >= 3){
                  iAttributeId = Integer.parseInt(sAttribute.substring(2));
                }
                totalAmount += Amount;

              }
            } // Inner loop block
            totals += totalAmount*-1;
            eAccount.setBalance(eAccount.getBalance()+totalAmount);
            eAccount.setLastUpdated(idegaTimestamp.getTimestampRightNow());
            eAccount.update();
            iAccountCount++;
          } // Outer loop block
          AR.setTotals((float)(totals));
          AR.setAccountCount(iAccountCount);
          AR.update();
          t.commit();
          return AR;
        } // Try block
        catch(Exception e) {
          try {
            t.rollback();
          }
          catch(javax.transaction.SystemException ex) {
            ex.printStackTrace();
          }
          e.printStackTrace();
          throw new CampusFinanceException();
        }
        }
      }
      else
        throw new CampusFinanceException("No Users :");
    }
    else
      throw new CampusFinanceException("No Tariffs :");

    return null;
  }

  public static AssessmentRound assessPhones(idegaTimestamp paydate,String roundName,String accountType,int iAccountKeyId,int iCashierId)throws CampusFinanceException{
    //List listOfUsers = CampusAccountFinder.listOfRentingUserAccountsByType(accountType);
    List listOfAccounts = CampusAccountFinder.listOfContractAccounts();
    if(listOfAccounts != null){
      //System.err.println("phoneaccounts :"+listOfUsers.size());

      Iterator I = listOfAccounts.iterator();
      ContractAccounts accounts;
      AssessmentRound AR = null;

      int iRoundId = -1;
      int iAccountCount = 0;
      try {
          AR = new AssessmentRound();
          AR.setAsNew(roundName);
          AR.setType(Account.typePhone);
          AR.insert();
          iRoundId = AR.getID();
        }
        catch (SQLException ex) {
          ex.printStackTrace();
          try {
            AR.delete();
          }
          catch (SQLException ex2) {
            ex2.printStackTrace();
            AR = null;
          }
        }

        if(AR != null){
          javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

          try{
            t.begin();
            int totals = 0;
            int totalAmount = 0;
            // All tenants accounts (Outer loop)
            AccountPhoneEntry ape;
            AccountKey AK = new AccountKey(iAccountKeyId);
            while (I.hasNext()) {
              accounts = (ContractAccounts)I.next();
              Account eFinanceAccount = new Account(accounts.getPhoneAccountId());
              Account ePhoneAccount = new Account(accounts.getPhoneAccountId());
              totalAmount = 0;
              float Amount = 0;
              List PhoneEntries = AccountManager.listOfUnBilledPhoneEntries(accounts.getPhoneAccountId(),null,idegaTimestamp.RightNow());
              if(PhoneEntries != null){
                Iterator it = PhoneEntries.iterator();
                AccountEntry AE = insertKreditEntry(accounts.getFinanceAccountId(),iRoundId,paydate,0,AK,iCashierId);
                while(it.hasNext()){
                  ape = (AccountPhoneEntry) it.next();
                  Amount = ape.getPrice();
                  totalAmount += Amount;
                  ape.setAccountEntryId(AE.getID());
                  ape.setLastUpdated(idegaTimestamp.getTimestampRightNow());
                  ape.update();
                }
                AE.setPrice(totalAmount);
                AE.update();
                totals += totalAmount;
                eFinanceAccount.setBalance(eFinanceAccount.getBalance()+AE.getPrice());
                System.err.print(eFinanceAccount.getBalance());
                System.err.print("+"+AE.getPrice());
                eFinanceAccount.setLastUpdated(idegaTimestamp.getTimestampRightNow());

                eFinanceAccount.update();
                System.err.println("="+eFinanceAccount.getBalance());
                iAccountCount++;
              }
              else{
               // System.err.println("no phone entries for account "+accounts.getPhoneAccountId());
              }
            }
            AR.setTotals((float)(totals));
            AR.setAccountCount(iAccountCount);
            AR.update();
            t.commit();
            return AR;
          }
          catch(Exception e) {
            try {
              t.rollback();
            }
            catch(javax.transaction.SystemException ex) {
              ex.printStackTrace();
            }
            try {
              AR.delete();
            }
            catch (Exception ex2) {
              ex2.printStackTrace();

            }
            e.printStackTrace();
            throw new CampusFinanceException();
          }
        }
    }
    return null;

  }

  private static float insertEntry(Vector V,Tariff T,int iAccountId,int iRoundId,idegaTimestamp itPaydate,int iCashierId)
  throws SQLException{
    AccountEntry AE = new AccountEntry();
    AE.setAccountId(iAccountId);
    AE.setAccountKeyId(T.getAccountKeyId());
    AE.setCashierId(iCashierId);
    AE.setLastUpdated(idegaTimestamp.getTimestampRightNow());
    AE.setPrice(-T.getPrice());
    AE.setRoundId(iRoundId);
    AE.setName(T.getName());
    AE.setInfo(T.getInfo());
    AE.setStatus(AE.statusCreated);
    AE.setCashierId(1);
    AE.setPaymentDate(itPaydate.getTimestamp());
    AE.insert();
    if(V!=null)
      V.add(AE);
    return AE.getPrice();
    /*
    System.err.println("totals before"+totals);
    totals = totals + AE.getPrice();
    System.err.println("price"+AE.getPrice());
    System.err.println("totals after"+totals);
    */
  }

  private static AccountEntry insertKreditEntry(int iAccountId,int iRoundId,idegaTimestamp itPaydate,float amount,AccountKey key,int iCashierId) throws SQLException{
    AccountEntry AE = new AccountEntry();
    AE.setAccountId(iAccountId);
    AE.setAccountKeyId(key.getID());
    AE.setCashierId(iCashierId);
    AE.setLastUpdated(idegaTimestamp.getTimestampRightNow());
    AE.setPrice(-amount);
    AE.setRoundId(iRoundId);
    AE.setName(key.getName());
    AE.setInfo(key.getInfo());
    AE.setStatus(AE.statusCreated);
    AE.setPaymentDate(itPaydate.getTimestamp());
    AE.insert();
    return AE;
  }

  public static void groupEntriesWithSQL(idegaTimestamp from, idegaTimestamp to) throws CampusFinanceException{

    try{
     ///////////////////////////
      AccountEntry ae = (AccountEntry)AccountEntry.getStaticInstance(AccountEntry.class);
      EntryGroup EG = null;
      int gid = -1;
      try {
        EG = new EntryGroup();
        EG.setGroupDate(idegaTimestamp.RightNow().getSQLDate());
        EG.insert();
        gid = EG.getID();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
        EG = null;
      }

      if(EG !=null){
        StringBuffer sql = new StringBuffer("update ");
        sql.append(ae.getEntityTableName());
        sql.append(" set ");
        sql.append(ae.getEntryGroupIdColumnName());
        sql.append(" = ");
        sql.append(gid);
        sql.append(" where ");
        sql.append(ae.getEntryGroupIdColumnName());
        sql.append(" is null ");
        if(from !=null){
          sql.append(" and last_updated >= ");
          sql.append(from.getSQLDate());
        }
        if(to !=null){
          sql.append(" and last_updated <= ");
          sql.append('\'');
          sql.append(to.getSQLDate());
          sql.append(" 23:59:59'");
        }

        String where = " where "+ae.getEntryGroupIdColumnName()+" = "+gid;
        String sMinSql = "select min("+ae.getIDColumnName()+") from "+ae.getEntityTableName()+where;
        String sMaxSql = "select max("+ae.getIDColumnName()+") from "+ae.getEntityTableName()+where;
        //System.err.println(sql.toString());
        //System.err.println(sMinSql);
        System.err.println(sMaxSql);
        SimpleQuerier.execute(sql.toString());
        String[] mi = SimpleQuerier.executeStringQuery(sMinSql);
        String[] ma = SimpleQuerier.executeStringQuery(sMaxSql);
        if(mi!=null && mi.length > 0){
          EG.setEntryIdFrom(new Integer(mi[0]).intValue());
        }
        if(ma!=null && ma.length > 0){
          EG.setEntryIdTo(new Integer(ma[0]).intValue());
        }
        EG.update();
      }
      else throw new CampusFinanceException();
     ///////////////////////////

    }
    catch(Exception e) {

      e.printStackTrace();
      throw new CampusFinanceException();
    }


  }

  public static void groupEntries(idegaTimestamp from, idegaTimestamp to) throws CampusFinanceException{
    List L = Finder.listOfFinanceEntriesWithoutGroup(from,to);
    if(L!=null){
      int min = 0,max = 0;
      EntryGroup EG = null;
      try {
        EG = new EntryGroup();
        EG.setGroupDate(idegaTimestamp.RightNow().getSQLDate());
        EG.insert();
        int gid = EG.getID();
        //System.err.println(" gid "+gid);
      }
      catch (Exception ex) {
        ex.printStackTrace();
        try {
            EG.delete();
          }
          catch (SQLException ex2) {
            ex2.printStackTrace();
            EG = null;
          }
           throw new CampusFinanceException();
      }

      if(EG !=null){
        javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

        try{
         t.begin();
         ////////////////////////
          Iterator It = L.iterator();

          AccountEntry AE;
          int aeid = 0;
          AE = (AccountEntry) It.next();
          aeid = AE.getID();
          min = aeid;
          max = aeid;

          AE.setEntryGroupId(EG.getID());
          AE.update();

          while(It.hasNext()){
            AE = (AccountEntry) It.next();
            aeid = AE.getID();
            min = aeid < min ? aeid : min ;
            max = aeid > min ? aeid : max ;
            AE.setEntryGroupId(EG.getID());
            AE.update();

          }

          EG.setEntryIdFrom(min);
          EG.setEntryIdTo(max);
          EG.update();
        //////////////////////////////
         t.commit();

        }
        catch(Exception e) {
          try {
            t.rollback();

          }
          catch(javax.transaction.SystemException ex) {
            ex.printStackTrace();
          }
          try {
              EG.delete();
          }
          catch (SQLException ex) {

          }
          e.printStackTrace();
          throw new CampusFinanceException();
        }
      }//if EG null
    }
  }

  public static int getGroupEntryCount(EntryGroup entryGroup){
    int count = 0;
    if(entryGroup !=null ){
      StringBuffer sql = new StringBuffer("select count(*) from ");
      sql.append(AccountEntry.getEntityTableName());
      sql.append(" where ");
      sql.append(AccountEntry.getEntryGroupIdColumnName());
      sql.append(" = ");
      sql.append(entryGroup.getID());
      //System.err.println(sql.toString());
      try {
        count = entryGroup.getNumberOfRecords(sql.toString());
      }
      catch (SQLException ex) {
        ex.printStackTrace();
        count = 0;
      }
    }
    return count;
  }

  public static void updateAllAccounts(){
    String sql = "update fin_account f set f.balance = (select sum(price) from fin_acc_entry  f2 where f2.fin_account_id = f.fin_account_id)";
    try {

      SimpleQuerier.execute(sql);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
