/*
 * $Id: ReferenceNumberInfo.java,v 1.3 2002/01/08 15:26:43 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.application.presentation;

import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.data.WaitingList;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import is.idega.idegaweb.campus.block.application.business.CampusReferenceNumberInfoHelper;
import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Complex;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.text.Text;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Table;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.HiddenInput;
import java.util.Vector;
import java.util.Date;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.TreeSet;
import java.text.DateFormat;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ReferenceNumberInfo extends PresentationObjectContainer {
  public static final String SESSION_REFERENCE_NUMBER = "session_ref_num";
  private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.application";
  private IWResourceBundle _iwrb = null;

  /**
   *
   */
  public ReferenceNumberInfo() {
  }

  /**
   *
   */
  protected void control(IWContext iwc) {
    String which = (String)iwc.getSessionAttribute("DUMMY_LOGIN");
    if (which == null) {
    System.out.println("Handling reference number lookup");
    String ref = ReferenceNumberHandler.getReferenceNumber(iwc);

    int aid = 0;
    try {
      aid = Integer.parseInt(ref);
    }
    catch(java.lang.NumberFormatException e) {
      aid = 0;
    }

    CampusApplicationHolder holder = CampusApplicationFinder.getApplicationInfo(aid);

    String update = iwc.getParameter("updatePhoneEmail");
    String confirm = iwc.getParameter("confirmWL");

    if (update != null) {
      String phone = iwc.getParameter("phone");
      String email = iwc.getParameter("email");

      CampusReferenceNumberInfoHelper.updatePhoneAndEmail(holder,phone,email);
    }
    else if (confirm != null) {
      String confirm1 = iwc.getParameter("confirm1");
      String confirm2 = iwc.getParameter("confirm2");
      String confirm3 = iwc.getParameter("confirm3");

      String wl1 = iwc.getParameter("wl1");
      String wl2 = iwc.getParameter("wl2");
      String wl3 = iwc.getParameter("wl3");

      Vector v = (Vector)holder.getWaitingList();

      if (confirm1 == null) {
        if (wl1 != null) {
          CampusReferenceNumberInfoHelper.stayOnWaitingList(Integer.parseInt(wl1),false);
        }
      }
      else {
        if (wl1 != null) {
          CampusReferenceNumberInfoHelper.stayOnWaitingList(Integer.parseInt(wl1),true);
        }
      }

      if (confirm2 == null) {
        if (wl2 != null) {
          CampusReferenceNumberInfoHelper.stayOnWaitingList(Integer.parseInt(wl2),false);
        }
      }
      else {
        if (wl2 != null) {
          CampusReferenceNumberInfoHelper.stayOnWaitingList(Integer.parseInt(wl2),true);
        }
      }

      if (confirm3 == null) {
        if (wl3 != null) {
          CampusReferenceNumberInfoHelper.stayOnWaitingList(Integer.parseInt(wl3),false);
        }
      }
      else {
        if (wl3 != null) {
          CampusReferenceNumberInfoHelper.stayOnWaitingList(Integer.parseInt(wl3),true);
        }
      }

      holder = CampusApplicationFinder.getApplicationInfo(aid);
    }

    Table refTable = new Table();
    refTable.setCellpadding(5);

    int row = 1;

    if (holder == null) {
      refTable.add(new Text(_iwrb.getLocalizedString("appNoSuchApplication","There is no application associated with that reference number")),1,row);
      row++;
    }
    else {
      Form form = new Form();
      Applicant applicant = holder.getApplicant();
      Application app = holder.getApplication();
      CampusApplication camApp = holder.getCampusApplication();

      Text nameText = new Text(applicant.getFullName());
      nameText.setBold();

      refTable.add(nameText,1,row);
      row++;

      DateFormat format = DateFormat.getDateInstance(1,iwc.getCurrentLocale());
      String date = format.format(new Date(app.getSubmitted().getTime()));
      Text dateText = new Text(date);
      dateText.setBold();

      refTable.add(new Text(_iwrb.getLocalizedString("appReceived","Your application was received") + " "),1,row);
      refTable.add(dateText,1,row);
      row++;

      Table updateTable = new Table(3,2);
      updateTable.add(new Text(_iwrb.getLocalizedString("phone","Telephone") + " : "),1,1);
      updateTable.add(new TextInput("phone",applicant.getResidencePhone()),2,1);
      updateTable.add(new Text(_iwrb.getLocalizedString("email","Email") + " : "),1,2);
      updateTable.add(new TextInput("email",camApp.getEmail()),2,2);
      updateTable.add(new SubmitButton("updatePhoneEmail",_iwrb.getLocalizedString("update","Update")),3,2);

      refTable.add(updateTable,1,row);
      row++;

      String status = app.getStatus();

      if (status.equalsIgnoreCase(Application.statusSubmitted))
        status = _iwrb.getLocalizedString("appSubmitted","Waiting to be processed");
      else if (status.equalsIgnoreCase(Application.statusApproved))
        status = _iwrb.getLocalizedString("appApproved","Approved / On waiting list");
      else if (status.equalsIgnoreCase(Application.statusRejected))
        status = _iwrb.getLocalizedString("appRejected","Rejected");
      else
        status = _iwrb.getLocalizedString("appUnknownStatus","Lost in limbo somewhere");

      Contract c = holder.getContract();

      if (c == null) { //Fékk ekki úthlutað, eða ekki búið að úthluta.
        Vector wl = holder.getWaitingList();
        Vector choices = holder.getApplied();

        refTable.add(new Text(_iwrb.getLocalizedString("appStatus","Application status") + ": "),1,row);
        Text statusText = new Text(status);
        statusText.setBold();
        refTable.add(statusText,1,row);
        if (wl == null ) {
          Text star = new Text(" *");
          star.setStyle("required");
          refTable.add(star,1,row);
          row++;
        }
        else {
          row++;
        }

        Table container = new Table();
        Table appliedTable = new Table();
        appliedTable.setBorder(1);
        appliedTable.setColumns(3);
        Text appliedText1 = new Text(_iwrb.getLocalizedString("appAppliedHeader","Applied for"));
        Text appliedText2 = new Text(_iwrb.getLocalizedString("appPositionOnList","# on list"));
        Text appliedText3 = new Text(_iwrb.getLocalizedString("appStayOnList","Stay on list"));
        appliedText1.setBold();
        appliedText2.setBold();
        appliedText3.setBold();
        appliedTable.add(appliedText1,1,1);
        appliedTable.add(appliedText2,2,1);
        appliedTable.add(appliedText3,3,1);

        int pos = 1;
        Iterator it = choices.iterator();
        while(it.hasNext()) {
          Applied applied = (Applied)it.next();
          pos++;
          ApartmentType aType = BuildingCacher.getApartmentType(applied.getApartmentTypeId().intValue());
          Complex complex = BuildingCacher.getComplex(applied.getComplexId().intValue());
          Text appType = new Text(aType.getName()+" ("+complex.getName()+")");

          appliedTable.add(appType,1,pos);

          WaitingList wait = null;
          if (wl != null) {
            Iterator it1 = wl.iterator();
            while (it1.hasNext()) {
              wait = (WaitingList)it1.next();
              if ((wait.getApartmentTypeId().intValue() == aType.getID()) && (wait.getComplexId().intValue() == complex.getID()))
                break;
              else
                wait = null;
            }
          }

          if (wait != null) {
            appliedTable.add(new Text(wait.getOrder().toString()),2,pos);
            appliedTable.setAlignment(2,pos,"center");

            CheckBox check = new CheckBox("confirm" + applied.getOrder().toString(),"true");
            HiddenInput hidden = new HiddenInput("wl" + applied.getOrder().toString(),wait.getIDInteger().toString());
            form.add(hidden);
            check.setChecked(true);
            appliedTable.add(check,3,pos);
            appliedTable.setAlignment(3,pos,"center");
          }
        }

        container.setAlignment(1,1,"right");
        container.setAlignment(1,2,"right");
        container.add(appliedTable,1,1);
        container.add(new SubmitButton("confirmWL",_iwrb.getLocalizedString("confirmWL","Confirm")),1,2);

        refTable.add(container,1,row);
        row++;

        if (wl == null) { //Ekki búið að úthluta
          Text notAllocated = new Text("&nbsp;*&nbsp;"+_iwrb.getLocalizedString("appNotYetAssigned","Apartments have not yet been allocated"));
          notAllocated.setStyle("required");
          refTable.add(notAllocated,1,row);
          row++;
        }
      }
      else {
        refTable.add(new Text(_iwrb.getLocalizedString("appAssigned","You have been assigned to an apartment")),1,row);
        row++;
      }

      form.add(refTable);
      add(form);
    }
    }
    else {
      System.out.println("Handling display of ssn lookup");
      java.util.List li = CampusReferenceNumberInfoHelper.getUserLogin(iwc);
      if (li == null || li.size() != 2) {
        add(new Text("Það er enginn skráður á þessa kennitölu"));
      }
      else {
        String userid = (String)li.get(0);
        String passwd = (String)li.get(1);
        Text idText = new Text("userid : ");
        add(idText);
        add(userid);
        add(Text.getBreak());
        Text passwdText = new Text("passwd : ");
        add(passwdText);
        add(passwd);
        iwc.removeSessionAttribute("DUMMY_LOGIN");
        iwc.removeSessionAttribute("referenceNumber");
      }
    }
  }

  /**
   *
   */
  private void approved(IWContext iwc) {
  }

  /**
   *
   */
  private void rejected(IWContext iwc) {
  }

  /**
   *
   */
  public String getBundleIdentifier() {
    return(IW_BUNDLE_IDENTIFIER);
  }

  /**
   *
   */
  public void main(IWContext iwc) {
    _iwrb = getResourceBundle(iwc);

    control(iwc);
  }
}
