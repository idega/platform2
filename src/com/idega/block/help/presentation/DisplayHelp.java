/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.help.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.FileUtil;
import com.idega.xml.XMLAttribute;
import com.idega.xml.XMLCDATA;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLParser;

import java.io.File;
import java.util.Locale;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class DisplayHelp extends PresentationObjectContainer {
	public static final String HELP_KEY = Help.HELP_KEY;
	public static final String HELP_BUNDLE = Help.HELP_BUNDLE;
	public static final String TITLE_STYLE = HelpTree.TITLE_STYLE;
	public static final String TITLE_CLASS = HelpTree.TITLE_CLASS;
	public static final String BODY_STYLE = HelpTree.BODY_STYLE;
	public static final String BODY_CLASS = HelpTree.BODY_CLASS;
	
	private static final String BUNDLE_IDENTIFIER = "com.idega.block.help";
	
	private static final String HELP_NO_HELP_SELECTED = "hlp_no_help_available";
	
	private static final String DEFAULT_HELP_KEY = "hlp_default_key";
	private static final String DEFAULT_HELP_BUNDLE = "hlp_default_bundle";

	private String _localizedTitle = null;
	private String _localizedHelpText = null;

	private final static String XML_ROOT = "xml";
	private final static String XML_HELP = "help";
	private final static String XML_ID = "id";
	private final static String XML_TITLE = "title";
	private final static String XML_FOLDER = "help";
	private final static String XML_EXTENSION = ".xml";
	private final static String HELP_FILE_PREFIX = "hlp_";
	
	private XMLDocument _doc = null;

	protected String _titleStyleAttribute = null;
	protected String _titleStyleClass = null;
	protected String _bodyStyleAttribute = null;
	protected String _bodyStyleClass = null;
	protected String _linkStyleAttribute = null;
	protected String _linkStyleClass = null;
	protected String _seeAlsoStyleAttribute = null;
	protected String _seeAlsoStyleClass = null;
			
	public void main(IWContext iwc) throws Exception {
		String key = iwc.getParameter(HELP_KEY);
		String bundle = iwc.getParameter(HELP_BUNDLE);
		
		_titleStyleAttribute = iwc.getParameter(TITLE_STYLE);
		_titleStyleClass = iwc.getParameter(TITLE_CLASS);
		_bodyStyleAttribute = iwc.getParameter(BODY_STYLE);
		_bodyStyleClass = iwc.getParameter(BODY_CLASS);
		
		Table t = null;
		int row = 1;
		t = new Table(1, 3);

    Locale loc = iwc.getCurrentLocale();

		/*
		 * Get the default values if either values are null
		 */
		if (key == null || bundle == null) {
			IWBundle help_bundle = getBundle(iwc);
			key = help_bundle.getProperty(DEFAULT_HELP_KEY);
			bundle = help_bundle.getProperty(DEFAULT_HELP_BUNDLE);			
		}

		if (key != null && bundle != null) {
			getHelpText(iwc, key, bundle, loc);
		}

		Text title = null;
		if (_localizedTitle != null) {
			title = new Text(_localizedTitle);
			
      if (_titleStyleAttribute != null)
				title.setStyleAttribute(_titleStyleAttribute);						
			else if (_titleStyleClass != null)
				title.setStyleClass(_titleStyleClass);
  		t.add(title, 1, row++);
		}

		row++;
		
		Text body = null;
		if (_localizedHelpText != null) {
			body = new Text(_localizedHelpText);
			
			if (_bodyStyleAttribute != null)
				body.setStyleAttribute(_bodyStyleAttribute);
			else if (_bodyStyleClass != null)
				body.setStyleClass(_bodyStyleClass);

			t.add(_localizedHelpText, 1, row);
		}
		
		if (_localizedTitle != null || _localizedHelpText != null)				
			add(t);	
		else {			
			add(getResourceBundle(iwc).getLocalizedString(HELP_NO_HELP_SELECTED,"Select help item from the tree"));
		}
	}
	
	private void getHelpText(IWContext iwc, String helpKey, String bundle, Locale loc) {
		_localizedTitle = null;
		_localizedHelpText = null;

		if (_doc == null) 
			loadHelpText(iwc,helpKey,bundle,loc);

		XMLElement root = _doc.getRootElement();
		if (root == null)
			return;
			
		XMLElement help = root.getChild(XML_HELP);
		if (help == null)
			return;
			
		XMLAttribute id = help.getAttribute(XML_ID);
		if (id == null || !id.getValue().equals(helpKey))
			return;

		XMLElement title = help.getChild(XML_TITLE);
		if (title != null) {
			String tmp = title.getText();
			_localizedTitle = title.getTextTrim();
		}
			
		XMLCDATA cdata = help.getXMLCDATAContent();
		if (cdata != null) 
			_localizedHelpText = cdata.getText();		
	}	
	
	private void loadHelpText(IWContext iwc, String helpKey, String bundle, Locale loc) {
		try {
			IWBundle iwb = null;
			if (bundle == null)
				iwb = iwc.getApplication().getBundle(HELP_BUNDLE);
			else
				iwb = iwc.getApplication().getBundle(bundle);
				
			XMLParser parser = new XMLParser(false);
			StringBuffer fileName = new StringBuffer(iwb.getResourcesRealPath(loc));
			if (!fileName.toString().endsWith(FileUtil.getFileSeparator()))
				fileName.append(FileUtil.getFileSeparator());

			fileName.append(XML_FOLDER);
			File file = new File(fileName.toString());
			file.mkdir();

			fileName.append(FileUtil.getFileSeparator());
			fileName.append(HELP_FILE_PREFIX);
			fileName.append(helpKey);
			fileName.append(XML_EXTENSION);
			
			file = new File(fileName.toString());
			file.createNewFile();

			_doc = parser.parse(file);
		}
		catch (Exception e) {
			_doc = new XMLDocument(new XMLElement(XML_ROOT));
		}
	}	
	
	public void setTitleStyleAttribute(String styleAttribute) {
		_titleStyleAttribute = styleAttribute;
	}
	
	public void setTitleStyleClass(String styleClass) {
		_titleStyleClass = styleClass;	
	}
	
	public void setBodyStyleAttribute(String styleAttribute) {
		_bodyStyleAttribute = styleAttribute;		
	}
	
	public void setBodyStyleClass(String styleClass) {
		_bodyStyleClass = styleClass;			
	}
	
	public void setLinkStyleAttribute(String styleAttribute) {
		_linkStyleAttribute = styleAttribute;
	}
	
	public void setLinkStyleClass(String styleClass) {
		_linkStyleClass = styleClass;			
	}
	
	public void setSeeAlsoStyleAttribute(String styleAttribute) {
		_seeAlsoStyleAttribute = styleAttribute;		
	}
	
	public void setSeeAlsoStyleClass(String styleClass) {
		_seeAlsoStyleClass = styleClass;			
	}	
	
	public String getBundleIdentifier() {
		return BUNDLE_IDENTIFIER;
	}	
}