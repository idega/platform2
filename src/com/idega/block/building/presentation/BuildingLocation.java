package com.idega.block.building.presentation;

import java.sql.SQLException;
import com.idega.jmodule.object.interfaceobject.Window;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.block.building.data.Complex;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.interfaceobject.CloseButton;
import com.idega.idegaweb.IWResourceBundle;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class BuildingLocation extends Window {

private static final String IW_RESOURCE_BUNDLE = "com.idega.block.building";
protected IWResourceBundle iwrb_;
private int complexID;

  public BuildingLocation() {
    setWidth(688);
    setHeight(500);
    setAllMargins(0);
    complexID = 0;
    this.setResizable(false);
    this.setScrollbar(false);
  }

  public void main(ModuleInfo modinfo) {
    iwrb_ = this.getResourceBundle(modinfo);
    setTitle(iwrb_.getLocalizedString("map","Map of the area"));

    try {
      complexID = Integer.parseInt(modinfo.getParameter(BuildingViewer.PARAMETER_STRING));
    }
    catch (NumberFormatException e) {
      complexID = 0;
    }

    if ( complexID > 0 ) {
      getMap();
    }
    else {
      close();
    }
  }

  public void getMap() {
    int imageID = -1;

    Image location = iwrb_.getImage("/building/default.jpg");
    Image closeImage = iwrb_.getImage("/room/close.gif");

    try {
      imageID = new Complex(complexID).getImageId();
      if ( imageID != -1 )
        location = new Image(imageID);
    }
    catch (SQLException e) {
      imageID = -1;
    }

    Table table = new Table(1,2);
      table.setAlignment("center");
      table.setCellpadding(0);
      table.setCellspacing(0);
      table.setAlignment(1,2,"center");

    table.add(location,1,1);
    table.add(new CloseButton(closeImage),1,2);
    add(table);

  }

  public String getBundleIdentifier() {
    return(IW_RESOURCE_BUNDLE);
  }
}