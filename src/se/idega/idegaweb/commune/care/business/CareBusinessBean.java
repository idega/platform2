/*
 * $Id: CareBusinessBean.java,v 1.9 2005/08/09 16:34:50 laddi Exp $
 * Created on Oct 13, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.check.data.GrantedCheck;
import se.idega.idegaweb.commune.care.check.data.GrantedCheckHome;
import se.idega.idegaweb.commune.care.data.CurrentSchoolSeason;
import se.idega.idegaweb.commune.care.data.CurrentSchoolSeasonHome;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2005/08/09 16:34:50 $ by $Author: laddi $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.9 $
 */
public class CareBusinessBean extends IBOServiceBean  implements CareBusiness{
	
	private SchoolBusiness schoolBusiness = null;
	private UserBusiness userBusiness = null;
	
	public School getProviderForUser(User user) throws FinderException, RemoteException {
		Group primaryGroup = user.getPrimaryGroup();
		SchoolBusiness schoolBuiz = getSchoolBusiness();
		try {
			if (primaryGroup.equals(schoolBuiz.getRootProviderAdministratorGroup()) || primaryGroup.equals(schoolBuiz.getRootSchoolAdministratorGroup()) || primaryGroup.equals(schoolBuiz.getRootMusicSchoolAdministratorGroup())) {
				SchoolUserBusiness sub = (SchoolUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolUserBusiness.class);
				Collection schoolIds = sub.getSchools(user);
				if (!schoolIds.isEmpty()) {
					Iterator iter = schoolIds.iterator();
					while (iter.hasNext()) {
						School school = sub.getSchoolHome().findByPrimaryKey(iter.next());
						return school;
					}
				}
			}
		}
		catch (CreateException ce) {
			ce.printStackTrace();
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
		catch (FinderException e) {
			Collection schools;
			try {
				schools = ((SchoolBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), SchoolBusiness.class)).getSchoolHome().findAllBySchoolGroup(user);
			}
			catch (RemoteException e1) {
				throw new IBORuntimeException(e1.getMessage());
			}
			if (!schools.isEmpty()) {
				Iterator iter = schools.iterator();
				while (iter.hasNext()) {
					return (School) iter.next();
				}
			}
		}
		throw new FinderException("No provider found for user: "+user.getPrimaryKey().toString());
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
	
	public SchoolSeason getCurrentSeason() throws java.rmi.RemoteException, javax.ejb.FinderException {
		CurrentSchoolSeason season = getCurrentSchoolSeasonHome().findCurrentSeason();
		return getSchoolSeasonHome().findByPrimaryKey(season.getCurrent());
	}
	
	public CurrentSchoolSeasonHome getCurrentSchoolSeasonHome() throws java.rmi.RemoteException {
		return (CurrentSchoolSeasonHome) this.getIDOHome(CurrentSchoolSeason.class);
	}
	
	public SchoolSeasonHome getSchoolSeasonHome() throws java.rmi.RemoteException {
		return (SchoolSeasonHome) this.getIDOHome(SchoolSeason.class);
	}
	

	public Map getStudentList(Collection students) throws RemoteException {
		HashMap coll = new HashMap();

		if (!students.isEmpty()) {
			Collection users = getUserBusiness().getUsers(this.getUserIDsFromClassMembers(students));
			User user;

			Iterator iter = users.iterator();
			while (iter.hasNext()) {
				user = (User) iter.next();
				coll.put(user.getPrimaryKey(), user);
			}
		}

		return coll;
	}
	
	private String[] getUserIDsFromClassMembers(Collection classMembers) {
		if (classMembers != null) {
			String[] userIDs = new String[classMembers.size()];
			SchoolClassMember classMember;

			int a = 0;
			Iterator iter = classMembers.iterator();
			while (iter.hasNext()) {
				classMember = (SchoolClassMember) iter.next();
				userIDs[a] = String.valueOf(classMember.getClassMemberId());
				a++;
			}
			return userIDs;
		}
		return null;
	}
	
	private SchoolBusiness getSchoolBusiness() throws RemoteException {
		if (schoolBusiness == null) {
			schoolBusiness = (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		}
		return schoolBusiness;
	}

	private UserBusiness getUserBusiness() throws RemoteException {
		if (userBusiness == null) {
			userBusiness = (UserBusiness) getServiceInstance(UserBusiness.class);
		}
		return userBusiness;
	}

	public void storeChildInformation(User child, Boolean growthDeviation, String growthDeviationDetails, Boolean allergies, String allergiesDetails, String lastCareProvider, boolean canContactLastProvider, String otherInformation) {
		if (growthDeviation != null) {
			child.setMetaData(CareConstants.METADATA_GROWTH_DEVIATION, growthDeviation.toString());
		}
		else {
			child.removeMetaData(CareConstants.METADATA_GROWTH_DEVIATION);
		}
		
		if (growthDeviationDetails != null && growthDeviationDetails.length() > 0) {
			child.setMetaData(CareConstants.METADATA_GROWTH_DEVIATION_DETAILS, growthDeviationDetails);
		}
		else {
			child.removeMetaData(CareConstants.METADATA_GROWTH_DEVIATION_DETAILS);
		}
		
		if (allergies != null) {
			child.setMetaData(CareConstants.METADATA_ALLERGIES, allergies.toString());
		}
		else {
			child.removeMetaData(CareConstants.METADATA_ALLERGIES);
		}
		
		if (allergiesDetails != null && allergiesDetails.length() > 0) {
			child.setMetaData(CareConstants.METADATA_ALLERGIES_DETAILS, allergiesDetails);
		}
		else {
			child.removeMetaData(CareConstants.METADATA_ALLERGIES_DETAILS);
		}
		
		if (lastCareProvider != null && lastCareProvider.length() > 0) {
			child.setMetaData(CareConstants.METADATA_LAST_CARE_PROVIDER, lastCareProvider);
		}
		else {
			child.removeMetaData(CareConstants.METADATA_LAST_CARE_PROVIDER);
		}
		
		child.setMetaData(CareConstants.METADATA_CAN_CONTACT_LAST_PROVIDER, String.valueOf(canContactLastProvider));
		
		if (otherInformation != null && otherInformation.length() > 0) {
			child.setMetaData(CareConstants.METADATA_OTHER_INFORMATION, otherInformation);
		}
		else {
			child.removeMetaData(CareConstants.METADATA_OTHER_INFORMATION);
		}
		
		child.store();
	}
	
	public Boolean hasGrowthDeviation(User child) {
		String meta = child.getMetaData(CareConstants.METADATA_GROWTH_DEVIATION);
		if (meta != null) {
			return new Boolean(meta);
		}
		return null;
	}

	public String getGrowthDeviationDetails(User child) {
		return child.getMetaData(CareConstants.METADATA_GROWTH_DEVIATION_DETAILS);
	}

	public Boolean hasAllergies(User child) {
		String meta = child.getMetaData(CareConstants.METADATA_ALLERGIES);
		if (meta != null) {
			return new Boolean(meta);
		}
		return null;
	}

	public String getAllergiesDetails(User child) {
		return child.getMetaData(CareConstants.METADATA_ALLERGIES_DETAILS);
	}

	public String getLastCareProvider(User child) {
		return child.getMetaData(CareConstants.METADATA_LAST_CARE_PROVIDER);
	}

	public boolean canContactLastCareProvider(User child) {
		String meta = child.getMetaData(CareConstants.METADATA_CAN_CONTACT_LAST_PROVIDER);
		if (meta != null) {
			return new Boolean(meta).booleanValue();
		}
		return false;
	}
	
	public String getOtherInformation(User child) {
		return child.getMetaData(CareConstants.METADATA_OTHER_INFORMATION);
	}


	public void storeExtraCustodian(User child, User custodian, String relation, String homePhone, String workPhone, String mobilePhone, String email) {
		updateUserInfo(custodian, homePhone, workPhone, mobilePhone, email);
		child.setMetaData(CareConstants.METADATA_OTHER_CUSTODIAN, custodian.getPrimaryKey().toString(), "com.idega.user.data.User");
		if (relation != null && relation.length() > 0) {
			storeUserRelation(child, custodian, relation);
		}
	}
	
	public User getExtraCustodian(User child) {
		String custodianPK = child.getMetaData(CareConstants.METADATA_OTHER_CUSTODIAN);
		if (custodianPK != null) {
			try {
				return getUserBusiness().getUser(new Integer(custodianPK));
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
		}
		return null;
	}
	
	public List getRelatives(User child) {
		List relatives = new ArrayList();
		
		try {
			String relativePK = child.getMetaData(CareConstants.METADATA_RELATIVE_1);
			if (relativePK != null) {
				User relative = getUserBusiness().getUser(new Integer(relativePK));
				if (relative != null) {
					relatives.add(relative);
				}
			}
			relativePK = child.getMetaData(CareConstants.METADATA_RELATIVE_2);
			if (relativePK != null) {
				User relative = getUserBusiness().getUser(new Integer(relativePK));
				if (relative != null) {
					relatives.add(relative);
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		
		return relatives;
	}
	
	public void storeRelative(User child, User relative, String relation, int number, String homePhone, String workPhone, String mobilePhone, String email) {
		if (number > 2 || number < 1) {
			return;
		}
		
		updateUserInfo(relative, homePhone, workPhone, mobilePhone, email);
		child.setMetaData(number == 1 ? CareConstants.METADATA_RELATIVE_1 : CareConstants.METADATA_RELATIVE_2, relative.getPrimaryKey().toString(), "com.idega.user.data.User");
		if (relation != null && relation.length() > 0) {
			storeUserRelation(child, relative, relation);
		}
	}
	
	public void storeUserRelation(User child, User relatedUser, String relation) {
		child.setMetaData(CareConstants.METADATA_RELATION + relatedUser.getPrimaryKey().toString(), relation, "java.lang.String");
	}
	
	public String getUserRelation(User child, User relatedUser) {
		return child.getMetaData(CareConstants.METADATA_RELATION + relatedUser.getPrimaryKey().toString());
	}
	
	public void updateUserInfo(User user, String homePhone, String workPhone, String mobilePhone, String email) {
		try {
			if (email != null && email.length() > 0) {
				try {
					getUserBusiness().updateUserMail(user, email);
				}
				catch (CreateException ce) {
					ce.printStackTrace();
				}
			}
			if (homePhone != null && homePhone.length() > 0) {
				getUserBusiness().updateUserHomePhone(user, homePhone);
			}
			if (workPhone != null && workPhone.length() > 0) {
				getUserBusiness().updateUserWorkPhone(user, workPhone);
			}
			if (mobilePhone != null && mobilePhone.length() > 0) {
				getUserBusiness().updateUserMobilePhone(user, mobilePhone);
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
}