package com.idega.block.reports.presentation;



import java.sql.SQLException;

import com.idega.block.reports.data.ReportCategory;
import com.idega.block.reports.data.ReportCategoryAttribute;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;



public class ReportCategoryAttributeMaker extends Block{



  private boolean isAdmin;

  private final int ACT0 = 0,ACT1=1,ACT2=2,ACT3=3,ACT4=4;

  private String prefix = "rcam_";

  private String sAction = prefix+"action";

  private String sActPrm = "0";

  private int iAction = 0;

  private String sName,sInfo,sAttId;

  private String sIndex,sId;



  public ReportCategoryAttributeMaker(){

    sIndex = "0";

    sId = "0";

    sName = "";

    sInfo = "";

    sAttId = "0";

  }



  private void control(IWContext iwc){



    try{

        doSome(iwc);



        if(iwc.getParameter(sAction) != null){

          sActPrm = iwc.getParameter(sAction);

          try{

            iAction = Integer.parseInt(sActPrm);

            switch(iAction){

              case ACT0: doSome(iwc); break;

              case ACT1: doAct1(iwc); break;

              case ACT2: doAct2(iwc); break;

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

    doMain(iwc);

  }



  private void doSome(IWContext iwc){

    int id = 0;

    String sIndex = iwc.getParameter(prefix+"drp");

    if(sIndex != null){

      id = Integer.parseInt(sIndex);

      if(id != 0){

        try {

          ReportCategoryAttribute RC = ((com.idega.block.reports.data.ReportCategoryAttributeHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategoryAttribute.class)).findByPrimaryKeyLegacy(id);

          sName = RC.getName();

          sAttId = String.valueOf(RC.getAttributeId());

          sId = String.valueOf(RC.getReportCategoryId());

        }

        catch (Exception ex) {

        }

      }

      else{

        String sId = iwc.getParameter(prefix+"drp2");

      }

    }

  }



  private void doMain(IWContext iwc){

    String sIndex = iwc.getParameter(prefix+"drp");

    String sId = iwc.getParameter(prefix+"drp2");

    Table T = new Table();

    Form myForm = new Form();

    if(sIndex == null)

      sIndex = "0";

     if(sId == null)// || sIndex == "0")

      sId = "0";

    DropdownMenu drp2 = this.drpCategories(prefix+"drp2",sId);

    DropdownMenu drp = this.drpAttributes(prefix+"drp",sIndex);

    drp.setToSubmit();

    TextInput tiName = new TextInput(prefix+"name",sName);

    TextInput tiId = new TextInput(prefix+"attid",sAttId);

    SubmitButton submit= new SubmitButton("Save",this.sAction,String.valueOf(this.ACT1));

    SubmitButton delete= new SubmitButton("Del",this.sAction,String.valueOf(this.ACT2));

    Table T2 = new Table();



    T2.add("Attribute",1,1);

    T2.add(drp,1,2);

    T2.add("Category",2,1);

    T2.add(drp2,2,2);

    T2.add("Name:",3,1);

    T2.add(tiName,3,2);

    T2.add("ID:",4,1);

    T2.add(tiId,4,2);

    T2.add(submit,5,2);

    T2.add(delete,6,2);

    myForm.add(T2);

    T.add(myForm);

    add(T);

  }



  private void doAct1(IWContext iwc){

    int id = -1;

    String sIndex = iwc.getParameter(prefix+"drp");

    if(sIndex != null)

       id = Integer.parseInt(sIndex);

    sId = iwc.getParameter(prefix+"drp2");

    sName = iwc.getParameter(prefix+"name");

    sAttId = iwc.getParameter(prefix+"attid");

    add(" cat: "+ sId );

    add(" att: "+ sName );

    add(" attid: "+ sAttId );





    if(id == 0)

      this.saveAttribute(sName,sAttId,sId);

    else

      this.updateAttribute(id,sName,sAttId,sId);



  }



  private void doAct2(IWContext iwc){

    int id = 0;

    String sIndex = iwc.getParameter(prefix+"drp");

    if(sIndex != null)

       id = Integer.parseInt(sIndex);

    if(id != 0)

      this.deleteAttribute(id);

    sName = "";

    sInfo = "";

  }



  private boolean saveAttribute(String sAttName,String sAttID,String cid){

    try {

      int tid = Integer.parseInt(cid);

      int attid = Integer.parseInt(sAttID);

      if(tid > 0){

        ReportCategoryAttribute rc = ((com.idega.block.reports.data.ReportCategoryAttributeHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategoryAttribute.class)).createLegacy();

        rc.setName(sAttName);

        rc.setReportCategoryId(tid);

        rc.setAttributeId(attid);

        rc.insert();

        return true;

      }

      else

        return false;

    }

    catch (Exception ex) {ex.printStackTrace();

      return false;

    }

  }

  private boolean updateAttribute(int id,String sAttName,String sAttID,String cid){

     try {

      int tid = Integer.parseInt(cid);

      int attid = Integer.parseInt(sAttID);

      if(id != -1 && tid > 0){

      ReportCategoryAttribute rc = ((com.idega.block.reports.data.ReportCategoryAttributeHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategoryAttribute.class)).findByPrimaryKeyLegacy(id);

      rc.setName(sAttName);

      rc.setReportCategoryId(tid);

      rc.setAttributeId(attid);

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

  private boolean deleteAttribute(int id){

    try {

      ReportCategoryAttribute rc = ((com.idega.block.reports.data.ReportCategoryAttributeHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategoryAttribute.class)).findByPrimaryKeyLegacy(id);

      rc.delete();

      return true;

    }

    catch (Exception ex) {

      return false;

    }

  }



  private DropdownMenu drpAttributes(String sPrm,String selected) {

    ReportCategoryAttribute[] cat = new ReportCategoryAttribute[0];

    try{

      cat = (ReportCategoryAttribute[]) (((com.idega.block.reports.data.ReportCategoryAttributeHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategoryAttribute.class)).createLegacy()).findAll();

    }

    catch(SQLException sql){sql.printStackTrace();}

    DropdownMenu drp = new DropdownMenu(sPrm);

    drp.addMenuElement("0","Attribute");

    for (int i = 0; i < cat.length; i++) {

      drp.addMenuElement(cat[i].getID(),cat[i].getName());

    }

    if(!selected.equalsIgnoreCase(""))

      drp.setSelectedElement(selected);

    return drp;

  }





  private DropdownMenu drpCategories(String sPrm,String selected) {

    ReportCategory[] cat = new ReportCategory[0];

    try{

      cat = (ReportCategory[]) (((com.idega.block.reports.data.ReportCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategory.class)).createLegacy()).findAll();

    }

    catch(SQLException sql){}

    DropdownMenu drp = new DropdownMenu(sPrm);

    drp.addMenuElement("0","Category");

    for (int i = 0; i < cat.length; i++) {

      drp.addMenuElement(cat[i].getID(),cat[i].getName());

    }

    if(!selected.equalsIgnoreCase(""))

      drp.setSelectedElement(selected);

    return drp;

  }



  public void main(IWContext iwc) {

    /* try{

      isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(iwc);

    }

    catch(SQLException e){

      isAdmin = false;

    }

    */

    isAdmin = true;

    control(iwc);

  }



}
