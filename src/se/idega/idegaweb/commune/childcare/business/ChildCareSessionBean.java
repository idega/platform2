package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class ChildCareSessionBean extends IBOSessionBean implements ChildCareSession {

	protected static final String PARAMETER_CHILD_CARE_ID = "cc_c_c_id";
	protected static final String PARAMETER_SCHOOL_TYPE_ID = "cc_school_type_id";	
	protected static final String PARAMETER_GROUP_ID = "cc_group_id";
	protected static final String PARAMETER_USER_ID = "cc_user_id";
	protected static final String PARAMETER_CHECK_ID = "cc_check_id";
	protected static final String PARAMETER_APPLICATION_ID = "cc_application_id";
	protected static final String PARAMETER_FROM = "cc_from";
	protected static final String PARAMETER_TO = "cc_to";
	protected static final String PARAMETER_SORT_BY = "cc_sort_by";
	protected static final String PARAMETER_SEASON = "cc_season";
	protected static final String PARAMETER_STATUS = "cc_status";

	protected int _childcareID = -1;
	protected School _provider;
	protected int _userID = -1;
	protected int _childID = -1;
	protected int _applicationID = -1;
	protected int _schoolTypeID = -1;
	protected int _groupID = -1;
	protected int _checkID = -1;
	protected int _seasonID = -1;
	protected int _sortBy = -1;
	protected IWTimestamp fromTimestamp;
	protected IWTimestamp toTimestamp;
	protected Boolean hasPrognosis;
	protected boolean _outDatedPrognosis = false;
	protected String _status;

	public CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneUserBusiness.class);
	}
	
	public ChildCareBusiness getChildCareBusiness() throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ChildCareBusiness.class);
	}
	
	public School getProvider() {
		return _provider;
	}
	
	public boolean hasPrognosis() throws RemoteException {
		if (hasPrognosis == null) {
			setHasPrognosis();
		}
		return hasPrognosis.booleanValue();
	}
	
	private void setHasPrognosis() throws RemoteException {
		ChildCarePrognosis prognosis = getChildCareBusiness().getPrognosis(getChildCareID());
		if (prognosis != null) {
			IWTimestamp stamp = new IWTimestamp();
			IWTimestamp lastUpdated = new IWTimestamp(prognosis.getUpdatedDate());
			if (IWTimestamp.getDaysBetween(lastUpdated, stamp) > 90) {
				hasPrognosis = new Boolean(false);
				_outDatedPrognosis = true;
			}
			else
				hasPrognosis = new Boolean(true);
		}
		else {
			hasPrognosis = new Boolean(false);
		}
	}
	
	public void setHasPrognosis(boolean hasPrognosis) {
		this.hasPrognosis = new Boolean(hasPrognosis);
	}
	
	public boolean hasOutdatedPrognosis() {
		return _outDatedPrognosis;
	}
	
	public void setHasOutdatedPrognosis(boolean hasOutdatedPrognosis) {
		_outDatedPrognosis = hasOutdatedPrognosis;
	}
	
	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public int getChildCareID() throws RemoteException {
		if (getUserContext().isLoggedOn()) {
			User user = getUserContext().getCurrentUser();
			int userID = ((Integer)user.getPrimaryKey()).intValue();
			
			if (_userID == userID) {
				if (_childcareID != -1) {
					return _childcareID;
				}
				else {
					hasPrognosis = null;
					return getChildCareIDFromUser(user);
				}
			}
			else {
				hasPrognosis = null;
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
				School school = getCommuneUserBusiness().getProviderForUser(user);
				if (school != null) {
					_provider = school;
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

	/**
	 * @return String
	 */
	public String getParameterUserID() {
		return PARAMETER_USER_ID;
	}

	/**
	 * @return String
	 */
	public String getParameterApplicationID() {
		return PARAMETER_APPLICATION_ID;
	}

	/**
	 * @return String
	 */
	public String getParameterSchoolTypeID() {
		return PARAMETER_SCHOOL_TYPE_ID;
	}


	/**
	 * @return String
	 */
	public String getParameterGroupID() {
		return PARAMETER_GROUP_ID;
	}

	/**
	 * @return String
	 */
	public String getParameterCheckID() {
		return PARAMETER_CHECK_ID;
	}

	/**
	 * @return int
	 */
	public int getApplicationID() {
		return _applicationID;
	}

	/**
	 * @return int
	 */
	public int getChildID() {
		return _childID;
	}

	/**
	 * @return int
	 */
	public int getCheckID() {
		return _checkID;
	}

	/**
	 * Sets the applicationID.
	 * @param applicationID The applicationID to set
	 */
	public void setApplicationID(int applicationID) {
		_applicationID = applicationID;
	}

	/**
	 * Sets the childID.
	 * @param childID The childID to set
	 */
	public void setChildID(int childID) {
		_childID = childID;
	}

	/**
	 * Sets the checkID.
	 * @param checkID The checkID to set
	 */
	public void setCheckID(int checkID) {
		_checkID = checkID;
	}

	/**
	 * @return String
	 */
	public String getParameterFrom() {
		return PARAMETER_FROM;
	}

	/**
	 * @return String
	 */
	public String getParameterSeasonID() {
		return PARAMETER_SEASON;
	}

	/**
	 * @return String
	 */
	public String getParameterStatus() {
		return PARAMETER_STATUS;
	}

	/**
	 * @return String
	 */
	public String getParameterSortBy() {
		return PARAMETER_SORT_BY;
	}

	/**
	 * @return String
	 */
	public String getParameterTo() {
		return PARAMETER_TO;
	}

	/**
	 * @return String
	 */
	public String getStatus() {
		return _status;
	}

	/**
	 * @return int
	 */
	public int getSortBy() {
		return _sortBy;
	}

	/**
	 * @return IWTimestamp
	 */
	public IWTimestamp getFromTimestamp() {
		return fromTimestamp;
	}

	/**
	 * @return IWTimestamp
	 */
	public IWTimestamp getToTimestamp() {
		return toTimestamp;
	}

	/**
	 * Sets the status.
	 * @param status The status to set
	 */
	public void setStatus(String status) {
		_status = status;
	}

	/**
	 * Sets the sortBy.
	 * @param sortBy The sortBy to set
	 */
	public void setSortBy(int sortBy) {
		_sortBy = sortBy;
	}

	/**
	 * Sets the fromTimestamp.
	 * @param fromTimestamp The fromTimestamp to set
	 */
	public void setFromTimestamp(String timestamp) {
		if (timestamp != null)
			this.fromTimestamp = new IWTimestamp(timestamp);
		else
			this.fromTimestamp = null;
	}

	/**
	 * Sets the toTimestamp.
	 * @param toTimestamp The toTimestamp to set
	 */
	public void setToTimestamp(String timestamp) {
		if (timestamp != null)
			this.toTimestamp = new IWTimestamp(timestamp);
		else
			this.toTimestamp = null;
	}


	/**
	 * @return int
	 */
	public int getSchoolTypeID() {
		return _schoolTypeID;
	}

	/**
	 * Sets the groupID.
	 * @param groupID The groupID to set
	 */
	public void setSchoolTypeID(int schTypeID) {
		_schoolTypeID = schTypeID;
	}

	/**
	 * @return int
	 */
	public int getGroupID() {
		return _groupID;
	}

	/**
	 * Sets the groupID.
	 * @param groupID The groupID to set
	 */
	public void setGroupID(int groupID) {
		_groupID = groupID;
	}

	/**
	 * @return
	 */
	public int getSeasonID() {
		return _seasonID;
	}

	/**
	 * @param seasonID
	 */
	public void setSeasonID(int seasonID) {
		_seasonID = seasonID;
	}

}