package com.idega.block.text.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.block.text.data.*;
import com.idega.block.text.business.*;
import com.idega.data.*;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.util.text.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;


public class TextReader extends JModuleObject{

private boolean isAdmin=false;
private TextModule text;

private TxText txText;
private LocalizedText locText;
private String sLocaleId;
private String sAttribute = null;
private Table myTable = new Table(2,2);
private String adminURL = "/text/textadmin.jsp";

private String textBgColor = "#FFFFFF";
private String textColor = "#000000";
private String headlineBgColor = "#FFFFFF";
private String headlineColor = "#000000";
private int textSize = 2;
private int tableTextSize = 1;
private int headlineSize = 3;
private String tableWidth = "";
private boolean displayHeadline=true;
private boolean enableDelete=true;
private String textWidth = "100%";
private String textStyle = "";
private String headlineStyle = "";
private boolean reverse = false;
private boolean crazy = false;
private boolean viewall = false;
private boolean newobjinst = false;
private boolean newWithAttribute = false;
private int iTextId = -1;
public static String prmTextId = "txtr.textid";

private IWBundle iwb;
private IWResourceBundle iwrb;
private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.text";

  public TextReader(){
    this.iTextId = -1;
  }

  public TextReader(String sAttribute){
    this.iTextId = -1;
    this.sAttribute = sAttribute;
  }

  public TextReader(int iTextId){
    this.iTextId = iTextId;
  }

  public void main(ModuleInfo modinfo) throws Exception {
    isAdmin = AccessControl.hasEditPermission(this,modinfo);
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);
    Locale locale = modinfo.getCurrentLocale();
    if(iTextId < 0){
      String sTextId = modinfo.getParameter(prmTextId );
      if(sTextId != null)
        iTextId = Integer.parseInt(sTextId);
      else if(getICObjectInstanceID() > 0){
        iTextId = TextFinder.getObjectInstanceTextId(getICObjectInstance());
        if(iTextId <= 0 ){
          newobjinst = true;
        }
      }
    }
    int iLocaleId = ICLocaleBusiness.getLocaleId(locale);

    if ( sAttribute != null ){
      txText = TextFinder.getText(sAttribute);
      newWithAttribute = true;
    }
    else if(iTextId > 0) {
      txText = new TxText((iTextId));
    }
    if(txText != null){
      locText = TextFinder.getLocalizedText(txText.getID(),iLocaleId);
    }
    if(locText != null){
        textTable();
    }
    if(isAdmin){
      addAdminPart();
    }
    add(myTable);
  }

  public void textTable() throws IOException,SQLException {
    myTable.setCellpadding(3);
    myTable.setCellspacing(3);
    myTable.mergeCells(1,1,2,1);
    myTable.mergeCells(1,2,2,2);
    myTable.setRowColor(1,headlineBgColor);
    myTable.setRowColor(2,textBgColor);
    myTable.setWidth(textWidth);

    //Text headline = new Text(text.getTextHeadline());
    Text headline = new Text(locText.getHeadline());
    headline.setFontSize(headlineSize);
    headline.setFontColor(headlineColor);
    headline.setBold();
    headline.setAttribute("class","headlinetext");
    headline.setFontStyle(headlineStyle);

    //String textBody = text.getTextBody();
    String textBody = locText.getBody();

    if ( reverse ) {
      textBody = textReverse(textBody);
    }
    if ( crazy ) {
      textBody = textCrazy(textBody);
    }

    textBody = formatText(textBody);

    Text body = new Text(textBody);
    body.setFontSize(textSize);
    body.setFontColor(textColor);
    body.setAttribute("class","bodytext");
    body.setFontStyle(textStyle);

    Image bodyImage;

    //if ( text.getIncludeImage().equals("Y") ) {
      //bodyImage = new Image(text.getImageId());
    if ( txText.getIncludeImage() ) {
      bodyImage = new Image(txText.getImageId());
      bodyImage.setAttribute("align","right");
      bodyImage.setAttribute("vspace","6");
      bodyImage.setAttribute("hspace","6");

      if ( displayHeadline ) {
        myTable.add(bodyImage,1,2);
      }
      else {
        myTable.add(bodyImage,1,1);
      }
    }

    if ( displayHeadline ) {
      if ( headline.getText() != null ) {
      Anchor headlineAnchor = new Anchor(headline,headline.getText());
      headlineAnchor.setFontColor(headlineColor);
      myTable.add(headlineAnchor ,1,1);
      myTable.add(body,1,2);
      }
    }
    else {
      myTable.mergeCells(1,1,1,2);
      myTable.add(body,1,1);
    }

  }

  public void addAdminPart(){
     Window adminWindow = new Window("AdminWindow",TextEditorWindow.class,com.idega.jmodule.object.Page.class);
      adminWindow.setWidth(570);
      adminWindow.setHeight(430);
      myTable.resize(2,3);
      if(iTextId > 0){
      Link breyta = new Link(iwrb.getImage("change.gif"));
        breyta.setWindowToOpen(TextEditorWindow.class);
        breyta.addParameter(TextEditorWindow.prmTextId,iTextId);
      myTable.add(breyta,1,3);
      Link delete = new Link(iwrb.getImage("delete.gif"));
        delete.setWindowToOpen(TextEditorWindow.class);
        delete.addParameter(TextEditorWindow.prmDelete,iTextId);
         if ( enableDelete ) {
          myTable.add(delete,2,3);
        }
      }
      if(newobjinst){
        Link newObjectInstanceLink = new Link(iwrb.getImage("new.gif"));
        newObjectInstanceLink.setWindowToOpen(TextEditorWindow.class);
        newObjectInstanceLink.addParameter(TextEditorWindow.prmObjInstId,getICObjectInstanceID());
        myTable.add(newObjectInstanceLink,1,3);
      }
      else if(newWithAttribute){
        Link newAttributeLink = new Link(iwrb.getImage("new.gif"));
        newAttributeLink.setWindowToOpen(TextEditorWindow.class);
        newAttributeLink.addParameter(TextEditorWindow.prmAttribute,sAttribute);
        myTable.add(newAttributeLink,1,3);
      }

  }

  public void noTextID() throws IOException,SQLException {

    //TextModule[] texts = (TextModule[]) (new TextModule()).findAll();
    TxText[] texts = (TxText[]) (new TxText()).findAll();

    myTable = new Table(2,texts.length+1);
    myTable.mergeCells(1,1,2,1);

    //		Text text_heading = new Text("Texts in this database:");
    // Breytt af gimmi 13.03.2001
    Text text_heading = new Text("");
    text_heading.setFontSize(3);
    text_heading.setBold();

    for ( int a = 0; a < texts.length; a++ ) {

      //Link textLink = new Link(texts[a].getTextHeadline(),"");
      Link textLink = new Link(texts[a].getHeadline(),"");
      textLink.addParameter(TextEditorWindow.prmTextId,texts[a].getID());

      myTable.addText(texts[a].getID()+".",1,a+2);
      myTable.add(textLink,2,a+2);
    }

    if ( texts.length == 0 ) {
      myTable.resize(2,2);
      myTable.addText("",2,2);
    }

    myTable.add(text_heading,1,1);

  }

  public String formatText(String textBody) {
    //Búa til töflu
    if (textBody==null || textBody.equals("")) textBody = "";

    Vector tableVector = createTextTable(textBody);

    for ( int a = 0; a < tableVector.size(); a++ ) {

      String tableRow = tableVector.elementAt(a).toString();

      if ( a == 0 ) {
        tableRow = TextSoap.findAndReplace(tableRow,"|","</font></td><td valign=\"top\"><font size=\""+(tableTextSize+1)+"\">");
      }
      else {
        tableRow = TextSoap.findAndReplace(tableRow,"|","</font></td><td valign=\"top\"><font size=\""+tableTextSize+"\">");
      }

      if ( a == 0 || a == tableVector.size()-1) {
        if ( a == 0 ) {
          tableRow = "<table border=\"0\" cellpadding=\"3\" cellspacing=\"0\" width=\""+tableWidth+"\"><tr bgcolor=\"#FFFFFF\"><td valign=\"top\"><font size=\""+(tableTextSize+1)+"\">"+tableRow+"</font></td></tr>";
        }

        if ( a == tableVector.size()-1 ) {
          tableRow = "<tr bgcolor=\"#FFFFFF\"><td valign=\"top\"><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr></table>";
        }
      }
      else {
        tableRow = "<tr bgcolor=\"#FFFFFF\"><td valign=\"top\"><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr>";
      }

      textBody = TextSoap.findAndReplace(textBody,tableVector.elementAt(a).toString(),tableRow);
    }

    textBody = TextSoap.findAndReplace(textBody,"|\r\n","");
    textBody = TextSoap.findAndReplace(textBody,"|","");
    //Töflugerð lokið


    //Búa til töflu 2
    if (textBody==null || textBody.equals("")) textBody = "";
    tableVector = createTextTableNoBanner(textBody);

    for ( int a = 0; a < tableVector.size(); a++ ) {

      String tableRow = tableVector.elementAt(a).toString();

      if ( a == 0 ) {
        tableRow = TextSoap.findAndReplace(tableRow,"?","</font></td><td><font size=\""+(tableTextSize+1)+"\">");
      }
      else {
        tableRow = TextSoap.findAndReplace(tableRow,"?","</font></td><td><font size=\""+tableTextSize+"\">");
      }

      if ( a == 0 || a == tableVector.size()-1) {
        if ( a == 0 ) {
          tableRow = "<table border=\"0\" cellpadding=\"3\" cellspacing=\"0\" width=\""+tableWidth+"\"><tr bgcolor=\"#FFFFFF\"><td><font size=\""+(tableTextSize+1)+"\">"+tableRow+"</font></td></tr>";
        }

        if ( a == tableVector.size()-1 ) {
          tableRow = "<tr bgcolor=\"#FFFFFF\"><td><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr></table>";
        }
      }
      else {
        tableRow = "<tr bgcolor=\"#FFFFFF\"><td><font size=\""+tableTextSize+"\">"+tableRow+"</font></td></tr>";
      }

      textBody = TextSoap.findAndReplace(textBody,tableVector.elementAt(a).toString(),tableRow);
    }

    textBody = TextSoap.findAndReplace(textBody,"?\r\n","");
    textBody = TextSoap.findAndReplace(textBody,"?","");
    //Töflugerð lokið

    //Búa til tengla
    Vector linkVector = createTextLink(textBody);
    for ( int a = 0; a < linkVector.size(); a++ ) {
      String link = linkVector.elementAt(a).toString();
      int comma = link.indexOf(",");
      link = "<a href=\""+link.substring(comma+1,link.length())+"\" target=\"_blank\">"+link.substring(0,comma)+"</a>";
      textBody = TextSoap.findAndReplace(textBody,"Link("+linkVector.elementAt(a).toString()+")",link);
    }

    //Almenn hreinsun
    textBody = TextSoap.findAndReplace(textBody,"*","<li>");
    textBody = TextSoap.findAndReplace(textBody,".\r\n",".<br><br>");
    textBody = TextSoap.findAndReplace(textBody,"?\r\n","?<br><br>");
    textBody = TextSoap.findAndReplace(textBody,"!\r\n","!<br><br>");

    //Búa til headline
    /*Vector testVector = testText(textBody);

    while ( textBody.indexOf("\r\n\r\n\r\n") != -1 ) {
      textBody = TextSoap.findAndReplace(textBody,"\r\n\r\n\r\n","\r\n\r\n");
    }

    int head_size = textSize + 1;

    for ( int a = 0; a < testVector.size(); a++ ) {
      textBody = TextSoap.findAndReplace(textBody,"\r\n\r\n"+testVector.elementAt(a).toString(),"temp");
      textBody = TextSoap.findAndReplace(textBody,"temp","<font size=\""+head_size+"\"><b>"+testVector.elementAt(a).toString()+"</b></font>");
    }*/
    //Headlinegerð búin

    textBody = TextSoap.findAndReplace(textBody,"\r\n","<br>");
    textBody = TextSoap.findAndReplace(textBody,"\t","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

    return textBody;
  }

  public Vector testText(String textBody) {
    Vector testVector = TextSoap.FindAllBetween(textBody,"\r\n\r\n","\r\n");
    return testVector;
  }

  public Vector createTextTable(String textBody) {
    Vector tableVector = TextSoap.FindAllBetween(textBody,"|","|\r\n");
    return tableVector;
  }

  public Vector createTextTableNoBanner(String textBody) {
    Vector tableVector = TextSoap.FindAllBetween(textBody,"?","?\r\n");
    return tableVector;
  }

  public Vector createTextLink(String textBody) {
    Vector linkVector = TextSoap.FindAllBetween(textBody,"Link(",")");
    return linkVector;
  }

  public String textReverse(String strengur) {
    StringBuffer buffer = new StringBuffer(strengur);
    String reverse = buffer.reverse().toString();
    return reverse;
  }

  public String textCrazy(String strengur) {

    String crazy = "";

    StringTokenizer token = new StringTokenizer(strengur);

    while ( token.hasMoreTokens() ) {
      StringBuffer buffer = new StringBuffer(token.nextToken());
      crazy += buffer.reverse().toString();
      if ( token.hasMoreTokens() ) {
        crazy += " ";
      }
    }

    return crazy;

  }

  public void setAdmin(boolean isAdmin){
    this.isAdmin=isAdmin;
  }

  public void setTextBgColor(String textBgColor) {
    this.textBgColor=textBgColor;
  }

  public void setTextColor(String textColor) {
    this.textColor=textColor;
  }

  public void setHeadlineBgColor(String headlineBgColor) {
    this.headlineBgColor=headlineBgColor;
  }

  public void setHeadlineColor(String headlineColor) {
    this.headlineColor=headlineColor;
  }

  public void setTextSize(int textSize) {
    this.textSize=textSize;
  }

  public void setTableTextSize(int tableTextSize) {
    this.tableTextSize=tableTextSize;
  }

  public void setTableWidth(String tableWidth) {
    this.tableWidth=tableWidth;
  }

  public void setHeadlineSize(int headlineSize) {
    this.headlineSize=headlineSize;
  }

  public void setTextStyle(String textStyle) {
    this.textStyle=textStyle;
  }

  public void setHeadlineStyle(String headlineStyle) {
    this.headlineStyle=headlineStyle;
  }

  public void displayHeadline(boolean displayHeadline) {
    this.displayHeadline=displayHeadline;
  }

  public void setEnableDelete(boolean enableDelete) {
    this.enableDelete=enableDelete;
  }

  /**
   * Sets alignment for the table around the text - added by gimmi@idega.is
   */
  public void setAlignment(String alignment) {
    this.myTable.setAlignment(alignment);
  }

  public void setWidth(String textWidth) {
    this.textWidth=textWidth;
  }

  public void setReverse() {
    this.reverse=true;
  }

  public void setCrazy() {
    this.crazy=true;
  }

  public void setViewAll() {
    this.viewall=true;
  }

  public String getAnchorName(){
  return text.getTextHeadline();
  }

  public String getBundleIdentifier(){
  return IW_BUNDLE_IDENTIFIER;
  }
}
