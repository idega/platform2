/*
 * $Id: CampusApplicationFinder.java,v 1.2 2001/07/09 13:35:48 aron Exp $
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
        Application application = (Application) B.get(i);
        HB.put(new Integer(application.getID()),application);
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
          applied = (Applied) A.get(i);
          if(appliedAppId == applied.getApplicationId().intValue()){
            if(vApplied != null)
              vApplied.add(applied);
          }
          else{
            vApplied = new Vector();
            vApplied.add(applied);
            campusApplication = (Application)HC.get((applied.getApplicationId()));
            application = (com.idega.block.application.data.Application)HB.get((campusApplication.getAppApplicationId()));
            applicant = (Applicant)HD.get(new Integer(application.getApplicantId()));
            AH = new CampusApplicationHolder(application,applicant,campusApplication,vApplied);
            V.add(AH);
          }
          appliedAppId = applied.getApplicationId().intValue();
        }

      }
    }
    return V;
  }
}




