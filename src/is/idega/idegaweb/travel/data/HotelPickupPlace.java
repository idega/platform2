package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface HotelPickupPlace extends com.idega.data.IDOLegacyEntity
{
 public void delete();
 public com.idega.core.data.Address getAddress();
 public java.lang.String getName();
 public void setAddress(com.idega.core.data.Address p0);
 public void setAddressId(int p0);
 public void setDefaultValues();
 public void setName(java.lang.String p0);
}
