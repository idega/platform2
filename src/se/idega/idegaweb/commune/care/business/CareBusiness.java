/*
 * $Id: CareBusiness.java,v 1.5 2004/10/21 10:57:27 thomas Exp $
 * Created on Oct 21, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.care.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.data.CurrentSchoolSeasonHome;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolSeasonHome;
import com.idega.business.IBOService;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/10/21 10:57:27 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.5 $
 */
public interface CareBusiness extends IBOService {

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getProviderForUser
	 */
	public School getProviderForUser(User user) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#hasGrantedCheck
	 */
	public boolean hasGrantedCheck(User child) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getCurrentSeason
	 */
	public SchoolSeason getCurrentSeason() throws java.rmi.RemoteException, javax.ejb.FinderException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getCurrentSchoolSeasonHome
	 */
	public CurrentSchoolSeasonHome getCurrentSchoolSeasonHome() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getSchoolSeasonHome
	 */
	public SchoolSeasonHome getSchoolSeasonHome() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.care.business.CareBusinessBean#getStudentList
	 */
	public Map getStudentList(Collection students) throws RemoteException;
}
