//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/
package com.idega.jmodule.datastore.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.presentation.text.*;
import	com.idega.presentation.*;
import	com.idega.presentation.ui.*;
import	com.idega.jmodule.news.data.*;
import	com.idega.data.*;
import com.idega.util.text.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class DataManipulator extends Block{

public void main(IWContext iwc)throws Exception{
	//IWContext iwc = getIWContext();

	/*Insert = new SubmitButton("i1","insert");
	Update = new SubmitButton("u1","update");
	Insert2 = new SubmitButton("i2","insert");
	Update2 = new SubmitButton("u2","update");
	Update3 = new SubmitButton("u3","Search");*/

        String control=iwc.getParameter("control");

        if(control==null){
          screen1(iwc);
        }
        else{

          if (control.equals("i1")){
                  doTheInsert(iwc);
          }

          else if (control.equals("u1")){
                  doTheUpdate(iwc);

          }
          else if (control.equals("i2")){
                  doTheInsert(iwc);
          }

          else if (control.equals("u2")){
                  doTheUpdate(iwc);

          }
          else if (control.equals("u3")){
                  doTheUpdate2(iwc);

          }
          else{
            screen1(iwc);
          }
        }
}

public void screen1(IWContext iwc)throws Exception{
	Form myForm = new Form();
	myForm.setMethod("get");
	add(myForm);
	DropdownMenu menu = new DropdownMenu("class");
	myForm.add(menu);
	menu.addMenuElement("com.idega.projects.golf.entity.Address");
	menu.addMenuElement("com.idega.projects.golf.entity.Card");
	menu.addMenuElement("com.idega.projects.golf.entity.Country");
	menu.addMenuElement("com.idega.projects.golf.entity.Family");
	menu.addMenuElement("com.idega.projects.golf.entity.Field");
	menu.addMenuElement("com.idega.projects.golf.entity.Group");
	menu.addMenuElement("com.idega.projects.golf.entity.ImageEntity");
	menu.addMenuElement("com.idega.projects.golf.entity.LoginTable");
	menu.addMenuElement("com.idega.projects.golf.entity.Member");
	menu.addMenuElement("com.idega.projects.golf.entity.MemberInfo");
	menu.addMenuElement("com.idega.projects.golf.entity.MemberInfoTables");
	menu.addMenuElement("com.idega.projects.golf.entity.MemberList");
	menu.addMenuElement("com.idega.projects.golf.entity.MemberOverView");
	menu.addMenuElement("com.idega.projects.golf.entity.Payment");
	menu.addMenuElement("com.idega.projects.golf.entity.PaymentType");
	menu.addMenuElement("com.idega.projects.golf.entity.Poll");
	menu.addMenuElement("com.idega.projects.golf.entity.Poll_option");
	menu.addMenuElement("com.idega.projects.golf.entity.Poll_result");
	menu.addMenuElement("com.idega.projects.golf.entity.Phone");
	menu.addMenuElement("com.idega.projects.golf.entity.PriceCatalogue");
	menu.addMenuElement("com.idega.projects.golf.entity.Scorecard");
	menu.addMenuElement("com.idega.projects.golf.entity.Stroke");
	menu.addMenuElement("com.idega.projects.golf.entity.Tee");
	menu.addMenuElement("com.idega.projects.golf.entity.TeeColor");
	menu.addMenuElement("com.idega.projects.golf.entity.Tournament");
	menu.addMenuElement("com.idega.projects.golf.entity.TournamentGroup");
	menu.addMenuElement("com.idega.projects.golf.entity.TournamentType");
	menu.addMenuElement("com.idega.projects.golf.entity.Union");
	menu.addMenuElement("com.idega.projects.golf.entity.ZipCode");


	SubmitButton Insert = new SubmitButton("i1","insert");
	myForm.add(Insert);
        myForm.add(new Parameter("control","i1"));

//        addBreak();

        Form myForm1 = new Form();
        add(myForm1);
        myForm1.add(menu);


	SubmitButton Update = new SubmitButton("u1","update");
	myForm1.add(Update);
        myForm1.add(new Parameter("control","u1"));

	Form myForm2 = new Form();
	myForm2.setMethod("get");
	add(myForm2);
	myForm2.addText("Eda annar klasi:");
	myForm2.addBreak();
	TextInput input = new TextInput("class");
	input.setLength(60);
	myForm2.add(input);
	SubmitButton Insert2 = new SubmitButton("i2","insert");

	myForm2.add(Insert2);
        myForm2.add(new Parameter("control","i2"));


        Form myForm3 = new Form();
	myForm3.setMethod("get");
	add(myForm3);


	TextInput input1 = new TextInput("class");
	input1.setLength(60);
        myForm3.add(input1);
        myForm3.add(new Parameter("control","u2"));
	SubmitButton Update2 = new SubmitButton("u2","update");
	myForm3.add(Update2);


	SubmitButton Update3 = new SubmitButton("u3","Search");

}


/*public void actionPerformed(ModuleEvent ev)throws Exception{

	IWContext iwc = getIWContext();

	Insert = new SubmitButton("i1","insert");
	Update = new SubmitButton("u1","update");
	Insert2 = new SubmitButton("i2","insert");
	Update2 = new SubmitButton("u2","update");
	Update3 = new SubmitButton("u3","Search");


	if (ev.getSource().equals(Insert)){
		doTheInsert(iwc);
	}

	else if (ev.getSource().equals(Update)){
		doTheUpdate(iwc);

	}
	if (ev.getSource().equals(Insert2)){
		doTheInsert(iwc);
	}

	else if (ev.getSource().equals(Update2)){
		doTheUpdate(iwc);

	}
	else if (ev.getSource().equals(Update3)){
		doTheUpdate2(iwc);

	}

}*/

public void doTheInsert(IWContext iwc)throws Exception{
		GenericEntity ent = (GenericEntity)Class.forName(iwc.getParameter("class")).newInstance();
		add(new EntityInsert(ent));
}

public void doTheUpdate(IWContext iwc)throws Exception{


	GenericEntity ent = (GenericEntity)Class.forName(iwc.getParameter("class")).newInstance();
	iwc.getSession().setAttribute("entity",ent);
	EntityManipulator manip = new EntityManipulator(ent);

        SubmitButton Update3 = new SubmitButton("u3","Search");

	for (int i = 0;i < ent.getColumnNames().length; i++ ){
		Form form = new Form();
                form.add(new Parameter("control","u2"));
		form.setMethod("get");
		form.add(ent.getLongName(ent.getColumnNames()[i]));
		form.add(manip.getComponent(ent.getColumnNames()[i],1));
		form.add(new HiddenInput("columnname",ent.getColumnNames()[i]));
		form.add(Update3);
		add(form);
	}



}


public void doTheUpdate2(IWContext iwc)throws Exception{
	GenericEntity ent = (GenericEntity)iwc.getSession().getAttribute("entity");
	if (ent instanceof com.idega.projects.golf.entity.Member){
		ent.setVisible("family",false);
	}
	String columnName = iwc.getParameter("columnname");

	/*java.util.Enumeration enum = iwc.getParameterNames();

	String tempString = (String)enum.nextElement();
	tempString = (String)enum.nextElement();*/

	add(new EntityUpdater(ent.findAllByColumn(columnName,iwc.getParameter(columnName)+"%")));

}

}
