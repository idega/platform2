//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/
package com.idega.jmodule.datastore.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.util.database.*;
import com.idega.jmodule.object.textObject.*;
import	com.idega.jmodule.object.*;
import	com.idega.jmodule.object.interfaceobject.*;
import	com.idega.jmodule.news.data.*;
import	com.idega.data.*;
import com.idega.util.text.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class DataCopier extends JModuleObject{

	public void main(ModuleInfo modinfo) throws Exception{
		String action = modinfo.getParameter("action");
		if (action == null) {action = "start";}


			if (action.equals("start")) {
				start(modinfo);
			}
			else if (action.equals("select")) {
				if (modinfo.getParameter("submit").equals("Copy")){
					copy(modinfo);
				}
			}

	}

	public void start(ModuleInfo modinfo)throws Exception {
		Form form = new Form();
		Table table = new Table(1,6);
		form.add(table);
		add(form);
		table.add("From datasource",1,1);
		table.add(new Parameter("action","select"),1,1);
		table.add("Entities separated by , in order from the top level class",1,3);
		DropdownMenu dataSources = new DropdownMenu("fromdatasource");
		table.add(dataSources,1,1);
		String[] sources;

		sources = ConnectionBroker.getDatasources();
		for(int i=0; i<sources.length;i++){
			dataSources.addMenuElement(sources[i]);
		}
		table.add("To datasource",1,2);
		DropdownMenu dataSources2 = new DropdownMenu("todatasource");
		table.add(dataSources2,1,2);
		sources = ConnectionBroker.getDatasources();
		for(int i=0; i<sources.length;i++){
			dataSources2.addMenuElement(sources[i]);
		}


		TextArea area = new TextArea("entities");
		area.setWidth(30);
		area.setHeight(15);
		table.add(area,1,4);
		CheckBox cbox = new CheckBox("maintain_ids");
		cbox.setChecked(true);
		table.add("Maintain IDs",1,5);
		table.add(cbox,1,5);
		SubmitButton button1 = new SubmitButton("submit","Copy");
		table.add(button1,1,6);


	}

	public void copy(ModuleInfo modinfo)throws Exception {
		TextSoap soap = new TextSoap();
		Vector classes = soap.FindAllWithSeparator(modinfo.getParameter("entities"),",");
		boolean check=true;
		String className="";
		for(Enumeration enum=classes.elements();enum.hasMoreElements();){
			className=(String)enum.nextElement();
			check=isValidClass(className);
		}

		if (check){
			add("Copy successful");
			String fromdatasource = modinfo.getParameter("fromdatasource");
			String todatasource = modinfo.getParameter("todatasource");
			GenericEntity entity;
			DatastoreInterface dsi;
			String maintain_ids=modinfo.getParameter("maintain_ids");

			for(Enumeration enum=classes.elements();enum.hasMoreElements();){
				className=(String)enum.nextElement();
				entity = (GenericEntity)Class.forName(className).newInstance();
				entity.setDatasource(fromdatasource);

				GenericEntity[] entities = entity.findAll();

				for(int i=0;i<entities.length;i++){
					entities[i].setDatasource(todatasource);
					if (maintain_ids==null){
						entities[i].removeFromColumn(entities[i].getIDColumnName());
					}
					else{
						int id = EntityControl.createUniqueID(entities[i]);
						while(id<=entities[i].getID()){
							id=EntityControl.createUniqueID(entities[i]);
						}
					}

					entities[i].insert();
				}

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
