//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.object.interfaceobject;

import java.io.*;
import com.idega.data.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import java.sql.*;
import javax.servlet.http.*;
import java.util.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class EntityInsert extends EntityManipulator{

private Table theTable;
private Form theForm;
private Parameter objectParameter;
private String action;
private String sessionStorageParameterName="idega_entity";
//private GenericEntity entity;
//private int debug1;

private String redirectPage;
private boolean notInsert=false;
private String buttonText;
private String[] fieldsNotDisplayed;
private Hashtable displayColumns;

/**
**Constructor to handle an insert of one row of the entity
**/
public EntityInsert(GenericEntity entity){
	this(entity,null);

}



public EntityInsert(GenericEntity entity,String redirectURLAfterInsert){
	super();
	action="insert";
	theForm = new Form();
	GenericEntity[] entityArr = (GenericEntity[])java.lang.reflect.Array.newInstance(entity.getClass(),1);
	entityArr[0]=entity;
	setEntity(entityArr);
	add(theForm);
	if(redirectURLAfterInsert != null){
		this.redirectPage=redirectURLAfterInsert+"&idega_special_form_event=entityInsert";
	}
	buttonText="Insert";
}




protected void setVerticalInsertableList(GenericEntity entity){
	if (entity != null){

        int numberOfFields=getNumberOfFields(entity);

		initializeTable(2,numberOfFields+1);
                String[] columns = getVisibleColumnNames(entity);

		for(int y = 0;y<numberOfFields;y++){
			theTable.add(new Text(entity.getLongName(columns[y])),1,y+1);
		}

		for(int y = 0;y<numberOfFields;y++){
			theTable.add(getComponent(columns[y],1),2,y+1);
		}

		theTable.setAlignment(2,numberOfFields+1,"right");
		theTable.add(new SubmitButton(buttonText),2,numberOfFields+1);
	}
}


public String getField(GenericEntity entity,int fieldNumber){
  String theReturn=null;
  String fields[] = entity.getVisibleColumnNames();
  int counter=-1;
  for(int i=0;i<fields.length;i++){
    if(isFieldDisplayed(fields[i])){
      counter++;
    }
    if (counter==fieldNumber){
        theReturn=fields[i];
        break;
    }
  }
  //System.out.println("debug fyrir EntityInsert: theReturn="+theReturn+" ,counter="+counter);
  return theReturn;

}


protected void setHorizontalInsertableList(GenericEntity entity){
	if (entity != null){

            int numberOfFields=getNumberOfFields(entity);

		initializeTable(numberOfFields,3);
                String[] columns = getVisibleColumnNames(entity);

		for(int x = 0;x<numberOfFields;x++){
			theTable.add(new Text(entity.getLongName(columns[x])),x+1,1);
		}

		for(int x = 0;x<numberOfFields;x++){
			theTable.add(getComponent(columns[x],1),x+1,2);
		}

		theTable.add(new SubmitButton(buttonText),numberOfFields,3);
	}
}


private int getNumberOfFields(GenericEntity entity){
  int theReturn = entity.getVisibleColumnNames().length;
  if(fieldsNotDisplayed!=null){
    theReturn=theReturn-fieldsNotDisplayed.length;
  }
  return theReturn;
}


protected Parameter getObjectParameter(){
	if ( objectParameter == null){
		if (getEntity().length > 0){
			objectParameter = new Parameter("idega_special_form_parameter",getEntity()[0].getEntityName());
		}
	}
	return objectParameter;
}

protected boolean thisObjectSubmitted(ModuleInfo modinfo){
	if(modinfo.parameterEquals(getObjectParameter())){
		return true;
	}
	else{
		return false;
	}
}


public String[] getVisibleColumnNames(GenericEntity entity){
  int length=getNumberOfFields(entity);
  String theReturn[] = new String[length];
  for(int i=0;i<length;i++){
    theReturn[i]=getField(entity,i);
  }
  return theReturn;
}

public void setFieldsNotDisplayed(String fieldNames[]){
  this.fieldsNotDisplayed=fieldNames;
}


public void setFieldNotDisplayed(String fieldName){
  if (fieldsNotDisplayed == null){
    fieldsNotDisplayed = new String[1];
    fieldsNotDisplayed[0] = fieldName;
  }
  else{
    String[] tempArr = new String[fieldsNotDisplayed.length+1];
    int i;
    for(i=0;i<fieldsNotDisplayed.length;i++){
      tempArr[i]=fieldsNotDisplayed[i];
    }
    tempArr[i]=fieldName;
    fieldsNotDisplayed=tempArr;
  }
}

private boolean isFieldDisplayed(String fieldName){
  if (fieldsNotDisplayed==null){
    return true;
  }
  else{
    boolean theReturn = true;
    if(fieldsNotDisplayed.length>0){
      for(int i=0;i<fieldsNotDisplayed.length;i++){
        if(fieldsNotDisplayed[i].equalsIgnoreCase(fieldName))
          {
          theReturn = false;
        }
      }
    }
    return theReturn;
  }
}

private void initializeTable(){
	initializeTable(1,1);
}

private void initializeTable(int columns,int rows){

	theTable = new Table(columns,rows);
	theTable.setBorder(0);
	if (getEntity()[0] != null){
		theForm.add(getObjectParameter());
	}
	theForm.add(theTable);
}


private void handleInsert(ModuleInfo modinfo)throws SQLException{
	HttpServletRequest request = modinfo.getRequest();
	HttpServletResponse response = modinfo.getResponse();
	int i=0;
	//String[] columns = getEntity()[i].getVisibleColumnNames();
        String[] columns = getVisibleColumnNames(getEntity()[0]);

	for(int n = 0;n<columns.length;n++){
		//request parameter not empty
		if (!(request.getParameterValues(columns[n])[i].equals(""))){
			getEntity()[i].setStringColumn(columns[n],request.getParameterValues(columns[n])[i]);
		}
	}
	if (!notInsert){
		getEntity()[i].insert();
	}
}


public void setNotToInsert(){
	notInsert=true;
}

public void main(ModuleInfo modinfo)throws Exception{
	/*HttpServletRequest request = modinfo.getRequest();
	HttpServletResponse response = modinfo.getResponse();
	if (thisObjectSubmitted(modinfo)){
		ModuleObjectContainer cont = (ModuleObjectContainer) request.getSession().getValue("idega_special_editablelist_parameters");
		if (cont != null){
			theForm.add(cont);
		}
		try{
			if (getAction().equals("insert")){
				handleInsert(modinfo);
			}
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
	}*/
	theForm.maintainAllParameters();
	//try{
		if (thisObjectSubmitted(modinfo)){
			if (getAction().equals("insert")){
                          System.err.println("action==insert");
				handleInsert(modinfo);
				modinfo.setSessionAttribute(sessionStorageParameterName,getFirstEntity());
                                if(getFirstEntity()==null){
                                  System.err.println("firstEntity==null");
                                }
                                else{
                                  System.err.println("firstEntity!=null");
                                }
				if (redirectPage != null){
					//modinfo.getResponse().sendRedirect(redirectPage);
					getParentPage().setToRedirect(redirectPage);
				}
				else{
					this.redirectPage=modinfo.getRequest().getRequestURI()+"&idega_special_form_event=entityInsert";
				}
				//modinfo.getResponse().sendRedirect(modinfo.getRequest().getRequestURI());
			}
                        else{
                            System.err.println("action!=insert");
                        }
		}
                else{
                    System.err.println("object!=submitted");
                }
	//}
	//catch(SQLException ex){

        //    (IOException)ex.fillInStackTrace();
            //throw new IOException(ex.getMessage());
	    //ex.printStackTrace();
	//}
        beforePrint(modinfo);

}

protected String getAction(){
	return this.action;
}

public void setButtonText(String buttonText){
	this.buttonText=buttonText;
}

protected void beforePrint(ModuleInfo modinfo)throws IOException{
	if (getAction().equals("insert")){
		//setInsertableList(getEntity()[0]);
		setVerticalInsertableList(getEntity()[0]);
	}
}


/**
 * This function returns the Object that has been inserted
 */
public GenericEntity getInsertedObject(ModuleInfo modinfo){
	GenericEntity returnObject = (GenericEntity)modinfo.getSessionAttribute(sessionStorageParameterName);
	//removeSessionAttribute(sessionStorageParameterName);
	return returnObject;
}

public boolean hasInserted(ModuleInfo modinfo){
	return thisObjectSubmitted(modinfo);
}

public void print(ModuleInfo modinfo)throws IOException{
	initVariables(modinfo);

	if(theForm != null){
		super.print(modinfo);
	}
}

public void setColumnValue(String columnName,Object columnValue){
  if(displayColumns==null){
    displayColumns = new Hashtable();
  }
  displayColumns.put(columnName,columnValue);
}

}
