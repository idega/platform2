/*
 * Created on 13.10.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.care.business;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;

/**
 * @author laddi
 */
public class ProviderSessionBean extends IBOSessionBean implements ProviderSession {

	private final String PARAMETER_SEASON_ID = "pr_season_id";
	private final String PARAMETER_YEAR_ID = "pr_year_id";
	private final String PARAMETER_STUDY_PATH_ID = "pr_study_path_id";
	private final String PARAMETER_PROVIDER_ID = "pr_prov_id";
	
	protected int _providerID = -1;
	protected School _provider;
	protected int _userID = -1;
	protected int _seasonID = -1;
	protected int _yearID = -1;
	protected int _studyPathID = -1;

	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public int getProviderID() {
		if (getUserContext().isLoggedOn()) {
			User user = getUserContext().getCurrentUser();
			int userID = ((Integer)user.getPrimaryKey()).intValue();
			
			if (_userID == userID) {
				if (_providerID != -1) {
					return _providerID;
				}
				else {
					return getProviderIDFromUser(user);
				}
			}
			else {
				_userID = userID;
				return getProviderIDFromUser(user);
			}
		}
		else {
			return -1;	
		}
	}
	
	private int getProviderIDFromUser(User user) {
		_providerID = -1;
		_provider = null;
		if (user != null) {
			try {
				School school = getUserBusiness().getProviderForUser(user);
				if (school != null) {
					_provider = school;
					_providerID = ((Integer) school.getPrimaryKey()).intValue();
				}
			}
			catch (FinderException fe) {
				_provider = null;
				_providerID = -1;
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re.getMessage());
			}
		}
		return _providerID;
	}

	public CommuneUserBusiness getUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneUserBusiness.class);
	}
	
	/**
	 * @return Returns the provider.
	 */
	public School getProvider() throws FinderException {
		if (getProviderID() != -1 && _provider != null)
			return this._provider;
		throw new FinderException("No provider found for this user.");
	}
	
	/**
	 * @param providerID The providerID to set.
	 */
	public void setProviderID(int providerID) {
		this._providerID = providerID;
	}
	
	public String getParameterSeasonID() {
		return PARAMETER_SEASON_ID;
	}

	public String getParameterYearID() {
		return PARAMETER_YEAR_ID;
	}
	
	public String getParameterStudyPathID() {
		return PARAMETER_STUDY_PATH_ID;
	}
	
	public String getParameterProviderID() {
		return PARAMETER_PROVIDER_ID;
	}
	
	/**
	 * @return Returns the seasonID.
	 */
	public int getSeasonID() {
		return this._seasonID;
	}
	
	/**
	 * @param seasonID The seasonID to set.
	 */
	public void setSeasonID(int seasonID) {
		this._seasonID = seasonID;
	}
	
	/**
	 * @return Returns the yearID.
	 */
	public int getYearID() {
		return this._yearID;
	}
	
	/**
	 * @param yearID The yearID to set.
	 */
	public void setYearID(int yearID) {
		this._yearID = yearID;
	}
	/**
	 * @return Returns the _studyPathID.
	 */
	public int getStudyPathID() {
		return this._studyPathID;
	}
	/**
	 * @param pathID The _studyPathID to set.
	 */
	public void setStudyPathID(int pathID) {
		this._studyPathID = pathID;
	}
}