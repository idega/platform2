package com.idega.block.finance.presentation;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.finance.business.AssessmentBusiness;
import com.idega.block.finance.business.AssessmentTariffPreview;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.business.FinanceHandler;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountInfo;
import com.idega.block.finance.data.RoundInfo;
import com.idega.block.finance.data.TariffGroup;
import com.idega.core.user.data.User;
import com.idega.idegaweb.presentation.BusyBar;
import com.idega.idegaweb.presentation.StatusBar;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Edit;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class TariffAssessments extends Finance {

  protected static final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5,ACT6 = 6,ACT7 = 7;
  public  String strAction = "tt_action";
  protected boolean isAdmin = false;

  private int iCashierId = 1;
  private static String prmGroup = "tass_grp";
//  private int iCategoryId = -1;

  private StatusBar status ;

  public TariffAssessments(){
    StatusBar status = new StatusBar("ass_status");

  }

  public String getLocalizedNameKey(){
    return "assessment";
  }

  public String getLocalizedNameValue(){
    return "Assessment";
  }

  protected void control(IWContext iwc){
    if(isAdmin){    	
//      iCategoryId = Finance.parseCategoryId(iwc);
      int iGroupId = -1;
      //List groups = FinanceFinder.getInstance().listOfTariffGroupsWithHandlers(iCategoryId);
      List groups = FinanceFinder.getInstance().listOfTariffGroups(iCategoryId);
      TariffGroup group = null;
      if(iwc.isParameterSet(prmGroup))
        iGroupId = Integer.parseInt(iwc.getParameter(prmGroup));

      //add("group "+iGroupId);
      if(iGroupId > 0 ){
        group = FinanceFinder.getInstance().getTariffGroup(iGroupId);
      }
      else if(groups !=null){
        group = (TariffGroup) groups.get(0);
        iGroupId = group.getID();
      }
      FinanceHandler handler = null;
      if(group !=null){
        iGroupId = group.getID();
        if(group.getHandlerId() > 0){
          handler = FinanceFinder.getInstance().getFinanceHandler(group.getHandlerId());
        }
      }
      try{
        PresentationObject MO = new Text();

        if(iwc.getParameter(strAction) == null){
          MO = getTableOfAssessments(iwc,iGroupId);
        }
        if(iwc.getParameter(strAction) != null){
          String sAct = iwc.getParameter(strAction);
          int iAct = Integer.parseInt(sAct);

          switch (iAct) {
            case ACT1 : MO = getTableOfAssessments(iwc,iGroupId); break;
            case ACT2 : MO = doMainTable(iwc,iCategoryId,iGroupId,handler);      break;
            case ACT3 : MO = doAssess( iwc,iCategoryId,iGroupId,handler);      break;
            case ACT4 : MO = getTableOfAssessmentAccounts( iwc);      break;
            case ACT5 : doRollback(iwc,handler); MO = getTableOfAssessments(iwc,iGroupId); break;
            case ACT6 : MO = getPreviewTable(handler,group); break;
            case ACT7 : MO = getTableOfAssessmentAccountEntries(iwc); break;
            default: MO = getTableOfAssessments(iwc,iGroupId);           break;
          }
        }
        Table T = new Table();
        T.setWidth("100%");
        T.setCellpadding(2);
        T.setCellspacing(0);
        T.add(Edit.headerText(iwrb.getLocalizedString("tariff_assessment","Tariff assessment"),3),1,1);
        //T.add(status,1,1);
        T.add(getGroupLinks(iCategoryId,iGroupId,groups),1,2);
        if(group != null) {
          T.add(makeLinkTable(  1,iCategoryId,iGroupId),1,3);
        }
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
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    T.setWidth("100%");
    int col = 1;
    if(groups!=null){
      java.util.Iterator I = groups.iterator();
      TariffGroup group;
      Link tab;

      while (I.hasNext()) {
        group = (TariffGroup) I.next();
        tab = new Link(iwb.getImageTab(group.getName(),false));
        tab.addParameter(Finance.getCategoryParameter(iCategoryId));
        tab.addParameter(prmGroup,group.getID());
        T.add(tab,col++,1);

      }
    }
    T.setWidth(col,1,"100%");
    T.add(status,++col,1);
    T.setAlignment(col,1,"right");
    return T;
  }

  private void doRollback(IWContext iwc,FinanceHandler handler){
    String sRoundId = iwc.getParameter("rollback");
    if(sRoundId != null){
      int iRoundId = Integer.parseInt(sRoundId);
      try{
         boolean rb = false;
        if(handler!=null){
          rb = handler.rollbackAssessment(iRoundId);
        }
        else{
          AssessmentBusiness assBuiz = (AssessmentBusiness)com.idega.business.IBOLookup.getServiceInstance(iwc,AssessmentBusiness.class);
          rb = assBuiz.rollBackAssessment(iRoundId);

        }
        //AssessmentBusiness.rollBackAssessment(iRoundId);
        if(rb)
          status.setMessage(iwrb.getLocalizedString("rollback_success","Rollback was successfull"));
        else
          status.setMessage(iwrb.getLocalizedString("rollback_unsuccess","Rollback was unsuccessfull"));
      }
      catch(Exception ex){
        ex.printStackTrace();
        status.setMessage(iwrb.getLocalizedString("rollback_illegal","Rollback was illegal"));
      }
    }

  }

  private PresentationObject doAssess(IWContext iwc,int iCategoryId,int iGroupId,FinanceHandler handler){
    //add(handler.getClass().getName());
    PresentationObject MO = new Text("failed");
    if(iwc.getParameter("pay_date")!=null){
      String date = iwc.getParameter("pay_date");
      String start = iwc.getParameter("start_date");
      String end = iwc.getParameter("end_date");
      String roundName = iwc.getParameter("round_name");
      String accountKeyId = iwc.getParameter("account_key_id");
      //add(accountKeyId);
      if(date !=null && date.length() == 10){
        if(roundName != null && roundName.trim().length() > 1){
          roundName = roundName == null?"":roundName;
          int iAccountKeyId = accountKeyId!=null?Integer.parseInt(accountKeyId):-1;
          IWTimestamp paydate = new IWTimestamp(date);
          IWTimestamp startdate = new IWTimestamp(start);
          IWTimestamp enddate = new IWTimestamp(end);
          //add(paydate.getISLDate());
          debug("Starting Execution "+IWTimestamp.RightNow().toString());
          boolean assessed = handler.executeAssessment(iCategoryId, iGroupId,roundName,1,iAccountKeyId,paydate,startdate,enddate);
          debug("Ending Execution "+IWTimestamp.RightNow().toString());
          if(assessed){
            status.setMessage(iwrb.getLocalizedString("assessment_sucess","Assessment succeded"));
          }
          else{
            status.setMessage(iwrb.getLocalizedString("assessment_failure","Assessment failed"));
          }
          MO = getTableOfAssessments(iwc,iGroupId);
        }
        else{
          add(iwrb.getLocalizedString("no_name_error","No name entered"));
          MO = doMainTable(iwc,iCategoryId,iGroupId,handler);
        }
      }
      else{
        MO = doMainTable(iwc,iCategoryId,iGroupId,handler);
      }
    }
    else
      System.err.println("did not have pay_date");
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
      status.setMessageCaller(Link1,iwrb.getLocalizedString("view_assessments","View assessments"));
      status.setMessageCaller(Link2,iwrb.getLocalizedString("make_new_assessment","Create new assessment"));
      status.setMessageCaller(Link3,iwrb.getLocalizedString("preview_assessment","Preview assessment"));
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
      LinkTable.add(Link3,3,1);
    }
    return LinkTable;
  }

  private PresentationObject getTableOfAssessments(IWContext iwc,int iGroupId){
    Table T = new Table();
    int row = 2;
    //List L = Finder.listOfAssessments();

    List L = FinanceFinder.getInstance().listOfAssessmentInfo(iCategoryId,iGroupId);
    if(L!= null){
      BusyBar busy = new BusyBar("busyguy");
      int len = L.size();

      String sRollBack = iwrb.getLocalizedString("rollback","Rollback");
      T.add(Edit.formatText(iwrb.getLocalizedString("assessment_name","Assessment name")),1,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("assessment_stamp","Timestamp")),2,1);
      T.add(Edit.formatText(iwrb.getLocalizedString("totals","Total amount")),3,1);
      T.add(Edit.formatText(sRollBack),4,1);

      int col = 1;
      row = 2;
      RoundInfo AR;
      java.text.NumberFormat nf=  java.text.NumberFormat.getNumberInstance(iwc.getCurrentLocale());
      float total = 0;
      for (int i = 0; i < len; i++) {
        col = 1;
        AR = (RoundInfo) L.get(i);
        T.add(getRoundLink(AR.getName(),AR.getRoundId(),iGroupId),col++,row);
        T.add(Edit.formatText(new IWTimestamp(AR.getRoundStamp()).getLocaleDate(iwc)),col++,row);
        T.add(Edit.formatText(nf.format(AR.getTotals())),col++,row);
        Link R = new Link(iwb.getImage("rollback.gif"));
        R.addParameter("rollback",AR.getRoundId());
        R.addParameter(strAction ,ACT5);
        R.addParameter(prmGroup,iGroupId);
        status.setMessageCaller(R,iwrb.getLocalizedString("rollback","Rolls back this assessment"));
        busy.setLinkObject(R);
        T.add(R,col++,row);
        total+= AR.getTotals();
        row++;
      }
      T.add(busy,2,row);
      T.add(Edit.formatText(nf.format(total)),3,row);
      T.setWidth("100%");
      T.setCellpadding(2);
      T.setCellspacing(1);
      T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
      T.setRowColor(1,Edit.colorMiddle);
      T.setColumnAlignment(3,"right");
      row++;
    }
    else
      T.add(iwrb.getLocalizedString("no_assessments","No assessments"),1,row);
    return T;
  }

  private PresentationObject getPreviewTable(FinanceHandler handler,TariffGroup group)throws java.rmi.RemoteException{
    Table T = new Table();
    T.setWidth("100%");
    if(handler !=null && group !=null){
      Collection L = handler.listOfAssessmentTariffPreviews(group.getID(),null,null);
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

  private Map mapOfAccountUsers(int iAssessmentRoundId){
    List L = FinanceFinder.getInstance().listOfAccountUsersByRoundId(iAssessmentRoundId);
    if(L!=null){
      Iterator iter = L.iterator();
      Map map = new java.util.HashMap();
      User u ;
      while(iter.hasNext()){
        u = (User) iter.next();
        map.put(new Integer(u.getID()),u);
      }
      return map;
    }
    return null;
  }

  private PresentationObject getTableOfAssessmentAccounts(IWContext iwc){
    Table T = new Table();
    String id = iwc.getParameter("ass_round_id");
    if(id != null){
      int ID = Integer.parseInt(id);
      List L = FinanceFinder.getInstance().listOfAccountsInfoInAssessmentRound(ID);
      //List L = null ;//CampusAccountFinder.listOfContractAccountApartmentsInAssessment(Integer.parseInt(id));
      if(L!= null){
        Map users = mapOfAccountUsers(ID);
        int len = L.size();
        T.add(Edit.formatText(iwrb.getLocalizedString("user_name","User name")),1,1);
        T.add(Edit.formatText(iwrb.getLocalizedString("account_name","Account name")),2,1);
        //T.add(Edit.titleText(iwrb.getLocalizedString("account_stamp","Last updated")),2,1);
        T.add(Edit.formatText(iwrb.getLocalizedString("last_updated","Last updated")),3,1);
        T.add(Edit.formatText(iwrb.getLocalizedString("balance","Balance")),4,1);

        int col = 1;
        int row = 2;
        AccountInfo A;
        java.text.NumberFormat nf=  java.text.NumberFormat.getCurrencyInstance(iwc.getCurrentLocale());
        float total = 0;
        String username;
        for (int i = 0; i < len; i++) {
          col = 1;
          username = "";
          A = (AccountInfo) L.get(i);

          if(users.containsKey(new Integer(A.getUserId())))
            username = ((User) users.get(new Integer(A.getUserId()))).getName();
          Link li = new Link(Edit.formatText(A.getName()));
          li.addParameter("ass_round_id",id);
          li.addParameter("ass_acc_id",A.getAccountId());
          li.addParameter(strAction,ACT7);
          T.add(Edit.formatText(username),col++,row);
          T.add(li,col++,row);
          T.add(Edit.formatText(new IWTimestamp(A.getLastUpdated()).getLocaleDate(iwc)),col++,row);
          T.add(Edit.formatText(nf.format(A.getBalance())),col++,row);
          total += A.getBalance();
          row++;
        }
        T.add(Edit.formatText(nf.format(total)),3,row);
        T.setWidth("100%");
        T.setCellpadding(2);
        T.setCellspacing(1);
        T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
        T.setRowColor(1,Edit.colorMiddle);
        T.setColumnAlignment(4,"right");
        row++;
      }
      else
        add("is empty");
    }
    return T;
  }

   private PresentationObject getTableOfAssessmentAccountEntries(IWContext iwc){
    Table T = new Table();
    String id = iwc.getParameter("ass_round_id");
    String acc_id = iwc.getParameter("ass_acc_id");
    if(id != null){
      int iAccountId = Integer.parseInt(acc_id);
      List L = FinanceFinder.getInstance().listOfAssessmentAccountEntries(iAccountId,Integer.parseInt(id));

      if(L!= null){
        int len = L.size();
        T.add(Edit.formatText(iwrb.getLocalizedString("entry_name","Entry name")),1,1);
        T.add(Edit.formatText(iwrb.getLocalizedString("entry_info","Info name")),2,1);
        //T.add(Edit.titleText(iwrb.getLocalizedString("account_stamp","Last updated")),2,1);
        T.add(Edit.formatText(iwrb.getLocalizedString("payment_date","Payment day")),3,1);
        T.add(Edit.formatText(iwrb.getLocalizedString("last_updated","Last updated")),4,1);
        T.add(Edit.formatText(iwrb.getLocalizedString("totals","Balance")),5,1);


        int col = 1;
        int row = 2;
        AccountEntry A;
        float total = 0;
        java.text.NumberFormat nf=  java.text.NumberFormat.getNumberInstance(iwc.getCurrentLocale());
        for (int i = 0; i < len; i++) {
          col = 1;

          A = (AccountEntry) L.get(i);
          T.add(Edit.formatText(A.getName()),col++,row);
          if(A.getInfo()!=null)
            T.add(Edit.formatText(A.getInfo()),col,row);
          col++;
          T.add(Edit.formatText(new IWTimestamp(A.getPaymentDate()).getLocaleDate(iwc)),col++,row);
          T.add(Edit.formatText(new IWTimestamp(A.getLastUpdated()).getLocaleDate(iwc)),col++,row);
          T.add(Edit.formatText(nf.format(A.getTotal())),col++,row);
          total+= A.getTotal();
          row++;
        }
        T.add(Edit.formatText(nf.format(total)),5,row);
        T.setWidth("100%");
        T.setCellpadding(2);
        T.setCellspacing(1);
        T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
        T.setRowColor(1,Edit.colorMiddle);
        T.setColumnAlignment(5,"right");
        row++;
      }
      else
        add("is empty");
    }
    return T;
  }

  private PresentationObject doMainTable(IWContext iwc,int iCategoryId ,int iGroupId, FinanceHandler handler){
    Form F = new Form();
    Table T = new Table();
    int iAccountCount = 0;
    if(handler!=null){
      iAccountCount = FinanceFinder.getInstance().countAccounts(iCategoryId,handler.getAccountType());
    }
    int row = 1;
    T.add(Edit.formatText(iwrb.getLocalizedString("number_of_accounts","Number of accounts")),1,row);
    T.add(String.valueOf(iAccountCount),2,row);
    row++;


    DateInput di = new DateInput("pay_date",true);
    di.setStyleAttribute("type",Edit.styleAttribute);
    IWTimestamp today = IWTimestamp.RightNow();
    di.setYearRange(today.getYear()-2,today.getYear()+3);
    today.addMonths(1);
    di.setDate(new IWTimestamp(1,today.getMonth(),today.getYear()).getSQLDate());

    DateInput start = new DateInput("start_date",true);
    start.setStyleAttribute("type",Edit.styleAttribute);
    IWTimestamp today1 = IWTimestamp.RightNow();
    start.setYearRange(today1.getYear()-2,today1.getYear()+3);
    today1.addMonths(1);
    start.setDate(new IWTimestamp(1,today.getMonth(),today.getYear()).getSQLDate());

    DateInput end = new DateInput("end_date",true);
    end.setStyleAttribute("type",Edit.styleAttribute);
    IWTimestamp today2 = IWTimestamp.RightNow();
    end.setYearRange(today2.getYear()-2,today2.getYear()+3);
    today2.addMonths(1);
    IWCalendar cal = new IWCalendar();
    int day = cal.getLengthOfMonth(today2.getMonth(),today2.getYear());
    end.setDate(new IWTimestamp(day,today2.getMonth(),today2.getYear()).getSQLDate());

    TextInput rn = new TextInput("round_name");

    Edit.setStyle(rn);
    SubmitButton sb = new SubmitButton("commit",iwrb.getLocalizedString( "commit","Commit"));
    Edit.setStyle(sb);

    DropdownMenu drpAccountKeys = drpAccountKeys(FinanceFinder.getInstance().listOfAccountKeys(),"account_key_id");
    Edit.setStyle(drpAccountKeys);

    T.add(Edit.formatText(iwrb.getLocalizedString("date_of_payment","Date of payment")),1,row);
    T.add(di,2,row);
    row++;

    T.add(Edit.formatText(iwrb.getLocalizedString("start_date","Start date")),1,row);
    T.add(start,2,row);
    row++;

    T.add(Edit.formatText(iwrb.getLocalizedString("end_date","End date")),1,row);
    T.add(end,2,row);
    row++;

    T.add(Edit.formatText(iwrb.getLocalizedString("account_key","Account key")),1,row);
    T.add(drpAccountKeys ,2,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("name_of_round","Assessment name")),1,row);
    T.add(rn,2,row);
    row++;
    T.add(sb,2,row);
    //sb.setOnClick("this.disabled = true");
    status.setMessageCaller(sb,iwrb.getLocalizedString("assessment_time","Assessment takes time"));

    sb.setOnClick("this.form.submit()");
    BusyBar bb = new BusyBar("busyguy");
    bb.setInterfaceObject(sb);
    T.add(bb,2,row);
    row++;


    T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    T.setRowColor(1,Edit.colorMiddle);
    T.mergeCells(1,1,2,1);
    T.setWidth("100%");
    F.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    F.add(new HiddenInput(prmGroup,String.valueOf(iGroupId)));
    F.add(T);
    return F;
  }

  private DropdownMenu drpAccountKeys(List AK,String name){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(-1,"--");
    if(AK != null){
      drp.addMenuElements(AK);
    }
    return drp;
  }


  private Link getRoundLink(String name,int id,int iGroupId){
    Link L = new Link(name);
    L.addParameter(strAction,ACT4);
    L.addParameter("ass_round_id",id);
    L.addParameter(prmGroup,iGroupId);
    L.setFontSize(Edit.textFontSize);
    return L;
  }


  public void main(IWContext iwc){
    if(status==null)
      status = new StatusBar("ass_status");
    status.setStyle("color: #ff0000;  font-style: normal; font-family: verdana; font-weight: normal; font-size:14px;");
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }

  public Object clone() {
    TariffAssessments obj = null;
    try {
      obj = (TariffAssessments)super.clone();
      if(this.status!=null)
        obj.status = this.status;
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }

}
