/*
 * $Id: ModuleObject.java,v 1.19 2001/08/27 20:37:37 gummi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.jmodule.object;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import java.sql.*;
import com.idega.util.database.*;
import com.idega.core.data.*;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWMainApplication;
import javax.swing.event.EventListenerList;
import com.idega.event.IWEvent;
import com.idega.event.IWLinkEvent;
import com.idega.event.IWSubmitEvent;
import com.idega.event.IWLinkListener;
import com.idega.event.IWSubmitListener;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.data.EntityFinder;
import com.idega.exception.ICObjectNotInstalledException;


/**
 *@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 *@version 1.2
 */
public class ModuleObject extends Object implements Cloneable {
  //private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb";
  private final static String IW_BUNDLE_IDENTIFIER="com.idega.core";

  protected static final String slash = "/";

  private HttpServletRequest Request;
  private HttpServletResponse Response;
  private PrintWriter out;
  private String interfaceStyle;
  private String language;
  public Hashtable attributes;
  private String name;
  protected ModuleObject parentObject;
  private boolean doPrint = true;
  private String errorMessage;
  protected boolean hasBeenAdded = false;
  protected String treeID;
  private boolean goneThroughMain = false;
  private int ib_object_instance_id;
  private static String emptyString = "";
  public static String sessionEventStorageName = IWMainApplication.IWEventSessionAddressParameter;
  public EventListenerList listenerList = null;
  private Hashtable eventAttributes = null;
  private static long InstnceUniqueID;
  private String UniqueInstnceName;
  private boolean listenerAdded = false;
  public String eventLocationString = "";
  private ModuleInfo eventModuleInfo = null;

  /**
   * Default constructor
   */
  public ModuleObject() {
  }

  /**
   * @return The parent (subclass of ModuleObjectContainer) of the current object
   */
  public ModuleObject getParentObject(){
    return parentObject;
  }

  public void setID() {
    int hashCode = hashCode();
    if (hashCode < 0) {
      hashCode = -hashCode;
    }
    setID("id"+hashCode);
  }

  public String getID() {
    String theReturn = getAttribute("id");
    if (theReturn == null || this.emptyString.equals(theReturn)) {
      setID();
      theReturn = getAttribute("id");
    }

    return theReturn;
  }

  public ModuleObject getRootParent() {
    ModuleObject tempobj = getParentObject();
    if (tempobj == null) {
      return null;
    }
    else {
      while (tempobj.getParentObject() != null ) {
        tempobj = tempobj.getParentObject();
      }
      return tempobj;
    }
  }

  public void setParentObject(ModuleObject modobj) {
    parentObject = modobj;
  }

  /**
   * Initializes variables contained in the ModuleInfo object
   */
  public void initVariables(ModuleInfo modinfo) throws IOException {
    this.Request = modinfo.getRequest();
    this.Response = modinfo.getResponse();
    this.language = modinfo.getLanguage();
    this.interfaceStyle = modinfo.getInterfaceStyle();
    if (language == null) {
      language = "HTML";
    }
    if (interfaceStyle == null) {
      interfaceStyle = "default";
    };
    this.out = modinfo.getWriter();
  }

  public void setDoPrint(boolean ifDoPrint) {
    this.doPrint = ifDoPrint;
  }

  public boolean doPrint(ModuleInfo modinfo) {
    if (this.doPrint) {
      ModuleObject parent = getParentObject();
      if (parent == null) {
        return this.doPrint;
      }
      else {
        return parent.doPrint(modinfo);
      }
    }
    else {
      return false;
    }
  }

  protected void setAttribute(Hashtable attributes) {
    this.attributes = attributes;
  }

  public void setAttribute(String attributeName,String attributeValue) {
    if (this.attributes == null) {
      this.attributes = new Hashtable();
    }
    this.attributes.put((Object) attributeName,(Object) attributeValue);
  }

  public void setAttribute(String attributeName){
    setAttribute(attributeName,slash);
  }

  public String getAttribute(String attributeName) {
    if (this.attributes != null){
      return (String)this.attributes.get((Object)attributeName);
    }
    else {
      return null;
    }
  }

  public boolean isAttributeSet(String attributeName) {
    if (getAttribute(attributeName) == null) {
      return false;
    }
    else {
      return true;
    }
  }

  public Hashtable getAttributes() {
    return this.attributes;
  }

  public String getAttributeString() {
    StringBuffer returnString = new StringBuffer();
    String Attribute ="";

    if (this.attributes != null) {
      Enumeration e = attributes.keys();
      while (e.hasMoreElements()) {
        Attribute = (String)e.nextElement();
        returnString.append(" ");
        returnString.append(Attribute);
        String attributeValue=getAttribute(Attribute);
        if(!attributeValue.equals(slash)){
          returnString.append("=\"");
          returnString.append(attributeValue);
          returnString.append("\"");
        }
        returnString.append("");
      }
    }

    return returnString.toString();
  }

  /**
   * Gets the name of this object
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name of this object
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Flushes the buffer in the printwriter out
   */
  public void flush() {
    out.flush();
  }

  /**
   * Uses the default PrintWriter object to print out a string
   */
  public void print(String string) {
    out.print(string);
  }

  /**
   * Uses the default PrintWriter object to print out a string with the endline character
   */
  public void println(String string) {
    out.println(string);
  }

  public void _print(ModuleInfo modinfo) throws Exception {
    this.print(modinfo);
  }

  /**
   * The default implementation for the print function
   */
  public void print(ModuleInfo modinfo) throws Exception {
    initVariables(modinfo);
    if (modinfo.getLanguage().equals("WML")) {
      getResponse().setContentType("text/vnd.wap.wml");
    }
  }

  /**
   * @deprecated Do not use this function, it is not safe
   * @resturn The Response object for the page
   */
  public HttpServletRequest getRequest() {
    return this.Request;
  }

  /**
   * @deprecated Do not use this function, it is not safe
   * @return The Request object for the page
   */
  public HttpServletResponse getResponse() {
    return this.Response;
  }

  /**
   * @return The "layout" language used and supplied by the ModuleInfo
   */
  public String getLanguage() {
    return this.language;
  }

  public void setID(String ID) {
    setAttribute("id",ID);
  }

  public void setID(int ID) {
    setAttribute("id",Integer.toString(ID));
  }

  /**
   * @return The interface style supplied by the ModuleInfo (optional)
   */
  public String getInterfaceStyle() {
    return this.interfaceStyle;
  }

  public PrintWriter getPrintWriter() {
    return out;
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

  /**
   * @return The Class name of the Object
   */
  public String getClassName() {
    return this.getClass().getName();
  }

  /**
   * Encodes a string to call special request such as pop-up windows in HTML
   */
  public String encodeSpecialRequestString(String RequestType,String RequestName, ModuleInfo modinfo) {
    String theOutput = "";
    theOutput = modinfo.getRequest().getRequestURI();
    theOutput = theOutput + "?idegaspecialrequesttype=" + RequestType + "&idegaspecialrequestname=" + RequestName;
    return theOutput;
  }

  /**
   * Sets the associated (attached) script object to this object
   */
  public void setAssociatedScript(Script myScript) {
    if (getRootParent() != null) {
      getRootParent().setAssociatedScript(myScript);
    }
  }

  /**
   * @return The associated (attached) script or null if there is no Script associated
   */
  public Script getAssociatedScript() {
    if (getRootParent() != null) {
      return getRootParent().getAssociatedScript();
    }
    else {
      return null;
    }
  }

  /**
   * @return The enclosing Page object
   */
  public Page getParentPage() {
    Page returnPage = null;
    ModuleObject obj = getParentObject();
    while (obj != null) {
      if (obj instanceof Page) {
        returnPage = (Page)obj;
      }
      obj = obj.getParentObject();
    }
    return returnPage;
  }

  public void main(ModuleInfo modinfo) throws Exception {
  }

  protected void prepareClone(ModuleObject newObjToCreate) {
  }

  public synchronized Object clone() {
    ModuleObject obj = null;
    try {
      //This is forbidden in clone i.e. "new":
      //obj = (ModuleObject)Class.forName(this.getClassName()).newInstance();
      obj = (ModuleObject)super.clone();
      if (this.attributes != null) {
        obj.setAttribute((Hashtable)this.attributes.clone());
      }
      obj.setName(this.name);
      //obj.setParentObject(this.parentObject);
      this.prepareClone(obj);
      Vector vector;

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }

    return obj;
  }

  /**
   * Function invoked before the print function
   */
  public void _main(ModuleInfo modinfo) throws Exception {
    if (!goneThroughMain) {
      initVariables(modinfo);
      main(modinfo);
    }
    goneThroughMain = true;
  }


  protected void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  protected String getErrorMessage() {
    return this.errorMessage;
  }

  public void setAsPrinted(boolean printed) {
    doPrint = printed;
  }

  /*public void setTreeID(String treeID) {
    this.treeID = treeID;
  }

  public String getTreeID() {
    return treeID;
  }*/


  public void setICObjectInstanceID(int id) {
    this.ib_object_instance_id = id;
  }


  public void setICObjectInstance(ICObjectInstance instance) {
    this.ib_object_instance_id = instance.getID();
  }

  /**
   * owerwrite in module
   */

  public int getICObjectInstanceID(ModuleInfo modinfo) throws SQLException {
    return getICObjectInstanceID();
  }

  public int getICObjectInstanceID(){
    return this.ib_object_instance_id;
  }

  public ICObjectInstance getICObjectInstance(ModuleInfo modinfo) throws SQLException {
    return getICObjectInstance();
  }

  public ICObjectInstance getICObjectInstance()throws SQLException{
    return new ICObjectInstance(getICObjectInstanceID());
  }

  public ICObject getICObject() throws SQLException {
    return this.getICObject(this.getClass());
  }

  protected ICObject getICObject(Class c) throws SQLException {
    List result = EntityFinder.findAllByColumn(ICObject.getStaticInstance(ICObject.class),"class_name",c.getName());
    if(result != null && result.size() > 0){
      return (ICObject)result.get(0);
    }else{
      throw new ICObjectNotInstalledException(this.getClass().getName());
    }
  }


  public ICObjectInstance getICInstance(ModuleInfo modinfo) throws IWException {
    try {
      return new ICObjectInstance(getICObjectInstanceID(modinfo));
    }
    catch (Exception excep) {
      IWException exep = new IWException(excep.getMessage());
      throw (IWException) excep.fillInStackTrace();
    }
  }

  public void addIWLinkListener(IWLinkListener l,ModuleInfo modinfo) {
    //System.err.println(this.getClass().getName() + " : listener added of type -> " + l.getClass().getName());
    getEventListenerList().add(IWLinkListener.class,l);
  }

  public IWLinkListener[] getIWLinkListeners() {
    return (IWLinkListener[])getEventListenerList().getListeners(IWLinkListener.class);
  }

  public void addIWSubmitListener(IWSubmitListener l,ModuleInfo modinfo){
    getEventListenerList().add(IWSubmitListener.class,l);
  }

    public IWSubmitListener[] getIWSubmitListeners(){
      if (listenerList == null){
        listenerList = new EventListenerList();
      }
      return (IWSubmitListener[])listenerList.getListeners(IWSubmitListener.class);
    }


    public void setEventAttribute(String attributeName,Object attributeValue){
    if (this.eventAttributes == null){
      this.eventAttributes = new Hashtable();
    }
    this.eventAttributes.put((Object) attributeName,(Object) attributeValue);
    }

    public Object getEventAttribute(String attributeName){
      if (this.eventAttributes != null){
        return this.eventAttributes.get((Object) attributeName);
      }
      else{
        return null;
      }
    }



     public void dispatchEvent(IWEvent e) {
          processEvent(e);
    }


      protected void processEvent(IWEvent e) {

          if (e instanceof IWLinkEvent) {
              processIWLinkEvent((IWLinkEvent)e);

          } else if (e instanceof IWSubmitEvent) {
              processIWSubmitEvent((IWSubmitEvent)e);
          } else{
              System.err.println("unable to prosess event: " + e);
          }
      }

      protected void processIWLinkEvent(IWLinkEvent e) {
        ModuleObject obj = (ModuleObject)e.getSource();
        // Guaranteed to return a non-null array
        IWLinkListener[] listeners = obj.getIWLinkListeners();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-1; i>=0; i--) {
          ((IWLinkListener)listeners[i]).actionPerformed(e);
        }
      }

      protected void processIWSubmitEvent(IWSubmitEvent e) {
        ModuleObject obj = (ModuleObject)e.getSource();
        // Guaranteed to return a non-null array
        IWSubmitListener[] listeners = obj.getIWSubmitListeners();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-1; i>=0; i--) {
          ((IWSubmitListener)listeners[i]).actionPerformed(e);
        }
      }




    /**
     * unimplemented
     */
    public void fireEvent(){
    }

    public void endEvent(ModuleInfo modinfo){
      modinfo.removeSessionAttribute(eventLocationString);
    }

    public void listenerAdded(boolean added){
      listenerAdded = added;
    }

    public boolean listenerAdded(){
      return listenerAdded;
    }

  public void setModuleInfo(ModuleInfo modinfo){
  //  System.err.println(this.getClass().getName() + ": modinfo set");
    eventModuleInfo = modinfo;
  }

  public EventListenerList getEventListenerList(){
    if (listenerList == null){
        listenerList = new EventListenerList();
    }
    return listenerList;
  }

  /**
   * @deprecated Do not use this function, it is not safe
   */
  public ModuleInfo getEventModuleInfo(){
    return eventModuleInfo;
  }

  public void _setModuleInfo(ModuleInfo modinfo){
    setModuleInfo(modinfo);
  }

  public void setProperty(String key, String values[]){
  }

  /**
   * Needs to be overrided to get the right IWBundle identifier for the object
   */
  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public IWBundle getBundle(ModuleInfo modinfo){
    IWMainApplication iwma = modinfo.getApplication();
    return iwma.getBundle(getBundleIdentifier());
  }

  public IWResourceBundle getResourceBundle(ModuleInfo modinfo){
    IWBundle bundle = getBundle(modinfo);
    if(bundle!=null){
      return bundle.getResourceBundle(modinfo.getCurrentLocale());
    }
    return null;
  }

  public String getLocalizedString(String key,ModuleInfo modinfo){
    IWResourceBundle bundle = getResourceBundle(modinfo);
    if(bundle!=null){
      return bundle.getLocalizedString(key);
    }
    return null;
  }

}
