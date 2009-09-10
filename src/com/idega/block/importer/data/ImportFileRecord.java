package com.idega.block.importer.data;

import com.idega.core.file.data.ICFile;

public interface ImportFileRecord extends ICFile
{
 public boolean hasBeenImported() throws java.rmi.RemoteException;
 public void setAsNotImported() throws java.rmi.RemoteException;
 public void setAsImported() throws java.rmi.RemoteException;
}
