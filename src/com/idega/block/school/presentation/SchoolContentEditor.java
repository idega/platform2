package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.boxoffice.presentation.Box;
import com.idega.block.documents.presentation.Doc;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolBusinessBean;
import com.idega.block.school.business.SchoolContentBusinessBean;
import com.idega.block.school.data.School;
import com.idega.block.text.data.LocalizedText;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICFileHome;
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
import com.idega.user.business.UserBusiness;
import com.idega.idegaweb.IWBundle;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import com.idega.user.data.User;

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
  
  private String CONTENT_EDITORS_GROUP_PARAMETER_NAME = "school.content_editors_group_id";
  	  
  private String PARAMETER_ACTION = "scr_act";
  private String ACTION_UPDATE = "scr_act_ud";

  private IWResourceBundle _iwrb;
  private School _school;
	private SchoolUserEditor sue;

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
		if (_school != null) {
			
			String action = iwc.getParameter(PARAMETER_ACTION);
			if (action == null || action.equals("")) {
				mainForm(iwc);
			}else if (action.equals( ACTION_UPDATE)) {
				boolean success = updateSchool(iwc);
				if (success) {
					addLeft(_iwrb.getLocalizedString("school.update_successful", "Update successful"));
				}else {
					addLeft(_iwrb.getLocalizedString("school.update_failed", "Update failed"));
				}
				mainForm(iwc);
			}else {
				add(_iwrb.getLocalizedString("school.general_error", "General error"));	
			}
			
		}else {
			add(_iwrb.getLocalizedString("school.no_school_selected", "No school selected"));	
		}
	}

	private void init(IWContext iwc) throws RemoteException {
		_iwrb = getResourceBundle(iwc);	
		
		String schoolId = iwc.getParameter(PARAMETER_SCHOOL_ID);
		if (schoolId != null) {
			try {
				_school = getSchoolBusiness(iwc).getSchoolHome().findByPrimaryKey(new Integer(schoolId));	
			} catch (FinderException e) {}
		}
		
		sue = getSchoolUserEditor(iwc);
		//GIMMI FIX THIS
		//sue.addParameter(PARAMETER_SCHOOL_ID, schoolId);
		

		Text testText = new Text("test");
		this.formatText(testText, false);
		sue.setTextStyleNormal( testText );

		Text testText2 = new Text("test");
		this.formatText(testText2, true);
		sue.setTextStyleTitle( testText2 );

		sue.setInputStyle(this.STYLE);
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
		imageInserter.setUseBoxParameterName(PARAMETER_USE_IMAGE);
		imageInserter.setSelected( true );
		
		TextInput schoolName = new TextInput(this.PARAMETER_SCHOOL_NAME);
		TextInput streetName = new TextInput(PARAMETER_SCHOOL_ADDRESS_STREET);
		TextInput areaCode = new TextInput(PARAMETER_SCHOOL_ADDRESS_POSTAL_CODE);
		TextInput zipArea = new TextInput(PARAMETER_SCHOOL_ZIP_AREA);
		TextInput mapUrl = new TextInput(PARAMETER_SCHOOL_MAP_URL);
		TextInput activity = new TextInput(PARAMETER_SCHOOL_ACTIVITY);
		TextInput openHours = new TextInput(PARAMETER_SCHOOL_OPEN_HOURS);
		
		schoolName.setSize(40);
//		streetName.setSize(40);
		areaCode.setSize(7);
//		zipArea.setSize(20);
		
		DropdownMenu manType = new DropdownMenu(PARAMETER_SCHOOL_MANAGEMENT_TYPE);
		manType.addMenuElement(SchoolBusinessBean.MANAGEMENT_TYPE_COMM_ID, _iwrb.getLocalizedString(getSchoolBusiness(iwc).getSchoolManagementTypeString(SchoolBusinessBean.MANAGEMENT_TYPE_COMM_ID), "Communal"));
		manType.addMenuElement(SchoolBusinessBean.MANAGEMENT_TYPE_INDE_ID, _iwrb.getLocalizedString(getSchoolBusiness(iwc).getSchoolManagementTypeString(SchoolBusinessBean.MANAGEMENT_TYPE_INDE_ID), "Independent"));
		manType.addMenuElement(SchoolBusinessBean.MANAGEMENT_TYPE_COOP_ID, _iwrb.getLocalizedString(getSchoolBusiness(iwc).getSchoolManagementTypeString(SchoolBusinessBean.MANAGEMENT_TYPE_COOP_ID), "Cooperative"));

		TextInput phone = new TextInput(PARAMETER_SCHOOL_PHONE);
		TextInput fax = new TextInput(PARAMETER_SCHOOL_FAX);
		TextInput webPage = new TextInput(PARAMETER_SCHOOL_WEBPAGE);
		
		Box box = new Box("Repps");
		box.setBorderColor("RED");
		
		Doc doc = new Doc();
		doc.setAutoCreate( true );
		doc.setBorderColor("BLUE");

		try {
	    LocalizedText text = _school.getLocalizedText( iwc.getCurrentLocaleId());
			if (text != null) {
				String body = text.getBody();
				if (body != null) {
					information.setContent(body);	
				}
				this.addHiddenInput(new HiddenInput(PARAMETER_LOCALIZED_TEXT_ID, text.getPrimaryKey().toString()));
			}
			
			if ( _school.getName() != null) {
				schoolName.setContent(_school.getName());
			}
			if (_school.getSchoolAddress() != null) {
				streetName.setContent(_school.getSchoolAddress());
			}
			if ( _school.getSchoolZipCode() != null) {
				areaCode.setContent(_school.getSchoolZipCode());
			}
			if (_school.getSchoolZipArea() != null) {
				zipArea.setContent(_school.getSchoolZipArea());
			}
			if ( _school.getSchoolPhone() != null) {
				phone.setContent(_school.getSchoolPhone());
			}
			if ( _school.getSchoolWebPage() != null) {
				webPage.setContent(_school.getSchoolWebPage());
			}
			if ( _school.getSchoolFax() != null) {
				fax.setContent(_school.getSchoolFax());
			}
			if ( _school.getSchoolManagermentType() != -1) {
				manType.setSelectedElement(_school.getSchoolManagermentType());
			}
			if ( _school.getMapUrl() != null ) {
				mapUrl.setContent(_school.getMapUrl());	
			}
			if ( _school.getActivity() != null ) {
				activity.setContent(_school.getActivity());	
			}
			if ( _school.getOpenHours() != null ) {
				openHours.setContent(_school.getOpenHours());	
			}
			
		} catch (IDORelationshipException e) {
			e.printStackTrace(System.err);
		}


		try {
				Collection files = _school.getImages();
				if (files != null && files.size() > 0) {
					Iterator iter = files.iterator();
					/**  Only using one image ... altough School supports more. */
					ICFile file = (ICFile) iter.next();
					imageInserter.setImageId( file.getID());
				}
		} catch (IDORelationshipException e) {
			e.printStackTrace(System.err);
		}

		addLeft(formatHeadline(_iwrb.getLocalizedString("school.school_info_editor", "Edit school information")), false);
//		this.addLeft(_school.getName(), "School Information Editor");
//		this.addBreak();

		this.addLeft(_iwrb.getLocalizedString("school.name", "Name"), schoolName, true);

		Table addressTable = new Table(3, 6);
		addressTable.setCellpaddingAndCellspacing(0);
		Text sNameText = new Text(_iwrb.getLocalizedString("school.address", "Address"));
		Text sNumberText = new Text(_iwrb.getLocalizedString("school.number", "Number"));
		Text sAreaCodeText = new Text(_iwrb.getLocalizedString("school.area_code", "Area code"));
		Text sZipAreaText = new Text(_iwrb.getLocalizedString("school.zip_area","Zip area"));
		Text sPhoneText = new Text(_iwrb.getLocalizedString("school.phone","Phone"));
		Text sFaxText = new Text(_iwrb.getLocalizedString("school.fax","Fax"));
		Text sActivityText = new Text(_iwrb.getLocalizedString("school.activity","Activity"));
		Text sOpenHoursText = new Text(_iwrb.getLocalizedString("school.open_hours","Open hours"));

		formatText(sNameText, true);
		formatText(sNumberText, true);
		formatText(sAreaCodeText, true);
		formatText(sPhoneText, true);
		formatText(sFaxText, true);
		formatText(sZipAreaText, true);
		formatText(sActivityText, true);
		formatText(sOpenHoursText, true);
		setStyle(streetName);
		setStyle(areaCode);
		setStyle(phone);
		setStyle(fax);
		setStyle(zipArea);
		setStyle(sActivityText);
		setStyle(sOpenHoursText);
		
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
		addressTable.setWidth(2, 3, "4");
		
		
//		this.addLeft(addressTable, false);
		
		setStyle(information);
		this.addLeft(_iwrb.getLocalizedString("school.information","Information"), information, true);

		this.addLeft(sue.getSchoolUsersTable(iwc, this._school, false), false);


		this.addRight(_iwrb.getLocalizedString("school.image","Image"), imageInserter, true);
		this.addRight(_iwrb.getLocalizedString("school.address", "Address"), streetName, true);
		this.addRight(_iwrb.getLocalizedString("school.area_code", "Area code"), areaCode, true);
		this.addRight(_iwrb.getLocalizedString("school.zip_area", "Zip"), zipArea, true);
		this.addRight(_iwrb.getLocalizedString("school.phone", "Phone"), phone, true);
		this.addRight(_iwrb.getLocalizedString("school.fax", "Fax"), fax, true);
		this.addRight(_iwrb.getLocalizedString("school.management_type", "Management type"), manType, true);
		this.addRight(_iwrb.getLocalizedString("school.web_page", "Web page"), webPage, true);
		this.addRight(_iwrb.getLocalizedString("school.map_url", "Map URL"), mapUrl, true);
		this.addRight(_iwrb.getLocalizedString("school.activity", "Activity"), activity, true);
		this.addRight(_iwrb.getLocalizedString("school.open_hours", "Open hours"), openHours, true);
		
//		this.addRight("Doc", doc, true);
//		this.addRight("Box", box, true);
		
		this.addHiddenInput( new HiddenInput(PARAMETER_SCHOOL_ID, _school.getPrimaryKey().toString() ));
		this.addRight(new String(""));
		this.addSubmitButton(new SubmitButton(_iwrb.getLocalizedString("school.save", "Save"), PARAMETER_ACTION, ACTION_UPDATE));
	}


	private boolean updateSchool(IWContext iwc) throws RemoteException {
		String id = iwc.getParameter( PARAMETER_SCHOOL_ID );
		if (id != null) {
			String information = iwc.getParameter( PARAMETER_INFORMATION );
			String imageId = iwc.getParameter( PARAMETER_IMAGE_ID );	
			String useImage = iwc.getParameter( PARAMETER_USE_IMAGE );
			String school_name = iwc.getParameter( PARAMETER_SCHOOL_NAME );
			String street = iwc.getParameter( PARAMETER_SCHOOL_ADDRESS_STREET );
			String postalCode = iwc.getParameter( PARAMETER_SCHOOL_ADDRESS_POSTAL_CODE );
			String zipArea = iwc.getParameter( PARAMETER_SCHOOL_ZIP_AREA);
			String phone = iwc.getParameter( PARAMETER_SCHOOL_PHONE );
			String fax = iwc.getParameter( PARAMETER_SCHOOL_FAX );
			String manType = iwc.getParameter( PARAMETER_SCHOOL_MANAGEMENT_TYPE);
			String webPage = iwc.getParameter( PARAMETER_SCHOOL_WEBPAGE );
			String mapUrl = iwc.getParameter( PARAMETER_SCHOOL_MAP_URL );
			String activity = iwc.getParameter( PARAMETER_SCHOOL_ACTIVITY );
			String openHours = iwc.getParameter( PARAMETER_SCHOOL_OPEN_HOURS );
			
			try {
				if (useImage == null) {
					/** Removes all images */
					_school.removeImages();
					iwc.removeSessionAttribute( PARAMETER_IMAGE_ID );
				}else {
					if (imageId != null && !imageId.equals("-1")) {
						ICFile file = ((ICFileHome) IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(imageId));
						_school.setImage( file );
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
				
				User currentUser = iwc.getCurrentUser();
				if(currentUser.getGroupID() == getContentEditorsGroupId()) {
					_school.setLocalizedText( information, iwc.getCurrentLocaleId() );
				}

				if (!school_name.equals("")) {
					_school.setSchoolName(school_name);	
				}
				if (!street.equals("")) {
					_school.setSchoolAddress(street);	
				}
				if (!postalCode.equals("")) {
					_school.setSchoolZipCode(postalCode);
				}
				if (!zipArea.equals("")) {
					_school.setSchoolZipArea(zipArea);	
				}
				if (!phone.equals("")) {
					_school.setSchoolPhone(phone);
				}
				if (!fax.equals("")) {
					_school.setSchoolFax(fax);
				}
				if (!webPage.equals("")) {
					_school.setSchoolWebPage(webPage);	
				}
				if (!activity.equals("")) {
					_school.setActivity(activity);	
				}
				if (!openHours.equals("")) {
					_school.setOpenHours(openHours);	
				}
				if (manType != null && !manType.equals("-1")) {
					try {
						_school.setSchoolManagementType(Integer.parseInt(manType));
					}catch (NumberFormatException e){
						e.printStackTrace(System.err);
					}
				}
						
				if (!mapUrl.equals("")) {
					_school.setMapUrl(mapUrl);	
				}
				
				_school.store();
				
				sue.updateUsers(iwc, _school);
	
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

	private int getContentEditorsGroupId() {
		// retrieves the group ID of Editors 
		String groupId = getCommuneBundle().getProperty(CONTENT_EDITORS_GROUP_PARAMETER_NAME);
		if (groupId != null) {
				return Integer.parseInt(groupId);
		}	
		return -1;
	}

	protected IWBundle getCommuneBundle() {
		return this.getIWApplicationContext().getApplication().getBundle(CommuneBlock.IW_BUNDLE_IDENTIFIER);
	}

	private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
	}
	
	private UserBusiness getUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return  (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}
	
	public static Form getFormWithButton(School school, String text, String styleClass) throws RemoteException {
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
	
	public static Link getLink(School school, PresentationObject po) throws RemoteException {
		Link link = new Link(po);
		link.setWindowToOpen( SchoolContentEditor.class);
		link.addParameter(PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString() );
		return link;
	}

}
