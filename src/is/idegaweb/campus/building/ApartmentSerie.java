package is.idegaweb.campus.building;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import is.idegaweb.campus.presentation.Edit;
import com.idega.block.building.data.*;
import com.idega.block.building.business.*;
import java.util.List;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ApartmentSerie extends ModuleObjectContainer {
  protected boolean isAdmin = false;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.building";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  public ApartmentSerie() {
  }


  protected void control(ModuleInfo modinfo){

    if(isAdmin){
      if(modinfo.getParameter("make")!=null){
        updateApartmentSerie();
      }
      else if(modinfo.getParameter("print")!=null){
        add(printApartmentSerie());
      }
      else if(modinfo.getParameter("reload")!=null){
        BuildingCacher.reload();
      }
      add(makeMainTable());
    }
    else
      this.add(Edit.formatText(iwrb.getLocalizedString("access_denied","Access denied")));

  }

  public ModuleObject makeMainTable(){
    Table T = new Table();
    Link Make = new Link("Make");
    Make.addParameter("make","make");
    Link Print = new Link("Print");
    Print.addParameter("print","print");
    Link Reload = new Link("Reload");
    Print.addParameter("reload","reload");
    T.add(Make,1,1);
    T.add(Print,1,2);
    T.add(Reload,1,3);
    return T;
  }

  public void updateApartmentSerie(){
    List L = BuildingFinder.listOfApartment();
    if(L != null){
      int len = L.size();
      for (int i = 0; i < len; i++) {
        Apartment A = (Apartment) L.get(i);
        String name = A.getName().trim();
        String prefix = name;
        String postfix = "00";
        if(name.length() > 3){
          String s = name.substring(3,4);
          if(s.equalsIgnoreCase("a"))
            postfix = "01";
          else if(s.equalsIgnoreCase("b"))
            postfix = "02";
          prefix  = name.substring(0,3);
        }
        A.setSerie(prefix+postfix);
        System.err.println(A.getSerie());
        try {
          A.update();
        }
        catch (SQLException ex) {

        }
      }
    }
  }

  public ModuleObject printApartmentSerie(){
    //List L = BuildingCacher.getApartments();
    List L = BuildingFinder.ListOfAparmentOrderedByFloor();
    if(L != null){
      Table T = new Table();
      int len = L.size();
      for (int i = 0; i < len; i++) {
        try{
        Apartment A = (Apartment) L.get(i);
        Floor F = new Floor(A.getFloorId());
        Building B = new Building(F.getBuildingId());
        //Floor F = BuildingCacher.getFloor(A.getFloorId());
        //Building B = BuildingCacher.getBuilding(F.getBuildingId());
        T.add(B.getName(),1,i+1);
        T.add(F.getName(),2,i+1);
        T.add(A.getName(),3,i+1);
        T.add(B.getSerie()+A.getSerie(),4,i+1);
        }catch(Exception e){}
      }
      return T;
    }
    else return new Text("Nothing to print");

  }
   public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    try{
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    control(modinfo);
  }

}
