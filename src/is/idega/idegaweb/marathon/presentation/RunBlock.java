/*
 * $Id: RunBlock.java,v 1.1 2005/05/24 12:06:29 laddi Exp $
 * Created on May 17, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.util.HashMap;
import java.util.Map;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.RadioButton;


/**
 * Last modified: $Date: 2005/05/24 12:06:29 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class RunBlock extends Block {

	public final static String STYLENAME_HEADER = "Header";
	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_SMALL_TEXT = "SmallText";
	public final static String STYLENAME_LINK = "Link";
	public final static String STYLENAME_INTERFACE = "Interface";
	public final static String STYLENAME_INTERFACE_BUTTON = "InterfaceButton";
	public final static String STYLENAME_CHECKBOX = "CheckBox";

	private IWResourceBundle iwrb = null;
	private IWBundle iwb = null;

	public void _main(IWContext iwc)throws Exception{
		setResourceBundle(getResourceBundle(iwc));
		setBundle(getBundle(iwc));
		super._main(iwc);
	}
	
	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { STYLENAME_HEADER, STYLENAME_TEXT, STYLENAME_SMALL_TEXT, STYLENAME_LINK, STYLENAME_LINK + ":hover", STYLENAME_INTERFACE, STYLENAME_CHECKBOX, STYLENAME_INTERFACE_BUTTON,  };
		String[] styleValues = { "", "", "", "", "", "", "", "" };

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
	
	public Text getHeader(String s) {
		return getStyleText(s, STYLENAME_HEADER);
	}

	public Text getText(String text) {
		return getStyleText(text, STYLENAME_TEXT);
	}

	public Text getSmallText(String text) {
		return getStyleText(text, STYLENAME_SMALL_TEXT);
	}

	public Link getLink(String text) {
		return getStyleLink(new Link(text), STYLENAME_LINK);
	}
	
	public InterfaceObject getStyledInterface(InterfaceObject obj) {
		return (InterfaceObject) setStyle(obj, STYLENAME_INTERFACE);
	}
	
	protected CheckBox getCheckBox(String name, String value) {
		return (CheckBox) setStyle(new CheckBox(name,value),STYLENAME_CHECKBOX);
	}
	
	protected RadioButton getRadioButton(String name, String value) {
		return (RadioButton) setStyle(new RadioButton(name,value),STYLENAME_CHECKBOX);
	}
	
	protected GenericButton getButton(GenericButton button) {
		button.setHeight("20");
		return (GenericButton) setStyle(button,STYLENAME_INTERFACE_BUTTON);
	}

	protected IWBundle getBundle() {
		return iwb;
	}

	protected void setBundle(IWBundle bundle) {
		this.iwb = bundle;
	}

	protected IWResourceBundle getResourceBundle() {
		return iwrb;
	}

	protected void setResourceBundle(IWResourceBundle resourceBundle) {
		this.iwrb = resourceBundle;
	}

	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}
}