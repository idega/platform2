package is.idega.idegaweb.campus.block.building.data;


public interface ApartmentTypeRent extends com.idega.data.IDOEntity
{
 public int getApartmentTypeId();
 public float getRent();
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
 public void initializeAttributes();
 public void setApartmentTypeId(int p0);
 public void setApartmentTypeId(java.lang.Integer p0);
 public void setRent(java.lang.Float p0);
 public void setValidFrom(java.sql.Date p0);
 public void setValidTo(java.sql.Date p0);
}
