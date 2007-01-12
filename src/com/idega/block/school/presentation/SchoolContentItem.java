package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import javax.ejb.FinderException;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolContentBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;

/**
 * @author gimmi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class SchoolContentItem extends Block {
  public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";
  
  protected IWContext _iwc = null;
  protected IWResourceBundle _iwrb = null;
  protected School _school = null;
  protected boolean _showDemoWhenSchoolIsNull = false;
  String _fontStyle = null;
  String _fontClass = null;
  String _fontColor = null;

	protected abstract PresentationObject getObject() throws RemoteException;

	public void main(IWContext iwc) throws Exception{
		this._iwc = iwc;
		this._iwrb = super.getResourceBundle( this._iwc );
		this._school = getSchool(iwc);
		
		if ( this._school != null) {
			add(getObject());
		}else if (this._showDemoWhenSchoolIsNull){
			add(getText(this._iwrb.getLocalizedString("content_item_not_found","Item not found")));
		}
		
	}
	
	protected School getSchool(IWContext iwc) throws RemoteException {
		String id = iwc.getParameter( getSchoolContentBusiness(iwc).getParameterSchoolId());
		if (id != null) {
			try {
				return getSchoolBusiness( iwc ).getSchoolHome().findByPrimaryKey(new Integer(id));
			}
			catch (FinderException fe) {
			}
		}
		return null;
	}

	public String getBundleIdentifier() {
			return IW_BUNDLE_IDENTIFIER;
	}

  protected Text getText(String content) {
    Text text = new Text(content);
    if (this._fontStyle != null) {
      text.setFontStyle(this._fontStyle);
    }
    if (this._fontColor != null) {
    	text.setFontColor(this._fontColor);
    }
    if (this._fontClass != null) {
    	text.setFontClass(this._fontClass);
    }
    
    return text;
  }

  public void setFontStyle(String style) {
    this._fontStyle = style;
  }
  
  public void setFontClass(String fontClass) {
  	this._fontClass = fontClass;	
  }
  	
  public void setFontColor(String color) {
    this._fontColor = color;
  }
  
  public void setShowDemoWhenEmpty(boolean showDemo) {
  	this._showDemoWhenSchoolIsNull = showDemo;	
  }
  
	protected SchoolContentBusiness getSchoolContentBusiness(IWUserContext iwac) throws RemoteException{
		return (SchoolContentBusiness) IBOLookup.getSessionInstance( iwac, SchoolContentBusiness.class);
	}

	protected SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) throws RemoteException{
		return (SchoolBusiness) IBOLookup.getServiceInstance( iwac, SchoolBusiness.class);
	}
	

}
