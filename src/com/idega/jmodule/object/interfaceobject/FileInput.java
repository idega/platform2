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
public class FileInput extends InterfaceObject{

public FileInput(){
	this("untitled");
}

public FileInput(String name){
	this(name,"unspecified");
}

public FileInput(String name,String value){
	super();
	setName(name);
	setContent(value);
}


public void print(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);
	//if ( doPrint(modinfo) ){
		if (getLanguage().equals("HTML")){
			//if (getInterfaceStyle().equals("something")){
			//
			//}
			//else{
				println("<input type=\"file\" name=\""+getName()+"\" "+getAttributeString()+" >");
				println("</input>");
			//}
		}
	//}

}


}

