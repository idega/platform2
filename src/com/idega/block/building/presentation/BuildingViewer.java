package com.idega.block.building.presentation;


import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.idega.block.building.business.BuildingService;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Complex;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.ImageSlideShow;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.util.text.TextSoap;

/**
 * Title: BuildingViewer
 * Description: Views buildings in a campus
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi?
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
private BuildingService buildingService = null;

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
    
    buildingService = (BuildingService)IBOLookup.getServiceInstance(iwc,BuildingService.class);

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


  private void getAllBuildings(IWContext iwc) throws Exception {
  	int row = 1;
    Collection complexes = buildingService.getComplexHome().findAll();
    Table campusTable = new Table(1,complexes.size());
    for (Iterator iter = complexes.iterator(); iter.hasNext();) {
		Complex complex = (Complex) iter.next();
		Integer iComplexId =(Integer)complex.getPrimaryKey();
      Collection types = buildingService.getApartmentTypeHome().findByComplex(iComplexId);
      //String[] types = BuildingFinder.findDistinctApartmentTypesInComplex(iComplexId);


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
	  

	    //BuildingBusiness.getStaticInstance().changeNameAndInfo(complex[a],iwc.getCurrentLocale());
	   String infoText = complex.getInfo();
	  	String nameText=complex.getName();

	 
        infoText = TextSoap.findAndReplace(infoText,"\n","<br>");

      //List L = BuildingFinder.listOfBuildingsInComplex(iComplexId);
      //List L = BuildingFinder.listOfBuildingImageFiles(iComplexId);
        Collection images = buildingService.getBuildingHome().getImageFilesByComplex(iComplexId);
      if(images!=null){
       //ICFile file = ((com.idega.core.data.ICFileHome)com.idega.data.IDOLookup.getHomeLegacy(ICFile.class)).findByPrimaryKeyLegacy(((Building)L.get(0)).getImageId());
        ImageSlideShow slide = new ImageSlideShow();
        //slide.setFileFolder(file);
        slide.setWidth(imageMaxSize);

        slide.setAlt(complex.getName());
        slide.setFiles(new java.util.Vector(images));

        complexTable.add(slide,3,2);
        /*
       Image buildingImage = new Image(((Building)L.get(0)).getImageId());
       buildingImage.setMaxImageWidth(imageMaxSize);
       complexTable.add(buildingImage,3,2);
       */
      }

      
      // Find out and present apartment category icons
      Collection categories = buildingService.getApartmentCategoryHome().findByComplex(iComplexId);
      if ( categories != null ) {

      	for (Iterator iterator = categories.iterator(); iterator.hasNext();) {
      		ApartmentCategory cat = (ApartmentCategory) iterator.next();
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


      // What genius made this ??
      // Link complexLink = new Link(moreImage,iwc.getRequestURI());
      Link complexLink = new Link(moreImage);
        complexLink.addParameter(PARAMETER_STRING,iComplexId.toString());

      Link locationLink = new Link(mapImage);
        locationLink.setWindowToOpen(BuildingLocation.class);
        locationLink.addParameter(PARAMETER_STRING,iComplexId.toString());

      complexTable.add(getNameText(nameText),1,1);
      complexTable.add(getInfoText(infoText),1,2);
      complexTable.add(complexLink,1,4);
      complexTable.add("&nbsp;&nbsp;&nbsp;",1,4);
      complexTable.add(locationLink,1,4);

      String divideText = "<br>.........<br><br>";


      campusTable.add(complexTable,1,row);
      if (row < complexes.size() ) {
        campusTable.add(getInfoText(divideText),1,row++);

      }
    }

    if ( complexes.size() == 0 ) {
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

  private void getSingleBuilding(IWContext iwc) throws Exception {


    //Complex complex = ((com.idega.block.building.data.ComplexHome)com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).findByPrimaryKeyLegacy(building_id);
    //ApartmentType[] types = BuildingFinder.findApartmentTypesInComplex(building_id);
  	Complex complex = buildingService.getComplexHome().findByPrimaryKey(String.valueOf(building_id));
  	Collection types = buildingService.getApartmentTypeHome().findByComplex(new Integer(building_id));
    Table complexTable = new Table(1,types.size()+1);

      complexTable.setWidth("100%");
	//BuildingBusiness.getStaticInstance().changeNameAndInfo(complex,iwc.getCurrentLocale());
    complexTable.add(getNameText(complex.getName()),1,1);
    int row = 2;
    for (Iterator iter = types.iterator(); iter.hasNext();) {
		ApartmentType type = (ApartmentType) iter.next();
		//BuildingBusiness.getStaticInstance().changeNameAndInfo(types[a],iwc.getCurrentLocale());
      Table typesTable = new Table(2,3);
        typesTable.setVerticalAlignment(2,2,"top");
        typesTable.setVerticalAlignment(1,2,"top");
        typesTable.setVerticalAlignment(1,1,"top");
        typesTable.mergeCells(2,1,2,2);
        typesTable.setAlignment(2,2,"right");
        typesTable.setWidth("100%");
        typesTable.setHeight(2,"100%");
        typesTable.setWidth(1,"100%");

      String typeName = formatText(type.getName()+" "+type.getArea()+"m2");
      String typeText = type.getInfo();
      //String typeText = types[a].getExtraInfo();
        typeText = TextSoap.findAndReplace(typeText,"\n","<br>");

      String divideText = ("<br>.........<br><br>");


      PresentationObject typeImage;
      if ( type.getImageId() == -1 )
        typeImage = iwrb_.getImage("/building/default.jpg");
      else{
        ImageSlideShow slide =  new ImageSlideShow();
        slide.setDelay(1);
        slide.setShowButtons(false);
        slide.setWidth(imageMaxSize);
        slide.setAlt(type.getName());
        slide.setFileId( type.getImageId());
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
        typeLink.addParameter(ApartmentTypeViewer.PARAMETER_STRING,type.getPrimaryKey().toString());

      BackButton BB = new BackButton( backImage);
      typesTable.add(getAddressText(typeName),1,1);
      typesTable.add(getInfoText(typeText),1,2);
      typesTable.add(BB,1,3);
      typesTable.add("&nbsp;&nbsp;&nbsp;",1,3);
      typesTable.add(typeLink,1,3);
      typesTable.add(typeImage,2,1);

      complexTable.add(typesTable,1,row);
      if ( row < types.size()+1 ) {
        complexTable.add(getInfoText(divideText),1,row);
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
