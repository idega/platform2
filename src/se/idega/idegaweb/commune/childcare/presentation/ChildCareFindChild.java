/*
 * Created on 30.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.business.ChildCareSession;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;
import se.idega.idegaweb.commune.presentation.CommuneUserFinder;

import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

/**
 * @author laddi
 */
public class ChildCareFindChild extends CommuneUserFinder {

	/**
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#addUser(com.idega.user.data.User)
	 */
	public boolean addUser(IWContext iwc, User user) {
		try {
			return getChildCareBusiness(iwc).hasApplications(((Integer)user.getPrimaryKey()).intValue());
		}
		catch (RemoteException e) {
			return false;
		}
	}

	/**
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getParameterName()
	 */
	public String getParameterName(IWContext iwc) {
		try {
			return getChildCareSession(iwc).getParameterUserID();
		}
		catch (RemoteException e) {
			return "cc_user_id";
		}
	}

	/**
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getEventListener()
	 */
	public Class getEventListener() {
		return ChildCareEventListener.class;
	}

	/**
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getSubmitDisplay()
	 */
	public String getSubmitDisplay() {
		return localize("child_care.show_applications","Show applications");
	}

	/**
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getNoUserFoundString()
	 */
	public String getNoUserFoundString() {
		return localize("child_care.no_child_found","No child found");
	}

	/**
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getFoundUsersString()
	 */
	public String getFoundUsersString() {
		return localize("child_care.found_children","Found children");
	}

	private ChildCareBusiness getChildCareBusiness(IWContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);	
	}
	
	private ChildCareSession getChildCareSession(IWContext iwc) throws RemoteException {
		return (ChildCareSession) IBOLookup.getSessionInstance(iwc, ChildCareSession.class);	
	}	
}