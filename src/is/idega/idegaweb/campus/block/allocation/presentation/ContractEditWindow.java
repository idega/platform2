package is.idega.idegaweb.campus.block.allocation.presentation;


import is.idega.idegaweb.campus.block.allocation.data.Contract;

import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import java.rmi.RemoteException;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingService;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentView;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.business.IBOLookup;
import com.idega.core.data.GenericGroup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TimestampInput;
import com.idega.presentation.ui.Window;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWTimestamp;


import com.idega.block.application.data.Applicant;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;

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
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TimestampInput;
import com.idega.presentation.ui.Window;
import com.idega.util.IWTimestamp;


/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class ContractEditWindow extends CampusWindow{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  
  private User eUser = null;
  private BuildingService buildingService =null;

  /*
    Bl?r litur ? topp # 27324B
    Hv?tur litur fyrir ne?an ?a? # FFFFFF
    Lj?sbl?r litur ? t?flu # ECEEF0
    Auka litur ?rl?ti? dekkri (? lagi a? nota l?ka) # CBCFD3
  */

  public ContractEditWindow() {
    setWidth(530);
    setHeight(370);
    setResizable(true);
  }

  protected void control(IWContext iwc)throws RemoteException{
//    debugParameters(iwc);
   
    buildingService = (BuildingService) IBOLookup.getServiceInstance(iwc,BuildingService.class);

      if(iwc.isParameterSet("save") || iwc.isParameterSet("save.x")){
        doSaveContract(iwc);
      }
      add(getEditTable(iwc));

    //  add(getText(localize("access_denied","Access denied")));

    //add(String.valueOf(iSubjectId));
  }


  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  private PresentationObject getEditTable(IWContext iwc){
    int iContractId = Integer.parseInt( iwc.getParameter("contract_id"));
    //Table T = new Table(2,8);
    DataTable T = new DataTable();
    T.setWidth("100%");
    T.addTitle(localize("contract_info","Contract info"));
    T.addButton(new CloseButton(getResourceBundle().getImage("close.gif")));
    T.addButton(new SubmitButton(getResourceBundle().getImage("save.gif"),"save"));

    int row = 1;
    int col = 1;

    try{
      if(iContractId > 0){

        Contract eContract = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(iContractId));
        Applicant eApplicant = eContract.getApplicant();
        User user = eContract.getUser();

        boolean isContractUser = user.getID() == eUser.getID();
        if(user !=null){

         
          T.add(new HiddenInput("contract_id",eContract.getPrimaryKey().toString()),1,row);
          T.add(getText(localize("name","Name")),1,row);
          T.add(getText(user.getName()),2,row);

          row++;
          T.add(getText(localize("ssn","SocialNumber")),1,row);
          T.add(getText(eApplicant.getSSN()),2,row);
          row++;

          T.add(getText(localize("apartment","Apartment")),1,row);
          T.add(getText(getApartmentString(eContract.getApartment())),2,row);

          row++;

          IWTimestamp today = IWTimestamp.RightNow();

          DateInput from = new DateInput("from_date",true);
          from.setYearRange(today.getYear()-3,today.getYear()+7);
          if(eContract.getValidFrom()!=null)
            from.setDate(eContract.getValidFrom());
      
          T.add(getText(localize("valid_from","Valid from")),1,row);
          T.add(from,2,row);
          row++;
          DateInput to = new DateInput("to_date",true);
          to.setYearRange(today.getYear()-3,today.getYear()+7);
          if(eContract.getValidTo()!=null)
            to.setDate(eContract.getValidTo());
        
          T.add(getText(localize("valid_to","Valid to")),1,row);
          T.add(to,2,row);
          row++;
          DateInput moving = new DateInput("moving_date",true);
          moving.setYearRange(today.getYear()-3,today.getYear()+7);
          if(eContract.getMovingDate()!=null)
            moving.setDate(eContract.getMovingDate());
          
          T.add(getText(localize("moving_to","Moving date")),1,row);
          T.add(moving,2,row);
          row++;
          TimestampInput deliver = new TimestampInput("deliver_date",true);
          deliver.setYearRange(today.getYear()-3,today.getYear()+7);
          if(eContract.getDeliverTime()!=null)
            deliver.setTimestamp(eContract.getDeliverTime());
 
          T.add(getText(localize("deliver_date","Deliver date")),1,row);
          T.add(deliver,2,row);
          row++;
          TimestampInput returnd = new TimestampInput("return_date",true);
          returnd.setYearRange(today.getYear()-3,today.getYear()+7);
          if(eContract.getReturnTime()!=null)
            returnd.setTimestamp(eContract.getReturnTime());
          
          T.add(getText(localize("return_date","Return date")),1,row);
          T.add(returnd,2,row);
          row++;
          T.add(getText(localize("has_key","Has key")),1,row);
          //T.add(status,2,row);
          T.add(getText(getStatus(eContract.getIsRented()?"yes":"no")),2,row);
          row++;
          //DropdownMenu status = getStatusDrop("status",eContract.getStatus());
          //Edit.setStyle(status);
          T.add(getText(localize("status","Status")),1,row);
          //T.add(status,2,row);
          T.add(getText(getStatus(eContract.getStatus())),2,row);
          row++;
          T.add(getText(localize("status_date","Status date")),1,row);
          if(eContract.getStatusDate()!=null){
            String sdate = eContract.getStatusDate().toString();
            T.add(getText(sdate),2,row);
          }
          row++;
          TextArea info = new TextArea("info");
          if(eContract.getResignInfo()!=null)
            info.setContent(eContract.getResignInfo());
          T.add(getText(localize("resign_info","Resign info")),1,row);
      
          T.add(info,2,row);
          row++;

        }
      }
    }
    catch(Exception ex){}

    Form F = new Form();
    F.add(T);
    return F;
  }

  private void doSaveContract(IWContext iwc){
    try{

      int id = Integer.parseInt(iwc.getParameter("contract_id"));

      Contract eContract = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHome(Contract.class)).findByPrimaryKey(new Integer(id));

      IWTimestamp from = null,to = null,moving = null,deliver = null, retur = null;
      String sfrom = iwc.getParameter("from_date");
      if(sfrom!=null && sfrom.length() == 10)
        eContract.setValidFrom(new IWTimestamp(sfrom).getSQLDate());
      String to_date = iwc.getParameter("to_date");
      if(to_date!=null && to_date.length() == 10)
        eContract.setValidTo(new IWTimestamp(to_date).getSQLDate());
      String moving_date = iwc.getParameter("moving_date");
      if(moving_date!=null && moving_date.length() == 10)
        eContract.setMovingDate(new IWTimestamp(moving_date).getSQLDate());
      String deliver_date = iwc.getParameter("deliver_date");
      if(deliver_date!=null && deliver_date.length() > 0)
        eContract.setDeliverTime(new IWTimestamp(deliver_date).getTimestamp());
      String return_date = iwc.getParameter("return_date");
      if(return_date!=null && return_date.length() > 0)
        eContract.setReturnTime(new IWTimestamp(return_date).getTimestamp());
      if(iwc.isParameterSet("status")){
        eContract.setStatus((iwc.getParameter("status")));
        eContract.setStatusDate(IWTimestamp.RightNow().getSQLDate());
      }
      if(iwc.isParameterSet("info")){
        eContract.setResignInfo((iwc.getParameter("info")));
      }

      eContract.store();
    }
    catch(Exception ex){
      ex.printStackTrace();
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
   
    S.append(A.getName());S.append(" ");
    Floor F = A.getFloor();
    S.append(F.getName());S.append(" ");
    Building B = F.getBuilding();
    S.append(B.getName());S.append(" ");
    Complex C = B.getComplex();
    S.append(C.getName());
    return S.toString();
  }

  public void main(IWContext iwc) throws Exception {
    eUser = iwc.getCurrentUser();
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
   
    control(iwc);
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
 
}
