package com.idega.block.building.business;


import com.idega.data.EntityFinder;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.data.*;

import java.util.List;
import java.util.Vector;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;



public class BuildingCacher {

  private static List Complexes,Buildings,Floors,Categories,Types,Apartments;
  private static Hashtable hComplexes,hBuildings,hFloors,hCategories,hTypes,hApartments;

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
  }

  public static List getBuildings(){
    return Buildings;
  }
  public static List getComplexes(){
    return Complexes;
  }
  public static List getFloors(){
    return Floors;
  }
  public static List getTypes(){
    return Types;
  }
  public static List getCategories(){
    return Categories;
  }
  public static List getApartments(){
    return Apartments;
  }

  public static Complex getComplex(int id){
    if(hComplexes == null){
      reload();
    }
    return (Complex)hComplexes.get(new Integer(id));
  }

  public static Building getBuilding(int id){
    if(hBuildings == null){
      reload();
    }
    return (Building)hBuildings.get(new Integer(id));
  }

  public static Floor getFloor(int id){
    if(hFloors == null){
      reload();
    }
    return (Floor)hFloors.get(new Integer(id));
  }

  public static ApartmentCategory getApartmentCategory(int id){
    if(hCategories == null){
      reload();
    }
    return (ApartmentCategory)hCategories.get(new Integer(id));
  }

  public static ApartmentType getApartmentType(int id){
    if(hTypes == null){
      reload();
    }
    return (ApartmentType)hTypes.get(new Integer(id));
  }

  public static Apartment getApartment(int id){
    if(hApartments == null){
      reload();
    }
    return (Apartment)hApartments.get(new Integer(id));
  }
} // Class end