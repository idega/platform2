//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class TextInput extends InterfaceObject{

private Script script;

private boolean isSetAsIntegers;
private boolean isSetAsFloat;
private boolean isSetAsAlphabetical;
private boolean isSetAsEmail;
private boolean isSetAsNotEmpty;


private String integersErrorMessage;
private String floatErrorMessage;
private String alphabetErrorMessage;
private String emailErrorMessage;
private String notEmptyErrorMessage;


public TextInput(){
	this("untitled");
}

public TextInput(String name){
	super();
	setName(name);
}

public TextInput(String name,String content){
	super();
	setName(name);
	setContent(content);

	isSetAsIntegers=false;
	isSetAsFloat=false;
	isSetAsAlphabetical=false;
	isSetAsEmail=false;
	isSetAsNotEmpty=false;
}

public void setLength(int length){
	setSize(length);
}

public void setSize(int size){
	setAttribute("size",Integer.toString(size));
}

public void setMaxlength(int maxlength){
	setAttribute("maxlength",Integer.toString(maxlength));
}

private void setScript(Script script){
	this.script = script;
	setAssociatedScript(script);
}

private Script getScript(){
	if (getAssociatedScript() == null){

		setScript(new Script());
	}
	else
	{
		script = getAssociatedScript();
	}
	return script;
}

private void setCheckSubmit(){
	if ( getScript().getFunction("checkSubmit") == null){
		getScript().addFunction("checkSubmit","function checkSubmit(inputs){\n\n}");
	}
}

public void setAsNotEmpty(){
	this.setAsNotEmpty("Please fill in the box "+this.getName());
}

public void setAsNotEmpty(String errorMessage){
	isSetAsNotEmpty=true;
	notEmptyErrorMessage=errorMessage;
}

public void setAsEmail(String errorMessage){
	isSetAsEmail=true;
	emailErrorMessage=errorMessage;
}

public void setAsEmail(){
	this.setAsEmail("This is not an email");
}

public void setAsIntegers(){
	this.setAsIntegers("Please use only numbers in "+this.getName());
}

public void setAsIntegers(String errorMessage){
	isSetAsIntegers=true;
	integersErrorMessage=errorMessage;
}


public void setAsFloat(){
	this.setAsFloat("Please use only numbers in "+this.getName());
}

public void setAsFloat(String errorMessage){
	isSetAsFloat=true;
	floatErrorMessage=errorMessage;
}

public void setAsAlphabetictext(){
	this.setAsAlphabeticText("Please use only alpabetical characters in "+this.getName());
}

public void setAsAlphabeticText(String errorMessage){
	isSetAsAlphabetical=true;
	alphabetErrorMessage=errorMessage;

}

public void setStyle(String style) {
  setAttribute("class",style);
}

public String getValue(){
	if (super.getValue() == null){
		return "";
	}
	else{
		return super.getValue();
	}
}



public void _main(ModuleInfo modinfo)throws Exception{

	if (getParentForm() != null){
		if (isSetAsNotEmpty){
			getParentForm().setOnSubmit("return checkSubmit(this)");
			setCheckSubmit();
			getScript().addToFunction("checkSubmit","if (warnIfEmpty (inputs."+getName()+",'"+notEmptyErrorMessage+"') == false ){\nreturn false;\n}\n");
			getScript().addFunction("warnIfEmpty","function warnIfEmpty (inputbox,warnMsg) {\n\n	if ( inputbox.value == '' ) { \n		alert ( warnMsg );\n		return false;\n	}\n	else{\n		return true;\n}\n\n}");
		}

		if (isSetAsIntegers){
			getParentForm().setOnSubmit("return checkSubmit(this)");
			//Not implemented yet
		}
		else if(isSetAsFloat){
			getParentForm().setOnSubmit("return checkSubmit(this)");
			//Not implemented yet
		}
		else if(isSetAsAlphabetical){
			getParentForm().setOnSubmit("return checkSubmit(this)");
			//Not implemented yet
		}
		else if(isSetAsEmail){
			getParentForm().setOnSubmit("return checkSubmit(this)");
			setCheckSubmit();
			getScript().addToFunction("checkSubmit","if (warnIfNotEmail (inputs."+getName()+") == false ){\nreturn false;\n}\n");
			getScript().addFunction("warnIfNotEmail","function warnIfNotEmail (inputbox) { \n\n	var emailVal = inputbox.value; \n\n	var atI = emailVal.indexOf (\"@\"); \n\n	var dotI = emailVal.indexOf (\".\"); \n\n	var warnMsg = \""+emailErrorMessage+"\\n\";\n\n	if (  atI && dotI ){\n		return true;\n	}\n	else{\n		alert(warnMsg);\n		return false;\n	}\n}");
			//Not finished  yet
		}
	}
}


public void print(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);
	//if ( doPrint(modinfo) ){
		if (getLanguage().equals("HTML")){

		if (keepStatus){
			if(this.getRequest().getParameter(this.getName()) != null){
				setContent(getRequest().getParameter(getName()));
			}
		}
			//if (getInterfaceStyle().equals("default"))
				println("<input type=\"text\" name=\""+getName()+"\" "+getAttributeString()+" >");
				//println("</input>");
			// }
		}

		else if (getLanguage().equals("WML")){
			println("<input type=\"text\" name=\""+getName()+"\" "+getAttributeString()+" />");
		}
	//}
}



  public synchronized Object clone() {
    TextInput obj = null;
    try {
      obj = (TextInput)super.clone();
      if(this.script != null){
        obj.script = (Script)this.script.clone();
      }
      obj.isSetAsIntegers = this.isSetAsIntegers;
      obj.isSetAsFloat = this.isSetAsFloat;
      obj.isSetAsAlphabetical = this.isSetAsAlphabetical;
      obj.isSetAsEmail = this.isSetAsEmail;
      obj.isSetAsNotEmpty = this.isSetAsNotEmpty;


      obj.integersErrorMessage = this.integersErrorMessage;
      obj.floatErrorMessage = this.floatErrorMessage;
      obj.alphabetErrorMessage = this.alphabetErrorMessage;
      obj.emailErrorMessage = this.emailErrorMessage;
      obj.notEmptyErrorMessage = this.notEmptyErrorMessage;
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }

    return obj;
  }





}
