package com.idega.jmodule.object;


import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.interfaceobject.Form;
import java.util.Vector;
import java.util.Iterator;
import com.idega.jmodule.object.interfaceobject.Window;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.CloseButton;

/**
 * Title:        User
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public abstract class ConfirmWindow extends Window{

  public Text question;
  public Form myForm;

  public SubmitButton confirm;
  public CloseButton close;
  public Table myTable = null;

  public static final String PARAMETER_CONFIRM = "confirm";

  public Vector parameters;

  public ConfirmWindow(){
    super("ConfirmWindow",300,130);
    super.setBackgroundColor("#d4d0c8");
    super.setScrollbar(false);
    super.setAllMargins(0);

    question = Text.getBreak();
    myForm = new Form();
    parameters = new Vector();
    confirm = new SubmitButton(ConfirmWindow.PARAMETER_CONFIRM,"   Yes   ");
    close = new CloseButton("   No    ");
    // close.setOnFocus();
    initialize();

  }


  public void lineUpElements(){
    myTable = new Table(2,2);
    myTable.setWidth("100%");
    myTable.setHeight("100%");
    myTable.setCellpadding(5);
    myTable.setCellspacing(5);
    //myTable.setBorder(1);


    myTable.mergeCells(1,1,2,1);

    myTable.add(question,1,1);

    myTable.add(confirm,1,2);

    myTable.add(close,2,2);

    myTable.setAlignment(1,1,"center");
//      myTable.setAlignment(2,1,"center");
    myTable.setAlignment(1,2,"right");
    myTable.setAlignment(2,2,"left");

    myTable.setVerticalAlignment(1,1,"middle");
    myTable.setVerticalAlignment(1,2,"middle");
    myTable.setVerticalAlignment(2,2,"middle");

    myTable.setHeight(2,"30%");

    myForm.add(myTable);

  }

  public void setQuestion(Text Question){
    question = Question;
  }


  /*abstract*/
  public abstract void initialize();
  public abstract void actionPerformed(ModuleInfo modinfo)throws Exception;



  public void maintainParameter(String parameter){
    parameters.add(parameter);
  }


  public void _main(ModuleInfo modinfo) throws Exception {
    Iterator iter = parameters.iterator();
    while (iter.hasNext()) {
      String item = (String)iter.next();
      myForm.maintainParameter(item);
    }

    String confirmThis = modinfo.getParameter(ConfirmWindow.PARAMETER_CONFIRM);

    if(confirmThis != null){
      this.actionPerformed(modinfo);
      this.setParentToReload();
      this.close();
    } else{
      this.empty();
      if(myTable == null){
        lineUpElements();
      }
      this.add(myForm);
    }
    super._main(modinfo);
  }

}

