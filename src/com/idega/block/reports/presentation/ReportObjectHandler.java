package com.idega.block.reports.presentation;

import com.idega.block.reports.data.*;
import com.idega.block.reports.business.*;
import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import java.sql.SQLException;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Script;
import com.idega.jmodule.object.ModuleObject;
import com.idega.data.GenericEntity;

public class ReportObjectHandler extends JModuleObject{


  public static DropdownMenu drpCategories(String sPrm,String selected) {
    ReportCategory[] cat = new ReportCategory[0];
    try{
      cat = (ReportCategory[]) (new ReportCategory()).findAll();
    }
    catch(SQLException sql){}
    DropdownMenu drp = new DropdownMenu(sPrm);
    drp.addMenuElement("0","Flokkar");
    for (int i = 0; i < cat.length; i++) {
      drp.addMenuElement(cat[i].getID(),cat[i].getName());
    }
    if(!selected.equalsIgnoreCase(""))
      drp.setSelectedElement(selected);
    return drp;
  }

  public static ModuleObject getInput(ReportCondition RC,String Name,String selected){
    ModuleObject mo = null;
    String Type = RC.getItem().getConditionType();
    if(Type.equalsIgnoreCase("T"))
      mo =  new TextInput(Name);
    else if(Type.equalsIgnoreCase("I")){
      mo =  new TextInput(Name);
    }
    else if(Type.equalsIgnoreCase("S"))
      mo =  drpEntity(Name,RC.getItem().getEntity(),RC.getItem().getField(),selected,false);
    return mo;
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
    GenericEntity[] entities = new GenericEntity[1];
    try{
      GenericEntity ge = (GenericEntity)java.lang.Class.forName(Entity).newInstance();
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