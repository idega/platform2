package com.idega.block.finance.presentation;


import com.idega.block.finance.business.*;
import com.idega.block.finance.data.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.util.*;
import com.idega.util.text.*;
import java.sql.*;
import java.text.*;
import java.util.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class TariffIndexEditor extends Finance {


  public static String strAction = "ti_action";
  public static String RentType = com.idega.block.finance.data.TariffIndexBMPBean.A;
  public static String ElType = com.idega.block.finance.data.TariffIndexBMPBean.B;
  public static String HeatType = com.idega.block.finance.data.TariffIndexBMPBean.C;
  public static String[] Types = {RentType,ElType,HeatType};
  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;

   public String getLocalizedNameKey(){
    return "indices";
  }

  public String getLocalizedNameValue(){
    return "Indices";
  }
  protected void control(IWContext iwc){
    if(isAdmin){
      try{
        PresentationObject MO = new Text("nothing");
        if(iwc.getParameter(strAction) == null){
          MO = getMainTable(iwc,iCategoryId);
        }
        if(iwc.getParameter(strAction) != null){
          String sAct = iwc.getParameter(strAction);
          int iAct = Integer.parseInt(sAct);

          switch (iAct) {
            case ACT1 : MO =  getMainTable(iwc,iCategoryId);        break;
            case ACT2 : MO = getChangeTable(iwc,iCategoryId);      break;
            case ACT3 : MO = doUpdate(iwc,iCategoryId);      break;
            default: MO = getMainTable(iwc,iCategoryId);           break;
          }
        }
        Table T = new Table(1,3);
          T.setCellpadding(0);
          T.setCellspacing(0);
          add(Edit.headerText(iwrb.getLocalizedString("tariff_index_editor","Tariff index editor"),3));
          T.add(makeLinkTable(1,iCategoryId));
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

  protected PresentationObject makeLinkTable(int menuNr,int iCategoryId){
    Table LinkTable = new Table(3,1);
    int last = 3;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(Edit.colorDark);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link(iwrb.getLocalizedString("view","View"));
    Link1.setFontColor(Edit.colorLight);
    Link1.setBold();
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link1.addParameter(Finance.getCategoryParameter(iCategoryId));
    Link Link2 = new Link(iwrb.getLocalizedString("change","Change"));
    Link2.setFontColor(Edit.colorLight);
    Link2.setBold();
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    Link2.addParameter(Finance.getCategoryParameter(iCategoryId));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }

  protected PresentationObject getMainTable(IWContext iwc,int iCategoryId){
    DateFormat dfLong = DateFormat.getDateInstance(DateFormat.LONG,iwc.getCurrentLocale());
    List L = getIndices(iCategoryId);
    int count = 0;
    if(L!= null)
      count = L.size();
    Table keyTable = new Table(6,count+1);
    keyTable.setWidth("100%");
    keyTable.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    keyTable.setRowColor(1,Edit.colorMiddle);
    keyTable.setCellpadding(2);
    keyTable.setCellspacing(1) ;
    //keyTable.setColumnAlignment(3, "right");
    keyTable.add(Edit.formatText("Nr"),1,1);
    keyTable.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),2,1);
    keyTable.add(Edit.formatText(iwrb.getLocalizedString("info","Info")),3,1);
    keyTable.add(Edit.formatText(iwrb.getLocalizedString("index","Index")),4,1);
    keyTable.add(Edit.formatText(iwrb.getLocalizedString("date","date")),5,1);
    keyTable.add(Edit.formatText(iwrb.getLocalizedString("type","Type")),6,1);
    if(isAdmin){
      if(count > 0){
        for (int i = 0;i < count;i++){
          TariffIndex ti = (TariffIndex) L.get(i);
          keyTable.add(Edit.formatText( String.valueOf(i+1)),1,i+2);
          keyTable.add(Edit.formatText(ti.getName()),2,i+2);
          keyTable.add(Edit.formatText(ti.getInfo()),3,i+2);
          keyTable.add(Edit.formatText(Float.toString(ti.getIndex())),4,i+2);
          keyTable.add(Edit.formatText(dfLong.format(ti.getDate())),5,i+2);
          keyTable.add(Edit.formatText(ti.getType()),6,i+2);
        }
      }
    }
    return (keyTable);
  }

  protected PresentationObject getChangeTable(IWContext iwc,int iCategoryId) throws SQLException{
    Form myForm = new Form();
    myForm.maintainAllParameters();
    List L= getIndices(iCategoryId);
    String t = com.idega.block.finance.data.TariffIndexBMPBean.indexType;
    int count = 0;
    if(L!= null)
      count = L.size();
    int inputcount = count+5;
    Table inputTable =  new Table(7,inputcount+1);
    inputTable.setWidth("100%");
    inputTable.setCellpadding(2);
    inputTable.setCellspacing(1);
    inputTable.setColumnAlignment(1,"left");
    inputTable.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    inputTable.setRowColor(1,Edit.colorMiddle);
    inputTable.add(Edit.formatText("Nr"),1,1);
    inputTable.add(Edit.formatText("Auðkenni"),2,1);
    inputTable.add(Edit.formatText("Lýsing"),3,1);
    inputTable.add(Edit.formatText("Stuðull"),4,1);
    inputTable.add(Edit.formatText("Týpa"),5,1);
    inputTable.add(Edit.formatText("Eyða"),6,1);

    for (int i = 1; i <= inputcount ;i++){
      String rownum = String.valueOf(i);
      TextInput nameInput, infoInput,indexInput;
      HiddenInput idInput;
      CheckBox delCheck;
      DropdownMenu typeDrp;
      int pos;
      if(i <= count ){
        pos = i-1;
        TariffIndex ti = (TariffIndex) L.get(pos);
        nameInput  = new TextInput("ti_nameinput"+i,(ti.getName()));
        infoInput = new TextInput("ti_infoinput"+i,(ti.getInfo()));
        indexInput = new TextInput("ti_indexinput"+i,(String.valueOf(ti.getIndex())));
        typeDrp = typeDrop("ti_typedrp"+i,ti.getType());
        idInput = new HiddenInput("ti_idinput"+i,String.valueOf(ti.getID()));
        delCheck = new CheckBox("ti_delcheck"+i,"true");
        Edit.setStyle(delCheck);
        inputTable.add(delCheck,6,i+1);
      }
      else{
        nameInput  = new TextInput("ti_nameinput"+i);
        infoInput = new TextInput("ti_infoinput"+i);
        indexInput = new TextInput("ti_indexinput"+i);
        typeDrp = typeDrop("ti_typedrp"+i,String.valueOf(t.charAt(i-1)));
        idInput = new HiddenInput("ti_idinput"+i,"-1");
      }
      nameInput.setSize(20);
      infoInput.setSize(40);
      indexInput.setSize(10);

      Edit.setStyle(nameInput);
      Edit.setStyle(infoInput);
      Edit.setStyle(indexInput);
      Edit.setStyle(typeDrp);

      inputTable.add(Edit.formatText(rownum),1,i+1);
      inputTable.add(nameInput,2,i+1);
      inputTable.add(infoInput,3,i+1);
      inputTable.add(indexInput,4,i+1);
      inputTable.add(typeDrp,5,i+1);
      inputTable.add(idInput);

    }
    myForm.add(new HiddenInput("ti_count", String.valueOf(inputcount) ));
    myForm.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    myForm.add(Finance.getCategoryParameter(iCategoryId));
    myForm.add(inputTable);
    myForm.add(new SubmitButton("save","Vista"));

    return (myForm);
  }

  protected PresentationObject doUpdate(IWContext iwc,int iCategoryId) throws SQLException{
    int count = Integer.parseInt(iwc.getParameter("ti_count"));
    String sName,sInfo,sDel,sIndex,sType;
    int ID;
    float findex = 0;
    TariffIndex ti = null;
    for (int i = 1; i < count+1 ;i++){
      sName = iwc.getParameter("ti_nameinput"+i ).trim();
      sInfo = iwc.getParameter("ti_infoinput"+i).trim();
      sIndex = iwc.getParameter("ti_indexinput"+i).trim();
      sDel = iwc.getParameter("ti_delcheck"+i);
      sType = iwc.getParameter("ti_typedrp"+i).trim();
      ID = Integer.parseInt(iwc.getParameter("ti_idinput"+i));
      java.sql.Timestamp stamp = IWTimestamp.getTimestampRightNow();
      if(!"".equals(sIndex)){
        sIndex = sIndex.replace(',','.');
        findex = Float.parseFloat(sIndex);

      }

      if(ID != -1 ){
        ti = FinanceFinder.getInstance().getTariffIndex(ID) ;
        float oldvalue = ti.getNewValue();
        if( sDel != null && sDel.equalsIgnoreCase("true")){
            FinanceBusiness.deleteTariffIndex(ID);
        }
        else if(!sName.equalsIgnoreCase(ti.getName()) || !sInfo.equalsIgnoreCase(ti.getInfo()) ||
                !sType.equalsIgnoreCase(ti.getType()) || !(findex == ti.getIndex())  ){
          FinanceBusiness.saveTariffIndex(-1,sName,sInfo,sType,findex,oldvalue,stamp,iCategoryId);
        }
      }
      else if(!"".equalsIgnoreCase(sName) && !"".equals(sIndex)){
        FinanceBusiness.saveTariffIndex(-1,sName,sInfo,sType,findex,findex,stamp,iCategoryId);
      }
    }// for loop

   return getMainTable(iwc,iCategoryId);
  }


  private DropdownMenu typeDrop(String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    String s = com.idega.block.finance.data.TariffIndexBMPBean.indexType;
    int len = s.length();
    for (int i = 0; i < len; i++) {
      drp.addMenuElement(String.valueOf(s.charAt(i)));
    }
    drp.setSelectedElement(selected);
    return drp;
  }

  private List getIndices(int iCategoryId){
    Vector V = new Vector();
    for (int i = 0; i < com.idega.block.finance.data.TariffIndexBMPBean.indexType.length(); i++) {
      TariffIndex ti= FinanceFinder.getInstance().getTariffIndex(String.valueOf(com.idega.block.finance.data.TariffIndexBMPBean.indexType.charAt(i)),iCategoryId);
      if(ti!= null)
        V.add(ti);
    }
    return V;
  }


  public void main(IWContext iwc){
    control(iwc);
  }

}// class TariffKeyEditor