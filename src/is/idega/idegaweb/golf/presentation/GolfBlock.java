/*
 * Created on 4.3.2004
 */
package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.block.login.business.AccessControl;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InputContainer;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.database.ConnectionBroker;

/**
 * @author laddi
 */
public abstract class GolfBlock extends Block {
	
	protected static String LOCALIZATION_SAVE_KEY="save";
	protected static String PARAM_SAVE="go_save";
	protected static String LOCALIZATION_CANCEL_KEY="cancel";
	protected static String PARAM_CANCEL="go_cancel";
	protected static String LOCALIZATION_EDIT_KEY="edit";
	protected static String PARAM_EDIT="go_edit";
	protected static String LOCALIZATION_DELETE_KEY="delete";
	protected static String PARAM_DELETE="go_delete";
	protected static String LOCALIZATION_COPY_KEY="copy";
	protected static String PARAM_COPY="go_copy";	
	protected static String LOCALIZATION_CREATE_KEY="create";
	protected static String PARAM_CREATE="go_create";	
	protected static String LOCALIZATION_CLOSE_KEY="close";
	protected static String PARAM_CLOSE="go_close";	
	protected static String LOCALIZATION_SUBMIT_KEY="submit";
	protected static String PARAM_SUBMIT="go_submit";	
	protected static String LOCALIZATION_RESET_KEY="reset";
	
	private static final String LIGHT_ROW_STYLE = "lightRow";
	private static final String DARK_ROW_STYLE = "darkRow";
	private static final String HEADER_ROW_STYLE = "headerRow";
	private static final String BIG_ROW_STYLE = "bigRow";
	private static final String LOGIN_ROW_STYLE = "loginRow";

	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_BIG_TEXT = "BigText";
	public final static String STYLENAME_SMALL_TEXT = "SmallText";
	public final static String STYLENAME_SMALL_BOLD_TEXT = "SmallBoldText";
	public final static String STYLENAME_SMALL_BOLD_BLACK_TEXT = "SmallBoldBlackText";
	public final static String STYLENAME_SMALL_BLACK_TEXT = "SmallBlackText";
	public final static String STYLENAME_HEADER = "Header";
	public final static String STYLENAME_BIG_HEADER = "BigHeader";
	public final static String STYLENAME_SMALL_HEADER = "SmallHeader";
	public final static String STYLENAME_SMALL_HEADER_LINK = "SmallHeaderLink";
	public final static String STYLENAME_MESSAGE = "Message";
	public final static String STYLENAME_LINK = "Link";
	public final static String STYLENAME_SMALL_LINK = "SmallLink";
	public final static String STYLENAME_LIST_HEADER = "ListHeader";
	public final static String STYLENAME_LIST_TEXT = "ListText";
	public final static String STYLENAME_LIST_LINK = "ListLink";
	public final static String STYLENAME_ERROR_TEXT = "ErrorText";
	public final static String STYLENAME_SMALL_ERROR_TEXT = "SmallErrorText";
	public final static String STYLENAME_INTERFACE = "Interface";
	public final static String STYLENAME_SMALL_INTERFACE = "SmallInterface";
	public final static String STYLENAME_INTERFACE_BUTTON = "InterfaceButton";
	public final static String STYLENAME_CHECKBOX = "CheckBox";
	public final static String STYLENAME_TEMPLATE_LINK = "TemplateLink";
	public final static String STYLENAME_TEMPLATE_LINK2 = "TemplateLink2";
	public final static String STYLENAME_TEMPLATE_LINK3 = "TemplateLink3";
	private final static String STYLENAME_TEMPLATE_LINK_SELECTED = "TemplateSelectedLink";
	private final static String STYLENAME_TEMPLATE_SUBLINK = "TemplateSubLink";
	private final static String STYLENAME_TEMPLATE_SUBLINK_SELECTED = "TemplateSelectedSubLink";
	private final static String STYLENAME_TEMPLATE_HEADER = "TemplateHeader";
	public final static String STYLENAME_TEMPLATE_HEADER2 = "TemplateHeader2";
	public final static String STYLENAME_TEMPLATE_HEADER_LINK = "TemplateHeaderLink";
	public final static String STYLENAME_TEMPLATE_HEADER_LINK2 = "TemplateHeaderLink2";
	public final static String STYLENAME_TEMPLATE_SMALL_HEADER = "TemplateSmallHeader";

	private final static String DEFAULT_BACKGROUND_COLOR = "#ffffff";
	private final static String DEFAULT_HEADER_COLOR = "#3C532A";
	private final static String DEFAULT_LINE_COLOR = "#B2B2B2";
	private final static String DEFAULT_ZEBRA_COLOR_1 = "#ffffff";
	private final static String DEFAULT_ZEBRA_COLOR_2 = "#DEE4D5";
	private final static String DEFAULT_BIG_TEXT_FONT_STYLE = "font-weight:bold;font-size:14px;";
	private final static String DEFAULT_TEXT_FONT_STYLE = "font-weight:plain;";
	private final static String DEFAULT_SMALL_TEXT_FONT_STYLE = "font-style:normal;color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
	private final static String DEFAULT_SMALL_BOLD_TEXT_FONT_STYLE = "font-style:normal;color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	private final static String DEFAULT_BIG_HEADER_FONT_STYLE = "font-weight:bold;font-size:14px;";
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
	private final static String DEFAULT_SMALL_INTERFACE_STYLE = "height:10px;color:#000000;font-size:8px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
	private final static String DEFAULT_CHECKBOX_STYLE = "margin:0px;padding:0px;height:12px;width:12px;";
	private final static String DEFAULT_INTERFACE_BUTTON_STYLE = "color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:normal;border-width:1px;border-style:solid;border-color:#000000;";
	private final static String DEFAULT_SMALL_HEADER_LINK_FONT_STYLE = "font-style:normal;color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	private final static String DEFAULT_SMALL_HEADER_LINK_FONT_STYLE_HOVER = "font-style:normal;color:#CCCCCC;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
	private static final String DEFAULT_LOGIN_ROW_STYLE = "padding:5px;border-bottom-width:1px;border-bottom-style:solid;border-bottom-color:"+DEFAULT_LINE_COLOR+";";

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
	private final static String LINE_COLOR_PROPERTY = "line_color";
	private final static String ZEBRA_COLOR1_PROPERTY = "zebra_color_1";
	private final static String ZEBRA_COLOR2_PROPERTY = "zebra_color_2";
	private final static String CELLPADDING_PROPERTY = "cellpadding";
	private final static String CELLSPACING_PROPERTY = "cellspacing";


  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";
  
  private IWResourceBundle _iwrb;
  private IWBundle _iwb;
  
  private boolean _isAdmin = false;
  private boolean _isClubWorker = false;
  private boolean _isClubAdmin = false;
  private boolean _isDeveloper = false;
  private boolean _isUser = false;
  
  private Member _member;

	public void _main(IWContext modinfo) throws Exception {
		_iwrb = getResourceBundle(modinfo);
		_iwb = getBundle(modinfo);
		
		_isAdmin = isAdmin(modinfo);
		_isClubWorker = isClubWorker(modinfo);
		_isClubAdmin = isClubAdmin(modinfo);
		_isDeveloper = isDeveloper(modinfo);
		_isUser = isUser(modinfo);
		
		_member = getMember(modinfo);

		if (modinfo.isParameterSet("union_id")) {
		  modinfo.setSessionAttribute("golf_union_id",modinfo.getParameter("union_id"));
		}

		super._main(modinfo);
	}
	
  /**
   * @return The default DatabaseConnection
   */
  public Connection getConnection() {
    return ConnectionBroker.getConnection();
  }

  public void freeConnection(Connection conn) {
    ConnectionBroker.freeConnection(conn);
  }

  public IWResourceBundle getResourceBundle() {
		return _iwrb;
	}
	
	public IWBundle getBundle() {
		return _iwb;
	}
	
	public abstract void main(IWContext modinfo) throws Exception;
  
  public Member getMember(IWContext modinfo){
		return (Member) modinfo.getSessionAttribute("member_login");
	}

  private boolean isAdmin(IWContext modinfo) {
    try {
      return AccessControl.isAdmin(modinfo);
    }
    catch(SQLException E) {
    	return false;
    }
  }

  private boolean isClubAdmin(IWContext modinfo) {
    return AccessControl.isClubAdmin(modinfo);
  }

  private boolean isClubWorker(IWContext modinfo) {
    try {
      return AccessControl.isClubWorker(modinfo);
    }
    catch(java.sql.SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public String getUnionID(IWContext modinfo){
    return (String)modinfo.getSessionAttribute("golf_union_id");
  }

  public void removeUnionIdSessionAttribute(IWContext modinfo){
    modinfo.removeSessionAttribute("golf_union_id");
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
	/**
	 * @return Returns the isAdmin.
	 */
	public boolean isAdmin() {
		return this._isAdmin;
	}
	/**
	 * @return Returns the isClubAdmin.
	 */
	public boolean isClubAdmin() {
		return this._isClubAdmin;
	}
	/**
	 * @return Returns the isClubWorker.
	 */
	public boolean isClubWorker() {
		return this._isClubWorker;
	}
	/**
	 * @return Returns the isDeveloper.
	 */
	public boolean isDeveloper() {
		return this._isDeveloper;
	}
	/**
	 * @return Returns the isUser.
	 */
	public boolean isUser() {
		return this._isUser;
	}
	/**
	 * @return Returns the member.
	 */
	public Member getMember() {
		return this._member;
	}
	
	
	
	
	
/**
 * Style related methods begin
 */
	
	
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
		if (_iwrb == null) {
			return defaultText;
		}
		return _iwrb.getLocalizedString(textKey, defaultText);
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
	
	public Text getBigText(String s) {
		return getStyleText(s, STYLENAME_BIG_TEXT);
	}

	public Text getBigHeader(String s) {
		return getStyleText(s, STYLENAME_BIG_HEADER);
	}
	
	public Text getLocalizedText(String s, String d) {
		return getText(localize(s, d));
	}

	public Text getSmallText(String s) {
		return getStyleText(s, STYLENAME_SMALL_TEXT);
	}

	public Text getSmallBoldText(String s) {
		return getStyleText(s, STYLENAME_SMALL_BOLD_TEXT);
	}

	public Text getSmallBoldBlackText(String s) {
		return getStyleText(s, STYLENAME_SMALL_BOLD_BLACK_TEXT);
	}

	public Text getSmallBlackText(String s) {
		return getStyleText(s, STYLENAME_SMALL_BLACK_TEXT);
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
	
	public Text getMessageText(String message) {
		return getStyleText(message, STYLENAME_MESSAGE);
	}

	public Text getLocalizedMessage(String key,String defaultText) {
		return getMessageText(localize(key,defaultText));
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
	
	public Link getTemplateHeaderLink(String s) {
		return getStyleLink(new Link(s), STYLENAME_TEMPLATE_HEADER_LINK);
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
	
	public InterfaceObject getStyledSmallInterface(InterfaceObject obj) {
		return (InterfaceObject) setStyle(obj, STYLENAME_SMALL_INTERFACE);
	}
	
	public String getHeaderColor() {
		return getProperty(HEADER_COLOR_PROPERTY,DEFAULT_HEADER_COLOR);
	}
	
	public String getLineSeperatorColor() {
		return getProperty(LINE_COLOR_PROPERTY,DEFAULT_LINE_COLOR);
	}
	
	public String getLoginRowClass() {
		return getStyleName(LOGIN_ROW_STYLE);
	}
	
	public String getLightRowClass() {
		return getStyleName(LIGHT_ROW_STYLE);
	}
	
	public String getDarkRowClass() {
		return getStyleName(DARK_ROW_STYLE);
	}
	
	public String getHeaderRowClass() {
		return getStyleName(HEADER_ROW_STYLE);
	}
	
	public String getBigRowClass() {
		return getStyleName(BIG_ROW_STYLE);
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
		GenericButton button = getSubmitButton(parameterName,null);
		return button;
	}
	
	protected GenericButton getSubmitButton(String parameterName,String parameterValue){
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
		GenericButton button = getButton(new CloseButton(localize(LOCALIZATION_CLOSE_KEY,"Close")));
		return button;
	}	

//	protected GenericButton getCloseButton(String parameterName){
//		GenericButton button = getButton(new SubmitButton(parameterName,localize(LOCALIZATION_CLOSE_KEY,"Close")));
//		return button;
//	}
		
		
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
		Image editImage = _iwb.getImage("shared/edit.gif", 12, 12);
		editImage.setToolTip(toolTip);
		return editImage;
	}

	/**
	 * Returns the default delete icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The delete icon.
	 */
	protected Image getDeleteIcon(String toolTip) {
		Image deleteImage = _iwb.getImage("shared/delete.gif", 12, 12);
		deleteImage.setToolTip(toolTip);
		return deleteImage;
	}

	/**
	 * Returns a PDF icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The PDF icon.
	 */
	protected Image getPDFIcon(String toolTip) {
		Image pdfImage = _iwb.getImage("shared/pdf-small.gif", 12, 12);
		pdfImage.setToolTip(toolTip);
		return pdfImage;
	}

	/**
	 * Returns a copy icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The copy icon.
	 */
	protected Image getCopyIcon(String toolTip) {
		Image copyImage = _iwb.getImage("shared/copy.gif", 12, 12);
		copyImage.setToolTip(toolTip);
		return copyImage;
	}

	/**
	 * Returns a question icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The question icon.
	 */
	protected Image getQuestionIcon(String toolTip) {
		Image questionImage = _iwb.getImage("shared/question.gif", 12, 12);
		questionImage.setToolTip(toolTip);
		return questionImage;
	}

	/**
	 * Returns an information icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The information icon.
	 */
	protected Image getInformationIcon(String toolTip) {
		Image informationImage = _iwb.getImage("shared/info.gif", 12, 12);
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

	/**
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = { STYLENAME_SMALL_BLACK_TEXT, STYLENAME_SMALL_BOLD_BLACK_TEXT, STYLENAME_TEMPLATE_HEADER_LINK2, STYLENAME_TEMPLATE_HEADER_LINK2+":hover", LOGIN_ROW_STYLE, STYLENAME_SMALL_INTERFACE, STYLENAME_TEXT, STYLENAME_BIG_TEXT, STYLENAME_SMALL_TEXT, STYLENAME_SMALL_BOLD_TEXT, STYLENAME_BIG_HEADER, STYLENAME_HEADER, STYLENAME_SMALL_HEADER, STYLENAME_LINK, STYLENAME_LIST_HEADER, STYLENAME_LIST_TEXT, STYLENAME_LIST_LINK, STYLENAME_ERROR_TEXT, STYLENAME_SMALL_ERROR_TEXT, STYLENAME_INTERFACE, STYLENAME_SMALL_LINK, STYLENAME_SMALL_LINK+":hover", STYLENAME_TEMPLATE_LINK, STYLENAME_TEMPLATE_LINK+":hover", STYLENAME_TEMPLATE_HEADER, STYLENAME_TEMPLATE_SMALL_HEADER, STYLENAME_TEMPLATE_LINK_SELECTED, STYLENAME_TEMPLATE_LINK_SELECTED+":hover", STYLENAME_TEMPLATE_SUBLINK, STYLENAME_TEMPLATE_SUBLINK+":hover", STYLENAME_TEMPLATE_SUBLINK_SELECTED, STYLENAME_TEMPLATE_SUBLINK_SELECTED+":hover", STYLENAME_TEMPLATE_HEADER_LINK, STYLENAME_TEMPLATE_HEADER_LINK+":hover", STYLENAME_TEMPLATE_LINK2, STYLENAME_TEMPLATE_LINK2+":hover", STYLENAME_TEMPLATE_LINK3, STYLENAME_TEMPLATE_LINK3+":hover", STYLENAME_CHECKBOX, STYLENAME_INTERFACE_BUTTON, STYLENAME_SMALL_HEADER_LINK, STYLENAME_SMALL_HEADER_LINK+":hover", STYLENAME_TEMPLATE_HEADER2 };
		String[] styleValues = { "", "", "", "", DEFAULT_LOGIN_ROW_STYLE, DEFAULT_SMALL_INTERFACE_STYLE, DEFAULT_TEXT_FONT_STYLE, DEFAULT_BIG_TEXT_FONT_STYLE, DEFAULT_SMALL_TEXT_FONT_STYLE, DEFAULT_SMALL_BOLD_TEXT_FONT_STYLE, DEFAULT_BIG_HEADER_FONT_STYLE, DEFAULT_HEADER_FONT_STYLE, DEFAULT_SMALL_HEADER_FONT_STYLE, DEFAULT_LINK_FONT_STYLE, DEFAULT_LIST_HEADER_FONT_STYLE, DEFAULT_LIST_FONT_STYLE, DEFAULT_LIST_LINK_FONT_STYLE, DEFAULT_ERROR_TEXT_FONT_STYLE, DEFAULT_SMALL_ERROR_TEXT_FONT_STYLE, DEFAULT_INTERFACE_STYLE, DEFAULT_SMALL_LINK_FONT_STYLE, DEFAULT_SMALL_LINK_FONT_STYLE_HOVER, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", DEFAULT_CHECKBOX_STYLE, DEFAULT_INTERFACE_BUTTON_STYLE, DEFAULT_SMALL_HEADER_LINK_FONT_STYLE, DEFAULT_SMALL_HEADER_LINK_FONT_STYLE_HOVER, "" };

		for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}

		return map;
	}

	public void addHeading(String heading) {
		Page p = getParentPage();
		if(p instanceof GolfWindow) {
			((GolfWindow)p).addHeading(heading);
		} else {
			Text tHeading = getBigHeader(heading);
			add(tHeading);
		}
	}
	
	
	
	/**
	 * Style related methods begin
	 */
	
}