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
public class InterfaceObject extends ModuleObject{

//protected String onFocus;
//protected String onBlur;
//protected String onSelect;
//protected String onChange;

//Tells if the object should maintain its status between "submits"
protected boolean keepStatus;
//The preceding text
private String precedingText;

public InterfaceObject(){
	super();
	setID();
	keepStatus=false;
}

protected boolean isEnclosedByForm(){
	//boolean returnBool=false;
	ModuleObject obj = getParentObject();
	while(obj != null){
		if(obj instanceof Form){
                        return true;
			//returnBool = true;
		}
		obj=obj.getParentObject();
	}
        return false;
	//return returnBool;
}

private void setOnAction(String actionType,String action){
  String attributeName = actionType;
  String previousAttribute = getAttribute(attributeName);
  if(previousAttribute==null){
    setAttribute(attributeName,action);

  }
  else{
    setAttribute(attributeName,previousAttribute+";"+action);
  }
}


public void setOnFocus(String s){
	setOnAction("onFocus",s);
}

public void setOnBlur(String s){
	setOnAction("onBlur",s);
}

public void setOnSelect(String s){
	setOnAction("onSelect",s);
}

public void setOnChange(String s){
	setOnAction("onChange",s);
}

public void setOnClick(String s){
	setOnAction("onClick",s);
}

public String getOnFocus(){
	return getAttribute("onFocus");
}

public String getOnBlur(){
	return getAttribute("onBlur");
}

public String getOnSelect(){
	return getAttribute("onSelect");
}

public String getOnChange(){
	return getAttribute("onChange");
}

public String getOnClick(){
	return getAttribute("onClick");
}

public void setValue(String s){;
	setAttribute("value",s);
}

public void setValue(int i){
	setAttribute("value",Integer.toString(i));
}

public void setContent(String s){
	setValue(s);
}

public String getValue(){
	return getAttribute("value");
}

public String getContent(){
	return getValue();
}

public void setPrecedingText(String theText){
	precedingText=theText;
}

public String getPrecedingText(){
	return precedingText;
}

public void setOnSubmit(String OnSubmitString){
	setOnAction("OnSubmit",OnSubmitString);
}

public String getOnSubmit(){
	return getAttribute("OnSubmit");
}

public Form getParentForm(){
	Form returnForm = null;
	ModuleObject obj = getParentObject();
	while(obj != null){
		if(obj instanceof Form){
			returnForm = (Form) obj;
		}
		obj=obj.getParentObject();
	}
	return returnForm;
}

public String getActionString(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);
	//String ActionString = "";
        // eiki jan 2001
        StringBuffer ActionString = new StringBuffer();

	if (getLanguage().equals("HTML")){

		if (getOnFocus() != null)
			//{ActionString = ActionString + " ONFOCUS=\""+getOnFocus()+"\" ";}
                {
                  ActionString.append(" ONFOCUS=\"");
                  ActionString.append(getOnFocus());
                  ActionString.append("\" ");
                }
		if (getOnBlur() != null)
			//{ActionString = ActionString + " ONBLUR=\""+getOnBlur()+"\" ";}
                {
                  ActionString.append(" ONBLUR=\"");
                  ActionString.append(getOnBlur());
                  ActionString.append("\" ");
                }
		if (getOnSelect() != null)
			//{ActionString = ActionString + " ONSELECT=\""+getOnSelect()+"\" ";}
                {
                  ActionString.append(" ONSELECT=\"");
                  ActionString.append(getOnSelect());
                  ActionString.append("\" ");
                }
		if (getOnChange() != null)
			//{ActionString = ActionString + " ONCHANGE=\""+getOnChange()+"\" ";}
                {
                  ActionString.append(" ONCHANGE=\"");
                  ActionString.append(getOnChange());
                  ActionString.append("\" ");
                }
		if (getOnClick() != null)
			//{ActionString = ActionString + " ONCLICK=\""+getOnClick()+"\" ";}
                {
                  ActionString.append(" ONCLICK=\"");
                  ActionString.append(getOnClick());
                  ActionString.append("\" ");
                }

	}
	return ActionString.toString();
}

public void handleKeepStatus(ModuleInfo modinfo){
	if (statusKeptOnAction()){
		if(modinfo.getRequest().getParameter(this.getName()) != null){
			//does nothing
		}
	}
}

public boolean statusKeptOnAction(){
	return keepStatus;
}

public void keepStatusOnAction(){
	keepStatus=true;
}


public void print(ModuleInfo modinfo)throws IOException{
	handleKeepStatus(modinfo);
	super.print(modinfo);
}


}

