package com.idega.block.finance.presentation;


import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.news.business.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.finance.data.*;
import com.idega.block.finance.business.*;
import com.idega.core.user.data.User;

import com.idega.data.*;
import com.idega.util.text.*;
import com.idega.util.text.TextSoap;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.core.data.ICFile;
import com.idega.block.media.servlet.MediaServlet;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class FinanceEditorWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.finance";
private boolean isAdmin=false;
private int iUserId = -1;
private User eUser = null;
private int iObjInsId = -1;
private int SAVECATEGORY = 1,SAVECONTENT = 2;

private static String prmPrefix = "fin_";
public static String prmCategory = prmPrefix+"category";
public static String prmObjInstId = prmPrefix+"icobjinstid";
public static String prmAttribute = prmPrefix+"attribute";
public  static String prmDelete = prmPrefix+"txdeleteid";
private static String prmImageId = prmPrefix+"imageid";
public static String prmContractId = prmPrefix+"contractid";
private static String actDelete = prmPrefix+"delete";
private static String actSave = prmPrefix+"save";
private static String modeDelete = "nwem_delete";
private static String prmFormProcess = prmPrefix+"formprocess";
private static String prmNewCategory = prmPrefix+"newcategory";
private static String prmEditCategory = prmPrefix+"editcategory";
private static String prmCatName= prmPrefix+"categoryname";
private static String prmCatDesc = prmPrefix+"categorydesc";
private static String prmValFrom = prmPrefix+"valfrom";
private static String prmValTo = prmPrefix+"valto";
private static String prmStatus = prmPrefix+"stat";
private static String prmMoveToCat = prmPrefix+"movtocat";
public static final  String imageAttributeKey = "newsimage";
private static String prmTag = prmPrefix+"tag";


private IWBundle iwb;
private IWBundle core;
private IWResourceBundle iwrb;

  public FinanceEditorWindow(){
    setWidth(570);
    setHeight(550);
    setResizable(true);
    setUnMerged();
  }

  private void init(){
    setAllMargins(0);
    //setTitle(sEditor);
  }

  private void control(IWContext iwc)throws Exception{
    init();
    boolean doView = true;

    //  debug:
    /*
    java.util.Enumeration E = iwc.getParameterNames();
    while(E.hasMoreElements()){
            String key = (String) E.nextElement();
      System.err.println(key+" "+iwc.getParameter(key));
    }
    System.err.println();
    */
    String sCategoryId = iwc.getParameter(prmCategory);
    //add("category"+sCategoryId+" ");
    int iCategoryId = sCategoryId !=null?Integer.parseInt(sCategoryId):-1;
    String sAtt = null;
    int saveInfo = getSaveInfo(iwc);

    if ( isAdmin ) {
      String sAction;
      // Text initialization
      String sContractId = null;
      int iContractId = -1;
      // Id Request :
      if(iwc.isParameterSet(prmContractId)){
        sContractId = iwc.getParameter(prmContractId);
				iContractId = Integer.parseInt(sContractId);
      }
      // Delete Request :
      else if(iwc.isParameterSet(prmDelete)){
        /*sContractId = iwc.getParameter(prmDelete);
        confirmDelete(sContractId,iObjInsId);
        doView = false;
        */
      }
      // Object Instance Request :
      else if(iwc.isParameterSet(prmObjInstId)){
        iObjInsId = Integer.parseInt(iwc.getParameter(prmObjInstId ) );
        doView = false;
        if(iObjInsId > 0 && saveInfo != SAVECATEGORY)
          iCategoryId = FinanceFinder.getObjectInstanceCategoryId(iObjInsId );
      }

      // Form processing
      if(saveInfo == SAVECONTENT)
        processForm(iwc,iCategoryId,iContractId);
      else if(saveInfo == SAVECATEGORY)
        processCategoryForm(iwc,sCategoryId,iObjInsId);
        if(iObjInsId > 0){
          addCategoryFields(FinanceFinder.getFinanceCategory(iCategoryId),iObjInsId  );
        }
      //doView = false;
      /*
      if(doView)
        doViewContract(iContractId,iCategoryId );
      */
    }
    else {
      noAccess();
    }
  }

  private int getSaveInfo(IWContext iwc){
    if(iwc.getParameter(prmFormProcess)!=null){
      if(iwc.getParameter(prmFormProcess).equals("Y"))
        return SAVECONTENT;
      else if(iwc.getParameter(prmFormProcess).equals("C"))
        return SAVECATEGORY;
        //doView = false;
    }
    return 0;
  }

  // Form Processing :
  private void processForm(IWContext iwc,int iCategory,int iContractId){
    // Save :
    if(iwc.isParameterSet(actSave)|| iwc.isParameterSet(actSave+".x") ){
     // saveContract(iwc,iCategory,iContractId);
    }
    // Delete :
    else if(iwc.isParameterSet( actDelete ) || iwc.isParameterSet(actDelete+".x")){
      try {
        if(iwc.getParameter(modeDelete)!=null){
          int I = Integer.parseInt(iwc.getParameter(modeDelete));
          //deleteContract(I);
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    // New:
     /** @todo make possible */
   /*else if(iwc.getParameter( actNew ) != null || iwc.getParameter(actNew+".x")!= null){
      sNewsId = null;
    }
    */
    // end of Form Actions
  }

  private void processCategoryForm(IWContext iwc,String sCategoryId,int iObjInsId){
    String sName = iwc.getParameter(prmCatName);
    String sDesc = iwc.getParameter(prmCatDesc);
    int iCatId = sCategoryId != null ? Integer.parseInt(sCategoryId):-1;
    // saving :
    if(iwc.isParameterSet(actSave) || iwc.isParameterSet(actSave+".x") ){
      if(sName!=null){
        int id = FinanceBusiness.saveCategory(iCatId,iObjInsId,sName,sDesc);
      }
    }
    // deleting :
    else if(iwc.isParameterSet(actDelete) || iwc.isParameterSet(actDelete+".x") ){
      FinanceBusiness.deleteCategory(iCatId);
    }
  }

  private void addCategoryFields(FinanceCategory eCategory,int iObjInst){

    String sCategory= iwrb.getLocalizedString("category","Category");
    String sName = iwrb.getLocalizedString("name","Name");
    String sDesc = iwrb.getLocalizedString("description","Description");
    String sFields = iwrb.getLocalizedString("fields","Fields");
    boolean hasCategory = eCategory !=null ? true:false;

    Link newLink = new Link(core.getImage("/shared/create.gif"));
    newLink.addParameter(prmCategory,-1);
    newLink.addParameter(prmObjInstId,iObjInst);
    newLink.addParameter(prmFormProcess,"C");

    List L = FinanceFinder.listOfCategories();
    DropdownMenu catDrop = new DropdownMenu(L,prmCategory);
    catDrop.addMenuElementFirst("-1",sCategory);
    catDrop.setToSubmit();

    TextInput tiName = new TextInput(prmCatName);
    tiName.setLength(40);
    tiName.setMaxlength(255);

    TextArea taDesc = new TextArea(prmCatDesc,65,5);

    Table catTable = new Table(5,1);
    catTable.setCellpadding(0);
    catTable.setCellspacing(0);
    setStyle(catDrop);
    catTable.add(catDrop,1,1);
    catTable.add(newLink,3,1);
    catTable.setWidth(2,1,"20");
    catTable.setWidth(4,1,"20");

    addLeft(sCategory,catTable,true,false);
    addLeft(sName,tiName,true);
    addLeft(sDesc,taDesc,true);

    if(hasCategory){
      int id = eCategory.getID();
      if(eCategory.getName()!=null)
        tiName.setContent(eCategory.getName());
      if(eCategory.getDescription()!=null)
        taDesc.setContent(eCategory.getDescription());
      catDrop.setSelectedElement(String.valueOf(id));

    }
    SubmitButton save = new SubmitButton(iwrb.getImage("save.gif"),actSave);
    addSubmitButton(save);
    addHiddenInput( new HiddenInput (prmObjInstId,String.valueOf(iObjInst)));
    addHiddenInput( new HiddenInput (prmFormProcess,"C"));

  }

  private void noAccess() throws IOException,SQLException {
    addLeft(iwrb.getLocalizedString("no_access","Login first!"));
    this.addSubmitButton(new CloseButton(iwrb.getLocalizedString("close","Closee")));
  }

  private DropdownMenu drpCategories(String name,String valueIfEmpty,String displayIfEmpty){
    List L = FinanceFinder.listOfCategories();
    if(L != null){
      DropdownMenu drp = new DropdownMenu(L,name);
      return drp;
    }
    else{
      DropdownMenu drp = new DropdownMenu(name);
      drp.addDisabledMenuElement("","");
      return drp;
    }
  }

  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    isAdmin = iwc.hasEditPermission(this);
    eUser = com.idega.block.login.business.LoginBusiness.getUser(iwc);
    iUserId = eUser != null?eUser.getID():-1;
    isAdmin = true;
    iwb = getBundle(iwc);
    core = iwc.getApplication().getBundle(iwc.getApplication().CORE_BUNDLE_IDENTIFIER);
    iwrb = getResourceBundle(iwc);
    addTitle(iwrb.getLocalizedString("finance_editor","Finance Editor"));
    control(iwc);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}
