package com.idega.block.building.business;


import com.idega.data.EntityFinder;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.data.*;

import java.util.List;
import java.util.Vector;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import com.idega.util.idegaTimestamp;



public class BuildingCacher {

  private static List Complexes,Buildings,Floors,Categories,Types,Apartments;
  private static Hashtable hComplexes,hBuildings,hFloors,hCategories,hTypes,hApartments;
  private static boolean setToReload = false;
  private static idegaTimestamp lastReloaded = idegaTimestamp.RightNow();
  private static int reloadCount = 0;

  public BuildingCacher() {
  }

  private static void initializeLodgings(){
    Complexes = BuildingFinder.listOfComplex();
    Buildings = BuildingFinder.listOfBuilding();
    Floors = BuildingFinder.listOfFloor();
    Categories = BuildingFinder.listOfApartmentCategory();
    Types = BuildingFinder.listOfApartmentType();
    Apartments = BuildingFinder.listOfApartment();
    if(Complexes != null){
      int len = Complexes.size();
      hComplexes = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        Complex C = (Complex)Complexes.get(i);
        hComplexes.put(new Integer(C.getID()),C);
      }
    }
    if(Buildings != null){
      int len = Buildings.size();
      hBuildings = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        Building C = (Building)Buildings.get(i);
        hBuildings.put(new Integer(C.getID()),C);
      }
    }
    if(Floors != null){
      int len = Floors.size();
      hFloors = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        Floor C = (Floor)Floors.get(i);
        hFloors.put(new Integer(C.getID()),C);
      }
    }
    if(Categories != null){
      int len = Categories.size();
      hCategories = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        ApartmentCategory C = (ApartmentCategory)Categories.get(i);
        hCategories.put(new Integer(C.getID()),C);
      }
    }
    if(Types != null){
      int len = Types.size();
      hTypes = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        ApartmentType C = (ApartmentType) Types.get(i);
        hTypes.put(new Integer(C.getID()),C);
      }
    }
    if(Apartments != null){
      int len = Apartments.size();
      hApartments = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        Apartment C = (Apartment) Apartments.get(i);
        hApartments.put(new Integer(C.getID()),C);
      }
    }
  }

  public static void reload(){
    initializeLodgings();
    lastReloaded = idegaTimestamp.RightNow();
    reloadCount++;
    setToReload = false;
  }

  public static void setToReloadNextTimeReferenced(){
    setToReload = true;
  }

  public static idegaTimestamp getLastReloaded(){
    return lastReloaded;
  }

  public static int getReloadedCount(){
    return reloadCount;
  }

  public static List getBuildings(){
    if(hComplexes == null || setToReload){
      reload();
    }
    return Buildings;
  }
  public static List getComplexes(){
    if(hBuildings == null || setToReload){
      reload();
    }
    return Complexes;
  }
  public static List getFloors(){
    if(hFloors == null || setToReload){
      reload();
    }
    return Floors;
  }
  public static List getTypes(){
    if(hTypes == null || setToReload){
      reload();
    }
    return Types;
  }
  public static List getCategories(){
    if(hCategories == null || setToReload){
      reload();
    }
    return Categories;
  }
  public static List getApartments(){
    if(hApartments == null || setToReload){
      reload();
    }
    return Apartments;
  }

  public static Hashtable hashOfBuildings(){
    if(hComplexes == null || setToReload){
      reload();
    }
    return hBuildings;
  }
  public static Hashtable hashOfComplexes(){
    if(hBuildings == null || setToReload){
      reload();
    }
    return hComplexes;
  }
  public static Hashtable hashOfFloors(){
    if(hFloors == null || setToReload){
      reload();
    }
    return hFloors;
  }
  public static Hashtable hashOfTypes(){
    if(hTypes == null || setToReload){
      reload();
    }
    return hTypes;
  }
  public static Hashtable hashOfCategories(){
    if(hCategories == null || setToReload){
      reload();
    }
    return hCategories;
  }
  public static Hashtable hashOfApartments(){
    if(hApartments == null || setToReload){
      reload();
    }
    return hApartments;
  }



  public static Complex getComplex(int id){
    if(hComplexes == null || setToReload){
      reload();
    }
    return (Complex)hComplexes.get(new Integer(id));
  }

  public static Building getBuilding(int id){
    if(hBuildings == null || setToReload){
      reload();
    }
    return (Building)hBuildings.get(new Integer(id));
  }

  public static Floor getFloor(int id){
    if(hFloors == null || setToReload){
      reload();
    }
    return (Floor)hFloors.get(new Integer(id));
  }

  public static ApartmentCategory getApartmentCategory(int id){
    if(hCategories == null || setToReload){
      reload();
    }
    return (ApartmentCategory)hCategories.get(new Integer(id));
  }

  public static ApartmentType getApartmentType(int id){
    if(hTypes == null || setToReload){
      reload();
    }
    return (ApartmentType)hTypes.get(new Integer(id));
  }

  public static Apartment getApartment(int id){
    if(hApartments == null || setToReload){
      reload();
    }
    return (Apartment)hApartments.get(new Integer(id));
  }
} // Class end