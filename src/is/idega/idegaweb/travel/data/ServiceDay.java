package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface ServiceDay extends com.idega.data.IDOLegacyEntity
{
 public int getDayOfWeek();
 public java.lang.String getIDColumnName();
 public int getServiceId();
 public void setDayOfWeek(int p0);
 public void setDayOfWeek(int p0,int p1);
 public void setServiceId(int p0);
}
