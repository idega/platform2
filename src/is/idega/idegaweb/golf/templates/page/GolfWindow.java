// idega - Gimmi & Eiki
package is.idega.idegaweb.golf.templates.page;

import is.idega.idegaweb.golf.presentation.GolfBlock;

import java.util.HashMap;
import java.util.Map;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWProperty;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWStyleManager;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.InputContainer;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.MenuBar;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Window;
import com.idega.util.text.StyleConstants;

public class GolfWindow extends Window {
	
	private IWStyleManager _manager;
	
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

	public final static String STYLENAME_TEXT = "Text";
	public final static String STYLENAME_BIG_TEXT = "BigText";
	public final static String STYLENAME_SMALL_TEXT = "SmallText";
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
	private final static String DEFAULT_HEADER_COLOR = "#3C532A";
	private final static String DEFAULT_ZEBRA_COLOR_1 = "#ffffff";
	private final static String DEFAULT_ZEBRA_COLOR_2 = "#DEE4D5";
	private final static String DEFAULT_BIG_TEXT_FONT_STYLE = "font-weight:bold;font-size:14px;";
	private final static String DEFAULT_TEXT_FONT_STYLE = "font-weight:plain;";
	private final static String DEFAULT_SMALL_TEXT_FONT_STYLE = "font-style:normal;color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
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

	private static final String LIGHT_ROW_STYLE = "lightRow";
	private static final String DARK_ROW_STYLE = "darkRow";
	private static final String HEADER_ROW_STYLE = "headerRow";
	private static final String ERROR_ROW_STYLE = "errorRow";

  private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.golf";

  private Table table;
  private Table contentTable;
  private Table mainTable;
  private Table menuTable;
  
  private Class _golfClassToInstanciate = null;

  private MenuBar Menu;

  public String header_color = "#F2BC00";

  public String color = "#F2BCFF";

  public String MenuAlignment = "&nbsp;&nbsp;&nbsp;&nbsp;";
  
  protected IWBundle _iwb;
  protected IWResourceBundle _iwrb;
  private boolean initialized;
  
  private String styleScript = "DefaultStyle.css";
  private boolean useStyleSheetFromCoreBundle = true;

  public GolfWindow() {
    super();
    this.setMarginHeight(0);
    this.setMarginWidth(0);
    this.setLeftMargin(0);
    this.setTopMargin(0);
    this.setBackgroundColor("#7faf46");

    initialized = false;
    constructTable();
  }

  public GolfWindow(String name, int width, int height) {
    super(name, width, height);
    this.setMarginHeight(0);
    this.setMarginWidth(0);
    this.setLeftMargin(0);
    this.setTopMargin(0);
    this.setBackgroundColor("#7faf46");

    initialized = false;
    constructTable();
  }
  
  private void constructTable() {
  		table = new Table(1, 7);
  		table.setAlignment(1,4,Table.HORIZONTAL_ALIGN_CENTER);
  		contentTable = new Table(3, 3);
  		mainTable = new Table(1, 2);
  		initializeMenuTable();
  }
  
  private void initializeMenuTable() {
  	menuTable = new Table();
  	menuTable.setNoWrap();
  	menuTable.setCellpaddingAndCellspacing(0);
  	table.emptyCell(1,4);
  	table.add(menuTable,1,4);
  }

  private void initTable(IWContext modinfo) {
    IWBundle iwb = getBundle(modinfo);
    IWResourceBundle iwrb = getResourceBundle(modinfo);

    table.setBorder(0);
    table.setCellpadding(0);
    table.setCellspacing(0);
    table.setWidth(Table.HUNDRED_PERCENT);
    table.setHeight(Table.HUNDRED_PERCENT);
    table.setVerticalAlignment(1, 7, Table.VERTICAL_ALIGN_TOP);

//    table.setHeight(1, 1, 59);
    table.setHeight(1, 2, 1);
    table.setHeight(1, 3, 1);
    table.setHeight(1, 4, 21);
    table.setHeight(1, 5, 1);
    table.setHeight(1, 6, 5);
    table.setHeight(1, 7, Table.HUNDRED_PERCENT);
    
    table.setCellpaddingTop(1, 7, 1);
    table.setCellpaddingLeft(1, 7, 6);
    table.setCellpaddingRight(1, 7, 6);
    table.setCellpaddingBottom(1, 7, 6);
    //7faf46
//    table.setBackgroundImage(1, 1, iwb.getImage("golfwindow/top_59px.jpg"));
    table.setColor(1, 2, "#CDCECD");
    table.setColor(1, 3, "#858584");
    table.setBackgroundImage(1, 4, iwb.getImage("golfwindow/menu_21px.jpg"));
    table.setColor(1, 5, "#3A5A20");
    //table.setBackgroundImage(1, 6, iwb.getImage("golfwindow/grad_5px.jpg"));
    table.setColor(1, 6, "#7faf46");
    table.setColor(1, 7, "#7faf46");

//    Image topLeft = iwb.getImage("golfwindow/golf-logo.jpg");
//    topLeft.setAlignment(Image.ALIGNMENT_LEFT);
//    Image topRight = iwb.getImage("golfwindow/idega-logo.jpg");
//    topRight.setAlignment(Image.ALIGNMENT_RIGHT);

//    table.add(topLeft, 1, 1);
    table.setRowStyleClass(1,"top");
    table.setStyleClass(1,1,"banner");
//    table.add(topRight, 1, 1);
    contentTable.setCellpadding(0);
    contentTable.setCellspacing(0);
    contentTable.setWidth(Table.HUNDRED_PERCENT);
    //contentTable.setHeight(Table.HUNDRED_PERCENT);
    contentTable.setCellBorder(1, 1, 1, "#3A5A20", "solid");
    contentTable.mergeCells(1, 1, 2, 2);
    contentTable.setWidth(3, 3);
    contentTable.setHeight(3, 3);
    contentTable.setWidth(1, 3, 3);
    contentTable.setHeight(3, 1, 3);
    contentTable.setColor(1, 1, "#FFFFFF");
    contentTable.setColor(2, 3, "#70924F");
    contentTable.setColor(3, 3, "#70924F");
    contentTable.setColor(3, 2, "#70924F");
    table.add(contentTable, 1, 7);

    mainTable.setCellpadding(0);
    mainTable.setCellspacing(0);
    mainTable.setWidth(Table.HUNDRED_PERCENT);
    mainTable.setHeight(Table.HUNDRED_PERCENT);
    mainTable.setHeight(1, 17);
    mainTable.setBackgroundImage(1, 1, iwb.getImage("golfwindow/heading_tiler.jpg"));
    mainTable.setCellpadding(1, 2, 12);
    mainTable.setCellpaddingLeft(1, 1, 3);
   contentTable.add(mainTable, 1, 1);
    
    super.add(table);
    initialized = true;
  }

  public void add(PresentationObject objectToAdd) {
  		mainTable.add(objectToAdd, 1, 2);
  }
  
  public void setContentAreaAlignment(String alignment) {
  	mainTable.setAlignment(1,2,alignment);
  }

  public void addHeading(String headingText) {
    Text heading = new Text(headingText);
    heading.setStyleAttribute(StyleConstants.ATTRIBUTE_FONT_FAMILY, StyleConstants.FONT_FAMILY_TREBUCHET);
    heading.setStyleAttribute(StyleConstants.ATTRIBUTE_FONT_SIZE, "10px");
    heading.setStyleAttribute(StyleConstants.ATTRIBUTE_COLOR, "#3A5A20");
    
    mainTable.emptyCell(1, 1);
    mainTable.add(heading, 1, 1);
  }
  
  public void addMenuLink(Link menuLink) {
  	int columns = menuTable.getColumns();
  	if(columns>1){
	  	Text t = new Text("|");
	  	t.setStyleClass(getMenuTextStyleClass());  	
	  	
	  	menuTable.add(t,columns+1,1);
	  	menuTable.setCellpaddingLeft(columns+1,1,5);
	  	menuTable.setCellpaddingRight(columns+1,1,5);
    }
  	menuLink.setStyleClass(getMenuLinkStyleClass());
  	menuTable.add(menuLink,columns+2,1);
  }
  
  public void emptyMenuArea() {
  	initializeMenuTable();
  }

  public void empty() {
  	mainTable.emptyCell(1, 2);
  }

  public void setContentHorizontalAlignment(String align) {
  	mainTable.setAlignment(1, 2, align);
  }

  public void setContentVerticalAlignment(String align) {
  	mainTable.setVerticalAlignment(1, 2, align);
  }

  private void MenuBar() {

    Menu.setPosition(0, 39);
    Menu.setSizes(1, 1, 0);
    Menu.setColors("#444444", "#FFFFFF", "#BDBDBD", "#444444", "#F2BC00",
        "#444444", "#BDBDBD", "#F2BC00", "#444444");
    Menu.setFonts("Arial", "Helvetica", "sans-serif", "normal", "normal", 8,
        "Arial", "Helvetica", "sans-serif", "normal", "normal", 8);
    Menu.scaleNavBar();

    Menu.addMenu("file", 80, 120);
    Menu.addMenu("addons", 80, 120);
    Menu.addMenu("tools", 80, 120);
    Menu.addMenu("options", 80, 120);
    Menu.addMenu("help", 80, 120);

    Menu.addItem("file", MenuAlignment + "File");
    Menu.addItem("addons", MenuAlignment + "Add-ons");
    Menu.addItem("tools", MenuAlignment + "Tools");
    Menu.addItem("options", MenuAlignment + "Options");
    Menu.addItem("help", MenuAlignment + "Help");

    this.addToOptionsMenu("Themes", "");
    this.addToOptionsMenu("Language", "");
    this.addToHelpMenu("Help", "");

  }

  public void addToFileMenu(String ItemName, String Url) {
    Menu.addItem("file", MenuAlignment + ItemName, Url);
  }

  public void addToAddOnsMenu(String ItemName, String Url) {
    Menu.addItem("addons", MenuAlignment + ItemName, Url);
  }

  public void addToToolsMenu(String ItemName, String Url) {
    Menu.addItem("tools", MenuAlignment + ItemName, Url);
  }

  public void addToOptionsMenu(String ItemName, String Url) {
    Menu.addItem("options", MenuAlignment + ItemName, Url);
  }

  public void addToHelpMenu(String ItemName, String Url) {
    Menu.addItem("help", MenuAlignment + ItemName, Url);
  }

  public MenuBar getMenu() {
    return Menu;
  }

  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  }
  
  public void initializeInMain(IWContext iwc) throws InstantiationException, IllegalAccessException {
  	if(_golfClassToInstanciate != null) {
  		this.add(_golfClassToInstanciate.newInstance());
  	}
  }
  
  public void setGolfClassToInstanciate(Class c) {
  	_golfClassToInstanciate = c;
  }

  public void _main(IWContext modinfo) throws Exception {
  	_iwb = getBundle(modinfo);
    _iwrb = getResourceBundle(modinfo);
    _manager = new IWStyleManager();
    
    try {
      if (!initialized) {
        initTable(modinfo);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    super._main(modinfo);
    setStyleSheetURL(this.getStyleSheetPath(modinfo)+styleScript);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#clone()
   */
  public Object clone() {
    GolfWindow obj = null;
    try {
      obj = (GolfWindow) super.clone();
      obj.table = (Table) this.table.clone();
      obj.contentTable = (Table) this.contentTable.clone();
      obj.mainTable = (Table) this.mainTable.clone();
      obj.Menu = (MenuBar) this.Menu.clone();
      obj.initialized = this.initialized;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    return obj;
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
  	
  	public String getMenuTextStyleClass() {
  		return getStyleName(STYLENAME_TEMPLATE_HEADER);
  	}
  	
  	public String getMenuLinkStyleClass() {
  		return getStyleName(STYLENAME_TEMPLATE_HEADER_LINK);
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
  		String[] styleNames = { STYLENAME_TEXT, STYLENAME_BIG_TEXT, STYLENAME_SMALL_TEXT, STYLENAME_BIG_HEADER, STYLENAME_HEADER, STYLENAME_SMALL_HEADER, STYLENAME_LINK, STYLENAME_LIST_HEADER, STYLENAME_LIST_TEXT, STYLENAME_LIST_LINK, STYLENAME_ERROR_TEXT, STYLENAME_SMALL_ERROR_TEXT, STYLENAME_INTERFACE, STYLENAME_SMALL_LINK, STYLENAME_SMALL_LINK+":hover", STYLENAME_TEMPLATE_LINK, STYLENAME_TEMPLATE_LINK+":hover", STYLENAME_TEMPLATE_HEADER, STYLENAME_TEMPLATE_SMALL_HEADER, STYLENAME_TEMPLATE_LINK_SELECTED, STYLENAME_TEMPLATE_LINK_SELECTED+":hover", STYLENAME_TEMPLATE_SUBLINK, STYLENAME_TEMPLATE_SUBLINK+":hover", STYLENAME_TEMPLATE_SUBLINK_SELECTED, STYLENAME_TEMPLATE_SUBLINK_SELECTED+":hover", STYLENAME_TEMPLATE_HEADER_LINK, STYLENAME_TEMPLATE_HEADER_LINK+":hover", STYLENAME_TEMPLATE_LINK2, STYLENAME_TEMPLATE_LINK2+":hover", STYLENAME_TEMPLATE_LINK3, STYLENAME_TEMPLATE_LINK3+":hover", STYLENAME_CHECKBOX, STYLENAME_INTERFACE_BUTTON, STYLENAME_SMALL_HEADER_LINK, STYLENAME_SMALL_HEADER_LINK+":hover" };
  		String[] styleValues = { DEFAULT_TEXT_FONT_STYLE, DEFAULT_BIG_TEXT_FONT_STYLE, DEFAULT_SMALL_TEXT_FONT_STYLE, DEFAULT_BIG_HEADER_FONT_STYLE, DEFAULT_HEADER_FONT_STYLE, DEFAULT_SMALL_HEADER_FONT_STYLE, DEFAULT_LINK_FONT_STYLE, DEFAULT_LIST_HEADER_FONT_STYLE, DEFAULT_LIST_FONT_STYLE, DEFAULT_LIST_LINK_FONT_STYLE, DEFAULT_ERROR_TEXT_FONT_STYLE, DEFAULT_SMALL_ERROR_TEXT_FONT_STYLE, DEFAULT_INTERFACE_STYLE, DEFAULT_SMALL_LINK_FONT_STYLE, DEFAULT_SMALL_LINK_FONT_STYLE_HOVER, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", DEFAULT_CHECKBOX_STYLE, DEFAULT_INTERFACE_BUTTON_STYLE, DEFAULT_SMALL_HEADER_LINK_FONT_STYLE, DEFAULT_SMALL_HEADER_LINK_FONT_STYLE_HOVER };

  		for (int a = 0; a < styleNames.length; a++) {
  			map.put(styleNames[a], styleValues[a]);
  		}

  		return map;
  	}


	public Text getStyleText(String text, String styleName) {
		return (Text) getStyleText(new Text(text),styleName);	
	}
	
	public Text getStyleText(Text text, String styleName) {
		return (Text) setStyle(text,styleName);	
	}
	
	public Link getStyleLink(String link, String styleName) {
		return (Link) getStyleLink(new Link(link),styleName);	
	}
	
	public Link getStyleLink(Link link, String styleName) {
		return (Link) setStyle(link,styleName);	
	}
	
	/**
	 * Gets a prefixed stylename to use for objects, with prefix specific for the bundle used by this block
	 * if the block is in the core bundle, no prefix is added
	 * @param styleName
	 * @return stylename
	 */
	private String getStyleName(String styleName, boolean isLink){
		if ( getIWUserContext() != null ) {
			String prefix = getBundle(getIWUserContext()).getBundleName();
			if (prefix != Builderaware.IW_CORE_BUNDLE_IDENTIFIER) {
				prefix = prefix.substring(prefix.lastIndexOf(".") + 1) + "_";
				styleName = prefix+styleName;
			}
		}
		if (_manager != null) {
			if (!_manager.isStyleSet(styleName))
				_manager.setStyle(styleName, "");
			if (autoCreateGlobalHoverStyles() && !_manager.isStyleSet(styleName + ":hover"))
				_manager.setStyle(styleName + ":hover", "");
		}
					
		return styleName;
	}
	
	protected boolean autoCreateGlobalHoverStyles() {
		return false;
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
	
	private PresentationObject setStyle(PresentationObject obj, String styleName, boolean isLink) {
		obj.setStyleClass(getStyleName(styleName, isLink));
		return obj;
	}

	public PresentationObject setStyle(PresentationObject obj, String styleName) {
		return setStyle(obj, styleName, false);
	}

  	


	
	  /**
	   * This method depends on iwbCore and iwb to be initialized
	   * @return the path of the chosen stylesheet
	   */
	  private String getStyleSheetPath(IWContext iwc) {
	  	IWProperty styleSheet = null;
		String styleSrc = null;
		IWBundle iwbCore = iwc.getIWMainApplication().getCoreBundle();
	  	if(useStyleSheetFromCoreBundle) {
	  		styleSheet = iwc.getIWMainApplication().getSystemProperties().getIWProperty(iwbCore.getBundleIdentifier()+".editorwindow_styleSheet_path");
	  		if(styleSheet==null) {
				styleSrc = iwbCore.getVirtualPath()+"/editorwindow/";
				iwc.getIWMainApplication().getSystemProperties().getNewProperty().setProperty(iwbCore.getBundleIdentifier()+".editorwindow_styleSheet_path",styleSrc);
			} else {
				styleSrc = styleSheet.getValue();
			}
	  	} else {
	  		styleSheet = iwc.getIWMainApplication().getSystemProperties().getIWProperty(getBundleIdentifier()+".editorwindow_styleSheet_name");
	  		if(styleSheet==null) {
	  			styleSrc = _iwb.getVirtualPath()+"/editorwindow/";
	  			iwc.getIWMainApplication().getSystemProperties().getNewProperty().setProperty(getBundleIdentifier()+".editorwindow_styleSheet_name",styleSrc);
	  		} else {
	  			styleSrc = styleSheet.getValue();
	  		}
	  	}
		return styleSrc;
	  }
	  
	  public void setStyleScript(String styleScriptName) {
	  	styleScript= styleScriptName;
	  }
	  
	  /**
	   * 
	   * @param value if false it uses the current bundle of the extended class.  Default value is true.
	   */
	  public void setToUseStyleSheetFromCoreBundle(boolean value) {
	  	useStyleSheetFromCoreBundle = value;
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
		
		public String getErrorRowClass() {
			return getStyleName(ERROR_ROW_STYLE);
		}
		
		public Link getTemplateHeaderLink(String s) {
			return getStyleLink(new Link(s), STYLENAME_TEMPLATE_HEADER_LINK);
		}

		
}