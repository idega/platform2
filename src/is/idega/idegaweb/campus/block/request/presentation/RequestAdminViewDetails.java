/*
 * $Id: RequestAdminViewDetails.java,v 1.8 2004/06/04 17:32:48 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.presentation;


import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.request.business.RequestFinder;
import is.idega.idegaweb.campus.block.request.data.Request;
import is.idega.idegaweb.campus.block.request.data.RequestHome;
import is.idega.idegaweb.campus.presentation.CampusWindow;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.FinderException;

import com.idega.block.application.data.Applicant;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Floor;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.util.Edit;


/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestAdminViewDetails extends CampusWindow {
  
  protected final static String REQUESTADMIN_SEND = "requestadmin_send";
  protected final static String REQUEST_TYPE = "request_type";
  public final static String REQUEST_STREET = "request_street";
  public final static String REQUEST_APRT = "request_aprt";
  public final static String REQUEST_NAME = "request_name";
  public final static String REQUEST_TEL = "request_tel";
  public final static String REQUEST_EMAIL = "request_email";
  protected final static String REQUESTADMIN_TABLE_TITLE = "requestadmin_table_title";

  protected final static String REQUEST_DATE_OF_CRASH = "request_date_of_crash";
  protected final static String REQUEST_COMMENT = "request_comment";
  protected final static String REQUEST_TIME = "request_time";
  protected final static String REQUEST_DAYTIME = "request_daytime";
  protected final static String REQUEST_SPECIAL_TIME = "request_special_time";
  protected final static String REQUEST_STATUS = "request_status";

  protected final static String REQUEST_NO_COMMENT = "request_no_comment";
  protected final static String REQUEST_NO_DATE_OF_CRASH = "request_no_date_of_crash";
  protected final static String REQUEST_NO_SPECIAL_TIME = "request_no_special_time";

  protected final static String REQUEST_REPAIR = "R";
  protected final static String REQUEST_COMPUTER = "C";

  private boolean _isAdmin;
  private boolean _isLoggedOn;

  /**
   *
   */
  public RequestAdminViewDetails() {
    setWidth(650);
    setHeight(450);
    setResizable(true);
  }

  /**
   *
   */
  public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  }

  /**
   *
   */
  protected void control(IWContext iwc) {
   
    if (_isAdmin || _isLoggedOn){

      if (iwc.isParameterSet(REQUESTADMIN_SEND)) {
        boolean check = doSendRequest(iwc);
        if (check) {
          setParentToReload();
          close();
        }
        else
        ; //Do some error checking
      }

      addMainForm(iwc);
    }
    else
      add(getText(localize("access_denied","Access denied")));
  }

  /**
   *
   */
  protected boolean doSendRequest(IWContext iwc) {
    String status = iwc.getParameter(REQUEST_STATUS);
    String id = iwc.getParameter("request_id");

    System.out.println("id = " + id);
    System.out.println("status = " + status);
    if (id != null) {
      try {
        Request request = ((RequestHome)IDOLookup.getHome(Request.class)).findByPrimaryKey(new Integer(id));
        request.setStatus(status);
        request.store();
      }
      catch(RemoteException e) {
        e.printStackTrace();
        return false;
      }
      catch(FinderException e) {
        e.printStackTrace();

        return false;
      }
    }
    return true;
  }

  /**
   *
   */
  protected void addMainForm(IWContext iwc) {
    Form form = new Form();
    add(form);

    Request request = null;
    String street = null;
    String aprt = null;
    String name = null;
    String telephone = null;
    String email = null;
    String type = null;
    String id = iwc.getParameter("request_id");
    if (id != null) {
      try {
        request = ((RequestHome)IDOLookup.getHome(Request.class)).findByPrimaryKey(new Integer(id));
        type = request.getRequestType();
      }
      catch(RemoteException e) {
        e.printStackTrace();
      }
      catch(FinderException e) {
        e.printStackTrace();
      }
    }

    if (request != null) {
      Contract contract = null;
      Apartment apartment = null;
      Floor floor = null;
      Building building = null;
      Applicant applicant = null;
      try {
        contract = ContractFinder.findByUser(request.getUserId());
      }
      catch(Exception e) {
        contract = null;
      }
      if (contract != null) {
        apartment = BuildingCacher.getApartment(contract.getApartmentId().intValue());
        floor = BuildingCacher.getFloor(apartment.getFloorId());
        building = BuildingCacher.getBuilding(floor.getBuildingId());
        applicant = ContractFinder.getApplicant(contract);
      }

      CampusApplication campusApplication = null;
      try {
        campusApplication = CampusApplicationFinder.getApplicantInfo(applicant).getCampusApplication();
      }
      catch(Exception e) {
        campusApplication = null;
      }

      if (building != null)
        street = building.getName();
      if (apartment != null)
        aprt = apartment.getName();
      if (applicant != null) {
        name = applicant.getFullName();
        telephone = applicant.getResidencePhone();
      }
      if (campusApplication != null)
        email = campusApplication.getEmail();
    }

    DataTable data = new DataTable();
    data.setWidth("100%");
    data.addTitle(localize(REQUESTADMIN_TABLE_TITLE,"Skoða beiðni"));
    data.addButton(new SubmitButton(REQUESTADMIN_SEND,"Uppfæra beiðni"));
    form.add(data);

    int row = 1;
    data.add(Edit.formatText(localize(REQUEST_STREET,"Götuheiti")),1,row);
    data.add(Edit.formatText(street),2,row);
    row++;
    data.add(Edit.formatText(localize(REQUEST_APRT,"Herb./íbúð")),1,row);
    data.add(Edit.formatText(aprt),2,row);
    row++;
    data.add(Edit.formatText(localize(REQUEST_NAME,"Nafn")),1,row);
    data.add(Edit.formatText(name),2,row);
    row++;
    data.add(Edit.formatText(localize(REQUEST_TEL,"Símanúmer")),1,row);
    data.add(Edit.formatText(telephone),2,row);
    row++;
    data.add(Edit.formatText(localize(REQUEST_EMAIL,"email")),1,row);
    data.add(Edit.formatText(email),2,row);
    row++;

    if (type.equals(REQUEST_REPAIR))
      addRepair(data,row,request);
    else if (type.equals(REQUEST_COMPUTER))
      addComputer(data,row,request);

    form.add(new HiddenInput("request_id",id));
  }

  /**
   *
   */
  protected void addRepair(DataTable data, int row, Request request) {
    data.add(Edit.formatText(localize(REQUEST_DATE_OF_CRASH,"Dagsetning bilunar")),1,row);
    Timestamp dateFailure = null;
    String comment = null;
    String special = null;
    String requestStatus = null;
    try {
      dateFailure = request.getDateFailure();
      comment = request.getDescription();
      special = request.getSpecialTime();
      requestStatus = request.getStatus();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    data.add(Edit.formatText(dateFailure.toString()),2,row);
    row++;
    data.add(Edit.formatText(localize(REQUEST_COMMENT,"Athugasemdir")),1,row);
    data.add(Edit.formatText(comment),2,row);
    row++;
    RadioButton b1 = new RadioButton(REQUEST_TIME,REQUEST_DAYTIME);
    b1.setDisabled(true);
    if (special == null || special.equals(""))
      b1.setSelected();
    data.add(b1 ,1,row);
    data.add(Edit.formatText(localize(REQUEST_DAYTIME,"Viðgerð má fara fram á dagvinnutíma, án þess að nokkur sé heima.Þriðjudagar eru almennir viðgerðardagar.")),2,row);
    row++;
    RadioButton b2 = new RadioButton(REQUEST_TIME,REQUEST_DAYTIME);
    b2.setDisabled(true);
    if (special != null && !special.equals(""))
      b2.setSelected();
    data.add(b2,1,row);
    data.add(Edit.formatText(localize(REQUEST_SPECIAL_TIME,"Ég óska eftir sérstakri tímasetningu og að viðgerð verði framkvæmd: ")),2,row);
    if (special != null)
      data.add(Edit.formatText(special),2,row);
    row++;
    DropdownMenu status = new DropdownMenu(REQUEST_STATUS);

    status.addMenuElement(RequestFinder.REQUEST_STATUS_SENT,localize("REQUEST_STATUS_S","S"));
    status.addMenuElement(RequestFinder.REQUEST_STATUS_RECEIVED,localize("REQUEST_STATUS_R","R"));
    status.addMenuElement(RequestFinder.REQUEST_STATUS_IN_PROGRESS,localize("REQUEST_STATUS_P","P"));
    status.addMenuElement(RequestFinder.REQUEST_STATUS_DONE,localize("REQUEST_STATUS_D","D"));
    status.addMenuElement(RequestFinder.REQUEST_STATUS_DENIED,localize("REQUEST_STATUS_X","X"));

    Edit.setStyle(status);
    if (requestStatus != null)
      status.setSelectedElement(requestStatus);
    data.add(Edit.formatText(localize(REQUEST_STATUS,"Staða")),1,row);
    data.add(status,2,row);
    row++;
  }

  /**
   *
   */
  protected void addComputer(DataTable data, int row, Request request) {
    data.add(Edit.formatText(localize(REQUEST_DATE_OF_CRASH,"Dagsetning bilunar")),1,row);
    Timestamp dateFailure = null;
    String comment = null;
    String special = null;
    String requestStatus = null;
    try {
      dateFailure = request.getDateFailure();
      comment = request.getDescription();
      special = request.getSpecialTime();
      requestStatus = request.getStatus();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    data.add(Edit.formatText(dateFailure.toString()),2,row);
    row++;
    data.add(Edit.formatText(localize(REQUEST_COMMENT,"Athugasemdir")),1,row);
    data.add(Edit.formatText(comment),2,row);
    row++;
    RadioButton b2 = new RadioButton(REQUEST_TIME,REQUEST_DAYTIME);
    b2.setDisabled(true);
    if (special != null && !special.equals(""))
      b2.setSelected();
    data.add(b2,1,row);
    data.add(Edit.formatText(localize(REQUEST_SPECIAL_TIME,"Ég óska eftir sérstakri tímasetningu og að viðgerð verði framkvæmd: ")),2,row);
    if (special != null)
      data.add(Edit.formatText(special),2,row);
    row++;
    DropdownMenu status = new DropdownMenu(REQUEST_STATUS);

    status.addMenuElement(RequestFinder.REQUEST_STATUS_SENT,localize("REQUEST_STATUS_S","S"));
    status.addMenuElement(RequestFinder.REQUEST_STATUS_RECEIVED,localize("REQUEST_STATUS_R","R"));
    status.addMenuElement(RequestFinder.REQUEST_STATUS_IN_PROGRESS,localize("REQUEST_STATUS_P","P"));
    status.addMenuElement(RequestFinder.REQUEST_STATUS_DONE,localize("REQUEST_STATUS_D","D"));
    status.addMenuElement(RequestFinder.REQUEST_STATUS_DENIED,localize("REQUEST_STATUS_X","X"));

    Edit.setStyle(status);
    if (requestStatus != null)
      status.setSelectedElement(requestStatus);
    data.add(Edit.formatText(localize(REQUEST_STATUS,"Staða")),1,row);
    data.add(status,2,row);
    row++;
  }

  /**
   *
   */
  public void main(IWContext iwc) throws Exception {
    _isAdmin = iwc.hasEditPermission(this);
    _isLoggedOn = LoginBusinessBean.isLoggedOn(iwc);
    control(iwc);
  }
}
