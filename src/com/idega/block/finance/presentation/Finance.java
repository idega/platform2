package com.idega.block.finance.presentation;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.idega.block.category.presentation.CategoryBlock;
import com.idega.block.finance.business.FinanceException;
import com.idega.block.finance.business.FinanceService;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.CollectionNavigator;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * Title: idegaclasses Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class Finance extends CategoryBlock implements Builderaware {
	public static final String CATEGORY_TYPE = "Finance";
	protected static String LOCALIZATION_SAVE_KEY = "save";
	protected static String PARAM_SAVE = "cb_save";
	protected static String LOCALIZATION_CANCEL_KEY = "cancel";
	protected static String PARAM_CANCEL = "cb_cancel";
	protected static String LOCALIZATION_EDIT_KEY = "edit";
	protected static String PARAM_EDIT = "cb_edit";
	protected static String LOCALIZATION_DELETE_KEY = "delete";
	protected static String PARAM_DELETE = "cb_delete";
	protected static String LOCALIZATION_COPY_KEY = "copy";
	protected static String PARAM_COPY = "cb_copy";
	protected static String LOCALIZATION_CREATE_KEY = "create";
	protected static String PARAM_CREATE = "cb_create";
	protected static String LOCALIZATION_CLOSE_KEY = "close";
	protected static String PARAM_CLOSE = "cb_close";
	protected static String LOCALIZATION_SUBMIT_KEY = "submit";
	protected static String PARAM_SUBMIT = "cb_submit";
	protected static String LOCALIZATION_RESET_KEY = "reset";
	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_NEGATIVE_AMOUNT = "NegativeAmount";
	public final static String STYLENAME_POSITIVE_AMOUNT = "PositiveAmount";
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
	private final static String DEFAULT_NEG_AMNT_FONT_STYLE = "font-weight:bold;color:#FF0000;";
	private final static String DEFAULT_POS_AMNT_FONT_STYLE = "font-weight:bold;color:#207b22;";
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
	private String amountNegativeFontStyle = DEFAULT_TEXT_FONT_STYLE;
	private String amountPositiveFontStyle = DEFAULT_TEXT_FONT_STYLE;
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
	protected final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4, ACT5 = 5;
	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.finance";
	public final static String CATEGORY_PROPERTY = "finance_category";
	protected boolean isAdmin = false;
	protected IWResourceBundle iwrb;
	protected IWBundle iwb, core;
	boolean newobjinst = false;
	boolean administrative = true;
	private List FinanceObjects = null;
	public final static String FRAME_NAME = "fin_frame";
	public static final String prmFinanceClass = "fin_clss";
	public static final String prmAccountId = "fin_acc_id";
	public int iCategoryId = -1;
	
	private FinanceService financeService = null;
	private int collectionIndex = 0;
	private int collectionSize = 0;
	private int collectionViewSize = 10;
	private Form form = null;
	private Table table = null;
	//public static final String prmCategoryId = "fin_cat_id";
	
	public Finance() {
		setAutoCreate(false);
		setWidth("600");
	}
	
	public Object clone() {
		Finance obj = null;
		try {
			obj = (Finance) super.clone();
			obj.form = this.form;
			obj.table = this.table;
			obj.FinanceObjects = FinanceObjects;
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}
	
	/**
	 * Sets a localized title for this application form.
	 * The title will appear at the top of the application form.
	 * @param textkey the text key for the title
	 * @param defaultText the default localized text for the title
	 */
	public void setLocalizedTitle(String textKey, String defaultText) {
		table.add(getHeader(localize(textKey, defaultText)), 1, 2);
		table.setRowColor(2, getHeaderColor());
		table.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_CENTER);
	}
	
	public void setTitle(String text) {
		table.add(getHeader(text), 1, 2);
		table.setRowColor(2, getHeaderColor());
		table.setAlignment(1, 2, Table.HORIZONTAL_ALIGN_CENTER);
	}
	
	/**
	 * Sets the info panel for this application form.
	 * The info panel will appear below title in the application form.
	 * @param infoPanel the table containing the search panel
	 */
	public void setInfoPanel(PresentationObject searchPanel) {
		table.add(searchPanel, 1, 3);
	}

	/**
	 * Sets the search panel for this application form.
	 * The search panel will appear below info in the application form.
	 * @param searchPanel the table containing the search panel
	 */
	public void setSearchPanel(PresentationObject searchPanel) {
		table.add(searchPanel, 1, 4);
	}
	
	public void setTabPanel(PresentationObject tabPanel) {
		table.add(tabPanel, 1, 1);
	}

	/**
	 * Sets the main panel for this application form.
	 * The main panel will appear below the search panel in the application form.
	 * @param mainPanel the presentation object containing the main panel
	 */
	public void setMainPanel(PresentationObject mainPanel) {
		table.add(mainPanel, 1, 5);
	}

	/**
	 * Sets the button panel for this application form.
	 * The button panel will appear at the bottom of the application form.
	 * @param buttonPanel the button panel to set
	 * @see ButtonPanel
	 */
	public void setButtonPanel(PresentationObject buttonPanel) {
		table.add(buttonPanel, 1, 7);
	}
	
	public void setNavigationPanel(PresentationObject navPanel) {
		table.add(navPanel, 1, 6);
	}
	
	/**
	 * Adds a hidden input to this application form.
	 * @param parameter the hidden input parameter name
	 * @param value the hidden input parameter va?ue
	 */
	public void addHiddenInput(String parameter, String value) {
		table.add(new HiddenInput(parameter, value), 1, 4);
	}
	
	/**
	 * Maintains the specified parameter in the form request
	 * @param parameterName
	 */
	public void maintainParameter(String parameterName){
		if(this.form!=null)
			this.form.maintainParameter(parameterName);
	}
	public boolean getMultible() {
		return false;
	}
	public String getCategoryType() {
		return CATEGORY_TYPE;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public String getTextFontStyle() {
		return textFontStyle;
	}
	
	public String getAmountNegativeFontStyle() {
		return amountNegativeFontStyle;
	}
	public String getAmountPositiveFontStyle() {
		return amountPositiveFontStyle;
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
	public void setAmountNegativeFontStyle(String fontStyle) {
		this.amountNegativeFontStyle = fontStyle;
	}
	public void setAmountPositiveFontStyle(String fontStyle) {
		this.amountPositiveFontStyle = fontStyle;
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
		return getProperty(HEADER_COLOR_PROPERTY, DEFAULT_HEADER_COLOR);
	}
	public String getZebraColor1() {
		return getProperty(ZEBRA_COLOR1_PROPERTY, DEFAULT_ZEBRA_COLOR_1);
	}
	public String getZebraColor2() {
		return getProperty(ZEBRA_COLOR2_PROPERTY, DEFAULT_ZEBRA_COLOR_2);
	}
	protected int getCellpadding() {
		return Integer.parseInt(getProperty(CELLPADDING_PROPERTY, "2"));
	}
	protected int getCellspacing() {
		return Integer.parseInt(getProperty(CELLSPACING_PROPERTY, "2"));
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
		return (CheckBox) setStyle(new CheckBox(name, value), STYLENAME_CHECKBOX);
	}
	protected RadioButton getRadioButton(String name, String value) {
		return (RadioButton) setStyle(new RadioButton(name, value), STYLENAME_CHECKBOX);
	}
	protected GenericButton getButton(GenericButton button) {
		//temporary, will be moved to IWStyleManager for handling...
		button.setHeight("20");
		return (GenericButton) setStyle(button, STYLENAME_INTERFACE_BUTTON);
	}
	protected GenericButton getSaveButton() {
		return getSaveButton(PARAM_SAVE);
	}
	protected GenericButton getSaveButton(String parameterName) {
		GenericButton button = getButton(new SubmitButton(parameterName, localize(LOCALIZATION_SAVE_KEY, "Save")));
		return button;
	}
	protected GenericButton getCancelButton() {
		return getCancelButton(PARAM_CANCEL);
	}
	protected GenericButton getCancelButton(String parameterName) {
		GenericButton button = getButton(new SubmitButton(parameterName, localize(LOCALIZATION_CANCEL_KEY, "Cancel")));
		return button;
	}
	protected GenericButton getEditButton() {
		return getEditButton(PARAM_EDIT);
	}
	protected GenericButton getEditButton(String parameterName) {
		GenericButton button = getButton(new SubmitButton(parameterName, localize(LOCALIZATION_EDIT_KEY, "Edit")));
		return button;
	}
	protected GenericButton getDeleteButton() {
		return getDeleteButton(PARAM_DELETE);
	}
	protected GenericButton getDeleteButton(String parameterName) {
		GenericButton button = getButton(new SubmitButton(parameterName, localize(LOCALIZATION_DELETE_KEY, "Delete")));
		return button;
	}
	protected GenericButton getCopyButton() {
		return getCopyButton(PARAM_COPY);
	}
	protected GenericButton getCopyButton(String parameterName) {
		GenericButton button = getButton(new SubmitButton(parameterName, localize(LOCALIZATION_COPY_KEY, "Copy")));
		return button;
	}
	protected GenericButton getCreateButton() {
		return getCreateButton(PARAM_CREATE);
	}
	protected GenericButton getCreateButton(String parameterName) {
		GenericButton button = getButton(new SubmitButton(parameterName, localize(LOCALIZATION_CREATE_KEY, "Create")));
		return button;
	}
	protected GenericButton getSubmitButton() {
		return getSubmitButton(PARAM_SUBMIT);
	}
	protected GenericButton getSubmitButton(String parameterName) {
		GenericButton button = getSubmitButton2(parameterName, null);
		return button;
	}
	//TODO: Rename this method getSubmitButton!
	protected GenericButton getSubmitButton2(String parameterName, String parameterValue) {
		GenericButton button = null;
		if (parameterValue == null) {
			button = getButton(new SubmitButton(parameterName, localize(LOCALIZATION_SUBMIT_KEY, "Submit")));
		} else {
			button = getButton(new SubmitButton(localize(LOCALIZATION_SUBMIT_KEY, "Submit"), parameterName,
					parameterValue));
		}
		return button;
	}
	protected GenericButton getResetButton() {
		GenericButton button = getButton(new ResetButton(localize(LOCALIZATION_RESET_KEY, "Reset")));
		return button;
	}
	protected GenericButton getCloseButton() {
		return getCloseButton(PARAM_CLOSE);
	}
	protected GenericButton getCloseButton(String parameterName) {
		GenericButton button = getButton(new SubmitButton(parameterName, localize(LOCALIZATION_CLOSE_KEY, "Close")));
		return button;
	}
	protected Image getEditIcon(String toolTip) {
		Image editImage = iwb.getImage("shared/edit.gif", 12, 12);
		editImage.setToolTip(toolTip);
		return editImage;
	}
	/**
	 * Returns the default delete icon with the tooltip specified.
	 * 
	 * @param toolTip
	 *                    The tooltip to display on mouse over.
	 * @return Image The delete icon.
	 */
	protected Image getDeleteIcon(String toolTip) {
		Image deleteImage = iwb.getImage("shared/delete.gif", 12, 12);
		deleteImage.setToolTip(toolTip);
		return deleteImage;
	}
	/**
	 * Returns a PDF icon with the tooltip specified.
	 * 
	 * @param toolTip
	 *                    The tooltip to display on mouse over.
	 * @return Image The PDF icon.
	 */
	protected Image getPDFIcon(String toolTip) {
		Image pdfImage = iwb.getImage("shared/pdf-small.gif", 12, 12);
		pdfImage.setToolTip(toolTip);
		return pdfImage;
	}
	/**
	 * Returns a copy icon with the tooltip specified.
	 * 
	 * @param toolTip
	 *                    The tooltip to display on mouse over.
	 * @return Image The copy icon.
	 */
	protected Image getCopyIcon(String toolTip) {
		Image copyImage = iwb.getImage("shared/copy.gif", 12, 12);
		copyImage.setToolTip(toolTip);
		return copyImage;
	}
	/**
	 * Returns a question icon with the tooltip specified.
	 * 
	 * @param toolTip
	 *                    The tooltip to display on mouse over.
	 * @return Image The question icon.
	 */
	protected Image getQuestionIcon(String toolTip) {
		Image questionImage = iwb.getImage("shared/question.gif", 12, 12);
		questionImage.setToolTip(toolTip);
		return questionImage;
	}
	/**
	 * Returns an information icon with the tooltip specified.
	 * 
	 * @param toolTip
	 *                    The tooltip to display on mouse over.
	 * @return Image The information icon.
	 */
	protected Image getInformationIcon(String toolTip) {
		Image informationImage = iwb.getImage("shared/info.gif", 12, 12);
		informationImage.setToolTip(toolTip);
		return informationImage;
	}
	/**
	 * Returns the default various icon with the tooltip specified. May be used
	 * for various purposes (handle, go, whatever...)
	 * 
	 * @param toolTip
	 *                    The tooltip to display on mouse over.
	 * @return Image The various icon.
	 */
	protected Image getVariousIcon(String toolTip) {
		return getEditIcon(toolTip);
	}
	/**
	 * Gets the common number format for the current locale
	 */
	public NumberFormat getNumberFormat(Locale locale) {
		return NumberFormat.getInstance(locale);
	}
	/**
	 * Gets the common short date format for the given locale
	 */
	public DateFormat getShortDateFormat(Locale locale) {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale);
	}
	/**
	 * Gets the common long date format for the given locale
	 */
	public DateFormat getLongDateFormat(Locale locale) {
		return DateFormat.getDateInstance(DateFormat.LONG, locale);
	}
	/**
	 * Gets the common date-time-format for the given locale
	 */
	public DateFormat getDateTimeFormat(Locale locale) {
		return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
	}
	
	public NumberFormat getCurrencyFormat(){
		return java.text.NumberFormat.getCurrencyInstance(this.getIWApplicationContext().getApplicationSettings().getDefaultLocale());
	}
	
	public Text getAmountText(double amount){
		return getStyleText(getCurrencyFormat().format(amount),amount>=0?STYLENAME_POSITIVE_AMOUNT:STYLENAME_NEGATIVE_AMOUNT);
	}
	/**
	 * Returns a formatted and localized form label.
	 * 
	 * @param textKey
	 *                    the text key to localize
	 * @param defaultText
	 *                    the default localized text
	 *  
	 */
	protected Text getLocalizedLabel(String textKey, String defaultText) {
		return getSmallHeader(localize(textKey, defaultText) + ":");
	}
	/**
	 * Returns a formatted and localized exception text.
	 * 
	 * @param ex
	 *                    AccountingException to localize
	 *  
	 */
	public Text getLocalizedException(FinanceException ex) {
		return getErrorText(localize(ex.getTextKey(), ex.getDefaultText()));
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
	/**
	 * Returns a formatted text input with the specified width.
	 * 
	 * @param parameter
	 *                    the form parameter
	 * @param text
	 *                    the text to set
	 * @param width
	 *                    the width of the text input
	 *  
	 */
	protected TextInput getTextInput(String parameter, String text, int width) {
		TextInput ti = getTextInput(parameter, "" + text);
		ti.setWidth("" + width);
		return ti;
	}
	/**
	 * Returns a formatted text input with the specified width and size.
	 * 
	 * @param parameter
	 *                    the form parameter
	 * @param text
	 *                    the text to set
	 * @param width
	 *                    the width of the text input
	 * @param size
	 *                    the number of character in the text input
	 *  
	 */
	protected TextInput getTextInput(String parameter, String text, int width, int size) {
		TextInput ti = getTextInput(parameter, text, width);
		ti.setSize(width);
		ti.setMaxlength(size);
		return ti;
	}
	/**
	 * Returns a formatted link.
	 * 
	 * @param text
	 *                    the link text
	 * @param parameter
	 *                    the form parameter
	 * @param value
	 *                    the parameter value
	 *  
	 */
	protected Link getLink(String text, String parameter, String value) {
		Link l = getSmallLink(text);
		l.addParameter(parameter, value);
		return l;
	}
	/**
	 * Returns a formatted and localized button.
	 * 
	 * @param parameter
	 *                    the form parameter
	 * @param textKey
	 *                    the text key to localize
	 * @param defaultText
	 *                    the default localized text
	 *  
	 */
	protected SubmitButton getLocalizedButton(String parameter, String textKey, String defaultText) {
		return getSubmitButton(new SubmitButton(parameter, localize(textKey, defaultText)));
	}
	/**
	 * Sets the style for the specified button.
	 * 
	 * @param button
	 *                    the submit button to stylize
	 *  
	 */
	protected SubmitButton getSubmitButton(SubmitButton button) {
		button.setHeight("20");
		return (SubmitButton) setStyle(button, STYLENAME_INTERFACE_BUTTON);
	}
	/**
	 * Formats a float to two decimals and the current Locale's decimal symbol
	 * 
	 * @param amount
	 * @return
	 */
	public String formatCurrency(float amount) {
		NumberFormat currenyFormat = NumberFormat.getInstance();
		currenyFormat.setMinimumFractionDigits(2);
		currenyFormat.setMaximumFractionDigits(2);
		currenyFormat.setGroupingUsed(true);
		return currenyFormat.format(amount);
	}
	/**
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		HashMap map = new HashMap();
		String[] styleNames = {STYLENAME_TEXT, STYLENAME_SMALL_TEXT, STYLENAME_HEADER, STYLENAME_SMALL_HEADER,
				STYLENAME_LINK, STYLENAME_LIST_HEADER, STYLENAME_LIST_TEXT, STYLENAME_LIST_LINK, STYLENAME_ERROR_TEXT,
				STYLENAME_SMALL_ERROR_TEXT, STYLENAME_INTERFACE, STYLENAME_SMALL_LINK, STYLENAME_SMALL_LINK + ":hover",
				STYLENAME_TEMPLATE_LINK, STYLENAME_TEMPLATE_LINK + ":hover", STYLENAME_TEMPLATE_HEADER,
				STYLENAME_TEMPLATE_SMALL_HEADER, STYLENAME_TEMPLATE_LINK_SELECTED,
				STYLENAME_TEMPLATE_LINK_SELECTED + ":hover", STYLENAME_TEMPLATE_SUBLINK,
				STYLENAME_TEMPLATE_SUBLINK + ":hover", STYLENAME_TEMPLATE_SUBLINK_SELECTED,
				STYLENAME_TEMPLATE_SUBLINK_SELECTED + ":hover", STYLENAME_TEMPLATE_HEADER_LINK,
				STYLENAME_TEMPLATE_HEADER_LINK + ":hover", STYLENAME_TEMPLATE_LINK2,
				STYLENAME_TEMPLATE_LINK2 + ":hover", STYLENAME_TEMPLATE_LINK3, STYLENAME_TEMPLATE_LINK3 + ":hover",
				STYLENAME_CHECKBOX, STYLENAME_INTERFACE_BUTTON, STYLENAME_SMALL_HEADER_LINK,
				STYLENAME_SMALL_HEADER_LINK + ":hover",
				STYLENAME_NEGATIVE_AMOUNT,
				STYLENAME_POSITIVE_AMOUNT};
		String[] styleValues = {DEFAULT_TEXT_FONT_STYLE, DEFAULT_SMALL_TEXT_FONT_STYLE, DEFAULT_HEADER_FONT_STYLE,
				DEFAULT_SMALL_HEADER_FONT_STYLE, DEFAULT_LINK_FONT_STYLE, DEFAULT_LIST_HEADER_FONT_STYLE,
				DEFAULT_LIST_FONT_STYLE, DEFAULT_LIST_LINK_FONT_STYLE, DEFAULT_ERROR_TEXT_FONT_STYLE,
				DEFAULT_SMALL_ERROR_TEXT_FONT_STYLE, DEFAULT_INTERFACE_STYLE, DEFAULT_SMALL_LINK_FONT_STYLE,
				DEFAULT_SMALL_LINK_FONT_STYLE_HOVER, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
				DEFAULT_CHECKBOX_STYLE, DEFAULT_INTERFACE_BUTTON_STYLE, DEFAULT_SMALL_HEADER_LINK_FONT_STYLE,
				DEFAULT_SMALL_HEADER_LINK_FONT_STYLE_HOVER,
				DEFAULT_NEG_AMNT_FONT_STYLE,
				DEFAULT_POS_AMNT_FONT_STYLE};
		for (int a = 0; a < styleNames.length; a++) {
			map.put(styleNames[a], styleValues[a]);
		}
		return map;
	}
	public void main(IWContext iwc) throws java.rmi.RemoteException {
		//control(iwc);
	}
	public void initializeInMain(IWContext iwc) {
		super.initializeInMain(iwc);
		init(iwc);
		if (isAdmin && administrative && getICObjectInstanceID() > 0) {
			add(getAdminPart(getCategoryId(), false, newobjinst, false, iwc));
		}
		form = new Form();
		table = new Table(1, 7);
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		form.add(table);
		add(this.form);
	}
	public void init(IWContext iwc) {
		iwrb = getResourceBundle(iwc);
		iwb = getBundle(iwc);
		core = iwc.getIWMainApplication().getCoreBundle();
		isAdmin = this.hasEditPermission();
		
		initCategoryId(iwc);
		try {
			getFinanceService(iwc);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	protected void control(IWContext iwc) throws java.rmi.RemoteException {
		Table T = new Table();
		T.setWidth("100%");
		// T.setHeight("100%");
		T.setCellpadding(0);
		T.setCellspacing(0);
		FinanceIndex index = new FinanceIndex(getCategoryId());
		if (FinanceObjects != null)
			index.addFinanceObjectAll(FinanceObjects);
		T.add(index, 1, 2);
		super.add(T);
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	private PresentationObject getAdminPart(int iCategoryId, boolean enableDelete, boolean newObjInst, boolean info,
			IWContext iwc) {
		Table T = new Table(3, 1);
		T.setCellpadding(2);
		T.setCellspacing(2);
		IWBundle core = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		//if(iCategoryId > 0)
		{
			/*
			 * Link ne = new
			 * Link(core.getImage("/shared/create.gif","create"));
			 * ne.setWindowToOpen(FinanceEditorWindow.class);
			 * ne.addParameter(FinanceEditorWindow.prmCategory,iCategoryId);
			 * T.add(ne,1,1); T.add(T.getTransparentCell(iwc),1,1);
			 */
			Link change = getCategoryLink();
			change.setImage(core.getImage("/shared/edit.gif", "edit"));
			T.add(change, 1, 1);
		}
		T.setWidth("100%");
		return T;
	}
	public static Parameter getCategoryParameter(int iCategoryId) {
		return new Parameter(prmCategoryId, String.valueOf(iCategoryId));
	}
	public static Parameter getCategoryParameter(Integer iCategoryId) {
		return new Parameter(prmCategoryId, iCategoryId.toString());
	}
	public static int parseCategoryId(IWContext iwc) {
		if (iwc.isParameterSet(prmCategoryId))
			return Integer.parseInt(iwc.getParameter(prmCategoryId));
		else if (iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(CATEGORY_PROPERTY) != null)
			return Integer.parseInt(iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(
					CATEGORY_PROPERTY));
		else
			return -1;
	}
	private void initCategoryId(IWContext iwc) {
		iCategoryId = getCategoryId();
		if (iCategoryId <= 0) {
			if (iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(CATEGORY_PROPERTY) != null)
				iCategoryId = Integer.parseInt(iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(
						CATEGORY_PROPERTY));
		}
	}
	public Integer getFinanceCategoryId() {
		return new Integer(iCategoryId);
	}
	public Link getLink(Class cl, String name) {
		Link L = getLink(name);
		L.addParameter(Finance.getCategoryParameter(getCategoryId()));
		L.addParameter(getFinanceObjectParameter(cl));
		L.setFontSize(1);
		
		return L;
	}
	public Parameter getFinanceObjectParameter(Class financeClass) {
		return new Parameter(prmFinanceClass, financeClass.getName());
	}
	public void addFinanceObject(Block obj) {
		if (FinanceObjects == null)
			FinanceObjects = new java.util.Vector();
		FinanceObjects.add(obj);
	}
	/*
	 * public void main(IWContext iwc){ isAdmin = iwc.hasEditPermission(this);
	 * core = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
	 * control(iwc); }
	 */
	public void setAdministrative(boolean administrative) {
		this.administrative = administrative;
	}
	
	public FinanceService getFinanceService() {
		return this.financeService;
	}
	public FinanceService getFinanceService(IWApplicationContext iwac) throws RemoteException {
		if (financeService == null)
			financeService = (FinanceService) IBOLookup.getServiceInstance(iwac, FinanceService.class);
		return financeService;
	}
	public CollectionNavigator getCollectionNavigator(IWContext iwc) {
		CollectionNavigator navigator = new CollectionNavigator(getCollectionSize());
		navigator.setTextStyle(STYLENAME_SMALL_TEXT);
		navigator.setLinkStyle(STYLENAME_SMALL_LINK);
		navigator.setNumberOfEntriesPerPage(getCollectionViewSize());
		navigator.setPadding(getCellpadding());
		setCollectionIndex(navigator.getStart(iwc));
		return navigator;
	}
	/**
	 * @return Returns the collectionIndex.
	 */
	public int getCollectionIndex() {
		return collectionIndex;
	}
	/**
	 * @param collectionIndex
	 *                    The collectionIndex to set.
	 */
	public void setCollectionIndex(int collectionIndex) {
		this.collectionIndex = collectionIndex;
	}
	/**
	 * @return Returns the collectionSize.
	 */
	public int getCollectionSize() {
		return collectionSize;
	}
	/**
	 * @param collectionSize
	 *                    The collectionSize to set.
	 */
	public void setCollectionSize(int collectionSize) {
		this.collectionSize = collectionSize;
	}
	/**
	 * @return Returns the collectionViewSize.
	 */
	public int getCollectionViewSize() {
		return collectionViewSize;
	}
	/**
	 * @param collectionViewSize
	 *                    The collectionViewSize to set.
	 */
	public void setCollectionViewSize(int collectionViewSize) {
		this.collectionViewSize = collectionViewSize;
	}
	
	public DataTable getDataTable(){
		DataTable T = new DataTable();
		//T.setHeaderColor(getHeaderColor());
		T.setTitleColor(getHeaderColor());
		T.setZebraColors(getZebraColor1(),getZebraColor2());
		return T;
	}
	
	
}