package is.idega.idegaweb.campus.block.building.presentation;

import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.sql.SQLException;
import java.util.List;

import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Floor;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class ApartmentSerie extends CampusBlock {
  protected boolean isAdmin = false;
  
  public String getLocalizedNameKey(){
    return "apartment_series";
  }

  public String getLocalizedNameValue(){
    return "Series";
  }


  protected void control(IWContext iwc){

    if(isAdmin){
      if(iwc.getParameter("make")!=null){
        updateApartmentSerie();
      }
      else if(iwc.getParameter("print")!=null){
        add(printApartmentSerie());
      }
      else if(iwc.getParameter("reload")!=null){
        BuildingCacher.reload();
      }
      add(makeMainTable());
    }
    else
      this.add(getNoAccessObject(iwc));

  }

  public PresentationObject makeMainTable(){
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
        //System.err.println(A.getSerie());
        try {
          A.update();
        }
        catch (SQLException ex) {

        }
      }
    }
  }

  public PresentationObject printApartmentSerie(){
    //List L = BuildingCacher.getApartments();
    List L = BuildingFinder.ListOfAparmentOrderedByFloor();
    if(L != null){
      Table T = new Table();
      int len = L.size();
      for (int i = 0; i < len; i++) {
        try{
        Apartment A = (Apartment) L.get(i);
        Floor F = A.getFloor();
        Building B = F.getBuilding();
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

  public void main(IWContext iwc){
  
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }

}
