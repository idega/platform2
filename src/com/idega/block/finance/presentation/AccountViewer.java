package com.idega.block.finance.presentation;

import com.idega.block.finance.data.*;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import java.text.NumberFormat;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Image;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;
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

public class AccountViewer extends com.idega.presentation.PresentationObjectContainer {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private boolean isAdmin,isLoggedOn;

  private Image image ;

  private String sHeaderColor,sDarkColor,sLightColor,sWhiteColor;
  private String sTextColor,sHeaderTextColor;
  private String sDebetColor,sKreditColor;
  private NumberFormat NF ;
  private final String prmFromDate = "from_date",prmToDate = "to_date";
  public static final String prmUserId = "user_id",prmAccountId = "account_id";
  public static final String prmClean = "av_clean";
  private List listAccount = null;
  private User eUser = null;
  protected String styleAttribute = "font-size: 8pt";
  private int iUserId = -1,iAccountId = -1;
  private boolean specialview = false;

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

  public void setColors(String HeaderColor,String DarkColor){
    sHeaderColor =  "#942829";
    sDarkColor =  "#27324B";
  }

  private void control( IWContext iwc ){
    image = Table.getTransparentCell(iwc);
    image.setHeight(6);

    checkIds(iwc);
    idegaTimestamp itFromDate = getFromDate(iwc);
    idegaTimestamp itToDate = getToDate(iwc);
    specialview = iwc.isParameterSet("specview");
    boolean clean = iwc.isParameterSet(prmClean);
    if(isAdmin || isLoggedOn){
      if(listAccount != null){
        if(iAccountId <= 0)
          iAccountId = ((Account)listAccount.get(0)).getID();

        add(getAccountView(iAccountId,listAccount,itFromDate,itToDate,isAdmin,clean));
      }
      else
        add("no_accounts");
    }
    else{
      add(iwrb.getLocalizedString("accessdenied","Access denied"));
    }
  }

  public PresentationObject getMainTable(IWContext iwc){
    return new Text();
  }

  public PresentationObject getAccountView(int iAccountId,List listAccount,idegaTimestamp FromDate,idegaTimestamp ToDate,boolean showallkeys,boolean clean){
    Table T = new Table(1,3);
    T.setWidth("100%");
    T.add(getEntrySearchTable(iAccountId,listAccount,FromDate,ToDate),1,2);
    if(clean){
      T.add(getCleanAccountTable(iAccountId),1,2);
      T.add(getEntryTable(iAccountId,FromDate,ToDate,showallkeys,true),1,3);
    }
    else{
      T.add(getAccountTable(iAccountId),1,2);
      T.add(getEntryTable(iAccountId,FromDate,ToDate,showallkeys,false),1,3);
    }
    return T;
  }

  private String getDateString(idegaTimestamp stamp){
    return stamp.getISLDate(".",true);
  }

  public PresentationObject getEntrySearchTable(int iAccountId,List listAccount,idegaTimestamp from,idegaTimestamp to){
    Table T = new Table(6,2);
    T.setWidth("100%");
    String sFromDate = getDateString(from);
    String sToDate =  getDateString(to);

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


    CheckBox specialCheck = new CheckBox("specview");
    if(specialview)
      specialCheck.setChecked(true);

    Link printable = new Link(formatText("printable",1,null));
    printable.setURL(IWMainApplication.getObjectInstanciatorURL(AccountViewer.class));
    printable.addParameter(prmFromDate,getDateString(from));
    printable.addParameter(prmToDate,getDateString(to));
    printable.addParameter(prmAccountId,iAccountId);
    printable.addParameter(prmClean,"true");
    printable.setWindowToOpen(AccountWindow.class);



    int row = 1;

    T.add(formatText(iwrb.getLocalizedString("account","Account")),1,row);
    T.add(formatText(iwrb.getLocalizedString("from","From")),2,row);
    T.add(formatText(iwrb.getLocalizedString("to","To")),3,row);
    T.add(formatText(iwrb.getLocalizedString("special","Special")),4,row);

    row++;
    T.add(drpAccounts,1,row);
    T.add(tiFromDate,2,row);
    T.add(tiToDate,3,row);
    T.add(specialCheck,4,row);
    T.add(fetch,5,row);

    T.setWidth(6,row,"100%");

    myForm.add(new HiddenInput(IWMainApplication.classToInstanciateParameter,"com.idega.block.finance.presentation.AccountViewer"));
    myForm.add(T);

    return myForm;
  }

  public PresentationObject getAccountTable(int AccountId){
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

      T.setWidth("100%");
      T.setCellspacing(1);
      T.setCellpadding(3);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"left");
      T.setColumnAlignment(3,"right");
      T.setColumnAlignment(4,"right");

      T.setRowColor(1,FinanceColors.DARKBLUE);
      T.setRowColor(2,FinanceColors.DARKGREY);
      T.setRowColor(3,FinanceColors.WHITE);

      T.mergeCells(1,1,4,1);

      String fontColor = sHeaderColor;
      int fontSize = 1;
      int row = 1;

      Text Title = new Text(iwrb.getLocalizedString("account","Account"),true,false,false);
      Title.setFontColor(FinanceColors.WHITE);
      T.add(Title,1,row);
      if(eAccount != null){
      row = 2;
      Text[] TableTitles = new Text[4];
      TableTitles[0] = new Text(iwrb.getLocalizedString("account","Account"));
      TableTitles[1] = new Text(iwrb.getLocalizedString("owner","Owner"));
      TableTitles[2] = new Text(iwrb.getLocalizedString("lastentry","Last Entry"));
      TableTitles[3] = new Text(iwrb.getLocalizedString("balance","Balance"));

      Text[] TableTexts = new Text[4];
      TableTexts[0] = new Text(eAccount.getName());
      TableTexts[1] = new Text(eUser.getName());
      TableTexts[2] = new Text(getDateString(new idegaTimestamp(eAccount.getLastUpdated())));
      float b = eAccount.getBalance();
      boolean debet = b > 0?true:false;
      TableTexts[3] = new Text(NF.format( (double) b));

      for(int i = 0 ; i < 4;i++){
        TableTitles[i].setFontSize(fontSize);
        //TableTitles[i].setFontColor(sWhiteColor);
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

   public PresentationObject getCleanAccountTable(int AccountId){
    Account eAccount = FinanceFinder.getAccount(AccountId);
    if(eAccount !=null)
      eUser = FinanceFinder.getUser(eAccount.getUserId());

    Table T = new Table(2,4);
    if(eAccount != null){
      T.setWidth("100%");
      T.setCellspacing(0);
      T.setCellpadding(2);

      String fontColor = sHeaderColor;
      int fontSize = 1;
      int row = 1;

      T.add(formatText(iwrb.getLocalizedString("account","Account"),fontSize,null),1,row);
      T.add(formatText(eAccount.getName(),fontSize,null),2,row);
      row++;
      T.add(formatText(iwrb.getLocalizedString("owner","Owner"),fontSize,null),1,row);
      T.add(formatText(eUser.getName(),fontSize,null),2,row);
      row++;
      T.add(formatText(iwrb.getLocalizedString("lastentry","Last Entry"),fontSize,null),1,row);
      T.add(formatText(getDateString(new idegaTimestamp(eAccount.getLastUpdated())),fontSize,null),2,row);
      row++;
      T.add(formatText(iwrb.getLocalizedString("balance","Balance"),fontSize,null),1,row);
      float b = eAccount.getBalance();
      boolean debet = b > 0?true:false;
      T.add(formatText(NF.format( (double) b),fontSize,null),2,row);
      row++;

    }
    else{
      T.add(iwrb.getLocalizedString("no_account","No Account"));
    }
    return T;
  }

  private PresentationObject getEntryTable(int iAccountId,idegaTimestamp from,idegaTimestamp to,boolean showallkeys,boolean clean){
    PresentationObject mo = null;
    try{
      Account a = new Account(iAccountId);
      mo =  getEntryTable(a, from, to, showallkeys,clean);
    }
    catch(SQLException ex){
      ex.printStackTrace();
      mo = new Text();
    }
    return mo;
  }

  private PresentationObject getEntryTable(Account eAccount,idegaTimestamp from,idegaTimestamp to,boolean showallkeys,boolean clean){
    List listEntries = null;
    if(eAccount.getType().equals(Account.typeFinancial)){
      if(showallkeys)
        listEntries = AccountManager.listOfAccountEntries(eAccount.getID(),from,to);
      else
        listEntries = AccountManager.listOfKeySortedEntries(eAccount.getID(),from,to);
      if(clean)
        return getCleanFinanceEntryTable(listEntries);
      else
        return getFinanceEntryTable(listEntries);
    }
    else if(eAccount.getType().equals(Account.typePhone)){
      listEntries = AccountManager.listOfPhoneEntries(eAccount.getID(),from,to);
      if(specialview)
        return getPhoneEntryReportTable(listEntries);
      else
        return getPhoneEntryTable(listEntries);
    }
    else return new Text();
  }

  private PresentationObject getPhoneEntryTable(List listEntries){
    int tableDepth = 4;
    int cols = 9;
    if(listEntries != null){
      tableDepth += listEntries.size();
    }

    int row = 1;

    Table T = new Table(cols,tableDepth);
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

    T.setHorizontalZebraColored(FinanceColors.LIGHTGREY,FinanceColors.WHITE);
    T.setRowColor(1,FinanceColors.DARKBLUE);
    T.setRowColor(2,FinanceColors.DARKGREY);

    String fontColor = sWhiteColor;
    int fontSize = 1;

    Text Title = new Text(iwrb.getLocalizedString("entries","Entries"),true,false,false);
    Title.setFontColor(FinanceColors.WHITE);
    T.add(Title,1,row);
    T.mergeCells(1,row,cols,row);
    row++;
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
      //TableTitles[i].setFontColor(sWhiteColor);
      T.add(TableTitles[i],i+1,row);
    }
    row++;
    Text[] TableTexts = new Text[cols];
    boolean debet = false;
    if(listEntries != null){
      int len = listEntries.size();
      int totNight = 0,totDay = 0,totDur = 0;
      float totPrice = 0;
      for(int j = 0; j < len; j++){
        AccountPhoneEntry entry = (AccountPhoneEntry) listEntries.get(j);
        TableTexts[0] = new Text(getDateString(new idegaTimestamp(entry.getLastUpdated())));
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
          T.add(TableTexts[i],i+1,row);
        }
        row++;
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

      T.add(txTotNight,6,row);
      T.add(txTotDay,7,row);
      T.add(txTotDur,8,row);
      T.add(txTotPrice,9,row);
    }
    T.mergeCells(1,row,cols,row);
    T.add(image,1,row);
    T.setColor(1,row,FinanceColors.DARKRED);
    return T;
  }

  private PresentationObject getPhoneEntryReportTable(List listEntries){
    int tableDepth = 3;
    Vector other = new Vector();
    Vector mobile = new Vector();
    Vector foreign = new Vector();

    String sMob1 = "8";
    String sMob2 = "6";
    String sFor = "00";
    float tax = 0.245f;

    Table T = new Table(5,9);
    T.setWidth("100%");
    T.setCellpadding(0);
    T.setCellspacing(0);


    T.setHorizontalZebraColored(FinanceColors.WHITE,FinanceColors.LIGHTGREY);
    T.setRowColor(1,FinanceColors.DARKBLUE);
    T.setRowColor(2,FinanceColors.LIGHTGREY);
    T.setColumnAlignment(3,"right");
    T.setColumnAlignment(4,"right");
    T.setColumnAlignment(5,"right");

    String fontColor = sWhiteColor;
    int fontSize = 1;

    Text Title = new Text(iwrb.getLocalizedString("entries","Entries"),true,false,false);
    Title.setFontColor(FinanceColors.WHITE);
    T.add(Title,1,1);

    T.add(formatText(iwrb.getLocalizedString("type","Type"),fontSize,fontColor),2,2);
    T.add(formatText(iwrb.getLocalizedString("count","Count"),fontSize,fontColor),3,2);
    T.add(formatText(iwrb.getLocalizedString("duration","Duration"),fontSize,fontColor),4,2);
    T.add(formatText(iwrb.getLocalizedString("amount","amount"),fontSize,fontColor),5,2);

    if(listEntries != null){
      Iterator IT = listEntries.iterator();
      String phonedNumber;
      long otherTime = 0,forTime = 0,mobTime = 0,totalTime = 0;
      float otherPrice= 0,forPrice= 0,mobPrice= 0,totalPrice = 0;
      int otherCount = 0, forCount =0,mobCount = 0,totalCount = 0;
      AccountPhoneEntry entry;
      while(IT.hasNext()){
        entry = (AccountPhoneEntry) IT.next();
        phonedNumber = entry.getPhonedNumber();
        if(phonedNumber.startsWith(sFor)){
          forTime += entry.getDuration();
          forPrice += entry.getPrice();
          forCount++;
        }
        //mobile
        else if(phonedNumber.startsWith(sMob1) || phonedNumber.startsWith(sMob2)){
          mobTime += entry.getDuration();
          mobPrice += entry.getPrice();
          mobCount++;
        }
        else{
          otherTime += entry.getDuration();
          otherPrice += entry.getPrice();
          otherCount++;
        }
      }
      totalCount = otherCount + mobCount + forCount;
      totalPrice = otherPrice + mobPrice + forPrice;
      totalTime = otherTime + mobTime + forTime;
      float taxprice = totalPrice * tax;
      T.add(formatText(iwrb.getLocalizedString("other","Other")),2,3);
      T.add(formatText(iwrb.getLocalizedString("mobile","Mobile")),2,4);
      T.add(formatText(iwrb.getLocalizedString("foreign","Foreign")),2,5);
      T.add(formatText(iwrb.getLocalizedString("total","Total")),2,6);
      //new java.sql.Time(entry.getDuration()*1000).toString()
      T.add(formatText(String.valueOf(otherCount)),3,3);
      T.add(formatText(String.valueOf(mobCount)),3,4);
      T.add(formatText(String.valueOf(forCount)),3,5);
      T.add(formatText(String.valueOf(totalCount)),3,6);

      T.add(formatText(new java.sql.Time(otherTime*1000).toString()),4,3);
      T.add(formatText(new java.sql.Time(mobTime*1000).toString()),4,4);
      T.add(formatText(new java.sql.Time(forTime*1000).toString()),4,5);
      T.add(formatText(new java.sql.Time(totalTime*1000).toString()),4,6);

      T.add(formatText(NF.format(otherPrice)),5,3);
      T.add(formatText(NF.format(mobPrice)),5,4);
      T.add(formatText(NF.format(forPrice)),5,5);
      T.add(formatText(NF.format(totalPrice)),5,6);
      T.add(Text.getNonBrakingSpace(),4,7);
      T.add(formatText(iwrb.getLocalizedString("tax","Tax")),4,8);
      T.add(formatText(NF.format(taxprice)),5,8);
      T.add(formatText(iwrb.getLocalizedString("amount","Amount")),4,9);
      T.add(formatText(NF.format(totalPrice+taxprice)),5,9);


      T.setLineAfterRow(5);

    }
    return T;
  }

  private PresentationObject getFinanceEntryTable(List listEntries){

    int tableDepth = 5;
    if(listEntries != null){
      tableDepth += listEntries.size();
    }
    int row = 1;
    Table T = new Table(4,tableDepth);
    T.setWidth("100%");
    //T.setWidth(1,"65");
    T.setCellspacing(1);
    T.setCellpadding(3);
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

    T.setHorizontalZebraColored(FinanceColors.WHITE,FinanceColors.LIGHTGREY);
    T.setRowColor(1,FinanceColors.DARKBLUE);
    T.setRowColor(2,FinanceColors.DARKGREY);

    String fontColor = sWhiteColor;
    int fontSize = 1;

    Text Title = new Text(iwrb.getLocalizedString("entries","Entries"),true,false,false);
    Title.setFontColor(FinanceColors.WHITE);
    T.add(Title,1,row);
    T.mergeCells(1,row,4,row);
    row++;
    Text[] TableTitles = new Text[4];
    TableTitles[0] = new Text(iwrb.getLocalizedString("date","Date"));
    TableTitles[1] = new Text(iwrb.getLocalizedString("description","Description"));
    TableTitles[2] = new Text(iwrb.getLocalizedString("text","Text"));
    TableTitles[3] = new Text(iwrb.getLocalizedString("amount","Amount"));

    for(int i = 0 ; i < TableTitles.length;i++){
      TableTitles[i].setFontSize(fontSize);
      //TableTitles[i].setFontColor(sWhiteColor);
      T.add(TableTitles[i],i+1,row);
    }
    row++;
    Text[] TableTexts = new Text[4];
    boolean debet = false;
    if(listEntries != null){
      int len = listEntries.size();
      float totPrice = 0;
      for(int j = 0; j < len; j++){
        AccountEntry entry = (AccountEntry) listEntries.get(j);
        TableTexts[0] = new Text(getDateString(new idegaTimestamp(entry.getLastUpdated())));
        TableTexts[1] = new Text(entry.getName());
        TableTexts[2] = new Text(entry.getInfo());
        float p = entry.getTotal();
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
          T.add(TableTexts[i],i+1,row);
        }
        row++;
      }
      Text txTotPrice = new Text(NF.format(totPrice));
      txTotPrice.setFontSize(fontSize);
      if(totPrice >= 0 )
        txTotPrice.setFontColor(sDebetColor);
      else
        txTotPrice.setFontColor(sKreditColor);

      T.add(txTotPrice,4,row++);
    }

    T.mergeCells(1,row,4,row);
    T.add(image,1,row);
    T.setColor(1,row,FinanceColors.DARKRED);
    return T;
  }

  private PresentationObject getCleanFinanceEntryTable(List listEntries){

    int tableDepth = 3;
    if(listEntries != null){
      tableDepth += listEntries.size();
    }

    Table T = new Table(4,tableDepth);
    T.setWidth("100%");
    T.setCellspacing(0);
    T.setCellpadding(0);
    T.setLineColor("#000000");
    T.setColumnAlignment(1,"right");
    T.setColumnAlignment(2,"left");
    T.setColumnAlignment(3,"left");
    T.setColumnAlignment(4,"right");
    T.setAlignment(1,1,"left");
    T.setAlignment(1,2,"left");
    T.setWidth(1,"20");
    int fontSize = 1;
    T.add(formatText(iwrb.getLocalizedString("entries","Entries"),2,null),1,1);
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

    T.add(formatText(iwrb.getLocalizedString("date","Date")),1,2);
    T.add(formatText(iwrb.getLocalizedString("description","Description")),2,2);
    T.add(formatText(iwrb.getLocalizedString("text","Text")),3,2);
    T.add(formatText(iwrb.getLocalizedString("amount","Amount")),4,2);
    T.setTopLine(true);

    Text[] TableTexts = new Text[4];
    boolean debet = false;
    if(listEntries != null){
      int len = listEntries.size();
      float totPrice = 0;
      int row = 3;
      for(int j = 0; j < len; j++){
        AccountEntry entry = (AccountEntry) listEntries.get(j);
        float p = entry.getTotal();
        debet = p > 0 ? true : false ;
        totPrice += p;
        T.add(formatText(getDateString(new idegaTimestamp(entry.getLastUpdated()))),1,row );
        T.add(formatText(entry.getName()),2,row );
        T.add(formatText(entry.getInfo()),3,row );
        T.add(formatText(NF.format(p),1,debet?sDebetColor:sKreditColor),4,row );

        row++;
      }
      Text txTotPrice = new Text(NF.format(totPrice));
      txTotPrice.setFontSize(fontSize);
      if(totPrice >= 0 )
        txTotPrice.setFontColor(sDebetColor);
      else
        txTotPrice.setFontColor(sKreditColor);
      T.add(txTotPrice,4,tableDepth);
      //T.setLineAfterColumn(3);
      //T.setLineAfterRow(tableDepth-1);
      T.setLinesBetween(true);

    }
    return T;
  }

  private Text formatText(String text,int size,String color){
    Text T =new Text(text);
    T.setFontSize(size);
    if(color!=null)
      T.setFontColor(color);
    return T;
  }

  private Text formatText(String text){
    return formatText(text,1,null);
  }

  private idegaTimestamp getFromDate(IWContext iwc){
    if(iwc.getParameter(prmFromDate)!=null){
      String sFromDate = iwc.getParameter(prmFromDate);
      return parseStamp(sFromDate);
    }
    else{
       idegaTimestamp today =  idegaTimestamp.RightNow();
       return new idegaTimestamp(1,today.getMonth(),today.getYear());
    }
  }

  private idegaTimestamp getToDate(IWContext iwc){
    if(iwc.getParameter(prmToDate)!=null){
      String sToDate = iwc.getParameter(prmToDate);
      return parseStamp(sToDate);
    }
    else{
      return idegaTimestamp.RightNow();
    }
  }

  private void checkIds(IWContext iwc){
    if(iUserId == -1){
      if(iwc.getParameter(prmUserId)!=null){
        iUserId = Integer.parseInt(iwc.getParameter(prmUserId));
      }
      else if(isLoggedOn){
        iUserId = LoginBusiness.getUser(iwc).getID();
      }
    }
    if( iwc.getParameter(prmAccountId)!=null ){
      iAccountId = Integer.parseInt(iwc.getParameter(prmAccountId));
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

  private Link getPrintableLink(PresentationObject obj,idegaTimestamp from,idegaTimestamp to){
    Link printLink = new Link(obj);
    printLink.addParameter(prmFromDate,getDateString(from));
    printLink.addParameter(prmToDate,getDateString(to));
    return printLink;
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

  public void main( IWContext iwc ) {
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    try{
      isAdmin = iwc.hasEditPermission(this);
      isLoggedOn = com.idega.block.login.business.LoginBusiness.isLoggedOn(iwc);
    }
    catch(Exception sql){ isAdmin = false;}
    control(iwc);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}// class AccountViewer
