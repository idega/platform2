package se.idega.idegaweb.commune.childcare.check.business;

import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolType;
import com.idega.core.data.Address;
import com.idega.core.data.PostalCode;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoCustodianFound;

import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.childcare.check.data.CheckHome;
import se.idega.idegaweb.commune.childcare.check.data.GrantedCheck;
import se.idega.idegaweb.commune.childcare.check.data.GrantedCheckHome;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class CheckBusinessBean extends CaseBusinessBean implements CheckBusiness {
	public static final String PROPERTIES_CHILD_CARE = "child_care";
	public static final String PROPERTY_CHECK_FEE = "check_fee";
	public static final String PROPERTY_CHECK_AMOUNT = "check_amount";

	public static final int METHOD_USER = 1;
	public static final int METHOD_SYSTEM = 2;
	
	public CheckBusinessBean() {}

	private CheckHome getCheckHome() throws RemoteException {
		return (CheckHome) com.idega.data.IDOLookup.getHome(Check.class);
	}

	private GrantedCheckHome getGrantedCheckHome() throws RemoteException {
		return (GrantedCheckHome) com.idega.data.IDOLookup.getHome(GrantedCheck.class);
	}

	private ChildCareApplicationHome getChildCareApplicationHome() throws RemoteException {
		return (ChildCareApplicationHome) com.idega.data.IDOLookup.getHome(ChildCareApplication.class);
	}

	public Check getCheck(int checkId) throws RemoteException {
		try {
			return getCheckHome().findByPrimaryKey(new Integer(checkId));
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public Collection findChecks() throws FinderException,RemoteException {
		return getCheckHome().findChecks();
	}

	public Collection findUnhandledChecks() throws FinderException,RemoteException  {
		return getCheckHome().findNonApprovedChecks();
	}

	public boolean allRulesVerified(Check check) throws RemoteException {
		boolean rule1 = check.getRule1();
		boolean rule2 = check.getRule2();
		boolean rule3 = check.getRule3();
		boolean rule4 = check.getRule4();
		boolean rule5 = check.getRule5();
		return ( rule3 && rule4 ) || rule5;
	}

	public int createCheck(int childCareType, int workSituation1, int workSituation2, String motherTongueMotherChild, String motherTongueFatherChild, String motherTongueParents, int childId, int method, int amount, int checkFee, User user, String notes, boolean checkRule1, boolean checkRule2, boolean checkRule3, boolean checkRule4, boolean checkRule5) throws CreateException,RemoteException {
		CheckHome home = (CheckHome) com.idega.data.IDOLookup.getHome(Check.class);
		Check check = null;
		try {
			check = home.findCheckForChild(childId);
		}
		catch (FinderException fe) {
			check = home.create();
		}
		check.setChildCareType(childCareType);
		check.setWorkSituation1(workSituation1);
		check.setWorkSituation2(workSituation2);
		check.setMotherTongueMotherChild(motherTongueMotherChild);
		check.setMotherTongueFatherChild(motherTongueFatherChild);
		check.setMotherTongueParents(motherTongueParents);
		check.setChildId(childId);
		check.setMethod(method);
		check.setAmount(amount);
		check.setCheckFee(checkFee);
		check.setOwner(user);
		check.setNotes(notes);
		check.setRule1(checkRule1);
		check.setRule2(checkRule2);
		check.setRule3(checkRule3);
		check.setRule4(checkRule4);
		check.setRule5(checkRule5);
		check.setUserNotes("");
		check.setCaseStatus(this.getCaseStatusOpen());

		check.store();
		return ((Integer)check.getPrimaryKey()).intValue();
	}

	public Check createCheck(int childId, int method, int amount, int checkFee, User user) throws CreateException,RemoteException {
		CheckHome home = (CheckHome) com.idega.data.IDOLookup.getHome(Check.class);
		Check check = null;
		try {
			check = home.findCheckForChild(childId);
		}
		catch (FinderException fe) {
			check = home.create();
		}
		check.setChildId(childId);
		check.setMethod(method);
		check.setAmount(amount);
		check.setCheckFee(checkFee);
		if ( user != null )
			check.setOwner(user);
		check.setCaseStatus(this.getCaseStatusOpen());

		check.store();
		return check;
	}

	public void sendMessageToCitizen(Check check, int userID, String subject, String body) throws CreateException,RemoteException {
		Message message = getMessageBusiness().createUserMessage(userID, subject, body);
		if ( message != null ) {
			message.setParentCase(check);
			message.store();
		}
	}

	public void sendMessageToArchive(Check check, int userID, String subject, String body) throws Exception {
		try {
			Message message = getMessageBusiness().createPrintArchivationMessage(userID, subject, body);
			message.setParentCase(check);
			message.store();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public void sendMessageToPrinter(Check check, int userID, String subject, String body) throws Exception {
		try {
			Message message = getMessageBusiness().createPrintedLetterMessage(userID, subject, body);
			message.setParentCase(check);
			message.store();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public int getUserID(Check check) throws RemoteException {
		User user = check.getOwner();
		int userID = -1;
		if (user != null)
			userID = ((Integer) user.getPrimaryKey()).intValue();
		return userID;
	}
	
	public Check saveCheckRules(int checkID, String[] selectedRules, String notes, String userNotes, User performer) throws Exception {
		Check check = getCheck(checkID);
		return saveCheckRules(check,selectedRules,notes,userNotes,performer);
	}
	
	public Check saveCheckRules(Check check, String[] selectedRules, String notes, String userNotes, User performer) throws Exception {
		boolean rule1 = false,rule2 = false,rule3 = false,rule4 = false,rule5 = false;
		if ( selectedRules != null ) {
			for (int i = 0; i < selectedRules.length; i++) {
				int rule = Integer.parseInt(selectedRules[i]);
				switch (rule) {
					case 1 :
						rule1 = true;
						break;
					case 2 :
						rule2 = true;
						break;
					case 3 :
						rule3 = true;
						break;
					case 4 :
						rule4 = true;
						break;
					case 5 :
						rule5 = true;
						break;
				}
			}
		}

		check.setNotes(notes);
		check.setUserNotes(userNotes);
		check.setRule1(rule1);
		check.setRule2(rule2);
		check.setRule3(rule3);
		check.setRule4(rule4);
		check.setRule5(rule5);
		changeCaseStatus(check, check.getCaseStatus().getPrimaryKey().toString(), performer);
		return check;
	}

	public SchoolType getSchoolType(int schoolTypeId) throws Exception {
		try {
			SchoolBusiness schoolBusiness = (SchoolBusiness) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
			return schoolBusiness.getSchoolType(new Integer(schoolTypeId));
		} catch (EJBException e) {
			return null;
		}
	}

	public User getUserById(int userId) throws Exception {
		try {
			return getUserBusiness().getUser(userId);
		} catch (EJBException e) {
			return null;
		}
	}

	public User getUserByPersonalId(String personalID) throws Exception {
		return getUserBusiness().getUserHome().findByPersonalID(personalID);
	}

	public Address getUserAddress(User user) {
		try {
			return getUserBusiness().getUserAddress1(((Integer) user.getPrimaryKey()).intValue());
		} catch (Exception e) {
			return null;
		}
	}

	public PostalCode getUserPostalCode(User user) {
		try {
			return getUserBusiness().getUserAddress1(((Integer) user.getPrimaryKey()).intValue()).getPostalCode();
		} catch (Exception e) {
			return null;
		}
	}

	public User getParent(User child) throws RemoteException{
		try {
			Collection custodians = getMemberFamilyLogic().getCustodiansFor(child);
			Iterator iter = custodians.iterator();
			while (iter.hasNext()) {
				return ((User) iter.next());
			}
			return null;
		}
		catch (NoCustodianFound ncf) {
			return null;
		}
	}
	
	public void approveCheck(Check check,String subject,String body,User performer) throws Exception {
		changeCaseStatus(check, this.getCaseStatusGranted().getPrimaryKey().toString(), performer);
		this.createGrantedCheck(check);
		sendMessageToCitizen(check,getUserID(check),subject,body);
		sendMessageToArchive(check,getUserID(check),subject,body);
		sendMessageToPrinter(check,getUserID(check),subject,body);
	}

	public void retrialCheck(Check check,String subject,String body,User performer) throws Exception {
		changeCaseStatus(check, this.getCaseStatusReview().getPrimaryKey().toString(), performer);
		sendMessageToCitizen(check,getUserID(check),subject,body+"\n\n"+check.getUserNotes());
	}

	private UserBusiness getUserBusiness() throws RemoteException {
		return (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
	}

	private MessageBusiness getMessageBusiness() throws RemoteException {
		return (MessageBusiness) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), MessageBusiness.class);
	}
	
	private MemberFamilyLogic getMemberFamilyLogic() throws RemoteException {
		return (MemberFamilyLogic) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), MemberFamilyLogic.class);
	}
	
	public int getCheckFee() {
		try {
			return new Integer(getIWApplicationContext().getSystemProperties().getProperties(PROPERTIES_CHILD_CARE).getProperty(PROPERTY_CHECK_FEE)).intValue();
		}
		catch (NullPointerException npe) {
			return -1;
		}
		catch (NumberFormatException nfe) {
			return -1;
		}
	}
	
	public int getCheckAmount() {
		try {
			return new Integer(getIWApplicationContext().getSystemProperties().getProperties(PROPERTIES_CHILD_CARE).getProperty(PROPERTY_CHECK_AMOUNT)).intValue();
		}
		catch (NullPointerException npe) {
			return -1;
		}
		catch (NumberFormatException nfe) {
			return -1;
		}
	}
	
	public int getMethodUser() {
		return METHOD_USER;
	}
	
	public int getMethodSystem() {
		return METHOD_SYSTEM;	
	}
	
	public Collection findAllApprovedChecksByUser(User user) {
		try {
			return getCheckHome().findApprovedChecksByUser(user);
		}
		catch (RemoteException e) {
		}
		catch (FinderException e) {
		}
		
		return null;
	}
	
	public Collection findAllChecksByUser(User user) {
		try {
			return getCheckHome().findChecksByUser(user);
		}
		catch (RemoteException e) {
		}
		catch (FinderException e) {
		}
		
		return null;
	}
	
	public int hasChildApprovedCheck(int childID) throws RemoteException {
		/*Collection checks = findAllApprovedChecksByUser(user);
		if (checks != null && !checks.isEmpty()) {
			Iterator iter = checks.iterator();
			while (iter.hasNext()) {
				Check element = (Check) iter.next();
				if (element.getChildId() == childID)
					return ((Integer)element.getPrimaryKey()).intValue();
			}	
		}
		return -1;*/
		try {
			GrantedCheck check = getGrantedCheckHome().findChecksByUser(childID);
			return ((Integer)check.getPrimaryKey()).intValue();
		}
		catch (FinderException fe) {
			return -1;
		}
	}
	
	public GrantedCheck getGrantedCheckByChild(User child) throws RemoteException {
		try {
			return getGrantedCheckHome().findChecksByUser(child);
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public GrantedCheck getGrantedCheck(int checkID) throws RemoteException {
		try {
			return getGrantedCheckHome().findByPrimaryKey(new Integer(checkID));
		}
		catch (FinderException fe) {
			return null;
		}
	}
	
	public int createGrantedCheck(User child) throws CreateException,RemoteException {
		GrantedCheckHome home = (GrantedCheckHome) com.idega.data.IDOLookup.getHome(GrantedCheck.class);
		GrantedCheck grantedCheck = home.create();
		grantedCheck.setChild(child);
			
		Timestamp now = IWTimestamp.getTimestampRightNow();
		grantedCheck.setDateGranted(now);
		
		grantedCheck.store();
		return ((Integer)grantedCheck.getPrimaryKey()).intValue();
	}
	
	public int createGrantedCheck(Check check) throws CreateException,RemoteException {
		GrantedCheckHome home = (GrantedCheckHome) com.idega.data.IDOLookup.getHome(GrantedCheck.class);
		GrantedCheck grantedCheck = home.create();
		grantedCheck.setChildId(check.getChildId());
		grantedCheck.setCheck(check);
			
		Timestamp now = IWTimestamp.getTimestampRightNow();
		grantedCheck.setDateGranted(now);
		
		grantedCheck.store();
		return ((Integer)grantedCheck.getPrimaryKey()).intValue();
	}
	
	public boolean hasGrantedCheck(User child) throws RemoteException {
		try {
			GrantedCheckHome home = (GrantedCheckHome) com.idega.data.IDOLookup.getHome(GrantedCheck.class);
			GrantedCheck check = home.findChecksByUser(child);
			if (check != null)
				return true;
			return false;
		}
		catch (FinderException fe) {
			return false;
		}
	}
	
	public Check getCheckForChild(User child) throws RemoteException {
		try {
			return getCheckHome().findCheckForChild(((Integer)child.getPrimaryKey()).intValue());
		}
		catch (FinderException fe) {
			return null;
		}
	}
}