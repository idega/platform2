/*
 * $Id: RequestAdminView.java,v 1.3 2002/02/21 00:21:48 palli Exp $
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
import is.idega.idegaweb.campus.block.request.business.RequestFinder;
import is.idega.idegaweb.campus.block.request.business.RequestHolder;
import is.idega.idegaweb.campus.presentation.Edit;
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

    add(getRequests(1));
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

  private Form getRequests(int id) {
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


    RadioGroup grp = new RadioGroup("req_admin_filter");
    grp.addRadioButton(Request.REQUEST_STATUS_SENT,Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_S")));
    grp.addRadioButton(Request.REQUEST_STATUS_RECEIVED,Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_R")));
    grp.addRadioButton(Request.REQUEST_STATUS_IN_PROGRESS,Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_P")));
    grp.addRadioButton(Request.REQUEST_STATUS_DONE,Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_D")));
    grp.addRadioButton(Request.REQUEST_STATUS_DENIED,Edit.formatText(_iwrb.getLocalizedString("REQUEST_STATUS_X")));
    grp.setVertical(false);
    grp.keepStatusOnAction();

    String selected = grp.getSelected();
    System.out.println("Selected = " + selected);
    if (selected == null) {
      selected = Request.REQUEST_STATUS_SENT;
      grp.setSelected(selected);
    }

    List requests = RequestFinder.getAllRequestsByType(selected);
    Request request = null;
    RequestHolder holder = null;

    if ( requests != null ) {
      for ( int a = 0; a < requests.size(); a++ ) {
        holder = (RequestHolder) requests.get(a);
        request = holder.getRequest();
        String type = request.getRequestType();
        String status = request.getStatus();
        table.add(Edit.formatText(_iwrb.getLocalizedString("REQUEST_TYPE_" + type,"Almenn viðgerð")),1,row);
        table.add(Edit.formatText(new idegaTimestamp(request.getDateSent()).getISLDate(".",true)),2,row);
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

/*  private Text formatText(String text){
    return formatText(text,"#000000",false);
  }

  private Text formatText(String text,String color){
    return formatText(text,color,false);
  }

  private Text formatText(String text,String color,boolean bold){
    if ( text == null ) text = "";
    Text T =new Text(text);
      _styler.setStyleValue(StyleConstants.ATTRIBUTE_COLOR,color);
      if ( bold )
        _styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_WEIGHT,StyleConstants.FONT_WEIGHT_BOLD);
      else
        _styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_WEIGHT,StyleConstants.FONT_WEIGHT_NORMAL);

      T.setFontStyle(_styler.getStyleString());

    return T;
  }*/

}