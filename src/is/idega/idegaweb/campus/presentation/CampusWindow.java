/*
 * Created on Apr 2, 2004
 *
 */
package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.business.CampusService;
import is.idega.idegaweb.campus.business.CampusSettings;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWStyleManager;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.user.business.UserBusiness;

/**
 * CampusWindow
 * @author aron 
 * @version 1.0
 */
public class CampusWindow extends StyledIWAdminWindow {
	
	private IWResourceBundle iwrb = null;
	private IWBundle iwb = null;
	private IWStyleManager manager;
	
	public final static String IW_BUNDLE_IDENTIFIER =CampusSettings.IW_BUNDLE_IDENTIFIER;
	
	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_HEADER = "Header";
	public final static String STYLENAME_ERROR_TEXT = "ErrorText";
	
	private final static String DEFAULT_TEXT_FONT_STYLE = "font-weight:plain;";
	private final static String DEFAULT_HEADER_FONT_STYLE = "font-weight:bold;";
	private final static String DEFAULT_ERROR_TEXT_FONT_STYLE = "font-weight:plain;color:#ff0000;";
	
	//private String textFontStyle = DEFAULT_TEXT_FONT_STYLE;
	//private String headerFontStyle = DEFAULT_HEADER_FONT_STYLE;
	//private String errorTextFontStyle = DEFAULT_ERROR_TEXT_FONT_STYLE;
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	
	public void _main(IWContext iwc) throws Exception {
		this.setResourceBundle(getResourceBundle(iwc));
		iwb = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		
		manager = new IWStyleManager();
	
		if (getStyleNames() != null) {
			String prefix = getBundle(this.getIWUserContext()).getBundleName();
			if (prefix != Builderaware.IW_CORE_BUNDLE_IDENTIFIER)
				prefix = prefix.substring(prefix.lastIndexOf(".") + 1) + "_";

			Map styles = getStyleNames();
			Iterator iter = styles.keySet().iterator();
			while (iter.hasNext()) {
				String style = (String) iter.next();
				if (!manager.isStyleSet(prefix + style))
					manager.setStyle(prefix + style, (String) styles.get(style));
			}
		}
		super._main(iwc);
	}

	public void setResourceBundle(IWResourceBundle iwrb) {
		this.iwrb = iwrb;
	}
		

	
	public IWResourceBundle getResourceBundle() {
		return this.iwrb;
	}

	public IWBundle getBundle() {
		return this.iwb;
	}
	
	public Text getText(String s) {
		return getStyleText(s, this.STYLENAME_TEXT);
	}
	
	public Text getErrorText(String s) {
		return getStyleText(s, this.STYLENAME_ERROR_TEXT);
	}
	
	public Text getHeader(String s) {
		return getStyleText(s, this.STYLENAME_HEADER);
	}
	
	public Text getStyleText(String text, String styleName) {
		return (Text) getStyleText(new Text(text),styleName);	
	}
	
	public Text getStyleText(Text text, String styleName) {
		return (Text) setStyle(text,styleName);	
	}
	
	public PresentationObject setStyle(PresentationObject obj, String styleName) {
		return setStyle(obj, styleName, false);
	}
	
	private PresentationObject setStyle(PresentationObject obj, String styleName, boolean isLink) {
		obj.setStyleClass(getStyleName(styleName, isLink));
		return obj;
	}
	private String getStyleName(String styleName, boolean isLink){
		if ( getIWUserContext() != null ) {
			String prefix = getBundle(getIWUserContext()).getBundleName();
			if (prefix != Builderaware.IW_CORE_BUNDLE_IDENTIFIER) {
				prefix = prefix.substring(prefix.lastIndexOf(".") + 1) + "_";
				styleName = prefix+styleName;
			}
		}
		if (manager != null) {
			if (!manager.isStyleSet(styleName))
				manager.setStyle(styleName, "");
		
		}
					
		return styleName;
	}

	/**
	 * Gets a prefixed stylename to use for objects, with prefix specific for the bundle used by this block
	 * if the block is in the core bundle, no prefix is added
	 * @param styleName
	 * @return stylename
	 */
	public String getStyleName(String styleName){
		return getStyleName(styleName, false);
	}
	
	/**
	 * Override to add styles (names) to stylesheet.  Add name (String) as key and style (String) as value.
	 */
	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { STYLENAME_TEXT,  STYLENAME_HEADER,STYLENAME_ERROR_TEXT};
		String[] styleValues = { DEFAULT_TEXT_FONT_STYLE, DEFAULT_HEADER_FONT_STYLE,DEFAULT_ERROR_TEXT_FONT_STYLE};

		for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}

		return map;
	}
	
	public String localize(String textKey, String defaultText) {
		if (iwrb == null) {
			return defaultText;
		}
		return iwrb.getLocalizedString(textKey, defaultText);
	}
	
	public PresentationObject getNoAccessObject(IWContext iwc){
		return getErrorText(localize("restricted_zone","Restricted zone"));
	}
	
	public CampusService getCampusService(IWContext iwc)throws RemoteException{
		return (CampusService)IBOLookup.getServiceInstance(iwc,CampusService.class);
	}
	
	public CampusSettings getCampusSettings(IWContext iwc)throws RemoteException{
		return getCampusService(iwc).getCampusSettings();
	}
	
	public UserBusiness getUserService(IWContext iwc)throws RemoteException{
		return (UserBusiness) IBOLookup.getServiceInstance(iwc,UserBusiness.class);
	}
}
