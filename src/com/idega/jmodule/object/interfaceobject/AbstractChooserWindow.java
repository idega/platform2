package com.idega.jmodule.object.interfaceobject;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.Script;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public abstract class AbstractChooserWindow extends Window {

  String chooserSelectionParameter;
  protected static String SELECT_FUNCTION_NAME = "chooserSelect";


  protected static final String DISPLAYSTRING_PARAMETER_NAME = AbstractChooser.DISPLAYSTRING_PARAMETER_NAME;
  protected static final String VALUE_PARAMETER_NAME = AbstractChooser.VALUE_PARAMETER_NAME;
  protected static final String SCRIPT_PREFIX_PARAMETER = AbstractChooser.SCRIPT_PREFIX_PARAMETER;
  protected static final String SCRIPT_SUFFIX_PARAMETER = AbstractChooser.SCRIPT_SUFFIX_PARAMETER;

  public AbstractChooserWindow(){
  }


  public void main(ModuleInfo modinfo){
    Script script = this.getAssociatedScript();
    String prefix = modinfo.getParameter(SCRIPT_PREFIX_PARAMETER);
    String suffix = modinfo.getParameter(SCRIPT_SUFFIX_PARAMETER);
    String displayString = modinfo.getParameter(DISPLAYSTRING_PARAMETER_NAME);

    String valueString = modinfo.getParameter(VALUE_PARAMETER_NAME);

    //script.addFunction(SELECT_FUNCTION_NAME,"function "+SELECT_FUNCTION_NAME+"(displaystring,value){ "+AbstractChooser.DISPLAYSTRING_PARAMETER_NAME+".value=displaystring;"+AbstractChooser.VALUE_PARAMETER_NAME+".value=value;window.close();return false }");
    script.addFunction(SELECT_FUNCTION_NAME,"function "+SELECT_FUNCTION_NAME+"(displaystring,value){"+prefix+displayString+"."+suffix+"=displaystring;"+prefix+valueString+".value=value;window.close();return false;}");

    displaySelection(modinfo);
  }

  public abstract void displaySelection(ModuleInfo modinfo);


  public String getOnSelectionCode(String displayString){
    return getOnSelectionCode(displayString,displayString);
  }

  public String getOnSelectionCode(String displayString,String value){
    return SELECT_FUNCTION_NAME+"("+displayString+","+value+")";
  }


  public String getSelectionParameter(ModuleInfo modinfo){
    return modinfo.getParameter(chooserSelectionParameter);
  }

}