package is.idega.idegaweb.campus.block.allocation.presentation;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collection;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.data.ApartmentView;
import com.idega.core.data.GenericGroup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
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

public class ContractRenewWindow extends CampusWindow{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  
  public final static String prmContractId = "cam_contract_id";
  
  private boolean isAdmin;
  private boolean isLoggedOn;
  private String login = null;
  private String passwd = null;
  private boolean print = false;
  
  private GenericGroup group = null;
  private User eUser = null;
  private Contract contract = null;
  private Applicant applicant = null;
  private User contractUser = null;
  private Collection newContracts = null;
  private IWTimestamp lastDate = null;
  private Integer contractId = new Integer(-1);
  private boolean save = false;

  private ContractService ContractBusiness;

  private String errMsg = "";


  /*
    Bl?r litur ? topp # 27324B
    Hv?tur litur fyrir ne?an ?a? # FFFFFF
    Lj?sbl?r litur ? t?flu # ECEEF0
    Auka litur ?rl?ti? dekkri (? lagi a? nota l?ka) # CBCFD3
  */

  public ContractRenewWindow() {
    setWidth(530);
    setHeight(370);
    setResizable(true);
  }

  protected void control(IWContext iwc){
    //   debugParameters(iwc);
  
    init(iwc);
    if(iwc.isParameterSet("save") ){
      save = doSaveContract(iwc);
      if(save)
        add(getHeader(localize("new_contract_was_made","New contract was made")));
      else{
        add(getHeader(localize("new_contract_could_not_be_made","New contract could not be made")));
        add(Text.getBreak());
        add(getText(errMsg));
        add(getEditTable(iwc));
      }
    }
    else
      add(getEditTable(iwc));

  }

  private void init(IWContext iwc){
    contractId = Integer.valueOf( iwc.getParameter(prmContractId));
   if(contractId.intValue() > 0 && !save){
      try{

      	ContractHome cHome = getContractService(iwc).getContractHome();
      contract = cHome.findByPrimaryKey(contractId);
      applicant = contract.getApplicant();
      contractUser  = contract.getUser();
      newContracts = cHome.findByApplicantInCreatedStatus(contract.getApplicantId());
      java.sql.Date d = cHome.getLastValidToForApartment(contract.getApartmentId());

      lastDate = d!=null?new IWTimestamp(d):new IWTimestamp();
      }
      catch(Exception ex){

      }
    }
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  private PresentationObject getEditTable(IWContext iwc){

    //Table T = new Table(2,8);
    DataTable T = new DataTable();
    T.setWidth("100%");
    T.addTitle(localize("contract_renewal","Contract renewal"));
    T.addButton(new CloseButton(getBundle().getImage("close.gif")));
    

    int row = 1;
    int col = 1;

    try{

      if(newContracts ==null || newContracts.isEmpty()){
      	T.addButton(new SubmitButton(getResourceBundle().getImage("save.gif"),"save"));
        boolean isContractUser = contractUser.getID() == eUser.getID();

        if(contractUser !=null){

          T.add(new HiddenInput(prmContractId,contract.getPrimaryKey().toString()),1,row);
          T.add(getHeader(localize("name","Name")),1,row);
          T.add(getText(contractUser.getName()),2,row);

          row++;
          T.add(getHeader(localize("ssn","SocialNumber")),1,row);
          T.add(getText(applicant.getSSN()),2,row);
          row++;

          T.add(getHeader(localize("apartment","Apartment")),1,row);
          T.add(getText(contract.getApartment().getName()),2,row);

          row++;

          IWTimestamp today = IWTimestamp.RightNow();

          IWTimestamp[] stamps = ContractBusiness.getContractStampsForApartment(contract.getApartmentId());

          IWTimestamp fromDate = new IWTimestamp(contract.getValidFrom());
          IWTimestamp toDate = new IWTimestamp(contract.getValidTo());

          
          T.add(getHeader(localize("status_changed","Status changed")),1,row);
          DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
          T.add(getText(df.format(contract.getStatusDate())),2,row);
          row++;
          T.add(getHeader(localize("status","Status")),1,row);
          T.add(getText(ContractBusiness.getLocalizedStatus(getResourceBundle(),contract.getStatus())),2,row);
          row++;
          DateFormat df2 = DateFormat.getDateInstance(DateFormat.SHORT,iwc.getCurrentLocale());
          T.add(getHeader(localize("current_valid_from","Current valid from")),1,row);
          T.add(getText(df2.format(fromDate.getDate())),2,row);
          row++;
          T.add(getHeader(localize("current_valid_to","Current valid to")),1,row);
          T.add(getText(df2.format(toDate.getDate())),2,row);
          row++;

          DateInput from = new DateInput("from_date",true);
          from.setYearRange(today.getYear()-3,today.getYear()+7);
          if(lastDate.isLaterThan(fromDate)){
            from.setDate(lastDate.getSQLDate());
          }
          else{
            from.setDate(fromDate.getSQLDate());
          }
          from.keepStatusOnAction(true);
       

          T.add(getHeader(localize("new_start_date","New start date")),1,row);
          T.add(from,2,row);
          row++;

          DateInput to = new DateInput("to_date",true);
          to.setYearRange(today.getYear()-3,today.getYear()+7);

            to.setDate(toDate.getSQLDate());
            to.keepStatusOnAction(true);
       

          T.add(getHeader(localize("new_end_date","New end date")),1,row);
          T.add(to,2,row);
          row++;

          if(contract.getStatus().equals(ContractBMPBean.statusSigned)){
            T.add(getHeader(localize("end_old_contract","End old contract")),1,row);
            CheckBox endOldContract = new CheckBox("end_old_contr","true");
            T.add(endOldContract,2,row);
            row++;
            T.add(getHeader(localize("sync_dates","Synchronize dates")),1,row);
            CheckBox syncDates= new CheckBox("sync_dates","true");
            T.add(syncDates,2,row);
            T.add(getHeader(localize("sets_enddate_same_as_movingdate ","Sets the end date same as moving date below")),2,row);
            row++;
            DateInput moving = new DateInput("moving_date",true);
            moving.setYearRange(today.getYear()-3,today.getYear()+7);
              moving.setDate(today.getSQLDate());
              moving.keepStatusOnAction();
            
            T.add(getHeader(localize("moving_date","Moving date")),1,row);
            T.add(moving,2,row);
            row++;
          }
        }
        else
          T.add(getHeader(localize("no_contract_user","No Contract user found")),1,row);
      }
      else{
        T.add(getHeader(localize("has_already_new_contract","Has already a new Contract")),1,row);
      }

    }
    catch(Exception ex){ex.printStackTrace();}

    Form F = new Form();
    F.add(T);
    return F;
  }

  private boolean doSaveContract(IWContext iwc){
    try{
      Contract eContract = ContractBusiness.getContractHome().findByPrimaryKey(contractId);

      IWTimestamp from = null,to = null,move = null;
      String sfrom = iwc.getParameter("from_date");
      if(sfrom!=null && sfrom.length() == 10)
       from = new IWTimestamp(sfrom);
      String to_date = iwc.getParameter("to_date");
      if(to_date!=null && to_date.length() == 10)
        to = (new IWTimestamp(to_date));
      boolean endOld = iwc.isParameterSet("end_old_contr");
      boolean syncDates = iwc.isParameterSet("sync_dates");
      String move_date = iwc.getParameter("moving_date");
      if(move_date!=null && move_date.length() == 10)
         move = (new IWTimestamp(move_date));
      if(move==null){
      	
      }

      if(endOld){
      	 ContractBusiness.endContract((Integer)contract.getPrimaryKey(),move,"",syncDates);
      }

//      if(eContract.getStatus().equals(ContractBMPBean.statusSigned) && !endOld)
//        return false;

     
      if(from !=null && to !=null)
      	if(from.isLaterThan(new IWTimestamp(contract.getValidTo()))){
      		Contract c=  ContractBusiness.createNewContract((Integer)contractUser.getPrimaryKey(),(Integer)applicant.getPrimaryKey(),contract.getApartmentId(),from.getDate(),to.getDate());
      		return c!=null;
      	}
      	else{
      		this.errMsg = localize("contracts_overlap","Contracts overlap");
      		return false;
      	}
      else
        System.err.println("no dates in renewal");
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    return false;
  }

  private void doAddEmail( int iUserId ,IWContext iwc)throws RemoteException{
    String sEmail = iwc.getParameter("new_email");
    getUserService(iwc).addNewUserEmail(iUserId,sEmail);
  }
  
  private String getApartmentString(ApartmentView A){
  	StringBuffer S = new StringBuffer();
  	
  	S.append(A.getApartmentName());S.append(" ");
  	S.append(A.getFloorName());S.append(" ");
  	S.append(A.getBuildingName());S.append(" ");
  	S.append(A.getComplexName());
  	return S.toString();
  }


  public void main(IWContext iwc) throws Exception {
    eUser = iwc.getCurrentUser();
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    //isAdmin = iwc.hasEditPermission(this);
    isLoggedOn = iwc.isLoggedOn();
    ContractBusiness = getContractService(iwc);
    control(iwc);
  }
  
  
  

}
