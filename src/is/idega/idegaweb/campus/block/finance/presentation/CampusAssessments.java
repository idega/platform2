package is.idega.idegaweb.campus.block.finance.presentation;


import is.idega.idegaweb.campus.presentation.Edit;
import is.idega.idegaweb.campus.block.finance.business.*;
import is.idega.idegaweb.campus.exception.*;
import com.idega.block.finance.data.*;
import com.idega.block.building.data.*;
import com.idega.block.finance.business.*;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.data.GenericEntity;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.text.*;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.idegaTimestamp;

import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CampusAssessments extends PresentationObjectContainer {

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  public  String strAction = "tt_action";
  protected boolean isAdmin = false;
  public final char cComplex = 'x';
  public final char cAll = 'a';
  public final char cBuilding = 'b';
  public final char cFloor = 'f';
  public final char cCategory = 'c';
  public final char cType = 't';
  public final char cApartment = 'p';

  private int iCashierId = 1;


  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus.block.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusAssessments() {

  }

   protected void control(IWContext iwc){

   if(isAdmin){
      try{
        PresentationObject MO = new Text();

        if(iwc.getParameter(strAction) == null){
          MO = getTableOfAssessments(iwc);
        }
        if(iwc.getParameter(strAction) != null){
          String sAct = iwc.getParameter(strAction);
          int iAct = Integer.parseInt(sAct);

          switch (iAct) {
            case ACT1 : MO = getTableOfAssessments(iwc); break;
            case ACT2 : MO = doMainTable(iwc);      break;
            case ACT3 : MO = doSomeThing( iwc);      break;
            case ACT4 : MO = getTableOfAssessmentAccounts( iwc);      break;
            case ACT5 : MO = doRollback(iwc); break;
            default: MO = getTableOfAssessments(iwc);           break;
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

  private PresentationObject doRollback(IWContext iwc){
    Table T = new Table();
    String sRoundId = iwc.getParameter("rollback");
    if(sRoundId != null){
      int iRoundId = Integer.parseInt(sRoundId);
      try{
        CampusAssessmentBusiness.rollBackAssessment(iRoundId);
        T.add(iwrb.getLocalizedString("rollbac_success","Rollback was successfull"));
      }
      catch(RollBackException ex){
        T.add(iwrb.getLocalizedString("rollbac_illegal","Rollback was illegal"));
      }
    }
    return T;
  }

  private PresentationObject doSomeThing(IWContext iwc){
    PresentationObject MO = new Text("failed");
    if(iwc.getParameter("pay_date")!=null){
      String date = iwc.getParameter("pay_date");
      String roundName = iwc.getParameter("round_name");
      String accountType = iwc.getParameter("account_type");
      String accountKeyId = iwc.getParameter("account_key_id");
      //add(accountKeyId);
      if(date !=null && date.length() == 10){
        if(roundName != null && roundName.trim().length() > 1){
          roundName = roundName == null?"":roundName;
          accountType = accountType != null?accountType:Account.typeFinancial;
          int iAccountKeyId = accountKeyId!=null?Integer.parseInt(accountKeyId):-1;
          idegaTimestamp paydate = new idegaTimestamp(date);
          //add(paydate.getISLDate());
          MO = doAssess(paydate,roundName,accountType,iAccountKeyId);
        }
        else{
          add(iwrb.getLocalizedString("no_name_error","No name entered"));
          MO = doMainTable(iwc);
        }
      }
      else{
        MO = doMainTable(iwc);
      }
    }
    return MO;
  }

  protected PresentationObject makeLinkTable(int menuNr){
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

  private PresentationObject getTableOfAssessments(IWContext iwc){
    Table T = new Table();
    int row = 2;
    List L = Finder.listOfAssessments();
    if(L!= null){
      int len = L.size();

      String sRollBack = iwrb.getLocalizedString("rollback","Rollback");
      T.add(Edit.formatText(iwrb.getLocalizedString("assessment_name","Assessment name")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("assessment_stamp","Timestamp")),2,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("totals","Total amount")),3,1);
      T.add(Edit.formatText(sRollBack),4,1);


      int col = 1;
      row = 2;
      AssessmentRound AR;
      java.text.NumberFormat nf=  java.text.NumberFormat.getNumberInstance(iwc.getCurrentLocale());
      for (int i = 0; i < len; i++) {
        col = 1;
        AR = (AssessmentRound) L.get(i);
        T.add(getRoundLink(AR.getName(),AR.getID()),col++,row);
        T.add(Edit.formatText(new idegaTimestamp(AR.getRoundStamp()).getLocaleDate(iwc)),col++,row);
        T.add(Edit.formatText(nf.format(AR.getTotals())),col++,row);
        Link R = new Link(iwb.getImage("rollback.gif"));
        R.addParameter("rollback",AR.getID());
        R.addParameter(strAction ,ACT5);
        T.add(R,col++,row);

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

  private PresentationObject getTableOfAssessmentAccounts(IWContext iwc){
    Table T = new Table();
    String id = iwc.getParameter("ass_round_id");
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
        java.text.NumberFormat nf=  java.text.NumberFormat.getNumberInstance(iwc.getCurrentLocale());
        for (int i = 0; i < len; i++) {
          col = 1;
          A = (Account) L.get(i);
          T.add(Edit.formatText(A.getName()),col++,row);
          T.add(Edit.formatText(new idegaTimestamp(A.getLastUpdated()).getLocaleDate(iwc)),col++,row);
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

  private PresentationObject doMainTable(IWContext iwc){
    Form F = new Form();
    Table T = new Table();

    int iNumberOfRentableApartments = BuildingFinder.countRentableApartments();
    int iSignedContractCount = ContractFinder.countContracts(Contract.statusSigned);
    int iAccountCount = CampusAccountFinder.countAccounts(Account.typeFinancial);
    int row = 2;
    T.add(Edit.formatText(iwrb.getLocalizedString("rentable_apartments","Rentable Apartments")),1,row);
    T.add(String.valueOf(iNumberOfRentableApartments),2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("rented_apartments","Rented Apartments")),1,row);
    T.add(String.valueOf(iSignedContractCount),2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("number_of_accounts","Number of accounts")),1,row);
    T.add(String.valueOf(iAccountCount),2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("date_of_payment","Date of payment")),1,row);

    DateInput di = new DateInput("pay_date",true);
    di.setStyleAttribute("type",Edit.styleAttribute);
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

    DropdownMenu drpAccountKeys = drpAccountKeys(Finder.getAccountKeys(),"account_key_id");
    Edit.setStyle(drpAccountKeys);

    T.add(di,2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("type_of_account","Account type")),1,row);
    T.add(drpAccountTypes ,2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("account_key","Account key")),1,row);
    T.add(drpAccountKeys ,2,row);
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

  private DropdownMenu drpAccountKeys(List AK,String name){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(0,"--");
    if(AK != null){
      drp.addMenuElements(AK);
    }
    return drp;
  }

  private PresentationObject doAssess(idegaTimestamp paydate,String roundName,String accountType,int iAccountKeyId){
    if(accountType.equalsIgnoreCase(Account.typeFinancial)){
      return doAssessFinance(paydate,roundName ,accountType);
    }
    else if(accountType.equalsIgnoreCase(Account.typePhone)){
      return doAssessPhone( paydate,roundName,accountType,iAccountKeyId );
    }
    return new Table();
  }

  private PresentationObject doAssessPhone(idegaTimestamp paydate,String roundName,String accountType,int iAccountKeyId){
    Table T = new Table();
    try{
      AssessmentRound AR = CampusAssessmentBusiness.assessPhones(paydate,roundName,accountType ,iAccountKeyId ,iCashierId );
      T.add(Edit.formatText(iwrb.getLocalizedString("assessment_successful","Assessment was successfull")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("total_amount","Total amount")),1,2);
      if(AR !=null){
        T.add(Edit.formatText(new java.text.DecimalFormat().format(AR.getTotals() )),2,2);
        T.add(Edit.formatText(iwrb.getLocalizedString("account_number","Accounts")),1,3);
        T.add(Edit.formatText(AR.getAccountCount()),2,3);
      }
    }
    catch(CampusFinanceException ex){
      ex.printStackTrace();
      T.add(iwrb.getLocalizedString("insert_error","Insert error"));
    }
    return T;
  }

  private PresentationObject doAssessFinance(idegaTimestamp paydate,String roundName,String accountType){
    Table T = new Table();
    try {
      AssessmentRound AR = CampusAssessmentBusiness.assessFinance(paydate,roundName ,accountType ,iCashierId );
      T.add(Edit.formatText(iwrb.getLocalizedString("assessment_successful","Assessment was successfull")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("total_amount","Total amount")),1,2);
      if(AR !=null){
        T.add(Edit.formatText(new java.text.DecimalFormat().format(AR.getTotals())),2,2);
        T.add(Edit.formatText(iwrb.getLocalizedString("account_number","Accounts")),1,3);
        T.add(Edit.formatText(AR.getAccountCount()),2,3);
      }
    }
    catch (CampusFinanceException ex) {
      ex.printStackTrace();
      T.add(iwrb.getLocalizedString("no_tariffs","No Tariffs"));
    }
    return T;
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
    L.setFontSize(Edit.textFontSize);
    return L;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
}
