package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.boxoffice.presentation.Box;
import com.idega.block.boxoffice.presentation.BoxCategoryChooser;
import com.idega.block.documents.presentation.Doc;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolBusinessBean;
import com.idega.block.school.business.SchoolContentBusinessBean;
import com.idega.block.school.data.School;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.business.IBOLookup;
import com.idega.core.data.Email;
import com.idega.core.data.EmailHome;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICFileHome;
import com.idega.core.data.Phone;
import com.idega.core.data.PhoneHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.texteditor.TextEditor;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

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
	  
  private String PARAMETER_ACTION = "scr_act";
  private String ACTION_UPDATE = "scr_act_ud";

  private IWResourceBundle _iwrb;
  private School _school;

	public SchoolContentEditor() {
    setUnMerged();
    setWidth(600);
    setHeight(650);
    setResizable(true);
    setScrollbar(true);
		setTitle( "School Editor" );	
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
					addLeft("Update successful");
				}else {
					addLeft("Update failed");
				}
				mainForm(iwc);
			}else {
				add("dont know what to do");	
			}
			
		}else {
			add("No school selected");	
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
		
		
	}


	private void mainForm(IWContext iwc) throws RemoteException{
		TextEditor information = new TextEditor(this.PARAMETER_INFORMATION, "");

		ImageInserter imageInserter = new ImageInserter(this.PARAMETER_IMAGE_ID);
		imageInserter.setUseBoxParameterName(PARAMETER_USE_IMAGE);
		imageInserter.setSelected( true );
		
		TextInput schoolName = new TextInput(this.PARAMETER_SCHOOL_NAME);
		TextInput streetName = new TextInput(PARAMETER_SCHOOL_ADDRESS_STREET);
		TextInput areaCode = new TextInput(PARAMETER_SCHOOL_ADDRESS_POSTAL_CODE);
		TextInput zipArea = new TextInput(PARAMETER_SCHOOL_ZIP_AREA);
		schoolName.setSize(40);
		streetName.setSize(40);
		areaCode.setSize(7);
		zipArea.setSize(20);
		
		DropdownMenu manType = new DropdownMenu(PARAMETER_SCHOOL_MANAGEMENT_TYPE);
		manType.addMenuElement(SchoolBusinessBean.MANAGEMENT_TYPE_PRIVATE_ID, _iwrb.getLocalizedString(getSchoolBusiness(iwc).getSchoolManagementTypeString(SchoolBusinessBean.MANAGEMENT_TYPE_PRIVATE_ID), "Private"));
		manType.addMenuElement(SchoolBusinessBean.MANAGEMENT_TYPE_PUBLIC_ID, _iwrb.getLocalizedString(getSchoolBusiness(iwc).getSchoolManagementTypeString(SchoolBusinessBean.MANAGEMENT_TYPE_PUBLIC_ID), "Public"));
		
		TextInput phone = new TextInput(PARAMETER_SCHOOL_PHONE);
		TextInput fax = new TextInput(PARAMETER_SCHOOL_FAX);
		TextInput webPage = new TextInput(PARAMETER_SCHOOL_WEBPAGE);

		Box box = new Box("Repps");
		box.setBorderColor("RED");
		BoxCategoryChooser boxCh = new BoxCategoryChooser();
		
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

		addLeft(formatHeadline(_iwrb.getLocalizedString("school.school_info_editor", "School information editor")), false);
//		this.addLeft(_school.getName(), "School Information Editor");
//		this.addBreak();

		this.addLeft(_iwrb.getLocalizedString("school.name", "Name"), schoolName, true);

		Table addressTable = new Table(2, 6);
		Text sNameText = new Text(_iwrb.getLocalizedString("school.address", "Address"));
		Text sNumberText = new Text(_iwrb.getLocalizedString("school.number", "Number"));
		Text sAreaCodeText = new Text(_iwrb.getLocalizedString("school.area_code", "Area Code"));
		Text sZipAreaText = new Text(_iwrb.getLocalizedString("school.zip_area","Zip Area"));
		Text sPhoneText = new Text(_iwrb.getLocalizedString("school.phone","Phone"));
		Text sFaxText = new Text(_iwrb.getLocalizedString("school.fax","Fax"));

		formatText(sNameText, true);
		formatText(sNumberText, true);
		formatText(sAreaCodeText, true);
		formatText(sPhoneText, true);
		formatText(sFaxText, true);
		formatText(sZipAreaText, true);
		setStyle(streetName);
		setStyle(areaCode);
		setStyle(phone);
		setStyle(fax);
		setStyle(zipArea);
		
		addressTable.add(sNameText, 1, 1);
		addressTable.add(streetName, 1, 2);
		addressTable.mergeCells(1, 1, 2, 1);
		addressTable.mergeCells(1, 2, 2, 2);
		addressTable.add(sAreaCodeText, 1, 3);
		addressTable.add(sZipAreaText, 2, 3);
		addressTable.add(areaCode, 1, 4);
		addressTable.add(zipArea, 2, 4);
		addressTable.add(sPhoneText, 1, 5);
		addressTable.add(sFaxText, 2, 5);
		addressTable.add(phone, 1, 6);
		addressTable.add(fax, 2, 6);
			
		
		this.addLeft(addressTable, true);
		
		setStyle(information);
		this.addLeft(_iwrb.getLocalizedString("school.information","Information"), information, true);



		this.addRight(_iwrb.getLocalizedString("school.image","Image"), imageInserter, true);
		this.addRight(_iwrb.getLocalizedString("school.management_type","Management type"), manType, true);
		this.addRight(_iwrb.getLocalizedString("school.web_page","Web Page"), webPage, true);

//		this.addRight("Doc", doc, true);
//		this.addRight("Box", box, true);
		
		this.addHiddenInput( new HiddenInput(PARAMETER_SCHOOL_ID, _school.getPrimaryKey().toString() ));
		this.addSubmitButton(new SubmitButton(_iwrb.getLocalizedString("school.save","Save"), PARAMETER_ACTION, ACTION_UPDATE));
		
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
				
				LocalizedText text = _school.getLocalizedText(iwc.getCurrentLocaleId());
				if (text == null) {
					text = ((com.idega.block.text.data.LocalizedTextHome)com.idega.data.IDOLookup.getHome(LocalizedText.class)).create();
					text.setLocaleId( iwc.getCurrentLocaleId() );
				}
				text.setBody(information);
				text.store();

				// ATHUGA HONDLAR BARA 1 Locale
				_school.setLocalizedText( text );

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
				if (manType != null && !manType.equals("-1")) {
					try {
						_school.setSchoolManagementType(Integer.parseInt(manType));
					}catch (NumberFormatException e){
						e.printStackTrace(System.err);
					}
				}
				
				_school.store();
	
				return true;
			} catch (IDORelationshipException e) {
				e.printStackTrace(System.err);
			} catch (FinderException e) {
				e.printStackTrace(System.err);
			} catch (CreateException e) {
				e.printStackTrace(System.err);
			}
			
		}
		return false;
	}

	private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
	}
	
	private UserBusiness getUserBusiness(IWApplicationContext iwac) throws RemoteException {
		return  (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
	}
	
	public static Link getLink(School school) throws RemoteException {
		Link link = new Link("Content Editor");
		link.setWindowToOpen( SchoolContentEditor.class);
		link.addParameter(PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString() );
		return link;
	}

}
