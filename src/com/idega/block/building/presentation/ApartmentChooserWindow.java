package com.idega.block.building.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ApartmentChooserWindow extends IWAdminWindow {

  public ApartmentChooserWindow() {
    super();
  }

  public void main(IWContext iwc) throws Exception{
    ApartmentChooser BE = new ApartmentChooser();
    add(BE);
    setTitle("Building Editor");
    addTitle("Building Editor");
  }
}