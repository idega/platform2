/*
 * $Id: CareBusinessBean.java,v 1.2 2004/10/14 13:42:44 thomas Exp $
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
 *  Last modified: $Date: 2004/10/14 13:42:44 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public class CareBusinessBean extends IBOServiceBean  implements CareBusiness{
	
	private SchoolBusiness schoolBusiness = null;
	
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

	private SchoolBusiness getSchoolBusiness() throws RemoteException {
		if (schoolBusiness == null) {
			schoolBusiness = (SchoolBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), SchoolBusiness.class);
		}
		return schoolBusiness;
	}
}
