package com.idega.block.building.business;

import com.idega.block.building.data.*;
import java.sql.SQLException;
import com.idega.util.idegaCalendar;
import com.idega.util.idegaTimestamp;
import java.util.List;
import java.util.Hashtable;
import java.util.Vector;
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

  public static List listOfComplex(){
    try{
      return EntityFinder.findAll(new Complex());
    }
    catch(SQLException e){return null;}
  }
  public static List listOfBuilding(){
    try{
      return EntityFinder.findAll(new Building());
    }
    catch(SQLException e){return null;}
  }

  public static List listOfFloor(){
    try{
      return EntityFinder.findAll(new Floor());
    }
    catch(SQLException e){return null;}
  }

  public static List listOfApartmentType(){
    try{
     return EntityFinder.findAll(new ApartmentType());
    }
    catch(SQLException e){return null;}
  }

  public static List listOfApartmentCategory(){
    try{
     return EntityFinder.findAll(new ApartmentCategory());
    }
    catch(SQLException e){return null;}
  }

  public static List listOfApartment(){
    try{
     return EntityFinder.findAll(new Apartment());
    }
    catch(SQLException e){return null;}
  }

  public static List ListOfAparmentOrderedByFloor(){
    try{
     return EntityFinder.findAllOrdered(new Apartment(),Apartment.getFloorIdColumnName());
    }
    catch(SQLException e){return null;}
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

  public static Hashtable getLodgingsHash(){
    Hashtable hashtable = new Hashtable();
    List BuildingList = listOfBuilding();
    List FloorList = listOfFloor();
    List TypeList = listOfApartmentType();
    List CategoryList = listOfApartmentCategory();
    List ComplexList  = listOfComplex();
    if(ComplexList != null){
      int clen = ComplexList.size();
      Complex C;
      for (int i = 0; i < clen; i++) {
        C = (Complex) ComplexList.get(i);
        hashtable.put("x_"+C.getID(),C);
      }
    }
    if(BuildingList != null){
      int clen = BuildingList.size();
      Building B;
      for (int i = 0; i < clen; i++) {
        B = (Building) BuildingList.get(i);
        hashtable.put("b_"+B.getID(),B);
      }
    }
    if(FloorList != null){
      int len = FloorList.size();
      Floor F;
      for (int i = 0; i < len; i++) {
        F = (Floor) FloorList.get(i);
        hashtable.put("f_"+F.getID(),F);
      }
    }
    if(TypeList != null){
      int len = TypeList.size();
      ApartmentType T;
      for (int i = 0; i < len; i++) {
        T = (ApartmentType) TypeList.get(i);
        hashtable.put("t_"+T.getID(),T);
      }
    }
    if(CategoryList != null){
      int len = CategoryList.size();
      ApartmentCategory C;
      for (int i = 0; i < len; i++) {
        C = (ApartmentCategory) CategoryList.get(i);
        hashtable.put("c_"+C.getID(),C);
      }
    }

    return hashtable;
  }

  public static List listOfApartmentsInType(int id){
    Apartment A = new Apartment();
    List L = null;
    try{
     L = EntityFinder.findAllByColumnOrdered(A,A.getIDColumnName(),String.valueOf(id),A.getNameColumnName());
    }
    catch(SQLException sql){}
    return L;
  }

  public static List searchApartment(String searchname){
    Apartment A = new Apartment();
    List L = null;
    try {
      L = EntityFinder.findAllByColumnOrdered(A,A.getNameColumnName(),searchname,A.getFloorIdColumnName());
    }
    catch (SQLException ex) {

    }
    return L;
  }

  public static Vector getApartmentTypesComplexForCategory(int categoryId) {
    Vector v = new Vector();
    Connection Conn = null;

    String sqlString = "select * from v_cam_aprt_type_complex where bu_aprt_cat_id = " + categoryId;

    try {
      Conn = com.idega.util.database.ConnectionBroker.getConnection();
      Statement stmt = Conn.createStatement();
      ResultSet RS  = stmt.executeQuery(sqlString);
      int a = 0;

      while (RS.next()) {
        ApartmentTypeComplexHelper appHelp = new ApartmentTypeComplexHelper();
        int typeId = RS.getInt("bu_aprt_type_id");
        int complexId = RS.getInt("bu_complex_id");
        String typeName = RS.getString("aprt_type_name");
        String complexName = RS.getString("complex_name");

        appHelp.setKey(typeId,complexId);
        appHelp.setName(typeName + " (" + complexName + ")");
        v.addElement(appHelp);
      }

      RS.close();
      stmt.close();
    }
    catch(SQLException e) {
      System.err.println(e.toString());
    }
    finally{
      com.idega.util.database.ConnectionBroker.freeConnection(Conn);
    }

    return(v);
  }

  public static List listOfBuildingsInComplex(int id){
     Building B = new Building();
    List L = null;
    try {
      L = EntityFinder.findAllByColumn(B,B.getComplexIdColumnName(),id);
    }
    catch (SQLException ex) {

    }
    return L;

  }

}// class end