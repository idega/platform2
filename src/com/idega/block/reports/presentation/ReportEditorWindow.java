package com.idega.block.reports.presentation;

import java.util.List;

import com.idega.block.category.data.ICCategory;
import com.idega.block.reports.business.ReportBusiness;
import com.idega.block.reports.business.ReportFinder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ReportEditorWindow extends IWAdminWindow {

  private final String sAction = "rep.edit.action";
  protected final static int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5,ACT6=6,ACT7=7;

  protected boolean isAdmin = false;
  private String prefix = "rep.edit.";
  private String sManual = null;
  private int iCategoryId = -1;
  private int iReportId = -1;
  private boolean useCheckBoxes = true;

	private static String actDelete = "rep_delete";
	private static String actSave = "rep_save";
	private static String actClose = "rep_close";

	public static final  String prmCategoryId = "rep_categoryid";
	public  final static String prmDelete = "rep_deleteid";

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.reports";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb,core;

	private int iObjInsId = -1;
	private int iUserId = -1;


	private static String prmFormProcess = "nwe_formprocess";
  public final static String prmReportId = "rep_report_id";
	public final static String prmNewReport = "rep_newreport";
	public final static String prmObjInstId = "rep_icobjinstid";
	private static String prmCatName= "rep_categoryname";
	private static String prmCatDesc = "rep_categorydesc";
	public final static String prmItems = "rep_items";
  public final static String prmDelim = ";";
	private int SAVECATEGORY = 1,SAVECONTENT = 2;

  public ReportEditorWindow() {
    setWidth(500);
    setHeight(460);
    setResizable(true);
		setUnMerged();
  }

  protected void control(IWContext iwc){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);

		 // debug
		 /*
		System.err.println("ReportEditorWindow parameters:");
		java.util.Enumeration E = iwc.getParameterNames();
		while (E.hasMoreElements()) {
			String name = (String) E.nextElement();
			System.err.println(name+" "+iwc.getParameter(name));
		}
		System.err.println();
		*/

		if(iCategoryId <= 0 && iwc.isParameterSet(prmCategoryId)){
			iCategoryId = Integer.parseInt(iwc.getParameter(prmCategoryId ));
		}

		if(iwc.isParameterSet(prmObjInstId)){
			iObjInsId = Integer.parseInt(iwc.getParameter(prmObjInstId ) );
		}
    if(isAdmin){
			int saveInfo = getSaveInfo(iwc);
      if(iObjInsId > 0 && saveInfo != SAVECATEGORY){
				iCategoryId = ReportFinder.getObjectInstanceCategoryId(iObjInsId );
      }
			// Form processing
			if(saveInfo == SAVECATEGORY){
        processCategoryForm(iwc,iCategoryId,iObjInsId);
      }
      addCategoryFields(ReportFinder.getCategory(iCategoryId),iObjInsId  );
    }
    else{
      add(formatText(iwrb.getLocalizedString("access_denied","Access denied")));
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

    private void processCategoryForm(IWContext iwc,int iCategoryId,int iObjInsId){
    String sName = iwc.getParameter(prmCatName);
    String sDesc = iwc.getParameter(prmCatDesc);
		int iCatId = iCategoryId ;
			// saving :
			if(iwc.isParameterSet(actSave) || iwc.isParameterSet(actSave+".x") ){
				if(sName!=null){
					ReportBusiness.saveCategory(iCatId,iObjInsId,sName,sDesc);
				}
			}
			if(iwc.isParameterSet(actClose) || iwc.isParameterSet(actClose+".x") ){
				setParentToReload();
				close();
			}
			// deleting :
			else if(iwc.isParameterSet(actDelete) || iwc.isParameterSet(actDelete+".x") ){
				ReportBusiness.deleteCategory(iCatId);
			}
  }

	 private void addCategoryFields(ICCategory eCategory,int iObjInst){

	  String sCategory= iwrb.getLocalizedString("category","Category");
    String sName = iwrb.getLocalizedString("name","Name");
    String sDesc = iwrb.getLocalizedString("description","Description");
		//String sFields = iwrb.getLocalizedString("fields","Fields");
		boolean hasCategory = eCategory !=null ? true:false;

		Link newLink = new Link(core.getImage("/shared/create.gif"));
		newLink.addParameter(prmCategoryId,-1);
		newLink.addParameter(prmObjInstId,iObjInst);
		newLink.addParameter(prmFormProcess,"C");

		List L = ReportFinder.listOfCategories();
		DropdownMenu catDrop = new DropdownMenu(L,prmCategoryId);
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
			int iReportCount = ReportFinder.countReportsInCategory(id);
			if(eCategory.getName()!=null)
				tiName.setContent(eCategory.getName());
			if(eCategory.getDescription()!=null)
				taDesc.setContent(eCategory.getDescription());

		  catDrop.setSelectedElement(String.valueOf(id));

			if(iReportCount == 0){
			Link deleteLink = new Link(core.getImage("/shared/delete.gif"));
			deleteLink.addParameter(actDelete,"true");
			deleteLink.addParameter(prmCategoryId,id);
			deleteLink.addParameter(prmObjInstId,iObjInst);
			deleteLink.addParameter(prmFormProcess,"C");
			catTable.add(deleteLink,5,1);
			}
		}
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),actSave);
		SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close","Close"),actClose);
    addSubmitButton(save);
		addSubmitButton(close);
    addHiddenInput( new HiddenInput (prmObjInstId,String.valueOf(iObjInst)));
    addHiddenInput( new HiddenInput (prmFormProcess,"C"));

  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
		core = iwc.getIWMainApplication().getBundle(Reporter.IW_CORE_BUNDLE_IDENTIFIER);
    String title = iwrb.getLocalizedString("report_editor","Report Editor");
    setTitle(title);
    addTitle(title);

    isAdmin = iwc.hasEditPermission(this);

    control(iwc);
    sManual = iwrb.getLocalizedString("manual","");
  }

}
