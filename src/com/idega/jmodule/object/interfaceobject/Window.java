//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
package com.idega.jmodule.object.interfaceobject;

import com.idega.jmodule.object.*;
import java.io.*;
import com.idega.idegaweb.*;

/*Class to create pop up windows and such*/
public class Window extends Page{

private String title;
private int width;
private int height;
private String url;
//private boolean newURL;
//private Script theAssociatedScript;
private static String emptyString="";

//settings for the window:
private boolean toolbar;
private boolean location;
private boolean scrollbar;
private boolean directories;
private boolean menubar;
private boolean status;
private boolean titlebar;
private boolean resizable;


public Window(){
	this(emptyString);
        String className = this.getClass().getName();
        setTitle(className.substring(className.lastIndexOf(".")+1));
}



public Window(String name){
	this(name,400,400);
}

public Window(int width, int heigth) {
        this(emptyString,width,heigth);
        String className = this.getClass().getName();
        setTitle(className.substring(className.lastIndexOf(".")+1));
}

public Window(String name,int width,int height){
	//super();
	//setTitle(name);
	//this.height=height;
	//this.width=width;
	//newURL=false;
	//setSettings();
        this(name,width,height,IWMainApplication.windowOpenerURL);

}

public Window(String name,String url){
	this(name,400,400,url);
}

public Window(String name, int width, int height, String url){
	//super();
	setTitle(name);
	this.height=height;
	this.width=width;
	this.url = url;
	//newURL=true;
	setSettings();
}

private void setSettings(){
        setID();
	setToolbar(false);
	setLocation(false);
	setScrollbar(true);
	setDirectories(false);
	setMenubar(false);
	setStatus(false);
	setTitlebar(false);
	setResizable(false);
}

public void setToolbar(boolean ifToolbar){
	toolbar=ifToolbar;
}

public void setLocation(boolean ifLocation){
	location=ifLocation;
}

public void setScrollbar(boolean ifScrollbar){
	scrollbar=ifScrollbar;
}

public void setDirectories(boolean ifDirectories){
	directories=ifDirectories;
}

public void setMenubar(boolean ifMenubar){
	menubar=ifMenubar;
}

public void setStatus(boolean ifStatus){
	status=ifStatus;
}

public void setTitlebar(boolean ifTitlebar){
	titlebar=ifTitlebar;
}

public void setResizable(boolean ifResizable){
	resizable=ifResizable;
}


/*returns if the window is a reference to a new url or is created in the same page*/
//private boolean isNewURL(){
//	//return newURL;
//        return true;
//}

public String getURL(ModuleInfo modinfo){
//	if (isNewURL()){
//		return url;
//	}
//	else{
//		return encodeSpecialRequestString("window",this.getName(),modinfo);
//	}
        if(url==null){
          return IWMainApplication.windowOpenerURL;
        }
        else{
          return url;
        }
}

public void setBackgroundColor(String color){
	setAttribute("bgcolor",color);
}

public void setTextColor(String color){
	setAttribute("text",color);
}

public void setAlinkColor(String color){
	setAttribute("alink",color);
}

public void setVlinkColor(String color){
	setAttribute("vlink",color);
}

public void setLinkColor(String color){
	setAttribute("link",color);
}

public void setMarginWidth(int width){
	setAttribute("marginwidth",Integer.toString(width));
}

public void setMarginHeight(int height){
	setAttribute("marginheight",Integer.toString(height));
}

public void setLeftMargin(int leftmargin){
	setAttribute("leftmargin",Integer.toString(leftmargin));
}

public void setTopMargin(int topmargin){
	setAttribute("topmargin",Integer.toString(topmargin));
}

public void setTitle(String title){
	this.title=title;
}

public String getTitle(){
	return this.title;
}

public String getName(){
	return this.getTitle();
}

public int getWidth(){
	return this.width;
}

public void setWidth(int width){
	this.width=width;
}

public int getHeight(){
	return this.height;
}

public void setHeight(int height){
	this.height=height;
}

/*
public String getUrl(){
	return this.url;
}
*/

public void setURL(String url){
	this.url=url;
        //newURL=true;
}

/*public void setAssociatedScript(Script myScript){
	theAssociatedScript = myScript;
}

public Script getAssociatedScript(){
	return theAssociatedScript;
}*/

private String returnCheck(boolean checkBool){
	if (checkBool == true){
		return "yes";
	}
	else{
		return "no";
	}
}





public String getCallingScriptString(ModuleInfo modinfo,String url){
	//return "window.open('"+getURL(modinfo)+"','"+getName()+"','resizable="+returnCheck(resizable)+",toolbar="+returnCheck(toolbar)+",location="+returnCheck(location)+",directories="+returnCheck(directories)+",status="+returnCheck(status)+",scrollbars="+returnCheck(scrollbar)+",menubar="+returnCheck(menubar)+",titlebar="+returnCheck(titlebar)+",width="+getWidth()+",height="+getHeight()+"')";
          /*if (this.getName().equalsIgnoreCase("untitled")){
            setID();
            setName(getID());
          }*/
         return "window.open('"+url+"','"+getTarget()+"','resizable="+returnCheck(resizable)+",toolbar="+returnCheck(toolbar)+",location="+returnCheck(location)+",directories="+returnCheck(directories)+",status="+returnCheck(status)+",scrollbars="+returnCheck(scrollbar)+",menubar="+returnCheck(menubar)+",titlebar="+returnCheck(titlebar)+",width="+getWidth()+",height="+getHeight()+"')";
}

public String getCallingScriptString(ModuleInfo modinfo){
	//return "window.open('"+getURL(modinfo)+"','"+getName()+"','resizable="+returnCheck(resizable)+",toolbar="+returnCheck(toolbar)+",location="+returnCheck(location)+",directories="+returnCheck(directories)+",status="+returnCheck(status)+",scrollbars="+returnCheck(scrollbar)+",menubar="+returnCheck(menubar)+",titlebar="+returnCheck(titlebar)+",width="+getWidth()+",height="+getHeight()+"')";
          /*if (this.getName().equalsIgnoreCase("untitled")){
            setID();
            setName(getID());
          }*/
         //return "window.open('"+getURL(modinfo)+"','"+getTarget()+"','resizable="+returnCheck(resizable)+",toolbar="+returnCheck(toolbar)+",location="+returnCheck(location)+",directories="+returnCheck(directories)+",status="+returnCheck(status)+",scrollbars="+returnCheck(scrollbar)+",menubar="+returnCheck(menubar)+",titlebar="+returnCheck(titlebar)+",width="+getWidth()+",height="+getHeight()+"')";
          return getCallingScriptString(modinfo,getURL(modinfo));
}



protected String getCallingScriptStringForForm(ModuleInfo modinfo){
	//return "window.open('"+getURL(modinfo)+"','"+getName()+"','resizable="+returnCheck(resizable)+",toolbar="+returnCheck(toolbar)+",location="+returnCheck(location)+",directories="+returnCheck(directories)+",status="+returnCheck(status)+",scrollbars="+returnCheck(scrollbar)+",menubar="+returnCheck(menubar)+",titlebar="+returnCheck(titlebar)+",width="+getWidth()+",height="+getHeight()+"')";
          /*if (this.getName().equalsIgnoreCase("untitled")){
            setID();
            setName(getID());
          }*/
         return "window.open('','"+getTarget()+"','resizable="+returnCheck(resizable)+",toolbar="+returnCheck(toolbar)+",location="+returnCheck(location)+",directories="+returnCheck(directories)+",status="+returnCheck(status)+",scrollbars="+returnCheck(scrollbar)+",menubar="+returnCheck(menubar)+",titlebar="+returnCheck(titlebar)+",width="+getWidth()+",height="+getHeight()+"')";
}


public void setBackgroundImage(String imageURL){
	setAttribute("background",imageURL);
}

public void setBackgroundImage(Image backgroundImage){
	setAttribute("background",backgroundImage.getURL());
}

public boolean doPrint(ModuleInfo modinfo){
	boolean returnBoole;
	if (modinfo.getRequest().getParameter("idegaspecialrequesttype") == null){
	/*no special request*/
		/*Check if there is a parent object*/
		if (getParentObject() == null){
		/*if there is no parent object then do print directly out*/
			returnBoole = true;
		}
		else{
		/*if there is a parent object then do not print directly out*/
			returnBoole = false;
		}
	}
	else if (modinfo.getRequest().getParameter("idegaspecialrequesttype").equals("window") && modinfo.getRequest().getParameter("idegaspecialrequestname").equals(this.getName()) ){
		returnBoole = true;
	}
	else{
		returnBoole = false;
	}

	return returnBoole;
}


public String getTarget(){
  return getID();
}



  public synchronized Object clone() {
    Window obj = null;
    try {
      obj = (Window)super.clone();
      obj.title = this.title;
      obj.width = this.width;
      obj.height = this.height;
      obj.url = this.url;

      obj.toolbar = this.toolbar;
      obj.location = this.location;
      obj.scrollbar = this.scrollbar;
      obj.directories = this.directories;
      obj.menubar = this.menubar;
      obj.status = this.status;
      obj.titlebar = this.titlebar;
      obj.resizable = this.resizable;
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }

    return obj;
  }




/*
public void print(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);
	if ( doPrint(modinfo) ){
		if (! isAttributeSet("bgcolor")){
			setBackgroundColor(modinfo.getDefaultPrimaryInterfaceColor());
		}

		if (getLanguage().equals("HTML")){

			//if (getInterfaceStyle().equals(" something ")){
			//}
			//else{
			if (this.url == null){
				println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">\n<html>");
				println("\n<head>");
				if ( getAssociatedScript() != null){
					getAssociatedScript().print(modinfo);
				}
				println("\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n<META NAME=\"generator\" CONTENT=\"idega arachnea\">\n");
				println("<title>"+getTitle()+"</title>");
				println("</head>\n<body  "+getAttributeString()+" >\n");
				super.print(modinfo);
				println("\n</body>\n</html>");
			}
			//}
		}
	}
}*/


}//End class
