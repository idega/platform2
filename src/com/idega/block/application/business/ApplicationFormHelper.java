/*
 * $Id: ApplicationFormHelper.java,v 1.3 2001/10/05 07:59:57 tryggvil Exp $
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

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationFormHelper {
  public static void saveApplicantInformation(IWContext iwc) {
    String firstName = iwc.getParameter("firstName");
    String middleName = iwc.getParameter("middleName");
    String lastName = iwc.getParameter("lastName");
    String ssn = iwc.getParameter("ssn");
    String legalResidence = iwc.getParameter("legalResidence");
    String residence = iwc.getParameter("residence");
    String residencePhone = iwc.getParameter("residencePhone");
    String po = iwc.getParameter("po");

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