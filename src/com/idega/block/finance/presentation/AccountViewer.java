package com.idega.block.finance.presentation;

import com.idega.block.finance.business.AccountBusiness;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountInfo;
import com.idega.block.finance.data.AccountPhoneEntry;
import com.idega.block.finance.data.FinanceAccount;
import com.idega.business.IBOLookup;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextFormat;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class AccountViewer extends Finance {

  private boolean isLoggedOn;

  private Image image ;

  private float tax = 1.245f;

  private String sHeaderColor,sDarkColor,sLightColor,sWhiteColor;
  private String sTextColor,sHeaderTextColor;
  private String sDebetColor,sKreditColor;
  private NumberFormat NF ;
  private final String prmFromDate = "from_date",prmToDate = "to_date";
  public static final String prmUserId = "user_id",prmAccountId = "fin_acc_id";
  public static final String prmClean = "av_clean";
  private List listAccount = null;
  private User eUser = null;
  protected String styleAttribute = "font-size: 8pt";
  private int iUserId = -1,iAccountId = -1;
  private boolean specialview = false;
  private AccountBusiness accBuiz;
	private DateFormat df,tf;

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

  protected void control( IWContext iwc )throws java.rmi.RemoteException{
    accBuiz = (AccountBusiness) IBOLookup.getServiceInstance(iwc,AccountBusiness.class);
    image = Table.getTransparentCell(iwc);
    image.setHeight(6);
    NF = NumberFormat.getCurrencyInstance(iwc.getCurrentLocale());
    checkIds(iwc);
    IWTimestamp itFromDate = getFromDate(iwc);
    IWTimestamp itToDate = getToDate(iwc);
    specialview = iwc.isParameterSet("specview");
    boolean clean = iwc.isParameterSet(prmClean);
    if(isAdmin || isLoggedOn){
      if(listAccount != null && listAccount.size() >0){
        FinanceAccount eAccount = getAccount(iAccountId,listAccount);
        add(getAccountView(eAccount,listAccount,itFromDate,itToDate,isAdmin,clean));
      }
      else
        add("no_accounts");
    }
    else{
      add(iwrb.getLocalizedString("accessdenied","Access denied"));
    }
  }

  private FinanceAccount getAccount(int iAccountId,List listOfAccounts)throws java.rmi.RemoteException{
    Iterator iter = listOfAccounts.iterator();
    FinanceAccount account = (FinanceAccount) listOfAccounts.get(0);
    while(iter.hasNext()){
      FinanceAccount acc = (FinanceAccount) iter.next();
      if(acc.getAccountId() == iAccountId){
        account = acc;
        break;
      }
    }
    return account;
  }

  public PresentationObject getMainTable(IWContext iwc){
    return new Text();
  }

  public PresentationObject getAccountView(FinanceAccount eAccount,List listAccount,IWTimestamp FromDate,IWTimestamp ToDate,boolean showallkeys,boolean clean)throws java.rmi.RemoteException{
    Table T = new Table(1,3);
    T.setWidth("100%");
    T.add(getEntrySearchTable(iAccountId,listAccount,FromDate,ToDate),1,2);
    if(clean){
      T.add(getCleanAccountTable(iAccountId),1,2);
      T.add(getEntryTable(iAccountId,FromDate,ToDate,showallkeys,true),1,3);
    }
    else{
      T.add(getAccountTable(eAccount,listAccount),1,2);
      T.add(getEntryTable(eAccount,FromDate,ToDate,showallkeys,false),1,3);
    }
    return T;
  }

  private String getDateString(IWTimestamp stamp){
    return stamp.getISLDate(".",true);
  }

  public PresentationObject getEntrySearchTable(int iAccountId,List listAccount,IWTimestamp from,IWTimestamp to){
    Table T = new Table(6,2);
    T.setWidth("100%");
    String sFromDate = getDateString(from);
    String sToDate =  getDateString(to);

    Form myForm = new Form();
    //DropdownMenu drpAccounts = new DropdownMenu(listAccount,prmAccountId);
    //drpAccounts.setToSubmit();
    //drpAccounts.setAttribute("style",styleAttribute);
    //drpAccounts.setSelectedElement(String.valueOf(iAccountId));
    T.add(new HiddenInput(prmAccountId,String.valueOf(iAccountId)));
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



    int row = 1;
    int col = 1;
    //T.add(formatText(iwrb.getLocalizedString("account","Account")),1,row);
    T.add(formatText(iwrb.getLocalizedString("from","From")),col++,row);
    T.add(formatText(iwrb.getLocalizedString("to","To")),col++,row);
    T.add(formatText(iwrb.getLocalizedString("special","Special")),col++,row);

    row++;
    col = 1;
    //T.add(drpAccounts,1,row);
    T.add(tiFromDate,col++,row);
    T.add(tiToDate,col++,row);
    T.add(specialCheck,col++,row);
    T.add(fetch,col++,row);

    T.setWidth(col,row,"100%");

    myForm.add(new HiddenInput(IWMainApplication.classToInstanciateParameter,"com.idega.block.finance.presentation.AccountViewer"));
    myForm.add(T);

    return myForm;
  }

  public PresentationObject getAccountTable(FinanceAccount eAccount,List listAccounts)throws java.rmi.RemoteException{
    if(eAccount != null){
      if( eUser.getID() != eAccount.getUserId()){
        eUser = com.idega.core.user.business.UserBusiness.getUser(eAccount.getUserId());
      }
    }

    TextFormat tf = TextFormat.getInstance();

    DataTable T = new DataTable();
    T.setTitlesHorizontal(true);
    T.addTitle(iwrb.getLocalizedString("accounts","Accounts"));
    T.setWidth("100%");

    int row = 1;
    int col = 1;
    T.add(tf.format(iwrb.getLocalizedString("account","Account")),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("owner","Owner")),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("lastentry","Last entry")),col++,row);
    T.add(tf.format(iwrb.getLocalizedString("balance","Balance")),col++,row);
    row++;
    if(listAccounts != null){
      Iterator iter = listAccounts.iterator();
      while(iter.hasNext()){
        col = 1;
        FinanceAccount account = (FinanceAccount) iter.next();
        Link accountLink = new Link(tf.format(account.getAccountName()));
        accountLink.addParameter(prmAccountId,account.getAccountId());
        //if(eUser.getID() != account.getUserId())
          accountLink.addParameter(prmUserId,account.getUserId());
        T.add(accountLink,col++,row);
        T.add(tf.format(eUser.getName()),col++,row);
        T.add(tf.format(getDateString(new IWTimestamp(account.getLastUpdated()))),col++,row);
        float b = eAccount.getBalance();

        if(account.getAccountType().equals(com.idega.block.finance.data.AccountBMPBean.typePhone)){
          b = FinanceFinder.getInstance().getPhoneAccountBalance(account.getAccountId());
          b = b*tax;
        }

         T.add(tf.format(NF.format( (double) b)),col++,row);
        row++;
      }
      T.getContentTable().setColumnAlignment(4,"right");
      T.getContentTable().setColumnAlignment(3,"right");

    }
    else{
      T.add(iwrb.getLocalizedString("no_account","No Account"));
    }

    return T;
  }

   public PresentationObject getCleanAccountTable(int AccountId){
    AccountInfo eAccount = FinanceFinder.getInstance().getAccountInfo(AccountId);
    if(eAccount !=null)
      eUser = FinanceFinder.getInstance().getUser(eAccount.getUserId());

    Table T = new Table(2,4);
    if(eAccount != null){
      T.setWidth("100%");
      T.setCellspacing(0);
      T.setCellpadding(2);

      int fontSize = 1;
      int row = 1;

      T.add(formatText(iwrb.getLocalizedString("account","Account"),fontSize,null),1,row);
      T.add(formatText(eAccount.getName(),fontSize,null),2,row);
      row++;
      T.add(formatText(iwrb.getLocalizedString("owner","Owner"),fontSize,null),1,row);
      T.add(formatText(eUser.getName(),fontSize,null),2,row);
      row++;
      T.add(formatText(iwrb.getLocalizedString("lastentry","Last Entry"),fontSize,null),1,row);
      T.add(formatText(getDateString(new IWTimestamp(eAccount.getLastUpdated())),fontSize,null),2,row);
      row++;
      T.add(formatText(iwrb.getLocalizedString("balance","Balance"),fontSize,null),1,row);
      float b = eAccount.getBalance();
      T.add(formatText(NF.format( (double) b),fontSize,null),2,row);
      row++;

    }
    else{
      T.add(iwrb.getLocalizedString("no_account","No Account"));
    }
    return T;
  }

  private PresentationObject getEntryTable(int iAccountId,IWTimestamp from,IWTimestamp to,boolean showallkeys,boolean clean){
    PresentationObject mo = null;
    try{
      Account a = accBuiz.getAccount(iAccountId);
      //Account a = ((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).findByPrimaryKeyLegacy(iAccountId);
      mo =  getEntryTable(a, from, to, showallkeys,clean);
    }
    catch(Exception ex){
      ex.printStackTrace();
      mo = new Text();
    }
    return mo;
  }

  private PresentationObject getEntryTable(FinanceAccount eAccount,IWTimestamp from,IWTimestamp to,boolean showallkeys,boolean clean)throws java.rmi.RemoteException{
    List listEntries = null;
    if(eAccount.getAccountType().equals(com.idega.block.finance.data.AccountBMPBean.typeFinancial)){
      if(showallkeys)
        listEntries = accBuiz.listOfAccountEntries(eAccount.getAccountId(),from,to);
      else
        listEntries = accBuiz.listOfKeySortedEntries(eAccount.getAccountId(),from,to);
      if(clean)
        return getCleanFinanceEntryTable(eAccount,listEntries,from,to);
      else
        return getFinanceEntryTable(eAccount,listEntries,from,to);
    }
    else if(eAccount.getAccountType().equals(com.idega.block.finance.data.AccountBMPBean.typePhone)){
      listEntries = accBuiz.listOfPhoneEntries(eAccount.getAccountId(),from,to);
      if(specialview)
        return getPhoneEntryReportTable(eAccount,listEntries,from,to);
      else
        return getPhoneEntryTable(eAccount,listEntries,from,to);
    }
    else return new Text();
  }

  private PresentationObject getPhoneEntryTable(FinanceAccount eAccount,List listEntries,IWTimestamp from ,IWTimestamp to){
    int tableDepth = 4;
    int cols = 6;
    if(listEntries != null){
      tableDepth += listEntries.size();
    }

    int row = 1;

    Table T = new Table(cols,tableDepth);
    T.setWidth("100%");

    T.setCellspacing(0);
    T.setCellpadding(2);

    T.setColumnAlignment(1,"left");
    T.setColumnAlignment(2,"right");
    T.setColumnAlignment(cols,"right");
    T.setAlignment(1,1,"left");
    T.setAlignment(1,2,"left");

    T.setHorizontalZebraColored(FinanceColors.LIGHTGREY,FinanceColors.WHITE);
    T.setRowColor(1,FinanceColors.DARKBLUE);
    T.setRowColor(2,FinanceColors.DARKGREY);

    int fontSize = 1;

		String title = iwrb.getLocalizedString("entries","Entries")+"  "
			+iwrb.getLocalizedString("for","for")+" "+df.format(from.getSQLDate())+" - "+df.format(to.getSQLDate());
    Text Title = new Text(title,true,false,false);
    Title.setFontColor(FinanceColors.WHITE);
    T.add(Title,1,row);
    T.mergeCells(1,row,cols,row);
    row++;
    Text[] TableTitles = new Text[cols];

    TableTitles[0] = new Text(iwrb.getLocalizedString("dating","Dating"));
    TableTitles[1] = new Text(iwrb.getLocalizedString("number","Number"));
    TableTitles[2] = new Text(iwrb.getLocalizedString("night_time","Night time"));
    TableTitles[3] = new Text(iwrb.getLocalizedString("day_time","Day time"));
    TableTitles[4] = new Text(iwrb.getLocalizedString("time","Time"));
    TableTitles[5] = new Text(iwrb.getLocalizedString("amount","Amount"));

    for(int i = 0 ; i < TableTitles.length;i++){
      TableTitles[i].setFontSize(fontSize);
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
        //TableTexts[0] = new Text(getDateString(new IWTimestamp(entry.getLastUpdated())));
        //TableTexts[1] = new Text(entry.getMainNumber());
        //TableTexts[0] = new Text(entry.getSubNumber());

        TableTexts[0] = new Text(new IWTimestamp(entry.getPhonedStamp()).toSQLString());
				TableTexts[1] = new Text(entry.getPhonedNumber());
        TableTexts[2] = new Text(getCorrectedTimeString(entry.getNightDuration()));
        TableTexts[3] = new Text(getCorrectedTimeString(entry.getDayDuration()));
        TableTexts[4] = new Text(getCorrectedTimeString(entry.getDuration()));
        totNight += entry.getNightDuration();
        totDay += entry.getDayDuration();
        totDur += entry.getDuration();
        float p = entry.getPrice();
        totPrice += p;
        debet = p >= 0 ? true : false ;
        TableTexts[5] = new Text(NF.format(p));

        for(int i = 0 ; i < cols;i++){
          TableTexts[i].setFontSize(fontSize);
          TableTexts[i].setFontColor("#000000");
          if(i == 5){
            if(debet)
            	TableTexts[i].setFontColor(sDebetColor);
            else
            	TableTexts[i].setFontColor(sKreditColor);
          }
          else
            TableTexts[i].setFontColor("#000000");
          T.add(TableTexts[i],i+1,row);
        }
        row++;
      }
      Text txTotNight = new Text(tf.format(new java.sql.Time(totNight*1000)));
      Text txTotDay = new Text(tf.format(new java.sql.Time(totDay*1000)));
      Text txTotDur = new Text(tf.format(new java.sql.Time(totDur*1000)));
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

      T.add(txTotNight,3,row);
      T.add(txTotDay,4,row);
      T.add(txTotDur,5,row);
      T.add(txTotPrice,6,row);
    }
    T.mergeCells(1,row,cols,row);
    T.add(image,1,row);
    T.setColor(1,row,FinanceColors.DARKRED);
    return T;
  }

  private PresentationObject getPhoneEntryReportTable(FinanceAccount eAccount,List listEntries,IWTimestamp from ,IWTimestamp to){
    String sMob1 = "8";
    String sMob2 = "6";
    String sFor = "00";
    float tax = 0.245f;

    Table T = new Table(5,9);
    T.setWidth("100%");
    T.setCellpadding(2);
    T.setCellspacing(1);
		T.mergeCells(1,1,5,1);


    T.setHorizontalZebraColored(FinanceColors.WHITE,FinanceColors.LIGHTGREY);
    T.setRowColor(1,FinanceColors.DARKBLUE);
    T.setRowColor(2,FinanceColors.LIGHTGREY);
    T.setColumnAlignment(3,"right");
    T.setColumnAlignment(4,"right");
    T.setColumnAlignment(5,"right");

    String fontColor = sDarkColor;;
    int fontSize = 1;


		String title = iwrb.getLocalizedString("sum_report","Report")+"  "
			+iwrb.getLocalizedString("for","for")+" "+df.format(from.getSQLDate())+" - "+df.format(to.getSQLDate());
    Text Title = new Text(title,true,false,false);
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
        if(phonedNumber !=null){
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

      T.add(formatText(getCorrectedTimeString(otherTime)),4,3);
      T.add(formatText(getCorrectedTimeString(mobTime)),4,4);
      T.add(formatText(getCorrectedTimeString(forTime)),4,5);
      T.add(formatText(getCorrectedTimeString(totalTime)),4,6);

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

  private PresentationObject getFinanceEntryTable(FinanceAccount eAccount,List listEntries,IWTimestamp from ,IWTimestamp to)throws java.rmi.RemoteException{

    int tableDepth = 5;
    if(listEntries != null){
      tableDepth += listEntries.size();
    }
    int row = 1;
    DataTable T = new DataTable();
    T.setWidth("100%");
    T.setTitlesHorizontal(true);


    int fontSize = 1;

    String title = iwrb.getLocalizedString("entries","Entries")+" "+
      eAccount.getAccountName()+"   "+df.format(from.getSQLDate())+" - "+df.format(to.getSQLDate());

    T.addTitle(title);


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
      double totPrice = 0;
      for(int j = 0; j < len; j++){
        AccountEntry entry = (AccountEntry) listEntries.get(j);
        TableTexts[0] = new Text(getDateString(new IWTimestamp(entry.getLastUpdated())));
        TableTexts[1] = new Text(entry.getName());
        TableTexts[2] = new Text(entry.getInfo());
        double p = entry.getTotal();
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
      T.getContentTable().setColumnAlignment(4,"right");
    }

    return T;
  }

  private PresentationObject getCleanFinanceEntryTable(FinanceAccount eAccount,List listEntries,IWTimestamp from ,IWTimestamp to){

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

    boolean debet = false;
    if(listEntries != null){
      int len = listEntries.size();
      double totPrice = 0;
      int row = 3;
      for(int j = 0; j < len; j++){
        AccountEntry entry = (AccountEntry) listEntries.get(j);
        double p = entry.getTotal();
        debet = p > 0 ? true : false ;
        totPrice += p;
        T.add(formatText(getDateString(new IWTimestamp(entry.getLastUpdated()))),1,row );
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

  private IWTimestamp getFromDate(IWContext iwc){
    if(iwc.getParameter(prmFromDate)!=null){
      String sFromDate = iwc.getParameter(prmFromDate);
      return parseStamp(sFromDate);
    }
    else{
       IWTimestamp today =  IWTimestamp.RightNow();
       return new IWTimestamp(1,today.getMonth(),today.getYear());
    }
  }

  private IWTimestamp getToDate(IWContext iwc){
    if(iwc.getParameter(prmToDate)!=null){
      String sToDate = iwc.getParameter(prmToDate);
      return parseStamp(sToDate);
    }
    else{
      return IWTimestamp.RightNow();
    }
  }

  private void checkIds(IWContext iwc)throws java.rmi.RemoteException{

    if(iwc.isParameterSet(prmAccountId)){
      iAccountId = Integer.parseInt(iwc.getParameter(prmAccountId));
      FinanceAccount acc = accBuiz.getAccount(iAccountId);
      if(acc !=null)
        iUserId = acc.getUserId();
    }
    else if(iwc.isParameterSet(prmUserId)){
      iUserId = Integer.parseInt(iwc.getParameter(prmUserId));
      eUser = com.idega.core.user.business.UserBusiness.getUser(iUserId);
    }
    else if(iwc.isLoggedOn()){
      eUser = iwc.getUser();
      iUserId = eUser.getID();
    }
    listAccount = FinanceFinder.getInstance().listOfFinanceAccountsByUserId(iUserId);
  }

  private Link getPrintableLink(PresentationObject obj,IWTimestamp from,IWTimestamp to){
    Link printLink = new Link(obj);
    printLink.addParameter(prmFromDate,getDateString(from));
    printLink.addParameter(prmToDate,getDateString(to));
    return printLink;
  }

  private IWTimestamp parseStamp(String sDate){
     IWTimestamp it = new IWTimestamp();
     try{
      StringTokenizer st = new StringTokenizer(sDate," .-/+");
      int day = 1,month = 1,year = 2001;
      if(st.hasMoreTokens()){
        day = Integer.parseInt(st.nextToken());
        month = Integer.parseInt(st.nextToken());
        year = Integer.parseInt(st.nextToken());

      }
      it = new IWTimestamp(day,month,year);
    }
    catch(Exception pe){ it = new IWTimestamp();}
    return it;
  }

  public void main( IWContext iwc ) throws java.rmi.RemoteException{
    isLoggedOn = iwc.isLoggedOn();
    eUser = iwc.getUser();
		df = DateFormat.getDateInstance(DateFormat.SHORT);
		tf = DateFormat.getTimeInstance();
    control(iwc);
  }

	public String getCorrectedTimeString(long seconds){
	  return tf.format(new java.sql.Time((seconds+(60*60))*1000));
	}

}// class AccountViewer
