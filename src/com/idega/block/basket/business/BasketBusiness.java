package com.idega.block.basket.business;


public interface BasketBusiness extends com.idega.business.IBOSession
{
 public void addExtraInfo(java.lang.Object p0) throws java.rmi.RemoteException;
 public void addItem(com.idega.block.basket.data.BasketItem p0,int p1) throws java.rmi.RemoteException;
 public void addItem(com.idega.block.basket.data.BasketItem p0) throws java.rmi.RemoteException;
 public void changeQuantity(com.idega.block.basket.data.BasketItem p0,int p1) throws java.rmi.RemoteException;
 public boolean checkForItemInBasket(com.idega.block.basket.data.BasketItem p0) throws java.rmi.RemoteException;
 public void checkout(com.idega.block.basket.business.BasketCheckoutBusiness p0) throws java.rmi.RemoteException;
 public void emptyBasket() throws java.rmi.RemoteException;
 public void emtpyExtraInfo() throws java.rmi.RemoteException;
 public java.util.Map getBasket() throws java.rmi.RemoteException;
 public java.util.List getExtraInfo() throws java.rmi.RemoteException;
 public void removeExtraInfo(java.lang.Object p0) throws java.rmi.RemoteException;
 public void removeItem(com.idega.block.basket.data.BasketItem p0) throws java.rmi.RemoteException;
}
