package is.idegaweb.campus.allocation;

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
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import com.idega.core.data.Email;
import com.idega.core.user.business.UserBusiness;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.data.Account;
import com.idega.block.login.business.LoginCreator;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.user.data.User;
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

public class ContractSigner extends ModuleObjectContainer{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private String redColor = "#942829",TextFontColor = "#000000";
  private String blueColor = "#27324B",lightBlue ="#ECEEF0",WhiteColor = "#FFFFFF";
  private boolean isAdmin;
  protected int fontSize = 2;
  protected boolean fontBold = false;
  protected String styleAttribute = "font-size: 8pt";
  private String login = null;
  private String passwd = null;
  private boolean print = false;

  /*
    Blár litur í topp # 27324B
    Hvítur litur fyrir neðan það # FFFFFF
    Ljósblár litur í töflu # ECEEF0
    Auka litur örlítið dekkri (í lagi að nota líka) # CBCFD3
  */

  public ContractSigner() {
  }

  protected void control(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    if(isAdmin){

      if(modinfo.getParameter("sign")!=null || modinfo.getParameter("save")!=null){
        doSignContract(modinfo);
      }
      add(getSignatureTable(modinfo));
    }
    else
      add(formatText(iwrb.getLocalizedString("access_denied","Access denied")));
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
      idegaTimestamp from = new idegaTimestamp(eContract.getValidFrom());
      idegaTimestamp to = new idegaTimestamp(eContract.getValidTo());
      Applicant eApplicant = new Applicant(eContract.getApplicantId().intValue());
      List lEmails = UserBusiness.listOfUserEmails(eContract.getUserId().intValue());
      List lAccounts = AccountManager.listOfAccounts(eContract.getUserId().intValue());
      LoginTable loginTable = LoginDBHandler.findUserLogin(eContract.getUserId().intValue());
      Table T = new Table();

      SubmitButton save = new SubmitButton("save",iwrb.getLocalizedString("save","Save"));
      SubmitButton signed = new SubmitButton("sign",iwrb.getLocalizedString("signed","Signed"));
      CloseButton close = new CloseButton(iwrb.getLocalizedString("close","Close"));
      PrintButton PB = new PrintButton(iwrb.getLocalizedString("print","Print"));
      TextInput email = new TextInput("new_email");
      CheckBox accountCheck = new CheckBox("new_account");
      accountCheck.setChecked(true);
      CheckBox loginCheck = new CheckBox("new_login");
      loginCheck.setChecked(true);

      T.add(boldText(iwrb.getLocalizedString("name","Name")+" : "),1,2);
      T.add(eApplicant.getFullName(),2,2);
      T.add(boldText(iwrb.getLocalizedString("ssn","SocialNumber")+" : "),1,3);
      T.add(eApplicant.getSSN(),2,3);
      T.add(boldText(iwrb.getLocalizedString("apartment","Apartment")+" : "),1,4);
      T.add(formatText(getApartmentString(new Apartment(eContract.getApartmentId().intValue()))),2,4);
      T.add(boldText(iwrb.getLocalizedString("contractdate","Contract date")+" :"),1,5);
      T.add(formatText(from.getLocaleDate(modinfo)+" "+to.getLocaleDate(modinfo)),2,5);
      T.add(boldText(iwrb.getLocalizedString("email","Email")+" : "),1,6);
      if(lEmails !=null){
        T.add(formatText( ((Email)lEmails.get(0)).getEmailAddress()),2,6);
      }
      else{
        T.add(email,2,6);
      }
      if(lAccounts == null){
        T.add(accountCheck,2,8);
        T.add(boldText(iwrb.getLocalizedString("new_account","New finance account")),2,8);
      }
      else{
        int len = lAccounts.size();
        for (int i = 0; i < len; i++) {
          T.add(boldText(iwrb.getLocalizedString("fin_account","Account")+" : "),1,8);
          T.add(formatText( ((Account)lAccounts.get(i)).getName() +" "),2,8);

        }
      }
      if(loginTable != null ){
        T.add(boldText(iwrb.getLocalizedString("login","Login")+" : "),1,9);
        T.add(formatText(loginTable.getUserLogin()),2,9);
        T.add(boldText(iwrb.getLocalizedString("passwd","Passwd")+" : "),1,10);
        if(passwd != null)
          T.add(formatText(passwd),2,10);


      }
      else{
        T.add(loginCheck,2,9);
        T.add(boldText(iwrb.getLocalizedString("new_login","New login")),2,9);
      }

      HiddenInput HI = new HiddenInput("signed_id",String.valueOf(eContract.getID()));
      if(eContract.getStatus().equalsIgnoreCase(eContract.statusSigned))
        T.add(save,2,12);
      else
        T.add(signed,2,12);
      if(print){
        T.add(PB,2,12);
      }
      T.add(close,2,12);

      T.add(HI,1,7);
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
  private void doSignContract(ModuleInfo modinfo){

    int id = Integer.parseInt(modinfo.getParameter("signed_id"));
    String sEmail = modinfo.getParameter("new_email");
    String sSendMail = modinfo.getParameter("send_mail");
    String sAccount = modinfo.getParameter("new_account");
    String sCreateLogin = modinfo.getParameter("new_login");
    Contract eContract = null;

      try {
        eContract = new Contract(id);
        if(modinfo.getParameter("sign")!=null){
        eContract.setStatusSigned();
        eContract.update();
        }
      }
      catch (SQLException ex) {

      }

    if(eContract != null){
      int iUserId = eContract.getUserId().intValue();
      if(sEmail !=null && sEmail.trim().length() >0){
        UserBusiness.addNewUserEmail(iUserId,sEmail);
      }
      if(sAccount != null){
        AccountManager.makeNewAccount(iUserId,String.valueOf(iUserId),"",1);
      }
      if(sCreateLogin != null){
        try {
          User eUser = new User(iUserId);
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
        }
        catch (SQLException ex) {
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

  private void doAddEmail( int iUserId ,ModuleInfo modinfo){
    String sEmail = modinfo.getParameter("new_email");
    UserBusiness.addNewUserEmail(iUserId,sEmail);
  }

  private ModuleObject getApartmentTable(Apartment A){
    Table T = new Table();
    Floor F = BuildingCacher.getFloor(A.getFloorId());
    Building B = BuildingCacher.getBuilding(F.getBuildingId());
    Complex C = BuildingCacher.getComplex(B.getComplexId());
    T.add(formatText(A.getName()),1,1);
    T.add(formatText(F.getName()),2,1);
    T.add(formatText(B.getName()),3,1);
    T.add(formatText(C.getName()),4,1);
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

   public Text headerText(String text){
    Text T = new Text(text);
    T.setBold();
    T.setFontColor(this.WhiteColor);
    T.setFontSize(1);
    return T;
  }
  public Text formatText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      if(this.fontBold)
      T.setBold();
      T.setFontColor(this.TextFontColor);
      T.setFontSize(this.fontSize);
    }
    return T;
  }
  public Text formatText(int i){
    return formatText(String.valueOf(i));
  }

  public Text boldText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      T.setBold();
      T.setFontColor(this.TextFontColor);
      T.setFontSize(this.fontSize);
    }
    return T;
  }
  public Text boldText(int i){
    return boldText(String.valueOf(i));
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