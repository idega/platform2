package is.idega.idegaweb.campus.block.allocation.presentation;


import is.idega.idegaweb.campus.block.allocation.business.*;
import is.idega.idegaweb.campus.presentation.Edit;
import is.idega.idegaweb.campus.block.allocation.data.*;
import is.idega.idegaweb.campus.data.SystemProperties;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.*;
import com.idega.block.application.data.Applicant;

import com.idega.util.IWTimestamp;
import java.sql.SQLException;

import com.idega.core.contact.data.Email;
import com.idega.core.user.business.UserBusiness;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.data.Account;
import com.idega.core.accesscontrol.business.LoginCreator;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.user.data.User;
import com.idega.core.data.GenericGroup;
import com.idega.core.accesscontrol.data.LoginTable;
import java.util.List;
import com.idega.util.SendMail;

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
    if(iwc.isParameterSet("save") || iwc.isParameterSet("save.x")){
      save = doSaveContract(iwc);
      if(save)
        add(Edit.formatText(iwrb.getLocalizedString("new_contract_was_made","New contract was made")));
      else{
        add(Edit.formatText(iwrb.getLocalizedString("new_contract_could_not_be_made","New contract could not be made")));
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
      eContract = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKeyLegacy(iContractId);
      eApplicant = ((com.idega.block.application.data.ApplicantHome)com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(eContract.getApplicantId().intValue());
      contractUser  = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(eContract.getUserId().intValue());
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
    T.addButton(new SubmitButton(iwrb.getImage("save.gif"),"save"));

    int row = 1;
    int col = 1;

    try{

      if(newContracts ==null || newContracts.isEmpty()){
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
          IWTimestamp[] stamps = ContractBusiness.getContractStampsForApartment(eContract.getApartmentId().intValue());


          DateInput from = new DateInput("from_date",true);
          from.setYearRange(today.getYear()-3,today.getYear()+7);
          if(lastDate.isLaterThan(stamps[0])){
            from.setDate(lastDate.getSQLDate());
          }
          else{
            from.setDate(stamps[0].getSQLDate());
          }
          Edit.setStyle(from);

          T.add(Edit.formatText(iwrb.getLocalizedString("valid_from","Valid from")),1,row);
          T.add(from,2,row);
          row++;

          DateInput to = new DateInput("to_date",true);
          to.setYearRange(today.getYear()-3,today.getYear()+7);

            to.setDate(stamps[1].getSQLDate());
          Edit.setStyle(to);

          T.add(Edit.formatText(iwrb.getLocalizedString("valid_to","Valid to")),1,row);
          T.add(to,2,row);
          row++;

          if(eContract.getStatus().equals(ContractBMPBean.statusSigned)){
            T.add(Edit.formatText(iwrb.getLocalizedString("end_old_contract","End old contract")),1,row);
            CheckBox endOldContract = new CheckBox("end_old_contr","true");
            T.add(endOldContract,2,row);
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
      Contract eContract = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKeyLegacy(id);

      IWTimestamp from = null,to = null;
      String sfrom = iwc.getParameter("from_date");
      if(sfrom!=null && sfrom.length() == 10)
       from = new IWTimestamp(sfrom);
      String to_date = iwc.getParameter("to_date");
      if(to_date!=null && to_date.length() == 10)
        to = (new IWTimestamp(to_date));
      boolean endOld = iwc.isParameterSet("end_old_contr");

      if(endOld)
        ContractBusiness.endContract(eContract.getID(),new IWTimestamp(eContract.getValidTo()),"",false);

//      if(eContract.getStatus().equals(ContractBMPBean.statusSigned) && !endOld)
//        return false;
      if(from !=null && to !=null)
        return ContractBusiness.makeNewContract(iwc,contractUser,eApplicant,eContract.getApartmentId().intValue(),from,to);
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
