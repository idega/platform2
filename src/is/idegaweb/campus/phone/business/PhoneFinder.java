package is.idegaweb.campus.phone.business;

import com.idega.data.EntityFinder;
import is.idegaweb.campus.entity.CampusPhone;
import java.util.List;
import java.util.Hashtable;
import java.util.Map;
import java.sql.SQLException;
import com.idega.block.finance.data.Account;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public abstract class PhoneFinder {
  public final  static int NAME = 0,SSN=1,APARTMENT = 2,FLOOR=3,BUILDING=4,
      COMPLEX=5,CATEGORY=6,TYPE=7;

  public static List listOfPhones(){
    try {
      return(EntityFinder.findAll(new CampusPhone()));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static Hashtable mapOfPhonesByPhoneNumber(){
    List L = listOfPhones();
    if(L!=null){
      Hashtable H = new Hashtable();
      int len = L.size();
      for (int i = 0; i < len; i++) {
        CampusPhone C = (CampusPhone) L.get(i);
        H.put(C.getPhoneNumber(),C);
      }
      return H;
    }
    else
      return null;
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

  public static Map mapOfPhones(List listOfPhones){
    if(listOfPhones != null){
      int len = listOfPhones.size();
      Hashtable H = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        CampusPhone P = (CampusPhone) listOfPhones.get(i);
        H.put(new Integer(P.getID()),P);
      }
      return H;
    }
    else return null;
  }


  public static Map mapOfPhonesByApartmentId(List listOfPhones){
    if(listOfPhones != null){
      int len = listOfPhones.size();
      Hashtable H = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        CampusPhone P = (CampusPhone) listOfPhones.get(i);
        H.put(new Integer(P.getApartmentId()),P);
      }
      return H;
    }
    else return null;
  }

  public static List listOfPhones(String sComplexId,String sBuildingId,String sFloorId,String sType,String sCategory,int iOrder){

    StringBuffer sql = new StringBuffer("select pho.* ");
    sql.append(" from bu_apartment a,bu_floor f,bu_building b");
    sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y,cam_phone pho ");
    sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
    sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
    sql.append(" and a.bu_floor_id = f.bu_floor_id ");
    sql.append(" and f.bu_building_id = b.bu_building_id ");
    sql.append(" and b.bu_complex_id = c.bu_complex_id ");
    sql.append(" and a.bu_apartment_id = pho.bu_apartment_id");

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
      return  EntityFinder.findAll(new CampusPhone(),sql.toString());
    }
    catch(SQLException ex){
      return null;
    }
  }
}
