/*
 * $Id: QAndALayoutHandler.java,v 1.1 2004/08/09 17:20:35 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.questions.business;

import java.util.List;

import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class QAndALayoutHandler implements ICPropertyHandler {
  public static final int DEFAULT_LAYOUT = 1;
  public static final int SINGLE_RANDOM_LAYOUT = 2;
  

  /**
   *
   */
  public QAndALayoutHandler() {
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
    menu.addMenuElement(DEFAULT_LAYOUT ,"DEFAULT");
    menu.addMenuElement(SINGLE_RANDOM_LAYOUT,"SINGLE RANDOM Q & A");
    menu.setSelectedElement(value);
    return(menu);
  }

  /**
   *
   */
  public void onUpdate(String values[], IWContext iwc) {
  }
}
