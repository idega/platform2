/*
 * Created on Sep 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound;
import is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRoundHome;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffHome;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractHome;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;
import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntryHome;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentContract;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractHome;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentType;
import is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeHome;
import is.idega.idegaweb.member.isi.block.accounting.presentation.plugin.CashierWindowPlugin;
import is.idega.idegaweb.member.isi.block.accounting.presentation.plugin.CreditCardExtraInfo;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.basket.data.BasketEntry;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOService;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

/**
 * @author IBM
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface AccountingBusiness extends IBOService {
    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#doAssessment
     */
    public boolean doAssessment(String name, Group club, Group division,
            String groupId, User user, boolean includeChildren,
            String[] tariffs, Timestamp paymentDate, Timestamp runOnDate)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllTariffByClub
     */
    public Collection findAllTariffByClub(Group club)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllTariffByClubAndDivision
     */
    public Collection findAllTariffByClubAndDivision(Group club, Group division)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllValidTariffByGroup
     */
    public Collection findAllValidTariffByGroup(Group group)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllValidTariffByGroup
     */
    public Collection findAllValidTariffByGroup(String groupId)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertTariff
     */
    public boolean insertTariff(Group club, Group division, String groupId,
            String typeId, String text, String amount, Date from, Date to,
            boolean applyToChildren, String skip)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertTariff
     */
    public boolean insertTariff(Group club, Group division, Group group,
            ClubTariffType type, String text, double amount, Date from,
            Date to, boolean applyToChildren, String skipList, List skip)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findDivisionForGroup
     */
    public Group findDivisionForGroup(Group group)
            throws java.rmi.RemoteException;

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
    public Collection findAllTariffTypeByClub(Group club)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertTariffType
     */
    public boolean insertTariffType(String type, String name, String locKey,
            Group club) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#deleteTariffType
     */
    public boolean deleteTariffType(String[] ids)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllCreditCardType
     */
    public Collection findAllCreditCardType() throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllCreditCardContractByClub
     */
    public Collection findAllCreditCardContractByClub(Group club)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertCreditCardContract
     */
    public boolean insertCreditCardContract(Group club, String division,
            String group, String contractNumber, String type)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertCreditCardContract
     */
    public boolean insertCreditCardContract(Group club, Group division,
            Group group, String contractNumber, CreditCardType type)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#deleteContract
     */
    public boolean deleteContract(String[] ids) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllAssessmentRoundByClubAndDivision
     */
    public Collection findAllAssessmentRoundByClubAndDivision(Group club,
            Group division) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertAssessmentRound
     */
    public AssessmentRound insertAssessmentRound(String name, Group club,
            Group division, Group group, User user, Timestamp start,
            Timestamp end, boolean includeChildren, Timestamp paymentDate,
            Timestamp runOnDate) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#deleteAssessmentRound
     */
    public boolean deleteAssessmentRound(String[] ids)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertPayment
     */
    public boolean insertPayment(String type, String amount, User currentUser,
            Map basket, IWUserContext iwuc) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertPayment
     */
    public boolean insertPayment(PaymentType type, int amount,
            User currentUser, Map basket, IWUserContext iwuc)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertPayment
     */
    public boolean insertPayment(Group club, Group division, User contractUser,
            String cardNumber, String cardType, IWTimestamp expires,
            IWTimestamp firstPayment, int nop, String paymentType,
            String[] amount, User currentUser, Map basket, IWUserContext iwuc)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertPayment
     */
    public boolean insertPayment(Group club, Group division, User contractUser,
            String cardNumber, CreditCardType cardType, IWTimestamp expires,
            IWTimestamp firstPayment, int nop, PaymentType type, int[] amount,
            User currentUser, Map basket, IWUserContext iwuc)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertManualAssessment
     */
    public boolean insertManualAssessment(Group club, Group div, User user,
            String groupId, String tariffId, String amount, String info,
            User currentUser, Timestamp paymentDate)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#insertManualAssessment
     */
    public boolean insertManualAssessment(Group club, Group div, User user,
            Group group, ClubTariff tariff, double amount, String info,
            User currentUser, Timestamp paymentDate)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#equalizeEntries
     */
    public void equalizeEntries(Group club, Group div, User user, double amount)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllOpenAssessmentEntriesByUserGroupAndDivision
     */
    public Collection findAllOpenAssessmentEntriesByUserGroupAndDivision(
            Group club, Group div, User user) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllPaymentEntriesByUserGroupAndDivision
     */
    public Collection findAllPaymentEntriesByUserGroupAndDivision(Group club,
            Group div, User user) throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getFinanceEntryByPrimaryKey
     */
    public FinanceEntry getFinanceEntryByPrimaryKey(Integer id)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findAllPaymentTypes
     */
    public Collection findAllPaymentTypes() throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getFinanceEntriesByDateIntervalDivisionsAndGroups
     */
    public Collection getFinanceEntriesByDateIntervalDivisionsAndGroups(
            Group club, String[] types, java.sql.Date dateFrom,
            java.sql.Date dateTo, Collection divisionsFilter,
            Collection groupsFilter, String personalID)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getFinanceEntriesByPaymentDateDivisionsAndGroups
     */
    public Collection getFinanceEntriesByPaymentDateDivisionsAndGroups(
            Group club, String[] types, Collection divisionsFilter,
            Collection groupsFilter, String personalID)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#beforeUserRemove
     */
    public void beforeUserRemove(User user) throws RemoveException,
            RemoteException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#afterUserCreate
     */
    public void afterUserCreate(User user) throws CreateException,
            RemoteException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#beforeGroupRemove
     */
    public void beforeGroupRemove(Group group) throws RemoveException,
            RemoteException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#afterGroupCreate
     */
    public void afterGroupCreate(Group group) throws CreateException,
            RemoteException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getPresentationObjectClass
     */
    public Class getPresentationObjectClass() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#instanciateEditor
     */
    public PresentationObject instanciateEditor(Group group)
            throws RemoteException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#instanciateViewer
     */
    public PresentationObject instanciateViewer(Group group)
            throws RemoteException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getUserPropertiesTabs
     */
    public List getUserPropertiesTabs(User user) throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getGroupPropertiesTabs
     */
    public List getGroupPropertiesTabs(Group group) throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getMainToolbarElements
     */
    public List getMainToolbarElements() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getGroupToolbarElements
     */
    public List getGroupToolbarElements(Group group) throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#getListViewerFields
     */
    public Collection getListViewerFields() throws RemoteException,
            java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#findGroupsByFields
     */
    public Collection findGroupsByFields(Collection listViewerFields,
            Collection finderOperators, Collection listViewerFieldValues)
            throws RemoteException, java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#isUserAssignableFromGroupToGroup
     */
    public String isUserAssignableFromGroupToGroup(User user,
            Group sourceGroup, Group targetGroup)
            throws java.rmi.RemoteException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusinessBean#isUserSuitedForGroup
     */
    public String isUserSuitedForGroup(User user, Group targetGroup)
            throws java.rmi.RemoteException;

}
