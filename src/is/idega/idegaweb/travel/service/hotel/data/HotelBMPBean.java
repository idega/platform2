package is.idega.idegaweb.travel.service.hotel.data;

import java.sql.*;


import com.idega.data.*;
import is.idega.idegaweb.travel.data.*;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class HotelBMPBean extends GenericEntity implements Hotel {

  public HotelBMPBean() {
    super();
  }

  public HotelBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName(),"Service_id",true,true,Integer.class,"one-to-one",Service.class);
    addAttribute(getNumberOfUnitsColumnName(), "Fjöldi eininga", true, true, Integer.class);
  }

  public String getEntityName() {
    return getHotelTableName();
  }

  public int getNumberOfUnits() {
    return getIntColumnValue(getNumberOfUnitsColumnName());
  }

  public void setNumberOfUnits(int units) {
    setColumn(getNumberOfUnitsColumnName(), units);
  }

  public static String getHotelTableName() {return "TB_ACCOMOTATION";}
  public static String getNumberOfUnitsColumnName() {return "NUMBER_OF_UNITS";}

  public void setPrimaryKey(Object object) {
    super.setPrimaryKey(object);
  }

}
