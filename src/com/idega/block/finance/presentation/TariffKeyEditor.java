package com.idega.block.finance.presentation;


import com.idega.block.finance.business.*;
import com.idega.block.finance.data.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.util.text.*;
import java.sql.*;
import java.util.*;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class TariffKeyEditor extends Finance {


  public String strAction = "tke_action";
  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;


  public String getLocalizedNameKey(){
    return "tariffkey";
  }

  public String getLocalizedNameValue(){
    return "Tariffkey";
  }

  protected void control(IWContext iwc){
    if(isAdmin){
      try{
        PresentationObject MO = new Text();

        if(iwc.getParameter(strAction) == null){
          MO = getMain(iwc,iCategoryId);
        }
        if(iwc.getParameter(strAction) != null){
          String sAct = iwc.getParameter(strAction);
          int iAct = Integer.parseInt(sAct);

          switch (iAct) {
            case ACT1 : MO = getMain(iwc,iCategoryId);        break;
            case ACT2 : MO = getChange(iwc,iCategoryId);      break;
            case ACT3 : MO = doUpdate(iwc,iCategoryId);      break;
            default: MO = getMain(iwc,iCategoryId);           break;
          }
        }
        Table T = new Table(1,3);
        T.add(Edit.headerText(iwrb.getLocalizedString("tariff_key_editor","Tariff key editor"),3),1,1);
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

  protected PresentationObject makeTabTable(int iCategoryId, int iTariffGroupId){
    Table T = new Table();
    List groups = FinanceFinder.getInstance().listOfTariffGroups(iCategoryId);
    return T;
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
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link1.addParameter(Finance.getCategoryParameter(iCategoryId));
    Link Link2 = new Link(iwrb.getLocalizedString("change","Change"));
    Link2.setFontColor(Edit.colorLight);
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    Link2.addParameter(Finance.getCategoryParameter(iCategoryId));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }

  protected PresentationObject getMain(IWContext iwc,int iCategoryId){
    Table keyTable = new Table();
    List keys = FinanceFinder.getInstance().listOfTariffKeys(iCategoryId);
    int count = 0;
    if(keys !=null)
      count = keys.size();
    keyTable = new Table(3,count+1);
    keyTable.setWidth("100%");
    keyTable.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    keyTable.setRowColor(1,Edit.colorMiddle);
    keyTable.setCellpadding(2);
    keyTable.setCellspacing(1) ;
    //keyTable.setColumnAlignment(3, "right");
    keyTable.add(Edit.formatText("Nr"),1,1);
    keyTable.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),2,1);
    keyTable.add(Edit.formatText(iwrb.getLocalizedString("info","Info")),3,1);
    if(isAdmin){
      if(count > 0){
        TariffKey key;
        for (int i = 0;i < count;i++){
         key = (TariffKey) keys.get(i);
          keyTable.add(Edit.formatText( String.valueOf(i+1)),1,i+2);
          keyTable.add(Edit.formatText(key.getName()),2,i+2);
          keyTable.add(Edit.formatText(key.getInfo()),3,i+2);
        }
      }
    }
    return (keyTable);
  }

  protected PresentationObject getChange(IWContext iwc,int iCategoryId) throws SQLException{
    Form myForm = new Form();
    myForm.add(Finance.getCategoryParameter(iCategoryId));
    //myForm.maintainAllParameters();
    List keys = FinanceFinder.getInstance().listOfTariffKeys(iCategoryId);
    int count = 0;
    if(keys !=null)
      count = keys.size();
      int inputcount = count+5;
      Table inputTable =  new Table(4,inputcount+1);
      inputTable.setWidth("100%");
      inputTable.setCellpadding(2);
      inputTable.setCellspacing(1);
     // inputTable.setColumnAlignment(1,"right");
      inputTable.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
      inputTable.setRowColor(1,Edit.colorMiddle);
      inputTable.add(Edit.formatText("Nr"),1,1);
      inputTable.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),2,1);
      inputTable.add(Edit.formatText(iwrb.getLocalizedString("info","Info")),3,1);
      inputTable.add(Edit.formatText(iwrb.getLocalizedString("delete","Delete")),4,1);

      TariffKey key ;
      for (int i = 1; i <= inputcount ;i++){

        String rownum = String.valueOf(i);
        String s = "";
        TextInput nameInput, infoInput;
        HiddenInput idInput;
        CheckBox delCheck;
        int pos;
        if(i <= count ){
          pos = i-1;
          key = (TariffKey) keys.get(pos);
          nameInput  = new TextInput("tke_nameinput"+i,key.getName());
          infoInput = new TextInput("tke_infoinput"+i,key.getInfo());
          idInput = new HiddenInput("tke_idinput"+i,String.valueOf(key.getID()));
          delCheck = new CheckBox("tke_delcheck"+i,"true");
          Edit.setStyle(delCheck);
          inputTable.add(delCheck,4,i+1);
        }
        else{
          nameInput  = new TextInput("tke_nameinput"+i);
          infoInput = new TextInput("tke_infoinput"+i);
          idInput = new HiddenInput("tke_idinput"+i,"-1");
        }
        nameInput.setSize(20);
        infoInput.setSize(40);

        Edit.setStyle(nameInput);
        Edit.setStyle(infoInput);

        inputTable.add(Edit.formatText(rownum),1,i+1);
        inputTable.add(nameInput,2,i+1);
        inputTable.add(infoInput,3,i+1);
        inputTable.add(idInput);
      }
      myForm.add(new HiddenInput("tke_count", String.valueOf(inputcount) ));
      myForm.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
      myForm.add(inputTable);
      SubmitButton save = new SubmitButton(iwrb.getLocalizedString("save","Save"));
      Edit.setStyle(save);
      myForm.add(save);


    return (myForm);
  }

  protected PresentationObject doUpdate(IWContext iwc, int iCategoryId){
    int count = Integer.parseInt(iwc.getParameter("tke_count"));
    String sName,sInfo,sDel;
    int ID;
    TariffKey[] keys = new TariffKey[count];
    TariffKey key = null;

    for (int i = 1; i < count+1 ;i++){
      sName = iwc.getParameter("tke_nameinput"+i );
      sInfo = iwc.getParameter("tke_infoinput"+i);
      sDel = iwc.getParameter("tke_delcheck"+i);
      ID = Integer.parseInt(iwc.getParameter("tke_idinput"+i));
      if(sDel != null && sDel.equalsIgnoreCase("true")){
        FinanceBusiness.deleteTariffKey(ID);
      }
      else if (!"".equals(sName)){
        FinanceBusiness.saveTariffKey(ID,sName,sInfo,iCategoryId);
      }
    }// for loop
    return getMain(iwc,iCategoryId);
  }

  public void main(IWContext iwc){
    control(iwc);
  }
}// class TariffKeyEditor
