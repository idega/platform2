/*
 * $Id: CalendarViewHandler.java,v 1.6 2003/04/03 07:21:44 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.calendar.business;

import java.util.List;

import com.idega.builder.handler.PropertyHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class CalendarViewHandler implements PropertyHandler {
  /**
   *
   */
  public CalendarViewHandler() {
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
    menu.addMenuElement(CalendarBusiness.DAY,"Day view");
    menu.addMenuElement(CalendarBusiness.MONTH,"Month view");
    menu.addMenuElement(CalendarBusiness.YEAR,"Year view");
		menu.addMenuElement(CalendarBusiness.AHEAD_VIEW,"Ahead view");
    menu.setSelectedElement(value);
    return(menu);
  }

  /**
   *
   */
  public void onUpdate(String values[], IWContext iwc) {
  }
}
