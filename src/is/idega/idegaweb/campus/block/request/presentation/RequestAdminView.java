/*
 * $Id: RequestAdminView.java,v 1.6 2002/04/15 16:10:09 palli Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.block.request.presentation;

import com.idega.block.building.business.BuildingCacher;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.RadioGroup;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import com.idega.util.idegaTimestamp;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.Floor;
import com.idega.block.building.data.Building;
import com.idega.block.application.data.Applicant;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.request.business.RequestFinder;
import is.idega.idegaweb.campus.block.request.business.RequestHolder;
import is.idega.idegaweb.campus.presentation.Edit;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.business.ContractFinder;
import java.util.List;

import is.idega.idegaweb.campus.block.request.data.Request;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class RequestAdminView extends Block {
  private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";
  private static final String NAME_KEY = "cam_request_admin_view_block";
  private static final String DEFAULT_VALUE = "Requests";
  private static final String CAM_REQ_VIEW_SELECTED = "req_admin_filter";

  private IWResourceBundle _iwrb = null;

  /**
   *
   */
  public RequestAdminView() {
  }

  /**
   *
   */
  public void main(IWContext iwc) {
    _iwrb = getResourceBundle(iwc);
    String selected = iwc.getParameter(CAM_REQ_VIEW_SELECTED);

    System.out.println("adding test to bundle " + _iwrb.getResourcesURL());
    _iwrb.setLocalizedString("Test","test");
    java.util.Enumeration e = _iwrb.getKeys();
    while (e.hasMoreElements())
      System.out.println("key = " + (String)e.nextElement());


    add(getRequests(selected));
  }

  /**
   *
   */
  public String getLocalizedNameKey() {
    return(NAME_KEY);
  }

  /**
   *
   */
  public String getLocalizedNameValue() {
    return(DEFAULT_VALUE);
  }

  /**
   *
   */
  public String getBundleIdentifier() {
    return(IW_BUNDLE_IDENTIFIER);
  }

  private Form getRequests(String selected) {
    Form f = new Form();
    Table t = new Table(1,2);
    t.setWidth("100%");
    DataTable table = new DataTable();
    table.setWidth("100%");
    table.setTitlesHorizontal(true);

    table.addTitle(_iwrb.getLocalizedString("REQUEST_HEADER","Requests"));
    table.add(Edit.formatText(_iwrb.getLocalizedString("REQUEST_TYPE","Request"),true),1,1);
    table.add(Edit.formatText(_iwrb.getLocalizedString("REQUEST_SENT","Sent"),true),2,1);
    table.add(Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS","Status"),true),3,1);

    int row = 2;


    RadioGroup grp = new RadioGroup(CAM_REQ_VIEW_SELECTED);
    grp.addRadioButton(RequestFinder.REQUEST_STATUS_SENT,Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_S")));
    grp.addRadioButton(RequestFinder.REQUEST_STATUS_RECEIVED,Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_R")));
    grp.addRadioButton(RequestFinder.REQUEST_STATUS_IN_PROGRESS,Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_P")));
    grp.addRadioButton(RequestFinder.REQUEST_STATUS_DONE,Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_D")));
    grp.addRadioButton(RequestFinder.REQUEST_STATUS_DENIED,Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_X")));
    grp.setVertical(false);
    grp.keepStatusOnAction();

    if (selected == null) {
      selected = RequestFinder.REQUEST_STATUS_SENT;
      grp.setSelected(selected);
    }

    List requests = RequestFinder.getAllRequestsByType(selected);
    Request request = null;
    RequestHolder holder = null;

    if ( requests != null ) {
      for ( int a = 0; a < requests.size(); a++ ) {
        holder = (RequestHolder) requests.get(a);
        request = holder.getRequest();
        String type = null;
        try {
          type = request.getRequestType();
        }
        catch(java.rmi.RemoteException e) {}
        String status = null;
        try {
          status = request.getStatus();
        }
        catch(java.rmi.RemoteException e) {}
        String linkText = _iwrb.getLocalizedString("REQUEST_TYPE_" + type,"Almenn viðgerð");
        Link details = new Link(linkText);
        Edit.setStyle(details);
        details.setWindowToOpen(RequestAdminViewDetails.class);
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
          details.addParameter(RequestAdminViewDetails.REQUEST_STREET,building.getName());
        if (apartment != null)
          details.addParameter(RequestAdminViewDetails.REQUEST_APRT,apartment.getName());
        if (applicant != null) {
          details.addParameter(RequestAdminViewDetails.REQUEST_NAME,applicant.getFullName());
          details.addParameter(RequestAdminViewDetails.REQUEST_TEL,applicant.getResidencePhone());
        }
        if (campusApplication != null)
          details.addParameter(RequestAdminViewDetails.REQUEST_EMAIL,campusApplication.getEmail());

//        details.addParameter("id",request.getpr);

        table.add(details,1,row);
        try {
          table.add(Edit.formatText(new idegaTimestamp(request.getDateSent()).getISLDate(".",true)),2,row);
        }
        catch(java.rmi.RemoteException e) {
          table.add(null,2,row);
        }
        table.add(Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_" + status,"Innsend")),3,row);
        row++;
      }
    }

    t.add(grp,1,1);
    t.add(table,1,2);
    f.add(t);
    grp.setToSubmit();
    return(f);
  }
}
