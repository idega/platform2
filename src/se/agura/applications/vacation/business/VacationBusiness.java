/*
 * $Id: VacationBusiness.java,v 1.2 2004/12/06 21:30:34 laddi Exp $
 * Created on 5.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.agura.applications.vacation.business;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.agura.applications.vacation.data.VacationRequest;
import se.agura.applications.vacation.data.VacationType;

import com.idega.block.process.business.CaseBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2004/12/06 21:30:34 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public interface VacationBusiness extends CaseBusiness {

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#getVacationRequest
	 */
	public VacationRequest getVacationRequest(Object primaryKey) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#getVacationType
	 */
	public VacationType getVacationType(Object primaryKey) throws FinderException, java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#getVacationTimes
	 */
	public Collection getVacationTimes(VacationRequest vacation) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#storeApplication
	 */
	public void storeApplication(User user, Date fromDate, Date toDate, int ordinaryWorkingHours, VacationType type, String[] workingHours, Collection extraInfo, String comment) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#storeApplication
	 */
	public void storeApplication(Object pk, User user, Date fromDate, Date toDate, int ordinaryWorkingHours, VacationType type, String[] workingHours, Collection extraInfo, String comment) throws CreateException, java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#approveApplication
	 */
	public void approveApplication(VacationRequest vacation, User performer, String comment) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#rejectApplication
	 */
	public void rejectApplication(VacationRequest vacation, User performer, String comment) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#forwardApplication
	 */
	public void forwardApplication(VacationRequest vacation, User performer, Group handler, String comment) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#getVacationTypes
	 */
	public Collection getVacationTypes() throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#getLogs
	 */
	public Collection getLogs(VacationRequest vacation) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#getExtraVacationTypeInformation
	 */
	public Map getExtraVacationTypeInformation(VacationType type) throws java.rmi.RemoteException;

	/**
	 * @see se.agura.applications.vacation.business.VacationBusinessBean#getExtraInformationType
	 */
	public String getExtraInformationType(VacationType type, String key) throws java.rmi.RemoteException;

}
