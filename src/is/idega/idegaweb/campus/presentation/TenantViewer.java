package is.idega.idegaweb.campus.presentation;



import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;

import java.sql.SQLException;
import java.util.List;

import com.idega.block.application.data.Applicant;
import com.idega.block.login.presentation.LoginEditor;
import com.idega.block.login.presentation.LoginEditorWindow;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.contact.data.Email;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.TextInput;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

 */



public class TenantViewer extends PresentationObjectContainer {

  private IWResourceBundle iwrb;

  private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";

  private User eUser = null;

  private Applicant eApplicant = null;

  private int iUserId = -1;



  public TenantViewer() {

  }



  public TenantViewer(int iUserId)  {

    try {

      eUser = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(iUserId);

    }

    catch (SQLException ex) {

      eUser = null;

    }

  }



  private void control(IWContext iwc){

    if(eUser == null)

      eUser = LoginBusinessBean.getUser(iwc);

    eApplicant = ContractFinder.findApplicant(eUser);

    if(eApplicant!= null)

      add(getTableInfo(eUser,eApplicant));

    else

      add("is not a tenant");

  }



  private PresentationObject getTableInfo(User user,Applicant applicant){

    List lEmail = UserBusiness.listOfUserEmails(user.getID());

    String sEmail = "",sLogin = "";

    if(lEmail != null)

      sEmail = ((Email)lEmail.get(0)).getEmailAddress();

    String sPhone = applicant.getResidencePhone();

    String sMobile = applicant.getMobilePhone();

    LoginTable LT = LoginDBHandler.getUserLogin(user.getID());

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

  public void main(IWContext iwc){

    iwrb = getResourceBundle(iwc);

    control(iwc);

  }

}

