//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import com.idega.jmodule.*;
import com.idega.data.*;
import java.io.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import javax.servlet.http.*;
import java.sql.SQLException;
import java.util.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class EditableList extends GenericList{

private Table theTable;
private Form theForm;
private Parameter objectParameter;
private String action;
private GenericEntity entity;
private int debug1;

/**
**Constructor to handle an update of one row
**/
public EditableList(GenericEntity entity){
	action="update";
	theForm = new Form();
	GenericEntity[] entityArr = (GenericEntity[])java.lang.reflect.Array.newInstance(entity.getClass(),1);
	entityArr[0]=entity;
	setEntity(entityArr);
}

/**
**Constructor to handle updates on one or many rows
**/
public EditableList(GenericEntity[] entity){
	super(entity);
	action="update";
	theForm = new Form();
}

public String getAction(){
	return action;
}


protected ModuleObject getComponent(String columnName,int rowIndex){

	ModuleObject theReturn = null;

		if (getEntity()[rowIndex-1].getStorageClassName(columnName).equals("java.lang.Integer")){
			if(getEntity()[rowIndex-1].getRelationShipClassName(columnName).equals("")){
				if(getEntity()[rowIndex-1].getStringColumnValue(columnName) != null){
					IntegerInput tempInput = new IntegerInput(columnName,getEntity()[rowIndex-1].getIntColumnValue(columnName));
					tempInput.setSize(10);
					theReturn = (ModuleObject) tempInput;
				}
				else{
					IntegerInput tempInput = new IntegerInput(columnName);
					tempInput.setSize(10);
					theReturn = (ModuleObject)  tempInput;
				}
			}
			else{
				if(getEntity()[rowIndex-1].getStringColumnValue(columnName) != null){
					try{
						GenericEntity entity = (GenericEntity)Class.forName(getEntity()[rowIndex-1].getRelationShipClassName(columnName)).newInstance();
						DropdownMenu tempInput = new DropdownMenu(entity.findAll());
						tempInput.setName(columnName);
						tempInput.setSelectedElement(getEntity()[rowIndex-1].getStringColumnValue(columnName));
						theReturn = (ModuleObject) tempInput;
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
				}
				else{
					try{
						GenericEntity entity = (GenericEntity)Class.forName(getEntity()[rowIndex-1].getRelationShipClassName(columnName)).newInstance();
						DropdownMenu tempInput = new DropdownMenu(entity.findAll());
						tempInput.setName(columnName);
						theReturn = (ModuleObject) tempInput;
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		}
		else if (getEntity()[rowIndex-1].getStorageClassName(columnName).equals("java.lang.String")){
			if(getEntity()[rowIndex-1].getStringColumnValue(columnName) != null){
				TextInput tempInput = new TextInput(columnName,getEntity()[rowIndex-1].getStringColumnValue(columnName));
				tempInput.setSize(30);
				theReturn = (ModuleObject) tempInput;
			}
			else{
				TextInput tempInput = new TextInput(columnName);
				tempInput.setSize(30);
				theReturn =  (ModuleObject) tempInput;
			}

		}
		else if (getEntity()[rowIndex-1].getStorageClassName(columnName).equals("java.lang.Boolean")){
			CheckBox box = new CheckBox(columnName,"Y");
			if(getEntity()[rowIndex-1].getStringColumnValue(columnName) != null){
				/*TextInput tempInput = new TextInput(columnName,getEntity()[rowIndex-1].getStringColumnValue(columnName));
				tempInput.setSize(10);
				theReturn = (ModuleObject) tempInput;*/
				if (getEntity()[rowIndex-1].getBooleanColumnValue(columnName) == true){
					box.setChecked(true);
				}
			}
			theReturn = (ModuleObject) box;
		}
		else if (getEntity()[rowIndex-1].getStorageClassName(columnName).equals("java.lang.Float")){
			if(getEntity()[rowIndex-1].getStringColumnValue(columnName) != null){
				FloatInput tempInput = new FloatInput(columnName,getEntity()[rowIndex-1].getFloatColumnValue(columnName));
				tempInput.setSize(10);
				theReturn = (ModuleObject) tempInput;
			}
			else{
				FloatInput tempInput = new FloatInput(columnName);
				tempInput.setSize(10);
				theReturn = (ModuleObject) tempInput;
			}
		}
		else if (getEntity()[rowIndex-1].getStorageClassName(columnName).equals("java.lang.Double")){
			if(getEntity()[rowIndex-1].getStringColumnValue(columnName) != null){
				FloatInput tempInput = new FloatInput(columnName,getEntity()[rowIndex-1].getFloatColumnValue(columnName));
				tempInput.setSize(10);
				theReturn = (ModuleObject) tempInput;
			}
			else{
				FloatInput tempInput = new FloatInput(columnName);
				tempInput.setSize(10);
				theReturn = (ModuleObject) tempInput;
			}
		}
		else if (getEntity()[rowIndex-1].getStorageClassName(columnName).equals("java.sql.Timestamp")){
			if(getEntity()[rowIndex-1].getStringColumnValue(columnName) != null){
				TimestampInput tempInput = new TimestampInput(columnName);
				theReturn = (ModuleObject) tempInput;
			}
			else{
				TimestampInput tempInput = new TimestampInput(columnName);
				theReturn = (ModuleObject) tempInput;
			}
		}
		else if (getEntity()[rowIndex-1].getStorageClassName(columnName).equals("java.sql.Date")){
			if(getEntity()[rowIndex-1].getStringColumnValue(columnName) != null){
				DateInput tempInput = new DateInput(columnName);
				tempInput.setDate((java.sql.Date)getEntity()[rowIndex-1].getColumnValue(columnName));
				theReturn = (ModuleObject) tempInput;
			}
			else{
				DateInput tempInput = new DateInput(columnName);
				theReturn = (ModuleObject) tempInput;
			}
		}
		else if (getEntity()[rowIndex-1].getStorageClassName(columnName).equals("java.sql.Time")){
			if(getEntity()[rowIndex-1].getStringColumnValue(columnName) != null){
				TimeInput tempInput = new TimeInput(columnName);
				tempInput.setTime((java.sql.Time)getEntity()[rowIndex-1].getColumnValue(columnName));
				theReturn = (ModuleObject) tempInput;
			}
			else{
				TimeInput tempInput = new TimeInput(columnName);
				theReturn = (ModuleObject) tempInput;
			}
		}

	return theReturn;

}


/*protected void addComponent(String columnName,int columnIndex,int rowIndex){
	if(getEntity()[rowIndex-1].getStringColumnValue(columnName) != null){
		TextInput tempInput = new TextInput(columnName,getEntity()[rowIndex-1].getStringColumnValue(columnName));
		tempInput.setSize(10);
		theTable.add(tempInput,columnIndex+1,rowIndex+1);
	}
	else{
		TextInput tempInput = new TextInput(columnName);
		tempInput.setSize(10);
		theTable.add(tempInput,columnIndex+1,rowIndex+1);
	}
}
*/





/*public void setInsertableList(GenericEntity entity){
	if (entity != null){

		initializeTable(entity.getVisibleColumnNames().length,3);


		for(int x = 0;x<entity.getVisibleColumnNames().length;x++){
			theTable.add(new Text(entity.getLongName(entity.getVisibleColumnNames()[x])),x+1,1);
		}

		for(int x = 0;x<entity.getVisibleColumnNames().length;x++){
			addComponent(entity.getVisibleColumnNames()[x],x,1);
		}

		theTable.add(new SubmitButton("insert"),entity.getVisibleColumnNames().length,3);
	}
}*/


public void setList(GenericEntity[] entity){
	setUpdatableList(entity);
}

public void setUpdatableList(GenericEntity[] entity){
	if (entity != null){
		if(entity.length > 0){
			initializeTable( entity[0].getVisibleColumnNames().length,entity.length+2);
			for(int y = 0;y<=entity.length;y++){
				if(y==0){
					for(int x = 0;x<entity[0].getVisibleColumnNames().length;x++){
						theTable.add(new Text(getEntity()[0].getLongName(entity[0].getVisibleColumnNames()[x])),x+1,y+1);
					}
				}
				else{
					for(int x = 0;x<entity[0].getVisibleColumnNames().length;x++){
						String columnName = entity[0].getVisibleColumnNames()[x];
						theTable.add(getComponent(columnName,y),x+1,y+1);
						//addComponent(getEntity()[0].getVisibleColumnNames()[x],x,y);

					}
				}
			}
			theTable.add(new SubmitButton("update"),entity[0].getVisibleColumnNames().length,entity.length+2);
		}

	}
}

public Parameter getObjectParameter(){
	if ( objectParameter == null){
		if (getEntity().length > 0){
			objectParameter = new Parameter("idega_special_form",getEntity()[0].getEntityName());
		}
	}
	return objectParameter;
}

public boolean thisObjectSubmitted(ModuleInfo modinfo){
	if(modinfo.parameterEquals(getObjectParameter())){
		return true;
	}
	else{
		return false;
	}
}

private void initializeTable(){
	initializeTable(1,1);
}

private void initializeTable(int columns,int rows){

	theTable = new Table(columns,rows);
	theTable.setBorder(0);
	theTable.setRowColor(1,"D0D0D0");
	if (getEntity()[0] != null){
		theForm.add(getObjectParameter());
	}
	theForm.add(theTable);
}


/*private void handleInsert(ModuleInfo modinfo)throws SQLException{
	HttpServletRequest request = modinfo.getRequest();
	HttpServletResponse response = modinfo.getResponse();
	int i=0;
	String[] columns = getEntity()[i].getVisibleColumnNames();

	for(int n = 0;n<columns.length;n++){
		//request parameter not empty
		if (!(request.getParameterValues(columns[n])[i].equals(""))){
			getEntity()[i].setStringColumn(columns[n],request.getParameterValues(columns[n])[i]);
		}
	}
	getEntity()[i].insert();

}*/

private void handleUpdate(ModuleInfo modinfo)throws SQLException{
	HttpServletRequest request = modinfo.getRequest();
	HttpServletResponse response = modinfo.getResponse();
	for (int i=0;i<getEntity().length;i++){
		boolean entityChanged=false;
		String[] columns = getEntity()[i].getVisibleColumnNames();
			for(int n = 0;n<columns.length;n++){
				//request parameter not empty
				if (!(request.getParameterValues(columns[n])[i].equals(""))){
					if (!(request.getParameterValues(columns[n])[i].equals(getEntity()[i].getStringColumnValue(columns[n])))){
						entityChanged=true;
						getEntity()[i].setStringColumn(columns[n],request.getParameterValues(columns[n])[i]);
					}
				}
				//request parameter empty - ""
				else{
					if( getEntity()[i].getStringColumnValue(columns[n]) != null ){
						entityChanged=true;
						getEntity()[i].setStringColumn(columns[n],request.getParameterValues(columns[n])[i]);
					}
				}
			}

		if (entityChanged){
			getEntity()[i].update();
		}
	}
}

public void main(ModuleInfo modinfo)throws IOException{
	HttpServletRequest request = modinfo.getRequest();
	HttpServletResponse response = modinfo.getResponse();
	if (thisObjectSubmitted(modinfo)){
		ModuleObjectContainer cont = (ModuleObjectContainer) request.getSession().getValue("idega_special_editablelist_parameters");
		if (cont != null){
			theForm.add(cont);
		}
		try{
			if (getAction().equals("update")){
				handleUpdate(modinfo);
			}
			/*else if (getAction().equals("insert")){
				handleInsert(modinfo);
			}*/
		}
		catch(SQLException ex){
			throw new IOException(ex.getMessage());
		}
	}
	else{
		ModuleObjectContainer cont = new ModuleObjectContainer();
		for (Enumeration enum = request.getParameterNames();enum.hasMoreElements();){
			String tempString = (String)enum.nextElement();
			cont.add(new Parameter(tempString,request.getParameter(tempString)));
		}
		theForm.add(cont);
		request.getSession().putValue("idega_special_editablelist_parameters",cont);
	}
}

public void beforePrint(ModuleInfo modinfo)throws IOException{
	if(getAction().equals("update")){
		setUpdatableList(getEntity());
	}
	/*else if (getAction().equals("insert")){
		//setInsertableList(getEntity()[0]);
		setVerticalInsertableList(getEntity()[0]);
	}*/
}

public void print(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);
	//main(modinfo);
	beforePrint(modinfo);
	if(theForm != null){
		theForm.print(modinfo);
	}
}



}
