package com.idega.block.basket.business;


public interface BasketBusiness extends com.idega.business.IBOSession
{
 public void addItem(com.idega.block.basket.data.BasketItem p0) throws java.rmi.RemoteException;
 public void addItem(com.idega.block.basket.data.BasketItem p0,int p1) throws java.rmi.RemoteException;
 public void removeItem(com.idega.block.basket.data.BasketItem p0) throws java.rmi.RemoteException;
}
