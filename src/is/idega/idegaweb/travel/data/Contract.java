package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface Contract extends com.idega.data.IDOEntity
{
 public int getAlotment() throws java.rmi.RemoteException;
 public java.lang.String getDiscount() throws java.rmi.RemoteException;
 public int getExpireDays() throws java.rmi.RemoteException;
 public java.sql.Timestamp getFrom() throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Reseller getReseller()throws java.sql.SQLException, java.rmi.RemoteException;
 public int getResellerId() throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.Service getService()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int getServiceId() throws java.rmi.RemoteException;
 public java.sql.Timestamp getTo() throws java.rmi.RemoteException;
 public void setAlotment(int p0) throws java.rmi.RemoteException;
 public void setDiscount(java.lang.String p0) throws java.rmi.RemoteException;
 public void setExpireDays(int p0) throws java.rmi.RemoteException;
 public void setFrom(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setReseller(com.idega.block.trade.stockroom.data.Reseller p0) throws java.rmi.RemoteException;
 public void setResellerId(int p0) throws java.rmi.RemoteException;
 public void setService(is.idega.idegaweb.travel.data.Service p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setServiceId(int p0) throws java.rmi.RemoteException;
 public void setTo(java.sql.Timestamp p0) throws java.rmi.RemoteException;
}
