package is.idega.idegaweb.golf.entity;


public interface Address extends is.idega.idegaweb.golf.entity.GolfEntity
{
 public java.lang.String getAddressType();
 public is.idega.idegaweb.golf.entity.Country getCountry();
 public int getCountryId();
 public java.lang.String getExtraInfo();
 public is.idega.idegaweb.golf.entity.Member getMember()throws javax.ejb.FinderException;
 public java.lang.String getName();
 public java.lang.String getSeason();
 public java.lang.String getStreet();
 public java.lang.String getStreetNumber();
 public is.idega.idegaweb.golf.entity.ZipCode getZipCode()throws java.sql.SQLException;
 public int getZipcodeId();
 public void setAddressType(java.lang.String p0);
 public void setCountryId(java.lang.Integer p0);
 public void setCountryId(int p0);
 public void setDefaultValues();
 public void setExtraInfo(java.lang.String p0);
 public void setMember(is.idega.idegaweb.golf.entity.Member p0);
 public void setSeason(java.lang.String p0);
 public void setStreet(java.lang.String p0);
 public void setStreetNumber(int p0);
 public void setStreetNumber(java.lang.String p0);
 public void setZipcode(is.idega.idegaweb.golf.entity.ZipCode p0);
 public void setZipcodeId(int p0);
 public void setZipcodeId(java.lang.Integer p0);
}
