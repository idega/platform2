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

  public final  static int APARTMENT = 2,FLOOR=3,BUILDING=4,COMPLEX=5,CATEGORY=6,TYPE=7;


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

  public static String[] findDistinctApartmentTypesInComplex(int iComplexId) {
    StringBuffer sql = new StringBuffer("select distinct ");
     sql.append(ApartmentType.getApartmentCategoryIdColumnName());
    sql.append(" from ");
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
    sql.append(" order by ");
    sql.append(ApartmentType.getApartmentCategoryIdColumnName());

    String[] L = null;
    try{
     L = com.idega.data.SimpleQuerier.executeStringQuery(sql.toString());
    }
    catch(Exception e){}
    return L;
  }

  public static ApartmentType[] findApartmentTypesInCategory(int iCategoryId){
    ApartmentType[] rt = new ApartmentType[0];
    try {
      rt = (ApartmentType[]) new ApartmentType().findAllByColumn(ApartmentType.getApartmentCategoryIdColumnName(),iCategoryId);
    }
    catch (Exception ex) {

    }
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

  public static List listOfApartmentsInTypeAndComplex(int typeId,int cmplxId){
    Apartment A = new Apartment();
    List L = null;
    StringBuffer sql = new StringBuffer("select bu_apartment.* ");
    sql.append("from bu_apartment,bu_floor,bu_building,bu_aprt_type,bu_complex ");
    sql.append("where bu_apartment.bu_aprt_type_id = bu_aprt_type_id ");
    sql.append("and bu_apartment.bu_floor_id = bu_floor_id ");
    sql.append("and bu_building.bu_building_id = bu_floor.bu_building_id ");
    sql.append("and bu_building.bu_complex_id = bu_complex.bu_complex_id ");
    sql.append("and bu_complex.bu_complex_id = ");
    sql.append(cmplxId);
    sql.append(" and bu_aprt_type.bu_aprt_type_id = ");
    sql.append(typeId);
    sql.append(" order by bu_apartment.bu_floor_id ");

    try{
      L = EntityFinder.findAll(A,sql.toString());
    }
    catch(SQLException ex){}
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

  public static Vector getAllApartmentTypesComplex() {
    Vector v = new Vector();
    Connection Conn = null;

    String sqlString = "select * from v_cam_aprt_type_complex";

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
  public static int countApartmentsInTypeAndComplex(int typeId,int cmplxId){
    StringBuffer sql = new StringBuffer("select count(*) ");
    sql.append(" from bu_apartment a,bu_floor f,bu_building b,bu_complex c,bu_aprt_type t ");
    sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
    sql.append(" and a.bu_floor_id = f.bu_floor_id ");
    sql.append(" and f.bu_building_id = b.bu_building_id ");
    sql.append(" and b.bu_complex_id = c.bu_complex_id ");
    sql.append(" and t.bu_aprt_type_id = ");
    sql.append(typeId);
    sql.append(" and c.bu_complex_id = ");
    sql.append(cmplxId);
    int count = 0;
    try{
      count = new Complex().getNumberOfRecords(sql.toString());
    }
    catch(SQLException ex){}
    if(count < 0)
      count = 0;
    return count;
  }

  public static int countRentableApartments(){
    String sql = "select count(*) from "+Apartment.getNameTableName() +" where "+Apartment.getRentableColumnName()+" = 'Y'";
    int count = 0;
    //System.err.println(sql.toString());
    try{
      count = new Apartment().getNumberOfRecords(sql.toString());

    }
    catch(SQLException ex){ex.printStackTrace();}
    if(count < 0)
      count = 0;
    return count;
  }

   public static List listOfApartments(String sComplexId,String sBuildingId,String sFloorId,String sType,String sCategory,int iOrder){

    StringBuffer sql = new StringBuffer("select a.* ");
    sql.append(" from bu_apartment a,bu_floor f,bu_building b");
    sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y");
    sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
    sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
    sql.append(" and a.bu_floor_id = f.bu_floor_id ");
    sql.append(" and f.bu_building_id = b.bu_building_id ");
    sql.append(" and b.bu_complex_id = c.bu_complex_id ");

    if(sComplexId !=null && !"-1".equals(sComplexId)){
      sql.append(" and bu_complex_id  = ");
      sql.append(sComplexId);
    }
    if(sBuildingId !=null && !"-1".equals(sBuildingId)){
      sql.append(" and bu_building_id = ");
      sql.append(sBuildingId);
    }
    if(sFloorId !=null && !"-1".equals(sFloorId)){
      sql.append(" and bu_floor_id = ");
      sql.append(sFloorId);
    }
    if(sType !=null && !"-1".equals(sType)){
      sql.append(" and bu_aprt_type_id = ");
      sql.append(sType);
    }
    if(sCategory !=null && !"-1".equals(sCategory)){
      sql.append(" and bu_aprt_cat_id = ");
      sql.append(sCategory);
    }
    String order = getOrderString(iOrder);
    if(order != null){
      sql.append(" order by ");
      sql.append(order);
    }
    String sSQL = sql.toString();
    //System.err.println(sSQL);
    try{
      return  EntityFinder.findAll(new Apartment(),sql.toString());
    }
    catch(SQLException ex){
      return null;
    }
  }

  private static String getOrderString(int type){
    String order = null;
    switch (type) {
      case BUILDING : order = " b.name " ; break;
      case COMPLEX: order =  " c.name " ; break;
      case FLOOR:  order =  " f.name " ; break;
      case APARTMENT: order =" a.name " ; break;
      case CATEGORY:  order =  " y.name " ; break;
      case TYPE: order =" t.name " ; break;
      default: order = " a.bu_apartment_id ";

    }
    return order;
  }

}// class end