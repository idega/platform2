package is.idega.idegaweb.travel.business;

import com.idega.data.SimpleQuerier;
import com.idega.util.idegaTimestamp;
import is.idega.idegaweb.travel.data.Contract;
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
    int temp;
    try {
      conn = ConnectionBroker.getConnection();
      for (int i = 0; i < contracts.length; i++) {
        temp = getNumberOfAssignedSeatsByContract(product.getID(), contracts[i].getResellerId(), stamp, contracts[i], conn);
        returner += temp;
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
            sql.append("Select sum("+is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameAlotment()+") from "+is.idega.idegaweb.travel.data.ContractBMPBean.getContractTableName());
            sql.append(" where ");
            sql.append(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId()+"="+serviceId);
            sql.append(" and ");
            sql.append(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameFrom()+" <= '"+stamp.toSQLDateString()+"'");
            sql.append(" and ");
            sql.append(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameTo()+" >= '"+stamp.toSQLDateString()+"'");
            if (resellerId != -1) {
              sql.append(" and ");
              sql.append(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId()+"="+resellerId);
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
      contracts = (Contract[]) (is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class)).findAllByColumn(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId(), Integer.toString(product.getID()) );
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return contracts;
  }

}
