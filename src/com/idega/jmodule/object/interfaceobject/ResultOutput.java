package com.idega.jmodule.object.interfaceobject;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Script;
import com.idega.jmodule.object.ModuleObject;
import java.util.List;
import java.util.Vector;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ResultOutput extends ModuleObjectContainer {

  Script script;
  String functionName = "functionName";
  TextInput resultOutput = new TextInput("resultOutput");
  List moduleObjects = new Vector();
  List forms = new Vector();
  String nameOfForm = "temp";

  public ResultOutput(String nameOfForm) {
      this.nameOfForm = nameOfForm;
  }

  public void _main(ModuleInfo modinfo) {
      script = getParentPage().getAssociatedScript();

      String moduleObject;
      String form;

      StringBuffer theScript = new StringBuffer();
        theScript.append("function "+functionName+" () {");
        theScript.append("\n\n          document."+nameOfForm+"."+resultOutput.getName()+".value=(");
        for (int i = 0; i < moduleObjects.size(); i++) {
            if (i != 0) theScript.append("+");
            moduleObject = (String) moduleObjects.get(i);
            form = (String) forms.get(i);
            theScript.append("(1*"+form+"."+moduleObject+".value)");
        }
        theScript.append(")");


        theScript.append("\n\n}");

      script.addFunction(functionName, theScript.toString());

      resultOutput.setDisabled(true);
      super.add(resultOutput);
  }


  public void add(ModuleObject mo, String formName) {
    if (mo instanceof TextInput) {
      TextInput temp = (TextInput) mo;
        temp.setOnBlur("javascript:"+functionName+"();");
        moduleObjects.add(temp.getName());
        forms.add(formName);
    }
  }


}