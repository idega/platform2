/*
 * $Id: CampusTariffer.java,v 1.8 2001/08/30 06:37:39 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.tariffs;

import is.idegaweb.campus.presentation.Edit;
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
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.textObject.*;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.idegaTimestamp;

import is.idegaweb.campus.entity.ContractAccountApartment;
import is.idegaweb.campus.allocation.ContractFinder;
import is.idegaweb.campus.entity.Contract;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusTariffer extends ModuleObjectContainer {

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4;
  public  String strAction = "tt_action";
  protected boolean isAdmin = false;
  public final char cComplex = 'x';
  public final char cAll = 'a';
  public final char cBuilding = 'b';
  public final char cFloor = 'f';
  public final char cCategory = 'c';
  public final char cType = 't';
  public final char cApartment = 'p';

  private int iCashierId = -1;


  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusTariffer() {

  }

   protected void control(ModuleInfo modinfo){

   if(isAdmin){
      try{
        ModuleObject MO = new Text();

        if(modinfo.getParameter(strAction) == null){
          MO = getTableOfAssessments(modinfo);
        }
        if(modinfo.getParameter(strAction) != null){
          String sAct = modinfo.getParameter(strAction);
          int iAct = Integer.parseInt(sAct);

          switch (iAct) {
            case ACT1 : MO = getTableOfAssessments(modinfo); break;
            case ACT2 : MO = doMainTable(modinfo);      break;
            case ACT3 : MO = doSomeThing( modinfo);      break;
            case ACT4 : MO = getTableOfAssessmentAccounts( modinfo);      break;
            default: MO = getTableOfAssessments(modinfo);           break;
          }
        }
         Table T = new Table();
         T.setWidth("100%");
        T.setCellpadding(2);
        T.setCellspacing(0);
        T.add(Edit.headerText(iwrb.getLocalizedString("tariffer","Tariffer"),3),1,1);
        T.add(makeLinkTable(  1),1,2);
        T.add(MO,1,3);
        add(T);

      }
      catch(Exception S){
        S.printStackTrace();
      }
    }
    else
      add(iwrb.getLocalizedString("access_denied","Access denies"));
  }


  private ModuleObject doSomeThing(ModuleInfo modinfo){
    ModuleObject MO = new Text("failed");
    if(modinfo.getParameter("pay_date")!=null){
      String date = modinfo.getParameter("pay_date");
      String roundName = modinfo.getParameter("round_name");
      String accountType = modinfo.getParameter("account_type");
      if(date !=null && date.length() == 10){
        roundName = roundName == null?"":roundName;
        accountType = accountType != null?accountType:Account.typeFinancial;
        idegaTimestamp paydate = new idegaTimestamp(date);
        add(paydate.getISLDate());
        MO = doAssess(paydate,roundName,accountType);
      }
      else{
        MO = doMainTable(modinfo);
      }
    }
    return MO;
  }

  protected ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(3,1);
    int last = 3;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(Edit.colorDark);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link(iwrb.getLocalizedString("view","View"));
    Link1.setFontColor(Edit.colorLight);
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link Link2 = new Link(iwrb.getLocalizedString("new","New"));
    Link2.setFontColor(Edit.colorLight);
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }

  private ModuleObject getTableOfAssessments(ModuleInfo modinfo){
    Table T = new Table();
    int row = 2;
    List L = Finder.listOfAssessments();
    if(L!= null){
      int len = L.size();

      T.add(Edit.titleText(iwrb.getLocalizedString("assessment_name","Assessment name")),1,1);
      T.add(Edit.titleText(iwrb.getLocalizedString("assessment_stamp","Timestamp")),2,1);
      T.add(Edit.titleText(iwrb.getLocalizedString("totals","Total amount")),3,1);

      int col = 1;
      row = 2;
      AssessmentRound AR;
      java.text.NumberFormat nf=  java.text.NumberFormat.getNumberInstance(modinfo.getCurrentLocale());
      for (int i = 0; i < len; i++) {
        col = 1;
        AR = (AssessmentRound) L.get(i);
        T.add(getRoundLink(AR.getName(),AR.getID()),col++,row);
        T.add(Edit.formatText(new idegaTimestamp(AR.getRoundStamp()).getLocaleDate(modinfo)),col++,row);
        T.add(Edit.formatText(nf.format(AR.getTotals())),col++,row);
        row++;
      }
      T.setWidth("100%");
      T.setCellpadding(2);
      T.setCellspacing(1);
      T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
      T.setRowColor(1,Edit.colorMiddle);
      row++;
    }
    else
      T.add(iwrb.getLocalizedString("no_assessments","No assessments"),1,row);
    return T;
  }

  private ModuleObject getTableOfAssessmentAccounts(ModuleInfo modinfo){
    Table T = new Table();
    String id = modinfo.getParameter("ass_round_id");
    if(id != null){
      List L = Finder.listOfAccountsInAssessmentRound(Integer.parseInt(id));
      if(L!= null){
        int len = L.size();
        T.add(Edit.titleText(iwrb.getLocalizedString("account_name","Account name")),1,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("account_stamp","Last updated")),2,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("totals","Balance")),3,1);

        int col = 1;
        int row = 2;
        Account A;
        java.text.NumberFormat nf=  java.text.NumberFormat.getNumberInstance(modinfo.getCurrentLocale());
        for (int i = 0; i < len; i++) {
          col = 1;
          A = (Account) L.get(i);
          T.add(Edit.formatText(A.getName()),col++,row);
          T.add(Edit.formatText(new idegaTimestamp(A.getLastUpdated()).getLocaleDate(modinfo)),col++,row);
          T.add(Edit.formatText(nf.format(A.getBalance())),col++,row);
          row++;
        }
        T.setWidth("100%");
        T.setCellpadding(2);
        T.setCellspacing(1);
        T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
        T.setRowColor(1,Edit.colorMiddle);
        row++;
      }
      else
        add("is empty");
    }
    return T;
  }

  private ModuleObject doMainTable(ModuleInfo modinfo){
    Form F = new Form();
    Table T = new Table();

    int iSignedContractCount = ContractFinder.countContracts(Contract.statusSigned);
    int iAccountCount = CampusAccountFinder.countAccounts();
    int row = 2;
    T.add(Edit.formatText(iwrb.getLocalizedString("rented_apartments","Rented Apartments")),1,row);
    T.add(String.valueOf(iSignedContractCount),2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("number_of_accounts","Number of accounts")),1,row);
    T.add(String.valueOf(iAccountCount),2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("date_of_payment","Date of payment")),1,row);
    T.add(String.valueOf(iAccountCount),2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("date_of_payment","Date of payment")),1,row);



    DateInput di = new DateInput("pay_date",true);
    di.setStyleAttribute("typ",Edit.styleAttribute);
    idegaTimestamp today = idegaTimestamp.RightNow();
    today.addMonths(1);
    di.setDate(new idegaTimestamp(1,today.getMonth(),today.getYear()).getSQLDate());

    TextInput rn = new TextInput("round_name");

    Edit.setStyle(rn);
    SubmitButton sb = new SubmitButton("commit",iwrb.getLocalizedString( "commit","Commit"));
    Edit.setStyle(sb);

    DropdownMenu drpAccountTypes = new DropdownMenu("account_type");
    drpAccountTypes.addMenuElement(Account.typeFinancial,iwrb.getLocalizedString("financial","Financial"));
    drpAccountTypes.addMenuElement(Account.typePhone,iwrb.getLocalizedString("phone","phone"));
    Edit.setStyle(drpAccountTypes );

    T.add(di,2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("type_of_account","Account type")),1,row);
    T.add(drpAccountTypes ,2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("name_of_round","Assessment name")),1,row);
    T.add(rn,2,row);
    row++;
    T.add(sb,2,row);
    row++;


    T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    T.setRowColor(1,Edit.colorMiddle);
    T.mergeCells(1,1,2,1);
    T.setWidth("100%");
    F.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    F.add(T);
    return F;
  }

  private ModuleObject doAssess(idegaTimestamp paydate,String roundName,String accountType){
    Table T = new Table();
    List listOfTariffs = Finder.listOfTariffs();
    List listOfUsers = CampusAccountFinder.listOfRentingUserAccountsByType(accountType);
    System.err.println(accountType);
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
          T.add(Edit.formatText(iwrb.getLocalizedString("assessment_successful","Assessment was successfull")),1,1);
          T.add(Edit.formatText(iwrb.getLocalizedString("total_amount","Total amount")),1,2);
          T.add(Edit.formatText(new java.text.DecimalFormat().format(totals *-1)),2,2);
          T.add(Edit.formatText(iwrb.getLocalizedString("account_number","Accounts")),1,3);
          T.add(Edit.formatText(rlen));
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

  private float insertEntry(Vector V,Tariff T,ContractAccountApartment U,int iRoundId,idegaTimestamp itPaydate)
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

  private Link getRoundLink(String name,int id){
    Link L = new Link(name);
    L.addParameter(strAction,ACT4);
    L.addParameter("ass_round_id",id);
    return L;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    try{
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    control(modinfo);
  }
}