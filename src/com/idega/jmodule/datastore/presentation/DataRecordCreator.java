//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/
package com.idega.jmodule.datastore.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.util.text.*;
import com.idega.util.database.*;
import com.idega.jmodule.object.textObject.*;
import	com.idega.jmodule.object.*;
import	com.idega.jmodule.object.interfaceobject.*;
import	com.idega.jmodule.news.data.*;
import	com.idega.data.*;
import com.idega.util.text.*;
import com.idega.transaction.*;
import javax.transaction.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class DataRecordCreator extends JModuleObject{


	public void main(ModuleInfo modinfo) throws Exception{
		String action = modinfo.getParameter("action");
		if (action == null) {action = "start";}
                TransactionManager tm = IdegaTransactionManager.getInstance();
                tm.begin();
                boolean thereWasAnException = false;

                try{
			if (action.equals("start")) {
				start(modinfo);
			}
			else if (action.equals("select")) {
				if (modinfo.getParameter("submit").equals("Create")){
					create(modinfo);
				}
				else if (modinfo.getParameter("submit").equals("Delete")){
					delete(modinfo);
				}
			}
                }
                catch(Exception ex){
                    thereWasAnException=true;
                    add(new ExceptionWrapper(ex,this));
                }

                if(thereWasAnException){
                  tm.rollback();
                }
                else{
                  tm.commit();
                }
	}

	public void start(ModuleInfo modinfo)throws Exception {
		Form form = new Form();
		Table table = new Table(1,4);
		form.add(table);
		add(form);
		table.add("Datasource",1,1);
		table.add(new Parameter("action","select"),1,1);
		table.add("Entities separated by , in order from the top level class",1,2);
		DropdownMenu dataSources = new DropdownMenu("datasource");
		table.add(dataSources,1,1);
		String[] sources = ConnectionBroker.getDatasources();
		for(int i=0; i<sources.length;i++){
			dataSources.addMenuElement(sources[i]);
		}



		TextArea area = new TextArea("entities");
		area.setWidth(50);
		area.setHeight(15);
		table.add(area,1,3);
		SubmitButton button1 = new SubmitButton("submit","Create");
		table.add(button1,1,4);
		SubmitButton button2 = new SubmitButton("submit","Delete");
		table.add(button2,1,4);
	}

	public void create(ModuleInfo modinfo)throws Exception {
		TextSoap soap = new TextSoap();
		Vector classes = soap.FindAllWithSeparator(modinfo.getParameter("entities"),",");
		boolean check=true;
		String className="";
		for(Enumeration enum=classes.elements();enum.hasMoreElements();){
			className=(String)enum.nextElement();
			check=isValidClass(className);
		}

		if (check){
			add("Creation successful");
			String datasource = modinfo.getParameter("datasource");
			GenericEntity entity;
			DatastoreInterface dsi;
			for(Enumeration enum=classes.elements();enum.hasMoreElements();){
				className=(String)enum.nextElement();
				entity = (GenericEntity)Class.forName(className).newInstance();
				entity.setDatasource(datasource);
				dsi=DatastoreInterface.getInstance(entity);
				dsi.createEntityRecord(entity);
			}
		}
		else{
			add("The class "+className+" is not correct");
		}
	}

	public void delete(ModuleInfo modinfo)throws Exception {
		//TextSoap soap = new TextSoap();
		Vector classes = TextSoap.FindAllWithSeparator(modinfo.getParameter("entities"),",");
		boolean check=true;
		String className="";
		for(Enumeration enum=classes.elements();enum.hasMoreElements();){
			className=(String)enum.nextElement();
			check=isValidClass(className);
		}

		if (check){
			add("Deletion successful");
			String datasource = modinfo.getParameter("datasource");
			GenericEntity entity;
			DatastoreInterface dsi;
			for(Enumeration enum=classes.elements();enum.hasMoreElements();){
				className=(String)enum.nextElement();
				entity = (GenericEntity)Class.forName(className).newInstance();
				entity.setDatasource(datasource);
				dsi=DatastoreInterface.getInstance(entity);
				dsi.deleteEntityRecord(entity);
			}
		}
		else{
			add("The class "+className+" is not correct");
		}
	}

	private boolean isValidClass(String className){
		boolean theReturn = true;
		try{
			Class.forName(className);
		}
		catch(Exception ex){
			theReturn=false;
		}


		return theReturn;
	}
}
