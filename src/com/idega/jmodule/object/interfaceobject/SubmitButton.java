//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.event.IWSubmitEvent;
import com.idega.event.IWSubmitListener;
import com.idega.idegaweb.IWMainApplication;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class SubmitButton extends GenericButton{

private Window window;
private Image defaultImage;
//private Parameter includedParameter;
private boolean usingControlParameter=false;
private String parameterName;
private String parameterValue;
private String headerText;

private static final String emptyString = "";

public SubmitButton(){
	this("","Submit");
	setName(getDefaultName());
}

public SubmitButton(Image defaultImage){
	this(defaultImage,"default");
	setName(getDefaultName());
  this.parameterName=getDefaultName();
}

public SubmitButton(Image defaultImage, String name){
	this(name,"default");
	this.defaultImage = defaultImage;
}

public SubmitButton(Image defaultImage, String name, String value){
	this(name,value);
	this.defaultImage = defaultImage;

        this.parameterName=name;
        this.parameterValue=value;
        usingControlParameter=true;
}

/**
 * Constructor that includes another parameter/parametervalue with the submitbutton
 */
public SubmitButton(String displayText,String parameterName,String parameterValue){
  this(displayText);
  //this.includedParameter = new Parameter(parameterName,parameterValue);
  this.parameterName=parameterName;
  this.parameterValue=parameterValue;
  usingControlParameter=true;
  /*System.out.println("Inni i constructor");
  if (includedParameter==null){
    System.out.println("includedParameter==null");
  }*/
}

/**
 * Constructor that generates a parametername and the value is the displayText
 */
public SubmitButton(String displayText){
	this("",displayText);
	setName(getDefaultName());
}

public SubmitButton(String name,String displayText){
	super(name,displayText);
}


public String getDefaultName(){
	return "sub"+getID();
}

/**
*Only works if no form is around the button and it is used to open a new Window.
*/
public SubmitButton(Window window){
	this("","Submit");
	setName(getDefaultName());
	this.window=window;
}

/**
*Only works if no form is around the button and it is used to open a new Window.
*/
public SubmitButton(Window window,String displayString){
	this("",displayString);
	setName(getDefaultName());
	this.window=window;
}

public SubmitButton(Window window,String name,String value){
	super(name,value);
	this.window=window;
}

private String generateScriptCode(Window myWindow){
	return "";
}

public void setOnSubmit(String script){
	setAttribute("OnSubmit",script);
}

public void setTarget(String target){
	setAttribute("target",target);
}



  public void addIWSubmitListener(IWSubmitListener l, Form form, ModuleInfo modinfo){
    if (!listenerAdded()){
      postIWSubmitEvent(modinfo, form);
    }
    super.addIWSubmitListener(l, modinfo);
  }


  private void postIWSubmitEvent(ModuleInfo modinfo, Form form){
      eventLocationString = this.getID();
      IWSubmitEvent event = new IWSubmitEvent(this,IWSubmitEvent.SUBMIT_PERFORMED);
      //this.addParameter(sessionEventStorageName,eventLocationString);
      this.setOnClick("javascript:document."+form.getID()+"."+IWMainApplication.IWEventSessionAddressParameter+".value=this.id ");
      modinfo.setSessionAttribute(eventLocationString, event);
      listenerAdded(true);
  }


public void setStyle(String style) {
  setAttribute("class",style);
}


public void main(ModuleInfo modinfo){

  if (usingControlParameter){
    if(!parameterName.equals(emptyString)){
      this.getParentForm().setControlParameter(parameterName,"");
      this.setOnClick("this.form."+parameterName+".value='"+parameterValue+"'");
    }
  }

}


private void printButton(ModuleInfo modinfo) throws IOException{


	if (defaultImage == null){

		println("<input type=\"submit\" name=\""+getName()+"\" "+getAttributeString()+" >");
        }
	else{
		setAttribute("border","0");
		println("<input type=\"image\" src=\""+defaultImage.getURL()+"\" name=\""+getName()+"\" "+getAttributeString()+" >");
	}
}


public void print(ModuleInfo modinfo) throws Exception{
	initVariables(modinfo);
	//if ( doPrint(modinfo) ) {
		if (getLanguage().equals("HTML")){

			if(isEnclosedByForm()){

			//if (getInterfaceStyle().equals("something")){
			//}
			//else{

				printButton(modinfo);

			//}
			}
			else{
				Form myForm = new Form();
				myForm.setParentObject(getParentObject());
				this.setParentObject(myForm);
				myForm.add(this);

				//If a window is put inside the submit button, only implemented so if you have no form default around the button
				if (window != null){
					getParentForm().setAction(window.getURL(modinfo));
					getParentForm().setTarget(window.getTarget());
					//getParentForm().setTarget("#");
					getParentForm().setOnSubmit(window.getCallingScriptStringForForm(modinfo));
				}
				myForm.print(modinfo);
			}
		}
	//}
}


}
