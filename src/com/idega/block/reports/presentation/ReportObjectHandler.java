package com.idega.block.reports.presentation;



import com.idega.block.reports.data.*;

import com.idega.block.reports.business.*;

import com.idega.presentation.Block;

import com.idega.presentation.IWContext;

import java.sql.SQLException;

import com.idega.presentation.Table;

import com.idega.presentation.ui.*;

import com.idega.presentation.Script;

import com.idega.presentation.PresentationObject;

import com.idega.data.IDOLegacyEntity;



public class ReportObjectHandler extends Block{





  public static DropdownMenu drpCategories(String sPrm,String selected) {

    ReportCategory[] cat = new ReportCategory[0];

    try{

      cat = (ReportCategory[]) (((com.idega.block.reports.data.ReportCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ReportCategory.class)).createLegacy()).findAll();

    }

    catch(SQLException sql){}

    DropdownMenu drp = new DropdownMenu(sPrm);

    drp.addMenuElement("0","Flokkar");

    for (int i = 0; i < cat.length; i++) {

      drp.addMenuElement(cat[i].getID(),cat[i].getName());

    }

    if(!"".equalsIgnoreCase(selected))

      drp.setSelectedElement(selected);

    return drp;

  }



  public static InterfaceObject getInput(ReportCondition RC,String Name,String selected){

    InterfaceObject mo = null;

    String Type = RC.getItem().getConditionType();

    if(Type.equalsIgnoreCase("T"))

      mo =  new TextInput(Name);

    else if(Type.equalsIgnoreCase("I")){

			TextInput ti = new TextInput(Name);

			ti.setAsIntegers();

      mo =  ti;



    }

    else if(Type.equalsIgnoreCase("S"))

      mo =  drpEntity(Name,RC.getItem().getEntity(),RC.getItem().getField(),selected,false);

    else if(Type.equalsIgnoreCase("C"))

      mo = drpValues(RC,Name,selected,false);

    else if(Type.equalsIgnoreCase("D"))

    mo = drpValues(RC,Name,selected,true);

    return mo;

  }



	public static DropdownMenu drpTypes(String name,String selected){

		DropdownMenu drp = new DropdownMenu(name);

		drp.addMenuElement("I","Integer");

		drp.addMenuElement("T","Text");

		drp.addMenuElement("S","Select All");

		drp.addMenuElement("C","List with default");

		drp.addMenuElement("D","List without default");

    if(!selected.equalsIgnoreCase(""))

      drp.setSelectedElement(selected);

    return drp;

	}



	public static DropdownMenu drpFunctions(String name,String selected){

		DropdownMenu drp = new DropdownMenu(name);

		drp.addDisabledMenuElement("","--");

		drp.addMenuElement("COUNT","COUNT");

		drp.addMenuElement("AVG","AVERAGE");

		drp.addMenuElement("SUM","SUM");

		drp.addMenuElement("MAX","MAX");

		drp.addMenuElement("MIN","MIN");

		drp.addMenuElement("UPPER","UPPER");

    if(!selected.equalsIgnoreCase(""))

      drp.setSelectedElement(selected);

    return drp;

	}



	public static DropdownMenu drpOperators(String name,String selected){

		DropdownMenu drp = new DropdownMenu(name);

		drp.addMenuElement("LIKE","LIKE");

		drp.addMenuElement("=","EQUAL");

		drp.addMenuElement("<=","LESS OR EQUAL");

		drp.addMenuElement("<","LESS");

		drp.addMenuElement(">=","GREATER OR EQUAL");

		drp.addMenuElement("!=","NOT EQUAL");

		drp.addMenuElement("!=","NOT EQUAL");

		drp.addMenuElement("BETWEEN","BETWEEN");

		drp.addMenuElement("IN","IN");

    if(!selected.equalsIgnoreCase(""))

      drp.setSelectedElement(selected);

    return drp;

	}



  public static DropdownMenu drpValues(ReportCondition RC,String Name,String selected,boolean disabledvalue){

    DropdownMenu drp = new DropdownMenu(Name);

    if(disabledvalue )

      drp.addDisabledMenuElement("0","--");



    String[][] data = RC.getItem().getData();

    if(data != null){

      if(data.length ==1 && data[0] != null){

        int len = data[0].length;

        for (int i = 0; i < len; i++) {

          drp.addMenuElement(data[0][i]);

        }

      }

      else if(data.length == 2 && data[0] != null && data[1] != null){

        int len = data[1].length;

        for (int i = 0; i < len; i++) {

          drp.addMenuElement(data[0][i],data[1][i]);

        }

      }

      if(!selected.equalsIgnoreCase(""))

        drp.setSelectedElement(selected);

    }

    return drp;

  }



  public static DropdownMenu drpInteger(String Name,String selected,int f,int l){

    DropdownMenu drp = new DropdownMenu(Name);

    for(int i = f; i < l+1; i++){

      drp.addMenuElement(String.valueOf(i));

    }

    if(!selected.equalsIgnoreCase(""))

      drp.setSelectedElement(selected);

    return drp;

  }



  private static DropdownMenu drpEntity(String Name,String Entity,String field ,String selected,boolean withID) {

    IDOLegacyEntity[] entities = new IDOLegacyEntity[1];

    try{

      IDOLegacyEntity ge = (IDOLegacyEntity)java.lang.Class.forName(Entity).newInstance();

      entities = ge.findAll();

    }

    catch(SQLException e){}

    catch(Exception e){}

    DropdownMenu drp = new DropdownMenu(Name);

    drp.addDisabledMenuElement("0","--");

    for(int i = 0; i < entities.length ; i++){

      drp.addMenuElement(entities[i].getStringColumnValue(field));

    }

    if(!selected.equalsIgnoreCase("")){

      drp.setSelectedElement(selected);

    }

    return drp;

  }

}
