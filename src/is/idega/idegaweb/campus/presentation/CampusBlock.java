/*
 * Created on Dec 19, 2003
 *
 */
package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.application.business.ApplicationService;
import is.idega.idegaweb.campus.business.CampusService;
import is.idega.idegaweb.campus.business.CampusSettings;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.idega.block.building.business.BuildingService;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;

/**
 * CampusBlock
 * @author aron 
 * @version 1.0
 */
public abstract class CampusBlock extends Block {
	
	private IWResourceBundle iwrb = null;
	private IWBundle iwb = null;
	
	private String _width = "600";
	
	public final static String IW_BUNDLE_IDENTIFIER =CampusSettings.IW_BUNDLE_IDENTIFIER;
	
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
	
	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_SMALL_TEXT = "SmallText";
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
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	public void setResourceBundle(IWResourceBundle iwrb) {
		this.iwrb = iwrb;
	}
		
	public void _main(IWContext iwc)throws Exception{
		this.setResourceBundle(getResourceBundle(iwc));
		iwb = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
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

	public Text getSmallErrorText(String s) {
		return getStyleText(s, STYLENAME_SMALL_ERROR_TEXT);
	}

	public InterfaceObject getStyledInterface(InterfaceObject obj) {
		return (InterfaceObject) setStyle(obj, STYLENAME_INTERFACE);
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
	
	protected DatePicker getDatePicker(String name,Locale locale,Date date){
		DatePicker picker = new DatePicker(name,null,locale,date);
		picker.setStyleClass(STYLENAME_INTERFACE);
		return picker;
	}
	
	protected TextInput getTextInput(String name,String content,int length){
		TextInput input = new TextInput(name,content);
	    input.setLength(length);
	    return (TextInput) setStyle(input,STYLENAME_INTERFACE);
	}
	
	/**
	 * Returns a formatted text input.
	 * 
	 * @param parameter
	 *                    the form parameter
	 * @param text
	 *                    the text to set
	 *  
	 */
	protected TextInput getTextInput(String parameter, String text) {
		return (TextInput) getStyledInterface(new TextInput(parameter, text));
	}
	/**
	 * Returns a formatted text input.
	 * 
	 * @param parameter
	 *                    the form parameter
	 * 
	 *  
	 */
	protected TextInput getTextInput(String parameter) {
		return (TextInput) getStyledInterface(new TextInput(parameter));
	}
	
	protected TextArea getTextArea(String name,String content,int columns,int rows){
		TextArea area = new TextArea(name,content,columns,rows);
		area.setStyleAttribute(STYLENAME_INTERFACE);
		return area;
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
		GenericButton button = getSubmitButton(parameterName,null);
		return button;
	}
	
	//TODO: Rename this method getSubmitButton!
	protected GenericButton getSubmitButton(String parameterName,String parameterValue,String display,String display_key){
		GenericButton button=null;
		if(parameterValue==null){
			button = getButton(new SubmitButton(parameterName,localize(display_key,display)));
		}
		else{
			button = getButton(new SubmitButton(localize(display_key,display),parameterName,parameterValue));
		}
		return button;
	}
	
	protected GenericButton getSubmitButton(String parameterName,String parameterValue){
		return getSubmitButton(parameterName,parameterValue,"Submit",LOCALIZATION_SUBMIT_KEY);
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
	
	
	/**
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { STYLENAME_TEXT, STYLENAME_SMALL_TEXT, STYLENAME_HEADER, STYLENAME_SMALL_HEADER, STYLENAME_LINK, STYLENAME_LIST_HEADER, STYLENAME_LIST_TEXT, STYLENAME_LIST_LINK, STYLENAME_ERROR_TEXT, STYLENAME_SMALL_ERROR_TEXT, STYLENAME_INTERFACE, STYLENAME_SMALL_LINK, STYLENAME_SMALL_LINK+":hover", STYLENAME_TEMPLATE_LINK, STYLENAME_TEMPLATE_LINK+":hover", STYLENAME_TEMPLATE_HEADER, STYLENAME_TEMPLATE_SMALL_HEADER, STYLENAME_TEMPLATE_LINK_SELECTED, STYLENAME_TEMPLATE_LINK_SELECTED+":hover", STYLENAME_TEMPLATE_SUBLINK, STYLENAME_TEMPLATE_SUBLINK+":hover", STYLENAME_TEMPLATE_SUBLINK_SELECTED, STYLENAME_TEMPLATE_SUBLINK_SELECTED+":hover", STYLENAME_TEMPLATE_HEADER_LINK, STYLENAME_TEMPLATE_HEADER_LINK+":hover", STYLENAME_TEMPLATE_LINK2, STYLENAME_TEMPLATE_LINK2+":hover", STYLENAME_TEMPLATE_LINK3, STYLENAME_TEMPLATE_LINK3+":hover", STYLENAME_CHECKBOX, STYLENAME_INTERFACE_BUTTON, STYLENAME_SMALL_HEADER_LINK, STYLENAME_SMALL_HEADER_LINK+":hover" };
		String[] styleValues = { DEFAULT_TEXT_FONT_STYLE, DEFAULT_SMALL_TEXT_FONT_STYLE, DEFAULT_HEADER_FONT_STYLE, DEFAULT_SMALL_HEADER_FONT_STYLE, DEFAULT_LINK_FONT_STYLE, DEFAULT_LIST_HEADER_FONT_STYLE, DEFAULT_LIST_FONT_STYLE, DEFAULT_LIST_LINK_FONT_STYLE, DEFAULT_ERROR_TEXT_FONT_STYLE, DEFAULT_SMALL_ERROR_TEXT_FONT_STYLE, DEFAULT_INTERFACE_STYLE, DEFAULT_SMALL_LINK_FONT_STYLE, DEFAULT_SMALL_LINK_FONT_STYLE_HOVER, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", DEFAULT_CHECKBOX_STYLE, DEFAULT_INTERFACE_BUTTON_STYLE, DEFAULT_SMALL_HEADER_LINK_FONT_STYLE, DEFAULT_SMALL_HEADER_LINK_FONT_STYLE_HOVER };

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
	
	public DataTable getDataTable(){
		DataTable table = new DataTable();
		return table;
	}
	
	public boolean isAdministrator(IWContext iwc) {
		try {
			if (iwc.hasEditPermission(this))
				return true;
			
			//User user = iwc.getCurrentUser();
			//if (user != null)
				//return getUserBusiness(iwc).isRootCommuneAdministrator(user);
			return false;
		}
		catch (Exception re) {
			return false;
		}
	}
	
	public PresentationObject getNoAccessObject(IWContext iwc){
		return getErrorText(localize("restricted_zone","Restricted zone"));
	}
	
	public CampusService getCampusService(IWContext iwc)throws RemoteException{
		return (CampusService)IBOLookup.getServiceInstance(iwc,CampusService.class);
	}
	
	public ApplicationService getApplicationService(IWContext iwc)throws RemoteException{
		return (ApplicationService)IBOLookup.getServiceInstance(iwc,ApplicationService.class);
	}
	
	public BuildingService getBuildingService(IWContext iwc)throws RemoteException{
		return (BuildingService)IBOLookup.getServiceInstance(iwc,BuildingService.class);
	}
	
	public CampusSettings getCampusSettings(IWContext iwc)throws RemoteException{
		return getCampusService(iwc).getCampusSettings();
	}
	
	public UserBusiness getUserService(IWContext iwc)throws RemoteException{
		return (UserBusiness) IBOLookup.getServiceInstance(iwc,UserBusiness.class);
	}
	
	public ContractService getContractService(IWContext iwc)throws RemoteException{
		return (ContractService) IBOLookup.getServiceInstance(iwc,ContractService.class);
	}
	
	public NumberFormat getCurrencyFormat(){
		return java.text.NumberFormat.getCurrencyInstance(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale());
	}
	
	public Text getAmountText(double amount){
		return getText(getCurrencyFormat().format(amount));
	}
}
