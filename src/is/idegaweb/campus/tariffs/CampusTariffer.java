/*
 * $Id: CampusTariffer.java,v 1.4 2001/08/13 09:58:13 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.tariffs;

import com.idega.block.finance.data.*;
import com.idega.block.building.data.*;
import com.idega.block.finance.business.Finder;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.data.GenericEntity;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.textObject.*;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

import is.idegaweb.campus.entity.ContractAccountApartment;
import is.idegaweb.campus.allocation.ContractFinder;
import is.idegaweb.campus.entity.Contract;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusTariffer extends KeyEditor {

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4;
  public  String strAction = "tt_action";
  public final char cComplex = 'x';
  public final char cAll = 'a';
  public final char cBuilding = 'b';
  public final char cFloor = 'f';
  public final char cCategory = 'c';
  public final char cType = 't';
  public final char cApartment = 'p';

  private int iCashierId = -1;
  private String redColor = "#942829";
  private String blueColor = "#27324B",lightBlue ="#ECEEF0";
  private String bottomThickness = "8";

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusTariffer(String sHeader) {
    super(sHeader);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  protected void control(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    if(isAdmin){
      ModuleObject MO = new Text("Nothing");
      if(modinfo.getParameter("commit")!=null){
        if(modinfo.getParameter("pay_date")!=null){
          String date = modinfo.getParameter("pay_date");
          String roundName = modinfo.getParameter("round_name");
          if(date !=null && date.length() == 10){
            roundName = roundName == null?"":roundName;
            idegaTimestamp paydate = new idegaTimestamp(date);
            add(paydate.getISLDate());
            MO = doAssess(paydate,roundName);
          }
          else{
            MO = doMainTable();
          }
        }
        else{
          MO = doMainTable();
        }

      }
      else
        MO = doMainTable();

      add(MO);
    }
    else{
      add("access denied");
    }
  }

  private ModuleObject doMainTable(){
    Table T = new Table();

    int iSignedContractCount = ContractFinder.countContracts(Contract.statusSigned);
    int iAccountCount = CampusAccountFinder.countAccounts();
    int row = 2;
    T.add(iwrb.getLocalizedString("rented_apartments","Rented Apartments"),1,row);
    T.add(String.valueOf(iSignedContractCount),2,row);
    row++;
    T.add(iwrb.getLocalizedString("number_of_accounts","Number of accounts"),1,row);
    T.add(String.valueOf(iAccountCount),2,row);
    row++;
    T.add(iwrb.getLocalizedString("date_of_payment","Date of payment"),1,row);
    T.add(String.valueOf(iAccountCount),2,row);


    Form F = new Form();
    DateInput di = new DateInput("pay_date",true);
    di.setStyle(this.styleAttribute);
    idegaTimestamp today = idegaTimestamp.RightNow();
    today.addMonths(1);
    di.setDate(new idegaTimestamp(1,today.getMonth(),today.getYear()).getSQLDate());

    TextInput rn = new TextInput("round_name");
    rn.setStyle(this.styleAttribute);
    SubmitButton sb = new SubmitButton("commit","Commit");
    sb.setStyle(this.styleAttribute);
    F.add(di);
    F.add(rn);
    F.add(sb);

    T.add(F,1,row);
    row++;

    T.setHorizontalZebraColored(lightBlue,WhiteColor);
    T.setRowColor(1,blueColor);
    T.setRowColor(row,redColor);
    T.mergeCells(1,1,2,1);
    T.mergeCells(1,row,2,row);
    T.setWidth(1,"15");
    T.add(formatText(" "),1,row);
    T.setColumnAlignment(1,"left");
    T.setHeight(row,bottomThickness);
    T.setWidth("100%");

    return T;
  }

  private ModuleObject doAssess(idegaTimestamp paydate,String roundName){
    Table T = new Table();
    List listOfTariffs = Finder.listOfTariffs();
    List listOfUsers = CampusAccountFinder.listOfRentingUserAccounts();
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
            int Amount = 0;
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
                  Amount = insertEntry(vEntries,eTariff,user,iRoundId,paydate);
                }
                // other than all
                else{
                  // attribute check
                  if(sAttribute.length() >= 3){
                  iAttributeId = Integer.parseInt(sAttribute.substring(2));
                    switch (cAttribute) {
                      case cType: // Apartment type
                        if(iAttributeId == user.getApartmentTypeId())
                          Amount = insertEntry(vEntries,eTariff,user,iRoundId,paydate);
                      break;
                      case cCategory  : // Apartment category
                        if(iAttributeId == user.getApartmentCategoryId())
                          Amount = insertEntry(vEntries,eTariff,user,iRoundId,paydate);
                      break;
                      case cBuilding  : // Building
                        if(iAttributeId == user.getBuildingId())
                          Amount = insertEntry(vEntries,eTariff,user,iRoundId,paydate);
                      break;
                      case cFloor     : // Floor
                        if(iAttributeId == user.getFloorId())
                          Amount = insertEntry(vEntries,eTariff,user,iRoundId,paydate);
                      break;
                      case cComplex   : // Complex
                        if(iAttributeId == user.getComplexId())
                          Amount = insertEntry(vEntries,eTariff,user,iRoundId,paydate);
                      break;
                      case cApartment : // Apartment
                        if(iAttributeId == user.getApartmentId())
                          Amount = insertEntry(vEntries,eTariff,user,iRoundId,paydate);
                      break;
                    }// switch
                  } // attribute check
                }// other than all
                if(sAttribute.length() >= 3){
                  iAttributeId = Integer.parseInt(sAttribute.substring(2));
                  T.add(String.valueOf(iAttributeId),2,i+1);
                }
                totalAmount += Amount;

              }
            } // Inner loop block
            totals += totalAmount*-1;
            eAccount.setBalance(eAccount.getBalance()+totalAmount);
            eAccount.setLastUpdated(idegaTimestamp.getTimestampRightNow());
            eAccount.update();
          } // Outer loop block
          AR.setTotals((float)(totals));
          AR.update();
          t.commit();
          T.add(iwrb.getLocalizedString("assessment_successful","Assessment was successfull"));
        } // Try block
        catch(Exception e) {
          try {
            t.rollback();
          }
          catch(javax.transaction.SystemException ex) {
            ex.printStackTrace();
          }
          e.printStackTrace();
          T.add(iwrb.getLocalizedString("insert_error","Insert error"));
        }
        }
      }
      else
        T.add(iwrb.getLocalizedString("no_users","No Users"));
    }
    else
      T.add(iwrb.getLocalizedString("no_tariffs","No Tariffs"));
    return T;
  }

  private int insertEntry(Vector V,Tariff T,ContractAccountApartment U,int iRoundId,idegaTimestamp itPaydate)
  throws SQLException{
    AccountEntry AE = new AccountEntry();
    AE.setAccountId(U.getAccountId());
    AE.setAccountKeyId(T.getAccountKeyId());
    AE.setCashierId(this.iCashierId);
    AE.setLastUpdated(idegaTimestamp.getTimestampRightNow());
    AE.setPrice(-T.getPrice());
    AE.setRoundId(iRoundId);
    AE.setName(T.getName());
    AE.setInfo(T.getInfo());
    AE.setCashierId(1);
    AE.setPaymentDate(itPaydate.getTimestamp());
    AE.insert();
    V.add(AE);
    return AE.getPrice();
    /*
    System.err.println("totals before"+totals);
    totals = totals + AE.getPrice();
    System.err.println("price"+AE.getPrice());
    System.err.println("totals after"+totals);
    */
  }

  private List listOfConAccAprt(char cAttribute, int iAttributeId){
    List L = null;
    switch (cAttribute) {
    case 'a' : L = CampusAccountFinder.listOfRentingUserAccounts();  break;
    case 't' : L = CampusAccountFinder.listOfConAccAprtByType(iAttributeId); break;
    case 'p' : L = CampusAccountFinder.listOfConAccAprtByApartment(iAttributeId); break;
    }
  return L;
  }
}