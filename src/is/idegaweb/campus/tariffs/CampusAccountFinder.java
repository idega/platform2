
package is.idegaweb.campus.tariffs;

import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;
import com.idega.data.EntityFinder;
import is.idegaweb.campus.entity.ContractAccountApartment;
import is.idegaweb.campus.entity.TariffIndex;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusAccountFinder  {

  public static List listOfRentingUserAccounts(){
   try {
     return EntityFinder.findAll(new ContractAccountApartment());
   }
   catch (SQLException ex) {
    return null;
   }
  }

  public static List listOfRentingUserAccountsByType(String type){
   try {
     return EntityFinder.findAllByColumn(new ContractAccountApartment(),ContractAccountApartment.getApartmentTypeIdColumnName(),type);
   }
   catch (SQLException ex) {
    return null;
   }
  }
  public static List listOfConAccAprtByType(int typeId){
    try {
      ContractAccountApartment CAA = new ContractAccountApartment();
      return EntityFinder.findAllByColumn(new ContractAccountApartment(),CAA.getApartmentTypeIdColumnName(),typeId);
    }
    catch (SQLException ex) {
      return null;
    }
  }
  public static List listOfConAccAprtByApartment(int aprtId){
    try {
      ContractAccountApartment CAA = new ContractAccountApartment();
      return EntityFinder.findAllByColumn(new ContractAccountApartment(),CAA.getApartmentIdColumnName(),aprtId);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static int countAccounts(){
    String sql = "select count(*) from "+ContractAccountApartment.getEntityTableName();
    int count = 0;
    try{
      count = new ContractAccountApartment().getNumberOfRecords(sql.toString());
    }
    catch(SQLException ex){}
    if(count < 0)
      count = 0;
    return count;
  }

  public static TariffIndex getTariffIndex(String type){
    TariffIndex ti = new TariffIndex();
    try {
      List L = EntityFinder.findAllByColumnDescendingOrdered(ti,ti.getColumnNameType(),type,ti.getColumnNameDate());
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

  /*
  public Account findAccountFromApartmentSeries(String serie){

    //
    select ac.* from fin_account
    //
  }
  */

}