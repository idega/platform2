package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface ServiceDay extends com.idega.data.IDOLegacyEntity
{
 public int getDayOfWeek();
 public int getEstimated();
 public java.lang.String getIDColumnName();
 public int getMax();
 public int getMin();
 public java.util.Collection getServiceDays(int p0)throws javax.ejb.FinderException;
 public int getServiceId();
 public void setDayOfWeek(int p0,int p1);
 public void setDayOfWeek(int p0);
 public void setEstimated(int p0);
 public void setMax(int p0);
 public void setMin(int p0);
 public void setServiceId(int p0);
}
