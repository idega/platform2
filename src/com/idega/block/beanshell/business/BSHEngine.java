package com.idega.block.beanshell.business;


public interface BSHEngine extends com.idega.business.IBOService
{
 public java.lang.String getBshVersion() throws java.rmi.RemoteException;
 public java.lang.Object runScript(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.Object runScript(java.lang.String p0,com.idega.presentation.IWContext p1) throws java.rmi.RemoteException;
}
