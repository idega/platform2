/*
 * $Id: StaffLayoutHandler.java,v 1.1 2002/01/06 14:09:07 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.staff.business;

import java.util.List;
import com.idega.block.staff.presentation.Staff;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.text.Text;
import com.idega.builder.handler.PropertyHandler;

/**
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class StaffLayoutHandler implements PropertyHandler {
  /**
   *
   */
  public StaffLayoutHandler() {
  }

  /**
   *
   */
  public List getDefaultHandlerTypes() {
    return(null);
  }

  /**
   *
   */
  public PresentationObject getHandlerObject(String name, String value, IWContext iwc) {
    DropdownMenu menu = new DropdownMenu(name);
    menu.addMenuElement("","Select:");
    menu.addMenuElement(Staff.ALL_STAFF,"All staff");
    menu.addMenuElement(Staff.DIVISION_STAFF,"Staff by division");
    menu.setSelectedElement(value);
    return(menu);
  }

  /**
   *
   */
  public void onUpdate(String values[], IWContext iwc) {
  }
}