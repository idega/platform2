package is.idega.idegaweb.campus.block.allocation.business;


import is.idega.idegaweb.campus.data.ApartmentContracts;
import is.idega.idegaweb.campus.block.allocation.data.*;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import com.idega.data.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;
import com.idega.core.user.data.User;
import com.idega.block.application.data.Applicant;
//import com.idega.block.application.data.ApplicantBean;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.business.BuildingCacher;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import java.sql.*;
import com.idega.util.database.ConnectionBroker;


/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public abstract class ContractFinder {
	
   public final  static int NAME = 0,SSN=1,APARTMENT = 2,FLOOR=3,BUILDING=4,
      COMPLEX=5,CATEGORY=6,TYPE=7,CONTRACT = 8,APPLICANT = 9;
/*

  public static Contract getContract(int id){
    if(id > 0){
      try {
        return ((ContractHome)IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(id));
      }
      catch (Exception ex) {
      }
    }
    return null;
  }

  public static List listOfContracts(){
    try {
      return EntityFinder.getInstance().findAll(Contract.class);
    }
    catch(Exception e){
      return(null);
    }
  }

  public static List listOfApartmentUsers(int iApartmentId){
    StringBuffer sql = new StringBuffer("select u.* from");
    sql.append(" ic_user u,cam_contract c ");
    sql.append(" where u.ic_user_id = c.ic_user_id ");
    sql.append(" and c.bu_apartment_id = ");
    sql.append(iApartmentId );
    try {
      return EntityFinder.getInstance().findAll(User.class,sql.toString());
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static Map mapOfApartmentUsersBy(List listOfUsers){
    if(listOfUsers!=null){
      Iterator I = listOfUsers.iterator();
      Hashtable H = new Hashtable();
      User user;
      while(I.hasNext()){
        user = (User) I.next();
        H.put(new Integer(user.getID()),user);
      }
      return H;
    }
    return null;
  }

  public static List listOfApartmentContracts(int iApartmentId){
    try {
      return EntityFinder.getInstance().findAllByColumnDescendingOrdered( Contract.class,ContractBMPBean.getApartmentIdColumnName(),String.valueOf(iApartmentId ),ContractBMPBean.getValidToColumnName());
    }
    catch(Exception e){
      return(null);
    }
  }

  public static List listOfApartmentContracts(int iApartmentId,String status){
    try {
      return EntityFinder.getInstance().findAllByColumnDescendingOrdered( Contract.class,ContractBMPBean.getApartmentIdColumnName(),String.valueOf(iApartmentId ),ContractBMPBean.getStatusColumnName(),status,ContractBMPBean.getValidToColumnName());
    }
    catch(Exception e){
      return(null);
    }
  }

   public static List listOfApartmentContracts(int iApartmentId,boolean rented){
    try {
      return EntityFinder.getInstance().findAllByColumnDescendingOrdered( Contract.class,ContractBMPBean.getApartmentIdColumnName(),String.valueOf(iApartmentId ),ContractBMPBean.getRentedColumnName(),(rented? "Y":"N"),ContractBMPBean.getValidToColumnName());
    }
    catch(Exception e){
      return(null);
    }
  }

  public static List listOfStatusContracts(String S){
    try {
      return(EntityFinder.getInstance().findAllByColumn(Contract.class,ContractBMPBean.getStatusColumnName(),S));
    }
    catch(Exception e){
      return(null);
    }
  }

	public static List listOfStatusContracts(String S,String s){
    try {
      return(EntityFinder.getInstance().findAllByColumn(Contract.class,ContractBMPBean.getStatusColumnName(),S));
    }
    catch(Exception e){
      return(null);
    }
  }

   public static List listOfStatusContractUsers(String S){
    try {
      return EntityFinder.getInstance().findAllByColumn(User.class,ContractBMPBean.getStatusColumnName(),S);
    }
    catch(Exception e){
      return(null);
    }
  }

  public static Map mapOfStatusContractUsersByUserId(String S){
    List L = listOfStatusContractUsers(S);
    Hashtable h = null;
    if(L!= null){
      int len = L.size();
      h = new Hashtable(len);
      Iterator I = L.iterator();
      User C;
      while(I.hasNext()){
        C = (User) I.next();
        h.put(new Integer(C.getID()),C);
      }
    }
    return h;
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
        H.put(new Integer(C.getPrimaryKey().toString()),C);
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

  public static Hashtable mapOfNewContractsByApplicantID(){
    List L = listOfStatusContracts(ContractBMPBean.statusCreated);
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

      return EntityFinder.getInstance().findAllByColumn(Contract.class,ContractBMPBean.getApplicantIdColumnName(),iApplicantId);
    }
    catch(Exception e){
      return(null);
    }
  }

  public static List listOfApplicantContracts(int iApplicantId,String status){
    try {

      return EntityFinder.getInstance().findAll(Contract.class,"select * from "+ContractBMPBean.getContractEntityName()+" where "+ContractBMPBean.getApplicantIdColumnName()+" = "+iApplicantId+" and "+ContractBMPBean.getStatusColumnName()+" = '"+status+"'");

    }
    catch(Exception e){
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
      return  EntityFinder.getInstance().findAll(Contract.class,sql.toString());
    }
    catch(Exception ex){
      return null;
    }
  }

  public static List listOfContracts(String sComplexId, String sBuildingId, String sAprtName) {

    StringBuffer sql = new StringBuffer("select con.* ");
    sql.append(" from bu_apartment a, bu_floor f, bu_building b, cam_contract con ");
    sql.append(" where a.bu_floor_id = f.bu_floor_id ");
    sql.append(" and f.bu_building_id = b.bu_building_id ");
    sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
    sql.append(" and b.bu_complex_id  = ");
    sql.append(sComplexId);
    sql.append(" and b.bu_building_id = ");
    sql.append(sBuildingId);
    sql.append(" and a.name = '");
		sql.append(sAprtName);
		sql.append("'");
/*    sql.append(" order by ");
    sql.append(order);*//*
    String sSQL = sql.toString();
    System.err.println(sSQL);
    try {
      return EntityFinder.getInstance().findAll(Contract.class,sql.toString());
    }
    catch(Exception ex){
      return null;
    }
  }

  public static List listOfContractsInComplex(int complexID){

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
    sql.append(" and bu_complex_id  = ");
    sql.append(complexID);
    String order = getOrderString(0);
    if(order != null){
      sql.append(" order by ");
      sql.append(order);
    }
    String sSQL = sql.toString();
    try{
      List list = EntityFinder.getInstance().findAll(Contract.class,sql.toString());
      if ( list != null ) {
        return list;
      }
      return null;
    }
    catch(Exception ex){
      return null;
    }
  }

  public static List listOfContractsInComplex(int complexID,Boolean rented){

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
    sql.append(" and con.app_applicant_id = p.app_applicant_id");
    sql.append(" and bu_complex_id  = ");
    sql.append(complexID);
    if(rented !=null){
      sql.append(" and con.rented = ");
      sql.append(rented.booleanValue()?"'Y'":"'N'");
    }
    String order = getOrderString(0);
    if(order != null){
      sql.append(" order by ");
      sql.append(order);
    }
    String sSQL = sql.toString();
    //System.err.println(sSQL);
    try{
      List list = EntityFinder.getInstance().findAll(Contract.class,sql.toString());
      if ( list != null ) {
        return list;
      }
      return null;
    }
    catch(Exception ex){
      return null;
    }
  }

/*
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
      count = ((Contract)IDOLookup.createLegacy(Contract.class)).getNumberOfRecords(sql.toString());
    }
    catch(Exception ex){}
    if(count < 0)
      count = 0;
    return count;
  }
*/
  /*
  public static int countContracts(String status){

    StringBuffer sql = new StringBuffer("select * from cam_contract ");
    if(!"".equalsIgnoreCase(status)){
      sql.append(" where status = '");
      sql.append(status);
      sql.append("'");
    }
    int count = 0;
    try{
      count = ((Contract)IDOLookup.createLegacy(Contract.class)).getNumberOfRecords(sql.toString());
    }
    catch(Exception ex){}
    if(count < 0)
      count = 0;
    return count;
  }*//*

  /**
   * @deprecated Replaced by findByApplicant(int applicantId)
   *//*
  public static Contract findApplicant(int userID){
    Contract contract = null;
    try {
      List L = EntityFinder.getInstance().findAllByColumn(ContractBMPBean.getStaticInstance(Contract.class),ContractBMPBean.getUserIdColumnName(),userID);
      if(L!= null)
        contract = (Contract) L.get(0);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      contract = null;
    }
    return contract;
  }

  public static Applicant findApplicant(User eUser){
    Applicant eApplicant = null;
    StringBuffer sql = new StringBuffer("select a.* from app_applicant a,cam_contract c");
    sql.append(" where c.app_applicant_id = a.app_applicant_id ");
    sql.append(" and c.ic_user_id =  ");
    sql.append(eUser.getID());
    try {
      List L = EntityFinder.getInstance().findAll(((com.idega.block.application.data.ApplicantHome)com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).createLegacy(),sql.toString());
      if(L!= null)
        eApplicant = (Applicant) L.get(0);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      eApplicant = null;
    }
    return eApplicant;
  }

  public static Applicant getApplicant(Contract contract){
    try {
      return ((com.idega.block.application.data.ApplicantHome)com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(contract.getApplicantId().intValue());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static User findApplicant(Applicant eApplicant){
    User eUser = null;
    StringBuffer sql = new StringBuffer("select u.* from ic_user a,cam_contract c");
    sql.append(" where c.app_applicant_id = a.app_applicant_id ");
    sql.append(" and c.app_applicant_id =  ");
    sql.append(eApplicant.getID());
    try {
      List L = EntityFinder.getInstance().findAll(((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).createLegacy(),sql.toString());
      if(L!= null)
        eUser = (User) L.get(0);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      eUser = null;
    }
    return eUser;
  }


  public static Map mapOfAvailableApartmentContracts(int iApartmentTypeId, int iComplexId){
    List L = listOfAvailable(CONTRACT ,iApartmentTypeId ,iComplexId ) ;
    if(L!= null){
      Hashtable H = new Hashtable();
      Iterator I = L.iterator();
      Integer aprtId;
      ApartmentContracts AC;
      while(I.hasNext() ){
        Contract C = (Contract) I.next();
        aprtId = C.getApartmentId();
        if(H.containsKey(aprtId)){
          AC = (ApartmentContracts) H.get(aprtId);
        }
        else{
          AC = new ApartmentContracts(C.getApartmentId().intValue());
          AC.setApartment(BuildingCacher.getApartment(aprtId.intValue()));
        }
        //System.err.println("adding contract "+C.getID()+" with key "+aprtId);
        AC.addContract(C);
        H.put(aprtId,AC);
      }
      return H;
    }

    return null;
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
    //System.err.println(sql.toString());
    try{
      return  EntityFinder.getInstance().findAll(Contract.class,sql.toString());
    }
    catch(Exception ex){
      ex.printStackTrace();
      return null;
    }
  }

   public static List listOfAvailable(int entity,int iApartmentTypeId, int iComplexId){
    StringBuffer sql = new StringBuffer("select ");
    try{
    if(entity == APARTMENT )
      sql.append(" distinct a.* ");
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
	/** @todo  which contract statuses are defined apartment as available *//*
	// 
    sql.append(" and con.status not in ('G') ");
    // not the ones in garbage
    /** *//*
    if(iComplexId > 0){
      sql.append(" and b.bu_complex_id  = ");
      sql.append(iComplexId);
    }
    if(iApartmentTypeId  > 0){
      sql.append(" and a.bu_aprt_type_id = ");
      sql.append(iApartmentTypeId);
    }
    try{
      List L = null;
      if(entity == CONTRACT)
        L =  EntityFinder.getInstance().findAll(Contract.class,sql.toString());
      else if(entity== APARTMENT)
        L =  EntityFinder.getInstance().findAll(Apartment.class,sql.toString());
      /*
      if(entity == APARTMENT){
        List A = listOfNonContractApartments(iApartmentTypeId,iComplexId);
        if(A != null)
          L.addAll(A);
      }*//*

      return L;
    }
    catch(Exception ex){
      ex.printStackTrace();
      return null;
    }
  }

  /**
   *
   *//*
  public static Contract findByApplicant(int applicantId){
    Contract contract = null;
    try {
      List L = EntityFinder.getInstance().findAllByColumn(ContractBMPBean.getStaticInstance(Contract.class),ContractBMPBean.getApplicantIdColumnName(),applicantId);
      if(L!= null)
        contract = (Contract) L.get(0);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      contract = null;
    }
    return contract;
  }


  /**
   *
   *//*
  public static List findAllContractsByApplicant(int applicantId){
    Contract contract = null;
    try {
      List L = EntityFinder.getInstance().findAllByColumn(ContractBMPBean.getStaticInstance(Contract.class),ContractBMPBean.getApplicantIdColumnName(),applicantId);
      return L;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }


  /**
   *
   *//*
  public static Contract findByUser(int user){
    Contract contract = null;
    try {
      List L = EntityFinder.getInstance().findAllByColumn(ContractBMPBean.getStaticInstance(Contract.class),ContractBMPBean.getUserIdColumnName(),user);
      if(L!= null)
        contract = (Contract) L.get(L.size()-1);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      contract = null;
    }
    return contract;
  }

	
	public static Contract findValidContractByUser(int user){
		Contract contract = null;
		try {
			List L = EntityFinder.getInstance().findAllByColumn(ContractBMPBean.getStaticInstance(Contract.class),ContractBMPBean.getUserIdColumnName(),user,ContractBMPBean.getRentedColumnName(),"Y");
			if (L!= null) {
				contract = (Contract) L.get(L.size()-1);
			} else {
				contract = findByUser(user);			    
			}
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			contract = findByUser(user);
		}
		return contract;
	}

  public static Contract findByUser(User user){
    return findByUser(user.getID());
  }

	public static Contract findValidContractByUser(User user){
		return findValidContractByUser(user.getID());
	}

  public static java.sql.Date getLastContractDateForApartment(int apartmentId)throws SQLException{
    return getLastContractDateForApartment(apartmentId,2);
  }

  private static java.sql.Date getLastContractDateForApartment(int apartmentId,int type)throws SQLException{
    Connection conn= null;
    Statement Stmt= null;
    ResultSetMetaData metaData;
    Vector vector=null;
    String sql;
    if(type == 1)
      sql = "select max(c.valid_from) from cam_contract c where c.bu_apartment_id =  "+apartmentId;
    else
      sql = "select max(c.valid_to) from cam_contract c where c.bu_apartment_id =  "+apartmentId;

    Date date = null;
    try{
      conn = ConnectionBroker.getConnection();
      Stmt = conn.createStatement();

      //System.err.println(sql);
      ResultSet RS = Stmt.executeQuery(sql);
      metaData = RS.getMetaData();
      int count = 1;
      if(RS.next()){
        date = RS.getDate(1);
      }
      RS.close();
    }
    catch(SQLException ex){
      throw new SQLException("SQL : "+sql);
    }
    finally{
      if(Stmt != null){
        Stmt.close();
      }
      if (conn != null){
        ConnectionBroker.freeConnection(conn);
      }
    }
    return date;
  }



  public static Date getNextValidFromDate(int iApartmentId)throws SQLException{
    Date last = getLastContractDateForApartment(iApartmentId,2);
    return null;
  }

  public static Date getNextValidToDate(int iApartmentId)throws SQLException{
    Date last = getLastContractDateForApartment(iApartmentId,2);
    return null;
  }

   public static ApartmentTypePeriods getPeriod(int aprt_type_id){
    try {
      ApartmentTypePeriods A = ((is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriodsHome)com.idega.data.IDOLookup.getHomeLegacy(ApartmentTypePeriods.class)).createLegacy();
      List L = EntityFinder.getInstance().findAllByColumn(A,is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriodsBMPBean.getApartmentTypeIdColumnName(),aprt_type_id);
      if(L!=null)
        return (ApartmentTypePeriods) L.get(0);
      else
        return null;
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List findAllContractsBySSN(String ssn)throws com.idega.data.IDOFinderException{
    StringBuffer sql = new StringBuffer("select c.* ");
    sql.append(" from cam_contract c, app_applicant a where ");
    sql.append(" c.app_applicant_id = a.app_applicant_id and a.ssn like '");
    sql.append(ssn);
    sql.append("'");
    return EntityFinder.getInstance().findAll(Contract.class,sql.toString());
  }

  public static List findAllNonContractApplicationsBySSN(String ssn)throws com.idega.data.IDOFinderException{
    StringBuffer sql = new StringBuffer("select a.* ");
    sql.append(" from app_applicant a ");
    sql.append(" where a.app_applicant_id ");
    sql.append(" not in (select c.app_applicant_id from cam_contract c) ");
    sql.append(" and ssn like '");
    sql.append(ssn);
    sql.append("'");
    return EntityFinder.getInstance().findAll(Applicant.class,sql.toString());
  }
  
  public static ContractHome getContractHome()throws RemoteException{
  		return (ContractHome) IDOLookup.getHome(Contract.class);
  }
}
