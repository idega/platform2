package is.idega.travel.business;

import java.sql.SQLException;
import com.idega.data.EntityControl;
import com.idega.util.idegaTimestamp;
import com.idega.data.SimpleQuerier;
import com.idega.block.trade.stockroom.data.Reseller;
import is.idega.travel.data.Inquery;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Inquirer {

  public Inquirer() {
  }

  public static int getNumberOfUnansweredInqueries(int productId, idegaTimestamp stamp) {
    return Inquirer.getNumberOfUnansweredInqueries(productId, stamp, -1);
  }

  public static int getNumberOfUnansweredInqueries(int productId, idegaTimestamp stamp, int resellerId) {
    int returner = 0;
    try {
      Inquery inq = (Inquery) Inquery.getStaticInstance(Inquery.class);
      Reseller res = (Reseller)Reseller.getStaticInstance(Reseller.class);
      String middleTable = EntityControl.getManyToManyRelationShipTableName(Inquery.class, Reseller.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT count(i."+inq.getIDColumnName()+") FROM "+Inquery.getInqueryTableName()+" i , "+Reseller.getResellerTableName()+" r, "+middleTable+" mi");
        buffer.append(" WHERE ");
        buffer.append("i."+inq.getIDColumnName()+" = mi."+inq.getIDColumnName());
        buffer.append(" AND ");
        buffer.append("m."+res.getIDColumnName()+" = mi."+res.getIDColumnName());
        buffer.append(" AND ");
        buffer.append("i."+inq.getAnsweredColumnName() +" = 'N'");
        buffer.append(" AND ");
        buffer.append("i."+inq.getServiceIDColumnName()+" = "+productId);
        buffer.append(" AND ");
        buffer.append("i."+inq.getInqueryDateColumnName() +" like '"+stamp.toSQLDateString()+"%'");
        if (resellerId != -1) {
          buffer.append(" AND ");
          buffer.append("m."+res.getIDColumnName()+" = "+resellerId);
        }
      String[] bufferReturn = SimpleQuerier.executeStringQuery(buffer.toString());
      if (bufferReturn != null)
        if (bufferReturn.length > 0) {
          returner = Integer.parseInt(bufferReturn[0]);
        }

    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return returner;
  }

  public static int getInqueredSeats(int serviceId, idegaTimestamp stamp, boolean unansweredOnly) {
    return Inquirer.getInqueredSeats(serviceId, stamp, -1, unansweredOnly);
  }

  /**
   * @todo Add resellerID supporti
   */
  public static int getInqueredSeats(int serviceId, idegaTimestamp stamp, int resellerId, boolean unansweredOnly) {
    int returner = 0;
    try {
      StringBuffer sql = new StringBuffer();
        sql.append("Select sum("+Inquery.getNumberOfSeatsColumnName()+") from "+Inquery.getInqueryTableName());
        sql.append(" WHERE ");
        sql.append(Inquery.getInqueryDateColumnName()+" = '"+stamp.toSQLDateString()+"'");
        sql.append(" AND ");
        sql.append(Inquery.getServiceIDColumnName()+" = "+serviceId);
        if (unansweredOnly) {
          sql.append(" AND ");
          sql.append(Inquery.getAnsweredColumnName()+" = 'N'");
        }

        String[] result = SimpleQuerier.executeStringQuery(sql.toString());
        if (result != null) {
          if (result.length > 0) {
            if (result[0] != null)
            returner = Integer.parseInt(result[0]);
          }
        }
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }

    return returner;
  }

  public static Inquery[] getInqueries(int serviceId, idegaTimestamp stamp, boolean unansweredOnly, String orderBy) {
    return Inquirer.getInqueries(serviceId, stamp, -1, unansweredOnly, orderBy);
  }
  /**
   * @todo Add resellerID supporti
   */
  public static Inquery[] getInqueries(int serviceId, idegaTimestamp stamp, int resellerId, boolean unansweredOnly, String orderBy) {
    Inquery[] inqueries = {};
    if (orderBy == null) orderBy = "";
    try {
      StringBuffer sql = new StringBuffer();
        sql.append("Select * from "+Inquery.getInqueryTableName());
        sql.append(" WHERE ");
        sql.append(Inquery.getInqueryDateColumnName()+" = '"+stamp.toSQLDateString()+"'");
        sql.append(" AND ");
        sql.append(Inquery.getServiceIDColumnName()+" = "+serviceId);
        if (unansweredOnly) {
          sql.append(" AND ");
          sql.append(Inquery.getAnsweredColumnName()+" = 'N'");
        }
        if (!orderBy.equals("")) {
          sql.append(" ORDER BY "+orderBy);
        }

      inqueries = (Inquery[]) (Inquery.getStaticInstance(Inquery.class)).findAll(sql.toString());
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return inqueries;
  }

  public static int sendInquery(String name,String email, idegaTimestamp inqueryDate, int productId, int numberOfSeats, int bookingId, Reseller reseller  ) throws SQLException {
    int returner = -1;
        Inquery inq = new Inquery();
          inq.setAnswered(false);
          inq.setEmail(email);
          inq.setInqueryDate(inqueryDate.getTimestamp());
          inq.setInquery("TEMP - IS available here ???");
          inq.setInqueryPostDate(idegaTimestamp.getTimestampRightNow());
          inq.setName(name);
          inq.setServiceID(productId);
          inq.setNumberOfSeats(numberOfSeats);
          inq.setBookingId(bookingId);
        inq.insert();

        if (reseller != null) {
          inq.addTo(reseller);
        }
      returner = inq.getID();
    return returner;
  }
}