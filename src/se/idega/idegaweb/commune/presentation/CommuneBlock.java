package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.business.IBOLookup;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InputContainer;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class CommuneBlock extends com.idega.presentation.Block {
	
	protected static String LOCALIZATION_SAVE_KEY="save";
	protected static String PARAM_SAVE="cb_save";
	protected static String LOCALIZATION_CANCEL_KEY="cancel";
	protected static String PARAM_CANCEL="cb_cancel";
	protected static String LOCALIZATION_EDIT_KEY="edit";
	protected static String PARAM_EDIT="cb_edit";
	protected static String LOCALIZATION_DELETE_KEY="delete";
	protected static String PARAM_DELETE="cb_delete";
	protected static String LOCALIZATION_COPY_KEY="copy";
	protected static String PARAM_COPY="cb_copy";	
	protected static String LOCALIZATION_CREATE_KEY="create";
	protected static String PARAM_CREATE="cb_create";	
	protected static String LOCALIZATION_CLOSE_KEY="close";
	protected static String PARAM_CLOSE="cb_close";	
	protected static String LOCALIZATION_SUBMIT_KEY="submit";
	protected static String PARAM_SUBMIT="cb_submit";	
	protected static String LOCALIZATION_RESET_KEY="reset";

	public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

	public final static String STYLENAME_HEADING_ROW = "HeadingRow";
	public final static String STYLENAME_HEADER_ROW = "HeaderRow";
	public final static String STYLENAME_HEADER_ROW2 = "HeaderRow2";
	public final static String STYLENAME_LIGHT_ROW = "LightRow";
	public final static String STYLENAME_DARK_ROW = "DarkRow";
	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_SMALL_TEXT = "SmallText";
	public final static String STYLENAME_BIG_HEADER = "BigHeader";
	public final static String STYLENAME_HEADER = "Header";
	public final static String STYLENAME_SMALL_HEADER = "SmallHeader";
	public final static String STYLENAME_SMALL_HEADER_LINK = "SmallHeaderLink";
	public final static String STYLENAME_LINK = "Link";
	public final static String STYLENAME_SMALL_LINK = "SmallLink";
	public final static String STYLENAME_LIST_HEADER = "ListHeader";
	public final static String STYLENAME_LIST_TEXT = "ListText";
	public final static String STYLENAME_LIST_LINK = "ListLink";
	public final static String STYLENAME_ERROR_TEXT = "ErrorText";
	public final static String STYLENAME_SMALL_ERROR_TEXT = "SmallErrorText";
	public final static String STYLENAME_INTERFACE = "Interface";
	public final static String STYLENAME_INTERFACE_BUTTON = "InterfaceButton";
	public final static String STYLENAME_CHECKBOX = "CheckBox";
	private final static String STYLENAME_TEMPLATE_LINK = "TemplateLink";
	private final static String STYLENAME_TEMPLATE_LINK2 = "TemplateLink2";
	private final static String STYLENAME_TEMPLATE_LINK3 = "TemplateLink3";
	private final static String STYLENAME_TEMPLATE_LINK_SELECTED = "TemplateSelectedLink";
	private final static String STYLENAME_TEMPLATE_SUBLINK = "TemplateSubLink";
	private final static String STYLENAME_TEMPLATE_SUBLINK_SELECTED = "TemplateSelectedSubLink";
	private final static String STYLENAME_TEMPLATE_HEADER = "TemplateHeader";
	private final static String STYLENAME_TEMPLATE_HEADER_LINK = "TemplateHeaderLink";
	private final static String STYLENAME_TEMPLATE_SMALL_HEADER = "TemplateSmallHeader";

	private final static String DEFAULT_BACKGROUND_COLOR = "#ffffff";
	private final static String DEFAULT_HEADER_COLOR = "#d0daea";
	private final static String DEFAULT_ZEBRA_COLOR_1 = "#ffffff";
	private final static String DEFAULT_ZEBRA_COLOR_2 = "#f4f4f4";
	private final static String DEFAULT_TEXT_FONT_STYLE = "font-weight:plain;";
	private final static String DEFAULT_SMALL_TEXT_FONT_STYLE = "font-style:normal;color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
	private final static String DEFAULT_HEADER_FONT_STYLE = "font-weight:bold;";
	private final static String DEFAULT_SMALL_HEADER_FONT_STYLE = "font-style:normal;color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	private final static String DEFAULT_LINK_FONT_STYLE = "color:#0000cc;";
	private final static String DEFAULT_SMALL_LINK_FONT_STYLE = "font-style:normal;color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
	private final static String DEFAULT_SMALL_LINK_FONT_STYLE_HOVER = "font-style:normal;color:#CCCCCC;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
	private final static String DEFAULT_LIST_HEADER_FONT_STYLE = "font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	private final static String DEFAULT_LIST_FONT_STYLE = "font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
	private final static String DEFAULT_LIST_LINK_FONT_STYLE = "font-style:normal;color:#0000cc;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
	private final static String DEFAULT_ERROR_TEXT_FONT_STYLE = "font-weight:plain;color:#ff0000;";
	private final static String DEFAULT_SMALL_ERROR_TEXT_FONT_STYLE = "font-style:normal;color:#ff0000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
	private final static String DEFAULT_INTERFACE_STYLE = "color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
	private final static String DEFAULT_CHECKBOX_STYLE = "margin:0px;padding:0px;height:12px;width:12px;";
	private final static String DEFAULT_INTERFACE_BUTTON_STYLE = "color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
	private final static String DEFAULT_SMALL_HEADER_LINK_FONT_STYLE = "font-style:normal;color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	private final static String DEFAULT_SMALL_HEADER_LINK_FONT_STYLE_HOVER = "font-style:normal;color:#CCCCCC;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";

	private String backgroundColor = DEFAULT_BACKGROUND_COLOR;
	private String textFontStyle = DEFAULT_TEXT_FONT_STYLE;
	private String smallTextFontStyle = DEFAULT_SMALL_TEXT_FONT_STYLE;
	private String linkFontStyle = DEFAULT_LINK_FONT_STYLE;
	private String headerFontStyle = DEFAULT_HEADER_FONT_STYLE;
	private String smallHeaderFontStyle = DEFAULT_SMALL_HEADER_FONT_STYLE;
	private String listHeaderFontStyle = DEFAULT_LIST_HEADER_FONT_STYLE;
	private String listFontStyle = DEFAULT_LIST_FONT_STYLE;
	private String listLinkFontStyle = DEFAULT_LIST_LINK_FONT_STYLE;
	private String errorTextFontStyle = DEFAULT_ERROR_TEXT_FONT_STYLE;
	private String smallErrorTextFontStyle = DEFAULT_SMALL_ERROR_TEXT_FONT_STYLE;
	
	private final static String HEADER_COLOR_PROPERTY = "header_color";
	private final static String ZEBRA_COLOR1_PROPERTY = "zebra_color_1";
	private final static String ZEBRA_COLOR2_PROPERTY = "zebra_color_2";
	private final static String CELLPADDING_PROPERTY = "cellpadding";
	private final static String CELLSPACING_PROPERTY = "cellspacing";

	private IWResourceBundle iwrb = null;
	private IWBundle iwb = null;
	private ICPage formResponsePage;
	private ICPage formBackPage;
	private boolean iUseStyleNames;

	private static String _width = "600";
	//private String _width = iwb.getProperty("table.width"); 

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
		_width = iwb.getProperty("table.width", "600"); 
		iUseStyleNames = new Boolean(iwb.getProperty("layout.use_style_names", "false")).booleanValue();
		super._main(iwc);
	}
	
	public IWResourceBundle getResourceBundle() {
		return this.iwrb;
	}

	public IWBundle getBundle() {
		return this.iwb;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public String getTextFontStyle() {
		return textFontStyle;
	}

	public String getSmallTextFontStyle() {
		return smallTextFontStyle;
	}

	public String getLinkFontStyle() {
		return linkFontStyle;
	}

	public String getHeaderFontStyle() {
		return headerFontStyle;
	}

	public String getSmallHeaderFontStyle() {
		return smallHeaderFontStyle;
	}

	public String getListHeaderFontStyle() {
		return listHeaderFontStyle;
	}

	public String getListFontStyle() {
		return listFontStyle;
	}

	public String getListLinkFontStyle() {
		return listLinkFontStyle;
	}

	public String getErrorTextFontStyle() {
		return errorTextFontStyle;
	}

	public String getSmallErrorTextFontStyle() {
		return smallErrorTextFontStyle;
	}

	public void setBackroundColor(String color) {
		this.backgroundColor = color;
	}

	public void setTextFontStyle(String fontStyle) {
		this.textFontStyle = fontStyle;
	}

	public void setSmallTextFontStyle(String fontStyle) {
		this.smallTextFontStyle = fontStyle;
	}

	public void setLinkFontStyle(String fontStyle) {
		this.linkFontStyle = fontStyle;
	}

	public void setHeaderFontStyle(String fontStyle) {
		this.headerFontStyle = fontStyle;
	}

	public void setSmallHeaderFontStyle(String fontStyle) {
		this.smallHeaderFontStyle = fontStyle;
	}

	public void setListHeaderFontStyle(String fontStyle) {
		this.listHeaderFontStyle = fontStyle;
	}

	public void setListFontStyle(String fontStyle) {
		this.listFontStyle = fontStyle;
	}

	public void setListLinkFontStyle(String fontStyle) {
		this.listLinkFontStyle = fontStyle;
	}

	public void setErrorTextFontStyle(String fontStyle) {
		this.errorTextFontStyle = fontStyle;
	}

	public void setSmallErrorTextFontStyle(String fontStyle) {
		this.smallErrorTextFontStyle = fontStyle;
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

	public Text getSmallText(String s) {
		return getStyleText(s, STYLENAME_SMALL_TEXT);
	}

	public Text getLocalizedSmallText(String s, String d) {
		return getSmallText(localize(s, d));
	}

	public Text getHeader(String s) {
		return getStyleText(s, STYLENAME_HEADER);
	}

	public Text getLocalizedHeader(String s, String d) {
		return getHeader(localize(s, d));
	}

	public Text getSmallHeader(String s) {
		return getStyleText(s, STYLENAME_SMALL_HEADER);
	}

	public Link getSmallHeaderLink(String s) {
		return getStyleLink(new Link(s), STYLENAME_SMALL_HEADER_LINK);
	}

	public Text getLocalizedSmallHeader(String s, String d) {
		return getSmallHeader(localize(s, d));
	}

	public Link getLocalizedSmallHeaderLink(String s, String d) {
		return getSmallHeaderLink(localize(s, d));
	}

	public Link getLink(String s) {
		return getStyleLink(new Link(s), STYLENAME_LINK);
	}
	
	public Link getSmallLink(String link) {
		return getStyleLink(new Link(link), STYLENAME_SMALL_LINK);
	}

	public Link getLocalizedLink(String s, String d) {
		return getLink(localize(s, d));
	}

	public Text getErrorText(String s) {
		return getStyleText(s, STYLENAME_ERROR_TEXT);
	}

	public Text getBigHeader(String s) {
		return getStyleText(s, STYLENAME_BIG_HEADER);
	}

	public Text getSmallErrorText(String s) {
		return getStyleText(s, STYLENAME_SMALL_ERROR_TEXT);
	}

	public InterfaceObject getStyledInterface(InterfaceObject obj) {
		return (InterfaceObject) setStyle(obj, STYLENAME_INTERFACE);
	}
	
	public ICPage getBackPage() {
		return this.formBackPage;
	}
	
	public boolean useStyleNames() {
		return iUseStyleNames;
	}
	
	public void setBackPage(ICPage page) {
		this.formBackPage = page;
	}

	public ICPage getResponsePage() {
		return this.formResponsePage;
	}
	
	public void setResponsePage(ICPage page) {
		this.formResponsePage = page;
	}
	
	public String getHeaderColor() {
		return getProperty(HEADER_COLOR_PROPERTY,DEFAULT_HEADER_COLOR);
	}
	
	public String getZebraColor1() {
		return getProperty(ZEBRA_COLOR1_PROPERTY,DEFAULT_ZEBRA_COLOR_1);
	}
	
	public String getZebraColor2() {
		return getProperty(ZEBRA_COLOR2_PROPERTY,DEFAULT_ZEBRA_COLOR_2);
	}
	
	protected int getCellpadding() {
		return Integer.parseInt(getProperty(CELLPADDING_PROPERTY,"2"));	
	}
	
	protected int getCellspacing() {
		return Integer.parseInt(getProperty(CELLSPACING_PROPERTY,"2"));	
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
	
	protected CheckBox getCheckBox(String name, String value) {
		return (CheckBox) setStyle(new CheckBox(name,value),STYLENAME_CHECKBOX);
	}
	
	protected RadioButton getRadioButton(String name, String value) {
		return (RadioButton) setStyle(new RadioButton(name,value),STYLENAME_CHECKBOX);
	}
	
	protected GenericButton getButton(GenericButton button) {
		//temporary, will be moved to IWStyleManager for handling...
		button.setHeight("20");
		return (GenericButton) setStyle(button,STYLENAME_INTERFACE_BUTTON);
	}
	
	protected GenericButton getSaveButton(){
		return getSaveButton(PARAM_SAVE);
	}	

	protected GenericButton getSaveButton(String parameterName){
		GenericButton button = getButton(new SubmitButton(parameterName,localize(LOCALIZATION_SAVE_KEY,"Save")));
		return button;
	}
	
	protected GenericButton getCancelButton(){
		return getCancelButton(PARAM_CANCEL);
	}	

	protected GenericButton getCancelButton(String parameterName){
		GenericButton button = getButton(new SubmitButton(parameterName,localize(LOCALIZATION_CANCEL_KEY,"Cancel")));
		return button;
	}
	
	protected GenericButton getEditButton(){
		return getEditButton(PARAM_EDIT);
	}	

	protected GenericButton getEditButton(String parameterName){
		GenericButton button = getButton(new SubmitButton(parameterName,localize(LOCALIZATION_EDIT_KEY,"Edit")));
		return button;
	}
	
	protected GenericButton getDeleteButton(){
		return getDeleteButton(PARAM_DELETE);
	}	

	protected GenericButton getDeleteButton(String parameterName){
		GenericButton button = getButton(new SubmitButton(parameterName,localize(LOCALIZATION_DELETE_KEY,"Delete")));
		return button;
	}

	protected GenericButton getCopyButton(){
		return getCopyButton(PARAM_COPY);
	}	

	protected GenericButton getCopyButton(String parameterName){
		GenericButton button = getButton(new SubmitButton(parameterName,localize(LOCALIZATION_COPY_KEY,"Copy")));
		return button;
	}
	
	protected GenericButton getCreateButton(){
		return getCreateButton(PARAM_CREATE);
	}	

	protected GenericButton getCreateButton(String parameterName){
		GenericButton button = getButton(new SubmitButton(parameterName,localize(LOCALIZATION_CREATE_KEY,"Create")));
		return button;
	}
	
	protected GenericButton getSubmitButton(){
		return getSubmitButton(PARAM_SUBMIT);
	}	

	protected GenericButton getSubmitButton(String parameterName){
		GenericButton button = getSubmitButton2(parameterName,null);
		return button;
	}
	
	//TODO: Rename this method getSubmitButton!
	protected GenericButton getSubmitButton2(String parameterName,String parameterValue){
		GenericButton button=null;
		if(parameterValue==null){
			button = getButton(new SubmitButton(parameterName,localize(LOCALIZATION_SUBMIT_KEY,"Submit")));
		}
		else{
			button = getButton(new SubmitButton(localize(LOCALIZATION_SUBMIT_KEY,"Submit"),parameterName,parameterValue));
		}
		return button;
	}
	
	
	protected GenericButton getResetButton(){
		GenericButton button = getButton(new ResetButton(localize(LOCALIZATION_RESET_KEY,"Reset")));
		return button;
	}	

	protected GenericButton getCloseButton(){
		return getCloseButton(PARAM_CLOSE);
	}	

	protected GenericButton getCloseButton(String parameterName){
		GenericButton button = getButton(new SubmitButton(parameterName,localize(LOCALIZATION_CLOSE_KEY,"Close")));
		return button;
	}
		
		
	protected InputContainer getInputContainer(String textKey,PresentationObject inputObject){
		return getInputContainer(textKey,textKey,inputObject);
	}
	
	protected InputContainer getInputContainer(String textKey,String defaultTextValue,PresentationObject inputObject){
		Text tText = this.getLocalizedSmallText(textKey,defaultTextValue);
		InputContainer iCont = new InputContainer(tText,inputObject);
		iCont.setCellWidth(200);
		return iCont;
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

	/**
	 * Returns a copy icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The copy icon.
	 */
	protected Image getCopyIcon(String toolTip) {
		Image copyImage = iwb.getImage("shared/copy.gif", 12, 12);
		copyImage.setToolTip(toolTip);
		return copyImage;
	}

	/**
	 * Returns a question icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The question icon.
	 */
	protected Image getQuestionIcon(String toolTip) {
		Image questionImage = iwb.getImage("shared/question.gif", 12, 12);
		questionImage.setToolTip(toolTip);
		return questionImage;
	}

	/**
	 * Returns an information icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The information icon.
	 */
	protected Image getInformationIcon(String toolTip) {
		Image informationImage = iwb.getImage("shared/info.gif", 12, 12);
		informationImage.setToolTip(toolTip);
		return informationImage;
	}

	/**
	 * Returns the default various icon with the tooltip specified.  May be used for various
	 * purposes (handle, go, whatever...)
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The various icon.
	 */
	protected Image getVariousIcon(String toolTip) {
		return getEditIcon(toolTip);
	}
	
	protected String getHeadingRowClass() {
		return getStyleName(STYLENAME_HEADING_ROW);
	}

	protected String getHeaderRowClass() {
		return getStyleName(STYLENAME_HEADER_ROW);
	}

	protected String getHeaderRow2Class() {
		return getStyleName(STYLENAME_HEADER_ROW2);
	}

	protected String getLightRowClass() {
		return getStyleName(STYLENAME_LIGHT_ROW);
	}

	protected String getDarkRowClass() {
		return getStyleName(STYLENAME_DARK_ROW);
	}

	/**
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { STYLENAME_HEADING_ROW, STYLENAME_HEADER_ROW2, STYLENAME_HEADER_ROW, STYLENAME_LIGHT_ROW, STYLENAME_DARK_ROW, STYLENAME_BIG_HEADER, STYLENAME_TEXT, STYLENAME_SMALL_TEXT, STYLENAME_HEADER, STYLENAME_SMALL_HEADER, STYLENAME_LINK, STYLENAME_LIST_HEADER, STYLENAME_LIST_TEXT, STYLENAME_LIST_LINK, STYLENAME_ERROR_TEXT, STYLENAME_SMALL_ERROR_TEXT, STYLENAME_INTERFACE, STYLENAME_SMALL_LINK, STYLENAME_SMALL_LINK+":hover", STYLENAME_TEMPLATE_LINK, STYLENAME_TEMPLATE_LINK+":hover", STYLENAME_TEMPLATE_HEADER, STYLENAME_TEMPLATE_SMALL_HEADER, STYLENAME_TEMPLATE_LINK_SELECTED, STYLENAME_TEMPLATE_LINK_SELECTED+":hover", STYLENAME_TEMPLATE_SUBLINK, STYLENAME_TEMPLATE_SUBLINK+":hover", STYLENAME_TEMPLATE_SUBLINK_SELECTED, STYLENAME_TEMPLATE_SUBLINK_SELECTED+":hover", STYLENAME_TEMPLATE_HEADER_LINK, STYLENAME_TEMPLATE_HEADER_LINK+":hover", STYLENAME_TEMPLATE_LINK2, STYLENAME_TEMPLATE_LINK2+":hover", STYLENAME_TEMPLATE_LINK3, STYLENAME_TEMPLATE_LINK3+":hover", STYLENAME_CHECKBOX, STYLENAME_INTERFACE_BUTTON, STYLENAME_SMALL_HEADER_LINK, STYLENAME_SMALL_HEADER_LINK+":hover" };
		String[] styleValues = { "", "", "", "", "", "", DEFAULT_TEXT_FONT_STYLE, DEFAULT_SMALL_TEXT_FONT_STYLE, DEFAULT_HEADER_FONT_STYLE, DEFAULT_SMALL_HEADER_FONT_STYLE, DEFAULT_LINK_FONT_STYLE, DEFAULT_LIST_HEADER_FONT_STYLE, DEFAULT_LIST_FONT_STYLE, DEFAULT_LIST_LINK_FONT_STYLE, DEFAULT_ERROR_TEXT_FONT_STYLE, DEFAULT_SMALL_ERROR_TEXT_FONT_STYLE, DEFAULT_INTERFACE_STYLE, DEFAULT_SMALL_LINK_FONT_STYLE, DEFAULT_SMALL_LINK_FONT_STYLE_HOVER, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", DEFAULT_CHECKBOX_STYLE, DEFAULT_INTERFACE_BUTTON_STYLE, DEFAULT_SMALL_HEADER_LINK_FONT_STYLE, DEFAULT_SMALL_HEADER_LINK_FONT_STYLE_HOVER };

		for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}

		return map;
	}

	public void setWidth(String width) {
		_width = width;
	}

	public String getWidth() {
		return _width;
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
	
	private CommuneUserBusiness getUserBusiness(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
	
	
}
