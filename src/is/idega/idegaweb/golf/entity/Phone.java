package is.idega.idegaweb.golf.entity;


public interface Phone extends com.idega.data.IDOLegacyEntity
{
 public is.idega.idegaweb.golf.entity.Country getCountry()throws java.sql.SQLException;
 public int getCountryId();
 public java.lang.String getNumber();
 public java.lang.String getPhoneType();
 public int getPhoneTypeId();
 public void setCountry(is.idega.idegaweb.golf.entity.Country p0);
 public void setCountryId(java.lang.Integer p0);
 public void setCountryId(int p0);
 public void setNumber(java.lang.String p0);
 public void setPhoneType(java.lang.String p0);
 public void setPhoneTypeId(int p0);
}
