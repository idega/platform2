/*
 * $Id$
 * Created on 14.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.nationalregister.business;

import is.idega.block.family.business.FamilyLogic;
import is.idega.block.nationalregister.data.NationalRegister;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.core.location.data.PostalCode;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision$
 */
public interface NationalRegisterBusiness extends IBOService , UserGroupPlugInBusiness {

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getEntryBySSN
	 */
	public NationalRegister getEntryBySSN(String ssn) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#updateEntry
	 */
	public boolean updateEntry(String symbol, String oldId, String ssn, String familyId, String name, String commune,
			String street, String building, String floor, String sex, String maritialStatus, String empty,
			String prohibitMarking, String nationality, String placeOfBirth, String spouseSSN, String fate,
			String parish, String po, String address, String addressCode, String dateOfModification,
			String placementCode, String dateOfCreation, String lastDomesticAddress, String agentSsn, String sNew,
			String addressName, String dateOfDeletion, String newSsnOrName, String dateOfBirth, Group citizenGroup)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#updateUserPersonalID
	 */
	public void updateUserPersonalID(String oldPersonalID, String newPersonalID) throws IBOLookupException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#updateUserOldID
	 */
	public void updateUserOldID(String oldID, String personalID) throws IBOLookupException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#updateUserAddress
	 */
	public void updateUserAddress(User user, UserBusiness userBiz, String address, String po) throws RemoteException,
			CreateException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getPostalCode
	 */
	public PostalCode getPostalCode(String po) throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#afterUserCreate
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#afterGroupCreate
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getPresentationObjectClass
	 */
	public Class getPresentationObjectClass() throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException;     

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getListViewerFields
	 */
	public Collection getListViewerFields() throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#findGroupsByFields
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators,
			Collection listViewerFieldValues) throws RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.nationalregister.business.NationalRegisterBusinessBean#getFamilyLogic
	 */
	public FamilyLogic getFamilyLogic() throws RemoteException;
}
