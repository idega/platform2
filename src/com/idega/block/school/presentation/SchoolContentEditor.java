package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.boxoffice.presentation.Box;
import com.idega.block.boxoffice.presentation.BoxCategoryChooser;
import com.idega.block.documents.presentation.Doc;
import com.idega.block.media.presentation.ImageInserter;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolContentBusinessBean;
import com.idega.block.school.data.School;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.business.IBOLookup;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.texteditor.TextEditor;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;

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

		this.addLeft(_school.getName(), "School Information Editor");
		this.addBreak();
		this.addLeft(_iwrb.getLocalizedString("nacka.information","Information"), information, true);
		
		this.addRight(_iwrb.getLocalizedString("nacka.image","Image"), imageInserter, true);
		
//		this.addRight("Doc", doc, true);
//		this.addRight("Box", box, true);
		
		this.addHiddenInput( new HiddenInput(PARAMETER_SCHOOL_ID, _school.getPrimaryKey().toString() ));
		this.addSubmitButton(new SubmitButton(_iwrb.getLocalizedString("nacka.save","Save"), PARAMETER_ACTION, ACTION_UPDATE));
		
	}


	private boolean updateSchool(IWContext iwc) throws RemoteException {
		String id = iwc.getParameter( PARAMETER_SCHOOL_ID );
		if (id != null) {
			String information = iwc.getParameter( PARAMETER_INFORMATION );
			String imageId = iwc.getParameter( PARAMETER_IMAGE_ID );	
			String useImage = iwc.getParameter( PARAMETER_USE_IMAGE );
			
			try {
				if (useImage == null) {
					System.out.println("User image == null... removing images");
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
	
	public static Link getLink(School school) throws RemoteException {
		Link link = new Link("Content Editor");
		link.setWindowToOpen( SchoolContentEditor.class);
		link.addParameter(PARAMETER_SCHOOL_ID, school.getPrimaryKey().toString() );
		return link;
	}

}
