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

/**
 * Title: BuildingViewer
 * Description: Views buildings in a campus
 * Copyright:    Copyright (c) 2001
 * Company: idega
 * @author Laddi
 * @version 1.0
 */

public class BuildingViewer extends JModuleObject{

private int building_id = 0;
private String nameStyle;
private String addressStyle;
private String infoStyle;

public BuildingViewer(){
}

public BuildingViewer(int building_id){
    this.building_id=building_id;
}


  public void main(ModuleInfo modinfo) throws Exception {

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

    Building[] building = (Building[]) (new Building()).findAllOrdered("name");

    Table campusTable = new Table(1,building.length);

    for ( int a = 0; a < building.length; a++ ) {

      //Address address = new Address(building[a].getAddressId());
      String address = building[a].getStreet();

      Table buildingTable = new Table(3,4);
        buildingTable.mergeCells(2,1,2,4);
        buildingTable.mergeCells(3,2,3,3);
        buildingTable.setVerticalAlignment(3,2,"top");
        buildingTable.setWidth("100%");
        buildingTable.setWidth(2,1,"20");

      Text buildingName = new Text(building[a].getName());
        if ( nameStyle != null ) {
          buildingName.setFontStyle(nameStyle);
        }
        else {
          buildingName.setBold();
          buildingName.setFontSize(3);
        }

        Text buildingAddress = new Text(address);

        if ( addressStyle != null ) {
          buildingAddress.setFontStyle(addressStyle);
        }
        else {
          buildingAddress.setBold();
          buildingAddress.setFontSize(3);
        }

      String infoText = building[a].getInfo();
        infoText = TextSoap.findAndReplace(infoText,"\n","<br>");

      Text buildingText = new Text(infoText);
        if ( infoStyle != null ) {
          buildingText.setFontStyle(infoStyle);
        }

      Image buildingImage = new Image(building[a].getImageId());

      Image moreImage = new Image("/pics/meira.gif");
      Link buildingLink = new Link(moreImage,modinfo.getRequestURI());
        buildingLink.addParameter("building_id",building[a].getID());

      buildingTable.add(buildingName,1,1);
      buildingTable.add(buildingAddress,1,2);
      buildingTable.add(buildingText,1,3);
      buildingTable.add(buildingLink,1,4);
      buildingTable.add(buildingImage,3,2);

      Text divideText = new Text("<br>.........<br><br>");
        if ( infoStyle != null ) {
          divideText.setFontStyle(infoStyle);
        }

      campusTable.add(buildingTable,1,a+1);
      if ( a+1 < building.length ) {
        campusTable.add(divideText,1,a+1);
      }
    }

    if ( building.length == 0 ) {
      add("Engar byggingar í grunni...");
    }

    else {
      add(campusTable);
    }

  }

  private void getSingleBuilding(ModuleInfo modinfo) throws SQLException {

    Building building = new Building(building_id);
    ApartmentType[] rooms = BuildingFinder.findApartmentTypesInBuilding(building_id);

    Table buildingTable = new Table(1,rooms.length+1);
      buildingTable.setWidth("100%");

    Text buildingName = new Text(building.getName());
      if ( nameStyle != null ) {
        buildingName.setFontStyle(nameStyle);
      }
      else {
        buildingName.setBold();
        buildingName.setFontSize(3);
      }

    buildingTable.add(buildingName,1,1);

    for ( int a = 0; a < rooms.length; a++ ) {

      Table roomsTable = new Table(3,3);
        buildingTable.mergeCells(2,1,2,3);
        roomsTable.setVerticalAlignment(3,2,"top");
        roomsTable.setWidth("100%");
        roomsTable.setWidth(2,1,"20");

      Text roomName = new Text(rooms[a].getName()+" "+rooms[a].getArea()+"m<sup>2</sup>");
        if ( addressStyle != null ) {
          roomName.setFontStyle(addressStyle);
        }

      String roomText = rooms[a].getInfo();
        roomText = TextSoap.findAndReplace(roomText,"\n","<br>");

      Text roomInfo = new Text(roomText);
        if ( infoStyle != null ) {
          roomInfo.setFontStyle(infoStyle);
        }

      Text divideText = new Text("<br>.........<br><br>");
        if ( infoStyle != null ) {
          divideText.setFontStyle(infoStyle);
        }

      Image roomImage = new Image(rooms[a].getImageId());

      Window roomWindow = new Window("Herbergi",400,550,"/room.jsp");
        roomWindow.setScrollbar(false);
      Image moreImage = new Image("/pics/meira.gif");
      Link roomLink = new Link(moreImage,roomWindow);
        roomLink.addParameter("room_sub_type_id",rooms[a].getID());

      roomsTable.add(roomName,1,1);
      roomsTable.add(roomInfo,1,2);
      roomsTable.add(roomLink,1,3);
      roomsTable.add(roomImage,3,2);

      buildingTable.add(roomsTable,1,a+2);
      if ( a+1 < rooms.length ) {
        buildingTable.add(divideText,1,a+2);
      }

    }

    add(buildingTable);

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

}