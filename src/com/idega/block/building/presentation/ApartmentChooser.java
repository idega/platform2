package com.idega.block.building.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.building.business.BuildingService;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */


public class ApartmentChooser extends BuildingEditor{
	
	private BuildingService service =null;

  public void main(IWContext iwc)  {
  
    try{
    	service =getBuildingService(iwc);
      String apartment = iwc.getParameter("dr_id");

      if ( apartment != null ) {
        iwc.setSessionAttribute("dr_id",iwc.getParameter("dr_id"));
        this.getParentPage().setParentToReload();
        this.getParentPage().close();
      }
      else {
        this.getParentPage().setTitle("Appartment Chooser");
        this.getParentPage().setAllMargins(0);
        add(getApartments());
      }
    }
    catch(Exception e){
      e.printStackTrace(System.err);
    }
  }

  private PresentationObject getApartments()throws FinderException,RemoteException{
    int border = 0;
    int padding = 6;
    int spacing = 1;



    //Complex[] C = (Complex[])(((com.idega.block.building.data.ComplexHome)com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).createLegacy()).findAll();
  	Collection complexes = service.getComplexHome().findAll();
    //int clen = C.length;
    int b = 1, f = 1;

    Table T = new Table();
    T.setRowVerticalAlignment(1,"top");
    T.setCellpadding(padding);
    T.setCellspacing(spacing);
    T.setVerticalZebraColored("#942829","#21304a");
    T.setBorder(border);
    int i =1;
    for (Iterator iter = complexes.iterator(); iter.hasNext();) {
		Complex complex = (Complex) iter.next();
		
	
      T.add(getHeaderText( complex.getName()),i,1);
      Collection buildings =service.getBuildingHome().findByComplex((Integer)complex.getPrimaryKey());
      //Building[] B = (Building[])(((com.idega.block.building.data.BuildingHome)com.idega.data.IDOLookup.getHomeLegacy(Building.class)).createLegacy()).findAllByColumnOrdered(com.idega.block.building.data.BuildingBMPBean.getComplexIdColumnName(),String.valueOf(C[i].getID()),com.idega.block.building.data.BuildingBMPBean.getNameColumnName());
      //int blen = B.length;
      Table BuildingTable = new Table();
      BuildingTable.setCellpadding(padding);
      BuildingTable.setCellspacing(spacing);
      BuildingTable.setBorder(border);
      T.add(BuildingTable,i++,2);
      b = 1;
      //for (int j = 0; j < blen; j++) {
      for (Iterator iter2 = buildings.iterator(); iter2.hasNext();) {
		Building building = (Building) iter2.next();
		
	
        BuildingTable.add(getHeaderText( building.getName()),1,b++);
        Collection floors = service.getFloorHome().findByBuilding((Integer)building.getPrimaryKey());
        //Floor[] F = (Floor[])(((com.idega.block.building.data.FloorHome)com.idega.data.IDOLookup.getHomeLegacy(Floor.class)).createLegacy()).findAllByColumnOrdered(com.idega.block.building.data.FloorBMPBean.getBuildingIdColumnName(),String.valueOf(B[j].getID()),com.idega.block.building.data.FloorBMPBean.getNameColumnName());
       // int flen = F.length;
        Table FloorTable = new Table();
        FloorTable.setCellpadding(padding);
        FloorTable.setCellspacing(spacing);
        FloorTable.setBorder(border);
        BuildingTable.add(FloorTable,1,b++);
        f = 1;
        for (Iterator iter3 = floors.iterator(); iter3.hasNext();) {
			Floor floor = (Floor) iter3.next();
          FloorTable.add(getHeaderText(floor.getName()),1,f++);
          //Apartment[] A = (Apartment[])(((com.idega.block.building.data.ApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).createLegacy()).findAllByColumnOrdered(com.idega.block.building.data.ApartmentBMPBean.getFloorIdColumnName(),String.valueOf(F[k].getID()),com.idega.block.building.data.ApartmentBMPBean.getNameColumnName());
          Collection apartments =service.getApartmentHome().findByFloor((Integer)floor.getPrimaryKey());
          //int alen = A.length;
         // if(alen > 0){
          if(apartments!=null &&!apartments.isEmpty()){
            Table ApartmentTable = new Table();
            ApartmentTable.setCellpadding(padding);
            ApartmentTable.setBorder(border);
            ApartmentTable.setCellspacing(spacing);
            FloorTable.add(ApartmentTable,1,f++);
           // for (int l = 0; l < alen; l++) {
            int l = 1;
            for (Iterator iter4 = apartments.iterator(); iter4.hasNext();) {
				Apartment apartment = (Apartment) iter4.next();
				
			
              ApartmentTable.add(getApLink(apartment.getPrimaryKey().toString(),apartment.getName()),1,l++);
            }
          }

        }

      }

    }
      T.setRowVerticalAlignment(2,"top");
      T.setVerticalZebraColored("#942829","#21304a");
       return T;
    
  }

  private Text getHeaderText(String s){
    Text T = new Text(s);
    T.setBold();
    T.setFontColor("#FFFFFF");
    return T;
  }

  private Link getATLink(int id,String name){
    Link L = new Link(name);
      L.setFontColor("#FFFFFF");
      L.addParameter("dr_id",id);
      //L.addParameter(sAction,TYPE);
      //L.addParameter("bm_choice",TYPE);

    return L;
  }

  private Link getApLink(String id,String name){
    Link L = new Link(name);
      L.setFontColor("#FFFFFF");
      L.addParameter("dr_id",id);
      //L.addParameter(sAction,APARTMENT);
      //L.addParameter("bm_choice",APARTMENT);

    return L;
  }
  


}// class AppartmentChooser
