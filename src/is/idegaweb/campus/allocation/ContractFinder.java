/*
 * $Id: ContractFinder.java,v 1.6 2001/08/18 21:43:44 aron Exp $
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

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public abstract class ContractFinder {

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

  public static List listOfApplicantContracts(int iApplicantId){
    try {
      Contract C = new Contract();
      return EntityFinder.findAllByColumn(C,C.getApplicantIdColumnName(),iApplicantId);
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static List listOfContracts(String sComplexId,String sBuildingId,String sFloorId,String sType,String sCategory,String status){

    StringBuffer sql = new StringBuffer("select con.* ");
    sql.append(" from bu_apartment a,bu_floor f,bu_building b");
    sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y,cam_contract con ");
    sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
    sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
    sql.append(" and a.bu_floor_id = f.bu_floor_id ");
    sql.append(" and f.bu_building_id = b.bu_building_id ");
    sql.append(" and b.bu_complex_id = c.bu_complex_id ");
    sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
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
    sql.append(" order by con.bu_apartment_id");
    String sSQL = sql.toString();
    System.err.println(sSQL);
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

    StringBuffer sql = new StringBuffer("select * from cam_contract ");
    if(!"".equalsIgnoreCase(status)){
      sql.append(" where status = '");
      sql.append(status);
      sql.append("'");
    }
    int count = 0;
    try{
      count = new Contract().getNumberOfRecords(sql.toString());
    }
    catch(SQLException ex){}
    if(count < 0)
      count = 0;
    return count;
  }
}




