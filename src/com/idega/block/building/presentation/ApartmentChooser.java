package com.idega.block.building.presentation;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.*;
import com.idega.block.building.data.*;
import com.idega.block.building.presentation.BuildingEditor;
import java.sql.SQLException;
import java.io.IOException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class ApartmentChooser extends BuildingEditor{

  public void main(ModuleInfo modinfo)  {
    try{
      String apartment = modinfo.getParameter("dr_id");

      if ( apartment != null ) {
        modinfo.setSessionAttribute("dr_id",modinfo.getParameter("dr_id"));
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

  private ModuleObject getApartments(){
    int border = 0;
    int padding = 6;
    int spacing = 1;

  try{

    Complex[] C = (Complex[])(new Complex()).findAll();
    int clen = C.length;
    int c = 1, b = 1, f = 1, a = 1;

    Table T = new Table();
    T.setRowVerticalAlignment(1,"top");
    T.setCellpadding(padding);
    T.setCellspacing(spacing);
    T.setVerticalZebraColored("#942829","#21304a");
    T.setBorder(border);
    for (int i = 0; i < clen; i++) {
      T.add(getHeaderText( C[i].getName()),i+1,1);
      Building[] B = (Building[])(new Building()).findAllByColumnOrdered(Building.getComplexIdColumnName(),String.valueOf(C[i].getID()),Building.getNameColumnName());
      int blen = B.length;
      Table BuildingTable = new Table();
      BuildingTable.setCellpadding(padding);
      BuildingTable.setCellspacing(spacing);
      BuildingTable.setBorder(border);
      T.add(BuildingTable,i+1,2);
      b = 1;
      for (int j = 0; j < blen; j++) {
        BuildingTable.add(getHeaderText( B[j].getName()),1,b++);
        Floor[] F = (Floor[])(new Floor()).findAllByColumnOrdered(Floor.getBuildingIdColumnName(),String.valueOf(B[j].getID()),Floor.getNameColumnName());
        int flen = F.length;
        Table FloorTable = new Table();
        FloorTable.setCellpadding(padding);
        FloorTable.setCellspacing(spacing);
        FloorTable.setBorder(border);
        BuildingTable.add(FloorTable,1,b++);
        f = 1;
        for (int k = 0; k < flen; k++) {
          FloorTable.add(getHeaderText(F[k].getName()),1,f++);
          Apartment[] A = (Apartment[])(new Apartment()).findAllByColumnOrdered(Apartment.getFloorIdColumnName(),String.valueOf(F[k].getID()),Apartment.getNameColumnName());
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
