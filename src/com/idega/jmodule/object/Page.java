/*
 * $Id: Page.java,v 1.10 2001/07/16 17:58:33 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.jmodule.object;

import com.idega.jmodule.*;
import com.idega.jmodule.object.textObject.Text;
import java.io.*;
import com.idega.servlet.IWCoreServlet;
import com.idega.util.FrameStorageInfo;
import java.util.Enumeration;
import java.util.Hashtable;
import com.idega.idegaweb.IWMainApplication;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/

public class Page extends ModuleObjectContainer {
  private String title;
  private Script theAssociatedScript;
  private boolean zeroWait = false;
  private String redirectInfo;
  private boolean doReload = false;
  private String linkColor = "#000000";
  private String visitedColor = "#000000";
  private String hoverColor = "#000000";
  private String textDecoration = "underline";
  private String pageStyleFont = Text.FONT_FACE_ARIAL;
  private String pageStyleFontSize = Text.FONT_SIZE_7_STYLE_TAG;
  private String pageStyleFontStyle = Text.FONT_FACE_STYLE_NORMAL;
  private String styleSheetURL = "/style/style.css";
  private boolean addStyleSheet = false;
  private Hashtable frameProperties;
  private boolean isTemplate=false;



  protected static final String ROWS_PROPERTY = "ROWS";

  private static final String IW_FRAME_PARAMETER="idegaweb_frame_page_s";
  protected static final String IW_PAGE_KEY = "idegaweb_page";
  public static final String IW_FRAME_STORAGE_PARMETER = IWMainApplication.windowOpenerParameter;

  private final static String START_TAG="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">\n<html>";
  private final static String END_TAG="</html>";

  private static final String slash = "/";


  public Page() {
    this("");
  }

  public Page(String s) {
    super();
    setTitle(s);
  }

  public void setBackgroundColor(String color) {
    setAttribute("bgcolor",color);
  }

  public void setTextColor(String color) {
    setAttribute("text",color);
  }

  public void setAlinkColor(String color) {
    setAttribute("alink",color);
  }

  public void setHoverColor(String color) {
    setAttribute("alink",color);
    this.hoverColor = color;
  }

  public void setTextDecoration(String textDecoration) {
    this.textDecoration = textDecoration;
  }

  public void setStyleSheetURL(String styleSheetURL) {
    this.addStyleSheet = true;
    this.styleSheetURL = styleSheetURL;
  }

  public void setVlinkColor(String color) {
    setAttribute("vlink",color);
    this.visitedColor = color;
  }

  public void setLinkColor(String color) {
    setAttribute("link",color);
    this.linkColor = color;
  }

  public void setPageFontFace(String TextFontFace) {
    this.pageStyleFont = TextFontFace;
  }

  public void setPageFontSize(String TextFontSize) {
    this.pageStyleFont = TextFontSize;
  }

  public void setPageFontStyle (String TextFontStyle) {
    this.pageStyleFontStyle = TextFontStyle;
  }

  public String getPageFontFace() {
    return this.pageStyleFont;
  }

  public String getPageFontSize() {
    return this.pageStyleFont;
  }

  public String getPageFontStyle () {
    return this.pageStyleFontStyle;
  }

  public void setTitle(String title) {
    setName(title);
  }

  public void setMarginWidth(int width) {
    setAttribute("marginwidth",Integer.toString(width));
  }

  public void setMarginHeight(int height) {
    setAttribute("marginheight",Integer.toString(height));
  }

  public void setLeftMargin(int leftmargin) {
    setAttribute("leftmargin",Integer.toString(leftmargin));
  }

  public void setTopMargin(int topmargin) {
    setAttribute("topmargin",Integer.toString(topmargin));
  }

  public void setAllMargins(int allMargins) {
    setMarginWidth(allMargins);
    setMarginHeight(allMargins);
    setLeftMargin(allMargins);
    setTopMargin(allMargins);
  }

  public String getTitle() {
    return getName();
  }

  public void setAssociatedScript(Script myScript) {
    theAssociatedScript = myScript;
  }

  private void initializeAssociatedScript() {
    if(theAssociatedScript == null) {
      theAssociatedScript = new Script();
    }
  }

  public Script getAssociatedScript() {
    initializeAssociatedScript();
    return this.theAssociatedScript;
  }

  public void setBackgroundImage(String imageURL) {
    setAttribute("background",imageURL);
  }

  public void setBackgroundImage(Image backgroundImage) {
    setAttribute("background",backgroundImage.getURL());
  }

  public void setOnLoad(String action) {
    setAttribute("onLoad",action);
  }

  public void setOnUnLoad(String action) {
    setAttribute("onUnLoad",action);
  }

  /**
   * Sets the window to close immediately
   */
  public void close() {
    setOnLoad("window.close()");
  }

  public void setToGoBack() {
    setOnLoad("history.go(-1)");
  }


  //Sets the parent (caller) window to reload on Unload
  public void setParentToReload() {
    setOnUnLoad("window.opener.location.reload()");
  }

  // Displaying an Alert
  // aron@idega.is
  public void setToLoadAlert(String sMessage) {
    setOnLoad("alert('"+sMessage+"')");
  }

  public boolean doPrint(ModuleInfo modinfo) {
    boolean returnBoole;
    if (modinfo.getRequest().getParameter("idegaspecialrequesttype") == null) {
      returnBoole = true;
    }
    else if (modinfo.getRequest().getParameter("idegaspecialrequesttype").equals("page") && modinfo.getRequest().getParameter("idegaspecialrequestname").equals(this.getName())) {
      returnBoole = true;
    }
    else {
      returnBoole = false;
    }

    return returnBoole;
  }

  private void setDefaultAttributes(ModuleInfo modinfo) {
    if (!isAttributeSet("bgcolor")) {
      setBackgroundColor(modinfo.getDefaultBackgroundColor());
    }
  }

  public void setToReload() {
    doReload = true;
  }

  public void setToRedirect(String URL) {
    zeroWait = true;
    setToRedirect(URL,0);
  }

  public void setToRedirect(String URL,int secondInterval) {
    redirectInfo = "" + secondInterval + " ;URL=" + URL;
  }

  public String getRedirectInfo() {
    return redirectInfo;
  }

  protected void prepareClone(ModuleObject newObjToCreate) {
   super.prepareClone(newObjToCreate);
   Page newPage = (Page)newObjToCreate;
   newPage.title = this.title;
   Script newScript = (Script)this.theAssociatedScript;
   if(newScript!=null){
    newPage.theAssociatedScript = (Script)newScript.clone();
   }
   newPage.zeroWait = this.zeroWait;
   newPage.redirectInfo = this.redirectInfo;
   newPage.doReload = this.doReload;
   newPage.linkColor = this.linkColor;
   newPage.visitedColor = this.visitedColor;
   newPage.hoverColor = this.hoverColor;
  }

  public synchronized Object clone() {
    Page obj = null;
    try {
      obj = (Page)super.clone();
      if (this.theAssociatedScript != null) {
        obj.theAssociatedScript = (Script)this.theAssociatedScript.clone();
      }
      obj.title = this.title;
      obj.zeroWait = this.zeroWait;
      obj.redirectInfo = this.redirectInfo;
      obj.doReload = this.doReload;
      obj.linkColor = this.linkColor;
      obj.visitedColor = this.visitedColor;
      obj.hoverColor = this.hoverColor;
      obj.textDecoration = this.textDecoration;
      obj.styleSheetURL = this.styleSheetURL;
      obj.addStyleSheet = this.addStyleSheet;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }

    return obj;
  }




  public void main(ModuleInfo modinfo) throws Exception {
    if (doReload) {
      if(modinfo.getSession().getAttribute("idega_special_reload") != null) {
        modinfo.getSession().removeAttribute("idega_special_reload");
      }
      else {
        setToRedirect(modinfo.getRequest().getRequestURI());
        modinfo.getSession().setAttribute("idega_special_reload","true");
      }
    }
  }

  public void print(ModuleInfo modinfo) throws Exception {
    initVariables(modinfo);
    setDefaultAttributes(modinfo);

    if (getLanguage().equals("HTML")) {
      //StringBuffer buf= new StringBuffer();
      //buf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">\n<html>");
      println(getStartTag());
      if(zeroWait) {
        this.setDoPrint(false);
      }
      println("\n<head>");
      if (getAssociatedScript() != null) {
        getAssociatedScript().print(modinfo);
      }
      //println("\n<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\">\n<meta name=\"generator\" content=\"idega arachnea 1.2\">\n<meta name=\"author\" content=\"idega.is\">\n<meta name=\"copyright\" content=\"idega.is\">\n");
      //if (getRedirectInfo() != null) {
      //  println("<meta http-equiv=\"refresh\" content=\""+getRedirectInfo()+"\">");
      //}

      println(getMetaInformation(modinfo));
      println("<title>"+getTitle()+"</title>");
      if (addStyleSheet) {
        println("<link rel=\"stylesheet\" href=\""+styleSheetURL+"\" type=\"text/css\">\n");
      }
      else {
        println("<STYLE TYPE=\"text/css\">\n<!--\n	A:link {color:"+linkColor+"; text-decoration:"+textDecoration+";}\n	A:visited {color:"+visitedColor+"; text-decoration:"+textDecoration+";}\n	A:hover {color:"+hoverColor+"; text-decoration:"+textDecoration+";}\n	body {  font-family: "+ pageStyleFont +"; font-size: "+pageStyleFontSize+"; font-style: "+pageStyleFontStyle+ ";}\n   -->\n</STYLE>");
     }
      println("</head>\n<body  "+getAttributeString()+" >\n");

      //Catch all exceptions that are thrown in print functions of objects stored inside
      try {
        super.print(modinfo);
      }
      catch(Exception ex) {
        println("<h1>Villa var&eth;!</h1>");
        println("IW Error");
        println("<pre>");
        ex.printStackTrace(modinfo.getResponse().getWriter());
        println("</pre>");
      }

      println("\n</body>");
      println(getEndTag());
        //}
    }
    else if (getLanguage().equals("WML")) {
      println("<?xml version=\"1.0\"?>");
      println("<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\" \"http://www.wapforum.org/DTD/wml_1.1.xml\">");
      println("<wml>");
      println("<card title=\""+getTitle()+"\" id=\"card1\">");


      //Catch all exceptions that are thrown in print functions of objects stored inside
      try {
        super.print(modinfo);
      }
      catch(Exception ex) {
        println("<p>Villa var&eth;!</p>");
        println("<p>IWError</p>");
        println("<p>");
        ex.printStackTrace(modinfo.getResponse().getWriter());
        println("</p>");
      }

      println("</card>");
      println("</wml>");
    }
  }

  public void setProperty(String key, String values[]) {
    if (key.equalsIgnoreCase("title")) {
      setTitle(values[0]);
    }
  }

  public static String getStartTag(){
    return START_TAG;
  }

  public static String getEndTag(){
    return END_TAG;
  }

  public String getMetaInformation(ModuleInfo modinfo){
      String theReturn = "\n<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\">\n<meta name=\"generator\" content=\"idega arachnea 1.2\">\n<meta name=\"author\" content=\"idega.is\">\n<meta name=\"copyright\" content=\"idega.is\">\n";
      if (getRedirectInfo() != null) {
        theReturn += "<meta http-equiv=\"refresh\" content=\""+getRedirectInfo()+"\">";
      }
      return theReturn;
  }

  /**
   * Used to find the Page object to be printed in top of the current page
   */
  public static Page getPage(ModuleInfo modinfo){
      String frameKey = modinfo.getParameter(IW_FRAME_STORAGE_PARMETER);

      if(frameKey==null){
      /**
       * Inside a top level page:
       */
       Page page =  (Page) IWCoreServlet.retrieveObject(IW_PAGE_KEY);
       return page;
      }
      else{

        Page page = getPage(getFrameStorageInfo(modinfo),modinfo);
        return page;
        //return getPageFromSession(modinfo,frameKey);
      }
      /*Page page = (Page)retrieveObject("idega_page");
        if (page==null){
          String servletName = this.getServletConfig().getServletName();
          String attributeKey =servletName+"_idega_page";
          Page newPage = (Page)getServletContext().getAttribute(attributeKey);
          //System.out.println("AttributeKey="+attributeKey+" for getPage()");
          page = (Page)newPage.clone();
          storeObject("idega_page",page);
        }
        return page;*/
  }


  private static FrameStorageInfo getFrameStorageInfo(ModuleInfo modinfo){
    String key = modinfo.getParameter(IW_FRAME_STORAGE_PARMETER);
    FrameStorageInfo info =  (FrameStorageInfo)modinfo.getSessionAttribute(key);
    if(info==null){
      info = FrameStorageInfo.EMPTY_FRAME;
    }
    return info;
  }



  private static Page getPage(FrameStorageInfo info,ModuleInfo modinfo){
      String key = info.getStorageKey();
      Page theReturn = (Page)modinfo.getSessionAttributeWeak(key);
      if(theReturn ==null){
        try{
          theReturn = (Page)info.getFrameClass().newInstance();
        }
        catch(Exception ex){
          if(theReturn==null){
            theReturn = new Page("Expired");
            theReturn.add("This page has expired");
          }
          ex.printStackTrace();
        }
        storePage(theReturn,modinfo);
      }
      return theReturn;
  }



  public static void storePage(Page page,ModuleInfo modinfo){
      String storageKey = page.getID();
      String infoKey=storageKey;
      FrameStorageInfo info = new FrameStorageInfo(storageKey,page.getClass());
      modinfo.setSessionAttribute(infoKey,info);
      modinfo.setSessionAttributeWeak(storageKey,page);
    }


  public static void setTopPage(Page page){
    IWCoreServlet.storeObject(IW_PAGE_KEY,page);
  }


  public static boolean isRequestingTopPage(ModuleInfo modinfo){
    return !modinfo.isParameterSet(IW_FRAME_PARAMETER);
  }



  protected void setFrameProperty(String propertyName,String propertyValue){
    if(frameProperties ==null){
      frameProperties = new Hashtable();
    }
    frameProperties.put(propertyName,propertyValue);
  }

  protected void setFrameProperty(String propertyName){
    setFrameProperty(propertyName,slash);
  }

  protected String getFrameProperty(String propertyName){
    if(frameProperties == null){
      return null;
    }
    return (String)frameProperties.get(propertyName);
  }

  protected String getFramePropertiesString(){
    StringBuffer returnString = new StringBuffer();
    String Attribute ="";
    if (this.attributes != null) {
      Enumeration e = frameProperties.keys();
      while (e.hasMoreElements()) {
        Attribute = (String)e.nextElement();
        if(!Attribute.equals(ROWS_PROPERTY)){
          returnString.append(" ");
          returnString.append(Attribute);

          String AttributeValue = (String)frameProperties.get(Attribute);
          if(!AttributeValue.equals(slash)){

            returnString.append("=\"");
            returnString.append(AttributeValue);
            returnString.append("\" ");

          }

        }
      }
    }
    return returnString.toString();
  }

  public void setTemplate(boolean isTemlpate){
    this.isTemplate=isTemplate;
  }

  public boolean isTemplate(){
    return isTemplate;
  }
}
