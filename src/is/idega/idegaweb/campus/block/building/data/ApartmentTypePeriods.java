package is.idega.idegaweb.campus.block.building.data;


public interface ApartmentTypePeriods extends com.idega.data.IDOEntity
{
 public int getApartmentTypeId();
 public java.sql.Date getFirstDate();
 public int getFirstDateDay();
 public int getFirstDateMonth();
 public java.lang.String getName();
 public java.sql.Date getSecondDate();
 public int getSecondDateDay();
 public int getSecondDateMonth();
 public boolean hasFirstPeriod();
 public boolean hasSecondPeriod();
 public void setApartmentTypeId(int p0);
 public void setApartmentTypeId(java.lang.Integer p0);
 public void setFirstDate(java.sql.Date p0);
 public void setFirstDate(int p0,int p1);
 public void setName(java.lang.String p0);
 public void setSecondDate(java.sql.Date p0);
 public void setSecondDate(int p0,int p1);
}
