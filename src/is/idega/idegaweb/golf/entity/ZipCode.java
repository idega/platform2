package is.idega.idegaweb.golf.entity;


public interface ZipCode extends GolfEntity
{
 public java.lang.String getCity();
 public java.lang.String getCode();
 public is.idega.idegaweb.golf.entity.Country getCountry();
 public int getCountryID();
 public java.lang.String getName();
 public void setCity(java.lang.String p0);
 public void setCode(java.lang.String p0);
 public void setCountry(is.idega.idegaweb.golf.entity.Country p0);
 public void setCountryID(int p0);
}
