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
  List onChangeVector = new Vector();
  private int size = -1;
  private String content = "";
  private String name;
  private String theOperator = "+";

  public ResultOutput() {
    this("unspecified","");
  }

  public ResultOutput(String name) {
    this.functionName = name;
    this.name = name + "_input";
  }

  public ResultOutput(String name, String content) {
    this.functionName = name;
    this.name = name + "_input";
    this.content = content;
  }

  public void _main(ModuleInfo modinfo) {
      script = getParentPage().getAssociatedScript();

      resultOutput = new TextInput(name, content);

      ModuleObject moduleObject = null;

      StringBuffer theScript = new StringBuffer();
        theScript.append("function "+functionName+"(myForm) {");
        theScript.append("\n          myForm."+resultOutput.getName()+".value=(");
        for (int i = 0; i < moduleObjects.size(); i++) {
            if (i != 0) theScript.append(theOperator);
            if (moduleObjects.get(i) instanceof TextInput)
              moduleObject = (TextInput) moduleObjects.get(i);
            else if (moduleObjects.get(i) instanceof ResultOutput)
              moduleObject = (ResultOutput) moduleObjects.get(i);
            theScript.append("(1*myForm."+moduleObject.getName()+".value)");
        }
        theScript.append(");");


        theScript.append("\n}");

      script.addFunction(functionName, theScript.toString());

      resultOutput.setDisabled(true);
      if (this.size > 0) resultOutput.setSize(size);
      for (int i = 0; i < onChangeVector.size(); i++) {
          resultOutput.setOnChange((String ) onChangeVector.get(i) );
      }

      super.add(resultOutput);
  }

  public void setSize(int size) {
    this.size = size;
  }

  public void setOnChange(String s) {
      onChangeVector.add(s);
  }

  public List getAddedObjects() {
    return this.moduleObjects;
  }


  public String getName() {
    return this.name;
  }

  public void setOperator(String operatori) {
    this.theOperator = operatori;
  }

  public void add(ModuleObject mo) {
    if (mo instanceof TextInput) {
      TextInput temp = (TextInput) mo;
        temp.setOnChange(functionName+"(this.form)");
        moduleObjects.add(temp);
    }
    else if (mo instanceof ResultOutput) {
      ResultOutput temp = (ResultOutput) mo;
      List list = temp.getAddedObjects();
      for ( int a = 0; a < list.size(); a++ ) {
        TextInput text = (TextInput) list.get(a);
          text.setOnChange(functionName+"(this.form)");
      }
      moduleObjects.add(temp);
    }
  }


}