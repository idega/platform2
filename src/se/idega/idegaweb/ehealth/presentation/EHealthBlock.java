/*
 * Created on 2004-okt-04
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.ehealth.presentation;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.user.data.User;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class EHealthBlock extends Block {
	private IWResourceBundle iwrb = null;
	private IWBundle iwb = null;
	
	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_CHECKBOX = "CheckBox";
	
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void setResourceBundle(IWResourceBundle iwrb) {
		this.iwrb = iwrb;
	}
	
	public void setBundle(IWBundle iwb) {
		this.iwb = iwb;
	}
	
	public void _main(IWContext iwc)throws Exception{
		this.setResourceBundle(getResourceBundle(iwc));
		setBundle(getBundle(iwc));
		
		super._main(iwc);
	}
	
	public IWResourceBundle getResourceBundle() {
		return this.iwrb;
	}

	public IWBundle getBundle() {
		return this.iwb;
	}
	
	private String getProperty(String propertyName, String nullValue) {
		IWPropertyList property = getIWApplicationContext().getSystemProperties().getProperties("layout_settings");	
		if (property != null) {
			String propertyValue = property.getProperty(propertyName);
			if (propertyValue != null)
				return propertyValue;
		}
		return nullValue;
	}
	public String localize(String textKey, String defaultText) {
		if (iwrb == null) {
			return defaultText;
		}
		return iwrb.getLocalizedString(textKey, defaultText);
	}
	
	/**
	 * Method localize.
	 * @param text text[0] is key, text[1] is default value.
	 * @return String The locale text
	 */
	public String localize(String[] text) {
		return localize(text[0], text[1]);
	}	

	public Text getText(String s) {
		return getStyleText(s, STYLENAME_TEXT);
	}

	public Text getLocalizedText(String s, String d) {
		return getText(localize(s, d));
	}
	
	/**
	 * Returns a PDF icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The PDF icon.
	 */
	protected Image getPDFIcon(String toolTip) {
		Image pdfImage = iwb.getImage("shared/pdf-small.gif", 12, 12);
		pdfImage.setToolTip(toolTip);
		return pdfImage;
	}
	
	/**
	 * Retuns a download link to a file, with a pdf icon 
	 * @param fileID
	 * @param tooltip
	 * @return
	 */
	protected DownloadLink getPDFLink(int fileID,String tooltip){
		DownloadLink link = new DownloadLink(fileID);
		link.setPresentationObject(getPDFIcon(tooltip));
		return link;
	}
	
	protected CheckBox getCheckBox(String name, String value) {
		return (CheckBox) setStyle(new CheckBox(name,value),STYLENAME_CHECKBOX);
	}

	public boolean isCommuneAdministrator(IWContext iwc) {
		try {
			if (isAdministrator(iwc))
				return true;
			
			if (iwc.isLoggedOn()){
				User user = iwc.getCurrentUser();
				return getUserBusiness(iwc).isRootCommuneAdministrator(user);
			}
			return false;
		}
		catch (Exception re) {
			return false;
		}
	}
	
	protected CommuneUserBusiness getUserBusiness(IWApplicationContext  iwc) throws IBOLookupException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
}
