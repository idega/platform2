/*
*Copyright 2000-2002 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.block.login.presentation;

import is.idega.idegaweb.golf.access.LoginTable;
import is.idega.idegaweb.golf.block.login.business.GolfLoginBusiness;
import is.idega.idegaweb.golf.entity.Member;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.SendMail;

/**
 * Title: is.idega.idegaweb.golf.login.presentation.LoginEmailer
 * Description:
 * Copyright:    Simple class to email a user his/hers username/password
 * Description:
 * Copyright:    Copyright (c) 2000-2002 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */
public class GolfLoginEmailer extends Block{


private final static String ACTION_PARAMETER="lo_em_action";
private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf.block.login";

protected IWResourceBundle iwrb;
protected IWBundle iwb;

  public GolfLoginEmailer() {
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void init(IWContext modinfo){

  }

  public void main(IWContext modinfo)throws Exception{
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    Form form = new Form();
    Table tafla = new Table(3,2);
    tafla.setWidth(400);
    tafla.mergeCells(1,1,3,1);

    TextInput email = new TextInput(ACTION_PARAMETER);
    Text bold = new Text();
    bold.setBold();


    if(modinfo.getParameter(ACTION_PARAMETER)!=null){
      String theEmail = modinfo.getParameter(ACTION_PARAMETER);
      email.setContent(theEmail);

      Member member = GolfLoginBusiness.getMemberByEmail(theEmail);
      if( member!=null ){
        LoginTable login = GolfLoginBusiness.getLoginForMember(member);
        if(login!=null){
          SendMail.send("golf@idega.is",theEmail,"","","mail.idega.is",
            iwrb.getLocalizedString("email.login.emailer.emailsubject","Lykilorð fyrir golf.is"),
            iwrb.getLocalizedString("email.login.emailer.username","Notandanafn: ")+
            login.getUserLogin()+"\r\n"+
            iwrb.getLocalizedString("email.login.emailer.password","Lykilorð: ")+
            login.getUserPassword() );
            bold.setText(iwrb.getLocalizedString("email.login.emailer.sent","Notandanafn/lykilorð hefur verið sent!"));
        }
        else{
          bold.setText(iwrb.getLocalizedString("email.login.emailer.nologin","Ekkert notandanafn/lykilorð fannst með þetta email. Vinsamlegast hafðu samband við klúbbinn þinn til að búa til nýtt notandanafn/lykilorð"));
        }

      }
      else{
        bold.setText(iwrb.getLocalizedString("email.login.emailer.nomember","Enginn notandi fannst með þetta email. Vinsamlegast hafðu samband við klúbbinn þinn til að fá lykilorðið þitt."));
        tafla.add(new BackButton(),1,2);
      }

    }
    else{
      bold.setText(iwrb.getLocalizedString("email.login.emailer.text","Ef þú ert búin/nn að gleyma lykilorði/notandanafni þá sláðu inn emailið þitt hér að neðan til að fá það sent."));
      tafla.add(iwrb.getLocalizedString("email","email : "),1,2);
      tafla.add(email,2,2);
      tafla.add(new SubmitButton(),3,2);
    }

    tafla.add(bold,1,1);
    tafla.add(Text.BREAK,1,1);
    tafla.add(Text.BREAK,1,1);

    form.add(tafla);
    add(form);
  }

}
