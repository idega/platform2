package is.idega.idegaweb.golf.entity;

import javax.ejb.*;

public interface Poll_result extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.lang.String getAddress();
 public java.lang.String getName();
 public is.idega.idegaweb.golf.entity.Poll_option getOption();
 public int getOptionID();
 public void setOption(is.idega.idegaweb.golf.entity.Poll_option p0);
 public void setOption(int p0);
 public void setUserIPAddress(java.lang.String p0);
}
