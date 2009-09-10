package com.idega.block.trade.stockroom.data;


public interface SettingsHome extends com.idega.data.IDOHome
{
 public Settings create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Settings findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public Settings create(com.idega.data.IDOLegacyEntity p0)throws javax.ejb.CreateException, java.rmi.RemoteException;

}