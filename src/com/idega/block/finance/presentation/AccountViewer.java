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
      int fontSize = 2;
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

  private Table getEntryTable(int AccountId,idegaTimestamp from,idegaTimestamp to,boolean showallkeys){
    List listEntries = null;
    if(showallkeys)
      listEntries = AccountManager.listOfAccountEntries(AccountId,from,to);
    else
      listEntries = AccountManager.listOfKeySortedEntries(AccountId,from,to);

    int tableDepth = 2;
    if(listEntries != null){
      tableDepth += listEntries.size();
    }

    Table T = new Table(4,tableDepth);
    T.setWidth("100%");
    T.setWidth(1,"65");
    T.setCellspacing(0);
    T.setCellpadding(2);
    T.setColumnAlignment(1,"right");
    T.setColumnAlignment(2,"left");
    T.setColumnAlignment(3,"left");
    T.setColumnAlignment(4,"right");
    T.setAlignment(1,1,"left");
    T.setAlignment(1,2,"left");
    T.setWidth(1,"20");
    T.setWidth(2,"40%");
    T.setWidth(3,"60%");
    T.setWidth(4,"20");

    T.setHorizontalZebraColored(sLightColor,sWhiteColor);
    T.setRowColor(1,sWhiteColor);
    T.setRowColor(2,sHeaderColor);

    String fontColor = sWhiteColor;
    int fontSize = 2;

    Text Title = new Text(iwrb.getLocalizedString("movement","Movement"),true,false,false);
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
      for(int j = 0; j < len; j++){
        AccountEntry entry = (AccountEntry) listEntries.get(j);
        TableTexts[0] = new Text(new idegaTimestamp(entry.getLastUpdated()).getISLDate(".",true));
        TableTexts[1] = new Text(entry.getName());
        TableTexts[2] = new Text(entry.getInfo());
        int p = entry.getPrice();
        debet = p > 0 ? true : false ;
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