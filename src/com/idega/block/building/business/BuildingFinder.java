package com.idega.block.building.business;

import com.idega.block.building.data.*;
import java.sql.SQLException;
import com.idega.util.idegaCalendar;
import com.idega.util.idegaTimestamp;
import java.util.List;
import com.idega.data.EntityFinder;
import java.lang.StringBuffer;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class BuildingFinder {

  public static List BuildingsList(){
    try{
      return EntityFinder.findAll(new Building());
    }
    catch(SQLException e){return null;}
  }

  public static List FloorsList(){
    try{
      return EntityFinder.findAll(new Floor());
    }
    catch(SQLException e){return null;}
  }

  public static List RoomTypesList(){
    try{
     return EntityFinder.findAll(new RoomType());
    }
    catch(SQLException e){return null;}
  }
 public static Building[] findBuildings(){
   Building[] buildings = new Building[0];
    try{
      buildings = (Building[]) (new Building().findAll());
    }
    catch(SQLException e){}
    return buildings;
  }

  public static Floor[] findFloors(){
    Floor[] floors = new Floor[0];
    try{
    floors = (Floor[]) (new Floor()).findAll();
    }
    catch(SQLException e){}
    return floors;
  }

  public static RoomType[] findRoomTypes(){
    RoomType[] types = new RoomType[0];
    try{
    types = (RoomType[]) (new RoomType()).findAll();
    }
    catch(SQLException e){}
    return types;
  }

  public static ApartmentType[] findApartmentTypesInBuilding(int iBuildingId){
    ApartmentType[] rt = new ApartmentType[0];
    StringBuffer sql = new StringBuffer("select distinct apartment_type.* ");
    sql.append("from apartment_type at,apartment a,floor f ");
    sql.append("where at.apartment_type_id = a.apartment_type_id ");
    sql.append("and a.floor_id = f.floor_id ");
    sql.append("and f.building_id = ");
    sql.append(iBuildingId);
    try{
    rt= (ApartmentType[])(new ApartmentType()).findAll(sql.toString());
    }
    catch(SQLException ex){}
    return rt;
  }

}// class end