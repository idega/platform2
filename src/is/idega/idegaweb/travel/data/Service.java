package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface Service extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public com.idega.core.data.Address getAddress()throws java.sql.SQLException;
 public com.idega.core.data.Address[] getAddresses()throws java.sql.SQLException;
 public java.sql.Timestamp getArrivalTime();
 public java.sql.Timestamp getDepartureTime();
 public java.lang.String getDescription();
 public java.lang.String getName();
 public com.idega.block.trade.stockroom.data.Product getProduct();
 public void setAttivalTime(java.sql.Timestamp p0);
 public void setDefaultValues();
 public void setDepartureTime(java.sql.Timestamp p0);
}
