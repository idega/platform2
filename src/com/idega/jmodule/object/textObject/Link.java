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
*/
public class Link extends Text{

private ModuleObject obj;
private String ObjectType;
private Window myWindow;
private String parameterString;
private boolean addSessionId = true;
private static String sessionStorageName=IWMainApplication.windowOpenerParameter;
private static String emptyString="";
private Form formToSubmit;



public Link(){
	this(emptyString);
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
public Link(int file_id){
	this(new Text("File"),"/servlet/FileModule?file_id="+file_id);
}

public Link(int file_id,String file_name){
	this(new Text(file_name),"/servlet/FileModule?file_id="+file_id);
}

public Link(ModuleObject mo,int file_id){
	super();
	obj = mo;
	setURL("/servlet/FileModule?file_id="+file_id);
	obj.setParentObject(this);
	ObjectType="ModuleObject";
}

public Link(int file_id, Window myWindow){
	//super();
	this.myWindow = myWindow;
	myWindow.setParentObject(this);
	ObjectType="Window";
}


public Link(String displayText,Class classToInstanciate){
       this(displayText,IWMainApplication.getObjectInstanciatorURL(classToInstanciate));
}



public Link(String displayText,String classToInstanciate,String template){
       this(displayText,IWMainApplication.getObjectInstanciatorURL(classToInstanciate,template));
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

    String sessionParameterName=com.idega.servlet.WindowOpener.storeWindow(modinfo,myWindow);
    addParameter(sessionStorageName,sessionParameterName);

    //String sessionParameterName=this.getID();
    //addParameter(sessionStorageName,sessionParameterName);
    //modinfo.setSessionAttribute(sessionParameterName,myWindow);

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
	if (parameterString == null){
		parameterString = "&"+ParameterName+"="+ParameterValue;
	}
	else{
		parameterString=parameterString+"&"+ParameterName+"="+ParameterValue;
	}
}

public void addParameter(String parameterName,int parameterValue){
   addParameter(parameterName,Integer.toString(parameterValue));
}

public void setOnFocus(String s){
	setAttribute("onfocus",s);
}

public void setOnBlur(String s){
	setAttribute("onblur",s);
}

public void setOnSelect(String s){
	setAttribute("onselect",s);
}

public void setOnChange(String s){
	setAttribute("onchange",s);
}

public void setOnClick(String s){
	setAttribute("onclick",s);
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
	setAttribute("target",target);
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




public String getParameterString(ModuleInfo modinfo,String URL){
        if (URL == null){
          URL="";
        }
	if (parameterString == null){
          if (addSessionId){
		if(URL.indexOf("://") == -1){
			if (URL.indexOf("?") != -1){

				return "&idega_session_id="+modinfo.getSession().getId();

                        }
			//the case where the URL is etc. http://www.idega.is
			else if ((URL.indexOf("//") != -1) && (URL.lastIndexOf("/") == URL.lastIndexOf("//") + 1 ) ){

                                  return "/?idega_session_id="+modinfo.getSession().getId();

                        }
			else{
				if(URL.indexOf("/") != -1){
					//If the URL ends with a "/"
					if (URL.lastIndexOf("/") == URL.length()-1){

                                              return "?idega_session_id="+modinfo.getSession().getId();

                                        }
					else{
						//There is a dot after the last "/" interpreted as a file not a directory
						if(URL.lastIndexOf(".") > URL.lastIndexOf("/") ){
							return "?idega_session_id="+modinfo.getSession().getId();
						}
						else{
							return "/?idega_session_id="+modinfo.getSession().getId();
						}
					}
				}
				else{
					return "?&idega_session_id="+modinfo.getSession().getId();
				}
			}
		}
		/**
		*Temporary solution??? :// in link then no idega_session_id
		*/
		else{
			return "";
		}
            }
            else{
              return "";
            }
	}

	else{
		String session_id;

		/**
		*Temporary solution??? :// in link then no idega_session_id
		*/
                if (addSessionId){
                  if(URL.indexOf("://") == -1){
                          session_id="&idega_session_id="+modinfo.getSession().getId();
                  }
                  else{
                          session_id="";
                  }
                }
                else{
                  session_id="";
                }

		if ( URL.indexOf("?") == -1 ){
			return "?"+parameterString+"&idega_session_id="+session_id;
		}
		else{
			return parameterString+"&idega_session_id="+session_id;
		}
	}
}


public void clearParameters(){
  parameterString=null;
}


public void print(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);
	//if ( doPrint(modinfo) ){
            if (getURL() == null){
                    setURL(modinfo.getRequest().getRequestURI());
            }
            String oldURL = getURL();

		if (getLanguage().equals("HTML")){
			//if (getInterfaceStyle().equals("something")){
			//}
			//else{
				//unfinished
				if (ObjectType.equals("Window")){
					if (obj == null){

                                                //myWindow.setURL(myWindow.getURL(modinfo)+getParameterString(modinfo,myWindow.getURL(modinfo)));

                                                setURL("#");
						setOnClick(myWindow.getCallingScriptString(modinfo,myWindow.getURL(modinfo)+getParameterString(modinfo,myWindow.getURL(modinfo))));
						//setTarget(myWindow.getTarget());
						print("<a "+getAttributeString()+" >");
						Text myText = new Text(myWindow.getName());
						myText.print(modinfo);
						print("</a>");
					}
					else{
                                                //myWindow.setURL(myWindow.getURL(modinfo)+getParameterString(modinfo,myWindow.getURL(modinfo)));
						setURL("#");
                                                setOnClick(myWindow.getCallingScriptString(modinfo,myWindow.getURL(modinfo)+getParameterString(modinfo,myWindow.getURL(modinfo))));
						//setTarget(myWindow.getTarget());
						print("<a "+getAttributeString()+" >");
						obj.print(modinfo);
						print("</a>");
					}
				}
				else{
					setURL(oldURL+getParameterString(modinfo,oldURL));
                                        print("<a "+getAttributeString()+" >");
					obj.print(modinfo);
					print("</a>");
				}
			// }
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






}

