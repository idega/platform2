package is.idega.idegaweb.golf.service;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

import java.sql.SQLException;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.UnionMemberInfo;
import is.idega.idegaweb.golf.entity.LoginTable;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


public class MemberLogin extends Editor{

  Member eMember = null;
  UnionMemberInfo eUMI = null;
  String sUnionId = null;
  String OkImageUrl = "/pics/formtakks/ok.gif";
  String CloseImageUrl = "/pics/formtakks/close.gif";
  String errorMsg = "";

	private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf.login";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

	public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
	}

  public MemberLogin(){
  }

  protected void control(IWContext iwc){
		iwrb = getResourceBundle(iwc);
    String member_id = iwc.getParameter("member_id");
    String union_id = iwc.getParameter("union_id");
    sUnionId = union_id;

    this.makeView();
    this.setTextFontBold(true);
    if(member_id != null && union_id != null){
      try{
        eMember = new Member(Integer.parseInt(member_id));
        String userlogin = null;
        boolean check = false;
        if(iwc.getParameter("ok")!=null || iwc.getParameter("ok.x")!=null ){
          check = doAddTo(iwc,eMember.getID());
        }
        userlogin = this.getUsrLogin(eMember.getID());

        this.addLinks(formatText(iwrb.getLocalizedString("login","Aðgangsorð")));
        this.addMain(doView(eMember,userlogin));
        this.addToHeader(getMsgText(this.errorMsg));
        this.setBorder(2);
      }
      catch(SQLException sql){
        errorMsg = "sql error";}
    }
    else
      errorMsg =(iwrb.getLocalizedString("empty","enginn valinn"));
  }

  private Text getMsgText(String msg){
    Text t = formatText(msg);
    t.setFontColor("#FF0000");
    return t;
  }

  protected PresentationObject makeLinkTable(int menuNr){
    return new Text("");
  }

  private String getUsrLogin(int mbid)throws SQLException{
    String userLogin = getUserLogin(mbid);
    if(userLogin != null)
      return userLogin;
    else
      return iwrb.getLocalizedString("notexist","Ekkert til");
  }

  private boolean doAddTo(IWContext iwc,int iMemberId){
    String sLogin = iwc.getParameter("ml.usrlgn");
    String sPasswd = iwc.getParameter("ml.psw1");
    String sConfirm = iwc.getParameter("ml.psw2");
    boolean register = false;
    if(sLogin != null && sPasswd != null && sConfirm != null){
      if(sLogin.length() > 0  && sPasswd.length() > 0 && sConfirm.length() > 0){
        try{
          register = registerMemberLogin(iMemberId,sLogin,sPasswd,sConfirm);
        }
        catch(SQLException sql){
          sql.printStackTrace();
          register = false;
          errorMsg = "Error in database";
        }
      }
      else
        this.errorMsg = iwrb.getLocalizedString("emptyfields","Vantar í Reiti");
    }
    return register;
  }

  private PresentationObject doView(Member member,String sUserLogin){
    Table T = new Table();
    T.add(formatText(member.getName()),1,1);
    String kt = member.getSocialSecurityNumber()!=null?member.getSocialSecurityNumber():"engin";
    T.add(formatText(kt),1,2);

    TextInput tUsrLgn = new TextInput("ml.usrlgn",sUserLogin);
    this.setStyle(tUsrLgn);
    PasswordInput psw1 = new PasswordInput("ml.psw1");
    this.setStyle(psw1);
    PasswordInput psw2 = new PasswordInput("ml.psw2");
    this.setStyle(psw2);

    T.add(formatText(iwrb.getLocalizedString("login","Notandanafn")),1,3);
    T.add(tUsrLgn,1,4);
    T.add(formatText(iwrb.getLocalizedString("password","Lykilorð")),1,5);
    T.add(psw1,1,6);
    T.add(formatText(iwrb.getLocalizedString("confirm","Staðfesting")),1,7);
    T.add(psw2,1,8);

    SubmitButton ok    = new SubmitButton(new Image(OkImageUrl),"ok");
    CloseButton close    = new CloseButton(new Image(CloseImageUrl));
    T.add(ok,1,9);
    T.add(close,1,9);
    T.add(new HiddenInput("member_id",String.valueOf(member.getID())));
    T.add(new HiddenInput("union_id",String.valueOf(sUnionId)));
    Form myForm = new Form();
    myForm.add(T);

    return myForm;
  }

  public boolean registerMemberLogin(int member_id,String user_login,String user_pass_one,String user_pass_two) throws SQLException {
    boolean returner = false;

    if (user_pass_one.equals(user_pass_two)) {
      LoginTable[] logTable = (LoginTable[]) (new LoginTable()).findAllByColumn("USER_LOGIN",user_login);
      if (logTable.length == 0) {
          LoginTable logT = new LoginTable();
          logT.setMemberId(member_id);
          logT.setUserLogin(user_login);
          logT.setUserPassword(user_pass_one);
          logT.insert();
          returner = true;
          errorMsg = iwrb.getLocalizedString("newmade","Nýtt smíðað");
      }
      else if (logTable.length == 1) {
        if (logTable[0].getMemberId()  == member_id ) {
          logTable[0].setMemberId(member_id);
          logTable[0].setUserLogin(user_login);
          logTable[0].setUserPassword(user_pass_one);
          logTable[0].update();
          returner = true;
          errorMsg = iwrb.getLocalizedString("updated","Uppfært");
        }
        else
          errorMsg = iwrb.getLocalizedString("alreadyexist","Er þegar til");
          returner = false;
      }
      else {
          returner = false;
          errorMsg = iwrb.getLocalizedString("nonexist","Engin til");
      }
    }
    else
      errorMsg = iwrb.getLocalizedString("wrongconfirm","Röng staðfesting");

    return returner;
}

  public  String getUserLogin(int iMemberId)throws SQLException{
    LoginTable[] logins = (LoginTable[])new LoginTable().findAllByColumn("MEMBER_ID",iMemberId);
    if(logins != null && logins.length > 0)
      return logins[0].getUserLogin();
    else
      return null;
  }
}
