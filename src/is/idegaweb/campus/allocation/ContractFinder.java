/*
 * $Id: ContractFinder.java,v 1.14 2001/11/08 15:40:39 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.allocation;


import is.idegaweb.campus.entity.*;
import java.sql.SQLException;
import java.util.List;
import com.idega.data.EntityFinder;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;
import com.idega.core.user.data.User;
import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.Apartment;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public abstract class ContractFinder {
  public final  static int NAME = 0,SSN=1,APARTMENT = 2,FLOOR=3,BUILDING=4,
      COMPLEX=5,CATEGORY=6,TYPE=7,CONTRACT = 8,APPLICANT = 9;

  public static List listOfContracts(){
    try {
      return(EntityFinder.findAll(new Contract()));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static List listOfStatusContracts(String S){
    try {
      return(EntityFinder.findAllByColumn(new Contract(),Contract.getStatusColumnName(),S));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static Map mapOfStatusContractByApartmentId(String s){
    List L = listOfStatusContracts(s);
    Hashtable h = null;
    if(L!= null){
      int len = L.size();
      h = new Hashtable(len);
      Iterator I = L.iterator();
      Contract C;
      while(I.hasNext()){
        C = (Contract) I.next();
        h.put(C.getApartmentId(),C);
      }
    }
    return h;
  }

  public static Hashtable hashOfContracts(){
    List L = listOfContracts();
    if(L!=null){
      Hashtable H = new Hashtable();
      int len = L.size();
      for (int i = 0; i < len; i++) {
        Contract C = (Contract) L.get(i);
        H.put(new Integer(C.getID()),C);
      }
      return H;
    }
    else
      return null;
  }

  public static Hashtable hashOfApartmentsContracts(){
    List L = listOfContracts();
    if(L!=null){
      Hashtable H = new Hashtable();
      int len = L.size();
      for (int i = 0; i < len; i++) {
        Contract C = (Contract) L.get(i);
        H.put((C.getApartmentId()),C);
      }
      return H;
    }
    else
      return null;
  }

  public static Hashtable hashOfApplicantsContracts(){
    List L = listOfContracts();
    if(L!=null){
      Hashtable H = new Hashtable();
      int len = L.size();
      for (int i = 0; i < len; i++) {
        Contract C = (Contract) L.get(i);
        H.put((C.getApplicantId()),C);
      }
      return H;
    }
    else
      return null;
  }

  public static Map mapOfContracts(List listOfContracts,int keyType){
    if(listOfContracts!=null){
      Hashtable H = new Hashtable();
      int len = listOfContracts.size();
      for (int i = 0; i < len; i++) {
        Contract C = (Contract) listOfContracts.get(i);
        String key = String.valueOf(C.getID());
        switch (keyType) {
          case APARTMENT : key = String.valueOf(C.getApartmentId()); break;
          case APPLICANT: key = String.valueOf(C.getApplicantId()); break;
          case CONTRACT : key = String.valueOf(C.getID());break;
        }
        H.put(key,C);
      }
      return H;
    }
    else
      return null;
  }

  public static List listOfApplicantContracts(int iApplicantId){
    try {
      Contract C = new Contract();
      return EntityFinder.findAllByColumn(C,C.getApplicantIdColumnName(),iApplicantId);
    }
    catch(SQLException e){
      return(null);
    }
  }

  private static String getOrderString(int type){
    String order = null;
    switch (type) {
      case NAME :  order = " p.first_name,p.middle_name,p.last_name "; break;
      case SSN:  order = " p.ssn "; break;
      case BUILDING : order = " b.name " ; break;
      case COMPLEX: order =  " c.name " ; break;
      case FLOOR:  order =  " f.name " ; break;
      case APARTMENT: order =" a.name " ; break;
      case CATEGORY:  order =  " y.name " ; break;
      case TYPE: order =" t.name " ; break;
      default: order = " con.bu_apartment_id ";

    }
    return order;
  }

  public static List listOfNonContractApartments(int iApartmentTypeId, int iComplexId){
    StringBuffer sql = new StringBuffer("select a.*");
    sql.append(" from bu_apartment a,bu_floor f,bu_building b,app_applicant p ");
    sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y,cam_contract con ");
    sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
    sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
    sql.append(" and a.bu_floor_id = f.bu_floor_id ");
    sql.append(" and f.bu_building_id = b.bu_building_id ");
    sql.append(" and b.bu_complex_id = c.bu_complex_id ");
    sql.append(" and a.bu_apartment_id not in ( select bu_apartment_id from cam_contract");
    if(iComplexId > 0){
      sql.append(" and bu_complex_id  = ");
      sql.append(iComplexId);
    }
    if(iApartmentTypeId  > 0){
      sql.append(" and bu_aprt_type_id = ");
      sql.append(iApartmentTypeId);
    }
    System.err.println(sql.toString());
    try{
      return  EntityFinder.findAll(new Contract(),sql.toString());
    }
    catch(SQLException ex){
      return null;
    }
  }

  public static List listOfAvailable(int entity,int iApartmentTypeId, int iComplexId){
    StringBuffer sql = new StringBuffer("select ");
    try{
    if(entity == APARTMENT )
      sql.append(" a.* ");
    else if(entity == CONTRACT)
      sql.append(" con.* ");
    else
      throw new IllegalArgumentException();
    }
    catch(IllegalArgumentException e){
      e.printStackTrace();
      return null;
    }
    sql.append(" from bu_apartment a,bu_floor f,bu_building b,app_applicant p ");
    sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y,cam_contract con ");
    sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
    sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
    sql.append(" and a.bu_floor_id = f.bu_floor_id ");
    sql.append(" and f.bu_building_id = b.bu_building_id ");
    sql.append(" and b.bu_complex_id = c.bu_complex_id ");
    sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
    sql.append(" and con.app_applicant_id = p.app_applicant_id");
    sql.append(" and con.status in ('T','E') ");
    if(iComplexId > 0){
      sql.append(" and bu_complex_id  = ");
      sql.append(iComplexId);
    }
    if(iApartmentTypeId  > 0){
      sql.append(" and bu_aprt_type_id = ");
      sql.append(iApartmentTypeId);
    }
    System.err.println(sql.toString());
    try{
      List L = null;
      if(entity == CONTRACT)
        L =  EntityFinder.findAll(new Contract(),sql.toString());
      else if(entity== APARTMENT)
        L =  EntityFinder.findAll(new Apartment(),sql.toString());
      if(entity == APARTMENT){
        List A = listOfNonContractApartments(iApartmentTypeId,iComplexId);
        if(A != null)
          L.addAll(A);
      }
      return L;
    }
    catch(SQLException ex){
      return null;
    }
  }


  public static List listOfContracts(String sComplexId,String sBuildingId,String sFloorId,String sType,String sCategory,String status,int iOrder){

    StringBuffer sql = new StringBuffer("select con.* ");
    sql.append(" from bu_apartment a,bu_floor f,bu_building b,app_applicant p ");
    sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y,cam_contract con ");
    sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
    sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
    sql.append(" and a.bu_floor_id = f.bu_floor_id ");
    sql.append(" and f.bu_building_id = b.bu_building_id ");
    sql.append(" and b.bu_complex_id = c.bu_complex_id ");
    sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
    sql.append(" and con.app_applicant_id = p.app_applicant_id");
    if(status !=null && !"".equals(status)){
      sql.append(" and con.status = '");
      sql.append(status);
      sql.append("' ");
    }
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
      return  EntityFinder.findAll(new Contract(),sql.toString());
    }
    catch(SQLException ex){
      return null;
    }
  }

  public static int countApartmentsInTypeAndComplex(int typeId,int cmplxId,String status){
    StringBuffer sql = new StringBuffer("select count(*) ");
    sql.append(" from bu_apartment a,bu_floor f,bu_building b");
    sql.append(",bu_complex c,bu_aprt_type t,cam_contract con ");
    sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
    sql.append(" and a.bu_floor_id = f.bu_floor_id ");
    sql.append(" and f.bu_building_id = b.bu_building_id ");
    sql.append(" and b.bu_complex_id = c.bu_complex_id ");
    sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
    if(status !=null && !"".equals(status)){
      sql.append(" and con.status = '");
      sql.append(status);
      sql.append("' ");
    }
    sql.append(" and t.bu_aprt_type_id = ");
    sql.append(typeId);
    sql.append(" and c.bu_complex_id = ");
    sql.append(cmplxId);
    int count = 0;
    try{
      count = new Contract().getNumberOfRecords(sql.toString());
    }
    catch(SQLException ex){}
    if(count < 0)
      count = 0;
    return count;
  }

  public static int countContracts(String status){

    StringBuffer sql = new StringBuffer("select count(*) from cam_contract ");
    if(!"".equalsIgnoreCase(status)){
      sql.append(" where status = '");
      sql.append(status);
      sql.append("'");
    }
    //System.err.println(sql.toString());
    int count = 0;
    try{
      count = new Contract().getNumberOfRecords(sql.toString());
    }
    catch(SQLException ex){}
    if(count < 0)
      count = 0;
    return count;
  }

  public static Applicant findApplicant(User eUser){
    Applicant eApplicant = null;
    if(eUser != null){
    StringBuffer sql = new StringBuffer("select a.* from app_applicant a,cam_contract c");
    sql.append(" where c.app_applicant_id = a.app_applicant_id ");
    sql.append(" and c.ic_user_id =  ");
    sql.append(eUser.getID());
    try {
      List L = EntityFinder.findAll(new Applicant(),sql.toString());
      if(L!= null)
        eApplicant = (Applicant) L.get(0);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      eApplicant = null;
    }
    }
    return eApplicant;
  }

  public static User findApplicant(Applicant eApplicant){
    User eUser = null;
    StringBuffer sql = new StringBuffer("select u.* from ic_user a,cam_contract c");
    sql.append(" where c.app_applicant_id = a.app_applicant_id ");
    sql.append(" and c.app_applicant_id =  ");
    sql.append(eApplicant.getID());
    try {
      List L = EntityFinder.findAll(new User(),sql.toString());
      if(L!= null)
        eUser = (User) L.get(0);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      eUser = null;
    }
    return eUser;
  }


}




