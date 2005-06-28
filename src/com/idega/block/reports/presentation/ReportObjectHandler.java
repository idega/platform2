package com.idega.block.reports.presentation;



import java.sql.SQLException;

import com.idega.block.reports.business.ReportCondition;
import com.idega.block.reports.data.ReportCategory;
import com.idega.presentation.Block;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.TextInput;
import com.idega.repository.data.RefactorClassRegistry;



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

		drp.addMenuElement("","--");

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

      drp.addMenuElement("0","--");



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

    java.util.List L  = null;
    try{

      com.idega.data.IDOLegacyEntity leg =  (com.idega.data.IDOLegacyEntity)com.idega.data.IDOLookup.instanciateEntity(RefactorClassRegistry.forName(Entity));
     // java.lang.Class.forName(Entity)
      //com.idega.data.IDOHome home = (com.idega.data.IDOHome) com.idega.data.IDOLookup.getHomeLegacy(java.lang.Class.forName(Entity));
      //com.idega.data.IDOEntity ge = home.createIDO();
      L = com.idega.data.EntityFinder.findAll(leg);

      //IDOLegacyEntity ge = (IDOLegacyEntity)java.lang.Class.forName(Entity).newInstance();

      //entities = ge.findAll();

    }

    //catch(SQLException e){e.printStackTrace();}

    catch(Exception e){e.printStackTrace();}

    DropdownMenu drp = new DropdownMenu(Name);

    drp.addMenuElement("0","--");

    if(L!=null){
      java.util.Iterator iter = L.iterator();
      while(iter.hasNext()){
        com.idega.data.IDOLegacyEntity leg = (com.idega.data.IDOLegacyEntity) iter.next();
        String sField = leg.getStringColumnValue(field);
        if(sField.length()>=20)
          sField = sField.substring(0,20);
        drp.addMenuElement(sField);

      }
      /*
      for(int i = 0; i < entities.length ; i++){
        if(entities[i]!=null){
        String sField = entities[i].getStringColumnValue(field);
        if(sField!=null)
        drp.addMenuElement(sField);
        }
      }
      */
    }

    if(!selected.equalsIgnoreCase("")){

      drp.setSelectedElement(selected);

    }

    return drp;

  }

}
