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
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.user.data.User;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class EHealthBlock extends Block {
	private IWResourceBundle iwrb = null;
	private IWBundle iwb = null;
	
	public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.ehealth";
	
	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_CHECKBOX = "CheckBox";
	public final static String STYLENAME_INTERFACE = "Interface";
	public final static String STYLENAME_INTERFACE_BUTTON = "InterfaceButton";
	public final static String STYLENAME_SMALL_HEADER = "SmallHeader";
	public final static String STYLENAME_HEADER = "Header";
	public final static String STYLENAME_ERROR_TEXT = "ErrorText";
	public final static String STYLENAME_SMALL_TEXT = "SmallText";
	
	private final static String CELLPADDING_PROPERTY = "cellpadding";
	private ICPage formResponsePage;
	
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

	protected GenericButton getButton(GenericButton button) {
		//temporary, will be moved to IWStyleManager for handling...
		button.setHeight("17");
		
		return (GenericButton) setStyle(button,STYLENAME_INTERFACE_BUTTON);
	}
	
	public IWBundle getBundle() {
		return this.iwb;
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
	
	public Text getLocalizedSmallHeader(String s, String d) {
		return getSmallHeader(localize(s, d));
	}
	
	public Text getSmallHeader(String s) {
		return getStyleText(s, STYLENAME_SMALL_HEADER);
	}
	
	public Text getHeader(String s) {
		return getStyleText(s, STYLENAME_HEADER);
	}
	public Text getLocalizedHeader(String s, String d) {
		return getHeader(localize(s, d));
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
	public InterfaceObject getStyledInterface(InterfaceObject obj) {
		return (InterfaceObject) setStyle(obj, STYLENAME_INTERFACE);
	}
	
	/**
	 * @param iwc
	 * @param healthcentre
	 * @return
	 */
	public PresentationObject getHealthCareMap(IWContext iwc/*, HealthCentre healthcentre*/)
	{
		Image image = getBundle(iwc).getImage("karta1.gif");
		return image;
	}
	
	/**
	 * @param iwc
	 * @return
	 */
	public PresentationObject getVKLogo(IWContext iwc)
	{
		Image image = getBundle(iwc).getImage("vardkontoid_40px.gif");
		return image;
	}
	
	
	/**
	 * @param iwc
	 * @param 
	 * @return
	 */
	public PresentationObject getPrintIcon(IWContext iwc)
	{
		Image image = getBundle(iwc).getImage("printIcon.gif");
		return image;
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
	
	protected int getCellpadding() {
		return Integer.parseInt(getProperty(CELLPADDING_PROPERTY,"2"));	
	}
	
	
	public Text getSmallText(String s) {
		return getStyleText(s, STYLENAME_SMALL_TEXT);
	}
	
	public Text getErrorText(String s) {
		return getStyleText(s, STYLENAME_ERROR_TEXT);
	}
	
	public ICPage getResponsePage() {
		return this.formResponsePage;
	}
	
	public void setResponsePage(ICPage page) {
		this.formResponsePage = page;
	}
	
	protected CommuneUserBusiness getUserBusiness(IWApplicationContext  iwc) throws IBOLookupException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
}
