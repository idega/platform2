//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.textObject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.idegaweb.*;
import com.idega.idegaweb.IWURL;

//added by gummi@idega.is
//begin
import javax.swing.event.EventListenerList;
import java.awt.event.*;
import com.idega.event.IWLinkEvent;
import com.idega.event.IWLinkListener;
//end

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*@modified by  <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
*/
public class Link extends Text{

private ModuleObject obj;
private String ObjectType;
private Window myWindow;
private StringBuffer parameterString;
private boolean addSessionId = true;
private static String sessionStorageName=IWMainApplication.windowOpenerParameter;
private Form formToSubmit;
private Class windowClass;
private boolean maintainAllGlobalParameters = false;

private static final String TARGET_ATTRIBUTE = "target";

public Link(){
  this("");
}

public Link(String text){
  this( new Text(text) );
}

public Link(ModuleObject mo, Window myWindow){			//gummi@idega.is

	//super();

	this.myWindow = myWindow;
	myWindow.setParentObject(this);
	ObjectType="Window";

	obj = mo;
	obj.setParentObject(this);

}

public Link(Window myWindow){
	//super();
	//this.myWindow = myWindow;
	//myWindow.setParentObject(this);
	//ObjectType="Window";
        this(new Text(myWindow.getName()),myWindow);
}

public Link(ModuleObject mo){
	//super();
	obj = mo;
	obj.setParentObject(this);
	ObjectType="ModuleObject";
}

public Link(Text text){
	//super();
	text.setFontColor("");
	obj = (ModuleObject)text;
	obj.setParentObject(this);
	ObjectType="Text";

}

public Link(String text,String url){
	this(new Text(text),url);
}

public Link(ModuleObject mo,String url){
	//super();
	obj = mo;
	setURL(url);
	obj.setParentObject(this);
	ObjectType="ModuleObject";
}

public Link(Text text,String url){
	//super();
	text.setFontColor("");
	obj = (ModuleObject)text;
	setURL(url);
	obj.setParentObject(this);
	ObjectType="Text";
}

//for files
/**
 * @deprecated replaced with com.idega.jmodule.object.interfaceobject.FilePresentation
 */
public Link(int file_id){
	this(new Text("File"),"/servlet/FileModule?file_id="+file_id);
}

/**
 * @deprecated replaced with com.idega.jmodule.object.interfaceobject.FilePresentation
 */
public Link(int file_id,String file_name){
	this(new Text(file_name),"/servlet/FileModule?file_id="+file_id);
}


/**
 * @deprecated replaced with com.idega.jmodule.object.interfaceobject.FilePresentation
 */
public Link(ModuleObject mo,int file_id){
	super();
	obj = mo;
	setURL("/servlet/FileModule?file_id="+file_id);
	obj.setParentObject(this);
	ObjectType="ModuleObject";
}

/**
 * @deprecated replaced with com.idega.jmodule.object.interfaceobject.FilePresentation
 */
public Link(int file_id, Window myWindow){
	//super();
	this.myWindow = myWindow;
	myWindow.setParentObject(this);
	ObjectType="Window";
}



public Link(ModuleObject mo,Class classToInstanciate){
       this(mo,IWMainApplication.getObjectInstanciatorURL(classToInstanciate));
}



public Link(ModuleObject mo,String classToInstanciate,String template){
       this(mo,IWMainApplication.getObjectInstanciatorURL(classToInstanciate,template));
}


public Link(String displayText,Class classToInstanciate){
       this(displayText,IWMainApplication.getObjectInstanciatorURL(classToInstanciate));
}



public Link(String displayText,String classToInstanciate,String template){
       this(displayText,IWMainApplication.getObjectInstanciatorURL(classToInstanciate,template));
}


public void setWindow(Window window){
    myWindow=window;
    ObjectType="Window";
    myWindow.setParentObject(this);
}

public void setModuleObject(ModuleObject object){
  this.obj=object;
  ObjectType="ModuleObject";
  object.setParentObject(this);
}

//



/*public String getAttribute(String attributeName,ModuleInfo modinfo){
  if (attributeName.equals("href")){
    return super.getAttribute("href")+getParameterString(modinfo);
  }
  else{
    return getAttribute(attributeName);
  }
}


public String getAttributeString(ModuleInfo modinfo){
	String returnString="";
	if (this.attributes != null){
		for (Enumeration e = attributes.keys(); e.hasMoreElements();){

			String Attribute=(String)e.nextElement();
			String AttributeString = (String) Attribute;
			returnString = returnString + " " + AttributeString + "=\""+ getAttribute(Attribute,modinfo)+"\" ";
		}
	}
	return returnString;

}*/

public void main(ModuleInfo modinfo)throws Exception{
  if(ObjectType.equals("Window")){

    if(myWindow!=null){
      if(myWindow.getURL(modinfo).indexOf(IWMainApplication.windowOpenerURL)!=-1){
        String sessionParameterName=com.idega.servlet.WindowOpener.storeWindow(modinfo,myWindow);
        addParameter(sessionStorageName,sessionParameterName);
      }
      //String sessionParameterName=this.getID();
      //addParameter(sessionStorageName,sessionParameterName);
      //modinfo.setSessionAttribute(sessionParameterName,myWindow);
    }
  }
  /*if(link!=null){
    link.main(modinfo);
  }*/
}


public void setURL(String url){
	setAttribute("href",url);
}

public String getURL(){
	return getAttribute("href");
}

public void addParameter(Parameter parameter){
  addParameter(parameter.getName(),parameter.getValue());
}

public void addParameter(String ParameterName, String ParameterValue){
  if(ParameterName!=null){
    ParameterName = java.net.URLEncoder.encode(ParameterName);
  }
  if(ParameterValue!=null){
    ParameterValue = java.net.URLEncoder.encode(ParameterValue);
  }
  if( (ParameterName!=null) && (ParameterValue!=null)){
    if( parameterString==null ){
      parameterString = new StringBuffer();
      //parameterString.append("?");
      parameterString.append("&");
    }
    else parameterString.append("&");

    parameterString.append(ParameterName);
    parameterString.append("=");
    parameterString.append(ParameterValue);
  }
}

public void addParameter(String parameterName,int parameterValue){
   addParameter(parameterName,Integer.toString(parameterValue));
}

public void maintainParameter(String parameterName,ModuleInfo modinfo){
  String parameterValue = modinfo.getParameter(parameterName);
  if(parameterValue!=null){
    addParameter(parameterName,parameterValue);
  }
}

private void setOnEvent(String eventType,String eventString){
 //temp
 // String value = this.getAttribute(eventType);
 // if(value==null){
    setAttribute(eventType,eventString);
  //}
  //else{
 //   setAttribute(eventType,value+";"+eventString);
 // }
}

public void setOnFocus(String s){
	setOnEvent("onfocus",s);
}

public void setOnBlur(String s){
	setOnEvent("onblur",s);
}

public void setOnSelect(String s){
	setOnEvent("onselect",s);
}

public void setOnChange(String s){
	setOnEvent("onchange",s);
}

public void setOnClick(String s){
	setOnEvent("onclick",s);
}

public String getOnFocus(){
	return getAttribute("onfocus");
}

public String getOnBlur(){
	return getAttribute("onblur");
}

public String getOnSelect(){
	return getAttribute("onselect");
}

public String getOnChange(){
	return getAttribute("onchange");
}

public String getOnClick(){
	return getAttribute("onclick");
}

public void setTarget(String target){
	setAttribute(TARGET_ATTRIBUTE,target);
}

public String getTarget(){
  return getAttribute(TARGET_ATTRIBUTE);
}

public void setFontSize(String s){
    if(ObjectType.equals("Text")){
	((Text)obj).setFontSize(s);
    }
}

public void setFontSize(int i){
	setFontSize(Integer.toString(i));
}

public void setFontFace(String s){
    if(ObjectType.equals("Text")){
	((Text)obj).setFontFace(s);
    }
}

public void setFontColor(String color){
    if(ObjectType.equals("Text")){
	((Text)obj).setFontColor(color);
    }
}

public void setFontStyle(String style){
    if(ObjectType.equals("Text")){
	((Text)obj).setFontStyle(style);
    }
}

public void setSessionId(boolean addSessionId){
  this.addSessionId=addSessionId;
}

public void addBreak(){
    if(ObjectType.equals("Text")){
	((Text)obj).addBreak();
    }
}

public void setTeleType(){
    if(ObjectType.equals("Text")){
	((Text)obj).setTeleType();
    }
}


public void setBold(){
    if(ObjectType.equals("Text")){
	((Text)obj).setBold();
    }
}

public void setItalic(){
    if(ObjectType.equals("Text")){
	((Text)obj).setItalic();
    }
}

public void setUnderline(){
    if(ObjectType.equals("Text")){
	((Text)obj).setUnderline();
    }
}


public void setText(String text){
   if (ObjectType.equals("Text")){
      ((Text)obj).setText(text);
   }
}

public void addToText(String text){
   if (ObjectType.equals("Text")){
      ((Text)obj).addToText(text);
   }
}


public void setObject(ModuleObject object){
   obj=object;
   ObjectType="ModuleObject";

}

public ModuleObject getObject(){
  return obj;
}


private boolean isLinkOpeningOnSamePage(){
  return !isAttributeSet(TARGET_ATTRIBUTE);
}

  public synchronized Object clone() {
    Link linkObj = null;
    try {
      linkObj = (Link)super.clone();

      if(this.obj != null){
        linkObj.obj = (ModuleObject)this.obj.clone();
      }
      if(myWindow != null){
        linkObj.myWindow = (Window)this.myWindow.clone();
      }

      if(formToSubmit != null){
        linkObj.formToSubmit = (Form)this.formToSubmit.clone();
      }

      if(windowClass != null){
        linkObj.windowClass = this.windowClass;
      }


      linkObj.ObjectType = this.ObjectType;
      linkObj.parameterString = this.parameterString;
      linkObj.addSessionId = this.addSessionId;
      linkObj.maintainAllGlobalParameters=this.maintainAllGlobalParameters;

      if(this.parameterString != null){
        linkObj.parameterString = new StringBuffer(this.parameterString.toString());
      }

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }

    return linkObj;
  }

private void addTheMaintainedParameters(ModuleInfo modinfo){
  List list = com.idega.idegaweb.IWURL.getGloballyMaintainedParameters(modinfo);
  if(list!=null){
    Iterator iter = list.iterator();
    while (iter.hasNext()) {
      String parameterName = (String)iter.next();
      String parameterValue = modinfo.getParameter(parameterName);
      if(parameterValue!=null){
        addParameter(parameterName,parameterValue);
      }
    }
  }
}

public void setToMaintainGlobalParameters(){
  this.maintainAllGlobalParameters=true;
}

protected String getParameterString(ModuleInfo modinfo,String URL){
        if(maintainAllGlobalParameters){
          addTheMaintainedParameters(modinfo);
        }
        else{
          if(this.isLinkOpeningOnSamePage()){
            addTheMaintainedParameters(modinfo);
          }
        }

        if (URL == null){
          URL="";
        }

	if (parameterString == null){
          parameterString = new StringBuffer();
          if (addSessionId && (!modinfo.isSearchEngine()) ){
		if(URL.equals("#")){
                  return "";
                }
                else if(URL.indexOf("://") == -1){//does not include ://
                  if (URL.indexOf("?") != -1){
                    parameterString.append("&idega_session_id=");
                    parameterString.append(modinfo.getSession().getId());
                    return parameterString.toString();
                  }
                  else if ((URL.indexOf("//") != -1) && (URL.lastIndexOf("/") == URL.lastIndexOf("//") + 1 ) ){
                  //the case where the URL is etc. http://www.idega.is
                    parameterString.append("/?idega_session_id=");
                    parameterString.append(modinfo.getSession().getId());
                    return parameterString.toString();
                  }
                  else{
                    if(URL.indexOf("/") != -1){
                      //If the URL ends with a "/"
                      if (URL.lastIndexOf("/") == (URL.length()-1) ){
                        parameterString.append("?idega_session_id=");
                        parameterString.append(modinfo.getSession().getId());
                        return parameterString.toString();
                      }
                      else{
                        //There is a dot after the last "/" interpreted as a file not a directory
                        if(URL.lastIndexOf(".") > URL.lastIndexOf("/") ){
                          parameterString.append("?idega_session_id=");
                          parameterString.append(modinfo.getSession().getId());
                          return parameterString.toString();
                        }
                        else{
                          parameterString.append("/?idega_session_id=");
                          parameterString.append(modinfo.getSession().getId());
                          return parameterString.toString();
                        }
                      }
                    }
                    else{
                      parameterString.append("?idega_session_id=");
                      parameterString.append(modinfo.getSession().getId());
                      return parameterString.toString();
                    }
                  }
		}
		else{
                  //Temporary solution??? :// in link then no idega_session_id
		  return "";
		}
            }
            else{
              return "";
            }
	}

	else{
          /**
          *Temporary solution??? :// in link then no idega_session_id
          */
          if(URL.indexOf("?")==-1){
            if (addSessionId && (!modinfo.isSearchEngine()) ){
              if ( parameterString.toString().indexOf("?") == -1 ) {
                //parameterString.append("?");
                parameterString.insert(0,'?');
              }
              //else{
               parameterString.append("&");
              //}

              if(URL.indexOf("://") == -1){
                parameterString.append("idega_session_id=");
                parameterString.append(modinfo.getSession().getId());
              }
            }
	        }
          else{
            if (addSessionId && (!modinfo.isSearchEngine()) ){
              parameterString.append("&");
              if(URL.indexOf("://") == -1){
                parameterString.append("idega_session_id=");
                parameterString.append(modinfo.getSession().getId());
              }
            }

          }

          return parameterString.toString();
	}
}


public void clearParameters(){
  parameterString=null;
}


public void print(ModuleInfo modinfo)throws Exception{
	initVariables(modinfo);
	//if ( doPrint(modinfo) ){
            String oldURL = getURL();
            if(oldURL==null){
                oldURL=modinfo.getRequestURI();
                setURL(oldURL);
            }
            else if (oldURL.equals("")){
                oldURL=modinfo.getRequestURI();
                setURL(oldURL);
            }


		if (getLanguage().equals("HTML")){
				if (ObjectType.equals("Window")){
                                        if(windowClass==null){
					  setOnClick(myWindow.getCallingScriptString(modinfo,myWindow.getURL(modinfo)+getParameterString(modinfo,myWindow.getURL(modinfo))));
			                }
                                        else{
                                          setOnClick(Window.getCallingScriptString(windowClass,getURL()+getParameterString(modinfo,getURL()),true));
                                        }
                                        setURL("#");
                                        print("<a "+getAttributeString()+" >");
                                        if (obj == null){
					  Text myText = new Text(myWindow.getName());
                                          myText.print(modinfo);
					}
					else{
                                          obj.print(modinfo);
					}
                                        print("</a>");
				}
				else{
					setURL(oldURL+getParameterString(modinfo,oldURL));
                                        print("<a "+getAttributeString()+" >");
					obj.print(modinfo);
					print("</a>");
				}
		}
		else if (getLanguage().equals("WML")){
				if (ObjectType.equals("Window")){

					setURL(myWindow.getURL(modinfo)+getParameterString(modinfo,oldURL));
					//println("<p>");
                                        setURL("#");
					print("<a "+getAttributeString()+" >");
					print(myWindow.getName());
					print("</a>");
					//println("</p>");
				}
				else{
					//println("<p>");
                                        setURL(oldURL+getParameterString(modinfo,oldURL));
                                        print("<a "+getAttributeString()+" >");
					obj.print(modinfo);
					print("</a>");
					//println("</p>");
				}
		}
                /**
                 * !!Find out why this is necessary:
                 */
	        setURL(oldURL);
        //}
	//else{
	//	if (ObjectType.equals("Window")){
	//		myWindow.print(modinfo);
	//	}
	//	else{
	//		obj.print(modinfo);
	//	}
        //
        //}

}


//added by gummi@idega.is
//begin

  public void addIWLinkListener(IWLinkListener l,ModuleInfo modinfo){
    if (!listenerAdded()){
      postIWLinkEvent(modinfo);
    }
    super.addIWLinkListener(l, modinfo);
  }


  private void postIWLinkEvent(ModuleInfo modinfo){
      eventLocationString = this.getID();
      IWLinkEvent event = new IWLinkEvent(this,IWLinkEvent.LINK_ACTION_PERFORMED);
      if(this.formToSubmit == null){
        this.addParameter(sessionEventStorageName,eventLocationString);
      }
      modinfo.setSessionAttribute(eventLocationString, event);
      listenerAdded(true);
  }

//end


  public void setToFormSubmit(Form form) {
      this.setToFormSubmit(form,false);
  }

  public void setToFormSubmit(Form form, boolean useEvent ) {
      this.formToSubmit = form;
      this.setURL("#");
      if((this.getIWLinkListeners() != null && this.getIWLinkListeners().length != 0) || useEvent ){
         this.setOnClick("javascript:document."+form.getID()+"."+IWMainApplication.IWEventSessionAddressParameter+".value=this.id ;document."+form.getID()+".submit()");
      } else {
        this.setOnClick("javascript:document."+form.getID()+".submit()");
      }
  }

  public void setAsBackLink(int backUpHowManyPages) {
      this.setOnClick("history.go(-"+backUpHowManyPages+")");
      this.setURL("#");
  }

  public void  setAsBackLink() {
      setAsBackLink(1);
  }


  public void setProperty(String key, String values[]) {
    if (key.equalsIgnoreCase("text")) {
      setText(values[0]);
    }
    else if (key.equalsIgnoreCase("url")) {
      setURL(values[0]);
    }
  }

  public void setEventListener(Class eventListenerClass){
    setEventListener(eventListenerClass.getName());
  }

  public void setEventListener(String eventListenerClassName){
    this.addParameter(IWMainApplication.IdegaEventListenerClassParameter,IWMainApplication.getEncryptedClassName(eventListenerClassName));
  }

  public void sendToControllerFrame(){
    this.setTarget(IWConstants.IW_CONTROLLER_FRAME_NAME);
  }

  public void setWindowToOpen(Class windowClass){
    ObjectType="Window";
    this.windowClass=windowClass;
    setURL(IWMainApplication.windowOpenerURL);
    //this.setOnClick(Window.getCallingScriptString(windowClass,true));
    addParameter(Page.IW_FRAME_CLASS_PARAMETER,windowClass.getName());
  }

}

