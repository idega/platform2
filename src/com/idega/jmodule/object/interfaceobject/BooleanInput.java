//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import java.io.*;
import java.util.*;
import com.idega.jmodule.object.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class BooleanInput extends DropdownMenu{

  private static final String NO_KEY = "booleaninput.no";
  private static final String YES_KEY = "booleaninput.yes";

  public BooleanInput(){
      this("booleaninput");
  }

  public BooleanInput(String name){
    super(name);
    addMenuElement("N");
    addMenuElement("Y");
  }

  public void main(ModuleInfo modinfo)throws Exception{
    super.main(modinfo);
    IWBundle iwb = this.getBundle(modinfo);
    IWResourceBundle iwrb = iwb.getResourceBundle(modinfo);
    setMenuElementDisplayString("N",iwrb.getLocalizedString(NO_KEY));
    setMenuElementDisplayString("Y",iwrb.getLocalizedString(YES_KEY));
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
