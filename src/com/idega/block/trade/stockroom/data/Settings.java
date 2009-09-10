package com.idega.block.trade.stockroom.data;


public interface Settings extends com.idega.data.IDOEntity
{
 public int getCurrencyId() throws java.rmi.RemoteException;
 public boolean getIfDoubleConfirmation() throws java.rmi.RemoteException;
 public boolean getIfEmailAfterOnlineBooking() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void setCurrencyId(int p0) throws java.rmi.RemoteException;
 public void setIfDoubleConfirmation(boolean p0) throws java.rmi.RemoteException;
 public void setIfEmailAfterOnlineBooking(boolean p0) throws java.rmi.RemoteException;
}
