package com.idega.block.building.business;

import com.idega.presentation.Block;

import com.idega.block.building.data.*;
import java.sql.SQLException;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class BuildingBusiness {

  public static boolean saveBuilding(int id,String sName,String sAddress,
    String sInfo,int imageid,int complexid,String sSerie){
   Building ebuilding = new Building();
    boolean update = false;
    try{
      if(complexid > 0){
        if(id > 0 ){
          ebuilding = new Building(id);
          update = true;
        }
        ebuilding.setName(sName);
        ebuilding.setStreet(sAddress);
        ebuilding.setInfo(sInfo);
        ebuilding.setImageId(imageid);
        ebuilding.setComplexId(complexid);
        ebuilding.setSerie(sSerie);
        if(update)
          ebuilding.update();
        else
          ebuilding.insert();
        BuildingCacher.reload();
        return true;
      }
    }
    catch(SQLException e){}
    return false;
  }
  public static boolean saveFloor(int id,String sName,int buildingid,String sInfo,int imageid){
      Floor efloor = new Floor();
      boolean update = false;
      try{
      if(id > 0){
        efloor = new Floor(id);
        update = true;
      }
      efloor.setName(sName);
      efloor.setBuildingId(buildingid);
      efloor.setInfo(sInfo);
      efloor.setImageId(imageid);
      if(update)
        efloor.update();
      else
        efloor.insert();
       BuildingCacher.reload();
      return true;
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    return false;
  }

  public static boolean saveApartmentCategory(int id,String sName,String sInfo,int imageid){
    try{
      boolean update = false;
      ApartmentCategory eACategory = new ApartmentCategory();
      if(id >0){
        eACategory = new ApartmentCategory(id);
        update = true;
      }
      eACategory.setName(sName);
      eACategory.setInfo(sInfo);
      eACategory.setImageId(imageid);
      if(update)
        eACategory.update();
      else
        eACategory.insert();
      BuildingCacher.reload();
      return true;
    }
    catch(SQLException e){}
    return false;
  }

  public static boolean saveApartmentType(int id,String sName,String sInfo,
    String sExtraInfo,int planid,int imageid,int categoryid,float area,
    int roomcount,int rent,boolean balcony,boolean bath,boolean kitchen,
    boolean storage,boolean study,boolean furniture){
     try{
        if(categoryid > 0){
          ApartmentType etype = new ApartmentType();
          boolean update = false;
          if(id > 0){
            etype = new ApartmentType(id);
            update = true;
          }
          etype.setName(sName);
          etype.setInfo(sInfo);
          etype.setExtraInfo(sExtraInfo);
          etype.setFloorPlanId(planid);
          etype.setImageId(imageid);
          etype.setApartmentCategoryId(categoryid);
          etype.setArea(area);
          etype.setRoomCount(roomcount);
          etype.setRent(rent);

          etype.setBalcony(balcony);
          etype.setBathRoom(bath);
          etype.setKitchen(kitchen);
          etype.setLoft(storage);
          etype.setStorage(storage);
          etype.setStudy(study);
          etype.setFurniture(furniture);

          if(update){
            etype.update();
          }
          else{
            etype.insert();
          }
          BuildingCacher.reload();
          return true;
        }
      }
      catch(SQLException e){
        e.printStackTrace();
      }

      catch(Exception e){
        e.printStackTrace();
      }
      return false;
  }
  public static boolean saveApartment(int id,String sName,String sInfo,
    int floorid,int typeid,boolean rentable,int imageid,String sSerie){
    try{
      boolean update = false;
      Apartment apartment = new Apartment();
      if(id >0){
        apartment = new Apartment(id);
        update = true;
      }
      apartment.setName(sName);
      apartment.setFloorId( floorid);
      apartment.setApartmentTypeId(typeid);
      apartment.setInfo(sInfo);
      apartment.setRentable(rentable);
      apartment.setImageId(imageid);
      apartment.setSerie(sSerie);
      if(update)
        apartment.update();
      else
        apartment.insert();
      BuildingCacher.reload();
      return true;
    }
    catch(SQLException e){}
    return false;
  }

  public static  void deleteComplex(int id){
    try{
      new Complex(id).delete();
      BuildingCacher.reload();
    }
    catch(SQLException sql){}
  }
  public static void deleteBuilding(int id){
    try {
      new Building(id).delete();
      BuildingCacher.reload();
    }
    catch (SQLException ex) {
    }
  }
  public static void deleteFloor(int id){
    try {
      new Floor(id).delete();
      BuildingCacher.reload();
    }
    catch (SQLException ex) {

    }
  }
  public static void deleteApartment(int id){
    try {
      new Apartment(id).delete();
      BuildingCacher.reload();
    }
    catch (SQLException ex) {
    }
  }
  public  static void deleteApartmentCategory(int id){
    try {
      new ApartmentCategory(id).delete();
      BuildingCacher.reload();
    }
    catch (SQLException ex) {
    }
  }
  public  static void deleteApartmentType(int id){
    try {
      new ApartmentType(id).delete();
      BuildingCacher.reload();
    }
    catch (SQLException ex) {

    }
  }

}