package se.idega.idegaweb.commune.childcare.check.business;

import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.school.business.SchoolTypeBusiness;
import com.idega.block.school.data.SchoolType;
import com.idega.core.data.Address;
import com.idega.core.data.PostalCode;
import com.idega.presentation.IWContext;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.childcare.check.data.Check;
import se.idega.idegaweb.commune.childcare.check.data.CheckHome;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.EJBException;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class CheckBusinessBean extends CaseBusinessBean implements CheckBusiness {

	public CheckBusinessBean() {}

	private CheckHome getCheckHome() throws java.rmi.RemoteException {
		return (CheckHome) com.idega.data.IDOLookup.getHome(Check.class);
	}

	public Check getCheck(int checkId) throws Exception {
		return getCheckHome().findByPrimaryKey(new Integer(checkId));
	}

	public Collection findChecks() throws Exception {
		return getCheckHome().findChecks();
	}

	public Collection findUnhandledChecks() throws Exception {
		return getCheckHome().findAllCasesByStatus(getCaseStatusOpen());
	}

	public boolean allRulesVerified(Check check) throws RemoteException {
		boolean rule1 = check.getRule1();
		boolean rule2 = check.getRule2();
		boolean rule3 = check.getRule3();
		boolean rule4 = check.getRule4();
		boolean rule5 = check.getRule5();
		return ( rule1 && rule2 && rule3 && rule4 ) || rule5;
	}

	public void createCheck(int childCareType, int workSituation1, int workSituation2, String motherTongueMotherChild, String motherTongueFatherChild, String motherTongueParents, int childId, int method, int amount, int checkFee, User user, String notes, boolean checkRule1, boolean checkRule2, boolean checkRule3, boolean checkRule4, boolean checkRule5) throws Exception {
		CheckHome home = (CheckHome) com.idega.data.IDOLookup.getHome(Check.class);
		Check check = home.create();
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
		check.setCaseStatus(this.getCaseStatusOpen());

		check.store();
	}

	public void sendMessageToCitizen(IWContext iwc,Check check, int userID, String subject, String body) throws Exception {
		try {
			Message message = getMessageBusiness(iwc).createUserMessage(userID, subject, body);
			message.setParentCase(check);
			message.store();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public void sendMessageToArchive(IWContext iwc,Check check, int userID, String subject, String body) throws Exception {
		try {
			Message message = getMessageBusiness(iwc).createPrintArchivationMessage(userID, subject, body);
			message.setParentCase(check);
			message.store();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public void sendMessageToPrinter(IWContext iwc,Check check, int userID, String subject, String body) throws Exception {
		try {
			Message message = getMessageBusiness(iwc).createPrintedLetterMessage(userID, subject, body);
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
	
	public Check saveCheckRules(int checkId, String[] selectedRules, String notes, int managerId) throws Exception {
		Check check = getCheck(checkId);
		boolean rule1 = false,rule2 = false,rule3 = false,rule4 = false,rule5 = false;
		check.setManagerId(managerId);
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
		check.setRule1(rule1);
		check.setRule2(rule2);
		check.setRule3(rule3);
		check.setRule4(rule4);
		check.setRule5(rule5);
		return check;
	}

	public SchoolType getSchoolType(IWContext iwc, int schoolTypeId) throws Exception {
		try {
			SchoolTypeBusiness schoolTypeBusiness = (SchoolTypeBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, SchoolTypeBusiness.class);
			return schoolTypeBusiness.getSchoolType(new Integer(schoolTypeId));
		} catch (EJBException e) {
			return null;
		}
	}

	public User getUserById(IWContext iwc, int userId) throws Exception {
		try {
			return getUserBusiness(iwc).getUser(userId);
		} catch (EJBException e) {
			return null;
		}
	}

	public User getUserByPersonalId(IWContext iwc, String personalID) throws Exception {
		return getUserBusiness(iwc).getUserHome().findByPersonalID(personalID);
	}

	public Address getUserAddress(IWContext iwc, User user) {
		try {
			return getUserBusiness(iwc).getUserAddress1(((Integer) user.getPrimaryKey()).intValue());
		} catch (Exception e) {
			return null;
		}
	}

	public PostalCode getUserPostalCode(IWContext iwc, User user) {
		try {
			return getUserBusiness(iwc).getUserAddress1(((Integer) user.getPrimaryKey()).intValue()).getPostalCode();
		} catch (Exception e) {
			return null;
		}
	}

	public void commit(Check check) throws Exception {
		check.store();
	}

	public void approveCheck(IWContext iwc,Check check,String subject,String body) throws Exception {
		sendMessageToCitizen(iwc,check,getUserID(check),subject,body);
		sendMessageToArchive(iwc,check,getUserID(check),subject,body);
		sendMessageToPrinter(iwc,check,getUserID(check),subject,body);
		check.setCaseStatus(this.getCaseStatusGranted());
		commit(check);
	}

	public void retrialCheck(IWContext iwc,Check check,String subject,String body) throws Exception {
		check.setCaseStatus(this.getCaseStatusReview());
		commit(check);
	}

	public void saveCheck(Check check) throws Exception {
		check.setCaseStatus(this.getCaseStatusOpen());
		commit(check);
	}

	private UserBusiness getUserBusiness(IWContext iwc) throws Exception {
		return (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
	}

	private MessageBusiness getMessageBusiness(IWContext iwc) throws Exception {
		return (MessageBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MessageBusiness.class);
	}
}