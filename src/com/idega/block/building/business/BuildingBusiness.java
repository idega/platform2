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

  public static boolean saveComplex(int id,String sName,String sInfo,int imageid,int textid){
   Complex eComplex = ((com.idega.block.building.data.ComplexHome)com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).createLegacy();
    boolean update = false;
    try{
      if(id > 0){
        eComplex = ((com.idega.block.building.data.ComplexHome)com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).findByPrimaryKeyLegacy(id);
      }
      eComplex.setName(sName);
      eComplex.setInfo(sInfo);
      eComplex.setImageId(imageid);
      eComplex.setTextId(textid);
      eComplex.store();
      BuildingCacher.reload();
      return true;

    }
    catch(SQLException e){e.printStackTrace();}
    return false;
  }

  public static boolean saveBuilding(int id,String sName,String sAddress,
    String sInfo,int imageid,int complexid,String sSerie){
   Building ebuilding = ((com.idega.block.building.data.BuildingHome)com.idega.data.IDOLookup.getHomeLegacy(Building.class)).createLegacy();
    boolean update = false;
    try{
      if(complexid > 0){
        if(id > 0 ){
          ebuilding = ((com.idega.block.building.data.BuildingHome)com.idega.data.IDOLookup.getHomeLegacy(Building.class)).findByPrimaryKeyLegacy(id);
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
      Floor efloor = ((com.idega.block.building.data.FloorHome)com.idega.data.IDOLookup.getHomeLegacy(Floor.class)).createLegacy();
      boolean update = false;
      try{
      if(id > 0){
        efloor = ((com.idega.block.building.data.FloorHome)com.idega.data.IDOLookup.getHomeLegacy(Floor.class)).findByPrimaryKeyLegacy(id);
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
      ApartmentCategory eACategory = ((com.idega.block.building.data.ApartmentCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentCategory.class)).createLegacy();
      if(id >0){
        eACategory = ((com.idega.block.building.data.ApartmentCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentCategory.class)).findByPrimaryKeyLegacy(id);
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
    boolean storage,boolean study,boolean furniture,boolean loft){
     try{
        if(categoryid > 0){
          ApartmentType etype = ((com.idega.block.building.data.ApartmentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentType.class)).createLegacy();
          boolean update = false;
          if(id > 0){
            etype = ((com.idega.block.building.data.ApartmentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentType.class)).findByPrimaryKeyLegacy(id);
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
          etype.setLoft(loft);
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
      Apartment apartment = ((com.idega.block.building.data.ApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).createLegacy();
      if(id >0){
        apartment = ((com.idega.block.building.data.ApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).findByPrimaryKeyLegacy(id);
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
      ((com.idega.block.building.data.ComplexHome)com.idega.data.IDOLookup.getHomeLegacy(Complex.class)).findByPrimaryKeyLegacy(id).delete();
      BuildingCacher.reload();
    }
    catch(SQLException sql){}
  }
  public static void deleteBuilding(int id){
    try {
      ((com.idega.block.building.data.BuildingHome)com.idega.data.IDOLookup.getHomeLegacy(Building.class)).findByPrimaryKeyLegacy(id).delete();
      BuildingCacher.reload();
    }
    catch (SQLException ex) {
    }
  }
  public static void deleteFloor(int id){
    try {
      ((com.idega.block.building.data.FloorHome)com.idega.data.IDOLookup.getHomeLegacy(Floor.class)).findByPrimaryKeyLegacy(id).delete();
      BuildingCacher.reload();
    }
    catch (SQLException ex) {

    }
  }
  public static void deleteApartment(int id){
    try {
      ((com.idega.block.building.data.ApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).findByPrimaryKeyLegacy(id).delete();
      BuildingCacher.reload();
    }
    catch (SQLException ex) {
    }
  }
  public  static void deleteApartmentCategory(int id){
    try {
      ((com.idega.block.building.data.ApartmentCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentCategory.class)).findByPrimaryKeyLegacy(id).delete();
      BuildingCacher.reload();
    }
    catch (SQLException ex) {
    }
  }
  public  static void deleteApartmentType(int id){
    try {
      ((com.idega.block.building.data.ApartmentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentType.class)).findByPrimaryKeyLegacy(id).delete();
      BuildingCacher.reload();
    }
    catch (SQLException ex) {

    }
  }

}
