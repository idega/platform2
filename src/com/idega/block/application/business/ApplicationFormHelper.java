/*
 * $Id: ApplicationFormHelper.java,v 1.2 2001/08/29 22:51:41 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import com.idega.jmodule.object.ModuleInfo;
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
  public static void saveApplicantInformation(ModuleInfo modinfo) {
    String firstName = modinfo.getParameter("firstName");
    String middleName = modinfo.getParameter("middleName");
    String lastName = modinfo.getParameter("lastName");
    String ssn = modinfo.getParameter("ssn");
    String legalResidence = modinfo.getParameter("legalResidence");
    String residence = modinfo.getParameter("residence");
    String residencePhone = modinfo.getParameter("residencePhone");
    String po = modinfo.getParameter("po");

    Applicant applicant = new Applicant();
    applicant.setFirstName(firstName);
    applicant.setMiddleName(middleName);
    applicant.setLastName(lastName);
    applicant.setSSN(ssn);
    applicant.setLegalResidence(legalResidence);
    applicant.setResidence(residence);
    applicant.setResidencePhone(residencePhone);
    applicant.setPO(po);

    modinfo.setSessionAttribute("applicant",applicant);
  }

  public static String saveDataToDB(ModuleInfo modinfo) {
    Applicant applicant = (Applicant)modinfo.getSessionAttribute("applicant");
    Application application = (Application)modinfo.getSessionAttribute("application");

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
      modinfo.removeSessionAttribute("applicant");
      modinfo.removeSessionAttribute("application");
    }

    return(string);
  }

  public static void saveSubject(ModuleInfo modinfo) {
    String subject = (String)modinfo.getParameter("subject");
    Application application = new Application();
    application.setSubjectId(Integer.parseInt(subject));
    application.setSubmitted(idegaTimestamp.getTimestampRightNow());
    application.setStatusSubmitted();
    application.setStatusChanged(idegaTimestamp.getTimestampRightNow());
    modinfo.setSessionAttribute("application",application);
  }
}