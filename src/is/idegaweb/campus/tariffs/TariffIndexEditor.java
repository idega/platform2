package is.idegaweb.campus.tariffs;

import com.idega.block.finance.data.*;
import com.idega.block.finance.business.Finder;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.textObject.*;
import com.idega.util.idegaTimestamp;
import java.text.DateFormat;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idegaweb.campus.presentation.Edit;
import com.idega.jmodule.object.ModuleObjectContainer;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class TariffIndexEditor extends ModuleObjectContainer {


  public static String strAction = "ti_action";
  public static String RentType = TariffIndex.A;
  public static String ElType = TariffIndex.B;
  public static String HeatType = TariffIndex.C;
  public static String[] Types = {RentType,ElType,HeatType};
  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  protected boolean isAdmin = false;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public TariffIndexEditor(){

  }
  protected void control(ModuleInfo modinfo){
    if(isAdmin){
      try{
        ModuleObject MO = new Text("nothing");
        if(modinfo.getParameter(strAction) == null){
          MO = getMainTable(modinfo);
        }
        if(modinfo.getParameter(strAction) != null){
          String sAct = modinfo.getParameter(strAction);
          int iAct = Integer.parseInt(sAct);

          switch (iAct) {
            case ACT1 : MO =  getMainTable(modinfo);        break;
            case ACT2 : MO = getChangeTable(modinfo);      break;
            case ACT3 : MO = doUpdate(modinfo);      break;
            default: MO = getMainTable(modinfo);           break;
          }
        }
        Table T = new Table(1,3);
          T.setCellpadding(0);
          T.setCellspacing(0);
          add(Edit.headerText(iwrb.getLocalizedString("tariff_index_editor","Tariff index editor"),3));
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

  protected ModuleObject makeLinkTable(int menuNr){
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
    Link Link2 = new Link(iwrb.getLocalizedString("change","Change"));
    Link2.setFontColor(Edit.colorLight);
    Link2.setBold();
    Link2.addParameter(this.strAction,String.valueOf(this.ACT2));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }

  protected ModuleObject getMainTable(ModuleInfo modinfo){
    DateFormat dfLong = DateFormat.getDateInstance(DateFormat.LONG,modinfo.getCurrentLocale());
    List L = getIndices();
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

  protected ModuleObject getChangeTable(ModuleInfo modinfo) throws SQLException{
    Form myForm = new Form();
    myForm.maintainAllParameters();
    List L= getIndices();
    String t = TariffIndex.indexType;
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
      String s = "";
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
    myForm.add(inputTable);
    myForm.add(new SubmitButton("save","Vista"));

    return (myForm);
  }

  protected ModuleObject doUpdate(ModuleInfo modinfo) throws SQLException{
    int count = Integer.parseInt(modinfo.getParameter("ti_count"));
    String sName,sInfo,sDel,sIndex,sType;
    int ID;
    float findex = 0;
    TariffIndex ti = null;
    for (int i = 1; i < count+1 ;i++){
      sName = modinfo.getParameter("ti_nameinput"+i ).trim();
      sInfo = modinfo.getParameter("ti_infoinput"+i).trim();
      sIndex = modinfo.getParameter("ti_indexinput"+i).trim();
      sDel = modinfo.getParameter("ti_delcheck"+i);
      sType = modinfo.getParameter("ti_typedrp"+i).trim();
      ID = Integer.parseInt(modinfo.getParameter("ti_idinput"+i));
      java.sql.Timestamp stamp = idegaTimestamp.getTimestampRightNow();
      if(!"".equals(sIndex))
        findex = Float.parseFloat(sIndex);

      try{
        if(ID != -1 ){
          ti = new TariffIndex(ID);
          float oldvalue = ti.getNewValue();
          if( sDel != null && sDel.equalsIgnoreCase("true")){
              ti.delete();
          }
          else if(!sName.equalsIgnoreCase(ti.getName()) || !sInfo.equalsIgnoreCase(ti.getInfo()) ||
                  !sType.equalsIgnoreCase(ti.getType()) || !(findex == ti.getIndex())  ){

            ti = new TariffIndex();
            ti.setName(sName);
            ti.setInfo(sInfo);
            ti.setNewValue(findex);
            ti.setOldValue(oldvalue);
            ti.setIndex(findex);
            ti.setDate(stamp);
            ti.setType(sType);
            ti.insert();

          }
        }
        else if(!"".equalsIgnoreCase(sName) && !"".equals(sIndex)){

          ti = new TariffIndex();
          ti.setName(sName);
          ti.setInfo(sInfo);
          ti.setNewValue(findex);
          ti.setOldValue(findex);
          ti.setDate(stamp);
          ti.setType(sType);
          ti.insert();
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }


    }// for loop

   return getMainTable(modinfo);
  }


  private DropdownMenu typeDrop(String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    String s = TariffIndex.indexType;
    int len = s.length();
    for (int i = 0; i < len; i++) {
      drp.addMenuElement(String.valueOf(s.charAt(i)));
    }
    drp.setSelectedElement(selected);
    return drp;
  }

  private List getIndices(){
    Vector V = new Vector();
    for (int i = 0; i < TariffIndex.indexType.length(); i++) {
      TariffIndex ti= Finder.getTariffIndex(String.valueOf(TariffIndex.indexType.charAt(i)));
      if(ti!= null)
        V.add(ti);
    }
    return V;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    try{
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    control(modinfo);
  }

}// class TariffKeyEditor