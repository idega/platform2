package is.idega.idegaweb.campus.data;

import javax.ejb.*;

public interface AccountPhone extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.lang.Integer getAccountId();
 public java.sql.Timestamp getDeliverTime();
 public java.lang.String getPhoneNumber();
 public java.sql.Timestamp getReturnTime();
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
 public void setValidFrom(java.sql.Date p0);
 public void setValidTo(java.sql.Date p0);
}
