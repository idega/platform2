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
  TextInput resultOutput;
  List moduleObjects = new Vector();
  private int size = -1;
  private String content = "";
  private String name;

  public ResultOutput() {
    this("unspecified","");
  }

  public ResultOutput(String name) {
    this.name = name;
    this.functionName = this.name;
  }

  public ResultOutput(String name, String content) {
    this.name = name;
    this.functionName = this.name;
    this.content = content;
  }

  public void _main(ModuleInfo modinfo) {
      script = getParentPage().getAssociatedScript();

      resultOutput = new TextInput(name, content);

      TextInput moduleObject;

      StringBuffer theScript = new StringBuffer();
        theScript.append("function "+functionName+"(myForm) {");
        theScript.append("\n          myForm."+resultOutput.getName()+".value=(");
        for (int i = 0; i < moduleObjects.size(); i++) {
            if (i != 0) theScript.append("+");
            moduleObject = (TextInput) moduleObjects.get(i);
            theScript.append("(1*myForm."+moduleObject.getName()+".value)");
        }
        theScript.append(");");


        theScript.append("\n}");

      script.addFunction(functionName, theScript.toString());

      resultOutput.setDisabled(true);
      if (this.size > 0) resultOutput.setSize(size);
      super.add(resultOutput);
  }

  public void setSize(int size) {
    this.size = size;
  }


  public void add(ModuleObject mo) {
    if (mo instanceof TextInput) {
      TextInput temp = (TextInput) mo;
        temp.setOnChange(functionName+"(this.form)");
        moduleObjects.add(temp);
    }
  }


}