package is.idega.travel.business;

import com.idega.data.SimpleQuerier;
import com.idega.util.idegaTimestamp;
import is.idega.travel.data.Contract;
import com.idega.block.trade.stockroom.data.Product;
import java.sql.SQLException;
import java.sql.Connection;
import com.idega.util.database.ConnectionBroker;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Assigner {

  public Assigner() {
  }

  /**
   * @deprecated
   */
  public static int getNumberOfAssignedSeats(int serviceId, idegaTimestamp stamp) {
    return getNumberOfAssignedSeats(serviceId, -1, stamp);
  }


  /**
   * @deprecated
   */
  public static int getNumberOfAssignedSeatsByContract(int serviceId, idegaTimestamp stamp, Contract contract) {
    idegaTimestamp theStamp= idegaTimestamp.RightNow();
      theStamp.addDays(contract.getExpireDays()-1);
    if (stamp.isLaterThan(theStamp)) {
      return getNumberOfAssignedSeats(serviceId, -1, stamp);
    }else {
      return 0;
    }
  }


  /**
   * @deprecated
   */
  private static int getNumberOfAssignedSeatsByContract(int serviceId, idegaTimestamp stamp, Contract contract, Connection conn) {
    idegaTimestamp theStamp= idegaTimestamp.RightNow();
      theStamp.addDays(contract.getExpireDays()-1);
    if (stamp.isLaterThan(theStamp)) {
      return getNumberOfAssignedSeats(serviceId, -1, stamp, conn);
    }else {
      return 0;
    }
  }



  public static int getNumberOfAssignedSeatsByContract(int serviceId, int resellerId, idegaTimestamp stamp, Contract contract, Connection conn) {
    idegaTimestamp theStamp= idegaTimestamp.RightNow();
      theStamp.addDays(contract.getExpireDays()-1);
    if (stamp.isLaterThan(theStamp)) {
      return getNumberOfAssignedSeats(serviceId, resellerId, stamp, conn);
    }else {
      return 0;
    }

  }



  public static int getNumberOfAssignedSeats(Product product, idegaTimestamp stamp) {
    int returner = 0;
    Contract[] contracts = getContracts(product);
    Connection conn= null;
    try {
      conn = ConnectionBroker.getConnection();
      for (int i = 0; i < contracts.length; i++) {
        returner += getNumberOfAssignedSeatsByContract(product.getID(), contracts[i].getResellerId(), stamp, contracts[i], conn);
      }
    }
    finally {
      if (conn != null){
          product.freeConnection(conn);
      }
    }
    return returner;
  }

  public static int getNumberOfAssignedSeats(int serviceId,int resellerId, idegaTimestamp stamp) {
    return getNumberOfAssignedSeats(serviceId, resellerId, stamp, null);
  }

  private static int getNumberOfAssignedSeats(int serviceId,int resellerId, idegaTimestamp stamp, Connection conn) {
    int returner = 0;
    try {
        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select sum("+Contract.getColumnNameAlotment()+") from "+Contract.getContractTableName());
            sql.append(" where ");
            sql.append(Contract.getColumnNameServiceId()+"="+serviceId);
            sql.append(" and ");
            sql.append(Contract.getColumnNameFrom()+" <= '"+stamp.toSQLDateString()+"'");
            sql.append(" and ");
            sql.append(Contract.getColumnNameTo()+" >= '"+stamp.toSQLDateString()+"'");
            if (resellerId != -1) {
              sql.append(" and ");
              sql.append(Contract.getColumnNameResellerId()+"="+resellerId);
            }

        if (conn != null) {
          many = SimpleQuerier.executeStringQuery(sql.toString(), conn);
        }else {
          many = SimpleQuerier.executeStringQuery(sql.toString());
        }

        if (many != null) {
          if (many[0] != null)
          returner += Integer.parseInt(many[0]);
        }
    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }


  public static Contract[] getContracts(Product product) {
    Contract[] contracts = {};
    try {
      contracts = (Contract[]) (Contract.getStaticInstance(Contract.class)).findAllByColumn(Contract.getColumnNameServiceId(), Integer.toString(product.getID()) );
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return contracts;
  }

}