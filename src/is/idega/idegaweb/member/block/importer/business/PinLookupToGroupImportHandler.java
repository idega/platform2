package is.idega.idegaweb.member.block.importer.business;

import com.idega.user.handler.plugin.UserPinLookupToGroupImportHandler;


public interface PinLookupToGroupImportHandler extends com.idega.business.IBOSession,com.idega.block.importer.business.ImportFileHandler, UserPinLookupToGroupImportHandler
{
 public java.util.List getFailedRecords() throws java.rmi.RemoteException;
 public boolean handleRecords()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setImportFile(com.idega.block.importer.data.ImportFile p0) throws java.rmi.RemoteException;
 public void setRootGroup(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
}
