package com.idega.block.importer.business;
import java.rmi.RemoteException;
import java.util.List;

import com.idega.block.importer.data.ImportFile;
import com.idega.user.data.Group;

/**
 * <p>Title: ImportFileHandler</p>
 * <p>Description: An business interface for handling of classes implementing the ImportFile interface</p>
 * <p>Copyright: (c) 2002</p>
 * <p>Company: Idega Software</p>
 * @author <a href="mailto:eiki@idega.is">Eirikur Sveinn Hrafnsson</a>
 * @version 1.0 
 */

public interface ImportFileHandler {

public boolean handleRecords() throws RemoteException;
public void setImportFile(ImportFile file) throws RemoteException;
public void setRootGroup(Group rootGroup) throws RemoteException;
public List getFailedRecords() throws RemoteException;
}