package com.idega.block.building.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.ui.Window;
import com.idega.presentation.IWContext;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ApartmentTypeWindow extends Window {

  public ApartmentTypeWindow() {
    setHeight(515);
    setWidth(420);
    //setResizable(true);
  }

  public void main(IWContext iwc) throws Exception{
    int id = Integer.parseInt(iwc.getParameter(ApartmentTypeViewer.PARAMETER_STRING));
    ApartmentTypeViewer BE = new ApartmentTypeViewer(id);
    add(BE);
    setTitle("Apartment Viewer");
    //addTitle("Building Editor");
  }
}
