package com.idega.block.building.business;


import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentCategoryHome;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.ComplexHome;
import com.idega.block.building.data.Floor;
import com.idega.block.building.data.FloorHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.IWTimestamp;



public class BuildingCacher {

  private static Collection Complexes,Buildings,Floors,Categories,Types,Apartments;
  private static Hashtable hComplexes,hBuildings,hFloors,hCategories,hTypes,hApartments;
  private static boolean setToReload = false;
  private static IWTimestamp lastReloaded = IWTimestamp.RightNow();
  private static int reloadCount = 0;
  
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

  public BuildingCacher() {
  }

  private static void initializeLodgings(){
    try {
		Complexes = ((ComplexHome)IDOLookup.getHome(Complex.class)).findAll();
		Buildings = ((BuildingHome) IDOLookup.getHome(Building.class)).findAll();
		Floors = ((FloorHome)IDOLookup.getHome(Floor.class)).findAll();
		Categories = ((ApartmentCategoryHome)IDOLookup.getHome(ApartmentCategory.class)).findAll();
		Types = ((ApartmentTypeHome)IDOLookup.getHome(ApartmentType.class)).findAll();
		Apartments = ((ApartmentHome)IDOLookup.getHome(Apartment.class)).findAll();
		if(Complexes != null){
		  int len = Complexes.size();
		  hComplexes = new Hashtable(len);
		 for (Iterator iter = Complexes.iterator(); iter.hasNext();) {
			Complex C = (Complex) iter.next();
		    hComplexes.put((Integer)(C.getPrimaryKey()),C);
		  }
		}
		if(Buildings != null){
		  int len = Buildings.size();
		  hBuildings = new Hashtable(len);
		  for (Iterator iter = Buildings.iterator(); iter.hasNext();) {
		    Building C = (Building)iter.next();
		    hBuildings.put((Integer)(C.getPrimaryKey()),C);
		  }
		}
		if(Floors != null){
		  int len = Floors.size();
		  hFloors = new Hashtable(len);
		  for (Iterator iter = Floors.iterator(); iter.hasNext();) {
		    Floor C = (Floor)iter.next();
		    hFloors.put((Integer)(C.getPrimaryKey()),C);
		  }
		}
		if(Categories != null){
		  int len = Categories.size();
		  hCategories = new Hashtable(len);
		  for (Iterator iter = Categories.iterator(); iter.hasNext();) {
		    ApartmentCategory C = (ApartmentCategory)iter.next();
		    hCategories.put((Integer)(C.getPrimaryKey()),C);
		  }
		}
		if(Types != null){
		  int len = Types.size();
		  hTypes = new Hashtable(len);
		  for (Iterator iter = Types.iterator(); iter.hasNext();) {
		    ApartmentType C = (ApartmentType) iter.next();
		    hTypes.put((Integer)(C.getPrimaryKey()),C);
		  }
		}
		if(Apartments != null){
		  int len = Apartments.size();
		  hApartments = new Hashtable(len);
		  for (Iterator iter = Apartments.iterator(); iter.hasNext();) {
		    Apartment C = (Apartment) iter.next();
		    hApartments.put((Integer)(C.getPrimaryKey()),C);
		  }
		}
	} catch (IDOLookupException e) {
		e.printStackTrace();
	} catch (EJBException e) {
		e.printStackTrace();
	} catch (FinderException e) {
		e.printStackTrace();
	}
  }

  public static void reload(){
    initializeLodgings();
    lastReloaded = IWTimestamp.RightNow();
    reloadCount++;
    setToReload = false;
  }

  public static void setToReloadNextTimeReferenced(){
    setToReload = true;
  }

  public static IWTimestamp getLastReloaded(){
    return lastReloaded;
  }

  public static int getReloadedCount(){
    return reloadCount;
  }

  public static Collection getBuildings(){
    if(hComplexes == null || setToReload){
      reload();
    }
    return Buildings;
  }
  public static Collection getComplexes(){
    if(hBuildings == null || setToReload){
      reload();
    }
    return Complexes;
  }
  public static Collection getFloors(){
    if(hFloors == null || setToReload){
      reload();
    }
    return Floors;
  }
  public static Collection getTypes(){
    if(hTypes == null || setToReload){
      reload();
    }
    return Types;
  }
  public static Collection getCategories(){
    if(hCategories == null || setToReload){
      reload();
    }
    return Categories;
  }
  public static Collection getApartments(){
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

  public static Complex getComplex(){
    if(hComplexes == null || setToReload){
      reload();
    }
    else {
      Iterator iter = hComplexes.keySet().iterator();
      while (iter.hasNext()) {
        return (Complex) hComplexes.get(iter.next());
      }
    }
    return null;
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

  public static Map mapOfLodgingsObjects(){
    Hashtable hashtable = new Hashtable();
    Collection BuildingList = getBuildings();
    Collection FloorList = getFloors();
    Collection TypeList = getTypes();
    Collection CategoryList = getCategories();
    Collection ComplexList  = getComplexes();
    hashtable.put(PREFIXALL,"ALL");
    if(TypeList != null){
      ApartmentType T;
      for (Iterator iter = TypeList.iterator(); iter.hasNext();) {
        T = (ApartmentType) iter.next();
        hashtable.put(PREFIXTYPE+T.getPrimaryKey(),T);
      }
    }
    if(CategoryList != null){
      ApartmentCategory C;
      for (Iterator iter = CategoryList.iterator(); iter.hasNext();) {
        C = (ApartmentCategory) iter.next();
        hashtable.put(PREFIXCATEGORY+C.getPrimaryKey(),C);
      }
    }

    if(FloorList != null){
      Floor F;
      for (Iterator iter = FloorList.iterator(); iter.hasNext();) {
        F = (Floor) iter.next();
        hashtable.put(PREFIXFLOOR+F.getPrimaryKey(),F);
      }
    }

    if(BuildingList != null){
      Building B;
      for (Iterator iter = BuildingList.iterator(); iter.hasNext();) {
        B = (Building)iter.next();
        hashtable.put(PREFIXBUILDING+B.getPrimaryKey(),B);
      }
    }

    if(ComplexList != null){

      Complex C;
      for (Iterator iter = ComplexList.iterator(); iter.hasNext();) {
        C = (Complex) iter.next();
        hashtable.put(PREFIXCOMPLEX+C.getPrimaryKey(),C);
      }
    }

    return hashtable;
  }

  public static List listOfMapEntries(){
    Vector list = new Vector();
    Collection BuildingList = getBuildings();
    Collection FloorList = getFloors();
    Collection TypeList = getTypes();
    Collection CategoryList = getCategories();
    Collection ComplexList  = getComplexes();
    if(TypeList != null){
      ApartmentType T;
      for (Iterator iter = TypeList.iterator(); iter.hasNext();) {
        T = (ApartmentType)iter.next();
        list.add(PREFIXTYPE+T.getPrimaryKey().toString());
      }
    }
    if(CategoryList != null){
      ApartmentCategory C;
      for (Iterator iter = CategoryList.iterator(); iter.hasNext();) {
        C = (ApartmentCategory) iter.next();
        list.add(PREFIXCATEGORY+C.getPrimaryKey().toString());
      }
    }

    if(FloorList != null){
      Floor F;
      for (Iterator iter = FloorList.iterator(); iter.hasNext();) {
        F = (Floor) iter.next();
        list.add(PREFIXFLOOR+F.getPrimaryKey().toString());
      }
    }

    if(BuildingList != null){
      Building B;
      for (Iterator iter = BuildingList.iterator(); iter.hasNext();) {
        B = (Building) iter.next();
        list.add(PREFIXBUILDING+B.getPrimaryKey().toString());
      }
    }

    if(ComplexList != null){
      Complex C;
      for (Iterator iter = CategoryList.iterator(); iter.hasNext();) {
        C = (Complex) iter.next();
        list.add(PREFIXCOMPLEX+C.getPrimaryKey().toString());
      }
    }

    return list;
  }

  public static Map mapOfLodgingsNames(){
    Hashtable hashtable = new Hashtable();
    Collection BuildingList = getBuildings();
    Collection FloorList = getFloors();
    Collection TypeList = getTypes();
    Collection CategoryList = getCategories();
    Collection ComplexList  = getComplexes();
    if(TypeList != null){
      ApartmentType T;
      for (Iterator iter = TypeList.iterator(); iter.hasNext();) {
        T = (ApartmentType) iter.next();
        hashtable.put(PREFIXTYPE+T.getPrimaryKey().toString(),T.getName());
      }
    }
    if(CategoryList != null){
      ApartmentCategory C;
      for (Iterator iter = CategoryList.iterator(); iter.hasNext();) {
        C = (ApartmentCategory) iter.next();
        hashtable.put(PREFIXCATEGORY+C.getPrimaryKey().toString(),C.getName());
      }
    }

    if(FloorList != null){
      Floor F;
      for (Iterator iter = FloorList.iterator(); iter.hasNext();) {
        F = (Floor) iter.next();
        hashtable.put(PREFIXFLOOR+F.getPrimaryKey().toString(),F.getName());
      }
    }

    if(BuildingList != null){
      Building B;
      for (Iterator iter = BuildingList.iterator(); iter.hasNext();) {
        B = (Building) iter.next();
        hashtable.put(PREFIXBUILDING+B.getPrimaryKey().toString(),B.getName());
      }
    }

    if(ComplexList != null){
      Complex C;
      for (Iterator iter = ComplexList.iterator(); iter.hasNext();) {
        C = (Complex) iter.next();
        hashtable.put(PREFIXCOMPLEX+C.getPrimaryKey().toString(),C.getName());
      }
    }

    return hashtable;
  }

} // Class end
