package is.idega.idegaweb.campus.block.phone.business;


import com.idega.block.finance.business.FinanceHandler;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.business.AssessmentTariffPreview;
import is.idega.idegaweb.campus.data.ContractAccounts;
import is.idega.idegaweb.campus.block.finance.business.CampusAccountFinder;
import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AccountPhoneEntry;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.TariffKey;
import com.idega.block.building.business.BuildingCacher;
import com.idega.data.EntityBulkUpdater;
import com.idega.data.SimpleQuerier;
import com.idega.util.idegaTimestamp;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.util.Collection;
import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class PhoneFinanceHandler implements FinanceHandler{

  public static float tax = 1.245f;

  int count = 0;
  public PhoneFinanceHandler() {
  }

  public String getAccountType(){
    return Account.typePhone;
  }

  public boolean rollbackAssessment(int iAssessmentRoundId){
    StringBuffer sql = new StringBuffer("update ").append(AccountPhoneEntry.getEntityTableName());
    sql.append(" set ").append(AccountPhoneEntry.getColumnNameAccountEntryId()).append(" = null ");
    sql.append(" , ").append(AccountPhoneEntry.getRoundIdColumnName()).append(" = null ");
    sql.append(" , ").append(AccountPhoneEntry.getColumnNameStatus()).append(" = '").append(AccountPhoneEntry.statusRead).append("'");
    sql.append(" where ").append(AccountPhoneEntry.getRoundIdColumnName()).append(" = ").append(iAssessmentRoundId);
    System.err.println(sql.toString());
    try{
      SimpleQuerier.execute(sql.toString());
      SimpleQuerier.execute(getSQLString(iAssessmentRoundId));
      SimpleQuerier.execute("delete from fin_acc_entry where fin_assessment_round_id = "+iAssessmentRoundId);
      SimpleQuerier.execute("delete from fin_assessment_round where fin_assessment_round_id = "+iAssessmentRoundId);

     return true;
    }
    catch(Exception ex){
     ex.printStackTrace();
    }
    return false;

    /*
    EntityBulkUpdater bulk = new EntityBulkUpdater();
    Hashtable H = new Hashtable();
    Vector V = new Vector();
    if(iAssessmentRoundId > 0){
      AssessmentRound AR = new AssessmentRound();
      try{
        AR = new AssessmentRound(iAssessmentRoundId);

      List L = AccountManager.listOfAccountEntries(AR.getID());

      if(L!=null){
        java.util.Iterator I = L.iterator();
        AccountEntry ae;
        Account a;
        Integer Aid;
        float Amount;
        while(I.hasNext()){
          ae = (AccountEntry) I.next();
          if(ae.getStatus().equals(ae.statusCreated)){
            Amount = ae.getTotal();
            Aid = new Integer(ae.getAccountId());
            if( H.containsKey( Aid ) ){
              a = (Account) H.get(Aid);
            }
            else{
              a = new Account(ae.getAccountId());
              H.put(new Integer(a.getID()),a);
            }
            bulk.add(ae,bulk.delete);
            // lowering the account
            a.addKredit( Amount);
          }
        }
      }
      }
      catch(Exception ex){ ex.printStackTrace();}
      bulk.addAll(H.values(),bulk.update);
      bulk.add(AR,bulk.delete);
      try{
      SimpleQuerier.execute(getSQLString(iAssessmentRoundId));
      bulk.execute();
       return true;
      }
      catch(Exception ex){
       ex.printStackTrace();
      }

    }
    return false;
    */
  }

  public static List listOfContractAccounts(){
   try {
     return com.idega.data.EntityFinder.findAll(new ContractAccounts());
   }
   catch (SQLException ex) {
    ex.printStackTrace();
    return null;
   }
  }

  public Map getMapOfContractsAccountsByPhoneAccountId(List listOfContractAccounts){
    if(listOfContractAccounts!=null){
      Iterator iter = listOfContractAccounts.iterator();
      ContractAccounts conAcc ;
      Hashtable H = new Hashtable(listOfContractAccounts.size());
      while(iter.hasNext()){
        conAcc = (ContractAccounts) iter.next();
        H.put(new Integer(conAcc.getPhoneAccountId()),conAcc);
      }
      return H;
    }
    return null;
  }

  public boolean executeAssessment(int iCategoryId,int iTariffGroupId,String roundName,int iCashierId,int iAccountKeyId,idegaTimestamp paydate,idegaTimestamp start,idegaTimestamp end){
    List listOfAccounts = CampusAccountFinder.listOfContractAccounts(start,end);

    //List listOfAccounts = listOfContractAccounts();
    if(listOfAccounts != null){
      System.err.println("Accounts not null");
      Map M = getMapOfContractsAccountsByPhoneAccountId(listOfAccounts);
      Map E = new HashMap(listOfAccounts.size());
      List entries = FinanceFinder.getInstance().listOfUnBilledPhoneEntries(-1,start,end);

      AccountPhoneEntry ape;
      ContractAccounts accounts;
      if(entries != null){
        System.err.println("Entries not null ");
        AssessmentRound AR = null;

        int iRoundId = -1;
        int iAccountCount = 0;
        try {
            AR = new AssessmentRound();
            AR.setAsNew(roundName);
            AR.setCategoryId(iCategoryId);
            AR.setTariffGroupId(iTariffGroupId);
            AR.setType(getAccountType());
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
            AccountKey AK = new AccountKey(iAccountKeyId);
            TariffKey TK = new TariffKey(AK.getTariffKeyId());
            Integer phAccId;
            Iterator iter = entries.iterator();
            AccountEntry AE;
            while(iter.hasNext()){
              ape = (AccountPhoneEntry) iter.next();
              phAccId = new Integer(ape.getAccountId());
              if(M.containsKey(phAccId)){
                accounts = (ContractAccounts) M.get(phAccId);
                if(E.containsKey(phAccId)){
                  AE = (AccountEntry) E.get(phAccId);
                  AE.setNetto(AE.getNetto()+ape.getPrice());
                }
                else{
                  AE = insertEntry(accounts.getFinanceAccountId(),iRoundId,paydate,ape.getPrice(),AK,TK,iCashierId);
                  E.put(phAccId,AE);
                }
                ape.setAccountEntryId(AE.getID());
                ape.setLastUpdated(idegaTimestamp.getTimestampRightNow());
                ape.setStatus(ape.statusBilled);
                ape.setRoundId(iRoundId);
                ape.update();
              }
            }

            if(E.size() >0){

              Iterator ents = E.entrySet().iterator();
              AccountEntry entry;
              AccountPhoneEntry phoneEntry;
              Map.Entry me;
              Integer AccountId;
              while(ents.hasNext()){
                me = (Map.Entry) ents.next();
                entry = (AccountEntry) me.getValue();
                AccountId = (Integer) me.getKey();
                entry.setTotal(entry.getNetto()*tax);
                phoneEntry = new AccountPhoneEntry();
                phoneEntry.setAccountId(AccountId.intValue());
                phoneEntry.setPrice(-1*entry.getNetto());
                phoneEntry.setRoundId(iRoundId);
                phoneEntry.setAccountEntryId(entry.getID());
                phoneEntry.setStatus(phoneEntry.statusBilled);
                phoneEntry.insert();
                entry.update();
              }
            }
            t.commit();
            return true;
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
          }
        }

      }
      else
        System.err.println("Entries  null ");

    }


    return false;
   }

      /*
      Iterator I = listOfAccounts.iterator();
      ContractAccounts accounts;
      AssessmentRound AR = null;

      int iRoundId = -1;
      int iAccountCount = 0;
      try {
          AR = new AssessmentRound();
          AR.setAsNew(roundName);
          AR.setCategoryId(iCategoryId);
          AR.setTariffGroupId(iTariffGroupId);
          AR.setType(getAccountType());
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
            TariffKey TK = new TariffKey(AK.getTariffKeyId());
            while (I.hasNext()) {
              accounts = (ContractAccounts)I.next();
              totalAmount = 0;
              float Amount = 0;
              List PhoneEntries = FinanceFinder.getInstance().listOfUnBilledPhoneEntries(accounts.getPhoneAccountId(),null,idegaTimestamp.RightNow());
              if(PhoneEntries != null){
                Iterator it = PhoneEntries.iterator();
                AccountEntry AE = insertKreditEntry(accounts.getFinanceAccountId(),iRoundId,paydate,0,AK,TK,iCashierId);
                while(it.hasNext()){
                  ape = (AccountPhoneEntry) it.next();
                  Amount = ape.getPrice();
                  totalAmount += Amount;
                  ape.setAccountEntryId(AE.getID());
                  ape.setLastUpdated(idegaTimestamp.getTimestampRightNow());
                  ape.setStatus(ape.statusBilled);
                  ape.setRoundId(iRoundId);
                  ape.update();
                }
                //System.err.println("totalAmount : "+totalAmount);
                AE.setTotal(totalAmount*tax);
                AE.setNetto(totalAmount);
                AE.update();
                totals += totalAmount;

                iAccountCount++;
              }
              else{
               // System.err.println("no phone entries for account "+accounts.getPhoneAccountId());
              }
            }
            AR.update();
            t.commit();
            return true;
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
          }
        }
    }
    return false;
    */



  public String getSQLString(int iAssessmentRound){
    StringBuffer sql = new StringBuffer("update fin_phone_entry p ");
    sql.append(" set p.fin_acc_entry_id = null ");
    sql.append(" where p.fin_phone_entry_id in ( ");
    sql.append(" select pe.fin_phone_entry_id ");
    sql.append(" from fin_phone_entry pe, fin_acc_entry ae, fin_assessment_round ar ");
    sql.append(" where pe.fin_acc_entry_id = ae.fin_acc_entry_id ");
    sql.append(" and ae.fin_assessment_round_id = ");
    sql.append(iAssessmentRound);
    sql.append(" )");
    //System.err.println(sql.toString());
    return sql.toString();
  }


  public Collection listOfAssessmentTariffPreviews(int iTariffGroupId,idegaTimestamp start,idegaTimestamp end){
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
    return AE.getTotal();
    /*
    System.err.println("totals before"+totals);
    totals = totals + AE.getPrice();
    System.err.println("price"+AE.getPrice());
    System.err.println("totals after"+totals);
    */
  }

  private static AccountEntry insertEntry(int iAccountId,int iRoundId,idegaTimestamp itPaydate,float nettoamount,AccountKey key,TariffKey tkey,int iCashierId) throws SQLException{
    AccountEntry AE = new AccountEntry();
    AE.setAccountId(iAccountId);
    AE.setAccountKeyId(key.getID());
    AE.setCashierId(iCashierId);
    AE.setLastUpdated(idegaTimestamp.getTimestampRightNow());
    AE.setNetto(nettoamount);
    AE.setRoundId(iRoundId);
    AE.setName(tkey.getName());
    AE.setInfo(tkey.getInfo());
    AE.setStatus(AE.statusCreated);
    AE.setPaymentDate(itPaydate.getTimestamp());
    AE.insert();
    return AE;
  }


  public Map getAttributeMap(){
    Hashtable H = new Hashtable(2);
    H.put("a","Mánaðargjald");
    H.put("b","Stofngjald");
    H.put("t","Skattur (%)");

    return H;
  }

   public List listOfAttributes(){
    Vector v = new Vector();
    v.add("a");
    v.add("b");
    v.add("t");
    return v;

  }


}