package is.idega.idegaweb.campus.block.mailinglist.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;

import java.util.List;
import java.util.Vector;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.building.business.ApartmentHolder;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookup;


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
  int applicantID = -1;
  Applicant eApplicant;
  Application eApplication;
  CampusApplication eCampusApplication;
  ApartmentHolder apartmentHolder;
  Contract eContract;
  CampusApplicationHolder holder;
  List emails;
  
  public EntityHolder(Contract eContract) {
    this.eContract = eContract;
    applicantID = eContract.getApplicantId().intValue();
    init();
  }
  public EntityHolder(int iContractId){
    try {
      eContract  = ((ContractHome)IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(iContractId));
      applicantID = eContract.getApplicantId().intValue();
      init();
    }
    catch (Exception ex) {

    }

  }

  private void init(){
    try {
	  holder = CampusApplicationFinder.getApplicantInfo(applicantID);
      eApplicant = holder.getApplicant();
      eApplication = holder.getApplication();
      eCampusApplication = holder.getCampusApplication();

      if(eContract!=null){
        eUser = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(eContract.getUserId().intValue());
        if(eUser!=null)
          emails = UserBusiness.listOfUserEmails(eUser.getID());
        apartmentHolder = new ApartmentHolder(eContract.getApartmentId().intValue());
      }

      if(emails==null){
        String[] sEmails = CampusApplicationFinder.getApplicantEmail(eApplicant.getID());
        if(sEmails!=null && sEmails.length >0){
          emails = new Vector();
          emails.add(sEmails[0]);
        }
      }
      
 
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public EntityHolder(Applicant eApplicant) {
    this.applicantID = eApplicant.getID();
    init();
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
  public Application getApplication(){
    return eApplication;
  }
  public CampusApplication getCampusApplication(){
    return eCampusApplication;
  }
  public CampusApplicationHolder getApplicationHolder(){
  	return holder;
  }
}
