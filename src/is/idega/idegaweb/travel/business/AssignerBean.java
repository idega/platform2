package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.data.ContractHome;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;
import com.idega.util.IWTimestamp;
import com.idega.util.database.ConnectionBroker;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class AssignerBean extends IBOServiceBean implements Assigner{

  public AssignerBean() {
  }

  public int getNumberOfAssignedSeats(int serviceId, IWTimestamp stamp) {
    return getNumberOfAssignedSeats(serviceId, -1, stamp);
  }


  public int getNumberOfAssignedSeatsByContract(int serviceId, int resellerId, IWTimestamp stamp, Contract contract, Connection conn) throws RemoteException {
    IWTimestamp theStamp= IWTimestamp.RightNow();
      theStamp.addDays(contract.getExpireDays()-1);
    if (stamp.isLaterThan(theStamp)) {
      return getNumberOfAssignedSeats(serviceId, resellerId, stamp, conn);
    }else {
      return 0;
    }

  }



  public int getNumberOfAssignedSeats(Product product, IWTimestamp stamp) throws RemoteException{
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
          ConnectionBroker.freeConnection(conn);
      }
    }
    return returner;
  }

  public int getNumberOfAssignedSeats(int serviceId,int resellerId, IWTimestamp stamp) {
    return getNumberOfAssignedSeats(serviceId, resellerId, stamp, null);
  }

  private int getNumberOfAssignedSeats(int serviceId,int resellerId, IWTimestamp stamp, Connection conn) {
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

        if (many != null && many.length > 0) {
          if (many[0] != null)
          returner += Integer.parseInt(many[0]);
        }
    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }


  // TODO change this to return Collection
  public Contract[] getContracts(Product product) throws RemoteException{
    Contract[] contracts = {};
    try {
    	ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
    	Collection coll = cHome.findByProductId(product.getID());
      //contracts = (Contract[]) (is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class)).findAllByColumn(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId(), Integer.toString(product.getID()) );
      if (coll != null && !coll.isEmpty()) {
      	contracts = new Contract[coll.size()];
      	Iterator iter = coll.iterator();
      	for ( int i = 0; i < contracts.length; i++) {
      		contracts[i] = (Contract) iter.next();
      	}
      }
    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }
    return contracts;
  }

}
