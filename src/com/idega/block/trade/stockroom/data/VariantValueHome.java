package com.idega.block.trade.stockroom.data;


public interface VariantValueHome extends com.idega.data.IDOHome
{
 public VariantValue create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public VariantValue findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}