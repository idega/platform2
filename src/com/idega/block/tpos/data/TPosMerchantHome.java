package com.idega.block.tpos.data;


public interface TPosMerchantHome extends com.idega.data.IDOHome
{
 public TPosMerchant create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public TPosMerchant findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}