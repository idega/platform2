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

  public static int getInqueredSeats(int serviceId, idegaTimestamp stamp, boolean unansweredOnly) {
    return Inquirer.getInqueredSeats(serviceId, stamp, -1, unansweredOnly);
  }

  public static int getInqueredSeats(int serviceId, idegaTimestamp stamp, int resellerId, boolean unansweredOnly) {
    int returner = 0;
    try {
      Inquery inq = (Inquery) Inquery.getStaticInstance(Inquery.class);
      Reseller res = (Reseller)Reseller.getStaticInstance(Reseller.class);
      String middleTable = EntityControl.getManyToManyRelationShipTableName(Inquery.class, Reseller.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT sum(i."+inq.getNumberOfSeatsColumnName()+") FROM "+Inquery.getInqueryTableName()+" i , "+Reseller.getResellerTableName()+" r, "+middleTable+" mi");
        buffer.append(" WHERE ");
        buffer.append("i."+inq.getIDColumnName()+" = mi."+inq.getIDColumnName());
        buffer.append(" AND ");
        buffer.append("r."+res.getIDColumnName()+" = mi."+res.getIDColumnName());

        if (unansweredOnly) {
        buffer.append(" AND ");
        buffer.append("i."+inq.getAnsweredColumnName() +" = 'N'");
        }

        buffer.append(" AND ");
        buffer.append("i."+inq.getServiceIDColumnName()+" = "+serviceId);
        buffer.append(" AND ");
        buffer.append("i."+inq.getInqueryDateColumnName() +" like '"+stamp.toSQLDateString()+"%'");
        if (resellerId != -1) {
          buffer.append(" AND ");
          buffer.append("r."+res.getIDColumnName()+" = "+resellerId);
        }
      String[] bufferReturn = SimpleQuerier.executeStringQuery(buffer.toString());
      if (bufferReturn != null)
        if (bufferReturn.length > 0) {
          if (bufferReturn[0] != null)
          returner = Integer.parseInt(bufferReturn[0]);
        }

    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return returner;
  }


  public static Inquery[] getInqueries(int serviceId, idegaTimestamp stamp, boolean unansweredOnly) {
    return Inquirer.getInqueries(serviceId, stamp, -1, unansweredOnly, Inquery.getInqueryDateColumnName());
  }


  public static Inquery[] getInqueries(int serviceId, idegaTimestamp stamp, boolean unansweredOnly, String orderBy) {
    return Inquirer.getInqueries(serviceId, stamp, -1, unansweredOnly, orderBy);
  }


  public static Inquery[] getInqueries(int serviceId, idegaTimestamp stamp, int resellerId, boolean unansweredOnly, String orderBy) {
    Inquery[] inqueries = {};
    if (orderBy == null) orderBy = "";
    try {
      Inquery inq = (Inquery) Inquery.getStaticInstance(Inquery.class);
      Reseller res = (Reseller)Reseller.getStaticInstance(Reseller.class);
      String middleTable = EntityControl.getManyToManyRelationShipTableName(Inquery.class, Reseller.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT i.* FROM "+Inquery.getInqueryTableName()+" i , "+Reseller.getResellerTableName()+" r, "+middleTable+" mi");
        buffer.append(" WHERE ");
        buffer.append("i."+inq.getIDColumnName()+" = mi."+inq.getIDColumnName());
        buffer.append(" AND ");
        buffer.append("r."+res.getIDColumnName()+" = mi."+res.getIDColumnName());

        if (unansweredOnly) {
        buffer.append(" AND ");
        buffer.append("i."+inq.getAnsweredColumnName() +" = 'N'");
        }

        buffer.append(" AND ");
        buffer.append("i."+inq.getServiceIDColumnName()+" = "+serviceId);
        buffer.append(" AND ");
        buffer.append("i."+inq.getInqueryDateColumnName() +" like '"+stamp.toSQLDateString()+"%'");
        if (resellerId != -1) {
          buffer.append(" AND ");
          buffer.append("r."+res.getIDColumnName()+" = "+resellerId);
        }

        if (!orderBy.equals("")) {
          buffer.append(" ORDER BY "+orderBy);
        }

      inqueries = (Inquery[]) (Inquery.getStaticInstance(Inquery.class)).findAll(buffer.toString());
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return inqueries;
  }

  public static int sendInquery(String name,String email, idegaTimestamp inqueryDate, int productId, int numberOfSeats, int bookingId, Reseller reseller  ) throws SQLException {
    String sInquery = "TEMP - IS available here ???";


    int returner = -1;
        Inquery inq = new Inquery();
          inq.setAnswered(false);
          inq.setEmail(email);
          inq.setInqueryDate(inqueryDate.getTimestamp());
          inq.setInquery(sInquery);
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