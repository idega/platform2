//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import com.idega.jmodule.object.*;
import java.io.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class IntegerInput extends TextInput{

public IntegerInput(){
	this("untitled");
	setAsIntegers();
}

public IntegerInput(String name){
	super(name);
	setAsIntegers();
}

public IntegerInput(String name,String errorWarning){
	super(name);
	setAsIntegers(errorWarning);
}

public IntegerInput(String name,int value){
	super(name,Integer.toString(value));
	setAsIntegers();
}

public IntegerInput(String name,int value,String errorWarning){
	super(name,Integer.toString(value));
	setAsIntegers(errorWarning);
}


public void setValue(int value){
	setValue(Integer.toString(value));
}

public void setValue(Integer value){
	setValue(value.toString());
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
			println("<p><input type=\"text\" format=\"NN\" name=\""+getName()+"\" value=\""+getValue()+"\" />");
			println("</p>");
		}
	//}
}



}
