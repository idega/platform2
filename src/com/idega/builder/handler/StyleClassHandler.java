/*
 * $Id: StyleHandler.java,v 1.3 2002/04/06 19:07:39 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.builder.handler;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.idegaweb.IWStyleManager;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class StyleClassHandler implements ICPropertyHandler {
  /**
   *
   */
  public StyleClassHandler() {
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
    IWStyleManager manager = IWStyleManager.getInstance();
    List list = manager.getStyleList();
    Collections.sort(list);
    
    DropdownMenu chooser = new DropdownMenu(name);
    chooser.setStyleAttribute("font-size: 8pt; border: 1 solid #000000");
    chooser.addMenuElement("","Select:");
	  
	  Iterator iter = list.iterator();
    while (iter.hasNext()) {
    	String style = (String) iter.next();
    	if (style.indexOf(":") == -1) {
			chooser.addMenuElement(style);
		}
    }
    chooser.setSelectedElement(value);
    return(chooser);
  }

  /**
   *
   */
  public void onUpdate(String values[], IWContext iwc) {
  }
}
