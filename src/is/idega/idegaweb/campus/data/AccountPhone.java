package is.idega.idegaweb.campus.data;


public interface AccountPhone extends com.idega.data.IDOEntity
{
 public java.lang.Integer getAccountId();
 public java.lang.String getPhoneNumber();
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
 public void setValidFrom(java.sql.Date p0);
 public void setValidTo(java.sql.Date p0);
}
