package is.idegaweb.campus.tenant;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.core.data.Email;
import com.idega.core.user.data.User;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.login.presentation.LoginEditorWindow;
import com.idega.block.login.business.LoginBusiness;
import com.idega.block.login.presentation.LoginEditor;
import com.idega.block.application.business.ApplicationFinder;
import com.idega.block.application.data.Applicant;
import is.idegaweb.campus.allocation.ContractFinder;
import java.sql.SQLException;
import java.util.List;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class TenantViewer extends ModuleObjectContainer {
  private IWResourceBundle iwrb;
  private final static String IW_BUNDLE_IDENTIFIER = "is.idegaweb.campus.tenant";
  private User eUser = null;
  private Applicant eApplicant = null;
  private int iUserId = -1;

  public TenantViewer() {
  }

  public TenantViewer(int iUserId)  {
    try {
      eUser = new User(iUserId);
    }
    catch (SQLException ex) {
      eUser = null;
    }
  }

  private void control(ModuleInfo modinfo){
    if(eUser == null)
      eUser = LoginBusiness.getUser(modinfo);
    eApplicant = ContractFinder.findApplicant(eUser);
    if(eApplicant!= null)
      add(getTableInfo(eUser,eApplicant));
    else
      add("is not a tenant");
  }

  private ModuleObject getTableInfo(User user,Applicant applicant){
    List lEmail = UserBusiness.listOfUserEmails(user.getID());
    String sEmail = "",sLogin = "";
    if(lEmail != null)
      sEmail = ((Email)lEmail.get(0)).getEmailAddress();
    String sPhone = applicant.getResidencePhone();
    String sMobile = applicant.getMobilePhone();
    LoginTable LT = LoginDBHandler.findUserLogin(user.getID());
    if(LT!= null)
      sLogin = LT.getUserLogin();
    Link loginLink = new Link(sLogin);
    loginLink.addParameter(LoginEditor.prmUserId,user.getID());
    loginLink.setWindowToOpen(LoginEditorWindow.class);
    sPhone = sPhone==null?"":sPhone;
    sMobile = sMobile==null?"":sMobile;

    TextInput tiEmail = new TextInput("usr_email",sEmail);
    TextInput tiMobile = new TextInput("usr_mobile",sMobile);
    TextInput tiPhone = new TextInput("usr_phone",sPhone);
    Table Frame = new Table();
    Table T = new Table();
    T.add(iwrb.getLocalizedString("name","Name"),1,1);
    T.add(iwrb.getLocalizedString("ssn","Socialnumber"),1,2);
    T.add(iwrb.getLocalizedString("phone","Phone"),1,3);
    T.add(iwrb.getLocalizedString("mobile","Mobile"),1,4);
    T.add(iwrb.getLocalizedString("email","Email"),1,5);
    T.add(iwrb.getLocalizedString("login","Login"),1,6);

    T.add(user.getName(),3,1);
    T.add(applicant.getSSN(),3,2);
    T.add(tiPhone,3,3);
    T.add(tiMobile,3,4);
    T.add(tiEmail,3,5);
    T.add(loginLink,3,6);
    Frame.add(T,1,1);
   /* IFrame ifr = new IFrame("peer",LoginEditor.class,Page.class);
    ifr.setWidth(200);
    ifr.setHeight(300);
    //Frame.add(ifr,2,1);
  */
    return Frame;
  }
   private Link getLoginLink(String s){

    Link L = new Link();
    return L;
  }


  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
  public void main(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    control(modinfo);
  }
}