package com.idega.block.building.business;


import com.idega.data.EntityFinder;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.building.data.*;

import java.util.List;
import java.util.Vector;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
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

  public static int getNumberOfComplexes(){
    return getComplexes().size();
  }

  public static int getNumberOfBuildings(){
    return getBuildings().size();
  }

  public static int getNumberOfApartments(){
    return getApartments().size();
  }

  public static int getNumberOfFloors(){
    return getFloors().size();
  }

  public static String getApartmentString(int iApartmentId){
    Apartment A = getApartment(iApartmentId);
    StringBuffer string = new StringBuffer(A.getName());
    string.append(" ");
    Floor F = getFloor(A.getFloorId());
    string.append(F.getName());
    string.append(" ");
    Building B = getBuilding(F.getBuildingId());
    string.append(B.getName());
    return string.toString();
  }

  public final static char CHARCOMPLEX = 'x';
  public final static char CHARBUILDING = 'b';
  public final static char CHARFLOOR = 'f';
  public final static char CHARAPARTMENT = 'a';
  public final static char CHARCATEGORY = 'c';
  public final static char CHARTYPE = 't';
  public final static char CHARALL = 'h';

  private static final String score = "_";
  public static String PREFIXCOMPLEX = String.valueOf(CHARCOMPLEX)+score;
  public static String PREFIXBUILDING = String.valueOf(CHARBUILDING)+score;
  public static String PREFIXFLOOR = String.valueOf(CHARFLOOR)+score;
  public static String PREFIXAPARTMENT = String.valueOf(CHARAPARTMENT)+score;
  public static String PREFIXCATEGORY = String.valueOf(CHARCATEGORY)+score;
  public static String PREFIXTYPE = String.valueOf(CHARTYPE)+score;
  public static String PREFIXALL = String.valueOf(CHARALL)+score;

  public static Map mapOfLodgingsObjects(){
    Hashtable hashtable = new Hashtable();
    List BuildingList = getBuildings();
    List FloorList = getFloors();
    List TypeList = getTypes();
    List CategoryList = getCategories();
    List ComplexList  = getComplexes();
    hashtable.put(PREFIXALL,"ALL");
    if(TypeList != null){
      int len = TypeList.size();
      ApartmentType T;
      for (int i = 0; i < len; i++) {
        T = (ApartmentType) TypeList.get(i);
        hashtable.put(PREFIXTYPE+T.getID(),T);
      }
    }
    if(CategoryList != null){
      int len = CategoryList.size();
      ApartmentCategory C;
      for (int i = 0; i < len; i++) {
        C = (ApartmentCategory) CategoryList.get(i);
        hashtable.put(PREFIXCATEGORY+C.getID(),C);
      }
    }

    if(FloorList != null){
      int len = FloorList.size();
      Floor F;
      for (int i = 0; i < len; i++) {
        F = (Floor) FloorList.get(i);
        hashtable.put(PREFIXFLOOR+F.getID(),F);
      }
    }

    if(BuildingList != null){
      int clen = BuildingList.size();
      Building B;
      for (int i = 0; i < clen; i++) {
        B = (Building) BuildingList.get(i);
        hashtable.put(PREFIXBUILDING+B.getID(),B);
      }
    }

    if(ComplexList != null){
      int clen = ComplexList.size();
      Complex C;
      for (int i = 0; i < clen; i++) {
        C = (Complex) ComplexList.get(i);
        hashtable.put(PREFIXCOMPLEX+C.getID(),C);
      }
    }

    return hashtable;
  }

  public static Map mapOfLodgingsNames(){
    Hashtable hashtable = new Hashtable();
    List BuildingList = getBuildings();
    List FloorList = getFloors();
    List TypeList = getTypes();
    List CategoryList = getCategories();
    List ComplexList  = getComplexes();
    if(TypeList != null){
      int len = TypeList.size();
      ApartmentType T;
      for (int i = 0; i < len; i++) {
        T = (ApartmentType) TypeList.get(i);
        hashtable.put(PREFIXTYPE+T.getID(),T.getName());
      }
    }
    if(CategoryList != null){
      int len = CategoryList.size();
      ApartmentCategory C;
      for (int i = 0; i < len; i++) {
        C = (ApartmentCategory) CategoryList.get(i);
        hashtable.put(PREFIXCATEGORY+C.getID(),C.getName());
      }
    }

    if(FloorList != null){
      int len = FloorList.size();
      Floor F;
      for (int i = 0; i < len; i++) {
        F = (Floor) FloorList.get(i);
        hashtable.put(PREFIXFLOOR+F.getID(),F.getName());
      }
    }

    if(BuildingList != null){
      int clen = BuildingList.size();
      Building B;
      for (int i = 0; i < clen; i++) {
        B = (Building) BuildingList.get(i);
        hashtable.put(PREFIXBUILDING+B.getID(),B.getName());
      }
    }

    if(ComplexList != null){
      int clen = ComplexList.size();
      Complex C;
      for (int i = 0; i < clen; i++) {
        C = (Complex) ComplexList.get(i);
        hashtable.put(PREFIXCOMPLEX+C.getID(),C.getName());
      }
    }

    return hashtable;
  }

} // Class end