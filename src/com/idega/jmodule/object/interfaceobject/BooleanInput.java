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
public class BooleanInput extends DropdownMenu{

public BooleanInput(){
	this("booleaninput");
}

public BooleanInput(String name){
	super(name);
	addMenuElement("N","Nei");
	addMenuElement("Y","Já");
}


public void setSelected(boolean selected){
  if(selected){
    this.setSelectedElement("Y");
  }
  else{
    this.setSelectedElement("N");
  }
}

}
