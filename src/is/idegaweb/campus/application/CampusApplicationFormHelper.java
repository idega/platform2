/*
 * $Id: CampusApplicationFormHelper.java,v 1.2 2001/08/29 22:56:06 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.application;

import com.idega.block.application.business.ApplicationFormHelper;
import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.data.ApartmentType;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.util.CypherText;
import com.idega.util.SendMail;
import com.idega.util.idegaTimestamp;
import is.idegaweb.campus.entity.Applied;
import is.idegaweb.campus.entity.CampusApplication;
import java.sql.SQLException;
import java.util.Vector;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CampusApplicationFormHelper extends ApplicationFormHelper {
  /**
   *
   */
  public static void saveAppliedFor(ModuleInfo modinfo) {
    String key1 = (String)modinfo.getParameter("aprtType");
    String key2 = (String)modinfo.getParameter("aprtType2");
    String key3 = (String)modinfo.getParameter("aprtType3");

    Applied applied1 = null;
    Applied applied2 = null;
    Applied applied3 = null;

    applied1 = new Applied();
    int type = ApartmentTypeComplexHelper.getPartKey(key1,1);
    int complex = ApartmentTypeComplexHelper.getPartKey(key1,2);
    applied1.setApartmentTypeId(type);
    applied1.setComplexId(complex);
    applied1.setOrder(1);

    if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
      applied2 = new Applied();
      type = ApartmentTypeComplexHelper.getPartKey(key2,1);
      complex = ApartmentTypeComplexHelper.getPartKey(key2,2);
      applied2.setApartmentTypeId(type);
      applied2.setComplexId(complex);
      applied2.setOrder(2);
    }

    if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
      applied3 = new Applied();
      type = ApartmentTypeComplexHelper.getPartKey(key3,1);
      complex = ApartmentTypeComplexHelper.getPartKey(key3,2);
      applied3.setApartmentTypeId(type);
      applied3.setComplexId(complex);
      applied3.setOrder(3);
    }

    modinfo.setSessionAttribute("applied1",applied1);
    if (applied2 != null)
      modinfo.setSessionAttribute("applied2",applied2);
    if (applied3 != null)
      modinfo.setSessionAttribute("applied3",applied3);
  }

  /**
   *
   */
  public static String saveDataToDB(ModuleInfo modinfo) {
    Applicant applicant = (Applicant)modinfo.getSessionAttribute("applicant");
    Application application = (Application)modinfo.getSessionAttribute("application");
    CampusApplication campusApplication = (CampusApplication)modinfo.getSessionAttribute("campusapplication");
    Applied applied1 = (Applied)modinfo.getSessionAttribute("applied1");
    Applied applied2 = (Applied)modinfo.getSessionAttribute("applied2");
    Applied applied3 = (Applied)modinfo.getSessionAttribute("applied3");

    String cypher = "";

    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

    try {
      t.begin();
      applicant.insert();

      application.setApplicantId(applicant.getID());
      application.insert();

      campusApplication.setAppApplicationId(application.getID());
      campusApplication.insert();

      applied1.setApplicationId(campusApplication.getID());
      applied1.insert();

      if (applied2 != null) {
        applied2.setApplicationId(campusApplication.getID());
        applied2.insert();
      }

      if (applied3 != null) {
        applied3.setApplicationId(campusApplication.getID());
        applied3.insert();
      }

      ReferenceNumberHandler h = new ReferenceNumberHandler();
      String key = h.getCypherKey(modinfo);
      CypherText ct = new CypherText();

      String id = Integer.toString(application.getID());
      while (id.length() < 6)
        id = "0" + id;

      cypher = ct.doCyper(id,key);

      String receiver = "aron@idega.is";
      String e_mail = campusApplication.getEmail();
      if ( e_mail != null ) {
        if ( e_mail.length() > 0 ) {
          receiver = e_mail;
        }
      }

      String body = new String("Umsókn þín hefur verið skráð. Tilvísunarnúmer þitt er : " + cypher);
      SendMail.send("admin@campus.is",receiver,"","palli@idega.is","mail.idega.is","Umsókn skráð",body);

      t.commit();
      modinfo.removeSessionAttribute("applicant");
      modinfo.removeSessionAttribute("application");
      modinfo.removeSessionAttribute("campusapplication");
      modinfo.removeSessionAttribute("applied1");
      modinfo.removeSessionAttribute("applied2");
      modinfo.removeSessionAttribute("applied3");
      modinfo.removeSessionAttribute("aprtCat");
    }
    catch(Exception e) {
      try {
        t.rollback();
      }
      catch(javax.transaction.SystemException ex) {
        ex.printStackTrace();
      }
      e.printStackTrace();
      return(null);
    }

    return(cypher);
  }

  /**
   *
   */
  public static void saveCampusInformation(ModuleInfo modinfo) {
    int studyBeginMon = 0;
    int studyBeginYr = 0;
    int studyEndMo = 0;
    int studyEndYr = 0;
    String faculty = modinfo.getParameter("faculty");
    String studyTrack = modinfo.getParameter("studyTrack");
    int currentResidence = 0;
    int spouseOccupation = 0;
    String resInfo = modinfo.getParameter("resInfo");
    String spouseName = modinfo.getParameter("spouseName");
    String spouseSSN = modinfo.getParameter("spouseSSN");
    String spouseSchool = modinfo.getParameter("spouseSchool");
    String spouseStudyTrack = modinfo.getParameter("spouseStudyTrack");
    int spouseStudyBeginMo = 0;
    int spouseStudyBeginYr = 0;
    int spouseStudyEndMo = 0;
    int spouseStudyEndYr = 0;
    String children = modinfo.getParameter("children");
    int income = 0;
    int spouseIncome = 0;
    String wantHousingFrom = modinfo.getParameter("wantHousingFrom");
    String waitingList = modinfo.getParameter("waitingList");
    String furniture = modinfo.getParameter("furniture");
    String contact = modinfo.getParameter("contact");
    String email = modinfo.getParameter("email");
    String info = modinfo.getParameter("info");

    CampusApplication application = new CampusApplication();

    try {
      currentResidence = Integer.parseInt(modinfo.getParameter("currentResidence"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseOccupation = Integer.parseInt(modinfo.getParameter("spouseOccupation"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyBeginMon = Integer.parseInt(modinfo.getParameter("studyBeginMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyBeginYr = Integer.parseInt(modinfo.getParameter("studyBeginYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyEndMo = Integer.parseInt(modinfo.getParameter("studyEndMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyEndYr = Integer.parseInt(modinfo.getParameter("studyEndYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseIncome = Integer.parseInt(modinfo.getParameter("spouseIncome"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyBeginMo = Integer.parseInt(modinfo.getParameter("spouseStudyBeginMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyBeginYr = Integer.parseInt(modinfo.getParameter("spouseStudyBeginYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyEndMo = Integer.parseInt(modinfo.getParameter("spouseStudyEndMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyEndYr = Integer.parseInt(modinfo.getParameter("spouseStudyEndYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      income = Integer.parseInt(modinfo.getParameter("income"));
    }
    catch(java.lang.NumberFormatException e) {}

    application.setCurrentResidenceId(currentResidence);
    application.setSpouseOccupationId(spouseOccupation);
    application.setStudyBeginMonth(studyBeginMon);
    application.setStudyBeginYear(studyBeginYr);
    application.setStudyEndMonth(studyEndMo);
    application.setStudyEndYear(studyEndYr);
    application.setFaculty(faculty);
    application.setStudyTrack(studyTrack);
    application.setSpouseName(spouseName);
    application.setSpouseIncome(spouseIncome);
    application.setSpouseSSN(spouseSSN);
    application.setSpouseSchool(spouseSchool);
    application.setSpouseStudyTrack(spouseStudyTrack);
    application.setSpouseStudyBeginMonth(spouseStudyBeginMo);
    application.setSpouseStudyBeginYear(spouseStudyBeginYr);
    application.setSpouseStudyEndMonth(spouseStudyEndMo);
    application.setSpouseStudyEndYear(spouseStudyEndYr);
    application.setChildren(children);
    application.setIncome(income);
    idegaTimestamp t = new idegaTimestamp(wantHousingFrom);
    application.setHousingFrom(t.getSQLDate());
    if (waitingList == null)
      application.setOnWaitinglist(false);
    else
      application.setOnWaitinglist(true);
    if (furniture == null)
      application.setWantFurniture(false);
    else
      application.setWantFurniture(true);
    application.setContactPhone(contact);
    application.setOtherInfo(info);
    application.setEmail(email);

    modinfo.setSessionAttribute("campusapplication",application);
  }

  /**
   *
   */
  public static void saveSubject(ModuleInfo modinfo) {
    String subject = (String)modinfo.getParameter("subject");
    String aprtCat = (String)modinfo.getParameter("aprtCat");
    Application application = new Application();
    application.setSubjectId(Integer.parseInt(subject));
    application.setSubmitted(idegaTimestamp.getTimestampRightNow());
    application.setStatusSubmitted();
    application.setStatusChanged(idegaTimestamp.getTimestampRightNow());
    modinfo.setSessionAttribute("application",application);
    modinfo.setSessionAttribute("aprtCat",aprtCat);
  }

  /**
   *
   */
  public static Vector checkAparmentTypesSelected(ModuleInfo modinfo) {
    String key1 = (String)modinfo.getParameter("aprtType");
    String key2 = (String)modinfo.getParameter("aprtType2");
    String key3 = (String)modinfo.getParameter("aprtType3");

    Vector ret = new Vector(3);

    try {
      int type = ApartmentTypeComplexHelper.getPartKey(key1,1);
      ApartmentType room = new ApartmentType(type);

      int pic = room.getFloorPlanId();
      ret.add(0,new Integer(pic));

      if ((key2 != null) && (!key2.equalsIgnoreCase("-1"))) {
        type = ApartmentTypeComplexHelper.getPartKey(key2,1);
        room = new ApartmentType(type);
        pic = room.getFloorPlanId();
      }
      ret.add(1,new Integer(pic));

      if ((key3 != null) && (!key3.equalsIgnoreCase("-1"))) {
        type = ApartmentTypeComplexHelper.getPartKey(key3,1);
        room = new ApartmentType(type);
        pic = room.getFloorPlanId();
      }
      ret.add(2,new Integer(pic));
    }
    catch(SQLException e) {
      e.printStackTrace();
    }

    return(ret);
  }
}