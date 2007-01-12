package com.idega.block.email.client.presentation;

import com.idega.block.email.client.business.*;
import com.idega.block.email.business.EmailAccount;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

import java.util.Map;
import java.util.Iterator;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class MailClient extends Block {

  private final static String prmMsgStartIndex = "em_cl_msg_st_in";
  private final static String prmMsgNum = "em_cl_msg_num";
  private final static String prmSessionUser = "em_email_user";
  private final static String prmSessionUserMsgs = "em_email_user_msgs";
  private final static String prmAction = "em_client_action";

  private MailUserBean mailuser;
  private Map messagesMap;
  private EmailAccount mailaccount;

  private IWBundle iwb ;
  private IWResourceBundle iwrb;

  private String EMAILBUNDLE_IDENTIFIER = "com.idega.block.email";

  public MailClient() {
  }

  public String getBundleIdentifier(){
    return this.EMAILBUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc) throws Exception{
    debugParameters(iwc);
    this.iwb = getBundle(iwc);
    this.iwrb = getResourceBundle(iwc);
    // process forms
    processForm(iwc);
    // initialize accounts
    initAccount(iwc);
    Table T = new Table();
    if(this.mailuser!=null){
      if(iwc.isParameterSet(prmMsgNum)) {
		T.add(getMessage(iwc));
	}
	else {
		T.add(getListMessages(iwc));
	}
    }
    else{
      T.add(getLogin(iwc));
    }
    Form f = new Form();
    f.add(T);
    add(f);
  }

  public void initAccount(IWContext iwc) throws Exception{
    if(iwc.getSessionAttribute(prmSessionUser)!=null){
      this.mailuser = ( MailUserBean ) iwc.getSessionAttribute(prmSessionUser);
      this.messagesMap = (Map) iwc.getSessionAttribute(prmSessionUserMsgs);
    }
    else {
      if(this.mailaccount!=null){
        this.mailuser = new MailUserBean();
        this.mailuser.setHostname(this.mailaccount.getHost());
        this.mailuser.setPassword(this.mailaccount.getPassword());
        this.mailuser.setProtocol(this.mailaccount.getProtocolName());
        this.mailuser.setUsername(this.mailaccount.getUser());
      }
      else if("login".equals(iwc.getParameter(prmAction))){
        this.mailuser = new MailUserBean();
        String user = iwc.getParameter("user");
        String pass = iwc.getParameter("pass");
        String host = iwc.getParameter("host");
        String prot = iwc.getParameter("prot");
        this.mailuser.setHostname(host);
        this.mailuser.setPassword(pass);
        this.mailuser.setProtocol(prot);
        this.mailuser.setUsername(user);
      }

      if(this.mailuser!=null){
        this.mailuser.login();
        this.messagesMap = MessageFinder.getMappedMessagesInfo(this.mailuser);
        iwc.setSessionAttribute(prmSessionUser,this.mailuser);
        iwc.setSessionAttribute(prmSessionUserMsgs,this.messagesMap);
      }

    }
  }

  public void processForm(IWContext iwc){

  }

  public PresentationObject getLogin(IWContext iwc){
    Table T = new Table();

    T.add(this.iwrb.getLocalizedString("client.user","User"),1,1);
    T.add(new TextInput("user"),2,1);
    T.add(this.iwrb.getLocalizedString("client.password","Password"),1,2);
    T.add(new TextInput("pass"),2,2);
    T.add(this.iwrb.getLocalizedString("client.host","Host"),1,3);
    T.add(new TextInput("host"),2,3);
    T.add(this.iwrb.getLocalizedString("client.protocol","Protocol"),1,4);
    T.add(new TextInput("prot","pop3"),2,4);


    T.add(new SubmitButton(this.iwrb.getLocalizedImageButton("client.login","Login"),prmAction,"login"),2,5);
    return T;
  }

  public PresentationObject getListMessages(IWContext iwc){
    Table T = new Table();
    int row = 1;
    T.add(this.iwrb.getLocalizedString("client.from","From") ,1,row);
    T.add(this.iwrb.getLocalizedString("client.subject","Subject"),2,row);
    T.add(this.iwrb.getLocalizedString("client.date","date"),3,row);
    row++;
    try {
      if(this.messagesMap!=null && this.messagesMap.size() >0){
        Iterator iter = this.messagesMap.values().iterator();
        MessageInfo m;
        while(iter.hasNext()){
          m = (MessageInfo) iter.next();
          Link l = new Link(m.getSubject());
          l.addParameter(prmMsgNum,m.getNum());
          T.add(m.getFrom(),1,row);
          T.add(l,2,row);
          T.add(m.getDate(),3,row);
          T.add(m.getNum(),4,row);
        row++;
        }
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return T;
  }

  public PresentationObject getMessage(IWContext iwc) throws Exception{
    Table T = new Table(2,5);
    String num = iwc.getParameter(prmMsgNum);
    if(num!=null){
      MessageInfo m = (MessageInfo) this.messagesMap.get(new Integer(num));
      if(m!=null){
        T.add(this.iwrb.getLocalizedString("client.from","From"),1,1);
        T.add(m.getFrom(),2,1);
        T.add(this.iwrb.getLocalizedString("client.date","Date"),1,2);
        T.add(m.getDate(),2,2);
        T.add(this.iwrb.getLocalizedString("client.to","To"),1,3);
        T.add(m.getTo(),2,3);
        T.add(this.iwrb.getLocalizedString("client.subject","Subject"),1,4);
        T.add(m.getSubject(),2,4);
        T.mergeCells(1,5,2,5);
        T.add(m.getBody(),1,5);


      }
    }
    return T;
  }


}
