/*
 * $Id: ApplicationFormHelper.java,v 1.4 2002/03/18 19:59:37 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import com.idega.presentation.IWContext;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import com.idega.block.application.presentation.ApplicationForm;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationFormHelper {
  public static void saveApplicantInformation(IWContext iwc) {
    String firstName = iwc.getParameter(ApplicationForm.APP_FIRST_NAME);
    String middleName = iwc.getParameter(ApplicationForm.APP_MIDDLE_NAME);
    String lastName = iwc.getParameter(ApplicationForm.APP_LAST_NAME);
    String ssn = iwc.getParameter(ApplicationForm.APP_SSN);
    String legalResidence = iwc.getParameter(ApplicationForm.APP_LEGAL_RESIDENCE);
    String residence = iwc.getParameter(ApplicationForm.APP_RESIDENCE);
    String residencePhone = iwc.getParameter(ApplicationForm.APP_PHONE);
    String po = iwc.getParameter(ApplicationForm.APP_PO);

    Applicant applicant = new Applicant();
    applicant.setFirstName(firstName);
    applicant.setMiddleName(middleName);
    applicant.setLastName(lastName);
    applicant.setSSN(ssn);
    applicant.setLegalResidence(legalResidence);
    applicant.setResidence(residence);
    applicant.setResidencePhone(residencePhone);
    applicant.setPO(po);

    iwc.setSessionAttribute("applicant",applicant);
  }

  public static String saveDataToDB(IWContext iwc) {
    Applicant applicant = (Applicant)iwc.getSessionAttribute("applicant");
    Application application = (Application)iwc.getSessionAttribute("application");

    String string = "";

    try {
      applicant.insert();

      application.setApplicantId(applicant.getID());
      application.insert();
    }
    catch(SQLException e) {
      System.err.println(e.toString());
      return(null);
    }
    finally {
      iwc.removeSessionAttribute("applicant");
      iwc.removeSessionAttribute("application");
    }

    return(string);
  }

  public static void saveSubject(IWContext iwc) {
    String subject = (String)iwc.getParameter("subject");
    Application application = new Application();
    application.setSubjectId(Integer.parseInt(subject));
    application.setSubmitted(idegaTimestamp.getTimestampRightNow());
    application.setStatusSubmitted();
    application.setStatusChanged(idegaTimestamp.getTimestampRightNow());
    iwc.setSessionAttribute("application",application);
  }
}