package se.idega.idegaweb.commune.presentation;

import java.util.HashMap;

import com.idega.builder.data.IBPage;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public class CommuneBlock extends com.idega.presentation.Block {
  public final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

  private final static String COMMUNE_STYLE = "Commune_";
  private final static String STYLENAME_TEXT = COMMUNE_STYLE+"Text";
  private final static String STYLENAME_SMALL_TEXT = COMMUNE_STYLE+"SmallText";
  private final static String STYLENAME_HEADER = COMMUNE_STYLE+"Header";
  private final static String STYLENAME_SMALL_HEADER = COMMUNE_STYLE+"SmallHeader";
  private final static String STYLENAME_LINK = COMMUNE_STYLE+"Link";
  private final static String STYLENAME_LIST_HEADER = COMMUNE_STYLE+"ListHeader";
  private final static String STYLENAME_LIST_TEXT = COMMUNE_STYLE+"ListText";
  private final static String STYLENAME_LIST_LINK = COMMUNE_STYLE+"ListLink";
  private final static String STYLENAME_ERROR_TEXT = COMMUNE_STYLE+"ErrorText";
  private final static String STYLENAME_SMALL_ERROR_TEXT = COMMUNE_STYLE+"SmallErrorText";
 
  private final static String DEFAULT_BACKGROUND_COLOR = "#f0f0f0";
  private final static String DEFAULT_TEXT_FONT_STYLE = "font-weight:plain;";
  private final static String DEFAULT_SMALL_TEXT_FONT_STYLE = "font-style:normal;color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
  private final static String DEFAULT_HEADER_FONT_STYLE = "font-weight:bold;";
  private final static String DEFAULT_SMALL_HEADER_FONT_STYLE = "font-style:normal;color:#000000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
  private final static String DEFAULT_LINK_FONT_STYLE = "color:#0000cc;";
  private final static String DEFAULT_LIST_HEADER_FONT_STYLE = "font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:bold;";
  private final static String DEFAULT_LIST_FONT_STYLE = "font-style:normal;color:#000000;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
  private final static String DEFAULT_LIST_LINK_FONT_STYLE = "font-style:normal;color:#0000cc;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";
  private final static String DEFAULT_ERROR_TEXT_FONT_STYLE = "font-weight:plain;color:#ff0000;";
  private final static String DEFAULT_SMALL_ERROR_TEXT_FONT_STYLE = "font-style:normal;color:#ff0000;font-size:10px;font-family:Verdana,Arial,Helvetica,sans-serif;font-weight:plain;";;

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

  private IWResourceBundle iwrb = null;
  private IBPage formResponsePage;

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void setResourceBundle(IWResourceBundle iwrb){
    this.iwrb = iwrb;
  }

  public IWResourceBundle getResourceBundle(){
    return this.iwrb;
  }

  public String getBackgroundColor(){
    return backgroundColor;
  }

  public String getTextFontStyle(){
    return textFontStyle;
  }

  public String getSmallTextFontStyle(){
    return smallTextFontStyle;
  }

  public String getLinkFontStyle(){
    return linkFontStyle;
  }

  public String getHeaderFontStyle(){
    return headerFontStyle;
  }

  public String getSmallHeaderFontStyle(){
    return smallHeaderFontStyle;
  }

  public String getListHeaderFontStyle(){
    return listHeaderFontStyle;
  }

  public String getListFontStyle(){
    return listFontStyle;
  }

  public String getListLinkFontStyle(){
    return listLinkFontStyle;
  }

  public String getErrorTextFontStyle(){
    return errorTextFontStyle;
  }

  public String getSmallErrorTextFontStyle(){
    return smallErrorTextFontStyle;
  }

  public void setBackroundColor(String color){
    this.backgroundColor = color;
  }

  public void setTextFontStyle(String fontStyle){
    this.textFontStyle = fontStyle;
  }

  public void setSmallTextFontStyle(String fontStyle){
    this.smallTextFontStyle = fontStyle;
  }

  public void setLinkFontStyle(String fontStyle){
    this.linkFontStyle = fontStyle;
  }

  public void setHeaderFontStyle(String fontStyle){
    this.headerFontStyle = fontStyle;
  }

  public void setSmallHeaderFontStyle(String fontStyle){
    this.smallHeaderFontStyle = fontStyle;
  }

  public void setListHeaderFontStyle(String fontStyle){
    this.listHeaderFontStyle = fontStyle;
  }

  public void setListFontStyle(String fontStyle){
    this.listFontStyle = fontStyle;
  }

  public void setListLinkFontStyle(String fontStyle){
    this.listLinkFontStyle = fontStyle;
  }

  public void setErrorTextFontStyle(String fontStyle){
    this.errorTextFontStyle = fontStyle;
  }

  public void setSmallErrorTextFontStyle(String fontStyle){
    this.smallErrorTextFontStyle = fontStyle;
  }

  public String localize(String textKey, String defaultText){
    if(iwrb==null){
      return defaultText;
    }
    return iwrb.getLocalizedString(textKey, defaultText);
  }

  public Text getText(String s){
    Text t = new Text(s);
    t.setFontClass(this.STYLENAME_TEXT);
    return t;
  }

  public Text getLocalizedText(String s, String d){
    return getText(localize(s,d));
  }

  public Text getSmallText(String s){
    Text t = new Text(s);
    t.setFontClass(this.STYLENAME_SMALL_TEXT);
    return t;
  }

  public Text getLocalizedSmallText(String s, String d){
    return getSmallText(localize(s,d));
  }

  public Text getHeader(String s){
    Text header = new Text(s);
    header.setFontClass(this.STYLENAME_HEADER);
    return header;
  }

  public Text getLocalizedHeader(String s, String d){
    return getHeader(localize(s,d));
  }

  public Text getSmallHeader(String s){
    Text header = new Text(s);
    header.setFontClass(this.STYLENAME_SMALL_HEADER);
    return header;
  }

  public Text getLocalizedSmallHeader(String s, String d){
    return getSmallHeader(localize(s,d));
  }

  public Link getLink(String s){
    Link l = new Link(s);
    l.setStyle(this.STYLENAME_LINK);
    return l;
  }

  public Link getLocalizedLink(String s, String d){
    return getLink(localize(s,d));
  }

  public Text getErrorText(String s){
    Text t = new Text(s);
    t.setFontClass(this.STYLENAME_ERROR_TEXT);
    return t;
  }

  public Text getSmallErrorText(String s){
    Text t = new Text(s);
    t.setFontClass(this.STYLENAME_SMALL_ERROR_TEXT);
    return t;
  }

  public IBPage getResponsePage(){
    return this.formResponsePage;
  }

  public void setResponsePage(IBPage page){
    this.formResponsePage = page;
  }
	
	/**
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public HashMap getStyleNames() {
  	HashMap map = new HashMap();
  	String[] styleNames = {STYLENAME_TEXT,STYLENAME_SMALL_TEXT,STYLENAME_HEADER,STYLENAME_SMALL_HEADER,
  												 STYLENAME_LINK,STYLENAME_LIST_HEADER,STYLENAME_LIST_TEXT,STYLENAME_LIST_LINK,
  												 STYLENAME_ERROR_TEXT,STYLENAME_SMALL_ERROR_TEXT};
  	String[] styleValues = {DEFAULT_TEXT_FONT_STYLE,DEFAULT_SMALL_TEXT_FONT_STYLE,DEFAULT_HEADER_FONT_STYLE,
  													DEFAULT_SMALL_HEADER_FONT_STYLE,DEFAULT_LINK_FONT_STYLE,DEFAULT_LIST_HEADER_FONT_STYLE,
  													DEFAULT_LIST_FONT_STYLE,DEFAULT_LIST_LINK_FONT_STYLE,DEFAULT_ERROR_TEXT_FONT_STYLE,
  													DEFAULT_SMALL_ERROR_TEXT_FONT_STYLE};
  		
  	for ( int a = 0; a < styleNames.length; a++ ) {
  		map.put(styleNames[a], styleValues[a]);	
  	}
  	
  	return map;	
	}

}