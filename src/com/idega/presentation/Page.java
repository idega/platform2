/*
 *  $Id: Page.java,v 1.150.2.1 2007/01/12 19:31:32 idegaweb Exp $
 *  Created in 2000 by Tryggvi Larusson
 *  Copyright (C) 2001-2005 Idega Software hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 *
 */
package com.idega.presentation;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.data.ICDomain;
import com.idega.core.builder.data.ICDynamicPageTrigger;
import com.idega.core.builder.data.ICPage;
import com.idega.core.data.ICTreeNode;
import com.idega.core.file.business.ICFileSystem;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDONoDatastoreError;
import com.idega.event.IWFrameBusiness;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.idegaweb.IWStyleManager;
import com.idega.idegaweb.IWUserContext;
import com.idega.idegaweb.include.GlobalIncludeManager;
import com.idega.idegaweb.include.StyleSheetLink;
import com.idega.io.serialization.FileObjectReader;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Window;
import com.idega.repository.data.ImplementorRepository;
import com.idega.repository.data.PropertyDescription;
import com.idega.repository.data.PropertyDescriptionHolder;
import com.idega.repository.data.RefactorClassRegistry;
import com.idega.servlet.IWCoreServlet;
import com.idega.util.FacesUtil;
import com.idega.util.FrameStorageInfo;
import com.idega.util.IWColor;
import com.idega.util.URLUtil;
import com.idega.util.datastructures.QueueMap;

/**
 * <p>
 * An instance of this class (or subclass) is always a top level object in
 * UIComponent tree in an HTML presentation in idegaWeb. This object maps to and
 * renders the
 * 
 * <pre>
 * &lt;HTML&gt;&lt;HEAD&gt;...&lt;/HEAD&gt; &lt;BODY&gt;... &lt;/BODY&gt;&lt;/HTML&gt;
 * </pre>
 * 
 * tags in HTML and renders the children inside the body tags.
 * </p>
 * Last modified: $Date: 2007/01/12 19:31:32 $ by $Author: idegaweb $
 * 
 * @author <a href="mailto:tryggvil@idega.com">tryggvil</a>
 * @version $Revision: 1.150.2.1 $
 */
public class Page extends PresentationObjectContainer implements
		PropertyDescriptionHolder {

	// static variables:
	private static Page NULL_CLONE_PAGE = new Page();
	private static boolean NULL_CLONE_PAGE_INITIALIZED = false;
	protected final static String ROWS_PROPERTY = "ROWS";
	protected final static String IW_PAGE_KEY = "idegaweb_page";
	public final static String IW_FRAME_STORAGE_PARMETER = "idegaweb_frame_page";
	public final static String IW_FRAME_CLASS_PARAMETER = "idegaweb_frame_class";
	public final static String IW_FRAMESET_PAGE_PARAMETER = "idegaweb_frameset_path";
	public final static String IW_FRAME_NAME_PARAMETER = "idegaweb_frame_name";
	public final static String PRM_IW_BROWSE_EVENT_SOURCE = "iw_b_e_s";
	// private final static String
	// START_TAG="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n<html>";
	/**
	 * By skipping the validation URL XML compliant browser still recognise
	 * attributes such as height / width *
	 */
	public final static String DOCTYPE_HTML_4_0_1_TRANSITIONAL = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">";
	public final static String DOCTYPE_HTML_4_0_1_STRICT = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";
	public final static String DOCTYPE_XHTML_1_0_TRANSITIONAL = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n\t\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
	public final static String DOCTYPE_XHTML_1_1 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\n\t\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">";

	// private final static String START_TAG =
	// "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n<html>";
	private final static String START_TAG_HTML_4_0 = "<html>";

	private final static String END_TAG = "</html>";
	private static String META_KEYWORDS = "keywords";
	private static String META_DESCRIPTION = "description";
	private static String META_HTTP_EQUIV_EXPIRES = "Expires";
	private final static String NEWLINE = "\n";

	// State held variables:
	private int _ibPageID;
	private String _title;
	// private Script _theAssociatedScript;
	// private Script associatedBodyScript = null;
	// private Script _theSourceScript;
	private boolean _zeroWait = false;
	private int _redirectSecondInterval = -1;
	private String _redirectURL = null;
	private String _redirectInfo;
	private boolean _doReload = false;
	private String _linkColor = "#000000";
	private String _visitedColor = "#000000";
	private String _hoverColor = "#000000";
	private String _textDecoration = "underline";
	private String _hoverDecoration = "underline";
	private String _pageStyleFont = Text.FONT_FACE_ARIAL;
	private String _pageStyleFontSize = Text.FONT_SIZE_10_STYLE_TAG;
	private String _pageStyleFontStyle = Text.FONT_FACE_STYLE_NORMAL;
	// private String _styleSheetURL;
	private String _shortCutIconURL = null;
	private int _shortCutIconID = -1;
	private boolean _addStyleSheet = false;
	private boolean _addBody = true;
	private Map _frameProperties;
	private boolean _isTemplate = false;
	private boolean _isPage = true;
	private boolean _isDraft = false;
	private boolean _isExtendingTemplate = false;
	private String _templateId = null;
	private Map _styleDefinitions;
	private Map _metaTags;
	private QueueMap _styleSheets;
	private QueueMap _javascripts;
	private QueueMap _javascriptStringsBeforeJSUrls;
	private QueueMap _javascriptStringsAfterJSUrls;
	private Map _HTTPEquivs;
	protected Map _localizationMap;
	private boolean addGlobalScript = true;
	private ICFile styleFile = null;
	private ICDynamicPageTrigger dynamicPageTrigger = null;
	private boolean _isCategory = false;
	private ICPage _windowToOpenOnLoad;
	private int _windowWidth = 800;
	private int _windowHeight = 600;
	private ICPage forwardPage;
	private String docType;
	private boolean useIE7Extension = false;

	/**
	 */
	public Page() {
		this("");
	}

	/**
	 *@param s
	 *            Description of the Parameter
	 */
	public Page(String s) {
		super();
		setTransient(false);
		setTitle(s);
	}

	/**
	 *@param color
	 *            The new backgroundColor value
	 */
	public void setBackgroundColor(String color) {
		setStyleAttribute("background-color:" + color);
	}

	/**
	 *@param color
	 *            The new backgroundColor value
	 */
	public void setBackgroundColor(IWColor color) {
		setBackgroundColor(color.getHexColorString());
	}

	/**
	 *@param color
	 *            The new textColor value
	 */
	public void setTextColor(String color) {
		setMarkupAttribute("text", color);
	}

	/**
	 *@param color
	 *            The new alinkColor value
	 */
	public void setAlinkColor(String color) {
		setMarkupAttribute("alink", color);
	}

	/**
	 *@param color
	 *            The new hoverColor value
	 */
	public void setHoverColor(String color) {
		setMarkupAttribute("alink", color);
		this._hoverColor = color;
	}

	/**
	 * Sets the styleDefinition attribute of the Page object
	 * 
	 *@param styleName
	 *            The new styleDefinition value
	 *@param styleAttribute
	 *            The new styleDefinition value
	 */
	public void setStyleDefinition(String styleName, String styleAttribute) {
		if (this._styleDefinitions == null) {
			this._styleDefinitions = new Hashtable();
		}
		this._styleDefinitions.put(styleName, styleAttribute);
	}

	public void addStyleSheetURL(String URL) {
		if (this._styleSheets == null) {
			this._styleSheets = new QueueMap();
		}
		this._styleSheets.put(URL, URL);
	}

	private String getStyleSheetURL(String markup, IWContext iwc) {

		QueueMap map = new QueueMap();
		// The default style sheet MUST come first so we can override it in
		// latter sheets!
		List sheets = GlobalIncludeManager.getInstance().getStyleSheets();
		for (Iterator iter = sheets.iterator(); iter.hasNext();) {
			StyleSheetLink sheet = (StyleSheetLink) iter.next();
			String url = (String) sheet.getUrl();
			String styleSheetURL = iwc.getIWMainApplication()
					.getTranslatedURIWithContext(url);
			map.put(styleSheetURL, styleSheetURL);
			// this.addStyleSheetURL(styleSheetURL);
		}

		StringBuffer buffer = new StringBuffer();

		// First the default and global style sheets
		if (!map.isEmpty()) {
			Iterator iter = map.values().iterator();
			while (iter.hasNext()) {
				String URL = (String) iter.next();
				addStyleSheet(buffer, markup, URL);
			}
		}

		// Now the added style
		if (this._styleSheets != null && !this._styleSheets.isEmpty()) {
			Iterator iter = this._styleSheets.values().iterator();
			while (iter.hasNext()) {
				String URL = (String) iter.next();
				addStyleSheet(buffer, markup, URL);
			}
		}

		return buffer.toString();
	}

	private StringBuffer addStyleSheet(StringBuffer buffer, String markup,
			String URL) {
		return buffer.append("<link type=\"text/css\" href=\"" + URL
				+ "\" rel=\"stylesheet\" " + (!markup.equals(HTML) ? "/" : "")
				+ ">\n");
	}

	public void addJavascriptURL(String URL) {
		if (this._javascripts == null) {
			this._javascripts = new QueueMap();
		}
		this._javascripts.put(URL, URL);
	}

	protected String getJavascriptURLs(IWContext iwc) {
		if (this.addGlobalScript) {
			StringBuffer buffer = new StringBuffer();
			// Print a reference to the global .js script file
			String src = iwc.getIWMainApplication().getCoreBundle()
					.getResourcesURL();
			try {
				ICDomain d = iwc.getDomain();

				if (d.getURL() != null) {
					if (src.startsWith("/")) {
						String protocol;
						/**
						 * @todo this is case sensitive and could break! move to
						 *       IWContext. Also done in Link, SubmitButton,
						 *       Image and PageIncluder
						 **/
						if (iwc.getRequest().isSecure()) {
							protocol = "https://";
						} else {
							protocol = "http://";
						}
						src = protocol + d.getURL() + src;
					}
				}
			} catch (IDONoDatastoreError de) {
				// de.printStackTrace();
			}
			buffer.append("<script type=\"text/javascript\" src=\"" + src
					+ "/iw_core.js\">");
			buffer.append("</script>");
			if (this._javascripts != null && !this._javascripts.isEmpty()) {
				Iterator iter = this._javascripts.values().iterator();
				while (iter.hasNext()) {
					String URL = (String) iter.next();
					buffer.append("<script type=\"text/javascript\" src=\""
							+ URL + "\"></script>\n");
				}
			}
			buffer.append("\n");
			return buffer.toString();
		}
		return "";

	}

	/**
	 * Sets the linkStyle attribute of the Page object
	 * 
	 *@param style
	 *            The new linkStyle value
	 */
	public void setLinkStyle(String style) {
		setStyleDefinition("A", style);
	}

	public void setStyleInStyleSheet(String name, String style) {
		IWStyleManager manager = IWStyleManager.getInstance();
		if (name != null && style != null) {
			manager.setStyle(name, style);
		}
	}

	/**
	 * Sets the linkHoverStyle attribute of the Page object
	 * 
	 *@param style
	 *            The new linkHoverStyle value
	 */
	public void setLinkHoverStyle(String style) {
		setStyleDefinition("A:hover", style);
	}

	/**
	 * Sets the pageStyle attribute of the Page object
	 * 
	 *@param style
	 *            The new pageStyle value
	 */
	public void setPageStyle(String style) {
		setStyleDefinition("body", style);
		setStyleDefinition("table", style);
	}

	/**
	 * Sets the metaTag attribute of the Page object
	 * 
	 *@param tagName
	 *            The new metaTag value
	 *@param tagValue
	 *            The new metaTag value
	 */
	public void setMetaTag(String tagName, String tagValue) {
		if (this._metaTags == null) {
			this._metaTags = new Hashtable();
		}
		this._metaTags.put(tagName, tagValue);
	}

	/**
	 * Sets the hTTPEquivTag attribute of the Page object
	 * 
	 *@param tagName
	 *            The new hTTPEquivTag value
	 *@param tagValue
	 *            The new hTTPEquivTag value
	 */
	public void setHTTPEquivTag(String tagName, String tagValue) {
		if (this._HTTPEquivs == null) {
			this._HTTPEquivs = new Hashtable();
		}
		this._HTTPEquivs.put(tagName, tagValue);
	}

	/**
	 * Sets the keywordsMetaTag attribute of the Page object
	 * 
	 *@param wordsCommaSeparated
	 *            The new keywordsMetaTag value
	 */
	public void setKeywordsMetaTag(String wordsCommaSeparated) {
		setMetaTag(META_KEYWORDS, wordsCommaSeparated);
	}

	/**
	 * Sets the descriptionMetaTag attribute of the Page object
	 * 
	 *@param wordsCommaSeparated
	 *            The new descriptionMetaTag value
	 */
	public void setDescriptionMetaTag(String wordsCommaSeparated) {
		setMetaTag(META_DESCRIPTION, wordsCommaSeparated);
	}

	/**
	 * Sets the expiryDate attribute of the Page object
	 * 
	 *@param dateString
	 *            The new expiryDate value
	 */
	public void setExpiryDate(String dateString) {
		this.setHTTPEquivTag(META_HTTP_EQUIV_EXPIRES, dateString);
	}

	/**
	 * Sets the defaultValues attribute of the Page object
	 */
	private void setDefaultValues() {
		// addStyleSheetURL(_styleSheetURL);
		// the script is now a standard script called iwOpenWindow in iw_core.js
		// getAssociatedScript().addFunction("windowopen",
		// Window.windowScript());
	}

	/**
	 * Gets the styleDefinition attribute of the Page object
	 * 
	 *@return The styleDefinition value
	 */
	public String getStyleDefinition() {
		StringBuffer returnString = new StringBuffer();
		String styleName = "";

		if (this._styleDefinitions != null) {
			returnString.append("<style type=\"text/css\">\n<!--\n");
			Iterator keyIter = this._styleDefinitions.keySet().iterator();
			// Enumeration e = _styleDefinitions.keys();
			// while (e.hasMoreElements()) {
			while (keyIter.hasNext()) {
				// styleName = (String) e.nextElement();
				styleName = (String) keyIter.next();
				returnString.append("\t");
				returnString.append(styleName);
				String styleAttribute = getStyleAttribute(styleName);
				if (!styleAttribute.equals(slash)) {
					returnString.append(" { ");
					returnString.append(styleAttribute);
					returnString.append(" }\n");
				}
				returnString.append("");
			}
			returnString.append("   -->\n</style>");
			returnString.append("\n");
		}

		return returnString.toString();
	}

	/**
	 * <p>
	 * This method gets the script fragment that calls the javacript for the IE7
	 * (plugin) that makes IE more standards compliant.<br/>
	 * See: <a
	 * href="http://dean.edwards.name/IE7/">http://dean.edwards.name/IE7/</a>
	 * </p>
	 * 
	 * @return
	 */
	public String getIE7() {

		String scriptUrl = IWMainApplication.getDefaultIWMainApplication()
				.getCoreBundle().getResourcesURL()
				+ "/ie7/ie7-standard-p.js";

		String scriptString = "<!-- compliance patch for microsoft browsers -->\n"
				+ "<!--[if lt IE 7]><script src=\""
				+ scriptUrl
				+ "\" type=\"text/javascript\"></script><![endif]-->";

		return scriptString;
	}

	/**
	 * <p>
	 * Gets if the IE7 Code fragment is rendered out in the header of the page.
	 * Defaults to false.
	 * </p>
	 * 
	 * @return
	 */
	public boolean getUseIE7Extension() {
		return this.useIE7Extension;
	}

	/**
	 * <p>
	 * Sets if the IE7 Extension (http://dean.edwards.name/IE7/) should be used.
	 * Default is false.
	 * </p>
	 */
	public void setUseIE7Extension(boolean useIE7Extension) {
		this.useIE7Extension = useIE7Extension;
	}

	/**
	 * Gets the styleAttribute attribute of the Page object
	 * 
	 *@param styleName
	 *            Description of the Parameter
	 *@return The styleAttribute value
	 */
	public String getStyleAttribute(String styleName) {
		if (this._styleDefinitions != null) {
			return (String) this._styleDefinitions.get((Object) styleName);
		} else {
			return null;
		}
	}

	/**
	 * Gets the metaTags attribute of the Page object
	 * 
	 *@return The metaTags value
	 */
	public String getMetaTags(String markup) {
		StringBuffer returnString = new StringBuffer();
		String tagName = "";

		if (this._metaTags != null) {
			// Enumeration e = _metaTags.keys();
			// while (e.hasMoreElements()) {
			Iterator keyIter = this._metaTags.keySet().iterator();
			while (keyIter.hasNext()) {
				// tagName = (String) e.nextElement();
				tagName = (String) keyIter.next();
				returnString.append("<meta name=\"");
				returnString.append(tagName);
				returnString.append("\" ");
				String tagValue = getMetaTag(tagName);
				if (tagValue != null) {
					returnString.append(" content=\"");
					returnString.append(tagValue);
					returnString.append("\"");
				}
				returnString.append(" " + (!markup.equals(HTML) ? "/" : "")
						+ ">\n");
			}
			returnString.append("\n");
		}

		if (this._HTTPEquivs != null) {
			// Enumeration e = _HTTPEquivs.keys();
			// while (e.hasMoreElements()) {
			Iterator keyIter = this._HTTPEquivs.keySet().iterator();
			while (keyIter.hasNext()) {
				// tagName = (String) e.nextElement();
				tagName = (String) keyIter.next();
				returnString.append("<meta http-equiv=\"");
				returnString.append(tagName);
				returnString.append("\" ");
				String tagValue = getHTTPEquivTag(tagName);
				if (tagValue != null) {
					returnString.append(" content=\"");
					returnString.append(tagValue);
					returnString.append("\"");
				}
				returnString.append(" " + (!markup.equals(HTML) ? "/" : "")
						+ ">\n");
			}
			returnString.append("\n");
		}

		return returnString.toString();
	}

	/**
	 * Gets the hTTPEquivTag attribute of the Page object
	 * 
	 *@param tagName
	 *            Description of the Parameter
	 *@return The hTTPEquivTag value
	 */
	public String getHTTPEquivTag(String tagName) {
		if (this._HTTPEquivs != null) {
			return (String) this._HTTPEquivs.get((Object) tagName);
		} else {
			return null;
		}
	}

	/**
	 * Gets the metaTag attribute of the Page object
	 * 
	 *@param tagName
	 *            Description of the Parameter
	 *@return The metaTag value
	 */
	public String getMetaTag(String tagName) {
		if (this._metaTags != null) {
			return (String) this._metaTags.get((Object) tagName);
		} else {
			return null;
		}
	}

	/**
	 *@param textDecoration
	 *            The new textDecoration value
	 */
	public void setTextDecoration(String textDecoration) {
		this._textDecoration = textDecoration;
	}

	/**
	 *@param hoverDecoration
	 *            The new hoverDecoration value
	 */
	public void setHoverDecoration(String hoverDecoration) {
		this._hoverDecoration = hoverDecoration;
	}

	/**
	 *@param styleSheetURL
	 *            The new styleSheetURL value
	 */
	public void setStyleSheetURL(String styleSheetURL) {
		int index = styleSheetURL.indexOf(",");

		while (index > -1) {
			addStyleSheetURL(styleSheetURL.substring(0, index));

			try {
				styleSheetURL = styleSheetURL.substring(index + 1);
			} catch (ArrayIndexOutOfBoundsException e) {
				styleSheetURL = styleSheetURL.substring(index);
			}
			styleSheetURL.trim();
			index = styleSheetURL.indexOf(",");
		}

		addStyleSheetURL(styleSheetURL);
	}

	/**
	 *@param color
	 *            The new vlinkColor value
	 */
	public void setVlinkColor(String color) {
		setMarkupAttribute("vlink", color);
		this._visitedColor = color;
	}

	/**
	 *@param color
	 *            The new linkColor value
	 */
	public void setLinkColor(String color) {
		setMarkupAttribute("link", color);
		this._linkColor = color;
	}

	/**
	 *@param textFontFace
	 *            The new pageFontFace value
	 */
	public void setPageFontFace(String textFontFace) {
		this._pageStyleFont = textFontFace;
	}

	/**
	 *@param textFontSize
	 *            The new pageFontSize value
	 */
	public void setPageFontSize(String textFontSize) {
		this._pageStyleFont = textFontSize;
	}

	/**
	 *@param textFontStyle
	 *            The new pageFontStyle value
	 */
	public void setPageFontStyle(String textFontStyle) {
		this._pageStyleFontStyle = textFontStyle;
	}

	/**
	 *@return The pageFontFace value
	 */
	public String getPageFontFace() {
		return (this._pageStyleFont);
	}

	/**
	 *@return The pageFontSize value
	 */
	public String getPageFontSize() {
		return (this._pageStyleFont);
	}

	/**
	 *@return The pageFontStyle value
	 */
	public String getPageFontStyle() {
		return (this._pageStyleFontStyle);
	}

	/**
	 *@param title
	 *            The new title value
	 */
	public void setTitle(String title) {
		this._title = title;
		setName(title);
	}

	public void setLocalizedTitle(String text) {
	}

	public void setIsCategory(boolean isCategory) {
		this._isCategory = isCategory;
	}

	public String getLocalizedTitle(IWContext iwc) {
		// Map tree = PageTreeNode.getTree(iwc);
		BuilderService bservice;
		ICTreeNode node = null;
		try {
			bservice = getBuilderService(iwc);
			int pageId = bservice.getCurrentPageId(iwc);
			int currentUserId = -1;
			if (iwc.isLoggedOn()) {
				currentUserId = iwc.getCurrentUserId();
				node = (ICTreeNode) bservice.getPageTree(pageId, currentUserId);
			} else {
				node = (ICTreeNode) bservice.getPageTree(pageId);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} catch (IDONoDatastoreError de) {
			// de.printStackTrace();
		}

		if (node != null) {
			String locName = node.getNodeName(iwc.getCurrentLocale());
			if (locName != null && !locName.equals("")) {
				return locName;
			}
		}

		return getTitle();
	}

	/**
	 *@param width
	 *            The new marginWidth value
	 */
	public void setMarginWidth(int width) {
		setLeftMargin(width);
		// setAttribute("marginwidth", Integer.toString(width));
	}

	/**
	 *@param height
	 *            The new marginHeight value
	 */
	public void setMarginHeight(int height) {
		setTopMargin(height);
		// setAttribute("marginheight", Integer.toString(height));
	}

	/**
	 *@param leftmargin
	 *            The new leftMargin value
	 */
	public void setLeftMargin(int leftmargin) {
		setStyleAttribute("margin-left:" + leftmargin + "px");
		// setAttribute("leftmargin", Integer.toString(leftmargin));
	}

	/**
	 *@param topmargin
	 *            The new topMargin value
	 */
	public void setTopMargin(int topmargin) {
		setStyleAttribute("margin-top:" + topmargin + "px");
		// setAttribute("topmargin", Integer.toString(topmargin));
	}

	/**
	 *@param allMargins
	 *            The new allMargins value
	 */
	public void setAllMargins(int allMargins) {
		// setMarginWidth(allMargins);
		// setMarginHeight(allMargins);
		setStyleAttribute("margin:" + allMargins + "px");
	}

	/**
	 *@return The title value
	 */
	public String getTitle() {
		return this._title;
	}

	/**
	 *@param myScript
	 *            The new associatedScript value
	 */
	public void setAssociatedScript(Script myScript) {
		getFacets().put("page_associated_script", myScript);
		// _theAssociatedScript = myScript;
	}

	/*
	 *
	 */
	/**
	 * Description of the Method
	 */
	private void initializeAssociatedScript() {
		Script _theAssociatedScript = (Script) getFacets().get(
				"page_associated_script");
		if (_theAssociatedScript == null) {
			_theAssociatedScript = new Script();
			setAssociatedScript(_theAssociatedScript);
		}
	}

	/**
	 *@return The associatedScript value
	 */
	public Script getAssociatedScript() {
		initializeAssociatedScript();
		// return _theAssociatedScript;
		return (Script) getFacets().get("page_associated_script");
	}

	/**
	 *@param imageURL
	 *            The new backgroundImage value
	 */
	public void setBackgroundImage(String imageURL) {
		setStyleAttribute("background:url('" + imageURL + "')");
	}

	/**
	 *@param backgroundImage
	 *            The new backgroundImage value
	 *@todo : this must implemented in the print method...like in the Link
	 *       class IMPORTANT! for this to work you must have an application
	 *       property called IW_USES_OLD_MEDIA_TABLES (set to anything)
	 */
	public void setBackgroundImage(Image backgroundImage) {
		if (backgroundImage != null) {
			setBackgroundImage(getImageUrl(backgroundImage));
		}
	}

	/**
	 *@param image
	 *            Description of the Parameter
	 *@return The imageUrl value
	 *@todo : replace this with a implementation in print IMPORTANT! for this
	 *       to work you must have an application property called
	 *       IW_USES_OLD_MEDIA_TABLES (set to anything)
	 */
	private String getImageUrl(Image image) {

		if (image.getURL() != null) {
			return image.getURL();
		} else {
			return image.getMediaURL();
		}

	}

	/**
	 *@param action
	 *            The new onLoad value
	 */
	public void setOnLoad(String action) {
		setMarkupAttributeMultivalued("onload", action);
	}

	/**
	 * Sets an alert that is displayed on page load,
	 * 
	 * @param alert
	 *            The alert to display.
	 */
	public void setAlertOnLoad(String alert) {
		setOnLoad("alert('" + alert + "');");
	}

	/**
	 *@param action
	 *            The new onBlur value
	 */
	public void setOnBlur(String action) {
		setMarkupAttributeMultivalued("onblur", action);
	}

	/**
	 *@param action
	 *            The new onUnLoad value
	 */
	public void setOnUnLoad(String action) {
		setMarkupAttributeMultivalued("onunload", action);
	}

	/**
	 * Sets an alert that is displayed on page unload,
	 * 
	 * @param alert
	 *            The alert to display.
	 */
	public void setAlertOnUnLoad(String alert) {
		setOnUnLoad("alert('" + alert + "');");
	}

	/**
	 * Sets the window to close immediately when this page is loaded
	 */
	public void close() {
		setOnLoad("window.close()");
	}

	/**
	 * Sets the window to close immediately when page is loaded and the focus on
	 * its parent ( opener ) if exists
	 * 
	 * @param focusOnparent
	 */
	public void close(boolean focusOnParent) {
		if (focusOnParent) {
			setOnLoad("if(window.opener && window.opener.focus){ window.opener.focus(); } window.close()");
		} else {
			close();
		}
	}

	/**
	 * Sets the window to maintain focus when it is blurred
	 */
	public void keepFocus() {
		setOnBlur("window.focus()");
	}

	/**
	 * Sets the page to go directly back in history one step on load of this
	 * page
	 */
	public void setToGoBack() {
		setOnLoad("history.go(-1)");
	}

	/**
	 * Sets the parent (caller) window to reload on Unload
	 */
	public void setParentToReload() {
		setOnUnLoad("window.opener.location.reload()");
	}

	public void setParentToReloadWithURL(String url) {
		setOnUnLoad("window.opener.location.href='" + url + "'");
	}

	/**
	 * Sets the parent (caller) window to submit its first form when this page
	 * unloads if there is one
	 */
	public void setParentPageMainFormToSubmitOnUnLoad() {
		setParentPageFormToSubmitOnUnLoad(0);
	}

	/**
	 * Sets the parent (caller) window to submit its form at index formIndex if
	 * there is one, on unload of this page.
	 * 
	 * @param formIndex
	 *            index of the form in the parent page
	 */
	public void setParentPageFormToSubmitOnUnLoad(int formIndex) {
		setOnUnLoad("window.opener.document.forms[" + formIndex + "].submit()");
	}

	/**
	 * Sets the parent (caller) window to submit the form with the given name if
	 * there is one, on unload of this page.
	 * 
	 * @param formIndex
	 *            index of the form in the parent page
	 */
	public void setParentPageFormToSubmitOnUnLoad(String formName) {
		setOnUnLoad("javascript:window.opener.document.getElementById('"
				+ formName + "').submit()");
	}

	/**
	 *Sets the parent (caller) page to change location (URL) when this page
	 * unloads
	 * 
	 * @param URL
	 *            The new toRedirect value
	 */
	public void setParentToRedirect(String URL) {
		setOnUnLoad("javascript:window.opener.location = '" + URL + "';");
	}

	/**
	 * Displays an alert on load of this page.<br>
	 * 
	 * @author aron@idega.is
	 *@param sMessage
	 *            The new toLoadAlert value
	 */
	public void setToLoadAlert(String sMessage) {
		setOnLoad("alert('" + sMessage + "')");
	}

	/**
	 *@param iwc
	 *            Description of the Parameter
	 *@return Description of the Return Value
	 */
	public boolean doPrint(IWContext iwc) {
		boolean returnBoole;
		if (iwc.getParameter("idegaspecialrequesttype") == null) {
			returnBoole = true;
		} else if (iwc.getParameter("idegaspecialrequesttype").equals("page")
				&& iwc.getParameter("idegaspecialrequestname").equals(
						this.getName())) {
			returnBoole = true;
		} else {
			returnBoole = false;
		}

		return returnBoole;
	}

	/*
	 *
	 */
	/**
	 * Sets the defaultAttributes attribute of the Page object
	 * 
	 *@param iwc
	 *            The new defaultAttributes value
	 */
	private void setDefaultAttributes(IWContext iwc) {
		/*
		 * if (!isAttributeSet("bgcolor")) {
		 * setBackgroundColor(iwc.getDefaultBackgroundColor()); }
		 */
	}

	/**
	 */
	public void setToReload() {
		this._doReload = true;
	}

	/**
	 * Sets the addBody attribute of the Page object
	 * 
	 *@param addBodyTag
	 *            The new addBody value
	 */
	public void setAddBody(boolean addBodyTag) {
		this._addBody = addBodyTag;
	}

	/**
	 *@param URL
	 *            The new toRedirect value
	 */
	public void setToRedirect(String URL) {
		this._zeroWait = true;
		setToRedirect(URL, 0);
	}

	/**
	 *@param URL
	 *            The new toRedirect value
	 *@param secondInterval
	 *            The new toRedirect value
	 */
	public void setToRedirect(String URL, int secondInterval) {
		this._redirectInfo = "" + secondInterval + " ;URL=" + URL;
		this._redirectSecondInterval = secondInterval;
		this._redirectURL = URL;
	}

	/**
	 *@return The redirectInfo value
	 */
	public String getRedirectInfo() {
		return this._redirectInfo;
	}

	public void setToForwardToPage(ICPage page) {
		this.forwardPage = page;
	}

	/**
	 *@param milliseconds
	 *            The new toClose value
	 */
	public void setToClose(int milliseconds) {
		getAssociatedScript().addFunction("close_time",
				"setTimeout(\"window.close()\"," + milliseconds + ")");
	}

	/*
	 *
	 */
	/**
	 * Description of the Method
	 * 
	 *@param newObjToCreate
	 *            Description of the Parameter
	 */
	protected void prepareClone(PresentationObject newObjToCreate) {
		super.prepareClone(newObjToCreate);
		Page newPage = (Page) newObjToCreate;
		newPage._title = this._title;
		// Script newScript = (Script) _theAssociatedScript;
		// if (newScript != null) {
		// newPage._theAssociatedScript = (Script) newScript.clone();
		// }
		newPage._zeroWait = this._zeroWait;
		newPage._redirectInfo = this._redirectInfo;
		newPage._doReload = this._doReload;
		newPage._linkColor = this._linkColor;
		newPage._visitedColor = this._visitedColor;
		newPage._hoverColor = this._hoverColor;
	}

	/**
	 * Description of the Method
	 * 
	 *@param iwc
	 *            Description of the Parameter
	 *@param askForPermission
	 *            Description of the Parameter
	 *@return Description of the Return Value
	 */

	public Object clonePermissionChecked(IWUserContext iwuc,
			boolean askForPermission) {

		// return this.clone(iwc,true);
		if (askForPermission) {
			if (iwuc.hasViewPermission(this)) {

				return this.clone(iwuc, askForPermission);

			} else {
				if (!NULL_CLONE_PAGE_INITIALIZED) {
					try {
						IWContext iwc = IWContext.getInstance();
						// Text pageNotFound = new Text("No permission", true,
						// false, false);
						// pageNotFound.setFontSize(4);
						// NULL_CLONE_PAGE.add(pageNotFound);

						Image noPermissionImage = getBundle(iwc).getImage(
								"shared/stopalert.gif");
						NULL_CLONE_PAGE.add(noPermissionImage);

						if (iwc != null) {
							BuilderService bservice = getBuilderService(iwc);
							int pageId = 1;
							String page = null;
							// getProperty //iwc.getParameter(_PRM_PAGE_ID);
							if (page != null) {
								try {
									pageId = Integer.parseInt(page);
								} catch (NumberFormatException ex) {
									pageId = bservice.getRootPageId();
								}
							} else {
								pageId = bservice.getRootPageId();
							}
							NULL_CLONE_PAGE.setOnLoad("document.location='"
									+ bservice.getPageURI(pageId) + "'");
						}
						NULL_CLONE_PAGE_INITIALIZED = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return NULL_CLONE_PAGE;
			}
		} else {
			return this.clone();
		}

	}

	/**
	 *@param iwc
	 *            Description of the Parameter
	 *@param askForPermission
	 *            Description of the Parameter
	 *@return Description of the Return Value
	 */
	public Object clone(IWUserContext iwc, boolean askForPermission) {
		Page obj = null;
		try {
			obj = (Page) super.clone(iwc, askForPermission);
			// if (_theAssociatedScript != null) {
			// obj._theAssociatedScript = (Script) _theAssociatedScript.clone();
			// }
			obj._title = this._title;
			obj._zeroWait = this._zeroWait;
			obj._redirectInfo = this._redirectInfo;
			obj._doReload = this._doReload;
			obj._linkColor = this._linkColor;
			obj._visitedColor = this._visitedColor;
			obj._hoverColor = this._hoverColor;
			obj._textDecoration = this._textDecoration;
			// obj._styleSheetURL = _styleSheetURL;
			obj._addStyleSheet = this._addStyleSheet;
			obj._ibPageID = this._ibPageID;
			obj.styleFile = this.styleFile;
			if (this._javascripts != null) {
				obj._javascripts = this._javascripts;
			}
			if (this._styleSheets != null) {
				obj._styleSheets = this._styleSheets;
			}
			if (this._styleDefinitions != null) {
				obj._styleDefinitions = this._styleDefinitions;
			}
			if (this.dynamicPageTrigger != null) {
				obj.dynamicPageTrigger = (ICDynamicPageTrigger) this.dynamicPageTrigger
						.clone();
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

		return obj;
	}

	/*
	 * public synchronized Object clone() { Page obj = null; try { obj =
	 * (Page)super.clone(); if (this.theAssociatedScript != null) {
	 * obj.theAssociatedScript = (Script)this.theAssociatedScript.clone(); }
	 * obj.title = this.title; obj.zeroWait = this.zeroWait; obj.redirectInfo =
	 * this.redirectInfo; obj.doReload = this.doReload; obj.linkColor =
	 * this.linkColor; obj.visitedColor = this.visitedColor; obj.hoverColor =
	 * this.hoverColor; obj.textDecoration = this.textDecoration;
	 * obj.styleSheetURL = this.styleSheetURL; obj.addStyleSheet =
	 * this.addStyleSheet; } catch(Exception ex) {
	 * ex.printStackTrace(System.err); } return obj; }
	 */

	/**
	 *@param iwc
	 *            Description of the Parameter
	 *@exception Exception
	 *                Description of the Exception
	 */
	public void main(IWContext iwc) throws Exception {
		if (this.forwardPage != null) {
			iwc.forwardToIBPage(this, this.forwardPage);
		}

		if (this._doReload) {
			if (iwc.getSession().getAttribute("idega_special_reload") != null) {
				iwc.getSession().removeAttribute("idega_special_reload");
			} else {
				setToRedirect(iwc.getRequestURI());
				iwc.getSession().setAttribute("idega_special_reload", "true");
			}
		}

		/* get the files cached url */
		if (this.styleFile != null) {
			ICFileSystem fsystem = getICFileSystem(iwc);
			String styleSheetURL = fsystem.getFileURI(((Integer) this.styleFile
					.getPrimaryKey()).intValue());
			setStyleSheetURL(styleSheetURL);
		}
	}

	/*
	 *
	 */
	/**
	 * Gets the childOfOtherPage attribute of the Page object
	 * 
	 *@return The childOfOtherPage value
	 */
	protected boolean isChildOfOtherPage() {
		UIComponent parent = getParent();
		if (parent != null) {
			if (parent instanceof Page) {
				if (parent instanceof FrameSet) {
					return false;
				} else {
					return true;
				}
			} else if (parent instanceof UIViewRoot) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * @return a boolean wether (this) has a parent that is a FrameSet
	 */
	protected boolean isInFrameSet() {
		UIComponent parent = getParent();
		if (parent != null) {
			if (parent instanceof FrameSet || parent instanceof Frame) {
				return true;
			}
		}

		return false;
	}

	/**
	 * <code>Gets the contents inside the Head <head> </head> tags with the exception of the title and
	 * the "associated script.</code>
	 * 
	 * @param iwc
	 * @return
	 */
	protected String getHeadContents(IWContext iwc) {
		IWMainApplicationSettings settings = iwc.getApplicationSettings();
		String characterEncoding = settings.getCharacterEncoding();
		String markup = iwc.getApplicationSettings().getProperty(
				MARKUP_LANGUAGE, HTML);
		return getHeadContents(markup, characterEncoding, iwc);
	}

	/**
	 * <code>Gets the contents inside the Head <head> </head> tags with the exception of the title and
	 * the "associated script.</code>
	 * 
	 * @param iwc
	 * @return
	 */
	protected String getHeadContents(String markup, String characterEncoding,
			IWContext iwc) {
		StringBuffer buf = new StringBuffer();

		buf.append(getPrintableSchortCutIconURL(iwc));
		if (getUseIE7Extension()) {
			buf.append(getIE7());
		}
		buf.append(getMetaInformation(markup, characterEncoding));
		buf.append(getMetaTags(markup));
		buf.append(getJavaScriptBeforeJavascriptURLs(iwc));
		buf.append(getJavascriptURLs(iwc));
		buf.append(getJavaScriptAfterJavascriptURLs(iwc));
		buf.append(getStyleSheetURL(markup, iwc));
		buf.append(getStyleDefinition());
		return buf.toString();
	}

	/**
	 * <code>Adds the script string to the <head> of the page before javascript.js files are loaded, the added string are printed in the same order as they come in</code>
	 * 
	 * @param script
	 */
	public void addJavaScriptBeforeJavaScriptURLs(String keyInMap, String script) {
		if (this._javascriptStringsBeforeJSUrls == null) {
			this._javascriptStringsBeforeJSUrls = new QueueMap();
		}
		this._javascriptStringsBeforeJSUrls.put(keyInMap, script);
	}

	/**
	 * <code>Adds the script string to the <head> of the page after javascript.js files are loaded, the added string are printed in the same order as they come in</code>
	 * 
	 * @param script
	 */
	public void addJavaScriptAfterJavaScriptURLs(String keyInMap, String script) {
		if (this._javascriptStringsAfterJSUrls == null) {
			this._javascriptStringsAfterJSUrls = new QueueMap();
		}
		this._javascriptStringsAfterJSUrls.put(keyInMap, script);

	}

	public void removeJavaScriptFromJavascriptBeforeJavaScriptsUrlsMap(
			String key) {
		if (this._javascriptStringsBeforeJSUrls != null) {
			this._javascriptStringsBeforeJSUrls.remove(key);
		}
	}

	public void removeJavaScriptFromJavascriptAfterJavaScriptsUrlsMap(String key) {
		if (this._javascriptStringsAfterJSUrls != null) {
			this._javascriptStringsAfterJSUrls.remove(key);
		}
	}

	/**
	 * Gets a block of free form javascript (just strings) to insert BEFORE
	 * importing some javascript.js files
	 * 
	 * @param iwc
	 * @return a javascript block
	 */
	private String getJavaScriptBeforeJavascriptURLs(IWContext iwc) {
		StringBuffer buffer = new StringBuffer();
		if (this._javascriptStringsBeforeJSUrls != null
				&& !this._javascriptStringsBeforeJSUrls.isEmpty()) {
			buffer.append("<script type=\"text/javascript\">\n");

			Iterator iter = this._javascriptStringsBeforeJSUrls.values()
					.iterator();
			while (iter.hasNext()) {
				String value = (String) iter.next();
				buffer.append(value).append("\n");
			}

			buffer.append("</script>\n");
		}
		return buffer.toString();
	}

	/**
	 * Gets a block of free form javascript (just strings) to insert AFTER
	 * importing some javascript.js files
	 * 
	 * @param iwc
	 * @return a javascript block
	 */
	private String getJavaScriptAfterJavascriptURLs(IWContext iwc) {
		StringBuffer buffer = new StringBuffer();
		if (this._javascriptStringsAfterJSUrls != null
				&& !this._javascriptStringsAfterJSUrls.isEmpty()) {
			buffer.append("<script type=\"text/javascript\">\n");

			Iterator iter = this._javascriptStringsAfterJSUrls.values()
					.iterator();
			while (iter.hasNext()) {
				String value = (String) iter.next();
				buffer.append(value).append("\n");
			}

			buffer.append("</script>\n");
		}
		return buffer.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.idega.presentation.PresentationObject#initVariables(com.idega.
	 * presentation.IWContext)
	 */
	public void initVariables(IWContext iwc) throws IOException {
		super.initVariables(iwc);
		setDefaultValues();
		setDefaultAttributes(iwc);
	}

	/**
	 *@param iwc
	 *            Description of the Parameter
	 *@exception Exception
	 *                Description of the Exception
	 */
	public void print(IWContext iwc) throws Exception {

		this.printBegin(iwc);
		// Catch all exceptions that are thrown in print functions of objects
		// stored inside
		try {
			super.print(iwc);
		} catch (Exception ex) {
			println("<h1>An Error Occurred!</h1>");
			println("IW Error");
			println("<pre>");
			String message = ex.getMessage();
			if (message != null) {
				println(message);
			}
			ex.printStackTrace(System.err);
			println("</pre>");
		}

		this.printEnd(iwc);
	}

	public void encodeBegin(FacesContext context) throws IOException {
		callMain(context);
		this.printBegin(IWContext.getIWContext(context));
	}

	/**
	 * Bridging method for JSF:
	 * 
	 * @throws Exception
	 */
	public void printBegin(IWContext iwc) throws IOException {
		this.initVariables(iwc);
		boolean isInsideOtherPage = this.isChildOfOtherPage();

		if (getMarkupLanguage().equals(IWConstants.MARKUP_LANGUAGE_HTML)) {
			if (!isInsideOtherPage) {
				IWMainApplicationSettings settings = iwc
						.getApplicationSettings();
				String characterEncoding = settings.getCharacterEncoding();
				String markup = getMarkupLanguageForPage();
				String docType = getDocType();
				println(getStartTag(iwc.getCurrentLocale(), docType,
						characterEncoding));
				if (this._zeroWait) {
					setDoPrint(false);
				}

				if (this._windowToOpenOnLoad != null) {
					URLUtil url = new URLUtil(iwc, this._windowToOpenOnLoad);
					setOnLoad("javascript:"
							+ Window.getWindowCallingScript(url.toString(),
									"Window", false, false, false, false,
									false, true, true, true, false,
									this._windowWidth, this._windowHeight));
				}

				println("<head>");
				println("<title>" + getLocalizedTitle(iwc) + "</title>\n");
				/*
				 * //shortcut icon println(getPrintableSchortCutIconURL(iwc));
				 * print(getMetaInformation(markup, characterEncoding));
				 * print(getMetaTags(markup)); print(getJavascriptURLs(iwc)); if
				 * (getAssociatedScript() != null) {
				 * getAssociatedScript()._print(iwc); }
				 * print(getStyleSheetURL(markup)); print(getStyleDefinition());
				 */

				print(getHeadContents(markup, characterEncoding, iwc));
				if (getAssociatedScript() != null) {
					// getAssociatedScript()._print(iwc);
					UIComponent script = getAssociatedScript();
					this.renderChild(iwc, script);
				}

				// Laddi: Made obsolete with default style sheet
				/*
				 * if (_addStyleSheet) {
				 * println("<link rel=\"stylesheet\" href=\"" + _styleSheetURL +
				 * "\" type=\"text/css\">\n"); }
				 */

				println("\n</head>");

				if (this._addBody) {
					println("<body " + getMarkupAttributesString() + ">");
					if (getAssociatedBodyScript() != null) {
						// getAssociatedBodyScript()._print(iwc);
						UIComponent script = getAssociatedBodyScript();
						this.renderChild(iwc, script);
					}
				}
				// added by Eiki for frameSet in a page support
			}
		} else if (getMarkupLanguage().equals(IWConstants.MARKUP_LANGUAGE_WML)) {
			println("<?xml version=\"1.0\"?>");
			if (true) {
				println("<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\" \"http://www.wapforum.org/DTD/wml_1.1.xml\">");
			}
			println("<wml>");
			println("<head>");
			println("<meta http-equiv=\"cache-control\" content=\"no-cache\"/>");
			println("</head>");
			print("<card title=\"" + getLocalizedTitle(iwc) + "\"");
			if (this._redirectSecondInterval > -1) {
				print(" ontimer=\"" + this._redirectURL + "\"");
				println(" id=\"card1\">");
				println("<timer value=\"" + this._redirectSecondInterval * 10
						+ "\"/>");
			} else {
				println(" id=\"card1\">");
			}
		} else if (getMarkupLanguage().equals(
				IWConstants.MARKUP_LANGUAGE_PDF_XML)) {
			println("<?xml version=\"1.0\" encoding=\"iso-8859-1\" ?>");
			// println("<!DOCTYPE ITEXT SYSTEM \"http://www.lowagie.com/iText/itext.dtd\">");
			println("<itext producer=\"Idega Software, http://www.idega.com\">");
		}
	}

	public void encodeChildren(FacesContext context) throws IOException {
		List children = getChildren();
		// This is a temporary workaround, because of iterator
		// NoSuchElementException problem (iterator should be used when it
		// starts working)

		try {
			Object[] array = children.toArray();
			for (int i = 0; i < array.length; i++) {
				Object obj = array[i];
				UIComponent child = (UIComponent) obj;
				renderChild(context, child);
			}
		} catch (NotLoggedOnException noex) {
			// TODO: Change this, this is a workaround till a better not logged
			// on error page is created:
			IWContext iwc = castToIWContext(context);
			String notLoggedOnString = getResourceBundle(iwc)
					.getLocalizedString("error_not_logged_on",
							"You are not logged on, please go to login page and log in.");
			println("<h2>" + notLoggedOnString + "</h2>");

		}

		/*
		 * Iterator iter = children.iterator(); int size = children.size();
		 * while(iter.hasNext()){ //for (Iterator iter = children.iterator();
		 * iter.hasNext();) { UIComponent child = (UIComponent) iter.next();
		 * this.renderChild(context,child); }
		 */
	}

	public void encodeEnd(FacesContext context) throws IOException {
		this.printEnd(IWContext.getIWContext(context));
		resetGoneThroughMain();
		long time = FacesUtil.registerRequestEnd(context);
		String renderingText = time + " ms";
		context.getResponseWriter().writeComment(renderingText);
	}

	/**
	 * Bridging method for JSF:
	 */
	public void printEnd(IWContext iwc) throws IOException {

		boolean isInsideOtherPage = this.isChildOfOtherPage();

		if (getMarkupLanguage().equals(IWConstants.MARKUP_LANGUAGE_HTML)) {
			if (!isInsideOtherPage) {
				if (this._addBody) {
					println("\n\n</body>");
				}
				println(getEndTag());
			}
		} else if (getMarkupLanguage().equals(IWConstants.MARKUP_LANGUAGE_WML)) {
			println("</card>");
			println("</wml>");
		} else if (getMarkupLanguage().equals(
				IWConstants.MARKUP_LANGUAGE_PDF_XML)) {
			println("</itext>");

		}
	}

	/**
	 *@param key
	 *            The new property value
	 *@param values
	 *            The new property value
	 */
	public void setProperty(String key, String values[]) {
		if (key.equalsIgnoreCase("title")) {
			setTitle(values[0]);
		}
	}

	/**
	 *@return The startTag value
	 */
	public static String getStartTag(Locale locale, String docType,
			String encoding) {
		StringBuffer buffer = new StringBuffer();
		if (docType.equals(DOCTYPE_XHTML_1_0_TRANSITIONAL)) {
			buffer.append("<?xml version=\"1.0\" encoding=\"").append(
					encoding != null ? encoding : "ISO-8859-1").append("\"?>")
					.append("\n");
			buffer.append(docType);
			buffer.append(NEWLINE);
			buffer
					.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"");
			buffer.append(locale.getLanguage());
			buffer.append("\" lang=\"");
			buffer.append(locale.getLanguage());
			buffer.append("\">");
			return buffer.toString();
		} else if (docType.equals(DOCTYPE_XHTML_1_1)) {
			buffer.append("<?xml version=\"1.0\" encoding=\"").append(
					encoding != null ? encoding : "ISO-8859-1").append("\"?>")
					.append("\n");
			buffer.append(docType);
			buffer.append(NEWLINE);
			buffer
					.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"");
			buffer.append(locale.getLanguage());
			buffer.append("\">");
			return buffer.toString();
		} else {
			buffer.append(docType);
			buffer.append(NEWLINE);
			buffer.append(START_TAG_HTML_4_0);
			buffer.append(NEWLINE);
			return buffer.toString();
		}

		/*
		 * if (markup.equals(XHTML)) { StringBuffer buffer = new StringBuffer();
		 * buffer.append("<?xml version=\"1.0\" encoding=\"").append(encoding !=
		 * null ? encoding : "ISO-8859-1").append("\"?>").append("\n");
		 * //buffer.
		 * append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\""
		 * ).append("\n");//buffer.append(
		 * "\t\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
		 * ).append("\n"); buffer.append(DOCTYPE_XHTML_1_0_TRANSITIONAL);
		 * buffer.
		 * append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\""
		 * +locale.getLanguage()+"\" lang=\""+locale.getLanguage()+"\">");
		 * return buffer.toString(); } else if (markup.equals(XHTML1_1)) {
		 * StringBuffer buffer = new StringBuffer();
		 * buffer.append("<?xml version=\"1.0\" encoding=\"").append(encoding !=
		 * null ? encoding : "ISO-8859-1").append("\"?>").append("\n");
		 * //buffer.
		 * append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"")
		 * .append("\n");
		 * //buffer.append("\t\"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"
		 * ).append("\n"); buffer.append(DOCTYPE_XHTML_1_1);
		 * buffer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\""
		 * +locale.getLanguage()+"\">"); return buffer.toString(); } return
		 * START_TAG;
		 */
	}

	/**
	 *@return The endTag value
	 */
	public static String getEndTag() {
		return END_TAG;
	}

	/**
	 *@param iwc
	 *            Description of the Parameter
	 *@return The metaInformation value
	 */
	public String getMetaInformation(String markup, String characterEncoding) {

		boolean addIdegaAuthorAndCopyRight = false;

		String theReturn = "<meta http-equiv=\"content-type\" content=\"text/html; charset="
				+ characterEncoding
				+ "\" "
				+ (!markup.equals(HTML) ? "/" : "")
				+ ">\n<meta name=\"generator\" content=\"idegaWeb "
				+ IWContext.getInstance().getIWMainApplication()
						.getProductInfo().getVersion()
				+ "\" "
				+ (!markup.equals(HTML) ? "/" : "") + ">\n";

		// If the user is logged on then there is no caching by proxy servers
		boolean notUseProxyCaching = true;

		if (notUseProxyCaching) {
			theReturn += "<meta http-equiv=\"pragma\" content=\"no-cache\" "
					+ (!markup.equals(HTML) ? "/" : "") + ">\n";
		}
		if (getRedirectInfo() != null) {
			theReturn += "<meta http-equiv=\"refresh\" content=\""
					+ getRedirectInfo() + "\" "
					+ (!markup.equals(HTML) ? "/" : "") + ">\n";
		}

		if (addIdegaAuthorAndCopyRight) {
			theReturn += "<meta name=\"author\" content=\"idega.is\"/>\n<meta name=\"copyright\" content=\"idega.is\" "
					+ (!markup.equals(HTML) ? "/" : "") + ">\n";
		}
		return theReturn;
	}

	/**
	 * Used to find the Page object to be printed in top of the current page
	 * 
	 *@param iwc
	 *            Description of the Parameter
	 *@return The page value
	 */
	public static Page getPage(IWContext iwc) {
		Page page = (Page) IWCoreServlet.retrieveObject(IW_PAGE_KEY);
		return page;
	}

	/**
	 *@param iwc
	 *            Description of the Parameter
	 *@return Description of the Return Value
	 *@exception Exception
	 *                Description of the Exception
	 */
	public static Page loadPage(IWContext iwc) throws Exception {
		String classKey = iwc.getParameter(IW_FRAME_CLASS_PARAMETER);
		String frameKey = iwc.getParameter(IW_FRAME_STORAGE_PARMETER);
		String framePathKey = iwc.getParameter(IW_FRAMESET_PAGE_PARAMETER);
		String frameNameKey = iwc.getParameter(IW_FRAME_NAME_PARAMETER);

		if (framePathKey != null && frameNameKey != null) {
			/**
			 * @todo EJB create
			 */
			IWFrameBusiness fb = (IWFrameBusiness) IBOLookup
					.getSessionInstance(iwc, IWFrameBusiness.class);
			Page pg = fb.getFrame(framePathKey, frameNameKey);
			if (pg != null) {
				// if( iwc.getParameter(PRM_IW_BROWSE_EVENT_SOURCE) != null &&
				// pg instanceof IWBrowseControl){
				// //System.out.println("dispatchEvent(iwc)");
				// ((IWBrowseControl)pg).dispatchEvent(iwc);
				// }
				// else {
				// System.out.println("!dispatchEvent(iwc)");
				// }
				return pg;
			} else {
				Page defaultPage = new Page();
				// defaultPage.setBackgroundColor("#FF0000");
				System.err.println("[" + Page.class + "]: Frame "
						+ frameNameKey + ": page is null");
				return defaultPage;
			}

		} else if (frameKey != null) {
			Page page = getPage(getFrameStorageInfo(iwc), iwc);
			System.out
					.println("com.idega.presentation.Page: Trying to get page stored in session");
			return page;
		} else if (classKey != null) {
			// try{
			String className = IWMainApplication.decryptClassName(classKey);
			Page page = null;
			try {
				page = (Page) RefactorClassRegistry.forName(className)
						.newInstance();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new IWPageInitializationException(
						"There was an error, your session is probably expired");
			}
			String sID = iwc
					.getParameter(IWMainApplication._PARAMETER_IC_OBJECT_INSTANCE_ID);
			try {
				if (sID != null) {
					System.err.println("sID: " + sID);
					page.setICObjectInstanceID(Integer.parseInt(sID));
					// this.ib_object_instance_id = Integer.parseInt(sID);
					System.err.println("Integer.parseInt(sID): "
							+ Integer.parseInt(sID));
					System.err.println("getICObjectInstanceID: "
							+ page.getICObjectInstanceID());
				}
				/*
				 * else{ System.err.println("sID == null"); }
				 */
			} catch (NumberFormatException ex) {
				System.err
						.println(page + ": cannot init ic_object_instance_id");
			}
			return page;
			// }
			// catch(Exception e){
			// Page page = new Page();
			// page.add("Page invalid");
			// page.addBreak();
			// page.add(e.getClass().getName()+"Message: "+e.getMessage());
			// e.printStackTrace();
			// return page;
			// }
		} else {
			return new Page();
		}
	}

	/*
	 *
	 */
	/**
	 * Gets the frameStorageInfo attribute of the Page class
	 * 
	 *@param iwc
	 *            Description of the Parameter
	 *@return The frameStorageInfo value
	 */
	private static FrameStorageInfo getFrameStorageInfo(IWContext iwc) {
		String key = iwc.getParameter(IW_FRAME_STORAGE_PARMETER);
		FrameStorageInfo info = (FrameStorageInfo) iwc.getSessionAttribute(key);
		if (info == null) {
			info = FrameStorageInfo.EMPTY_FRAME;
		}
		return info;
	}

	/*
	 *
	 */
	/**
	 * Gets the page attribute of the Page class
	 * 
	 *@param info
	 *            Description of the Parameter
	 *@param iwc
	 *            Description of the Parameter
	 *@return The page value
	 */
	private static Page getPage(FrameStorageInfo info, IWContext iwc) {
		String key = info.getStorageKey();
		Page theReturn = (Page) iwc.getSessionAttributeWeak(key);
		if (theReturn == null) {
			try {
				theReturn = (Page) info.getFrameClass().newInstance();
			} catch (Exception ex) {
				if (theReturn == null) {
					theReturn = new Page("Expired");
					theReturn.add("This page has expired");
				}
				ex.printStackTrace();
			}
			storePage(theReturn, iwc);
		}
		return theReturn;
	}

	/**
	 *@param page
	 *            Description of the Parameter
	 *@param iwc
	 *            Description of the Parameter
	 */
	public static void storePage(Page page, IWContext iwc) {
		String storageKey = page.getID();
		String infoKey = storageKey;
		FrameStorageInfo info = new FrameStorageInfo(storageKey, page
				.getClass());
		iwc.setSessionAttribute(infoKey, info);
		iwc.setSessionAttributeWeak(storageKey, page);
	}

	/**
	 *@param page
	 *            The new topPage value
	 */
	public static void setTopPage(Page page) {
		IWCoreServlet.storeObject(IW_PAGE_KEY, page);
	}

	/**
	 *@param iwc
	 *            Description of the Parameter
	 *@return The requestingTopPage value
	 */
	public static boolean isRequestingTopPage(IWContext iwc) {
		return !iwc.isParameterSet(IW_FRAME_STORAGE_PARMETER);
	}

	/**
	 * Sets the ID (BuilderPage ID)
	 * 
	 *@param id
	 *            The new pageID value
	 */
	public void setPageID(int id) {
		this._ibPageID = id;
	}

	/**
	 * method for adding a style sheet file the url generating is done in the
	 * main method
	 * 
	 * @param file
	 *            The new styleSheet value
	 */
	public void setStyleSheet(ICFile file) {
		this.styleFile = file;
	}

	/**
	 * Returns set the (BuilderPage) ID set to this page
	 * 
	 *@return The pageID value
	 */
	public int getPageID() {
		return this._ibPageID;
	}

	/**
	 * Sets this page to be a template page
	 */
	public void setIsTemplate() {
		this._isTemplate = true;
		this._isPage = false;
		this._isDraft = false;
	}

	/**
	 * Sets this page to be a "normal" page
	 */
	public void setIsPage() {
		this._isTemplate = false;
		this._isPage = true;
		this._isDraft = false;
	}

	/**
	 * Sets this page to be a draft
	 */
	public void setIsDraft() {
		this._isTemplate = false;
		this._isPage = false;
		this._isDraft = true;
	}

	/**
	 *@return The isTemplate value
	 */
	public boolean getIsTemplate() {
		return (this._isTemplate);
	}

	/**
	 *@return The isPage value
	 */
	public boolean getIsPage() {
		return (this._isPage);
	}

	/**
	 *@return The isDraft value
	 */
	public boolean getIsDraft() {
		return (this._isDraft);
	}

	/**
	 */
	public void setIsExtendingTemplate() {
		this._isExtendingTemplate = true;
	}

	/**
	 *@return The isExtendingTemplate value
	 */
	public boolean getIsExtendingTemplate() {
		return (this._isExtendingTemplate);
	}

	/**
	 * Sets the windowToOpenOnLoad attribute of the Page object
	 * 
	 *@param link
	 *            The new windowToOpenOnLoad value
	 *@param iwc
	 *            The new windowToOpenOnLoad value
	 */
	public void setWindowToOpenOnLoad(Link link, IWContext iwc) {
		this.setOnLoad(link.getWindowToOpenCallingScript(iwc));
	}

	public void setWindowToOpenOnLoad(ICPage page) {
		setWindowToOpenOnLoad(page, 800, 600);
	}

	public void setWindowToOpenOnLoad(ICPage page, int width, int height) {
		this._windowToOpenOnLoad = page;
		this._windowWidth = width;
		this._windowHeight = height;
	}

	/**
	 * Sets the templateId attribute of the Page object
	 * 
	 *@param id
	 *            The new templateId value
	 */
	public void setTemplateId(String id) {
		this._templateId = id;
	}

	/**
	 * Gets the templateId attribute of the Page object
	 * 
	 *@return The templateId value
	 */
	public String getTemplateId() {
		return (this._templateId);
	}

	/**
	 * Used to add source of scriptfiles (JavaScript) The file url should end on
	 * the form "scriptfile.js"
	 * 
	 *@param jsString
	 *            The feature to be added to the ScriptSource attribute
	 */
	public void addScriptSource(String jsString) {
		getAssociatedScript().addScriptSource(jsString);
	}

	/**
	 * Gets the file id of the shortcut icon
	 * 
	 * @return the shortcut icon file id
	 */
	public int getShortCutIconID() {
		return this._shortCutIconID;
	}

	/**
	 * Gets the URL of the shortcut icon
	 * 
	 * @return URL to shortcut icon
	 */
	public String getShortCutIconURL() {
		return this._shortCutIconURL;
	}

	/**
	 * Sets the file id of the shortcut icon
	 * 
	 * @param id
	 *            of the icon file
	 */
	public void setShortCutIconID(int id) {
		this._shortCutIconID = id;
	}

	/**
	 * Sets the URL to the shortcut icon
	 * 
	 * @param url
	 *            to the icon file
	 */
	public void setShortCutIconURL(String url) {
		this._shortCutIconURL = url;
	}

	private String getPrintableSchortCutIconURL(IWContext iwc) {
		String url = null;
		if (getShortCutIconID() > 0) {
			ICFileSystem fsystem;
			try {
				fsystem = getICFileSystem(iwc);
				url = fsystem.getFileURI(getShortCutIconID());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else if (getShortCutIconURL() != null) {
			url = getShortCutIconURL();
		}
		if (url != null) {
			return "<link type=\"shortcut icon\" href=\"" + url + "\" />";
		}
		return "";
		// <link rel="shortcut icon" href="/favicon.ico">
	}

	public ICDynamicPageTrigger getDynamicPageTrigger() {
		if (this.dynamicPageTrigger == null) {
			this.dynamicPageTrigger = (ICDynamicPageTrigger) ImplementorRepository
					.getInstance().newInstanceOrNull(
							ICDynamicPageTrigger.class, this.getClass());
			if (this.dynamicPageTrigger == null) {
				throw new RuntimeException(
						"[Page] Implementation of ICDynamicPageTrigger could not be found. Implementing bundle was not loaded.");
			}
		}
		return this.dynamicPageTrigger;
	}

	/**
	 * Returns the associatedBodyScript.
	 * 
	 * @return Script
	 */
	public Script getAssociatedBodyScript() {
		Script associatedBodyScript = (Script) getFacets().get(
				"page_associated_body_script");
		if (associatedBodyScript == null) {
			associatedBodyScript = new Script();
			setAssociatedBodyScript(associatedBodyScript);
		}
		return associatedBodyScript;
	}

	/**
	 * Sets the associatedScript.
	 * 
	 * @param associatedScript
	 *            The associatedScript to set
	 */
	public void setAssociatedBodyScript(Script script) {
		// this.associatedBodyScript = script;
		getFacets().put("page_associated_body_script", script);
	}

	/**
	 * Set the docType for the header of the page. Default it is set to Html
	 * 4.0.1. transitional. Most commonn doctypes are defined in the static
	 * contsants DOCTYPE_... in this class.
	 * 
	 * @param docType
	 */
	public void setDoctype(String docType) {
		this.docType = docType;
	}

	/**
	 * Get the set docType. If no doctype/markupLanguage is set in the
	 * page/system then this method returns the HTML 4.0.1 Transitional.
	 * 
	 * @return
	 */
	public String getDocType() {
		if (this.docType == null) {
			String markup = getSetApplicationMarkupLanguage();
			if (markup.equals(XHTML)) {
				return DOCTYPE_XHTML_1_0_TRANSITIONAL;
			} else if (markup.equals(XHTML1_1)) {
				return DOCTYPE_XHTML_1_1;
			} else {
				return DOCTYPE_HTML_4_0_1_TRANSITIONAL;
			}
		} else {
			return this.docType;
		}
	}

	/**
	 * Checks if an XHTML doctype is defined for the page or the system.
	 * 
	 * @return True if an XHTML doctype has been set for the document or XHTML
	 *         markup for the application.
	 */
	public boolean isXHtmlDocTypeDeclared() {
		String docType = getDocType();
		if (docType.equals(DOCTYPE_XHTML_1_0_TRANSITIONAL)) {
			return true;
		} else if (docType.equals(DOCTYPE_XHTML_1_1)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets if the Markup Language for the Page. This method uses the set
	 * DocType (if any) to calculate the used MarkupLanguage String.
	 * 
	 * @return
	 */
	public String getMarkupLanguageForPage() {
		if (this.docType != null) {
			if (this.docType.equals(DOCTYPE_XHTML_1_0_TRANSITIONAL)) {
				return XHTML;
			} else if (this.docType.equals(DOCTYPE_XHTML_1_1)) {
				return XHTML1_1;
			}
		}
		return getSetApplicationMarkupLanguage();
	}

	/**
	 * Add javascript urls to page HEAD, comma separated
	 * 
	 * @param urls
	 */
	public void setJavascriptURLs(String urls) {
		if (urls != null) {
			int index = urls.indexOf(",");
			while (index > -1) {
				String tmp = urls.substring(0, index);
				addJavascriptURL(tmp.trim());
				urls = urls.substring(index + 1);
				index = urls.indexOf(",");
			}
			addJavascriptURL(urls.trim());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.faces.component.StateHolder#restoreState(javax.faces.context.
	 * FacesContext, java.lang.Object)
	 */
	public void restoreState(FacesContext context, Object state) {
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		this._ibPageID = ((Integer) values[1]).intValue();
		this._title = (String) values[2];
		this._zeroWait = ((Boolean) values[3]).booleanValue();
		this._redirectSecondInterval = ((Integer) values[4]).intValue();
		this._redirectURL = (String) values[5];
		this._redirectInfo = (String) values[6];
		this._redirectURL = (String) values[7];
		this._doReload = ((Boolean) values[8]).booleanValue();
		this._linkColor = (String) values[9];
		this._visitedColor = (String) values[10];
		this._hoverColor = (String) values[11];
		this._textDecoration = (String) values[12];
		this._hoverDecoration = (String) values[13];
		this._pageStyleFont = (String) values[14];
		this._pageStyleFontSize = (String) values[15];
		this._pageStyleFontStyle = (String) values[16];
		this._shortCutIconURL = (String) values[17];
		this._shortCutIconID = ((Integer) values[18]).intValue();
		this._addStyleSheet = ((Boolean) values[19]).booleanValue();
		this._addBody = ((Boolean) values[20]).booleanValue();
		this._frameProperties = (Map) values[21];
		this._isTemplate = ((Boolean) values[22]).booleanValue();
		this._isPage = ((Boolean) values[23]).booleanValue();
		this._isDraft = ((Boolean) values[24]).booleanValue();
		this._isExtendingTemplate = ((Boolean) values[25]).booleanValue();
		this._templateId = (String) values[26];
		this._styleDefinitions = (Map) values[27];
		this._metaTags = (Map) values[28];
		this._styleSheets = (QueueMap) values[29];
		this._javascripts = (QueueMap) values[30];
		this._javascriptStringsBeforeJSUrls = (QueueMap) values[31];
		this._javascriptStringsAfterJSUrls = (QueueMap) values[32];
		this._HTTPEquivs = (Map) values[33];
		this._localizationMap = (Map) values[34];
		this.addGlobalScript = ((Boolean) values[35]).booleanValue();
		this.styleFile = (ICFile) values[36];
		// this.dynamicPageTrigger=(ICDynamicPageTrigger)values[37];
		this._isCategory = ((Boolean) values[38]).booleanValue();
		this._windowToOpenOnLoad = (ICPage) values[39];
		this._windowWidth = ((Integer) values[40]).intValue();
		this._windowHeight = ((Integer) values[41]).intValue();
		this.forwardPage = (ICPage) values[42];
		this.docType = (String) values[43];
		this.useIE7Extension = ((Boolean) values[44]).booleanValue();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.component.StateHolder#saveState(javax.faces.context.FacesContext
	 * )
	 */
	public Object saveState(FacesContext context) {
		Object values[] = new Object[45];
		values[0] = super.saveState(context);
		values[1] = new Integer(this._ibPageID);
		values[2] = this._title;
		values[3] = Boolean.valueOf(this._zeroWait);
		values[4] = new Integer(this._redirectSecondInterval);
		values[5] = this._redirectURL;
		values[6] = this._redirectInfo;
		values[7] = this._redirectURL;
		values[8] = Boolean.valueOf(this._doReload);
		values[9] = this._linkColor;
		values[10] = this._visitedColor;
		values[11] = this._hoverColor;
		values[12] = this._textDecoration;
		values[13] = this._hoverDecoration;
		values[14] = this._pageStyleFont;
		values[15] = this._pageStyleFontSize;
		values[16] = this._pageStyleFontStyle;
		values[17] = this._shortCutIconURL;
		values[18] = new Integer(this._shortCutIconID);
		values[19] = Boolean.valueOf(this._addStyleSheet);
		values[20] = Boolean.valueOf(this._addBody);
		values[21] = this._frameProperties;
		values[22] = Boolean.valueOf(this._isTemplate);
		values[23] = Boolean.valueOf(this._isPage);
		values[24] = Boolean.valueOf(this._isDraft);
		values[25] = Boolean.valueOf(this._isExtendingTemplate);
		values[26] = this._templateId;
		values[27] = this._styleDefinitions;
		values[28] = this._metaTags;
		values[29] = this._styleSheets;
		values[30] = this._javascripts;
		values[31] = this._javascriptStringsBeforeJSUrls;
		values[32] = this._javascriptStringsAfterJSUrls;
		values[33] = this._HTTPEquivs;
		values[34] = this._localizationMap;
		values[35] = Boolean.valueOf(this.addGlobalScript);
		values[36] = this.styleFile;
		// values[37]=this.dynamicPageTrigger;
		values[38] = Boolean.valueOf(this._isCategory);
		values[39] = this._windowToOpenOnLoad;
		values[40] = new Integer(this._windowWidth);
		values[41] = new Integer(this._windowHeight);
		values[42] = this.forwardPage;
		values[43] = this.docType;
		values[44] = Boolean.valueOf(this.useIE7Extension);
		return values;
	}

	public List getPropertyDescriptions() {
		List list = new ArrayList();
		list.add(new PropertyDescription(
				"method:1:implied:void:setStyleSheetURL:java.lang.String:",
				"1", File.class.getName(), FileObjectReader.class.getName(),
				false));
		list.add(new PropertyDescription(
				":method:1:implied:void:setTemplateId:java.lang.String:", "1",
				ICPage.class.getName(), ICPage.class.getName(), true));
		return list;
	}

}