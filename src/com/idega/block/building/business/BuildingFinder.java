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

  public static RoomSubType[] findRoomSubTypesInBuilding(int iBuildingId){
    RoomSubType[] rt = new RoomSubType[0];
    StringBuffer sql = new StringBuffer("select distinct room_sub_type_id,room_type_id,name,info,image_id");
    sql.append("room_count,area,kitchen,bathroom,storage,balcony,study,loft,rent ");
    sql.append("from room_sub_type,room,floor,building");
    sql.append("where room_sub_type_id = room.sub_type_id");
    sql.append("and room.floor_id = floor.floor_id");
    sql.append("and building.building_id = ");
    sql.append(iBuildingId);
    try{
    rt= (RoomSubType[])(new RoomSubType()).findAll(sql.toString());
    }
    catch(SQLException ex){}
    return rt;
  }

}// class end