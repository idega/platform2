package is.idega.idegaweb.campus.block.mailinglist.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import com.idega.core.user.data.User;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.application.business.ApplicationBusiness;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class EntityHolder {

  User eUser;
  Applicant eApplicant;
  Contract eContract;

  public EntityHolder(Contract eContract) {
    this.eContract = eContract;
    try {
      eApplicant = new Applicant(eContract.getApplicantId().intValue());
      eUser = new User(eContract.getUserId().intValue());
    }
    catch (Exception ex) {

    }

  }
  public EntityHolder(int iContractId){
    try {
      eContract  = new Contract(iContractId);
      eApplicant = new Applicant(eContract.getApplicantId().intValue());
      eUser = new User(eContract.getUserId().intValue());
    }
    catch (Exception ex) {

    }

  }
  public EntityHolder(Applicant eApplicant) {
    this.eContract = null;
    this.eApplicant = eApplicant;
    this.eUser = null;
  }
  public User getUser(){
    return this.eUser;
  }
  public Applicant getApplicant(){
    return this.eApplicant;
  }
  public Contract getContract(){
    return this.eContract;
  }
}