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
public class PasswordInput extends InterfaceObject{

public PasswordInput(){
	this("untitled");
}

public PasswordInput(String name){
	super();
	setName(name);
}

public PasswordInput(String name,String content){
	super();
	setName(name);
	setContent(content);
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


public void print(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);
	//if ( doPrint(modinfo) ){
		if (getLanguage().equals("HTML")){
			//if (getInterfaceStyle().equals("something")){
			//
			//}
			//else{
				println("<input type=\"password\" name=\""+getName()+"\" "+getAttributeString()+" >");
				//println("</input>");
			//}
		}
		else if (getLanguage().equals("WML")){
			println("<input type=\"password\" name=\""+getName()+"\"  >");
			//println("</input>");
		}
	//}

}


}

