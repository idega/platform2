//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import java.sql.*;
import com.idega.util.database.*;
import com.idega.builder.data.*;
import com.idega.idegaweb.IWException;
//added by gummi@idega.is
//begin
import com.idega.idegaweb.IWMainApplication;
import javax.swing.event.EventListenerList;
import com.idega.event.IWActionEvent;
import com.idega.event.IWActionListener;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.AWTEvent;
import java.awt.EventQueue;
//end


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class ModuleObject extends Object implements Cloneable{

private HttpServletRequest Request;
private HttpServletResponse Response;
private PrintWriter out;
private String interfaceStyle;
private String language;
public Hashtable attributes;
//private Connection Conn;  //was to be part of moduleinfo - but now changed
private String name;
protected ModuleObject parentObject;
private boolean doPrint=true;
private String errorMessage;
protected boolean hasBeenAdded=false;
protected String treeID;
private boolean goneThroughMain=false;
private int ib_object_instance_id;
private static String emptyString="";

//added by gummi@idega.is
//begin
public static String sessionEventStorageName= IWMainApplication.IWEventSessionAddressParameter;  //gummi@idega.is
public EventListenerList listenerList = new EventListenerList(); //gummi@idega.is
public IWActionEvent actionEvent = null;  //gummi@idega.is
private Hashtable eventAttributes = null;
private static long InstnceUniqueID;
private String UniqueInstnceName;
private boolean listenerAdded = false;
//end


/**

 * Default constructor

 */
public ModuleObject(){
	//this.attributes = new Hashtable();
}

/**

 * Returns the parent (subclass of ModuleObjectContainer) of the current object

 */
public ModuleObject getParentObject(){
	return parentObject;
}

public void setID(){
	int hashCode = hashCode();
	if (hashCode < 0){
		hashCode = -hashCode;
	}
	setID("id"+hashCode);
}

public String getID(){
	String theReturn = getAttribute("id");
        if(theReturn.equals(this.emptyString)){
          setID();
          theReturn=getAttribute("id");
        }
        return theReturn;

}

public ModuleObject getRootParent(){
	ModuleObject tempobj = getParentObject();
	if (tempobj == null){
		return null;
	}
	else{
		while ( tempobj.getParentObject() != null ){
			tempobj = tempobj.getParentObject();
		}
		return tempobj;
	}
}

public void setParentObject(ModuleObject modobj){
	parentObject=modobj;
}


/**
*Initializes variables contained in the ModuleInfo object
**/
public void initVariables(ModuleInfo modinfo) throws IOException{
	this.Request= modinfo.getRequest();
	this.Response= modinfo.getResponse();
	this.language = modinfo.getLanguage();
	this.interfaceStyle = modinfo.getInterfaceStyle();
	if (language == null){language = "HTML";}
	if (interfaceStyle == null) { interfaceStyle = "default"; };
	this.out = this.Response.getWriter();
//	this.Conn = modinfo.getConnection();

}

public void setDoPrint(boolean ifDoPrint){
  this.doPrint=ifDoPrint;
}

public boolean doPrint(ModuleInfo modinfo){
	if(this.doPrint){
          ModuleObject parent = getParentObject();
          if(parent == null){
                  return this.doPrint;
          }
          else{
                  return parent.doPrint(modinfo);
          }
        }
        else{
          return false;
        }
}

protected void setAttribute(Hashtable attributes){
	this.attributes = attributes;
}

public void setAttribute(String attributeName,String attributeValue){
	if (this.attributes == null){
		this.attributes = new Hashtable();
	}
	this.attributes.put((Object) attributeName,(Object) attributeValue);
}

public String getAttribute(String attributeName){
	if (this.attributes != null){
		return (String) this.attributes.get((Object) attributeName);
	}
	else{
		return "";
	}
}

public boolean isAttributeSet(String attributeName){
	if (getAttribute(attributeName) == null){
		return false;
	}
	else{
		return true;
	}
}

public Hashtable getAttributes(){
	return this.attributes;
}





public String getAttributeString(){
	StringBuffer returnString = new StringBuffer();
        //String returnString = "";
        String Attribute ="";

	if (this.attributes != null){
		/*for (Enumeration e = attributes.keys(); e.hasMoreElements();){

			String Attribute=(String)e.nextElement();
			String AttributeString = (String) Attribute;
			returnString = returnString + " " + AttributeString + "=\""+ getAttribute(Attribute)+"\" ";
		}*/
                //eiki jan 2001
                Enumeration e = attributes.keys();
                while(e.hasMoreElements()){

			Attribute=(String)e.nextElement();
                        returnString.append(" ");
                        returnString.append(Attribute);
                        returnString.append("=\"");
                        returnString.append(getAttribute(Attribute));
                        returnString.append("\" ");
		}
	}
	return returnString.toString();

}

/**

 * Gets the name of this object

 */
public String getName(){
	return this.name;
}


/**

 * Sets the name of this object

 */
public void setName(String name){
	this.name=name;
}


/**

 * Flushes the buffer in the printwriter out

 */
public void flush(){
	out.flush();
}


/**

 * Uses the default PrintWriter object to print out a string

 */
public void print(String string){
	//out.print(s+"Thread:"+Thread.currentThread().hashCode());
	out.print(string);
	//flush();
}

/**

 * Uses the default PrintWriter object to print out a string with the endline character

 */
public void println(String string){
	out.println(string);
	//out.println(s+"Thread:"+Thread.currentThread().hashCode());
	//flush();
}

/**
*the default implementation for the print function
**/
public void print(ModuleInfo modinfo) throws IOException{
	initVariables(modinfo);
	if (modinfo.getLanguage().equals("WML")){
		getResponse().setContentType("text/vnd.wap.wml");
	}
}

/**
*@deprecated Do not use this function, it is not safe
*Returns the Response object for the page
**/
public HttpServletRequest getRequest(){
	return this.Request;
}

/**
*@deprecated Do not use this function, it is not safe
*Returns the Request object for the page
**/
public HttpServletResponse getResponse(){
	return this.Response;
}

/**
*returns the "layout" language used and supplied by the ModuleInfo
**/
public String getLanguage(){
	return this.language;
}

public void setID(String ID){
	setAttribute("id",ID);
}

public void setID(int ID){
	setAttribute("id",Integer.toString(ID));
}

/**
*returns the interface style supplied by the ModuleInfo (optional)
*/
public String getInterfaceStyle(){
	return this.interfaceStyle;
}

public PrintWriter getPrintWriter(){
	return out;
	//return getResponse().getWriter();
}

/**
*returns the default DatabaseConnection
**/
public Connection getConnection(){
	return ConnectionBroker.getConnection();
}

public void freeConnection(Connection conn){
	ConnectionBroker.freeConnection(conn);
}

/**
*returns the Class name of the Object
**/
public String getClassName(){
	return this.getClass().getName();
}

/**
*Encodes a string to call special request such as pop-up windows in HTML
*
*/
public String encodeSpecialRequestString(String RequestType,String RequestName, ModuleInfo modinfo){
	String theOutput = "";
	theOutput = modinfo.getRequest().getRequestURI();
	theOutput = theOutput + "?idegaspecialrequesttype="+RequestType+"&idegaspecialrequestname="+RequestName;
	return theOutput;
}

/**
**Sets the associated (attached) script object to this object
**/
public void setAssociatedScript(Script myScript){
	if (getRootParent() != null){
		getRootParent().setAssociatedScript(myScript);
	}
}


/**
*
*Returns the associated (attached) script or null if there is no Script associated
**/
public Script getAssociatedScript(){
	if (getRootParent() != null){
		return getRootParent().getAssociatedScript();
	}
	else{
		return null;
	}
}

/*
public void whenAdded(){

}*/


/**
*Returns the enclosing Page object
*/
public Page getParentPage(){
	Page returnPage = null;
	ModuleObject obj = getParentObject();
	while(obj != null){
		if(obj instanceof Page){
			returnPage = (Page) obj;
		}
		obj=obj.getParentObject();
	}
	return returnPage;
}

public void main(ModuleInfo modinfo)throws Exception{
}

protected void prepareClone(ModuleObject newObjToCreate){
}

public Object clone(){

	ModuleObject obj = null;

	try{
                //This is forbidden in clone i.e. "new":
		//obj = (ModuleObject)Class.forName(this.getClassName()).newInstance();
		obj=(ModuleObject)super.clone();

                if (this.attributes != null){
			obj.setAttribute((Hashtable)this.attributes.clone());
		}
		obj.setName(this.name);
		//obj.setParentObject(this.parentObject);
		this.prepareClone(obj);

	}
	catch(Exception ex){
          //System.err.println("Exception in ModuleObject: "+ex.getClass().getName()+": "+ex.getMessage());
          ex.printStackTrace(System.err);
	}
	return obj;

}

/**
 * Function invoked before the print function
 */
public void _main(ModuleInfo modinfo)throws Exception{
        if(!goneThroughMain){
        //try{
          initVariables(modinfo);
	  main(modinfo);
        }
        goneThroughMain=true;
        /*}
        catch(Throwable ex){
          setErrorMessage(ex.getMessage());
          System.err.println(ex.getMessage());
          ex.printStackTrace(System.err);
        }*/
        //System.out.println("Inside _main() for: "+this.getClass().getName()+" - Tread: "+Thread.currentThread().toString());
}


protected void setErrorMessage(String errorMessage){
  this.errorMessage=errorMessage;
}

protected String getErrorMessage(){
  return this.errorMessage;
}

/*
protected void setAsAdded(){
  hasBeenAdded=true;
}

protected boolean hasBeenAdded(){
  return hasBeenAdded;
}*/

public void setAsPrinted(boolean printed){
  doPrint=printed;
}


public void setTreeID(String treeID){
  this.treeID=treeID;
}

public String getTreeID(){
  return treeID;
}

/**
 * @deprecated Replaced with setIBObjectInstanceID()
 */
public void setIBObjectInstanceID(int id){
  this.ib_object_instance_id=id;
}

/**
 * @deprecated Replaced with setIBObjectInstance()
 */
public void setIBObjectInstance(IBObjectInstance instance){
  this.ib_object_instance_id=instance.getID();
}

/**
 * owerwrite in module
 */

/**
 * @deprecated Replaced with getIBObjectInstanceID()
 */
public int getIBObjectInstanceID(ModuleInfo modinfo) throws SQLException{
  return this.ib_object_instance_id;
}

/**
 * @deprecated Replaced with getIBObjectInstance()
 */
public IBObjectInstance getIBObjectInstance(ModuleInfo modinfo) throws SQLException{
  return new IBObjectInstance(getIBObjectInstanceID(modinfo));
}

/**
 * @deprecated Replaced with getIBObject()
 */
public IBObject getIBObject()throws SQLException{
/*  try {*/
      return (IBObject)(new IBObject()).findAllByColumn("class_name",this.getClass().getName())[0];
/*  }
  catch (Exception ex) {
    IBObject obj = new IBObject();
    obj.setClassName(this.getClass().getName());
    obj.setName("Untitled");
    obj.insert();
    return obj; //(IBObject)(new IBObject()).findAllByColumn("class_name",this.getClass().getName())[0];
  }
*/
}






public IBObjectInstance getIBInstance(ModuleInfo modinfo)throws IWException{
  try{
    return new IBObjectInstance(getIBObjectInstanceID(modinfo));
  }
  catch (Exception excep) {
    IWException exep = new IWException(excep.getMessage());
    throw (IWException) excep.fillInStackTrace();
  }
}




//added by gummi@idega.is
//begin

  public void addIWActionListener(IWActionListener l,ModuleInfo modinfo){
    listenerList.add(IWActionListener.class,l);
  }


  public IWActionListener[] getIWActionListeners(){
    return (IWActionListener[])listenerList.getListeners(IWActionListener.class);
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





  public void postIWActionEvent () {
    fireAction();
  }

  protected void fireAction() {
      // Guaranteed to return a non-null array
      IWActionListener[] listeners = getIWActionListeners();
      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length-1; i>=0; i--) {
              // Lazily create the event:
              System.out.println("Listeners" + i);
              if (actionEvent == null)
                  actionEvent = new IWActionEvent(this);
              ((IWActionListener)listeners[i]).actionPerformed(actionEvent);

      }
  }

  public void listenerAdded(boolean added){
    listenerAdded = added;
  }

  public boolean listenerAdded(){
    return listenerAdded;
  }

//end



}
