/*
 * $Id: GolfImportHandler.java,v 1.1 2004/10/12 14:52:33 eiki Exp $
 * Created on Oct 11, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.importer.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOSession;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/10/12 14:52:33 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.1 $
 */
public interface GolfImportHandler extends IBOSession, ImportFileHandler, UserGroupPlugInBusiness {

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#handleRecords
	 */
	public boolean handleRecords() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#setImportFile
	 */
	public void setImportFile(ImportFile file) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#setRootGroup
	 */
	public void setRootGroup(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#getFailedRecords
	 */
	public List getFailedRecords() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#afterUserCreate
	 */
	public void afterUserCreate(User user) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#afterGroupCreate
	 */
	public void afterGroupCreate(Group group) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#getPresentationObjectClass
	 */
	public Class getPresentationObjectClass() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#getListViewerFields
	 */
	public Collection getListViewerFields() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#findGroupsByFields
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators,
			Collection listViewerFieldValues) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.importer.business.GolfImportHandlerBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws java.rmi.RemoteException;
}
