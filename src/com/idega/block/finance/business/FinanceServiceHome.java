package com.idega.block.finance.business;


public interface FinanceServiceHome extends com.idega.business.IBOHome
{
 public FinanceService create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}