/*
 * $Id: FamilyLogic.java,v 1.5 2004/09/06 10:54:11 joakim Exp $
 * Created on 6.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.business;

import is.idega.block.family.data.FamilyData;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.business.IBOService;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/09/06 10:54:11 $ by $Author: joakim $
 * 
 * @author <a href="mailto:Joakim@idega.com">Joakim</a>
 * @version $Revision: 1.5 $
 */
public interface FamilyLogic extends IBOService {

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getChildrenFor
	 */
	public Collection getChildrenFor(User user) throws NoChildrenFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getChildrenInCustodyOf
	 */
	public Collection getChildrenInCustodyOf(User user) throws NoChildrenFound, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSiblingsFor
	 */
	public Collection getSiblingsFor(User user) throws NoSiblingFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSpouseFor
	 */
	public User getSpouseFor(User user) throws NoSpouseFound, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCohabitantFor
	 */
	public User getCohabitantFor(User user) throws NoCohabitantFound, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCustodiansFor
	 */
	public Collection getCustodiansFor(User user, boolean returnParentsIfNotFound) throws NoCustodianFound,
			RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCustodiansFor
	 */
	public Collection getCustodiansFor(User user) throws NoCustodianFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getParentsFor
	 */
	public Collection getParentsFor(User user) throws NoParentFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotChildren
	 */
	public boolean hasPersonGotChildren(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotSpouse
	 */
	public boolean hasPersonGotSpouse(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotCohabitant
	 */
	public boolean hasPersonGotCohabitant(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#hasPersonGotSiblings
	 */
	public boolean hasPersonGotSiblings(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isChildOf
	 */
	public boolean isChildOf(User childToCheck, User parent) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isChildInCustodyOf
	 */
	public boolean isChildInCustodyOf(User childToCheck, User parent) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isParentOf
	 */
	public boolean isParentOf(User parentToCheck, User child) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isCustodianOf
	 */
	public boolean isCustodianOf(User custodianToCheck, User child) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isSpouseOf
	 */
	public boolean isSpouseOf(User personToCheck, User relatedPerson) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isCohabitantOf
	 */
	public boolean isCohabitantOf(User personToCheck, User relatedPerson) throws RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#isSiblingOf
	 */
	public boolean isSiblingOf(User personToCheck, User relatedPerson) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsChildFor
	 */
	public void setAsChildFor(User personToSet, User parent) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsParentFor
	 */
	public void setAsParentFor(User parent, User child) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsCustodianFor
	 */
	public void setAsCustodianFor(User custodian, User child) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsSpouseFor
	 */
	public void setAsSpouseFor(User personToSet, User relatedPerson) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsCohabitantFor
	 */
	public void setAsCohabitantFor(User personToSet, User relatedPerson) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setAsSiblingFor
	 */
	public void setAsSiblingFor(User personToSet, User relatedPerson) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsChildFor
	 */
	public void removeAsChildFor(User personToSet, User parent) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsParentFor
	 */
	public void removeAsParentFor(User parent, User child) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsCustodianFor
	 */
	public void removeAsCustodianFor(User custodian, User child) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsSpouseFor
	 */
	public void removeAsSpouseFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsCohabitantFor
	 */
	public void removeAsCohabitantFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsSiblingFor
	 */
	public void removeAsSiblingFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsChildFor
	 */
	public void removeAsChildFor(User personToSet, User parent, User performer) throws RemoveException,
			RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsParentFor
	 */
	public void removeAsParentFor(User parent, User child, User performer) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsCustodianFor
	 */
	public void removeAsCustodianFor(User custodian, User child, User performer) throws RemoveException,
			RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsSpouseFor
	 */
	public void removeAsSpouseFor(User personToSet, User relatedPerson, User performer) throws RemoveException,
			RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsCohabitantFor
	 */
	public void removeAsCohabitantFor(User personToSet, User relatedPerson, User performer) throws RemoveException,
			RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAsSiblingFor
	 */
	public void removeAsSiblingFor(User personToSet, User relatedPerson, User performer) throws RemoveException,
			RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getChildRelationType
	 */
	public String getChildRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getParentRelationType
	 */
	public String getParentRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSiblingRelationType
	 */
	public String getSiblingRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getSpouseRelationType
	 */
	public String getSpouseRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCohabitantRelationType
	 */
	public String getCohabitantRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getCustodianRelationType
	 */
	public String getCustodianRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getUserBusiness
	 */
	public UserBusiness getUserBusiness() throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#registerAsDeceased
	 */
	public void registerAsDeceased(User user, Date deceasedDate) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#registerAsDeceased
	 */
	public void registerAsDeceased(User user, Date deceasedDate, User performer) throws RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAllFamilyRelationsForUser
	 */
	public void removeAllFamilyRelationsForUser(User user) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#removeAllFamilyRelationsForUser
	 */
	public void removeAllFamilyRelationsForUser(User user, User performer) throws RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#setFamilyForUser
	 */
	public void setFamilyForUser(String familyiNr, User user) throws RemoteException, CreateException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#updateFamilyForUser
	 */
	public void updateFamilyForUser(String familyNr, User user) throws RemoteException, CreateException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.block.family.business.FamilyLogicBean#getFamily
	 */
	public FamilyData getFamily(String familyNr) throws RemoteException, FinderException, java.rmi.RemoteException;
}
