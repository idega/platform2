package is.idega.idegaweb.campus.block.finance.business;


import com.idega.util.IWTimestamp;
import com.idega.util.IWCalendar;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;
import is.idega.idegaweb.campus.data.*;

import com.idega.block.finance.data.TariffIndex;
import com.idega.block.finance.data.Account;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusAccountFinder  {

  public static List listOfRentingUserAccounts(){
   try {
     return EntityFinder.findAll(((is.idega.idegaweb.campus.data.ContractAccountApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(ContractAccountApartment.class)).createLegacy());
   }
   catch (SQLException ex) {
    return null;
   }
  }

  public static List listOfContractAccountApartmentsInAssessment(int iAssessmentId){
    StringBuffer sql = new StringBuffer("select distinct v.* from V_CONT_ACCT_APRT v");
    sql.append(" where v.fin_account_id in ( ");
    sql.append(" select a.fin_account_id ");
    sql.append("from fin_acc_entry e,fin_assessment_round r, fin_account a ");
    sql.append(" where a.fin_account_id = e.fin_account_id ");
    sql.append(" and e.fin_assessment_round_id = r.fin_assessment_round_id ");
    sql.append(" and r.fin_assessment_round_id = ");
    sql.append(iAssessmentId);
    sql.append(" )");
    //System.print
    try {
      return EntityFinder.findAll(((is.idega.idegaweb.campus.data.ContractAccountApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(ContractAccountApartment.class)).createLegacy(),sql.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }

  }

  /**
   *  Returns a list of view entity ConatractAccountApartment
   *  that have legal contracts in period specified
   *  returns null if nothing found
   */
  public static List listOfContractAccountApartment(String type,IWTimestamp startDate,IWTimestamp endDate){
    StringBuffer sql = new StringBuffer("select * from ").append(is.idega.idegaweb.campus.data.ContractAccountApartmentBMPBean.getEntityTableName());
    sql.append(" where ").append(is.idega.idegaweb.campus.data.ContractAccountApartmentBMPBean.getAccountTypeColumnName());
    sql.append(" = '").append(type).append("'");
    String start = "'"+startDate.toSQLString()+"'";
    String end = "'"+endDate.toSQLString()+"'";
    String validto = is.idega.idegaweb.campus.data.ContractAccountApartmentBMPBean.getColumnValidTo();
    String validfrom = is.idega.idegaweb.campus.data.ContractAccountApartmentBMPBean.getColumnValidFrom();
    String less = " <= ";
    String more = " >= ";
    /*
    1.  validfrom <= start and valid_to >= end
    2.  validfrom <= start and valid_to <= end
    3.  validfrom >= start and valid_to >= end
    4.  validfrom >= start and valid_to <= end

    i.  validfrom <= start and ((valid_to >= end ) or (valid_to <= end))
    ii. validfrom >= start and ((valid_to >= end ) or (valid_to <= end))

    or between
    */
    /*
    sql.append(" and (");
    sql.append("(").append(validfrom).append(less).append(start).append(" and ");
    sql.append("(( ").append(validto).append(more).append(end).append(") or (").append(validto).append(less).append(end).append(")) ) ");;
    sql.append(" or (").append(validfrom).append(more).append(start).append(" and ");
    sql.append("(( ").append(validto).append(more).append(end).append(") or (").append(validto).append(less).append(end).append(")) ) ");;
    sql.append(")");

      (begin <= valto && valto <= endin) ||
      (begin <= valfr && valfr <= endin) ||
      (begin <= valfr && valto <= endin) ||
      (valfr <= begin && endin <= valto)
    */
    sql.append(" and (");
    sql.append("(").append(start).append(less).append(validto).append(" and ").append(validto).append(less).append(end).append(")");
    sql.append(" or ");
    sql.append("(").append(start).append(less).append(validto).append(" and ").append(validto).append(less).append(end).append(")");
    sql.append(" or ");
    sql.append("(").append(start).append(less).append(validto).append(" and ").append(validto).append(less).append(end).append(")");
    sql.append(" or ");
    sql.append("(").append(validfrom).append(less).append(start).append(" and ").append(end).append(less).append(validto).append(")");
    sql.append(")");
    sql.append(" order by ic_user_id ");


    //System.err.println(sql);
    try {
      return EntityFinder.getInstance().findAll(ContractAccountApartment.class,sql.toString());
     }
     catch (com.idega.data.IDOFinderException ex) {
      ex.printStackTrace();
      return null;
     }
  }

   /**
   *  Returns a list of view entity ConatractAccounts
   *  that have legal contracts in period specified
   *  returns null if nothing found
   */
  public static List listOfContractAccounts(IWTimestamp startDate,IWTimestamp endDate){
    StringBuffer sql = new StringBuffer("select * from ").append(is.idega.idegaweb.campus.data.ContractAccountsBMPBean.getEntityTableName());
/*    sql.append(" where ");
    String start = "'"+startDate.toSQLString()+"'";
    String end = "'"+endDate.toSQLString()+"'";
    String validto = is.idega.idegaweb.campus.data.ContractAccountsBMPBean.getColumnValidTo();
    String validfrom = is.idega.idegaweb.campus.data.ContractAccountsBMPBean.getColumnValidFrom();
    String less = " <= ";
    String more = " >= ";
    /*
      (begin <= valto && valto <= endin) ||
      (begin <= valfr && valfr <= endin) ||
      (begin <= valfr && valto <= endin) ||
      (valfr <= begin && endin <= valto)
    */
/*    sql.append("(");
    sql.append("(").append(start).append(less).append(validto).append(" and ").append(validto).append(less).append(end).append(")");
    sql.append(" or ");
    sql.append("(").append(start).append(less).append(validto).append(" and ").append(validto).append(less).append(end).append(")");
    sql.append(" or ");
    sql.append("(").append(start).append(less).append(validto).append(" and ").append(validto).append(less).append(end).append(")");
    sql.append(" or ");
    sql.append("(").append(validfrom).append(less).append(start).append(" and ").append(end).append(less).append(validto).append(")");
    sql.append(")");*/
/*    sql.append(start);
    sql.append(" > ");
    sql.append(endDate);
    sql.append(" and ");
    sql.append(end)
    sql.append(" < ")*/
    
    
    //System.err.println(sql);
    try {
      return EntityFinder.getInstance().findAll(ContractAccounts.class,sql.toString());
     }
     catch (com.idega.data.IDOFinderException ex) {
      ex.printStackTrace();
      return null;
     }
  }

  public static List listOfRentingUserAccountsByType(String type){
   String sql = "select * from V_CONT_ACCT_APRT where fin_account_type = '"+type+"'";
   try {
     return EntityFinder.findAll(((is.idega.idegaweb.campus.data.ContractAccountApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(ContractAccountApartment.class)).createLegacy(),sql);
   }
   catch (SQLException ex) {
    ex.printStackTrace();
    return null;
   }
  }
  public static List listOfConAccAprtByType(int typeId){
    try {
      ContractAccountApartment CAA = ((is.idega.idegaweb.campus.data.ContractAccountApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(ContractAccountApartment.class)).createLegacy();
      return EntityFinder.findAllByColumn(((is.idega.idegaweb.campus.data.ContractAccountApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(ContractAccountApartment.class)).createLegacy(),is.idega.idegaweb.campus.data.ContractAccountApartmentBMPBean.getApartmentTypeIdColumnName(),typeId);
    }
    catch (SQLException ex) {
      return null;
    }
  }
  public static List listOfConAccAprtByApartment(int aprtId){
    try {
      ContractAccountApartment CAA = ((is.idega.idegaweb.campus.data.ContractAccountApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(ContractAccountApartment.class)).createLegacy();
      return EntityFinder.findAllByColumn(((is.idega.idegaweb.campus.data.ContractAccountApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(ContractAccountApartment.class)).createLegacy(),is.idega.idegaweb.campus.data.ContractAccountApartmentBMPBean.getApartmentIdColumnName(),aprtId);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfContractAccounts(){
   try {
     return EntityFinder.findAll(((is.idega.idegaweb.campus.data.ContractAccountsHome)com.idega.data.IDOLookup.getHomeLegacy(ContractAccounts.class)).createLegacy());
   }
   catch (SQLException ex) {
    ex.printStackTrace();
    return null;
   }
  }

  public static int countAccounts(String accType){
    String sql = "select count(*) from "+is.idega.idegaweb.campus.data.ContractAccountApartmentBMPBean.getEntityTableName() +" where "+is.idega.idegaweb.campus.data.ContractAccountApartmentBMPBean.getAccountTypeColumnName()+" = '"+accType+"'";
    int count = 0;
    //System.err.println(sql.toString());
    try{
      count = ((is.idega.idegaweb.campus.data.ContractAccountApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(ContractAccountApartment.class)).createLegacy().getNumberOfRecords(sql.toString());

    }
    catch(SQLException ex){ex.printStackTrace();}
    if(count < 0)
      count = 0;
    return count;
  }

  public static TariffIndex getTariffIndex(String type){
    TariffIndex ti = ((com.idega.block.finance.data.TariffIndexHome)com.idega.data.IDOLookup.getHomeLegacy(TariffIndex.class)).createLegacy();
    try {
      List L = EntityFinder.findAllByColumnDescendingOrdered(ti,com.idega.block.finance.data.TariffIndexBMPBean.getColumnNameType(),type,ti.getIDColumnName());
      if(L!= null)
        ti =  (TariffIndex) L.get(0);
      else
        ti =  null;
    }
    catch (SQLException ex) {
      ti = null;
    }
    return ti;
  }

  public static List getSSNAccounts(String ssn,String type){
    /*
      select f.fin_account_id
      from cam_contract c,fin_account f,app_applicant a
      where a.ssn = '0606795419'
      and a.app_applicant_id = c.app_applicant_id
      and f.ic_user_id = c.ic_user_id
      and f.account_type = 'FINANCE'
    */
    StringBuffer sql = new StringBuffer("select f.* " );
    sql.append(" from cam_contract c,fin_account f,app_applicant a " );
    sql.append(" where a.app_applicant_id = c.app_applicant_id ");
    sql.append(" and f.ic_user_id = c.ic_user_id ");
    sql.append(" and f.account_type = ").append(type);
    sql.append(" and a.ssn = '").append(ssn).append("'");

    try {
      return EntityFinder.getInstance().findAll(Account.class,sql.toString());
    }
    catch (IDOFinderException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static List getAccountEntryReport(int building_id,int account_key_id,java.sql.Timestamp from,java.sql.Timestamp to){
    StringBuffer sql = new StringBuffer(" select ");
    sql.append(" b.bu_building_id building_id, ");
    sql.append(" b.name building_name, ");
    sql.append(" k.fin_acc_key_id key_id, ");
    sql.append(" k.name key_name, ");
    sql.append(" k.info key_info, ");
    sql.append(" sum(e.total) total, ");
    sql.append(" count(acc.fin_account_id) number ");
    sql.append(" from ");
    sql.append(" bu_apartment a,bu_building b,bu_floor f, ");
    sql.append(" cam_contract c,fin_account acc,fin_acc_entry e,fin_acc_key k ");
    sql.append(" where b.bu_building_id = f.bu_building_id ");
    sql.append(" and f.bu_floor_id = a.bu_floor_id ");
    sql.append(" and a.bu_apartment_id = c.bu_apartment_id ");
    sql.append(" and c.ic_user_id = acc.ic_user_id ");
    sql.append(" and e.fin_account_id = acc.fin_account_id ");
    sql.append(" and k.fin_acc_key_id = e.fin_acc_key_id ");

    boolean and = false;

    if(building_id > 0){
      sql.append(" and ");
      sql.append(" b.bu_building_id ");
      sql.append( " = ");
      sql.append(building_id);
      and = true;
    }
    if(account_key_id > 0){
      sql.append(" and ");
      sql.append(" k.fin_acc_key_id ");
      sql.append(" = ").append(account_key_id);
    }
    if(from!=null){
      sql.append(" and e.payment_date >= '");
      sql.append(from.toString());
      sql.append("'");
    }
    if(to!=null){
      sql.append(" and e.payment_date <= '");
      sql.append(to.toString());
      sql.append("'");
    }



    sql.append(" group by b.bu_building_id,b.name,k.fin_acc_key_id,k.name,k.info ");
    sql.append(" order by b.bu_building_id ");


    //System.err.println(sql.toString());
     try {
      return EntityFinder.getInstance().findAll(BuildingAccountEntry.class,sql.toString());
      }
      catch (IDOFinderException ex) {
        ex.printStackTrace();
        System.err.println(sql.toString());
      }
      return null;
  }

}
