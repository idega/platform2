package is.idega.idegaweb.campus.block.allocation.presentation;


import is.idega.idegaweb.campus.block.allocation.business.ContractBusiness;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.data.SystemProperties;
import is.idega.idegaweb.campus.presentation.Edit;
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
import com.idega.core.data.Email;
import com.idega.core.data.EmailHome;
import com.idega.core.user.business.UserBusiness;
import com.idega.block.finance.business.FinanceFinder;
import com.idega.block.finance.data.Account;
import com.idega.core.accesscontrol.business.LoginCreator;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.user.data.User;
import com.idega.core.data.GenericGroup;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import is.idega.idegaweb.campus.block.mailinglist.business.MailingListBusiness;
import is.idega.idegaweb.campus.block.mailinglist.business.LetterParser;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class ContractSignWindow extends Window{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private boolean isAdmin;
  private String login = null;
  private String passwd = null;
  private boolean print = false;
  private SystemProperties SysProps = null;
  private GenericGroup eGroup = null;
  public static final String prmAdmin = "is_camp_csat";

  /*
    Blár litur í topp # 27324B
    Hvítur litur fyrir neðan það # FFFFFF
    Ljósblár litur í töflu # ECEEF0
    Auka litur örlítið dekkri (í lagi að nota líka) # CBCFD3
  */

  public ContractSignWindow() {
    setResizable(true);
  }

  protected void control(IWContext iwc)throws java.rmi.RemoteException{
    debugParameters(iwc);
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    // permissons !!
    if(true){
      if(iwc.getApplicationAttribute(is.idega.idegaweb.campus.data.SystemPropertiesBMPBean.getEntityTableName())!=null){
      SysProps = (SystemProperties)iwc.getApplicationAttribute(is.idega.idegaweb.campus.data.SystemPropertiesBMPBean.getEntityTableName());
      }

      if(iwc.isParameterSet("save") || iwc.isParameterSet("save.x")){
        doSignContract(iwc);
        setParentToReload();
//        this.getParentPage().
      }
      add(getSignatureTable(iwc));
    }
    else
      add(Edit.formatText(iwrb.getLocalizedString("access_denied","Access denied")));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  private PresentationObject getSignatureTable(IWContext iwc)throws java.rmi.RemoteException{
    int iContractId = Integer.parseInt( iwc.getParameter("signed_id"));
    try {
      Contract eContract = ((is.idega.idegaweb.campus.block.allocation.data.ContractHome)com.idega.data.IDOLookup.getHomeLegacy(Contract.class)).findByPrimaryKeyLegacy(iContractId);
      List listOfContracts = ContractFinder.listOfApartmentContracts(eContract.getApartmentId().intValue(),true);
      User eUser = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(eContract.getUserId().intValue());
      IWTimestamp from = new IWTimestamp(eContract.getValidFrom());
      IWTimestamp to = new IWTimestamp(eContract.getValidTo());
      Applicant eApplicant = ((com.idega.block.application.data.ApplicantHome)com.idega.data.IDOLookup.getHomeLegacy(Applicant.class)).findByPrimaryKeyLegacy(eContract.getApplicantId().intValue());
      Collection lEmails = null;
	try {
		lEmails = ((EmailHome) IDOLookup.getHome(Email.class)).findEmailsForUser(eContract.getUserId().intValue());
	}
	catch (IDOLookupException e) {
		e.printStackTrace();
	}
	catch (FinderException e) {
		e.printStackTrace();
	}
      List lFinanceAccounts = FinanceFinder.getInstance().listOfAccountByUserIdAndType(eContract.getUserId().intValue(),com.idega.block.finance.data.AccountBMPBean.typeFinancial);
      List lPhoneAccounts = FinanceFinder.getInstance().listOfAccountByUserIdAndType(eContract.getUserId().intValue(),com.idega.block.finance.data.AccountBMPBean.typePhone);

      if(SysProps != null){
        int groupId = SysProps.getDefaultGroup();
        try {
          eGroup = ((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).findByPrimaryKeyLegacy(groupId);
        }
        catch (SQLException ex) {
          ex.printStackTrace();
          eGroup = null;
        }
      }
      LoginTable loginTable = LoginDBHandler.getUserLogin(eContract.getUserId().intValue());
      DataTable T = new DataTable();
      T.setWidth("100%");
      T.addTitle(iwrb.getLocalizedString("contract_signing","Contract signing"));
      T.addButton(new CloseButton(iwrb.getImage("close.gif")));
      T.addButton(new SubmitButton(iwrb.getImage("save.gif"),"save"));


      SubmitButton save = new SubmitButton("save",iwrb.getLocalizedString("save","Save"));
      SubmitButton signed = new SubmitButton("sign",iwrb.getLocalizedString("signed","Signed"));
      CloseButton close = new CloseButton(iwrb.getLocalizedString("close","Close"));
      PrintButton PB = new PrintButton(iwrb.getLocalizedString("print","Print"));
      TextInput emailInput = new TextInput("new_email");
	  emailInput.setAsEmail(iwrb.getLocalizedString("warning_illlegal_email","Please enter a legal email address"));
      CheckBox accountCheck = new CheckBox("new_fin_account","true");
      accountCheck.setChecked(true);
      CheckBox phoneAccountCheck = new CheckBox("new_phone_account","true");
      phoneAccountCheck.setChecked(true);
      CheckBox loginCheck = new CheckBox("new_login","true");
      loginCheck.setChecked(true);

      int row = 1;
      HiddenInput HI = new HiddenInput("signed_id",String.valueOf(eContract.getID()));

      T.add(HI,1,row);
      if(iwc.isParameterSet(prmAdmin)){
        T.add(new HiddenInput(prmAdmin,"true"));
      }
      T.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),1,row);
      T.add(Edit.formatText(eApplicant.getFullName()),2,row);
      row++;
      T.add(Edit.formatText(iwrb.getLocalizedString("ssn","SocialNumber")),1,row);
      T.add(Edit.formatText(eApplicant.getSSN()),2,row);
      row++;
      T.add(Edit.formatText(iwrb.getLocalizedString("apartment","Apartment")),1,row);
      T.add(Edit.formatText(getApartmentString(((com.idega.block.building.data.ApartmentHome)com.idega.data.IDOLookup.getHomeLegacy(Apartment.class)).findByPrimaryKeyLegacy(eContract.getApartmentId().intValue()))),2,row);
      row++;
      T.add(Edit.formatText(iwrb.getLocalizedString("valid_from","Valid from")),1,row);
      T.add(Edit.formatText(from.getLocaleDate(iwc)),2,row);
      row++;
      T.add(Edit.formatText(iwrb.getLocalizedString("valid_to","Valid to")),1,row);
      T.add(Edit.formatText(to.getLocaleDate(iwc)),2,row);
      row++;
      boolean canSign = true;
      int con_id = -1;
      if(listOfContracts != null && !listOfContracts.isEmpty()){
        Contract C = (Contract) listOfContracts.get(0);
        con_id = C.getID();
        if(con_id != eContract.getID())
          canSign = false;
      }
      if(canSign ){
        T.add(Edit.formatText(iwrb.getLocalizedString("email","Email")),1,row);
        if(lEmails !=null){
          //T.add(Edit.formatText( ((Email)lEmails.get(0)).getEmailAddress()),2,row);
          int pos = lEmails.size()-1;
          Edit.setStyle(emailInput);
          Email email =null;
          for (Iterator iter = lEmails.iterator(); iter.hasNext();) {
			email = (Email) iter.next();
		  }
		  if(email!=null)
		  emailInput.setContent(email.getEmailAddress());
          T.add(emailInput,2,row);
        }
        else{
          T.add(emailInput,2,row);
        }
        row++;
        if(eGroup != null){
          HiddenInput Hgroup = new HiddenInput("user_group",String.valueOf(eGroup.getID()));
          T.add(Hgroup);
          if(lFinanceAccounts.isEmpty()){
            T.add(accountCheck,2,row);
            T.add(Edit.formatText(iwrb.getLocalizedString("fin_account","New finance account")),2,row);
          }
          else{
            int len = lFinanceAccounts.size();
            for (int i = 0; i < len; i++) {
              T.add(Edit.formatText(iwrb.getLocalizedString("fin_account","Finance account")),1,row);
              T.add(Edit.formatText( ((Account)lFinanceAccounts.get(i)).getName() +" "),2,row);
            }
          }
          row++;
          if(lPhoneAccounts.isEmpty()){
            T.add(phoneAccountCheck,2,row);
            T.add(Edit.formatText(iwrb.getLocalizedString("phone_account","New phone account")),2,row);
          }
          else{
            int len = lPhoneAccounts.size();
            for (int i = 0; i < len; i++) {
              T.add(Edit.formatText(iwrb.getLocalizedString("phone_account","Phone account")),1,row);
              T.add(Edit.formatText( ((Account)lPhoneAccounts.get(i)).getName() +" "),2,row);
            }
          }
          row++;
          if(loginTable != null ){
            T.add(Edit.formatText(iwrb.getLocalizedString("login","Login")),1,row);
            T.add(Edit.formatText(loginTable.getUserLogin()),2,row);
            row++;
            T.add(Edit.formatText(iwrb.getLocalizedString("passwd","Passwd")),1,row);
            if(passwd != null)
              T.add(Edit.formatText(passwd),2,row++);
          }
          else{
            T.add(loginCheck,2,row);
            T.add(Edit.formatText(iwrb.getLocalizedString("new_login","New login")),2,row);
          }
          row++;

          /*
          if(eContract.getStatus().equalsIgnoreCase(eContract.statusSigned))
            T.add(save,2,row);
          else
            T.add(signed,2,row);
          if(print){
            T.add(PB,2,row);
          }
          T.add(close,2,row);
          */

        }
        else{
        T.add(Edit.formatText(iwrb.getLocalizedString("sys_props_error","System property error")),2,row++);
        T.add(Edit.formatText(iwrb.getLocalizedString("no_default_group","No default group")),2,row++);
        }
      }
      else{
        row++;
        Text msg = Edit.formatText(iwrb.getLocalizedString("contract_conflict","Apartment is still in rent"));
        msg.setFontColor("#FF0000");
        T.add(msg,2,row);
        //T.add(CampusContracts.getReSignLink(iwb.getImage("/scissors.gif"),con_id),2,row);
      }

      Form F = new Form();
      F.add(T);
      return F;
    }
    catch (SQLException ex) {
      return new Text("");
    }
  }

  private void doSignContract(IWContext iwc){

    int id = Integer.parseInt(iwc.getParameter("signed_id"));
    String sEmail = iwc.getParameter("new_email");
    String sSendMail = iwc.getParameter("send_mail");
    String sFinAccount = iwc.getParameter("new_fin_account");
    String sPhoneAccount = iwc.getParameter("new_phone_account");
    String sCreateLogin = iwc.getParameter("new_login");
    String sUserGroup = iwc.getParameter("user_group");
    String sSigned =  iwc.getParameter("sign");
    int iGroupId = sUserGroup != null ? Integer.parseInt(sUserGroup):-1;
    boolean sendMail =  sSendMail != null ? true:false;
    sendMail = true;
    boolean newAccount =  sFinAccount != null ? true:false;
    boolean newPhoneAccount =   sPhoneAccount != null ? true:false;
    boolean createLogin =   sCreateLogin != null ? true:false;
    passwd = ContractBusiness.signCampusContract(iwc,id,iGroupId ,1,sEmail,sendMail,
      newAccount ,newPhoneAccount ,createLogin ,false,iwrb,login,passwd  );
    if(login !=null && passwd !=null)
      print = true;
    else
      print = false;
    //add(passwd);
  }

  private void doAddEmail( int iUserId ,IWContext iwc){
    String sEmail = iwc.getParameter("new_email");
    UserBusiness.addNewUserEmail(iUserId,sEmail);
  }

  private PresentationObject getApartmentTable(Apartment A){
    Table T = new Table();
    Floor F = BuildingCacher.getFloor(A.getFloorId());
    Building B = BuildingCacher.getBuilding(F.getBuildingId());
    Complex C = BuildingCacher.getComplex(B.getComplexId());
    T.add(Edit.formatText(A.getName()),1,1);
    T.add(Edit.formatText(F.getName()),2,1);
    T.add(Edit.formatText(B.getName()),3,1);
    T.add(Edit.formatText(C.getName()),4,1);
    return T;
  }

  private String getApartmentString(Apartment A){
    StringBuffer S = new StringBuffer();
    Floor F = BuildingCacher.getFloor(A.getFloorId());
    Building B = BuildingCacher.getBuilding(F.getBuildingId());
    Complex C = BuildingCacher.getComplex(B.getComplexId());
    S.append(A.getName());S.append(" ");
    S.append(F.getName());S.append(" ");
    S.append(B.getName());S.append(" ");
    S.append(C.getName());
    return S.toString();
  }

  public void main(IWContext iwc)throws java.rmi.RemoteException{
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.isParameterSet(prmAdmin);
    control(iwc);
  }
}
