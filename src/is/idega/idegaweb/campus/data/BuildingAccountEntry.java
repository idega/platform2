package is.idega.idegaweb.campus.data;


public interface BuildingAccountEntry extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getKeyName();
 public int getKeyId();
 public int getBuildingId();
 public float getTotal();
 public java.lang.String getBuildingName();
 public void delete()throws java.sql.SQLException;
 public java.lang.String getKeyInfo();
 public int getNumber();
}
