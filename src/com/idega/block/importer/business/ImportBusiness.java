package com.idega.block.importer.business;

import java.util.Collection;
import javax.ejb.*;

public interface ImportBusiness extends com.idega.business.IBOService
{
/**
 * Method getImportHandlers used to get a list of all import handlers registered in the database.
 * @return A Collection of ImportHandler Beans or null if none was found.
 * @throws RemoteException
 */
 public Collection getImportHandlers() throws java.rmi.RemoteException;
/**
 * Method getImportFileTypes used to get a list of all import file types registered in the database.
 * @return A Collection of ImportFileClass Beans or null if none was found.
 * @throws RemoteException
 */
 public Collection getImportFileTypes() throws java.rmi.RemoteException;

	/**
	 * Method importRecords.
	 * @param handlerClass
	 * @param fileClass
	 * @param filePath
	 * @param groupId
	 * @return boolean
	 */
	boolean importRecords(String handlerClass,String fileClass,String filePath,Integer groupId) throws java.rmi.RemoteException;
	
	/**
	 * Method importRecords.
	 * @param handlerClass
	 * @param fileClass
	 * @param filePath
	 * @return boolean
	 */
	boolean importRecords(String handlerClass,String fileClass,String filePath) throws java.rmi.RemoteException;

}
