package com.idega.block.trade.stockroom.data;


public interface VariantHome extends com.idega.data.IDOHome
{
 public Variant create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Variant findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}