package is.idega.idegaweb.campus.block.finance.business;





import com.idega.util.idegaTimestamp;

import com.idega.util.idegaCalendar;

import java.sql.SQLException;

import java.util.StringTokenizer;

import java.util.List;

import java.util.Vector;

import com.idega.data.EntityFinder;

import com.idega.data.IDOFinderException;

import is.idega.idegaweb.campus.data.ContractAccountApartment;

import is.idega.idegaweb.campus.data.ContractAccounts;

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

  public static List listOfContractAccountApartment(String type,idegaTimestamp startDate,idegaTimestamp endDate){

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

  public static List listOfContractAccounts(idegaTimestamp startDate,idegaTimestamp endDate){

    StringBuffer sql = new StringBuffer("select * from ").append(is.idega.idegaweb.campus.data.ContractAccountsBMPBean.getEntityTableName());

    sql.append(" where ");

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

    sql.append("(");

    sql.append("(").append(start).append(less).append(validto).append(" and ").append(validto).append(less).append(end).append(")");

    sql.append(" or ");

    sql.append("(").append(start).append(less).append(validto).append(" and ").append(validto).append(less).append(end).append(")");

    sql.append(" or ");

    sql.append("(").append(start).append(less).append(validto).append(" and ").append(validto).append(less).append(end).append(")");

    sql.append(" or ");

    sql.append("(").append(validfrom).append(less).append(start).append(" and ").append(end).append(less).append(validto).append(")");

    sql.append(")");





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



  /*

  public Account findAccountFromApartmentSeries(String serie){



    //

    select ac.* from fin_account

    //

  }

  */



}

