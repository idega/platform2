package com.idega.block.finance.presentation;

import com.idega.block.finance.data.*;
import com.idega.block.finance.business.AccountManager;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import java.text.NumberFormat;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.StringTokenizer;
import com.idega.block.login.business.LoginBusiness;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class AccountViewer extends com.idega.jmodule.object.ModuleObjectContainer {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private boolean isAdmin,isLoggedOn;

  private String sHeaderColor,sDarkColor,sLightColor,sWhiteColor;
  private String sTextColor,sHeaderTextColor;
  private String sDebetColor,sKreditColor;
  private NumberFormat NF ;
  private final String prmFromDate = "from_date",prmToDate = "to_date";
  public static final String prmUserId = "user_id",prmAccountId = "account_id";
  private List listAccount = null;
  private User eUser = null;
  protected String styleAttribute = "font-size: 8pt";
  private int iUserId = -1,iAccountId = -1;

  public AccountViewer(){
    this(-1);
  }

  public AccountViewer(User eUser){
    this(eUser.getID());
  }

  public AccountViewer(int iUserId){
    sHeaderColor =  "#942829";
    sDarkColor =  "#27324B";
    sLightColor = "#ECEEF0";
    sTextColor = "#000000";
    sHeaderTextColor = "#FFFFFF";
    sDebetColor = "0000FF";
    sKreditColor = "#FF0000";
    sWhiteColor = "#FFFFFF";
    NF = java.text.NumberFormat.getInstance();
    this.iUserId = iUserId;
  }

  public void control( ModuleInfo modinfo ){
    checkIds(modinfo);
    idegaTimestamp itFromDate = getFromDate(modinfo);
    idegaTimestamp itToDate = getToDate(modinfo);
    if(isAdmin || isLoggedOn){
      if(listAccount != null){
        if(iAccountId <= 0)
          iAccountId = ((Account)listAccount.get(0)).getID();

        add(getAccountView(iAccountId,listAccount,itFromDate,itToDate,isAdmin));
      }
      else
        add("no_accounts");
    }
    else{
      add(iwrb.getLocalizedString("accessdenied","Access denied"));
    }
  }

  public ModuleObject getMainTable(ModuleInfo modinfo){
    return new Text();
  }

  public ModuleObject getAccountView(int iAccountId,List listAccount,idegaTimestamp FromDate,idegaTimestamp ToDate,boolean showallkeys){
    Table T = new Table(1,3);
    T.setWidth("100%");
    T.add(getEntrySearchTable(iAccountId,listAccount,FromDate,ToDate),1,2);
    T.add(getAccountTable(iAccountId),1,2);
    T.add(getEntryTable(iAccountId,FromDate,ToDate,showallkeys),1,3);
    return T;
  }

  public ModuleObject getEntrySearchTable(int iAccountId,List listAccount,idegaTimestamp from,idegaTimestamp to){
    Table T = new Table();
    T.setWidth("100%");
    String sFromDate = from.getISLDate(".",true);
    String sToDate =  to.getISLDate(".",true);

    Form myForm = new Form();
    DropdownMenu drpAccounts = new DropdownMenu(listAccount,prmAccountId);
    drpAccounts.setToSubmit();
    drpAccounts.setAttribute("style",styleAttribute);
    drpAccounts.setSelectedElement(String.valueOf(iAccountId));
    TextInput tiFromDate = new TextInput(prmFromDate,sFromDate);
    tiFromDate.setLength(10);
    tiFromDate.setAttribute("style",styleAttribute);
    TextInput tiToDate = new TextInput(prmToDate,sToDate);
    tiToDate.setLength(10);
    tiToDate.setAttribute("style",styleAttribute);
    SubmitButton fetch = new SubmitButton("fetch",iwrb.getLocalizedString("fetch","Fetch"));
    fetch.setAttribute("style",styleAttribute);
    int row = 1;
    T.add(drpAccounts,1,row);
    T.add(tiFromDate,1,row);
    T.add(tiToDate,1,row);
    T.add(fetch,1,row);
    T.mergeCells(1,row,4,row);

    myForm.add(T);
    return myForm;
  }

  public ModuleObject getAccountTable(int AccountId){
    Account eAccount = null;
    if(AccountId > 0){
      try {
        eAccount = new Account(AccountId);
        eUser = new User(eAccount.getUserId());
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }

    Table T = new Table(4,3);
    if(eAccount != null){
      T.setWidth("100%");
      T.setCellspacing(0);
      T.setCellpadding(2);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"left");
      T.setColumnAlignment(3,"right");
      T.setColumnAlignment(4,"right");

      T.setRowColor(1,sWhiteColor);
      T.setRowColor(2,sHeaderColor);
      T.setRowColor(3,sWhiteColor);

      String fontColor = sHeaderColor;
      int fontSize = 1;
      int row = 1;

      Text Title = new Text(iwrb.getLocalizedString("account","Account"),true,false,false);
      Title.setFontColor(sHeaderColor);
      T.add(Title,1,row);

      row = 2;
      Text[] TableTitles = new Text[4];
      TableTitles[0] = new Text(iwrb.getLocalizedString("account","Account"));
      TableTitles[1] = new Text(iwrb.getLocalizedString("owner","Owner"));
      TableTitles[2] = new Text(iwrb.getLocalizedString("lastentry","Last Entry"));
      TableTitles[3] = new Text(iwrb.getLocalizedString("balance","Balance"));

      Text[] TableTexts = new Text[4];
      TableTexts[0] = new Text(eAccount.getName());
      TableTexts[1] = new Text(eUser.getName());
      TableTexts[2] = new Text(new idegaTimestamp(eAccount.getLastUpdated()).getISLDate(".",true));
      float b = eAccount.getBalance();
      boolean debet = b > 0?true:false;
      TableTexts[3] = new Text(NF.format( (double) b));

      for(int i = 0 ; i < 4;i++){
        TableTitles[i].setFontSize(fontSize);
        TableTitles[i].setFontColor(sWhiteColor);
        T.add(TableTitles[i],i+1,row);
        TableTexts[i].setFontSize(fontSize);
        TableTexts[i].setFontColor(sTextColor);
        if(i == 3){
            if(debet) TableTexts[i].setFontColor(sDebetColor);
            else TableTexts[i].setFontColor(sKreditColor);
          }
        else
          TableTexts[i].setFontColor(sTextColor);
          T.add(TableTexts[i],i+1,row+1);
      }
    }
    else{
      T.add(iwrb.getLocalizedString("no_account","No Account"));
    }
    return T;
  }

  private ModuleObject getEntryTable(int iAccountId,idegaTimestamp from,idegaTimestamp to,boolean showallkeys){
    ModuleObject mo = null;
    try{
      Account a = new Account(iAccountId);
      mo =  getEntryTable(a, from, to, showallkeys);
    }
    catch(SQLException ex){
      ex.printStackTrace();
      mo = new Text();
    }
    return mo;
  }

  private ModuleObject getEntryTable(Account eAccount,idegaTimestamp from,idegaTimestamp to,boolean showallkeys){
    List listEntries = null;
    if(eAccount.getType().equals(Account.typeFinancial)){
      if(showallkeys)
        listEntries = AccountManager.listOfAccountEntries(eAccount.getID(),from,to);
      else
        listEntries = AccountManager.listOfKeySortedEntries(eAccount.getID(),from,to);
      return getFinanceEntryTable(listEntries);
    }
    else if(eAccount.getType().equals(Account.typePhone)){
      listEntries = AccountManager.listOfPhoneEntries(eAccount.getID(),from,to);
      return getPhoneEntryTable(listEntries);
    }
    else return new Text();
  }

  private ModuleObject getPhoneEntryTable(List listEntries){
    int tableDepth = 3;
    int cols = 9;
    if(listEntries != null){
      tableDepth += listEntries.size();
    }

    Table T = new Table(9,tableDepth);
    T.setWidth("100%");
    T.setWidth(1,"65");
    T.setCellspacing(0);
    T.setCellpadding(2);
    T.setColumnAlignment(1,"right");
    T.setColumnAlignment(2,"left");
    T.setColumnAlignment(3,"left");
    T.setColumnAlignment(4,"right");
    T.setColumnAlignment(cols,"right");
    T.setAlignment(1,1,"left");
    T.setAlignment(1,2,"left");
    T.setWidth(1,"20");
   // T.setWidth(2,"40%");
   // T.setWidth(3,"60%");
    T.setWidth(cols,"25");
    T.setWidth("100%");

    T.setHorizontalZebraColored(sLightColor,sWhiteColor);
    T.setRowColor(1,sWhiteColor);
    T.setRowColor(2,sHeaderColor);

    String fontColor = sWhiteColor;
    int fontSize = 1;

    Text Title = new Text(iwrb.getLocalizedString("entries","Entries"),true,false,false);
    Title.setFontColor(sHeaderColor);
    T.add(Title,1,1);
    T.mergeCells(1,1,4,1);

    Text[] TableTitles = new Text[cols];
    TableTitles[0] = new Text(iwrb.getLocalizedString("date","Date"));
    TableTitles[1] = new Text(iwrb.getLocalizedString("anumber","A-Number"));
    TableTitles[2] = new Text(iwrb.getLocalizedString("subnumber","Sub-Number"));
    TableTitles[3] = new Text(iwrb.getLocalizedString("number","Number"));
    TableTitles[4] = new Text(iwrb.getLocalizedString("dating","Dating"));
    TableTitles[5] = new Text(iwrb.getLocalizedString("night_time","Night time"));
    TableTitles[6] = new Text(iwrb.getLocalizedString("day_time","Day time"));
    TableTitles[7] = new Text(iwrb.getLocalizedString("time","Time"));
    TableTitles[8] = new Text(iwrb.getLocalizedString("amount","Amount"));


    for(int i = 0 ; i < TableTitles.length;i++){
      TableTitles[i].setFontSize(fontSize);
      TableTitles[i].setFontColor(sWhiteColor);
      T.add(TableTitles[i],i+1,2);
    }

    Text[] TableTexts = new Text[cols];
    boolean debet = false;
    if(listEntries != null){
      int len = listEntries.size();
      int totNight = 0,totDay = 0,totDur = 0;
      float totPrice = 0;
      for(int j = 0; j < len; j++){
        AccountPhoneEntry entry = (AccountPhoneEntry) listEntries.get(j);
        TableTexts[0] = new Text(new idegaTimestamp(entry.getLastUpdated()).getISLDate(".",true));
        TableTexts[1] = new Text(entry.getMainNumber());
        TableTexts[2] = new Text(entry.getSubNumber());
        TableTexts[3] = new Text(entry.getPhonedNumber());
        TableTexts[4] = new Text(new idegaTimestamp(entry.getPhonedStamp()).toSQLString());
        TableTexts[5] = new Text(new java.sql.Time(entry.getNightDuration()*1000).toString());
        TableTexts[6] = new Text(new java.sql.Time(entry.getDayDuration()*1000).toString());
        TableTexts[7] = new Text(new java.sql.Time(entry.getDuration()*1000).toString());
        totNight += entry.getNightDuration();
        totDay += entry.getDayDuration();
        totDur += entry.getDuration();
        float p = entry.getPrice();
        totPrice += p;
        debet = p >= 0 ? true : false ;
        TableTexts[8] = new Text(NF.format(p));

        for(int i = 0 ; i < cols;i++){
          TableTexts[i].setFontSize(fontSize);
          TableTexts[i].setFontColor("#000000");
          if(i == 8){
            if(debet) TableTexts[i].setFontColor(sDebetColor);
            else TableTexts[i].setFontColor(sKreditColor);
          }
          else
            TableTexts[i].setFontColor("#000000");
          T.add(TableTexts[i],i+1,j+3);
        }

      }
      Text txTotNight = new Text(new java.sql.Time(totNight*1000).toString());
      Text txTotDay = new Text(new java.sql.Time(totDay*1000).toString());
      Text txTotDur = new Text(new java.sql.Time(totDur*1000).toString());
      Text txTotPrice = new Text(NF.format(totPrice));
      txTotNight.setFontColor("#000000");
      txTotDay.setFontColor("#000000");
      txTotDur.setFontColor("#000000");
      if(totPrice >= 0 )
        txTotPrice.setFontColor(sDebetColor);
      else
        txTotPrice.setFontColor(sKreditColor);
      txTotNight.setFontSize(fontSize);
      txTotDay.setFontSize(fontSize);
      txTotDur.setFontSize(fontSize);
      txTotPrice.setFontSize(fontSize);

      T.add(txTotNight,6,tableDepth);
      T.add(txTotDay,7,tableDepth);
      T.add(txTotDur,8,tableDepth);
      T.add(txTotPrice,9,tableDepth);
    }
    return T;
  }

  private ModuleObject getFinanceEntryTable(List listEntries){

    int tableDepth = 3;
    if(listEntries != null){
      tableDepth += listEntries.size();
    }

    Table T = new Table(4,tableDepth);
    T.setWidth("100%");
    //T.setWidth(1,"65");
    T.setCellspacing(0);
    T.setCellpadding(2);
    T.setColumnAlignment(1,"right");
    T.setColumnAlignment(2,"left");
    T.setColumnAlignment(3,"left");
    T.setColumnAlignment(4,"right");
    T.setAlignment(1,1,"left");
    T.setAlignment(1,2,"left");
    T.setWidth(1,"20");
    //T.setWidth(2,"40%");
    //T.setWidth(3,"60%");
    //T.setWidth(4,"25");

    T.setHorizontalZebraColored(sLightColor,sWhiteColor);
    T.setRowColor(1,sWhiteColor);
    T.setRowColor(2,sHeaderColor);

    String fontColor = sWhiteColor;
    int fontSize = 1;

    Text Title = new Text(iwrb.getLocalizedString("entries","Entries"),true,false,false);
    Title.setFontColor(sHeaderColor);
    T.add(Title,1,1);
    T.mergeCells(1,1,4,1);

    Text[] TableTitles = new Text[4];
    TableTitles[0] = new Text(iwrb.getLocalizedString("date","Date"));
    TableTitles[1] = new Text(iwrb.getLocalizedString("description","Description"));
    TableTitles[2] = new Text(iwrb.getLocalizedString("text","Text"));
    TableTitles[3] = new Text(iwrb.getLocalizedString("amount","Amount"));

    for(int i = 0 ; i < TableTitles.length;i++){
      TableTitles[i].setFontSize(fontSize);
      TableTitles[i].setFontColor(sWhiteColor);
      T.add(TableTitles[i],i+1,2);
    }

    Text[] TableTexts = new Text[4];
    boolean debet = false;
    if(listEntries != null){
      int len = listEntries.size();
      float totPrice = 0;
      for(int j = 0; j < len; j++){
        AccountEntry entry = (AccountEntry) listEntries.get(j);
        TableTexts[0] = new Text(new idegaTimestamp(entry.getLastUpdated()).getISLDate(".",true));
        TableTexts[1] = new Text(entry.getName());
        TableTexts[2] = new Text(entry.getInfo());
        float p = entry.getPrice();
        debet = p > 0 ? true : false ;
        totPrice += p;
        TableTexts[3] = new Text(NF.format(p));

        for(int i = 0 ; i < 4;i++){
          TableTexts[i].setFontSize(fontSize);
          TableTexts[i].setFontColor("#000000");
          if(i == 3){
            if(debet) TableTexts[i].setFontColor(sDebetColor);
            else TableTexts[i].setFontColor(sKreditColor);
          }
          else
            TableTexts[i].setFontColor("#000000");
          T.add(TableTexts[i],i+1,j+3);
        }
      }
      Text txTotPrice = new Text(NF.format(totPrice));
      txTotPrice.setFontSize(fontSize);
      if(totPrice >= 0 )
        txTotPrice.setFontColor(sDebetColor);
      else
        txTotPrice.setFontColor(sKreditColor);
      T.add(txTotPrice,4,tableDepth);
    }
    return T;
  }

  private idegaTimestamp getFromDate(ModuleInfo modinfo){
    if(modinfo.getParameter(prmFromDate)!=null){
      String sFromDate = modinfo.getParameter(prmFromDate);
      return parseStamp(sFromDate);
    }
    else{
       idegaTimestamp today =  idegaTimestamp.RightNow();
       return new idegaTimestamp(1,today.getMonth(),today.getYear());
    }
  }

  private idegaTimestamp getToDate(ModuleInfo modinfo){
    if(modinfo.getParameter(prmToDate)!=null){
      String sToDate = modinfo.getParameter(prmToDate);
      return parseStamp(sToDate);
    }
    else{
      return idegaTimestamp.RightNow();
    }
  }

  private void checkIds(ModuleInfo modinfo){
    if(iUserId == -1){
      if(modinfo.getParameter(prmUserId)!=null){
        iUserId = Integer.parseInt(modinfo.getParameter(prmUserId));
      }
      else if(isLoggedOn){
        iUserId = LoginBusiness.getUser(modinfo).getID();
      }
    }
    if( modinfo.getParameter(prmAccountId)!=null ){
      iAccountId = Integer.parseInt(modinfo.getParameter(prmAccountId));
    }
    if( iUserId != -1 && !isAdmin){
      try {
        eUser = new User(iUserId);
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
      listAccount = AccountManager.listOfAccounts(iUserId);
    }
    else if(isAdmin){
      listAccount = AccountManager.listOfAccounts();
    }
  }

  private idegaTimestamp parseStamp(String sDate){
       idegaTimestamp it = new idegaTimestamp();
       try{
        StringTokenizer st = new StringTokenizer(sDate," .-/+");
        int day = 1,month = 1,year = 2001;
        if(st.hasMoreTokens()){
          day = Integer.parseInt(st.nextToken());
          month = Integer.parseInt(st.nextToken());
          year = Integer.parseInt(st.nextToken());

        }
        it = new idegaTimestamp(day,month,year);
      }
      catch(Exception pe){ it = new idegaTimestamp();}
      return it;
  }

  public void main( ModuleInfo modinfo ) {
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    try{
      isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
      isLoggedOn = com.idega.block.login.business.LoginBusiness.isLoggedOn(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    control(modinfo);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}// class AccountViewer