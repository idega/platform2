/*
 * $Id: CampusApplicationHolder.java,v 1.1 2001/07/09 13:45:12 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.application;

import com.idega.block.application.data.*;
import is.idegaweb.campus.entity.Applied;
import java.util.Vector;

/**
 *
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class CampusApplicationHolder {
  private Application eApplication = null;
  private Applicant eApplicant = null;
  private is.idegaweb.campus.entity.Application eCampusApplication = null;
  private Vector vApplied = null;

  public CampusApplicationHolder(Application application,Applicant applicant,
        is.idegaweb.campus.entity.Application campusApplication,Vector vapplied){
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
  public void setCampusApplication(is.idegaweb.campus.entity.Application application){
    this.eCampusApplication = application;
  }
  public void setApplied(Vector applied){
    this.vApplied = applied;
  }
  public Application getApplication(){
    return this.eApplication;
  }
  public is.idegaweb.campus.entity.Application getCampusApplication(){
    return eCampusApplication;
  }
  public Applicant getApplicant(){
    return this.eApplicant ;
  }
   public Vector getApplied(){
    return this.vApplied ;
  }
}
