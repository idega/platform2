/*
 * $Id: CareBusinessBean.java,v 1.1 2004/10/13 15:29:57 thomas Exp $
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
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/10/13 15:29:57 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public class CareBusinessBean extends IBOServiceBean  implements CareBusiness{
	
	public School getProviderForUser(User user) throws FinderException {
		Group primaryGroup = user.getPrimaryGroup();
		try {
			if (primaryGroup.equals(getRootProviderAdministratorGroup()) || primaryGroup.equals(getRootSchoolAdministratorGroup()) || primaryGroup.equals(getRootMusicSchoolAdministratorGroup())) {
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
	
	/**
	* Returns or creates (if not available) the default usergroup all ChildCare(provider) administors have as their primary group.
	* @throws CreateException if it failed to create the group.
	* @throws FinderException if it failed to locate the group.
	*/
	public Group getRootProviderAdministratorGroup() throws CreateException, FinderException, RemoteException {
		return getSchoolBusiness().getRootProviderAdministratorGroup();
	}
	
	/**
	* Returns or creates (if not available) the default usergroup all school administors have as their primary group.
	* @throws CreateException if it failed to create the group.
	* @throws FinderException if it failed to locate the group.
	*/
	public Group getRootSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException {
		return getSchoolBusiness().getRootSchoolAdministratorGroup();
	}

	public Group getRootMusicSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException {
		return getSchoolBusiness().getRootMusicSchoolAdministratorGroup();
	}
	
	public SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
	}
}
