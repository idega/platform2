package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface ResellerDay extends com.idega.data.IDOLegacyEntity
{
 public int getDayOfWeek();
 public java.lang.String getIDColumnName();
 public int getResellerId();
 public int getServiceId();
 public void setDayOfWeek(int p0,int p1,int p2);
 public void setDayOfWeek(int p0);
 public void setResellerId(int p0);
 public void setServiceId(int p0);
}
