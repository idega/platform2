package is.idega.idegaweb.golf.entity;

import javax.ejb.*;

public interface LoginTable extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.lang.String getIDColumnName();
 public int getMemberId();
 public java.lang.String getUserLogin();
 public java.lang.String getUserPassword();
 public void setMemberId(int p0);
 public void setMemberId(java.lang.Integer p0);
 public void setUserLogin(java.lang.String p0);
 public void setUserPassword(java.lang.String p0);
}
