/*
 * Created on 20.6.2004
 */
package com.idega.block.school.presentation;

import java.util.HashMap;
import java.util.Map;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.RadioButton;


/**
 * @author laddi
 */
public abstract class SchoolBlock extends Block {

  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

  public final static String STYLENAME_HEADER_ROW = "HeaderRow";
	public final static String STYLENAME_LIGHT_ROW = "LightRow";
	public final static String STYLENAME_DARK_ROW = "DarkRow";

	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_SMALL_TEXT = "SmallText";
	public final static String STYLENAME_HEADER = "Header";
	public final static String STYLENAME_SMALL_HEADER = "SmallHeader";
	public final static String STYLENAME_LINK = "Link";
	public final static String STYLENAME_SMALL_LINK = "SmallLink";

	public final static String STYLENAME_INTERFACE = "Interface";
	public final static String STYLENAME_INTERFACE_BUTTON = "InterfaceButton";
	public final static String STYLENAME_CHECKBOX = "CheckBox";

	private IWBundle iwb;
	private IWResourceBundle iwrb;
	private SchoolBusiness business;
	
	public void main(IWContext iwc) throws Exception {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		business = getSchoolBusiness(iwc);
		init(iwc);
	}
	
	protected abstract void init(IWContext iwc) throws Exception;
	
	/**
	 * @see com.idega.presentation.Block#getBundleIdentifier()
	 */
  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
  
	/**
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
  public Map getStyleNames() {
  		Map map = new HashMap();
  		map.put(STYLENAME_HEADER_ROW, "");
  		map.put(STYLENAME_LIGHT_ROW, "");
  		map.put(STYLENAME_DARK_ROW, "");
  		
  		map.put(STYLENAME_TEXT, "");
  		map.put(STYLENAME_SMALL_TEXT, "");
  		map.put(STYLENAME_HEADER, "");
  		map.put(STYLENAME_SMALL_HEADER, "");
  		map.put(STYLENAME_LINK, "");
  		map.put(STYLENAME_SMALL_LINK, "");
  		
  		map.put(STYLENAME_INTERFACE, "");
  		map.put(STYLENAME_INTERFACE_BUTTON, "");
  		map.put(STYLENAME_CHECKBOX, "");

  		return map;
  }

  private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
	
	public String localize(String textKey, String defaultText) {
		if (iwrb == null) {
			return defaultText;
		}
		return iwrb.getLocalizedString(textKey, defaultText);
	}
	
	protected String getHeaderRowClass() {
		return getStyleName(STYLENAME_HEADER_ROW);
	}

	protected String getLightRowClass() {
		return getStyleName(STYLENAME_LIGHT_ROW);
	}

	protected String getDarkRowClass() {
		return getStyleName(STYLENAME_DARK_ROW);
	}

	protected Text getText(String text) {
		if (text == null) {
			text = "-";
		}
		return getStyleText(text, STYLENAME_TEXT);
	}

	protected Text getSmallText(String text) {
		if (text == null) {
			text = "-";
		}
		return getStyleText(text, STYLENAME_SMALL_TEXT);
	}

	protected Text getHeader(String text) {
		if (text == null) {
			text = "-";
		}
		return getStyleText(text, STYLENAME_HEADER);
	}

	protected Text getSmallHeader(String text) {
		if (text == null) {
			text = "-";
		}
		return getStyleText(text, STYLENAME_SMALL_HEADER);
	}

	protected Link getLink(String text) {
		if (text == null) {
			text = "-";
		}
		return getStyleLink(text, STYLENAME_LINK);
	}

	protected Link getSmallLink(String text) {
		if (text == null) {
			text = "-";
		}
		return getStyleLink(text, STYLENAME_SMALL_LINK);
	}

	protected InterfaceObject getStyledInterface(InterfaceObject obj) {
		return (InterfaceObject) setStyle(obj, STYLENAME_INTERFACE);
	}
	
	protected GenericButton getButton(GenericButton button) {
		return (GenericButton) setStyle(button,STYLENAME_INTERFACE_BUTTON);
	}
	
	protected CheckBox getCheckBox(String name, String value) {
		return (CheckBox) setStyle(new CheckBox(name,value),STYLENAME_CHECKBOX);
	}
	
	protected RadioButton getRadioButton(String name, String value) {
		return (RadioButton) setStyle(new RadioButton(name,value),STYLENAME_CHECKBOX);
	}
	
	/**
	 * Returns the default edit icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The edit icon.
	 */
	protected Image getEditIcon(String toolTip) {
		Image editImage = iwb.getImage("shared/edit.gif", 12, 12);
		editImage.setToolTip(toolTip);
		return editImage;
	}

	/**
	 * Returns the default delete icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The delete icon.
	 */
	protected Image getDeleteIcon(String toolTip) {
		Image deleteImage = iwb.getImage("shared/delete.gif", 12, 12);
		deleteImage.setToolTip(toolTip);
		return deleteImage;
	}

	protected SchoolBusiness getBusiness() {
		return business;
	}
	
	protected IWBundle getBundle() {
		return iwb;
	}
	
	protected IWResourceBundle getResourceBundle() {
		return iwrb;
	}
}