/*
 * Created on Apr 5, 2004
 *
 */
package com.idega.block.application.business;

import java.rmi.RemoteException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationHome;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.ApplicationSubjectHome;
import com.idega.block.application.data.ApplicationSubjectInfo;
import com.idega.block.application.data.ApplicationSubjectInfoHome;
import com.idega.business.IBOServiceBean;

/**
 * ApplicationServiceBean
 * @author aron 
 * @version 1.0
 */
public class ApplicationServiceBean extends IBOServiceBean {
	
	public ApplicationHome getApplicationHome() throws RemoteException{
		return (ApplicationHome ) getIDOHome(Application.class);
	}
	
	public ApplicantHome getApplicantHome() throws RemoteException{
		return (ApplicantHome) getIDOHome(Applicant.class);
	}
	
	public ApplicationSubjectHome getSubjectHome() throws RemoteException{
		return (ApplicationSubjectHome) getIDOHome(ApplicationSubject.class);
	}
	
	public ApplicationSubjectInfoHome getSubjectInfoHome() throws RemoteException{
		return (ApplicationSubjectInfoHome) getIDOHome(ApplicationSubjectInfo.class);
	}
	
	
}
