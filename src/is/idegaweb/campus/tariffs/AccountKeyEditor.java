package is.idegaweb.campus.tariffs;

import com.idega.block.finance.presentation.*;
import is.idegaweb.campus.tariffs.*;
import com.idega.block.finance.presentation.*;
import com.idega.block.finance.data.*;
import com.idega.block.finance.business.Finder;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.textObject.*;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import com.idega.data.EntityFinder;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class AccountKeyEditor extends KeyEditor {

  public String strAction = "ake_action";

  public AccountKeyEditor(){
    this("");
  }

  public AccountKeyEditor(String sHeader){
    super(sHeader);
  }

  public void setColors(String LightColor,String MainColor,String DarkColor){
    if(LightColor.startsWith("#"))
      this.LightColor = LightColor;
    if(MainColor.startsWith("#"))
      this.MiddleColor = MainColor;
    if(DarkColor.startsWith("#"))
      this.DarkColor = DarkColor;
  }
  public void setHeaderText(String sHeader){
    this.sHeader = sHeader;
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

  private void doMain(ModuleInfo modinfo){

    AccountKey[] keys = Finder.findAccountKeys();
    int count = keys.length;
    Table keyTable = new Table(4,count+1);
    keyTable.setWidth("100%");
    keyTable.setHorizontalZebraColored(LightColor,WhiteColor);
    keyTable.setRowColor(1,MiddleColor);
    keyTable.setCellpadding(2);
    keyTable.setCellspacing(1) ;
    keyTable.add(formatText("Nr"),1,1);
    keyTable.add(formatText("Auðkenni"),2,1);
    keyTable.add(formatText("Lýsing"),3,1);
    keyTable.add(formatText("Gjaldliður"),4,1);

    Hashtable hk = getKeys();
    if(isAdmin){
      if(count > 0){
        for (int i = 0;i < count;i++){
          keyTable.add(formatText(String.valueOf(i+1)),1,i+2);
          keyTable.add(formatText(keys[i].getName()),2,i+2);
          keyTable.add(formatText(keys[i].getInfo()),3,i+2);
          Integer tkid = new Integer(keys[i].getTariffKeyId());
          if(hk.containsKey(tkid))
            keyTable.add( formatText( (String)hk.get( tkid) ),4,i+2);
        }
      }
    }
    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(keyTable);
  }

  private void doChange(ModuleInfo modinfo) throws SQLException{
    Form myForm = new Form();
    myForm.maintainAllParameters();
    AccountKey[] keys = Finder.findAccountKeys();
    int count = keys.length;
    int inputcount = count+5;
    Table inputTable =  new Table(5,inputcount+1);
    inputTable.setWidth("100%");
    inputTable.setCellpadding(2);
    inputTable.setCellspacing(1);
    inputTable.setColumnAlignment(1,"right");
    inputTable.setHorizontalZebraColored(LightColor,WhiteColor);
    inputTable.setRowColor(1,MiddleColor);
    inputTable.add(formatText("Nr"),1,1);
    inputTable.add(formatText("Auðkenni"),2,1);
    inputTable.add(formatText("Lýsing"),3,1);
    inputTable.add(formatText("Gjaldliður"),4,1);
    inputTable.add(formatText("Eyða"),5,1);
    List TK = null;
    try{
      TK = EntityFinder.findAll(new TariffKey());
    }
    catch(SQLException sql){}

    for (int i = 1; i <= inputcount ;i++){
      String rownum = String.valueOf(i);
      String s = "";
      TextInput nameInput, infoInput;
      HiddenInput idInput;
      CheckBox delCheck;
      DropdownMenu iDrp =  keyDrp(TK);
      iDrp.setName("ake_keydrp"+i);
      setStyle(iDrp);
      int pos;
      if(i <= count ){
        pos = i-1;
        nameInput  = new TextInput("ake_nameinput"+i,(keys[pos].getName()));
        infoInput = new TextInput("ake_infoinput"+i,(keys[pos].getInfo()));
        String sId = String.valueOf(keys[pos].getID());
        idInput = new HiddenInput("ake_idinput"+i,sId);
        delCheck = new CheckBox("ake_delcheck"+i,"true");
        iDrp.setSelectedElement(String.valueOf(keys[pos].getTariffKeyId()));
        setStyle(delCheck);
        inputTable.add(delCheck,5,i+1);
      }
      else{
        nameInput  = new TextInput("ake_nameinput"+i);
        infoInput = new TextInput("ake_infoinput"+i);
        idInput = new HiddenInput("ake_idinput"+i,"-1");
      }
      nameInput.setSize(20);
      infoInput.setSize(40);

      setStyle(nameInput);
      setStyle(infoInput);

      inputTable.add(formatText(rownum),1,i+1);
      inputTable.add(nameInput,2,i+1);
      inputTable.add(infoInput,3,i+1);
      inputTable.add(iDrp,4,i+1);
      inputTable.add(idInput);
    }
    myForm.add(new HiddenInput("ake_count", String.valueOf(inputcount) ));
    myForm.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    myForm.add(inputTable);
    myForm.add(new SubmitButton("Vista"));

    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(myForm);
  }

  private void doUpdate(ModuleInfo modinfo) throws SQLException{
    int count = Integer.parseInt(modinfo.getParameter("ake_count"));
    String sName,sInfo,sDel,sTKid;
    int ID,TKid;
    AccountKey key = null;

    for (int i = 1; i < count+1 ;i++){
      sName = modinfo.getParameter("ake_nameinput"+i ).trim();
      sInfo = modinfo.getParameter("ake_infoinput"+i).trim();
      sDel = modinfo.getParameter("ake_delcheck"+i);
      sTKid = modinfo.getParameter("ake_keydrp"+i);
      TKid = Integer.parseInt(sTKid);
      ID = Integer.parseInt(modinfo.getParameter("ake_idinput"+i));
      if(ID != -1 && TKid > 0){
        try{
          key = new AccountKey(ID);
          if(sDel != null && sDel.equalsIgnoreCase("true")){
            key.delete();
          }
          else{
            key.setName(sName);
            key.setInfo(sInfo);
            key.setTariffKeyId(TKid);
            key.update();
          }
        }
        catch(SQLException e){
          e.printStackTrace();
        }
      }
      else {
        if(!sName.equalsIgnoreCase("")){
          try {
            key = new AccountKey();
            key.setName(sName);
            key.setInfo(sInfo);
            key.setTariffKeyId(TKid);
            key.insert();
          }
          catch (Exception ex) {
          }
        }
      }
    }// for loop

   doMain(modinfo);
  }

  private Hashtable getKeys(){
    Hashtable h = new Hashtable();
    List TK = null;
    try{
      TK = EntityFinder.findAll(new TariffKey());
    }
    catch(SQLException sql){}
    if(TK != null){
      int len = TK.size();
      for (int i = 0; i < len; i++) {
        TariffKey T = (TariffKey) TK.get(i);
        h.put(new Integer(T.getID()),T.getName());
      }
    }
    return h;

  }

  private DropdownMenu keyDrp(List TK){
    DropdownMenu drp = new DropdownMenu();
    drp.addMenuElement(0,"--");
    if(TK != null)
      drp.addMenuElements(TK);
    return drp;
  }

}// class AccountKeyEditor
