package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface Contract extends com.idega.data.IDOLegacyEntity
{
 public int getAlotment();
 public java.lang.String getDiscount();
 public int getExpireDays();
 public java.sql.Timestamp getFrom();
 public com.idega.block.trade.stockroom.data.Reseller getReseller()throws java.sql.SQLException;
 public int getResellerId();
 public is.idega.idegaweb.travel.data.Service getService()throws java.sql.SQLException;
 public int getServiceId();
 public java.sql.Timestamp getTo();
 public void setAlotment(int p0);
 public void setDiscount(java.lang.String p0);
 public void setExpireDays(int p0);
 public void setFrom(java.sql.Timestamp p0);
 public void setReseller(com.idega.block.trade.stockroom.data.Reseller p0);
 public void setResellerId(int p0);
 public void setService(is.idega.idegaweb.travel.data.Service p0);
 public void setServiceId(int p0);
 public void setTo(java.sql.Timestamp p0);
}
