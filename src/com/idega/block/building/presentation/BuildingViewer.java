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
//import com.idega.data.genericentity.Address;
import com.idega.block.building.data.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.core.data.ICFile;

/**
 * Title: BuildingViewer
 * Description: Views buildings in a campus
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi©
 * @version 1.0
 */

public class BuildingViewer extends Block{

private static final String IW_RESOURCE_BUNDLE = "com.idega.block.building";
public static final String PARAMETER_STRING = "complex_id";
private int building_id = 0;
private String nameStyle = "font-family:verdana; font-size: 11pt; font-weight: bold; color: #27324B;";
private String addressStyle = "font-family:verdana; font-size: 10pt; font-weight: bold; color: #9FA9B3;";
private String infoStyle= "font-family:verdana,arial,sans-serif; font-size:10px; color:#000000; text-align: justify;";
protected IWResourceBundle iwrb_;
private Class apartmentTypeWindowClass = ApartmentTypeWindow.class;

private int imageMaxSize = 165;

public BuildingViewer(){
}

public BuildingViewer(int building_id){
    this.building_id=building_id;
}

public void setApartmentTypeWindowClass(Class windowClass){
  apartmentTypeWindowClass = windowClass;
}


  public void main(IWContext iwc) throws Exception {
    if ( iwrb_ == null ) {
      iwrb_ = getResourceBundle(iwc);
    }

    if ( iwc.getParameter(PARAMETER_STRING) != null ) {
      try {
        building_id = Integer.parseInt(iwc.getParameter(PARAMETER_STRING));
      }
      catch (NumberFormatException e) {
        building_id = 0;
      }
    }

    if ( building_id == 0 ) {
      getAllBuildings(iwc);
    }

    else {
      getSingleBuilding(iwc);
    }

  }

  public void setDefaultValues() {
    this.nameStyle = "font-family:verdana; font-size: 11pt; font-weight: bold; color: #27324B;";
    this.addressStyle = "font-family:verdana; font-size: 10pt; font-weight: bold; color: #9FA9B3;";
    this.infoStyle = "font-family:arial; font-size:8pt; color:#000000; line-height: 1.8; text-align: justify;";
  }

  private void getAllBuildings(IWContext iwc) throws SQLException {

    Complex[] complex = (Complex[]) (((com.idega.block.building.data.ComplexHome)com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).createLegacy()).findAllOrdered(com.idega.block.building.data.ComplexBMPBean.getNameColumnName());
    Table campusTable = new Table(1,complex.length);

    for ( int a = 0; a < complex.length; a++ ) {
      int iComplexId = complex[a].getID();
      String[] types = BuildingFinder.findDistinctApartmentTypesInComplex(iComplexId);

      Table complexTable = new Table(3,4);
        complexTable.mergeCells(2,1,2,4);
        complexTable.mergeCells(1,2,1,3);
        complexTable.mergeCells(3,3,3,4);
        complexTable.setAlignment(3,3,"center");
        complexTable.setVerticalAlignment(3,2,"top");
        complexTable.setVerticalAlignment(3,3,"top");
        complexTable.setVerticalAlignment(1,2,"top");
        complexTable.setVerticalAlignment(1,4,"bottom");
        complexTable.setWidth("100%");
        complexTable.setWidth(2,1,"20");
        complexTable.setBorder(0);

      String infoText = complex[a].getInfo();
        infoText = TextSoap.findAndReplace(infoText,"\n","<br>");

      //List L = BuildingFinder.listOfBuildingsInComplex(iComplexId);
      List L = BuildingFinder.listOfBuildingImageFiles(iComplexId);
      if(L!=null){
       //ICFile file = ((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).findByPrimaryKeyLegacy(((Building)L.get(0)).getImageId());
        ImageSlideShow slide = new ImageSlideShow();
        //slide.setFileFolder(file);
        slide.setWidth(imageMaxSize);
        slide.setAlt(complex[a].getName());
        slide.setFiles(L);
        complexTable.add(slide,3,2);
        /*
       Image buildingImage = new Image(((Building)L.get(0)).getImageId());
       buildingImage.setMaxImageWidth(imageMaxSize);
       complexTable.add(buildingImage,3,2);
       */
      }

      if ( types != null ) {
        for ( int b = 0; b < types.length; b++ ) {
          ApartmentCategory cat = ((com.idega.block.building.data.ApartmentCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentCategory.class)).findByPrimaryKeyLegacy(Integer.parseInt(types[b]));
          Image image = new Image(cat.getImageId());
            image.setName("");
            image.setHorizontalSpacing(4);
            image.setVerticalSpacing(2);
          if ( cat.getImageId() != -1 )
            complexTable.add(image,3,3);
        }
      }


      Image moreImage = iwrb_.getImage("/building/more.gif");
      Image mapImage = iwrb_.getImage("/building/map.gif");

      Link complexLink = new Link(moreImage,iwc.getRequestURI());
        complexLink.addParameter(PARAMETER_STRING,iComplexId);

      Link locationLink = new Link(mapImage);
        locationLink.setWindowToOpen(BuildingLocation.class);
        locationLink.addParameter(PARAMETER_STRING,iComplexId);

      complexTable.add(getNameText(complex[a].getName()),1,1);
      complexTable.add(getInfoText(infoText),1,2);
      complexTable.add(complexLink,1,4);
      complexTable.add("&nbsp;&nbsp;&nbsp;",1,4);
      complexTable.add(locationLink,1,4);

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

  private void getSingleBuilding(IWContext iwc) throws SQLException {

    Complex complex = ((com.idega.block.building.data.ComplexHome)com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).findByPrimaryKeyLegacy(building_id);
    ApartmentType[] types = BuildingFinder.findApartmentTypesInComplex(building_id);

    Table complexTable = new Table(1,types.length+1);
      complexTable.setWidth("100%");

    complexTable.add(getNameText(complex.getName()),1,1);
    for ( int a = 0; a < types.length; a++ ) {

      Table typesTable = new Table(2,3);
        typesTable.setVerticalAlignment(2,2,"top");
        typesTable.setVerticalAlignment(1,2,"top");
        typesTable.setVerticalAlignment(1,1,"top");
        typesTable.mergeCells(2,1,2,2);
        typesTable.setAlignment(2,2,"right");
        typesTable.setWidth("100%");
        typesTable.setHeight(2,"100%");
        typesTable.setWidth(1,"100%");

      String typeName = formatText(types[a].getName()+" "+types[a].getArea()+"m2");

      //String typeText = types[a].getInfo();
      String typeText = types[a].getExtraInfo();
        typeText = TextSoap.findAndReplace(typeText,"\n","<br>");

      String divideText = ("<br>.........<br><br>");


      PresentationObject typeImage;
      if ( types[a].getImageId() == -1 )
        typeImage = iwrb_.getImage("/building/default.jpg");
      else{
        ImageSlideShow slide =  new ImageSlideShow();
        slide.setWidth(imageMaxSize);
        slide.setAlt(types[a].getName());
        slide.setFileId( types[a].getImageId());
        typeImage = slide;

      }

       // Image typeImage = new Image(types[a].getImageId());
       // typeImage.setHorizontalSpacing(6);
       // typeImage.setMaxImageWidth(imageMaxSize);

      /*Window typeWindow = new Window("Herbergi",ApartmentTypeViewer.class,Page.class);
        typeWindow.setWidth(400);
        typeWindow.setHeight(550);
        typeWindow.setScrollbar(false);
			*/
      Image moreImage = iwrb_.getImage("/building/more.gif");
      Image backImage = iwrb_.getImage("/building/back.gif");
      Link typeLink = new Link(moreImage);
				typeLink.setWindowToOpen(apartmentTypeWindowClass);
        typeLink.addParameter(ApartmentTypeViewer.PARAMETER_STRING,types[a].getID());

      BackButton BB = new BackButton( backImage);
      typesTable.add(getAddressText(typeName),1,1);
      typesTable.add(getInfoText(typeText),1,2);
      typesTable.add(BB,1,3);
      typesTable.add("&nbsp;&nbsp;&nbsp;",1,3);
      typesTable.add(typeLink,1,3);
      typesTable.add(typeImage,2,1);

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
