/*
 * $Id: CampusApplicationFinder.java,v 1.11 2001/07/30 11:48:31 palli Exp $
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
import is.idegaweb.campus.entity.CampusApplication;
import is.idegaweb.campus.entity.Applied;
import is.idegaweb.campus.entity.WaitingList;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.business.ApplicationFinder;
import java.sql.SQLException;
import java.util.List;
import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
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
      return(EntityFinder.findAll(new CampusApplication()));
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

    return listOfCampusApplicationHolders(A,B,C,D);
  }

  public static List listOfApplicationHoldersInSubject(int id,String status){

    List A = listOfNewApplied();
    List B = listOfNewCampusApplication();
    List C = ApplicationFinder.listOfApplicationInSubject(id,status);
    List D = ApplicationFinder.listOfNewApplicants();

    return listOfCampusApplicationHolders(A,B,C,D);
  }

  public static List listOfCampusApplicationHolders(List lApplied,List lCamApp,List lApp,List lApplicant){
    List A = lApplied;
    List B = lCamApp;
    List C = lApp;
    List D = lApplicant;

    Vector V = null;
    try{
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
        CampusApplication campusapplication = (CampusApplication) B.get(i);
        HB.put(new Integer(campusapplication.getID()),campusapplication);
      }

      if(A != null){
        int iLen = A.size();
        com.idega.block.application.data.Application application;
        Applicant applicant;
        CampusApplication campusApplication;
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
            if(HB.containsKey(applied.getApplicationId()) ){
              campusApplication = (CampusApplication) HB.get((applied.getApplicationId()));
              if(HC.containsKey(campusApplication.getAppApplicationId()) ){
                application = (com.idega.block.application.data.Application) HC.get((campusApplication.getAppApplicationId()));
                if(HD.containsKey(new Integer(application.getApplicantId()))){
                  applicant = (com.idega.block.application.data.Applicant)HD.get(new Integer(application.getApplicantId()));
                  AH = new CampusApplicationHolder(application,applicant,campusApplication,vApplied);
                  V.add(AH);
                }
              }
            }
          }
          appliedAppId = (applied.getApplicationId()).intValue();
        }

      }
    }
    }catch(Exception ex){ex.printStackTrace();}
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

  /**
   *
   */
  public static CampusApplicationHolder getCampusApplicationInfo(int applicationId) {
    CampusApplicationHolder cah = null;
    CampusApplication ca = null;

/*    try {
      ca = new CampusApplication(applicationId);
      cah = getCampusApplicationInfo(ca);
    }
    catch(SQLException e) {
      e.printStackTrace();
    }*/

    return(cah);
  }

  /**
   *
   */
  public static CampusApplicationHolder getCampusApplicationInfo(CampusApplication app) {
    return(null);
  }

  /**
   *
   */
  public static CampusApplicationHolder getApplicantInfo(Applicant eApplicant){
    CampusApplicationHolder CAH = null;
    if(eApplicant !=null){

      try {
        com.idega.block.application.data.Application eApplication =  new com.idega.block.application.data.Application();
        List L = EntityFinder.findAllByColumn(eApplication,eApplication.getApplicantIdColumnName(),eApplicant.getID());
        if(L!=null){
          eApplication = (com.idega.block.application.data.Application) L.get(0);
          CampusApplication eCampusApplication = new CampusApplication();
          L = EntityFinder.findAllByColumn(eCampusApplication,eCampusApplication.getApplicationIdColumnName(),eApplication.getID());
          if(L!=null){
            eCampusApplication = (CampusApplication) L.get(0);
            Applied eApplied = new Applied();
            L = EntityFinder.findAllByColumn(eApplied,eApplied.getApplicationIdColumnName(),eCampusApplication.getID());
            Vector V = null;
            if(L!=null){
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

  public static List listOfEntityInWaitingList(GenericEntity returntype,int aprtTypeId,int cmplxId){
    List L = null;
    boolean bapplied = false;
    if(returntype instanceof Applied)
      bapplied = true;
    /** @todo  laga */
    boolean btype = true,bcmplx = true;

    if(aprtTypeId <= 0)
      btype = false;
    if(cmplxId <= 0)
      bcmplx = false;
    StringBuffer sql = new StringBuffer("select ");
    //if(!(btype && bcmplx))
      sql.append(" distinct ");
    sql.append(returntype.getEntityName());
    sql.append(".* ");
    sql.append(" from cam_application ca,app_application an,app_applicant aa");
    sql.append(",cam_waiting_list wl ,cam_applied ad");
    sql.append(" where ca.app_application_id = an.app_application_id ");
    sql.append(" and an.app_applicant_id = aa.app_applicant_id ");
    sql.append(" and aa.app_applicant_id = wl.app_applicant_id ");
    if(bapplied){
      sql.append(" and ad.bu_aprt_type_id =  wl.bu_apartment_type_id ");
      sql.append(" and ad.bu_complex_id = wl.bu_complex_id ");
    }
    if(btype){
      sql.append(" and wl.bu_apartment_type_id =  ");
      sql.append(aprtTypeId);
    }
    if(bcmplx){
      sql.append(" and wl.bu_complex_id =  ");
      sql.append(cmplxId);
    }
    //System.err.println(sql.toString());
    try {
      L = EntityFinder.findAll(returntype,sql.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return L;
  }

  public static List listOfCampusApplicationHoldersInWaitinglist(int aprtTypeId,int cmplxId){
    com.idega.block.application.data.Application A = new com.idega.block.application.data.Application();
    List lApplication = listOfEntityInWaitingList(A, aprtTypeId, cmplxId);
    List lApplicant = listOfEntityInWaitingList(new Applicant(), aprtTypeId, cmplxId);
    List lCampusApplication = listOfEntityInWaitingList(new CampusApplication(), aprtTypeId, cmplxId);
    List lApplied = listOfEntityInWaitingList(new Applied(), aprtTypeId, cmplxId);

    return listOfCampusApplicationHolders(lApplied,lCampusApplication,lApplication,lApplicant);
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
      count = new CampusApplication().getNumberOfRecords(sql.toString());
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
  public static int countWaitingWithTypeAndComplex(int typeId,int cmplxId,int order){
    StringBuffer sql = new StringBuffer("select count(distinct cam_waiting_list_id) ");
    sql.append(" from cam_waiting_list wl ,cam_applied ad ");
    sql.append(" where wl.bu_apartment_type_id = ad.bu_aprt_type_id ");
    sql.append(" and wl.bu_complex_id = ad.bu_complex_id ");
    sql.append(" and wl.bu_apartment_type_id =  ");
    sql.append(typeId);
    sql.append(" and wl.bu_complex_id =  ");
    sql.append(cmplxId);
     if(order > 0){
      sql.append(" and ad.ordered = ");
      sql.append(order);
    }
    int count = 0;
    //System.err.println(sql.toString());
    //System.err.println();
    try{
      count = new Applied().getNumberOfRecords(sql.toString());
    }
    catch(SQLException ex){}
    if(count < 0)
      count = 0;
    return count;
  }
}




