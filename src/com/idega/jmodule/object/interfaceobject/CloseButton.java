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
public class CloseButton extends GenericButton{

private Image defaultImage;

public CloseButton(){
	this("Close");
}

public CloseButton(String displayString){
	super();
	setName("");
	setValue(displayString);

	setAttribute("OnClick","window.close()");
}

public CloseButton(Image defaultImage){
	super();
	this.defaultImage = defaultImage;
	setAttribute("src",defaultImage.getURL());
	setAttribute("OnClick","window.close()");
}

public void print(ModuleInfo modinfo) throws IOException{
	initVariables(modinfo);
        StringBuffer printString = new StringBuffer();
	//if ( doPrint(modinfo) ){
		if (getLanguage().equals("HTML")){
			if (getInterfaceStyle().equals("default")){
				if (defaultImage == null){
                                  printString.append("<input type=\"button\" name=\"");
                                  printString.append(getName());
                                  printString.append("\" ");
                                  printString.append(getAttributeString());
                                  printString.append(" >");
                                  println(printString.toString());
				}
				else{
                                  setAttribute("border","0");
                                  printString.append("<input type=\"image\" name=\"");
                                  printString.append(getName());
                                  printString.append("\" ");
                                  printString.append(getAttributeString());
                                  printString.append(" >");

                                  println(printString.toString());
				}
/*
				if (defaultImage == null){

					println("<input type=\"button\" name=\""+getName()+"\" "+getAttributeString()+" >");
					//println("</input>");
				}
				else{
					println("<img "+getAttributeString()+" >");
					//println("</img>");
				}
*/
			}
		}
		else if (getLanguage().equals("WML")){

			if (getInterfaceStyle().equals("default")){
				println("<input type=\"button\" name=\""+getName()+"\" "+getAttributeString()+" >");
				println("</input>");
			}
		}
	//}
}

}

