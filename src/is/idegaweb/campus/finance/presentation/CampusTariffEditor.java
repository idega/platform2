/*
 * $Id: CampusTariffEditor.java,v 1.5 2001/10/17 12:54:25 gummi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.finance.presentation;

import is.idegaweb.campus.presentation.Edit;
import com.idega.block.finance.data.*;
import com.idega.block.building.data.*;
import com.idega.block.finance.business.Finder;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.business.BuildingCacher;
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
import java.util.Map;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import com.idega.presentation.Editor;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import java.text.DecimalFormat;
import com.idega.util.text.TextSoap;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusTariffEditor extends PresentationObjectContainer{


  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4;
  protected final int ACT5 = 5,ACT6 = 6, ACT7 = 7,ACT8  = 8;
  public  String strAction = "te_action";
  public final   int YEAR=1,MONTH=2,WEEK=3,DAY=4;
  private idegaTimestamp workingPeriod;
  private int period = MONTH;
  private GenericEntity[] entities = null;
  private int iPeriod;
  private int iNumberOfDecimals = 0;
  private boolean bRoundAmounts = true;;

  public static String prefixComplex = "x";
  public static String prefixAll = "a";
  public static String prefixBuilding = "b";
  public static String prefixFloor = "f";
  public static String prefixCategory = "c";
  public static String prefixType = "t";
  public static String prefixApartment = "p";

  protected boolean isAdmin = false;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusTariffEditor() {

  }

  public void setRoundAmounts(boolean round){
    bRoundAmounts = round;
  }

  public void setNumberOfDecimals(int decimals){
    iNumberOfDecimals = decimals;
  }

  public void setEntities(GenericEntity[] entities){
    this.entities = entities;
  }

  protected void control(IWContext iwc){
    this.getParentPage().setAllMargins(0);
    if(isAdmin){
    try{
      PresentationObject MO = new Text();

      if(iwc.getParameter("updateindex")!=null){
        MO =doUpdateIndex(iwc);
      }
      else if(iwc.getParameter("savetariffs")!=null){
        MO =doUpdate(iwc);
      }
      else if(iwc.getParameter(strAction) != null){
        String sAct = iwc.getParameter(strAction);
        int iAct = Integer.parseInt(sAct);
        switch (iAct) {
          case ACT1 : MO =getMain(iwc);        break;
          case ACT2 : MO =getChange(iwc,false,false);break;
          case ACT3 : MO =doUpdate(iwc);      break;
          case ACT4 : MO =getChange(iwc,true,false); break;
          default: MO =getMain(iwc);           break;
        }
      }
      else{
        MO = getMain(iwc);
      }
    Table T = new Table(1,3);
        T.add(Edit.headerText(iwrb.getLocalizedString("tariff_editor","Tariff editor"),3),1,1);
        T.add(makeLinkTable(1));
        T.add(MO);
        T.setWidth("100%");
        add(T);
      }
      catch(Exception S){
        S.printStackTrace();
      }
    }
    else
      add(iwrb.getLocalizedString("access_denied","Access denies"));
  }
  protected PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(4,1);
    int last = 4;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(Edit.colorDark);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link(iwrb.getLocalizedString("view","View"));
    Link1.setFontColor(Edit.colorLight);
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link Link2 = new Link(iwrb.getLocalizedString("change","Change"));
    Link2.setFontColor(Edit.colorLight);
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    Link Link3 = new Link(iwrb.getLocalizedString("new","New"));
    Link3.setFontColor(Edit.colorLight);
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
  protected PresentationObject getPeriodChooser(int init){
    PresentationObject mo;
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
  protected PresentationObject getMain(IWContext iwc){
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
    T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    T.setRowColor(1,Edit.colorMiddle);
    T.setCellpadding(2);
    T.setCellspacing(1) ;
    T.add(Edit.formatText("Nr"),1,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("connection","Connection")),2,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),3,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("amount","Amount")),4,1);
    //T.add(Edit.formatText(iwrb.getLocalizedString("info","Info")),5,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("account_key","Account key")),5,1);
    idegaTimestamp from = this.getDateFrom();
    idegaTimestamp to = this.getDateTo();
    if(isAdmin){
      if(count != 0){
        for (int i = 0;i < count;i++){
          T.add(Edit.formatText( String.valueOf(i+1)),1,i+2);
          String tatt = tariffs[i].getTariffAttribute();
          if(hLodgings.containsKey(tatt))
            T.add(Edit.formatText(((GenericEntity)hLodgings.get(tatt)).getName()),2,i+2);
          T.add(Edit.formatText(tariffs[i].getName()),3,i+2);
          T.add(Edit.formatText(String.valueOf(tariffs[i].getPrice())),4,i+2);
          //T.add(Edit.formatText(tariffs[i].getInfo()),4,i+2);
          Integer I = new Integer(tariffs[i].getAccountKeyId());
          if(hAK.containsKey(I))
            T.add(Edit.formatText((String)hAK.get(I)),5,i+2);
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
    T3.add(Edit.formatText("Tímabil:"),1,1);
    T3.add(Edit.formatText(from.getISLDate(".",true)),2,1);
    T3.add(Edit.formatText(" til "),3,1);
    T3.add(Edit.formatText(to.getISLDate(".",true)),4,1);
    //T2.add(T3,1,1);
    T2.add(T,1,1);
    return T2;

  }

  private PresentationObject doUpdateIndex(IWContext iwc){
    /** @todo  *
     *
     */
    return getChange(iwc,false,true);
  }

  protected PresentationObject getChange(IWContext iwc,boolean ifnew,boolean factor){
    Form myForm = new Form();
    myForm.maintainAllParameters();
    boolean updateIndex = factor;
    idegaTimestamp today = new idegaTimestamp();
    Tariff[] tariffs = Finder.findTariffs();
    List AK = Finder.getAccountKeys();
    TariffIndex[] TI = getTariffIndices();
    Hashtable hash = BuildingFinder.getLodgingsHash();

    List BL = BuildingCacher.getBuildings();
    List FL= BuildingCacher.getFloors();
    List TL = BuildingCacher.getTypes();
    List CL = BuildingCacher.getCategories();
    List XL = BuildingCacher.getComplexes();

    List indices = Finder.listOfTypeGroupedIndices();
    Map M = Finder.mapOfIndicesByTypes(indices);



    int count = tariffs.length;
    int inputcount = count+5;
    Table BorderTable = new Table();
    BorderTable.setCellpadding(1);
    BorderTable.setCellspacing(0);
    BorderTable.setColor(Edit.colorDark);
    BorderTable.setWidth("100%");
    Table T2 = new Table(1,3);
    T2.setColor(Edit.colorWhite);
    T2.setCellpadding(0);
    T2.setCellpadding(0);
    T2.setWidth("100%");
    Table T =  new Table(8,inputcount+1);
    T.setWidth("100%");
    T.setCellpadding(2);
    T.setCellspacing(1);
    T.setColumnAlignment(1,"right");
    T.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    T.setRowColor(1,Edit.colorMiddle);
    T.add(Edit.formatText("Nr"),1,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("connection","Connection")),2,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),3,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("amount","Amount")),4,1);
    //T.add(Edit.formatText(iwrb.getLocalizedString("info","Info")),5,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("account_key","Account key")),5,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("index","Index")),6,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("updated","Updated")),7,1);
    T.add(Edit.formatText(iwrb.getLocalizedString("delete","Delete")),8,1);
    idegaTimestamp from = this.getDateFrom();
    idegaTimestamp to = this.getDateTo();
    if(count != 0 && tariffs[0]!=null){
      from = new idegaTimestamp(tariffs[0].getUseFromDate());
      to = new idegaTimestamp(tariffs[0].getUseToDate());
    }

    for (int i = 1; i <= inputcount ;i++){
      String rownum = String.valueOf(i);
      String s = "";
      int hid = -1;
      TextInput nameInput,priceInput,infoInput;
      DropdownMenu drpAtt,drpAK,drpIx;
      HiddenInput idInput;
      CheckBox delCheck;
      drpAtt = drpLodgings("te_attdrp"+i,XL,BL,FL,CL,TL);
      drpAK = drpAccountKeys(AK,("te_akdrp"+i));
      drpIx = drpIndicesByType(indices,"te_ixdrp"+i);

      nameInput  = new TextInput("te_nameinput"+i);
      priceInput = new TextInput("te_priceinput"+i);
      infoInput = new TextInput("te_infoinput"+i);
      //drpAtt = this.drpLodgings("te_attdrp"+i,"",XL,BL,FL,CL,TL);
      //drpAK = this.drpAccountKeys(AK,"te_akdrp"+i,"");


      int pos;
      if(i <= count && !ifnew ){
        pos = i-1;
        float iPrice = tariffs[pos].getPrice();
        String ixType = tariffs[pos].getIndexType();
        String ixDate = iwc.getParameter("te_ixdate"+i);
        idegaTimestamp ixdate = null;

        if(ixDate != null){
          ixdate = new idegaTimestamp(ixDate);
        }
        else if(tariffs[pos].getIndexUpdated() != null){
          ixdate = new idegaTimestamp(tariffs[pos].getIndexUpdated());
          T.add(new HiddenInput("te_ixdate"+i,ixdate.toString()));
        }

        if(updateIndex && ixType != null && M != null && M.containsKey(ixType)){
          TariffIndex ti = (TariffIndex) M.get(ixType);
          java.sql.Timestamp stamp = ti.getDate();
          if(ixdate != null){
            if( !stamp.equals(ixdate.getTimestamp())){
              iPrice = iPrice * getAddFactor(ti.getNewValue(),ti.getOldValue());
            }
            System.err.println(stamp.toString() +" "+ixdate.toString());
          }
          else
            iPrice = iPrice * getAddFactor(ti.getNewValue(),ti.getOldValue());
        }

        iPrice = new Float(TextSoap.decimalFormat((double)iPrice,iNumberOfDecimals)).floatValue();

        if(bRoundAmounts)
          iPrice = Math.round((double)iPrice);

        nameInput.setContent(tariffs[pos].getName());
        if(tariffs[pos].getInfo()!=null)
        infoInput.setContent(tariffs[pos].getInfo());

        priceInput.setContent(String.valueOf(iPrice));

        drpAtt.setSelectedElement(tariffs[pos].getTariffAttribute());
        drpAK.setSelectedElement(String.valueOf(tariffs[pos].getAccountKeyId()));
        drpIx.setSelectedElement(ixType);

        delCheck = new CheckBox("te_delcheck"+i,"true");
        hid = tariffs[pos].getID();
        Edit.setStyle(delCheck);

        T.add(delCheck,8,i+1);
      }

      idInput = new HiddenInput("te_idinput"+i,String.valueOf(hid ));

      nameInput.setSize(20);
      priceInput.setSize(8);
      infoInput.setSize(30);

      Edit.setStyle(nameInput);
      Edit.setStyle(priceInput);
      Edit.setStyle(infoInput);
      Edit.setStyle(drpAtt);
      Edit.setStyle(drpAK);
      Edit.setStyle(drpIx);

      T.add(Edit.formatText(rownum),1,i+1);
      T.add(drpAtt,2,i+1);
      T.add(nameInput,3,i+1);
      T.add(priceInput,4,i+1);
      //T.add(infoInput,5,i+1);
      T.add(drpAK,5,i+1);
      T.add(drpIx,6,i+1);
      //T.add(indexCheck,6,i+1);
      T.add(idInput);
    }
    Table T3 = new Table(8,1);
    T3.setWidth("100%");
    T3.setWidth(5,1,"100%");
    T3.setColumnAlignment(6,"right");
    T3.setColumnAlignment(7,"right");
    TextInput datefrom = new TextInput("te_datefrom",from.getISLDate(".",true));
    datefrom.setLength(8);
    Edit.setStyle(datefrom);
    TextInput dateto = new TextInput("te_dateto",to.getISLDate(".",true));
    dateto.setLength(8);
    Edit.setStyle(dateto);
    //TextInput index = new TextInput("te_index",sIndex);
    //HiddenInput hindex = new HiddenInput("te_hindex",sHindex);
    dateto.setLength(8);
    //Edit.setStyle(index);
    SubmitButton update = new SubmitButton("updateindex",iwrb.getLocalizedString("update","Update"));
    Edit.setStyle(update);
    T3.add(Edit.formatText(iwrb.getLocalizedString("period","Period")),1,1);
    T3.add(datefrom,3,1);
    T3.add(dateto,4,1);
    //T3.add(Edit.formatText(iwrb.getLocalizedString("index","Index")),6,1);
    //T3.add(index,7,1);
    //T3.add(hindex,7,1);
    T3.add(update,8,1);
    SubmitButton save = new SubmitButton("savetariffs",iwrb.getLocalizedString("save","Save"));
    Edit.setStyle(save);
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

    return (myForm);
  }

  protected PresentationObject doUpdate(IWContext iwc) {
    Map M = Finder.mapOfIndicesByTypes(Finder.listOfTypeGroupedIndices());
    int count = Integer.parseInt(iwc.getParameter("te_count"));
    String sName,sInfo,sDel,sPrice,sAtt,sAK,sTK,sID,sDateFrom,sDateTo,sIndex,sIndexStamp;
    int ID,Attid,AKid,TKid;
    float Price;
    boolean bIndex;
    idegaTimestamp dFrom,dTo;

    Tariff tariff = null;
    sDateFrom = iwc.getParameter("te_datefrom");
    dFrom = this.parseStamp(sDateFrom);
    sDateTo = iwc.getParameter("te_dateto");
    dTo = this.parseStamp(sDateTo);

    for (int i = 1; i < count+1 ;i++){
      sName = iwc.getParameter("te_nameinput"+i);
      sPrice = (iwc.getParameter("te_priceinput"+i));
      sInfo = iwc.getParameter("te_infoinput"+i);
      sAtt = iwc.getParameter("te_attdrp"+i);
      sAK = (iwc.getParameter("te_akdrp"+i));
      sIndex = (iwc.getParameter("te_ixdrp"+i));
      sDel = iwc.getParameter("te_delcheck"+i);
      sID = iwc.getParameter("te_idinput"+i);
      sIndexStamp = iwc.getParameter("te_ixdate"+i);
      idegaTimestamp stamp = sIndexStamp!= null ?new idegaTimestamp(sIndexStamp):null;

      if(stamp == null && sIndex !=null && M!=null && M.containsKey(sIndex)){
        stamp = new idegaTimestamp(((TariffIndex)M.get(sIndex)).getDate());
      }

      if(sIndex != null && !sIndex.equals("-1")){
        bIndex = true;
      }
      else{
        bIndex = false;
        sIndex = "";
      }

      ID = Integer.parseInt(iwc.getParameter("te_idinput"+i));
      if(ID != -1){
        try{
          tariff = new Tariff(ID);
          if(sDel != null && sDel.equalsIgnoreCase("true")){
            tariff.delete();
          }
          else{
            tariff.setName(sName);
            tariff.setInfo(sInfo);
            tariff.setPrice(Float.parseFloat(sPrice));
            tariff.setAccountKeyId(Integer.parseInt(sAK));
            tariff.setUseFromDate(dFrom.getTimestamp());
            tariff.setUseToDate(dTo.getTimestamp());
            tariff.setTariffAttribute(sAtt);
            tariff.setIndexType(sIndex);
            tariff.setUseIndex(bIndex);
            if(stamp!=null)
              tariff.setIndexUpdated(stamp.getTimestamp());
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
            tariff.setPrice(Float.parseFloat(sPrice));
            tariff.setAccountKeyId(Integer.parseInt(sAK));
            tariff.setUseFromDate(dFrom.getTimestamp());
            tariff.setUseToDate(dTo.getTimestamp());
            tariff.setTariffAttribute(sAtt);
            tariff.setIndexType(sIndex);
            tariff.setUseIndex(bIndex);
            if(stamp!=null)
              tariff.setIndexUpdated(stamp.getTimestamp());
            tariff.insert();
          }
          catch (SQLException ex) {
          }
        }
      }
    }// for loop

   return getChange(iwc,false,false);
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

  private DropdownMenu drpAccountKeys(List AK,String name){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(0,"--");
    if(AK != null){
      drp.addMenuElements(AK);
    }
    return drp;
  }
  private PresentationObject YearChooser(int year){
    return new Text();
  }
  private PresentationObject MonthChooser(int month){
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
  private PresentationObject WeekChooser(int week){ return new Text();}
  private PresentationObject DayChooser(int day){ return new Text();}

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
    drp.addMenuElement("a",iwrb.getLocalizedString("all","All"));
    GenericEntity G;
    for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
      G = (GenericEntity) e.nextElement();
      drp.addMenuElement(String.valueOf(G.getID()),G.getName());
    }
    if(!selected.equalsIgnoreCase(""))
      drp.setSelectedElement(selected);
    return drp;
  }

  private DropdownMenu drpLodgings(String name,
      List ComplexList,List BuildingList, List FloorList, List CategoryList, List TypeList){
    Hashtable Bhash = new Hashtable();
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(0,"--");
    drp.addMenuElement("a",iwrb.getLocalizedString("all","All"));
    if(TypeList != null){
      drp.addSeparator();
      int len = TypeList.size();
      ApartmentType T;
      for (int i = 0; i < len; i++) {
        T = (ApartmentType) TypeList.get(i);
        drp.addMenuElement(String.valueOf(prefixType+"_"+T.getID()),T.getName());
      }
    }
    if(CategoryList != null){
      drp.addSeparator();
      int len = CategoryList.size();
      ApartmentCategory C;
      for (int i = 0; i < len; i++) {
        C = (ApartmentCategory) CategoryList.get(i);
        drp.addMenuElement(String.valueOf(prefixCategory+"_"+C.getID()),C.getName());
      }
    }
    if(ComplexList != null){
      drp.addSeparator();
      int clen = ComplexList.size();
      Complex C;
      for (int i = 0; i < clen; i++) {
        C = (Complex) ComplexList.get(i);
        drp.addMenuElement(String.valueOf(prefixComplex+"_"+C.getID()),C.getName());
      }
    }
    if(BuildingList != null){
      drp.addSeparator();
      int clen = BuildingList.size();
      Building B;
      for (int i = 0; i < clen; i++) {
        B = (Building) BuildingList.get(i);
        drp.addMenuElement(String.valueOf(prefixBuilding+"_"+B.getID()),B.getName());
        Bhash.put(new Integer(B.getID()),B.getName());
      }
    }
    if(FloorList != null){
      drp.addSeparator();
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
    return drp;
  }

  private DropdownMenu drpIndicesByType(List L,String name){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElementFirst("-1",iwrb.getLocalizedString("index","Index"));
    if(L!= null){
      int len = L.size();
      for (int i = 0; i < len; i++) {
        TariffIndex ti = (TariffIndex) L.get(i);
        drp.addMenuElement(ti.getType(),ti.getName());
      }
      drp.setSelectedElement("-1");
    }
    return drp;
  }

  private TariffIndex[] getTariffIndices(){
    TariffIndex[] ti = new TariffIndex[0];
    try {
       ti = (TariffIndex[])(new TariffIndex()).findAllDescendingOrdered(TariffIndex.getColumnNameDate());
    }
    catch (SQLException ex) {

    }
    return ti;
  }

  private float getDifferenceFactor(float now, float then){
    float factor = (now - then)/then;
    return factor;
  }

  private float getAddFactor(float now, float then){
    return 1+getDifferenceFactor( now,then);
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
