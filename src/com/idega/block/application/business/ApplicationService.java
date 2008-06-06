package com.idega.block.application.business;


import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.ApplicationHome;
import com.idega.block.application.data.ApplicationSubjectInfoHome;
import com.idega.business.IBOService;
import com.idega.block.application.data.ApplicationSubject;
import java.sql.Date;
import java.rmi.RemoteException;
import com.idega.block.application.data.ApplicationSubjectHome;

public interface ApplicationService extends IBOService {
	/**
	 * @see com.idega.block.application.business.ApplicationServiceBean#getApplicationHome
	 */
	public ApplicationHome getApplicationHome() throws RemoteException,
			RemoteException;

	/**
	 * @see com.idega.block.application.business.ApplicationServiceBean#getApplicantHome
	 */
	public ApplicantHome getApplicantHome() throws RemoteException,
			RemoteException;

	/**
	 * @see com.idega.block.application.business.ApplicationServiceBean#getSubjectHome
	 */
	public ApplicationSubjectHome getSubjectHome() throws RemoteException,
			RemoteException;

	/**
	 * @see com.idega.block.application.business.ApplicationServiceBean#getSubjectInfoHome
	 */
	public ApplicationSubjectInfoHome getSubjectInfoHome()
			throws RemoteException, RemoteException;

	/**
	 * @see com.idega.block.application.business.ApplicationServiceBean#getApplicationStatuses
	 */
	public String[] getApplicationStatuses() throws RemoteException;

	/**
	 * @see com.idega.block.application.business.ApplicationServiceBean#storeApplicationSubject
	 */
	public ApplicationSubject storeApplicationSubject(Integer ID, String sName,
			Date starts, Date expires, String extra) throws RemoteException,
			RemoteException;

	/**
	 * @see com.idega.block.application.business.ApplicationServiceBean#removeApplicationSubject
	 */
	public boolean removeApplicationSubject(Integer id) throws RemoteException,
			RemoteException;
}