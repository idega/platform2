package is.idega.travel.business;

import com.idega.data.SimpleQuerier;
import com.idega.util.idegaTimestamp;
import is.idega.travel.data.Contract;
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

  public static int getNumberOfAssignedSeats(int serviceId,int resellerId, idegaTimestamp stamp) {
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

        many = SimpleQuerier.executeStringQuery(sql.toString());

        if (many != null) {
          if (many[0] != null)
          returner += Integer.parseInt(many[0]);
        }
    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }

}