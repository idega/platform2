/*
 * $Id: ApplicationFinder.java,v 1.8 2001/07/18 11:42:02 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import com.idega.block.application.data.*;
import com.idega.block.application.business.ApplicationHolder;
import java.sql.SQLException;
import java.util.List;
import com.idega.data.EntityFinder;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

public class ApplicationFinder {

  public static List listOfNewApplicationInSubject(int subjectId){
    try {
      Application A = new Application();
      String sql = "select * from "+A.getEntityName()+" where "+A.getSubjectIdColumnName()+" = "+subjectId +" and "+A.getStatusColumnName()+"='"+A.statusSubmitted+"'";
      //System.err.println(sql);
      return(EntityFinder.findAll(A,sql));

    }
    catch(SQLException e){
      return(null);
    }
  }
  public static List listOfApplicationInSubject(int subjectId,String status){
    try {
      Application A = new Application();
      String sql = "select * from "+A.getEntityName()+" where "+A.getSubjectIdColumnName()+" = "+subjectId +" and "+A.getStatusColumnName()+"='"+status+"'";
      //System.err.println(sql);
      return(EntityFinder.findAll(A,sql));

    }
    catch(SQLException e){
      return(null);
    }
  }
  public static List listOfNewApplications(){
    try {
     Application A = new Application();
     String sql = "select * from "+A.getEntityName()+" where "+A.getStatusColumnName()+" = '"+A.statusSubmitted+"'";

      return(EntityFinder.findAll(A,sql));
    }
    catch(SQLException e){
      return(null);
    }
  }


  public static List listOfNewApplicants(){
    try {
       Applicant a = new Applicant();
      Application A = new Application();
      StringBuffer sql = new StringBuffer("select ");
      sql.append(a.getEntityName());
      sql.append(".* from ");
      sql.append(a.getEntityName());
      sql.append(",");
      sql.append(A.getEntityName());
      sql.append(" where ");
      sql.append(A.getEntityName());
      sql.append(".");
      sql.append(A.getStatusColumnName());
      sql.append(" = '");
      sql.append(A.statusSubmitted);
      sql.append("' and ");
      sql.append(a.getIDColumnName());
      sql.append(" = ");
      sql.append(A.getApplicantIdColumnName());
      return(EntityFinder.findAll(a,sql.toString()));
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

  public static List listOfNewApplicationHoldersInSubject(int id,String status){
    List A = listOfNewApplicants();
    List L = listOfApplicationInSubject(id,status);
    return listOfApplicationHolders(L,A);
  }

  public static List listOfSubject() {
    try {
      ApplicationSubject AS = new ApplicationSubject();
      return EntityFinder.findAllDescendingOrdered(AS,AS.getExpiresColumnName());
    }
    catch(SQLException e) {
      e.printStackTrace();
      return(null);
    }
  }

  public static List listOfNonExpiredSubjects() {
    try {
      ApplicationSubject subject = new ApplicationSubject();
      StringBuffer sql = new StringBuffer("select ");
      sql.append("* from ");
      sql.append(subject.getEntityName());
      sql.append(" where ");
      sql.append(subject.getExpiresColumnName());
      sql.append(" > '");
      sql.append(com.idega.util.idegaTimestamp.RightNow().toSQLDateString());
      sql.append("'");

      return(EntityFinder.findAll(subject,sql.toString()));
    }
    catch(SQLException e) {
      e.printStackTrace();
      return(null);
    }
  }
}