package is.idega.idegaweb.campus.block.finance.business;

import com.idega.block.finance.business.FinanceHandler;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.business.AssessmentTariffPreview;
import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.AssessmentRound;
import com.idega.block.building.business.BuildingCacher;
import com.idega.util.idegaTimestamp;
import is.idega.idegaweb.campus.data.ContractAccountApartment;
import com.idega.data.EntityBulkUpdater;
import java.util.Map;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
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

public class CampusFinanceHandler implements FinanceHandler{
  int count = 0;
  public CampusFinanceHandler() {
  }

  public String getAccountType(){
    return Account.typeFinancial;
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
      bulk.execute();
    }
    return false;
  }

  public boolean executeAssessment(int iCategoryId,int iTariffGroupId,String roundName,int iCashierId,int iAccountKeyId,idegaTimestamp paydate){
    List listOfTariffs = FinanceFinder.listOfTariffs(iTariffGroupId);
    List listOfUsers = CampusAccountFinder.listOfRentingUserAccountsByType(getAccountType());
    //Map mapOfContracts = ContractFinder.mapOfApartmentUsersBy();
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
          //AR.setCategoryId(iCategoryId);
          AR.setTariffGroupId(iTariffGroupId);
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
                if(cAttribute == BuildingCacher.CHARALL){
                  Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                }
                // other than all
                else{
                  // attribute check
                  if(sAttribute.length() >= 3){
                  iAttributeId = Integer.parseInt(sAttribute.substring(2));
                    switch (cAttribute) {
                      case BuildingCacher.CHARTYPE: // Apartment type
                        if(iAttributeId == user.getApartmentTypeId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                      case BuildingCacher.CHARCATEGORY  : // Apartment category
                        if(iAttributeId == user.getApartmentCategoryId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                      case BuildingCacher.CHARBUILDING  : // Building
                        if(iAttributeId == user.getBuildingId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                      case BuildingCacher.CHARFLOOR     : // Floor
                        if(iAttributeId == user.getFloorId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                      case BuildingCacher.CHARCOMPLEX : // Complex
                        if(iAttributeId == user.getComplexId())
                          Amount = insertEntry(vEntries,eTariff,user.getAccountId(),iRoundId,paydate,iCashierId);
                      break;
                      case BuildingCacher.CHARAPARTMENT : // Apartment
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
          return true;
        } // Try block
        catch(Exception e) {
          try {
            t.rollback();
          }
          catch(javax.transaction.SystemException ex) {
            ex.printStackTrace();
          }
          e.printStackTrace();

        }
        }
      }
    }
    return false;
  }


  public Collection listOfAssessmentTariffPreviews(int iTariffGroupId){
    List listOfTariffs = FinanceFinder.listOfTariffs(iTariffGroupId);
    List listOfUsers = CampusAccountFinder.listOfRentingUserAccountsByType(getAccountType());

    if(listOfTariffs !=null && listOfUsers!=null){
      Hashtable H = new Hashtable(listOfTariffs.size());
      int rlen = listOfUsers.size();
      int tlen = listOfTariffs.size();
      Tariff eTariff;
      char cAttribute;
      ContractAccountApartment user;

      int iAttributeId = -1;
      String sAttribute;

      // All tenants accounts (Outer loop)
      for(int o = 0; o < rlen ; o++){
        user = (ContractAccountApartment)listOfUsers.get(o);
        // For each tariff (Inner loop)
        for (int i=0; i < tlen ;i++ ) {
          eTariff = (Tariff) listOfTariffs.get(i);
          sAttribute = eTariff.getTariffAttribute();
          // If we have an tariff attribute
          if(sAttribute != null){
            iAttributeId = -1;
            cAttribute = sAttribute.charAt(0);
            //System.err.println("att "+String.valueOf(cAttribute));
            // If All
            if(cAttribute == BuildingCacher.CHARALL){
              addAmount(H,eTariff);
            }
            // other than all
            else{
              // attribute check
              if(sAttribute.length() >= 3){
              iAttributeId = Integer.parseInt(sAttribute.substring(2));
                switch (cAttribute) {
                  case BuildingCacher.CHARTYPE: // Apartment type
                    if(iAttributeId == user.getApartmentTypeId())
                      addAmount(H,eTariff);
                  break;
                  case BuildingCacher.CHARCATEGORY  : // Apartment category
                    if(iAttributeId == user.getApartmentCategoryId())
                      addAmount(H,eTariff);
                  break;
                  case BuildingCacher.CHARBUILDING  : // Building
                    if(iAttributeId == user.getBuildingId())
                      addAmount(H,eTariff);
                  break;
                  case BuildingCacher.CHARFLOOR     : // Floor
                    if(iAttributeId == user.getFloorId())
                      addAmount(H,eTariff);
                  break;
                  case BuildingCacher.CHARCOMPLEX : // Complex
                    if(iAttributeId == user.getComplexId())
                      addAmount(H,eTariff);
                  break;
                  case BuildingCacher.CHARAPARTMENT : // Apartment
                    if(iAttributeId == user.getApartmentId())
                      addAmount(H,eTariff);
                  break;
                }// switch
              } // attribute check
            }// other than all
            if(sAttribute.length() >= 3){
              iAttributeId = Integer.parseInt(sAttribute.substring(2));
            }
          }
        } // Inner loop block
      } // Outer loop block
      System.err.println("count "+count);
      if(H!=null){
        return H.values();
      }
    } // listcheck
    else
      System.err.println("nothing to preview");
    return null;
  }

  private synchronized void addAmount(Map map,Tariff tariff){
    //System.err.println("map size "+map.size());
    Integer id = new Integer(tariff.getID());
    AssessmentTariffPreview preview;
    if(map.containsKey(id)){
      preview = (AssessmentTariffPreview) map.get(id);
    }
    else{
      preview = new AssessmentTariffPreview(tariff.getName());
    }
    preview.addAmount(tariff.getPrice());
    map.put(id,preview);
    count++;
  }

  private static float insertEntry(Vector V,Tariff T,int iAccountId,int iRoundId,idegaTimestamp itPaydate,int iCashierId)
  throws SQLException{
    AccountEntry AE = new AccountEntry();
    AE.setAccountId(iAccountId);
    AE.setAccountKeyId(T.getAccountKeyId());
    AE.setCashierId(iCashierId);
    AE.setLastUpdated(idegaTimestamp.getTimestampRightNow());
    AE.setTotal(-T.getPrice());
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

  public Map getAttributeMap(){
    Map map = BuildingCacher.mapOfLodgingsNames();
    map.put("a","All");
    return map;
  }

  public List listOfAttributes(){
    List list = BuildingCacher.listOfMapEntries();
    list.add(0,"a");
    return list;
  }


}