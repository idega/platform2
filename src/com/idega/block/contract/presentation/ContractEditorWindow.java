package com.idega.block.contract.presentation;


import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.contract.business.ContractBusiness;
import com.idega.block.contract.business.ContractFinder;
import com.idega.block.contract.business.ContractService;
import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractCategory;
import com.idega.block.contract.data.ContractTag;
import com.idega.block.media.servlet.MediaServlet;
import com.idega.core.file.data.ICFile;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ContractEditorWindow extends IWAdminWindow{

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.contract";
private boolean isAdmin=false;
private int iUserId = -1;
private User eUser = null;
private int iObjInsId = -1;
private int defaultPublishDays = 50;
private int SAVECATEGORY = 1,SAVECONTENT = 2;

private static String prmPrefix = "cew_";
public static String prmCategory = prmPrefix+"category";
public static String prmObjInstId = prmPrefix+"icobjinstid";
public static String prmAttribute = prmPrefix+"attribute";
private static String prmUseImage = "insertImage";//nwep.useimage
public  static String prmDelete = prmPrefix+"txdeleteid";
private static String prmImageId = prmPrefix+"imageid";
public static String prmContractId = prmPrefix+"contractid";
private static String actDelete = prmPrefix+"delete";
private static String actSave = prmPrefix+"save";
private static String modeDelete = "nwem_delete";
private static String prmFormProcess = "nwe_formprocess";
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

  public ContractEditorWindow(){
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
		int saveInfo = getSaveInfo(iwc);

    if ( this.isAdmin ) {

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
        sContractId = iwc.getParameter(prmDelete);
        confirmDelete(sContractId,this.iObjInsId);
        doView = false;
      }
      // Object Instance Request :
      else if(iwc.isParameterSet(prmObjInstId)){
        this.iObjInsId = Integer.parseInt(iwc.getParameter(prmObjInstId ) );
        doView = false;
        if(this.iObjInsId > 0 && saveInfo != this.SAVECATEGORY) {
			iCategoryId = ContractFinder.getObjectInstanceCategoryId(this.iObjInsId );
		}
      }

			if(iwc.isParameterSet("deletefile")){
			  int iFile = Integer.parseInt(iwc.getParameter("deletefile"));
				deleteFile(iwc,iFile,iContractId);
			}
			//add("category id "+iCategoryId);
			//add(" instance id "+iObjInsId);
      // end of News initialization

      // Form processing
      if(saveInfo == this.SAVECONTENT) {
		processForm(iwc,iCategoryId,iContractId);
	}
	else if(saveInfo == this.SAVECATEGORY) {
		processCategoryForm(iwc,sCategoryId,this.iObjInsId);
	}

			if(this.iObjInsId > 0){
			  addCategoryFields(ContractFinder.getContractCategory(iCategoryId),this.iObjInsId  );
			}
      //doView = false;

      if(doView) {
		doViewContract(iContractId,iCategoryId );
	}
    }
    else {
      noAccess();
    }
  }

	private void deleteFile(IWContext iwc,int iFileID,int iContractID) throws RemoteException{
		getContractService(iwc).removeContractFile(iFileID,iContractID);
	}

	private int getSaveInfo(IWContext iwc){
	  if(iwc.getParameter(prmFormProcess)!=null){
      if(iwc.getParameter(prmFormProcess).equals("Y")) {
		return this.SAVECONTENT;
	}
	else if(iwc.getParameter(prmFormProcess).equals("C")) {
		return this.SAVECATEGORY;
        //doView = false;
	}
    }
		return 0;
	}

  // Form Processing :
  private void processForm(IWContext iwc,int iCategory,int iContractId){
    // Save :
    if(iwc.isParameterSet(actSave)|| iwc.isParameterSet(actSave+".x") ){
      saveContract(iwc,iCategory,iContractId);
    }
    // Delete :
    else if(iwc.isParameterSet( actDelete ) || iwc.isParameterSet(actDelete+".x")){
      try {
        if(iwc.getParameter(modeDelete)!=null){
          int I = Integer.parseInt(iwc.getParameter(modeDelete));
          deleteContract(I);
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
					int id = ContractBusiness.saveCategory(iCatId,iObjInsId,sName,sDesc);
				  if(iwc.isParameterSet("contags")) {
					updateTags(iwc,id);
				}
				}
			}
			// deleting :
			else if(iwc.isParameterSet(actDelete) || iwc.isParameterSet(actDelete+".x") ){
				ContractBusiness.deleteCategory(iCatId);
			}
  }

	private Map mapOfTagsInResponse(IWContext iwc,int iCategoryId){
	  List L = ContractFinder.listOfContractTagsInUse(iCategoryId);
		if(L!=null){
			Iterator I = L.iterator();
			Hashtable H = new Hashtable();
			String prm;
			while(I.hasNext()){
				ContractTag tag = (ContractTag) I.next();
				prm = prmTag + tag.getID();
				if(iwc.isParameterSet(prm)){
				  H.put(String.valueOf(tag.getID()),iwc.getParameter(prm));
				}
			}
			return H;
		}
		return null;
	}

	protected PresentationObject getTagChange(ContractCategory category){
    Table T = new Table();
		int count = 0;
		List contractTags = null;
		if(category != null){
		  contractTags = ContractFinder.listOfContractTags(((Integer)category.getPrimaryKey()).intValue());
			if(contractTags !=null) {
				count = contractTags.size();
			}
		}

		int inputcount = count+5;
		Table inputTable =  new Table(6,inputcount+1);
		inputTable.setWidth("100%");
		inputTable.setCellpadding(2);
		inputTable.setCellspacing(1);
	 // inputTable.setColumnAlignment(1,"right");
		//inputTable.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
		inputTable.setRowColor(1,Edit.colorMiddle);
		inputTable.add(Edit.formatText("Nr"),1,1);
		inputTable.add(Edit.formatText(this.iwrb.getLocalizedString("name","Name")),2,1);
		inputTable.add(Edit.formatText(this.iwrb.getLocalizedString("info","Info")),3,1);
		inputTable.add(Edit.formatText(this.iwrb.getLocalizedString("use","use")),4,1);
		inputTable.add(Edit.formatText(this.iwrb.getLocalizedString("list","list")),5,1);
		inputTable.add(Edit.formatText(this.iwrb.getLocalizedString("delete","Delete")),6,1);

		ContractTag tag;
		TextInput nameInput, infoInput;
		HiddenInput idInput;
		CheckBox delCheck,useCheck,listCheck;
		for (int i = 1; i <= inputcount ;i++){

			String rownum = String.valueOf(i);

			int pos;
			nameInput  = new TextInput("contag_nameinput"+i);
			infoInput = new TextInput("contag_infoinput"+i);
			useCheck = new CheckBox("contag_usecheck"+i,"true");
			listCheck = new CheckBox("contag_listcheck"+i,"true");
			if(i <= count ){
				pos = i-1;
				tag = (ContractTag)contractTags.get(pos);

				nameInput.setContent(tag.getName());
				infoInput.setContent(tag.getInfo());
				useCheck.setChecked(tag.getInUse());
				listCheck.setChecked(tag.getInList());
				idInput = new HiddenInput("contag_idinput"+i,String.valueOf(tag.getID()));
				delCheck = new CheckBox("contag_delcheck"+i,"true");
				Edit.setStyle(delCheck);
				inputTable.add(delCheck,6,i+1);
			}
			else{
				idInput = new HiddenInput("contag_idinput"+i,"-1");
			}
			nameInput.setSize(20);
			infoInput.setSize(40);

			Edit.setStyle(nameInput);
			Edit.setStyle(infoInput);
			Edit.setStyle(useCheck);
			Edit.setStyle(listCheck);

			inputTable.add(Edit.formatText(rownum),1,i+1);
			inputTable.add(nameInput,2,i+1);
			inputTable.add(infoInput,3,i+1);
			inputTable.add(useCheck,4,i+1);
			inputTable.add(listCheck,5,i+1);
			inputTable.add(idInput);
		}
		T.add(new HiddenInput("contag_count", String.valueOf(inputcount) ));
		T.add(new HiddenInput("contags","save"));
		T.add(inputTable);
		//SubmitButton save = new SubmitButton(iwrb.getLocalizedString("save","Save"));
		//Edit.setStyle(save);
		//T.add(save);

    return (T);
  }

	private void updateTags(IWContext iwc,int iCategoryId){
    int count = Integer.parseInt(iwc.getParameter("contag_count"));
    String sName,sInfo;
		boolean bDel,bUse,bList;
    int ID;

    for (int i = 1; i < count+1 ;i++){
      sName = iwc.getParameter("contag_nameinput"+i );
      sInfo = iwc.getParameter("contag_infoinput"+i);
      bDel = iwc.isParameterSet("contag_delcheck"+i);
			bUse = iwc.isParameterSet("contag_usecheck"+i);
			bList = iwc.isParameterSet("contag_listcheck"+i);
      ID = Integer.parseInt(iwc.getParameter("contag_idinput"+i));
			if(bDel) {
				ContractBusiness.deleteTag(ID);
			}
			else if(!"".equals(sName)) {
				ContractBusiness.saveTag(ID,sName,sInfo,bUse,bList,iCategoryId);
			}

    }// for loop

  }

  private void doViewContract(int iContractId,int iCategoryId){
    Contract contract = null;
    if(iContractId > 0){
      contract = ContractFinder.getContract(iContractId);
			iCategoryId = contract.getCategoryId().intValue();
    }

    addContractFields(contract,this.iObjInsId,iCategoryId);

  }

  private void saveContract(IWContext iwc,int iCategoryId,int iContractId){
		String sValFrom = iwc.getParameter(prmValFrom);
    String sValTo = iwc.getParameter(prmValTo);
		String sStatus = iwc.getParameter(prmStatus);
		if(sValFrom !=null && sValTo !=null && !sValFrom.equals(sValTo)){
			if(sStatus == null){
				Map M = mapOfTagsInResponse(iwc,iCategoryId);
				sStatus = com.idega.block.contract.data.ContractBMPBean.statusCreated;
				IWTimestamp today = IWTimestamp.RightNow();
				IWTimestamp ValFrom = sValFrom!=null ? new IWTimestamp(sValFrom):today;
				IWTimestamp ValTo = sValTo!=null ?new IWTimestamp(sValTo):today;
				ContractBusiness.saveContract(iCategoryId ,ValFrom,ValTo,sStatus,M);
			}
			else{
				ContractBusiness.saveContractStatus(iContractId,sStatus);
			}
		}

    setParentToReload();
    close();
  }

  private void deleteContract(int iContractId ) {

    setParentToReload();
    close();
  }


  private void addCategoryFields(ContractCategory eCategory,int iObjInst){

	  String sCategory= this.iwrb.getLocalizedString("category","Category");
    String sName = this.iwrb.getLocalizedString("name","Name");
    String sDesc = this.iwrb.getLocalizedString("description","Description");
		String sFields = this.iwrb.getLocalizedString("fields","Fields");
		boolean hasCategory = eCategory !=null ? true:false;

		Link newLink = new Link(this.core.getImage("/shared/create.gif"));
		newLink.addParameter(prmCategory,-1);
		newLink.addParameter(prmObjInstId,iObjInst);
		newLink.addParameter(prmFormProcess,"C");

		Collection categories = ContractFinder.listOfContractCategories();
		DropdownMenu catDrop = new DropdownMenu(categories,prmCategory);
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
			int id = ((Integer)eCategory.getPrimaryKey()).intValue();
			int iContractCount = ContractFinder.countContractsInCategory(id);
			if(eCategory.getName()!=null) {
				tiName.setContent(eCategory.getName());
			}
			if(eCategory.getDescription()!=null) {
				taDesc.setContent(eCategory.getDescription());
			}
			addLeft(sFields,getTagChange(eCategory),true,false);
		  catDrop.setSelectedElement(String.valueOf(id));

			if(iContractCount == 0){
			Link deleteLink = new Link(this.core.getImage("/shared/delete.gif"));
			deleteLink.addParameter(actDelete,"true");
			deleteLink.addParameter(prmCategory,id);
			deleteLink.addParameter(prmObjInstId,iObjInst);
			deleteLink.addParameter(prmFormProcess,"C");
			catTable.add(deleteLink,5,1);
			}
		}
		SubmitButton save = new SubmitButton(this.iwrb.getImage("save.gif"),actSave);
    addSubmitButton(save);
    addHiddenInput( new HiddenInput (prmObjInstId,String.valueOf(iObjInst)));
    addHiddenInput( new HiddenInput (prmFormProcess,"C"));

  }

  private void addContractFields(Contract eContract ,int iObjInsId,int iCategoryId){
    boolean hasContract = eContract != null;

		String sValidFrom = this.iwrb.getLocalizedString("validfrom","Valid from");
    String sValidTo = this.iwrb.getLocalizedString("validto","Valid to");
    String sStatus = this.iwrb.getLocalizedString("status","Status");
		String sFields = this.iwrb.getLocalizedString("fields","Fields");

		IWTimestamp now = IWTimestamp.RightNow();
    DateInput ValidFrom = new DateInput(prmValFrom,true);
		setStyle(ValidFrom);
    DateInput ValidTo = new DateInput(prmValTo,true);
		setStyle(ValidTo);
    DropdownMenu status = statusDrop(prmStatus,"");

    // Fill or not Fill
    // if contract exists
    if( hasContract ){
		  ValidFrom.setDate(eContract.getValidFrom());
			ValidTo.setDate(eContract.getValidTo());
			status.setSelectedElement(eContract.getStatus());
      addHiddenInput(new HiddenInput(prmContractId,eContract.getPrimaryKey().toString()));
      addHiddenInput(new HiddenInput(prmCategory ,String.valueOf(eContract.getCategoryId())));
    }
		// if new contract
    else{
		  ValidFrom.setDate(now.getSQLDate());
			ValidTo.setDate(now.getSQLDate());
      addHiddenInput(new HiddenInput(prmCategory ,String.valueOf(iCategoryId)));
    }

		Table T = new Table();
		List L = ContractFinder.listOfContractTagsInUse(iCategoryId);
		if(L!=null){
			Iterator I = L.iterator();
			ContractTag tag;
			int row = 1;
			while(I.hasNext()){
			  tag = (ContractTag) I.next();
				T.add(formatText(tag.getName()),1,row);
				if(!hasContract){
					TextInput input = new TextInput(prmTag+tag.getID());
					setStyle(input);
				  T.add(input,2,row);
				}
				else{
					String value = (String) eContract.getMetaData(String.valueOf(tag.getID()));
					if(value !=null) {
						T.add(formatText(value),2,row);
					}
				}


				row++;
			}
		}

		if(hasContract){
		  Collection files = ContractFinder.listOfContractFiles(eContract);
			Table fileTable = new Table();
			int row = 1;
			if(files !=null){
				Iterator I = files.iterator();
				while(I.hasNext()){
				  ICFile file = (ICFile) I.next();
					fileTable.add(formatText(file.getName()),1,row);
					fileTable.add(conLink(file),2,row);
					fileTable.add(delLink(file,iCategoryId,((Integer)eContract.getPrimaryKey()).intValue()),3,row);
					row++;
				}
			}
			Link generator = new Link(this.iwrb.getLocalizedImageButton("generate","Generate"));
			generator.setWindowToOpen(ContractFilerWindow.class);
			generator.addParameter(ContractFilerWindow.prmCategoryId,iCategoryId);
			generator.addParameter(ContractFilerWindow.prmContractId,((Integer)eContract.getPrimaryKey()).intValue());
			fileTable.add(generator,1,row);
			addRight("Files",fileTable,true,false);

		}

		addLeft(sValidFrom,ValidFrom,true);
		addLeft(sValidTo,ValidTo,true);
		if(hasContract) {
			addLeft(sStatus,status,true);
		}
		addLeft(sFields,T,true,false);

		SubmitButton save = new SubmitButton(this.iwrb.getLocalizedImageButton("save","Save"),actSave);
    addSubmitButton(save);
    addHiddenInput( new HiddenInput (prmFormProcess,"Y"));
  }

	private Link conLink(ICFile file){
		Link L = new Link(this.iwb.getImage("pdf.gif"));
		L.setURL("/servlet/MediaServlet");
		//MediaServlet.debug = true;
		L.addParameter(MediaServlet.getParameter(((Integer)file.getPrimaryKey()).intValue()));
		return L;

	}

	private Link delLink(ICFile file,int iCategoryId,int iContractId){
		Link L = new Link(this.core.getImage("/shared/delete.gif"));
		L.addParameter(prmCategory,iCategoryId);
		L.addParameter(prmContractId,iContractId);
		L.addParameter("deletefile",file.getPrimaryKey().toString());
		return L;

	}

	private void deleteCat(int iCatId){

	}

  private void confirmDelete(String sContractId,int iObjInsId ) throws IOException,SQLException {
    int iContractId = Integer.parseInt(sContractId);
    Contract eContract = ContractFinder.getContract(iContractId);

    if ( eContract != null ) {
      addLeft(this.iwrb.getLocalizedString("contract_to_delete","Contract to delete"));
      addLeft(this.iwrb.getLocalizedString("confirm_delete","Are you sure?"));
      addSubmitButton(new SubmitButton(this.iwrb.getImage("delete.gif"),actDelete));
      addHiddenInput(new HiddenInput(modeDelete,eContract.getPrimaryKey().toString()));
      addHiddenInput( new HiddenInput (prmFormProcess,"Y"));
    }
    else {
      addLeft(this.iwrb.getLocalizedString("not_exists","News already deleted or not available."));
      addSubmitButton(new CloseButton(this.iwrb.getImage("close.gif")));
    }
  }

  private void noAccess() throws IOException,SQLException {
    addLeft(this.iwrb.getLocalizedString("no_access","Login first!"));
    this.addSubmitButton(new CloseButton(this.iwrb.getLocalizedString("close","Closee")));
  }

  private DropdownMenu drpCategories(String name,String valueIfEmpty,String displayIfEmpty){
    Collection categories = ContractFinder.listOfContractCategories();
    if(categories != null){
      DropdownMenu drp = new DropdownMenu(categories,name);
      return drp;
    }
    else{
      DropdownMenu drp = new DropdownMenu(name);
      drp.addDisabledMenuElement("","");
      return drp;
    }
  }

	private String getStatus(String status){
    String r = "";
    char c = status.charAt(0);
    switch (c) {
      case 'C': r = this.iwrb.getLocalizedString("created","Created"); break;
      case 'P': r = this.iwrb.getLocalizedString("printed","Printed"); break;
      case 'S': r = this.iwrb.getLocalizedString("signed","Signed");   break;
      case 'R': r = this.iwrb.getLocalizedString("rejected","Rejected");  break;
      case 'T': r = this.iwrb.getLocalizedString("terminated","Terminated");   break;
      case 'E': r = this.iwrb.getLocalizedString("ended","Ended");  break;
    }
    return r;
  }

  private DropdownMenu statusDrop(String name,String selected){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement("C",getStatus("C"));
    drp.addMenuElement("P",getStatus("P"));
    drp.addMenuElement("S",getStatus("S"));
    drp.addMenuElement("R",getStatus("R"));
    drp.addMenuElement("T",getStatus("T"));
    drp.addMenuElement("E",getStatus("E"));
    drp.setSelectedElement(selected);
    return drp;
  }

  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    this.isAdmin = iwc.hasEditPermission(this);
    this.eUser = com.idega.core.accesscontrol.business.LoginBusinessBean.getUser(iwc);
    this.iUserId = this.eUser != null?this.eUser.getID():-1;
    this.isAdmin = true;
    this.iwb = getBundle(iwc);
		this.core = iwc.getIWMainApplication().getBundle(ContractViewer.IW_CORE_BUNDLE_IDENTIFIER);
    this.iwrb = getResourceBundle(iwc);
    addTitle(this.iwrb.getLocalizedString("contract_editor","Contract Editor"));
    control(iwc);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
  
  public ContractService getContractService(IWContext iwc) throws  RemoteException{
  		return (ContractService)IDOLookup.getServiceInstance(iwc,ContractService.class);
  }
}
