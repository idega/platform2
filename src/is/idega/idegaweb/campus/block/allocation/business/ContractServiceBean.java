/*
 * $Id: ContractServiceBean.java,v 1.14 2004/06/16 03:44:46 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.allocation.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.allocation.data.ContractText;
import is.idega.idegaweb.campus.block.allocation.data.ContractTextHome;
import is.idega.idegaweb.campus.block.application.business.ApplicationService;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.WaitingListHome;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriodsHome;
import is.idega.idegaweb.campus.block.mailinglist.business.LetterParser;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListService;
import is.idega.idegaweb.campus.data.ApartmentContracts;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.data.SystemPropertiesBMPBean;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;
import com.idega.util.database.ConnectionBroker;


/**
 * Title: Service Bean for the campus contract system
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 */

public class ContractServiceBean extends IBOServiceBean implements ContractService {
	public final static int NAME = 0,
		SSN = 1,
		APARTMENT = 2,
		FLOOR = 3,
		BUILDING = 4,
		COMPLEX = 5,
		CATEGORY = 6,
		TYPE = 7,
		CONTRACT = 8,
		APPLICANT = 9;
	public String signContract(
		Integer contractID,
		Integer groupID,
		Integer cashierID,Integer financeCategoryID,
		String sEmail,
		boolean sendMail,
		boolean newAccount,
		boolean newPhoneAccount,
		boolean newLogin,
		boolean generatePasswd,
		IWResourceBundle iwrb,
		String login,
		String passwd) {
		Contract eContract = null;
		String pass = null;
		javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();
		try {
			t.begin();
			eContract = getContractHome().findByPrimaryKey(contractID);
			if (eContract != null) {
				Integer userID = eContract.getUserId();
				System.err.println("Signing user " + userID.toString() + " contract id : " + contractID);
				if (sEmail != null && sEmail.trim().length() > 0) {
					getUserService().addNewUserEmail(userID.intValue(), sEmail);
				}
				if (newAccount) {
					String prefix = iwrb.getLocalizedString("finance", "Finance");
					AccountManager.makeNewFinanceAccount(
						userID.intValue(),
						prefix + " - " + String.valueOf(userID),
						"",
						cashierID.intValue(),
						financeCategoryID.intValue());
				}
				if (newPhoneAccount) {
					String prefix = iwrb.getLocalizedString("phone", "Phone");
					AccountManager.makeNewPhoneAccount(
						userID.intValue(),
						prefix + " - " + String.valueOf(userID),
						"",
						cashierID.intValue(),
						financeCategoryID.intValue());
				}
				if (newLogin && groupID.intValue() > 0) {
					createUserLogin(userID, groupID, login, pass, generatePasswd);
				}
				deleteFromWaitingList(eContract);
				changeApplicationStatus(eContract);
				eContract.setStatusSigned();
				eContract.store();
				getMailingListService().processMailEvent(contractID.intValue(), LetterParser.SIGNATURE);
			}
			t.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				t.rollback();
			}
			catch (javax.transaction.SystemException ex) {
				ex.printStackTrace();
			}
		}
		return pass;
	}
	public void createUserLogin(Integer userID, Integer groupID, String login, String pass, boolean generatePasswd)
		throws Exception {
		User user = getUserService().getUser(userID);
		getUserService().generateUserLogin(user);
		Group group = getUserService().getGroupHome().findByPrimaryKey(groupID);
		group.addGroup(user);
	}
	public void changeApplicationStatus(Contract eContract) throws Exception {
		
		Collection L = getApplicationService().getApplicationHome().findByApplicantID(eContract.getApplicantId());
		if (L != null) {
			Iterator I = L.iterator();
			while (I.hasNext()) {
				Application A = (Application) I.next();
				A.setStatusSigned();
				A.store();
			}
		}
	}
	
	public void deleteFromWaitingList(Contract eContract) {
			deleteFromWaitingList(eContract.getApplicant());
	}
	
	public void deleteFromWaitingList(Applicant applicant) {
		Collection L =
			WaitingListFinder.listOfWaitingList(
				WaitingListFinder.APPLICANT,
				((Integer)applicant.getPrimaryKey()).intValue(),
				0,
				0);
		if (L != null) {
			Iterator I = L.iterator();

			while (I.hasNext()) {
				try {
					((WaitingList) I.next()).remove();
				}
				catch (EJBException e) {
					e.printStackTrace();
				}
				catch (RemoveException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void endContract(Integer contractID,IWTimestamp movingDate,String info, boolean datesync){
		try {
			endContract(getContractHome().findByPrimaryKey(contractID),movingDate,info,datesync);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	public void endContract(Contract C, IWTimestamp movingDate, String info, boolean datesync) {
		try {
			
			if(movingDate!=null)
				C.setMovingDate(movingDate.getDate());
			if (datesync)
				C.setValidTo(movingDate.getDate());
			C.setResignInfo(info);
			C.setStatusEnded();
			C.store();
			getMailingListService().processMailEvent(((Integer)C.getPrimaryKey()).intValue(), LetterParser.TERMINATION);
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void endExpiredContracts(){
		Collection contracts;
		try {
			contracts = getContractHome().findByStatusAndValidBeforeDate(ContractBMPBean.statusSigned,IWTimestamp.RightNow().getDate());
		
			for (Iterator iter = contracts.iterator(); iter.hasNext();) {
				Contract contract = (Contract) iter.next();
				endContract(contract,null,null,false);
				
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	
	}
	
	public void finalizeGarbageContracts(java.sql.Date lastChangeDate){
		Collection contracts;
		try {
			contracts = getContractHome().findByStatusAndChangeDate(ContractBMPBean.statusFinalized,lastChangeDate);
		
			for (Iterator iter = contracts.iterator(); iter.hasNext();) {
				Contract contract = (Contract) iter.next();
				contract.setStatusFinalized();
				contract.store();
				
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	public void returnKey(IWApplicationContext iwac, Integer contractID) {
		try {
			Contract C = getContractHome().findByPrimaryKey(contractID);
			C.setEnded();
			C.store();
			getMailingListService().processMailEvent(contractID.intValue(), LetterParser.RETURN);
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	public void deliverKey(Integer contractID, Timestamp when) {
		try {
			Contract C = getContractHome().findByPrimaryKey(contractID);
			if (when == null)
				C.setStarted();
			else
				C.setStarted(when);
			C.store();
			getMailingListService().processMailEvent(contractID.intValue(), LetterParser.DELIVER);
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	public void deliverKey(Integer contractID) {
		deliverKey(contractID, null);
	}
	public void resignContract(Integer contractID, IWTimestamp movingDate, String info, boolean datesync) {
		try {
			Contract C = getContractHome().findByPrimaryKey(contractID);
			C.setMovingDate(movingDate.getDate());
			if (datesync)
				C.setValidTo(movingDate.getDate());
			C.setResignInfo(info);
			C.setStatusResigned();
			C.store();
			getMailingListService().processMailEvent(contractID.intValue(), LetterParser.RESIGN);
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}
	public Contract createNewContract(Integer userID, Integer applicantID, Integer apartmentID, Date from, Date to)
		throws RemoteException, CreateException {
		Contract contract = getContractHome().create();
		contract.setApartmentId(apartmentID);
		contract.setApplicantId(applicantID);
		contract.setUserId(userID);
		contract.setStatusCreated();
		contract.setValidFrom((java.sql.Date) from);
		contract.setValidTo((java.sql.Date) to);
		contract.store();
		getMailingListService().processMailEvent(
			((Integer) contract.getPrimaryKey()).intValue(),
			LetterParser.ALLOCATION);
		return contract;
	}
	public User createNewUser(Applicant A, String[] emails) throws RemoteException, CreateException {
		//User user = getUserService().createUser(A.getFirstName(), A.getMiddleName(), A.getLastName(), A.getSSN());
		User user = getUserService().createUserByPersonalIDIfDoesNotExist(A.getFirstName(), A.getMiddleName(), A.getLastName(), A.getSSN(),null,null);
		if (emails != null && emails.length > 0) {
			Integer userID = (Integer) user.getPrimaryKey();
			getUserService().addNewUserEmail(userID.intValue(), emails[0]);
		}
		return user;
	}
	public boolean deleteAllocation(Integer contractID, User currentUser) {
		try {
			Contract eContract = getContractHome().findByPrimaryKey(contractID);
			//getUserService().deleteUser(eContract.getUserId().intValue(), currentUser);
			eContract.remove();
			return true;
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		catch (RemoveException e) {
			e.printStackTrace();
		}
		return false;
     
    }
 

 


  


  public  boolean makeNewContract(IWApplicationContext iwc,User eUser,Applicant eApplicant,int iApartmentId,IWTimestamp from,IWTimestamp to)throws java.rmi.RemoteException{
    try{
      Contract eContract = getContractHome().create();
      eContract.setApartmentId(iApartmentId);
      eContract.setApplicantId((Integer)eApplicant.getPrimaryKey());
      eContract.setUserId(((Integer)eUser.getPrimaryKey()).intValue());
      eContract.setStatusCreated();
      eContract.setValidFrom(from.getDate());
      eContract.setValidTo(to.getDate());

        eContract.store();
        getMailingListService().processMailEvent(((Integer)eContract.getPrimaryKey()).intValue(),LetterParser.ALLOCATION);
        return true;
      }
      catch(Exception ex){
        return false;
      }
  }

  public User makeNewUser(Applicant A,String[] emails){

    try{
    	User u = null;
    	
    	String ssn = A.getSSN();
    	if(ssn!=null){
    		
    			try {
					u = ((UserHome) IDOLookup.getHome(User.class)).findByPersonalID(ssn);
				}
				catch (RuntimeException e) {
					e.printStackTrace();
				}
    		
    		
    	}
    	else{
    		u = getUserService().insertUser(A.getFirstName(),A.getMiddleName(),A.getLastName(),A.getFirstName(),"",null,null,null);
    	}
    if(emails !=null && emails.length >0)
      getUserService().addNewUserEmail( ((Integer) u.getPrimaryKey()).intValue(),emails[0]);

    return u;
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    return null;
  }


   public  IWTimestamp[] getContractStampsFromPeriod(ApartmentTypePeriods ATP,int monthOverlap){
     IWTimestamp contractDateFrom = IWTimestamp.RightNow();
     IWTimestamp contractDateTo = IWTimestamp.RightNow();
     if(ATP!=null){
        // Period checking
        //System.err.println("ATP exists");
        boolean first = ATP.hasFirstPeriod();
        boolean second = ATP.hasSecondPeriod();
         IWTimestamp today = new IWTimestamp();

        // Two Periods
        if(first && second){

          if(today.getMonth() > ATP.getFirstDateMonth()+monthOverlap && today.getMonth() <= ATP.getSecondDateMonth()+monthOverlap ){
            contractDateFrom = new IWTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
            contractDateTo = new IWTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
          }
          else if(today.getMonth() <= 12){
            contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
            contractDateTo = new IWTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear()+1);
          }
          else{
            contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear());
            contractDateTo = new IWTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
          }

        }
        // One Periods
        else if(first && !second){
          //System.err.println("two sectors");
          contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear());
          contractDateTo = new IWTimestamp(ATP.getFirstDateDay(),ATP.getFirstDateMonth(),today.getYear()+1);
        }
        else if(!first && second){
          //System.err.println("two sectors");
          contractDateFrom = new IWTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear());
          contractDateTo = new IWTimestamp(ATP.getSecondDateDay(),ATP.getSecondDateMonth(),today.getYear()+1);
        }
     }

      IWTimestamp[] stamps = {contractDateFrom,contractDateTo};
      return stamps;
  }

 

	public IWTimestamp[] getContractStampsForApartment(Integer apartmentID) throws FinderException,RemoteException{
		Apartment ap = getBuildingService().getApartmentHome().findByPrimaryKey(apartmentID);
		return getContractStampsForApartment(ap);
	}
	public IWTimestamp[] getContractStampsForApartment(Apartment apartment) {
		try {
			ApartmentTypePeriods ATP = getApartmentTypePeriod(new Integer(apartment.getApartmentTypeId()));
			// ContractFinder.getPeriod(apartment.getApartmentTypeId());
			return getContractStampsFromPeriod(ATP, new Integer(1));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public IWTimestamp[] getContractStampsFromPeriod(ApartmentTypePeriods ATP, Integer monthOverlap) {
		IWTimestamp contractDateFrom = IWTimestamp.RightNow();
		IWTimestamp contractDateTo = IWTimestamp.RightNow();
		if (ATP != null) {
			// Period checking
			//System.err.println("ATP exists");
			boolean first = ATP.hasFirstPeriod();
			boolean second = ATP.hasSecondPeriod();
			IWTimestamp today = new IWTimestamp();
			// Two Periods
			if (first && second) {
				if (today.getMonth() > ATP.getFirstDateMonth() + monthOverlap.intValue()
					&& today.getMonth() <= ATP.getSecondDateMonth() + monthOverlap.intValue()) {
					contractDateFrom =
						new IWTimestamp(ATP.getSecondDateDay(), ATP.getSecondDateMonth(), today.getYear());
					contractDateTo =
						new IWTimestamp(ATP.getFirstDateDay(), ATP.getFirstDateMonth(), today.getYear() + 1);
				}
				else if (today.getMonth() <= 12) {
					contractDateFrom =
						new IWTimestamp(ATP.getFirstDateDay(), ATP.getFirstDateMonth(), today.getYear() + 1);
					contractDateTo =
						new IWTimestamp(ATP.getSecondDateDay(), ATP.getSecondDateMonth(), today.getYear() + 1);
				}
				else {
					contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(), ATP.getFirstDateMonth(), today.getYear());
					contractDateTo = new IWTimestamp(ATP.getSecondDateDay(), ATP.getSecondDateMonth(), today.getYear());
				}
			}
			// One Periods
			else if (first && !second) {
				//System.err.println("two sectors");
				contractDateFrom = new IWTimestamp(ATP.getFirstDateDay(), ATP.getFirstDateMonth(), today.getYear());
				contractDateTo = new IWTimestamp(ATP.getFirstDateDay(), ATP.getFirstDateMonth(), today.getYear() + 1);
			}
			else if (!first && second) {
				//System.err.println("two sectors");
				contractDateFrom = new IWTimestamp(ATP.getSecondDateDay(), ATP.getSecondDateMonth(), today.getYear());
				contractDateTo = new IWTimestamp(ATP.getSecondDateDay(), ATP.getSecondDateMonth(), today.getYear() + 1);
			}
		}
		IWTimestamp[] stamps = { contractDateFrom, contractDateTo };
		return stamps;
	}
	public String getLocalizedStatus(com.idega.idegaweb.IWResourceBundle iwrb, String status) {
		String r = "";
		char c = status.charAt(0);
		switch (c) {
			case 'C' :
				r = iwrb.getLocalizedString("created", "Created");
				break;
			case 'P' :
				r = iwrb.getLocalizedString("printed", "Printed");
				break;
			case 'S' :
				r = iwrb.getLocalizedString("signed", "Signed");
				break;
			case 'R' :
				r = iwrb.getLocalizedString("rejected", "Rejected");
				break;
			case 'T' :
				r = iwrb.getLocalizedString("terminated", "Terminated");
				break;
			case 'E' :
				r = iwrb.getLocalizedString("ended", "Ended");
				break;
			case 'G' :
				r = iwrb.getLocalizedString("garbage", "Garbage");
				break;
		}
		return r;
	}
	public boolean doGarbageContract(Integer contractID) {
		try {
			Contract eContract = getContractHome().findByPrimaryKey(contractID);
			eContract.setStatusGarbage();
			eContract.store();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
	public ApartmentTypePeriods getApartmentTypePeriod(Integer typeID) {
		try {
			ApartmentTypePeriodsHome aHome = (ApartmentTypePeriodsHome) getIDOHome(ApartmentTypePeriods.class);
			Collection periods = aHome.findByApartmentType(typeID);
			if (periods != null && !periods.isEmpty())
				return (ApartmentTypePeriods) periods.iterator().next();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Collection getAvailableEntries(int entity, int iApartmentTypeId, int iComplexId) {
		StringBuffer sql = new StringBuffer("select ");
		try {
			if (entity == APARTMENT)
				sql.append(" distinct a.* ");
			else if (entity == CONTRACT)
				sql.append(" con.* ");
			else
				throw new IllegalArgumentException();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
		sql.append(" from bu_apartment a,bu_floor f,bu_building b,app_applicant p ");
		sql.append(",bu_complex c,bu_aprt_type t,bu_aprt_cat y,cam_contract con ");
		sql.append(" where a.bu_aprt_type_id = t.bu_aprt_type_id ");
		sql.append(" and t.bu_aprt_cat_id = y.bu_aprt_cat_id");
		sql.append(" and a.bu_floor_id = f.bu_floor_id ");
		sql.append(" and f.bu_building_id = b.bu_building_id ");
		sql.append(" and b.bu_complex_id = c.bu_complex_id ");
		sql.append(" and a.bu_apartment_id = con.bu_apartment_id");
		sql.append(" and con.app_applicant_id = p.app_applicant_id");
		/** @todo  which contract statuses are defined apartment as available */
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
			//L = EntityFinder.getInstance().findAll(Contract.class, sql.toString());
			else if (entity == APARTMENT)
				L = ((ApartmentHome) getIDOHome(Apartment.class)).findBySQL(sql.toString());
			return L;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public Contract allocate(
		Integer contractID,
		Integer apartmentID,
		Integer applicantID,
		Date validFrom,
		Date validTo)
		throws AllocationException {
		//javax.transaction.TransactionManager transaction= com.idega.transaction.IdegaTransactionManager.getInstance();
		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			if (apartmentID != null && apartmentID.intValue() > 0) {
				Apartment apartment = getBuildingService().getApartmentHome().findByPrimaryKey(apartmentID);
				IWTimestamp firstAllowedFromDate = new IWTimestamp(getNextAvailableDate(apartment).getTime());
				firstAllowedFromDate.setAsDate();
				if (validFrom != null && validTo != null) {
					IWTimestamp from = new IWTimestamp((java.sql.Date) validFrom);
					IWTimestamp to = new IWTimestamp((java.sql.Date) validTo);
					from.setAsDate();
					to.setAsDate();
					//System.err.println("Saving new contract : Applicant : " + sApplicantId);
					//System.err.println("Must be from : " + mustBeFrom.toString() + " , is from " + from.toString());
					if (firstAllowedFromDate.isLaterThan(from)) {
						throw new AllocationException("Contract dates overlap");
					}
					if (applicantID != null) {
						if (apartmentID.intValue() > 0) {
							Applicant applicant =
								getApplicationService().getApplicantHome().findByPrimaryKey(applicantID);
							Collection applicantNewContracts =
								getContractHome().findByApplicantAndStatus(applicantID, ContractBMPBean.statusCreated);
							if (applicantNewContracts == null || applicantNewContracts.isEmpty()) {
								String[] emails = getApplicationService().getApplicantEmail(applicantID.intValue());
								Contract contract = null;
								try {
									transaction.begin();
									User eUser = createNewUser(applicant, emails);
									//System.out.println("user created "+eUser.getPrimaryKey());
									contract =
										createNewContract(
											(Integer) eUser.getPrimaryKey(),
											(Integer) applicant.getPrimaryKey(),
											apartmentID,
											from.getDate(),
											to.getDate());
									transaction.commit();
								}
								catch (Exception e1) {
									try {
										transaction.rollback();
									}
									catch (Exception e2) {
										e2.printStackTrace();
										throw new AllocationException("Transaction: " + e2.getMessage());
									}
									//e1.printStackTrace();
									throw new AllocationException("Transaction: " + e1.getMessage());
								}
								return contract;
							}
							else {
								throw new AllocationException("Applicant has already a new contract");
							}
						}
						else if (contractID != null && contractID.intValue() > 0) {
							Contract contract = getContractHome().findByPrimaryKey(contractID);
							contract.setValidFrom(from.getDate());
							contract.setValidTo(to.getDate());
							if (apartmentID != null) {
								contract.setApartmentId(apartmentID);
								contract.store();
								return contract;
							}
							else {
								throw new AllocationException("No apartment supplied for current contract");
							}
						}
						else {
							if (contractID == null)
								throw new AllocationException("No apartment or contract supplied ");
						}
					}
					else {
						throw new AllocationException("No applicant supplied");
					}
				}
				else {
					throw new AllocationException("No dates supplied to contract");
				}
			}
			else {
				throw new AllocationException("No apartment to contract");
			}
		}
		catch (IDOStoreException e) {
			throw new AllocationException(e.getMessage());
		}
		
		catch (RemoteException e) {
			throw new AllocationException(e.getMessage());
		}
		catch (FinderException e) {
			throw new AllocationException(e.getMessage());
		}
		throw new AllocationException("No contract info was supplied");
	}
	public Period getValidPeriod(Contract contract, Apartment apartment, Integer dayBuffer, Integer monthOverlap) {
		IWTimestamp contractDateFrom = new IWTimestamp();
		IWTimestamp contractDateTo = new IWTimestamp();
		ApartmentTypePeriods ATP = null;
		// if we have a contract return those dates;
		if (contract != null) {
			contractDateTo = new IWTimestamp(contract.getValidTo());
			contractDateFrom = new IWTimestamp(contract.getValidFrom());
		}
		// if we have an apartment lets see if there exist some definitions for it
		else if (apartment != null) {
			ATP = getApartmentTypePeriod(new Integer(apartment.getApartmentTypeId()));
			// Period checking
			//System.err.println("ATP exists");
			IWTimestamp[] stamps = getContractStampsFromPeriod(ATP, monthOverlap);
			contractDateTo = stamps[1];
			contractDateFrom = stamps[0];
			if (dayBuffer.intValue() > 0) {
				contractDateFrom.addDays(dayBuffer.intValue());
			}
			// end of Period checks
		}
		// are the System Properties set
		else if (
			getIWApplicationContext().getApplicationAttribute(SystemPropertiesBMPBean.getEntityTableName()) != null) {
			SystemProperties SysProps =
				(SystemProperties) getIWApplicationContext().getApplicationAttribute(
					SystemPropertiesBMPBean.getEntityTableName());
			contractDateTo = new IWTimestamp(SysProps.getValidToDate());
			contractDateFrom = new IWTimestamp();
		}
		else {
			contractDateTo = new IWTimestamp();
			contractDateFrom = new IWTimestamp();
		}
		Date nextAvailable = getNextAvailableDate(apartment);
		if (nextAvailable != null)
			contractDateFrom = new IWTimestamp(nextAvailable.getTime());
		return new Period(contractDateFrom.getDate(), contractDateFrom.getDate());
	}
	public Date getNextAvailableDate(Apartment apartment) {
		ApartmentContracts apartmentContracts = new ApartmentContracts(apartment);
		Date nextAvailable = apartmentContracts.getNextDate();
		// If apartment is not in contract table:
		if (!apartmentContracts.hasContracts() && apartment.getUnavailableUntil() != null) {
			nextAvailable = (Date) apartment.getUnavailableUntil();
		}
		return nextAvailable;
	}
	public void resetWaitingListRejection(Integer waitingListID) throws RemoteException, FinderException {
		try {
			WaitingList wl = getWaitingListHome().findByPrimaryKey(waitingListID);
			wl.setRejectFlag(false);
			wl.store();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}
	public void reactivateWaitingList(Integer waitingListID) throws RemoteException, FinderException {
		try {
			WaitingList wl = getWaitingListHome().findByPrimaryKey(waitingListID);
			wl.setRemovedFromList(is.idega.idegaweb.campus.block.application.data.WaitingListBMPBean.NO);
			wl.store();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}
	/**
	 * remove waitinglist entry from database
	 */
	public void removeWaitingList(Integer waitingListID) throws RemoteException, FinderException {
		try {
			WaitingList wl = getWaitingListHome().findByPrimaryKey(waitingListID);
			wl.remove();
		}
		catch (RemoveException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}
	}
	/**
	 * Gets a map of firsta available dates for apartments , keyed by apartmentID
	 */
	public Map getAvailableApartmentDates(Integer aprtTypeID, Integer cplxID) throws FinderException {
		Hashtable map = new Hashtable();
		try {
			String sqlString = getAvailableApartmentDatesSQL(aprtTypeID, cplxID);
			Connection conn = ConnectionBroker.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet RS = stmt.executeQuery(sqlString);
			Integer apartmentID;
			Date date;
			while (RS.next()) {
				apartmentID = new Integer(RS.getInt(1));
				date = RS.getDate(2);
				map.put(apartmentID, date);
			}
		}
		catch (SQLException e) {
			throw new FinderException(e.getMessage());
		}
		return map;
	}
	private String getAvailableApartmentDatesSQL(Integer aprtTypeID, Integer cplxID) {
		StringBuffer sql = new StringBuffer();
		sql
			.append("	 select c.bu_apartment_id,max(c.valid_to) ")
			.append(" from cam_contract c, bu_apartment a, bu_floor f, bu_building b, bu_aprt_type t, bu_complex x")
			.append(" where c.bu_apartment_id = a.bu_apartment_id ")
			.append(" and a.bu_floor_id = f.bu_floor_id ")
			.append(" and f.bu_building_id = b.bu_building_id ")
			.append(" and b.bu_complex_id = ")
			.append(cplxID)
			.append(" and a.bu_aprt_type_id = ")
			.append(aprtTypeID)
			.append(" and c.status not in('G') ")
			.append(" group by c.bu_apartment_id ");
		return sql.toString();
	}
	public Map getApplicantContractsByStatus(String status) throws RemoteException, FinderException {
		Map map = new Hashtable();
		Collection contracts = getContractHome().findByStatus(status);
		for (Iterator iter = contracts.iterator(); iter.hasNext();) {
			Contract element = (Contract) iter.next();
			map.put(element.getApplicantId(), element);
		}
		return map;
	}
	public Map getNewApplicantContracts() throws RemoteException, FinderException {
		return getApplicantContractsByStatus(ContractBMPBean.statusCreated);
	}
	public UserBusiness getUserService() throws RemoteException {
		return (UserBusiness) getServiceInstance(UserBusiness.class);
	}
	public ContractHome getContractHome() throws RemoteException {
		return (ContractHome) getIDOHome(Contract.class);
	}
	public ContractTextHome getContractTextHome() throws RemoteException {
		return (ContractTextHome) getIDOHome(ContractText.class);
	}
	public WaitingListHome getWaitingListHome() throws RemoteException {
		return (WaitingListHome) getIDOHome(WaitingList.class);
	}
	public MailingListService getMailingListService() throws RemoteException {
		return (MailingListService) getServiceInstance(MailingListService.class);
	}
	public ApplicationService getApplicationService() throws RemoteException {
		return (ApplicationService) getServiceInstance(ApplicationService.class);
	}
	public BuildingService getBuildingService() throws RemoteException{
		return (BuildingService)getServiceInstance(BuildingService.class);
	}
}
