package com.idega.block.finance.presentation;


import com.idega.util.text.Edit;

import com.idega.block.finance.data.*;
import com.idega.block.building.data.*;
import com.idega.block.finance.business.*;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.data.GenericEntity;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Block;
import com.idega.presentation.text.*;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;
import java.util.Hashtable;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.idegaTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class TariffAssessments extends Block {

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5,ACT6 = 6;
  public  String strAction = "tt_action";
  protected boolean isAdmin = false;

  private int iCashierId = 1;
  private static String prmGroup = "tass_grp";

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public String getLocalizedNameKey(){
    return "assessment";
  }

  public String getLocalizedNameValue(){
    return "Assessment";
  }

  protected void control(IWContext iwc){
    if(isAdmin){
      int iCategoryId = Finance.parseCategoryId(iwc);
      int iGroupId = -1;
      List groups = FinanceFinder.listOfTariffGroupsWithHandlers(iCategoryId);
      TariffGroup group = null;
      if(iwc.isParameterSet(prmGroup))
        iGroupId = Integer.parseInt(iwc.getParameter(prmGroup));
      if(iGroupId > 0 ){
        group = FinanceFinder.getTariffGroup(iGroupId);
      }
      else if(groups !=null){
        group = (TariffGroup) groups.get(0);
        iGroupId = group.getID();
      }
      FinanceHandler handler = null;
      if(group !=null){
        iGroupId = group.getID();
        if(group.getHandlerId() > 0){
          handler = FinanceFinder.getFinanceHandler(group.getHandlerId());
        }
      }
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
            case ACT2 : MO = doMainTable(iwc,iCategoryId,handler);      break;
            case ACT3 : MO = doAssess( iwc,iCategoryId,iGroupId,handler);      break;
            case ACT4 : MO = getTableOfAssessmentAccounts( iwc);      break;
            case ACT5 : MO = doRollback(iwc); break;
            case ACT6 : MO = getPreviewTable(handler,group); break;
            default: MO = getTableOfAssessments(iwc);           break;
          }
        }
        Table T = new Table();
        T.setWidth("100%");
        T.setCellpadding(2);
        T.setCellspacing(0);
        T.add(Edit.headerText(iwrb.getLocalizedString("tariff_assessment","Tariff assessment"),3),1,1);
        T.add(getGroupLinks(iCategoryId,iGroupId,groups),1,2);
        if(group != null)
          T.add(makeLinkTable(  1,iCategoryId,iGroupId),1,3);
        T.add(MO,1,4);
        add(T);

      }
      catch(Exception S){
        S.printStackTrace();
      }
    }
    else
      add(iwrb.getLocalizedString("access_denied","Access denies"));
  }

  private PresentationObject getGroupLinks(int iCategoryId , int iGroupId,List groups){
    Table T = new Table(1,1);
    T.setCellpadding(0);
    T.setCellspacing(0);
    if(groups!=null){
      java.util.Iterator I = groups.iterator();
      TariffGroup group;
      Link tab;
      while (I.hasNext()) {
        group = (TariffGroup) I.next();
        tab = new Link(iwb.getImageTab(group.getName(),true));
        tab.addParameter(Finance.getCategoryParameter(iCategoryId));
        tab.addParameter(prmGroup,group.getID());
        T.add(tab,1,1);
      }
    }
    return T;
  }

  private PresentationObject doRollback(IWContext iwc){
    Table T = new Table();
    String sRoundId = iwc.getParameter("rollback");
    if(sRoundId != null){
      int iRoundId = Integer.parseInt(sRoundId);
      try{
        AssessmentBusiness.rollBackAssessment(iRoundId);
        T.add(iwrb.getLocalizedString("rollbac_success","Rollback was successfull"));
      }
      catch(Exception ex){
        T.add(iwrb.getLocalizedString("rollbac_illegal","Rollback was illegal"));
      }
    }
    return T;
  }

  private PresentationObject doAssess(IWContext iwc,int iCategoryId,int iGroupId,FinanceHandler handler){
    PresentationObject MO = new Text("failed");
    if(iwc.getParameter("pay_date")!=null){
      String date = iwc.getParameter("pay_date");
      String roundName = iwc.getParameter("round_name");
      String accountKeyId = iwc.getParameter("account_key_id");
      //add(accountKeyId);
      if(date !=null && date.length() == 10){
        if(roundName != null && roundName.trim().length() > 1){
          roundName = roundName == null?"":roundName;
          int iAccountKeyId = accountKeyId!=null?Integer.parseInt(accountKeyId):-1;
          idegaTimestamp paydate = new idegaTimestamp(date);
          //add(paydate.getISLDate());
          boolean assessed = handler.executeAssessment(iGroupId,roundName,1,paydate);
        }
        else{
          add(iwrb.getLocalizedString("no_name_error","No name entered"));
          MO = doMainTable(iwc,iCategoryId,handler);
        }
      }
      else{
        MO = doMainTable(iwc,iCategoryId,handler);
      }
    }
    return MO;
  }

  protected PresentationObject makeLinkTable(int menuNr,int iCategoryId,int iGroupId){
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
    Link1.addParameter(prmGroup,iGroupId);
    Link1.addParameter(Finance.getCategoryParameter(iCategoryId));
    Link Link2 = new Link(iwrb.getLocalizedString("new","New"));
    Link2.setFontColor(Edit.colorLight);
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    Link2.addParameter(prmGroup,iGroupId);
    Link2.addParameter(Finance.getCategoryParameter(iCategoryId));
    Link Link3= new Link(iwrb.getLocalizedString("preview","Preview"));
    Link3.setFontColor(Edit.colorLight);
    Link3.addParameter(this.strAction,String.valueOf(this.ACT6));
    Link3.addParameter(prmGroup,iGroupId);
    Link3.addParameter(Finance.getCategoryParameter(iCategoryId));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
      LinkTable.add(Link3,3,1);
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

  private PresentationObject getPreviewTable(FinanceHandler handler,TariffGroup group){
    Table T = new Table();
    T.setWidth("100%");
    if(handler !=null && group !=null){
      Collection L = handler.listOfAssessmentTariffPreviews(group.getID());
      if(L!=null){
        int row = 1;
        float totals = 0;
        T.add(iwrb.getLocalizedString("tariff_name","Tariff name"),1,row);
        T.add(iwrb.getLocalizedString("account_count","Account count"),2,row);
        T.add(iwrb.getLocalizedString("total_amount","Total amount"),3,row);
        row++;
        Iterator I = L.iterator();
        AssessmentTariffPreview preview;
        double amount;
        while(I.hasNext()){
          preview = (AssessmentTariffPreview) I.next();
          T.add(Edit.formatText(preview.getName()),1,row);
          T.add(Edit.formatText(preview.getAccounts()),2,row);
          amount = preview.getTotals();
          T.add(Edit.formatText(Double.toString(amount)),3,row);
          totals+=amount;
          row++;
        }
        T.add(Edit.formatText(Double.toString(totals)),3,row);
        T.setColumnAlignment(3,"right");
      }
    }


    return T;
  }

  private PresentationObject getTableOfAssessmentAccounts(IWContext iwc){
    Table T = new Table();
    String id = iwc.getParameter("ass_round_id");
    if(id != null){
      //List L = Finder.listOfAccountsInAssessmentRound(Integer.parseInt(id));
      List L = null ;//CampusAccountFinder.listOfContractAccountApartmentsInAssessment(Integer.parseInt(id));
      if(L!= null){
        int len = L.size();
        T.add(Edit.titleText(iwrb.getLocalizedString("account_name","Account name")),1,1);
        //T.add(Edit.titleText(iwrb.getLocalizedString("account_stamp","Last updated")),2,1);
        T.add(Edit.titleText(iwrb.getLocalizedString("apartment_name","Apartment")),2,1);
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

  private PresentationObject doMainTable(IWContext iwc,int iCategoryId , FinanceHandler handler){
    Form F = new Form();
    Table T = new Table();
    int iAccountCount = FinanceFinder.countAccounts(iCategoryId,handler.getAccountType());
    int row = 1;
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

    DropdownMenu drpAccountKeys = drpAccountKeys(Finder.getAccountKeys(),"account_key_id");
    Edit.setStyle(drpAccountKeys);

    T.add(di,2,row);
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

    return new Table();
  }



  private PresentationObject doAssessFinance(idegaTimestamp paydate,String roundName,String accountType){
    Table T = new Table();
    try {
      AssessmentRound AR = AssessmentBusiness.assessFinance(paydate,roundName ,accountType ,iCashierId );
      T.add(Edit.formatText(iwrb.getLocalizedString("assessment_successful","Assessment was successfull")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("total_amount","Total amount")),1,2);
      if(AR !=null){
        T.add(Edit.formatText(new java.text.DecimalFormat().format(AR.getTotals())),2,2);
        T.add(Edit.formatText(iwrb.getLocalizedString("account_number","Accounts")),1,3);
        T.add(Edit.formatText(AR.getAccountCount()),2,3);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      T.add(iwrb.getLocalizedString("no_tariffs","No Tariffs"));
    }
    return T;
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
