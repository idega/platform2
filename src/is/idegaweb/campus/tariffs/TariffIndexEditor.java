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
import is.idegaweb.campus.entity.TariffIndex;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class TariffIndexEditor extends KeyEditor {


  public static String strAction = "ti_action";
  public static String RentType = TariffIndex.A;
  public static String ElType = TariffIndex.B;
  public static String HeatType = TariffIndex.C;
  public static String[] Types = {RentType,ElType,HeatType};

  public TariffIndexEditor(String sHeader){
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
    DateFormat dfLong = DateFormat.getDateInstance(DateFormat.LONG,modinfo.getCurrentLocale());
    List L = getIndices();
    int count = 0;
    if(L!= null)
      count = L.size();
    Table keyTable = new Table(6,count+1);
    keyTable.setWidth("100%");
    keyTable.setHorizontalZebraColored(LightColor,WhiteColor);
    keyTable.setRowColor(1,MiddleColor);
    keyTable.setCellpadding(2);
    keyTable.setCellspacing(1) ;
    //keyTable.setColumnAlignment(3, "right");
    keyTable.add(formatText("Nr"),1,1);
    keyTable.add(formatText("Auðkenni"),2,1);
    keyTable.add(formatText("Lýsing"),3,1);
    keyTable.add(formatText("Stuðull"),4,1);
    keyTable.add(formatText("Dags"),5,1);
    keyTable.add(formatText("Týpa"),6,1);
    if(isAdmin){
      if(count > 0){
        for (int i = 0;i < count;i++){
          TariffIndex ti = (TariffIndex) L.get(i);
          keyTable.add(formatText( String.valueOf(i+1)),1,i+2);
          keyTable.add(formatText(ti.getName()),2,i+2);
          keyTable.add(formatText(ti.getInfo()),3,i+2);
          keyTable.add(formatText(ti.getIndex()),4,i+2);
          keyTable.add(formatText(dfLong.format(ti.getDate())),5,i+2);
          keyTable.add(formatText(ti.getType()),6,i+2);
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
    List L= getIndices();
    int count = 0;
    if(L!= null)
      count = L.size();
    int inputcount = count+5;
    Table inputTable =  new Table(7,inputcount+1);
    inputTable.setWidth("100%");
    inputTable.setCellpadding(2);
    inputTable.setCellspacing(1);
    inputTable.setColumnAlignment(1,"left");
    inputTable.setHorizontalZebraColored(LightColor,WhiteColor);
    inputTable.setRowColor(1,MiddleColor);
    inputTable.add(formatText("Nr"),1,1);
    inputTable.add(formatText("Auðkenni"),2,1);
    inputTable.add(formatText("Lýsing"),3,1);
    inputTable.add(formatText("Stuðull"),4,1);
    inputTable.add(formatText("Týpa"),5,1);
    inputTable.add(formatText("Eyða"),6,1);

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
        setStyle(delCheck);
        inputTable.add(delCheck,6,i+1);
      }
      else{
        nameInput  = new TextInput("ti_nameinput"+i);
        infoInput = new TextInput("ti_infoinput"+i);
        indexInput = new TextInput("ti_indexinput"+i);
        typeDrp = typeDrop("ti_typedrp"+i,"");
        idInput = new HiddenInput("ti_idinput"+i,"-1");
      }
      nameInput.setSize(20);
      infoInput.setSize(40);
      indexInput.setSize(10);

      setStyle(nameInput);
      setStyle(infoInput);
      setStyle(indexInput);
      setStyle(typeDrp);

      inputTable.add(formatText(rownum),1,i+1);
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

    this.makeView();
    this.addHeader(this.makeLinkTable(0));
    this.addMain(myForm);
  }

  protected void doUpdate(ModuleInfo modinfo) throws SQLException{
    int count = Integer.parseInt(modinfo.getParameter("ti_count"));
    String sName,sInfo,sDel,sIndex,sType;
    int ID;
    TariffIndex ti = null;
    for (int i = 1; i < count+1 ;i++){
      sName = modinfo.getParameter("ti_nameinput"+i );
      sInfo = modinfo.getParameter("ti_infoinput"+i);
      sIndex = modinfo.getParameter("ti_indexinput"+i);
      sDel = modinfo.getParameter("ti_delcheck"+i);
      sType = modinfo.getParameter("ti_typedrp"+i);
      ID = Integer.parseInt(modinfo.getParameter("ti_idinput"+i));

      try{
        if(ID != -1){
          ti = new TariffIndex(ID);
          if(sDel != null && sDel.equalsIgnoreCase("true")){
            ti.delete();
          }
        }
        else if(!"".equalsIgnoreCase(sName) && !"".equals(sIndex)){
          float findex = Float.parseFloat(sIndex);
          ti = new TariffIndex();
          ti.setName(sName);
          ti.setInfo(sInfo);
          ti.setIndex(findex);
          ti.setDate(idegaTimestamp.RightNow().getSQLDate());
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

   doMain(modinfo);
  }

  public Text formatText(float f){
    return formatText(String.valueOf(f));

  }

  private DropdownMenu typeDrop(String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    for (int i = 0; i < Types.length; i++) {
      drp.addMenuElement(Types[i]);
    }
    drp.setSelectedElement(selected);
    return drp;
  }

  private List getIndices(){
    Vector V = new Vector();
    for (int i = 0; i < Types.length; i++) {
      TariffIndex ti= CampusAccountFinder.getTariffIndex(Types[i]);
      if(ti!= null)
        V.add(ti);
    }
    return V;


  }
}// class TariffKeyEditor