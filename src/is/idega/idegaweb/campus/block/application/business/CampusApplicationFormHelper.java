/*
 * $Id: CampusApplicationFormHelper.java,v 1.5 2002/04/03 18:09:08 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.business;


import com.idega.block.application.business.ApplicationFormHelper;
import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.block.application.data.Applicant;
//import com.idega.block.application.data.ApplicantBean;
import com.idega.block.application.data.Application;
import com.idega.block.building.business.ApartmentTypeComplexHelper;
import com.idega.block.building.data.ApartmentType;
import com.idega.presentation.IWContext;
import com.idega.util.CypherText;
import com.idega.util.SendMail;
import com.idega.util.idegaTimestamp;
import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import java.sql.SQLException;
import java.util.Vector;
import java.util.StringTokenizer;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CampusApplicationFormHelper extends ApplicationFormHelper {
  /**
   *
   */
  public static void saveAppliedFor(IWContext iwc) {
    String key1 = (String)iwc.getParameter("aprtType");
    String key2 = (String)iwc.getParameter("aprtType2");
    String key3 = (String)iwc.getParameter("aprtType3");

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

    iwc.setSessionAttribute("applied1",applied1);
    if (applied2 != null)
      iwc.setSessionAttribute("applied2",applied2);
    if (applied3 != null)
      iwc.setSessionAttribute("applied3",applied3);
  }

  /**
   *
   */
  public static String saveDataToDB(IWContext iwc) {
    Applicant applicant = (Applicant)iwc.getSessionAttribute("applicant");
    Applicant spouse = (Applicant)iwc.getSessionAttribute("spouse");
    Vector childs = (Vector) iwc.getSessionAttribute("childs");
    Application application = (Application)iwc.getSessionAttribute("application");
    CampusApplication campusApplication = (CampusApplication)iwc.getSessionAttribute("campusapplication");
    Applied applied1 = (Applied)iwc.getSessionAttribute("applied1");
    Applied applied2 = (Applied)iwc.getSessionAttribute("applied2");
    Applied applied3 = (Applied)iwc.getSessionAttribute("applied3");

    String cypher = "";

    javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();

    try {
      t.begin();
      applicant.insert();
      applicant.addChild(applicant);

      if(spouse !=null){
        spouse.insert();
        applicant.addChild(spouse
        );
      }

      if(childs!=null && childs.size() > 0){
        for (int i = 0; i < childs.size(); i++) {
          Applicant child = (Applicant) childs.get(i);
          child.insert();
          applicant.addChild(child);
        }
      }

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
      String key = h.getCypherKey(iwc);
      CypherText ct = new CypherText();

      String id = Integer.toString(application.getID());
      while (id.length() < 6)
        id = "0" + id;

      cypher = ct.doCyper(id,key);

      String receiver = "aron@idega.is";
      String e_mail = campusApplication.getEmail();
      if ( e_mail != null ) {
        if ( e_mail.length() > 0 ) {

          try {
            new javax.mail.internet.InternetAddress(e_mail);
            receiver = e_mail;
          }
          catch (Exception ex) {

          }

        }
      }

      String body = new String("Umsókn þín hefur verið skráð. Tilvísunarnúmer þitt er : " + cypher);
      SendMail.send("admin@campus.is",receiver,"","palli@idega.is","mail.idega.is","Umsókn skráð",body);

      t.commit();
      iwc.removeSessionAttribute("applicant");
      iwc.removeSessionAttribute("spouse");
      iwc.removeSessionAttribute("childs");
      iwc.removeSessionAttribute("application");
      iwc.removeSessionAttribute("campusapplication");
      iwc.removeSessionAttribute("applied1");
      iwc.removeSessionAttribute("applied2");
      iwc.removeSessionAttribute("applied3");
      iwc.removeSessionAttribute("aprtCat");
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
  public static void saveCampusInformation(IWContext iwc) {
    int studyBeginMon = 0;
    int studyBeginYr = 0;
    int studyEndMo = 0;
    int studyEndYr = 0;
    String faculty = iwc.getParameter("faculty");
    String studyTrack = iwc.getParameter("studyTrack");
    int currentResidence = 0;
    int spouseOccupation = 0;
    String resInfo = iwc.getParameter("resInfo");
    String spouseName = iwc.getParameter("spouseName");
    String spouseSSN = iwc.getParameter("spouseSSN");
    String spouseSchool = iwc.getParameter("spouseSchool");
    String spouseStudyTrack = iwc.getParameter("spouseStudyTrack");
    int spouseStudyBeginMo = 0;
    int spouseStudyBeginYr = 0;
    int spouseStudyEndMo = 0;
    int spouseStudyEndYr = 0;
    String children = iwc.getParameter("children");
    //int income = 0;
    //int spouseIncome = 0;
    String wantHousingFrom = iwc.getParameter("wantHousingFrom");
    String waitingList = iwc.getParameter("waitingList");
    String furniture = iwc.getParameter("furniture");
    String contact = iwc.getParameter("contact");
    String email = iwc.getParameter("email");
    String info = iwc.getParameter("info");

    CampusApplication application = new CampusApplication();
    Applicant spouse = new Applicant();
    Vector childs = new Vector();

    try {
      currentResidence = Integer.parseInt(iwc.getParameter("currentResidence"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseOccupation = Integer.parseInt(iwc.getParameter("spouseOccupation"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyBeginMon = Integer.parseInt(iwc.getParameter("studyBeginMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyBeginYr = Integer.parseInt(iwc.getParameter("studyBeginYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyEndMo = Integer.parseInt(iwc.getParameter("studyEndMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      studyEndYr = Integer.parseInt(iwc.getParameter("studyEndYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    /*
    try {
      spouseIncome = Integer.parseInt(iwc.getParameter("spouseIncome"));
    }
    catch(java.lang.NumberFormatException e) {}
    */
    try {
      spouseStudyBeginMo = Integer.parseInt(iwc.getParameter("spouseStudyBeginMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyBeginYr = Integer.parseInt(iwc.getParameter("spouseStudyBeginYr"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyEndMo = Integer.parseInt(iwc.getParameter("spouseStudyEndMo"));
    }
    catch(java.lang.NumberFormatException e) {}

    try {
      spouseStudyEndYr = Integer.parseInt(iwc.getParameter("spouseStudyEndYr"));
    }
    catch(java.lang.NumberFormatException e) {}
/*
    try {
      income = Integer.parseInt(iwc.getParameter("income"));
    }
    catch(java.lang.NumberFormatException e) {}
*/
    application.setCurrentResidenceId(currentResidence);
    application.setSpouseOccupationId(spouseOccupation);
    application.setStudyBeginMonth(studyBeginMon);
    application.setStudyBeginYear(studyBeginYr);
    application.setStudyEndMonth(studyEndMo);
    application.setStudyEndYear(studyEndYr);
    application.setFaculty(faculty);
    application.setStudyTrack(studyTrack);
    application.setSpouseName(spouseName);
    //application.setSpouseIncome(spouseIncome);
    application.setSpouseSSN(spouseSSN);
    application.setSpouseSchool(spouseSchool);
    application.setSpouseStudyTrack(spouseStudyTrack);
    application.setSpouseStudyBeginMonth(spouseStudyBeginMo);
    application.setSpouseStudyBeginYear(spouseStudyBeginYr);
    application.setSpouseStudyEndMonth(spouseStudyEndMo);
    application.setSpouseStudyEndYear(spouseStudyEndYr);
    application.setChildren(children);
    //application.setIncome(income);
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

    // spouse part
    if(spouseName.length() > 0){
      spouse.setFullName(spouseName);
      spouse.setSSN(spouseSSN);
      spouse.setStatus("P");
      iwc.setSessionAttribute("spouse",spouse);
    }
    // Children part
    if(iwc.isParameterSet("children_count")){
      int count = Integer.parseInt(iwc.getParameter("children_count"));
      String name, birth;
      for (int i = 0; i < count; i++) {
        Applicant child = new Applicant();
        name = iwc.getParameter("childname"+i);
        birth = iwc.getParameter("childbirth"+i);
        if(name.length() >0){
          child.setFullName(name);
          child.setSSN(birth);
          child.setStatus("C");
          childs.add(child);
        }
      }
      iwc.setSessionAttribute("childs",childs);
    }
    iwc.setSessionAttribute("campusapplication",application);
  }

  /**
   *
   */
  public static void saveSubject(IWContext iwc) {
    String subject = (String)iwc.getParameter("subject");
    String aprtCat = (String)iwc.getParameter("aprtCat");
    Application application = new Application();
    application.setSubjectId(Integer.parseInt(subject));
    application.setSubmitted(idegaTimestamp.getTimestampRightNow());
    application.setStatusSubmitted();
    application.setStatusChanged(idegaTimestamp.getTimestampRightNow());
    iwc.setSessionAttribute("application",application);
    iwc.setSessionAttribute("aprtCat",aprtCat);
  }

  /**
   *
   */
  public static Vector checkAparmentTypesSelected(IWContext iwc) {
    String key1 = (String)iwc.getParameter("aprtType");
    String key2 = (String)iwc.getParameter("aprtType2");
    String key3 = (String)iwc.getParameter("aprtType3");

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
