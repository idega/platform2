package com.idega.block.contract.business;


public interface ContractServiceHome extends com.idega.business.IBOHome
{
 public ContractService create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}