/*
 * $Id: ApplicationFormHelper.java,v 1.10 2004/06/05 06:16:42 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.util.IWTimestamp;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.idega.block.application.presentation.ApplicationForm;
import com.idega.business.IBOLookup;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationFormHelper {
  public static void saveApplicantInformation(IWContext iwc)throws RemoteException,CreateException {
    String firstName = iwc.getParameter(ApplicationForm.APP_FIRST_NAME);
    String middleName = iwc.getParameter(ApplicationForm.APP_MIDDLE_NAME);
    String lastName = iwc.getParameter(ApplicationForm.APP_LAST_NAME);
    String ssn = iwc.getParameter(ApplicationForm.APP_SSN);
    String legalResidence = iwc.getParameter(ApplicationForm.APP_LEGAL_RESIDENCE);
    String residence = iwc.getParameter(ApplicationForm.APP_RESIDENCE);
    String residencePhone = iwc.getParameter(ApplicationForm.APP_PHONE);
    String mobilePhone = iwc.getParameter(ApplicationForm.APP_MOBILE);
    String po = iwc.getParameter(ApplicationForm.APP_PO);

    Applicant applicant = getApplicationService(iwc).getApplicantHome().create();
    applicant.setFirstName(firstName);
    applicant.setMiddleName(middleName);
    applicant.setLastName(lastName);
    applicant.setSSN(ssn);
    applicant.setLegalResidence(legalResidence);
    applicant.setResidence(residence);
    applicant.setResidencePhone(residencePhone);
    applicant.setMobilePhone(mobilePhone);
    applicant.setPO(po);
    applicant.setStatus("S");

    iwc.setSessionAttribute("applicant",applicant);
  }

  public static String saveDataToDB(IWContext iwc) {
    Applicant applicant = (Applicant)iwc.getSessionAttribute("applicant");
    Application application = (Application)iwc.getSessionAttribute("application");

    String string = "";

    try {
      applicant.store();

      application.setApplicantId(((Integer)applicant.getPrimaryKey()).intValue());
      application.store();
    }
    catch(Exception e) {
      System.err.println(e.toString());
      return(null);
    }
    finally {
      iwc.removeSessionAttribute("applicant");
      iwc.removeSessionAttribute("application");
    }

    return(string);
  }

  public static void saveSubject(IWContext iwc)throws RemoteException,CreateException {
    String subject = (String)iwc.getParameter("subject");
    Application application = getApplicationService(iwc).getApplicationHome().create();
    application.setSubjectId(Integer.parseInt(subject));
    application.setSubmitted(IWTimestamp.getTimestampRightNow());
    application.setStatusSubmitted();
    application.setStatusChanged(IWTimestamp.getTimestampRightNow());
    iwc.setSessionAttribute("application",application);
  }
  
  public static ApplicationService getApplicationService(IWApplicationContext iwac)throws RemoteException{
  	return (ApplicationService) IBOLookup.getServiceInstance(iwac,ApplicationService.class);
  }
}
