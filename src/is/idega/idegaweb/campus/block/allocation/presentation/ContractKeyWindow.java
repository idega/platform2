package is.idega.idegaweb.campus.block.allocation.presentation;


import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import java.rmi.RemoteException;
import java.util.Collection;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class ContractKeyWindow extends CampusWindow{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  
  public static String prmContractId = "contract_id";

  /*
    Bl?r litur ? topp # 27324B
    Hv?tur litur fyrir ne?an ?a? # FFFFFF
    Lj?sbl?r litur ? t?flu # ECEEF0
    Auka litur ?rl?ti? dekkri (? lagi a? nota l?ka) # CBCFD3
  */

  public ContractKeyWindow() {
    //keepFocus();
    setWidth(300);
    setHeight(250);
    setResizable(true);
  }

  protected void control(IWContext iwc)throws RemoteException{

    if(iwc.isLoggedOn()){
      //add(iwrb.getLocalizedString("manual","Instructions"));

      if(iwc.isParameterSet("save") || iwc.isParameterSet("save.x")){
        doContract(iwc);
        setParentToReload();
      }
      add(getSignatureTable(iwc));
    }
    else
      add(getText(localize("access_denied","Access denied")));

    //add(String.valueOf(iSubjectId));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  private PresentationObject getSignatureTable(IWContext iwc){
    int iContractId = Integer.parseInt( iwc.getParameter(prmContractId));
    try {

      ContractHome contractHome = (ContractHome) IDOLookup.getHome(Contract.class);
      Contract eContract =contractHome.findByPrimaryKey(new Integer(iContractId));
      Collection listOfContracts = contractHome.findByApartmentAndRented(eContract.getApartmentId(),new Boolean(true));
      User eUser =eContract.getUser();
      Apartment apartment = eContract.getApartment();
      IWTimestamp from = new IWTimestamp(eContract.getValidFrom());
      IWTimestamp to = new IWTimestamp(eContract.getValidTo());
      Applicant eApplicant = ((ApplicantHome)com.idega.data.IDOLookup.getHome(Applicant.class)).findByPrimaryKey(eContract.getApplicantId());
      boolean apartmentReturn = eContract.getIsRented();
      DataTable T = new DataTable();
      T.setWidth("100%");
      T.addButton(new CloseButton(getResourceBundle().getImage("close.gif")));
      String val = "";
      SubmitButton save = new SubmitButton(getResourceBundle().getImage("save.gif"),"save");
      boolean canSave = false;
      if(apartmentReturn){
        T.addTitle(getResourceBundle().getLocalizedString("apartment_return","Apartment return"));
        val = "return";
        if(eContract.getStatus().equals(is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean.statusEnded) || eContract.getStatus().equals(is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean.statusResigned) ) {
          T.addButton(save);
          canSave = true;
        }
      }
      else{
        T.addTitle(localize("apartment_deliver","Apartment deliver"));
        val = "deliver";
        if(eContract.getStatus().equals(is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean.statusSigned) ) {
          T.addButton(save);
          canSave = true;
        }
      }
      T.add(new HiddenInput("val",val),1,1);


      int row = 1;
      HiddenInput HI = new HiddenInput(prmContractId,eContract.getPrimaryKey().toString());
      T.add(HI,1,row);
      T.add(getHeader(localize("name","Name")),1,row);
      T.add(getText(eApplicant.getFullName()),2,row);
      row++;
      T.add(getHeader(localize("ssn","SocialNumber")),1,row);
      T.add(getText(eApplicant.getSSN()),2,row);
      row++;

      T.add(getHeader(localize("apartment","Apartment")),1,row);
      T.add(getText(getApartmentString(apartment)),2,row);

      row++;
      T.add(getHeader(localize("valid_from","Valid from")),1,row);
      T.add(getText(from.getLocaleDate(iwc)),2,row);
      row++;
      T.add(getHeader(localize("valid_to","Valid to")),1,row);
      T.add(getText(to.getLocaleDate(iwc)),2,row);
      row++;
      T.add(getHeader(localize("status","Status")),1,row);
      T.add(getText(getStatus(eContract.getStatus())),2,row);
      row++;
      T.add(getHeader(localize("returned","Returned")),1,row);
      java.sql.Timestamp retstamp = eContract.getReturnTime();
      if(retstamp !=null){
        IWTimestamp ret = new IWTimestamp(retstamp);
        T.add(getText(ret.getLocaleDate(iwc)),2,row);
      }
      row++;
      T.add(getHeader(localize("delivered","Delivered")),1,row);
      java.sql.Timestamp delstamp = eContract.getDeliverTime();
      if(delstamp !=null){
        IWTimestamp del = new IWTimestamp(delstamp);
        T.add(getText(del.getLocaleDate(iwc)),2,row);
      } 
      else {
      	if (canSave) {
	      	IWTimestamp	del = new IWTimestamp();
  	    	DateInput input = new DateInput("deliveredDate");
    	  	input.setDate(del.getSQLDate());
      		
        	T.add(input,2,row);
      	}
      }

      row++;
      boolean canSign = true;
      if(listOfContracts != null && !listOfContracts.isEmpty()){

        Contract C = (Contract) listOfContracts.iterator().next();
       
        if(!C.getPrimaryKey().toString().equals(eContract.getPrimaryKey().toString()))
          canSign = false;
      }

      Form F = new Form();
      F.add(T);
      return F;
    }
    catch (Exception ex) {
      return new Text("");
    }
  }

  private void doContract(IWContext iwc)throws RemoteException{

    Integer id = Integer.valueOf(iwc.getParameter(prmContractId));
    if(iwc.isParameterSet("val")){
      String val = iwc.getParameter("val");
      if(val.equals("return")){
        //ContractBusiness.returnKey(iwc,id);
      	getContractService(iwc).returnKey(iwc,id);
      }
      else if(val.equals("deliver")){
      	String from = iwc.getParameter("deliveredDate");
      	IWTimestamp fromStamp = new IWTimestamp(from);
//        ContractBusiness.deliverKey(iwc,id);
				//ContractBusiness.deliverKey(iwc,id,fromStamp.getTimestamp());
      	getContractService(iwc).deliverKey(id,fromStamp.getTimestamp());
      }
    }
  }


  private void doAddEmail( int iUserId ,IWContext iwc){
    String sEmail = iwc.getParameter("new_email");
   try {
		 UserBusiness ub = (UserBusiness) IBOLookup.getServiceInstance(iwc,UserBusiness.class);
		  ub.addNewUserEmail(iUserId,sEmail);
	}
	catch (RemoteException e) {
		e.printStackTrace();
	}
	}

  private String getApartmentString(Apartment A){
    StringBuffer S = new StringBuffer();
    Floor F = A.getFloor();
    Building B = F.getBuilding();
    Complex C = B.getComplex();
    S.append(A.getName());S.append(" ");
    S.append(F.getName());S.append(" ");
    S.append(B.getName());S.append(" ");
    S.append(C.getName());
    return S.toString();

  }

  private String getStatus(String status){
    String r = "";
    char c = status.charAt(0);
    switch (c) {
      case 'C': r = localize("created","Created"); break;
      case 'P': r = localize("printed","Printed"); break;
      case 'S': r = localize("signed","Signed");   break;
      case 'R': r = localize("rejected","Rejected");  break;
      case 'T': r = localize("terminated","Terminated");   break;
      case 'E': r = localize("ended","Ended");  break;
    }
    return r;
  }

  public void main(IWContext iwc)throws RemoteException{
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    control(iwc);
  }
}
