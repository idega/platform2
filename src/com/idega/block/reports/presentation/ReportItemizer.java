package com.idega.block.reports.presentation;

import com.idega.block.reports.data.*;
import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import java.sql.SQLException;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.Script;


public class ReportItemizer extends JModuleObject{

  private boolean isAdmin;
  private final int ACT0 = 0,ACT1=1,ACT2=2,ACT3=3,ACT4=4;
  protected String MiddleColor,LightColor,DarkColor,WhiteColor,TextFontColor,HeaderFontColor,IndexFontColor;
  private String sAction = "report_action";
  private String sActPrm = "0";
  private int iAction = 0;
  private String sName,sInfo;
  private String sIndex;
  private int fontSize = 2;
  protected boolean fontBold = false;
  protected String styleAttribute = "font-size: 8pt";

  public ReportItemizer(){
    sIndex = "0";
    sName = "";
    sInfo = "";
  }

  private void control(ModuleInfo modinfo){

    try{
        doSome(modinfo);

        if(modinfo.getParameter(sAction) != null){
          sActPrm = modinfo.getParameter(sAction);
          try{
            iAction = Integer.parseInt(sActPrm);
            switch(iAction){
              case ACT0: doSome(modinfo); break;
              case ACT1: doAct1(modinfo); break;
              case ACT2: doAct2(modinfo); break;
              case ACT3: break;
              case ACT4: break;
            }
          }
          catch(Exception e){

          }
        }
    }
    catch(Exception S){
      S.printStackTrace();
    }
    doMain(modinfo);
  }

  private void doSome(ModuleInfo modinfo){
    int id = 0;
    String sIndex = modinfo.getParameter("rep_cat_drp");
    if(sIndex != null){
      id = Integer.parseInt(sIndex);
      if(id != 0){
        try {
          ReportCategory RC = new ReportCategory(id);
          sName = RC.getName();
          sInfo = RC.getInfo();
        }
        catch (Exception ex) {
        }
      }
    }
  }

  private void doMain(ModuleInfo modinfo){
    String sIndex = modinfo.getParameter("rep_cat_drp");
    Table T = new Table();
    Form myForm = new Form();
    DropdownMenu drp = this.drpCategories("rep_cat_drp",sIndex);
    drp.setToSubmit();
    TextInput tiName = new TextInput("rep_cat_name",sName);
    TextInput tiInfo = new TextInput("rep_cat_info",sInfo);
    SubmitButton submit= new SubmitButton("Save",this.sAction,String.valueOf(this.ACT1));
    SubmitButton delete= new SubmitButton("Del",this.sAction,String.valueOf(this.ACT2));
    Table T2 = new Table();

    T2.add("Flokkur",1,1);
    T2.add(drp,1,2);
    T2.add("Name:",2,1);
    T2.add(tiName,2,2);
    T2.add("Info:",3,1);
    T2.add(tiInfo,3,2);
    T2.add(submit,4,2);
    T2.add(delete,5,2);
    myForm.add(T2);
    T.add(myForm);
    add(T);
  }
  protected void doChange(ModuleInfo modinfo) throws SQLException{
    Form myForm = new Form();
    //myForm.maintainAllParameters();
    String sCatId = modinfo.getParameter("category_id");
    int iCatId = 0;
    if(sCatId != null)
      iCatId = Integer.parseInt(sCatId);

    ReportItem[] RI;
    try{
    RI = (ReportItem[])new ReportItem().findAll();
    }
    catch(Exception e){RI = new ReportItem[0];}
    int count = RI.length;
    int inputcount = count+5;
    Table inputTable =  new Table(4,inputcount+1);
    inputTable.setWidth("100%");
    inputTable.setCellpadding(2);
    inputTable.setCellspacing(1);
    inputTable.setColumnAlignment(1,"right");
    inputTable.setHorizontalZebraColored(LightColor,WhiteColor);
    inputTable.setRowColor(1,MiddleColor);
    int a = 1;
    inputTable.add(formatText("Name"),a++,1);
    inputTable.add(formatText("Field"),a++,1);
    inputTable.add(formatText("Maintable"),a++,1);
    inputTable.add(formatText("Joins"),a++,1);
    inputTable.add(formatText("Join Tables"),a++,1);
    inputTable.add(formatText("Condition Data"),a++,1);
    inputTable.add(formatText("Condition Operator"),a++,1);
    inputTable.add(formatText("Information"),a++,1);

    for (int i = 1; i <= inputcount ;i++){
      String rownum = String.valueOf(i);
      String s = "";
      TextInput nameInput, infoInput;
      HiddenInput idInput;
      CheckBox delCheck;
      int pos;
      if(i <= count ){
        pos = i-1;
        nameInput  = new TextInput("tke_nameinput"+i,(RI[pos].getName()));
        infoInput = new TextInput("tke_infoinput"+i,(RI[pos].getInfo()));
        idInput = new HiddenInput("tke_idinput"+i,String.valueOf(RI[pos].getID()));
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
    myForm.add(new HiddenInput(this.sAction,String.valueOf(this.ACT3 )));
    myForm.add(inputTable);
    myForm.add(new SubmitButton("Vista"));

    add(myForm);
  }

  private ReportCategory[] findCategorys(int iCatId){
    try {
      if(iCatId > 0){
        return (ReportCategory[]) new ReportCategory().findAllByColumn("category",iCatId);
      }
      else{
        return new ReportCategory[0];
      }
    }
    catch (SQLException ex) {
      return new ReportCategory[0];
    }
  }


  private void doAct1(ModuleInfo modinfo){
    int id = -1;
    String sIndex = modinfo.getParameter("rep_cat_drp");
    if(sIndex != null)
       id = Integer.parseInt(sIndex);
    sName = modinfo.getParameter("rep_cat_name");
    sInfo = modinfo.getParameter("rep_cat_info");
    if(id == 0)
      this.saveCategory(sName,sInfo);
    else
      this.updateCategory(id,sName,sInfo);
  }

  private void doAct2(ModuleInfo modinfo){
    int id = 0;
    String sIndex = modinfo.getParameter("rep_cat_drp");
    if(sIndex != null)
       id = Integer.parseInt(sIndex);
    if(id != 0)
      this.deleteCategory(id);
    sName = "";
    sInfo = "";
  }

  private boolean saveCategory(String name,String info){
    try {
      ReportCategory rc = new ReportCategory();
      rc.setName(name);
      rc.setInfo(info);
      rc.insert();
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }
  private boolean updateCategory(int id,String name,String info){
     try {
      if(id != -1){
      ReportCategory rc = new ReportCategory(id);
      rc.setName(name);
      rc.setInfo(info);
      rc.update();
      return true;
      }
      else
        return false;
    }
    catch (Exception ex) {
      return false;
    }
  }
  private boolean deleteCategory(int id){
    try {
      ReportCategory rc = new ReportCategory(id);
      rc.delete();
      return true;
    }
    catch (Exception ex) {
      return false;
    }
  }

  private DropdownMenu drpCategories(String sPrm,String selected) {
    ReportCategory[] cat = new ReportCategory[0];
    try{
      cat = (ReportCategory[]) (new ReportCategory()).findAll();
    }
    catch(SQLException sql){}
    DropdownMenu drp = new DropdownMenu(sPrm);
    drp.addMenuElement("0","Flokkar");
    for (int i = 0; i < cat.length; i++) {
      drp.addMenuElement(cat[i].getID(),cat[i].getName());
    }
    if(!selected.equalsIgnoreCase(""))
      drp.setSelectedElement(selected);
    return drp;
  }

  public Text formatText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      if(this.fontBold)
      T.setBold();
      T.setFontSize(this.fontSize);
    }
    return T;
  }
  public Text formatText(int i){
    return formatText(String.valueOf(i));
  }
  protected void setStyle(InterfaceObject O){
    O.setAttribute("style",this.styleAttribute);
  }

  public void main(ModuleInfo modinfo) {
    /* try{
      isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException e){
      isAdmin = false;
    }
    */
    isAdmin = true;
    control(modinfo);
  }

}
