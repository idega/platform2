/*
 * Created on Sep 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.idega.block.importer.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;


import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOService;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.business.GroupBusiness;

/**
 * @author IBM
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ImportBusiness extends IBOService {
    /**
     * @see com.idega.block.importer.business.ImportBusinessBean#getImportHandlers
     */
    public Collection getImportHandlers() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.importer.business.ImportBusinessBean#getImportFileTypes
     */
    public Collection getImportFileTypes() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.importer.business.ImportBusinessBean#importRecords
     */
    public boolean importRecords(String handlerClass, String fileClass,
            String filePath, Integer groupId, IWUserContext iwuc,
            List failedRecords) throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.importer.business.ImportBusinessBean#importRecords
     */
    public boolean importRecords(String handlerClass, String fileClass,
            String filePath, IWUserContext iwuc) throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.importer.business.ImportBusinessBean#getGroupBusiness
     */
    public GroupBusiness getGroupBusiness() throws Exception,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.importer.business.ImportBusinessBean#getImportFileHandler
     */
    public ImportFileHandler getImportFileHandler(String handlerClass,
            IWUserContext iwuc) throws Exception, java.rmi.RemoteException;

    /**
     * @see com.idega.block.importer.business.ImportBusinessBean#getImportFile
     */
    public ImportFile getImportFile(String fileClass) throws Exception,
            java.rmi.RemoteException;

    /**
     * @see com.idega.block.importer.business.ImportBusinessBean#getImportHandlers
     */
    public DropdownMenu getImportHandlers(IWContext iwc, String name)
            throws RemoteException, java.rmi.RemoteException;

    /**
     * @see com.idega.block.importer.business.ImportBusinessBean#getImportFileClasses
     */
    public DropdownMenu getImportFileClasses(IWContext iwc, String name)
            throws RemoteException, java.rmi.RemoteException;

}
