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


  public static int getNumberOfAssignedSeatsByContract(int serviceId, idegaTimestamp stamp, Contract contract) {
    stamp.addDays(contract.getExpireDays());
    return getNumberOfAssignedSeats(serviceId, -1, stamp);
  }


  private static int getNumberOfAssignedSeatsByContract(int serviceId, idegaTimestamp stamp, Contract contract, Connection conn) {
    stamp.addDays(contract.getExpireDays());
    return getNumberOfAssignedSeats(serviceId, -1, stamp, conn);
  }


  public static int getNumberOfAssignedSeatsByContract(int serviceId, int resellerId, idegaTimestamp stamp, Contract contract, Connection conn) {
    stamp.addDays(contract.getExpireDays());
    System.err.println(serviceId + " : "+stamp.toSQLDateString()+ " : "+contract.getID()+" : "+getNumberOfAssignedSeats(serviceId, -1, stamp, conn));
    return getNumberOfAssignedSeats(serviceId, resellerId, stamp);
  }


  public static int getNumberOfAssignedSeats(Product product, idegaTimestamp stamp) {
    return getNumberOfAssignedSeats(product, -1, stamp);
  }


  public static int getNumberOfAssignedSeats(Product product, int resellerId, idegaTimestamp stamp) {
    int returner = 0;
    Contract[] contracts = getContracts(product);
    Connection conn= null;
    try {
      conn = ConnectionBroker.getConnection();
      for (int i = 0; i < contracts.length; i++) {
        returner += getNumberOfAssignedSeatsByContract(product.getID(), resellerId, stamp, contracts[i], conn);
      }
    }
    finally {
      if (conn != null){
          product.freeConnection(conn);
      }
    }
    return returner;
  }

  /**
   * @deprecated
   */
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