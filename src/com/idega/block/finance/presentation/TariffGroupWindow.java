package com.idega.block.finance.presentation;


import com.idega.block.finance.business.FinanceBusiness;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.data.TariffGroup;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class TariffGroupWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.finance";
private boolean isAdmin=false;
private int iUserId = -1;
private User eUser = null;
private int iObjInsId = -1;
private int SAVECATEGORY = 1,SAVECONTENT = 2;

private static final String prefix ="tgrp_";
public static final String prmCategory = prefix+"cat";
public static final String prmGroup = prefix+"group";
private static final String actSave = prefix+"save";

private IWBundle iwb;
private IWBundle core;
private IWResourceBundle iwrb;

  public TariffGroupWindow(){
    setWidth(500);
    setHeight(500);
    setResizable(true);
    setUnMerged();
  }

  private void control(IWContext iwc)throws Exception{
    debugParameters(iwc);
    int iCategoryId = Finance.parseCategoryId(iwc);
    if(iCategoryId > 0){
      int groupId = -1;
      if(iwc.isParameterSet(prmGroup))
        groupId = Integer.parseInt(iwc.getParameter(prmGroup));
      if(iwc.isParameterSet(actSave) || iwc.isParameterSet(actSave+".x")){
        groupId = processCategoryForm(iwc,iCategoryId,groupId);
      }
      addCategoryFields(FinanceFinder.getInstance().getTariffGroup(groupId),iCategoryId);
    }
    else
      add("no category ");
  }


  private int processCategoryForm(IWContext iwc,int iCategoryId,int iGroupId){
    String sName = iwc.getParameter("cat_name");
    String sInfo = iwc.getParameter("cat_info");
    boolean UseIndex = iwc.isParameterSet("use_index");
    int handlerid = Integer.parseInt(iwc.getParameter("fhandler"));
    return FinanceBusiness.saveTariffGroup(iGroupId,sName,sInfo,handlerid,UseIndex,iCategoryId);
  }

  private void addCategoryFields(TariffGroup group,int iCategoryId){

    String sGroup= iwrb.getLocalizedString("tariffgroup","Tariffgroup");
    String sName = iwrb.getLocalizedString("name","Name");
    String sDesc = iwrb.getLocalizedString("description","Description");
    String sHandlers = iwrb.getLocalizedString("handlers","Handlers");
    String sIndex = iwrb.getLocalizedString("useindices","Use indices");
    boolean hasCategory = group !=null ? true:false;

    Link newLink = new Link(core.getImage("/shared/create.gif"));
    newLink.addParameter(prmCategory,-1);

    List L = FinanceFinder.getInstance().listOfTariffGroups(iCategoryId);
    DropdownMenu groups = new DropdownMenu(L,prmGroup);
    groups.addMenuElementFirst("-1",sGroup);
    groups.setToSubmit();

    List L2 = FinanceFinder.getInstance().listOfFinanceHandlers();
    DropdownMenu handlers = new DropdownMenu(L2,"fhandler");
    handlers.addMenuElementFirst("-1",sHandlers);

    TextInput tiName = new TextInput("cat_name");
    tiName.setLength(40);
    tiName.setMaxlength(255);

    TextArea taDesc = new TextArea("cat_info",65,5);

    CheckBox useIndexes = new CheckBox("use_index","true");

    Table catTable = new Table(5,1);
    catTable.setCellpadding(0);
    catTable.setCellspacing(0);
    setStyle(groups);
    catTable.add(groups,1,1);
    catTable.add(newLink,3,1);
    catTable.setWidth(2,1,"20");
    catTable.setWidth(4,1,"20");

    addLeft(sGroup,catTable,true,false);
    addLeft(sName,tiName,true);
    addLeft(sDesc,taDesc,true);
    setStyle(handlers);
    addLeft(sIndex,useIndexes,true);
    addLeft(sDesc,handlers,true);
    addLeft(Finance.getCategoryParameter(iCategoryId));
    if(hasCategory){
      int id = group.getID();
      if(group.getName()!=null)
        tiName.setContent(group.getName());
      if(group.getInfo()!=null)
        taDesc.setContent(group.getInfo());
      groups.setSelectedElement(String.valueOf(id));
      useIndexes.setChecked(group.getUseIndex());
      handlers.setSelectedElement(String.valueOf(group.getHandlerId()));
    }
    SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),actSave);
    addSubmitButton(save);

  }

  private void noAccess() throws IOException,SQLException {
    addLeft(iwrb.getLocalizedString("no_access","Login first!"));
    this.addSubmitButton(new CloseButton(iwrb.getLocalizedString("close","Close")));
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
    addTitle(iwrb.getLocalizedString("tariff_group_editor","Tariffgroup Editor"));
    control(iwc);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}
