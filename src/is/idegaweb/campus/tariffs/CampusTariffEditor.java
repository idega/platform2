/*
 * $Id: CampusTariffEditor.java,v 1.4 2001/07/23 10:00:00 aron Exp $
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
import is.idegaweb.campus.entity.TariffIndex;
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
import java.util.Hashtable;
import java.util.Enumeration;
import com.idega.jmodule.object.Editor;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusTariffEditor extends KeyEditor{


  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4;
  protected final int ACT5 = 5,ACT6 = 6, ACT7 = 7,ACT8  = 8;
  public  String strAction = "te_action";
  public final   int YEAR=1,MONTH=2,WEEK=3,DAY=4;
  private idegaTimestamp workingPeriod;
  private int period = MONTH;
  private GenericEntity[] entities = null;
  private int iPeriod;

  public static String prefixComplex = "x";
  public static String prefixAll = "a";
  public static String prefixBuilding = "b";
  public static String prefixFloor = "f";
  public static String prefixCategory = "c";
  public static String prefixType = "t";
  public static String prefixApartment = "p";

  public CampusTariffEditor(String sHeader) {
    super(sHeader);
  }

  public void setEntities(GenericEntity[] entities){
    this.entities = entities;
  }

  protected void control(ModuleInfo modinfo){

    try{


      if(modinfo.getParameter("updateindex")!=null){
        doUpdateIndex(modinfo);
      }
      else if(modinfo.getParameter("savetariffs")!=null){
        doUpdate(modinfo);
      }
      else if(modinfo.getParameter(strAction) != null){
        String sAct = modinfo.getParameter(strAction);
        int iAct = Integer.parseInt(sAct);
        switch (iAct) {
          case ACT1 : doMain(modinfo);        break;
          case ACT2 : doChange(modinfo,false,1,"1","1");break;
          case ACT3 : doUpdate(modinfo);      break;
          case ACT4 : doChange(modinfo,true,1,"1","1"); break;
          default: doMain(modinfo);           break;
        }
      }
      else{
        doMain(modinfo);
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
    Tariff[] tariffs = Finder.findTariffs();
    List AK = Finder.getAccountKeys();
    Hashtable hAK = getKeys(AK);
    Hashtable hLodgings = BuildingFinder.getLodgingsHash();

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
    T.add(formatText("Tenging"),2,1);
    T.add(formatText("Auðkenni"),3,1);
    T.add(formatText("Upphæð"),4,1);
    T.add(formatText("Lýsing"),5,1);
    T.add(formatText("Bókunarliður"),6,1);
    idegaTimestamp from = this.getDateFrom();
    idegaTimestamp to = this.getDateTo();
    if(isAdmin){
      if(count != 0){
        for (int i = 0;i < count;i++){
          T.add(formatText( String.valueOf(i+1)),1,i+2);
          String tatt = tariffs[i].getTariffAttribute();
          if(hLodgings.containsKey(tatt))
            T.add(formatText(((GenericEntity)hLodgings.get(tatt)).getName()),2,i+2);
          T.add(formatText(tariffs[i].getName()),3,i+2);
          T.add(formatText(tariffs[i].getPrice()),4,i+2);
          T.add(formatText(tariffs[i].getInfo()),5,i+2);
          Integer I = new Integer(tariffs[i].getAccountKeyId());
          if(hAK.containsKey(I))
            T.add(formatText((String)hAK.get(I)),6,i+2);
        }
      }
    }

    Table T3 = new Table(7,1);
    T3.setWidth("100%");
    T3.setWidth(5,1,"100%");
    T3.setColumnAlignment(6,"right");
    T3.setColumnAlignment(7,"right");
    T3.setCellpadding(0);
    T3.setCellspacing(0);
    T3.add(formatText("Tímabil:"),1,1);
    T3.add(formatText(from.getISLDate(".",true)),2,1);
    T3.add(formatText(" til "),3,1);
    T3.add(formatText(to.getISLDate(".",true)),4,1);
    //T2.add(T3,1,1);
    T2.add(T,1,1);
    this.setBorder(0);
    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(T2);
  }

  private void doUpdateIndex(ModuleInfo modinfo){
    /** @todo  */
    String index = modinfo.getParameter("te_index");
    String hindex = modinfo.getParameter("te_hindex");
    float factor = 1;
    if(!index.equals(hindex)){
      float now = (new Double(index)).floatValue();
      float then = (new Double(hindex)).floatValue();
      factor = 1+((now - then)/then);
    }
    doChange(modinfo,false,factor,index,hindex);
  }

  protected void doChange(ModuleInfo modinfo,boolean ifnew,float factor,String sNewIndex,String sOldIndex){
    Form myForm = new Form();
    myForm.maintainAllParameters();
    idegaTimestamp today = new idegaTimestamp();
    Tariff[] tariffs = Finder.findTariffs();
    List AK = Finder.getAccountKeys();
    TariffIndex[] TI = getTariffIndices();
    Hashtable hash = BuildingFinder.getLodgingsHash();
    List BL = BuildingFinder.listOfBuilding();
    List FL= BuildingFinder.listOfFloor();
    List TL = BuildingFinder.listOfApartmentType();
    List CL = BuildingFinder.listOfApartmentCategory();
    List XL = BuildingFinder.listOfComplex();

    String sIndex ,sHindex ;
    if(factor != 1){
      sIndex = sNewIndex;
      sHindex =sOldIndex;
    }
    else{
      sIndex = String.valueOf( this.findLastTariffIndex(TI));
      sHindex = String.valueOf( this.findLastTariffIndex(TI));
    }
    int count = tariffs.length;
    int inputcount = count+5;
    Table BorderTable = new Table();
    BorderTable.setCellpadding(1);
    BorderTable.setCellspacing(0);
    BorderTable.setColor(DarkColor);
    BorderTable.setWidth("100%");
    Table T2 = new Table(1,3);
    T2.setColor(this.WhiteColor);
    T2.setCellpadding(0);
    T2.setCellpadding(0);
    T2.setWidth("100%");
    Table T =  new Table(8,inputcount+1);
    T.setWidth("100%");
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setColumnAlignment(1,"right");
    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,MiddleColor);
    T.add(formatText("Nr"),1,1);
    T.add(formatText("Tenging"),2,1);
    T.add(formatText("Auðkenni"),3,1);
    T.add(formatText("Upphæð"),4,1);
    T.add(formatText("Lýsing"),5,1);
    T.add(formatText("Bókunarliður"),6,1);
    T.add(formatText("Vísitala"),7,1);
    T.add(formatText("Eyða"),8,1);
    idegaTimestamp from = this.getDateFrom();
    idegaTimestamp to = this.getDateTo();
    if(count != 0 && tariffs[0]!=null){
      from = new idegaTimestamp(tariffs[0].getUseFromDate());
      to = new idegaTimestamp(tariffs[0].getUseToDate());
    }
    for (int i = 1; i <= inputcount ;i++){
      String rownum = String.valueOf(i);
      String s = "";
      TextInput nameInput,priceInput,infoInput;
      DropdownMenu drpAtt,drpAK;
      HiddenInput idInput;
      CheckBox indexCheck = new CheckBox("te_indexcheck"+i,"true");
      CheckBox delCheck;
      int pos;
      if(i <= count && !ifnew ){
        pos = i-1;
        int iPrice = tariffs[pos].getPrice();
        nameInput  = new TextInput("te_nameinput"+i,tariffs[pos].getName());
        infoInput = new TextInput("te_infoinput"+i,tariffs[pos].getInfo());
        drpAtt = this.drpLodgings("te_attdrp"+i,tariffs[pos].getTariffAttribute(),XL,BL,FL,CL,TL);
        //drpAtt = this.drpAttributes(hash,"te_attdrp"+i,tariffs[pos].getTariffAttribute());
        drpAK = this.drpAccountKeys(AK,("te_akdrp"+i),String.valueOf(tariffs[pos].getAccountKeyId()));
        delCheck = new CheckBox("te_delcheck"+i,"true");
        if(tariffs[pos].getUseIndex()){
          indexCheck.setChecked(true);
          iPrice = Math.round(factor*iPrice);
        }
        priceInput = new TextInput("te_priceinput"+i,String.valueOf(iPrice));
        idInput = new HiddenInput("te_idinput"+i,String.valueOf(tariffs[pos].getID()));
        setStyle(delCheck);
        T.add(delCheck,8,i+1);
      }
      else{
        nameInput  = new TextInput("te_nameinput"+i);
        priceInput = new TextInput("te_priceinput"+i);
        infoInput = new TextInput("te_infoinput"+i);
        //drpAtt = this.drpAttributes(hash,"te_attdrp"+i,"");
        drpAtt = this.drpLodgings("te_attdrp"+i,"",XL,BL,FL,CL,TL);
        drpAK = this.drpAccountKeys(AK,"te_akdrp"+i,"");
        idInput = new HiddenInput("te_idinput"+i,"-1");
      }
      nameInput.setSize(20);
      priceInput.setSize(6);
      infoInput.setSize(30);

      setStyle(nameInput);
      setStyle(priceInput);
      setStyle(infoInput);
      setStyle(drpAtt);
      setStyle(drpAK);
      setStyle(indexCheck);

      T.add(formatText(rownum),1,i+1);
      T.add(drpAtt,2,i+1);
      T.add(nameInput,3,i+1);
      T.add(priceInput,4,i+1);
      T.add(infoInput,5,i+1);
      T.add(drpAK,6,i+1);
      T.add(indexCheck,7,i+1);
      T.add(idInput);
    }
    Table T3 = new Table(8,1);
    T3.setWidth("100%");
    T3.setWidth(5,1,"100%");
    T3.setColumnAlignment(6,"right");
    T3.setColumnAlignment(7,"right");
    TextInput datefrom = new TextInput("te_datefrom",from.getISLDate(".",true));
    datefrom.setLength(8);
    setStyle(datefrom);
    TextInput dateto = new TextInput("te_dateto",to.getISLDate(".",true));
    dateto.setLength(8);
    setStyle(dateto);
    TextInput index = new TextInput("te_index",sIndex);
    HiddenInput hindex = new HiddenInput("te_hindex",sHindex);
    dateto.setLength(8);
    setStyle(index);
    SubmitButton update = new SubmitButton("updateindex","Uppfæra");
    setStyle(update);
    T3.add(formatText("Tímabil"),1,1);
    T3.add(datefrom,3,1);
    T3.add(dateto,4,1);
    T3.add(formatText("Vísitala"),6,1);
    T3.add(index,7,1);
    T3.add(hindex,7,1);
    T3.add(update,8,1);
    SubmitButton save = new SubmitButton("savetariffs","Vista");
    setStyle(save);
    Table T4 = new Table();
    T4.setAlignment("right");
    T4.add(save);
    T2.add(T3,1,1);
    T2.add(T,1,2);
    T2.add(T4,1,3);
    BorderTable.add(T2);
    myForm.add(new HiddenInput("te_count", String.valueOf(inputcount)));
    //myForm.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    myForm.add(BorderTable);
    this.setBorder(0);
    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(myForm);
  }

  protected void doUpdate(ModuleInfo modinfo) {
    int count = Integer.parseInt(modinfo.getParameter("te_count"));
    String sName,sInfo,sDel,sPrice,sAtt,sAK,sTK,sID,sDateFrom,sDateTo,sIndex;
    int ID,Attid,AKid,TKid,Price;
    idegaTimestamp dFrom,dTo;

    Tariff tariff = null;
    sDateFrom = modinfo.getParameter("te_datefrom");
    dFrom = this.parseStamp(sDateFrom);
    sDateTo = modinfo.getParameter("te_dateto");
    dTo = this.parseStamp(sDateTo);
    String index = modinfo.getParameter("te_index");
    String hindex = modinfo.getParameter("te_hindex");
    if(!index.equals(hindex)){
      try {
        TariffIndex TI = new TariffIndex();
        TI.setIndex((new Double(index)).floatValue());
        TI.setDate(idegaTimestamp.getTimestampRightNow());
        TI.setInfo("");
        TI.insert();
      }
      catch (SQLException ex) {}
      catch(Exception e){e.printStackTrace();}
    }

    for (int i = 1; i < count+1 ;i++){
      sName = modinfo.getParameter("te_nameinput"+i);
      sPrice = (modinfo.getParameter("te_priceinput"+i));
      sInfo = modinfo.getParameter("te_infoinput"+i);
      sAtt = modinfo.getParameter("te_attdrp"+i);
      sAK = (modinfo.getParameter("te_akdrp"+i));
      sIndex = (modinfo.getParameter("te_indexcheck"+i));
      sDel = modinfo.getParameter("te_delcheck"+i);
      sID = modinfo.getParameter("te_idinput"+i);
      boolean bIndex = (sIndex != null )?true:false;

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
            tariff.setUseFromDate(dFrom.getTimestamp());
            tariff.setUseToDate(dTo.getTimestamp());
            tariff.setTariffAttribute(sAtt);
            tariff.setUseIndex(bIndex);
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
            tariff.setUseFromDate(dFrom.getTimestamp());
            tariff.setUseToDate(dTo.getTimestamp());
            tariff.setTariffAttribute(sAtt);
            tariff.setUseIndex(bIndex);
            tariff.insert();
          }
          catch (SQLException ex) {
          }
        }
      }
    }// for loop

   doMain(modinfo);
  }

  private Hashtable getKeys(List AK){
    Hashtable h = new Hashtable();
    if(AK != null){
      int len = AK.size();
      for (int i = 0; i < len; i++) {
        AccountKey T = (AccountKey) AK.get(i);
        h.put(new Integer(T.getID()),T.getName());
      }
    }
    return h;

  }

  private DropdownMenu drpAccountKeys(List AK,String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(0,"--");
    if(AK != null){
      drp.addMenuElements(AK);
      if(!selected.equalsIgnoreCase(""))
        drp.setSelectedElement(selected);
    }
    return drp;
  }
  private ModuleObject YearChooser(int year){
    return new Text();
  }
  private ModuleObject MonthChooser(int month){
    Link left = new Link("<< ");
    left.addParameter(this.strAction,this.ACT7);
    Link right = new Link(" >>");
    right.addParameter(this.strAction,this.ACT8);
    Table T = new Table();
    T.add(left,1,1);
    T.add(String.valueOf(this.iPeriod),2,1);
    T.add(right,3,1);
    return T;
  }
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

  private DropdownMenu drpAttributes(Hashtable hash,String name,String selected){
    int len = hash.size();
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(0,"--");
    drp.addMenuElement("a","Allir");
    GenericEntity G;
    for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
      G = (GenericEntity) e.nextElement();
      drp.addMenuElement(String.valueOf(G.getID()),G.getName());
    }
    if(!selected.equalsIgnoreCase(""))
      drp.setSelectedElement(selected);
    return drp;
  }

  private DropdownMenu drpLodgings(String name,String selected,
      List ComplexList,List BuildingList, List FloorList, List CategoryList, List TypeList){
    Hashtable Bhash = new Hashtable();
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(0,"--");
    drp.addMenuElement("a","Allir");
    if(TypeList != null){
      int len = TypeList.size();
      ApartmentType T;
      for (int i = 0; i < len; i++) {
        T = (ApartmentType) TypeList.get(i);
        drp.addMenuElement(String.valueOf(prefixType+"_"+T.getID()),T.getName());
      }
    }
    if(CategoryList != null){
      int len = CategoryList.size();
      ApartmentCategory C;
      for (int i = 0; i < len; i++) {
        C = (ApartmentCategory) CategoryList.get(i);
        drp.addMenuElement(String.valueOf(prefixCategory+"_"+C.getID()),C.getName());
      }
    }
    if(ComplexList != null){
      int clen = ComplexList.size();
      Complex C;
      for (int i = 0; i < clen; i++) {
        C = (Complex) ComplexList.get(i);
        drp.addMenuElement(String.valueOf(prefixComplex+"_"+C.getID()),C.getName());
      }
    }
    if(BuildingList != null){
      int clen = BuildingList.size();
      Building B;
      for (int i = 0; i < clen; i++) {
        B = (Building) BuildingList.get(i);
        drp.addMenuElement(String.valueOf(prefixBuilding+"_"+B.getID()),B.getName());
        Bhash.put(new Integer(B.getID()),B.getName());
      }
    }
    if(FloorList != null){
      int len = FloorList.size();
      Floor F;
      for (int i = 0; i < len; i++) {
        F = (Floor) FloorList.get(i);
        Integer I = new Integer(F.getBuildingId());
        String sAdd = "";
        if(Bhash.containsKey(I))
           sAdd = (String)Bhash.get(I);
        drp.addMenuElement(String.valueOf(prefixFloor+"_"+F.getID()),F.getName()+" "+sAdd);
      }
    }

     if(!selected.equalsIgnoreCase(""))
      drp.setSelectedElement(selected);
    return drp;
  }


  private TariffIndex[] getTariffIndices(){
    TariffIndex[] ti = new TariffIndex[0];
    try {
       ti = (TariffIndex[])(new TariffIndex()).findAllDescendingOrdered(TariffIndex.getDateColumnName());
    }
    catch (SQLException ex) {

    }
    return ti;
  }

  private float getDifferenceFactor(float now, float then){
    float factor = (now - then)/then;
    return factor;
  }

  private float findIndexDifferenceFactor(TariffIndex[] ti){
    float now = 1;
    float then = 1;
    float diff = 1;
    try {

      if(ti.length > 0 ){
        now = ti[0].getIndex();
        if(ti.length > 0 ){
          then = ti[1].getIndex();
          diff = now - then;
        }
      }

    }
    catch (Exception ex) { }
    float factor = (now - then)/then;
    return factor;
  }

  private float findLastTariffIndex(TariffIndex[] ti){
    float f = 1;
    if(ti.length > 0)
      f = ti[0].getIndex();
    return f;
  }

}