/*
 * Created on Apr 5, 2004
 *
 */
package com.idega.block.application.business;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationHome;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.ApplicationSubjectHome;
import com.idega.block.application.data.ApplicationSubjectInfo;
import com.idega.block.application.data.ApplicationSubjectInfoHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOStoreException;

/**
 * ApplicationServiceBean
 * @author aron 
 * @version 1.0
 */
public class ApplicationServiceBean extends IBOServiceBean implements ApplicationService {
	
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
	
	

	public ApplicationSubject storeApplicationSubject(Integer ID, String sName, java.sql.Date expires,String extra)
		throws RemoteException {

		try {
			ApplicationSubject subject = getSubjectHome().create();

			if (ID != null && ID.intValue() > 0) {
				subject = getSubjectHome().findByPrimaryKey(ID);
			}
			subject.setExpires(expires);
			subject.setDescription(sName);
			if(extra!=null)
				subject.setExtraAttribute(extra);
			subject.store();

			return subject;
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean removeApplicationSubject(Integer id) {
		try {
			getSubjectHome().findByPrimaryKey(id).remove();
			return true;
		}
		catch (Exception ex) {

		}
		return false;
	}
	
	
}
