/*
 * $Id: ImportBusiness.java 1.1 3.2.2005 gimmi Exp $
 * Created on 3.2.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.importer.business;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import javax.ejb.CreateException;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOService;
import com.idega.core.file.data.ICFile;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.business.GroupBusiness;


/**
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:gimmi@idega.com">gimmi</a>
 * @version $Revision: 1.1 $
 */
public interface ImportBusiness extends IBOService {

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#getImportHandlers
	 */
	public Collection getImportHandlers() throws RemoteException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#getImportFileTypes
	 */
	public Collection getImportFileTypes() throws RemoteException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#importRecords
	 */
	public boolean importRecords(String handlerClass, String fileClass, String filePath, Integer groupId,
			IWUserContext iwuc, List failedRecords) throws RemoteException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#importRecords
	 */
	public boolean importRecords(String handlerClass, String fileClass, String filePath, IWUserContext iwuc)
			throws RemoteException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#getGroupBusiness
	 */
	public GroupBusiness getGroupBusiness() throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#getImportFileHandler
	 */
	public ImportFileHandler getImportFileHandler(String handlerClass, IWUserContext iwuc) throws Exception,
			java.rmi.RemoteException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#getImportFile
	 */
	public ImportFile getImportFile(String fileClass) throws Exception, java.rmi.RemoteException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#getImportHandlers
	 */
	public DropdownMenu getImportHandlers(IWContext iwc, String name) throws RemoteException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#getImportFileClasses
	 */
	public DropdownMenu getImportFileClasses(IWContext iwc, String name) throws RemoteException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#getReportFolder
	 */
	public ICFile getReportFolder(String importFileName, boolean createIfNotFound) throws RemoteException,
			CreateException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#addReport
	 */
	public void addReport(File importFile, File reportFile) throws RemoteException, CreateException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#addReport
	 */
	public void addReport(File importFile, String name, Collection data, String separator) throws RemoteException,
			CreateException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#addExcelReport
	 */
	public void addExcelReport(File importFile, String name, Collection data, String separator) throws RemoteException,
			CreateException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#getReport
	 */
	public File getReport(String name, Collection data, String separator) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.importer.business.ImportBusinessBean#getExcelReport
	 */
	public File getExcelReport(String name, Collection data, String separator) throws java.rmi.RemoteException;
}
