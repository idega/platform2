package se.idega.idegaweb.commune.complaint.business;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.idega.idegaweb.commune.complaint.data.Complaint;
import se.idega.idegaweb.commune.complaint.data.ComplaintHome;
import se.idega.idegaweb.commune.message.business.CommuneMessageBusiness;

import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.CaseCode;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.process.data.CaseStatusHome;
import com.idega.block.process.message.data.Message;
import com.idega.user.data.User;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega software
 * @author
 * @version 1.0
 */

public class ComplaintBusinessBean extends CaseBusinessBean implements ComplaintBusiness {

	public static final String CASE_STATUS_ANSWERED = "KLAR";

	public ComplaintBusinessBean() {
	}

	private ComplaintHome getComplaintHome() throws RemoteException {
		return (ComplaintHome) com.idega.data.IDOLookup.getHome(Complaint.class);
	}

	public Complaint getComplaint(int complaintID) throws FinderException, RemoteException {
		return getComplaintHome().findByPrimaryKey(new Integer(complaintID));
	}

	public Collection findComplaints() throws FinderException, RemoteException {
		return getComplaintHome().findAllComplaints();
	}

	public Collection findUserComplaints(User user) throws FinderException, RemoteException {
		return getComplaintHome().findAllComplaintsByUser(user);
	}

	public Collection findComplaintsForManager(User manager) throws FinderException, RemoteException {
		return getComplaintHome().findAllComplaintsByManager(manager);
	}

	public Collection findComplaintsByStatus(CaseStatus status) throws FinderException, RemoteException {
		return getComplaintHome().findAllComplaintsByStatus(status);
	}

	public Collection findComplaintsByType(CaseCode type) throws FinderException, RemoteException {
		return getComplaintHome().findAllComplaintsByType(type);
	}
	
	public Collection findAllComplaintTypes() throws FinderException,RemoteException {
		Collection collection = getCaseCodeHome().findAllCaseCodes();
		
		CommuneMessageBusiness msg = (CommuneMessageBusiness) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneMessageBusiness.class);
		collection.remove(msg.getCaseCodePrintedLetterMessage());
		//collection.remove(msg.getCaseCodeSystemArchivationMessage());
		collection.remove(msg.getCaseCodeUserMessage());
		
		return collection;
	}

	public void createComplaint(String complaintText, String description, CaseCode type, User user) throws CreateException, RemoteException {
		Complaint complaint = getComplaintHome().create();
		complaint.setComplaint(complaintText);
		complaint.setDescription(description);
		if ( type != null )
			complaint.setComplaintType(type);
		complaint.setOwner(user);
		complaint.setCaseStatus(getCaseStatusOpen());
		complaint.store();
	}

	public void answerComplaint(int complaintID, String answerText, User manager) throws FinderException, RemoteException {
		Complaint complaint = getComplaint(complaintID);
		complaint.setAnswer(answerText);
		complaint.setManagerID((Integer) manager.getPrimaryKey());
		complaint.setCaseStatus(getCaseStatusAnswered());
		complaint.store();
	}

	public void forwardComplaint(int complaintID, CaseCode type, User manager) throws FinderException, RemoteException {
		Complaint complaint = getComplaint(complaintID);
		if (manager != null) {
			complaint.setManagerID((Integer) manager.getPrimaryKey());
		}
		if (type != null) {
			complaint.setComplaintType(type);
		}
		complaint.store();
	}

	public void sendMessageToCitizen(Complaint complaint, int userID, String subject, String body) throws CreateException,RemoteException {
		CommuneMessageBusiness msg = (CommuneMessageBusiness) com.idega.business.IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneMessageBusiness.class);
		Message message = msg.createUserMessage(userID, subject, body);
		message.setParentCase(complaint);
		message.store();
	}

	private CaseStatus getCaseStatusAnswered() throws FinderException, RemoteException {
		return ((CaseStatusHome) com.idega.data.IDOLookup.getHome(CaseStatus.class)).findByPrimaryKey(CASE_STATUS_ANSWERED);
	}
}