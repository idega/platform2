/*
 * $Id: ApplicationFinder.java,v 1.21 2003/04/03 07:05:44 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.ApplicationSubjectInfo;
import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

public class ApplicationFinder {



  public static List listOfNewApplicationInSubject(int subjectId){
    try {
      Application A = ((com.idega.block.application.data.ApplicationHome)com.idega.data.IDOLookup.getHomeLegacy(Application.class)).createLegacy();
      String sql = "select * from "+A.getEntityName()+" where "+com.idega.block.application.data.ApplicationBMPBean.getSubjectIdColumnName()+" = "+subjectId +" and "+com.idega.block.application.data.ApplicationBMPBean.getStatusColumnName()+"='"+com.idega.block.application.data.ApplicationBMPBean.STATUS_SUBMITTED+"'";
      //System.err.println(sql);
      return(EntityFinder.findAll(A,sql));

    }
    catch(SQLException e){
      return(null);
    }
  }
  public static List listOfApplicationInSubject(int subjectId,String status){
    try {
      Application A = ((com.idega.block.application.data.ApplicationHome)com.idega.data.IDOLookup.getHomeLegacy(Application.class)).createLegacy();
      String sql = null;
      if (subjectId != -99)
        sql = "select * from "+A.getEntityName()+" where " +com.idega.block.application.data.ApplicationBMPBean.getSubjectIdColumnName()+" = "+subjectId +" and "+com.idega.block.application.data.ApplicationBMPBean.getStatusColumnName()+"='"+status+"'";
      else
        sql = "select * from "+A.getEntityName()+" where " +com.idega.block.application.data.ApplicationBMPBean.getStatusColumnName()+"='"+status+"'";
      //System.err.println(sql);
      return(EntityFinder.findAll(A,sql));

    }
    catch(SQLException e){
      return(null);
    }
  }
  public static List listOfNewApplications(){
    try {
     Application A = ((com.idega.block.application.data.ApplicationHome)com.idega.data.IDOLookup.getHomeLegacy(Application.class)).createLegacy();
     String sql = "select * from "+A.getEntityName()+" where "+com.idega.block.application.data.ApplicationBMPBean.getStatusColumnName()+" = '"+com.idega.block.application.data.ApplicationBMPBean.STATUS_SUBMITTED+"'";

      return(EntityFinder.findAll(A,sql));
    }
    catch(SQLException e){
      return(null);
    }
  }

  public static List listOfNewApplicants(){
    return listOfApplicants(null,com.idega.block.application.data.ApplicationBMPBean.STATUS_SUBMITTED);
  }

  public static List listOfNewApplicantsOrdered(String order){
    return listOfApplicants(null,com.idega.block.application.data.ApplicationBMPBean.STATUS_SUBMITTED);
  }

  public static List listOfApplicantsWithStatus(String status){
    return listOfApplicants(null,status);
  }

  public static List listOfApplicants(String order,String status){
    try {
      Applicant a = ((com.idega.block.application.data.ApplicantHome)com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).createLegacy();
      Application A = ((com.idega.block.application.data.ApplicationHome)com.idega.data.IDOLookup.getHomeLegacy(Application.class)).createLegacy();
      StringBuffer sql = new StringBuffer("select distinct ");
      sql.append(a.getEntityName());
      sql.append(".* from ");
      sql.append(a.getEntityName());
      sql.append(",");
      sql.append(A.getEntityName());
      sql.append(" where ");
      sql.append(a.getEntityName());
      sql.append(".");
      sql.append(a.getIDColumnName());
      sql.append(" = ");
      sql.append(A.getEntityName());
      sql.append(".");
      sql.append(com.idega.block.application.data.ApplicationBMPBean.getApplicantIdColumnName());
      sql.append(" and ");
      if(status != null){
        sql.append(A.getEntityName());
        sql.append(".");
        sql.append(com.idega.block.application.data.ApplicationBMPBean.getStatusColumnName());
        sql.append(" = '");
        sql.append(status);
        sql.append("' ");
      }
      if(order != null && order.length() > 0){
        sql.append(" order by ");
        sql.append( order);
      }
      //System.err.println(sql.toString());
      List L = EntityFinder.findAll(a,sql.toString());
      return(L);

    }
    catch(SQLException e){
      e.printStackTrace();
      return(null);
    }

  }

  private static List listOfApplicationHolders(List lApplications,List lApplicants){
    Vector V = null;
    if(lApplicants != null){
      int len = lApplicants.size();
      Hashtable H = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        Applicant applicant = (Applicant) lApplicants.get(i);
        H.put(new Integer(applicant.getID()),applicant);
      }

      if(lApplications != null){
        int iLen = lApplications.size();
        Application application;
        Applicant applicant;
        ApplicationHolder AH;
        V = new Vector();
        for (int i = 0; i < iLen; i++) {
          application = (Application) lApplications.get(i);
          Integer id = new Integer(application.getApplicantId());
          if(H.containsKey(id)){
            applicant = (Applicant) H.get(id);
            AH = new ApplicationHolder(application,applicant);
            V.addElement(AH);
          }
        }
      }
    }
    return V;
  }

  private static List listOfHolders(List lApplications,List lApplicants){
    Vector V = null;
    if(lApplications != null){
      int len = lApplications.size();
      //System.err.println("applications length :"+len);
      Hashtable H = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        Application application = (Application) lApplications.get(i);
        H.put(new Integer(application.getApplicantId()),application);
      }

      if(lApplicants != null){
        int iLen = lApplicants.size();
         //System.err.println("applicant length :"+iLen);
        Application application;
        Applicant applicant;
        ApplicationHolder AH;
        V = new Vector();
        for (int i = 0; i < iLen; i++) {
          applicant = (Applicant) lApplicants.get(i);
          Integer id = new Integer(applicant.getID());
          if(H.containsKey(id)){
            application = (Application) H.get(id);
            AH = new ApplicationHolder(application,applicant);
            V.addElement(AH);
          }
        }
      }
    }
    return V;
  }

  public static List listOfNewApplicationHolders(){
    List A = listOfNewApplicants();
    List L = listOfNewApplications();
    return listOfApplicationHolders(L,A);
  }

  public static List listOfNewApplicationHoldersInSubject(int id){
    List A = listOfNewApplicants();
    List L = listOfNewApplicationInSubject(id);
    return listOfApplicationHolders(L,A);
  }

  public static List listOfApplicationHoldersInSubject(int id,String status){
    List A = listOfNewApplicants();
    List L = listOfApplicationInSubject(id,status);
    return listOfApplicationHolders(L,A);
  }

  public static List listOfApplicationHoldersInSubject(int id,String status,String order){
    List A = listOfApplicants(order,status);
    List L = listOfApplicationInSubject(id,status);
    List B = listOfHolders(L,A);
    return B;
  }

  public static List listOfSubject() {
    try {
      ApplicationSubject AS = ((com.idega.block.application.data.ApplicationSubjectHome)com.idega.data.IDOLookup.getHomeLegacy(ApplicationSubject.class)).createLegacy();
      return EntityFinder.findAllDescendingOrdered(AS,AS.getExpiresColumnName());
    }
    catch(SQLException e) {
      e.printStackTrace();
      return(null);
    }
  }

  public static List listOfSubjectInfo(){
    try {
      return EntityFinder.findAll(((com.idega.block.application.data.ApplicationSubjectInfoHome)com.idega.data.IDOLookup.getHomeLegacy(ApplicationSubjectInfo.class)).createLegacy());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static List listOfNonExpiredSubjects() {
    try {
      ApplicationSubject subject = ((com.idega.block.application.data.ApplicationSubjectHome)com.idega.data.IDOLookup.getHomeLegacy(ApplicationSubject.class)).createLegacy();
      StringBuffer sql = new StringBuffer("select ");
      sql.append("* from ");
      sql.append(subject.getEntityName());
      sql.append(" where ");
      sql.append(subject.getExpiresColumnName());
      sql.append(" > '");
      sql.append(com.idega.util.IWTimestamp.RightNow().toSQLDateString());
      sql.append("'");

      return(EntityFinder.findAll(subject,sql.toString()));
    }
    catch(SQLException e) {
      e.printStackTrace();
      return(null);
    }
  }

  public List lookupSSN(String ssn){
    try {
      return EntityFinder.getInstance().findAllByColumn(Applicant.class,com.idega.block.application.data.ApplicantBMPBean.getSSNColumnName(),ssn);
    }
    catch (IDOFinderException ex) {
      ex.printStackTrace();
    }
    return null;
  }
}
