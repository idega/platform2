package is.idega.idegaweb.campus.block.allocation.presentation;


import is.idega.idegaweb.campus.block.allocation.business.ContractBusiness;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.block.allocation.data.ContractHome;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.presentation.Edit;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.List;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.ApplicantHome;
import com.idega.block.building.business.BuildingCacher;
import com.idega.core.data.GenericGroup;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.core.user.data.UserHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
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

public class ContractRenewWindow extends Window{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  public final static String prmContractId = "cam_contract_id";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private boolean isAdmin;
  private boolean isLoggedOn;
  private String login = null;
  private String passwd = null;
  private boolean print = false;
  private SystemProperties SysProps = null;
  private GenericGroup eGroup = null;
  private User eUser = null;
  private Contract eContract = null;
  private Applicant eApplicant = null;
  private User contractUser = null;
  private List newContracts = null;
  private IWTimestamp lastDate = null;
  private int iContractId = -1;
  private boolean save = false;
  private String errMsg = "";

  /*
    Blár litur í topp # 27324B
    Hvítur litur fyrir neðan það # FFFFFF
    Ljósblár litur í töflu # ECEEF0
    Auka litur örlítið dekkri (í lagi að nota líka) # CBCFD3
  */

  public ContractRenewWindow() {
    setWidth(530);
    setHeight(370);
    setResizable(true);
  }

  protected void control(IWContext iwc){
    //   debugParameters(iwc);
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    init(iwc);
    if(iwc.isParameterSet("save") ){
      save = doSaveContract(iwc);
      if(save)
        add(Edit.formatText(iwrb.getLocalizedString("new_contract_was_made","New contract was made")));
      else{
        add(Edit.formatText(iwrb.getLocalizedString("new_contract_could_not_be_made","New contract could not be made")));
        add(Text.getBreak());
        add(Edit.formatText(errMsg));
        add(getEditTable(iwc));
      }
    }
    else
      add(getEditTable(iwc));

  }

  private void init(IWContext iwc){
    iContractId = Integer.parseInt( iwc.getParameter(prmContractId));
   if(iContractId > 0 && !save){
      try{
      eContract = ((ContractHome)IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKeyLegacy(iContractId);
      eApplicant = ((ApplicantHome)IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(eContract.getApplicantId().intValue());
      contractUser  = ((UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(eContract.getUserId().intValue());
      newContracts = ContractFinder.listOfApplicantContracts(eApplicant.getID(),ContractBMPBean.statusCreated);
      java.sql.Date d= ContractFinder.getLastContractDateForApartment(eContract.getApartmentId().intValue());
      lastDate = d!=null?new IWTimestamp(d):new IWTimestamp();
      }
      catch(SQLException ex){

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
    T.addTitle(iwrb.getLocalizedString("contract_renewal","Contract renewal"));
    T.addButton(new CloseButton(iwrb.getImage("close.gif")));
    

    int row = 1;
    int col = 1;

    try{

      if(newContracts ==null || newContracts.isEmpty()){
      	T.addButton(new SubmitButton(iwrb.getImage("save.gif"),"save"));
        boolean isContractUser = contractUser.getID() == eUser.getID();

        if(contractUser !=null){
          T.add(new HiddenInput(prmContractId,String.valueOf(eContract.getID())),1,row);
          T.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),1,row);
          T.add(Edit.formatText(contractUser.getName()),2,row);
          row++;
          T.add(Edit.formatText(iwrb.getLocalizedString("ssn","SocialNumber")),1,row);
          T.add(Edit.formatText(eApplicant.getSSN()),2,row);
          row++;
          T.add(Edit.formatText(iwrb.getLocalizedString("apartment","Apartment")),1,row);
          T.add(Edit.formatText(BuildingCacher.getApartmentString((eContract.getApartmentId().intValue()))),2,row);
          row++;

          IWTimestamp today = IWTimestamp.RightNow();
          IWTimestamp fromDate = new IWTimestamp(eContract.getValidFrom());
          IWTimestamp toDate = new IWTimestamp(eContract.getValidTo());
          
          T.add(Edit.formatText(iwrb.getLocalizedString("status_changed","Status changed")),1,row);
          DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT,iwc.getCurrentLocale());
          T.add(Edit.formatText(df.format(eContract.getStatusDate())),2,row);
          row++;
          T.add(Edit.formatText(iwrb.getLocalizedString("status","Status")),1,row);
          T.add(Edit.formatText(ContractBusiness.getLocalizedStatus(iwrb,eContract.getStatus())),2,row);
          row++;
          DateFormat df2 = DateFormat.getDateInstance(DateFormat.SHORT,iwc.getCurrentLocale());
          T.add(Edit.formatText(iwrb.getLocalizedString("current_valid_from","Current valid from")),1,row);
          T.add(Edit.formatText(df2.format(fromDate.getDate())),2,row);
          row++;
          T.add(Edit.formatText(iwrb.getLocalizedString("current_valid_to","Current valid to")),1,row);
          T.add(Edit.formatText(df2.format(toDate.getDate())),2,row);
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
          Edit.setStyle(from);

          T.add(Edit.formatText(iwrb.getLocalizedString("new_start_date","New start date")),1,row);
          T.add(from,2,row);
          row++;

          DateInput to = new DateInput("to_date",true);
          to.setYearRange(today.getYear()-3,today.getYear()+7);

            to.setDate(toDate.getSQLDate());
            to.keepStatusOnAction(true);
          Edit.setStyle(to);

          T.add(Edit.formatText(iwrb.getLocalizedString("new_end_date","New end date")),1,row);
          T.add(to,2,row);
          row++;

          if(eContract.getStatus().equals(ContractBMPBean.statusSigned)){
            T.add(Edit.formatText(iwrb.getLocalizedString("end_old_contract","End old contract")),1,row);
            CheckBox endOldContract = new CheckBox("end_old_contr","true");
            T.add(endOldContract,2,row);
            row++;
            T.add(Edit.formatText(iwrb.getLocalizedString("sync_dates","Synchronize dates")),1,row);
            CheckBox syncDates= new CheckBox("sync_dates","true");
            T.add(syncDates,2,row);
            T.add(Edit.formatText(iwrb.getLocalizedString("sets_enddate_same_as_movingdate ","Sets the end date same as moving date below")),2,row);
            row++;
            DateInput moving = new DateInput("moving_date",true);
            moving.setYearRange(today.getYear()-3,today.getYear()+7);
              moving.setDate(today.getSQLDate());
              moving.keepStatusOnAction();
            Edit.setStyle(moving);
            T.add(Edit.formatText(iwrb.getLocalizedString("moving_date","Moving date")),1,row);
            T.add(moving,2,row);
            row++;
          }
        }
        else
          T.add(Edit.formatText(iwrb.getLocalizedString("no_contract_user","No Contract user found")),1,row);
      }
      else{
        T.add(Edit.formatText(iwrb.getLocalizedString("has_already_new_contract","Has already a new Contract")),1,row);
      }

    }
    catch(Exception ex){ex.printStackTrace();}

    Form F = new Form();
    F.add(T);
    return F;
  }

  private boolean doSaveContract(IWContext iwc){
    try{

      int id = iContractId;
      Contract eContract = ((ContractHome)IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKeyLegacy(id);

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

      if(endOld)
        eContract = ContractBusiness.endContract(eContract.getID(),move,"",syncDates);

//      if(eContract.getStatus().equals(ContractBMPBean.statusSigned) && !endOld)
//        return false;
      if(from !=null && to !=null)
      	if(from.isLaterThan(new IWTimestamp(eContract.getValidTo()))){
      		return ContractBusiness.makeNewContract(iwc,contractUser,eApplicant,eContract.getApartmentId().intValue(),from,to);
      	}
      	else{
      		this.errMsg = iwrb.getLocalizedString("contracts_overlap","Contracts overlap");
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

  private void doAddEmail( int iUserId ,IWContext iwc){
    String sEmail = iwc.getParameter("new_email");
    UserBusiness.addNewUserEmail(iUserId,sEmail);
  }


  public void main(IWContext iwc) throws Exception {
    eUser = iwc.getUser();
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    //isAdmin = iwc.hasEditPermission(this);
    isLoggedOn = com.idega.core.accesscontrol.business.LoginBusinessBean.isLoggedOn(iwc);
    control(iwc);
  }

}
