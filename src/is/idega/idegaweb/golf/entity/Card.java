package is.idega.idegaweb.golf.entity;


public interface Card extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.lang.String getCardCompany();
 public java.lang.String getCardNumber();
 public java.lang.String getCardType();
 public java.sql.Date getExpireDate();
 public java.lang.String getName();
 public java.lang.String getSocialSecurityNumber();
 public void setCardCompany(java.lang.String p0);
 public void setCardNumber(java.lang.String p0);
 public void setCardType(java.lang.String p0);
 public void setExpireDate(java.sql.Date p0);
 public void setName(java.lang.String p0);
 public void setSocialSecurityNumber(java.lang.String p0);
}
