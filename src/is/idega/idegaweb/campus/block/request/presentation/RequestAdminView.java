/*
 * $Id: RequestAdminView.java,v 1.2 2002/02/15 11:20:47 palli Exp $
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
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import com.idega.util.idegaTimestamp;
import com.idega.util.text.StyleConstants;
import com.idega.util.text.TextStyler;
import is.idega.idegaweb.campus.block.request.business.RequestFinder;
import is.idega.idegaweb.campus.block.request.business.RequestHolder;
import is.idega.idegaweb.campus.presentation.CampusColors;
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

  private TextStyler _styler;
  private Image _image;

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
    _styler = new TextStyler();
    _styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_FAMILY,StyleConstants.FONT_FAMILY_ARIAL);
    _styler.setStyleValue(StyleConstants.ATTRIBUTE_FONT_SIZE,"8pt");

    add(getRequests());
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

  private Table getRequests() {
//    DataTable table = new DataTable();
    Table table = new Table();
    table.setCellspacing(1);
    table.setCellpadding(3);
    table.mergeCells(1,1,3,1);
    table.setWidth("100%");

//    System.out.println("_iwrb = " + _iwrb);


    table.add(formatText(_iwrb.getLocalizedString("requests","Requests"),"#FFFFFF",true),1,1);
    table.add(formatText(_iwrb.getLocalizedString("request","Request")),1,2);
    table.add(formatText(_iwrb.getLocalizedString("sent","Sent")),2,2);
    table.add(formatText(_iwrb.getLocalizedString("status","Status")),3,2);

    int row = 3;

    List requests = RequestFinder.getAllRequests();
    Request request = null;
    RequestHolder holder = null;

    if ( requests != null ) {
      for ( int a = 0; a < requests.size(); a++ ) {
        holder = (RequestHolder) requests.get(a);
        request = holder.getRequest();
        table.add(formatText(new idegaTimestamp(request.getDateSent()).getISLDate(".",true)),2,row);
        table.add(formatText(request.getStatus()),3,row);
        row++;
      }
    }

    table.setHorizontalZebraColored(CampusColors.WHITE,CampusColors.LIGHTGREY);
    table.setColor(1,1,CampusColors.DARKBLUE);
    table.setRowColor(2,CampusColors.DARKGREY);
    table.mergeCells(1,row,3,row);

//    table.add(_image,1,row);
    table.setColor(1,row,CampusColors.DARKRED);

    return table;
  }

  private Text formatText(String text){
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
  }

}