package is.idega.idegaweb.member.business;

import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.*;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.*;


/**
 * @author Joakim
 *
 */
public interface MemberFamilyLogic {

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getChildrenFor
	 */
	public Collection getChildrenFor(User user) throws NoChildrenFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getChildrenInCustodyOf
	 */
	public Collection getChildrenInCustodyOf(User user) throws NoChildrenFound, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getSiblingsFor
	 */
	public Collection getSiblingsFor(User user) throws NoSiblingFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getSpouseFor
	 */
	public User getSpouseFor(User user) throws NoSpouseFound, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getCohabitantFor
	 */
	public User getCohabitantFor(User user) throws NoCohabitantFound, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getCustodiansFor
	 */
	public Collection getCustodiansFor(User user, boolean returnParentsIfNotFound) throws NoCustodianFound,
			RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getCustodiansFor
	 */
	public Collection getCustodiansFor(User user) throws NoCustodianFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getParentsFor
	 */
	public Collection getParentsFor(User user) throws NoParentFound, RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#hasPersonGotChildren
	 */
	public boolean hasPersonGotChildren(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#hasPersonGotSpouse
	 */
	public boolean hasPersonGotSpouse(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#hasPersonGotCohabitant
	 */
	public boolean hasPersonGotCohabitant(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#hasPersonGotSiblings
	 */
	public boolean hasPersonGotSiblings(User person) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#isChildOf
	 */
	public boolean isChildOf(User childToCheck, User parent) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#isChildInCustodyOf
	 */
	public boolean isChildInCustodyOf(User childToCheck, User parent) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#isParentOf
	 */
	public boolean isParentOf(User parentToCheck, User child) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#isCustodianOf
	 */
	public boolean isCustodianOf(User custodianToCheck, User child) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#isSpouseOf
	 */
	public boolean isSpouseOf(User personToCheck, User relatedPerson) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#isCohabitantOf
	 */
	public boolean isCohabitantOf(User personToCheck, User relatedPerson) throws RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#isSiblingOf
	 */
	public boolean isSiblingOf(User personToCheck, User relatedPerson) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#setAsChildFor
	 */
	public void setAsChildFor(User personToSet, User parent) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#setAsParentFor
	 */
	public void setAsParentFor(User parent, User child) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#setAsCustodianFor
	 */
	public void setAsCustodianFor(User custodian, User child) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#setAsSpouseFor
	 */
	public void setAsSpouseFor(User personToSet, User relatedPerson) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#setAsCohabitantFor
	 */
	public void setAsCohabitantFor(User personToSet, User relatedPerson) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#setAsSiblingFor
	 */
	public void setAsSiblingFor(User personToSet, User relatedPerson) throws CreateException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#removeAsChildFor
	 */
	public void removeAsChildFor(User personToSet, User parent) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#removeAsParentFor
	 */
	public void removeAsParentFor(User parent, User child) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#removeAsCustodianFor
	 */
	public void removeAsCustodianFor(User custodian, User child) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#removeAsSpouseFor
	 */
	public void removeAsSpouseFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#removeAsCohabitantFor
	 */
	public void removeAsCohabitantFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#removeAsSiblingFor
	 */
	public void removeAsSiblingFor(User personToSet, User relatedPerson) throws RemoveException, RemoteException,
			java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getChildRelationType
	 */
	public String getChildRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getParentRelationType
	 */
	public String getParentRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getSiblingRelationType
	 */
	public String getSiblingRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getSpouseRelationType
	 */
	public String getSpouseRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getCohabitantRelationType
	 */
	public String getCohabitantRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getCustodianRelationType
	 */
	public String getCustodianRelationType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#getUserBusiness
	 */
	public UserBusiness getUserBusiness() throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#registerAsDeceased
	 */
	public void registerAsDeceased(User user, Date deceasedDate) throws RemoteException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.MemberFamilyLogicBean#removeAllFamilyRelationsForUser
	 */
	public void removeAllFamilyRelationsForUser(User user) throws RemoteException, java.rmi.RemoteException;
}
