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

public class ApartmentTypeViewer extends JModuleObject{

private int apartmenttypeid = 0;
private String style;
private String infoStyle;

public ApartmentTypeViewer(int apartmenttypeid){
    this.apartmenttypeid=apartmenttypeid;
}


    public void main(ModuleInfo modinfo) throws Exception {

       getApartmentType(modinfo);

    }

    public void setDefaultValues() {
      this.style = "font-family:verdana; font-size: 11pt; font-weight: bold; color: #FFFFFF;";
      this.infoStyle = "font-family:arial; font-size:8pt; color:#000000; line-height: 1.8; text-align: justify;";
    }

    private void getApartmentType(ModuleInfo modinfo) throws SQLException {

      ApartmentType room = new ApartmentType(apartmenttypeid);

      Table roomTable = new Table(1,5);
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
      topTable.add(new Image("/pics/room/topright.gif","",153,40),2,1);

      roomTable.add(topTable,1,1);
      roomTable.add(getApartmentTable(room),1,3);
      roomTable.add(getAllRoomTypes(room),1,4);
      roomTable.add(new Image("/pics/room/bottom.gif","",400,66),1,5);

      add(roomTable);

    }

    private Table getApartmentTable(ApartmentType room) throws SQLException {

       Table roomTable = new Table(3,3);
        roomTable.mergeCells(1,1,1,3);
        roomTable.mergeCells(2,1,2,3);
        roomTable.setWidth("100%");
        roomTable.setWidth(2,"20");
        roomTable.setAlignment(1,1,"center");

       Image floorPlan = new Image(room.getFloorPlanId());
       roomTable.add(floorPlan,1,1);

        String roomText = room.getInfo();
          if ( roomText != null ) {
            roomText = TextSoap.findAndReplace(roomText,"\n","<br>");
          }

       Text infoText = new Text(roomText);
        if ( infoStyle != null ) {
          infoText.setFontStyle(infoStyle);
        }
       roomTable.add(infoText,3,1);

       Text rent = new Text("Leiga: ");
        if ( infoStyle != null ) {
          rent.setFontStyle(infoStyle+"font-weight: bold;");
        }

       Text rentPrice = new Text(room.getRent()+"");
        if ( infoStyle != null ) {
          rentPrice.setFontStyle(infoStyle);
        }

       roomTable.add(rent,3,2);
       roomTable.add(rentPrice,3,2);

       Table linksTable = new Table(2,4);
        linksTable.setWidth("100%");
        linksTable.mergeCells(1,1,2,1);
        linksTable.mergeCells(1,4,2,4);
        linksTable.setCellpadding(0);
        linksTable.setCellspacing(0);
        linksTable.addText("",1,1);
        linksTable.addText("",1,4);
        linksTable.setHeight(1,"3");
        linksTable.setHeight(4,"3");
        linksTable.setBackgroundImage(1,1,new Image("/pics/room/line.gif"));
        linksTable.setBackgroundImage(1,4,new Image("/pics/room/line.gif"));

        Link applyLink = new Link(new Image("/pics/room/saekjaum.gif"),"");
        PrintButton print = new PrintButton(new Image("/pics/room/prentaut.gif"));
        CloseButton close = new CloseButton(new Image("/pics/room/loka.gif"));

      linksTable.add(applyLink,1,2);
      linksTable.add(print,2,2);
      linksTable.add(close,1,3);

      roomTable.add(linksTable,3,3);

      return roomTable;
    }

    private Form getAllRoomTypes(ApartmentType room) throws SQLException {

      int building_id = 1;

      Room[] appartment = (Room[]) (new Room()).findAllByColumn("sub_type_id",room.getID());
        if ( appartment.length > 0 ) {
          Floor floor = new Floor(appartment[0].getFloorId());
          building_id = floor.getBuildingId();
          Building building = new Building(building_id);
        }

      ApartmentType[] rooms = BuildingFinder.findApartmentTypesInBuilding(building_id);

      Form roomForm = new Form();

      Table formTable = new Table(1,1);
        formTable.setWidth("90%");
        formTable.setAlignment("center");
        formTable.setAlignment(1,1,"right");

      DropdownMenu roomTypes = new DropdownMenu("room_sub_type_id");
        roomTypes.setToSubmit();
        roomTypes.keepStatusOnAction();
        for ( int a = 0; a < rooms.length; a++ ) {
         roomTypes.addMenuElement(rooms[a].getID()+"",rooms[a].getName());
        }
        roomTypes.setSelectedElement(room.getID()+"");

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

}