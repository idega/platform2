package com.idega.block.building.business;

import com.idega.block.building.data.*;
import java.sql.SQLException;
import com.idega.util.idegaCalendar;
import com.idega.util.idegaTimestamp;
import java.util.List;
import java.sql.*;
import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.util.database.ConnectionBroker;
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
    /*
    select distinct bu_aprt_type.*
    from bu_aprt_type p,bu_apartment a,bu_floor f
    where p.bu_aprt_type_id = a.bu_aprt_type_id
    and a.bu_floor_id = f.bu_floor_id
    and f.bu_building_id = 2

    StringBuffer sql = new StringBuffer(" select distinct bu_aprt_type.* ");
    sql.append(" from bu_aprt_type p,bu_apartment a,bu_floor f ");
    sql.append(" where p.bu_aprt_type_id = a.bu_aprt_type_id");
    sql.append(" and a.bu_floor_id = f.bu_floor_id");
    sql.append(" and f.bu_building_id = ");
    sql.append(iBuildingId);
    */
    StringBuffer sql = new StringBuffer("select distinct ");
     sql.append(ApartmentType.getNameTableName());
    sql.append(".* from ");
    sql.append(ApartmentType.getNameTableName());
    sql.append(" p ,");
    sql.append(Apartment.getNameTableName());
    sql.append(" a ,");
    sql.append(Floor.getNameTableName());
    sql.append(" f where p.");
    sql.append(ApartmentType.getNameTableName());
    sql.append("_id = a.");
    sql.append(ApartmentType.getNameTableName());
    sql.append("_id and a.");
    sql.append(Apartment.getFloorIdColumnName());
    sql.append(" = f.");
    sql.append(Floor.getNameTableName());
    sql.append("_id and f.");
    sql.append(Floor.getBuildingIdColumnName());
    sql.append(" = ");
    sql.append(iBuildingId);

    try{
      rt= (ApartmentType[])(new ApartmentType()).findAll(sql.toString());
    }
    catch(SQLException ex){}
    return rt;
  }

  public static ApartmentType[] findApartmentTypesInComplex(int iComplexId){
    ApartmentType[] rt = new ApartmentType[0];
    /*
    select distinct bu_aprt_type.*
    from bu_aprt_type p,bu_apartment a,bu_floor f, bu_building b
    where p.bu_aprt_type_id = a.bu_aprt_type_id
    and a.bu_floor_id = f.bu_floor_id
    and f.bu_building_id = b.building_id
    and b.bu_complex_id = 2
    */
    StringBuffer sql = new StringBuffer("select distinct ");
     sql.append(ApartmentType.getNameTableName());
    sql.append(".* from ");
    sql.append(ApartmentType.getNameTableName());
    sql.append(" p ,");
    sql.append(Apartment.getNameTableName());
    sql.append(" a ,");
    sql.append(Floor.getNameTableName());
    sql.append(" f ,");
    sql.append(Building.getNameTableName());
    sql.append(" b where p.");
    sql.append(ApartmentType.getNameTableName());
    sql.append("_id = a.");
    sql.append(ApartmentType.getNameTableName());
    sql.append("_id and a.");
    sql.append(Apartment.getFloorIdColumnName());
    sql.append(" = f.");
    sql.append(Floor.getNameTableName());
    sql.append("_id and f.");
    sql.append(Floor.getBuildingIdColumnName());
    sql.append(" = b.");
    sql.append(Building.getNameTableName());
    sql.append("_id and b.");
    sql.append(Complex.getNameTableName());
    sql.append("_id = ");
    sql.append(iComplexId);

    try{
      rt= (ApartmentType[])(new ApartmentType()).findAll(sql.toString());
    }
    catch(SQLException ex){}
    return rt;
  }

  public static ApartmentType[] findApartmentTypesForCategory(int categoryId) {
    ApartmentType aprtType[] = null;
    try {
      aprtType = (ApartmentType[])new ApartmentType().findAllByColumn(ApartmentType.getApartmentCategoryIdColumnName(),categoryId);
    }
    catch(SQLException e) {
    }

    return(aprtType);
  }

  public static int getComplexIdFromTypeId(int id) {
    StringBuffer sql = new StringBuffer("select distinct ");
    sql.append(Complex.getNameTableName());
    sql.append(".* from ");
    sql.append(ApartmentType.getNameTableName());
    sql.append(" p ,");
    sql.append(Apartment.getNameTableName());
    sql.append(" a ,");
    sql.append(Floor.getNameTableName());
    sql.append(" f ,");
    sql.append(Complex.getNameTableName());
    sql.append(" c ,");
    sql.append(Building.getNameTableName());
    sql.append(" b where p.");
    sql.append(ApartmentType.getNameTableName());
    sql.append("_id = a.");
    sql.append(ApartmentType.getNameTableName());
    sql.append("_id and a.");
    sql.append(Apartment.getFloorIdColumnName());
    sql.append(" = f.");
    sql.append(Floor.getNameTableName());
    sql.append("_id and f.");
    sql.append(Floor.getBuildingIdColumnName());
    sql.append(" = b.");
    sql.append(Building.getNameTableName());
    sql.append("_id and b.");
    sql.append(Complex.getNameTableName());
    sql.append("_id = c.");
    sql.append(Complex.getNameTableName());
    sql.append("_id and a.");
    sql.append(ApartmentType.getNameTableName());
    sql.append("_id = ");
    sql.append(id);
    int r = -1;
    Complex[] c = new Complex[0];
    try{
      c= (Complex[])(new Complex()).findAll(sql.toString());
      if(c.length > 0)
        r = c[0].getID();
    }
    catch(SQLException ex){}
    return r;

  }

}// class end