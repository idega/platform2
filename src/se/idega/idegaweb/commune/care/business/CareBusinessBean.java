/*
 * $Id: CareBusinessBean.java,v 1.6 2004/10/21 11:10:19 thomas Exp $
 * Created on Oct 13, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
 *  Last modified: $Date: 2004/10/21 11:10:19 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.6 $
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


}
