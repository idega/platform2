package com.idega.block.building.presentation;

import java.sql.SQLException;

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
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class ApartmentChooser extends BuildingEditor{

  public void main(IWContext iwc)  {
    try{
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

  private PresentationObject getApartments(){
    int border = 0;
    int padding = 6;
    int spacing = 1;

  try{

    Complex[] C = (Complex[])(((com.idega.block.building.data.ComplexHome)com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).createLegacy()).findAll();
    int clen = C.length;
    int b = 1, f = 1;

    Table T = new Table();
    T.setRowVerticalAlignment(1,"top");
    T.setCellpadding(padding);
    T.setCellspacing(spacing);
    T.setVerticalZebraColored("#942829","#21304a");
    T.setBorder(border);
    for (int i = 0; i < clen; i++) {
      T.add(getHeaderText( C[i].getName()),i+1,1);
      Building[] B = (Building[])(((com.idega.block.building.data.BuildingHome)com.idega.data.IDOLookup.getHomeLegacy(Building.class)).createLegacy()).findAllByColumnOrdered(com.idega.block.building.data.BuildingBMPBean.getComplexIdColumnName(),String.valueOf(C[i].getID()),com.idega.block.building.data.BuildingBMPBean.getNameColumnName());
      int blen = B.length;
      Table BuildingTable = new Table();
      BuildingTable.setCellpadding(padding);
      BuildingTable.setCellspacing(spacing);
      BuildingTable.setBorder(border);
      T.add(BuildingTable,i+1,2);
      b = 1;
      for (int j = 0; j < blen; j++) {
        BuildingTable.add(getHeaderText( B[j].getName()),1,b++);
        Floor[] F = (Floor[])(((com.idega.block.building.data.FloorHome)com.idega.data.IDOLookup.getHomeLegacy(Floor.class)).createLegacy()).findAllByColumnOrdered(com.idega.block.building.data.FloorBMPBean.getBuildingIdColumnName(),String.valueOf(B[j].getID()),com.idega.block.building.data.FloorBMPBean.getNameColumnName());
        int flen = F.length;
        Table FloorTable = new Table();
        FloorTable.setCellpadding(padding);
        FloorTable.setCellspacing(spacing);
        FloorTable.setBorder(border);
        BuildingTable.add(FloorTable,1,b++);
        f = 1;
        for (int k = 0; k < flen; k++) {
          FloorTable.add(getHeaderText(F[k].getName()),1,f++);
          Apartment[] A = (Apartment[])(((com.idega.block.building.data.ApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).createLegacy()).findAllByColumnOrdered(com.idega.block.building.data.ApartmentBMPBean.getFloorIdColumnName(),String.valueOf(F[k].getID()),com.idega.block.building.data.ApartmentBMPBean.getNameColumnName());
          int alen = A.length;
          if(alen > 0){
            Table ApartmentTable = new Table();
            ApartmentTable.setCellpadding(padding);
            ApartmentTable.setBorder(border);
            ApartmentTable.setCellspacing(spacing);
            FloorTable.add(ApartmentTable,1,f++);
            for (int l = 0; l < alen; l++) {
              ApartmentTable.add(getApLink(A[l].getID(),A[l].getName()),1,l+1);
            }
          }

        }

      }

    }
      T.setRowVerticalAlignment(2,"top");
      T.setVerticalZebraColored("#942829","#21304a");
       return T;
    }
    catch(SQLException sql){
      return new Text();
    }
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

  private Link getApLink(int id,String name){
    Link L = new Link(name);
      L.setFontColor("#FFFFFF");
      L.addParameter("dr_id",id);
      //L.addParameter(sAction,APARTMENT);
      //L.addParameter("bm_choice",APARTMENT);

    return L;
  }

}// class AppartmentChooser
