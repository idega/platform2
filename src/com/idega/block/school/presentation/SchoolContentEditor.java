package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.boxoffice.presentation.Box;
import com.idega.block.documents.presentation.Doc;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolContentBusinessBean;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolType;
import com.idega.block.text.data.LocalizedText;
import com.idega.business.IBOLookup;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.texteditor.TextEditor;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;

/**
 * @author gimmi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SchoolContentEditor extends IWAdminWindow{
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";
  public final static String PARAMETER_SCHOOL_ID = SchoolContentBusinessBean.PARAMETER_SCHOOL_ID;
  
  private String PARAMETER_INFORMATION = "scr_inf";
  private String PARAMETER_LOCALIZED_TEXT_ID = "scr_ltid";
  private String PARAMETER_IMAGE_ID = "scr_img";
  private String PARAMETER_USE_IMAGE = "scr_usimg";
  private String PARAMETER_SCHOOL_NAME = "scr_schn";
  private String PARAMETER_SCHOOL_ADDRESS_STREET = "scr_adst";
  private String PARAMETER_SCHOOL_ADDRESS_POSTAL_CODE = "scr_adpc";
  private String PARAMETER_SCHOOL_ZIP_AREA = "scr_zipa";
  private String PARAMETER_SCHOOL_PHONE = "scr_ph";
  private String PARAMETER_SCHOOL_FAX = "scr_fx";
  private String PARAMETER_SCHOOL_WEBPAGE = "scr_swp";
  private String PARAMETER_SCHOOL_MANAGEMENT_TYPE = "scr_smtid";
  private String PARAMETER_SCHOOL_MAP_URL = "scr_mprl";
  private String PARAMETER_SCHOOL_ACTIVITY = "scr_scac";
  private String PARAMETER_SCHOOL_OPEN_HOURS = "scr_scoa";
  private String PARAMETER_SCHOOL_EMAIL = "scr_email";
  private String PARAMETER_SCHOOL_VISIT_ADDRESS = "scr_vis_addr"; 

  //private String CONTENT_EDITORS_GROUP_PARAMETER_NAME = "school.content_editors_group_id";
  	  
  private String PARAMETER_ACTION = "scr_act";
  private String ACTION_UPDATE = "scr_act_ud";

  private IWResourceBundle _iwrb;
  private School _school;
 private SchoolUserEditor sue;
  private boolean _highSchoolCategory = false;
private boolean _isAdultEducation = false;
	public SchoolContentEditor() {
    setUnMerged();
    // Window changed to 780/580 to make space for the wider editor by Kelly (kelly@lindman.se) 15 may 2003
    setWidth(780);
    setHeight(750);
    setResizable(true);
    setScrollbar(true);
		setTitle( "Edit school information" );	
	}

	public void main( IWContext iwc ) throws RemoteException {
		init(iwc);
		if (this._school != null) {
	try {
			Collection coll = this._school.getSchoolTypes();
			Iterator iterCollection = coll.iterator();	
				
			while (iterCollection.hasNext()) {									
				SchoolType schoolType = (SchoolType) iterCollection.next();
				String schoolCategory =  schoolType.getSchoolCategory();
				if (schoolCategory.equalsIgnoreCase(getSchoolUserBusiness(iwc).getSchoolBusiness().getHighSchoolSchoolCategory())) {
					this._highSchoolCategory = true;
				}
				else if (schoolCategory.equalsIgnoreCase(getSchoolUserBusiness(iwc).getSchoolBusiness().getCategoryAdultEducation().getCategory())) {
					this._isAdultEducation = true;
				}
				
			}
										
		}
				catch (Exception e){
			}

			String action = iwc.getParameter(this.PARAMETER_ACTION);
			if (action == null || action.equals("")) {
				if (this._highSchoolCategory) {
					mainFormHighSchool(iwc);
				} else {
					mainForm(iwc);	
				}
			}else if (action.equals( this.ACTION_UPDATE)) {
				boolean success = updateSchool(iwc);
				if (success) {
					addLeft(this._iwrb.getLocalizedString("school.update_successful", "Update successful"));
				}else {
					addLeft(this._iwrb.getLocalizedString("school.update_failed", "Update failed"));
				}
				if (this._highSchoolCategory) {
					mainFormHighSchool(iwc);
				} else {
					mainForm(iwc);	
				}
			}else {
				add(this._iwrb.getLocalizedString("school.general_error", "General error"));	
			}
			
		}else {
			add(this._iwrb.getLocalizedString("school.no_school_selected", "No school selected"));	
		}
	}

	private void init(IWContext iwc) throws RemoteException {
		this._iwrb = getResourceBundle(iwc);	
		
		String schoolId = iwc.getParameter(PARAMETER_SCHOOL_ID);
		if (schoolId != null) {
			try {
				this._school = getSchoolBusiness(iwc).getSchoolHome().findByPrimaryKey(new Integer(schoolId));	
			} catch (FinderException e) {}
		}
		
		this.sue = getSchoolUserEditor(iwc);
		//GIMMI FIX THIS
		//sue.addParameter(PARAMETER_SCHOOL_ID, schoolId);
		

		Text testText = new Text("test");
		this.formatText(testText, false);
		this.sue.setTextStyleNormal( testText );

		Text testText2 = new Text("test");
		this.formatText(testText2, true);
		this.sue.setTextStyleTitle( testText2 );

		this.sue.setInputStyle(STYLE);
	}

	public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
	}

	protected SchoolUserEditor getSchoolUserEditor(IWContext iwc) throws RemoteException {
		return new SchoolUserEditor(iwc);
	}


	private void mainForm(IWContext iwc) throws RemoteException{

		TextEditor information = new TextEditor(this.PARAMETER_INFORMATION, "");
		// Modified by Kelly (kelly@lindman.se) 14 May 2003 
		// changed width/height on edit window
		// Changed to 3 different management types
		// Activity and Open hours added
		 
		information.setWidth("580");
		information.setHeight("300");

		ImageInserter imageInserter = new ImageInserter(this.PARAMETER_IMAGE_ID);
		imageInserter.setUseBoxParameterName(this.PARAMETER_USE_IMAGE);
		imageInserter.setSelected( true );
		
		TextInput schoolName = new TextInput(this.PARAMETER_SCHOOL_NAME);
		TextInput streetName = new TextInput(this.PARAMETER_SCHOOL_ADDRESS_STREET);
		TextInput areaCode = new TextInput(this.PARAMETER_SCHOOL_ADDRESS_POSTAL_CODE);
		TextInput zipArea = new TextInput(this.PARAMETER_SCHOOL_ZIP_AREA);
		TextInput mapUrl = new TextInput(this.PARAMETER_SCHOOL_MAP_URL);
		TextInput activity = new TextInput(this.PARAMETER_SCHOOL_ACTIVITY);
		TextInput openHours = new TextInput(this.PARAMETER_SCHOOL_OPEN_HOURS);
		
		TextInput visitAddress = new TextInput(this.PARAMETER_SCHOOL_VISIT_ADDRESS);
		
		schoolName.setSize(40);
//		streetName.setSize(40);
		areaCode.setSize(7);
//		zipArea.setSize(20);
		
		SelectorUtility util = new SelectorUtility();
		DropdownMenu manType = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(this.PARAMETER_SCHOOL_MANAGEMENT_TYPE), getSchoolBusiness(iwc).getSchoolManagementTypes(), "getLocalizedKey", this._iwrb);
		
		TextInput phone = new TextInput(this.PARAMETER_SCHOOL_PHONE);
		TextInput fax = new TextInput(this.PARAMETER_SCHOOL_FAX);
		TextInput email = new TextInput(this.PARAMETER_SCHOOL_EMAIL);
		TextInput webPage = new TextInput(this.PARAMETER_SCHOOL_WEBPAGE);
		
		Box box = new Box("Repps");
		box.setBorderColor("RED");
		
		Doc doc = new Doc();
		doc.setAutoCreate( true );
		doc.setBorderColor("BLUE");

		try {
	    LocalizedText text = this._school.getLocalizedText( iwc.getCurrentLocaleId());
			if (text != null) {
				String body = text.getBody();
				if (body != null) {
					information.setContent(body);	
				}
				this.addHiddenInput(new HiddenInput(this.PARAMETER_LOCALIZED_TEXT_ID, text.getPrimaryKey().toString()));
			}
			
			if ( this._school.getName() != null) {
				schoolName.setContent(this._school.getName());
			}
			if (this._school.getSchoolAddress() != null) {
				streetName.setContent(this._school.getSchoolAddress());
			}
			if ( this._school.getSchoolZipCode() != null) {
				areaCode.setContent(this._school.getSchoolZipCode());
			}
			if (this._school.getSchoolZipArea() != null) {
				zipArea.setContent(this._school.getSchoolZipArea());
			}
			if ( this._school.getSchoolPhone() != null) {
				phone.setContent(this._school.getSchoolPhone());
			}
			if ( this._school.getSchoolWebPage() != null) {
				webPage.setContent(this._school.getSchoolWebPage());
			}
			if ( this._school.getSchoolFax() != null) {
				fax.setContent(this._school.getSchoolFax());
			}
			if ( this._school.getSchoolEmail() != null) {
				email.setContent(this._school.getSchoolEmail());
			}
			if ( this._school.getSchoolVisitAddress() != null) {
				visitAddress.setContent(this._school.getSchoolVisitAddress());
			}
			if ( this._school.getSchoolManagementType() != null) {
				manType.setSelectedElement(this._school.getSchoolManagementTypeString());
			}
			if ( this._school.getMapUrl() != null ) {
				mapUrl.setContent(this._school.getMapUrl());	
			}
			if ( this._school.getActivity() != null ) {
				activity.setContent(this._school.getActivity());	
			}
			if ( this._school.getOpenHours() != null ) {
				openHours.setContent(this._school.getOpenHours());	
			}
			
		} catch (IDORelationshipException e) {
			e.printStackTrace(System.err);
		}


		try {
				Collection files = this._school.getImages();
				if (files != null && files.size() > 0) {
					Iterator iter = files.iterator();
					/**  Only using one image ... altough School supports more. */
					ICFile file = (ICFile) iter.next();
					imageInserter.setImageId( ((Integer)file.getPrimaryKey()).intValue());
				}
		} catch (IDORelationshipException e) {
			e.printStackTrace(System.err);
		}

		addLeft(formatHeadline(this._iwrb.getLocalizedString("school.school_info_editor", "Edit school information")), false);
//		this.addLeft(_school.getName(), "School Information Editor");
//		this.addBreak();

		this.addLeft(this._iwrb.getLocalizedString("school.name", "Name"), schoolName, true);

		Table addressTable = new Table(3, 6);
		addressTable.setCellpaddingAndCellspacing(0);
		Text sNameText = new Text(this._iwrb.getLocalizedString("school.address", "Address"));
		Text sNumberText = new Text(this._iwrb.getLocalizedString("school.number", "Number"));
		Text sAreaCodeText = new Text(this._iwrb.getLocalizedString("school.area_code", "Area code"));
		Text sZipAreaText = new Text(this._iwrb.getLocalizedString("school.zip_area","Zip area"));
		Text sPhoneText = new Text(this._iwrb.getLocalizedString("school.phone","Phone"));
		Text sFaxText = new Text(this._iwrb.getLocalizedString("school.fax","Fax"));
		Text sEmailText = new Text(this._iwrb.getLocalizedString("school.email","Email"));
		Text sActivityText = new Text(this._iwrb.getLocalizedString("school.activity","Activity"));
		Text sOpenHoursText = new Text(this._iwrb.getLocalizedString("school.open_hours","Open hours"));
		Text sVisitAddress = null;
		if (this._isAdultEducation) {
			sVisitAddress = new Text(this._iwrb.getLocalizedString("school.educating_address","Educating address"));
		}
		else {
			sVisitAddress = new Text(this._iwrb.getLocalizedString("school.visiting_address","Visiting address"));
		}
		formatText(sNameText, true);
		formatText(sNumberText, true);
		formatText(sAreaCodeText, true);
		formatText(sPhoneText, true);
		formatText(sFaxText, true);
		formatText(sEmailText, true);
		formatText(sZipAreaText, true);
		formatText(sActivityText, true);
		formatText(sOpenHoursText, true);
		formatText(sVisitAddress, true);
		
		setStyle(streetName);
		setStyle(areaCode);
		setStyle(phone);
		setStyle(fax);
		setStyle(email);
		setStyle(zipArea);
		setStyle(sActivityText);
		setStyle(sOpenHoursText);
		setStyle(visitAddress);
		
		addressTable.add(sNameText, 1, 1);
		addressTable.add(streetName, 1, 2);
		addressTable.mergeCells(1, 1, 3, 1);
		addressTable.mergeCells(1, 2, 3, 2);
		addressTable.add(sAreaCodeText, 1, 3);
		addressTable.add(sZipAreaText, 3, 3);
		addressTable.add(areaCode, 1, 4);
		addressTable.add(zipArea, 3, 4);
		addressTable.add(sPhoneText, 1, 5);
		addressTable.add(sFaxText, 3, 5);
		addressTable.add(phone, 1, 6);
		addressTable.add(fax, 3, 6);
		addressTable.add(sEmailText, 3, 7);
		addressTable.add(email, 3, 8);
		addressTable.add(sVisitAddress, 3, 9);
		addressTable.add(visitAddress, 3, 10);
		addressTable.setWidth(2, 3, "4");
		
		
//		this.addLeft(addressTable, false);
		
		setStyle(information);
		this.addLeft(this._iwrb.getLocalizedString("school.information","Information"), information, true);

		this.addLeft(this.sue.getSchoolUsersTable(iwc, this._school, false), false);


		this.addRight(this._iwrb.getLocalizedString("school.image","Image"), imageInserter, true);
		if (this._isAdultEducation) {
			this.addRight(this._iwrb.getLocalizedString("school.educating_address","Educating address"), visitAddress, true);
		}
		else {
			this.addRight(this._iwrb.getLocalizedString("school.visiting_address","Visiting address"), visitAddress, true);
		}
		this.addRight(this._iwrb.getLocalizedString("school.address", "Address"), streetName, true);
		this.addRight(this._iwrb.getLocalizedString("school.area_code", "Area code"), areaCode, true);
		this.addRight(this._iwrb.getLocalizedString("school.zip_area", "Zip"), zipArea, true);
		this.addRight(this._iwrb.getLocalizedString("school.phone", "Phone"), phone, true);
		this.addRight(this._iwrb.getLocalizedString("school.fax", "Fax"), fax, true);
		this.addRight(this._iwrb.getLocalizedString("school.email", "Email"), email, true);
		this.addRight(this._iwrb.getLocalizedString("school.management_type", "Management type"), manType, true);
		this.addRight(this._iwrb.getLocalizedString("school.web_page", "Web page"), webPage, true);
		this.addRight(this._iwrb.getLocalizedString("school.map_url", "Map URL"), mapUrl, true);
		this.addRight(this._iwrb.getLocalizedString("school.activity", "Activity"), activity, true);
		this.addRight(this._iwrb.getLocalizedString("school.open_hours", "Open hours"), openHours, true);
		
//		this.addRight("Doc", doc, true);
//		this.addRight("Box", box, true);
		
		this.addHiddenInput( new HiddenInput(PARAMETER_SCHOOL_ID, this._school.getPrimaryKey().toString() ));
		this.addRight("");
		this.addSubmitButton(new SubmitButton(this._iwrb.getLocalizedString("school.save", "Save"), this.PARAMETER_ACTION, this.ACTION_UPDATE));
	}

	private void mainFormHighSchool(IWContext iwc) throws RemoteException{
				
			TextEditor information = new TextEditor(this.PARAMETER_INFORMATION, "");
			// Created by Malin (malin.anulf@agurait.com) 03 Nov 2003 
		 
			information.setWidth("580");
			information.setHeight("300");

			ImageInserter imageInserter = new ImageInserter(this.PARAMETER_IMAGE_ID);
			imageInserter.setUseBoxParameterName(this.PARAMETER_USE_IMAGE);
			imageInserter.setSelected( true );
		
			TextInput schoolName = new TextInput(this.PARAMETER_SCHOOL_NAME);
			TextInput streetName = new TextInput(this.PARAMETER_SCHOOL_ADDRESS_STREET);
			TextInput areaCode = new TextInput(this.PARAMETER_SCHOOL_ADDRESS_POSTAL_CODE);
			TextInput zipArea = new TextInput(this.PARAMETER_SCHOOL_ZIP_AREA);
			TextInput mapUrl = new TextInput(this.PARAMETER_SCHOOL_MAP_URL);
			TextInput activity = new TextInput(this.PARAMETER_SCHOOL_ACTIVITY);
			TextInput openHours = new TextInput(this.PARAMETER_SCHOOL_OPEN_HOURS);
			TextInput visitAddress = new TextInput(this.PARAMETER_SCHOOL_VISIT_ADDRESS);
			
		
			schoolName.setSize(40);
//			streetName.setSize(40);
			areaCode.setSize(7);
//			zipArea.setSize(20);
		
			SelectorUtility util = new SelectorUtility();
			DropdownMenu manType = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(this.PARAMETER_SCHOOL_MANAGEMENT_TYPE), getSchoolBusiness(iwc).getSchoolManagementTypes(), "getLocalizedString", this._iwrb);

			TextInput phone = new TextInput(this.PARAMETER_SCHOOL_PHONE);
			TextInput fax = new TextInput(this.PARAMETER_SCHOOL_FAX);
			TextInput email = new TextInput(this.PARAMETER_SCHOOL_EMAIL);
			TextInput webPage = new TextInput(this.PARAMETER_SCHOOL_WEBPAGE);
		
			Box box = new Box("Repps");
			box.setBorderColor("RED");
		
			Doc doc = new Doc();
			doc.setAutoCreate( true );
			doc.setBorderColor("BLUE");

			try {
			LocalizedText text = this._school.getLocalizedText( iwc.getCurrentLocaleId());
				if (text != null) {
					String body = text.getBody();
					if (body != null) {
						information.setContent(body);	
					}
					this.addHiddenInput(new HiddenInput(this.PARAMETER_LOCALIZED_TEXT_ID, text.getPrimaryKey().toString()));
				}
			
				if ( this._school.getName() != null) {
					schoolName.setContent(this._school.getName());
				}
				if (this._school.getSchoolAddress() != null) {
					streetName.setContent(this._school.getSchoolAddress());
				}
				if ( this._school.getSchoolZipCode() != null) {
					areaCode.setContent(this._school.getSchoolZipCode());
				}
				if (this._school.getSchoolZipArea() != null) {
					zipArea.setContent(this._school.getSchoolZipArea());
				}
				if ( this._school.getSchoolPhone() != null) {
					phone.setContent(this._school.getSchoolPhone());
				}
				if ( this._school.getSchoolEmail() != null) {
					email.setContent(this._school.getSchoolEmail());
				}
				if ( this._school.getSchoolVisitAddress() != null) {
					visitAddress.setContent(this._school.getSchoolVisitAddress());
				}
				if ( this._school.getSchoolWebPage() != null) {
					webPage.setContent(this._school.getSchoolWebPage());
				}
				if ( this._school.getSchoolFax() != null) {
					fax.setContent(this._school.getSchoolFax());
				}
				if ( this._school.getSchoolManagementType() != null) {
					manType.setSelectedElement(this._school.getSchoolManagementTypeString());
				}
				if ( this._school.getMapUrl() != null ) {
					mapUrl.setContent(this._school.getMapUrl());	
				}
				if ( this._school.getActivity() != null ) {
					activity.setContent(this._school.getActivity());	
				}
				if ( this._school.getOpenHours() != null ) {
					openHours.setContent(this._school.getOpenHours());	
				}
			
			} catch (IDORelationshipException e) {
				e.printStackTrace(System.err);
			}


			try {
					Collection files = this._school.getImages();
					if (files != null && files.size() > 0) {
						Iterator iter = files.iterator();
						/**  Only using one image ... altough School supports more. */
						ICFile file = (ICFile) iter.next();
						imageInserter.setImageId( ((Integer)file.getPrimaryKey()).intValue());
					}
			} catch (IDORelationshipException e) {
				e.printStackTrace(System.err);
			}

			addLeft(formatHeadline(this._iwrb.getLocalizedString("school.school_info_editor", "Edit school information")), false);
//			this.addLeft(_school.getName(), "School Information Editor");
//			this.addBreak();

			this.addLeft(this._iwrb.getLocalizedString("school.name", "Name"), schoolName, true);

			Table addressTable = new Table(3, 8);
			addressTable.setCellpaddingAndCellspacing(0);
			Text sNameText = new Text(this._iwrb.getLocalizedString("school.address", "Address"));
			//Text sNumberText = new Text(_iwrb.getLocalizedString("school.number", "Number"));
			Text sAreaCodeText = new Text(this._iwrb.getLocalizedString("school.area_code", "Area code"));
			Text sZipAreaText = new Text(this._iwrb.getLocalizedString("school.zip_area","Zip area"));
			Text sPhoneText = new Text(this._iwrb.getLocalizedString("school.phone","Phone"));
			Text sFaxText = new Text(this._iwrb.getLocalizedString("school.fax","Fax"));
			Text sEmailText = new Text(this._iwrb.getLocalizedString("school.email","Email"));
			Text sActivityText = new Text(this._iwrb.getLocalizedString("school.activity","Activity"));
			Text sOpenHoursText = new Text(this._iwrb.getLocalizedString("school.open_hours","Open hours"));
			Text sVisitAddress = new Text(this._iwrb.getLocalizedString("school.visiting_address","Visiting address"));
			
			formatText(sNameText, true);
		
			formatText(sAreaCodeText, true);
			formatText(sPhoneText, true);
			formatText(sFaxText, true);
			formatText(sEmailText, true);
			formatText(sZipAreaText, true);
			formatText(sActivityText, true);
			formatText(sOpenHoursText, true);
			formatText(sVisitAddress, true);
			
			setStyle(streetName);
			setStyle(areaCode);
			setStyle(phone);
			setStyle(fax);
			setStyle(email);
			setStyle(zipArea);
			setStyle(sActivityText);
			setStyle(sOpenHoursText);
			setStyle(visitAddress);
		
			addressTable.add(sNameText, 1, 1);
			addressTable.add(streetName, 1, 2);
			addressTable.mergeCells(1, 1, 3, 1);
			addressTable.mergeCells(1, 2, 3, 2);
			addressTable.add(sAreaCodeText, 1, 3);
			addressTable.add(sZipAreaText, 3, 3);
			addressTable.add(areaCode, 1, 4);
			addressTable.add(zipArea, 3, 4);
			addressTable.add(sPhoneText, 1, 5);
			addressTable.add(sFaxText, 3, 5);
			addressTable.add(phone, 1, 6);
			addressTable.add(fax, 3, 6);
			addressTable.add(sEmailText, 3, 7);
			addressTable.add(email, 3, 8);
			addressTable.add(sVisitAddress, 3, 9);
			addressTable.add(visitAddress, 3, 10);
			addressTable.setWidth(2, 3, "4");
		
		//addressTable.setBorder(1);

//			this.addLeft(addressTable, false);
		
			setStyle(information);
			this.addLeft(this._iwrb.getLocalizedString("school.information","Information"), information, true);

			//this.addLeft(sue.getSchoolUsersTable(iwc, this._school, false), false);
			try {
				this.addLeft(this.sue.getHighSchoolUsersTable(iwc, this._school, false), false);
			}catch (Exception e) {
			}

			this.addRight(this._iwrb.getLocalizedString("school.image","Image"), imageInserter, true);
			this.addRight(this._iwrb.getLocalizedString("school.visiting_address","Visiting address"), visitAddress, true);
			this.addRight(this._iwrb.getLocalizedString("school.address", "Address"), streetName, true);
			this.addRight(this._iwrb.getLocalizedString("school.area_code", "Area code"), areaCode, true);
			this.addRight(this._iwrb.getLocalizedString("school.zip_area", "Zip"), zipArea, true);
			this.addRight(this._iwrb.getLocalizedString("school.phone", "Phone"), phone, true);
			this.addRight(this._iwrb.getLocalizedString("school.fax", "Fax"), fax, true);
			this.addRight(this._iwrb.getLocalizedString("school.email", "Email"), email, true);
			this.addRight(this._iwrb.getLocalizedString("school.management_type", "Management type"), manType, true);
			this.addRight(this._iwrb.getLocalizedString("school.web_page", "Web page"), webPage, true);
			this.addRight(this._iwrb.getLocalizedString("school.map_url", "Map URL"), mapUrl, true);
			this.addRight(this._iwrb.getLocalizedString("school.activity", "Activity"), activity, true);
			this.addRight(this._iwrb.getLocalizedString("school.open_hours", "Open hours"), openHours, true);
		
			//this.addRight("Doc", doc, true);
			//this.addRight("Box", box, true);
		
			this.addHiddenInput( new HiddenInput(PARAMETER_SCHOOL_ID, this._school.getPrimaryKey().toString() ));
			this.addRight("");
			this.addSubmitButton(new SubmitButton(this._iwrb.getLocalizedString("school.save", "Save"), this.PARAMETER_ACTION, this.ACTION_UPDATE));
		}

	private boolean updateSchool(IWContext iwc) throws RemoteException {
		String id = iwc.getParameter( PARAMETER_SCHOOL_ID );
		if (id != null) {
			String information = iwc.getParameter( this.PARAMETER_INFORMATION );
			String imageId = iwc.getParameter( this.PARAMETER_IMAGE_ID );	
			String useImage = iwc.getParameter( this.PARAMETER_USE_IMAGE );
			String school_name = iwc.getParameter( this.PARAMETER_SCHOOL_NAME );
			String street = iwc.getParameter( this.PARAMETER_SCHOOL_ADDRESS_STREET );
			String postalCode = iwc.getParameter( this.PARAMETER_SCHOOL_ADDRESS_POSTAL_CODE );
			String zipArea = iwc.getParameter( this.PARAMETER_SCHOOL_ZIP_AREA);
			String phone = iwc.getParameter( this.PARAMETER_SCHOOL_PHONE );
			String fax = iwc.getParameter( this.PARAMETER_SCHOOL_FAX );
			String email = iwc.getParameter( this.PARAMETER_SCHOOL_EMAIL );
			String manType = iwc.getParameter( this.PARAMETER_SCHOOL_MANAGEMENT_TYPE);
			String webPage = iwc.getParameter( this.PARAMETER_SCHOOL_WEBPAGE );
			String mapUrl = iwc.getParameter( this.PARAMETER_SCHOOL_MAP_URL );
			String activity = iwc.getParameter( this.PARAMETER_SCHOOL_ACTIVITY );
			String openHours = iwc.getParameter( this.PARAMETER_SCHOOL_OPEN_HOURS );
			String visitAddress = iwc.getParameter( this.PARAMETER_SCHOOL_VISIT_ADDRESS);
			
			try {
				if (useImage == null) {
					/** Removes all images */
					this._school.removeImages();
					iwc.removeSessionAttribute( this.PARAMETER_IMAGE_ID );
				}else {
					if (imageId != null && !imageId.equals("-1")) {
						ICFile file = ((ICFileHome) IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(imageId));
						this._school.setImage( file );
					}
				}
				/*
				String ltId = iwc.getParameter(PARAMETER_LOCALIZED_TEXT_ID);
				LocalizedText text = null;
				if (ltId != null) {
					text = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHome(LocalizedText.class)).findByPrimaryKey(new Integer(ltId));
				} else {
					text = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHome(LocalizedText.class)).create();
					text.setLocaleId( iwc.getCurrentLocaleId() );
				}
				text.setBody(information);
				text.store();
				*/
				// ATHUGA HONDLAR BARA 1 Locale

				/**
				* Modified by Kelly (kelly@lindman.se) 14 May 2003 
				* Allow only Content Editors saving the information text.
				* Headmasters and school personell shall not be able to change the info.
				*/
				
				//User currentUser = iwc.getCurrentUser();
				this._school.setLocalizedText( information, iwc.getCurrentLocaleId() );
				
				//if (!school_name.equals("")) { 
				// changed all if cases below so that it saves the changes if the user emptied the field
				if (school_name != null) {
					this._school.setSchoolName(school_name);	
				}
				if (street != null) {
					this._school.setSchoolAddress(street);	
				}
				if (visitAddress != null) {
					this._school.setSchoolVisitAddress(visitAddress);	
				}				
				if (postalCode != null) {
					this._school.setSchoolZipCode(postalCode);
				}
				if (zipArea != null) {
					this._school.setSchoolZipArea(zipArea);	
				}
				if (phone != null) {
					this._school.setSchoolPhone(phone);
				}
				if (fax != null) {
					this._school.setSchoolFax(fax);
				}
				if (email != null) {
					this._school.setSchoolEmail(email);
				}
				if (webPage != null) {
					this._school.setSchoolWebPage(webPage);	
				}
				if (activity != null) {
					this._school.setActivity(activity);	
				}
				if (openHours != null) {
					this._school.setOpenHours(openHours);	
				}
				if (manType != null && !manType.equals("-1")) {
					try {
						this._school.setSchoolManagementType(manType);
					}catch (NumberFormatException e){
						e.printStackTrace(System.err);
					}
				}
						
				if (mapUrl != null) {
					this._school.setMapUrl(mapUrl);	
				}
				
				this._school.store();
				
				this.sue.updateDepartment(iwc);
				this.sue.updateUsers(iwc, this._school);
	
				return true;
			} catch (IDORelationshipException e) {
				e.printStackTrace(System.err);
			} catch (FinderException e) {
				e.printStackTrace(System.err);
			} /*catch (CreateException e) {
				e.printStackTrace(System.err);
			}*/
			
		}
		return false;
	}


	private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
	}
	
	/*
	private UserBusiness getUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return  (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}
	*/
	
	protected SchoolUserBusiness getSchoolUserBusiness(IWContext iwc) throws RemoteException {
		return (SchoolUserBusiness) IBOLookup.getServiceInstance(iwc, SchoolUserBusiness.class);
	}

	public static Form getFormWithButton(School school, String text, String styleClass) {
		Form form = new Form();
		SubmitButton button = new SubmitButton(text);
		form.setWindowToOpen( SchoolContentEditor.class );
		form.addParameter(PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString());
		///debug("[SchoolContentEditor] styleClass : "+styleClass);
		if (styleClass != null) {
			button.setStyleClass(styleClass);	
		}
		form.add(button);
		return form;
	}
	
	public static Link getLink(School school, PresentationObject po) {
		Link link = new Link(po);
		link.setWindowToOpen( SchoolContentEditor.class);
		link.addParameter(PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString() );
		return link;
	}
	


}
