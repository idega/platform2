/*
 * $Id: CampusApplicationHolder.java,v 1.5 2001/11/08 15:40:39 aron Exp $
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
import is.idegaweb.campus.entity.CampusApplication;
import is.idegaweb.campus.entity.Contract;
//import is.idegaweb.campus.entity.WaitingList;
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
  private Contract eContract = null;
  private Vector vApplied = null;
  private Vector vWaitingList = null;

  /**
   *
   */
  public CampusApplicationHolder(Application application, Applicant applicant,
                                 CampusApplication campusApplication, Vector vapplied) {
    initialize(application,applicant,campusApplication,vapplied,null,null);
  }

  /**
   *
   */
  public CampusApplicationHolder(Application application, Applicant applicant,
                                 CampusApplication campusApplication, Vector vapplied,
                                 Contract contract, Vector waitingList) {
    initialize(application,applicant,campusApplication,vapplied,contract,waitingList);
  }

  /**
   *
   */
  public void setApplication(Application application) {
    eApplication = application;
  }

  /**
   *
   */
  public void setApplicant(Applicant applicant) {
    eApplicant = applicant;
  }

  /**
   *
   */
  public void setCampusApplication(CampusApplication application) {
    eCampusApplication = application;
  }

  /**
   *
   */
  public void setApplied(Vector applied) {
    vApplied = applied;
  }

  /**
   *
   */
  public Application getApplication() {
    return(eApplication);
  }

  /**
   *
   */
  public CampusApplication getCampusApplication() {
    return(eCampusApplication);
  }

  /**
   *
   */
  public Applicant getApplicant() {
    return(eApplicant);
  }

  /**
   *
   */
  public Vector getApplied() {
    return(vApplied);
  }

  /**
   *
   */
  public void setContract(Contract contract) {
    eContract = contract;
  }

  /**
   *
   */
  public Contract getContract() {
    return(eContract);
  }

  /**
   *
   */
  public void setWaitingList(Vector waitingList) {
    vWaitingList = waitingList;
  }

  /**
   *
   */
  public Vector getWaitingList() {
    return(vWaitingList);
  }

  private void initialize(Application application, Applicant applicant,
                          CampusApplication campusApplication, Vector vapplied,
                          Contract contract, Vector waitingList ) {
    eApplicant = applicant;
    eApplication = application;
    eCampusApplication = campusApplication;
    vApplied = vapplied;
    eContract = contract;
    vWaitingList = waitingList;
  }
}
