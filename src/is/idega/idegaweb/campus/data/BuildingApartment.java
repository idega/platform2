package is.idega.idegaweb.campus.data;

import javax.ejb.*;

public interface BuildingApartment extends com.idega.data.IDOLegacyEntity
{
 public int getApartmentId();
 public java.lang.String getApartmentName();
 public int getBuildingId();
 public java.lang.String getBuildingName();
}
