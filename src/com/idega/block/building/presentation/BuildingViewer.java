package com.idega.block.building.presentation;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.util.text.TextSoap;
import com.idega.data.*;
import com.idega.block.building.business.BuildingFinder;
import com.idega.data.genericentity.Address;
import com.idega.block.building.data.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.idegaweb.IWResourceBundle;

/**
 * Title: BuildingViewer
 * Description: Views buildings in a campus
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi
 * @version 1.0
 */

public class BuildingViewer extends JModuleObject{

private static final String IW_RESOURCE_BUNDLE = "com.idega.block.building";
public static final String PARAMETER_STRING = "complex_id";
private int building_id = 0;
private String nameStyle = "font-family:verdana; font-size: 11pt; font-weight: bold; color: #27324B;";
private String addressStyle = "font-family:verdana; font-size: 10pt; font-weight: bold; color: #9FA9B3;";
private String infoStyle= "font-family:verdana,arial,sans-serif; font-size:10px; color:#000000; text-align: justify;";
protected IWResourceBundle iwrb_;

public BuildingViewer(){
}

public BuildingViewer(int building_id){
    this.building_id=building_id;
}


  public void main(ModuleInfo modinfo) throws Exception {
    if ( iwrb_ == null ) {
      iwrb_ = getResourceBundle(modinfo);
    }

    if ( modinfo.getParameter(PARAMETER_STRING) != null ) {
      try {
        building_id = Integer.parseInt(modinfo.getParameter(PARAMETER_STRING));
      }
      catch (NumberFormatException e) {
        building_id = 0;
      }
    }

    if ( building_id == 0 ) {
      getAllBuildings(modinfo);
    }

    else {
      getSingleBuilding(modinfo);
    }

  }

  public void setDefaultValues() {
    this.nameStyle = "font-family:verdana; font-size: 11pt; font-weight: bold; color: #27324B;";
    this.addressStyle = "font-family:verdana; font-size: 10pt; font-weight: bold; color: #9FA9B3;";
    this.infoStyle = "font-family:arial; font-size:8pt; color:#000000; line-height: 1.8; text-align: justify;";
  }

  private void getAllBuildings(ModuleInfo modinfo) throws SQLException {

    Complex[] complex = (Complex[]) (new Complex()).findAllOrdered(Complex.getNameColumnName());

    Table campusTable = new Table(1,complex.length);

    for ( int a = 0; a < complex.length; a++ ) {
      int iComplexId = complex[a].getID();
      Table complexTable = new Table(3,4);
        complexTable.mergeCells(2,1,2,4);
        complexTable.mergeCells(3,2,3,3);
        complexTable.setVerticalAlignment(3,2,"top");
        complexTable.setVerticalAlignment(1,2,"bottom");
        complexTable.setWidth("100%");
        complexTable.setWidth(2,1,"20");
        complexTable.setBorder(0);

      String infoText = complex[a].getInfo();
        infoText = TextSoap.findAndReplace(infoText,"\n","<br>");


      List L = BuildingFinder.listOfBuildingsInComplex(iComplexId);
      if(L!=null){
       Image buildingImage = new Image(((Building)L.get(0)).getImageId());
       complexTable.add(buildingImage,3,2);
      }

      Image moreImage = iwrb_.getImage("/building/more.gif");

      Link complexLink = new Link(moreImage,modinfo.getRequestURI());
        complexLink.addParameter(PARAMETER_STRING,iComplexId);

      complexTable.add(getNameText(complex[a].getName()),1,1);
      complexTable.add(getInfoText(infoText),1,2);
      complexTable.add(complexLink,1,3);

      String divideText = "<br>.........<br><br>";

      campusTable.add(complexTable,1,a+1);
      if ( a+1 < complex.length ) {
        campusTable.add(getInfoText(divideText),1,a+1);
      }
    }

    if ( complex.length == 0 ) {
      add(iwrb_.getLocalizedString("no_buildings","No buildings in database"));
    }

    else {
      add(campusTable);
    }

  }

   private Text getInfoText(String text){
      text = formatText(text);
      Text T = new Text(text);
      T.setFontStyle(infoStyle);
      return T;
    }

    private Text getAddressText(String text){
      Text T = new Text(text);
      T.setFontStyle(addressStyle);
      return T;
    }

    private Text getNameText(String text){
      Text T = new Text(text);
      T.setFontStyle(nameStyle);
      return T;
    }

  private void getSingleBuilding(ModuleInfo modinfo) throws SQLException {

    Complex complex = new Complex(building_id);
    ApartmentType[] types = BuildingFinder.findApartmentTypesInComplex(building_id);

    Table complexTable = new Table(1,types.length+1);
      complexTable.setWidth("100%");

    complexTable.add(getNameText(complex.getName()),1,1);
    for ( int a = 0; a < types.length; a++ ) {

      Table typesTable = new Table(2,3);
        typesTable.setVerticalAlignment(2,2,"top");
        typesTable.setVerticalAlignment(1,2,"top");
        typesTable.setWidth("100%");

      String typeName = formatText(types[a].getName()+" "+types[a].getArea()+"m2");

      String typeText = types[a].getInfo();
        typeText = TextSoap.findAndReplace(typeText,"\n","<br>");

      String divideText = ("<br>.........<br><br>");

      Image typeImage = new Image(types[a].getImageId());
        typeImage.setHorizontalSpacing(6);

      Window typeWindow = new Window("Herbergi",ApartmentTypeViewer.class,Page.class);
        typeWindow.setWidth(400);
        typeWindow.setHeight(550);
        typeWindow.setScrollbar(false);
      Image moreImage = iwrb_.getImage("/building/more.gif");
      Image backImage = iwrb_.getImage("/building/back.gif");
      Link typeLink = new Link(moreImage,typeWindow);
        typeLink.addParameter(ApartmentTypeViewer.PARAMETER_STRING,types[a].getID());

      BackButton BB = new BackButton( backImage);
      typesTable.add(getAddressText(typeName),1,1);
      typesTable.add(getInfoText(typeText),1,2);
      typesTable.add(BB,1,3);
      typesTable.add("&nbsp;&nbsp;&nbsp;",1,3);
      typesTable.add(typeLink,1,3);
      if ( types[a].getImageId() != -1 )
        typesTable.add(typeImage,2,2);

      complexTable.add(typesTable,1,a+2);
      if ( a+1 < types.length ) {
        complexTable.add(getInfoText(divideText),1,a+2);
      }
    }
    add(complexTable);
  }

  public void setNameStyle(String nameStyle) {
    this.nameStyle=nameStyle;
  }

  public void setAddressStyle(String addressStyle) {
    this.addressStyle=addressStyle;
  }

  public void setInfoStyle(String infoStyle) {
    this.infoStyle=infoStyle;
  }

  public String formatText(String text) {
    text = TextSoap.findAndReplace(text,"m2","m<sup>2</sup>");
    return text;
  }

  public String getBundleIdentifier() {
    return(IW_RESOURCE_BUNDLE);
  }
}