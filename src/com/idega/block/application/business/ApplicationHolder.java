/*
 * $Id: ApplicationHolder.java,v 1.1 2001/06/28 10:33:07 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.business;

import com.idega.block.application.data.*;

/**
 *
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class ApplicationHolder {
  private Application eApplication = null;
  private Applicant eApplicant = null;

  public ApplicationHolder(Application application,Applicant applicant){
    this.eApplicant = applicant;
    this.eApplication = application;
  }
  public void setApplication(Application application){
    this.eApplication = application;
  }
  public void setApplicant(Applicant applicant){
    this.eApplicant = applicant;
  }
  public Application getApplication(){
    return this.eApplication;
  }
  public Applicant getApplicant(){
    return this.eApplicant ;
  }
}
