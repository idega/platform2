//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.textObject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class Paragraph extends ModuleObjectContainer{


public Paragraph(){
	super();
}

public Paragraph(String align){
	super();
	setAlign(align);
}

public Paragraph(String align,String ID){
	super();
	setAlign(align);
	setID(ID);
}

public Paragraph(String align,String ID,String Class){
	super();
	setAlign(align);
	setID(ID);
	setClass(Class);
}


public Paragraph(String align,String ID,String Class,String style){
	super();
	setAlign(align);
	setID(ID);
	setClass(Class);
	setStyle(style);
}

public void setAlign(String s){
	setAttribute("align",s);
}

public void setClass(String s){
	setAttribute("class",s);
}

public void setStyle(String s){
	setAttribute("style",s);
}

public void print(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);
	//if ( doPrint(modinfo) ){
		if (getLanguage().equals("HTML")){
			//if (getInterfaceStyle().equals("something")){
			//}
			//else{

			println("<p "+getAttributeString()+" >");
			super.print(modinfo);
			println("</p>");

			// }
		}
		else if (getLanguage().equals("WML")){
			println("<p>");
			super.print(modinfo);
			println("</p>");		}
	        }

        //}


}

