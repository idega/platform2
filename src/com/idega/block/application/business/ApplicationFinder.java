/*
 * $Id: ApplicationFinder.java,v 1.22 2004/06/05 06:16:42 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;


import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.ApplicationHome;
import com.idega.block.application.data.ApplicationSubject;
import com.idega.block.application.data.ApplicationSubjectHome;
import com.idega.block.application.data.ApplicationSubjectInfo;
import com.idega.block.application.data.ApplicationSubjectInfoHome;
import com.idega.block.application.data.Status;


import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
/** @deprecated use service beans instead*/
public class ApplicationFinder {



  public static Collection listOfNewApplicationInSubject(int subjectId){
    try {
    	
    	ApplicationHome aHome = (ApplicationHome) IDOLookup.getHome(Application.class);
    	return aHome.findBySubjectAndStatus(new Integer(subjectId),Status.SUBMITTED.toString());

    }
    catch(Exception e){
      return(null);
    }
  }
  public static Collection listOfApplicationInSubject(int subjectId,String status){
    try {
    	
		ApplicationHome aHome = (ApplicationHome) IDOLookup.getHome(Application.class);
		if (subjectId != -99)	
			return aHome.findBySubjectAndStatus(new Integer(subjectId),status);
		else
			return aHome.findByStatus(status);
    }
    catch(Exception e){
      return(null);
    }
  }
  public static Collection listOfNewApplications(){
    try {
		ApplicationHome aHome = (ApplicationHome) IDOLookup.getHome(Application.class);
		return aHome.findByStatus(Status.SUBMITTED.toString());
    }
    catch(Exception e){
      return(null);
    }
  }

  public static Collection listOfNewApplicants(){
    return listOfApplicants(null,Status.SUBMITTED.toString());
  }

  public static Collection listOfNewApplicantsOrdered(String order){
    return listOfApplicants(null,Status.SUBMITTED.toString());
  }

  public static Collection listOfApplicantsWithStatus(String status){
    return listOfApplicants(null,status);
  }

  public static Collection listOfApplicants(String order,String status){
  	
  	try {
		ApplicantHome aHome = (ApplicantHome) IDOLookup.getHome(Applicant.class);
		aHome.findByApplicationStatusOrderedBy(status,order);
	}
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
	catch (FinderException e) {
		e.printStackTrace();
	}
  	return null;
  }

  private static Collection listOfApplicationHolders(Collection lApplications,Collection lApplicants){
    Vector V = null;
    if(lApplicants != null){
      int len = lApplicants.size();
      Hashtable H = new Hashtable(len);
      for (Iterator iter = lApplicants.iterator(); iter.hasNext();) {
      	Applicant applicant = (Applicant) iter.next();
        H.put(new Integer(applicant.getPrimaryKey().toString()),applicant);
      }

      if(lApplications != null){
        
        Application application;
        Applicant applicant;
        ApplicationHolder AH;
        V = new Vector();
        for (Iterator iter = lApplications.iterator(); iter.hasNext();) {
			application = (Application)iter.next();
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

  private static Collection listOfHolders(Collection lApplications,Collection lApplicants){
    Vector V = null;
    if(lApplications != null){
      int len = lApplications.size();
      //System.err.println("applications length :"+len);
      Hashtable H = new Hashtable(len);
      for (Iterator iter = lApplications.iterator(); iter.hasNext();) {
		Application application = (Application) iter.next();
        H.put(new Integer(application.getApplicantId()),application);
      }

      if(lApplicants != null){
         //System.err.println("applicant length :"+iLen);
        Application application;
        Applicant applicant;
        ApplicationHolder AH;
        V = new Vector();
        for (Iterator iter = lApplicants.iterator(); iter.hasNext();) {
			applicant = (Applicant) iter.next();
          Integer id = new Integer(applicant.getPrimaryKey().toString());
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

 

  public static Collection listOfApplicationHoldersInSubject(int id,String status,String order){
    Collection A = listOfApplicants(order,status);
    Collection L = listOfApplicationInSubject(id,status);
    Collection B = listOfHolders(L,A);
    return B;
  }

  public static Collection listOfSubject() {
    try {
    	
      ApplicationSubjectHome aHome = (ApplicationSubjectHome) IDOLookup.getHome(ApplicationSubject.class);
      return aHome.findAll();
    }
    catch(Exception e) {
      e.printStackTrace();
      return(null);
    }
  }

  public static Collection listOfSubjectInfo(){
    try {
    	ApplicationSubjectInfoHome aHome = (ApplicationSubjectInfoHome) IDOLookup.getHome(ApplicationSubjectInfo.class);
    	return aHome.findAll();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

 

  public Collection lookupSSN(String ssn){
    try {
    	ApplicantHome aHome = (ApplicantHome)IDOLookup.getHome(Applicant.class);
    	return aHome.findBySSN(ssn);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }
}
