/*
 * $Id: CampusApplicationHolder.java,v 1.2 2001/07/30 11:48:31 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.application;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import is.idegaweb.campus.entity.Applied;
import is.idegaweb.campus.entity.CampusApplication;
import java.util.Vector;

/**
 *
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class CampusApplicationHolder {
  private Application eApplication = null;
  private Applicant eApplicant = null;
  private CampusApplication eCampusApplication = null;
  private Vector vApplied = null;

  public CampusApplicationHolder(Application application,Applicant applicant,
        CampusApplication campusApplication,Vector vapplied){
    this.eApplicant = applicant;
    this.eApplication = application;
    this.eCampusApplication = campusApplication;
    this.vApplied = vapplied;
  }
  public void setApplication(Application application){
    this.eApplication = application;
  }
  public void setApplicant(Applicant applicant){
    this.eApplicant = applicant;
  }
  public void setCampusApplication(CampusApplication application){
    this.eCampusApplication = application;
  }
  public void setApplied(Vector applied){
    this.vApplied = applied;
  }
  public Application getApplication(){
    return this.eApplication;
  }
  public CampusApplication getCampusApplication(){
    return eCampusApplication;
  }
  public Applicant getApplicant(){
    return this.eApplicant ;
  }
   public Vector getApplied(){
    return this.vApplied ;
  }
}
