package is.idega.idegaweb.campus.block.mailinglist.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;

import java.util.Collection;
import java.util.Vector;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.building.business.ApartmentHolder;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.ApartmentViewHome;
import com.idega.data.IDOLookup;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;



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

  Collection emails;
  CampusApplicationHolder holder;
 
  public EntityHolder(Contract eContract) {
    this.eContract = eContract;
    applicantID = eContract.getApplicantId().intValue();
    init();
  }
  public EntityHolder(int iContractId){
    try {

      ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
      eContract  = cHome.findByPrimaryKey(new Integer(iContractId));

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
        eUser = ((UserHome)com.idega.data.IDOLookup.getHome(User.class)).findByPrimaryKey(eContract.getUserId());
        if(eUser!=null)
          emails = eUser.getEmails();
        apartmentHolder = new ApartmentHolder(((ApartmentViewHome)IDOLookup.getHome(ApartmentView.class)).findByPrimaryKey(eContract.getApartmentId()));
      }

      if(emails==null && eCampusApplication!=null){
		emails = new Vector();
		emails.add(eCampusApplication.getEmail());
      		
      }
      
 
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public EntityHolder(Applicant eApplicant) {
    this.applicantID = ((Integer)eApplicant.getPrimaryKey()).intValue();
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
  public Collection getEmails(){
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
