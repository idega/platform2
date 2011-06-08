/*
 * $Id: ContractServiceBean.java,v 1.24.4.7 2009/07/08 16:52:17 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.business;

import is.idega.idegaweb.campus.block.allocation.data.AutomaticCharges;
import is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesHome;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.allocation.data.ContractTariff;
import is.idega.idegaweb.campus.block.allocation.data.ContractTariffHome;
import is.idega.idegaweb.campus.block.allocation.data.ContractText;
import is.idega.idegaweb.campus.block.allocation.data.ContractTextHome;
import is.idega.idegaweb.campus.block.application.business.ApplicationService;
import is.idega.idegaweb.campus.block.application.data.ApplicantFamily;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriodsHome;
import is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusiness;
import is.idega.idegaweb.campus.block.mailinglist.business.LetterParser;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListService;
import is.idega.idegaweb.campus.business.CampusGroupException;
import is.idega.idegaweb.campus.business.CampusUserService;
import is.idega.idegaweb.campus.data.ApartmentContracts;
import is.idega.idegaweb.campus.data.ContractAccountApartment;
import is.idega.idegaweb.campus.data.ContractAccountApartmentHome;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.data.SystemPropertiesBMPBean;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.transaction.UserTransaction;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.building.business.BuildingService;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.data.Account;
import com.idega.block.finance.data.AccountBMPBean;
import com.idega.block.finance.data.AccountHome;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.AccountKeyHome;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.data.IDOLookup;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.database.ConnectionBroker;

/**
 * Title: Service Bean for the campus contract system Description: Copyright:
 * Copyright (c) 2000-2001 idega.is All Rights Reserved Company: idega
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 */

public class ContractServiceBean extends IBOServiceBean implements
		ContractService {
	private static final long serialVersionUID = 3613701313865796879L;
	public final static int NAME = 0, SSN = 1, APARTMENT = 2, FLOOR = 3,
			BUILDING = 4, COMPLEX = 5, CATEGORY = 6, TYPE = 7, CONTRACT = 8,
			APPLICANT = 9;

	public String signContract(Integer contractID, Integer groupID,
			Integer cashierID, Integer financeCategoryID, String sEmail,
			boolean sendMail, boolean newAccount, boolean newPhoneAccount,
			boolean newLogin, boolean generatePasswd, IWResourceBundle iwrb,
			String login, String passwd) {
		Contract eContract = null;
		String pass = null;
		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager
				.getInstance();
		try {
			t.begin();
			eContract = getContractHome().findByPrimaryKey(contractID);
			if (eContract != null) {
				Integer userID = eContract.getUserId();
				if (sEmail != null && sEmail.trim().length() > 0) {
					getUserService().addNewUserEmail(userID.intValue(), sEmail);
				}
				if (newAccount) {
					String prefix = iwrb.getLocalizedString("finance",
							"Finance");
					AccountManager.makeNewFinanceAccount(userID.intValue(),
							prefix + " - " + String.valueOf(userID), "",
							cashierID.intValue(), financeCategoryID.intValue());
				}
				if (newPhoneAccount) {
					String prefix = iwrb.getLocalizedString("phone", "Phone");
					AccountManager.makeNewPhoneAccount(userID.intValue(),
							prefix + " - " + String.valueOf(userID), "",
							cashierID.intValue(), financeCategoryID.intValue());
				}
				if (newLogin && groupID.intValue() > 0) {
					User user = getUserService().getUser(userID);
					createUserLogin(user, groupID, login, pass, generatePasswd);
					getUserService().setAsTenant(user);
				}
				deleteFromWaitingList(eContract);
				changeApplicationStatus(eContract);
				eContract.setStatusSigned();
				eContract.store();
				getMailingListService().processMailEvent(contractID.intValue(),
						LetterParser.SIGNATURE);
			}
			t.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				t.rollback();
			} catch (javax.transaction.SystemException ex) {
				ex.printStackTrace();
			}
		}
		return pass;
	}

	public void createUserLogin(User user, Integer groupID, String login,
			String pass, boolean generatePasswd) throws Exception {
		LoginTable loginEntry = getUserService().generateUserLogin(user);
		loginEntry.setLastChanged(null);
		loginEntry.store();
	}

	public void changeApplicationStatus(Contract eContract) throws Exception {
		if (eContract.getApplication() != null) {
			Application app = eContract.getApplication();
			app.setStatusSigned();
			app.store();
		}
	}

	public void deleteFromWaitingList(Contract eContract) {
		if (eContract.getApplication() != null) {
			deleteFromWaitingList(eContract.getApplication());
		}
	}

	public void deleteFromWaitingList(Application application) {
		Collection L = null;
		try {
			L = WaitingListFinder.getWaitingListHome().findByApplication(
					application);
		} catch (RemoteException e1) {
		} catch (FinderException e1) {
		}
		if (L != null) {
			Iterator I = L.iterator();

			while (I.hasNext()) {
				try {
					((WaitingList) I.next()).remove();
				} catch (EJBException e) {
					e.printStackTrace();
				} catch (RemoveException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteFromWaitingList(Applicant applicant) {
		Collection L = WaitingListFinder.listOfWaitingList(
				WaitingListFinder.APPLICANT, ((Integer) applicant
						.getPrimaryKey()).intValue(), 0, 0);
		if (L != null) {
			Iterator I = L.iterator();

			while (I.hasNext()) {
				try {
					((WaitingList) I.next()).remove();
				} catch (EJBException e) {
					e.printStackTrace();
				} catch (RemoveException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void endContract(Integer contractID, IWTimestamp movingDate,
			String info, boolean datesync) {
		try {
			endContract(getContractHome().findByPrimaryKey(contractID),
					movingDate, info, datesync);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void endContract(Contract C, IWTimestamp movingDate, String info,
			boolean datesync) {
		try {
			if (movingDate != null)
				C.setMovingDate(movingDate.getDate());
			if (datesync)
				C.setValidTo(movingDate.getDate());
			C.setResignInfo(info);
			C.setStatusEnded();
			C.store();
			getMailingListService().processMailEvent(
					((Integer) C.getPrimaryKey()).intValue(),
					LetterParser.TERMINATION);
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void endExpiredContracts() {
		Collection contracts;
		try {
			contracts = getContractHome().findByStatusAndValidBeforeDate(
					ContractBMPBean.STATUS_SIGNED,
					IWTimestamp.RightNow().getDate());
			for (Iterator iter = contracts.iterator(); iter.hasNext();) {
				Contract contract = (Contract) iter.next();
				endContract(contract, null, null, false);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

	}

	public void garbageEndedContracts(java.sql.Date lastChangeDate) {
		Collection contracts;
		try {
			contracts = getContractHome().findByStatusAndChangeDate(
					ContractBMPBean.STATUS_ENDED, lastChangeDate);
			if (contracts != null)
				System.out.println(contracts.size()
						+ " ended contracts found to be garbaged");
			for (Iterator iter = contracts.iterator(); iter.hasNext();) {
				Contract contract = (Contract) iter.next();
				contract.setStatusGarbage();
				contract.setIsRented(false);
				contract.store();

			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void garbageResignedContracts(java.sql.Date lastChangeDate) {
		Collection contracts;
		try {
			contracts = getContractHome().findByStatusAndChangeDate(
					ContractBMPBean.STATUS_RESIGNED, lastChangeDate);
			for (Iterator iter = contracts.iterator(); iter.hasNext();) {
				Contract contract = (Contract) iter.next();
				contract.setStatusGarbage();
				contract.setIsRented(false);
				contract.store();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void finalizeGarbageContracts(java.sql.Date lastChangeDate) {
		Collection contracts;
		try {
			contracts = getContractHome().findByStatusAndChangeDate(
					ContractBMPBean.STATUS_GARBAGE, lastChangeDate);
			for (Iterator iter = contracts.iterator(); iter.hasNext();) {
				Contract contract = (Contract) iter.next();
				contract.setStatusFinalized();
				contract.setIsRented(false);
				contract.store();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void automaticKeyStatusChange() {
		Collection contracts = null;
		IWTimestamp now = IWTimestamp.RightNow();
		try {
			contracts = getContractHome().findAllWithKeyChangeDateSet();
			for (Iterator iter = contracts.iterator(); iter.hasNext();) {
				Contract contract = (Contract) iter.next();
				IWTimestamp at = new IWTimestamp(contract
						.getChangeKeyStatusAt());
				if (now.isLaterThan(at)) {
					contract.setChangeKeyStatusAt(null);
					if (contract.getStatus().equals(ContractBMPBean.STATUS_GARBAGE) || contract.getStatus().equals(ContractBMPBean.STATUS_FINALIZED) || contract.getStatus().equals(ContractBMPBean.STATUS_STORAGE)) {
						contract.setIsRented(false);
					} else {
						contract.setIsRented(contract.getChangeKeyStatusTo());
					}
					contract.store();
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public void returnKey(Integer contractID, User currentUser) {
		try {
			Contract C = getContractHome().findByPrimaryKey(contractID);
			C.setEnded();
			C.store();
			getUserService().removeAsCurrentTenant(C.getUser(), currentUser);
			getMailingListService().processMailEvent(contractID.intValue(),
					LetterParser.RETURN);
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CampusGroupException e) {
			e.printStackTrace();
		}
	}

	public void deliverKey(Integer contractID, Timestamp when,
			boolean addKeyCharge, Integer accountKeyId, Integer tariffGroupId,
			Integer financeCategoryId, double amount) {
		try {
			Contract contract = getContractHome().findByPrimaryKey(contractID);
			if (when == null)
				contract.setStarted();
			else
				contract.setStarted(when);
			contract.store();
			getUserService().setAsCurrentTenant(contract.getUser());
			getMailingListService().processMailEvent(contractID.intValue(),
					LetterParser.DELIVER);

			if (addKeyCharge) {
				User user = contract.getUser();
				Account account = getAccountHome().findByUserAndType(user,
						AccountBMPBean.typeFinancial);

				IWTimestamp today = IWTimestamp.RightNow();
				
				AccountKey key = getAccountKeyHome().findByPrimaryKey(
						accountKeyId);

				getCampusAssessmentBusiness().assessTariffsToAccount(
						(float) amount, key.getInfo(), key.getInfo(),
						(Integer) account.getPrimaryKey(), accountKeyId,
						today.getDate(), tariffGroupId, financeCategoryId,
						contract.getApartmentId(), false, null);
			}
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CampusGroupException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}
	}

	public CampusAssessmentBusiness getCampusAssessmentBusiness()
			throws RemoteException {
		return (CampusAssessmentBusiness) getServiceInstance(CampusAssessmentBusiness.class);
	}

	public void resignContract(Integer contractID, IWTimestamp movingDate,
			String info, boolean datesync, boolean deleteContinuationContracts, String subjectID) {
		try {
			Contract C = getContractHome().findByPrimaryKey(contractID);
			C.setMovingDate(movingDate.getDate());
			if (datesync) {
				C.setValidTo(movingDate.getDate());
			}
			C.setResignInfo(info);
			C.setStatusResigned();
			C.store();
			getMailingListService().processMailEvent(contractID.intValue(),
					LetterParser.RESIGN);
			
			int contSubjId = 0;
			try {
				contSubjId = Integer.parseInt(subjectID);
			} catch (Exception e) {
				contSubjId = 0;
			}
			
			if (deleteContinuationContracts && contSubjId > 0) {
				ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
				Collection resultSet = cHome.findByApplicantAndStatus(C.getApplicantId(), ContractBMPBean.STATUS_SIGNED);
				if (resultSet != null && !resultSet.isEmpty()) {
					Iterator it = resultSet.iterator();
					while (it.hasNext()) {
						Contract cont = (Contract) it.next();
						if (cont.getApplication() != null) {
							if (cont.getApplication().getSubjectId() == contSubjId) {
								cont.setResignInfo(info);
								cont.setStatusResigned();
								cont.store();								
							}
						}
					}
				}
			}
		} catch (IDOStoreException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public Contract createNewContract(Integer userID, Integer applicantID,
			Integer apartmentID, Date from, Date to, Integer applicationID) throws RemoteException,
			CreateException {
		Contract contract = getContractHome().create();
		contract.setApartmentId(apartmentID);
		contract.setApplicantId(applicantID);
		contract.setUserId(userID);
		contract.setStatusCreated();
		contract.setValidFrom((java.sql.Date) from);
		contract.setValidTo((java.sql.Date) to);
		if (applicationID != null) {
			contract.setApplicationID(applicationID
					.intValue());
		}
		contract.store();
		getMailingListService().processMailEvent(
				((Integer) contract.getPrimaryKey()).intValue(),
				LetterParser.ALLOCATION);
		return contract;
	}

	public User createUserFamily(Applicant applicant, String[] emails)
			throws RemoteException, CreateException, CampusGroupException {
		User user = createNewUser(applicant, emails);
		getUserService().setAsTenant(user);
		ApplicantFamily family = getApplicationService().getApplicantFamily(
				applicant);
		Applicant applicantSpouse = family.getSpouse();
		if (applicantSpouse != null) {
			User uspouse = createNewUser(applicantSpouse, null);
			getUserService().setAsTenantSpouse(user, uspouse);
		}
		Collection children = family.getChildren();
		if (children != null && !children.isEmpty()) {
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				Applicant applicantChild = (Applicant) iter.next();
				User child = createNewUser(applicantChild, null);
				getUserService().setAsTenantChild(user, child);
			}
		}
		return user;
	}

	public User createNewUser(Applicant A, String[] emails)
			throws RemoteException, CreateException {
		User user = null;
		if (getAllowedTemporaryPersonalID().contains(A.getSSN()))
			user = getUserService().createUser(A.getFirstName(),
					A.getMiddleName(), A.getLastName(), A.getSSN());
		else
			user = getUserService().createUserByPersonalIDIfDoesNotExist(
					A.getFirstName(), A.getMiddleName(), A.getLastName(),
					A.getSSN(), null, null);
		if (user != null && emails != null && emails.length > 0) {
			Integer userID = (Integer) user.getPrimaryKey();
			getUserService().addNewUserEmail(userID.intValue(), emails[0]);
		}
		return user;
	}

	public boolean deleteAllocation(Integer contractID, User currentUser) {
		try {
			Contract eContract = getContractHome().findByPrimaryKey(contractID);
			// getUserService().deleteUser(eContract.getUserId().intValue(),
			// currentUser);
			eContract.remove();
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (EJBException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (RemoveException e) {
			e.printStackTrace();
		}
		return false;

	}

	public IWTimestamp[] getContractStampsFromPeriod(ApartmentTypePeriods ATP,
			int monthOverlap) {
		IWTimestamp contractDateFrom = IWTimestamp.RightNow();
		IWTimestamp contractDateTo = IWTimestamp.RightNow();
		if (ATP != null) {
			// Period checking
			// System.err.println("ATP exists");
			boolean first = ATP.hasFirstPeriod();
			boolean second = ATP.hasSecondPeriod();
			IWTimestamp today = new IWTimestamp();

			// Two Periods
			if (first && second) {

				if (today.getMonth() > ATP.getFirstDateMonth() + monthOverlap
						&& today.getMonth() <= ATP.getSecondDateMonth()
								+ monthOverlap) {
					contractDateFrom = new IWTimestamp(ATP.getSecondDateDay(),
							ATP.getSecondDateMonth(), today.getYear());
					contractDateTo = new IWTimestamp(ATP.getFirstDateDay(), ATP
							.getFirstDateMonth(), today.getYear() + 1);
				} else if (today.getMonth() <= 12) {
					contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(),
							ATP.getFirstDateMonth(), today.getYear() + 1);
					contractDateTo = new IWTimestamp(ATP.getSecondDateDay(),
							ATP.getSecondDateMonth(), today.getYear() + 1);
				} else {
					contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(),
							ATP.getFirstDateMonth(), today.getYear());
					contractDateTo = new IWTimestamp(ATP.getSecondDateDay(),
							ATP.getSecondDateMonth(), today.getYear());
				}

			}
			// One Periods
			else if (first && !second) {
				// System.err.println("two sectors");
				contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(), ATP
						.getFirstDateMonth(), today.getYear());
				contractDateTo = new IWTimestamp(ATP.getFirstDateDay(), ATP
						.getFirstDateMonth(), today.getYear() + 1);
			} else if (!first && second) {
				// System.err.println("two sectors");
				contractDateFrom = new IWTimestamp(ATP.getSecondDateDay(), ATP
						.getSecondDateMonth(), today.getYear());
				contractDateTo = new IWTimestamp(ATP.getSecondDateDay(), ATP
						.getSecondDateMonth(), today.getYear() + 1);
			}
		}

		IWTimestamp[] stamps = { contractDateFrom, contractDateTo };
		return stamps;
	}

	public IWTimestamp[] getContractStampsForApartment(Integer apartmentID)
			throws FinderException, RemoteException {
		Apartment ap = getBuildingService().getApartmentHome()
				.findByPrimaryKey(apartmentID);
		return getContractStampsForApartment(ap);
	}

	public IWTimestamp[] getContractStampsForApartment(Apartment apartment) {
		try {
			ApartmentTypePeriods ATP = getApartmentTypePeriod(new Integer(
					apartment.getApartmentTypeId()));
			// ContractFinder.getPeriod(apartment.getApartmentTypeId());
			return getContractStampsFromPeriod(ATP, new Integer(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public IWTimestamp[] getContractStampsFromPeriod(ApartmentTypePeriods ATP,
			Integer monthOverlap) {
		IWTimestamp contractDateFrom = IWTimestamp.RightNow();
		IWTimestamp contractDateTo = IWTimestamp.RightNow();
		if (ATP != null) {
			// Period checking
			// System.err.println("ATP exists");
			boolean first = ATP.hasFirstPeriod();
			boolean second = ATP.hasSecondPeriod();
			IWTimestamp today = new IWTimestamp();
			// Two Periods
			if (first && second) {
				if (today.getMonth() > ATP.getFirstDateMonth()
						+ monthOverlap.intValue()
						&& today.getMonth() <= ATP.getSecondDateMonth()
								+ monthOverlap.intValue()) {
					contractDateFrom = new IWTimestamp(ATP.getSecondDateDay(),
							ATP.getSecondDateMonth(), today.getYear());
					contractDateTo = new IWTimestamp(ATP.getFirstDateDay(), ATP
							.getFirstDateMonth(), today.getYear() + 1);
				} else if (today.getMonth() <= 12) {
					contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(),
							ATP.getFirstDateMonth(), today.getYear() + 1);
					contractDateTo = new IWTimestamp(ATP.getSecondDateDay(),
							ATP.getSecondDateMonth(), today.getYear() + 1);
				} else {
					contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(),
							ATP.getFirstDateMonth(), today.getYear());
					contractDateTo = new IWTimestamp(ATP.getSecondDateDay(),
							ATP.getSecondDateMonth(), today.getYear());
				}
			}
			// One Periods
			else if (first && !second) {
				// System.err.println("two sectors");
				contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(), ATP
						.getFirstDateMonth(), today.getYear());
				contractDateTo = new IWTimestamp(ATP.getFirstDateDay(), ATP
						.getFirstDateMonth(), today.getYear() + 1);
			} else if (!first && second) {
				// System.err.println("two sectors");
				contractDateFrom = new IWTimestamp(ATP.getSecondDateDay(), ATP
						.getSecondDateMonth(), today.getYear());
				contractDateTo = new IWTimestamp(ATP.getSecondDateDay(), ATP
						.getSecondDateMonth(), today.getYear() + 1);
			}
		}
		IWTimestamp[] stamps = { contractDateFrom, contractDateTo };
		return stamps;
	}

	public String getLocalizedStatus(com.idega.idegaweb.IWResourceBundle iwrb,
			String status) {
		String r = "";
		char c = status.charAt(0);
		switch (c) {
		case 'C':
			r = iwrb.getLocalizedString("created", "Created");
			break;
		case 'P':
			r = iwrb.getLocalizedString("printed", "Printed");
			break;
		case 'S':
			r = iwrb.getLocalizedString("signed", "Signed");
			break;
		case 'E':
			r = iwrb.getLocalizedString("ended", "Ended");
			break;
		case 'G':
			r = iwrb.getLocalizedString("garbage", "Garbage");
			break;
		case 'U':
			r = iwrb.getLocalizedString("resigned", "Resigned");
			break;
		case 'R':
			r = iwrb.getLocalizedString("rejected", "Rejected");
			break;
		case 'T':
			r = iwrb.getLocalizedString("terminated", "Terminated");
			break;

		case 'Z':
			r = iwrb.getLocalizedString("storage", "Storage");
			break;
		case 'D':
			r = iwrb.getLocalizedString("denied", "Denied");
			break;
		case 'F':
			r = iwrb.getLocalizedString("finalized", "Finalized");
			break;
		}

		return r;
	}

	public boolean doGarbageContract(Integer contractID) {
		try {
			Contract eContract = getContractHome().findByPrimaryKey(contractID);
			if (eContract.getStatus().equals(ContractBMPBean.STATUS_CREATED) || eContract.getStatus().equals(ContractBMPBean.STATUS_PRINTED)) {
				eContract.remove();
			} else {
				eContract.setStatusGarbage();
				eContract.setIsRented(false);
				eContract.store();
			}
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public ApartmentTypePeriods getApartmentTypePeriod(Integer typeID) {
		try {
			ApartmentTypePeriodsHome aHome = (ApartmentTypePeriodsHome) getIDOHome(ApartmentTypePeriods.class);
			Collection periods = aHome.findByApartmentType(typeID);
			if (periods != null && !periods.isEmpty())
				return (ApartmentTypePeriods) periods.iterator().next();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Collection getAvailableEntries(int entity, int iApartmentTypeId,
			int iComplexId) {
		StringBuffer sql = new StringBuffer("select ");
		try {
			if (entity == APARTMENT)
				sql.append(" distinct a.* ");
			else if (entity == CONTRACT)
				sql.append(" con.* ");
			else
				throw new IllegalArgumentException();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		sql
				.append(" from bu_apartment a,bu_floor f,bu_building b,app_applicant p ");
		sql
				.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y,cam_contract con ");
		sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
		sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
		sql.append(" and a.bu_floor_id = f.bu_floor_id ");
		sql.append(" and f.bu_building_id = b.bu_building_id ");
		sql.append(" and b.bu_complex_id = c.bu_complex_id ");
		sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
		sql.append(" and con.app_applicant_id = p.app_applicant_id");
		/** @todo which contract statuses are defined apartment as available */
		// 
		sql.append(" and con.status not in ('G') ");
		// not the ones in garbage
		/** */
		if (iComplexId > 0) {
			sql.append(" and b.bu_complex_id  = ");
			sql.append(iComplexId);
		}
		if (iApartmentTypeId > 0) {
			sql.append(" and a.bu_aprt_type_id = ");
			sql.append(iApartmentTypeId);
		}
		try {
			Collection L = null;
			if (entity == CONTRACT)
				L = getContractHome().findBySQL(sql.toString());
			// L = EntityFinder.getInstance().findAll(Contract.class,
			// sql.toString());
			else if (entity == APARTMENT)
				L = ((ApartmentHome) getIDOHome(Apartment.class)).findBySQL(sql
						.toString());
			return L;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Contract allocate(Integer contractID, Integer apartmentID,
			Integer applicantID, Date validFrom, Date validTo,
			Integer applicationID) throws AllocationException {
		// javax.transaction.TransactionManager transaction=
		// com.idega.transaction.IdegaTransactionManager.getInstance();
		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			if (apartmentID != null && apartmentID.intValue() > 0) {
				Apartment apartment = getBuildingService().getApartmentHome()
						.findByPrimaryKey(apartmentID);
				IWTimestamp firstAllowedFromDate = new IWTimestamp(
						getNextAvailableDate(apartment).getTime());
				firstAllowedFromDate.setAsDate();
				if (validFrom != null && validTo != null) {
					IWTimestamp from = new IWTimestamp(
							(java.sql.Date) validFrom);
					IWTimestamp to = new IWTimestamp((java.sql.Date) validTo);
					from.setAsDate();
					to.setAsDate();
					if (firstAllowedFromDate.isLaterThan(from)) {
						throw new AllocationException("Contract dates overlap");
					}
					if (applicantID != null) {
						if (apartmentID.intValue() > 0) {
							Applicant applicant = getApplicationService()
									.getApplicantHome().findByPrimaryKey(
											applicantID);
							Collection applicantNewContracts = getContractHome()
									.findByApplicantAndStatus(applicantID,
											ContractBMPBean.STATUS_CREATED);
							if (applicantNewContracts == null
									|| applicantNewContracts.isEmpty()) {
								String[] emails = getApplicationService()
										.getApplicantEmail(
												applicantID.intValue());
								Contract contract = null;
								try {
									transaction.begin();
									User eUser = createUserFamily(applicant,
											emails);
									contract = createNewContract(
											(Integer) eUser.getPrimaryKey(),
											(Integer) applicant.getPrimaryKey(),
											apartmentID, from.getDate(), to
													.getDate(), applicationID);
									transaction.commit();
								} catch (Exception e1) {
									try {
										transaction.rollback();
									} catch (Exception e2) {
										e2.printStackTrace();
										throw new AllocationException(
												"Transaction: "
														+ e2.getMessage());
									}
									throw new AllocationException(
											"Transaction: " + e1.getMessage());
								}

								return contract;
							} else {
								throw new AllocationException(
										"Applicant has already a new contract");
							}
						} else if (contractID != null
								&& contractID.intValue() > 0) {
							Contract contract = getContractHome()
									.findByPrimaryKey(contractID);
							contract.setValidFrom(from.getDate());
							contract.setValidTo(to.getDate());
							if (apartmentID != null) {
								contract.setApartmentId(apartmentID);
								if (applicationID != null) {
									contract.setApplicationID(applicationID
											.intValue());
								}
								contract.store();

								return contract;
							} else {
								throw new AllocationException(
										"No apartment supplied for current contract");
							}
						} else {
							if (contractID == null)
								throw new AllocationException(
										"No apartment or contract supplied ");
						}
					} else {
						throw new AllocationException("No applicant supplied");
					}
				} else {
					throw new AllocationException(
							"No dates supplied to contract");
				}
			} else {
				throw new AllocationException("No apartment to contract");
			}
		} catch (IDOStoreException e) {
			throw new AllocationException(e.getMessage());
		}

		catch (RemoteException e) {
			throw new AllocationException(e.getMessage());
		} catch (FinderException e) {
			throw new AllocationException(e.getMessage());
		}
		throw new AllocationException("No contract info was supplied");
	}

	public Period getValidPeriod(Contract contract, Apartment apartment,
			Integer dayBuffer, Integer monthOverlap) {
		IWTimestamp contractDateFrom = new IWTimestamp();
		IWTimestamp contractDateTo = new IWTimestamp();
		ApartmentTypePeriods ATP = null;
		// if we have a contract return those dates;
		if (contract != null) {
			contractDateTo = new IWTimestamp(contract.getValidTo());
			contractDateFrom = new IWTimestamp(contract.getValidFrom());
			return new Period(contractDateFrom.getDate(), contractDateTo
					.getDate());
		}
		// if we have an apartment lets see if there exist some definitions for
		// it
		else if (apartment != null) {
			ATP = getApartmentTypePeriod(new Integer(apartment
					.getApartmentTypeId()));
			// Period checking
			// System.err.println("ATP exists");
			IWTimestamp[] stamps = getContractStampsFromPeriod(ATP,
					monthOverlap);
			contractDateTo = stamps[1];
			contractDateFrom = stamps[0];
			if (dayBuffer.intValue() > 0) {
				contractDateFrom.addDays(dayBuffer.intValue());
			}
			// end of Period checks
		}
		// are the System Properties set
		else if (getIWApplicationContext().getApplicationAttribute(
				SystemPropertiesBMPBean.getEntityTableName()) != null) {
			SystemProperties SysProps = (SystemProperties) getIWApplicationContext()
					.getApplicationAttribute(
							SystemPropertiesBMPBean.getEntityTableName());
			contractDateTo = new IWTimestamp(SysProps.getValidToDate());
			contractDateFrom = new IWTimestamp();
		} else {
			contractDateTo = new IWTimestamp();
			contractDateFrom = new IWTimestamp();
		}
		int years = contractDateTo.getYear() - contractDateFrom.getYear();
		// int months = contractDateTo.getMonth()-contractDateFrom.getMonth();
		Date nextAvailable = getNextAvailableDate(apartment);
		if (nextAvailable != null) {
			IWTimestamp nextD = new IWTimestamp(nextAvailable.getTime());
			if (nextD.isLaterThan(contractDateFrom))
				contractDateFrom = nextD;
		}
		if (years > 0)
			contractDateTo.setYear(contractDateFrom.getYear() + years);

		return new Period(contractDateFrom.getDate(), contractDateTo.getDate());
	}

	/**
	 * Returns the first date that the given apartment can be rented from.
	 * Contracts with getAllocateableStatuses() are checked.
	 */
	public Date getNextAvailableDate(Apartment apartment) {
		ApartmentContracts apartmentContracts = new ApartmentContracts(
				apartment, getAllocateableStatuses());
		Date nextAvailable = apartmentContracts.getNextDate();
		// If apartment is not in contract table:
		if (!apartmentContracts.hasContracts()
				&& apartment.getUnavailableUntil() != null) {
			nextAvailable = (Date) apartment.getUnavailableUntil();
		}
		return nextAvailable;
	}

	public boolean getIsContractResigned(Apartment apartment) {
		ApartmentContracts apartmentContracts = new ApartmentContracts(
				apartment, getAllocateableStatuses());
		if (!apartmentContracts.hasContracts()) {
			return false;
		}

		return true;
	}

	public void resetWaitingListRejection(Integer waitingListID)
			throws RemoteException, FinderException {
		try {
			WaitingList wl = getWaitingListHome().findByPrimaryKey(
					waitingListID);
			wl.setRejectFlag(false);
			wl.store();
		} catch (IDOStoreException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	public void reactivateWaitingList(Integer waitingListID)
			throws RemoteException, FinderException {
		try {
			WaitingList wl = getWaitingListHome().findByPrimaryKey(
					waitingListID);
			wl
					.setRemovedFromList(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.NO);
			wl.store();
		} catch (IDOStoreException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * remove waitinglist entry from database
	 */
	public void removeWaitingList(Integer waitingListID)
			throws RemoteException, FinderException {
		try {
			WaitingList wl = getWaitingListHome().findByPrimaryKey(
					waitingListID);
			wl.remove();
		} catch (RemoveException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * Gets a map of firsta available dates for apartments , keyed by
	 * apartmentID
	 */
	public Map getAvailableApartmentDates(Integer aprtTypeID, Integer cplxID)
			throws FinderException {
		Hashtable map = new Hashtable();
		Connection conn = null;
		Statement stmt = null;
		try {
			String sqlString = getAvailableApartmentDatesSQL(aprtTypeID, cplxID);
			conn = ConnectionBroker.getConnection();
			stmt = conn.createStatement();
			ResultSet RS = stmt.executeQuery(sqlString);
			Integer apartmentID;
			Date date;
			while (RS.next()) {
				apartmentID = new Integer(RS.getInt(1));
				date = RS.getDate(2);
				map.put(apartmentID, date);
			}

			RS.close();
		} catch (SQLException e) {
			throw new FinderException(e.getMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			ConnectionBroker.freeConnection(conn);
		}
		return map;
	}

	private String getAvailableApartmentDatesSQL(Integer aprtTypeID,
			Integer cplxID) {
		StringBuffer sql = new StringBuffer();
		sql
				.append("	 select c.bu_apartment_id,max(c.valid_to) ")
				.append(
						" from cam_contract c, bu_apartment a, bu_floor f, bu_building b, bu_aprt_type t, bu_complex x")
				.append(" where c.bu_apartment_id = a.bu_apartment_id ")
				.append(" and a.bu_floor_id = f.bu_floor_id ").append(
						" and f.bu_building_id = b.bu_building_id ").append(
						" and b.bu_complex_id = ").append(cplxID).append(
						" and a.bu_aprt_type_id = ").append(aprtTypeID).append(
						" and c.status not in('G') ").append(
						" group by c.bu_apartment_id ");
		return sql.toString();
	}

	public Map getApplicantContractsByStatus(String status)
			throws RemoteException, FinderException {
		Map map = new Hashtable();
		Collection contracts = getContractHome().findByStatus(status);
		for (Iterator iter = contracts.iterator(); iter.hasNext();) {
			Contract element = (Contract) iter.next();
			if (element.getApplication() != null) {
				map.put(element.getApplication().getPrimaryKey().toString(),
						element);
			}
		}
		return map;
	}

	/**
	 * Returns statuses: signed, ended,resigned and terminated.
	 */
	public String[] getRentableStatuses() {
		String[] statuses = { ContractBMPBean.STATUS_SIGNED,
				ContractBMPBean.STATUS_ENDED, ContractBMPBean.STATUS_RESIGNED,
				ContractBMPBean.STATUS_TERMINATED };
		return statuses;
	}

	/**
	 * Returns statuses: created,printed, signed, ended,resigned and terminated.
	 */
	public String[] getAllocateableStatuses() {
		String[] statuses = { ContractBMPBean.STATUS_CREATED,
				ContractBMPBean.STATUS_PRINTED, ContractBMPBean.STATUS_SIGNED,
				ContractBMPBean.STATUS_ENDED, ContractBMPBean.STATUS_RESIGNED,
				ContractBMPBean.STATUS_TERMINATED };
		return statuses;
	}

	/**
	 * Returns statuses: resigned
	 */
	public String[] getResignStatus() {
		String[] statuses = { ContractBMPBean.STATUS_RESIGNED };
		return statuses;
	}

	public Collection getAllowedTemporaryPersonalID() {
		ArrayList list = new ArrayList(1);
		list.add("9999999999");
		return list;
	}

	public AutomaticCharges getAutomaticChargesByUser(
			User user) {
		try {
			return this.getAutomaticChargesHome().findByUser(user);
		} catch (RemoteException e) {
		} catch (FinderException e) {
		}

		return null;
	}

	public void addChargeForUnlimitedDownloadToUser(String userID) {
		try {
			User user = getUserService().getUser(Integer.valueOf(userID));
			AutomaticCharges charge = getAutomaticChargesByUser(user);
			if (charge == null) {
				charge = getAutomaticChargesHome().create();
				charge.setUser(user);
			}
			charge.setChargeForDownload(true);
			charge.store();
		} catch (NumberFormatException e) {
		} catch (RemoteException e) {
		} catch (CreateException e) {
		}
	}

	public void removeChargeForUnlimitedDownloadForUser(String userID) {
		try {
			User user = getUserService().getUser(Integer.valueOf(userID));
			AutomaticCharges charge = getAutomaticChargesByUser(user);
			if (charge != null) {
				charge.setChargeForDownload(false);
				charge.store();
			}
		} catch (NumberFormatException e) {
		} catch (RemoteException e) {
		}
	}

	public void addChargeForHandlingToUser(String userID) {
		try {
			User user = getUserService().getUser(Integer.valueOf(userID));
			AutomaticCharges charge = getAutomaticChargesByUser(user);
			if (charge == null) {
				charge = getAutomaticChargesHome().create();
				charge.setUser(user);
			}
			charge.setChargeForHandling(true);
			charge.store();
		} catch (NumberFormatException e) {
		} catch (RemoteException e) {
		} catch (CreateException e) {
		}
	}

	public void removeChargeForHandlingForUser(String userID) {
		try {
			User user = getUserService().getUser(Integer.valueOf(userID));
			AutomaticCharges charge = getAutomaticChargesByUser(user);
			if (charge != null) {
				charge.setChargeForHandling(false);
				charge.store();
			}
		} catch (NumberFormatException e) {
		} catch (RemoteException e) {
		}
	}

	public void addChargeForTransferToUser(String userID) {
		try {
			User user = getUserService().getUser(Integer.valueOf(userID));
			AutomaticCharges charge = getAutomaticChargesByUser(user);
			if (charge == null) {
				charge = getAutomaticChargesHome().create();
				charge.setUser(user);
			}
			charge.setChargeForTransfer(true);
			charge.store();
		} catch (NumberFormatException e) {
		} catch (RemoteException e) {
		} catch (CreateException e) {
		}
	}

	public void removeChargeForTransferForUser(String userID) {
		try {
			User user = getUserService().getUser(Integer.valueOf(userID));
			AutomaticCharges charge = getAutomaticChargesByUser(user);
			if (charge != null) {
				charge.setChargeForTransfer(false);
				charge.store();
			}
		} catch (NumberFormatException e) {
		} catch (RemoteException e) {
		}
	}

	public void removeAllAutomaticChargesForUser(String userID) {
		try {
			User user = getUserService().getUser(Integer.valueOf(userID));
			AutomaticCharges charge = getAutomaticChargesByUser(user);
			if (charge != null) {
				charge.setChargeForDownload(false);
				charge.setChargeForHandling(false);
				charge.setChargeForTransfer(false);
				charge.store();
			}
		} catch (NumberFormatException e) {
		} catch (RemoteException e) {
		}
	}
	
	public Map getNewApplicantContracts() throws RemoteException,
			FinderException {
		return getApplicantContractsByStatus(ContractBMPBean.STATUS_CREATED);
	}

	public Map getPrintedContracts() throws RemoteException, FinderException {
		return getApplicantContractsByStatus(ContractBMPBean.STATUS_PRINTED);
	}

	public CampusUserService getUserService() throws RemoteException {
		return (CampusUserService) getServiceInstance(CampusUserService.class);
	}

	public ContractHome getContractHome() throws RemoteException {
		return (ContractHome) getIDOHome(Contract.class);
	}

	public AutomaticChargesHome getAutomaticChargesHome()
			throws RemoteException {
		return (AutomaticChargesHome) getIDOHome(AutomaticCharges.class);
	}

	public AccountHome getAccountHome() throws RemoteException {
		return (AccountHome) getIDOHome(Account.class);
	}

	public ContractTextHome getContractTextHome() throws RemoteException {
		return (ContractTextHome) getIDOHome(ContractText.class);
	}

	public WaitingListHome getWaitingListHome() throws RemoteException {
		return (WaitingListHome) getIDOHome(WaitingList.class);
	}

	public ContractAccountApartmentHome getContractAccountApartmentHome()
			throws RemoteException {
		return ((ContractAccountApartmentHome) getIDOHome(ContractAccountApartment.class));
	}

	public AccountKeyHome getAccountKeyHome() throws RemoteException {
		return ((AccountKeyHome) getIDOHome(AccountKey.class));
	}

	public MailingListService getMailingListService() throws RemoteException {
		return (MailingListService) getServiceInstance(MailingListService.class);
	}

	public ApplicationService getApplicationService() throws RemoteException {
		return (ApplicationService) getServiceInstance(ApplicationService.class);
	}

	public BuildingService getBuildingService() throws RemoteException {
		return (BuildingService) getServiceInstance(BuildingService.class);
	}
	
	public ContractTariffHome getContractTariffHome() throws RemoteException {
		return (ContractTariffHome) getIDOHome(ContractTariff.class);
	}

}