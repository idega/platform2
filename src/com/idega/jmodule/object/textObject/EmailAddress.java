//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.textObject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;


/**
*@author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
*@version 1.2
*/
public class EmailAddress extends Link{

private ModuleObject obj;
private String ObjectType;
private String parameterString;

public EmailAddress(){
	super("");
}

public EmailAddress(String text){
	super( new Text(text) );
	setURL("mailto:"+text);
}

public EmailAddress(ModuleObject mo, Window myWindow){
}

public EmailAddress(Window myWindow){

}

public EmailAddress(ModuleObject mo){
	super();
	obj = mo;
	obj.setParentObject(this);
	ObjectType="ModuleObject";
}

public EmailAddress(Text text){
	super();
	text.setFontColor("");
	obj = (ModuleObject)text;
	obj.setParentObject(this);
	ObjectType="Text";

}

public EmailAddress(String text,String url){
	this(new Text(text),url);
}

public EmailAddress(ModuleObject mo,String url){
	super();
	obj = mo;
	setURL("mailto:"+url);
	obj.setParentObject(this);
	ObjectType="ModuleObject";
}

public EmailAddress(Text text,String url){
	super();
	text.setFontColor("");
	obj = (ModuleObject)text;
	setURL("mailto:"+url);
	obj.setParentObject(this);
	ObjectType="Text";
}

protected String getParameterString(ModuleInfo modinfo){

if ( parameterString==null) parameterString="";
return parameterString;

}

public void setEmailAddress(String email){

	setURL("mailto"+email);

}


}

