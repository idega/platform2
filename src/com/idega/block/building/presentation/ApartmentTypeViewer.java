package com.idega.block.building.presentation;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.text.*;
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
import com.idega.idegaweb.IWBundle;

/**
 * Title: BuildingViewer
 * Description: Views buildings in a campus
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi
 * @version 1.0
 */

public class ApartmentTypeViewer extends Block{

private static final String IW_RESOURCE_BUNDLE = "com.idega.block.building";
public static final String PARAMETER_STRING = "type_id";
private int apartmenttypeid = -1;
private String style = "font-family:verdana,arial,sans-serif; font-size: 11pt; font-weight: bold; color: #FFFFFF;";
private String infoStyle =  "font-family:verdana,arial,sans-serif; font-size:10px; color:#000000; text-align: justify;";
protected IWResourceBundle iwrb_;
protected IWBundle iwb_;
private String specialAttributesName = null;
private Map specialAttributes = null;

public ApartmentTypeViewer(){
}

public ApartmentTypeViewer(int apartmenttypeid){
    this.apartmenttypeid=apartmenttypeid;
}

public void setSpecialAttributes(String name,Map attributes){
  specialAttributesName  = name;
  specialAttributes = attributes;
}


    public void main(IWContext iwc) throws Exception {
      if ( iwrb_ == null ) {
        iwrb_ = getResourceBundle(iwc);
      }

      if ( iwb_ == null ) {
        iwb_ = getBundle(iwc);
      }

      if ( iwc.getParameter(PARAMETER_STRING) != null ) {
        try {
          apartmenttypeid = Integer.parseInt(iwc.getParameter(PARAMETER_STRING));
        }
        catch (NumberFormatException e) {
          apartmenttypeid = 0;
        }
      }
      this.getParentPage().setTitle("Appartment Viewer");
      this.getParentPage().setAllMargins(0);

      if ( iwrb_ != null && iwb_ != null ) {
        getApartmentType(iwc);
      }
      else {
        add(getBoldText("No bundle available"));
      }
    }

    public void setDefaultValues() {
      this.style = "font-family:verdana; font-size: 11pt; font-weight: bold; color: #FFFFFF;";
      this.infoStyle = "font-family:arial; font-size:8pt; color:#000000; line-height: 1.8; text-align: justify;";
    }

    private void getApartmentType(IWContext iwc) throws SQLException {

      ApartmentType room = ((com.idega.block.building.data.ApartmentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentType.class)).findByPrimaryKeyLegacy(apartmenttypeid);

      Table roomTable = new Table(1,6);
        roomTable.setWidth("400");
        roomTable.setHeight("100%");
        roomTable.setBorder(0);
        roomTable.setCellpadding(0);
        roomTable.setCellspacing(0);
        roomTable.setHeight(3,"100%");
        roomTable.setVerticalAlignment(1,3,"top");

      Table topTable = new Table(2,2);
        topTable.mergeCells(2,1,2,2);
        topTable.setColor(1,1,"#27324B");
        topTable.setWidth(1,"100%");
        topTable.addText("",1,2);
        topTable.setHeight(1,1,"33");
        topTable.setHeight(1,2,"7");
        topTable.setCellpadding(0);
        topTable.setCellspacing(0);

      Text roomName = new Text("&nbsp;:: "+room.getName());
        if ( style != null ) {
          roomName.setFontStyle(style);
        }
        else {
          roomName.setBold();
          roomName.setFontSize(3);
        }

      topTable.add(roomName,1,1);
      topTable.add(iwrb_.getImage("/room/topright.gif","",153,40),2,1);

      roomTable.add(topTable,1,1);
      roomTable.add(getApartmentTable(room, iwc),1,3);
      roomTable.add(getBuildingApartmentTypes(room),1,4);
      roomTable.add(getCategoryApartmentTypes(room),1,4);
      roomTable.add(iwrb_.getImage("/room/bottom.gif","",400,66),1,5);

      add(roomTable);

    }

    private Table getApartmentTable(ApartmentType room, IWContext iwc) throws SQLException {

       Table roomTable = new Table(2,2);
        roomTable.mergeCells(1,1,1,2);
        roomTable.setWidth("100%");
        roomTable.setHeight("100%");
        roomTable.setHeight(2,2,"100%");
        roomTable.setWidth(1,"212");
        roomTable.setAlignment(1,1,"center");
        roomTable.setColumnVerticalAlignment(1,"top");
        roomTable.setColumnVerticalAlignment(2,"top");
        roomTable.setBorder(0);

      try{
      int pid = room.getFloorPlanId();
      Image floorPlan = iwb_.getImage("/shared/room/nopic.gif");
        if(pid > 0)
         floorPlan = new Image(pid);
       floorPlan.setMaxImageWidth(200);
       floorPlan.setHorizontalSpacing(6);
       roomTable.add(floorPlan,1,1);

      String roomText = room.getInfo();
        if ( roomText != null ) {
          roomText = TextSoap.findAndReplace(roomText,"\n","<br>");
        }
        if ( roomText.length() == 0 ) {
          roomText = iwrb_.getLocalizedString("no_information","No information available");
        }
        else {
          roomTable.add(getBoldText(iwrb_.getLocalizedString("information","Information")),2,1);
          roomTable.add(Text.getBreak(),2,1);
        }
      roomTable.add(getInfoText(roomText),2,1);
      roomTable.add(Text.getBreak(),2,1);
      roomTable.add(Text.getBreak(),2,1);

      roomTable.add(getBoldText(iwrb_.getLocalizedString("size","Size (sqm)")+": "),2,1);
      String areaString = TextSoap.singleDecimalFormat((double)room.getArea());
        if ( areaString.length() > 0 ) {
          areaString += " m<sup>2</sup>";
        }
        else {
          areaString = "?? m<sup>2</sup>";
        }
      roomTable.add(getInfoText(areaString),2,1);
      roomTable.add(Text.getBreak(),2,1);

      roomTable.add(getBoldText(iwrb_.getLocalizedString("number_of_rooms","Number of rooms")+": "),2,1);
      roomTable.add(getInfoText(""+room.getRoomCount()),2,1);
      roomTable.add(Text.getBreak(),2,1);

      Table Inventory = new Table();
        Inventory.setCellpadding(0);
        Inventory.setCellspacing(3);
        Inventory.setWidth("100%");
        Inventory.setWidth(1,"50%");
        Inventory.setWidth(2,"50%");

      Image included = iwb_.getImage("/shared/room/x.gif",iwrb_.getLocalizedString("in_apartment","Available in apartment"),9,9);
        included.setAlignment("middle");
        included.setHorizontalSpacing(4);
      Image notIncluded = iwb_.getImage("/shared/room/x1.gif",iwrb_.getLocalizedString("not_in_apartment","Not available in apartment"),9,9);
        notIncluded.setAlignment("middle");
        notIncluded.setHorizontalSpacing(4);

      int a = 1;

      Inventory.add(room.getKitchen()?included:notIncluded,1,a);
      Inventory.add(getBoldText(iwrb_.getLocalizedString("kitchen","Kitchen")),1,a);

      Inventory.add(room.getStorage()?included:notIncluded,2,a);
      Inventory.add(getBoldText(iwrb_.getLocalizedString("storage","Storage")),2,a++);

      Inventory.add(room.getBathRoom()?included:notIncluded,1,a);
      Inventory.add(getBoldText(iwrb_.getLocalizedString("bathroom","Bathroom")),1,a);

      Inventory.add(room.getBalcony()?included:notIncluded,2,a);
      Inventory.add(getBoldText(iwrb_.getLocalizedString("balcony","Balcony")),2,a++);

      Inventory.add(room.getStudy()?included:notIncluded,1,a);
      Inventory.add(getBoldText(iwrb_.getLocalizedString("study","Study")),1,a);

      Inventory.add(room.getLoft()?included:notIncluded,2,a);
      Inventory.add(getBoldText(iwrb_.getLocalizedString("loft","Loft")),2,a++);

      Inventory.add(room.getFurniture()?included:notIncluded,1,a);
      Inventory.add(getBoldText(iwrb_.getLocalizedString("furniture","Furniture")),1,a++);

      roomTable.add(Inventory,2,1);
      roomTable.add(Text.getBreak(),2,1);

      /** @todo  get rid of*/
      /*
      if(room.getRent() > 0){
        roomTable.add(getBoldText(iwrb_.getLocalizedString("rent","Rent")+": "),2,1);
        NumberFormat format = DecimalFormat.getCurrencyInstance(iwc.getApplication().getSettings().getDefaultLocale());
        String rentString = format.format((long)room.getRent());
        roomTable.add(getInfoText(rentString),2,1);
      }
      */
      if(specialAttributes!=null){
        Table T = new Table();
        T.setCellpadding(0);
        T.setCellspacing(0);
        int row = 1;
        if(specialAttributesName!=null){
          T.add(getBoldText(specialAttributesName),1,row++);
        }
        Iterator iter = specialAttributes.entrySet().iterator();
        while(iter.hasNext()){
          Map.Entry me = (Map.Entry) iter.next();
          T.add(getBoldText((String)me.getKey()),1,row);
          T.add(getInfoText((String)me.getValue()),3,row);
          row++;
        }
        T.mergeCells(1,1,3,1);
        T.setColumnAlignment(3,"right");
        T.setWidth(2,"5");
         roomTable.add(T,2,1);
      }

       Table linksTable = new Table(2,3);
        linksTable.setWidth("100%");
        linksTable.mergeCells(1,1,2,1);
        linksTable.mergeCells(1,3,2,3);
        linksTable.setCellpadding(0);
        linksTable.setCellspacing(0);
        linksTable.addText("",1,1);
        linksTable.addText("",1,3);
        linksTable.setHeight(1,"3");
        linksTable.setHeight(3,"3");
        linksTable.setBackgroundImage(1,1,iwb_.getImage("/shared/room/line.gif"));
        linksTable.setBackgroundImage(1,3,iwb_.getImage("/shared/room/line.gif"));

        //Link applyLink = new Link(iwrb_.getImage("/room/apply.gif"));
        PrintButton print = new PrintButton(iwrb_.getImage("/room/print.gif"));
        CloseButton close = new CloseButton(iwrb_.getImage("/room/close.gif"));

      //linksTable.add(applyLink,1,2);
      linksTable.add(print,2,2);
      linksTable.add(close,1,2);

      roomTable.add(linksTable,2,2);
      }
      catch (Exception e){
        e.printStackTrace(System.err);
      }
      return roomTable;
    }

    private Text getInfoText(String text){
      Text T = new Text(text);
      T.setFontStyle(infoStyle);
      return T;
    }

    private Text getBoldText(String text){
      Text T = new Text(text);
      T.setFontStyle(infoStyle+"font-weight: bold;");
      return T;
    }

    private Form getBuildingApartmentTypes(ApartmentType type) throws SQLException {

      int id = BuildingFinder.getComplexIdFromTypeId(type.getID());
      ApartmentType[] types = BuildingFinder.findApartmentTypesInComplex(id);

      Form roomForm = new Form();

      Text appartmentText = getBoldText(iwrb_.getLocalizedString("other_apartments","Other apartments in building")+": ");

      Table formTable = new Table(1,1);
        formTable.setCellpadding(0);
        formTable.setCellspacing(0);
        formTable.setWidth("90%");
        formTable.setAlignment("center");
        formTable.setAlignment(1,1,"right");

      DropdownMenu roomTypes = new DropdownMenu("type_id");
        roomTypes.setToSubmit();
        roomTypes.keepStatusOnAction();
        for ( int a = 0; a < types.length; a++ ) {
         roomTypes.addMenuElement(types[a].getID(),types[a].getName());
        }
        roomTypes.setSelectedElement(String.valueOf(type.getID()));
        roomTypes.setAttribute("style","font-family: Verdana; font-size: 8pt; border: 1 solid #000000");

      formTable.add(appartmentText,1,1);
      formTable.add(roomTypes,1,1);
      roomForm.add(formTable);

      return roomForm;

    }

     private Form getCategoryApartmentTypes(ApartmentType type) throws SQLException {

      ApartmentType[] types = BuildingFinder.findApartmentTypesForCategory(type.getApartmentCategoryId());

      Form roomForm = new Form();

      Text appartmentText = getBoldText(iwrb_.getLocalizedString("category_apartments","Other apartments in category")+": ");

      Table formTable = new Table(1,1);
        formTable.setCellpadding(0);
        formTable.setCellspacing(0);
        formTable.setWidth("90%");
        formTable.setAlignment("center");
        formTable.setAlignment(1,1,"right");

      DropdownMenu roomTypes = new DropdownMenu("type_id");
        roomTypes.setToSubmit();
        roomTypes.keepStatusOnAction();
        for ( int a = 0; a < types.length; a++ ) {
         roomTypes.addMenuElement(types[a].getID(),types[a].getName());
        }
        roomTypes.setSelectedElement(String.valueOf(type.getID()));
        roomTypes.setAttribute("style","font-family: Verdana; font-size: 8pt; border: 1 solid #000000");

      formTable.add(appartmentText,1,1);
      formTable.add(roomTypes,1,1);
      roomForm.add(formTable);

      return roomForm;

    }

    public void setStyle(String style) {
      this.style=style;
    }

    public void setInfoStyle(String infoStyle) {
      this.infoStyle=infoStyle;
    }

    public String getBundleIdentifier() {
      return(IW_RESOURCE_BUNDLE);
    }

}
