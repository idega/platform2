/*
 * $Id: NewsLayoutHandler.java,v 1.6 2004/06/28 14:07:44 thomas Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.news.business;

import java.util.List;

import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;

/**
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class NewsLayoutHandler implements ICPropertyHandler {
  public static final int SINGLE_FILE_LAYOUT = 1;
  public static final int NEWS_SITE_LAYOUT = 2;
  public static final int NEWS_PAPER_LAYOUT = 3;
  public static final int SINGLE_LINE_LAYOUT = 4;
  public static final int COLLECTION_LAYOUT = 5;

  /**
   *
   */
  public NewsLayoutHandler() {
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
    menu.addMenuElement(SINGLE_FILE_LAYOUT ,"SINGLEFILE");
    menu.addMenuElement(NEWS_SITE_LAYOUT,"NEWSSITE");
    menu.addMenuElement(NEWS_PAPER_LAYOUT ,"NEWSPAPER");
    menu.addMenuElement(SINGLE_LINE_LAYOUT,"SINGLELINE");
    menu.addMenuElement(COLLECTION_LAYOUT,"COLLECTION");
    menu.setSelectedElement(value);
    return(menu);
  }

  /**
   *
   */
  public void onUpdate(String values[], IWContext iwc) {
  }
}
