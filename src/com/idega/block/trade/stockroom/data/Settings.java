package com.idega.block.trade.stockroom.data;

import javax.ejb.*;

public interface Settings extends com.idega.data.IDOEntity
{
 public boolean getIfDoubleConfirmation() throws java.rmi.RemoteException;
 public boolean getIfEmailAfterOnlineBooking() throws java.rmi.RemoteException;
 public void setDefaultValues() throws java.rmi.RemoteException;
 public void setIfDoubleConfirmation(boolean p0) throws java.rmi.RemoteException;
 public void setIfEmailAfterOnlineBooking(boolean p0) throws java.rmi.RemoteException;
}
