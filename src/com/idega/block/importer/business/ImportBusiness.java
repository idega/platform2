package com.idega.block.importer.business;

import javax.ejb.*;

public interface ImportBusiness extends com.idega.business.IBOService
{
 public com.idega.block.importer.business.ImportFileHandler getHandlerForImportFile(java.lang.String p0) throws java.rmi.RemoteException,java.lang.ClassNotFoundException;
 public com.idega.block.importer.business.ImportFileHandler getHandlerForImportFile(java.lang.Class p0) throws java.rmi.RemoteException;
 public boolean importRecords(com.idega.block.importer.data.ImportFile p0) throws java.rmi.RemoteException;
 public boolean importRecords(com.idega.user.data.Group,com.idega.block.importer.data.ImportFile p0) throws java.rmi.RemoteException;
  
}
