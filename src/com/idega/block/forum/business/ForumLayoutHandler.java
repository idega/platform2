/*
 * $Id: ForumLayoutHandler.java,v 1.1 2002/05/05 13:11:04 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.forum.business;

import java.util.List;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.text.Text;
import com.idega.builder.handler.PropertyHandler;

/**
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */
public class ForumLayoutHandler implements PropertyHandler {
  /**
   *
   */
  public ForumLayoutHandler() {
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
    menu.addMenuElement(ForumBusiness.FORUM_TOPICS,"Topics view");
    menu.addMenuElement(ForumBusiness.FORUM_COLLECTION,"Thread collection");
    menu.addMenuElement(ForumBusiness.TOPIC_COLLECTION,"Topic collection");
    menu.setSelectedElement(value);
    return(menu);
  }

  /**
   *
   */
  public void onUpdate(String values[], IWContext iwc) {
  }
}
