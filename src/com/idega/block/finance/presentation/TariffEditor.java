package com.idega.block.finance.presentation;

import com.idega.block.finance.data.*;
import com.idega.block.finance.business.Finder;
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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class TariffEditor extends KeyEditor{

  public static String strAction = "te_action";
  public final static  int YEAR=1,MONTH=2,WEEK=3,DAY=4;
  private idegaTimestamp workingPeriod;
  private int period = MONTH;
  private GenericEntity[] entities = null;

  public TariffEditor(String sHeader) {
    super(sHeader);
  }

  public void setEntities(GenericEntity[] entities){
    this.entities = entities;
  }

  protected void control(ModuleInfo modinfo){

    try{

      if(modinfo.getParameter(strAction) == null){
        doMain(modinfo);
      }
      if(modinfo.getParameter(strAction) != null){
        String sAct = modinfo.getParameter(strAction);

        int iAct = Integer.parseInt(sAct);
        switch (iAct) {
          case ACT1 : doMain(modinfo);        break;
          case ACT2 : doChange(modinfo,false);break;
          case ACT3 : doUpdate(modinfo);      break;
          case ACT4 : doChange(modinfo,true); break;
          default: doMain(modinfo);           break;
        }
      }
    }
    catch(Exception S){
      S.printStackTrace();
    }
  }

  protected ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(4,1);
    int last = 4;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(this.DarkColor);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link("Yfirlit");
    Link1.setFontColor(this.LightColor);
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link Link2 = new Link("Breyta");
    Link2.setFontColor(this.LightColor);
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    Link Link3 = new Link("Ný");
    Link3.setFontColor(this.LightColor);
    Link3.addParameter(this.strAction,String.valueOf(this.ACT4));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
      LinkTable.add(Link3,3,1);
    }
    return LinkTable;
  }
  protected void setPeriod(int period){
    this.period = period;
  }
  protected ModuleObject getPeriodChooser(int init){
    ModuleObject mo;
    switch (this.period) {
      case YEAR : mo =  this.YearChooser(init);      break;
      case MONTH: mo =  this.MonthChooser(init);     break;
      case WEEK : mo =  this.WeekChooser(init);      break;
      case DAY  : mo =  this.DayChooser(init);       break;
      default   : mo =  this.MonthChooser(init);     break;
    }
    return mo;
  }
  protected idegaTimestamp getDateFrom(){
    idegaCalendar c = new idegaCalendar();
    idegaTimestamp now = new idegaTimestamp();
    idegaTimestamp mo = now;
    switch (this.period) {
      case YEAR : mo =  new idegaTimestamp(1,1,now.getYear());     break;
      case MONTH: mo =  new idegaTimestamp(1,now.getMonth(),now.getYear());     break;
      case WEEK : mo =  new idegaTimestamp();     break;
      case DAY  : mo =  new idegaTimestamp();     break;
      default   : mo =  new idegaTimestamp(1,now.getMonth(),now.getYear());     break;
    }
    return mo;
  }
  protected idegaTimestamp getDateTo(){
    idegaCalendar c = new idegaCalendar();
    idegaTimestamp now = new idegaTimestamp();
    idegaTimestamp mo = now;
    switch (this.period) {
      case YEAR : mo =  new idegaTimestamp(31,12,now.getYear());     break;
      case MONTH: mo =  new idegaTimestamp(c.getLengthOfMonth(now.getMonth(),now.getYear()),now.getMonth(),now.getYear());     break;
      case WEEK : mo =  new idegaTimestamp();     break;
      case DAY  : mo =  new idegaTimestamp();     break;
      default   : mo =  new idegaTimestamp(c.getLengthOfMonth(now.getMonth(),now.getYear()),now.getMonth(),now.getYear());     break;
    }
    return mo;
  }
  protected void doMain(ModuleInfo modinfo){
    idegaTimestamp today = new idegaTimestamp();
    //Tariff[] tariffs = Finder.findTariffs(today.getMonth(),today.getYear());
    Tariff[] tariffs = Finder.findTariffs();
    int count = tariffs.length;
    Table T2 = new Table(1,2);
    T2.setCellpadding(0);
    T2.setCellspacing(0);
    T2.setWidth("100%");
    Table T = new Table(6,count+1);
    T.setWidth("100%");
    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,MiddleColor);
    T.setCellpadding(2);
    T.setCellspacing(1) ;
    T.add(formatText("Nr"),1,1);
    T.add(formatText("Auðkenni"),2,1);
    T.add(formatText("Upphæð"),3,1);
    T.add(formatText("Lýsing"),4,1);
    T.add(formatText("Gjaldliður"),5,1);
    T.add(formatText("Bókunarliður"),6,1);
    idegaTimestamp from = this.getDateFrom();
    idegaTimestamp to = this.getDateTo();
    if(isAdmin){
      if(count != 0){
        for (int i = 0;i < count;i++){
          T.add(formatText( String.valueOf(i+1)),1,i+2);
          T.add(formatText(tariffs[i].getName()),2,i+2);
          T.add(formatText(tariffs[i].getPrice()),3,i+2);
          T.add(formatText(tariffs[i].getInfo()),4,i+2);
          try{
          T.add(formatText(new TariffKey(tariffs[i].getTariffKeyId()).getName()),5,i+2);
          T.add(formatText(new AccountKey(tariffs[i].getAccountKeyId()).getName()),6,i+2);
          }
          catch(SQLException e){}
        }
        if(tariffs[0]!=null){
          from = new idegaTimestamp(tariffs[0].getUseFromDate());
          to = new idegaTimestamp(tariffs[0].getUseToDate());
        }
      }
    }

    Table T3 = new Table(4,1);
    T3.setCellpadding(0);
    T3.setCellspacing(0);
    T3.add(formatText("Tímabil:"),1,1);
    T3.add(formatText(from.getISLDate(".",true)),2,1);
    T3.add(formatText(" til "),3,1);
    T3.add(formatText(to.getISLDate(".",true)),4,1);
    T2.add(T3,1,1);
    T2.add(T,1,2);
    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(T2);
  }

  protected void doChange(ModuleInfo modinfo,boolean ifnew){
    Form myForm = new Form();
    myForm.maintainAllParameters();
    idegaTimestamp today = new idegaTimestamp();
    //Tariff[] tariffs = Finder.findTariffs(today.getMonth(),today.getYear());
    Tariff[] tariffs = Finder.findTariffs();
    int count = tariffs.length;
    int inputcount = count+5;
    Table T2 = new Table(1,2);
    T2.setCellpadding(0);
    T2.setCellpadding(0);
    T2.setWidth("100%");
    Table T =  new Table(7,inputcount+1);
    T.setWidth("100%");
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setColumnAlignment(1,"right");
    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,MiddleColor);
    T.add(formatText("Nr"),1,1);
    T.add(formatText("Auðkenni"),2,1);
    T.add(formatText("Upphæð"),3,1);
    T.add(formatText("Lýsing"),4,1);
    T.add(formatText("Gjaldliður"),5,1);
    T.add(formatText("Bókunarliður"),6,1);
    T.add(formatText("Eyða"),7,1);
    idegaTimestamp from = this.getDateFrom();
    idegaTimestamp to = this.getDateTo();

    for (int i = 1; i <= inputcount ;i++){
      String rownum = String.valueOf(i);
      String s = "";
      TextInput nameInput,priceInput,infoInput;
      DropdownMenu drpAK,drpTK;
      HiddenInput idInput;
      CheckBox delCheck;
      int pos;
      if(i <= count && !ifnew ){
        pos = i-1;
        nameInput  = new TextInput("te_nameinput"+i,tariffs[pos].getName());
        priceInput = new TextInput("te_priceinput"+i,String.valueOf(tariffs[pos].getPrice()));
        infoInput = new TextInput("te_infoinput"+i,tariffs[pos].getInfo());
        drpAK = this.drpAccountKeys(("te_akdrp"+i),String.valueOf(tariffs[pos].getAccountKeyId()));
        drpTK = this.drpTariffKeys(("te_tkdrp"+i),String.valueOf(tariffs[pos].getTariffKeyId()));
        delCheck = new CheckBox("te_delcheck"+i,"true");
        idInput = new HiddenInput("te_idinput"+i,String.valueOf(tariffs[pos].getID()));
        setStyle(delCheck);
        T.add(delCheck,7,i+1);
      }
      else{
        nameInput  = new TextInput("te_nameinput"+i);
        priceInput = new TextInput("te_priceinput"+i);
        infoInput = new TextInput("te_infoinput"+i);
        drpAK = this.drpAccountKeys("te_akdrp"+i,"");
        drpTK = this.drpTariffKeys("te_tkdrp"+i,"");
        idInput = new HiddenInput("te_idinput"+i,"-1");
      }
      nameInput.setSize(20);
      priceInput.setSize(6);
      infoInput.setSize(40);

      setStyle(nameInput);
      setStyle(priceInput);
      setStyle(infoInput);
      setStyle(drpAK);
      setStyle(drpTK);

      T.add(formatText(rownum),1,i+1);
      T.add(nameInput,2,i+1);
      T.add(priceInput,3,i+1);
      T.add(infoInput,4,i+1);
      T.add(drpTK,5,i+1);
      T.add(drpAK,6,i+1);
      T.add(idInput);
    }
    Table T3 = new Table(4,1);
    TextInput datefrom = new TextInput("te_datefrom",from.getISLDate(".",true));
    datefrom.setLength(8);
    setStyle(datefrom);
    TextInput dateto = new TextInput("te_dateto",to.getISLDate(".",true));
    dateto.setLength(8);
    setStyle(dateto);
    T3.add(formatText("Tímabil"));
    T3.add(datefrom,3,1);
    T3.add(dateto,4,1);
    T2.add(T3,1,1);
    T2.add(T,1,2);
    myForm.add(new HiddenInput("te_count", String.valueOf(inputcount) ));
    myForm.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    myForm.add(T2);
    myForm.add(new SubmitButton("Vista"));

    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(myForm);

  }

  protected void doUpdate(ModuleInfo modinfo) {
    int count = Integer.parseInt(modinfo.getParameter("te_count"));
    String sName,sInfo,sDel,sPrice,sAK,sTK,sID,sDateFrom,sDateTo;
    int ID,AKid,TKid,Price;
    idegaTimestamp dFrom,dTo;

    Tariff tariff = null;
    sDateFrom = modinfo.getParameter("te_datefrom");
    add(sDateFrom);
    dFrom = this.parseStamp(sDateFrom);
    sDateTo = modinfo.getParameter("te_dateto");
    add(sDateTo);
    dTo = this.parseStamp(sDateTo);

    for (int i = 1; i < count+1 ;i++){
      sName = modinfo.getParameter("te_nameinput"+i);
      sPrice = (modinfo.getParameter("te_priceinput"+i));
      sInfo = modinfo.getParameter("te_infoinput"+i);
      sAK = (modinfo.getParameter("te_akdrp"+i));
      sTK = (modinfo.getParameter("te_tkdrp"+i));
      sDel = modinfo.getParameter("te_delcheck"+i);
      sID = modinfo.getParameter("te_idinput"+i);

      ID = Integer.parseInt(modinfo.getParameter("te_idinput"+i));
      if(ID != -1){
        try{
          tariff = new Tariff(ID);
          if(sDel != null && sDel.equalsIgnoreCase("true")){
            tariff.delete();
          }
          else{
            tariff.setName(sName);
            tariff.setInfo(sInfo);
            tariff.setPrice(Integer.parseInt(sPrice));
            tariff.setAccountKeyId(Integer.parseInt(sAK));
            tariff.setTariffKeyId(Integer.parseInt(sTK));
            tariff.setUseFromDate(dFrom.getTimestamp());
            tariff.setUseToDate(dTo.getTimestamp());
            tariff.update();
          }
        }
        catch(SQLException e){
        }
      }
      else {
        if(!sName.equalsIgnoreCase("")){
          try {
            tariff = new Tariff();
            tariff.setName(sName);
            tariff.setInfo(sInfo);
            tariff.setPrice(Integer.parseInt(sPrice));
            tariff.setAccountKeyId(Integer.parseInt(sAK));
            tariff.setTariffKeyId(Integer.parseInt(sTK));
            tariff.setUseFromDate(dFrom.getTimestamp());
            tariff.setUseToDate(dTo.getTimestamp());
            tariff.insert();
          }
          catch (SQLException ex) {
          }
        }
      }
    }// for loop

   doMain(modinfo);
  }

  private DropdownMenu drpTariffKeys(String name, String selected){
    TariffKey[] keys= Finder.findTariffKeys();
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(0,"--");
    for (int i = 0; i < keys.length; i++) {
      drp.addMenuElement(keys[i].getID(),keys[i].getName());
    }
    if(!selected.equalsIgnoreCase(""))
      drp.setSelectedElement(selected);
    return drp;
  }

  private DropdownMenu drpAccountKeys(String name,String selected){
    AccountKey[] keys  = Finder.findAccountKeys();
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(0,"--");
    for (int i = 0; i < keys.length; i++) {
      drp.addMenuElement(keys[i].getID(),keys[i].getName());
    }
    if(!selected.equalsIgnoreCase(""))
      drp.setSelectedElement(selected);
    return drp;
  }
  private ModuleObject YearChooser(int year){
    return new Text();
  }
  private ModuleObject MonthChooser(int month){ return new Text();}
  private ModuleObject WeekChooser(int week){ return new Text();}
  private ModuleObject DayChooser(int day){ return new Text();}

  private idegaTimestamp parseStamp(String sDate){
    idegaTimestamp it = new idegaTimestamp();
    try{
      StringTokenizer st = new StringTokenizer(sDate," .-/+#");
      int day = 1,month = it.getMonth(),year = it.getYear();
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

}