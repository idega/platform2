/*
 * $Id: AccountingBusiness.java,v 1.40 2005/10/28 11:03:46 palli Exp $
 * Created on Oct 21, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentType;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.business.IBOService;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2005/10/28 11:03:46 $ by $Author: palli $
 * 
 * @author <a href="mailto:bluebottle@idega.com">bluebottle</a>
 * @version $Revision: 1.40 $
 */
public interface AccountingBusiness extends IBOService, UserGroupPlugInBusiness {

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#doAssessment
	 */
	public boolean doAssessment(String name, Group club, Group division, String groupId, User user,
			boolean includeChildren, String tariff, Timestamp paymentDate, Date tariffValidFrom, Date tariffValidTo,
			String amount, String skip) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllTariffByClub
	 */
	public Collection findAllTariffByClub(Group club) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllTariffByClubAndDivision
	 */
	public Collection findAllTariffByClubAndDivision(Group club, Group division) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllValidTariffByGroup
	 */
	public Collection findAllValidTariffByGroup(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllValidTariffByGroup
	 */
	public Collection findAllValidTariffByGroup(String groupId) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertTariff
	 */
	public ClubTariff insertTariff(Group club, Group division, Group group, ClubTariffType type, String text,
			double amount, Date from, Date to) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findDivisionForGroup
	 */
	public Group findDivisionForGroup(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findClubForGroup
	 */
	public Group findClubForGroup(Group group) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#deleteTariff
	 */
	public boolean deleteTariff(String[] ids) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllTariffTypeByClub
	 */
	public Collection findAllTariffTypeByClub(Group club) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertTariffType
	 */
	public boolean insertTariffType(String type, String name, String locKey, Group club)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#deleteTariffType
	 */
	public boolean deleteTariffType(String[] ids) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllCreditCardType
	 */
	public Collection findAllCreditCardType() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllCreditCardContractByClub
	 */
	public Collection findAllCreditCardContractByClub(Group club) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertCreditCardContract
	 */
	public boolean insertCreditCardContract(Group club, String division, String group, String contractNumber,
			String type) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertCreditCardContract
	 */
	public boolean insertCreditCardContract(Group club, Group division, Group group, String contractNumber,
			CreditCardType type) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#deleteContract
	 */
	public boolean deleteContract(String[] ids) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllAssessmentRoundByClubAndDivision
	 */
	public Collection findAllAssessmentRoundByClubAndDivision(Group club, Group division)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertAssessmentRound
	 */
	public AssessmentRound insertAssessmentRound(String name, Group club, Group division, Group group, User user,
			Timestamp start, Timestamp end, boolean includeChildren, Timestamp paymentDate, Date tariffValidFrom,
			Date tariffValidTo, String amount) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#deleteAssessmentRound
	 */
	public boolean deleteAssessmentRound(String[] ids) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertPayment
	 */
	public boolean insertPayment(String type, String amount, User currentUser, Map basket, IWUserContext iwuc)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertPayment
	 */
	public boolean insertPayment(String type, String amount, User currentUser, Map basket, IWUserContext iwuc,
			String payedBy) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertPayment
	 */
	public boolean insertPayment(PaymentType type, int amount, User currentUser, Map basket, IWUserContext iwuc)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertPayment
	 */
	public boolean insertPayment(PaymentType type, int amount, User currentUser, Map basket, IWUserContext iwuc,
			User payedBy) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertPayment
	 */
	public boolean insertPayment(Group club, Group division, User contractUser, String cardNumber, String cardType,
			IWTimestamp expires, IWTimestamp firstPayment, int nop, String paymentType, String[] amount,
			User currentUser, Map basket, IWUserContext iwuc) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertPayment
	 */
	public boolean insertPayment(Group club, Group division, User contractUser, String cardNumber,
			CreditCardType cardType, IWTimestamp expires, IWTimestamp firstPayment, int nop, PaymentType type,
			int[] amount, User currentUser, Map basket, IWUserContext iwuc) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertManualAssessment
	 */
	public boolean insertManualAssessment(Group club, Group div, User user, String groupId, String tariffId,
			String amount, String info, User currentUser, Timestamp paymentDate) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertManualAssessment
	 */
	public boolean insertManualAssessment(Group club, Group div, User user, Group group, ClubTariff tariff,
			double amount, String info, User currentUser, Timestamp paymentDate) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#equalizeEntries
	 */
	public void equalizeEntries(Group club, Group div, User user, double amount) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllOpenAssessmentEntriesByUserGroupAndDivision
	 */
	public Collection findAllOpenAssessmentEntriesByUserGroupAndDivision(Group club, Group div, User user)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllPaymentEntriesByUserGroupAndDivision
	 */
	public Collection findAllPaymentEntriesByUserGroupAndDivision(Group club, Group div, User user)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getFinanceEntryByPrimaryKey
	 */
	public FinanceEntry getFinanceEntryByPrimaryKey(Integer id) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllPaymentTypes
	 */
	public Collection findAllPaymentTypes() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getFinanceEntriesByDateIntervalDivisionsAndGroups
	 */
	public Collection getFinanceEntriesByDateIntervalDivisionsAndGroups(Group club, String[] types,
			java.sql.Date dateFrom, java.sql.Date dateTo, Collection divisionsFilter, Collection groupsFilter,
			String personalID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getFinanceEntriesByPaymentDateDivisionsAndGroups
	 */
	public Collection getFinanceEntriesByPaymentDateDivisionsAndGroups(Group club, String[] types,
			Collection divisionsFilter, Collection groupsFilter, String personalID) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user, Group parentGroup) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User user, Group parentGroup) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group, Group parentGroup) throws RemoveException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#afterGroupCreateOrUpdate
	 */
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup) throws CreateException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#canCreateSubGroup
	 */
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException;

}
