//idega 2001 - Gimmi

package com.idega.projects.nat.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;

public class ServiceDayOfWeek extends GenericEntity{


  public ServiceDayOfWeek(){
          super();
  }
  public ServiceDayOfWeek(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getServiceIDColumnName(), "Vara", true, true, Integer.class, "many-to-one", Service.class);
    addAttribute(getDayOfWeekColumnName(), "Dagur", true, true, Integer.class);
  }


  public String getEntityName(){
    return getServiceDayOfWeekTableName();
  }

  public Service getService() {
    return (Service) getColumnValue(getServiceIDColumnName());
  }

  public int getServiceID() {
    return getIntColumnValue(getServiceIDColumnName());
  }

  public void setServiceID(int id) {
    setColumn(getServiceIDColumnName(), id);
  }


  public static String getServiceDayOfWeekTableName(){return "TB_SERVICE_DAY_OF_WEEK";}
  public static String getServiceIDColumnName(){return "TB_SERVICE_ID";}
  public static String getDayOfWeekColumnName(){return "DAY_OF_WEEK";}




}
