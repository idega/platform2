package com.idega.block.importer.business;


public interface ImportBusiness extends com.idega.business.IBOService
{
 public com.idega.user.business.GroupBusiness getGroupBusiness()throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.block.importer.data.ImportFile getImportFile(java.lang.String p0)throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.block.importer.business.ImportFileHandler getImportFileHandler(java.lang.String p0,com.idega.idegaweb.IWUserContext p1)throws java.lang.Exception, java.rmi.RemoteException;
 public java.util.Collection getImportFileTypes()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getImportHandlers()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean importRecords(java.lang.String p0,java.lang.String p1,java.lang.String p2,com.idega.idegaweb.IWUserContext p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean importRecords(java.lang.String p0,java.lang.String p1,java.lang.String p2,java.lang.Integer p3,com.idega.idegaweb.IWUserContext p4)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.ui.DropdownMenu getImportHandlers(com.idega.presentation.IWContext p0, String p1) throws java.rmi.RemoteException;
 public com.idega.presentation.ui.DropdownMenu getImportFileClasses(com.idega.presentation.IWContext p0, String p1) throws java.rmi.RemoteException;
}
