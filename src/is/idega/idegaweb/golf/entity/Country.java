package is.idega.idegaweb.golf.entity;

import javax.ejb.*;

public interface Country extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.lang.String getAbbrevation();
 public int getAccessNumber();
 public java.lang.String getInternetSuffix();
 public java.lang.String getName();
 public void setAbbrevation(java.lang.String p0);
 public void setAccessNumber(java.lang.Integer p0);
 public void setInternetSuffix(java.lang.String p0);
 public void setName(java.lang.String p0);
}
