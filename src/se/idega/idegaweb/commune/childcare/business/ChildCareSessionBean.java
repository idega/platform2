package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;

/**
 * @author laddi
 */
public class ChildCareSessionBean extends IBOSessionBean implements ChildCareSession {

	protected static final String PARAMETER_CHILD_CARE_ID = "cc_c_c_id";

	protected int _childcareID = -1;
	protected int _userID = -1;

	public CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneUserBusiness.class);
	}
	
	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public int getChildCareID() throws RemoteException {
		User user = getUserContext().getCurrentUser();
		if (user != null) {
			int userID = ((Integer)user.getPrimaryKey()).intValue();
			
			if (_userID == userID) {
				if (_childcareID != -1) {
					return _childcareID;
				}
				else {
					return getChildCareIDFromUser(user);
				}
			}
			else {
				_userID = userID;
				return getChildCareIDFromUser(user);
			}
		}
		else {
			return -1;	
		}
	}
	
	private int getChildCareIDFromUser(User user) throws RemoteException {
		_childcareID = -1;
		if (user != null) {
			try {
				School school = getCommuneUserBusiness().getFirstManagingSchoolForUser(user);
				if (school != null) {
					_childcareID = ((Integer) school.getPrimaryKey()).intValue();
				}
			}
			catch (FinderException fe) {
				_childcareID = -1;
			}
		}
		return _childcareID;
	}

	/**
	 * @return int
	 */
	public int getUserID() {
		return _userID;
	}

	/**
	 * Sets the childcareID.
	 * @param childcareID The childcareID to set
	 */
	public void setChildCareID(int childcareID) {
		_childcareID = childcareID;
	}

	/**
	 * Sets the userID.
	 * @param userID The userID to set
	 */
	public void setUserID(int userID) {
		_userID = userID;
	}

	/**
	 * @return String
	 */
	public String getParameterChildCareID() {
		return PARAMETER_CHILD_CARE_ID;
	}

}