package com.idega.block.finance.presentation;

import com.idega.block.finance.data.*;
import com.idega.block.finance.business.Finder;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.textObject.*;
import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class TariffKeyEditor extends KeyEditor {


  public static String strAction = "tke_action";

  public TariffKeyEditor(String sHeader){
    super(sHeader);
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
          case ACT2 : doChange(modinfo);      break;
          case ACT3 : doUpdate(modinfo);      break;
          default: doMain(modinfo);           break;
        }
      }
    }
    catch(Exception S){
      S.printStackTrace();
    }
  }

  protected ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(3,1);
    int last = 3;
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
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }

  protected void doMain(ModuleInfo modinfo){

    TariffKey[] keys = Finder.findTariffKeys();
    int count = keys.length;
    Table keyTable = new Table(3,count+1);
    keyTable.setWidth("100%");
    keyTable.setHorizontalZebraColored(LightColor,WhiteColor);
    keyTable.setRowColor(1,MiddleColor);
    keyTable.setCellpadding(2);
    keyTable.setCellspacing(1) ;
    //keyTable.setColumnAlignment(3, "right");
    keyTable.add(formatText("Nr"),1,1);
    keyTable.add(formatText("Auðkenni"),2,1);
    keyTable.add(formatText("Lýsing"),3,1);
    if(isAdmin){
      if(count > 0){
        for (int i = 0;i < count;i++){
          keyTable.add(formatText( String.valueOf(i+1)),1,i+2);
          keyTable.add(formatText(keys[i].getName()),2,i+2);
          keyTable.add(formatText(keys[i].getInfo()),3,i+2);
        }
      }
    }
    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(keyTable);
  }

  protected void doChange(ModuleInfo modinfo) throws SQLException{
    Form myForm = new Form();
    myForm.maintainAllParameters();
    TariffKey[] keys = Finder.findTariffKeys();
    int count = keys.length;
    int inputcount = count+5;
    Table inputTable =  new Table(4,inputcount+1);
    inputTable.setWidth("100%");
    inputTable.setCellpadding(2);
    inputTable.setCellspacing(1);
    inputTable.setColumnAlignment(1,"right");
    inputTable.setHorizontalZebraColored(LightColor,WhiteColor);
    inputTable.setRowColor(1,MiddleColor);
    inputTable.add(formatText("Nr"),1,1);
    inputTable.add(formatText("Auðkenni"),2,1);
    inputTable.add(formatText("Lýsing"),3,1);
    inputTable.add(formatText("Eyða"),4,1);

    for (int i = 1; i <= inputcount ;i++){
      String rownum = String.valueOf(i);
      String s = "";
      TextInput nameInput, infoInput;
      HiddenInput idInput;
      CheckBox delCheck;
      int pos;
      if(i <= count ){
        pos = i-1;
        nameInput  = new TextInput("tke_nameinput"+i,(keys[pos].getName()));
        infoInput = new TextInput("tke_infoinput"+i,(keys[pos].getInfo()));
        idInput = new HiddenInput("tke_idinput"+i,String.valueOf(keys[pos].getID()));
        delCheck = new CheckBox("tke_delcheck"+i,"true");
        setStyle(delCheck);
        inputTable.add(delCheck,4,i+1);
      }
      else{
        nameInput  = new TextInput("tke_nameinput"+i);
        infoInput = new TextInput("tke_infoinput"+i);
        idInput = new HiddenInput("tke_idinput"+i,"-1");
      }
      nameInput.setSize(20);
      infoInput.setSize(40);

      setStyle(nameInput);
      setStyle(infoInput);

      inputTable.add(formatText(rownum),1,i+1);
      inputTable.add(nameInput,2,i+1);
      inputTable.add(infoInput,3,i+1);
      inputTable.add(idInput);
    }
    myForm.add(new HiddenInput("tke_count", String.valueOf(inputcount) ));
    myForm.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    myForm.add(inputTable);
    myForm.add(new SubmitButton("Vista"));

    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(myForm);
  }

  protected void doUpdate(ModuleInfo modinfo) throws SQLException{
    int count = Integer.parseInt(modinfo.getParameter("tke_count"));
    String sName,sInfo,sDel;
    int ID;
    TariffKey[] keys = new TariffKey[count];
    TariffKey key = null;

    for (int i = 1; i < count+1 ;i++){
      sName = modinfo.getParameter("tke_nameinput"+i );
      sInfo = modinfo.getParameter("tke_infoinput"+i);
      sDel = modinfo.getParameter("tke_delcheck"+i);
      ID = Integer.parseInt(modinfo.getParameter("tke_idinput"+i));
      if(ID != -1){
        try{
          key = new TariffKey(ID);
          if(sDel != null && sDel.equalsIgnoreCase("true")){
            key.delete();
          }
          else{
            key.setName(sName);
            key.setInfo(sInfo);
            key.update();
          }
        }
        catch(SQLException e){
        }
      }
      else {
        if(!sName.equalsIgnoreCase("")){
          try {
            key = new TariffKey();
            key.setName(sName);
            key.setInfo(sInfo);
            key.insert();
          }
          catch (Exception ex) {
          }
        }
      }
    }// for loop

   doMain(modinfo);
  }
}// class TariffKeyEditor