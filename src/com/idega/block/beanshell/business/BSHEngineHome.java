package com.idega.block.beanshell.business;


public interface BSHEngineHome extends com.idega.business.IBOHome
{
 public BSHEngine create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}