package is.idega.idegaweb.campus.block.phone.business;

import is.idega.idegaweb.campus.block.finance.business.CampusAccountFinder;
import com.idega.block.finance.business.FinanceHandler;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.business.AssessmentTariffPreview;
import is.idega.idegaweb.campus.data.ContractAccounts;
import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.finance.data.AccountPhoneEntry;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.building.business.BuildingCacher;
import com.idega.data.EntityBulkUpdater;
import com.idega.data.SimpleQuerier;
import com.idega.util.idegaTimestamp;
import java.util.Map;
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
  int count = 0;
  public PhoneFinanceHandler() {
  }

  public String getAccountType(){
    return Account.typePhone;
  }

  public boolean rollbackAssessment(int iAssessmentRoundId){
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
  }


  public boolean executeAssessment(int iCategoryId,int iTariffGroupId,String roundName,int iCashierId,int iAccountKeyId,idegaTimestamp paydate){
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
          //AR.setCategoryId(iCategoryId);
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
                eFinanceAccount.setBalance(eFinanceAccount.getBalance()+AE.getTotal());
                //System.err.print(eFinanceAccount.getBalance());
                //System.err.print("+"+AE.getPrice());
                eFinanceAccount.setLastUpdated(idegaTimestamp.getTimestampRightNow());

                eFinanceAccount.update();
                //System.err.println("="+eFinanceAccount.getBalance());
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

  }

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
    System.err.println(sql.toString());
    return sql.toString();
  }


  public Collection listOfAssessmentTariffPreviews(int iTariffGroupId){
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