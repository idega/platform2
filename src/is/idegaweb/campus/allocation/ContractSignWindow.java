package is.idegaweb.campus.allocation;

import is.idegaweb.campus.allocation.business.ContractBusiness;
import is.idegaweb.campus.presentation.Edit;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.*;
import com.idega.block.application.data.Applicant;
import is.idegaweb.campus.entity.Contract;
import is.idegaweb.campus.entity.SystemProperties;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import com.idega.core.data.Email;
import com.idega.core.user.business.UserBusiness;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.data.Account;
import com.idega.block.login.business.LoginCreator;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.user.data.User;
import com.idega.core.data.GenericGroup;
import com.idega.core.accesscontrol.data.LoginTable;
import java.util.List;
import com.idega.jmodule.server.mail.SendMail;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ContractSignWindow extends Window{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private boolean isAdmin;
  private String login = null;
  private String passwd = null;
  private boolean print = false;
  private SystemProperties SysProps = null;
  private GenericGroup eGroup = null;

  /*
    Blár litur í topp # 27324B
    Hvítur litur fyrir neðan það # FFFFFF
    Ljósblár litur í töflu # ECEEF0
    Auka litur örlítið dekkri (í lagi að nota líka) # CBCFD3
  */

  public ContractSignWindow() {
  }

  protected void control(ModuleInfo modinfo){
    setParentToReload();
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    if(isAdmin){
      if(modinfo.getApplicationAttribute(SysProps.getEntityTableName())!=null){
      SysProps = (SystemProperties)modinfo.getApplicationAttribute(SysProps.getEntityTableName());
      }

      if(modinfo.getParameter("sign")!=null || modinfo.getParameter("save")!=null){
        doSignContract(modinfo);
      }
      add(getSignatureTable(modinfo));
    }
    else
      add(Edit.formatText(iwrb.getLocalizedString("access_denied","Access denied")));

    //add(String.valueOf(iSubjectId));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public ModuleObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  private ModuleObject getSignatureTable(ModuleInfo modinfo){
    int iContractId = Integer.parseInt( modinfo.getParameter("signed_id"));
    try {
      Contract eContract = new Contract(iContractId);
      User eUser = new User(eContract.getUserId().intValue());
      idegaTimestamp from = new idegaTimestamp(eContract.getValidFrom());
      idegaTimestamp to = new idegaTimestamp(eContract.getValidTo());
      Applicant eApplicant = new Applicant(eContract.getApplicantId().intValue());
      List lEmails = UserBusiness.listOfUserEmails(eContract.getUserId().intValue());
      List lFinanceAccounts = AccountManager.listOfAccounts(eContract.getUserId().intValue(),Account.typeFinancial);
      List lPhoneAccounts = AccountManager.listOfAccounts(eContract.getUserId().intValue(),Account.typePhone);

      if(SysProps != null){
        int groupId = SysProps.getDefaultGroup();
        try {
          eGroup = new GenericGroup(groupId);
        }
        catch (SQLException ex) {
          eGroup = null;
        }
      }
      LoginTable loginTable = LoginDBHandler.findUserLogin(eContract.getUserId().intValue());
      Table T = new Table();

      SubmitButton save = new SubmitButton("save",iwrb.getLocalizedString("save","Save"));
      SubmitButton signed = new SubmitButton("sign",iwrb.getLocalizedString("signed","Signed"));
      CloseButton close = new CloseButton(iwrb.getLocalizedString("close","Close"));
      PrintButton PB = new PrintButton(iwrb.getLocalizedString("print","Print"));
      TextInput email = new TextInput("new_email");
      CheckBox accountCheck = new CheckBox("new_fin_account","true");
      accountCheck.setChecked(true);
      CheckBox phoneAccountCheck = new CheckBox("new_phone_account","true");
      phoneAccountCheck.setChecked(true);
      CheckBox loginCheck = new CheckBox("new_login","true");
      loginCheck.setChecked(true);

      int row = 2;
      T.add(Edit.headerText(iwrb.getLocalizedString("name","Name")+" : "),1,row);
      T.add(eApplicant.getFullName(),2,row);
      row++;
      T.add(Edit.headerText(iwrb.getLocalizedString("ssn","SocialNumber")+" : "),1,row);
      T.add(eApplicant.getSSN(),2,row);
      row++;
      T.add(Edit.headerText(iwrb.getLocalizedString("apartment","Apartment")+" : "),1,row);
      T.add(Edit.formatText(getApartmentString(new Apartment(eContract.getApartmentId().intValue()))),2,row);
      row++;
      T.add(Edit.headerText(iwrb.getLocalizedString("contractdate","Contract date")+" :"),1,row);
      T.add(Edit.formatText(from.getLocaleDate(modinfo)+" "+to.getLocaleDate(modinfo)),2,row);
      row++;
      T.add(Edit.headerText(iwrb.getLocalizedString("email","Email")+" : "),1,row);
      if(lEmails !=null){
        //T.add(Edit.formatText( ((Email)lEmails.get(0)).getEmailAddress()),2,row);
        int pos = lEmails.size()-1;
        email.setContent(((Email)lEmails.get(pos)).getEmailAddress());
        T.add(email,2,row);
      }
      else{
        T.add(email,2,row);
      }
      row++;
      row++;
      if(eGroup != null){
        HiddenInput Hgroup = new HiddenInput("user_group",String.valueOf(eGroup.getID()));
        T.add(Hgroup);
        if(lFinanceAccounts == null){
          T.add(accountCheck,2,row);
          T.add(Edit.headerText(iwrb.getLocalizedString("fin_account","New finance account")),2,row);
        }
        else{
          int len = lFinanceAccounts.size();
          for (int i = 0; i < len; i++) {
            T.add(Edit.headerText(iwrb.getLocalizedString("fin_account","Finance account")+" : "),1,row);
            T.add(Edit.formatText( ((Account)lFinanceAccounts.get(i)).getName() +" "),2,row);
          }
        }
        row++;
        if(lPhoneAccounts == null){
          T.add(phoneAccountCheck,2,row);
          T.add(Edit.headerText(iwrb.getLocalizedString("phone_account","New phone account")),2,row);
        }
        else{
          int len = lPhoneAccounts.size();
          for (int i = 0; i < len; i++) {
            T.add(Edit.headerText(iwrb.getLocalizedString("phone_account","Phone account")+" : "),1,row);
            T.add(Edit.formatText( ((Account)lPhoneAccounts.get(i)).getName() +" "),2,row);
          }
        }
        row++;
        if(loginTable != null ){
          T.add(Edit.headerText(iwrb.getLocalizedString("login","Login")+" : "),1,row);
          T.add(Edit.formatText(loginTable.getUserLogin()),2,row);
          row++;
          T.add(Edit.headerText(iwrb.getLocalizedString("passwd","Passwd")+" : "),1,row);
          if(passwd != null)
            T.add(Edit.formatText(passwd),2,row++);
        }
        else{
          T.add(loginCheck,2,row);
          T.add(Edit.headerText(iwrb.getLocalizedString("new_login","New login")),2,row);
        }
        row++;
        HiddenInput HI = new HiddenInput("signed_id",String.valueOf(eContract.getID()));
        if(eContract.getStatus().equalsIgnoreCase(eContract.statusSigned))
          T.add(save,2,row);
        else
          T.add(signed,2,row);
        if(print){
          T.add(PB,2,row);
        }
        T.add(close,2,row);

        T.add(HI,1,row);
      }
      else{
        T.add(Edit.formatText(iwrb.getLocalizedString("syspropserror","System property error")),2,row++);
        T.add(Edit.formatText(iwrb.getLocalizedString("no_default_group","No default group")),2,row++);
      }
      Form F = new Form();
      F.add(T);
      return F;
    }
    catch (SQLException ex) {
      return new Text("");
    }
  }

  /**
   *  Signing contracts included creation of financial account,email and login
   *  returns id of login
   */
   /*
  private void doSignContract(ModuleInfo modinfo){

    int id = Integer.parseInt(modinfo.getParameter("signed_id"));
    String sEmail = modinfo.getParameter("new_email");
    String sSendMail = modinfo.getParameter("send_mail");
    String sFinAccount = modinfo.getParameter("new_fin_account");
    String sPhoneAccount = modinfo.getParameter("new_phone_account");
    String sCreateLogin = modinfo.getParameter("new_login");
    String sUserGroup = modinfo.getParameter("user_group");
    String sSigned =  modinfo.getParameter("sign");
    Contract eContract = null;

      try {
        eContract = new Contract(id);
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }

    if(eContract != null ){
      boolean bSign = sSigned != null ? true:false;
      int iUserId = eContract.getUserId().intValue();
      if(sEmail !=null && sEmail.trim().length() >0){
        UserBusiness.addNewUserEmail(iUserId,sEmail);
      }
      if(sFinAccount != null){
        String prefix = iwrb.getLocalizedString("finance","Finance");
        AccountManager.makeNewFinanceAccount(iUserId,prefix+" - "+String.valueOf(iUserId),"",1);
      }
      if(sPhoneAccount != null){
        String prefix = iwrb.getLocalizedString("phone","Phone");
        AccountManager.makeNewPhoneAccount(iUserId,prefix+" - "+String.valueOf(iUserId),"",1);
      }
      if(sCreateLogin != null && sUserGroup!= null){
        try {
          User eUser = new User(iUserId);
          int gid = Integer.parseInt(sUserGroup);
          GenericGroup gg = new GenericGroup(gid);
          gg.addTo(eUser);
          login = LoginCreator.createLogin(eUser.getName());
          passwd = LoginCreator.createPasswd(8);

          idegaTimestamp today = idegaTimestamp.RightNow();
          int validDays = today.getDaysBetween(today,new idegaTimestamp(eContract.getValidTo()));
          try{
            LoginDBHandler.createLogin(iUserId,login,passwd);
            //LoginDBHandler.createLogin(iUserId,login,passwd,new Boolean(true),today,validDays,new Boolean(false),new Boolean(true),new Boolean(false),"");
            print = true;
          }
          catch(Exception ex){
            ex.printStackTrace();
            login = null;
            passwd = null;
            print = false;
          }
          if(bSign){
            eContract.setStatusSigned();
            eContract.update();
          }
        }
        catch (SQLException ex) {
          ex.printStackTrace();
        }
      }

      if(sSendMail != null){
        List lEmails = UserBusiness.listOfUserEmails(iUserId);
        if(lEmails != null){
          String address = ((Email)lEmails.get(0)).getEmailAddress();
          //SendMail mailer = new SendMail();
        }
      }
    }
  }

  */
  private void doSignContract(ModuleInfo modinfo){

    int id = Integer.parseInt(modinfo.getParameter("signed_id"));
    String sEmail = modinfo.getParameter("new_email");
    String sSendMail = modinfo.getParameter("send_mail");
    String sFinAccount = modinfo.getParameter("new_fin_account");
    String sPhoneAccount = modinfo.getParameter("new_phone_account");
    String sCreateLogin = modinfo.getParameter("new_login");
    String sUserGroup = modinfo.getParameter("user_group");
    String sSigned =  modinfo.getParameter("sign");
    int iGroupId = sUserGroup != null ? Integer.parseInt(sUserGroup):-1;
    boolean sendMail =  sSendMail != null ? true:false;
    sendMail = true;
    boolean newAccount =  sFinAccount != null ? true:false;
    boolean newPhoneAccount =   sPhoneAccount != null ? true:false;
    boolean createLogin =   sCreateLogin != null ? true:false;
    ContractBusiness.signContract(id,iGroupId ,1,sEmail,sendMail,
      newAccount ,newPhoneAccount ,createLogin ,iwrb,login,passwd );
    if(login !=null && passwd !=null)
      print = true;
    else
      print = false;
  }

  private void doAddEmail( int iUserId ,ModuleInfo modinfo){
    String sEmail = modinfo.getParameter("new_email");
    UserBusiness.addNewUserEmail(iUserId,sEmail);
  }

  private ModuleObject getApartmentTable(Apartment A){
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

  public void main(ModuleInfo modinfo){
    try{
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
      isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    control(modinfo);
  }
}