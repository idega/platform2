package is.idega.idegaweb.campus.block.mailinglist.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import com.idega.block.building.business.ApartmentHolder;
import com.idega.core.user.data.User;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.data.Email;
import com.idega.block.application.data.Applicant;
//import com.idega.block.application.data.ApplicantBean;
import com.idega.block.application.data.Application;
import com.idega.block.application.business.ApplicationBusiness;
import java.util.List;
import java.util.Vector;

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
  CampusApplication eCampusApplication;
  ApartmentHolder apartmentHolder;
  Contract eContract;
  List emails;

  public EntityHolder(Contract eContract) {
    this.eContract = eContract;
    init();
  }
  public EntityHolder(int iContractId){
    try {
      eContract  = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKeyLegacy(iContractId);
      init();
    }
    catch (Exception ex) {

    }

  }

  private void init(){
    try {
      eApplicant = ((com.idega.block.application.data.ApplicantHome)com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(eContract.getApplicantId().intValue());
      eUser = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(eContract.getUserId().intValue());
      emails = UserBusiness.listOfUserEmails(eUser.getID());
      apartmentHolder = new ApartmentHolder(eContract.getApartmentId().intValue());
      if(emails==null){
        String[] sEmails = CampusApplicationFinder.getApplicantEmail(eApplicant.getID());
        if(sEmails!=null && sEmails.length >0){
          emails = new Vector();
          emails.add(sEmails[0]);
        }
      }
    }
    catch (Exception ex) {

    }
  }

  public EntityHolder(Applicant eApplicant) {
    this.eContract = null;
    this.eApplicant = eApplicant;
    this.eUser = null;
    String[] sEmails = CampusApplicationFinder.getApplicantEmail(eApplicant.getID());
    if(sEmails!=null && sEmails.length >0){
      emails = new Vector();
      emails.add(sEmails[0]);
    }
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
  public List getEmails(){
    return this.emails;
  }
  public ApartmentHolder getApartmentHolder(){
    return apartmentHolder;
  }
}
