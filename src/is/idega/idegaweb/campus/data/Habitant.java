package is.idega.idegaweb.campus.data;


public interface Habitant extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.lang.String getAddress();
 public java.lang.String getApartment();
 public int getComplexId();
 public int getContractId();
 public java.lang.String getFloor();
 public java.lang.String getFullName();
 public java.lang.String getPhoneNumber();
 public int getUserId();
}
