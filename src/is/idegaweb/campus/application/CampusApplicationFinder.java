/*
 * $Id: CampusApplicationFinder.java,v 1.7 2001/07/16 18:08:30 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.application;

import is.idegaweb.campus.entity.SpouseOccupation;
import is.idegaweb.campus.entity.CurrentResidency;
import is.idegaweb.campus.entity.Application;
import is.idegaweb.campus.entity.Applied;
import is.idegaweb.campus.entity.WaitingList;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.business.ApplicationFinder;
import java.sql.SQLException;
import java.util.List;
import com.idega.data.EntityFinder;
import java.util.Vector;
import java.util.Hashtable;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public abstract class CampusApplicationFinder {

  public static List listOfApplicationInSubject(int subjectId){
    try {
      return(EntityFinder.findAll(new SpouseOccupation()));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static List listOfSpouseOccupations(){
    try {
      return(EntityFinder.findAll(new SpouseOccupation()));
    }
    catch(SQLException e){
      return(null);
    }
  }
  public static List listOfResidences(){
    try {
      return(EntityFinder.findAll(new CurrentResidency()));
    }
    catch(SQLException e){
      return(null);
    }
  }
  public static List listOfNewApplied(){
    try {
      return(EntityFinder.findAll(new Applied()));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static List listOfNewCampusApplication(){
     try {
      return(EntityFinder.findAll(new Application()));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static List listOfAppliedInApplication(int id){
    try {
      Applied A = new Applied();
      return(EntityFinder.findAllByColumn(A,A.getApplicationIdColumnName(),id));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static List listOfNewApplicationHolders(){
    List A = listOfNewApplied();
    List B = listOfNewCampusApplication();
    List C = ApplicationFinder.listOfNewApplications();
    List D = ApplicationFinder.listOfNewApplicants();

    Vector V = null;
    int len;
    if(B != null && C != null && D != null){
      len = D.size();
      Hashtable HD = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        Applicant applicant = (Applicant) D.get(i);
        HD.put(new Integer(applicant.getID()),applicant);
      }
      len = C.size();
      Hashtable HC = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        com.idega.block.application.data.Application application = (com.idega.block.application.data.Application) C.get(i);
        HC.put(new Integer(application.getID()),application);
      }
      len =  B.size();
      Hashtable HB = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        Application campusapplication = (Application) B.get(i);
        HB.put(new Integer(campusapplication.getID()),campusapplication);
      }

      if(A != null){
        int iLen = A.size();
        com.idega.block.application.data.Application application;
        Applicant applicant;
        Application campusApplication;
        Applied applied;
        CampusApplicationHolder AH;
        V = new Vector();
        Vector vApplied = null;
        int appliedAppId = -1;

        for (int i = 0; i < iLen; i++) {
          applied =  (is.idegaweb.campus.entity.Applied)(A.get(i));

          if(appliedAppId == applied.getApplicationId().intValue()){
            if(vApplied != null)
              vApplied.add(applied);
          }
          else{
            vApplied = new Vector();
            vApplied.add(applied);
            campusApplication = (is.idegaweb.campus.entity.Application) HB.get((applied.getApplicationId()));
            application = (com.idega.block.application.data.Application) HC.get((campusApplication.getAppApplicationId()));
            applicant = (com.idega.block.application.data.Applicant)HD.get(new Integer(application.getApplicantId()));
            AH = new CampusApplicationHolder(application,applicant,campusApplication,vApplied);
            V.add(AH);
          }
          appliedAppId = (applied.getApplicationId()).intValue();
        }

      }
    }
    return V;
  }

  public static CampusApplicationHolder getApplicantInfo(int iApplicantId){
    CampusApplicationHolder CAH = null;
    Applicant eApplicant = null;

      try {
        eApplicant = new Applicant(iApplicantId);
        CAH = getApplicantInfo(eApplicant);
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }

    return CAH;
  }

  public static CampusApplicationHolder getApplicantInfo(Applicant eApplicant){
    CampusApplicationHolder CAH = null;
    if(eApplicant !=null){
      System.err.println(" villa 1");
      try {
        com.idega.block.application.data.Application eApplication =  new com.idega.block.application.data.Application();
        List L = EntityFinder.findAllByColumn(eApplication,eApplication.getApplicantIdColumnName(),eApplicant.getID());
        if(L!=null){
          System.err.println(" villa 2");
          eApplication = (com.idega.block.application.data.Application) L.get(0);
          Application eCampusApplication = new Application();
          L = EntityFinder.findAllByColumn(eCampusApplication,eCampusApplication.getApplicationIdColumnName(),eApplication.getID());
          if(L!=null){
            System.err.println(" villa 3");
            eCampusApplication = (Application) L.get(0);
            Applied eApplied = new Applied();
            L = EntityFinder.findAllByColumn(eApplied,eApplied.getApplicationIdColumnName(),eCampusApplication.getID());
            Vector V = null;
            if(L!=null){
              System.err.println(" villa 4");
              V = new Vector(L.size());
              for (int i = 0; i < L.size(); i++) {
                Applied A = (Applied) L.get(i);
                V.add(A);
              }
            }
            CAH = new CampusApplicationHolder(eApplication,eApplicant,eCampusApplication,V);
          }
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    return CAH;
  }

  public static List listOfWaitinglist(int aprtTypeId,int cmplxId){
     try {
      WaitingList WL = new WaitingList();
      return(EntityFinder.findAllByColumn(WL,WL.getApartmentTypeIdColumnName(),String.valueOf(aprtTypeId),WL.getComplexIdColumnName(),String.valueOf(cmplxId)));
    }
    catch(SQLException e){
      return(null);
    }
  }

 public static int countAppliedInTypeAndComplex(int typeId,int cmplxId,int order){
    StringBuffer sql = new StringBuffer("select count(*) ");
    sql.append(" from cam_applied app ");
    sql.append(" where app.bu_aprt_type_id = ");
    sql.append(typeId);
    sql.append(" and app.bu_complex_id =");
    sql.append(cmplxId);
    if(order > 0){
      sql.append(" and app.ordered = ");
      sql.append(order);
    }
    int count = 0;
    try{
      count = new Applied().getNumberOfRecords(sql.toString());
    }
    catch(SQLException ex){}
    if(count < 0)
      count = 0;
    return count;
  }

  public static int countWaitingInTypeAndComplex(int typeId,int cmplxId,int order){
    StringBuffer sql = new StringBuffer("select count(*  ) ");
    sql.append(" from cam_waiting_list wl ");
    sql.append(" where wl.bu_apartment_type_id = ");
    sql.append(typeId);
    sql.append(" and wl.bu_complex_id = ");
    sql.append(cmplxId);
    //System.err.println(sql.toString());
    int count = 0;
    try{
      count = new Applied().getNumberOfRecords(sql.toString());
    }
    catch(SQLException ex){}
    if(count < 0)
      count = 0;
    return count;
  }
}




