/*
 * $Id: CareBusiness.java,v 1.1 2004/10/13 15:29:57 thomas Exp $
 * Created on Oct 13, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOService;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/10/13 15:29:57 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public interface CareBusiness extends IBOService {

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getProviderForUser
	 */
	public School getProviderForUser(User user) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getRootProviderAdministratorGroup
	 */
	public Group getRootProviderAdministratorGroup() throws CreateException, FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getRootSchoolAdministratorGroup
	 */
	public Group getRootSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getRootMusicSchoolAdministratorGroup
	 */
	public Group getRootMusicSchoolAdministratorGroup() throws CreateException, FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getSchoolBusiness
	 */
	public SchoolBusiness getSchoolBusiness() throws RemoteException;
}
