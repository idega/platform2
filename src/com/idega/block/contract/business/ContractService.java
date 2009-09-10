package com.idega.block.contract.business;


public interface ContractService extends com.idega.business.IBOService
{
 public com.idega.block.contract.data.Contract createAndPrintContract(int p0,int p1)throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.contract.data.ContractCategoryHome getContractCategoryHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.contract.data.ContractHome getContractHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public boolean removeContractFile(int p0,int p1) throws java.rmi.RemoteException;
}
