/*
 * $Id: CitizenAccountAdmin.java,v 1.1 2002/07/12 10:21:58 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.presentation;

import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.text.Text;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CitizenAccountAdmin extends CommuneBlock {
  private final static int ACTION_VIEW_LIST = 0;
  private final static int ACTION_VIEW_DETAILS = 1;
  private final static int ACTION_SUBMIT = 2;

  private final static int SUBACTION_APPROVE = 10;
  private final static int SUBACTION_REJECT = 11;
  private final static int SUBACTION_NOT_CITIZEN = 12;

  private final static String PARAM_PID = "caa_pid";
  private final static String PARAM_EMAIL = "caa_email";
  private final static String PARAM_PHONE_HOME = "caa_phone_home";
  private final static String PARAM_PHONE_WORK = "caa_phone_work";
  private final static String PARAM_NAME = "caa_adm_name";
  private final static String PARAM_ADDRESS = "caa_adm_address";

  private final static String PARAM_FORM_APPROVE = "caa_adm_approve";
  private final static String PARAM_FORM_REJECT = "caa_adm_reject";
  private final static String PARAM_FORM_NOT_CITIZEN = "caa_adm_not_citizen";
  private final static String PARAM_FORM_DETAILS = "caa_add_details";

  public CitizenAccountAdmin() {
  }

  public void main(IWContext iwc) {
    setResourceBundle(getResourceBundle(iwc));

    try {
      int action = parseAction(iwc);
      switch(action) {
        case ACTION_VIEW_LIST :
          viewList(iwc);
          break;
        case ACTION_VIEW_DETAILS :
          viewDetails(iwc);
          break;
        case ACTION_SUBMIT :
          submit(iwc);
          break;
      }
    }
    catch (Exception e) {
      super.add(new ExceptionWrapper(e,this));
    }
  }

  private int parseAction(IWContext iwc) {
    if (iwc.isParameterSet(PARAM_FORM_APPROVE)) {
      String value = iwc.getParameter(PARAM_FORM_APPROVE);
      if (value != null && !value.equals(""))
        System.out.println("Approving application " + value);
    }

    if (iwc.isParameterSet(PARAM_FORM_REJECT)) {
      String value = iwc.getParameter(PARAM_FORM_REJECT);
      if (value != null && !value.equals(""))
        System.out.println("Rejecting application " + value);
    }

    if (iwc.isParameterSet(PARAM_FORM_NOT_CITIZEN)) {
      String value = iwc.getParameter(PARAM_FORM_NOT_CITIZEN);
      if (value != null && !value.equals(""))
        System.out.println("Not citizen " + value);
    }

    if (iwc.isParameterSet(PARAM_FORM_DETAILS)) {
      String value = iwc.getParameter(PARAM_FORM_DETAILS);
      if (value != null && !value.equals(""))
        System.out.println("Details for " + value);

    }
    return ACTION_VIEW_LIST;
  }

  private void viewList(IWContext iwc) {
    Form form = new Form();
    DataTable data = new DataTable();
    data.setUseTitles(false);
    data.setUseBottom(false);
    data.setUseTop(false);
    data.setWidth("100%");

    int row = 1;
    int col = 1;
    data.add(getHeader(localize(PARAM_NAME,"Name")),col++,row);
    data.add(getHeader(localize(PARAM_PID,"PID")),col++,row);
    data.add(getHeader(localize(PARAM_ADDRESS,"Address")),col,row++);

    for (int i = 1; i < 4; i++) {
      col = 1;
      data.add(getSmallText("Jón Jónsson" + i),col++,row);
      data.add(getSmallText("010101010" + i),col++,row);
      data.add(getSmallText("Laugarvegur " + i),col++,row);

      SubmitButton details = new SubmitButton(localize(PARAM_FORM_DETAILS,"Administrate"),PARAM_FORM_DETAILS,Integer.toString(i));
      details.setAsImageButton(true);
      data.add(details,col,row++);
    }

    form.add(data);
    add(form);
  }

  private void submit(IWContext iwc) {

  }

  private void viewDetails(IWContext iwc) {
/*      SubmitButton approve = new SubmitButton(localize(PARAM_FORM_APPROVE,"Approve"),PARAM_FORM_APPROVE,Integer.toString(i));
      approve.setAsImageButton(true);
      SubmitButton reject = new SubmitButton(localize(PARAM_FORM_REJECT,"Reject"),PARAM_FORM_REJECT,Integer.toString(i));
      reject.setAsImageButton(true);
      SubmitButton notCitizen = new SubmitButton(localize(PARAM_FORM_NOT_CITIZEN,"Not citizen"),PARAM_FORM_NOT_CITIZEN,Integer.toString(i));
      notCitizen.setAsImageButton(true);*/
  }
}